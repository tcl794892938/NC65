package nc.itf.gl.synbase;

import nc.vo.fl.voucher.TempBaseDocVO2;
import nc.vo.pub.BusinessException;

public interface ISynBaseMaintain2 {
	
	/**
	 * by tcl
	 * @param oldts �ϴ�ʱ��
	 * @param newts ����ʱ��
	 * @param type ͬ������
	 * @throws BusinessException
	 */
	public void synBasedoc(String oldts,String newts, int type,TempBaseDocVO2 vo) throws BusinessException ;

}
