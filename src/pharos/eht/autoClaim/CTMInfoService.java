package pharos.eht.autoClaim;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import pharos.eht.autoClaim.bo.ctm.CTMInfoBus;


/**
 * 该接口需要注意两个方面：
 * 1、给华道通讯：必须作为一个ws服务器端，提供方法给华道，用来保存理赔数据
 * 2、给pharos通讯：I、如果使用pharos的缓存，可以通过调用pharos的远程接口来进行数据保存。
 * 				II、如果使用ws和pharos通讯，则需要作为客户端调用pharos的ws接口。
 * 
 * 
 * @author chenpanfeng
 *
 */
public class CTMInfoService {
	
	private final static Log log = LogFactory.getLog(CTMInfoService.class);
	
	/**
	 * 获取保单信息
	 * @param insuranceNo 		保单号，可以为空
	 * @param certificatetype	证件类型，不能为空
	 * @param certificatecode	证件号码，不能为空
	 * @param userid			调用方法指派的户名，不能为空
	 * @param userpass			调用方法指派的密码，不能为空
	 * @return
	 */
	public String getCTMInfo(String insuranceNo, String certificatetype, String certificatecode, String userid, String userpass) {
		if(userid==null || !userid.trim().equals("autoClaim01")) {
			log.error("接口调用户名错误=" + userid);
			return buildErrorInfo("0", "接口调用户名错误");
		}
		
		if(userpass==null || !userpass.trim().equals("autoClaim01")) {
			log.error("接口调用密码错误="+userpass);
			return buildErrorInfo("0", "接口调用密码错误");
		}
		
		if(insuranceNo!=null) {
			insuranceNo = insuranceNo.trim();
		}
		
		if(insuranceNo!=null && !insuranceNo.trim().equals("")) {
			insuranceNo = insuranceNo.trim();
		}
		
		if(certificatetype==null || certificatetype.trim().equals("")) {
			log.error("参数失败，证件类型不能为空");
			return buildErrorInfo("0", "参数失败，证件类型不能为空");
		}
		
		if(certificatecode==null || certificatecode.trim().equals("")) {
			log.error("参数失败，证件号不能为空");
			return buildErrorInfo("0", "参数失败，证件号不能为空");
		}
		
		return buildSuccessInfo(insuranceNo,certificatetype.trim(),certificatecode.trim());
	}
	
	/**
	 * 构建失败信息
	 * @param errorCode	错误码
	 * @param errorInfo	错误信息
	 * @return
	 */
	private String buildErrorInfo(String errorCode, String errorInfo) {
		StringBuilder xmlSB = new StringBuilder();
		xmlSB.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		xmlSB.append("<root>");
		xmlSB.append("<head>");
		xmlSB.append("<code>"+errorCode+"</code>");
		xmlSB.append("<message>"+errorInfo+"</message>");
		xmlSB.append("</head>");
		xmlSB.append("</root>");
		return xmlSB.toString();
	}
	
	/**
	 * 构建获取保单成功后的报文信息
	 * @param insuranceNo		保单号，可以为空
	 * @param certificatetype	证件类型，不能为空
	 * @param certificatecode	证件号码，不能为空
	 * @return
	 */
	private String buildSuccessInfo(String insuranceNo, String certificatetype, String certificatecode) {
		CTMInfoBus t = new CTMInfoBus();
		try {
			return t.getXML(insuranceNo, certificatetype, certificatecode);
		}catch (Exception e) {
			log.error(e.getLocalizedMessage());
			log.error(e.getMessage());
			return buildErrorInfo("0", e.getMessage());
		}
	}
	
}
