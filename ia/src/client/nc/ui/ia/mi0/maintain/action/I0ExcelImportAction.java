package nc.ui.ia.mi0.maintain.action;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Set;
import java.util.prefs.Preferences;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import nc.vo.ia.mi0.entity.I0BillVO;
import nc.vo.ia.mi0.entity.I0HeadVO;
import nc.vo.ia.mi0.entity.I0ItemVO;
import nc.vo.ia.pub.data.FieldConst;
import nc.vo.ia.pub.period.AccountPeriod;
import nc.vo.ia.pub.period.Calendar;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pubapp.AppContext;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.scmf.ic.mbatchcode.BatchcodeVO;
import nc.vo.scmpub.res.SCMActionCode;
import nc.itf.ia.mi0.II0Maintain;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.pubitf.ia.beginaccount.IBeginAccountQryService;
import nc.bs.framework.common.NCLocator;
import nc.ui.ia.bill.base.maintain.view.BaseBillForm;
import nc.ui.ia.bill.in.maintain.view.InListView;
import nc.ui.ia.mi0.maintain.pub.I0ImportProcessor;
import nc.ui.pub.beans.UIFileChooser;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pubapp.util.CardPanelValueUtils;
import nc.ui.scmpub.action.SCMActionInitializer;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.ui.uif2.UIState;
import nc.ui.uif2.model.AbstractAppModel;

/**
 * 期初入库单——Excel导入操作
 * 
 * @since 6.1
 * @version 2012-4-10 下午03:07:36
 * @author 谷允金
 */

public class I0ExcelImportAction extends NCAction {

  private static final String KEY_DEFAULT_DIR = "default";

  private static final long serialVersionUID = 7388654643278566070L;

  private static final String XLS_SUFFIX = ".xls";

  private UIFileChooser chooser;

  private BaseBillForm editor;

  private InListView list;

  private AbstractAppModel model;

  // 为了自动记住上次用户选择的文件目录(使用偏好)
  private Preferences preferences;

  public I0ExcelImportAction() {
    SCMActionInitializer.initializeAction(this, SCMActionCode.IA_EXCELIMPORT);
  }

  /**
   * 判断是否是xls文件
   * 
   * @param file
   * @return
   */
  static boolean isXLSFile(File file) {
    String name = file.getName().toLowerCase();
    return name.endsWith(I0ExcelImportAction.XLS_SUFFIX);
  }

  @Override
  public void doAction(ActionEvent e) {

    CardPanelValueUtils util =
        new CardPanelValueUtils(this.getEditor().getBillCardPanel());
    String pk_org = util.getHeadTailStringValue(FieldConst.PK_ORG);
    String pk_book = util.getHeadTailStringValue(FieldConst.PK_BOOK);
    if (pk_org == null && pk_book == null) {
      return;
    }
    // 如果成本域和所属账簿不为空，才能导入
    if (pk_org != null && pk_book != null) {
      try {
        boolean isBeginAccount =
            NCLocator.getInstance().lookup(IBeginAccountQryService.class)
                .isBeginAccount(pk_org, pk_book);
        if (isBeginAccount) {
          ExceptionUtils
              .wrappBusinessException(NCLangRes4VoTransl.getNCLangRes()
                  .getStrByID("2014004_0", "02014004-0123")/* @res "已经期初记账，无法导入期初数据！" */);
        }
      }
      catch (BusinessException e11) {
        ExceptionUtils.wrappBusinessException(NCLangRes4VoTransl.getNCLangRes()
            .getStrByID("2014004_0", "02014004-0000")/* @res "查询是否期初记账异常！" */);
      }
    }

    final JComponent parent = this.getModel().getContext().getEntranceUI();
    File selectedFile = this.getOpenExcelFile(parent);
    if (selectedFile == null) {
      return;
    }
    I0ImportProcessor processor = new I0ImportProcessor(this.editor);
    I0BillVO i0billvo = processor.importExcel(selectedFile);
    this.addBillData(i0billvo);
    II0Maintain maintain = NCLocator.getInstance().lookup(II0Maintain.class);
    I0BillVO[] i0BillVOs = null;
    try {
      i0BillVOs = maintain.insertI0(new I0BillVO[] {
        i0billvo
      });
    }
    catch (Exception ex) {
      ExceptionUtils.wrappException(ex);
    }
    Set<String> usedNames = i0billvo.getParentVO().usedAttributeNames();
    I0HeadVO headvo = i0billvo.getParentVO();
    if (i0BillVOs == null || i0BillVOs.length == 0) {
      return;
    }
    I0HeadVO i0HeadVO = i0BillVOs[0].getParentVO();
    for (String name : usedNames) {
      if (i0HeadVO.getAttributeValue(name) == null) {
        i0HeadVO.setAttributeValue(name, headvo.getAttributeValue(name));
      }
    }
    
    /**
     *插入批次号档案
     *by cwf
     */
    for(I0ItemVO bvo:i0billvo.getChildrenVO()){
		 try {
		 String code=bvo.getVbatchcode()==null?"":bvo.getVbatchcode().toString();
		 String cmcode=bvo.getCinventoryid()==null?"":bvo.getCinventoryid().toString();
		 String sql="select count(*) from scm_batchcode where nvl(dr,0)=0 and vbatchcode='"+code+"' and cmaterialvid='"+cmcode+"'";
		 IUAPQueryBS iQ=NCLocator.getInstance().lookup(IUAPQueryBS.class);
		 int a=(int) iQ.executeQuery(sql, new ColumnProcessor());
		 if(a>0){
			 continue;
		 }
			 BatchcodeVO vo=new BatchcodeVO();
		 vo.setVbatchcode(bvo.getVbatchcode());
		 vo.setCmaterialoid(bvo.getCinventoryvid());
		 vo.setCmaterialvid(bvo.getCinventoryid());
		vo.setVhashcode("123");
		vo.setBinqc(new UFBoolean(false));
		vo.setBseal(new UFBoolean(false));
		vo.setAttributeValue("dr", 0);
		vo.setPk_group(AppContext.getInstance().getPkGroup());
			HYPubBO_Client.insert(vo);
		} catch (Exception e1) {
			// TODO 自动生成的 catch 块
			e1.printStackTrace(); 
		}
	 }
    
    
    
    // this.getModel().initModel(i0BillVOs);
    this.getModel().setUiState(UIState.NOT_EDIT);
    // this.list.showMeUp();
    String msg =
        NCLangRes4VoTransl.getNCLangRes().getStrByID("2014004_0",
            "02014004-0100")/* @res "导入数据成功！" */;
    ShowStatusBarMsgUtil.showStatusBarMsg(msg, this.getModel().getContext());
  }

  public BaseBillForm getEditor() {
    return this.editor;
  }

  public InListView getList() {
    return this.list;
  }

  public AbstractAppModel getModel() {
    return this.model;
  }

  public void setEditor(BaseBillForm editor) {
    this.editor = editor;
  }

  public void setList(InListView list) {
    this.list = list;
  }

  public void setModel(AbstractAppModel model) {
    this.model = model;
    model.addAppEventListener(this);
  }

  @Override
  protected boolean isActionEnable() {

    boolean flag = false;
    flag = this.getModel().getUiState() == UIState.ADD;

    return flag;
  }

  private void addBillData(I0BillVO i0billvo) {
    BillCardPanel card = this.editor.getBillCardPanel();
    I0HeadVO head = i0billvo.getParentVO();
    head.setStatus(VOStatus.NEW);
    Set<String> usedNames = head.usedAttributeNames();

    for (String attrName : usedNames) {
      Object value = card.getHeadTailItem(attrName).getValueObject();
      if (value instanceof Boolean) {
        if (value == Boolean.TRUE) {
          value = UFBoolean.TRUE;
        }
        else if (value == Boolean.FALSE) {
          value = UFBoolean.FALSE;
        }
      }
      head.setAttributeValue(attrName, value);
    }

    String pk_org = head.getPk_org();
    String pk_book = head.getPk_book();
    Calendar cal = Calendar.getInstance(pk_org, pk_book);
    AccountPeriod startPeriod = cal.getStartPeriod();
    UFDate bizdate = startPeriod.getBeginDate().getDateBefore(1);

    for (I0ItemVO item : i0billvo.getChildrenVO()) {
      item.setDbizdate(bizdate);
      item.setDaccountdate(bizdate);
      item.setStatus(VOStatus.NEW);
    }

  }

  /**
   * 对弹出的选择框进行设置
   * 
   * @return
   */
  private UIFileChooser getFileChooser() {
    if (this.chooser == null) {
      this.chooser = new UIFileChooser();
      this.chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
      // 禁用"所有文件"选项
      this.chooser.setAcceptAllFileFilterUsed(false);
      // 新增xls文件选项
      try {
        this.chooser.addChoosableFileFilter(new FileFilter() {

          @Override
          public boolean accept(File f) {
            return f.isDirectory() || I0ExcelImportAction.isXLSFile(f);
          }

          @Override
          public String getDescription() {
            return NCLangRes4VoTransl.getNCLangRes().getStrByID("2014004_0",
                "02014004-0057")/*XLS文件*/;
          }
        });
      }
      catch (Exception ex) {
        ExceptionUtils.wrappException(ex);
      }
    }
    // 用户选择文件目录偏好
    String preferencesDir =
        this.getPreferences().get(I0ExcelImportAction.KEY_DEFAULT_DIR,
            System.getProperty("user.dir"));
    this.chooser.setCurrentDirectory(new File(preferencesDir));
    return this.chooser;
  }

  /**
   * 获取选择的文件
   * 
   * @param parent
   * @return
   */
  private File getOpenExcelFile(Container parent) {
    while (this.getFileChooser().showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
      File selectedFile = this.getFileChooser().getSelectedFile();
      if (selectedFile != null) {
        if (!I0ExcelImportAction.isXLSFile(selectedFile)) {
          ExceptionUtils.wrappBusinessException(NCLangRes4VoTransl
              .getNCLangRes().getStrByID("2014004_0", "02014004-0058")
          /*@res "目前只支持XLS格式文件的导入！"*/);
        }
        this.getPreferences().put(I0ExcelImportAction.KEY_DEFAULT_DIR,
            selectedFile.getParent());
        return selectedFile;
      }
    }
    return null;
  }

  /**
   * 用户选择目录偏好
   * 
   * @return
   */
  private Preferences getPreferences() {
    if (this.preferences == null) {
      this.preferences = Preferences.userNodeForPackage(this.getClass());
    }
    return this.preferences;
  }

}
