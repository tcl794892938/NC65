package nc.vo.fl.voucher;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDateTime;

public class TempBaseDocVO2 extends SuperVO {
	
	private String temp_id;
	private String dept_ts;
	private String psn_ts;
	private String cust_ts;
	private String material_ts;
	private String account_ts;
	private String zdy_ts;
	private String subj_ts;
	
	private String deptmsg;
	private String psnmsg;
	private String custmsg;
	private String materialmsg;
	private String accountmsg;
	private String zdymsg;
	private String subjmsg;
	
	private UFDateTime ts;
	private Integer dr=0;
	
	
	@Override
	public String getPKFieldName() {
		return "temp_id";
	}
	@Override
	public String getPrimaryKey() {
		return temp_id;
	}
	@Override
	public String getTableName() {
		return "temp_ts2";
	}
	public String getTemp_id() {
		return temp_id;
	}
	public void setTemp_id(String temp_id) {
		this.temp_id = temp_id;
	}
	public String getDept_ts() {
		return dept_ts;
	}
	public void setDept_ts(String dept_ts) {
		this.dept_ts = dept_ts;
	}
	public String getPsn_ts() {
		return psn_ts;
	}
	public void setPsn_ts(String psn_ts) {
		this.psn_ts = psn_ts;
	}
	public String getCust_ts() {
		return cust_ts;
	}
	public void setCust_ts(String cust_ts) {
		this.cust_ts = cust_ts;
	}
	public String getMaterial_ts() {
		return material_ts;
	}
	public void setMaterial_ts(String material_ts) {
		this.material_ts = material_ts;
	}
	public String getAccount_ts() {
		return account_ts;
	}
	public void setAccount_ts(String account_ts) {
		this.account_ts = account_ts;
	}
	public String getZdy_ts() {
		return zdy_ts;
	}
	public void setZdy_ts(String zdy_ts) {
		this.zdy_ts = zdy_ts;
	}
	public UFDateTime getTs() {
		return ts;
	}
	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}
	public Integer getDr() {
		return dr;
	}
	public void setDr(Integer dr) {
		this.dr = dr;
	}
	public String getSubj_ts() {
		return subj_ts;
	}
	public void setSubj_ts(String subj_ts) {
		this.subj_ts = subj_ts;
	}
	public String getDeptmsg() {
		return deptmsg;
	}
	public void setDeptmsg(String deptmsg) {
		this.deptmsg = deptmsg;
	}
	public String getPsnmsg() {
		return psnmsg;
	}
	public void setPsnmsg(String psnmsg) {
		this.psnmsg = psnmsg;
	}
	public String getCustmsg() {
		return custmsg;
	}
	public void setCustmsg(String custmsg) {
		this.custmsg = custmsg;
	}
	public String getMaterialmsg() {
		return materialmsg;
	}
	public void setMaterialmsg(String materialmsg) {
		this.materialmsg = materialmsg;
	}
	public String getAccountmsg() {
		return accountmsg;
	}
	public void setAccountmsg(String accountmsg) {
		this.accountmsg = accountmsg;
	}
	public String getZdymsg() {
		return zdymsg;
	}
	public void setZdymsg(String zdymsg) {
		this.zdymsg = zdymsg;
	}
	public String getSubjmsg() {
		return subjmsg;
	}
	public void setSubjmsg(String subjmsg) {
		this.subjmsg = subjmsg;
	}

}
