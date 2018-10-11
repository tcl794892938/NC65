package nc.vo.ic.reserve.entity;

import nc.vo.ic.onhand.entity.IHashCodeVO;
import nc.vo.ic.pub.util.DimObj;
import nc.vo.pub.IVOMeta;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;

/**
 * <p>
 * <b>����Ԥ��</b>
 * 
 * @version v60
 * @since v60
 * @author yangb
 * @time 2010-4-26 ����08:39:40
 */

public class ReserveVO extends SuperVO implements IHashCodeVO, DimObj {

  // �Ƿ�ϵͳ����
  public static final String BSYSFLAG = "bsysflag";

  // �ͻ���������
  public static final String CASSCUSTID = "casscustid";

  // ҵ��Ա
  public static final String CBIZID = "cbizid";

  // �ͻ�
  public static final String CCUSTOMERID = "ccustomerid";

  // ��λ
  public static final String CLOCATIONID = "clocationid";

  // ����
  public static final String CMATERIALOID = "cmaterialoid";

  // ���ϰ汾
  public static final String CMATERIALVID = "cmaterialvid";

  // ��������
  public static final String CPRODUCTORID = "cproductorid";

  // ��Ŀ
  public static final String CPROJECTID = "cprojectid";

  // Ԥ��ʱ��
  public static final String CREATIONTIME = "creationtime";

  // ������
  public static final String CREATOR = "creator";

  // ���󵥾���ϸ
  public static final String CREQBILLBID = "creqbillbid";

  // ���󵥾�ͷ
  public static final String CREQBILLID = "creqbillid";

  // ���󵥾��к�
  public static final String CREQBILLROWNO = "creqbillrowno";

  // ���󵥾�����
  public static final String CREQBILLTYPE = "creqbilltype";

  // ���󵥾ݽ�������
  public static final String CREQTRANSTYPE = "creqtranstype";

  // ��Դ������ϸ
  public static final String CSOURCEBILLBID = "csourcebillbid";

  // ��Դ����ͷ
  public static final String CSOURCEBILLID = "csourcebillid";

  // ��Դ��������
  public static final String CSOURCETYPE = "csourcetype";

  // �����ͻ�
  public static final String CTPLCUSTOMERID = "ctplcustomerid";

  // ����λ
  public static final String CUNITID = "cunitid";

  // ��Ӧ��
  public static final String CVENDORID = "cvendorid";

  // �ֿ�
  public static final String CWAREHOUSEID = "cwarehouseid";

  // ��������
  public static final String DREQDATE = "dreqdate";

  // Ԥ������
  public static final String FRESERVETYPE = "freservetype";

  // Ԥ��״̬
  public static final String FRESSTATE = "fresstate";

  // ����޸�ʱ��
  public static final String MODIFIEDTIME = "modifiedtime";

  // ����޸���
  public static final String MODIFIER = "modifier";

  // ���󵥾�ȱ������=����������-Ԥ��������
  public static final String NLACKNUM = "nlacknum";

  // ��������
  public static final String NREQNUM = "nreqnum";

  // �������ۼ�Ԥ������ ��������
  public static final String NREQRSNUM = "nreqrsnum";

  // ����Ԥ������
  public static final String NRSNUM = "nrsnum";

  // �ۼƳ�������
  public static final String NTALOUTNUM = "ntaloutnum";
  
  // �ۼ�ί������
  public static final String NPSCASTNUM = "npscastnum";

  // �ۼ�Ԥ���������
  public static final String NTALRSINNUM = "ntalrsinnum";

  // �ۼ�Ԥ������
  public static final String NTALRSNUM = "ntalrsnum";

  // �ۼ�Ԥ����������
  public static final String NTALRSOUTNUM = "ntalrsoutnum";
  

  public static final String[] numkeys = {
    ReserveVO.NREQNUM, ReserveVO.NTALOUTNUM, ReserveVO.NTALRSINNUM,
    ReserveVO.NTALRSNUM, ReserveVO.NTALRSOUTNUM,
  };

  // Ԥ����
  public static final String OPERATOR = "billmaker";

  // ����
  public static final String PK_BATCHCODE = "pk_batchcode";

  // ����
  public static final String PK_GROUP = "pk_group";

  // �����֯
  public static final String PK_ORG = "pk_org";

  // �����֯�汾
  public static final String PK_ORG_V = "pk_org_v";

  // Ԥ��
  public static final String PK_RESERVE = "pk_reserve";

  // ���󵥾ݵ�TS
  public static final String REQBILLROWTS = "reqbillrowts";

  // ���󵥾ݵ��ִ���ά���ֶ�
  public final static String[] reqOnhandDimFields = {
    ReserveVO.PK_GROUP, ReserveVO.PK_ORG_V, ReserveVO.PK_ORG,
    ReserveVO.CWAREHOUSEID, ReserveVO.CMATERIALVID, ReserveVO.CMATERIALOID,
    ReserveVO.PK_BATCHCODE, ReserveVO.VBATCHCODE, ReserveVO.CVENDORID,
    ReserveVO.CPROJECTID, ReserveVO.CASSCUSTID, ReserveVO.CPRODUCTORID,
    ReserveVO.VFREE1, ReserveVO.VFREE2, ReserveVO.VFREE3, ReserveVO.VFREE4,
    ReserveVO.VFREE5, ReserveVO.VFREE6, ReserveVO.VFREE7, ReserveVO.VFREE8,
    ReserveVO.VFREE9, ReserveVO.VFREE10,
  };

  public static final String tablealias = "reserve";

  public static final String tablename = "ic_reserve";

  // ʱ���
  public static final String TS = "ts";

  // ���κ�
  public static final String VBATCHCODE = "vbatchcode";

  // ������������1
  public static final String VFREE1 = "vfree1";

  // ������������10
  public static final String VFREE10 = "vfree10";

  // ������������2
  public static final String VFREE2 = "vfree2";

  // ������������3
  public static final String VFREE3 = "vfree3";

  // ������������4
  public static final String VFREE4 = "vfree4";

  // ������������5
  public static final String VFREE5 = "vfree5";

  // ������������6
  public static final String VFREE6 = "vfree6";

  // ������������7
  public static final String VFREE7 = "vfree7";

  // ������������8
  public static final String VFREE8 = "vfree8";

  // ������������9
  public static final String VFREE9 = "vfree9";

  // ɢ����
  public static final String VHASHCODE = "vhashcode";

  // ���󵥾ݺ�
  public static final String VREQBILLCODE = "vreqbillcode";

  // Ԥ����¼��
  public static final String VRESCODE = "vrescode";
  
  //�ͻ����ͣ�2018-3-23������
  public static final String CUSTOMERTYPE="customertype";
  
  //��ע��ҵ��Ա����2018-3-23������
  public static final String REMARKS ="remarks";

  private static final long serialVersionUID = 2010090211210051L;

  /**
   * ReserveVO �Ĺ�����
   */
  public ReserveVO() {
    super();
  }

  public String getBillmaker() {
    return (String) this.getAttributeValue(ReserveVO.OPERATOR);
  }

  public UFBoolean getBsysflag() {
    return (UFBoolean) this.getAttributeValue(ReserveVO.BSYSFLAG);
  }

  public String getCasscustid() {
    return (String) this.getAttributeValue(ReserveVO.CASSCUSTID);
  }

  public String getCbizid() {
    return (String) this.getAttributeValue(ReserveVO.CBIZID);
  }

  public String getCcustomerid() {
    return (String) this.getAttributeValue(ReserveVO.CCUSTOMERID);
  }

  public String getClocationid() {
    return (String) this.getAttributeValue(ReserveVO.CLOCATIONID);
  }

  public String getCmaterialoid() {
    return (String) this.getAttributeValue(ReserveVO.CMATERIALOID);
  }

  public String getCmaterialvid() {
    return (String) this.getAttributeValue(ReserveVO.CMATERIALVID);
  }

  public String getCproductorid() {
    return (String) this.getAttributeValue(ReserveVO.CPRODUCTORID);
  }

  public String getCprojectid() {
    return (String) this.getAttributeValue(ReserveVO.CPROJECTID);
  }

  public UFDateTime getCreationtime() {
    return (UFDateTime) this.getAttributeValue(ReserveVO.CREATIONTIME);
  }

  public String getCreator() {
    return (String) this.getAttributeValue(ReserveVO.CREATOR);
  }

  public String getCreqbillbid() {
    return (String) this.getAttributeValue(ReserveVO.CREQBILLBID);
  }

  public String getCreqbillid() {
    return (String) this.getAttributeValue(ReserveVO.CREQBILLID);
  }

  public String getCreqbillrowno() {
    return (String) this.getAttributeValue(ReserveVO.CREQBILLROWNO);
  }

  public String getCreqbilltype() {
    return (String) this.getAttributeValue(ReserveVO.CREQBILLTYPE);
  }

  public String getCreqtranstype() {
    return (String) this.getAttributeValue(ReserveVO.CREQTRANSTYPE);
  }

  public String getCsourcebillbid() {
    return (String) this.getAttributeValue(ReserveVO.CSOURCEBILLBID);
  }

  public String getCsourcebillid() {
    return (String) this.getAttributeValue(ReserveVO.CSOURCEBILLID);
  }

  public String getCsourcetype() {
    return (String) this.getAttributeValue(ReserveVO.CSOURCETYPE);
  }

  public String getCtplcustomerid() {
    return (String) this.getAttributeValue(ReserveVO.CTPLCUSTOMERID);
  }

  public String getCunitid() {
    return (String) this.getAttributeValue(ReserveVO.CUNITID);
  }

  public String getCvendorid() {
    return (String) this.getAttributeValue(ReserveVO.CVENDORID);
  }

  public String getCwarehouseid() {
    return (String) this.getAttributeValue(ReserveVO.CWAREHOUSEID);
  }

  public UFDate getDreqdate() {
    return (UFDate) this.getAttributeValue(ReserveVO.DREQDATE);
  }

  public Integer getFreservetype() {
    return (Integer) this.getAttributeValue(ReserveVO.FRESERVETYPE);
  }

  public Integer getFresstate() {
    return (Integer) this.getAttributeValue(ReserveVO.FRESSTATE);
  }

  /**
   * 
   */
  @Override
  public String getHashCode() {
    return this.getVhashcode();
  }

  /**
   * 
   */
  @Override
  public String[] getHashContentFields() {
    return ReserveVO.reqOnhandDimFields;
  }

  @Override
  public IVOMeta getMetaData() {
    IVOMeta meta = VOMetaFactory.getInstance().getVOMeta("ic.ReserveVO");
    return meta;
  }

  public UFDateTime getModifiedtime() {
    return (UFDateTime) this.getAttributeValue(ReserveVO.MODIFIEDTIME);
  }

  public String getModifier() {
    return (String) this.getAttributeValue(ReserveVO.MODIFIER);
  }

  public UFDouble getNlacknum() {
    return (UFDouble) this.getAttributeValue(ReserveVO.NLACKNUM);
  }

  public UFDouble getNreqnum() {
    return (UFDouble) this.getAttributeValue(ReserveVO.NREQNUM);
  }

  public UFDouble getNreqrsnum() {
    return (UFDouble) this.getAttributeValue(ReserveVO.NREQRSNUM);
  }

  public UFDouble getNrsnum() {
    return (UFDouble) this.getAttributeValue(ReserveVO.NRSNUM);
  }

  public UFDouble getNtaloutnum() {
    return (UFDouble) this.getAttributeValue(ReserveVO.NTALOUTNUM);
  }
  
  public UFDouble getNpscastnum() {
	    return (UFDouble) this.getAttributeValue(ReserveVO.NPSCASTNUM);
	  }

  public UFDouble getNtalrsinnum() {
    return (UFDouble) this.getAttributeValue(ReserveVO.NTALRSINNUM);
  }

  public UFDouble getNtalrsnum() {
    return (UFDouble) this.getAttributeValue(ReserveVO.NTALRSNUM);
  }

  public UFDouble getNtalrsoutnum() {
    return (UFDouble) this.getAttributeValue(ReserveVO.NTALRSOUTNUM);
  }

  public String getPk_batchcode() {
    return (String) this.getAttributeValue(ReserveVO.PK_BATCHCODE);
  }

  public String getPk_group() {
    return (String) this.getAttributeValue(ReserveVO.PK_GROUP);
  }

  public String getPk_org() {
    return (String) this.getAttributeValue(ReserveVO.PK_ORG);
  }

  public String getPk_org_v() {
    return (String) this.getAttributeValue(ReserveVO.PK_ORG_V);
  }

  public String getPk_reserve() {
    return (String) this.getAttributeValue(ReserveVO.PK_RESERVE);
  }

  public UFDateTime getReqbillrowts() {
    return (UFDateTime) this.getAttributeValue(ReserveVO.REQBILLROWTS);
  }

  public UFDateTime getTs() {
    return (UFDateTime) this.getAttributeValue(ReserveVO.TS);
  }

  public String getVbatchcode() {
    return (String) this.getAttributeValue(ReserveVO.VBATCHCODE);
  }

  public String getVfree1() {
    return (String) this.getAttributeValue(ReserveVO.VFREE1);
  }

  public String getVfree10() {
    return (String) this.getAttributeValue(ReserveVO.VFREE10);
  }

  public String getVfree2() {
    return (String) this.getAttributeValue(ReserveVO.VFREE2);
  }

  public String getVfree3() {
    return (String) this.getAttributeValue(ReserveVO.VFREE3);
  }

  public String getVfree4() {
    return (String) this.getAttributeValue(ReserveVO.VFREE4);
  }

  public String getVfree5() {
    return (String) this.getAttributeValue(ReserveVO.VFREE5);
  }

  public String getVfree6() {
    return (String) this.getAttributeValue(ReserveVO.VFREE6);
  }

  public String getVfree7() {
    return (String) this.getAttributeValue(ReserveVO.VFREE7);
  }

  public String getVfree8() {
    return (String) this.getAttributeValue(ReserveVO.VFREE8);
  }

  public String getVfree9() {
    return (String) this.getAttributeValue(ReserveVO.VFREE9);
  }

  public String getVhashcode() {
    return (String) this.getAttributeValue(ReserveVO.VHASHCODE);
  }

  public String getVreqbillcode() {
    return (String) this.getAttributeValue(ReserveVO.VREQBILLCODE);
  }

  public String getVrescode() {
    return (String) this.getAttributeValue(ReserveVO.VRESCODE);
  }
  //�ͻ����ͣ�2018-3-23������
  public Integer getCustomertype(){
	  return (Integer) this.getAttributeValue(ReserveVO.CUSTOMERTYPE);
	  
  }
  //��ע��ҵ��Ա����2018-3-23������
  public String getRemarks(){
	  return (String) this.getAttributeValue(ReserveVO.REMARKS);
  }
  
  //�ͻ����ͣ�2018-3-23������
  public void setCustomertype(Integer customertype){
	this.setAttributeValue(ReserveVO.CUSTOMERTYPE, customertype);
  }
  
//��ע��ҵ��Ա����2018-3-23������
  public void setRemarks(String remarks) {
	this.setAttributeValue(ReserveVO.REMARKS, remarks);
  }

  public void setBillmaker(String operator) {
    this.setAttributeValue(ReserveVO.OPERATOR, operator);
  }

  public void setBsysflag(UFBoolean b) {
    this.setAttributeValue(ReserveVO.BSYSFLAG, b);
  }

  public void setCasscustid(String casscustid) {
    this.setAttributeValue(ReserveVO.CASSCUSTID, casscustid);
  }

  public void setCbizid(String cbizid) {
    this.setAttributeValue(ReserveVO.CBIZID, cbizid);
  }

  public void setCcustomerid(String ccustomerid) {
    this.setAttributeValue(ReserveVO.CCUSTOMERID, ccustomerid);
  }

  public void setClocationid(String clocationid) {
    this.setAttributeValue(ReserveVO.CLOCATIONID, clocationid);
  }

  public void setCmaterialoid(String cmaterialoid) {
    this.setAttributeValue(ReserveVO.CMATERIALOID, cmaterialoid);
  }

  public void setCmaterialvid(String cmaterialvid) {
    this.setAttributeValue(ReserveVO.CMATERIALVID, cmaterialvid);
  }

  public void setCproductorid(String cproductorid) {
    this.setAttributeValue(ReserveVO.CPRODUCTORID, cproductorid);
  }

  public void setCprojectid(String cprojectid) {
    this.setAttributeValue(ReserveVO.CPROJECTID, cprojectid);
  }

  public void setCreationtime(UFDateTime creationtime) {
    this.setAttributeValue(ReserveVO.CREATIONTIME, creationtime);
  }

  public void setCreator(String creator) {
    this.setAttributeValue(ReserveVO.CREATOR, creator);
  }

  public void setCreqbillbid(String cbillbid) {
    this.setAttributeValue(ReserveVO.CREQBILLBID, cbillbid);
  }

  public void setCreqbillid(String cbillid) {
    this.setAttributeValue(ReserveVO.CREQBILLID, cbillid);
  }

  public void setCreqbillrowno(String creqbillrowno) {
    this.setAttributeValue(ReserveVO.CREQBILLROWNO, creqbillrowno);
  }

  public void setCreqbilltype(String cbilltype) {
    this.setAttributeValue(ReserveVO.CREQBILLTYPE, cbilltype);
  }

  public void setCreqtranstype(String creqtranstype) {
    this.setAttributeValue(ReserveVO.CREQTRANSTYPE, creqtranstype);
  }

  public void setCsourcebillbid(String csourcebillbid) {
    this.setAttributeValue(ReserveVO.CSOURCEBILLBID, csourcebillbid);
  }

  public void setCsourcebillid(String csourcebillid) {
    this.setAttributeValue(ReserveVO.CSOURCEBILLID, csourcebillid);
  }

  public void setCsourcetype(String csourcetype) {
    this.setAttributeValue(ReserveVO.CSOURCETYPE, csourcetype);
  }

  public void setCtplcustomerid(String ctplcustomerid) {
    this.setAttributeValue(ReserveVO.CTPLCUSTOMERID, ctplcustomerid);
  }

  public void setCunitid(String cunitid) {
    this.setAttributeValue(ReserveVO.CUNITID, cunitid);
  }

  public void setCvendorid(String cvendorid) {
    this.setAttributeValue(ReserveVO.CVENDORID, cvendorid);
  }

  public void setCwarehouseid(String cwarehouseid) {
    this.setAttributeValue(ReserveVO.CWAREHOUSEID, cwarehouseid);
  }

  public void setDreqdate(UFDate drequiredate) {
    this.setAttributeValue(ReserveVO.DREQDATE, drequiredate);
  }

  public void setFreservetype(Integer freservetype) {
    this.setAttributeValue(ReserveVO.FRESERVETYPE, freservetype);
  }

  public void setFresstate(Integer fresstate) {
    this.setAttributeValue(ReserveVO.FRESSTATE, fresstate);
  }

  /**
   * 
   */
  @Override
  public void setHashCode(String vcode) {
    this.setVhashcode(vcode);
  }

  public void setModifiedtime(UFDateTime modifiedtime) {
    this.setAttributeValue(ReserveVO.MODIFIEDTIME, modifiedtime);
  }

  public void setModifier(String modifier) {
    this.setAttributeValue(ReserveVO.MODIFIER, modifier);
  }

  public void setNlacknum(UFDouble nlacknum) {
    this.setAttributeValue(ReserveVO.NLACKNUM, nlacknum);
  }

  public void setNreqnum(UFDouble nreqnum) {
    this.setAttributeValue(ReserveVO.NREQNUM, nreqnum);
  }

  public void setNreqrsnum(UFDouble d) {
    this.setAttributeValue(ReserveVO.NREQRSNUM, d);
  }

  public void setNrsnum(UFDouble nrsnum) {
    this.setAttributeValue(ReserveVO.NRSNUM, nrsnum);
  }

  public void setNtaloutnum(UFDouble ntaloutnum) {
    this.setAttributeValue(ReserveVO.NTALOUTNUM, ntaloutnum);
  }
  
  public void setNpscastnum(UFDouble Npscastnum) {
	    this.setAttributeValue(ReserveVO.NPSCASTNUM, Npscastnum);
	  }

  public void setNtalrsinnum(UFDouble ntalrsinnum) {
    this.setAttributeValue(ReserveVO.NTALRSINNUM, ntalrsinnum);
  }

  public void setNtalrsnum(UFDouble ntalrsnum) {
    this.setAttributeValue(ReserveVO.NTALRSNUM, ntalrsnum);
  }

  public void setNtalrsoutnum(UFDouble ntalrsoutnum) {
    this.setAttributeValue(ReserveVO.NTALRSOUTNUM, ntalrsoutnum);
  }

  public void setPk_batchcode(String pk_batchcode) {
    this.setAttributeValue(ReserveVO.PK_BATCHCODE, pk_batchcode);
  }

  public void setPk_group(String pk_group) {
    this.setAttributeValue(ReserveVO.PK_GROUP, pk_group);
  }

  public void setPk_org(String pk_org) {
    this.setAttributeValue(ReserveVO.PK_ORG, pk_org);
  }

  public void setPk_org_v(String pk_org_v) {
    this.setAttributeValue(ReserveVO.PK_ORG_V, pk_org_v);
  }

  public void setPk_reserve(String pk_reserve) {
    this.setAttributeValue(ReserveVO.PK_RESERVE, pk_reserve);
  }

  public void setReqbillrowts(UFDateTime reqbillts) {
    this.setAttributeValue(ReserveVO.REQBILLROWTS, reqbillts);
  }

  public void setTs(UFDateTime ts) {
    this.setAttributeValue(ReserveVO.TS, ts);
  }

  public void setVbatchcode(String vbatchcode) {
    this.setAttributeValue(ReserveVO.VBATCHCODE, vbatchcode);
  }

  public void setVfree1(String vfree1) {
    this.setAttributeValue(ReserveVO.VFREE1, vfree1);
  }

  public void setVfree10(String vfree10) {
    this.setAttributeValue(ReserveVO.VFREE10, vfree10);
  }

  public void setVfree2(String vfree2) {
    this.setAttributeValue(ReserveVO.VFREE2, vfree2);
  }

  public void setVfree3(String vfree3) {
    this.setAttributeValue(ReserveVO.VFREE3, vfree3);
  }

  public void setVfree4(String vfree4) {
    this.setAttributeValue(ReserveVO.VFREE4, vfree4);
  }

  public void setVfree5(String vfree5) {
    this.setAttributeValue(ReserveVO.VFREE5, vfree5);
  }

  public void setVfree6(String vfree6) {
    this.setAttributeValue(ReserveVO.VFREE6, vfree6);
  }

  public void setVfree7(String vfree7) {
    this.setAttributeValue(ReserveVO.VFREE7, vfree7);
  }

  public void setVfree8(String vfree8) {
    this.setAttributeValue(ReserveVO.VFREE8, vfree8);
  }

  public void setVfree9(String vfree9) {
    this.setAttributeValue(ReserveVO.VFREE9, vfree9);
  }

  public void setVhashcode(String vhashcode) {
    this.setAttributeValue(ReserveVO.VHASHCODE, vhashcode);
  }

  public void setVreqbillcode(String vreqbillcode) {
    this.setAttributeValue(ReserveVO.VREQBILLCODE, vreqbillcode);
  }

  public void setVrescode(String vrescode) {
    this.setAttributeValue(ReserveVO.VRESCODE, vrescode);
  }
}