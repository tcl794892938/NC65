package nc.ui.ic.m46.billref;



import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pubapp.uif2app.query2.QueryConditionDLGDelegator;
import nc.ui.pubapp.uif2app.query2.refedit.IRefFilter;
import nc.ui.pubapp.uif2app.query2.refregion.CommonTwoLayerListener;
import nc.ui.querytemplate.QueryConditionDLG;
import nc.ui.querytemplate.filtereditor.FilterEditorWrapper;
import nc.ui.querytemplate.filtereditor.IFilterEditor;
import nc.ui.querytemplate.value.IFieldValueElement;
import nc.ui.querytemplate.valueeditor.ref.CompositeRefPanel;
import nc.vo.pub.lang.UFBoolean;

/**
 * <p>
 * <b>���Ų��յĹ�������</b>
 * <ul>
 * <li>˵���������֯Ψһ�����մ���֯���ˣ������ſɼ�
 * </ul>
 * <p>
 * <p>
 * 
 * @version 6.0
 * @since 6.0
 * @author hanbin
 * @time 2010-4-1 ����11:25:50
 */
public class QDeptFilter extends CommonTwoLayerListener {

  // ��ѯ�Ի���
  // private SCMQueryConditionDlg subdlg;

  // ����
  private String deptCode;

  private UFBoolean isforemultcorp;

  // ��֯�ֶ�
  private String pk_orgCode = "pk_org";

  // �Ƿ�����
  UFBoolean bUsedflag;

  public QDeptFilter(QueryConditionDLG dlg, String deptCode) {
    this(new QueryConditionDLGDelegator(dlg), deptCode);
  }

  public QDeptFilter(QueryConditionDLGDelegator dlg, String deptCode) {
    super(dlg);
    // this.subdlg = dlg;
    this.deptCode = deptCode;
  }

  public void addEditorListener() {
    this.setFatherPath(this.pk_orgCode);
    this.setChildPath(this.deptCode);
    // ע��༭�¼�
    this.qryCondDLGDelegator.registerCriteriaEditorListener(this);
  }

  public void filter() {
    // String wherePart = this.getWherePart();
    // if (StringUtils.isNotEmpty(wherePart)) {
    // this.qryCondDLGDelegator.setFilterRef(this.deptCode, wherePart);
    // }
    this.qryCondDLGDelegator.setRefFilter(this.deptCode, new IRefFilter() {

      @Override
      public void doFilter(UIRefPane refPane) {
        new FilterDeptRefUtils(refPane)
            .filtByUsedFlag(QDeptFilter.this.bUsedflag);
      }
    });
  }

  public void setbUsedflag(UFBoolean bUsedflag1) {
    this.bUsedflag = bUsedflag1;
  }

  @Override
  public void setChildRefRegion(List<IFieldValueElement> fatherValues,
      IFilterEditor editor) {
    List<String> diffValues = new ArrayList<String>();
    for (IFieldValueElement fve : fatherValues) {
      if (fve.getSqlString() == null) {
        continue;
      }
      if (diffValues.contains(fve.getSqlString())) {
        continue;
      }

      diffValues.add(fve.getSqlString());
    }

    FilterEditorWrapper wrapper = new FilterEditorWrapper(editor);
    // UIRefPane refPane = (UIRefPane)
    // wrapper.getFieldValueElemEditorComponent();
    JComponent component = wrapper.getFieldValueElemEditorComponent();
    UIRefPane refPane = null;
    if (component instanceof UIRefPane) {
      refPane = (UIRefPane) component;
    }
    else if (component instanceof CompositeRefPanel) {
      refPane = ((CompositeRefPanel) component).getStdRefPane();
    }
    if (refPane == null) {
      return;
    }
    if (refPane.getRefModel() == null) {
      return;
    }
    // // ���֮ǰ��ֵ
    // refPane.getRefModel().clearData();
    // �������֯Ψһ����������֯���ˣ������ſɼ�
    if (diffValues.size() == 1) {
      refPane.setPk_org(fatherValues.get(0).getSqlString());
      if (null == this.isforemultcorp) {
        refPane.setMultiCorpRef(false);
      }
      else {
        refPane.setMultiCorpRef(this.isforemultcorp.booleanValue());
        refPane.setMultiOrgSelected(this.isforemultcorp.booleanValue());
      }
    }
    else {
      // refPane.setPk_org(null);
      refPane.setMultiCorpRef(true);
    }
    String cuserid=ClientEnvironment.getInstance().getUser().getCuserid();
    refPane.getRefModel().addWherePart(" and exists(select 1 from v_tcl_dept v where v.pk_dept=org_dept.pk_dept and cuserid='"+cuserid+"') ");
  }

  public void setForceMultiCorpRef(UFBoolean isforemultcorp) {
    this.isforemultcorp = isforemultcorp;
  }

  public void setPk_orgCode(String pkOrgCode) {
    this.pk_orgCode = pkOrgCode;
  }

  // private String getWherePart() {
  // StringBuffer where = new StringBuffer();
  // if (this.bUsedflag != null) {
  // where.append(" usedflag = '").append(this.bUsedflag.toString())
  // .append("' ");
  // }
  //
  // return where.toString();
  // }
}
