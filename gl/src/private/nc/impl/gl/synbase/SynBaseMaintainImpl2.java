package nc.impl.gl.synbase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.itf.gl.synbase.ISynBaseMaintain2;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.ui.gl.synbase.BaseDocType;
import nc.vo.bd.accassitem.AccAssItemVO;
import nc.vo.bd.account.AccAsoaVO;
import nc.vo.bd.account.AccAssVO;
import nc.vo.bd.account.AccountVO;
import nc.vo.bd.bankaccount.BankAccSubVO;
import nc.vo.bd.bankaccount.BankAccUseVO;
import nc.vo.bd.bankaccount.BankAccbasVO;
import nc.vo.bd.bankdoc.BankdocVO;
import nc.vo.bd.banktype.BankTypeVO;
import nc.vo.bd.cust.CustSupplierVO;
import nc.vo.bd.cust.CustomerVO;
import nc.vo.bd.cust.custorg.CustOrgVO;
import nc.vo.bd.cust.finance.CustFinanceVO;
import nc.vo.bd.defdoc.DefdocVO;
import nc.vo.bd.material.MaterialConvertVO;
import nc.vo.bd.material.MaterialVO;
import nc.vo.bd.material.MaterialVersionVO;
import nc.vo.bd.material.fi.MaterialFiVO;
import nc.vo.bd.material.marbasclass.MarBasClassVO;
import nc.vo.bd.material.marorg.MarOrgVO;
import nc.vo.bd.psn.PsndocVO;
import nc.vo.bd.psn.PsnjobVO;
import nc.vo.fl.voucher.TempBaseDocVO2;
import nc.vo.logging.Debug;
import nc.vo.org.DeptVO;
import nc.vo.org.OrgVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.sm.UserVO;
import nc.vo.vorg.DeptVersionVO;

public class SynBaseMaintainImpl2 implements ISynBaseMaintain2 {

	@SuppressWarnings("unchecked")
	@Override
	public void synBasedoc(String oldts, String newts, int type,TempBaseDocVO2 tempvo) throws BusinessException {
		
		BaseDAO daoA=new BaseDAO(BaseDocType.design_A);
		BaseDAO daoB=new BaseDAO(BaseDocType.design_C);
		
		String tableA=BaseDocType.accountA;
		
		String tableB=BaseDocType.accountC;
		
		String pk_user=BaseDocType.pk_user;
		
		if(type==BaseDocType.syn_dept){
			
			try {
				String strmsg="";
//==================================================先同步用户(user_name5做为关联键)===================================================
				
				//=====================查询当前在B中维度存在的用户==========================
				String exsql="select distinct user_code from sm_user s where s.ts>='"+oldts+"' and s.ts<='"+newts+"' "
						+ " and not exists(select 1 from "+tableB+".sm_user r where r.user_name5=s.cuserid) "
						+ " and exists(select 1 from "+tableB+".sm_user r where r.user_code=s.user_code)";
				
				List<String> list=(List<String>)daoA.executeQuery(exsql, new ColumnListProcessor());
				if(list!=null&&list.size()>0){
					
					String strinfo="";
					for(String str:list){
						strinfo+="["+str+"]";
					}
					
					tempvo.setDeptmsg("编码为"+strinfo+"的用户为手工创建，不能重复同步，请先删除或更改编码！");
					daoA.updateVO(tempvo);
					return ;
				}
				
//============================================查询用户表的新增和修改数据=================================
				String sql_user="select * from sm_user s where s.ts>='"+oldts+"' and s.ts<='"+newts+"'";
				String sql_user_b="select * from sm_user r where exists(select 1 from "+tableA+".sm_user s"
						+ " where r.user_name5=s.cuserid and s.ts>='"+oldts+"' and s.ts<='"+newts+"')";
				List<UserVO> userlist=(List<UserVO>)daoA.executeQuery(sql_user, new BeanListProcessor(UserVO.class));
				List<UserVO> userlistb=(List<UserVO>)daoB.executeQuery(sql_user_b, new BeanListProcessor(UserVO.class));
				
				Map<String, UserVO> usermap=new HashMap<String,UserVO>();
				for(UserVO vo:userlistb){
					usermap.put(vo.getUser_name5(), vo);
				}
				
				List<UserVO> addlist=new ArrayList<UserVO>();
				List<UserVO> updlist=new ArrayList<UserVO>();
				for(UserVO vo:userlist){
					
					if(usermap.containsKey(vo.getCuserid())){//修改保留主键和引用key
						UserVO upvo=usermap.get(vo.getCuserid());
						vo.setUser_name5(upvo.getUser_name5());
						vo.setCuserid(upvo.getCuserid());
						updlist.add(vo);
					}else{
						vo.setUser_name5(vo.getCuserid());
						vo.setCuserid(null);
						addlist.add(vo);
					}
				}
				
				if(addlist.size()>0){
					daoB.insertVOArray(addlist.toArray(new UserVO[0]));
				}
				if(updlist.size()>0){
					daoB.updateVOArray(updlist.toArray(new UserVO[0]));
				}
				strmsg +="用户表(sm_user):插入数据"+addlist.size()+"条，更新数据"+updlist.size()+"条。\n\t";
				
//=========================================================同步部门表(def20做为关联键)==================================
				
				//========================org_reportorg,org_reportorg_v不同步==================================
				//==================先查询部门涉及的业务单元====================================================
				String sqlunit="select distinct g.* from org_dept s left join v_tcl_org2 g on g.pk_org=s.pk_org "
						+ "where s.ts>='"+oldts+"' and s.ts<='"+newts+"' ";
				List<Map<String, Object>> mapunit=(List<Map<String, Object>>)daoA.executeQuery(sqlunit, new MapListProcessor());
				
				Map<String, String> maporg=new HashMap<String,String>();
				if(mapunit!=null&&mapunit.size()>0){
					String strinfo="";
					for(Map<String, Object> map:mapunit){
						if(map.get("pk_org2")==null){
							strinfo+="["+getStrobj(map.get("code"))+"]";
						}else{
							maporg.put(getStrobj(map.get("pk_org")), getStrobj(map.get("pk_org2")));
						}
					}
					
					if(!"".equals(strinfo)){
						tempvo.setDeptmsg("编码为"+strinfo+"的业务单元在税务帐套还未创建，请先去建账！");
						daoA.updateVO(tempvo);
						return ;
					}
				}
				
				//===========================查询编码重复问题(业务单元维度)====================================
				String exsql_dept="select distinct g.code||'-'||s.code from org_dept s left join v_tcl_org2 g on s.pk_org=g.pk_org "
						+ " where s.ts>='"+oldts+"' and s.ts<='"+newts+"' "
						+ " and not exists(select 1 from "+tableB+".org_dept r where r.def20=s.pk_dept) "
						+ " and exists(select 1 from "+tableB+".org_dept r where r.code=s.code "
						+ "and r.pk_org=g.pk_org2 and s.pk_org=g.pk_org)";
				
				List<String> listdept=(List<String>)daoA.executeQuery(exsql_dept, new ColumnListProcessor());
				if(listdept!=null&&listdept.size()>0){
					
					String strinfo="";
					for(String str:listdept){
						strinfo+="["+str+"]";
					}
					
					tempvo.setDeptmsg("税务帐套格式[公司-部门]:编码为"+strinfo+"的部门为手工创建，不能重复同步，请先删除或更改编码！");
					daoA.updateVO(tempvo);
					return ;
				}
				
//==================================================同步表org_dept=================================================
				String sql_dept="select * from org_dept s where s.ts>='"+oldts+"' and s.ts<='"+newts+"'";
				String sql_dept_b="select * from org_dept r where exists(select 1 from "+tableA+".org_dept s"
						+ " where r.def20=s.pk_dept and s.ts>='"+oldts+"' and s.ts<='"+newts+"')";
				List<DeptVO> deptlist=(List<DeptVO>)daoA.executeQuery(sql_dept, new BeanListProcessor(DeptVO.class));
				List<DeptVO> deptlistb=(List<DeptVO>)daoB.executeQuery(sql_dept_b, new BeanListProcessor(DeptVO.class));
				
				Map<String, DeptVO> deptmap=new HashMap<String,DeptVO>();
				for(DeptVO vo:deptlistb){
					deptmap.put(vo.getDef20(), vo);
				}
				
				List<DeptVO> addlist2=new ArrayList<DeptVO>();
				List<DeptVO> updlist2=new ArrayList<DeptVO>();
				for(DeptVO vo:deptlist){
					
					if(deptmap.containsKey(vo.getPk_dept())){//修改保留主键和引用key
						DeptVO upvo=deptmap.get(vo.getPk_dept());
						vo.setCreator(pk_user);
						vo.setModifier(pk_user);
						vo.setDef20(upvo.getDef20());
						vo.setPk_dept(upvo.getPk_dept());
						vo.setPk_org(maporg.get(vo.getPk_org()));
						vo.setPk_vid(upvo.getPk_vid());
						updlist2.add(vo);
					}else{
						String newpk="tcl"+vo.getPk_dept().substring(3);
						String newvid="tcl"+vo.getPk_vid().substring(3);
						vo.setCreator(pk_user);
						vo.setModifier(pk_user);
						vo.setDef20(vo.getPk_dept());
						vo.setPk_dept(newpk);
						vo.setPk_org(maporg.get(vo.getPk_org()));
						vo.setPk_vid(newvid);
						addlist2.add(vo);
					}
				}
				
				if(addlist2.size()>0){
					daoB.insertVOArrayWithPK(addlist2.toArray(new DeptVO[0]));
				}
				if(updlist2.size()>0){
					daoB.updateVOArray(updlist2.toArray(new DeptVO[0]));
				}
				//更新上级
				String upd_dept="update org_dept s set s.pk_fatherorg=(select pk_dept m from org_dept m where m.def20=s.pk_fatherorg) "
						+ "where s.ts>='"+oldts+"'  "//and s.ts<='"+newts+"'插入后的ts比newts大
						+ " and nvl(s.pk_fatherorg,'~')<>'~' and exists(select 1 from org_dept t where t.def20=s.pk_fatherorg)";
				daoB.executeUpdate(upd_dept);
				
				strmsg +="部门表(org_dept):插入数据"+addlist2.size()+"条，更新数据"+updlist2.size()+"条。\n\t";
				
//=================================================同步表org_dept_v=================================================
				String sql_dept_v="select * from org_dept_v s where s.ts>='"+oldts+"' and s.ts<='"+newts+"'";
				String sql_dept_v_b="select * from org_dept_v r where exists(select 1 from "+tableA+".org_dept_v s"
						+ " where r.def20=s.pk_dept and s.ts>='"+oldts+"' and s.ts<='"+newts+"')";
				List<DeptVersionVO> deptlist_v=(List<DeptVersionVO>)daoA.executeQuery(sql_dept_v, new BeanListProcessor(DeptVersionVO.class));
				List<DeptVersionVO> deptlistb_v=(List<DeptVersionVO>)daoB.executeQuery(sql_dept_v_b, new BeanListProcessor(DeptVersionVO.class));
				
				Map<String, DeptVersionVO> deptvmap=new HashMap<String,DeptVersionVO>();
				for(DeptVersionVO vo:deptlistb_v){
					deptvmap.put(vo.getDef20(), vo);
				}
				
				List<DeptVersionVO> addlist2v=new ArrayList<DeptVersionVO>();
				List<DeptVersionVO> updlist2v=new ArrayList<DeptVersionVO>();
				for(DeptVersionVO vo:deptlist_v){
					
					if(deptvmap.containsKey(vo.getPk_dept())){//修改保留主键和引用key
						DeptVersionVO upvo=deptvmap.get(vo.getPk_dept());
						vo.setCreator(pk_user);
						vo.setModifier(pk_user);
						vo.setDef20(vo.getPk_dept());//
						vo.setPk_dept(upvo.getPk_dept());
						vo.setPk_org(maporg.get(vo.getPk_org()));
						vo.setPk_vid(upvo.getPk_vid());
						updlist2v.add(vo);
					}else{
						String newpk="tcl"+vo.getPk_dept().substring(3);
						String newvid="tcl"+vo.getPk_vid().substring(3);
						vo.setCreator(pk_user);
						vo.setModifier(pk_user);
						vo.setDef20(vo.getPk_dept());//
						vo.setPk_dept(newpk);
						vo.setPk_org(maporg.get(vo.getPk_org()));
						vo.setPk_vid(newvid);
						addlist2v.add(vo);
					}
				}
				
				if(addlist2v.size()>0){
					daoB.insertVOArrayWithPK(addlist2v.toArray(new DeptVersionVO[0]));
				}
				if(updlist2v.size()>0){
					daoB.updateVOArray(updlist2v.toArray(new DeptVersionVO[0]));
				}
				
				//更新上级
				String upd_dept_v="update org_dept_v s set s.pk_fatherorg=(select pk_dept m from org_dept m where m.def20=s.pk_fatherorg) "
						+ "where s.ts>='"+oldts+"'  "//and s.ts<='"+newts+"'
						+ " and nvl(s.pk_fatherorg,'~')<>'~' and exists(select 1 from org_dept t where t.def20=s.pk_fatherorg)";
				daoB.executeUpdate(upd_dept_v);
				
				strmsg +="部门版本表(org_dept_v):插入数据"+addlist2v.size()+"条，更新数据"+updlist2v.size()+"条。\n\t";
				
//=================================================同步表org_org========================================================================
				String sql_org="select * from org_orgs s where nvl(s.pk_ownorg,'~')<>'~' and s.isbusinessunit='N' and s.ts>='"+oldts+"' and s.ts<='"+newts+"'";
				String sql_org_v="select * from org_orgs r where exists(select 1 from "+tableA+".org_orgs s"
						+ " where r.def20=s.pk_org and nvl(s.pk_ownorg,'~')<>'~' and s.isbusinessunit='N' and s.ts>='"+oldts+"' and s.ts<='"+newts+"')";
				List<OrgVO> orglist=(List<OrgVO>)daoA.executeQuery(sql_org, new BeanListProcessor(OrgVO.class));
				List<OrgVO> orglistb=(List<OrgVO>)daoB.executeQuery(sql_org_v, new BeanListProcessor(OrgVO.class));
				
				Map<String, OrgVO> orgmap=new HashMap<String,OrgVO>();
				for(OrgVO vo:orglistb){
					orgmap.put(vo.getDef20(), vo);
				}
				
				List<OrgVO> addlist3=new ArrayList<OrgVO>();
				List<OrgVO> updlist3=new ArrayList<OrgVO>();
				for(OrgVO vo:orglist){
					
					if(orgmap.containsKey(vo.getPk_org())){//修改保留主键和引用key
						OrgVO upvo=orgmap.get(vo.getPk_org());
						vo.setCreator(pk_user);
						vo.setModifier(pk_user);
						vo.setDef20(upvo.getDef20());
						vo.setPk_org(upvo.getPk_org());
						vo.setPk_ownorg(maporg.get(vo.getPk_ownorg()));
						vo.setPk_vid(upvo.getPk_vid());
						vo.setPk_corp(vo.getPk_ownorg());
						updlist3.add(vo);
					}else{
						String newpk="tcl"+vo.getPk_org().substring(3);
						String newvid="tcl"+vo.getPk_vid().substring(3);
						vo.setCreator(pk_user);
						vo.setModifier(pk_user);
						vo.setDef20(vo.getPk_org());
						vo.setPk_org(newpk);
						vo.setPk_ownorg(maporg.get(vo.getPk_ownorg()));
						vo.setPk_vid(newvid);
						vo.setPk_corp(vo.getPk_ownorg());
						addlist3.add(vo);
					}
				}
				
				if(addlist3.size()>0){
					daoB.insertVOArrayWithPK(addlist3.toArray(new OrgVO[0]));
				}
				if(updlist3.size()>0){
					daoB.updateVOArray(updlist3.toArray(new OrgVO[0]));
				}
				
				//更新上级
				String upd_org="update org_orgs s set s.pk_fatherorg=(select pk_dept m from org_dept m where m.def20=s.pk_fatherorg) "
						+ "where s.ts>='"+oldts+"' and nvl(s.pk_ownorg,'~')<>'~' and s.isbusinessunit='N' "//and s.ts<='"+newts+"'
						+ " and nvl(s.pk_fatherorg,'~')<>'~' and exists(select 1 from org_dept t where t.def20=s.pk_fatherorg)";
				daoB.executeUpdate(upd_org);
				
				strmsg +="部门组织表(org_org):插入数据"+addlist3.size()+"条，更新数据"+updlist3.size()+"条。";
				
				tempvo.setDeptmsg(strmsg);
				tempvo.setDept_ts(newts);
				
			} catch (Exception e) {
				Debug.debug(e.getMessage());
				tempvo.setDeptmsg(e.getMessage());
			}
		}
		
		
		/**
		 * 同步人员
		 */
		else if(type==BaseDocType.syn_psn){
			
			try {
				String strmsg="";

//=========================================================同步人员表(def20做为关联键)==================================
				
				//==================先查询人员涉及的业务单元====================================================
				String sqlunit="select distinct g.* from bd_psndoc s left join v_tcl_org2 g on g.pk_org=s.pk_org "
						+ "where s.ts>='"+oldts+"' and s.ts<='"+newts+"' ";
				List<Map<String, Object>> mapunit=(List<Map<String, Object>>)daoA.executeQuery(sqlunit, new MapListProcessor());
				
				Map<String, String> maporg=new HashMap<String,String>();
				if(mapunit!=null&&mapunit.size()>0){
					String strinfo="";
					for(Map<String, Object> map:mapunit){
						if(map.get("pk_org2")==null){
							strinfo+="["+getStrobj(map.get("code"))+"]";
						}else{
							maporg.put(getStrobj(map.get("pk_org")), getStrobj(map.get("pk_org2")));
						}
					}
					
					if(!"".equals(strinfo)){
						tempvo.setPsnmsg("编码为"+strinfo+"的业务单元在税务帐套还未创建，请先去建账！");
						daoA.updateVO(tempvo);
						return ;
					}
				}
				
				//===========================查询编码重复问题(集团维度)====================================
				String exsql_dept="select distinct s.code from bd_psndoc s "
						+ " where s.ts>='"+oldts+"' and s.ts<='"+newts+"' "
						+ " and not exists(select 1 from "+tableB+".bd_psndoc r where r.def20=s.pk_psndoc) "
						+ " and exists(select 1 from "+tableB+".bd_psndoc r where r.code=s.code)";
				
				List<String> listdept=(List<String>)daoA.executeQuery(exsql_dept, new ColumnListProcessor());
				if(listdept!=null&&listdept.size()>0){
					
					String strinfo="";
					for(String str:listdept){
						strinfo+="["+str+"]";
					}
					
					tempvo.setPsnmsg("税务帐套编码为"+strinfo+"的人员为手工创建，不能重复同步，请先删除或更改编码！");
					daoA.updateVO(tempvo);
					return ;
				}
				
//==================================================同步表bd_psndoc=================================================
				String sql_dept="select * from bd_psndoc s where s.ts>='"+oldts+"' and s.ts<='"+newts+"'";
				String sql_dept_b="select * from bd_psndoc r where exists(select 1 from "+tableA+".bd_psndoc s"
						+ " where r.def20=s.pk_psndoc and s.ts>='"+oldts+"' and s.ts<='"+newts+"')";
				List<PsndocVO> deptlist=(List<PsndocVO>)daoA.executeQuery(sql_dept, new BeanListProcessor(PsndocVO.class));
				List<PsndocVO> deptlistb=(List<PsndocVO>)daoB.executeQuery(sql_dept_b, new BeanListProcessor(PsndocVO.class));
				
				Map<String, PsndocVO> deptmap=new HashMap<String,PsndocVO>();
				for(PsndocVO vo:deptlistb){
					deptmap.put(vo.getDef20(), vo);
				}
				
				List<PsndocVO> addlist2=new ArrayList<PsndocVO>();
				List<PsndocVO> updlist2=new ArrayList<PsndocVO>();
				for(PsndocVO vo:deptlist){
					
					if(deptmap.containsKey(vo.getPk_psndoc())){//修改保留主键和引用key
						PsndocVO upvo=deptmap.get(vo.getPk_psndoc());
						vo.setCreator(pk_user);
						vo.setModifier(pk_user);
						vo.setDef20(vo.getPk_psndoc());
						vo.setPk_psndoc(upvo.getPk_psndoc());
						vo.setPk_org(maporg.get(vo.getPk_org()));
						updlist2.add(vo);
					}else{
						String newpk="tcl"+vo.getPk_psndoc().substring(3);
						vo.setCreator(pk_user);
						vo.setModifier(pk_user);
						vo.setDef20(vo.getPk_psndoc());
						vo.setPk_psndoc(newpk);
						vo.setPk_org(maporg.get(vo.getPk_org()));
						addlist2.add(vo);
					}
				}
				
				if(addlist2.size()>0){
					daoB.insertVOArrayWithPK(addlist2.toArray(new PsndocVO[0]));
				}
				if(updlist2.size()>0){
					daoB.updateVOArray(updlist2.toArray(new PsndocVO[0]));
				}
				
				strmsg +="人员表(bd_psndoc):插入数据"+addlist2.size()+"条，更新数据"+updlist2.size()+"条。\n\t";
				
//=================================================同步表psnjob=================================================
				
				//==================先查询人员涉及的业务单元====================================================
				String sqljob="select distinct g.* from bd_psnjob s left join v_tcl_org2 g on g.pk_org=s.pk_org "
						+ "where s.ts>='"+oldts+"' and s.ts<='"+newts+"' ";
				List<Map<String, Object>> mapjob=(List<Map<String, Object>>)daoA.executeQuery(sqljob, new MapListProcessor());
				
				Map<String, String> maporg2=new HashMap<String,String>();
				if(mapjob!=null&&mapjob.size()>0){
					String strinfo="";
					for(Map<String, Object> map:mapjob){
						if(map.get("pk_org2")==null){
							strinfo+="["+getStrobj(map.get("code"))+"]";
						}else{
							maporg2.put(getStrobj(map.get("pk_org")), getStrobj(map.get("pk_org2")));
						}
					}
					
					if(!"".equals(strinfo)){
						tempvo.setPsnmsg("编码为"+strinfo+"的业务单元在税务帐套还未创建，请先去建账！");
						daoA.updateVO(tempvo);
						return ;
					}
				}
				
				//==================先查询人员涉及的部门====================================================
				String sqldept="select distinct g.*,sss.code orgcode from bd_psnjob s left join v_tcl_dept2 g on g.pk_dept=s.pk_dept "
						+ " left join v_tcl_org2 sss on s.pk_org=sss.pk_org where s.ts>='"+oldts+"' and s.ts<='"+newts+"' ";
				List<Map<String, Object>> listdepts=(List<Map<String, Object>>)daoA.executeQuery(sqldept, new MapListProcessor());
				
				Map<String, String> mapdept=new HashMap<String,String>();
				if(listdepts!=null&&listdepts.size()>0){
					String strinfo="";
					for(Map<String, Object> map:listdepts){
						if(map.get("pk_dept2")==null){
							strinfo+="["+getStrobj(map.get("orgcode"))+"-"+getStrobj(map.get("code"))+"]";
						}else{
							mapdept.put(getStrobj(map.get("pk_dept")), getStrobj(map.get("pk_dept2")));
						}
					}
					
					if(!"".equals(strinfo)){
						tempvo.setPsnmsg("检测到[组织-部门]编码为"+strinfo+"的部门档案还未同步，请先去同步部门档案！");
						daoA.updateVO(tempvo);
						return ;
					}
				}
				
				//==================先查询人员任职编码重复====================================================
				String exsql="select distinct s.psncode from bd_psnjob s "
						+ " where s.ts>='"+oldts+"' and s.ts<='"+newts+"' "
						+ " and not exists(select 1 from "+tableB+".bd_psndoc r where r.def20=s.pk_psndoc) "
						+ " and exists(select 1 from "+tableB+".bd_psnjob r where r.psncode=s.psncode)";
				
				List<String> listdept2=(List<String>)daoA.executeQuery(exsql, new ColumnListProcessor());
				if(listdept2!=null&&listdept2.size()>0){
					
					String strinfo="";
					for(String str:listdept2){
						strinfo+="["+str+"]";
					}
					
					tempvo.setPsnmsg("税务帐套人员任职编码为"+strinfo+"为手工创建，不能重复同步，请先删除或更改编码！");
					daoA.updateVO(tempvo);
					return ;
				}
			
//==================================================同步表bd_psnjob=================================================
				String sql_dept_v="select * from bd_psnjob s where s.ts>='"+oldts+"' and s.ts<='"+newts+"'";
				String sql_dept_v_b="select * from bd_psnjob r where exists(select 1 from "+tableA+".bd_psnjob s"
						+ " where r.def20=s.pk_psnjob and s.ts>='"+oldts+"' and s.ts<='"+newts+"')";
				List<PsnjobVO> deptlist_v=(List<PsnjobVO>)daoA.executeQuery(sql_dept_v, new BeanListProcessor(PsnjobVO.class));
				List<PsnjobVO> deptlistb_v=(List<PsnjobVO>)daoB.executeQuery(sql_dept_v_b, new BeanListProcessor(PsnjobVO.class));
				
				Map<String, PsnjobVO> deptvmap=new HashMap<String,PsnjobVO>();
				for(PsnjobVO vo:deptlistb_v){
					deptvmap.put(vo.getDef20(), vo);
				}
				
				List<PsnjobVO> addlist2v=new ArrayList<PsnjobVO>();
				List<PsnjobVO> updlist2v=new ArrayList<PsnjobVO>();
				for(PsnjobVO vo:deptlist_v){
					
					if(deptvmap.containsKey(vo.getPk_psnjob())){//修改保留主键和引用key
						PsnjobVO upvo=deptvmap.get(vo.getPk_psnjob());
						vo.setDef20(vo.getPk_psnjob());//
						vo.setPk_dept(mapdept.get(vo.getPk_dept()));
						vo.setPk_org(maporg2.get(vo.getPk_org()));
						vo.setPk_psndoc(upvo.getPk_psndoc());
						vo.setPk_psnjob(upvo.getPk_psnjob());
						updlist2v.add(vo);
					}else{
						String newpk="tcl"+vo.getPk_psndoc().substring(3);
						String newvid="tcl"+vo.getPk_psnjob().substring(3);
						vo.setDef20(vo.getPk_psnjob());//
						vo.setPk_dept(mapdept.get(vo.getPk_dept()));
						vo.setPk_org(maporg2.get(vo.getPk_org()));
						vo.setPk_psndoc(newpk);
						vo.setPk_psnjob(newvid);
						addlist2v.add(vo);
					}
				}
				
				if(addlist2v.size()>0){
					daoB.insertVOArrayWithPK(addlist2v.toArray(new PsnjobVO[0]));
				}
				if(updlist2v.size()>0){
					daoB.updateVOArray(updlist2v.toArray(new PsnjobVO[0]));
				}
				
				strmsg +="人员任职表(bd_psnjob):插入数据"+addlist2v.size()+"条，更新数据"+updlist2v.size()+"条。";
				
				tempvo.setPsnmsg(strmsg);
				tempvo.setPsn_ts(newts);
				
			} catch (Exception e) {
				Debug.debug(e.getMessage());
				tempvo.setPsnmsg(e.getMessage());
			}
		}
		
		/**
		 * 同步客商
		 */
		else if(type==BaseDocType.syn_cust){
			
			try {
				String strmsg="";

//=========================================================同步客商表(def20做为关联键)不同步分类以及银行 账户==================================
				
				//==================先查询客户涉及的业务单元====================================================
				String sqlunit="select distinct g.* from v_tcl_org2 g ";
				List<Map<String, Object>> mapunit=(List<Map<String, Object>>)daoA.executeQuery(sqlunit, new MapListProcessor());
				
				Map<String, String> maporg=new HashMap<String,String>();
				if(mapunit!=null&&mapunit.size()>0){
					String strinfo="";
					for(Map<String, Object> map:mapunit){
						if(map.get("pk_org2")==null){
							strinfo+="["+getStrobj(map.get("code"))+"]";
						}else{
							maporg.put(getStrobj(map.get("pk_org")), getStrobj(map.get("pk_org2")));
						}
					}
					
					if(!"".equals(strinfo)){
						tempvo.setCustmsg("编码为"+strinfo+"的业务单元在税务帐套还未创建，请先去建账！");
						daoA.updateVO(tempvo);
						return ;
					}
				}
				
				//===========================查询编码重复问题(集团)====================================
				String exsql_dept="select distinct s.code from bd_customer s "
						+ " where s.ts>='"+oldts+"' and s.ts<='"+newts+"' "
						+ " and not exists(select 1 from "+tableB+".bd_customer r where r.def20=s.pk_customer) "
						+ " and (exists(select 1 from "+tableB+".bd_customer r where r.code=s.code) "
						+ " or exists(select 1 from "+tableB+".bd_cust_supplier r where r.code=s.code))";
				
				List<String> listdept=(List<String>)daoA.executeQuery(exsql_dept, new ColumnListProcessor());
				if(listdept!=null&&listdept.size()>0){
					
					String strinfo="";
					for(String str:listdept){
						strinfo+="["+str+"]";
					}
					
					tempvo.setCustmsg("税务帐套编码为"+strinfo+"的客商编码为手工创建，不能重复同步，请先删除或更改编码！");
					daoA.updateVO(tempvo);
					return ;
				}
				
//==================================================同步表bd_customer=================================================
				String sql_dept="select * from bd_customer s where s.ts>='"+oldts+"' and s.ts<='"+newts+"'";
				String sql_dept_b="select * from bd_customer r where exists(select 1 from "+tableA+".bd_customer s"
						+ " where r.def20=s.pk_customer and s.ts>='"+oldts+"' and s.ts<='"+newts+"')";
				List<CustomerVO> deptlist=(List<CustomerVO>)daoA.executeQuery(sql_dept, new BeanListProcessor(CustomerVO.class));
				List<CustomerVO> deptlistb=(List<CustomerVO>)daoB.executeQuery(sql_dept_b, new BeanListProcessor(CustomerVO.class));
				
				Map<String, CustomerVO> deptmap=new HashMap<String,CustomerVO>();
				for(CustomerVO vo:deptlistb){
					deptmap.put(vo.getDef20(), vo);
				}
				
				List<CustomerVO> addlist2=new ArrayList<CustomerVO>();
				List<CustomerVO> updlist2=new ArrayList<CustomerVO>();
				for(CustomerVO vo:deptlist){
					
					if(deptmap.containsKey(vo.getPk_customer())){//修改保留主键和引用key
						CustomerVO upvo=deptmap.get(vo.getPk_customer());
						vo.setCreator(pk_user);
						vo.setModifier(pk_user);
						vo.setDef20(vo.getPk_customer());
						vo.setPk_customer(upvo.getPk_customer());
						if(vo.getPk_financeorg()!=null){
							vo.setPk_financeorg(maporg.get(vo.getPk_financeorg()));
						}
						vo.setPk_supplier(upvo.getPk_supplier());
						vo.setIssupplier(upvo.getIssupplier());
						vo.setPk_org(maporg.get(vo.getPk_org()));
						updlist2.add(vo);
					}else{
						String newpk="tcl"+vo.getPk_customer().substring(3);
						vo.setCreator(pk_user);
						vo.setModifier(pk_user);
						vo.setDef20(vo.getPk_customer());
						vo.setPk_customer(newpk);
						if(vo.getPk_financeorg()!=null){
							vo.setPk_financeorg(maporg.get(vo.getPk_financeorg()));
						}
						vo.setPk_supplier(null);
						vo.setIssupplier(new UFBoolean(false));
						vo.setPk_org(maporg.get(vo.getPk_org()));
						addlist2.add(vo);
					}
				}
				
				if(addlist2.size()>0){
					daoB.insertVOArrayWithPK(addlist2.toArray(new CustomerVO[0]));
				}
				if(updlist2.size()>0){
					daoB.updateVOArray(updlist2.toArray(new CustomerVO[0]));
				}
				
				strmsg +="客户表(bd_customer):插入数据"+addlist2.size()+"条，更新数据"+updlist2.size()+"条。\n\t";
				
				//======================================客户财务页签=============================================
				
				//======================同步客户财务==========================================
				String sqlfc="select * from bd_custfinance s where s.ts>='"+oldts+"' and s.ts<='"+newts+"'";
				List<CustFinanceVO> cflist=(List<CustFinanceVO>)daoA.executeQuery(sqlfc, new BeanListProcessor(CustFinanceVO.class));
				List<CustFinanceVO> addlistf=new ArrayList<CustFinanceVO>();
				List<CustFinanceVO> updlistf=new ArrayList<CustFinanceVO>();
				for(CustFinanceVO vo:cflist){
					
					String pk_cust=vo.getPk_customer();
					String pk_org=vo.getPk_org();
					
					String sqlm="select r.pk_customer,r.code,rr.pk_customer pk_cust,g.pk_org,g.pk_org2 "
							+ " from bd_customer r left join "+tableB+".bd_customer rr on r.pk_customer=rr.def20 "
							+ " left join v_tcl_org2 g on r.pk_org=g.pk_org where r.pk_customer='"+pk_cust+"' ";
					Map<String, Object> map=(Map<String, Object>)daoA.executeQuery(sqlm, new MapProcessor());
					if(map==null){
						continue ;
					}
					Object obj=map.get("pk_cust");
					Object obj2=map.get("pk_org2");
					Object obj3=map.get("code");
					if(obj==null||obj2==null){
						tempvo.setCustmsg("客户编码为"+obj3+"的客户存在异常！，请联系用友人员！");
						daoA.updateVO(tempvo);
						return ;
					}
					
					String sql="select * from bd_custfinance e where e.pk_customer='"+obj+"' and pk_org='"+maporg.get(pk_org)+"' and nvl(dr,0)=0";
					CustFinanceVO vo1=(CustFinanceVO)daoB.executeQuery(sql, new BeanProcessor(CustFinanceVO.class));
					if(vo1==null){
						vo.setCreator(pk_user);
						vo.setModifier(pk_user);
						vo.setPk_org(maporg.get(pk_org));
						vo.setPk_customer(obj.toString());
						addlistf.add(vo);
					}else{
						vo.setCreator(pk_user);
						vo.setModifier(pk_user);
						vo.setPk_org(maporg.get(pk_org));
						vo.setPk_customer(obj.toString());
						updlistf.add(vo);
					}
					
				}
				
				if(addlistf.size()>0){
					daoB.insertVOArray(addlistf.toArray(new CustFinanceVO[0]));
				}
				if(updlistf.size()>0){
					daoB.updateVOArray(updlistf.toArray(new CustFinanceVO[0]));
				}
				
				strmsg +="客户财务表(bd_custfinance):插入数据"+addlistf.size()+"条，更新数据"+updlistf.size()+"条。\n\t";
				
				//======================================客户分配页签=============================================
				
				//======================同步客户分配==========================================
				String sqloc="select * from bd_custorg s where s.ts>='"+oldts+"' and s.ts<='"+newts+"'";
				List<CustOrgVO> oflist=(List<CustOrgVO>)daoA.executeQuery(sqloc, new BeanListProcessor(CustOrgVO.class));
				List<CustOrgVO> addlisto=new ArrayList<CustOrgVO>();
				List<CustOrgVO> updlisto=new ArrayList<CustOrgVO>();
				for(CustOrgVO vo:oflist){
					
					String pk_cust=vo.getPk_customer();
					String pk_org=vo.getPk_org();
					
					String sqlm="select r.pk_customer,r.code,rr.pk_customer pk_cust,g.pk_org,g.pk_org2 "
							+ " from bd_customer r left join "+tableB+".bd_customer rr on r.pk_customer=rr.def20 "
							+ " left join v_tcl_org2 g on r.pk_org=g.pk_org where r.pk_customer='"+pk_cust+"' ";
					Map<String, Object> map=(Map<String, Object>)daoA.executeQuery(sqlm, new MapProcessor());
					if(map==null){
						continue ;
					}
					Object obj=map.get("pk_cust");
					Object obj2=map.get("pk_org2");
					Object obj3=map.get("code");
					
					if(obj==null||obj2==null){
						tempvo.setCustmsg("客户编码为"+obj3+"的客户存在异常！，请联系用友人员！");
						daoA.updateVO(tempvo);
						return ;
					}
					
					String sql="select * from bd_custorg e where e.pk_customer='"+obj+"' and pk_org='"+maporg.get(pk_org)+"' and nvl(dr,0)=0";
					CustOrgVO vo1=(CustOrgVO)daoB.executeQuery(sql, new BeanProcessor(CustOrgVO.class));
					if(vo1==null){
						vo.setPk_org(maporg.get(pk_org));
						vo.setPk_customer(obj.toString());
						addlisto.add(vo);
					}else{
						vo.setPk_org(maporg.get(pk_org));
						vo.setPk_customer(obj.toString());
						updlisto.add(vo);
					}
				}
				
				if(addlisto.size()>0){
					daoB.insertVOArray(addlisto.toArray(new CustOrgVO[0]));
				}
				if(updlisto.size()>0){
					daoB.updateVOArray(updlisto.toArray(new CustOrgVO[0]));
				}
				
				strmsg +="客户组织表(bd_custorg):插入数据"+addlisto.size()+"条，更新数据"+updlisto.size()+"条。\n\t";
				
//==============================================同步bd_cust_supplier=============================================================

				String sqlfcs="select * from bd_cust_supplier s where s.ts>='"+oldts+"' and s.ts<='"+newts+"' and custsuptype in(1,3)";
				List<CustSupplierVO> cslist=(List<CustSupplierVO>)daoA.executeQuery(sqlfcs, new BeanListProcessor(CustSupplierVO.class));
				List<CustSupplierVO> addlistcs=new ArrayList<CustSupplierVO>();
				List<CustSupplierVO> updlistcs=new ArrayList<CustSupplierVO>();
				for(CustSupplierVO vo:cslist){
					
					String pk_cust=vo.getPk_cust_sup();
					//String pk_org=vo.getPk_org();
					
					String sqlm="select r.pk_customer,r.code,rr.pk_customer pk_cust,g.pk_org,g.pk_org2 "
							+ " from bd_customer r left join "+tableB+".bd_customer rr on r.pk_customer=rr.def20 "
							+ " left join v_tcl_org2 g on r.pk_org=g.pk_org where r.pk_customer='"+pk_cust+"' ";
					Map<String, Object> map=(Map<String, Object>)daoA.executeQuery(sqlm, new MapProcessor());
					Object obj=map.get("pk_cust");
					Object obj2=map.get("pk_org2");
					Object obj3=map.get("code");
					if(obj==null||obj2==null){
						tempvo.setCustmsg("客商编码为"+obj3+"的客户存在异常！，请联系用友人员！");
						daoA.updateVO(tempvo);
						return ;
					}
					
					String sql="select * from bd_cust_supplier e where e.pk_cust_sup='"+obj+"' and nvl(dr,0)=0";
					CustSupplierVO vo1=(CustSupplierVO)daoB.executeQuery(sql, new BeanProcessor(CustSupplierVO.class));
					if(vo1==null){
						String newpk="tcl"+vo.getPk_cust_sup().substring(3);
						vo.setCustsuptype(1);
						vo.setPk_supplierclass(null);
						vo.setPk_cust_sup(newpk);
						if(vo.getPk_financeorg()!=null){
							vo.setPk_financeorg(maporg.get(vo.getPk_financeorg()));
						}
						vo.setPk_org(obj2.toString());
						addlistcs.add(vo);
					}else{
						vo.setCustsuptype(vo1.getCustsuptype());
						vo.setPk_cust_sup(vo1.getPk_cust_sup());
						vo.setPk_supplierclass(vo1.getPk_supplierclass());
						if(vo.getPk_financeorg()!=null){
							vo.setPk_financeorg(maporg.get(vo.getPk_financeorg()));
						}
						vo.setPk_org(obj2.toString());
						updlistcs.add(vo);
					}
					
				}
				
				if(addlistcs.size()>0){
					daoB.insertVOArrayWithPK(addlistcs.toArray(new CustSupplierVO[0]));
				}
				if(updlistcs.size()>0){
					daoB.updateVOArray(updlistcs.toArray(new CustSupplierVO[0]));
				}
				
				strmsg +="客商表(bd_cust_supplier):插入数据"+addlistcs.size()+"条，更新数据"+updlistcs.size()+"条。";
				
				tempvo.setCustmsg(strmsg);
				tempvo.setCust_ts(newts);
				
			} catch (Exception e) {
				Debug.debug(e.getMessage());
				tempvo.setCustmsg(e.getMessage());
			}
		}
		
		
		else if(type==BaseDocType.syn_material){
			
			try {
				String strmsg="";

//=========================================================同步物料分类表(def2做为关联键)===============================================
				
				//==================先查询分类涉及的业务单元====================================================
				String sqlunit="select distinct g.* from v_tcl_org2 g ";
				List<Map<String, Object>> mapunit=(List<Map<String, Object>>)daoA.executeQuery(sqlunit, new MapListProcessor());
				
				Map<String, String> maporg=new HashMap<String,String>();
				if(mapunit!=null&&mapunit.size()>0){
					String strinfo="";
					for(Map<String, Object> map:mapunit){
						if(map.get("pk_org2")==null){
							strinfo+="["+getStrobj(map.get("code"))+"]";
						}else{
							maporg.put(getStrobj(map.get("pk_org")), getStrobj(map.get("pk_org2")));
						}
					}
					
					if(!"".equals(strinfo)){
						tempvo.setMaterialmsg("编码为"+strinfo+"的业务单元在税务帐套还未创建，请先去建账！");
						daoA.updateVO(tempvo);
						return ;
					}
				}
				
				//===========================查询编码重复问题(业务单元)====================================
				String exsql_dept="select distinct g.code||'-'||s.code from bd_marbasclass s left join v_tcl_org2 g on s.pk_org=g.pk_org "
						+ " where s.ts>='"+oldts+"' and s.ts<='"+newts+"' "
						+ " and not exists(select 1 from "+tableB+".bd_marbasclass r where r.def2=s.pk_marbasclass) "
						+ " and exists(select 1 from "+tableB+".bd_marbasclass r where r.code=s.code "
						+ "and r.pk_org=g.pk_org2 and s.pk_org=g.pk_org)";
				
				List<String> listdept=(List<String>)daoA.executeQuery(exsql_dept, new ColumnListProcessor());
				if(listdept!=null&&listdept.size()>0){
					
					String strinfo="";
					for(String str:listdept){
						strinfo+="["+str+"]";
					}
					
					tempvo.setMaterialmsg("税务帐套格式[组织-物料分类]:编码为"+strinfo+"的分类为手工创建，不能重复同步，请先删除或更改编码！");
					daoA.updateVO(tempvo);
					return ;
				}
				
//==================================================同步表bd_marbasclass====================================================
				String sql_dept="select * from bd_marbasclass s where s.ts>='"+oldts+"' and s.ts<='"+newts+"'";
				String sql_dept_b="select * from bd_marbasclass r where exists(select 1 from "+tableA+".bd_marbasclass s"
						+ " where r.def2=s.pk_marbasclass and s.ts>='"+oldts+"' and s.ts<='"+newts+"')";
				List<MarBasClassVO> deptlist=(List<MarBasClassVO>)daoA.executeQuery(sql_dept, new BeanListProcessor(MarBasClassVO.class));
				List<MarBasClassVO> deptlistb=(List<MarBasClassVO>)daoB.executeQuery(sql_dept_b, new BeanListProcessor(MarBasClassVO.class));
				
				Map<String, MarBasClassVO> deptmap=new HashMap<String,MarBasClassVO>();
				for(MarBasClassVO vo:deptlistb){
					deptmap.put(vo.getDef2(), vo);
				}
				
				List<MarBasClassVO> addlist2=new ArrayList<MarBasClassVO>();
				List<MarBasClassVO> updlist2=new ArrayList<MarBasClassVO>();
				for(MarBasClassVO vo:deptlist){
					
					if(deptmap.containsKey(vo.getPk_marbasclass())){//修改保留主键和引用key
						MarBasClassVO upvo=deptmap.get(vo.getPk_marbasclass());
						vo.setCreator(pk_user);
						vo.setModifier(pk_user);
						vo.setDef2(upvo.getDef2());
						vo.setPk_marbasclass(upvo.getPk_marbasclass());
						vo.setPk_org(maporg.get(vo.getPk_org()));
						updlist2.add(vo);
					}else{
						String newpk="tcl"+vo.getPk_marbasclass().substring(3);
						vo.setCreator(pk_user);
						vo.setModifier(pk_user);
						vo.setDef2(vo.getPk_marbasclass());
						vo.setPk_marbasclass(newpk);
						vo.setPk_org(maporg.get(vo.getPk_org()));
						addlist2.add(vo);
					}
				}
				
				if(addlist2.size()>0){
					daoB.insertVOArrayWithPK(addlist2.toArray(new MarBasClassVO[0]));
				}
				if(updlist2.size()>0){
					daoB.updateVOArray(updlist2.toArray(new MarBasClassVO[0]));
				}
				//更新上级
				String upd_dept="update bd_marbasclass s set s.pk_parent=(select pk_marbasclass m from bd_marbasclass m where m.def2=s.pk_parent) "
						+ "where s.ts>='"+oldts+"'  "//and s.ts<='"+newts+"'插入后的ts比newts大
						+ " and nvl(s.pk_parent,'~')<>'~' and exists(select 1 from bd_marbasclass t where t.def2=s.pk_parent)";
				daoB.executeUpdate(upd_dept);
				
				strmsg +="物料分类表(bd_marbasclass):插入数据"+addlist2.size()+"条，更新数据"+updlist2.size()+"条。\n\t";
				
//============================================================物料信息bd_material======================================================================
				
				//===========================查询编码重复问题(集团)====================================
				String exsql_rep="select distinct s.code from bd_material s "
						+ " where s.ts>='"+oldts+"' and s.ts<='"+newts+"' "
						+ " and not exists(select 1 from "+tableB+".bd_material r where r.def20=s.pk_material) "
						+ " and exists(select 1 from "+tableB+".bd_material r where r.code=s.code) ";
				
				List<String> listrep=(List<String>)daoA.executeQuery(exsql_rep, new ColumnListProcessor());
				if(listrep!=null&&listrep.size()>0){
					
					String strinfo="";
					for(String str:listrep){
						strinfo+="["+str+"]";
					}
					
					tempvo.setMaterialmsg("税务帐套编码为"+strinfo+"的物料编码为手工创建，不能重复同步，请先删除或更改编码！");
					daoA.updateVO(tempvo);
					return ;
				}
				
				//==================先查询物料涉及的分类====================================================
				String sqldept="select distinct g.*,sss.code orgcode from bd_material s left join v_tcl_marbasclass2 g on g.pk_marbasclass=s.pk_marbasclass "
						+ " left join v_tcl_org2 sss on s.pk_org=sss.pk_org where s.ts>='"+oldts+"' and s.ts<='"+newts+"'  ";
				List<Map<String, Object>> listclass=(List<Map<String, Object>>)daoA.executeQuery(sqldept, new MapListProcessor());
				
				Map<String, String> mapclass=new HashMap<String,String>();
				if(listclass!=null&&listclass.size()>0){
					String strinfo="";
					for(Map<String, Object> map:listclass){
						if(map.get("pk_marbasclass2")==null){
							strinfo+="["+getStrobj(map.get("orgcode"))+"-"+getStrobj(map.get("code"))+"]";
						}else{
							mapclass.put(getStrobj(map.get("pk_marbasclass")), getStrobj(map.get("pk_marbasclass2")));
						}
					}
					
					if(!"".equals(strinfo)){
						tempvo.setMaterialmsg("检测到编码为"+strinfo+"的物料分类还未同步，请先去同步物料分类档案！");
						daoA.updateVO(tempvo);
						return ;
					}
				}
				
				//==================================================同步表bd_material=================================================
				String sql_dept_v="select * from bd_material s where s.ts>='"+oldts+"' and s.ts<='"+newts+"'";
				String sql_dept_v_b="select * from bd_material r where exists(select 1 from "+tableA+".bd_material s"
						+ " where r.def20=s.pk_material and s.ts>='"+oldts+"' and s.ts<='"+newts+"')";
				List<MaterialVO> deptlist_v=(List<MaterialVO>)daoA.executeQuery(sql_dept_v, new BeanListProcessor(MaterialVO.class));
				List<MaterialVO> deptlistb_v=(List<MaterialVO>)daoB.executeQuery(sql_dept_v_b, new BeanListProcessor(MaterialVO.class));
				
				Map<String, MaterialVO> deptvmap=new HashMap<String,MaterialVO>();
				for(MaterialVO vo:deptlistb_v){
					deptvmap.put(vo.getDef20(), vo);
				}
				
				List<MaterialVO> addlist2v=new ArrayList<MaterialVO>();
				List<MaterialVO> updlist2v=new ArrayList<MaterialVO>();
				for(MaterialVO vo:deptlist_v){
					
					if(deptvmap.containsKey(vo.getPk_material())){//修改保留主键和引用key
						MaterialVO upvo=deptvmap.get(vo.getPk_material());
						vo.setCreator(pk_user);
						vo.setModifier(pk_user);
						vo.setDef20(vo.getPk_material());//
						vo.setPk_marbasclass(mapclass.get(vo.getPk_marbasclass()));
						vo.setPk_material(upvo.getPk_material());
						vo.setPk_source(upvo.getPk_source());
						vo.setPk_org(maporg.get(vo.getPk_org()));
						updlist2v.add(vo);
					}else{
						String newpk="tcl"+vo.getPk_material().substring(3);
						vo.setCreator(pk_user);
						vo.setModifier(pk_user);
						vo.setDef20(vo.getPk_material());//
						vo.setPk_marbasclass(mapclass.get(vo.getPk_marbasclass()));
						vo.setPk_material(newpk);
						vo.setPk_source(newpk);
						vo.setPk_org(maporg.get(vo.getPk_org()));
						addlist2v.add(vo);
					}
				}
				
				if(addlist2v.size()>0){
					daoB.insertVOArrayWithPK(addlist2v.toArray(new MaterialVO[0]));
				}
				if(updlist2v.size()>0){
					daoB.updateVOArray(updlist2v.toArray(new MaterialVO[0]));
				}
				
				strmsg +="物料表(bd_material):插入数据"+addlist2v.size()+"条，更新数据"+updlist2v.size()+"条。\n\t";
				
				//==================================================同步表bd_material_v=================================================
				String sql_dept_v2="select * from bd_material_v s where s.ts>='"+oldts+"' and s.ts<='"+newts+"'";
				String sql_dept_v_b2="select * from bd_material_v r where exists(select 1 from "+tableA+".bd_material_v s"
						+ " where r.def20=s.pk_material and s.ts>='"+oldts+"' and s.ts<='"+newts+"')";
				List<MaterialVersionVO> deptlist_v2=(List<MaterialVersionVO>)daoA.executeQuery(sql_dept_v2, new BeanListProcessor(MaterialVersionVO.class));
				List<MaterialVersionVO> deptlistb_v2=(List<MaterialVersionVO>)daoB.executeQuery(sql_dept_v_b2, new BeanListProcessor(MaterialVersionVO.class));
				
				Map<String, MaterialVersionVO> deptv2map=new HashMap<String,MaterialVersionVO>();
				for(MaterialVersionVO vo:deptlistb_v2){
					deptv2map.put(vo.getDef20(), vo);
				}
				
				List<MaterialVersionVO> addlist2v2=new ArrayList<MaterialVersionVO>();
				List<MaterialVersionVO> updlist2v2=new ArrayList<MaterialVersionVO>();
				for(MaterialVersionVO vo:deptlist_v2){
					
					if(deptv2map.containsKey(vo.getPk_material())){//修改保留主键和引用key
						MaterialVersionVO upvo=deptv2map.get(vo.getPk_material());
						vo.setCreator(pk_user);
						vo.setModifier(pk_user);
						vo.setDef20(vo.getPk_material());//
						vo.setPk_marbasclass(mapclass.get(vo.getPk_marbasclass()));
						vo.setPk_material(upvo.getPk_material());
						vo.setPk_source(upvo.getPk_source());
						vo.setPk_org(maporg.get(vo.getPk_org()));
						updlist2v2.add(vo);
					}else{
						String newpk="tcl"+vo.getPk_material().substring(3);
						vo.setCreator(pk_user);
						vo.setModifier(pk_user);
						vo.setDef20(vo.getPk_material());//
						vo.setPk_marbasclass(mapclass.get(vo.getPk_marbasclass()));
						vo.setPk_material(newpk);
						vo.setPk_source(newpk);
						vo.setPk_org(maporg.get(vo.getPk_org()));
						addlist2v2.add(vo);
					}
				}
				
				if(addlist2v2.size()>0){
					daoB.insertVOArrayWithPK(addlist2v2.toArray(new MaterialVersionVO[0]));
				}
				if(updlist2v2.size()>0){
					daoB.updateVOArray(updlist2v2.toArray(new MaterialVersionVO[0]));
				}
				
				strmsg +="物料版本表(bd_material_v):插入数据"+addlist2v2.size()+"条，更新数据"+updlist2v2.size()+"条。\n\t";
				
				//======================================物料计量单位bd_materialconvert=============================================
				String sqlfc="select * from bd_materialconvert s where s.ts>='"+oldts+"' and s.ts<='"+newts+"'";
				List<MaterialConvertVO> cflist=(List<MaterialConvertVO>)daoA.executeQuery(sqlfc, new BeanListProcessor(MaterialConvertVO.class));
				List<MaterialConvertVO> addlistf=new ArrayList<MaterialConvertVO>();
				List<MaterialConvertVO> updlistf=new ArrayList<MaterialConvertVO>();
				for(MaterialConvertVO vo:cflist){
					
					String pk_mat=vo.getPk_material();
					String pk_mes=vo.getPk_measdoc();
					
					String sqlm="select r.pk_material,r.code,rr.pk_material pk_mate "
							+ " from bd_material r left join "+tableB+".bd_material rr on r.pk_material=rr.def20 "
							+ "  where r.pk_material='"+pk_mat+"' ";
					Map<String, Object> map=(Map<String, Object>)daoA.executeQuery(sqlm, new MapProcessor());
					if(map==null){
						continue ;
					}
					Object obj=map.get("pk_mate");
					Object obj3=map.get("code");
					if(obj==null){
						tempvo.setMaterialmsg("物料编码为"+obj3+"的物料存在异常(待同步)！，请联系用友人员！");
						daoA.updateVO(tempvo);
						return ;
					}
					
					String sql="select * from bd_materialconvert e where e.pk_material='"+obj+"' and pk_measdoc='"+pk_mes+"' and nvl(dr,0)=0";
					MaterialConvertVO vo1=(MaterialConvertVO)daoB.executeQuery(sql, new BeanProcessor(MaterialConvertVO.class));
					if(vo1==null){
						vo.setPk_material(obj.toString());
						vo.setPk_measdoc(pk_mes);
						addlistf.add(vo);
					}else{
						vo.setPk_material(obj.toString());
						vo.setPk_measdoc(pk_mes);
						updlistf.add(vo);
					}
					
				}
				
				if(addlistf.size()>0){
					daoB.insertVOArray(addlistf.toArray(new MaterialConvertVO[0]));
				}
				if(updlistf.size()>0){
					daoB.updateVOArray(updlistf.toArray(new MaterialConvertVO[0]));
				}
				
				strmsg +="物料单位表(bd_materialconvert):插入数据"+addlistf.size()+"条，更新数据"+updlistf.size()+"条。\n\t";
				
				//===========================================物料财务bd_materialfi==========================================
				String sqlfc2="select * from bd_materialfi s where s.ts>='"+oldts+"' and s.ts<='"+newts+"'";
				List<MaterialFiVO> cflist2=(List<MaterialFiVO>)daoA.executeQuery(sqlfc2, new BeanListProcessor(MaterialFiVO.class));
				List<MaterialFiVO> addlistf2=new ArrayList<MaterialFiVO>();
				List<MaterialFiVO> updlistf2=new ArrayList<MaterialFiVO>();
				for(MaterialFiVO vo:cflist2){
					
					String pk_cust=vo.getPk_material();
					String pk_org=vo.getPk_org();
					
					String sqlm="select r.pk_material,r.code,rr.pk_material pk_cust,g.pk_org,g.pk_org2 "
							+ " from bd_material r left join "+tableB+".bd_material rr on r.pk_material=rr.def20 "
							+ " left join v_tcl_org2 g on r.pk_org=g.pk_org where r.pk_material='"+pk_cust+"' ";
					Map<String, Object> map=(Map<String, Object>)daoA.executeQuery(sqlm, new MapProcessor());
					if(map==null){
						continue ;
					}
					Object obj=map.get("pk_cust");
					Object obj2=map.get("pk_org2");
					Object obj3=map.get("code");
					if(obj==null||obj2==null){
						tempvo.setMaterialmsg("物料编码为"+obj3+"的物料存在异常！，请联系用友人员！");
						daoA.updateVO(tempvo);
						return ;
					}
					
					String sql="select * from bd_materialfi e where e.pk_material='"+obj+"' and pk_org='"+maporg.get(pk_org)+"' and nvl(dr,0)=0";
					MaterialFiVO vo1=(MaterialFiVO)daoB.executeQuery(sql, new BeanProcessor(MaterialFiVO.class));
					if(vo1==null){
						vo.setCreator(pk_user);
						vo.setModifier(pk_user);
						vo.setPk_org(maporg.get(pk_org));
						vo.setPk_material(obj.toString());
						addlistf2.add(vo);
					}else{
						vo.setCreator(pk_user);
						vo.setModifier(pk_user);
						vo.setPk_org(maporg.get(pk_org));
						vo.setPk_material(obj.toString());
						updlistf2.add(vo);
					}
					
				}
				
				if(addlistf2.size()>0){
					daoB.insertVOArray(addlistf2.toArray(new MaterialFiVO[0]));
				}
				if(updlistf2.size()>0){
					daoB.updateVOArray(updlistf2.toArray(new MaterialFiVO[0]));
				}
				
				strmsg +="物料财务表(bd_materialfi):插入数据"+addlistf2.size()+"条，更新数据"+updlistf2.size()+"条。\n\t";
				
				//===========================================同步物料分配bd_marorg==========================================
				String sqloc="select * from bd_marorg s where s.ts>='"+oldts+"' and s.ts<='"+newts+"'";
				List<MarOrgVO> oflist=(List<MarOrgVO>)daoA.executeQuery(sqloc, new BeanListProcessor(MarOrgVO.class));
				List<MarOrgVO> addlisto=new ArrayList<MarOrgVO>();
				List<MarOrgVO> updlisto=new ArrayList<MarOrgVO>();
				for(MarOrgVO vo:oflist){
					
					String pk_cust=vo.getPk_material();
					String pk_org=vo.getPk_org();
					
					String sqlm="select r.pk_material,r.code,rr.pk_material pk_cust,g.pk_org,g.pk_org2 "
							+ " from bd_material r left join "+tableB+".bd_material rr on r.pk_material=rr.def20 "
							+ " left join v_tcl_org2 g on r.pk_org=g.pk_org where r.pk_material='"+pk_cust+"' ";
					Map<String, Object> map=(Map<String, Object>)daoA.executeQuery(sqlm, new MapProcessor());
					if(map==null){
						continue ;
					}
					Object obj=map.get("pk_cust");
					Object obj2=map.get("pk_org2");
					Object obj3=map.get("code");
					if(obj==null||obj2==null){
						tempvo.setMaterialmsg("物料编码为"+obj3+"的物料存在异常！，请联系用友人员！");
						daoA.updateVO(tempvo);
						return ;
					}
					
					String sql="select * from bd_marorg e where e.pk_material='"+obj+"' and pk_org='"+maporg.get(pk_org)+"' and nvl(dr,0)=0";
					MarOrgVO vo1=(MarOrgVO)daoB.executeQuery(sql, new BeanProcessor(MarOrgVO.class));
					if(vo1==null){
						vo.setPk_org(maporg.get(pk_org));
						vo.setPk_material(obj.toString());
						addlisto.add(vo);
					}else{
						vo.setPk_org(maporg.get(pk_org));
						vo.setPk_material(obj.toString());
						updlisto.add(vo);
					}
				}
				
				if(addlisto.size()>0){
					daoB.insertVOArray(addlisto.toArray(new MarOrgVO[0]));
				}
				if(updlisto.size()>0){
					daoB.updateVOArray(updlisto.toArray(new MarOrgVO[0]));
				}
				
				strmsg +="物料分配表(bd_marorg):插入数据"+addlisto.size()+"条，更新数据"+updlisto.size()+"条。";
				
				tempvo.setMaterialmsg(strmsg);
				tempvo.setMaterial_ts(newts);
				
			} catch (Exception e) {
				Debug.debug(e.getMessage());
				tempvo.setMaterialmsg(e.getMessage());
			}
		}
		
		else if(type==BaseDocType.syn_account){
			
			try {
				String strmsg="";
				//===========================查询编码重复问题(全局)====================================
				String exsql_dept="select distinct code from bd_banktype s "
						+ " where s.ts>='"+oldts+"' and s.ts<='"+newts+"' "
						+ " and not exists(select 1 from "+tableB+".bd_banktype r where r.def2=s.pk_banktype) "
						+ " and exists(select 1 from "+tableB+".bd_banktype r where r.code=s.code )";
				
				List<String> listdept=(List<String>)daoA.executeQuery(exsql_dept, new ColumnListProcessor());
				if(listdept!=null&&listdept.size()>0){
					
					String strinfo="";
					for(String str:listdept){
						strinfo+="["+str+"]";
					}
					
					tempvo.setAccountmsg("税务帐套编码为"+strinfo+"的银行类别档案为手工创建，不能重复同步，请先删除或更改编码！");
					daoA.updateVO(tempvo);
					return ;
				}
				
	//==================================================同步表bd_banktype====================================================
				String sql_dept="select * from bd_banktype s where s.ts>='"+oldts+"' and s.ts<='"+newts+"'";
				String sql_dept_b="select * from bd_banktype r where exists(select 1 from "+tableA+".bd_banktype s"
						+ " where r.def2=s.pk_banktype and s.ts>='"+oldts+"' and s.ts<='"+newts+"')";
				List<BankTypeVO> deptlist=(List<BankTypeVO>)daoA.executeQuery(sql_dept, new BeanListProcessor(BankTypeVO.class));
				List<BankTypeVO> deptlistb=(List<BankTypeVO>)daoB.executeQuery(sql_dept_b, new BeanListProcessor(BankTypeVO.class));
				
				Map<String, BankTypeVO> deptmap=new HashMap<String,BankTypeVO>();
				for(BankTypeVO vo:deptlistb){
					deptmap.put(vo.getDef2(), vo);
				}
				
				List<BankTypeVO> addlist2=new ArrayList<BankTypeVO>();
				List<BankTypeVO> updlist2=new ArrayList<BankTypeVO>();
				for(BankTypeVO vo:deptlist){
					
					if(deptmap.containsKey(vo.getPk_banktype())){//修改保留主键和引用key
						BankTypeVO upvo=deptmap.get(vo.getPk_banktype());
						vo.setCreator(pk_user);
						vo.setModifier(pk_user);
						vo.setDef2(upvo.getDef2());
						vo.setPk_banktype(upvo.getPk_banktype());
						updlist2.add(vo);
					}else{
						String newpk="tcl"+vo.getPk_banktype().substring(3);
						vo.setCreator(pk_user);
						vo.setModifier(pk_user);
						vo.setDef2(vo.getPk_banktype());
						vo.setPk_banktype(newpk);
						addlist2.add(vo);
					}
				}
				
				if(addlist2.size()>0){
					daoB.insertVOArrayWithPK(addlist2.toArray(new BankTypeVO[0]));
				}
				if(updlist2.size()>0){
					daoB.updateVOArray(updlist2.toArray(new BankTypeVO[0]));
				}
				
				strmsg +="银行类别档案(bd_banktype):插入数据"+addlist2.size()+"条，更新数据"+updlist2.size()+"条。\n\t";
				
//============================================================银行档案bd_bankdoc======================================================================
				
				//===========================查询编码重复问题(全局)====================================
				String exsql_rep="select distinct s.code from bd_bankdoc s "
						+ " where s.ts>='"+oldts+"' and s.ts<='"+newts+"' "
						+ " and not exists(select 1 from "+tableB+".bd_bankdoc r where r.def2=s.pk_bankdoc) "
						+ " and exists(select 1 from "+tableB+".bd_bankdoc r where r.code=s.code) ";
				
				List<String> listrep=(List<String>)daoA.executeQuery(exsql_rep, new ColumnListProcessor());
				if(listrep!=null&&listrep.size()>0){
					
					String strinfo="";
					for(String str:listrep){
						strinfo+="["+str+"]";
					}
					
					tempvo.setAccountmsg("税务帐套编码为"+strinfo+"的银行档案为手工创建，不能重复同步，请先删除或更改编码！");
					daoA.updateVO(tempvo);
					return ;
				}
				
				//==================先查询档案涉及的类别====================================================
				String sqldept="select distinct g.* from bd_bankdoc s left join v_tcl_banktype2 g on g.pk_banktype=s.pk_banktype "
						+ " where s.ts>='"+oldts+"' and s.ts<='"+newts+"' ";
				List<Map<String, Object>> listclass=(List<Map<String, Object>>)daoA.executeQuery(sqldept, new MapListProcessor());
				
				Map<String, String> mapclass=new HashMap<String,String>();
				if(listclass!=null&&listclass.size()>0){
					String strinfo="";
					for(Map<String, Object> map:listclass){
						if(map.get("pk_banktype2")==null){
							strinfo+="["+getStrobj(map.get("code"))+"]";
						}else{
							mapclass.put(getStrobj(map.get("pk_banktype")), getStrobj(map.get("pk_banktype2")));
						}
					}
					
					if(!"".equals(strinfo)){
						tempvo.setAccountmsg("检测到编码为"+strinfo+"的银行类别还未同步，请先去同步银行类别档案！");
						daoA.updateVO(tempvo);
						return ;
					}
				}
				
				//==================================================同步表bd_bankdoc=================================================
				String sql_dept_v="select * from bd_bankdoc s where s.ts>='"+oldts+"' and s.ts<='"+newts+"'";
				String sql_dept_v_b="select * from bd_bankdoc r where exists(select 1 from "+tableA+".bd_bankdoc s"
						+ " where r.def2=s.pk_bankdoc and s.ts>='"+oldts+"' and s.ts<='"+newts+"')";
				List<BankdocVO> deptlist_v=(List<BankdocVO>)daoA.executeQuery(sql_dept_v, new BeanListProcessor(BankdocVO.class));
				List<BankdocVO> deptlistb_v=(List<BankdocVO>)daoB.executeQuery(sql_dept_v_b, new BeanListProcessor(BankdocVO.class));
				
				Map<String, BankdocVO> deptvmap=new HashMap<String,BankdocVO>();
				for(BankdocVO vo:deptlistb_v){
					deptvmap.put(vo.getDef2(), vo);
				}
				
				List<BankdocVO> addlist2v=new ArrayList<BankdocVO>();
				List<BankdocVO> updlist2v=new ArrayList<BankdocVO>();
				for(BankdocVO vo:deptlist_v){
					
					if(deptvmap.containsKey(vo.getPk_bankdoc())){//修改保留主键和引用key
						BankdocVO upvo=deptvmap.get(vo.getPk_bankdoc());
						vo.setCreator(pk_user);
						vo.setModifier(pk_user);
						vo.setDef2(vo.getPk_bankdoc());//
						vo.setPk_banktype(mapclass.get(vo.getPk_banktype()));
						vo.setPk_bankdoc(upvo.getPk_bankdoc());
						updlist2v.add(vo);
					}else{
						String newpk="tcl"+vo.getPk_bankdoc().substring(3);
						vo.setCreator(pk_user);
						vo.setModifier(pk_user);
						vo.setDef2(vo.getPk_bankdoc());//
						vo.setPk_banktype(mapclass.get(vo.getPk_banktype()));
						vo.setPk_bankdoc(newpk);
						addlist2v.add(vo);
					}
				}
				
				if(addlist2v.size()>0){
					daoB.insertVOArrayWithPK(addlist2v.toArray(new BankdocVO[0]));
				}
				if(updlist2v.size()>0){
					daoB.updateVOArray(updlist2v.toArray(new BankdocVO[0]));
				}
				
				//更新上级
				String upd_dept="update bd_bankdoc s set s.pk_fatherbank=(select pk_bankdoc m from bd_bankdoc m where m.def2=s.pk_fatherbank) "
						+ "where s.ts>='"+oldts+"'  "//and s.ts<='"+newts+"'插入后的ts比newts大
						+ " and nvl(s.pk_fatherbank,'~')<>'~' and exists(select 1 from bd_bankdoc t where t.def2=s.pk_fatherbank)";
				daoB.executeUpdate(upd_dept);
				
				strmsg +="银行档案(bd_bankdoc):插入数据"+addlist2v.size()+"条，更新数据"+updlist2v.size()+"条。\n\t";
				
//==============================================同步表银行账户===============================================================================
				//==================先查询涉及的业务单元====================================================
				String sqlunit="select distinct g.* from v_tcl_org2 g ";
				List<Map<String, Object>> mapunit=(List<Map<String, Object>>)daoA.executeQuery(sqlunit, new MapListProcessor());
				
				Map<String, String> maporg=new HashMap<String,String>();
				if(mapunit!=null&&mapunit.size()>0){
					String strinfo="";
					for(Map<String, Object> map:mapunit){
						if(map.get("pk_org2")==null){
							strinfo+="["+getStrobj(map.get("code"))+"]";
						}else{
							maporg.put(getStrobj(map.get("pk_org")), getStrobj(map.get("pk_org2")));
						}
					}
					
					if(!"".equals(strinfo)){
						tempvo.setAccountmsg("编码为"+strinfo+"的业务单元在税务帐套还未创建，请先去建账！");
						daoA.updateVO(tempvo);
						return ;
					}
				}
				
				//===========================查询编码重复问题(集团维度)====================================
				String exsql_dept2="select distinct s.accnum from bd_bankaccbas s "
						+ " where s.ts>='"+oldts+"' and s.ts<='"+newts+"' "
						+ " and not exists(select 1 from "+tableB+".bd_bankaccbas r where r.def2=s.pk_bankaccbas) "
						+ " and exists(select 1 from "+tableB+".bd_bankaccbas r where r.code=s.code)";
				
				List<String> listdept2=(List<String>)daoA.executeQuery(exsql_dept2, new ColumnListProcessor());
				if(listdept2!=null&&listdept2.size()>0){
					
					String strinfo="";
					for(String str:listdept2){
						strinfo+="["+str+"]";
					}
					
					tempvo.setAccountmsg("税务帐套编码为"+strinfo+"的银行账户为手工创建，不能重复同步，请先删除或更改编码！");
					daoA.updateVO(tempvo);
					return ;
				}
				
				//==================先查询账户涉及的类别====================================================
				String sqldept3="select distinct g.* from bd_bankaccbas s left join v_tcl_banktype2 g on g.pk_banktype=s.pk_banktype "
						+ " where s.ts>='"+oldts+"' and s.ts<='"+newts+"'  ";
				List<Map<String, Object>> listclass3=(List<Map<String, Object>>)daoA.executeQuery(sqldept3, new MapListProcessor());
				
				Map<String, String> maptype=new HashMap<String,String>();
				if(listclass3!=null&&listclass3.size()>0){
					String strinfo="";
					for(Map<String, Object> map:listclass3){
						if(map.get("pk_banktype2")==null){
							strinfo+="["+getStrobj(map.get("code"))+"]";
						}else{
							maptype.put(getStrobj(map.get("pk_banktype")), getStrobj(map.get("pk_banktype2")));
						}
					}
					
					if(!"".equals(strinfo)){
						tempvo.setAccountmsg("检测到编码为"+strinfo+"的银行类别还未同步，请先去同步银行类别档案！");
						daoA.updateVO(tempvo);
						return ;
					}
				}
				
				//==================先查询账户涉及的银行档案====================================================
				String sqldept4="select distinct g.* from bd_bankaccbas s left join v_tcl_bankdoc2 g on g.pk_bankdoc=s.pk_bankdoc "
						+ " where s.ts>='"+oldts+"' and s.ts<='"+newts+"'  ";
				List<Map<String, Object>> listclass4=(List<Map<String, Object>>)daoA.executeQuery(sqldept4, new MapListProcessor());
				
				Map<String, String> mapdoc=new HashMap<String,String>();
				if(listclass4!=null&&listclass4.size()>0){
					String strinfo="";
					for(Map<String, Object> map:listclass4){
						if(map.get("pk_bankdoc2")==null){
							strinfo+="["+getStrobj(map.get("code"))+"]";
						}else{
							mapdoc.put(getStrobj(map.get("pk_bankdoc")), getStrobj(map.get("pk_bankdoc2")));
						}
					}
					
					if(!"".equals(strinfo)){
						tempvo.setAccountmsg("检测到编码为"+strinfo+"的银行档案还未同步，请先去同步银行档案！");
						daoA.updateVO(tempvo);
						return ;
					}
				}
//====================================================同步开始================================================================
				String sql_dept_v2="select * from bd_bankaccbas s where s.ts>='"+oldts+"' and s.ts<='"+newts+"'";
				String sql_dept_v_b2="select * from bd_bankaccbas r where exists(select 1 from "+tableA+".bd_bankaccbas s"
						+ " where r.def2=s.pk_bankaccbas and s.ts>='"+oldts+"' and s.ts<='"+newts+"')";
				List<BankAccbasVO> deptlist_v2=(List<BankAccbasVO>)daoA.executeQuery(sql_dept_v2, new BeanListProcessor(BankAccbasVO.class));
				List<BankAccbasVO> deptlistb_v2=(List<BankAccbasVO>)daoB.executeQuery(sql_dept_v_b2, new BeanListProcessor(BankAccbasVO.class));
				
				Map<String, BankAccbasVO> deptvmap2=new HashMap<String,BankAccbasVO>();
				for(BankAccbasVO vo:deptlistb_v2){
					deptvmap2.put(vo.getDef2(), vo);
				}
				
				List<BankAccbasVO> addlist=new ArrayList<BankAccbasVO>();
				List<BankAccbasVO> updlist=new ArrayList<BankAccbasVO>();
				for(BankAccbasVO vo:deptlist_v2){
					
					if(deptvmap2.containsKey(vo.getPk_bankaccbas())){//修改保留主键和引用key
						BankAccbasVO upvo=deptvmap2.get(vo.getPk_bankaccbas());
						vo.setCreator(pk_user);
						vo.setModifier(pk_user);
						vo.setDef2(vo.getPk_bankaccbas());//20不可用
						vo.setControlorg(maporg.get(vo.getControlorg()));
						vo.setFinanceorg(maporg.get(vo.getFinanceorg()));
						vo.setPk_bankaccbas(upvo.getPk_bankaccbas());
						vo.setPk_bankdoc(mapdoc.get(vo.getPk_bankdoc()));
						vo.setPk_banktype(maptype.get(vo.getPk_banktype()));
						vo.setPk_org(maporg.get(vo.getPk_org()));
						updlist.add(vo);
					}else{
						String newpk="tcl"+vo.getPk_bankaccbas().substring(3);
						vo.setCreator(pk_user);
						vo.setModifier(pk_user);
						vo.setDef2(vo.getPk_bankaccbas());//
						vo.setControlorg(maporg.get(vo.getControlorg()));
						vo.setFinanceorg(maporg.get(vo.getFinanceorg()));
						vo.setPk_bankaccbas(newpk);
						vo.setPk_bankdoc(mapdoc.get(vo.getPk_bankdoc()));
						vo.setPk_banktype(maptype.get(vo.getPk_banktype()));
						vo.setPk_org(maporg.get(vo.getPk_org()));
						addlist.add(vo);
					}
				}
				
				if(addlist.size()>0){
					daoB.insertVOArrayWithPK(addlist.toArray(new BankAccbasVO[0]));
				}
				if(updlist.size()>0){
					daoB.updateVOArray(updlist.toArray(new BankAccbasVO[0]));
				}
				
				strmsg +="银行账户表(bd_bankaccbas):插入数据"+addlist.size()+"条，更新数据"+updlist.size()+"条。\n\t";
				
				//======================================银行账户子户bd_bankaccsub,关联使用权，需插入主键=============================================
				String sqlfc="select * from bd_bankaccsub s where s.ts>='"+oldts+"' and s.ts<='"+newts+"'";
				List<BankAccSubVO> cflist=(List<BankAccSubVO>)daoA.executeQuery(sqlfc, new BeanListProcessor(BankAccSubVO.class));
				List<BankAccSubVO> addlistf=new ArrayList<BankAccSubVO>();
				List<BankAccSubVO> updlistf=new ArrayList<BankAccSubVO>();
				for(BankAccSubVO vo:cflist){
					
					String pk_bank=vo.getPk_bankaccbas();
					String accnum=vo.getAccnum();
					
					String sqlm="select r.pk_bankaccbas,r.accnum,rr.pk_bankaccbas pk_mate "
							+ " from bd_bankaccbas r left join "+tableB+".bd_bankaccbas rr on r.pk_bankaccbas=rr.def2 "
							+ "  where r.pk_bankaccbas='"+pk_bank+"' ";
					Map<String, Object> map=(Map<String, Object>)daoA.executeQuery(sqlm, new MapProcessor());
					if(map==null){
						continue ;
					}
					Object obj=map.get("pk_mate");
					Object obj3=map.get("accnum");
					if(obj==null){
						tempvo.setAccountmsg("银行账户帐号为"+obj3+"的帐号存在异常(待同步)！，请联系用友人员！");
						daoA.updateVO(tempvo);
						return ;
					}
					
					String sql="select * from bd_bankaccsub e where e.pk_bankaccbas='"+obj+"' and accnum='"+accnum+"' and nvl(dr,0)=0";
					BankAccSubVO vo1=(BankAccSubVO)daoB.executeQuery(sql, new BeanProcessor(BankAccSubVO.class));
					if(vo1==null){
						vo.setPk_bankaccbas(obj.toString());
						addlistf.add(vo);
					}else{
						vo.setPk_bankaccbas(obj.toString());
						updlistf.add(vo);
					}
					
				}
				
				if(addlistf.size()>0){
					daoB.insertVOArrayWithPK(addlistf.toArray(new BankAccSubVO[0]));
				}
				if(updlistf.size()>0){
					daoB.updateVOArray(updlistf.toArray(new BankAccSubVO[0]));
				}
				
				strmsg +="银行账户子户表(bd_bankaccsub):插入数据"+addlistf.size()+"条，更新数据"+updlistf.size()+"条。\n\t";
				
				//===========================================银行账户使用权bd_bankaccuse==========================================
				String sqlfc2="select * from bd_bankaccuse s where s.ts>='"+oldts+"' and s.ts<='"+newts+"'";
				List<BankAccUseVO> cflist2=(List<BankAccUseVO>)daoA.executeQuery(sqlfc2, new BeanListProcessor(BankAccUseVO.class));
				List<BankAccUseVO> addlistf2=new ArrayList<BankAccUseVO>();
				List<BankAccUseVO> updlistf2=new ArrayList<BankAccUseVO>();
				for(BankAccUseVO vo:cflist2){
					
					String pk_bank=vo.getPk_bankaccbas();
					String pk_sub=vo.getPk_bankaccsub();
					
					String sqlm="select r.pk_bankaccbas,r.accnum,rr.pk_bankaccbas pk_mate,g.pk_org,g.pk_org2 "
							+ " from bd_bankaccbas r left join "+tableB+".bd_bankaccbas rr on r.pk_bankaccbas=rr.def2 "
							+ " left join v_tcl_org2 g on r.pk_org=g.pk_org where r.pk_bankaccbas='"+pk_bank+"' ";
					Map<String, Object> map=(Map<String, Object>)daoA.executeQuery(sqlm, new MapProcessor());
					if(map==null){
						continue ;
					}
					Object obj=map.get("pk_mate");
					Object obj2=map.get("pk_org2");
					Object obj3=map.get("accnum");
					if(obj==null){
						tempvo.setAccountmsg("银行账户帐号为"+obj3+"的帐号存在异常(待同步)！，请联系用友人员！");
						daoA.updateVO(tempvo);
						return ;
					}
					
					String sql="select * from bd_bankaccuse e where e.pk_bankaccbas='"+obj+"' and pk_bankaccsub='"+pk_sub+"' and pk_org='"+obj2+"' and nvl(dr,0)=0";
					BankAccUseVO vo1=(BankAccUseVO)daoB.executeQuery(sql, new BeanProcessor(BankAccUseVO.class));
					if(vo1==null){
						vo.setPk_org(obj2.toString());
						vo.setPk_bankaccbas(obj.toString());
						addlistf2.add(vo);
					}else{
						vo.setPk_org(obj2.toString());
						vo.setPk_bankaccbas(obj.toString());
						updlistf2.add(vo);
					}
					
				}
				
				if(addlistf2.size()>0){
					daoB.insertVOArray(addlistf2.toArray(new BankAccUseVO[0]));
				}
				if(updlistf2.size()>0){
					daoB.updateVOArray(updlistf2.toArray(new BankAccUseVO[0]));
				}
				
				strmsg +="银行账户使用权(bd_bankaccuse):插入数据"+addlistf2.size()+"条，更新数据"+updlistf2.size()+"条。";
				
				tempvo.setAccountmsg(strmsg);
				tempvo.setAccount_ts(newts);
			} catch (Exception e) {
				
				Debug.debug(e.getMessage());
				tempvo.setAccountmsg(e.getMessage());
			}
				
		}
		
		else if(type==BaseDocType.syn_subj){
			
			try {
				
				String strmsg="";
				//===========================先同步辅助核算查询编码重复问题(全局)====================================
				String exsql_dept="select distinct code from bd_accassitem s "
						+ " where s.ts>='"+oldts+"' and s.ts<='"+newts+"' "
						+ " and not exists(select 1 from "+tableB+".bd_accassitem r where r.pk_accassitem=s.pk_accassitem) "
						+ " and exists(select 1 from "+tableB+".bd_accassitem r where r.code=s.code )";
				
				List<String> listdept=(List<String>)daoA.executeQuery(exsql_dept, new ColumnListProcessor());
				if(listdept!=null&&listdept.size()>0){
					
					String strinfo="";
					for(String str:listdept){
						strinfo+="["+str+"]";
					}
					
					tempvo.setSubjmsg("税务帐套编码为"+strinfo+"的会计辅助核算项目为手工创建，不能重复同步，请先删除或更改编码！");
					daoA.updateVO(tempvo);
					return ;
				}
				
	//==================================================同步表bd_accassitem====================================================
				String sql_dept="select * from bd_accassitem s where s.ts>='"+oldts+"' and s.ts<='"+newts+"'";
				String sql_dept_b="select * from bd_accassitem r where exists(select 1 from "+tableA+".bd_accassitem s"
						+ " where r.pk_accassitem=s.pk_accassitem and s.ts>='"+oldts+"' and s.ts<='"+newts+"')";
				List<AccAssItemVO> deptlist=(List<AccAssItemVO>)daoA.executeQuery(sql_dept, new BeanListProcessor(AccAssItemVO.class));
				List<AccAssItemVO> deptlistb=(List<AccAssItemVO>)daoB.executeQuery(sql_dept_b, new BeanListProcessor(AccAssItemVO.class));
				
				Map<String, AccAssItemVO> deptmap=new HashMap<String,AccAssItemVO>();
				for(AccAssItemVO vo:deptlistb){
					deptmap.put(vo.getPk_accassitem(), vo);
				}
				
				List<AccAssItemVO> addlist2=new ArrayList<AccAssItemVO>();
				List<AccAssItemVO> updlist2=new ArrayList<AccAssItemVO>();
				for(AccAssItemVO vo:deptlist){
					
					if(deptmap.containsKey(vo.getPk_accassitem())){//修改保留主键和引用key
						AccAssItemVO upvo=deptmap.get(vo.getPk_accassitem());
						vo.setCreator(pk_user);
						vo.setModifier(pk_user);
						vo.setPk_accassitem(upvo.getPk_accassitem());
						updlist2.add(vo);
					}else{
						vo.setCreator(pk_user);
						vo.setModifier(pk_user);
						addlist2.add(vo);
					}
				}
				
				if(addlist2.size()>0){
					daoB.insertVOArrayWithPK(addlist2.toArray(new AccAssItemVO[0]));
				}
				if(updlist2.size()>0){
					daoB.updateVOArray(updlist2.toArray(new AccAssItemVO[0]));
				}
				
				strmsg +="会计辅助核算项目表(bd_accassitem):插入数据"+addlist2.size()+"条，更新数据"+updlist2.size()+"条。\n\t";
				
//==================================================同步表bd_account====================================================
				//==================先查询涉及的科目表====================================================
				String sqlunit="select distinct g.* from v_tcl_accchart2 g ";
				List<Map<String, Object>> mapunit=(List<Map<String, Object>>)daoA.executeQuery(sqlunit, new MapListProcessor());
				
				Map<String, String> mapaccart=new HashMap<String,String>();
				if(mapunit!=null&&mapunit.size()>0){
					String strinfo="";
					for(Map<String, Object> map:mapunit){
						if(map.get("pk_accchart2")==null){
							strinfo+="["+getStrobj(map.get("orgcode"))+"-"+getStrobj(map.get("code"))+"]";
						}else{
							mapaccart.put(getStrobj(map.get("pk_accchart")), getStrobj(map.get("pk_accchart2")));
						}
					}
					
					if(!"".equals(strinfo)){
						tempvo.setSubjmsg("编码格式为[业务单元-科目表]"+strinfo+"的科目表在税务帐套还未创建，请先去建账！");
						daoA.updateVO(tempvo);
						return ;
					}
				}
				
				//===========================查询编码重复问题====================================
				String exsql_dept2="select distinct s.code from bd_account s "
						+ " where s.ts>='"+oldts+"' and s.ts<='"+newts+"' "
						+ " and not exists(select 1 from "+tableB+".bd_account r where r.pk_originalaccount=s.pk_account) "
						+ " and exists(select 1 from "+tableB+".bd_account r where r.code=s.code)";
				
				List<String> listdept2=(List<String>)daoA.executeQuery(exsql_dept2, new ColumnListProcessor());
				if(listdept2!=null&&listdept2.size()>0){
					
					String strinfo="";
					for(String str:listdept2){
						strinfo+="["+str+"]";
					}
					
					tempvo.setSubjmsg("税务帐套编码为"+strinfo+"的科目为手工创建，不能重复同步，请先删除或更改编码！");
					daoA.updateVO(tempvo);
					return ;
				}
				
				//==================================================同步表bd_account=================================================
				String sql_dept_v="select * from bd_account s where s.ts>='"+oldts+"' and s.ts<='"+newts+"'";
				String sql_dept_v_b="select * from bd_account r where exists(select 1 from "+tableA+".bd_account s"
						+ " where r.pk_originalaccount=s.pk_account and s.ts>='"+oldts+"' and s.ts<='"+newts+"')";
				List<AccountVO> deptlist_v=(List<AccountVO>)daoA.executeQuery(sql_dept_v, new BeanListProcessor(AccountVO.class));
				List<AccountVO> deptlistb_v=(List<AccountVO>)daoB.executeQuery(sql_dept_v_b, new BeanListProcessor(AccountVO.class));
				
				Map<String, AccountVO> deptvmap=new HashMap<String,AccountVO>();
				for(AccountVO vo:deptlistb_v){
					deptvmap.put(vo.getPk_originalaccount(), vo);
				}
				
				List<AccountVO> addlist2v=new ArrayList<AccountVO>();
				List<AccountVO> updlist2v=new ArrayList<AccountVO>();
				for(AccountVO vo:deptlist_v){
					
					if(deptvmap.containsKey(vo.getPk_account())){//修改保留主键和引用key
						AccountVO upvo=deptvmap.get(vo.getPk_account());
						vo.setCreator(pk_user);
						vo.setModifier(pk_user);
						vo.setPk_originalaccount(vo.getPk_account());//
						vo.setPk_accchart(mapaccart.get(vo.getPk_accchart()));
						vo.setPk_account(upvo.getPk_account());
						updlist2v.add(vo);
					}else{
						String newpk="tcl"+vo.getPk_account().substring(3);
						vo.setCreator(pk_user);
						vo.setModifier(pk_user);
						vo.setPk_originalaccount(vo.getPk_account());//
						vo.setPk_accchart(mapaccart.get(vo.getPk_accchart()));
						vo.setPk_account(newpk);
						addlist2v.add(vo);
					}
				}
				
				if(addlist2v.size()>0){
					daoB.insertVOArrayWithPK(addlist2v.toArray(new AccountVO[0]));
				}
				if(updlist2v.size()>0){
					daoB.updateVOArray(updlist2v.toArray(new AccountVO[0]));
				}
				
				//更新上级
				String upd_dept="update bd_account s set s.pid=(select pk_account m from bd_account m where m.pk_originalaccount=s.pid) "
						+ "where s.ts>='"+oldts+"'  "//and s.ts<='"+newts+"'插入后的ts比newts大
						+ " and nvl(s.pid,'~')<>'~' and exists(select 1 from bd_account t where t.pk_originalaccount=s.pid)";
				daoB.executeUpdate(upd_dept);
				
				strmsg +="科目表(bd_account):插入数据"+addlist2v.size()+"条，更新数据"+updlist2v.size()+"条。\n\t";
				
//==========================================================================================同步表bd_accasoa=================================================
				//查询涉及的科目
				String sqldept="select distinct g.* from bd_accasoa s left join v_tcl_account2 g on g.pk_account=s.pk_account "
						+ " where s.ts>='"+oldts+"' and s.ts<='"+newts+"' ";
				List<Map<String, Object>> listclass=(List<Map<String, Object>>)daoA.executeQuery(sqldept, new MapListProcessor());
				
				Map<String, String> mapaccount=new HashMap<String,String>();
				if(listclass!=null&&listclass.size()>0){
					String strinfo="";
					for(Map<String, Object> map:listclass){
						if(map.get("pk_account2")==null){
							strinfo+="["+getStrobj(map.get("code"))+"]";
						}else{
							mapaccount.put(getStrobj(map.get("pk_account")), getStrobj(map.get("pk_account2")));
						}
					}
					
					if(!"".equals(strinfo)){
						tempvo.setSubjmsg("检测到编码为"+strinfo+"的科目档案还未同步，请先去同步科目档案！");
						daoA.updateVO(tempvo);
						return ;
					}
				}
				
				//==================================================同步表bd_accasoa=================================================
				String sqlfc2="select * from bd_accasoa s where s.ts>='"+oldts+"' and s.ts<='"+newts+"'";
				List<AccAsoaVO> cflist2=(List<AccAsoaVO>)daoA.executeQuery(sqlfc2, new BeanListProcessor(AccAsoaVO.class));
				List<AccAsoaVO> addlistf2=new ArrayList<AccAsoaVO>();
				List<AccAsoaVO> updlistf2=new ArrayList<AccAsoaVO>();
				for(AccAsoaVO vo:cflist2){
					
					String pk_acc=vo.getPk_account();
					String pk_cha=vo.getPk_accchart();
					
					Object pk_acc2=mapaccount.get(pk_acc);
					Object pk_cha2=mapaccart.get(pk_cha);
					
					String sql="select * from bd_accasoa e where e.pk_accchart='"+pk_cha2+"' and pk_account='"+pk_acc2+"' and nvl(dr,0)=0";
					AccAsoaVO vo1=(AccAsoaVO)daoB.executeQuery(sql, new BeanProcessor(AccAsoaVO.class));
					if(vo1==null){
						vo.setCreator(pk_user);
						vo.setModifier(pk_user);
						vo.setPk_account(getStrobj(pk_acc2));
						vo.setPk_accchart(getStrobj(pk_cha2));
						vo.setRemcode(vo.getPk_accasoa());
						//vo.setPk_accasoa(newpk);不能插入默认pk
						addlistf2.add(vo);
					}else{
						vo.setCreator(pk_user);
						vo.setModifier(pk_user);
						vo.setRemcode(vo.getPk_accasoa());
						vo.setPk_account(getStrobj(pk_acc2));
						vo.setPk_accchart(getStrobj(pk_cha2));
						vo.setPk_accasoa(vo1.getPk_accasoa());
						updlistf2.add(vo);
					}
					
				}
				
				if(addlistf2.size()>0){
					daoB.insertVOArray(addlistf2.toArray(new AccAsoaVO[0]));
				}
				if(updlistf2.size()>0){
					daoB.updateVOArray(updlistf2.toArray(new AccAsoaVO[0]));
				}
				
				strmsg +="科目分配表(bd_accasoa):插入数据"+addlistf2.size()+"条，更新数据"+updlistf2.size()+"条。\n\t";
				
//==========================================================================================同步表bd_accass=================================================
				//要去除辅助核算
				//先查询辅助核算涉及的科目
				String sqlkm="select distinct s.pk_accasoa from bd_accass s where s.ts>='"+oldts+"' and s.ts<='"+newts+"'";
				List<Object> listobj=(List<Object>)daoA.executeQuery(sqlkm, new ColumnListProcessor());
				
				List<AccAssVO> addlistf=new ArrayList<AccAssVO>();
				List<AccAssVO> updlistf=new ArrayList<AccAssVO>();
				List<AccAssVO> dellistf=new ArrayList<AccAssVO>();
				
				for(Object objn : listobj){
					
					String sql="select * from bd_accass a where a.pk_accasoa='"+objn+"'";
					String pk="select pk_accasoa from bd_accasoa where remcode='"+objn+"'";//助记码
					String sql2="select * from bd_accass a where a.pk_accasoa=(select pk_accasoa from bd_accasoa where remcode='"+objn+"') ";
					List<AccAssVO> list1=(List<AccAssVO>)daoA.executeQuery(sql, new BeanListProcessor(AccAssVO.class));
					List<AccAssVO> list2=(List<AccAssVO>)daoB.executeQuery(sql2, new BeanListProcessor(AccAssVO.class));
					
					Object objpk=daoB.executeQuery(pk, new ColumnProcessor());
					
					Map<String, AccAssVO> map1=new HashMap<String,AccAssVO>();
					Map<String, AccAssVO> map2=new HashMap<String,AccAssVO>();
					
					for(AccAssVO avo : list1){
						map1.put(mapaccart.get(avo.getPk_accchart())+avo.getPk_entity(), avo);//转换accchart
					}
					for(AccAssVO avo : list2){
						map2.put(avo.getPk_accchart()+avo.getPk_entity(), avo);
					}
					
					for(AccAssVO avo : list1){
						
						String key=mapaccart.get(avo.getPk_accchart())+avo.getPk_entity();
						if(map2.containsKey(key)){//修改
							AccAssVO upvo=map2.get(key);
							avo.setPk_accasoa(upvo.getPk_accasoa());
							avo.setPk_accass(upvo.getPk_accass());
							avo.setPk_accchart(mapaccart.get(avo.getPk_accchart()));
							updlistf.add(avo);
						}else{//新增
							avo.setPk_accasoa(getStrobj(objpk));
							avo.setPk_accass(null);
							avo.setPk_accchart(mapaccart.get(avo.getPk_accchart()));
							addlistf.add(avo);
						}
						
					}
					
					for(AccAssVO avo : list2){
						String key=avo.getPk_accchart()+avo.getPk_entity();
						if(!map1.containsKey(key)){
							dellistf.add(avo);
						}
					}
				}
				
				if(dellistf.size()>0){
					daoB.deleteVOArray(dellistf.toArray(new AccAssVO[0]));
				}
				if(addlistf.size()>0){
					daoB.insertVOArray(addlistf.toArray(new AccAssVO[0]));
				}
				if(updlistf.size()>0){
					daoB.updateVOArray(updlistf.toArray(new AccAssVO[0]));
				}
				
				//更新上级
				String upd_dept2="update bd_accass s set s.pk_coveraccasoa=(select pk_accasoa m from bd_accasoa m where m.remcode=s.pk_coveraccasoa) "
						+ "where s.ts>='"+oldts+"'  "//and s.ts<='"+newts+"'插入后的ts比newts大
						+ " and nvl(s.pk_coveraccasoa,'~')<>'~' and exists(select 1 from bd_accasoa t where t.remcode=s.pk_coveraccasoa)";
				daoB.executeUpdate(upd_dept2);
				
				strmsg +="科目辅助表(bd_accass):插入数据"+addlistf.size()+"条，更新数据"+updlistf.size()+"条，删除数据"+dellistf.size()+"条。";
				
				tempvo.setSubjmsg(strmsg);
				tempvo.setSubj_ts(newts);
				
			} catch (Exception e) {
				
				Debug.debug(e.getMessage());
				tempvo.setSubjmsg(e.getMessage());
			}
		}
		
		else if(type==BaseDocType.syn_zdy){
			
			try {
				
				String strmsg="";
				
				//==================先查询涉及的业务单元====================================================
				String sqlunit="select distinct g.* from v_tcl_org2 g ";
				List<Map<String, Object>> mapunit=(List<Map<String, Object>>)daoA.executeQuery(sqlunit, new MapListProcessor());
				
				Map<String, String> maporg=new HashMap<String,String>();
				Map<String, String> maporg2=new HashMap<String,String>();
				if(mapunit!=null&&mapunit.size()>0){
					String strinfo="";
					for(Map<String, Object> map:mapunit){
						if(map.get("pk_org2")==null){
							strinfo+="["+getStrobj(map.get("code"))+"]";
						}else{
							maporg.put(getStrobj(map.get("pk_org")), getStrobj(map.get("pk_org2")));
							maporg2.put(getStrobj(map.get("pk_org")), getStrobj(map.get("code")));
						}
					}
					
					if(!"".equals(strinfo)){
						tempvo.setZdymsg("编码为"+strinfo+"的业务单元在税务帐套还未创建，请先去建账！");
						daoA.updateVO(tempvo);
						return ;
					}
				}
				
				//==================先查询档案涉及的类别====================================================
				String sqldept="select distinct g.* from bd_defdoc s left join v_tcl_list2 g on g.pk_defdoclist=s.pk_defdoclist "
						+ " where s.ts>='"+oldts+"' and s.ts<='"+newts+"' and exists(select 1 from v_tcl_list2 v where v.pk_defdoclist=s.pk_defdoclist) ";
				List<Map<String, Object>> listclass=(List<Map<String, Object>>)daoA.executeQuery(sqldept, new MapListProcessor());
				
				Map<String, String> mapclass=new HashMap<String,String>();
				Map<String, String> mapclass2=new HashMap<String,String>();
				if(listclass!=null&&listclass.size()>0){
					String strinfo="";
					for(Map<String, Object> map:listclass){
						if(map.get("pk_defdoclist2")==null){
							strinfo+="["+getStrobj(map.get("code"))+"]";
						}else{
							mapclass.put(getStrobj(map.get("pk_defdoclist")), getStrobj(map.get("pk_defdoclist2")));
							mapclass2.put(getStrobj(map.get("pk_defdoclist")), getStrobj(map.get("code")));
						}
					}
					
					if(!"".equals(strinfo)){
						tempvo.setZdymsg("检测到编码为"+strinfo+"的自定义档案分类还未同步，请先去同步自定义档案分类！");
						daoA.updateVO(tempvo);
						return ;
					}
				}
				
				//===========================同步表bd_defdoc数据，并判断是否重复=========================================
				String sqldept2="select * from bd_defdoc s where s.ts>='"+oldts+"' and s.ts<='"+newts+"' "
						+ " and exists(select 1 from v_tcl_list2 v where v.pk_defdoclist=s.pk_defdoclist) ";
				
				List<DefdocVO> list2=(List<DefdocVO>)daoA.executeQuery(sqldept2, new BeanListProcessor(DefdocVO.class));
				
				String str="";//编码重复
				List<DefdocVO> addlistf=new ArrayList<DefdocVO>();
				List<DefdocVO> updlistf=new ArrayList<DefdocVO>();
				for(DefdocVO vo : list2){
					
					String pk=vo.getPk_defdoc();
					String pk_org=vo.getPk_org();
					String pk_list=vo.getPk_defdoclist();
					String pk_org2=maporg.get(pk_org);
					String pk_list2=mapclass.get(pk_list);
					String sql="";
					
					if(pk_org.equals(BaseDocType.pk_group)||pk_org.equals(BaseDocType.pk_globel)){//集团或全局参数校验组织
						sql="select count(1) from bd_defdoc c where nvl(c.dr,0)=0 and pk_defdoclist='"+pk_list2+"' and def20<>'"+pk+"' and code='"+vo.getCode()+"' ";
					}else{//组织自定义
						sql="select count(1) from bd_defdoc c where nvl(c.dr,0)=0 and pk_defdoclist='"+pk_list2+"' and def20<>'"+pk+"' and code='"+vo.getCode()+"' "
						+ " and (pk_org='"+pk_org2+"' or pk_org='"+BaseDocType.pk_group+"' or pk_org='"+BaseDocType.pk_globel+"')";
					}
					Integer it=(Integer)daoB.executeQuery(sql, new ColumnProcessor());
					
					if(it>0){
						str+="["+mapclass2.get(pk_list)+"-"+maporg2.get(pk_org)+"-"+vo.getCode()+"]";
						continue ;
					}
					
					String sql2="select * from bd_defdoc c where nvl(dr,0)=0 and def20='"+pk+"'";
					DefdocVO dvo=(DefdocVO)daoB.executeQuery(sql2, new BeanProcessor(DefdocVO.class));
					if(dvo==null){//新增
						vo.setPk_defdoclist(pk_list2);
						vo.setPk_org(pk_org2);
						vo.setDef20(pk);
						vo.setPk_defdoc(null);
						addlistf.add(vo);
					}else{
						vo.setPk_defdoc(dvo.getPk_defdoc());
						vo.setPk_defdoclist(pk_list2);
						vo.setPk_org(pk_org2);
						vo.setDef20(pk);
						updlistf.add(vo);
					}
				}
				
				if(!"".equals(str)){
					tempvo.setZdymsg("税务帐套编码格式[分类-业务单元-编码]"+str+"的自定义档案为手工创建，不能重复同步，请先删除或更改编码！");
					daoA.updateVO(tempvo);
					return ;
				}
				
				if(addlistf.size()>0){
					daoB.insertVOArray(addlistf.toArray(new DefdocVO[0]));
				}
				if(updlistf.size()>0){
					daoB.updateVOArray(updlistf.toArray(new DefdocVO[0]));
				}
				
				strmsg +="自定义档案(bd_defdoc):插入数据"+addlistf.size()+"条，更新数据"+updlistf.size()+"条。";
				
				tempvo.setZdymsg(strmsg);
				tempvo.setZdy_ts(newts);
				
			} catch (Exception e) {
				Debug.debug(e.getMessage());
				tempvo.setZdymsg(e.getMessage());
			}
		}
		
		daoA.updateVO(tempvo);
	}
	
	private String getStrobj(Object obj){
		return obj==null?"":obj.toString();
	}
}