package nc.bs.so.m4331.maintain;

import nc.bs.so.m4331.maintain.rule.atp.DeliveryVOATPBeforeRule;
import nc.bs.so.m4331.maintain.rule.credit.RenovateARByHidsBeginRule;
import nc.bs.so.m4331.maintain.rule.credit.RenovateARByHidsEndRule;
import nc.bs.so.m4331.maintain.rule.insert.CheckBillCodeRule;
import nc.bs.so.m4331.maintain.rule.insert.CheckNewNullRule;
import nc.bs.so.m4331.maintain.rule.insert.CheckValityRule;
import nc.bs.so.m4331.maintain.rule.insert.FillNewDefaultRule;
import nc.bs.so.m4331.maintain.rule.insert.RewriteBillInsertRule;
import nc.bs.so.m4331.maintain.rule.material.MaterielDistributeCheckRule;
import nc.bs.so.m4331.maintain.rule.reverse.AutoReserveRule;
import nc.bs.so.m4331.maintain.rule.reverse.ReduceReserveNumRule;
import nc.bs.so.m4331.plugin.BP4331PlugInPoint;
import nc.bs.so.pub.rule.FillBillTailInfoRuleForIns;
import nc.impl.pubapp.bd.userdef.UserDefSaveRule;
import nc.impl.pubapp.pattern.data.bill.BillInsert;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.itf.scmpub.reference.uap.group.SysInitGroupQuery;
import nc.vo.credit.engrossmaintain.pub.action.M4331EngrossAction;
import nc.vo.pubapp.pattern.log.TimeLog;
import nc.vo.pubapp.util.SetAddAuditInfoRule;
import nc.vo.scmpub.rule.TrafficOrgEnableCheckRule;
import nc.vo.so.m4331.entity.DeliveryBVO;
import nc.vo.so.m4331.entity.DeliveryHVO;
import nc.vo.so.m4331.entity.DeliveryVO;
import nc.vo.so.m4331.rule.DeliveryMarginRule;

public class InsertDeliveryBP {

  private void addAfterRule(AroundProcesser<DeliveryVO> processer) {
    // ���ݺ��ظ���У��
    IRule<DeliveryVO> rule = new CheckBillCodeRule();
    processer.addAfterRule(rule);
    // ����ռ�ü��
    rule = new RenovateARByHidsEndRule(M4331EngrossAction.M4331Insert);
    processer.addAfterRule(rule);
    // ��д
    rule = new RewriteBillInsertRule();
    processer.addAfterRule(rule);

    boolean icEnable = SysInitGroupQuery.isICEnabled();
    if (icEnable) {
      // �Զ�Ԥ��
      rule = new AutoReserveRule();
      processer.addAfterRule(rule);
    }
    
    //by tcl 2018-03 �����������۳�Ԥ������
    rule = new ReduceReserveNumRule();
    processer.addAfterRule(rule);

  }

  private void addBeforeRule(AroundProcesser<DeliveryVO> processer) {

    // �����ǿ�У��
    IRule<DeliveryVO> rule = new CheckNewNullRule();
    processer.addBeforeRule(rule);

    // ������֯ͣ�ü��
    rule = new TrafficOrgEnableCheckRule<DeliveryVO>();
    processer.addBeforeRule(rule);
    // ���ݺϷ���У��
    rule = new CheckValityRule();
    processer.addBeforeRule(rule);
    // ���Ĭ��ֵ
    rule = new FillNewDefaultRule();
    processer.addBeforeRule(rule);
    // ����Ƶ���Ϣ
    rule = new FillBillTailInfoRuleForIns<DeliveryVO>();
    processer.addBeforeRule(rule);

    // ��������Ϣ:�����ˡ�����ʱ�䡢����޸��ˡ�����޸�ʱ��
    rule = new SetAddAuditInfoRule<DeliveryVO>();
    processer.addBeforeRule(rule);

    // ��������Ƿ���䵽��Ӧ�Ŀ����֯
    rule = new MaterielDistributeCheckRule();
    processer.addBeforeRule(rule);

    
    // ����ռ�ü��
    rule = new RenovateARByHidsBeginRule(M4331EngrossAction.M4331Insert);
    processer.addBeforeRule(rule);

    boolean icEnable = SysInitGroupQuery.isICEnabled();
    if (icEnable) {
      // ����ǰ���������
      rule = new DeliveryVOATPBeforeRule();
      processer.addBeforeRule(rule);
    }
    // У���ͷ����¼����Զ������Ƿ�����
    rule = new UserDefSaveRule<DeliveryVO>(new Class[] {
      DeliveryHVO.class, DeliveryBVO.class
    });
    processer.addBeforeRule(rule);
  }

  public DeliveryVO[] insert(DeliveryVO[] bills) {
    AroundProcesser<DeliveryVO> processer =
        new AroundProcesser<DeliveryVO>(BP4331PlugInPoint.InsertAction);

    // ����ִ��ǰҵ�����
    this.addBeforeRule(processer);

    // ����ִ�к�ҵ�����
    this.addAfterRule(processer);

    TimeLog.logStart();
    processer.before(bills);
    // buxh �������Ž����ظ����ŵ���� Ҫ�ڱ����ٴ���һ��β��
    DeliveryMarginRule margin = new DeliveryMarginRule();
    margin.process(bills);
    TimeLog.info("����ǰִ��ҵ�����"); /* -=notranslate=- */

    TimeLog.logStart();
    BillInsert<DeliveryVO> bo = new BillInsert<DeliveryVO>();
    DeliveryVO[] vos = bo.insert(bills);
    TimeLog.info("���浥�ݵ����ݿ�"); /* -=notranslate=- */

    TimeLog.logStart();
    processer.after(vos);
    TimeLog.info("�����ִ��ҵ�����"); /* -=notranslate=- */

    return vos;
  }

}
