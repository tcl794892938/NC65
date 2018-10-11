package nc.ui.gl.synvouchergl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.busibean.ISysInitQry;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.ui.gl.pubtools.PZRizhiDialog;
import nc.ui.gl.pubtools.XMLDocTools;
import nc.ui.gl.voucherdata.VoucherDataBridge;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.pub.tools.BannerDialog;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.fl.voucher.BillVoucherHVO2;
import nc.vo.gl.button.BillButtonNO;
import nc.vo.gl.pubparam.FileAbsPath;
import nc.vo.gl.pubvoucher.VoucherVO;
import nc.vo.gl.voucherquery.VoucherQueryConditionVO;
import nc.vo.gl.vouchertools.QueryElementVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pubapp.AppContext;
import nc.vo.trade.pub.HYBillVO;

public class MyEventHandler extends AbstractMyEventHandler {

	public MyEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}
	
	private static MyEventHandler myevent = null;
	
	public BannerDialog start;
	
	protected void onBoElse(int intBtn) throws Exception {
		super.onBoElse(intBtn);
		if (intBtn == BillButtonNO.RIZHI) {
			onLookLog();
		} else if (intBtn == BillButtonNO.SYN) {
			onSynVoucher();
		} 
	}
	
	/**
	 * ͬ��ƾ֤
	 * 
	 * @throws Exception
	 */
	public void onSynVoucher() throws Exception {
		
		MyThread tread=new MyThread(this);
		tread.start();
		
	}
	
	public BannerDialog getBannerDialog(boolean flag) {
		if (start == null||flag) {
			start = new BannerDialog(getBillUI());
			start.setName("Mtplan run");
			start.setStartText("����ͬ���У���ȴ�...");

		}
		return start;
	}

	public class MyThread extends Thread {

		public MyThread(MyEventHandler event) {
			myevent=event;
		};
		
		@SuppressWarnings("deprecation")
		public void run() {
			try {
				
				getBannerDialog(true).start();
				
				BillCardPanel bpanel = myevent.getBillCardPanelWrapper().getBillCardPanel();

				HYBillVO vo = (HYBillVO) myevent.getBufferData().getCurrentVO();
				if (null == vo) {
					MessageDialog.showHintDlg(bpanel, "��ʾ", "����ѡ�����ݣ�");
					return;
				}
				UIRefPane rep=(UIRefPane)bpanel.getHeadItem("yearmonth").getComponent();
				String yearMonth =rep.getRefCode();

				String url = (String) bpanel.getHeadItem("synurl").getValueObject();

				UIRefPane rep2=(UIRefPane)bpanel.getHeadItem("pk_corp").getComponent();
				String pk_corp=rep2.getRefPK();
				String ccorp = rep2.getRefCode();
				
				myevent.outXmlByYearMonthAndCorp(yearMonth, pk_corp, ccorp, url);

				// ͬ����д��ͬ����Ϣ,ˢ�±���
				BillVoucherHVO2 hvo = (BillVoucherHVO2) vo.getParentVO();
				hvo.setSynmaker(AppContext.getInstance().getPkUser());
				hvo.setSyndate(new UFDate());
				hvo.setIs_syn(new UFBoolean(true));
				HYPubBO_Client.update(hvo);

				myevent.onBoRefresh();
				getBillUI().showHintMessage("ͬ���ɹ���");
				
				//չʾ��־
				PZRizhiDialog rdlg = new PZRizhiDialog(bpanel);
				rdlg.showRizhiInfomation(yearMonth, bpanel, ccorp,FileAbsPath.SENDPATH2);
				
			} catch (Exception e) {
				MessageDialog.showHintDlg(myevent.getBillUI(), "��ʾ", e.getMessage());
			} finally {
				getBannerDialog(false).end();
			}
		}
	}

	/**
	 * �鿴��־
	 * 
	 * @throws Exception
	 */
	public void onLookLog() throws Exception {

		BillCardPanel bpanel = this.getBillCardPanelWrapper()
				.getBillCardPanel();

		HYBillVO vo = (HYBillVO) this.getBufferData().getCurrentVO();
		if (null == vo) {
			MessageDialog.showHintDlg(bpanel, "��ʾ", "����ѡ�����ݣ�");
			return;
		}
		UIRefPane rep=(UIRefPane)bpanel.getHeadItem("yearmonth").getComponent();
		String yearMonth =rep.getRefCode();

		UIRefPane rep2=(UIRefPane)bpanel.getHeadItem("pk_corp").getComponent();
		String ccorp = rep2.getRefCode();

		PZRizhiDialog rdlg = new PZRizhiDialog(bpanel);
		rdlg.showRizhiInfomation(yearMonth, bpanel, ccorp,FileAbsPath.SENDPATH2);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onBoSave() throws Exception {

		IUAPQueryBS iQ = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		// �ǿ�У��
		BillCardPanel cpanel = this.getBillCardPanelWrapper()
				.getBillCardPanel();
		cpanel.dataNotNullValidate();

		// ����ڼ䲻���ظ�
		String yearMonth = (String) cpanel.getHeadItem("yearmonth")
				.getValueObject();
		String pk_corp = (String) cpanel.getHeadItem("pk_corp")
				.getValueObject();
		Object obj = cpanel.getHeadItem("pk_voucher_h").getValueObject();
		
		String sql = "select yearmonth from fl_voucher_h2 where nvl(dr,0)=0 "
				+ "and yearmonth='" + yearMonth + "' and pk_voucher_h<>'"+obj+ "' and pk_corp='"+ pk_corp + "'";
		
		ArrayList<String> obj_year = (ArrayList<String>) iQ.executeQuery(sql,new ColumnListProcessor());
		if (obj_year != null && obj_year.size() > 0) {
			MessageDialog.showHintDlg(cpanel, "��ʾ", "��ҵ��Ԫ����ڼ��Ѵ��ڣ�������ѡ��");
			cpanel.setHeadItem("yearmonth", null);
			return;
		}

		super.onBoSave();
	}

	/**
	 * ����xml�ļ�
	 */
	private void outXmlByYearMonthAndCorp(String yearMonth, String pk_corp,
			String ccorp, String url) throws Exception {

		VoucherQueryConditionVO vo = new VoucherQueryConditionVO();
		QueryElementVO[] qevos = new QueryElementVO[3];
		for (int i = 0; i < qevos.length; i++) {
			QueryElementVO vob = new QueryElementVO();
			qevos[i] = vob;
		}
		// �����˱�
		qevos[0].setDatas(new String[] { pk_corp });
		qevos[0].setDatatype("String");
		qevos[0].setIsAnd(new UFBoolean(true));
		qevos[0].setOperator("=");
		qevos[0].setRestrictfield("gl_voucher.pk_org");

		// ��ʼ����
		String month = yearMonth.substring(5);
		qevos[1].setDatas(new String[] { month });
		qevos[1].setDatatype("String");
		qevos[1].setIsAnd(new UFBoolean(true));
		qevos[1].setOperator("=");
		qevos[1].setRestrictfield("gl_voucher.period");

		String year = yearMonth.substring(0, 4);
		qevos[2].setDatas(new String[] { year });
		qevos[2].setDatatype("String");
		qevos[2].setIsAnd(new UFBoolean(true));
		qevos[2].setOperator("=");
		qevos[2].setRestrictfield("gl_voucher.year");

		vo.setNormalconditionvo(qevos);

		VoucherQueryConditionVO[] vqcvo = new VoucherQueryConditionVO[] { vo };
		VoucherVO[] vos = null;
		try {
			vos = VoucherDataBridge.getInstance().queryByConditionVO(vqcvo,
					new Boolean(true));
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}

		if (vos == null || vos.length <= 0) {
			throw new BusinessException("��ǰ����ڼ�δ��ѯ��ƾ֤����˲飡");
		}

		// ����ƾ֤У��
		this.checkVoucherIsManager(vos);
		// ��������У��
		String param = this.getParaUrl(pk_corp, FileAbsPath.PARAM_CORP);
		if ("".equals(param)) {
			throw new BusinessException("�������ù�˾��������");
		}
		if (param.indexOf("����") != -1) {
			for (VoucherVO vvo : vos) {
				vvo.setPk_checked(null);
			}
		} else if (param.indexOf("�Ƶ�") != -1) {
			for (VoucherVO vvo : vos) {
				vvo.setPk_checked(null);
				vvo.setPk_manager("N/A");
				vvo.setTallydate(null);
			}
		}

		ArrayList<VoucherVO> vlist = new ArrayList<VoucherVO>();
		XMLDocTools xmltools=new XMLDocTools();
		
		String returnstr="";
		for (int i = 0; i < vos.length; i++) {
			vlist.add(vos[i]);
			if (vlist.size() == 800) {
				VoucherVO[] sendVos = vlist.toArray(new VoucherVO[0]);
				try {
					returnstr+=xmltools.outputXMLDoc(sendVos, ccorp,url,FileAbsPath.send_account2);
				} catch (Exception e) {
				}
				vlist.clear();
			}
		}

		if (vlist.size() != 0) {
			VoucherVO[] sendVos = vlist.toArray(new VoucherVO[0]);
			try {
				returnstr+=xmltools.outputXMLDoc(sendVos, ccorp,url,FileAbsPath.send_account2);
			} catch (Exception e) {
			}
			vlist.clear();
		}
		
		//�Է��ؽ���������
		Map<String, String> map=new HashMap<String,String>();
		for(VoucherVO vonew : vos){
			map.put(vonew.getPk_voucher(), vonew.getPk_voucher()+"[ƾ֤��"+vonew.getNo()+"]");
		}
		
		if(!"".equals(returnstr)){
			
			 String textContest="";
			 try {
				 ByteArrayInputStream inStream = new ByteArrayInputStream(returnstr.getBytes("UTF-8"));
				 BufferedReader reader = new BufferedReader(new InputStreamReader(inStream,"UTF-8"));  
				 String line = null;
				 boolean flag=false;
				 boolean end=false;
				 String tstr1="";
				 String tstr2="";
				 String tstr3="";
				 int k=0;// 3����Ԫ��
				 int g=0;
				 while ((line = reader.readLine()) != null){
						if(line.indexOf("<sendresult>")!=-1){
							tstr1="";
						}else if(line.indexOf("<resultcode>1</resultcode>")!=-1){
							g++;
							flag=true;
						}else if(line.indexOf("<bdocid>")!=-1){//ƴ��ƾ֤��
							k=1;
						}else if(line.indexOf("ƾ֤�Ѿ������")!=-1){
							flag=true;
						}else if(line.indexOf("</sendresult>")!=-1){
							end=true;
						}
						
						// 3����ƴװ
						if(k==0){
							tstr1+=line + "\n";
						}else if(k==1){
							++k;
							tstr2+=line + "\n";
						}else if(k==2){
							tstr3+=line + "\n";
						}
						
						if(end){
							if(!flag){
								String str="<bdocid>";
								int it=tstr2.indexOf(str);
								String pk=tstr2.substring(it+str.length(),it+str.length()+20);
								String billno=map.get(pk);
								tstr2=tstr2.replace(pk, billno);
								
								textContest += tstr1+tstr2+tstr3;
							}
							k=0;
							tstr1="";
							tstr2="";
							tstr3="";
							flag=false;
							end=false;
						}
					}
				 textContest="����ͬ��ƾ֤�ɹ�"+g+"����ʧ��"+(vos.length-g)+"����\n"+textContest;
				 reader.close();
				 inStream.close();
			} catch (Exception e) {
				throw new BusinessException(e.getMessage());
			}
			
			 returnstr=textContest;
		}
		
		try {
			byte [] buff=new byte[]{};
			String filename=ccorp+"-"+yearMonth+".txt";
			FileOutputStream output=new FileOutputStream(FileAbsPath.SENDPATH2+filename);
	        buff=returnstr.getBytes("UTF-8");  
	        output.write(buff, 0, buff.length); 
	        output.close();
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		
	}


	/**
	 * ����ƾ֤У��
	 */
	private void checkVoucherIsManager(VoucherVO[] vos)
			throws BusinessException {

		String str = "";
		for (int i = 0; i < vos.length; i++) {
			if (vos[i].getPk_manager() == null
					|| vos[i].getPk_manager().equals("N/A")) {
				str += "��" + vos[i].getNo() + "��";
			}
		}
		if (!"".equals(str)) {
			throw new BusinessException("�û���ڼ�ƾ֤��Ϊ" + str + "��ƾ֤��δ���ˣ���˲飡");
		}
	}

	// ������Ϣ
	private String getParaUrl(String pk_org, String initCode)
			throws BusinessException {
		ISysInitQry pfsys = (ISysInitQry) NCLocator.getInstance().lookup(
				ISysInitQry.class.getName());
		String strpara = pfsys.getParaString(pk_org, initCode) == null ? ""
				: pfsys.getParaString(pk_org, initCode);

		return strpara;
	}

	@Override
	protected void onBoCard() throws Exception {
		super.onBoCard();
		getBillCardPanelWrapper().getBillCardPanel().setEnabled(false);
	}

	@Override
	protected void onBoCancel() throws Exception {
		super.onBoCancel();
		getBillCardPanelWrapper().getBillCardPanel().setEnabled(false);
	}

	@Override
	protected void onBoCopy() throws Exception {
		super.onBoCopy();
		getBillCardPanelWrapper().getBillCardPanel().setHeadItem("synmaker", null);
		getBillCardPanelWrapper().getBillCardPanel().setHeadItem("syndate", null);
		getBillCardPanelWrapper().getBillCardPanel().setHeadItem("is_syn", new UFBoolean(false));
	}



	@Override
	protected void onBoDelete() throws Exception {
		

		// ����û�����ݻ��������ݵ���û��ѡ���κ���
		if (getBufferData().getCurrentVO() == null)
			return;

		if (MessageDialog.showOkCancelDlg(getBillUI(), NCLangRes.getInstance().getStrByID("uifactory",
				"UPPuifactory-000064")/* @res "����ɾ��" */, NCLangRes.getInstance().getStrByID("uifactory",
				"UPPuifactory-000065")/* @res "�Ƿ�ȷ��ɾ���û�������?" */
		, MessageDialog.ID_CANCEL) != UIDialog.ID_OK)
			return;

		AggregatedValueObject modelVo = getBufferData().getCurrentVO();
		
		Object objpk=modelVo.getParentVO().getAttributeValue("pk_voucher_h");
		HYPubBO_Client.deleteByWhereClause(BillVoucherHVO2.class, " pk_voucher_h='"+objpk+"'");
		
			getBillUI().removeListHeadData(getBufferData().getCurrentRow());
			if (getUIController() instanceof ISingleController) {
				ISingleController sctl = (ISingleController) getUIController();
				if (!sctl.isSingleDetail())
					getBufferData().removeCurrentRow();
			} else {
				getBufferData().removeCurrentRow();
			}

		
		if (getBufferData().getVOBufferSize() == 0)
			getBillUI().setBillOperate(IBillOperate.OP_INIT);
		else
			getBillUI().setBillOperate(IBillOperate.OP_NOTEDIT);
		getBufferData().setCurrentRow(getBufferData().getCurrentRow());
	
	}
	
	

}