package nc.ui.ic.general.action;

import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bs.framework.common.NCLocator;
import nc.bs.uif2.validation.IValidationService;
import nc.itf.pubapp.pub.exception.IResumeException;
import nc.itf.scmpub.reference.uap.group.SysInitGroupQuery;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.ic.general.model.ICGenBizEditorModel;
import nc.ui.ic.general.util.GenResumeExceptionHandle;
import nc.ui.ic.general.view.ICBizView;
import nc.ui.ic.pub.action.SaveAction;
import nc.ui.ic.pub.env.ICUIContext;
import nc.ui.ic.pub.model.ICBizModel;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pubapp.pub.common.context.PFlowContext;
import nc.ui.pubapp.pub.power.PowerSaveValidateService;
import nc.ui.pubapp.uif2app.AppUiState;
import nc.ui.pubapp.uif2app.actions.IActionExecutable;
import nc.ui.pubapp.uif2app.model.BillManageModel;
import nc.ui.pubapp.uif2app.validation.CompositeValidation;
import nc.ui.pubapp.uif2app.view.ShowUpableBillForm;
import nc.vo.ic.general.deal.ICBillValueSetter;
import nc.vo.ic.general.define.ICBillBodyVO;
import nc.vo.ic.general.define.ICBillVO;
import nc.vo.ic.general.util.ICLocationUtil;
import nc.vo.ic.location.ICLocationVO;
import nc.vo.ic.pub.define.BillOperator;
import nc.vo.ic.pub.define.ICPubMetaNameConst;
import nc.vo.ic.pub.pf.ICPFParameter;
import nc.vo.ic.pub.util.StringUtil;
import nc.vo.ic.pub.util.ValueCheckUtil;
import nc.vo.ic.sncode.SnCodeUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;

/**
 * <p>
 * <b>���涯����</b>
 * <ul>
 * <li>
 * </ul>
 * <p>
 * <p>
 * 
 * @version 6.0
 * @since 6.0
 * @author
 * @time 2010-8-27 ����09:08:51
 */
public class GeneralSaveAction extends SaveAction implements IActionExecutable {

  /**
   *
   */
  private static final long serialVersionUID = 2010082721520001L;

  @Override
  public void doBeforAction() {

	  
    super.doBeforAction();
    // ���˱������
    this.getEditorModel().getCardPanelWrapper().filterNullLine();

    // ��ǰ�ı�������
    int iRowCount =
        this.getEditorModel().getCardPanelWrapper().getBillCardPanel()
            .getRowCount();

    if (iRowCount <= 0) {
      ExceptionUtils
          .wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
              .getStrByID("4008001_0", "04008001-0031")/* @res "�ޱ�����" */);
    }
    try {
		this.checkSnDuplicate();
	} catch (Exception e) {
		// TODO �Զ����ɵ� catch ��
		e.printStackTrace();
	}
  }

  @Override
  public void setModel(BillManageModel model) {
    super.setModel(model);
    model.addAppEventListener(this);
  }

  /**
   * ����У����
   * 
   * @param validationService
   */
  @Override
  public void setValidationService(IValidationService validationService) {
    // У������Ȩ��У����
    if (validationService instanceof PowerSaveValidateService) {
      IValidationService validator = this.getValidationService();
      // �Ƿ��Ѿ��������У����
      if (validator instanceof CompositeValidation) {
        List<IValidationService> list =
            ((CompositeValidation) validator).getValidate();
        list.add(validationService);
        ((CompositeValidation) validator).setValidators(list);
      }
      else {
        validator = validationService;
      }
      super.setValidationService(validator);
    }
    // У���������У����
    else if (validationService instanceof CompositeValidation) {
      List<IValidationService> list =
          ((CompositeValidation) validationService).getValidate();
      if (this.getValidationService() != null) {
        list.add(this.getValidationService());
      }
      super.setValidationService(validationService);
    }
    else {// ����У������ʱ������
      super.setValidationService(validationService);
    }

  }

  @Override
public void doAction(ActionEvent e) throws Exception {
	
	  
	super.doAction(e);
}

private void afterSave() {
    this.getEditorModel().setTempBillPK(null);
    this.getEditorModel().clearAllBodyDetailData();
    ((ICGenBizEditorModel) this.getEditorModel()).getICBizModel()
        .clearLocationVOsAtSelectedRow();
    this.getModel().setAppUiState(AppUiState.NOT_EDIT);
    ((ICBizModel) this.getModel()).getIcUIContext().showStatusBarMessage(
        nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("common",
            "UCH005")/* @res "����ɹ�" */);
  }
  
  /**
   * У�鱾�β��������к��Ƿ��ظ�����У�����ݿ������ݣ�
 * @throws Exception 
   */
  private void checkSnDuplicate() throws Exception {
	
	  
	  
    ICBizView view = (ICBizView) this.getEditorModel().getICBizView();
    if (view == null) {
      return;
    }
    ICBillVO bill = (ICBillVO) this.getEditorModel().getICBizView().getValue();
    ICBillBodyVO[] bodys = bill.getBodys();
    Map<String, String> sns = new HashMap<String, String>();
    Map<String, Set<String>> dupsnRowNoMap = new LinkedHashMap<String, Set<String>>();
    // ���к�ģ���Ƿ����� �� true ����+��Ψһ ����Ψһ
    boolean isSNEndabled = SysInitGroupQuery.isSNEnabled();
    
    for (int i = 0; i < bodys.length; i++) {
      String rowno =
          this.getEditorModel().getCardPanelWrapper()
              .getBodyValueAt_String(i, ICPubMetaNameConst.CROWNO);
      ICLocationVO[] locs = this.getEditorModel().getBodyEditDetailData(i);
      if (ValueCheckUtil.isNullORZeroLength(locs)) {
        continue;
      }
      for (int j = 0; j < locs.length; j++) {
        String sn = locs[j].getVserialcode();
        if (StringUtil.isSEmptyOrNull(sn)) {
          continue;
        }
        String key = isSNEndabled ? bodys[i].getCmaterialvid() + sn : sn;
        
        if (sns.containsKey(key)) {
          if (dupsnRowNoMap.get(sn) == null) {
            Set<String> dupset = new HashSet<String>();
            dupset.add(sns.get(key));
            dupset.add(rowno);
            dupsnRowNoMap.put(sn, dupset);
          }
          else {
            dupsnRowNoMap.get(sn).add(sns.get(key));
            dupsnRowNoMap.get(sn).add(rowno);
          }
        }
        sns.put(key, rowno);
      }
    }
    if (ValueCheckUtil.isNullORZeroLength(dupsnRowNoMap)) {
      return;
    }
    this.showErrMsg(dupsnRowNoMap);
    
   
    
  }

  private void showAutoBalancedHint(Object[] retObj) {// ������ ��������ǰ��̨���л� �ֶζ�ʧ
    if (!(retObj instanceof ICBillVO[])) {
      return;
    }
    ICBillVO[] bills = (ICBillVO[]) retObj;
    for (ICBillVO bill : bills) {
      if (bill.getHead().getHasbalanced() == null
          || !bill.getHead().getHasbalanced().booleanValue()) {
        continue;
      }
      MessageDialog.showWarningDlg(this.getEditorModel().getContext()
          .getContainUI(), null, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
          .getStrByID("4008001_0", "04008001-0032")/* @res "���ݷ�����������" */);

    }

  }

  /**
   * ��ʾ������Ϣ
   * 
   * @param dupsnRowNoMap
   */
  private void showErrMsg(Map<String, Set<String>> dupsnRowNoMap) {
    StringBuilder errMsg = new StringBuilder();
    errMsg.append(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
        "4008001_0", "04008001-0033")/* @res "�������к��ظ���\n" */);
    for (Map.Entry<String, Set<String>> snent : dupsnRowNoMap.entrySet()) {
      StringBuffer rownostr = new StringBuffer();
      for (String rowno : snent.getValue()) {
        rownostr.append(rowno);
        rownostr.append(",");
      }
      rownostr.deleteCharAt(rownostr.length() - 1);
      errMsg.append(NCLangRes.getInstance().getStrByID("4008001_0",
          "04008001-0669", null, new String[] {
            snent.getKey(), rownostr.toString()
          })/* ���кţ�{0} �У�{1} */);
      errMsg.append("\n");
    }
    errMsg.deleteCharAt(errMsg.length() - 1);
    ExceptionUtils.wrappBusinessException(errMsg.toString());
  }

  /**
   *
   */
  @Override
  protected void fillUpContext(PFlowContext context) {
    ICPFParameter pfparam = (ICPFParameter) context.getUserObj();
    if (context.getUserObj() == null) {
      pfparam = new ICPFParameter();
    }
    if (this.getModel().getAppUiState() == AppUiState.ADD) {
      pfparam.setBillaction(BillOperator.New);
    }
    else if (this.getModel().getAppUiState() == AppUiState.EDIT) {
      pfparam.setBillaction(BillOperator.Edit);
    }
    context.setUserObj(pfparam);
    super.fillUpContext(context);
  }

  @Override
  protected boolean isResume(IResumeException resumeInfo) {
    boolean executed =
        new GenResumeExceptionHandle(this.getEditorModel(),
            this.getFlowContext()).isResume(resumeInfo);
    this.setExecuted(executed);
    return executed;
  }

  /**
   *
   */
  @Override
  protected Object[] processBefore(Object[] vos) {
	  IUAPQueryBS iQ=NCLocator.getInstance().lookup(IUAPQueryBS.class);
	  BillCardPanel pl = ((ShowUpableBillForm) this.editor).getBillCardPanel();
	  for(int i=0;i<pl.getBillModel().getRowCount();i++){
		  Object obj=pl.getBillModel().getValueAt(i, "csourcebillhid");
		  Object objnew=pl.getBillModel().getValueAt(i, "cgeneralbid");
		  if(obj==null || obj.equals("")){
			  continue;
		  }
		  String sql="select sum(nassistnum) from ic_finprodin_b where nvl(dr,0)=0 "
		  		+ "and csourcebillhid ='"+obj+"' and cgeneralbid<>'"+objnew+"' ";
	
			Object obj2;
			try {
				obj2 = iQ.executeQuery(sql, new ColumnProcessor());
				UFDouble ud=obj2==null?new UFDouble(0):new UFDouble(obj2.toString());
			Object obj3=pl.getBillModel().getValueAt(i, "nassistnum");
			UFDouble ud2=obj3==null?new UFDouble(0):new UFDouble(obj3.toString());
			String sql2="select ncheckastnum   from qc_reportbill where nvl(dr,0)=0 and pk_reportbill='"+obj+"'";
			Object obj4=iQ.executeQuery(sql2, new ColumnProcessor());
			UFDouble ud3=obj4==null?new UFDouble(0):new UFDouble(obj4.toString());
			if(ud3.compareTo(new UFDouble(0))<=0){
				continue;
			}
			 if(ud.add(ud2).compareTo(ud3)>0){
				 
				 MessageDialog.showHintDlg(pl, "��ʾ", "ʵ�������ܶ�����ʼ챨��������������飡");
				 return null;
			 }
			} catch (Exception e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
		
	  }
	  
	  
    ICBillVO icBillVO = null;
    ICUIContext context = ((ICBizModel) this.getModel()).getIcUIContext();
    ICBillValueSetter setter = new ICBillValueSetter();
    // ICBillEntityCheck check=new ICBillEntityCheck(false);
    for (Object vo : vos) {
      icBillVO = (ICBillVO) vo;
      try {
        setter.setBillInitData(icBillVO, context);
        // ���ñ���ĵ�Ʒ����
        ((ICGenBizEditorModel) this.getEditorModel())
            .setLocationVOToBodyForSave(icBillVO);
        icBillVO.setTempBillPK(this.getEditorModel().getTempBillPK());
        // ����ʵ�����ݼ��
        // delete ԭ��:��ʱ�ӱ������û�б���ֵ��Ϊnull�������ӱ��ֶ�һ�µ�У��ʱ��ͨ����
        // check.checkBill(icBillVO);
      }
      catch (Exception ex) {
        ExceptionUtils.wrappException(ex);
      }
    }
    return super.processBefore(vos);
  }

  @Override
  protected void processReturnObj(Object[] retObj) throws Exception {
	  
	  
	  
    super.processReturnObj(retObj);
    this.afterSave();
    this.showAutoBalancedHint(retObj);
  }

  @Override
  protected AbstractBill[] produceLightVO(AbstractBill[] newVO) {
    Map<Integer, Map<Integer, ICLocationVO[]>> beforeUpdatedVOMap =
        ICLocationUtil.getLocationVO((ICBillVO[]) newVO);
    AbstractBill[] lightVOs = super.produceLightVO(newVO);
    this.fillBatchInfoAfterLight(lightVOs, newVO);
    ICLocationUtil.setLocationVO((ICBillVO[]) lightVOs, beforeUpdatedVOMap);
    
    return (AbstractBill[]) lightVOs;
  }
  
  /**
   * ������������������������صļ�������
   * ���ڵ����ϵ����������ȼ����������ڣ�ʧЧ����
   * �����θı䣬�����ֶ�¼�������θı�ǰ��ֵ��ͬʱ��
   * ���ɵ����ε��������ֶ�û��ֵ
   * ����VO��ԭ��
   * <p>
   */
  private void fillBatchInfoAfterLight(AbstractBill[] lightVOs, AbstractBill[] newVO) {
    if (AppUiState.EDIT != this.model.getAppUiState()) {
      return;
    }

    Map<String, CircularlyAccessibleValueObject> newmap = getValueMap(newVO);
    if (ValueCheckUtil.isNullORZeroLength(newmap)) {
      return;
    }
    
    final String[] fillFields =
        new String[] {
          ICPubMetaNameConst.CQUALITYLEVELID, ICPubMetaNameConst.DPRODUCEDATE,
          ICPubMetaNameConst.DVALIDATE,ICPubMetaNameConst.CSNUNITID
        };

    try {
      for (AbstractBill lightVO : lightVOs) {
        fillBatchInfo(newmap, fillFields, lightVO);
      }

    }
    catch (BusinessException e) {
      ExceptionUtils.wrappException(e);
    }

  }

  private void fillBatchInfo(
      Map<String, CircularlyAccessibleValueObject> newmap,
      final String[] fillFields, AbstractBill lightVO) throws BusinessException {
    CircularlyAccessibleValueObject[] bodys = lightVO.getAllChildrenVO();
    if (ValueCheckUtil.isNullORZeroLength(bodys)) {
      return;
    }
    for (CircularlyAccessibleValueObject body : bodys) {
      String primarykey = body.getPrimaryKey();
      CircularlyAccessibleValueObject newbody = newmap.get(primarykey);
      if (StringUtil.isSEmptyOrNull(primarykey) || newbody == null) {
        continue;
      }
      for (String field : fillFields) {
        if (body.getAttributeValue(field) != null) {
          continue;
        }
        body.setAttributeValue(field, newbody.getAttributeValue(field));
      }
    }
  }

  private Map<String, CircularlyAccessibleValueObject> getValueMap(AbstractBill[] oldVOs) {
    Map<String, CircularlyAccessibleValueObject> oldmap =
        new HashMap<String, CircularlyAccessibleValueObject>();
    try {
      if (ValueCheckUtil.isNullORZeroLength(oldVOs)) {
        return null;
      }
      for (AbstractBill old : oldVOs) {
        CircularlyAccessibleValueObject[] bodys = old.getAllChildrenVO();
        for (CircularlyAccessibleValueObject body : bodys) {
          String primarykey = body.getPrimaryKey();
          if (StringUtil.isSEmptyOrNull(primarykey)) {
            continue;
          }
          oldmap.put(primarykey, body);
        }
      }
    }
    catch (BusinessException ex) {
      ExceptionUtils.wrappException(ex);
    }
    return oldmap;
  }

  private boolean isExecuted = true;

  @Override
  public boolean isExecuted() {
    return this.isExecuted;
  }

  public void setExecuted(boolean isExecuted) {
    this.isExecuted = isExecuted;
  }

}
