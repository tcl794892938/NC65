package nc.ui.gl.synbasegl;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.border.TitledBorder;

import nc.bs.framework.common.NCLocator;
import nc.itf.gl.synbase.ISynBaseMaintain2;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.ui.gl.synbase.BaseDocType;
import nc.ui.gl.synbase.RizhiDialog;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITextArea;
import nc.ui.pub.tools.BannerDialog;
import nc.vo.fl.voucher.TempBaseDocVO2;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDateTime;


@SuppressWarnings("deprecation")
public class DataBackImportUI extends ToftPanel implements ActionListener {
	
	private static final long serialVersionUID = -2617231086637759567L;

	@Override
	public String getTitle() {
		return "���ݵ������";//�ݲ���
	}

	protected void postInit(){
		UIScrollPane scrollPnl = new UIScrollPane();
		scrollPnl.setPreferredSize(new Dimension(1024,1024));
		scrollPnl.setViewportView(getContentPanel());
		add(scrollPnl,"Center");
		
		String str=this.setDefaultData();
		if(!"".equals(str)){
			MessageDialog.showHintDlg(this, "��ʾ", str);
			this.init(null);
		}
	}
	
	@Override
	public ButtonObject[] getButtons() {
		return new ButtonObject[]{m_synAll,m_logInfo};
	}
	
	private String setDefaultData(){
		
		String str="";
		String sql="select * from temp_ts2 where temp_id='tcl' ";
		IUAPQueryBS iQ=NCLocator.getInstance().lookup(IUAPQueryBS.class);
		
		try {
			vo=(TempBaseDocVO2)iQ.executeQuery(sql, new BeanProcessor(TempBaseDocVO2.class));
		} catch (BusinessException e) {
			e.getMessage();
		}
		if(vo==null){
			return "��ʼ��ʱ���쳣������ϵ������Ա��";
		}
		
		lbdept.setText(vo.getDept_ts());
		lbpsn.setText(vo.getPsn_ts());
		lbcust.setText(vo.getCust_ts());
		lbmate.setText(vo.getMaterial_ts());
		lbacc.setText(vo.getAccount_ts());
		lbzdy.setText(vo.getZdy_ts());
		//lbsubj.setText(vo.getSubj_ts());
		return str;
	}
	
	private void refreshNewData(){
		
		String sql="select * from temp_ts2 where temp_id='tcl' ";
		IUAPQueryBS iQ=NCLocator.getInstance().lookup(IUAPQueryBS.class);
		
		try {
			vo=(TempBaseDocVO2)iQ.executeQuery(sql, new BeanProcessor(TempBaseDocVO2.class));
		} catch (BusinessException e) {
			e.getMessage();
		}
		
		lbdept.setText(vo.getDept_ts());
		lbpsn.setText(vo.getPsn_ts());
		lbcust.setText(vo.getCust_ts());
		lbmate.setText(vo.getMaterial_ts());
		lbacc.setText(vo.getAccount_ts());
		lbzdy.setText(vo.getZdy_ts());
		//lbsubj.setText(vo.getSubj_ts());
	}
	
	/**
	 * �ӿ�
	 */
	private ISynBaseMaintain2 getService(){
		if(isbm==null){
			isbm=NCLocator.getInstance().lookup(ISynBaseMaintain2.class);
		}
		return isbm;
	}

	protected ButtonObject m_synAll = new ButtonObject("һ��ͬ��", "ͬ�����е���", 0, "synall");
	protected ButtonObject m_logInfo = new ButtonObject("�鿴��־", "�鿴ͬ����־", 0, "loginfo");
	
	private ISynBaseMaintain2 isbm=null;
	private TempBaseDocVO2 vo=null;
	private String newts="";
	
	private int btnWidth = 60;
	private int btnHeight = 30;
	
	//�����
	protected UIPanel m_mainPnl;
	//��ť���
	protected UIPanel m_butPnl;
	//���ż��û�����
	protected UIPanel m_deptPnl;
	//��Ա����
	protected UIPanel m_psnPnl;
	//���̵���
	protected UIPanel m_custPnl;
	//���Ϸ��༰����
	protected UIPanel m_materialPnl;
	//�����˻�������
	protected UIPanel m_accountPnl;
	//�Զ��嵵��
	protected UIPanel m_zdyPnl;
	//��Ŀ��
	//protected UIPanel m_subjPnl;
	
	protected UIButton m_deptBtn;
	protected UIButton m_psnBtn;
	protected UIButton m_custBtn;
	protected UIButton m_mateBtn;
	protected UIButton m_accBtn;
	protected UIButton m_zdyBtn;
	//protected UIButton m_subjBtn;
	
	protected UILabel lbdept;
	protected UILabel lbpsn;
	protected UILabel lbcust;
	protected UILabel lbmate;
	protected UILabel lbacc;
	protected UILabel lbzdy;
	//protected UILabel lbsubj;
	
	protected UITextArea m_instructionTxtArea;
	
	/**
	 * �����
	 * @return
	 */
	protected UIPanel getContentPanel(){
		if(m_mainPnl == null){
			m_mainPnl = new UIPanel();
			m_mainPnl.setLayout(new BorderLayout());
			m_mainPnl.add(getButAllPanel(),BorderLayout.CENTER);
			m_mainPnl.add(getInstructionTxtArea(),BorderLayout.SOUTH);
		}
		return m_mainPnl;
	}
	
	/**
	 * ���а�ť���
	 * @return
	 */
	protected UIPanel getButAllPanel(){
		if(m_butPnl == null){
			m_butPnl = new UIPanel();
			m_butPnl.setName("��ť���");
			m_butPnl.setLayout(new BoxLayout(m_butPnl,3));
			m_butPnl.setBorder(new TitledBorder(""));//�������޷ָ�
			m_butPnl.add(getDeptPanel());
			m_butPnl.add(getPsnPanel());
			m_butPnl.add(getCustPanel());
			m_butPnl.add(getMaterialPanel());
			m_butPnl.add(getAccountPanel());
			m_butPnl.add(getZdyPanel());
			//m_butPnl.add(getSubjPanel());
		}
		return m_butPnl;
	}
	
	/**
	 * ����panel
	 * @return
	 */
	protected UIPanel getDeptPanel(){
		if(m_deptPnl == null){
			m_deptPnl = new UIPanel();
			FlowLayout layout = new FlowLayout();
			layout.setAlignment(FlowLayout.LEFT);
			m_deptPnl.setLayout(layout);
			m_deptPnl.setBorder(new TitledBorder("���ż��û�����"));
			
			UILabel lab=new UILabel();
			lab.setText("�ϴ�ͬ��ʱ��:");
			lbdept=new UILabel();
			lbdept.setPreferredSize(new Dimension(135,btnHeight));
			
			m_deptPnl.add(lab);
			m_deptPnl.add(lbdept);
			m_deptPnl.add(getDeptBtn());
		}
		return m_deptPnl;
	}
	
	/**
	 * ��Աpanel
	 * @return
	 */
	protected UIPanel getPsnPanel(){
		if(m_psnPnl == null){
			m_psnPnl = new UIPanel();
			FlowLayout layout = new FlowLayout();
			layout.setAlignment(FlowLayout.LEFT);
			m_psnPnl.setLayout(layout);
			m_psnPnl.setBorder(new TitledBorder("��Ա����"));
			
			UILabel lab=new UILabel();
			lab.setText("�ϴ�ͬ��ʱ��:");
			lbpsn=new UILabel();
			lbpsn.setPreferredSize(new Dimension(135,btnHeight));
			
			m_psnPnl.add(lab);
			m_psnPnl.add(lbpsn);
			m_psnPnl.add(getPsnBtn());
		}
		return m_psnPnl;
	}
	
	/**
	 * ����panel
	 * @return
	 */
	protected UIPanel getCustPanel(){
		if(m_custPnl == null){
			m_custPnl = new UIPanel();
			FlowLayout layout = new FlowLayout();
			layout.setAlignment(FlowLayout.LEFT);
			m_custPnl.setLayout(layout);
			m_custPnl.setBorder(new TitledBorder("���̵���"));
			
			UILabel lab=new UILabel();
			lab.setText("�ϴ�ͬ��ʱ��:");
			lbcust=new UILabel();
			lbcust.setPreferredSize(new Dimension(135,btnHeight));
			
			m_custPnl.add(lab);
			m_custPnl.add(lbcust);
			m_custPnl.add(getCustBtn());
		}
		return m_custPnl;
	}
	
	/**
	 * ����panel
	 * @return
	 */
	protected UIPanel getMaterialPanel(){
		if(m_materialPnl == null){
			m_materialPnl = new UIPanel();
			FlowLayout layout = new FlowLayout();
			layout.setAlignment(FlowLayout.LEFT);
			m_materialPnl.setLayout(layout);
			m_materialPnl.setBorder(new TitledBorder("���Ϸ��༰����"));
			
			UILabel lab=new UILabel();
			lab.setText("�ϴ�ͬ��ʱ��:");
			lbmate=new UILabel();
			lbmate.setPreferredSize(new Dimension(135,btnHeight));
			
			m_materialPnl.add(lab);
			m_materialPnl.add(lbmate);
			m_materialPnl.add(getMateBtn());
		}
		return m_materialPnl;
	}
	
	/**
	 * ����panel
	 * @return
	 */
	protected UIPanel getAccountPanel(){
		if(m_accountPnl == null){
			m_accountPnl = new UIPanel();
			FlowLayout layout = new FlowLayout();
			layout.setAlignment(FlowLayout.LEFT);
			m_accountPnl.setLayout(layout);
			m_accountPnl.setBorder(new TitledBorder("�����˻�������"));
			
			UILabel lab=new UILabel();
			lab.setText("�ϴ�ͬ��ʱ��:");
			lbacc=new UILabel();
			lbacc.setPreferredSize(new Dimension(135,btnHeight));
			
			m_accountPnl.add(lab);
			m_accountPnl.add(lbacc);
			m_accountPnl.add(getAccBtn());
		}
		return m_accountPnl;
	}
	
	/**
	 * �Զ���panel
	 * @return
	 */
	protected UIPanel getZdyPanel(){
		if(m_zdyPnl == null){
			m_zdyPnl = new UIPanel();
			FlowLayout layout = new FlowLayout();
			layout.setAlignment(FlowLayout.LEFT);
			m_zdyPnl.setLayout(layout);
			m_zdyPnl.setBorder(new TitledBorder("�Զ��嵵��"));
			
			UILabel lab=new UILabel();
			lab.setText("�ϴ�ͬ��ʱ��:");
			lbzdy=new UILabel();
			lbzdy.setPreferredSize(new Dimension(135,btnHeight));
			
			m_zdyPnl.add(lab);
			m_zdyPnl.add(lbzdy);
			m_zdyPnl.add(getZdyBtn());
		}
		return m_zdyPnl;
	}
	
	/**
	 * ��Ŀpanel
	 * @return
	 */
	/*
	protected UIPanel getSubjPanel(){
		if(m_subjPnl == null){
			m_subjPnl = new UIPanel();
			FlowLayout layout = new FlowLayout();
			layout.setAlignment(FlowLayout.LEFT);
			m_subjPnl.setLayout(layout);
			m_subjPnl.setBorder(new TitledBorder("��Ŀ��"));
			
			UILabel lab=new UILabel();
			lab.setText("�ϴ�ͬ��ʱ��:");
			lbsubj=new UILabel();
			lbsubj.setPreferredSize(new Dimension(135,btnHeight));
			
			m_subjPnl.add(lab);
			m_subjPnl.add(lbsubj);
			m_subjPnl.add(getSubjBtn());
		}
		return m_subjPnl;
	}*/
	

	protected UIButton getDeptBtn(){
		if(m_deptBtn == null){
			m_deptBtn = new UIButton();
			m_deptBtn.setText("ͬ��");
			m_deptBtn.setName("syndept");
			m_deptBtn.setPreferredSize(new Dimension(btnWidth,btnHeight));
			m_deptBtn.addActionListener(this);
		}
		return m_deptBtn;
	}
	
	protected UIButton getPsnBtn(){
		if(m_psnBtn == null){
			m_psnBtn = new UIButton();
			m_psnBtn.setText("ͬ��");
			m_psnBtn.setName("synpsn");
			m_psnBtn.setPreferredSize(new Dimension(btnWidth,btnHeight));
			m_psnBtn.addActionListener(this);
		}
		return m_psnBtn;
	}
	
	protected UIButton getCustBtn(){
		if(m_custBtn == null){
			m_custBtn = new UIButton();
			m_custBtn.setText("ͬ��");
			m_custBtn.setName("syncust");
			m_custBtn.setPreferredSize(new Dimension(btnWidth,btnHeight));
			m_custBtn.addActionListener(this);
		}
		return m_custBtn;
	}
	
	protected UIButton getMateBtn(){
		if(m_mateBtn == null){
			m_mateBtn = new UIButton();
			m_mateBtn.setText("ͬ��");
			m_mateBtn.setName("synmate");
			m_mateBtn.setPreferredSize(new Dimension(btnWidth,btnHeight));
			m_mateBtn.addActionListener(this);
		}
		return m_mateBtn;
	}
	
	protected UIButton getAccBtn(){
		if(m_accBtn == null){
			m_accBtn = new UIButton();
			m_accBtn.setText("ͬ��");
			m_accBtn.setName("synacc");
			m_accBtn.setPreferredSize(new Dimension(btnWidth,btnHeight));
			m_accBtn.addActionListener(this);
		}
		return m_accBtn;
	}
	
	protected UIButton getZdyBtn(){
		if(m_zdyBtn == null){
			m_zdyBtn = new UIButton();
			m_zdyBtn.setText("ͬ��");
			m_zdyBtn.setName("synzdy");
			m_zdyBtn.setPreferredSize(new Dimension(btnWidth,btnHeight));
			m_zdyBtn.addActionListener(this);
		}
		return m_zdyBtn;
	}
	
	/*
	protected UIButton getSubjBtn(){
		if(m_subjBtn == null){
			m_subjBtn = new UIButton();
			m_subjBtn.setText("ͬ��");
			m_subjBtn.setName("synsubj");
			m_subjBtn.setPreferredSize(new Dimension(btnWidth,btnHeight));
			m_subjBtn.addActionListener(this);
		}
		return m_subjBtn;
	}*/
	
	
	String INSTRUCTION = "" +
			"˵����\n"+
			"1���ù�����ͬ���������ݴӴ�½���׽���������׶���Ƶġ�\n"+
			"2������ͬ������ÿ��һ�����Σ�ƾ֤ͬ��ǰ�����Ƚ��е���ͬ����\n"+
			"3����־�ļ����������һ�ε�ͬ�������\n"+
			"4������ͬ��ֻͬ���ͻ�����Ӧ�����ֹ��������ɡ�\n"+
			"5��ͬ�������������⼰ʱ��ϵʵʩ��Ա������÷  �绰��138-1543-7119��\n"
			;
	
	protected UITextArea getInstructionTxtArea(){
		if(m_instructionTxtArea == null){
			m_instructionTxtArea = new UITextArea();
			m_instructionTxtArea.setText(INSTRUCTION);
			m_instructionTxtArea.setEditable(false);
		}
		return m_instructionTxtArea;
	}
	
	public DataBackImportUI dlg;
	
	public BannerDialog start;

	public BannerDialog getBannerDialog(boolean flag) {
		if (start == null||flag) {
			start = new BannerDialog(this);
			start.setName("Mtplan run");
			start.setStartText("����ͬ���У���ȴ�...");

		}
		return start;
	}

	public class MyThread extends Thread {

		public MyThread(DataBackImportUI ui) {
			dlg=ui;
		};
		
		public void run() {
			try {
				getBannerDialog(true).start();
				
				onBoSynDept(false);
				onBoSynPsn(false);
				onBoSynCust(false);
				onBoSynMate(false);
				onBoSynAcc(false);
				onBoSynZdy(false);
				onBoSynSubj(false);
				dlg.doLogInfo(true);
				
			} catch (Exception e) {
				MessageDialog.showHintDlg(dlg, "��ʾ", e.getMessage());
			} finally {
				getBannerDialog(false).end();
			}
		}
	}

	public void actionPerformed(ActionEvent e){
		// ��ť
		if (e.getSource() instanceof UIButton) {
			
			UIButton uibtn = (UIButton) e.getSource();
			String bnName = uibtn.getName();
			
			if ("syndept".equals(bnName)) {
				onBoSynDept(true);
			}else if("synpsn".equals(bnName)){
				onBoSynPsn(true);
			}else if("syncust".equals(bnName)){
				onBoSynCust(true);
			}else if("synmate".equals(bnName)){
				onBoSynMate(true);
			}else if("synacc".equals(bnName)){
				onBoSynAcc(true);
			}else if("synzdy".equals(bnName)){
				onBoSynZdy(true);
			}else if("synsubj".equals(bnName)){
				onBoSynSubj(true);
			}
		}
	}
	
	protected void onBoSynDept(boolean flag){//flagΪ�Ƿ���ʾ��־
		
		//ͬ�������û��߼�
		if(flag){
			newts=new UFDateTime().toString();
		}
		String oldts=vo.getDept_ts();
		
		try {
			getService().synBasedoc(oldts, newts, BaseDocType.syn_dept,vo);
		} catch (BusinessException e) {
		}
		
		if(flag){
			this.setDataAndShowLogInfo(BaseDocType.syn_dept);
		}
	}
	
	protected void onBoSynPsn(boolean flag){//flagΪ�Ƿ���ʾ��־
		
		//ͬ����Ա�߼�
		if(flag){
			newts=new UFDateTime().toString();
		}
		String oldts=vo.getPsn_ts();
		
		try {
			getService().synBasedoc(oldts, newts, BaseDocType.syn_psn,vo);
		} catch (BusinessException e) {
		}
		
		if(flag){
			this.setDataAndShowLogInfo(BaseDocType.syn_psn);
		}
	}
	
	protected void onBoSynCust(boolean flag){//flagΪ�Ƿ���ʾ��־
		
		//ͬ�������߼�
		if(flag){
			newts=new UFDateTime().toString();
		}
		String oldts=vo.getCust_ts();
		
		try {
			getService().synBasedoc(oldts, newts, BaseDocType.syn_cust,vo);
		} catch (BusinessException e) {
		}
		
		if(flag){
			this.setDataAndShowLogInfo(BaseDocType.syn_cust);
		}
	}
	
	protected void onBoSynMate(boolean flag){//flagΪ�Ƿ���ʾ��־
		
		//ͬ�������߼�
		if(flag){
			newts=new UFDateTime().toString();
		}
		String oldts=vo.getMaterial_ts();
		
		try {
			getService().synBasedoc(oldts, newts, BaseDocType.syn_material,vo);
		} catch (BusinessException e) {
		}
		
		if(flag){
			this.setDataAndShowLogInfo(BaseDocType.syn_material);
		}
	}
	
	protected void onBoSynAcc(boolean flag){//flagΪ�Ƿ���ʾ��־
		
		//ͬ�������˻��߼�
		if(flag){
			newts=new UFDateTime().toString();
		}
		String oldts=vo.getAccount_ts();
		
		try {
			getService().synBasedoc(oldts, newts, BaseDocType.syn_account,vo);
		} catch (BusinessException e) {
		}
		
		if(flag){
			this.setDataAndShowLogInfo(BaseDocType.syn_account);
		}
	}
	
	protected void onBoSynZdy(boolean flag){//flagΪ�Ƿ���ʾ��־
		
		//ͬ���Զ����߼�
		if(flag){
			newts=new UFDateTime().toString();
		}
		String oldts=vo.getZdy_ts();
		
		try {
			getService().synBasedoc(oldts, newts, BaseDocType.syn_zdy,vo);
		} catch (BusinessException e) {
		}
		
		if(flag){
			this.setDataAndShowLogInfo(BaseDocType.syn_zdy);
		}
	}
	
	protected void onBoSynSubj(boolean flag){//flagΪ�Ƿ���ʾ��־
		
		//ͬ����Ŀ�߼�
		if(flag){
			newts=new UFDateTime().toString();
		}
		String oldts=vo.getSubj_ts();
		
		try {
			getService().synBasedoc(oldts, newts, BaseDocType.syn_subj,vo);
		} catch (BusinessException e) {
		}
		
		if(flag){
			this.setDataAndShowLogInfo(BaseDocType.syn_subj);
		}
	}

	@Override
	public void onButtonClicked(ButtonObject btn) {
		
		if(btn.getCode().equals("synall")){
			
			MyThread tread=new MyThread(this);
			tread.start();
			
		}
		
		if(btn.getCode().equals("loginfo")){
			
			this.doLogInfo(false);
		}
	}
	
	private void setDataAndShowLogInfo(int type){
		
		//ˢ�����µĽ���vo
		this.refreshNewData();
		
		String str="";
		if(type==BaseDocType.syn_dept){
			str+="=============================���ż��û�ͬ�����=================================\n";
			str+="�ϴ�ͬ��ʱ�䣺"+vo.getDept_ts()+"\n";
			str+="����ͬ��ʱ�䣺"+newts+"\n";
			str+="����ͬ�������"+vo.getDeptmsg();
		}
		
		if(type==BaseDocType.syn_psn){
			str+="===============================��Աͬ�����====================================\n";
			str+="�ϴ�ͬ��ʱ�䣺"+vo.getPsn_ts()+"\n";
			str+="����ͬ��ʱ�䣺"+newts+"\n";
			str+="����ͬ�������"+vo.getPsnmsg();
		}
		
		if(type==BaseDocType.syn_cust){
			str+="===============================����ͬ�����====================================\n";
			str+="�ϴ�ͬ��ʱ�䣺"+vo.getCust_ts()+"\n";
			str+="����ͬ��ʱ�䣺"+newts+"\n";
			str+="����ͬ�������"+vo.getCustmsg();
		}
		
		if(type==BaseDocType.syn_material){
			str+="===============================����ͬ�����====================================\n";
			str+="�ϴ�ͬ��ʱ�䣺"+vo.getMaterial_ts()+"\n";
			str+="����ͬ��ʱ�䣺"+newts+"\n";
			str+="����ͬ�������"+vo.getMaterialmsg();
		}
		
		if(type==BaseDocType.syn_account){
			str+="==============================�����˻�ͬ�����=================================\n";
			str+="�ϴ�ͬ��ʱ�䣺"+vo.getAccount_ts()+"\n";
			str+="����ͬ��ʱ�䣺"+newts+"\n";
			str+="����ͬ�������"+vo.getAccountmsg();
		}
		
		if(type==BaseDocType.syn_zdy){
			str+="==============================�Զ��嵵��ͬ�����===============================\n";
			str+="�ϴ�ͬ��ʱ�䣺"+vo.getZdy_ts()+"\n";
			str+="����ͬ��ʱ�䣺"+newts+"\n";
			str+="����ͬ�������"+vo.getZdymsg();
		}
		
		if(type==BaseDocType.syn_subj){
			str+="===============================��Ŀͬ�����====================================\n";
			str+="�ϴ�ͬ��ʱ�䣺"+vo.getSubj_ts()+"\n";
			str+="����ͬ��ʱ�䣺"+newts+"\n";
			str+="����ͬ�������"+vo.getSubjmsg();
		}
		
		RizhiDialog rg=new RizhiDialog(this);
		rg.showInfoMsg(str);
	}
	
	/**
	 * ��ʾ������־
	 */
	protected void doLogInfo(boolean flag){//flagΪtrue��ʾ����ͬ��ʱ��
		
		String str="";
		this.refreshNewData();
		
		if(!"".equals(getString(vo.getDeptmsg()))){
			str+="=============================���ż��û�ͬ�����=================================\n";
			str+="�ϴ�ͬ��ʱ�䣺"+vo.getDept_ts()+"\n";
			if(flag){
				str+="����ͬ��ʱ�䣺"+newts+"\n";
				str+="����ͬ�������"+vo.getDeptmsg()+"\n\n";
			}else{
				str+="�ϴ�ͬ�������"+vo.getDeptmsg()+"\n\n";
			}
		}
		
		if(!"".equals(getString(vo.getPsnmsg()))){
			str+="===============================��Աͬ�����====================================\n";
			str+="�ϴ�ͬ��ʱ�䣺"+vo.getPsn_ts()+"\n";
			if(flag){
				str+="����ͬ��ʱ�䣺"+newts+"\n";
				str+="����ͬ�������"+vo.getPsnmsg()+"\n\n";
			}else{
				str+="�ϴ�ͬ�������"+vo.getPsnmsg()+"\n\n";
			}
		}
		
		if(!"".equals(getString(vo.getCustmsg()))){
			str+="===============================����ͬ�����====================================\n";
			str+="�ϴ�ͬ��ʱ�䣺"+vo.getCust_ts()+"\n";
			if(flag){
				str+="����ͬ��ʱ�䣺"+newts+"\n";
				str+="����ͬ�������"+vo.getCustmsg()+"\n\n";
			}else{
				str+="�ϴ�ͬ�������"+vo.getCustmsg()+"\n\n";
			}
		}
		
		if(!"".equals(getString(vo.getMaterialmsg()))){
			str+="===============================����ͬ�����====================================\n";
			str+="�ϴ�ͬ��ʱ�䣺"+vo.getMaterial_ts()+"\n";
			if(flag){
				str+="����ͬ��ʱ�䣺"+newts+"\n";
				str+="����ͬ�������"+vo.getMaterialmsg()+"\n\n";
			}else{
				str+="�ϴ�ͬ�������"+vo.getMaterialmsg()+"\n\n";
			}
		}
		
		if(!"".equals(getString(vo.getAccountmsg()))){
			str+="==============================�����˻�ͬ�����=================================\n";
			str+="�ϴ�ͬ��ʱ�䣺"+vo.getAccount_ts()+"\n";
			if(flag){
				str+="����ͬ��ʱ�䣺"+newts+"\n";
				str+="����ͬ�������"+vo.getAccountmsg()+"\n\n";
			}else{
				str+="�ϴ�ͬ�������"+vo.getAccountmsg()+"\n\n";
			}
		}
		
		if(!"".equals(getString(vo.getZdymsg()))){
			str+="==============================�Զ��嵵��ͬ�����===============================\n";
			str+="�ϴ�ͬ��ʱ�䣺"+vo.getZdy_ts()+"\n";
			if(flag){
				str+="����ͬ��ʱ�䣺"+newts+"\n";
				str+="����ͬ�������"+vo.getZdymsg()+"\n\n";
			}else{
				str+="�ϴ�ͬ�������"+vo.getZdymsg()+"\n\n";
			}
		}
		
		if(!"".equals(getString(vo.getSubjmsg()))){
			str+="===============================��Ŀͬ�����====================================\n";
			str+="�ϴ�ͬ��ʱ�䣺"+vo.getSubj_ts()+"\n";
			if(flag){
				str+="����ͬ��ʱ�䣺"+newts+"\n";
				str+="����ͬ�������"+vo.getSubjmsg()+"\n\n";
			}else{
				str+="�ϴ�ͬ�������"+vo.getSubjmsg()+"\n\n";
			}
		}
		
		RizhiDialog rg=new RizhiDialog(this);
		rg.showInfoMsg(str);
	}
	
	private String getString(String msg){
		return msg==null?"":msg;
	}
	
}
