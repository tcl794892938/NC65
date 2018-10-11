package nc.ui.qc.c003.maintain.action;

import java.awt.event.ActionEvent;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pubapp.uif2app.AppUiState;
import nc.ui.pubapp.uif2app.actions.pflow.UNApproveScriptAction;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.vo.qc.c003.entity.ReportVO;
import nc.vo.qc.pub.enumeration.QCBillStatusEnum;

import org.apache.commons.lang.ArrayUtils;

/**
 * <p>
 * <b>������Ҫ������¹��ܣ�</b>
 * <ul>
 * <li>���� ��ť������Action
 * </ul>
 * <p>
 * <p>
 *
 * @version 6.0
 * @since 6.0
 * @author hanbin
 * @time 2010-1-12 ����02:15:12
 */
public class UnApproveUIAction extends UNApproveScriptAction {

  private static final long serialVersionUID = 5696038440990016421L;

  @Override
  protected boolean isActionEnable() {
    if (this.model.getSelectedData() == null) {
      return false;
    }

    // ������ڱ༭���ݣ����������
    if (this.model.getAppUiState() == AppUiState.EDIT) {
      return false;
    }

    Object[] objs = this.getModel().getSelectedOperaDatas();
    if (this.model.getSelectedData() != null && ArrayUtils.isEmpty(objs)) {
      return this.isOneVOEnable((ReportVO) this.model.getSelectedData());
    }

    if (objs.length > 1) {
      return true;
    }

    // �����������ˣ�������������
    ReportVO vo = (ReportVO) objs[0];
    return this.isOneVOEnable(vo);
  }

  @Override
  protected void showSuccessInfo() {
    ShowStatusBarMsgUtil.showStatusBarMsg(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("c010003_0","0c010003-0017")/*@res "ȡ����˳ɹ�"*/, this.getModel()
        .getContext());
  }

  @Override
public void doAction(ActionEvent e) throws Exception {
	  ReportVO aggvo=(ReportVO) getModel().getSelectedData();
	  String pk=aggvo.getHVO().getPk_reportbill();
	  String sql="select count(*) from ic_finprodin_b where nvl(dr,0)=0 and csourcebillhid='"+pk+"'";
	  IUAPQueryBS iQ=NCLocator.getInstance().lookup(IUAPQueryBS.class);
	  int count= (Integer) iQ.executeQuery(sql, new ColumnProcessor());
	  if(count>0){
		  MessageDialog.showHintDlg(getModel().getContext().getEntranceUI(), "��ʾ", "����Ʒ����д������ε��ݣ�����ȡ��������");
		  return;
	  }
	
	super.doAction(e);
}

private boolean isOneVOEnable(ReportVO vo) {
    if (QCBillStatusEnum.FREE.value().equals(vo.getHVO().getFbillstatus())) {
      // ����̬״̬������ȡ������
      return false;
    }
    return true;
  }
}