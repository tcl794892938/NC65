package nc.itf.gl.query;

import java.util.List;
import java.util.Map;

import nc.vo.gl.pubvoucher.VoucherVO;

public interface IVoucherQuery {
	
	/**
	 * ƴ�Ӳ�ѯ��䣬����б����1000������
	 */
	public List<Map<String, Object>> queryVoucherBills(VoucherVO[] vos)
			throws Exception;
}
