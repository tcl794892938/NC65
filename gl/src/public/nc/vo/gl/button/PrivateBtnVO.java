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
		//btnVo.setBtnCode("日志");
		btnVo.setBtnChinaName("查看日志");
		btnVo.setBtnName("查看日志");
		btnVo.setHintStr("查看日志");
		btnVo.setOperateStatus(new int[] {IBillOperate.OP_NOTEDIT});

		return btnVo;
	}
	
	public ButtonVO getSynBtnVO() {
		ButtonVO btnVo = new ButtonVO();
		btnVo.setBtnNo(BillButtonNO.SYN);
		btnVo.setBtnChinaName("同步凭证");
		btnVo.setBtnName("同步凭证");
		btnVo.setHintStr("同步该会计期间的凭证");
		btnVo.setOperateStatus(new int[] {IBillOperate.OP_NOTEDIT});

		return btnVo;
	}
	
	public ButtonVO getClearBtnVO() {
		ButtonVO btnVo = new ButtonVO();
		btnVo.setBtnNo(BillButtonNO.CLEAR);
		btnVo.setBtnChinaName("清除日志");
		btnVo.setBtnName("清除日志");
		btnVo.setHintStr("清除日志文件");
		btnVo.setOperateStatus(new int[] {IBillOperate.OP_NOTEDIT});

		return btnVo;
	}

}
