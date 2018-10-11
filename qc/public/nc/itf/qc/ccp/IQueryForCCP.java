package nc.itf.qc.ccp;

import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.pub.BusinessException;
import nc.vo.qc.c003.entity.ReportVO;

public interface IQueryForCCP {
	
	
	 public ReportVO[] queryReportForCCP(IQueryScheme scheme)
		      throws BusinessException;

}
