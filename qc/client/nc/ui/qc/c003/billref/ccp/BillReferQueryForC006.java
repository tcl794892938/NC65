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
 * ת����ѯ����--Ϊ��֤��
 * 
 * @since 6.0
 * @version 2011-2-23 ����09:10:37
 * @author hanbin
 */
public class BillReferQueryForC006 extends DefaultBillReferQuery {

  public BillReferQueryForC006(Container c, TemplateInfo info) {
    super(c, info);
  }

  private void setRefFilter(QueryConditionDLGDelegator qcd) {
    // ������֯��������
    new QMarterialFilter(qcd, ReportHeaderVO.PK_ORG,
        QCQueryTempletFieldCode.PK_SRCMATERIAL_CODE).addEditorListener();
    new QMarterialFilter(qcd, ReportHeaderVO.PK_ORG,
        QCQueryTempletFieldCode.PK_SRCMATERIAL_NAME).addEditorListener();

    // ���Ϸ���
    String classCode = QCQueryTempletFieldCode.PK_SRCMATERIAL_PK_MARBASCLASS;
    new QMarbasclassFilter(qcd, ReportHeaderVO.PK_ORG, classCode)
        .addEditorListener();
    
    // ���첿��
    QDeptFilter.createDeptFilterOfQC(qcd, ReportHeaderVO.PK_APPLYDEPT)
        .addEditorListener();
    // ������
    QPsndocFilter.createQPsndocFilterOfQC(qcd, ReportHeaderVO.PK_REPORTER)
        .addEditorListener();
    // ������
    QPsndocFilter.createQPsndocFilterOfQC(qcd, ReportHeaderVO.PK_APPLYER)
        .addEditorListener();

    // ���鲿��
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
	 
    // ���û��ѷ�����֯���й��˵���֯����
    dlgDelegator.registerNeedPermissionOrgFieldCode(ReportHeaderVO.PK_ORG);
    // ���õ����Ĳ��շ�Χ
    this.setRefFilter(dlgDelegator);
    // //��ȫ����ƽ̨�Ĳ�ѯģ���ϵͳ����������Ĭ��ֵ
    // this.setDefaultOrgValue(dlgDelegator);
    // ���ѯ��ʱ����Զ�ƴ������Ȩ�޵�����
    dlgDelegator.setPowerEnable(true);
  }


  
}
