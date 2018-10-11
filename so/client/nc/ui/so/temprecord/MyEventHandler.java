package nc.ui.so.temprecord;

import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.so.entity.TempRecordVO;

public class MyEventHandler extends CardEventHandler {

	public MyEventHandler(BillCardUI billUI, ICardController control) {
		super(billUI, control);
	}

	@Override
	protected void onBoElse(int intBtn) throws Exception {
		
		if(intBtn==ZQueryBtnVO.btnno){
			this.onBoBZQuery();
		}
	}
	
	@Override
	protected void onBoExport() throws Exception {
		
	}

	protected void onBoBZQuery()throws Exception{
		
		//非空校验
		getBillCardPanelWrapper().getBillCardPanel().dataNotNullValidate();
		BillCardPanel panel=getBillCardPanelWrapper().getBillCardPanel();
		Object pk_org=panel.getHeadItem("pk_org").getValueObject();//库存组织
		String strwhere=" 1=1 ";
		if(!getStrObj(pk_org).equals("")){
			strwhere+=" and pk_org='"+pk_org+"'";
		}
		
		UIRefPane pk_houseid=(UIRefPane)panel.getHeadItem("pk_houseid").getComponent();
		String[] pk_houseids=pk_houseid.getRefPKs();
		if(pk_houseids!=null&&pk_houseids.length>=0){
			
			String str="";
			for(String pk:pk_houseids){
				str+="'"+pk+"',";
			}
			str=str.substring(0, str.lastIndexOf(","));
			
			strwhere+=" and pk_houseid in("+str+") ";
		}
		
		UIRefPane pk_mate=(UIRefPane)panel.getHeadItem("pk_material").getComponent();
		String[] pk_mates=pk_mate.getRefPKs();
		if(pk_mates!=null&&pk_mates.length>=0){
			
			String str="";
			for(String pk:pk_mates){
				str+="'"+pk+"',";
			}
			str=str.substring(0, str.lastIndexOf(","));
			
			strwhere+=" and pk_material in("+str+") ";
		}
		
		UIRefPane pk_cust=(UIRefPane)panel.getHeadItem("pk_customer").getComponent();
		String[] pk_custs=pk_cust.getRefPKs();
		if(pk_custs!=null&&pk_custs.length>=0){
			
			String str="";
			for(String pk:pk_custs){
				str+="'"+pk+"',";
			}
			str=str.substring(0, str.lastIndexOf(","));
			
			strwhere+=" and pk_customer in("+str+")" ;
		}
		
		Object vcode=panel.getHeadItem("vbatchcode").getValueObject();
		if(!getStrObj(vcode).equals("")){
			strwhere+=" and vbatchcode='"+vcode+"'";
		}
		
		Object date1=panel.getHeadItem("ksrq").getValueObject();
		if(!getStrObj(date1).equals("")){
			strwhere+=" and fhdate>='"+date1.toString().substring(0, 10)+"'";
		}
		
		Object date2=panel.getHeadItem("jsrq").getValueObject();
		if(!getStrObj(date2).equals("")){
			strwhere+=" and fhdate<='"+date2.toString().substring(0, 10)+" 23:59:59'";
		}
		
		String sql="select * from tcl_record where "+strwhere+" order by fhdate ";
		IUAPQueryBS iQ=NCLocator.getInstance().lookup(IUAPQueryBS.class);
		List<TempRecordVO> listvos=(List<TempRecordVO>)iQ.executeQuery(sql, new BeanListProcessor(TempRecordVO.class));
		
		BillModel bmodel=panel.getBillModel();
		bmodel.clearBodyData();
		
		if(listvos==null||listvos.size()<=0){
			getBillUI().showHintMessage("未查到任何信息！");
			return ;
		}
		
		//界面重新赋值(表体)
		
		bmodel.setBodyDataVO(listvos.toArray(new TempRecordVO[0]));
		
		bmodel.execLoadFormula();
		
	}
	
	private String getStrObj(Object obj){
		return obj==null?"":obj.toString();
	}
	
}