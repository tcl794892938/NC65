package nc.vo.so.entity;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;

public class TempRecordVO extends SuperVO {
	
	private String pk_record;
	private String fhbillno;
	private String ylbillno;
	private UFDate fhdate;
	private String pk_org;
	private String pk_houseid;
	private String pk_material;
	private String pk_customer;
	private String vbatchcode;
	private UFDouble nylnum;
	private UFDouble nfhnum;
	private UFDouble nresnum;
	private Integer dr=0;
	private UFDateTime ts;

	@Override
	public String getPrimaryKey() {
		
		return pk_record;
	}

	@Override
	public String getTableName() {
		
		return "tcl_record";
	}

	public String getPk_record() {
		return pk_record;
	}

	public void setPk_record(String pk_record) {
		this.pk_record = pk_record;
	}

	public String getFhbillno() {
		return fhbillno;
	}

	public void setFhbillno(String fhbillno) {
		this.fhbillno = fhbillno;
	}

	public String getYlbillno() {
		return ylbillno;
	}

	public void setYlbillno(String ylbillno) {
		this.ylbillno = ylbillno;
	}

	public UFDate getFhdate() {
		return fhdate;
	}

	public void setFhdate(UFDate fhdate) {
		this.fhdate = fhdate;
	}

	public String getPk_org() {
		return pk_org;
	}

	public void setPk_org(String pk_org) {
		this.pk_org = pk_org;
	}

	public String getPk_houseid() {
		return pk_houseid;
	}

	public void setPk_houseid(String pk_houseid) {
		this.pk_houseid = pk_houseid;
	}

	public String getPk_material() {
		return pk_material;
	}

	public void setPk_material(String pk_material) {
		this.pk_material = pk_material;
	}

	public String getPk_customer() {
		return pk_customer;
	}

	public void setPk_customer(String pk_customer) {
		this.pk_customer = pk_customer;
	}

	public String getVbatchcode() {
		return vbatchcode;
	}

	public void setVbatchcode(String vbatchcode) {
		this.vbatchcode = vbatchcode;
	}

	public UFDouble getNylnum() {
		return nylnum;
	}

	public void setNylnum(UFDouble nylnum) {
		this.nylnum = nylnum;
	}

	public UFDouble getNfhnum() {
		return nfhnum;
	}

	public void setNfhnum(UFDouble nfhnum) {
		this.nfhnum = nfhnum;
	}

	public UFDouble getNresnum() {
		return nresnum;
	}

	public void setNresnum(UFDouble nresnum) {
		this.nresnum = nresnum;
	}

	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

	public UFDateTime getTs() {
		return ts;
	}

	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}

	@Override
	public String getPKFieldName() {
		return "pk_record";
	}
	
	

}
