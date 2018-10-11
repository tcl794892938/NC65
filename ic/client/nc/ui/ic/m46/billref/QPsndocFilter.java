package nc.ui.ic.m46.billref;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import nc.vo.pub.lang.UFBoolean;
import nc.ui.bd.ref.model.PsndocDefaultNCRefModel;
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

/**
 * <p>
 * <b>��Ա���յĹ�������</b>
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
public class QPsndocFilter extends CommonTwoLayerListener {

  // PsndocDefaultRefModel
  // ��ѯ�Ի���
  // private SCMQueryConditionDlg subdlg;

  // �Ƿ�����
  UFBoolean bUsedflag;

  // ��֯�ֶ�
  private String pk_orgCode = "pk_org";

  // ��Ա
  private String psnCode;

  private UFBoolean isforemultcorp;

  public QPsndocFilter(QueryConditionDLG dlg, String psnCode) {
    this(new QueryConditionDLGDelegator(dlg), psnCode);
  }

  public QPsndocFilter(QueryConditionDLGDelegator dlg, String psnCode) {
    super(dlg);
    // this.subdlg = dlg;
    this.psnCode = psnCode;
  }

  public void addEditorListener() {
    this.setFatherPath(this.pk_orgCode);
    this.setChildPath(this.psnCode);
    // ע��༭�¼�
    this.qryCondDLGDelegator.registerCriteriaEditorListener(this);
  }

  public void filter() {
    // String wherePart = this.getWherePart();
    // if (StringUtils.isNotEmpty(wherePart)) {
    // this.qryCondDLGDelegator.setFilterRef(this.psnCode, wherePart);
    // }
    this.qryCondDLGDelegator.setRefFilter(this.psnCode, new IRefFilter() {

      @Override
      public void doFilter(UIRefPane refPane) {
        new FilterPsndocRefUtils(refPane)
            .filtByUsedFlag(QPsndocFilter.this.bUsedflag);
      }
    });
  }

  /**
   * @return bUsedflag
   */
  public UFBoolean getbUsedflag() {
    return this.bUsedflag;
  }

  /**
   * @param bUsedflag
   *          Ҫ���õ� bUsedflag
   */
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
    JComponent compoent = wrapper.getFieldValueElemEditorComponent();
    UIRefPane refPane = null;
    if (compoent instanceof UIRefPane) {
      refPane = (UIRefPane) compoent;
    }
    else if (compoent instanceof CompositeRefPanel) {
      refPane = ((CompositeRefPanel) compoent).getStdRefPane();
    }
    if (refPane == null) {
      return;
    }
    if (refPane.getRefModel() == null) {
      return;
    }
    // �������֯Ψһ����������֯���ˣ������ſɼ�
    if (diffValues.size() == 1) {
      refPane.setPk_org(fatherValues.get(0).getSqlString());
      // �Ƿ�ǿ��ȫ��֯��ʾ
      if (null == this.isforemultcorp) {
        refPane.setMultiCorpRef(false);
      }
      else {
        refPane.setMultiCorpRef(this.isforemultcorp.booleanValue());
      }
    }
    else {
      // refPane.setPk_org(null);
      refPane.setMultiCorpRef(true);
    }
    // String wherePart = this.getWherePart();
    // if (StringUtils.isNotEmpty(wherePart)) {
    // refPane.setWhereString(wherePart);
    // }
    PsndocDefaultNCRefModel model=(PsndocDefaultNCRefModel)refPane.getRefModel();
    String cuserid=ClientEnvironment.getInstance().getUser().getCuserid();
    model.setClassWherePart(model.getClassWherePart()+" and exists(select 1 from v_tcl_dept v where v.pk_dept=org_dept.pk_dept and cuserid='"+cuserid+"')");
 
    new FilterPsndocRefUtils(refPane).filtByUsedFlag(this.bUsedflag);
  }

  public void setForceMultiCorpRef(UFBoolean isforemultcorp) {
    this.isforemultcorp = isforemultcorp;
  }

  public void setPk_orgCode(String pkOrgCode) {
    this.pk_orgCode = pkOrgCode;
  }

  public void setPsnCode(String psnCode) {
    this.psnCode = psnCode;
  }

  // private String getWherePart() {
  // StringBuffer where = new StringBuffer();
  //
  // if (this.bUsedflag != null && this.bUsedflag.booleanValue()) {
  // if (where.length() > 0) {
  // where.append(" and ");
  // }
  // where.append(" enablestate = ").append(2);
  // }
  //
  // return where.toString();
  // }
}
