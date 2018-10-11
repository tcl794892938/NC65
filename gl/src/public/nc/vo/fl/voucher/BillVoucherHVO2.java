package nc.vo.fl.voucher;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;

public class BillVoucherHVO2 extends SuperVO{
	
	private static final long serialVersionUID = 1L;
	private String pk_voucher_h;			
	private String dbillmaker;		
	private UFDate dmakedate;		
	private String synmaker;			
	private UFDate syndate;		
	private String synurl;			
	private String yearmonth;		
	private String def1;			
	private String def2;		
	private String def3;			
	private String def4;			
	private String def5;
	private UFDateTime ts;		
	private Integer dr;	
	private Integer numtype;
	private String pk_corp;
	private UFBoolean is_syn;


	@Override
	public String getPKFieldName() {
		return "pk_voucher_h";
	}

	@Override
	public String getParentPKFieldName() {
		return null;
	}

	@Override
	public String getTableName() {
		return "fl_voucher_h2";
	}

	public String getDbillmaker() {
		return dbillmaker;
	}

	public void setDbillmaker(String dbillmaker) {
		this.dbillmaker = dbillmaker;
	}

	public String getDef1() {
		return def1;
	}

	public void setDef1(String def1) {
		this.def1 = def1;
	}

	public String getDef2() {
		return def2;
	}

	public void setDef2(String def2) {
		this.def2 = def2;
	}

	public String getDef3() {
		return def3;
	}

	public void setDef3(String def3) {
		this.def3 = def3;
	}

	public String getDef4() {
		return def4;
	}

	public void setDef4(String def4) {
		this.def4 = def4;
	}

	public String getDef5() {
		return def5;
	}

	public void setDef5(String def5) {
		this.def5 = def5;
	}

	public UFDate getDmakedate() {
		return dmakedate;
	}

	public void setDmakedate(UFDate dmakedate) {
		this.dmakedate = dmakedate;
	}

	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

	public String getPk_voucher_h() {
		return pk_voucher_h;
	}

	public void setPk_voucher_h(String pk_voucher_h) {
		this.pk_voucher_h = pk_voucher_h;
	}

	public UFDate getSyndate() {
		return syndate;
	}

	public void setSyndate(UFDate syndate) {
		this.syndate = syndate;
	}

	public String getSynmaker() {
		return synmaker;
	}

	public void setSynmaker(String synmaker) {
		this.synmaker = synmaker;
	}

	public String getSynurl() {
		return synurl;
	}

	public void setSynurl(String synurl) {
		this.synurl = synurl;
	}

	public UFDateTime getTs() {
		return ts;
	}

	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}

	public String getYearmonth() {
		return yearmonth;
	}

	public void setYearmonth(String yearmonth) {
		this.yearmonth = yearmonth;
	}

	public Integer getNumtype() {
		return numtype;
	}

	public void setNumtype(Integer numtype) {
		this.numtype = numtype;
	}

	public String getPk_corp() {
		return pk_corp;
	}

	public void setPk_corp(String pk_corp) {
		this.pk_corp = pk_corp;
	}

	public UFBoolean getIs_syn() {
		return is_syn;
	}

	public void setIs_syn(UFBoolean is_syn) {
		this.is_syn = is_syn;
	}

}
