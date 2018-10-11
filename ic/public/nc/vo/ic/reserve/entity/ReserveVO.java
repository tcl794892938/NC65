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
 * <b>需求预留</b>
 * 
 * @version v60
 * @since v60
 * @author yangb
 * @time 2010-4-26 下午08:39:40
 */

public class ReserveVO extends SuperVO implements IHashCodeVO, DimObj {

  // 是否系统生成
  public static final String BSYSFLAG = "bsysflag";

  // 客户辅助属性
  public static final String CASSCUSTID = "casscustid";

  // 业务员
  public static final String CBIZID = "cbizid";

  // 客户
  public static final String CCUSTOMERID = "ccustomerid";

  // 货位
  public static final String CLOCATIONID = "clocationid";

  // 物料
  public static final String CMATERIALOID = "cmaterialoid";

  // 物料版本
  public static final String CMATERIALVID = "cmaterialvid";

  // 生产厂家
  public static final String CPRODUCTORID = "cproductorid";

  // 项目
  public static final String CPROJECTID = "cprojectid";

  // 预留时间
  public static final String CREATIONTIME = "creationtime";

  // 创建人
  public static final String CREATOR = "creator";

  // 需求单据明细
  public static final String CREQBILLBID = "creqbillbid";

  // 需求单据头
  public static final String CREQBILLID = "creqbillid";

  // 需求单据行号
  public static final String CREQBILLROWNO = "creqbillrowno";

  // 需求单据类型
  public static final String CREQBILLTYPE = "creqbilltype";

  // 需求单据交易类型
  public static final String CREQTRANSTYPE = "creqtranstype";

  // 来源单据明细
  public static final String CSOURCEBILLBID = "csourcebillbid";

  // 来源单据头
  public static final String CSOURCEBILLID = "csourcebillid";

  // 来源单据类型
  public static final String CSOURCETYPE = "csourcetype";

  // 货主客户
  public static final String CTPLCUSTOMERID = "ctplcustomerid";

  // 主单位
  public static final String CUNITID = "cunitid";

  // 供应商
  public static final String CVENDORID = "cvendorid";

  // 仓库
  public static final String CWAREHOUSEID = "cwarehouseid";

  // 需求日期
  public static final String DREQDATE = "dreqdate";

  // 预留类型
  public static final String FRESERVETYPE = "freservetype";

  // 预留状态
  public static final String FRESSTATE = "fresstate";

  // 最后修改时间
  public static final String MODIFIEDTIME = "modifiedtime";

  // 最后修改人
  public static final String MODIFIER = "modifier";

  // 需求单据缺货数量=需求主数量-预留主数量
  public static final String NLACKNUM = "nlacknum";

  // 需求数量
  public static final String NREQNUM = "nreqnum";

  // 需求行累计预留数量 计算属性
  public static final String NREQRSNUM = "nreqrsnum";

  // 本次预留数量
  public static final String NRSNUM = "nrsnum";

  // 累计出库数量
  public static final String NTALOUTNUM = "ntaloutnum";
  
  // 累计委外数量
  public static final String NPSCASTNUM = "npscastnum";

  // 累计预留入库数量
  public static final String NTALRSINNUM = "ntalrsinnum";

  // 累计预留数量
  public static final String NTALRSNUM = "ntalrsnum";

  // 累计预留出库数量
  public static final String NTALRSOUTNUM = "ntalrsoutnum";
  

  public static final String[] numkeys = {
    ReserveVO.NREQNUM, ReserveVO.NTALOUTNUM, ReserveVO.NTALRSINNUM,
    ReserveVO.NTALRSNUM, ReserveVO.NTALRSOUTNUM,
  };

  // 预留人
  public static final String OPERATOR = "billmaker";

  // 批次
  public static final String PK_BATCHCODE = "pk_batchcode";

  // 集团
  public static final String PK_GROUP = "pk_group";

  // 库存组织
  public static final String PK_ORG = "pk_org";

  // 库存组织版本
  public static final String PK_ORG_V = "pk_org_v";

  // 预留
  public static final String PK_RESERVE = "pk_reserve";

  // 需求单据的TS
  public static final String REQBILLROWTS = "reqbillrowts";

  // 需求单据的现存量维度字段
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

  // 时间戳
  public static final String TS = "ts";

  // 批次号
  public static final String VBATCHCODE = "vbatchcode";

  // 物料自由属性1
  public static final String VFREE1 = "vfree1";

  // 物料自由属性10
  public static final String VFREE10 = "vfree10";

  // 物料自由属性2
  public static final String VFREE2 = "vfree2";

  // 物料自由属性3
  public static final String VFREE3 = "vfree3";

  // 物料自由属性4
  public static final String VFREE4 = "vfree4";

  // 物料自由属性5
  public static final String VFREE5 = "vfree5";

  // 物料自由属性6
  public static final String VFREE6 = "vfree6";

  // 物料自由属性7
  public static final String VFREE7 = "vfree7";

  // 物料自由属性8
  public static final String VFREE8 = "vfree8";

  // 物料自由属性9
  public static final String VFREE9 = "vfree9";

  // 散列码
  public static final String VHASHCODE = "vhashcode";

  // 需求单据号
  public static final String VREQBILLCODE = "vreqbillcode";

  // 预留记录号
  public static final String VRESCODE = "vrescode";
  
  //客户类型（2018-3-23新增）
  public static final String CUSTOMERTYPE="customertype";
  
  //备注（业务员）（2018-3-23新增）
  public static final String REMARKS ="remarks";

  private static final long serialVersionUID = 2010090211210051L;

  /**
   * ReserveVO 的构造子
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
  //客户类型（2018-3-23新增）
  public Integer getCustomertype(){
	  return (Integer) this.getAttributeValue(ReserveVO.CUSTOMERTYPE);
	  
  }
  //备注（业务员）（2018-3-23新增）
  public String getRemarks(){
	  return (String) this.getAttributeValue(ReserveVO.REMARKS);
  }
  
  //客户类型（2018-3-23新增）
  public void setCustomertype(Integer customertype){
	this.setAttributeValue(ReserveVO.CUSTOMERTYPE, customertype);
  }
  
//备注（业务员）（2018-3-23新增）
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