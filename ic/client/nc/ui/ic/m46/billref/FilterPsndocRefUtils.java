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
   * 方法功能描述：根据部门过滤人员参照 <b>参数说明</b>
   * 
   * @param cdeptid
   *          <p>
   * @author liyu1
   * @time 2010-1-22 下午03:53:18
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
   * 方法功能描述：
   * <p>过滤非启用状态的人员
   * <b>参数说明</b>
   * <p>
   * 
   * @since 6.0
   * @author lirr
   * @time 2010-6-17 上午11:29:31
   */
  public void filterUnableState() {
    String sql1 = "bd_psndoc.dr=0 and bd_psndoc.enablestate=2";
    this.filterRef(sql1);
  }

  /**
   * 过滤集团下的人员，并设置业务单元默认组织
   * 
   * @param pk_org 组织
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
   * 根据部门主键，定位人员参照左侧列表部门光标位置
   * 
   * @param pk 部门主键
   * @author 孙伟
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
