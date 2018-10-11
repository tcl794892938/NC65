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
		return "数据导入界面";//暂不用
	}

	protected void postInit(){
		UIScrollPane scrollPnl = new UIScrollPane();
		scrollPnl.setPreferredSize(new Dimension(1024,1024));
		scrollPnl.setViewportView(getContentPanel());
		add(scrollPnl,"Center");
		
		String str=this.setDefaultData();
		if(!"".equals(str)){
			MessageDialog.showHintDlg(this, "提示", str);
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
			return "初始化时间异常，请联系开发人员！";
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
	 * 接口
	 */
	private ISynBaseMaintain2 getService(){
		if(isbm==null){
			isbm=NCLocator.getInstance().lookup(ISynBaseMaintain2.class);
		}
		return isbm;
	}

	protected ButtonObject m_synAll = new ButtonObject("一键同步", "同步所有档案", 0, "synall");
	protected ButtonObject m_logInfo = new ButtonObject("查看日志", "查看同步日志", 0, "loginfo");
	
	private ISynBaseMaintain2 isbm=null;
	private TempBaseDocVO2 vo=null;
	private String newts="";
	
	private int btnWidth = 60;
	private int btnHeight = 30;
	
	//主面板
	protected UIPanel m_mainPnl;
	//按钮面板
	protected UIPanel m_butPnl;
	//部门及用户档案
	protected UIPanel m_deptPnl;
	//人员档案
	protected UIPanel m_psnPnl;
	//客商档案
	protected UIPanel m_custPnl;
	//物料分类及档案
	protected UIPanel m_materialPnl;
	//银行账户及档案
	protected UIPanel m_accountPnl;
	//自定义档案
	protected UIPanel m_zdyPnl;
	//科目表
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
	 * 主面板
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
	 * 所有按钮面板
	 * @return
	 */
	protected UIPanel getButAllPanel(){
		if(m_butPnl == null){
			m_butPnl = new UIPanel();
			m_butPnl.setName("按钮面板");
			m_butPnl.setLayout(new BoxLayout(m_butPnl,3));
			m_butPnl.setBorder(new TitledBorder(""));//不设置无分隔
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
	 * 部门panel
	 * @return
	 */
	protected UIPanel getDeptPanel(){
		if(m_deptPnl == null){
			m_deptPnl = new UIPanel();
			FlowLayout layout = new FlowLayout();
			layout.setAlignment(FlowLayout.LEFT);
			m_deptPnl.setLayout(layout);
			m_deptPnl.setBorder(new TitledBorder("部门及用户档案"));
			
			UILabel lab=new UILabel();
			lab.setText("上次同步时间:");
			lbdept=new UILabel();
			lbdept.setPreferredSize(new Dimension(135,btnHeight));
			
			m_deptPnl.add(lab);
			m_deptPnl.add(lbdept);
			m_deptPnl.add(getDeptBtn());
		}
		return m_deptPnl;
	}
	
	/**
	 * 人员panel
	 * @return
	 */
	protected UIPanel getPsnPanel(){
		if(m_psnPnl == null){
			m_psnPnl = new UIPanel();
			FlowLayout layout = new FlowLayout();
			layout.setAlignment(FlowLayout.LEFT);
			m_psnPnl.setLayout(layout);
			m_psnPnl.setBorder(new TitledBorder("人员档案"));
			
			UILabel lab=new UILabel();
			lab.setText("上次同步时间:");
			lbpsn=new UILabel();
			lbpsn.setPreferredSize(new Dimension(135,btnHeight));
			
			m_psnPnl.add(lab);
			m_psnPnl.add(lbpsn);
			m_psnPnl.add(getPsnBtn());
		}
		return m_psnPnl;
	}
	
	/**
	 * 客商panel
	 * @return
	 */
	protected UIPanel getCustPanel(){
		if(m_custPnl == null){
			m_custPnl = new UIPanel();
			FlowLayout layout = new FlowLayout();
			layout.setAlignment(FlowLayout.LEFT);
			m_custPnl.setLayout(layout);
			m_custPnl.setBorder(new TitledBorder("客商档案"));
			
			UILabel lab=new UILabel();
			lab.setText("上次同步时间:");
			lbcust=new UILabel();
			lbcust.setPreferredSize(new Dimension(135,btnHeight));
			
			m_custPnl.add(lab);
			m_custPnl.add(lbcust);
			m_custPnl.add(getCustBtn());
		}
		return m_custPnl;
	}
	
	/**
	 * 物料panel
	 * @return
	 */
	protected UIPanel getMaterialPanel(){
		if(m_materialPnl == null){
			m_materialPnl = new UIPanel();
			FlowLayout layout = new FlowLayout();
			layout.setAlignment(FlowLayout.LEFT);
			m_materialPnl.setLayout(layout);
			m_materialPnl.setBorder(new TitledBorder("物料分类及档案"));
			
			UILabel lab=new UILabel();
			lab.setText("上次同步时间:");
			lbmate=new UILabel();
			lbmate.setPreferredSize(new Dimension(135,btnHeight));
			
			m_materialPnl.add(lab);
			m_materialPnl.add(lbmate);
			m_materialPnl.add(getMateBtn());
		}
		return m_materialPnl;
	}
	
	/**
	 * 银行panel
	 * @return
	 */
	protected UIPanel getAccountPanel(){
		if(m_accountPnl == null){
			m_accountPnl = new UIPanel();
			FlowLayout layout = new FlowLayout();
			layout.setAlignment(FlowLayout.LEFT);
			m_accountPnl.setLayout(layout);
			m_accountPnl.setBorder(new TitledBorder("银行账户及档案"));
			
			UILabel lab=new UILabel();
			lab.setText("上次同步时间:");
			lbacc=new UILabel();
			lbacc.setPreferredSize(new Dimension(135,btnHeight));
			
			m_accountPnl.add(lab);
			m_accountPnl.add(lbacc);
			m_accountPnl.add(getAccBtn());
		}
		return m_accountPnl;
	}
	
	/**
	 * 自定义panel
	 * @return
	 */
	protected UIPanel getZdyPanel(){
		if(m_zdyPnl == null){
			m_zdyPnl = new UIPanel();
			FlowLayout layout = new FlowLayout();
			layout.setAlignment(FlowLayout.LEFT);
			m_zdyPnl.setLayout(layout);
			m_zdyPnl.setBorder(new TitledBorder("自定义档案"));
			
			UILabel lab=new UILabel();
			lab.setText("上次同步时间:");
			lbzdy=new UILabel();
			lbzdy.setPreferredSize(new Dimension(135,btnHeight));
			
			m_zdyPnl.add(lab);
			m_zdyPnl.add(lbzdy);
			m_zdyPnl.add(getZdyBtn());
		}
		return m_zdyPnl;
	}
	
	/**
	 * 科目panel
	 * @return
	 */
	/*
	protected UIPanel getSubjPanel(){
		if(m_subjPnl == null){
			m_subjPnl = new UIPanel();
			FlowLayout layout = new FlowLayout();
			layout.setAlignment(FlowLayout.LEFT);
			m_subjPnl.setLayout(layout);
			m_subjPnl.setBorder(new TitledBorder("科目表"));
			
			UILabel lab=new UILabel();
			lab.setText("上次同步时间:");
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
			m_deptBtn.setText("同步");
			m_deptBtn.setName("syndept");
			m_deptBtn.setPreferredSize(new Dimension(btnWidth,btnHeight));
			m_deptBtn.addActionListener(this);
		}
		return m_deptBtn;
	}
	
	protected UIButton getPsnBtn(){
		if(m_psnBtn == null){
			m_psnBtn = new UIButton();
			m_psnBtn.setText("同步");
			m_psnBtn.setName("synpsn");
			m_psnBtn.setPreferredSize(new Dimension(btnWidth,btnHeight));
			m_psnBtn.addActionListener(this);
		}
		return m_psnBtn;
	}
	
	protected UIButton getCustBtn(){
		if(m_custBtn == null){
			m_custBtn = new UIButton();
			m_custBtn.setText("同步");
			m_custBtn.setName("syncust");
			m_custBtn.setPreferredSize(new Dimension(btnWidth,btnHeight));
			m_custBtn.addActionListener(this);
		}
		return m_custBtn;
	}
	
	protected UIButton getMateBtn(){
		if(m_mateBtn == null){
			m_mateBtn = new UIButton();
			m_mateBtn.setText("同步");
			m_mateBtn.setName("synmate");
			m_mateBtn.setPreferredSize(new Dimension(btnWidth,btnHeight));
			m_mateBtn.addActionListener(this);
		}
		return m_mateBtn;
	}
	
	protected UIButton getAccBtn(){
		if(m_accBtn == null){
			m_accBtn = new UIButton();
			m_accBtn.setText("同步");
			m_accBtn.setName("synacc");
			m_accBtn.setPreferredSize(new Dimension(btnWidth,btnHeight));
			m_accBtn.addActionListener(this);
		}
		return m_accBtn;
	}
	
	protected UIButton getZdyBtn(){
		if(m_zdyBtn == null){
			m_zdyBtn = new UIButton();
			m_zdyBtn.setText("同步");
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
			m_subjBtn.setText("同步");
			m_subjBtn.setName("synsubj");
			m_subjBtn.setPreferredSize(new Dimension(btnWidth,btnHeight));
			m_subjBtn.addActionListener(this);
		}
		return m_subjBtn;
	}*/
	
	
	String INSTRUCTION = "" +
			"说明：\n"+
			"1、该功能是同步基础数据从大陆帐套进入管理帐套而设计的。\n"+
			"2、建议同步档案每月一致两次，凭证同步前尽量先进行档案同步。\n"+
			"3、日志文件仅保留最近一次的同步结果。\n"+
			"4、客商同步只同步客户，供应商需手工关联生成。\n"+
			"5、同步过程遇到问题及时联系实施人员：吴阳梅  电话：138-1543-7119。\n"
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
			start.setStartText("档案同步中，请等待...");

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
				MessageDialog.showHintDlg(dlg, "提示", e.getMessage());
			} finally {
				getBannerDialog(false).end();
			}
		}
	}

	public void actionPerformed(ActionEvent e){
		// 按钮
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
	
	protected void onBoSynDept(boolean flag){//flag为是否显示日志
		
		//同步部门用户逻辑
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
	
	protected void onBoSynPsn(boolean flag){//flag为是否显示日志
		
		//同步人员逻辑
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
	
	protected void onBoSynCust(boolean flag){//flag为是否显示日志
		
		//同步客商逻辑
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
	
	protected void onBoSynMate(boolean flag){//flag为是否显示日志
		
		//同步物料逻辑
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
	
	protected void onBoSynAcc(boolean flag){//flag为是否显示日志
		
		//同步银行账户逻辑
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
	
	protected void onBoSynZdy(boolean flag){//flag为是否显示日志
		
		//同步自定义逻辑
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
	
	protected void onBoSynSubj(boolean flag){//flag为是否显示日志
		
		//同步科目逻辑
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
		
		//刷新最新的界面vo
		this.refreshNewData();
		
		String str="";
		if(type==BaseDocType.syn_dept){
			str+="=============================部门及用户同步结果=================================\n";
			str+="上次同步时间："+vo.getDept_ts()+"\n";
			str+="本次同步时间："+newts+"\n";
			str+="本次同步结果："+vo.getDeptmsg();
		}
		
		if(type==BaseDocType.syn_psn){
			str+="===============================人员同步结果====================================\n";
			str+="上次同步时间："+vo.getPsn_ts()+"\n";
			str+="本次同步时间："+newts+"\n";
			str+="本次同步结果："+vo.getPsnmsg();
		}
		
		if(type==BaseDocType.syn_cust){
			str+="===============================客商同步结果====================================\n";
			str+="上次同步时间："+vo.getCust_ts()+"\n";
			str+="本次同步时间："+newts+"\n";
			str+="本次同步结果："+vo.getCustmsg();
		}
		
		if(type==BaseDocType.syn_material){
			str+="===============================物料同步结果====================================\n";
			str+="上次同步时间："+vo.getMaterial_ts()+"\n";
			str+="本次同步时间："+newts+"\n";
			str+="本次同步结果："+vo.getMaterialmsg();
		}
		
		if(type==BaseDocType.syn_account){
			str+="==============================银行账户同步结果=================================\n";
			str+="上次同步时间："+vo.getAccount_ts()+"\n";
			str+="本次同步时间："+newts+"\n";
			str+="本次同步结果："+vo.getAccountmsg();
		}
		
		if(type==BaseDocType.syn_zdy){
			str+="==============================自定义档案同步结果===============================\n";
			str+="上次同步时间："+vo.getZdy_ts()+"\n";
			str+="本次同步时间："+newts+"\n";
			str+="本次同步结果："+vo.getZdymsg();
		}
		
		if(type==BaseDocType.syn_subj){
			str+="===============================科目同步结果====================================\n";
			str+="上次同步时间："+vo.getSubj_ts()+"\n";
			str+="本次同步时间："+newts+"\n";
			str+="本次同步结果："+vo.getSubjmsg();
		}
		
		RizhiDialog rg=new RizhiDialog(this);
		rg.showInfoMsg(str);
	}
	
	/**
	 * 显示所有日志
	 */
	protected void doLogInfo(boolean flag){//flag为true显示本次同步时间
		
		String str="";
		this.refreshNewData();
		
		if(!"".equals(getString(vo.getDeptmsg()))){
			str+="=============================部门及用户同步结果=================================\n";
			str+="上次同步时间："+vo.getDept_ts()+"\n";
			if(flag){
				str+="本次同步时间："+newts+"\n";
				str+="本次同步结果："+vo.getDeptmsg()+"\n\n";
			}else{
				str+="上次同步结果："+vo.getDeptmsg()+"\n\n";
			}
		}
		
		if(!"".equals(getString(vo.getPsnmsg()))){
			str+="===============================人员同步结果====================================\n";
			str+="上次同步时间："+vo.getPsn_ts()+"\n";
			if(flag){
				str+="本次同步时间："+newts+"\n";
				str+="本次同步结果："+vo.getPsnmsg()+"\n\n";
			}else{
				str+="上次同步结果："+vo.getPsnmsg()+"\n\n";
			}
		}
		
		if(!"".equals(getString(vo.getCustmsg()))){
			str+="===============================客商同步结果====================================\n";
			str+="上次同步时间："+vo.getCust_ts()+"\n";
			if(flag){
				str+="本次同步时间："+newts+"\n";
				str+="本次同步结果："+vo.getCustmsg()+"\n\n";
			}else{
				str+="上次同步结果："+vo.getCustmsg()+"\n\n";
			}
		}
		
		if(!"".equals(getString(vo.getMaterialmsg()))){
			str+="===============================物料同步结果====================================\n";
			str+="上次同步时间："+vo.getMaterial_ts()+"\n";
			if(flag){
				str+="本次同步时间："+newts+"\n";
				str+="本次同步结果："+vo.getMaterialmsg()+"\n\n";
			}else{
				str+="上次同步结果："+vo.getMaterialmsg()+"\n\n";
			}
		}
		
		if(!"".equals(getString(vo.getAccountmsg()))){
			str+="==============================银行账户同步结果=================================\n";
			str+="上次同步时间："+vo.getAccount_ts()+"\n";
			if(flag){
				str+="本次同步时间："+newts+"\n";
				str+="本次同步结果："+vo.getAccountmsg()+"\n\n";
			}else{
				str+="上次同步结果："+vo.getAccountmsg()+"\n\n";
			}
		}
		
		if(!"".equals(getString(vo.getZdymsg()))){
			str+="==============================自定义档案同步结果===============================\n";
			str+="上次同步时间："+vo.getZdy_ts()+"\n";
			if(flag){
				str+="本次同步时间："+newts+"\n";
				str+="本次同步结果："+vo.getZdymsg()+"\n\n";
			}else{
				str+="上次同步结果："+vo.getZdymsg()+"\n\n";
			}
		}
		
		if(!"".equals(getString(vo.getSubjmsg()))){
			str+="===============================科目同步结果====================================\n";
			str+="上次同步时间："+vo.getSubj_ts()+"\n";
			if(flag){
				str+="本次同步时间："+newts+"\n";
				str+="本次同步结果："+vo.getSubjmsg()+"\n\n";
			}else{
				str+="上次同步结果："+vo.getSubjmsg()+"\n\n";
			}
		}
		
		RizhiDialog rg=new RizhiDialog(this);
		rg.showInfoMsg(str);
	}
	
	private String getString(String msg){
		return msg==null?"":msg;
	}
	
}
