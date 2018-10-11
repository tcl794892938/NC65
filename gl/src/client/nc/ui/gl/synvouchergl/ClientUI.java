package nc.ui.gl.synvouchergl;

import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.gl.button.PrivateBtnVO;
import nc.vo.pub.CircularlyAccessibleValueObject;

public class ClientUI extends AbstractClientUI {
	private static final long serialVersionUID = 1L;

	@Override
	protected AbstractManageController createController() {
		return super.createController();
	}

	protected BusinessDelegator createBusinessDelegator() {
		return super.createBusinessDelegator();
	}
	
	@Override
	protected ManageEventHandler createEventHandler() {
		return new MyEventHandler(this, this.getUIControl());
	}

	@Override
	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
	}

	@Override
	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {
	}

	@Override
	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
	}

	@Override
	protected void initSelfData() {
		
	}

	@Override
	public void setDefaultData() throws Exception {

	}

	@Override
	protected void initPrivateButton() {
		addPrivateButton(PrivateBtnVO.getInstance().getSynBtnVO());
		addPrivateButton(PrivateBtnVO.getInstance().getRizhiBtnVO());
	}

}
