package nc.impl.webservice;

import org.w3c.dom.Document;

import nc.bs.dao.BaseDAO;
import nc.itf.webservice.IErmSendToNCSystem;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.gl.pubtools.DOMUtils;
import nc.vo.pfxx.pub.PostFile;
import nc.vo.pub.BusinessException;

public class ErmSendToNCSystemImpl implements IErmSendToNCSystem {

	@Override
	public String sendXml(String xml) throws BusinessException {
		
		String str="";
		//PostFile ps=new PostFile();
		Document doc=DOMUtils.parseXMLDocument(xml);
		//String corp=doc.getDocumentElement().getAttribute("receiver");
		String sql1="select defaultvalue from pub_sysinittemp where initcode='TCL11' ";
		String sql2="select defaultvalue from pub_sysinittemp where initcode='TCL12' ";
		BaseDAO dao=new BaseDAO("design");
		Object obj1=dao.executeQuery(sql1, new ColumnProcessor());
		Object obj2=dao.executeQuery(sql2, new ColumnProcessor());
		if(obj1==null||"".equals(obj1)||obj2==null||"".equals(obj2)){
			return "请先配置集团参数TCL01,TCL02，用于获取数据库连接信息！";
		}
		String url=obj1+"/service/XChangeServlet?account="+obj2+"&groupcode=G&langcode=simpchn";
		try {
			str=PostFile.sendDocument(doc, url, "UTF-8",true, null);
		} catch (Exception e) {
			e.printStackTrace();
			str=e.getMessage();
		}
		
		return str;
	}

}
