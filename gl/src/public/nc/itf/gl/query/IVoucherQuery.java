package nc.itf.gl.query;

import java.util.List;
import java.util.Map;

import nc.vo.gl.pubvoucher.VoucherVO;

public interface IVoucherQuery {
	
	/**
	 * 拼接查询语句，解决列表大于1000行问题
	 */
	public List<Map<String, Object>> queryVoucherBills(VoucherVO[] vos)
			throws Exception;
}
