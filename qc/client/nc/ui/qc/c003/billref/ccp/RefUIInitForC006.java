package nc.ui.qc.c003.billref.ccp;

import nc.impl.pubapp.env.BSContext;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pubapp.billref.src.IRefPanelInit;
import nc.ui.qc.c003.maintain.util.ReportScaleSetter;

/**
 * ���ݲ���UI��ʼ�������������Դ����ȣ���ʾ������
 * 
 * @since 6.0
 * @version 2011-6-9 ����02:00:39
 * @author hanbin
 */
public class RefUIInitForC006 implements IRefPanelInit {

  @Override
  public void refMasterPanelInit(BillListPanel masterPanel) {
    // ���ȴ���
    String pk_group = BSContext.getInstance().getGroupID();
    new ReportScaleSetter().setListScale(masterPanel, pk_group);
  }

  @Override
  public void refSinglePanelInit(BillListPanel singlePanel) {
    // ���ȴ���
    String pk_group = BSContext.getInstance().getGroupID();
    new ReportScaleSetter().setListScale(singlePanel, pk_group);
  }
}
