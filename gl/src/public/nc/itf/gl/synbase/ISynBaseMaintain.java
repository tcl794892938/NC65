package nc.itf.gl.synbase;

import nc.vo.fl.voucher.TempBaseDocVO;
import nc.vo.pub.BusinessException;

public interface ISynBaseMaintain {
	
	/**
	 * by tcl
	 * @param oldts �ϴ�ʱ��
	 * @param newts ����ʱ��
	 * @param type ͬ������
	 * @throws BusinessException
	 */
	public void synBasedoc(String oldts,String newts, int type,TempBaseDocVO vo) throws BusinessException ;

}
