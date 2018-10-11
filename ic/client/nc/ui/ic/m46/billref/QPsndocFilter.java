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
 * <b>人员参照的过滤器：</b>
 * <ul>
 * <li>说明：如果组织唯一，则按照此组织过滤；否则集团可见
 * </ul>
 * <p>
 * <p>
 * 
 * @version 6.0
 * @since 6.0
 * @author hanbin
 * @time 2010-4-1 上午11:25:50
 */
public class QPsndocFilter extends CommonTwoLayerListener {

  // PsndocDefaultRefModel
  // 查询对话框
  // private SCMQueryConditionDlg subdlg;

  // 是否启用
  UFBoolean bUsedflag;

  // 组织字段
  private String pk_orgCode = "pk_org";

  // 人员
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
    // 注册编辑事件
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
   *          要设置的 bUsedflag
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
    // 如果主组织唯一，则按照主组织过滤；否则集团可见
    if (diffValues.size() == 1) {
      refPane.setPk_org(fatherValues.get(0).getSqlString());
      // 是否强制全组织显示
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
