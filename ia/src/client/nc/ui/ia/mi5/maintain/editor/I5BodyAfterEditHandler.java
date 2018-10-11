/**
 * 
 */
package nc.ui.ia.mi5.maintain.editor;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.ia.bill.base.maintain.editor.BaseBodyAfterEditHandler;
import nc.ui.ia.bill.base.maintain.editor.body.CreditMumEditHandler;
import nc.ui.ia.bill.base.maintain.model.BaseBillModel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.bill.BillModel;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.CardBodyAfterEditEvent;
import nc.ui.pubapp.util.CardPanelValueUtils;
import nc.ui.uif2.UIState;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;

/**
 * <b>������</b>
 * <p>
 * �ڴ˴���Ӵ����������Ϣ
 * </p>
 * 
 * @author Ƥ֮��
 * @time 2010-1-28 ����07:12:55
 * @version NC6.0
 */
public class I5BodyAfterEditHandler implements
    IAppEventHandler<CardBodyAfterEditEvent> {

  private BaseBodyAfterEditHandler handler = new BaseBodyAfterEditHandler();

  private BaseBillModel model;

  public BaseBillModel getModel() {
    return this.model;
  }

  @Override
  public void handleAppEvent(CardBodyAfterEditEvent e) {
    if (this.handler.getPricemnynumTriggerPoints().contains(e.getKey())) {
      if (this.getModel().getUiState() == UIState.ADD) {
        CreditMumEditHandler.afterEdit(e);
      }
    }
    
    if(e.getKey().equals("vbatchcode")){//���κ�
    	
    	if(e.getValue()==null){
    		return;
    	}
    	CardPanelValueUtils util = new CardPanelValueUtils(e.getBillCardPanel());
    	Object pk_m=util.getBodyValue(e.getRow(), "cinventoryvid");
    	if(pk_m==null){
    		MessageDialog.showHintDlg(e.getBillCardPanel(), "��ʾ", "����¼�����ϣ�");
    		util.setBodyValue(null, e.getRow(), e.getKey());
    		return ;
    	}
    	
    	//1.У�����κŶ�Ӧ�ĵ����Ƿ����
    	String sql = "select count(*) from scm_batchcode where nvl(dr,0)=0 and vbatchcode='"+ e.getValue() + "' and cmaterialvid='" + pk_m + "'";
		IUAPQueryBS iQ = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		Integer it=0;
		try {
			it = (Integer) iQ.executeQuery(sql, new ColumnProcessor());
		} catch (BusinessException e1) {
		}
		if(it<=0){
			MessageDialog.showHintDlg(e.getBillCardPanel(), "��ʾ", "���κŵ��������ڣ�");
    		util.setBodyValue(null, e.getRow(), e.getKey());
    		return ;
		}
		//2.��ѯ�������
		String pk_org=util.getHeadTailStringValue("pk_org");//�ɱ���
		Object code=e.getValue();
		Object objpk=util.getHeadTailValue("cbillid");//pk
		String sql2="select sum(nnum) from "
				+ "(select b.nnum from ia_i0bill_b b where b.pk_org='"+pk_org+"'  "
				+ "and nvl(b.dr,0)=0 and b.cinventoryvid='"+pk_m+"' and b.vbatchcode='"+code+"' "
				+ "and exists(select 1 from ia_i0bill h where b.cbillid=h.cbillid and nvl(h.dr,0)=0) "
				+ "union all "
				+ "select b.nnum from ia_i2bill_b b where b.pk_org='"+pk_org+"' "
				+ " and nvl(b.dr,0)=0 and b.cinventoryvid='"+pk_m+"' and b.vbatchcode='"+code+"'"
				+ "	and exists(select 1 from ia_i2bill h where b.cbillid=h.cbillid and nvl(h.dr,0)=0) "
				+ "union all "
				+ "select 0-b.nnum from ia_i5bill_b b where b.pk_org='"+pk_org+"'  "
				+ "and nvl(b.dr,0)=0 and b.cinventoryvid='"+pk_m+"' and b.vbatchcode='"+code+"' and b.cbillid<>'"+objpk+"' "
				+ "and exists(select 1 from ia_i5bill h where b.cbillid=h.cbillid and nvl(h.dr,0)=0)  )";
		
		UFDouble jcs=new UFDouble(0);
		try {
			Object njc=iQ.executeQuery(sql2, new ColumnProcessor());
			jcs=njc==null?jcs:new UFDouble(njc.toString());
		} catch (BusinessException e1) {
		}
		/*if(jcs.compareTo(new UFDouble(0))<=0){
			MessageDialog.showHintDlg(e.getBillCardPanel(), "��ʾ", "�������Ϊ"+jcs);
    		util.setBodyValue(null, e.getRow(), e.getKey());
    		return ;
		}*/
		
		//3.У�鱾�γ�����
		BillModel model2=e.getBillCardPanel().getBillModel();
		int row=model2.getRowCount();
		UFDouble cks=new UFDouble(0);
		for(int i=0;i<row;i++){
			Object pk_m2=util.getBodyValue(i, "cinventoryvid");
			Object code2=util.getBodyValue(i, "vbatchcode");
			if(pk_m.equals(pk_m2)&&code.equals(code2)){
				Object nnum=util.getBodyValue(i, "nnum");
				UFDouble ud=nnum==null?new UFDouble(0):new UFDouble(nnum.toString());
				cks=cks.add(ud);
			}
		}
    	if(cks.compareTo(jcs)>0){
    		MessageDialog.showHintDlg(e.getBillCardPanel(), "��ʾ", "�ۼƳ�����ӦС�ڽ����"+jcs);
    		util.setBodyValue(null, e.getRow(), e.getKey());
    		return ;
    	}
    }
    
    if(e.getKey().equals("nnum")){//������
    	
    	if(e.getValue()==null){
    		return;
    	}
    	CardPanelValueUtils util = new CardPanelValueUtils(e.getBillCardPanel());
    	Object pk_m=util.getBodyValue(e.getRow(), "cinventoryvid");
    	if(pk_m==null){
    		MessageDialog.showHintDlg(e.getBillCardPanel(), "��ʾ", "����¼�����ϣ�");
    		util.setBodyValue(null, e.getRow(), e.getKey());
    		return ;
    	}
    	
    	Object code=util.getBodyValue(e.getRow(), "vbatchcode");
    	if(code==null){
    		return ;
    	}
    	
    	IUAPQueryBS iQ = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		//2.��ѯ�������
		String pk_org=util.getHeadTailStringValue("pk_org");//�ɱ���
		Object objpk=util.getHeadTailValue("cbillid");//pk
		String sql2="select sum(nnum) from "
				+ "(select b.nnum from ia_i0bill_b b where b.pk_org='"+pk_org+"'  "
				+ "and nvl(b.dr,0)=0 and b.cinventoryvid='"+pk_m+"' and b.vbatchcode='"+code+"' "
				+ "and exists(select 1 from ia_i0bill h where b.cbillid=h.cbillid and nvl(h.dr,0)=0) "
				+ "union all "
				+ "select b.nnum from ia_i2bill_b b where b.pk_org='"+pk_org+"' "
				+ " and nvl(b.dr,0)=0 and b.cinventoryvid='"+pk_m+"' and b.vbatchcode='"+code+"'"
				+ "	and exists(select 1 from ia_i2bill h where b.cbillid=h.cbillid and nvl(h.dr,0)=0) "
				+ "union all "
				+ "select 0-b.nnum from ia_i5bill_b b where b.pk_org='"+pk_org+"'  "
				+ "and nvl(b.dr,0)=0 and b.cinventoryvid='"+pk_m+"' and b.vbatchcode='"+code+"' and b.cbillid<>'"+objpk+"' "
				+ "and exists(select 1 from ia_i5bill h where b.cbillid=h.cbillid and nvl(h.dr,0)=0)  )";
		
		UFDouble jcs=new UFDouble(0);
		try {
			Object njc=iQ.executeQuery(sql2, new ColumnProcessor());
			jcs=njc==null?jcs:new UFDouble(njc.toString());
		} catch (BusinessException e1) {
		}
		/*if(jcs.compareTo(new UFDouble(0))<=0){
			MessageDialog.showHintDlg(e.getBillCardPanel(), "��ʾ", "�������Ϊ"+jcs);
    		util.setBodyValue(null, e.getRow(), e.getKey());
    		return ;
		}*/
		
		//3.У�鱾�γ�����
		BillModel model2=e.getBillCardPanel().getBillModel();
		int row=model2.getRowCount();
		UFDouble cks=new UFDouble(0);
		for(int i=0;i<row;i++){
			Object pk_m2=util.getBodyValue(i, "cinventoryvid");
			Object code2=util.getBodyValue(i, "vbatchcode");
			if(pk_m.equals(pk_m2)&&code.equals(code2)){
				Object nnum=util.getBodyValue(i, "nnum");
				UFDouble ud=nnum==null?new UFDouble(0):new UFDouble(nnum.toString());
				cks=cks.add(ud);
			}
		}
    	if(cks.compareTo(jcs)>0){
    		MessageDialog.showHintDlg(e.getBillCardPanel(), "��ʾ", "�ۼƳ�����ӦС�ڽ����"+jcs);
    		util.setBodyValue(null, e.getRow(), e.getKey());
    		return ;
    	}
    
    }

    this.handler.handleAppEvent(e);
  }

  public void setModel(BaseBillModel model) {
    this.model = model;
  }

}
