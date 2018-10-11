package nc.ui.qc.c003.billref.ccp;

import java.awt.Container;

import nc.ui.pubapp.billref.src.DefaultBillReferQuery;
import nc.ui.pubapp.uif2app.query2.QueryConditionDLGDelegator;
import nc.ui.scmpub.query.refregion.QDeptFilter;
import nc.ui.scmpub.query.refregion.QMarbasclassFilter;
import nc.ui.scmpub.query.refregion.QMarterialFilter;
import nc.ui.scmpub.query.refregion.QPsndocFilter;
import nc.vo.qc.c003.entity.ReportHeaderVO;
import nc.vo.qc.pub.constant.QCQueryTempletFieldCode;
import nc.vo.querytemplate.TemplateInfo;
import nc.vo.relation.IBusiRoleConst;

/**
 * 转单查询服务--为质证书
 * 
 * @since 6.0
 * @version 2011-2-23 上午09:10:37
 * @author hanbin
 */
public class BillReferQueryForC006 extends DefaultBillReferQuery {

  public BillReferQueryForC006(Container c, TemplateInfo info) {
    super(c, info);
  }

  private void setRefFilter(QueryConditionDLGDelegator qcd) {
    // 按主组织过滤物料
    new QMarterialFilter(qcd, ReportHeaderVO.PK_ORG,
        QCQueryTempletFieldCode.PK_SRCMATERIAL_CODE).addEditorListener();
    new QMarterialFilter(qcd, ReportHeaderVO.PK_ORG,
        QCQueryTempletFieldCode.PK_SRCMATERIAL_NAME).addEditorListener();

    // 物料分类
    String classCode = QCQueryTempletFieldCode.PK_SRCMATERIAL_PK_MARBASCLASS;
    new QMarbasclassFilter(qcd, ReportHeaderVO.PK_ORG, classCode)
        .addEditorListener();
    
    // 报检部门
    QDeptFilter.createDeptFilterOfQC(qcd, ReportHeaderVO.PK_APPLYDEPT)
        .addEditorListener();
    // 报告人
    QPsndocFilter.createQPsndocFilterOfQC(qcd, ReportHeaderVO.PK_REPORTER)
        .addEditorListener();
    // 报检人
    QPsndocFilter.createQPsndocFilterOfQC(qcd, ReportHeaderVO.PK_APPLYER)
        .addEditorListener();

    // 检验部门
    QDeptFilter chkfilter =
        QDeptFilter.createDeptFilterOfQC(qcd, ReportHeaderVO.PK_CHKDEPT);
    chkfilter.addEditorListener();
  }

  // private void setDefaultOrgValue(QueryConditionDLGDelegator
  // condDLGDelegator) {
  // String pk_org = QueryDlgUtils.getDefaultOrgUnit();
  // if (StringUtils.isEmpty(pk_org)) {
  // return;
  // }
  // if (OrgUnitPubService.isTypeOf(pk_org, IOrgConst.QCCENTERTYPE)) {
  // condDLGDelegator.setDefaultValue(ApplyHeaderVO.PK_ORG, pk_org);
  // }
  // }

  @Override
  protected void initQueryConditionDLG(QueryConditionDLGDelegator dlgDelegator) {
	 
    // 按用户已分配组织进行过滤的组织条件
    dlgDelegator.registerNeedPermissionOrgFieldCode(ReportHeaderVO.PK_ORG);
    // 设置档案的参照范围
    this.setRefFilter(dlgDelegator);
    // //完全依赖平台的查询模板的系统函数来设置默认值
    // this.setDefaultOrgValue(dlgDelegator);
    // 点查询的时候会自动拼上数据权限的条件
    dlgDelegator.setPowerEnable(true);
  }


  
}
