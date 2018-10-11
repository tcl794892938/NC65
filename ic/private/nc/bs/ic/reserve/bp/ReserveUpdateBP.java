package nc.bs.ic.reserve.bp;

import java.util.ArrayList;
import java.util.List;

import nc.bs.ic.pub.env.ICBSContext;
import nc.bs.ic.reserve.bp.rule.ReWriteRequireRsNum;
import nc.bs.ic.reserve.bp.rule.ReWriteSupply;
import nc.bs.ic.reserve.bp.rule.RequireDataCheck;
import nc.bs.ic.reserve.bp.rule.ReserveNumCheck;
import nc.bs.ic.reserve.bp.rule.ReserveStateCheck;
import nc.bs.ic.reserve.bp.rule.ReserveVOProcess;
import nc.bs.ic.reserve.bp.rule.UpdateOnhand;
import nc.bs.ml.NCLangResOnserver;
import nc.impl.pubapp.pattern.data.vo.VODelete;
import nc.impl.pubapp.pattern.data.vo.VOInsert;
import nc.impl.pubapp.pattern.data.vo.VOUpdate;
import nc.vo.ic.pub.util.VOEntityUtil;
import nc.vo.ic.pub.util.ValueCheckUtil;
import nc.vo.ic.reserve.entity.OnhandReserveVO;
import nc.vo.ic.reserve.entity.PreReserveVO;
import nc.vo.ic.reserve.entity.ReserveBillVO;
import nc.vo.ic.reserve.entity.ReserveVO;
import nc.vo.ic.reserve.pub.ReserveVOCheck;
import nc.vo.ic.reserve.pub.ReserveVOUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

/**
 * <p>
 * <b>修改预留数据</b>
 * 
 * @version v60
 * @since v60
 * @author yangb
 * @time 2010-4-30 下午08:42:56
 */
public class ReserveUpdateBP {

  /**
   * ReserveUpateBP 的构造子
   */
  public ReserveUpdateBP() {
    super();
  }

  /**
   * BP修改保存方法
   * 
   * @return
   * @since 6.0
   */
  public ReserveBillVO[] update(ReserveBillVO[] vos, ReserveBillVO[] originVOs) {
    ReserveBillVO[] resultVOs = null;
    try {
      ReserveVO[] resvos = VOEntityUtil.getHeadVOs(vos);
      new ReserveStateCheck().checkReserveStateWhenUpdate(resvos);
      ReserveVOProcess fv = new ReserveVOProcess();
      fv.setDefaultValue(resvos, new ICBSContext());
      fv.sumBodyRsNumToReserveVO(vos);
      new ReserveVOCheck().checkReserveBillVO(vos);
      resultVOs = this.update(vos);
      new ReserveNumCheck().checkReserveBillNum(vos);
      resvos = VOEntityUtil.getHeadVOs(vos);
      // 需求数据相关检查
      new RequireDataCheck().checkReserveVOs(resvos);
      // 更新现存预留
      new UpdateOnhand().updateOnhand(ReserveVOUtil
          .getOnhandReserveVOs(resultVOs), ReserveVOUtil
          .getOnhandReserveVOs(originVOs));
      // 更新预计入供给
      new ReWriteSupply().updateSupply(ReserveVOUtil
          .getPreReserveVOs(resultVOs), ReserveVOUtil
          .getPreReserveVOs(originVOs));
      // 更新需求单据累计预留数量
      new ReWriteRequireRsNum().updateRequiryTalRsNum(resvos,
          (ReserveVO[]) VOEntityUtil.getHeadVOs(originVOs));
    }
    catch (Exception e) {
      ExceptionUtils.wrappException(e);
    }
    return resultVOs;
  }

  /**
   * 
   */
  private ReserveBillVO[] update(ReserveBillVO[] vos) throws BusinessException {

    List<OnhandReserveVO> lnewhandvos = new ArrayList<OnhandReserveVO>();
    List<OnhandReserveVO> ldelhandvos = new ArrayList<OnhandReserveVO>();
    List<OnhandReserveVO> lupdatehandvos = new ArrayList<OnhandReserveVO>();

    List<PreReserveVO> lnewprevos = new ArrayList<PreReserveVO>();
    List<PreReserveVO> ldelprevos = new ArrayList<PreReserveVO>();
    List<PreReserveVO> lupdateprevos = new ArrayList<PreReserveVO>();
    boolean bbody = false;
    OnhandReserveVO[] handvos = null;
    PreReserveVO[] prevos = null;
    for (ReserveBillVO vo : vos) {
      bbody = false;
      handvos = vo.getOnhandReserveVO();
      if (!ValueCheckUtil.isNullORZeroLength(handvos)) {
        for (OnhandReserveVO handvo : handvos) {
          if (handvo.getStatus() == VOStatus.DELETED) {
            ldelhandvos.add(handvo);
            continue;
          }
          if (handvo.getStatus() == VOStatus.UPDATED) {
            lupdatehandvos.add(handvo);
          }
          else if (handvo.getStatus() == VOStatus.NEW) {
            lnewhandvos.add(handvo);
          }
          bbody = true;
        }
      }
      prevos = vo.getPreReserveVO();
      if (!ValueCheckUtil.isNullORZeroLength(prevos)) {
        for (PreReserveVO prevo : prevos) {
          if (prevo.getStatus() == VOStatus.DELETED) {
            ldelprevos.add(prevo);
            continue;
          }
          if (prevo.getStatus() == VOStatus.UPDATED) {
            lupdateprevos.add(prevo);
          }
          else if (prevo.getStatus() == VOStatus.NEW) {
            lnewprevos.add(prevo);
          }
          bbody = true;
        }
      }
      if (!bbody) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008020_0", "04008020-0062", null, new String[]{vo.getReserveVO().getVrescode()})/*单据{0}表体不能全为空*/);
      }
    }
    if (!ValueCheckUtil.isNullORZeroLength(lnewhandvos)) {
      new VOInsert<OnhandReserveVO>().insert(lnewhandvos
          .toArray(new OnhandReserveVO[lnewhandvos.size()]));
    }
    if (!ValueCheckUtil.isNullORZeroLength(ldelhandvos)) {
      new VODelete<OnhandReserveVO>().delete(ldelhandvos
          .toArray(new OnhandReserveVO[ldelhandvos.size()]));
    }
    if (!ValueCheckUtil.isNullORZeroLength(lupdatehandvos)) {
      new VOUpdate<OnhandReserveVO>().update(lupdatehandvos
          .toArray(new OnhandReserveVO[lupdatehandvos.size()]), new String[] {
        OnhandReserveVO.NRSNUM
      });
    }

    if (!ValueCheckUtil.isNullORZeroLength(lnewprevos)) {
      new VOInsert<PreReserveVO>().insert(lnewprevos
          .toArray(new PreReserveVO[lnewprevos.size()]));
    }
    if (!ValueCheckUtil.isNullORZeroLength(ldelprevos)) {
      new VODelete<PreReserveVO>().delete(ldelprevos
          .toArray(new PreReserveVO[ldelprevos.size()]));
    }
    if (!ValueCheckUtil.isNullORZeroLength(lupdateprevos)) {
      new VOUpdate<PreReserveVO>().update(lupdateprevos
          .toArray(new PreReserveVO[lupdateprevos.size()]), new String[] {
        PreReserveVO.NRSNUM
      });
    }
    ReserveVO[] resvos = VOEntityUtil.getHeadVOs(vos);
    new VOUpdate<ReserveVO>().update(resvos, new String[] {//by tcl 预留客户类型，备注更改
      ReserveVO.NTALRSNUM,ReserveVO.CUSTOMERTYPE,ReserveVO.REMARKS
    });
    return vos;
  }

}
