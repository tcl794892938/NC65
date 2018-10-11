package nc.bs.ic.onhand.bp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.bs.ic.onhand.action.SafetyStockCheck;
import nc.bs.ic.onhand.fix.OnhandNumFixBP;
import nc.bs.ic.onhand.pub.ICStateOnhandData;
import nc.bs.ic.pub.env.ICBSContext;
import nc.itf.ic.onhand.IOnhandData;
import nc.itf.ic.onhand.IOnhandSNData;
import nc.itf.scmpub.reference.uap.group.SysInitGroupQuery;
import nc.pubitf.ic.reserve.ReserveInOut;
import nc.vo.ic.general.define.ICBillVO;
import nc.vo.ic.onhand.entity.OnhandDimVO;
import nc.vo.ic.onhand.entity.OnhandNumVO;
import nc.vo.ic.onhand.entity.OnhandSNVO;
import nc.vo.ic.onhand.entity.OnhandSNViewVO;
import nc.vo.ic.onhand.entity.OnhandVO;
import nc.vo.ic.onhand.pub.HashVOUtils;
import nc.vo.ic.onhand.pub.OnhandVOTools;
import nc.vo.ic.pub.check.VOCheckUtil;
import nc.vo.ic.pub.define.ICPubMetaNameConst;
import nc.vo.ic.pub.lang.OnhandRes;
import nc.vo.ic.pub.util.CollectionUtils;
import nc.vo.ic.pub.util.NCBaseTypeUtils;
import nc.vo.ic.pub.util.StringUtil;
import nc.vo.ic.pub.util.ValueCheckUtil;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.log.TimeLog;
import nc.vo.pubapp.pattern.pub.AssertUtils;
import nc.vo.scmpub.res.billtype.ICBillType;

/**
 * <b> 现存量维护BP类 </b>
 * <p>
 * 包括现存量更新等
 * </p>
 * 创建日期:2009-09-24 09:24:28
 * 
 * @author
 * @version NCPrj ??
 */

public class OnhandBO {

  private static final String ICHandSNUpdateVO = "ICHandSNUpdateVO";

  private static final String ICHandUpdateVO = "ICHandUpdateVO";

  private static final String OriginBillVOs = "OriginBillVOs";

  // 库存单据保存后的VO
  private ICBillVO[] curbillvos;

  // 现存量维度与现存量维度pk的map，在现存量更新后保留，供单品现存更新使用
  private Map<String, String> mapdims;

  // 库存单据保存前的原始VO
  private ICBillVO[] originbillvos;

  /**
   * 按照默认方式创建构造子. 创建日期:2009-04-23 15:30:50
   */
  public OnhandBO() {
    super();
  }

  public OnhandSNViewVO[] fetchOnhandSNVO(AggregatedValueObject[] orgbillvos)
      throws BusinessException {
    // 单品现存
    IOnhandSNData sndatasrc =
        OnhandDataFactory.getOnhandSNDataSource(orgbillvos[0]);
    OnhandSNViewVO[] snvos = sndatasrc.getOnhandSNVOs(orgbillvos);
    return snvos;
  }

  public OnhandVO[] fetchOnhandVO(AggregatedValueObject[] orgbillvos)
      throws BusinessException {
    // 现存
    IOnhandData datasrc = OnhandDataFactory.getOnhandDataSource(orgbillvos[0]);
    OnhandVO[] handvos = datasrc.getOnhandVOs(orgbillvos);
    return handvos;
  }

  /**
   * 说明：过滤OnhandSNVO 创建日期:2009-04-23 15:30:50
   */
  public OnhandSNVO[] filterOnhandSNVO(OnhandSNVO[] snvos) {
    if (ValueCheckUtil.isNullORZeroLength(snvos)) {
      return null;
    }
    List<OnhandSNVO> lsn = new ArrayList<OnhandSNVO>();
    for (OnhandSNVO snvo : snvos) {
      if (NCBaseTypeUtils.isNullOrZero(snvo.getNonhandnum())) {
        continue;
      }
      lsn.add(snvo);
    }
    if (lsn.size() <= 0) {
      return null;
    }
    return lsn.toArray(new OnhandSNVO[lsn.size()]);
  }

  /**
   * 调整现存量
   * 
   * @param conds
   */
  public void fixOnhandNum(ConditionVO[] conds) {
    TimeLog.logStart();
    // 加锁
    new OnhandLock().addOnhandPub_Lock();
    TimeLog.info("加锁现存量用时");/*-=notranslate=-*/
    TimeLog.logStart();

    // 调整现存量 a
    new OnhandNumFixBP().fixOnHandNum(conds);// .fixOnHandNumAndSN(conds);
    TimeLog.info("调整现存量数量用时");/*-=notranslate=-*/
  }

  /**
   * 方法功能描述：修改现存量(单据)
   * <p>
   * <b>参数说明</b>
   * 
   * @param orgbillvos
   * @throws BusinessException <p>
   * @since 6.0
   * @author yangb
   * @time 2010-9-2 上午10:57:10
   */
  public void modifyOnhandBegin(AggregatedValueObject[] orgbillvos)
      throws BusinessException {
    if (ValueCheckUtil.isNullORZeroLength(orgbillvos)) {
      return;
    }

    ICBSContext bscontext = new ICBSContext();
    if (orgbillvos[0] instanceof ICBillVO) {
      bscontext.setSession(OnhandBO.OriginBillVOs, orgbillvos);
    }

    OnhandVO[] handvos = this.fetchOnhandVO(orgbillvos);
    bscontext.setSession(OnhandBO.ICHandUpdateVO, handvos);

    OnhandSNViewVO[] snvos = this.fetchOnhandSNVO(orgbillvos);
    bscontext.setSession(OnhandBO.ICHandSNUpdateVO, snvos);
  }

  /**
   * 方法功能描述：修改现存量(单据)
   * <p>
   * <b>参数说明</b>
   * 
   * @param billvos
   * @throws BusinessException <p>
   * @since 6.0
   * @author yangb
   * @time 2010-9-2 上午10:56:53
   */
  public void modifyOnhandEnd(AggregatedValueObject[] billvos)
      throws BusinessException {
    if (ValueCheckUtil.isNullORZeroLength(billvos)) {
      return;
    }
    ICBSContext context = new ICBSContext();
    if (billvos[0] instanceof ICBillVO) {
      this.curbillvos = (ICBillVO[]) billvos;
      this.originbillvos =
          (ICBillVO[]) context.getSession(OnhandBO.OriginBillVOs);
    }
    // 现存
    OnhandVO[] beforehandvos =
        (OnhandVO[]) context.getSession(OnhandBO.ICHandUpdateVO);
    // 单品现存
    OnhandSNViewVO[] beforehandsnvos =
        (OnhandSNViewVO[]) context.getSession(OnhandBO.ICHandSNUpdateVO);
    OnhandVO[] updatehandvos =
        this.modifyOnhandVOProcess(beforehandvos, beforehandsnvos, billvos);

    // 更新现存后，检查安全库存
    new SafetyStockCheck().check(updatehandvos);

    this.clearSessionData();
  }

  private void clearSessionData() {
    ICBSContext context = new ICBSContext();
    context.setSession(OnhandBO.OriginBillVOs, null);
    context.setSession(OnhandBO.ICHandUpdateVO, null);
    context.setSession(OnhandBO.ICHandSNUpdateVO, null);
  }

  /**
   * 方法功能描述：修改现存量
   * <p>
   * <b>参数说明</b>
   * 
   * @param handnumvos
   * @return
   * @throws BusinessException <p>
   * @since 6.0
   * @author yangb
   * @time 2010-9-2 上午10:56:36
   */
  public OnhandVO[] modifyOnhandNum(OnhandNumVO[] handnumvos)
      throws BusinessException {
    if (ValueCheckUtil.isNullORZeroLength(handnumvos)) {
      return null;
    }
    OnhandNumVO[] numvos = handnumvos;
    VOCheckUtil.checkVOsNotNullFields(numvos, new String[] {
      OnhandNumVO.PK_ONHANDDIM
    });
    numvos = OnhandVOTools.filterOnhandVONumZero(numvos);
    if (numvos == null || numvos.length <= 0) {
      return null;
    }
    OnhandVO[] handvos = new OnhandDimBO().getOnhandVO(numvos);
    try {
      // 现存量数据检查
      OnhandDataCheckRule tool = new OnhandDataCheckRule();
      tool.checkDimAllNotNullField(handvos);
      tool.checkNumNotNull(handvos);

    }
    catch (Exception e) {
      throw new BusinessException(OnhandRes.getDimDataErr());
    }
    // 更新
    handvos = this.modifyOnhandBase(handvos);
    return handvos;

  }

  /**
   * 方法功能描述：修改现存量
   * <p>
   * <b>参数说明</b>
   * 
   * @param vos
   * @return
   * @throws BusinessException <p>
   * @since 6.0
   * @author yangb
   * @time 2010-9-2 上午10:52:19
   */
  public OnhandVO[] modifyOnhandNum(OnhandVO[] vos) throws BusinessException {
    if (ValueCheckUtil.isNullORZeroLength(vos)) {
      return null;
    }
    OnhandVO[] handvos = vos;
    // 是否预留出入库
    boolean isreserve = false;
    if (!ValueCheckUtil.isNullORZeroLength(this.curbillvos)) {
      isreserve =
          this.isReserveInOut(this.curbillvos[0].getHead().getBillType());
    }
    else if (!ValueCheckUtil.isNullORZeroLength(this.originbillvos)) {
      isreserve =
          this.isReserveInOut(this.originbillvos[0].getHead().getBillType());
    }

    TimeLog.logStart();
    // 现存量数据基本检查
    OnhandDataCheckRule tool = new OnhandDataCheckRule();
    tool.checkDimNotNullContent(handvos);
    tool.checkNumNotNull(handvos);

    TimeLog.info("检查现存量数据用时");/*-=notranslate=-*/
    TimeLog.logStart();
    // 处理现存量纬度
    new OnhandDimBO().getDimPks(handvos);

    TimeLog.info("处理现存量纬度用时");/*-=notranslate=-*/
    // 处理预留出入库
    if (isreserve) {
      handvos = this.processReserveInOut(handvos);
    }

    TimeLog.logStart();
    // 按现存量纬度合并
    OnhandVO[] updatehandvos = OnhandVOTools.mergeOnhandByDim(handvos);

    TimeLog.info("现存量纬度合并用时");/*-=notranslate=-*/
    // 更新
    handvos = this.modifyOnhandBase(updatehandvos);
    // 备份现存量维度,供单品结存更新使用
    this.backupDimPk(CollectionUtils.combineArrs(handvos, updatehandvos));

    TimeLog.logStart();
    // 现存量检查
    new OnhandCheck(isreserve ? OnhandCheck.OnhandCheckType.UpdateReserveOut
        : OnhandCheck.OnhandCheckType.UpdateOnhandNumCheck)
        .checkOnhandNumMsgRow(updatehandvos, this.curbillvos);

    TimeLog.info("检查现存量数据用时");/*-=notranslate=-*/

    return handvos;
  }

  /**
   * 方法功能描述：修改单品现存量
   * <p>
   * <b>参数说明</b>
   * 
   * @param handsnvos <p>
   * @since 6.0
   * @author yangb
   * @time 2010-9-2 上午10:56:19
   */
  public void modifyOnhandSNNum(OnhandSNViewVO[] handsnvos) {
    if (handsnvos == null || handsnvos.length <= 0) {
      return;
    }
    try {
      // 填充现存量维度
      this.fillOnhandSNDimPk(handsnvos);
      // 获取单品现存VO
      OnhandSNVO[] snvos = OnhandVOTools.getOnhandSNVOs(handsnvos);
      //记录值后面补植 抛异常用
      Map<String,Object> pkmsgMap=new HashMap<String,Object>();
      String field = SysInitGroupQuery.isSNEnabled() ? ICPubMetaNameConst.PK_SERIALCODE : OnhandSNVO.VSNCODE;
      for(OnhandSNVO snvo:snvos){
        if(snvo.getAttributeValue(snvo.getVsncode())!=null){
          String value = (String)snvo.getAttributeValue(field);
          pkmsgMap.put(value,snvo.getAttributeValue(snvo.getVsncode()));
          pkmsgMap.put("snNum", snvo.getAttributeValue("snNum"));
        }
      }
      
      // 按序列号条码合并
      snvos = OnhandVOTools.mergeOnhandSNVO(snvos);
      // 过滤OnhandSNVO
      snvos = this.filterOnhandSNVO(snvos);
      if (ValueCheckUtil.isNullORZeroLength(snvos)) {
        return;
      }
      // 单品现存量数据基本检查
      new OnhandDataCheckRule().onCheckOnhandSN(snvos);
      // 排序
      snvos = OnhandVOTools.sortOnhandSNVOBySNCode(snvos);
      // 更新
      new OnhandSNDMO().updateOnhandSN(snvos);
      OnhandCheck onhandcheck = new OnhandCheck();
      //补植
      if(!pkmsgMap.isEmpty()){
        for(OnhandSNVO snvo:snvos){
          String value = (String)snvo.getAttributeValue(field);
          if(value!=null)
          {
            snvo.setAttributeValue(value, pkmsgMap.get(value));

          }
           snvo.setAttributeValue("snNum",pkmsgMap.get("snNum"));
        }
      }
      
      // 单品序列号校验
      onhandcheck.checkOnhandSNNum(snvos);
      // 单品条码校验，因为要走条码权限，所以和序列号分开校验
      onhandcheck.checkOnhandBarCodeByRight(snvos, handsnvos[0].getPk_org());
    }
    catch (Exception e) {
      ExceptionUtils.wrappException(e);
    }
  }

  public OnhandVO[] modifyOnhandVOProcess(OnhandVO[] beforehandvos,
      OnhandSNViewVO[] beforehandsnvos, AggregatedValueObject[] billvos)
      throws BusinessException {

    OnhandVO[] negbeforevos = OnhandVOTools.negativeOnhandVOs(beforehandvos);
    OnhandVO[] endhandvos = this.fetchOnhandVO(billvos);
    OnhandVO[] updatehandvos =
        this.modifyOnhandNum(CollectionUtils.combineArrs(negbeforevos,
            endhandvos));

    OnhandSNViewVO[] endsnvos = this.fetchOnhandSNVO(billvos);
    OnhandSNViewVO[] negbeforesnvos =
        OnhandVOTools.negativeOnhandSNViewVOs(beforehandsnvos);
    this.modifyOnhandSNNum(CollectionUtils
        .combineArrs(negbeforesnvos, endsnvos));
    return updatehandvos;
  }

  /**
   * 库存状态调整时更新现存量
   * 
   * @param vos
   * @return
   */
  public Map<String, String> modifyStateOnhand(SuperVO[] vos) {

    Map<String, String> hidToPkdim = new HashMap<String, String>();
    try {

      ICStateOnhandData dataSource = new ICStateOnhandData();
      // 获得差异的现存量VO
      OnhandVO[] beforeOnhandVOs = dataSource.getBeforeOnhandData(vos);
      OnhandVO[] afterOnhandVOs = dataSource.getAfterOnhandData(vos);

      beforeOnhandVOs = OnhandVOTools.negativeOnhandVOs(beforeOnhandVOs);
      OnhandVO[] changedOnhandvos =
          CollectionUtils.combineArrs(beforeOnhandVOs, afterOnhandVOs);
      TimeLog.logStart();
      new OnhandDataCheckRule().checkDimNotNullContent(changedOnhandvos);
      TimeLog.info("现存量检查用时");/*-=notranslate=-*/

      TimeLog.logStart();
      new OnhandDimBO().getDimPks(afterOnhandVOs);
      TimeLog.info("现存量维度更新用时");/*-=notranslate=-*/
      hidToPkdim = this.getHidAndPkdim(vos, afterOnhandVOs);

      TimeLog.logStart();
      OnhandVO[] updatehandvos =
          OnhandVOTools.mergeOnhandByDim(changedOnhandvos);
      TimeLog.info("现存量纬度合并用时");/*-=notranslate=-*/
      // 更新
      OnhandVO[] handvos = this.modifyOnhandBase(updatehandvos);
      // 备份现存量维度,供单品结存更新使用
      this.backupDimPk(CollectionUtils.combineArrs(handvos, updatehandvos));
      
      // 现存量检查
      new OnhandCheck(OnhandCheck.OnhandCheckType.UpdateOnhandNumCheck)
          .checkOnhandNum(updatehandvos);
      
      // 更新单品存量 
//      OnhandSNViewVO[] endsnvos = this.fetchOnhandSNVO(billvos); TODO 对比ICStateOnhandSNData
//      ICStateOnhandSNData sndataSource = new ICStateOnhandSNData();
      // 获得差异的现存量VO
      OnhandSNViewVO[] beforeOnhandSNVOs = dataSource.getBeforeOnhandSNData(vos);
      OnhandSNViewVO[] afterOnhandSNVOs = dataSource.getAfterOnhandSNData(vos);
      beforeOnhandSNVOs = OnhandVOTools.negativeOnhandSNViewVOs(beforeOnhandSNVOs);
      OnhandSNViewVO[] changedOnhandsnvos =
          CollectionUtils.combineArrs(beforeOnhandSNVOs, afterOnhandSNVOs);
      this.modifyOnhandSNNum(changedOnhandsnvos);
    }
    catch (BusinessException e) {
      ExceptionUtils.wrappException(e);
    }
    return hidToPkdim;

  }

  /**
   * 方法功能描述：备份现存量维度
   * <p>
   * <b>参数说明</b>
   * 
   * @param handvos <p>
   * @since 6.0
   * @author yangb
   * @time 2010-9-2 上午10:56:04
   */
  private void backupDimPk(OnhandVO[] handvos) {
    if (handvos == null || handvos.length <= 0) {
      return;
    }
    this.mapdims = new HashMap<String, String>();
    for (OnhandVO handvo : handvos) {
      if (handvo.getPk_onhanddim() == null) {
        continue;
      }
      String key = HashVOUtils.getContentKey(handvo);
      String pk_onhanddim = handvo.getPk_onhanddim();
      if (!this.mapdims.containsKey(key)) {
        this.mapdims.put(key, pk_onhanddim);
        continue;
      }
      AssertUtils.assertValue(
          StringUtil.isStringEqual(pk_onhanddim, this.mapdims.get(key)),
          nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4008019_0",
              "04008019-0036")/* @res "现存量更新维度错误！" */);
    }
  }

  /**
   * 方法功能描述：用备份现存量维度填充单品现存维度
   * <p>
   * <b>参数说明</b>
   * 
   * @param handsnvos <p>
   * @since 6.0
   * @author yangb
   * @time 2010-9-2 上午10:55:49
   */
  private void fillOnhandSNDimPk(OnhandSNViewVO[] handsnvos) {
    if (handsnvos == null || handsnvos.length <= 0) {
      return;
    }
    if (this.mapdims == null) {
      ExceptionUtils.wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl
          .getNCLangRes().getStrByID("4008019_0", "04008019-0037")/*
                                                                   * @res
                                                                   * "数据错误：无法填充单品现存维度"
                                                                   */);
    }
    String contentkey = null, dimpk = null;
    for (OnhandSNViewVO handsnvo : handsnvos) {
      contentkey = HashVOUtils.getContentKey(handsnvo);
      dimpk = this.mapdims.get(contentkey);
      if (dimpk == null) {
        ExceptionUtils.wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl
            .getNCLangRes().getStrByID("4008019_0", "04008019-0038")/*
                                                                     * @res
                                                                     * "数据错误：无法填充单品现存维度!!!"
                                                                     */);
      }
      handsnvo.setPk_onhanddim(dimpk);
    }
  }

  private Map<String, String> getHidAndPkdim(SuperVO[] vos,
      OnhandVO[] changedOnhandvos) {
    Map<String, String> hidAndPkDim = new HashMap<String, String>();
    for (int i = 0; i < vos.length; i++) {
      hidAndPkDim.put(vos[i].getPrimaryKey(), changedOnhandvos[i]
          .getOnhandDimVO().getPk_onhanddim());
    }
    return hidAndPkDim;

  }

  /**
   * 方法功能描述：是否预留出入库
   * <p>
   * <b>参数说明</b>
   * 
   * @param billtype
   * @return <p>
   * @since 6.0
   * @author yangb
   * @time 2010-9-2 上午10:57:21
   */
  private boolean isReserveInOut(ICBillType billtype) {//by tcl
    /*if (billtype == ICBillType.MaterialOut || billtype == ICBillType.SaleOut
        || billtype == ICBillType.TransOut
        || billtype == ICBillType.ProductionIn
        || billtype == ICBillType.PurchaseIn || billtype == ICBillType.TransIn) {
      return true;
    }*/
	  if (billtype == ICBillType.MaterialOut
		        || billtype == ICBillType.TransOut
		        || billtype == ICBillType.ProductionIn
		        || billtype == ICBillType.PurchaseIn || billtype == ICBillType.TransIn) {
		      return true;
		}
    return false;
  }

  /**
   * 方法功能描述：修改现存量
   * <p>
   * <b>参数说明</b>
   * 
   * @param vos
   * @return
   * @throws BusinessException <p>
   * @since 6.0
   * @author yangb
   * @time 2010-9-2 上午10:53:19
   */
  private OnhandVO[] modifyOnhandBase(OnhandVO[] vos) {
    if (ValueCheckUtil.isNullORZeroLength(vos)) {
      return null;
    }
    OnhandVO[] handvos = vos;
    TimeLog.logStart();
    // 处理现存量数据
    handvos = this.procOnhandDataByBiz(handvos);

    TimeLog.info("处理现存量数据用时");/*-=notranslate=-*/
    if (handvos == null || handvos.length <= 0) {
      return null;
    }
    TimeLog.logStart();
    // 排序
    OnhandVOTools.sortOnhandVOByDim(handvos);
    TimeLog.info("现存量数据内存排序用时");/*-=notranslate=-*/
    TimeLog.logStart();
    // 加锁
    // 数据库加锁 放入现存量检查更合理
    // new OnhandLock().lockByDimForBill(handvos);
    // new OnhandLock().lockByDim(handvos);
    new OnhandLock().addOnhandPubShared_Lock();

    TimeLog.info("加锁现存量用时");/*-=notranslate=-*/
    TimeLog.logStart();
    // 更新数据
    this.updateOnhandData(handvos);

    TimeLog.info("更新现存量数据用时");/*-=notranslate=-*/
    return handvos;
  }

  /**
   * 方法功能描述：处理预留出入库
   * <p>
   * <b>参数说明</b>
   * 
   * @param handvos
   * @return
   * @throws BusinessException <p>
   * @since 6.0
   * @author yangb
   * @time 2010-9-2 上午10:53:50
   */
  private OnhandVO[] processReserveInOut(OnhandVO[] vos)
      throws BusinessException {
    OnhandVO[] handvos = vos;

    TimeLog.logStart();
    // 处理预留出入库
    ReserveInOut rinout = NCLocator.getInstance().lookup(ReserveInOut.class);
    OnhandDimVO[] handdimvos = OnhandVOTools.getOnhandDimVOs(handvos);
    OnhandNumVO[] numvos =
        rinout.reserveInOut(this.curbillvos, this.originbillvos, handdimvos);
    numvos = OnhandVOTools.filterOnhandVONumZero(numvos);
    if (ValueCheckUtil.isNullORZeroLength(numvos)) {
      return handvos;
    }
    handvos =
        CollectionUtils.combineArrs(handvos,
            new OnhandDimBO().getOnhandVO(numvos));

    TimeLog.info("处理预留出入库用时");/*-=notranslate=-*/
    return handvos;
  }

  /**
   * 方法功能描述：按业务规则处理现存量数据
   * <p>
   * <b>参数说明</b>
   * 
   * @param hanvos
   * @return <p>
   * @since 6.0
   * @author yangb
   * @time 2010-9-2 上午10:55:05
   */
  private OnhandVO[] procOnhandDataByBiz(OnhandVO[] hanvos) {
    if (hanvos == null || hanvos.length <= 0) {
      return hanvos;
    }

    OnhandVO[] nonzeroVOs = OnhandVOTools.filterOnhandVONumZero(hanvos);
    if (nonzeroVOs != null) {
      OnhandVOTools.setTupdatetime(nonzeroVOs);
    }
    return nonzeroVOs;

  }

  /**
   * 方法功能描述：修改现存量记录
   * <p>
   * <b>参数说明</b>
   * 
   * @param handvos
   * @throws BusinessException <p>
   * @since 6.0
   * @author yangb
   * @time 2010-9-2 上午10:55:21
   */
  private void updateOnhandData(OnhandVO[] handvos) {
    if (handvos == null || handvos.length <= 0) {
      return;
    }
    new OnhandDMO().saveOnhandVOs(OnhandVOTools.getOnhandNumVOs(handvos));
  }
}
