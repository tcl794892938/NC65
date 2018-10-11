package nc.ui.abspanel.action;

import java.util.List;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;



import nc.funcnode.ui.action.INCAction;
import nc.ui.pubapp.billref.dest.TransferBillViewProcessor;
import nc.ui.uif2.NCAction;


public abstract class AbstractReferenceAction extends NCAction {
  public static final String PREX_ACTION_CODE = "Ref";

  private static final long serialVersionUID = 5414584277167603113L;

  private String btShowName;

  /** 业务流程 */
  private List<String> busitypes;

  private boolean flowBillType = true;

  private boolean isShowNameSetted = false;

  private String sourceBillName;

  private String sourceBillType;

  private TransferBillViewProcessor transferBillViewProcessor;

  private TransferViewProcessor transferViewProcessor;

  private List<String> transtypes;

  public String getBtShowName() {
    return this.btShowName;
  }

  /**
   * @return 业务流程
   */
  public List<String> getBusitypes() {
    return this.busitypes;
  }

  /**
   * 
   * 方法功能描述：获得来源单据类型的名称，将会作为按钮的名字。
   * <p>
   * <b>参数说明</b>
   * 
   * @return 来源单据类型的名称
   *         <p>
   * @since 6.0
   * @author duy
   * @time 2010-1-27 下午04:39:34
   */
  public String getSourceBillName() {
    return this.sourceBillName;
  }

  /**
   * 
   * 方法功能描述：获得来源单据类型。
   * <p>
   * <b>参数说明</b>
   * 
   * @return 来源单据类型，自制单据请返回nc.ui.pu.pub.action.ReferenceAction.MANUAL_CODE
   *         <p>
   * @since 6.0
   * @author duy
   * @time 2010-1-27 下午04:38:44
   */
  public String getSourceBillType() {
    return this.sourceBillType;
  }

  /**
   * 
   * 方法功能描述：获得转单的待转界面处理器。
   * <p>
   * <b>参数说明</b>
   * 
   * @return 转单处理器
   *         <p>
   * @since 6.0
   * @author duy
   * @time 2010-2-3 下午02:35:19
   * @deprecated 根据最新UE规范，拉单后界面数据显示处理请使用{@link #getTransferViewProcessor()}
   */
  @Deprecated
  public TransferBillViewProcessor getTransferBillViewProcessor() {
    return this.transferBillViewProcessor;
  }

  public TransferViewProcessor getTransferViewProcessor() {
    return this.transferViewProcessor;
  }

  public List<String> getTranstypes() {
    return this.transtypes;
  }

  /**
   * 
   * 方法功能描述：来源单据是否可以进行流程配置。
   * <p>
   * <b>参数说明</b>
   * 
   * @return true-可以进行流程配置的单据，false-不参与流程配置
   *         <p>
   * @since 6.0
   * @author duy
   * @time 2010-1-28 上午10:28:49
   */
  public boolean isFlowBillType() {
    return this.flowBillType;
  }

  /**
   * 
   * 方法功能描述：设置按钮显示名称。如果调用此方法，将无法通过setSourceBillType()自动获取单据名称多语。请自行设置多语
   * 
   * <p>
   * <b>参数说明</b>
   * 
   * @param btShowName
   *          按钮显示名称。
   *          <p>
   * 
   */
  public void setBtShowName(String btShowName) {
    this.isShowNameSetted = true;
    this.btShowName = btShowName;
    this.setBtnNameByOrder();
  }

  /**
   * @param busitypes
   *          业务流程
   */
  public void setBusitypes(List<String> busitypes) {
    this.busitypes = busitypes;
  }

  /**
   * 
   * 方法功能描述：来源单据是否可以进行流程配置。
   * <p>
   * <b>参数说明</b>
   * 
   * @param flowBillType
   *          true-可以进行流程配置，false-不能进行流程配置
   *          <p>
   * @since 6.0
   * @author duy
   * @time 2010-1-28 上午10:36:47
   */
  public void setFlowBillType(boolean flowBillType) {
    this.flowBillType = flowBillType;
  }

  public void setSourceBillName(String sourceBillName) {
    this.sourceBillName = sourceBillName;
    this.setBtnNameByOrder();
  }

  public void setSourceBillType(String sourceBillType) {
    this.sourceBillType = sourceBillType;
    if (this.getSourceBillType().equals("MANUAL")) {
      this.setSourceBillName(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
          .getStrByID("pubapp_0", "0pubapp-0295")/* * @res* "自制" */);
    }
    // 增加空判断，防止子类在构造子中设置值后被这里覆盖掉
    if (StringUtils
        .isBlank(ObjectUtils.toString(this.getValue(INCAction.CODE)))) {
      this.setCode(AbstractReferenceAction.PREX_ACTION_CODE + sourceBillType);
    }
    this.setBtnNameByOrder();
  }

  /**
   * 
   * 
   * @param transferBillViewProcessor
   * @deprecated 根据最新UE规范，拉单后界面数据显示处理请使用
   *             {@link #setTransferBillViewProcessor(TransferBillViewProcessor)}
   */
  @Deprecated
  public void setTransferBillViewProcessor(
      TransferBillViewProcessor transferBillViewProcessor) {
    this.transferBillViewProcessor = transferBillViewProcessor;
  }

  public void setTransferViewProcessor(
      TransferViewProcessor transferViewProcessor) {
    this.transferViewProcessor = transferViewProcessor;
  }

  public void setTranstypes(List<String> transtypes) {
    this.transtypes = transtypes;
  }

  /**
   * 
   * 方法功能描述：是否是自制按钮。
   * <p>
   * <b>参数说明</b>
   * 
   * @return true-自制按钮；false-不是自制按钮
   *         <p>
   * @since 6.0
   * @author duy
   * @time 2010-1-28 上午10:43:11
   */
  protected boolean isManual() {
    return false;
  }

  private void setBtnNameByOrder() {
    if (this.isShowNameSetted) {
      this.setBtnName(this.getBtShowName());
      return;
    }
    else if (this.getSourceBillName() != null
        && this.getSourceBillType() != null) {
      if (!nc.vo.ml.NCLangRes4VoTransl
          .getNCLangRes()
          .getCurrLanguage()
          .getCode()
          .equals(
              nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getDefaultLanguage()
                  .getCode())) {
        this.tranMultiLangName();
      }
      this.setBtnName(this.getSourceBillName());
    }
    else if (this.getSourceBillName() == null) {
      this.setBtnName("请设置来源单据名称（默认语种）！");/*-=notranslate=-*/
    }
    else if (this.getSourceBillType() == null) {
      this.setBtnName("请设置来源单据类型编码！");/*-=notranslate=-*/
    }
  }

  private void tranMultiLangName() {
    if (this.sourceBillType.equals("MANUAL")) {
      this.sourceBillName =
          nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("pubapp_0",
              "0pubapp-0295")/* * @res* "自制" */;
    }
    else {
      this.sourceBillName =
          nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("billtype",
              "D" + this.sourceBillType);
    }
  }

}
