package nc.ui.qc.c003.billref.ccp;

import java.awt.Container;

import nc.ui.pub.pf.BillSourceVar;
import nc.ui.pubapp.billref.src.view.SourceRefDlg;

/**
 * ת��--Ϊ��֤��
 * 
 * @since 6.0
 * @version 2011-2-23 ����09:11:02
 * @author hanbin
 */
public class SourceRefDlgForC006 extends SourceRefDlg {
  private static final long serialVersionUID = -8241391307105014221L;

  public SourceRefDlgForC006(
      Container parent, BillSourceVar bsVar) {
    super(parent, bsVar, true);
    this.setResizable(true);
  }

  @Override
  public String getRefBillInfoBeanPath() {
    return "nc/ui/qc/c003/billref/ccp/refinfo_forC006.xml";
  }
}
