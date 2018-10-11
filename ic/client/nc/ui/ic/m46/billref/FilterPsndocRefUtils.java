package nc.ui.ic.m46.billref;

import nc.ui.bd.ref.AbstractRefTreeModel;
import nc.ui.bd.ref.RefInitializeCondition;
import nc.ui.pub.beans.UIRefPane;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pubapp.pattern.pub.PubAppTool;
import nc.vo.pubapp.pattern.pub.SqlBuilder;

public class FilterPsndocRefUtils {
  private UIRefPane panel;

  private SqlBuilder sql = new SqlBuilder();

  public FilterPsndocRefUtils(UIRefPane panel) {
    this.panel = panel;

  }

  public void filtByUsedFlag(UFBoolean bUsedflag) {
    StringBuffer where = new StringBuffer();
    if (bUsedflag != null && bUsedflag.booleanValue()) {
      if (where.length() > 0) {
        where.append(" and ");
      }
      where.append(" enablestate = ").append(2);
      this.panel.setWhereString(where.toString());
    }
  }

  public void filterItemRefByOrg(String pk_org) {

    if (this.panel == null || this.panel.getRefModel() == null) {
      return;
    }
    this.panel.getRefModel().setPk_org(pk_org);

  }

  /**
   * �����������������ݲ��Ź�����Ա���� <b>����˵��</b>
   * 
   * @param cdeptid
   *          <p>
   * @author liyu1
   * @time 2010-1-22 ����03:53:18
   */
  public void filterPsndocRefByDept(String cdeptid) {
    if (PubAppTool.isNull(cdeptid)) {
      return;
    }
    AbstractRefTreeModel treemodel =
        (AbstractRefTreeModel) this.panel.getRefModel();

    SqlBuilder sqlwhere = new SqlBuilder();

    sqlwhere.append("pk_dept", cdeptid);

    treemodel.setClassWherePart(sqlwhere.toString());

  }

  /**
   * ��������������
   * <p>���˷�����״̬����Ա
   * <b>����˵��</b>
   * <p>
   * 
   * @since 6.0
   * @author lirr
   * @time 2010-6-17 ����11:29:31
   */
  public void filterUnableState() {
    String sql1 = "bd_psndoc.dr=0 and bd_psndoc.enablestate=2";
    this.filterRef(sql1);
  }

  /**
   * ���˼����µ���Ա��������ҵ��ԪĬ����֯
   * 
   * @param pk_org ��֯
   * @author fanly3
   */
  public void groupFilterRef(String pk_org) {
    this.panel.setMultiCorpRef(false);
    RefInitializeCondition refInitCon =
        this.panel.getRefUIConfig().getRefFilterInitconds()[0];
    refInitCon.setDefaultPk(pk_org);
    this.panel.getRefUIConfig().setRefFilterInitconds(
        new RefInitializeCondition[] {
          refInitCon
        });
  }

  /**
   * ���ݲ�����������λ��Ա��������б��Ź��λ��
   * 
   * @param pk ��������
   * @author ��ΰ
   */
  public void fixFocusByPK(String pk) {
    if (this.panel == null || this.panel.getRefModel() == null) {
      return;
    }
    this.panel.setClassLocatePK(pk);
  }

  private void filterRef(String sqlwhere) {

    if (this.panel == null || this.panel.getRefModel() == null) {
      return;
    }
    if (this.sql != null && this.sql.toString().length() > 0) {
      this.sql.append(" and ");

    }
    if (this.sql != null) {
      this.sql.append(sqlwhere);
      this.panel.setWhereString(this.sql.toString());
    }
  }
}
