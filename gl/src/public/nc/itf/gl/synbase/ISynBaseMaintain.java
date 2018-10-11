package nc.itf.gl.synbase;

import nc.vo.fl.voucher.TempBaseDocVO;
import nc.vo.pub.BusinessException;

public interface ISynBaseMaintain {
	
	/**
	 * by tcl
	 * @param oldts 上次时间
	 * @param newts 本次时间
	 * @param type 同步类型
	 * @throws BusinessException
	 */
	public void synBasedoc(String oldts,String newts, int type,TempBaseDocVO vo) throws BusinessException ;

}
