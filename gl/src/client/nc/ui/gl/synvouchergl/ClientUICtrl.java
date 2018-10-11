package nc.ui.gl.synvouchergl;

import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.fl.voucher.BillVoucherHVO2;
import nc.vo.gl.button.BillButtonNO;
import nc.vo.pub.SuperVO;
import nc.vo.trade.pub.HYBillVO;


public class ClientUICtrl extends AbstractManageController {

	public String[] getCardBodyHideCol() {
		return null;
	}

	public int[] getCardButtonAry() {

		return new int[] { 
				IBillButton.Return,		
				IBillButton.Save,
				IBillButton.Cancel,
				IBillButton.Add,
				IBillButton.Edit,
				IBillButton.Delete,
			    IBillButton.Refresh,
			    IBillButton.Copy,
			    BillButtonNO.SYN,
			    BillButtonNO.RIZHI
		};

	}

	public int[] getListButtonAry() {
		return new int[] { 
				IBillButton.Query,
				IBillButton.Card,
				IBillButton.Add,
				IBillButton.Edit,
				IBillButton.Delete,
			    IBillButton.Refresh,
			    IBillButton.Copy
		};

	}

	public boolean isShowCardRowNo() {
		return true;
	}

	public boolean isShowCardTotal() {
		return false;
	}

	public String getBillType() {
		return "200056";
	}

	public String[] getBillVoName() {
		return new String[] { 
				HYBillVO.class.getName(),
				BillVoucherHVO2.class.getName(),
				SuperVO.class.getName()
				};
	}

	public String getBodyCondition() {
		return null;
	}

	public String getBodyZYXKey() {
		return null;
	}

	public int getBusinessActionType() {
		return IBusinessActionType.BD;
	}

	public String getChildPkField() {
		return null;
	}

	public String getHeadZYXKey() {
		return null;
	}

	public String getPkField() {
		return null;
	}

	public Boolean isEditInGoing() throws Exception {
		return null;
	}

	public boolean isExistBillStatus() {
		return false;
	}

	public boolean isLoadCardFormula() {
		return true;
	}

	public String[] getListBodyHideCol() {
		return null;
	}

	public String[] getListHeadHideCol() {
		return null;
	}

	public boolean isShowListRowNo() {
		return true;
	}

	public boolean isShowListTotal() {
		return false;
	}
	
}
