package nc.bs.gl.query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.itf.gl.query.IVoucherQuery;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.vo.gl.pubvoucher.VoucherVO;

public class VoucherQueryImpl implements IVoucherQuery {

	/**
	 * 拼接查询语句，解决列表大于1000行问题
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryVoucherBills(VoucherVO[] vos)
			throws Exception {
		
		if(null==vos||vos.length<=0){
			return null;
		}
		
		BaseDAO dao = new BaseDAO();
		String sql="select g.*,b.code,c.user_code,d.user_code user_code2,e.user_code user_code3 from gl_voucher g"
			+ " left join bd_vouchertype b on g.pk_vouchertype=b.pk_vouchertype left join sm_user c on g.pk_prepared=c.cuserid "
			+ " left join sm_user d on g.pk_checked=d.cuserid left join sm_user e on g.pk_manager=e.cuserid where g.pk_voucher in ";
		int j = 0;// 循环标志
		int k = 0;
		String pk = "";
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (int i = 800 * j + 0; i < vos.length; i++) {
			pk += "'" + vos[i].getPk_voucher() + "',";
			k++;
			if (k == 800) {
				pk = pk.substring(0, pk.lastIndexOf(","));
				String sql_st = sql + "(" + pk + ")";
				List<Map<String, Object>> llist = (List<Map<String, Object>>) dao
						.executeQuery(sql_st, new MapListProcessor());
				list.addAll(llist);
				pk = "";
				k = 0;
				j++;// 循环位
			}
		}
		if (k != 0) {
			pk = pk.substring(0, pk.lastIndexOf(","));
			String sql_st = sql + "(" + pk + ")";
			List<Map<String, Object>> llist = (List<Map<String, Object>>) dao
					.executeQuery(sql_st, new MapListProcessor());
			list.addAll(llist);
		}
		return list;
	}
}
