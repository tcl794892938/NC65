package nc.bs.so.m4331.maintain.rule.reverse;

import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.vo.ic.reserve.entity.ReserveVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.so.entity.TempRecordVO;
import nc.vo.so.m4331.entity.DeliveryBVO;
import nc.vo.so.m4331.entity.DeliveryVO;

/**
 * 扣除预留数量
 * 
 * @author tcl 2018-03
 */
public class ReduceReserveNumRule implements IRule<DeliveryVO> {

	@Override
	public void process(DeliveryVO[] vos) {

		try {
			BaseDAO dao=new BaseDAO();
			// 根据当前数据回写扣减预留数量(按公司+仓库+物料+客户+批次号)
			for (DeliveryVO vo : vos) {
				
				DeliveryBVO[] bvos=vo.getChildrenVO();
				
				for(DeliveryBVO bvo:bvos){
					String pk_org=bvo.getCsendstockorgid();
					String cwhouseid=bvo.getCsendstordocid();
					String pk_mea=bvo.getCmaterialid();
					String pk_customer=bvo.getCordercustid();
					String pk_batch=bvo.getVbatchcode();
					
					if("".equals(getStr(pk_org))||"".equals(getStr(cwhouseid))||"".equals(getStr(pk_mea))||"".equals(getStr(pk_customer))||"".equals(getStr(pk_batch))){
						continue ;
					}
					
					String sql="select * from ic_reserve where nvl(dr,0)=0 and fresstate=1 and pk_org='"+pk_org+"' and cwarehouseid ='"+cwhouseid+"' and "
							+ "cmaterialoid='"+pk_mea+"' and ccustomerid='"+pk_customer+"' and vbatchcode='"+pk_batch+"' and ntalrsnum>0 order by vrescode" ;
					
					List<ReserveVO> listvo=(List<ReserveVO>)dao.executeQuery(sql, new BeanListProcessor(ReserveVO.class));
					if(listvo==null||listvo.size()<=0){
						continue ;
					}
						
					UFDouble nnum=bvo.getNnum();//主数量
					for(ReserveVO rvo:listvo){
						
						String pk=rvo.getPk_reserve();
						UFDouble ud=rvo.getNtalrsnum();
						if(ud.compareTo(nnum)>=0){
							rvo.setNtalrsnum(ud.sub(nnum));
							nnum=new UFDouble(0);
						}else{
							rvo.setNtalrsnum(new UFDouble(0));
							nnum=nnum.sub(ud);
						}
						
						dao.updateVO(rvo);
						UFDouble resmny=ud.sub(rvo.getNtalrsnum());
						String sql2="update ic_onhandnum set nrsnum=nrsnum-"+resmny+" where pk_onhanddim =(select pk_onhanddim from ic_onhanddim m"
								+ " where nvl(m.dr,0)=0 and pk_org='"+pk_org+"' and cwarehouseid ='"+cwhouseid+"' and cmaterialoid='"+pk_mea+"' "
										+ " and vbatchcode='"+pk_batch+"' )";//现存量
						String sql3="update ic_handreserve set nrsnum=nrsnum-"+resmny+" where nvl(dr,0)=0 and pk_reserve='"+pk+"'";//现存量预留明细 
						dao.executeUpdate(sql2);
						dao.executeUpdate(sql3);
						//插入中间表数据
						TempRecordVO tvo = new TempRecordVO();
						tvo.setFhbillno(vo.getParentVO().getVbillcode());
						tvo.setFhdate(vo.getParentVO().getDbilldate());
						tvo.setYlbillno(rvo.getVrescode());
						tvo.setPk_org(rvo.getPk_org());
						tvo.setPk_houseid(rvo.getCwarehouseid());
						tvo.setPk_material(rvo.getCmaterialoid());
						tvo.setPk_customer(rvo.getCcustomerid());
						tvo.setVbatchcode(rvo.getVbatchcode());
						tvo.setNylnum(ud);
						tvo.setNfhnum(resmny);
						tvo.setNresnum(rvo.getNtalrsnum());
						tvo.setDr(0);
						dao.insertVO(tvo);
						
						if(nnum.compareTo(new UFDouble(0))<=0){
							break ;
						}
					}
					
				}
			}

			
		} catch (Exception e) {
			ExceptionUtils.wrappException(e);
		}

	}
	
	private String getStr(String str){
		
		if(str==null||"".equals(str)||"~".equals(str)){
			return "";
		}
		return str;
	}

}
