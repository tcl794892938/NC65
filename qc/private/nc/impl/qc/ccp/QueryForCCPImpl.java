package nc.impl.qc.ccp;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import nc.impl.pubapp.pattern.data.view.ViewQuery;
import nc.impl.pubapp.pattern.database.DataAccessUtils;
import nc.itf.qc.ccp.IQueryForCCP;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.query2.sql.process.QuerySchemeProcessor;
import nc.vo.qc.c003.entity.ReportHeaderVO;
import nc.vo.qc.c003.entity.ReportItemVO;
import nc.vo.qc.c003.entity.ReportVO;
import nc.vo.qc.c003.entity.ReportViewVO;
import nc.vo.scmpub.util.CombineViewToAggUtil;

public class QueryForCCPImpl implements IQueryForCCP {

	@Override
	public ReportVO[] queryReportForCCP(IQueryScheme scheme)
			throws BusinessException {
	    try {
	        // �������sql
	        String sql = this.getQuerySql(scheme);
	        String[] bids =
	            new DataAccessUtils().query(sql).toOneDimensionStringArray();
	        if (ArrayUtils.isEmpty(bids)) {
	          return null;
	        }
	        // ����ͼVOת��Ϊ�ʼ챨���VO
	        ReportViewVO[] views =
	            new ViewQuery<ReportViewVO>(ReportViewVO.class).query(bids);
	        CombineViewToAggUtil<ReportVO> util =
	            new CombineViewToAggUtil<ReportVO>(ReportVO.class,
	                ReportHeaderVO.class, ReportItemVO.class);
	        return util.combineViewToAgg(views, ReportHeaderVO.PK_REPORTBILL);
	      }
	      catch (Exception ex) {
	        ExceptionUtils.marsh(ex);
	      }
	      return null;
	    }
	
	private String getQuerySql(IQueryScheme scheme) {
	    // ��ѯ����������
	    QuerySchemeProcessor pc = new QuerySchemeProcessor(scheme);
	   
	    
	    pc.appendCurrentGroup();
	    // ƴ����Ȩ�޵���֯����
	    pc.appendFuncPermissionOrgSql();
	    // ��ñ�ı���
	    String htname = pc.getMainTableAlias();
	    if (StringUtils.isEmpty(htname)) {
	      htname = "qc_reportbill";
	    }
	    String btname =
	        pc.getTableAliasOfAttribute("pk_reportbill_b.pk_reportbill_b");
	    StringBuilder sql = new StringBuilder();
	    // ����ѡ��Ƭ��
	    // ���������������ʼ챨��ſɲ�ѯ��������ͨ���ġ�Ĭ�Ϸ����µġ���������κŵġ�δ���ɹ���֤����ʼ챨�档
	    sql.append(" select " + btname + ".pk_reportbill_b ");
	    sql.append(pc.getFinalFromWhere());
	    // ���ӹ̶�����
	    sql.append(" and qc_reportbill.dr = 0 ");
	    // ֻ��ʾ�����¼���汾������(����ʾ��ʷ��������)
	    sql.append(" and qc_reportbill.bnewqcvsn = 'Y' ");
	    // ���״̬
	    sql.append(" and qc_reportbill.fbillstatus = 3 ");
	    // û�����ɹ���֤��
	   // sql.append(" and isnull(qc_reportbill.bcrtcertbill,'N') = 'N' ");
	    // ��������κŵ�
	    //��������  
	    sql.append(" and qc_reportbill.ctrantypeid ='0001A410000000001HVL' ");
	    sql.append(" and " + btname + ".vbatchcode is not null");
	    sql.append(" and (not exists(select 1 from ic_finprodin_b b where b.csourcebillhid=qc_reportbill.pk_reportbill and nvl(b.dr,0)=0) "
	    		+ "or nvl(qc_reportbill.ncheckastnum,0)>(select sum(nvl(b.nassistnum,0)) from ic_finprodin_b b "
	    		+ "where b.csourcebillhid=qc_reportbill.pk_reportbill and nvl(b.dr,0)=0))");
	    String finalSql = sql.toString();
	    finalSql = StringUtils.replace(finalSql, "qc_reportbill.", htname + ".");
	    return finalSql;
	  }

}
