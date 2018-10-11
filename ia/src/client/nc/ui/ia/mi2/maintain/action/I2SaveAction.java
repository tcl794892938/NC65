package nc.ui.ia.mi2.maintain.action;

import java.awt.event.ActionEvent;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.ia.bill.base.maintain.action.BaseSaveAction;
import nc.ui.ia.mi2.maintain.model.I2ModelService;
import nc.ui.trade.business.HYPubBO_Client;
import nc.vo.ia.mi2.entity.I2BillVO;
import nc.vo.ia.mi2.entity.I2ItemVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pubapp.AppContext;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.scmf.ic.mbatchcode.BatchcodeVO;

/**
 * 采购入库单--保存操作
 * 
 * @since 6.0
 * @version 2011-1-17 上午11:38:32
 * @author 王键
 */
public class I2SaveAction extends BaseSaveAction {

  private static final long serialVersionUID = -4269979107289806852L;

  private I2ModelService service;


  public void setService(I2ModelService service) {
    this.service = service;
  }

  @Override
public void doAction(ActionEvent e) {
	  I2BillVO aggvo= (I2BillVO) getEditor().getValue();
	 for(I2ItemVO bvo:aggvo.getChildrenVO()){
		 try {
			 String code=bvo.getVbatchcode()==null?"":bvo.getVbatchcode().toString();
			 String cmcode=bvo.getCinventoryid()==null?"":bvo.getCinventoryid().toString();
			 String sql="select count(*) from scm_batchcode where nvl(dr,0)=0 and vbatchcode='"+code+"' and cmaterialvid='"+cmcode+"'";
			 IUAPQueryBS iQ=NCLocator.getInstance().lookup(IUAPQueryBS.class);
			 int a=(int) iQ.executeQuery(sql, new ColumnProcessor());
			 if(a>0){
				 continue;
			 }
			 BatchcodeVO vo=new BatchcodeVO();
			 vo.setVbatchcode(bvo.getVbatchcode());
			 vo.setCmaterialoid(bvo.getCinventoryvid());
			 vo.setCmaterialvid(bvo.getCinventoryid());
			 vo.setVhashcode("123");
			 vo.setBinqc(new UFBoolean(false));
			 vo.setBseal(new UFBoolean(false));
			 vo.setAttributeValue("dr", 0);
			 vo.setPk_group(AppContext.getInstance().getPkGroup());
			 HYPubBO_Client.insert(vo);
		} catch (Exception e1) {
			e1.printStackTrace(); 
		}
	 }

	super.doAction(e);
}
  
  @Override
  public I2BillVO update(Object object) {
    I2BillVO retBill = null;
    try {
      retBill = (I2BillVO) this.service.update(object);
    }
    catch (Exception e) {
      ExceptionUtils.wrappException(e);
    }
    return retBill;
  }

}
