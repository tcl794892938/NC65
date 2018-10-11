package nc.ui.ic.m46.billref;

import nc.ui.bd.ref.RefInitializeCondition;
import nc.ui.pub.beans.UIRefPane;
import nc.vo.pub.lang.UFBoolean;

public class FilterDeptRefUtils {

  private UIRefPane pane;

  public FilterDeptRefUtils(UIRefPane pane) {

    this.pane = pane;

  }

  /**
   * 过滤集团下的部门，并设置业务单元默认组织
   * 
   * @param pk_org 组织
   * @author fanly3
   */
  public void groupFilterRef(String pk_org) {
    this.pane.setMultiCorpRef(false);
    this.pane.getRefModel().setFilterRefNodeName(new String[] {
      "业务单元" /* -=notranslate=- */
    });
    RefInitializeCondition refInitCon =
        this.pane.getRefUIConfig().getRefFilterInitconds()[0];
    refInitCon.setDefaultPk(pk_org);
    this.pane.getRefUIConfig().setRefFilterInitconds(
        new RefInitializeCondition[] {
          refInitCon
        });
  }

  public void filtByUsedFlag(UFBoolean bUsedflag) {
    StringBuffer where = new StringBuffer();
    if (bUsedflag != null) {
      where.append(" usedflag = '").append(bUsedflag.toString()).append("' ");
      this.pane.setWhereString(where.toString());
    }
  }

  public void filterItemRefByOrg(String pk_org) {
    if (this.pane == null || this.pane.getRefModel() == null) {
      return;
    }
    this.pane.getRefModel().setPk_org(pk_org);
  }
}
