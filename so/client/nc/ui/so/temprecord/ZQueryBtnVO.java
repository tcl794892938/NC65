package nc.ui.so.temprecord;

import nc.ui.trade.base.IBillOperate;
import nc.vo.trade.button.ButtonVO;
import nc.vo.trade.pub.IBillStatus;

public class ZQueryBtnVO {

	public static final String B1SBtnVO= "≤È—Ø";
	
	public static final Integer btnno=201;
	
	public ButtonVO getButtonVO(){
		ButtonVO btnvo = new ButtonVO();
		btnvo.setBtnCode("B1SBtnVO");
		btnvo.setBtnNo(btnno);
		btnvo.setBtnName(B1SBtnVO);
		btnvo.setHintStr(B1SBtnVO);
		btnvo.setBtnChinaName(B1SBtnVO);
		btnvo.setOperateStatus(new int[] { IBillOperate.OP_ALL});
		btnvo.setBusinessStatus(new int[] { IBillStatus.ALL });
		return btnvo;
	}
}
