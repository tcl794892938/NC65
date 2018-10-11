package nc.ui.qc.c003.billref.ccp;

import nc.bs.framework.common.NCLocator;
import nc.itf.qc.ccp.IQueryForCCP;
import nc.ui.pubapp.uif2app.query2.model.IRefQueryService;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.qc.c003.entity.ReportVO;

/**
 * 转单查询服务--为质证书
 * 
 * @since 6.0
 * @version 2011-2-23 上午09:10:20
 * @author hanbin
 */
public class RefQueryServiceForC006 implements IRefQueryService {

  @Override
  public Object[] queryByQueryScheme(IQueryScheme scheme) throws Exception {
    ReportVO[] rets = null;
    IQueryForCCP service = NCLocator.getInstance().lookup(IQueryForCCP.class);
    try {
    	
    	
      rets = service.queryReportForCCP(scheme);
    }
    catch (BusinessException e) {
      ExceptionUtils.wrappException(e);
    }
    return rets;
  }

  @Override
  public Object[] queryByWhereSql(String whereSql) throws Exception {
    return null;
  }

}
