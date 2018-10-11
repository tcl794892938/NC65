package nc.ui.ia.mi0.maintain.action;

import java.awt.event.ActionEvent;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.ia.bill.base.maintain.action.BaseSaveAction;
import nc.ui.ia.mi0.maintain.model.I0ModelService;
import nc.ui.trade.business.HYPubBO_Client;
import nc.vo.ia.mi0.entity.I0BillVO;
import nc.vo.ia.mi0.entity.I0ItemVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pubapp.AppContext;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.scmf.ic.mbatchcode.BatchcodeVO;

/**
 * 期初入库单——保存按钮操作
 * 
 * @since 6.0
 * @version 2010-06-4 下午06:08:04
 * @author 王键
 */
public class I0SaveAction extends BaseSaveAction {

	/**
   * 
   */
	private static final long serialVersionUID = 7454987980497409889L;

	private I0ModelService service;

	/**
	 * @param service
	 *            the service to set
	 */
	public void setService(I0ModelService service) {
		this.service = service;
	}

	@Override
	public void doAction(ActionEvent e) {
		I0BillVO aggvo = (I0BillVO) getEditor().getValue();
		for (I0ItemVO bvo : aggvo.getChildrenVO()) {
			try {
				String code = bvo.getVbatchcode() == null ? "" : bvo.getVbatchcode().toString();
				String cmcode = bvo.getCinventoryid() == null ? "" : bvo.getCinventoryid().toString();
				String sql = "select count(*) from scm_batchcode where nvl(dr,0)=0 and vbatchcode='"
						+ code + "' and cmaterialvid='" + cmcode + "'";
				IUAPQueryBS iQ = NCLocator.getInstance().lookup(IUAPQueryBS.class);
				int a = (int) iQ.executeQuery(sql, new ColumnProcessor());
				if (a > 0) {
					continue;
				}
				BatchcodeVO vo = new BatchcodeVO();
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
	public I0BillVO update(Object object) {
		I0BillVO retBill = null;
		try {
			retBill = (I0BillVO) this.service.update(object);
		} catch (Exception e) {
			ExceptionUtils.wrappException(e);
		}
		return retBill;
	}

}
