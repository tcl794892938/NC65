package nc.ui.so.temprecord;

import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.pub.bill.BillRendererVO;
import nc.vo.trade.button.ButtonVO;

public class ClientUI extends BillCardUI {

	public ClientUI() {
		
		try {
			new MyEventHandler(this, this.getUIControl()).onBoAdd(this.getButtonManager().getButton(IBillButton.Add));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected ICardController createController() {
		return new ClientUICtrl();
	}

	protected BusinessDelegator createBusinessDelegator() {
		return new MyDelegator();
	}

	protected CardEventHandler createEventHandler() {
		return new MyEventHandler(this, this.getUIControl());
	}

	public String getRefBillType() {
		return null;
	}

	protected void initSelfData() {
		
		UIRefPane rep=(UIRefPane)getBillCardPanel().getHeadItem("pk_houseid").getComponent();
		rep.setMultiSelectedEnabled(true);
		
		UIRefPane rep2=(UIRefPane)getBillCardPanel().getHeadItem("pk_material").getComponent();
		rep2.setMultiSelectedEnabled(true);
		
		UIRefPane rep3=(UIRefPane)getBillCardPanel().getHeadItem("pk_customer").getComponent();
		rep3.setMultiSelectedEnabled(true);
		
//		显示空显示零问题
		getBillCardPanel().getBodyPanel().setShowFlags(new BillRendererVO(true,false,false,true));
	}

	public void setDefaultData() throws Exception {}

	protected void initPrivateButton() {
		ButtonVO btn=new ZQueryBtnVO().getButtonVO();
		addPrivateButton(btn);
	}
	
	@Override
	public boolean onClosing() {
		return true;
	}

	@Override
	public void afterEdit(BillEditEvent e) {
		
		if(e.getKey().equals("pk_org")){
			
			String[] keys=new String[]{"pk_houseid","pk_material","pk_customer"};
			for(String key : keys){
				getBillCardPanel().setHeadItem(key, null);
			}
			
			if(e.getValue()==null){
				return ;
			}
			Object obj=getBillCardPanel().getHeadItem("pk_org").getValueObject();
			UIRefPane rep=(UIRefPane)getBillCardPanel().getHeadItem("pk_houseid").getComponent();
			rep.setPk_org(obj.toString());
			
			UIRefPane rep2=(UIRefPane)getBillCardPanel().getHeadItem("pk_material").getComponent();
			rep2.setPk_org(obj.toString());
			
			UIRefPane rep3=(UIRefPane)getBillCardPanel().getHeadItem("pk_customer").getComponent();
			rep3.setPk_org(obj.toString());
		}
	}

	@Override
	public boolean beforeEdit(BillEditEvent e) {
		
		return super.beforeEdit(e);
	}

}
