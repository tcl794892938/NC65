package nc.ui.ic.m46.billref;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;





import nc.bs.framework.common.NCLocator;
import nc.bs.uif2.IActionCode;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.pf.busiflow.PfButtonClickContext;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.abspanel.action.AbstractReferenceAction;
import nc.ui.am.billref.AppEventConst;
import nc.ui.ic.general.action.GeneralAddAction;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.pubapp.uif2app.funcnode.trantype.TrantypeFuncUtils;
import nc.ui.pubapp.uif2app.view.BillForm;
import nc.ui.uif2.AppEvent;
import nc.ui.uif2.UIState;
import nc.ui.uif2.actions.ActionInitializer;
import nc.ui.uif2.model.AbstractAppModel;
import nc.vo.ic.m46.entity.FinProdInBodyVO;
import nc.vo.ic.m46.entity.FinProdInHeadVO;
import nc.vo.ic.m46.entity.FinProdInVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.AppContext;
import nc.vo.qc.c003.entity.ReportVO;


public class RefAction extends AbstractReferenceAction{

	public RefAction() {
		super();
		this.setCode("refaction");
		this.setBtnName("参照质检报告");
	}
	private BillForm editor;

	private AbstractAppModel model;
	

	public BillForm getEditor() {
		return editor;
	}

	public void setEditor(BillForm editor) {
		this.editor = editor;
	}

	public AbstractAppModel getModel() {
		return model;
	}

	public void setModel(AbstractAppModel model) {
		this.model = model;
		model.addAppEventListener(this);
	}

	@Override
	public void doAction(ActionEvent arg0) throws Exception {
		
		PfUtilClient.childButtonClickedNew(createPfButtonClickContext());
		if (PfUtilClient.isCloseOK()) {
			
			ReportVO[] vos= (ReportVO[])PfUtilClient.getRetOldVos();
			 if(vos==null ||vos.length<=0 ){
				 MessageDialog.showErrorDlg(null, "错误", "请选择一条数据！");
				 return ;
			 }
			
			//单据号生成
			 IUAPQueryBS iQ=NCLocator.getInstance().lookup(IUAPQueryBS.class);
			 FinProdInVO aggvo=new FinProdInVO();
			 FinProdInHeadVO hvo=new FinProdInHeadVO();
			// FinProdInBodyVO[] bvo=new FinProdInBodyVO[vos.length];
			 List<FinProdInBodyVO> list=new ArrayList<FinProdInBodyVO>();
			hvo.setPk_org(vos[0].getHVO().getPk_stockorg());
			hvo.setPk_org_v(vos[0].getHVO().getPk_stockorg_v());
			hvo.setCprocalbodyoid(vos[0].getHVO().getPk_stockorg());
			hvo.setCprocalbodyvid(vos[0].getHVO().getPk_stockorg_v());
			hvo.setDbilldate(AppContext.getInstance().getBusiDate());
			hvo.setPk_group(vos[0].getHVO().getPk_group());
			hvo.setCdptid(vos[0].getHVO().getPk_applydept());
			hvo.setCdptvid(vos[0].getHVO().getPk_applydept_v());
			UFDouble ud=new UFDouble(0);
			for(ReportVO vo:vos){
				if(vo.getBVO()[0].getFprocessjudge()!=1){
					continue;
				}
				FinProdInBodyVO bvo=new FinProdInBodyVO();
				bvo.setCmaterialvid(vo.getHVO().getPk_material());
				bvo.setCmaterialoid(vo.getHVO().getPk_srcmaterial());
				ud=ud.add(vo.getHVO().getNeligiastnum());
				bvo.setCsourcebillhid(vo.getHVO().getPk_reportbill());
				bvo.setCsourcebillbid(vo.getBVO()[0].getPk_reportbill_b());
				bvo.setCsourcetype("C003");
				bvo.setVsourcebillcode(vo.getHVO().getVbillcode());
				bvo.setVnotebody(vos[0].getHVO().getVmemo());
				
				String sql2="select pk_supplier from bd_supplier where nvl(dr,0)=0 and pk_financeorg='"+vos[0].getHVO().getPk_stockorg()+"'";
				Object obj2=iQ.executeQuery(sql2, new ColumnProcessor());
				String sup=obj2==null?"":obj2.toString();
				bvo.setCvendorid(sup);
				bvo.setCunitid(vo.getHVO().getCunitid());
				bvo.setCastunitid(vo.getHVO().getCastunitid());
				bvo.setFproductclass(1);
				bvo.setCproductid(vo.getHVO().getPk_material());
				bvo.setVchangerate(vo.getBVO()[0].getVchangerate());
				//bvo.setVbatchcode("20171128");
				bvo.setPk_batchcode(vo.getBVO()[0].getPk_batchcode());
				bvo.setVbatchcode(vo.getBVO()[0].getVbatchcode());
				bvo.setDbizdate(AppContext.getInstance().getBusiDate());
				
				Object obj3=vo.getHVO().getBneeddeal();
				//UFBoolean ub=obj3==null?new UFBoolean(false):new UFBoolean(obj3.toString());
				 String sql="select sum(nassistnum ) from ic_finprodin_b where nvl(dr,0)=0 and csourcebillhid ='"+vo.getHVO().getPk_reportbill()+"'";
				 Object obj=iQ.executeQuery(sql, new ColumnProcessor());
				 UFDouble sum=obj==null?new UFDouble(0):new UFDouble(obj.toString());
				 
//				 if(!ub.booleanValue()){
//					 if(vo.getHVO().getNeligiastnum().sub(sum).compareTo(new UFDouble(0))>0){
//						 bvo.setNshouldassistnum(vo.getHVO().getNeligiastnum().sub(sum));
//						 bvo.setNshouldnum(bvo.getNshouldassistnum());
//						 bvo.setNassistnum(vo.getHVO().getNeligiastnum().sub(sum)); 
//						 bvo.setNnum(vo.getHVO().getNeligiastnum().sub(sum));
//						 list.add(bvo);
//					 }
//					 
//				 }else{
					 if(vo.getHVO().getNcheckastnum().sub(sum).compareTo(new UFDouble(0))>0){
						 bvo.setNshouldassistnum(vo.getHVO().getNcheckastnum().sub(sum));
						 bvo.setNshouldnum(bvo.getNshouldassistnum());
						 bvo.setNassistnum(vo.getHVO().getNcheckastnum().sub(sum)); 
						 bvo.setNnum(vo.getHVO().getNcheckastnum().sub(sum));
						 list.add(bvo); 
					 }
				 }
			//}
			hvo.setNtotalnum(ud);
			 aggvo.setParentVO(hvo);
			 aggvo.setChildrenVO(list.toArray(new FinProdInBodyVO[0]));
			 
//			 ActionInitializer.initializeAction( new GeneralAddAction(), IActionCode.ADD);
//				model.setOtherUiState(UIState.NOT_EDIT);
//				model.setUiState(UIState.ADD);
//				getModel().fireEvent(new AppEvent(AppEventConst.MODEL_INITIALIZED, this, null));
			 
			 //getModel().setUiState(UIState.ADD);
			//this.editor.getBillCardPanel().setBillValueVO(aggvo);
			 FinProdInVO []aggvos=new FinProdInVO[1];
			 aggvos[0]=aggvo;
			this.getTransferViewProcessor().processBillTransfer(aggvos);
			//this.editor.getBillCardPanel().getBillModel().setValueAt("20171128", 0, "vbatchcode");
		}
		
	}
	
	private PfButtonClickContext createPfButtonClickContext() {
		PfButtonClickContext context = new PfButtonClickContext();
		context.setParent(this.getModel().getContext().getEntranceUI());
		context.setSrcBillType(this.getSourceBillType());
		context.setPk_group(this.getModel().getContext().getPk_group());
		context.setUserId(this.getModel().getContext().getPk_loginUser());
		// 如果该节点是由交易类型发布的，那么这个参数应该传交易类型，否则传单据类型
		String vtrantype = TrantypeFuncUtils.getTrantype(this.getModel()
				.getContext());
		if (StringUtil.isEmptyWithTrim(vtrantype)) {
			context.setCurrBilltype("46");
		} else {
			context.setCurrBilltype(vtrantype);
		}
		context.setUserObj(null);
		context.setSrcBillId(null);
		context.setBusiTypes(this.getBusitypes());
		// 上面的参数在原来调用的方法中都有涉及，只不过封成了一个整结构，下面两个参数是新加的参数
//		// 上游的交易类型集合
		context.setTransTypes(this.getTranstypes());
//		// 标志在交换根据目的交易类型分组时，查找目的交易类型的依据，有三个可设置值：1（根据接口定义）、
//		// 2（根据流程配置）、-1（不根据交易类型分组）
		context.setClassifyMode(PfButtonClickContext.ClassifyByItfdef);
		return context;
	}

}
