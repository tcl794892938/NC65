package nc.itf.webservice;

import nc.vo.pub.BusinessException;

public interface IErmSendToNCSystem {
	
	public String sendXml(String xml)throws BusinessException;

}
