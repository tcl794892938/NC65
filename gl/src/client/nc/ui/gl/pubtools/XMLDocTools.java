package nc.ui.gl.pubtools;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.gl.query.IVoucherQuery;
import nc.vo.gl.pubparam.FileAbsPath;
import nc.vo.gl.pubvoucher.DetailVO;
import nc.vo.gl.pubvoucher.VoucherVO;
import nc.vo.glcom.ass.AssVO;
import nc.vo.pfxx.pub.PostFile;
import nc.vo.pub.lang.UFDouble;

public class XMLDocTools {

	public String outputXMLDoc(VoucherVO[] vos, String pk_corp,String url,String account)// 公司编码
			throws Exception {
		
		IVoucherQuery ivq = NCLocator.getInstance().lookup(IVoucherQuery.class);

		List<Map<String, Object>> list = ivq.queryVoucherBills(vos);

		Map<String, Map<String, Object>> mapmap = new HashMap<String, Map<String, Object>>();

		if (list != null && list.size() > 0) {
			for (Map<String, Object> map : list) {
				mapmap.put(map.get("pk_voucher").toString(), map);
			}
		}
		
		String dev=account;
		String pk_group=FileAbsPath.send_group;
		String defbook = FileAbsPath.send_book;// 账薄
		
		StringBuffer strb=new StringBuffer();
		strb.append("<?xml version='1.0' encoding='UTF-8'?>\n");
		strb.append("<ufinterface account='"+dev+"' billtype='vouchergl' businessunitcode='"+dev+"' filename='' groupcode='G' isexchange='' orgcode='' receiver='' replace='' roottag='' sender='tcl01'>\n");
		for(VoucherVO vo : vos){
			String pk=vo.getPk_voucher();
			strb.append("	<voucher id='"+vo.getPk_voucher()+"'>\n");
			strb.append("		<voucher_head>\n");
			strb.append("			<pk_vouchertype>"+getStrObj(mapmap.get(pk).get("code"))+"</pk_vouchertype>\n");
			strb.append("			<year>"+vo.getYear()+"</year>\n");
			strb.append("			<pk_system>GL</pk_system>\n");
			strb.append("			<voucherkind>0</voucherkind>\n");
			strb.append("			<pk_accountingbook>"+pk_corp+"-"+defbook+"</pk_accountingbook>\n");
			strb.append("			<discardflag>N</discardflag>\n");
			strb.append("			<period>"+vo.getPeriod()+"</period>\n");
			strb.append("			<no>"+vo.getNo()+"</no>\n");
			strb.append("			<attachment>"+vo.getAttachment()+"</attachment>\n");
			strb.append("			<prepareddate>"+vo.getPrepareddate()+"</prepareddate>\n");
			strb.append("			<pk_prepared>"+getStrObj(mapmap.get(pk).get("user_code"))+"</pk_prepared>\n");
			if(vo.getTallydate()==null&&vo.getPk_manager().equals("N/A")){//制单
				strb.append("			<pk_manager></pk_manager>\n");
				strb.append("			<tallydate></tallydate>\n");
			}else{
				strb.append("			<pk_manager>"+getStrObj(mapmap.get(pk).get("user_code3"))+"</pk_manager>\n");
				strb.append("			<tallydate>"+getStrObj(vo.getTallydate())+"</tallydate>\n");
			}
			strb.append("			<pk_org>"+pk_corp+"</pk_org>\n");
			strb.append("			<pk_group>"+pk_group+"</pk_group>\n");
			strb.append("			<details>\n");
			
			DetailVO[] dvos=vo.getDetails();
			for(int i=0;i<dvos.length;i++){
				DetailVO dvo=dvos[i];
				strb.append("				<item>\n");
				strb.append("					<detailindex>"+dvo.getDetailindex()+"</detailindex>\n");
				strb.append("					<explanation>"+dvo.getExplanation()+"</explanation>\n");
				strb.append("					<price>"+dvo.getPrice()+"</price>\n");
				
				UFDouble ud=dvo.getExcrate2()==null?new UFDouble(1):dvo.getExcrate2();
				strb.append("					<excrate2>"+ud+"</excrate2>\n");
				
				strb.append("					<debitquantity>"+dvo.getDebitquantity()+"</debitquantity>\n");
				strb.append("					<debitamount>"+dvo.getDebitamount()+"</debitamount>\n");
				strb.append("					<localdebitamount>"+dvo.getLocaldebitamount()+"</localdebitamount>\n");
				strb.append("					<groupdebitamount>"+dvo.getGroupdebitamount()+"</groupdebitamount>\n");
				strb.append("					<globaldebitamount>"+dvo.getGlobaldebitamount()+"</globaldebitamount>\n");
				strb.append("					<creditquantity>"+dvo.getCreditquantity()+"</creditquantity>\n");
				strb.append("					<creditamount>"+dvo.getCreditamount()+"</creditamount>\n");
				strb.append("					<localcreditamount>"+dvo.getLocalcreditamount()+"</localcreditamount>\n");
				strb.append("					<groupcreditamount>"+dvo.getGroupcreditamount()+"</groupcreditamount>\n");
				strb.append("					<globalcreditamount>"+dvo.getGlobalcreditamount()+"</globalcreditamount>\n");
				
				strb.append("					<pk_currtype>"+dvo.getCurrtypecode()+"</pk_currtype>\n");
				strb.append("					<pk_accasoa>"+dvo.getAccsubjcode()+"</pk_accasoa>\n");
				
				
				AssVO[] assvos = dvo.getAss();
				if(assvos!=null&&assvos.length>0){
					strb.append("					<ass>\n");
					for(AssVO svo:assvos){
						strb.append("						<item>\n");
						strb.append("							<pk_Checktype>"+svo.getChecktypecode()+"</pk_Checktype>\n");
						strb.append("							<pk_Checkvalue>"+svo.getCheckvaluecode()+"</pk_Checkvalue>\n");
						strb.append("						</item>\n");
					}
					strb.append("					</ass>\n");
				}
				strb.append("				</item>\n");
			}
			strb.append("			</details>\n");
			strb.append("		</voucher_head>\n");
			strb.append("	</voucher>\n");
		}
		strb.append("</ufinterface>\n");
		
		/*String str1=new String(strb.toString().getBytes("UTF-8"),"UTF-8");
		org.w3c.dom.Document doc3=DOMUtils.parseXMLDocument(str1);*/
		
		org.w3c.dom.Document doc3=DOMUtils.parseXMLDocument(strb.toString());
		
		// 向外部交换平台传输数据
		String str="";
		
		try {
			str=PostFile.sendDocument(doc3, url, "UTF-8",true,null);
		} catch (Exception e) {
			str=e.getMessage();
		}
		
		return str;
	}

	private String getStrObj(Object obj) {
		return obj == null ? "" : obj.toString();
	}

}
