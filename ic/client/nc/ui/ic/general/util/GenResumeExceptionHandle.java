package nc.ui.ic.general.util;

import nc.itf.pubapp.pub.exception.IResumeException;
import nc.itf.scmpub.reference.uap.group.SysInitGroupQuery;
import nc.ui.ic.pub.model.ICBizEditorModel;
import nc.ui.ic.pub.util.ResumeExceptionHandle;
import nc.ui.ic.pub.view.AccreditLoginDialog;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pubapp.pub.common.context.PFlowContext;
import nc.vo.am.exception.DefaultResumeException;
import nc.vo.ic.general.define.MetaNameConst;
import nc.vo.ic.pub.exp.BudgetControlCheckException;
import nc.vo.ic.pub.exp.ReserveCheckException;
import nc.vo.ic.pub.exp.RightcheckException;
import nc.vo.ic.pub.exp.SafetyStockCheckException;
import nc.vo.ic.pub.pf.ICPFParameter;
import nc.vo.pm.exp.BudgetCheckException;
import nc.vo.pu.m21.exception.AskNumException;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.sc.m61.exception.SCOrderAskPriceException;
import nc.vo.scmpub.exp.AtpNotEnoughException;

/**
 * <p>
 * <b>����ⵥ�ɻָ��쳣��������</b>
 * 
 * @version v60
 * @since v60
 * @author yangb
 * @time 2010-5-10 ����12:24:06
 */
public class GenResumeExceptionHandle extends
    ResumeExceptionHandle<IResumeException> {

  class ReserveCheckExceptionHandle extends
      ResumeExceptionHandle<ReserveCheckException> {
    /**
     *
     */
    @Override
    protected boolean handleResumeException(ReserveCheckException e) {
//      int iresults =
//          MessageDialog.showYesNoCancelDlg(
//              GenResumeExceptionHandle.this.model.getContext().getContainUI(),
//              nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
//                  "4008001_0", "04008001-0054")/* @res "����" */,
//              e.getMessage()
//                  + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
//                      "4008001_0", "04008001-0055")/* @res "�Ƿ�ɾ��Ԥ����¼��" */);
//      if (iresults != nc.ui.pub.beans.UIDialog.ID_CANCEL) {
//        ICPFParameter pfparam =
//            (ICPFParameter) GenResumeExceptionHandle.this.pfcontext
//                .getUserObj();
//        if (pfparam == null) {
//          pfparam = new ICPFParameter();
//        }
//        if (iresults == nc.ui.pub.beans.UIDialog.ID_YES) {
//          pfparam.setReserveProcessType(ICPFParameter.Reserve_Del);
//        }
//        else {
//          pfparam.setReserveProcessType(ICPFParameter.Reserve_NO_Del);
//        }
//        GenResumeExceptionHandle.this.pfcontext.setUserObj(pfparam);
//        return true;
//      }
     /* int iresults =
        MessageDialog.showYesNoDlg(
            GenResumeExceptionHandle.this.model.getContext().getContainUI(),
            nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
                "4008001_0", "04008001-0054") @res "����" ,
            e.getMessage()
                + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
                    "4008001_0", "04008001-0055") @res "�Ƿ�ɾ��Ԥ����¼��" );*/
        ICPFParameter pfparam =
          (ICPFParameter) GenResumeExceptionHandle.this.pfcontext
              .getUserObj();
        if (pfparam == null) {
          pfparam = new ICPFParameter();
        }      
        pfparam.setReserveProcessType(ICPFParameter.Reserve_NO_Del);    
        GenResumeExceptionHandle.this.pfcontext.setUserObj(pfparam);
        return true;
    }
  }

  class RightcheckExceptionHandle extends
      ResumeExceptionHandle<RightcheckException> {
    /**
     *
     */
    @Override
    protected boolean handleResumeException(RightcheckException e) {
      GenResumeExceptionHandle.this.model.getContext().showStatusBarMessage(
          e.getMessage()
              + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
                  "4008001_0", "04008001-0056")/* @res ".\nȨ��У�鲻ͨ��,����ʧ��! " */);
      GenResumeExceptionHandle.this.getAccreditLoginDialog().clearPassWord();
      if (GenResumeExceptionHandle.this.getAccreditLoginDialog().showModal() == nc.ui.pub.beans.UIDialog.ID_OK) {
        String sUserID =
            GenResumeExceptionHandle.this.getAccreditLoginDialog().getUserID();
        if (sUserID == null) {
          ExceptionUtils.wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl
              .getNCLangRes().getStrByID("4008001_0", "04008001-0057")/*
                                                                       * @res
                                                                       * "Ȩ��У�鲻ͨ��,����ʧ��. "
                                                                       */);
          return false;
        }
        ICPFParameter pfparam = new ICPFParameter();
        pfparam.setAccreditUserID(sUserID);
        GenResumeExceptionHandle.this.pfcontext.setUserObj(pfparam);
        return true;
      }
      return false;
    }
  }

  class SafetyStockExceptionHandle extends
      ResumeExceptionHandle<SafetyStockCheckException> {
    /**
     *
     */
    @Override
    protected boolean handleResumeException(SafetyStockCheckException e) {
      if (MessageDialog.showYesNoDlg(
          GenResumeExceptionHandle.this.model.getContext().getContainUI(),
          nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4008001_0",
              "04008001-0054")/* @res "����" */,
          e.getMessage()
              + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
                  "4008001_0", "04008001-0058")/* @res "�Ƿ������" */) == nc.ui.pub.beans.UIDialog.ID_YES) {
        ICPFParameter pfparam =
            (ICPFParameter) GenResumeExceptionHandle.this.pfcontext
                .getUserObj();
        if (pfparam == null) {
          pfparam = new ICPFParameter();
        }
        pfparam.setBSafetyStockCheckFlag(true);
        GenResumeExceptionHandle.this.pfcontext.setUserObj(pfparam);
        return true;
      }
      return false;
    }
  }

  class AtpExceptionHandle extends ResumeExceptionHandle<AtpNotEnoughException> {
    /**
 *
 */
    @Override
    protected boolean handleResumeException(AtpNotEnoughException e) {
      if (MessageDialog.showYesNoDlg(
          GenResumeExceptionHandle.this.model.getContext().getContainUI(),
          nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4008001_0",
              "04008001-0054")/* @res "����" */,
          e.getMessage()
//  �ں�̨������Ϣ���Ѿ�ƴ�����Ƿ���������ǵ��쳣��Ϣ��������ģ�����䣬��̫�ʺϸĶ��������������иĶ�
//              + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
//                  "4008001_0", "04008001-0058")/* @res "�Ƿ������" */
                  ) == nc.ui.pub.beans.UIDialog.ID_YES) {
        ICPFParameter pfparam =
            (ICPFParameter) GenResumeExceptionHandle.this.pfcontext
                .getUserObj();
        if (pfparam == null) {
          pfparam = new ICPFParameter();
        }
        pfparam.setBAtpCheckFlag(true);
        GenResumeExceptionHandle.this.pfcontext.setUserObj(pfparam);
        return true;
      }
      return false;
    }
  }

  /**
   * �ɹ��ݲ��쳣����
   * 
   * @since 6.0
   * @version 2011-5-27 ����03:43:32
   * @author wanghna
   */
  class POAskNumExceptionHandle extends ResumeExceptionHandle<AskNumException> {

    @Override
    protected boolean handleResumeException(AskNumException e) {
      if (!SysInitGroupQuery.isPOEnabled()) {
        return false;
      }
      if (MessageDialog.showYesNoDlg(
          GenResumeExceptionHandle.this.model.getContext().getContainUI(),
          nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4008001_0",
              "04008001-0054")/* @res "����" */, e.getMessage()) == nc.ui.pub.beans.UIDialog.ID_YES) {
        ICPFParameter pfparam =
            (ICPFParameter) GenResumeExceptionHandle.this.pfcontext
                .getUserObj();
        if (pfparam == null) {
          pfparam = new ICPFParameter();
        }
        pfparam.setbToleranceCheckFlag(true);
        GenResumeExceptionHandle.this.pfcontext.setUserObj(pfparam);
        return true;
      }
      return false;
    }
  }

  /**
   * ί�ⶩ���ݲ����
   * 
   * @since 6.0
   * @version 2011-9-8 ����11:07:07
   * @author wanghna
   */
  class SCOrderAskPriceExceptionHandle extends
      ResumeExceptionHandle<SCOrderAskPriceException> {

    @Override
    protected boolean handleResumeException(SCOrderAskPriceException e) {
      if (MessageDialog.showYesNoDlg(
          GenResumeExceptionHandle.this.model.getContext().getContainUI(),
          nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4008001_0",
              "04008001-0054")/* @res "����" */, e.getMessage()) == nc.ui.pub.beans.UIDialog.ID_YES) {
        ICPFParameter pfparam =
            (ICPFParameter) GenResumeExceptionHandle.this.pfcontext
                .getUserObj();
        if (pfparam == null) {
          pfparam = new ICPFParameter();
        }
        pfparam.setbToleranceCheckFlag(true);
        GenResumeExceptionHandle.this.pfcontext.setUserObj(pfparam);
        return true;
      }
      return false;
    }
  }

  class BudgetControlExceptionHandle extends
      ResumeExceptionHandle<BudgetControlCheckException> {

    @Override
    protected boolean handleResumeException(BudgetControlCheckException e) {
      if (MessageDialog.showYesNoDlg(
          GenResumeExceptionHandle.this.model.getContext().getContainUI(),
          nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4008001_0",
              "04008001-0054")/* @res "����" */,
          e.getMessage()
              + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
                  "4008001_0", "04008001-0058")/* @res "�Ƿ������" */) == nc.ui.pub.beans.UIDialog.ID_YES) {
        ICPFParameter pfparam =
            (ICPFParameter) GenResumeExceptionHandle.this.pfcontext
                .getUserObj();
        if (pfparam == null) {
          pfparam = new ICPFParameter();
        }
        pfparam.setbBudgetControlCheckFlag(true);
        GenResumeExceptionHandle.this.pfcontext.setUserObj(pfparam);
        return true;
      }
      return false;
    }
  }

  /**
   * �����ɱ����Ƽ���쳣����
   * 
   * @since 6.0
   * @version 2012-11-21 ����09:42:15
   * @author guofj
   */
  class WOCostCtrlExceptionHandle extends
      ResumeExceptionHandle<DefaultResumeException> {

    @Override
    protected boolean handleResumeException(DefaultResumeException e) {
      if (!SysInitGroupQuery.isEWMEnabled()) {
        return false;
      }
      // ������д�ӿڣ�������ֳɱ����ƻ������׳�һ��������쳣
      if (MessageDialog.showYesNoDlg(
          GenResumeExceptionHandle.this.model.getContext().getContainUI(),
          nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4008001_0",
              "04008001-0054")/* @res "����" */,
          e.getMessage()
              + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
                  "4008001_0", "04008001-0058")/* @res "�Ƿ������" */) == nc.ui.pub.beans.UIDialog.ID_YES) {
        // �����ɱ����ƻ���ѡ����-����
        ICPFParameter pfparam =
            (ICPFParameter) GenResumeExceptionHandle.this.pfcontext
                .getUserObj();
        if (pfparam == null) {
          pfparam = new ICPFParameter();
        }
        pfparam.getBusinessCheckMap().put(MetaNameConst.REPAIRPLANCOSTCHECK,
            Boolean.valueOf(false));
        GenResumeExceptionHandle.this.pfcontext.setUserObj(pfparam);
        return true;
      }
      return false;
    }
  }
  
  /**
   * ��Ŀ���Ԥ���쳣����
   * 
   * @since 6.3
   * @version 2012-11-22 ����08:40:56
   * @author mengjian
   */
  class PIMBudgetCheckExceptionHandle extends
      ResumeExceptionHandle<IResumeException> {

    @Override
    protected boolean handleResumeException(IResumeException e) {
      if (!SysInitGroupQuery.isPIMEnabled()) {
        return false;
      }
      if (MessageDialog.showYesNoDlg(
          GenResumeExceptionHandle.this.model.getContext().getContainUI(),
          nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4008001_0",
              "04008001-0054")/* @res "����" */,
          ((BusinessException) e).getMessage()
              + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
                  "4008001_0", "04008001-0058")/* @res "�Ƿ������" */) == nc.ui.pub.beans.UIDialog.ID_YES) {
        ICPFParameter pfparam =
          (ICPFParameter) GenResumeExceptionHandle.this.pfcontext
              .getUserObj();
        if (pfparam == null) {
          pfparam = new ICPFParameter();
        }
        pfparam.setbBudgetCheckkFlag(true);
        GenResumeExceptionHandle.this.pfcontext.setUserObj(pfparam);
        return true;
      }
      return false;
    }
  }

  protected AccreditLoginDialog m_AccreditLoginDialog;

  protected ICBizEditorModel model;

  protected PFlowContext pfcontext;

  /**
   * GenResumeExceptionHandle �Ĺ�����
   */
  public GenResumeExceptionHandle(ICBizEditorModel model, PFlowContext pfcontext) {
    this.model = model;
    this.pfcontext = pfcontext;
    this.initExceptionHandle();
  }

  /**
   *
   */
  public AccreditLoginDialog getAccreditLoginDialog() {
    if (this.m_AccreditLoginDialog == null) {
      this.m_AccreditLoginDialog =
          new AccreditLoginDialog(this.model.getContext().getLoginContext()
              .getEntranceUI());
    }
    return this.m_AccreditLoginDialog;
  }

  /**
   *
   */
  private void initExceptionHandle() {
    this.addResumeExceptionHandle(RightcheckException.class,
        new RightcheckExceptionHandle());
    this.addResumeExceptionHandle(SafetyStockCheckException.class,
        new SafetyStockExceptionHandle());
    this.addResumeExceptionHandle(ReserveCheckException.class,
        new ReserveCheckExceptionHandle());
    this.addResumeExceptionHandle(AskNumException.class,
        new POAskNumExceptionHandle());
    this.addResumeExceptionHandle(SCOrderAskPriceException.class,
        new SCOrderAskPriceExceptionHandle());
    this.addResumeExceptionHandle(AtpNotEnoughException.class,
        new AtpExceptionHandle());
    this.addResumeExceptionHandle(BudgetControlCheckException.class,
        new BudgetControlExceptionHandle());
    this.addResumeExceptionHandle(DefaultResumeException.class,
        new WOCostCtrlExceptionHandle());
    this.addResumeExceptionHandle(BudgetCheckException.class,
        new PIMBudgetCheckExceptionHandle());
  }

}
