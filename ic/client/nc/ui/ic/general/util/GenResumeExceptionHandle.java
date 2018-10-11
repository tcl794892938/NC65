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
 * <b>出入库单可恢复异常处理器：</b>
 * 
 * @version v60
 * @since v60
 * @author yangb
 * @time 2010-5-10 上午12:24:06
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
//                  "4008001_0", "04008001-0054")/* @res "警告" */,
//              e.getMessage()
//                  + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
//                      "4008001_0", "04008001-0055")/* @res "是否删除预留记录。" */);
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
                "4008001_0", "04008001-0054") @res "警告" ,
            e.getMessage()
                + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
                    "4008001_0", "04008001-0055") @res "是否删除预留记录。" );*/
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
                  "4008001_0", "04008001-0056")/* @res ".\n权限校验不通过,保存失败! " */);
      GenResumeExceptionHandle.this.getAccreditLoginDialog().clearPassWord();
      if (GenResumeExceptionHandle.this.getAccreditLoginDialog().showModal() == nc.ui.pub.beans.UIDialog.ID_OK) {
        String sUserID =
            GenResumeExceptionHandle.this.getAccreditLoginDialog().getUserID();
        if (sUserID == null) {
          ExceptionUtils.wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl
              .getNCLangRes().getStrByID("4008001_0", "04008001-0057")/*
                                                                       * @res
                                                                       * "权限校验不通过,保存失败. "
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
              "04008001-0054")/* @res "警告" */,
          e.getMessage()
              + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
                  "4008001_0", "04008001-0058")/* @res "是否继续？" */) == nc.ui.pub.beans.UIDialog.ID_YES) {
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
              "04008001-0054")/* @res "警告" */,
          e.getMessage()
//  在后台错误信息中已经拼接了是否继续，考虑到异常信息被其他各模块适配，不太适合改动，因此在这里进行改动
//              + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
//                  "4008001_0", "04008001-0058")/* @res "是否继续？" */
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
   * 采购容差异常处理
   * 
   * @since 6.0
   * @version 2011-5-27 下午03:43:32
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
              "04008001-0054")/* @res "警告" */, e.getMessage()) == nc.ui.pub.beans.UIDialog.ID_YES) {
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
   * 委外订单容差控制
   * 
   * @since 6.0
   * @version 2011-9-8 上午11:07:07
   * @author wanghna
   */
  class SCOrderAskPriceExceptionHandle extends
      ResumeExceptionHandle<SCOrderAskPriceException> {

    @Override
    protected boolean handleResumeException(SCOrderAskPriceException e) {
      if (MessageDialog.showYesNoDlg(
          GenResumeExceptionHandle.this.model.getContext().getContainUI(),
          nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4008001_0",
              "04008001-0054")/* @res "警告" */, e.getMessage()) == nc.ui.pub.beans.UIDialog.ID_YES) {
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
              "04008001-0054")/* @res "警告" */,
          e.getMessage()
              + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
                  "4008001_0", "04008001-0058")/* @res "是否继续？" */) == nc.ui.pub.beans.UIDialog.ID_YES) {
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
   * 工单成本控制检查异常处理
   * 
   * @since 6.0
   * @version 2012-11-21 上午09:42:15
   * @author guofj
   */
  class WOCostCtrlExceptionHandle extends
      ResumeExceptionHandle<DefaultResumeException> {

    @Override
    protected boolean handleResumeException(DefaultResumeException e) {
      if (!SysInitGroupQuery.isEWMEnabled()) {
        return false;
      }
      // 工单回写接口，如果发现成本超计划，将抛出一个特殊的异常
      if (MessageDialog.showYesNoDlg(
          GenResumeExceptionHandle.this.model.getContext().getContainUI(),
          nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4008001_0",
              "04008001-0054")/* @res "警告" */,
          e.getMessage()
              + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
                  "4008001_0", "04008001-0058")/* @res "是否继续？" */) == nc.ui.pub.beans.UIDialog.ID_YES) {
        // 工单成本超计划，选择是-继续
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
   * 项目检查预算异常处理
   * 
   * @since 6.3
   * @version 2012-11-22 上午08:40:56
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
              "04008001-0054")/* @res "警告" */,
          ((BusinessException) e).getMessage()
              + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
                  "4008001_0", "04008001-0058")/* @res "是否继续？" */) == nc.ui.pub.beans.UIDialog.ID_YES) {
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
   * GenResumeExceptionHandle 的构造子
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
