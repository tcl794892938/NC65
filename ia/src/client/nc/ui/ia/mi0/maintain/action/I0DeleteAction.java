package nc.ui.ia.mi0.maintain.action;

import java.awt.event.ActionEvent;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.ui.ia.bill.base.maintain.action.BaseDeleteAction;
import nc.ui.trade.business.HYPubBO_Client;
import nc.vo.ia.bill.entity.base.AbstractBaseBill;
import nc.vo.ia.mi0.entity.I0BillVO;
import nc.vo.ia.mi0.entity.I0ItemVO;
import nc.vo.scmf.ic.mbatchcode.BatchcodeVO;

/**
 * 录入期初单据删除按钮
 * 
 * @since 6.1
 * @version 2012-1-10下午03:19:27
 * @author 王键
 */
public class I0DeleteAction extends BaseDeleteAction {

  private static final long serialVersionUID = 1591573676759700193L;

  @Override
  protected boolean otherEnable(AbstractBaseBill bill) {
    return true;
  }

@Override
public void doAction(ActionEvent e) throws Exception {
	I0BillVO aggvo=(I0BillVO) getModel().getSelectedData();
	super.doAction(e);
	 for(I0ItemVO bvo:aggvo.getChildrenVO()){
		 try {
		 String code=bvo.getVbatchcode()==null?"":bvo.getVbatchcode().toString();
		 String cmcode=bvo.getCinventoryid()==null?"":bvo.getCinventoryid().toString();
		 String sql="select * from scm_batchcode where nvl(dr,0)=0 and vbatchcode='"+code+"' and cmaterialvid='"+cmcode+"'";
		 IUAPQueryBS iQ=NCLocator.getInstance().lookup(IUAPQueryBS.class);
		 BatchcodeVO vo=(BatchcodeVO) iQ.executeQuery(sql, new BeanProcessor(BatchcodeVO.class));
		 if(vo==null){
			 continue;
		 }
			HYPubBO_Client.delete(vo);
		} catch (Exception e1) {
			e1.printStackTrace(); 
		}
	 }
	
}

  
}
