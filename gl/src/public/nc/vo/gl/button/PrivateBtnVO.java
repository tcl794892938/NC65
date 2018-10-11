package nc.vo.gl.button;

import nc.ui.trade.base.IBillOperate;
import nc.vo.trade.button.ButtonVO;

public class PrivateBtnVO {
	
	private static PrivateBtnVO m_btnObj = null;
	
	public PrivateBtnVO() {
	}

	public static PrivateBtnVO getInstance() {
		
		if (m_btnObj == null) {
			m_btnObj = new PrivateBtnVO();
		}
		return m_btnObj;
	}
	
	public ButtonVO getRizhiBtnVO() {
		ButtonVO btnVo = new ButtonVO();
		btnVo.setBtnNo(BillButtonNO.RIZHI);
		//btnVo.setBtnCode("��־");
		btnVo.setBtnChinaName("�鿴��־");
		btnVo.setBtnName("�鿴��־");
		btnVo.setHintStr("�鿴��־");
		btnVo.setOperateStatus(new int[] {IBillOperate.OP_NOTEDIT});

		return btnVo;
	}
	
	public ButtonVO getSynBtnVO() {
		ButtonVO btnVo = new ButtonVO();
		btnVo.setBtnNo(BillButtonNO.SYN);
		btnVo.setBtnChinaName("ͬ��ƾ֤");
		btnVo.setBtnName("ͬ��ƾ֤");
		btnVo.setHintStr("ͬ���û���ڼ��ƾ֤");
		btnVo.setOperateStatus(new int[] {IBillOperate.OP_NOTEDIT});

		return btnVo;
	}
	
	public ButtonVO getClearBtnVO() {
		ButtonVO btnVo = new ButtonVO();
		btnVo.setBtnNo(BillButtonNO.CLEAR);
		btnVo.setBtnChinaName("�����־");
		btnVo.setBtnName("�����־");
		btnVo.setHintStr("�����־�ļ�");
		btnVo.setOperateStatus(new int[] {IBillOperate.OP_NOTEDIT});

		return btnVo;
	}

}
