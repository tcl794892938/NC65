package nc.ui.qc.c003.maintain.action;

import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pubapp.uif2app.actions.pflow.SaveScriptAction;
import nc.ui.pubapp.uif2app.view.ShowUpableBillForm;
import nc.ui.pubapp.util.CardPanelValueUtils;
import nc.ui.qc.c003.maintain.view.ReportBillForm;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.qc.c003.entity.ReportHeaderVO;
import nc.vo.qc.c003.entity.ReportVO;
import nc.vo.qc.c003.util.ReportCalcTotalNumUtil;
import nc.vo.qc.pub.constant.QCConstant;

/**
 * <p>
 * <b>本类主要完成以下功能：</b>
 * <ul>
 * <li>保存 按钮处理类
 * </ul>
 * <p>
 * <p>
 * 
 * @version 6.0
 * @since 6.0
 * @author hanbin
 * @time 2010-1-12 下午02:15:12
 */
public class SaveUIAction extends SaveScriptAction {

  private static final long serialVersionUID = 6556301184427090719L;

  private void setSimpleFlowForAdd() {
    CardPanelValueUtils utils =
        new CardPanelValueUtils(
            ((ReportBillForm) this.editor).getBillCardPanel());
    ReportVO selvo = (ReportVO) this.model.getSelectedData();
    utils.setHeadTailValue(ReportHeaderVO.NSIMPLEFLOW, selvo.getHVO()
        .getNsimpleflow());
  }

  @Override
  protected Object[] processBefore(Object[] vos) {
    BillCardPanel pl = ((ShowUpableBillForm) this.editor).getBillCardPanel();
    if (pl.getRowCount(QCConstant.REPORT_B) == 0) {
      ExceptionUtils.wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl
          .getNCLangRes().getStrByID("c010003_0", "0c010003-0075")/*
                                                                   * @res
                                                                   * "质检报告表体数据不能为空！"
                                                                   */);
    }
   
    // 计算的统计表体字段合计值(合格数量、不合格主数量、合格率等
    ReportCalcTotalNumUtil calcUtil = new ReportCalcTotalNumUtil();
    
    
    for (Object obj : vos) {
      calcUtil.calc((ReportVO) obj);
    }
    return vos;
  
  }

  
  
  @Override
public void doBeforAction() {
	  BillCardPanel pl = ((ShowUpableBillForm) this.editor).getBillCardPanel();
	  for(int i=0;i<pl.getBillModel().getRowCount();i++){
	    	Object obj=pl.getBillModel().getValueAt(i, "fprocessjudge");
	    	if(obj!=null&&obj.equals("不入库")){
	    		pl.setHeadItem("bneeddeal", new UFBoolean(true));
	    		
	    	}
	    }
	super.doBeforAction();
}

@Override
  protected void processReturnObj(Object[] pretObj) throws Exception {
    super.processReturnObj(pretObj);
    this.setSimpleFlowForAdd();
  }

  @Override
  protected void showSuccessInfo() {
    ShowStatusBarMsgUtil.showStatusBarMsg(nc.vo.ml.NCLangRes4VoTransl
        .getNCLangRes().getStrByID("common", "UCH005")/* @res "保存成功" */, this
        .getModel().getContext());
  }

}
