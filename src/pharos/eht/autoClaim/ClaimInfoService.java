package pharos.eht.autoClaim;

import javax.servlet.http.HttpServletRequest;

import org.apache.axis2.context.MessageContext;
import org.apache.axis2.transport.http.HTTPConstants;

import pharos.eht.autoClaim.bo.claim.ClaimInfoBus;

public class ClaimInfoService {

	/**
	 * 使用证件号码8888或者9999可以获取测试数据。
	 * @param insuranceNo
	 * @param certificatetype
	 * @param certificatecode
	 * @param happendate
	 * @param userid
	 * @param userpass
	 * @return
	 */
	public String getClaimInfo(String insuranceNo,String certificatetype,String certificatecode,String happendate,String userid,String userpass) {
		
		if(userid==null || !userid.trim().equals("autoClaim01")) {
			System.out.println(userid);
			return buildErrorInfo4Claim("0", "接口调用户名错误");
		}
		
		if(userpass==null || !userpass.trim().equals("autoClaim01")) {
			System.out.println(userpass);
			return buildErrorInfo4Claim("0", "接口调用密码错误");
		}
		
		if(insuranceNo==null || insuranceNo.trim().equals("")) {
			System.out.println(userpass);
			return buildErrorInfo4Claim("0", "没有找到赔案记录，可能保单号有问题");
		}
		
		if(certificatetype==null || certificatetype.trim().equals("")) {
			System.out.println(userpass);
			return buildErrorInfo4Claim("0", "没有找到赔案记录，可能证件类型有问题");
		}
		
		if(certificatecode==null || certificatecode.trim().equals("")) {
			System.out.println(userpass);
			return buildErrorInfo4Claim("0", "没有找到赔案记录，可能证件号码有问题");
		}
		
		if(happendate==null || happendate.trim().equals("")) {
			System.out.println(userpass);
			return buildErrorInfo4Claim("0", "没有找到赔案记录，可能出险时间有问题");
		}
		
		return buildSuccessInfo4Claim(insuranceNo, certificatetype, certificatecode, happendate);
	}
	
	private String buildErrorInfo4Claim(String errorCode, String errorInfo) {
		StringBuilder xmlSB = new StringBuilder();
		xmlSB.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		xmlSB.append("<head>");
		xmlSB.append("<code>"+errorCode+"</code>");
		xmlSB.append("<message>"+errorInfo+"</message>");
		xmlSB.append("</head>");
		return xmlSB.toString();
	}
	
	private String buildSuccessInfo4Claim(String insuranceNo, String certificatetype, String certificatecode, String happendate) {
		if(certificatecode.equals("8888") || certificatecode.equals("9999")) {
			return getDemo4Test(insuranceNo, certificatetype, certificatecode, happendate);
		}
		ClaimInfoBus bus = new ClaimInfoBus();
		try {
			return bus.getClaimXML(insuranceNo, certificatetype, certificatecode, happendate);	
		} catch (Exception e) {
			return buildErrorInfo4Claim("0", e.getMessage());
		}
		
	}
	
	private String getDemo4Test(String insuranceNo, String certificatetype, String certificatecode, String happendate) {
		StringBuilder xmlSB = new StringBuilder();
		xmlSB.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		xmlSB.append("<body>");
		xmlSB.append("<head>");
		xmlSB.append("<code>1</code>");
		xmlSB.append("<message>成功</message>");
		xmlSB.append("</head>");
		xmlSB.append("<FormInfo>");
		xmlSB.append("<ReportDate>2013-03-25 13:12:38</ReportDate>");
		xmlSB.append("<ReportContent>"+"大雾导致航班延误"+"</ReportContent>");
		xmlSB.append("<CallUserName>"+"张三"+"</CallUserName>");
		xmlSB.append("<CityId>"+"901"+"</CityId>");
		xmlSB.append("<City>"+"北京分公司"+"</City>");
		xmlSB.append("<CardType>"+certificatetype+"</CardType>");
		xmlSB.append("<ChitName>"+"张三"+"</ChitName>");
		xmlSB.append("<RecordNum>"+"3010000123456789"+"</RecordNum>");
		xmlSB.append("<InsuranceType>"+"H01"+"</InsuranceType>");
		xmlSB.append("<InsuranceName>航空险</InsuranceName>");
		xmlSB.append("<IsWB>否</IsWB>");
		xmlSB.append("<HappenDate>"+happendate+"</HappenDate>");
		xmlSB.append("<CurrCaseState>已报案</CurrCaseState>");
		xmlSB.append("<ChitCode>"+insuranceNo+"</ChitCode>");
		xmlSB.append("<ContactPhone>13311863333</ContactPhone>");
		xmlSB.append("<CardCode>"+certificatecode+"</CardCode>");
		xmlSB.append("<ReportReasonCode>04</ReportReasonCode>");
		xmlSB.append("<ReportReasonName>航空延误</ReportReasonName>");
		xmlSB.append("<UserEmail>zhangsan@163.com</UserEmail>");
		xmlSB.append("<Channel>1</Channel>");
		xmlSB.append("<FlightNo>AU001</FlightNo>");
		xmlSB.append("<FlightTime>2013-03-2513:12:38</FlightTime>");
		xmlSB.append("<AccountName>张三</AccountName>");
		xmlSB.append("<BankCode>21</BankCode>");
		xmlSB.append("<BankName>工商银行北京支行</BankName>");
		xmlSB.append("<AccountBankNo>132323998877444</AccountBankNo>");
		xmlSB.append("<BankProvinceCode>0343</BankProvinceCode>");
		xmlSB.append("<BankProvinceName>河北省</BankProvinceName>");
		xmlSB.append("<BankCityCode>01</BankCityCode>");
		xmlSB.append("<BankCityName>石家庄</BankCityName>");
		xmlSB.append("</FormInfo>");
		xmlSB.append("</body>");
		return xmlSB.toString();
	}
	
	

	/**
	 * 获取客户端IP地址，如调用方的IP，以便检查权限。
	　* 适用于axis发布的webservice
	 * @return
	 */
	public String getClientIpAxis() {
	
		MessageContext mc = null;
		HttpServletRequest request = null;
		try {
			mc = MessageContext.getCurrentMessageContext();
			if (mc == null)
			throw new Exception("无法获取到MessageContext");
		
			request = (HttpServletRequest) mc.getProperty(HTTPConstants.MC_HTTP_SERVLETREQUEST);
			System.out.println("remote  ip:  " + request.getRemoteAddr());
		
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	
		return request.getRemoteAddr();
	
	}

}
