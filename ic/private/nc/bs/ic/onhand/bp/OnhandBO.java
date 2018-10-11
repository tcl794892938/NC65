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
 * <b> �ִ���ά��BP�� </b>
 * <p>
 * �����ִ������µ�
 * </p>
 * ��������:2009-09-24 09:24:28
 * 
 * @author
 * @version NCPrj ??
 */

public class OnhandBO {

  private static final String ICHandSNUpdateVO = "ICHandSNUpdateVO";

  private static final String ICHandUpdateVO = "ICHandUpdateVO";

  private static final String OriginBillVOs = "OriginBillVOs";

  // ��浥�ݱ�����VO
  private ICBillVO[] curbillvos;

  // �ִ���ά�����ִ���ά��pk��map�����ִ������º���������Ʒ�ִ����ʹ��
  private Map<String, String> mapdims;

  // ��浥�ݱ���ǰ��ԭʼVO
  private ICBillVO[] originbillvos;

  /**
   * ����Ĭ�Ϸ�ʽ����������. ��������:2009-04-23 15:30:50
   */
  public OnhandBO() {
    super();
  }

  public OnhandSNViewVO[] fetchOnhandSNVO(AggregatedValueObject[] orgbillvos)
      throws BusinessException {
    // ��Ʒ�ִ�
    IOnhandSNData sndatasrc =
        OnhandDataFactory.getOnhandSNDataSource(orgbillvos[0]);
    OnhandSNViewVO[] snvos = sndatasrc.getOnhandSNVOs(orgbillvos);
    return snvos;
  }

  public OnhandVO[] fetchOnhandVO(AggregatedValueObject[] orgbillvos)
      throws BusinessException {
    // �ִ�
    IOnhandData datasrc = OnhandDataFactory.getOnhandDataSource(orgbillvos[0]);
    OnhandVO[] handvos = datasrc.getOnhandVOs(orgbillvos);
    return handvos;
  }

  /**
   * ˵��������OnhandSNVO ��������:2009-04-23 15:30:50
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
   * �����ִ���
   * 
   * @param conds
   */
  public void fixOnhandNum(ConditionVO[] conds) {
    TimeLog.logStart();
    // ����
    new OnhandLock().addOnhandPub_Lock();
    TimeLog.info("�����ִ�����ʱ");/*-=notranslate=-*/
    TimeLog.logStart();

    // �����ִ��� a
    new OnhandNumFixBP().fixOnHandNum(conds);// .fixOnHandNumAndSN(conds);
    TimeLog.info("�����ִ���������ʱ");/*-=notranslate=-*/
  }

  /**
   * ���������������޸��ִ���(����)
   * <p>
   * <b>����˵��</b>
   * 
   * @param orgbillvos
   * @throws BusinessException <p>
   * @since 6.0
   * @author yangb
   * @time 2010-9-2 ����10:57:10
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
   * ���������������޸��ִ���(����)
   * <p>
   * <b>����˵��</b>
   * 
   * @param billvos
   * @throws BusinessException <p>
   * @since 6.0
   * @author yangb
   * @time 2010-9-2 ����10:56:53
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
    // �ִ�
    OnhandVO[] beforehandvos =
        (OnhandVO[]) context.getSession(OnhandBO.ICHandUpdateVO);
    // ��Ʒ�ִ�
    OnhandSNViewVO[] beforehandsnvos =
        (OnhandSNViewVO[]) context.getSession(OnhandBO.ICHandSNUpdateVO);
    OnhandVO[] updatehandvos =
        this.modifyOnhandVOProcess(beforehandvos, beforehandsnvos, billvos);

    // �����ִ�󣬼�鰲ȫ���
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
   * ���������������޸��ִ���
   * <p>
   * <b>����˵��</b>
   * 
   * @param handnumvos
   * @return
   * @throws BusinessException <p>
   * @since 6.0
   * @author yangb
   * @time 2010-9-2 ����10:56:36
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
      // �ִ������ݼ��
      OnhandDataCheckRule tool = new OnhandDataCheckRule();
      tool.checkDimAllNotNullField(handvos);
      tool.checkNumNotNull(handvos);

    }
    catch (Exception e) {
      throw new BusinessException(OnhandRes.getDimDataErr());
    }
    // ����
    handvos = this.modifyOnhandBase(handvos);
    return handvos;

  }

  /**
   * ���������������޸��ִ���
   * <p>
   * <b>����˵��</b>
   * 
   * @param vos
   * @return
   * @throws BusinessException <p>
   * @since 6.0
   * @author yangb
   * @time 2010-9-2 ����10:52:19
   */
  public OnhandVO[] modifyOnhandNum(OnhandVO[] vos) throws BusinessException {
    if (ValueCheckUtil.isNullORZeroLength(vos)) {
      return null;
    }
    OnhandVO[] handvos = vos;
    // �Ƿ�Ԥ�������
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
    // �ִ������ݻ������
    OnhandDataCheckRule tool = new OnhandDataCheckRule();
    tool.checkDimNotNullContent(handvos);
    tool.checkNumNotNull(handvos);

    TimeLog.info("����ִ���������ʱ");/*-=notranslate=-*/
    TimeLog.logStart();
    // �����ִ���γ��
    new OnhandDimBO().getDimPks(handvos);

    TimeLog.info("�����ִ���γ����ʱ");/*-=notranslate=-*/
    // ����Ԥ�������
    if (isreserve) {
      handvos = this.processReserveInOut(handvos);
    }

    TimeLog.logStart();
    // ���ִ���γ�Ⱥϲ�
    OnhandVO[] updatehandvos = OnhandVOTools.mergeOnhandByDim(handvos);

    TimeLog.info("�ִ���γ�Ⱥϲ���ʱ");/*-=notranslate=-*/
    // ����
    handvos = this.modifyOnhandBase(updatehandvos);
    // �����ִ���ά��,����Ʒ������ʹ��
    this.backupDimPk(CollectionUtils.combineArrs(handvos, updatehandvos));

    TimeLog.logStart();
    // �ִ������
    new OnhandCheck(isreserve ? OnhandCheck.OnhandCheckType.UpdateReserveOut
        : OnhandCheck.OnhandCheckType.UpdateOnhandNumCheck)
        .checkOnhandNumMsgRow(updatehandvos, this.curbillvos);

    TimeLog.info("����ִ���������ʱ");/*-=notranslate=-*/

    return handvos;
  }

  /**
   * ���������������޸ĵ�Ʒ�ִ���
   * <p>
   * <b>����˵��</b>
   * 
   * @param handsnvos <p>
   * @since 6.0
   * @author yangb
   * @time 2010-9-2 ����10:56:19
   */
  public void modifyOnhandSNNum(OnhandSNViewVO[] handsnvos) {
    if (handsnvos == null || handsnvos.length <= 0) {
      return;
    }
    try {
      // ����ִ���ά��
      this.fillOnhandSNDimPk(handsnvos);
      // ��ȡ��Ʒ�ִ�VO
      OnhandSNVO[] snvos = OnhandVOTools.getOnhandSNVOs(handsnvos);
      //��¼ֵ���油ֲ ���쳣��
      Map<String,Object> pkmsgMap=new HashMap<String,Object>();
      String field = SysInitGroupQuery.isSNEnabled() ? ICPubMetaNameConst.PK_SERIALCODE : OnhandSNVO.VSNCODE;
      for(OnhandSNVO snvo:snvos){
        if(snvo.getAttributeValue(snvo.getVsncode())!=null){
          String value = (String)snvo.getAttributeValue(field);
          pkmsgMap.put(value,snvo.getAttributeValue(snvo.getVsncode()));
          pkmsgMap.put("snNum", snvo.getAttributeValue("snNum"));
        }
      }
      
      // �����к�����ϲ�
      snvos = OnhandVOTools.mergeOnhandSNVO(snvos);
      // ����OnhandSNVO
      snvos = this.filterOnhandSNVO(snvos);
      if (ValueCheckUtil.isNullORZeroLength(snvos)) {
        return;
      }
      // ��Ʒ�ִ������ݻ������
      new OnhandDataCheckRule().onCheckOnhandSN(snvos);
      // ����
      snvos = OnhandVOTools.sortOnhandSNVOBySNCode(snvos);
      // ����
      new OnhandSNDMO().updateOnhandSN(snvos);
      OnhandCheck onhandcheck = new OnhandCheck();
      //��ֲ
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
      
      // ��Ʒ���к�У��
      onhandcheck.checkOnhandSNNum(snvos);
      // ��Ʒ����У�飬��ΪҪ������Ȩ�ޣ����Ժ����кŷֿ�У��
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
   * ���״̬����ʱ�����ִ���
   * 
   * @param vos
   * @return
   */
  public Map<String, String> modifyStateOnhand(SuperVO[] vos) {

    Map<String, String> hidToPkdim = new HashMap<String, String>();
    try {

      ICStateOnhandData dataSource = new ICStateOnhandData();
      // ��ò�����ִ���VO
      OnhandVO[] beforeOnhandVOs = dataSource.getBeforeOnhandData(vos);
      OnhandVO[] afterOnhandVOs = dataSource.getAfterOnhandData(vos);

      beforeOnhandVOs = OnhandVOTools.negativeOnhandVOs(beforeOnhandVOs);
      OnhandVO[] changedOnhandvos =
          CollectionUtils.combineArrs(beforeOnhandVOs, afterOnhandVOs);
      TimeLog.logStart();
      new OnhandDataCheckRule().checkDimNotNullContent(changedOnhandvos);
      TimeLog.info("�ִ��������ʱ");/*-=notranslate=-*/

      TimeLog.logStart();
      new OnhandDimBO().getDimPks(afterOnhandVOs);
      TimeLog.info("�ִ���ά�ȸ�����ʱ");/*-=notranslate=-*/
      hidToPkdim = this.getHidAndPkdim(vos, afterOnhandVOs);

      TimeLog.logStart();
      OnhandVO[] updatehandvos =
          OnhandVOTools.mergeOnhandByDim(changedOnhandvos);
      TimeLog.info("�ִ���γ�Ⱥϲ���ʱ");/*-=notranslate=-*/
      // ����
      OnhandVO[] handvos = this.modifyOnhandBase(updatehandvos);
      // �����ִ���ά��,����Ʒ������ʹ��
      this.backupDimPk(CollectionUtils.combineArrs(handvos, updatehandvos));
      
      // �ִ������
      new OnhandCheck(OnhandCheck.OnhandCheckType.UpdateOnhandNumCheck)
          .checkOnhandNum(updatehandvos);
      
      // ���µ�Ʒ���� 
//      OnhandSNViewVO[] endsnvos = this.fetchOnhandSNVO(billvos); TODO �Ա�ICStateOnhandSNData
//      ICStateOnhandSNData sndataSource = new ICStateOnhandSNData();
      // ��ò�����ִ���VO
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
   * �������������������ִ���ά��
   * <p>
   * <b>����˵��</b>
   * 
   * @param handvos <p>
   * @since 6.0
   * @author yangb
   * @time 2010-9-2 ����10:56:04
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
              "04008019-0036")/* @res "�ִ�������ά�ȴ���" */);
    }
  }

  /**
   * ���������������ñ����ִ���ά����䵥Ʒ�ִ�ά��
   * <p>
   * <b>����˵��</b>
   * 
   * @param handsnvos <p>
   * @since 6.0
   * @author yangb
   * @time 2010-9-2 ����10:55:49
   */
  private void fillOnhandSNDimPk(OnhandSNViewVO[] handsnvos) {
    if (handsnvos == null || handsnvos.length <= 0) {
      return;
    }
    if (this.mapdims == null) {
      ExceptionUtils.wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl
          .getNCLangRes().getStrByID("4008019_0", "04008019-0037")/*
                                                                   * @res
                                                                   * "���ݴ����޷���䵥Ʒ�ִ�ά��"
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
                                                                     * "���ݴ����޷���䵥Ʒ�ִ�ά��!!!"
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
   * ���������������Ƿ�Ԥ�������
   * <p>
   * <b>����˵��</b>
   * 
   * @param billtype
   * @return <p>
   * @since 6.0
   * @author yangb
   * @time 2010-9-2 ����10:57:21
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
   * ���������������޸��ִ���
   * <p>
   * <b>����˵��</b>
   * 
   * @param vos
   * @return
   * @throws BusinessException <p>
   * @since 6.0
   * @author yangb
   * @time 2010-9-2 ����10:53:19
   */
  private OnhandVO[] modifyOnhandBase(OnhandVO[] vos) {
    if (ValueCheckUtil.isNullORZeroLength(vos)) {
      return null;
    }
    OnhandVO[] handvos = vos;
    TimeLog.logStart();
    // �����ִ�������
    handvos = this.procOnhandDataByBiz(handvos);

    TimeLog.info("�����ִ���������ʱ");/*-=notranslate=-*/
    if (handvos == null || handvos.length <= 0) {
      return null;
    }
    TimeLog.logStart();
    // ����
    OnhandVOTools.sortOnhandVOByDim(handvos);
    TimeLog.info("�ִ��������ڴ�������ʱ");/*-=notranslate=-*/
    TimeLog.logStart();
    // ����
    // ���ݿ���� �����ִ�����������
    // new OnhandLock().lockByDimForBill(handvos);
    // new OnhandLock().lockByDim(handvos);
    new OnhandLock().addOnhandPubShared_Lock();

    TimeLog.info("�����ִ�����ʱ");/*-=notranslate=-*/
    TimeLog.logStart();
    // ��������
    this.updateOnhandData(handvos);

    TimeLog.info("�����ִ���������ʱ");/*-=notranslate=-*/
    return handvos;
  }

  /**
   * ������������������Ԥ�������
   * <p>
   * <b>����˵��</b>
   * 
   * @param handvos
   * @return
   * @throws BusinessException <p>
   * @since 6.0
   * @author yangb
   * @time 2010-9-2 ����10:53:50
   */
  private OnhandVO[] processReserveInOut(OnhandVO[] vos)
      throws BusinessException {
    OnhandVO[] handvos = vos;

    TimeLog.logStart();
    // ����Ԥ�������
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

    TimeLog.info("����Ԥ���������ʱ");/*-=notranslate=-*/
    return handvos;
  }

  /**
   * ����������������ҵ��������ִ�������
   * <p>
   * <b>����˵��</b>
   * 
   * @param hanvos
   * @return <p>
   * @since 6.0
   * @author yangb
   * @time 2010-9-2 ����10:55:05
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
   * ���������������޸��ִ�����¼
   * <p>
   * <b>����˵��</b>
   * 
   * @param handvos
   * @throws BusinessException <p>
   * @since 6.0
   * @author yangb
   * @time 2010-9-2 ����10:55:21
   */
  private void updateOnhandData(OnhandVO[] handvos) {
    if (handvos == null || handvos.length <= 0) {
      return;
    }
    new OnhandDMO().saveOnhandVOs(OnhandVOTools.getOnhandNumVOs(handvos));
  }
}
