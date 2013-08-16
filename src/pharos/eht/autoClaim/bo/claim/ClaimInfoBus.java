package pharos.eht.autoClaim.bo.claim;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import pharos.eht.autoClaim.vo.ClaimVO;

public class ClaimInfoBus {

	
	private final static Log log = LogFactory.getLog(ClaimInfoBus.class);
	
	
	/**
	 * 面向外部接口的统一处理保单的方法，通过指定的参数进行查询保单，并组织对应报文进行返回
	 * @param insuranceNo		保单号
	 * @param certificatetype	证件类型
	 * @param certificatecode	证件号码
	 * @return
	 * @throws Exception
	 */
	public String getClaimXML(String insuranceNo, String certificatetype, String certificatecode, String happendate) throws Exception{
		
		ClaimInfoBO bo = new ClaimInfoBO();
		ClaimVO claim = bo.getRepeatClaimInfo();
		if(claim==null) {
			log.error("无相关报案记录保单号：" +insuranceNo + ",证件号码：" + certificatecode+",证件类型："
					+ certificatetype + ",出险日期：" + happendate);
//			String configError = "";
//			if(insuranceNo.equals("reboot")) {
//				configError = PropertiesReader4prop.reBoot().readValueByName("CLMErrorInfo");
//				log.error("reboot info");
//				System.out.println("rrrrrrrrrr");
//			} else {
//				configError = PropertiesReader4prop.getInstance().readValueByName("CLMErrorInfo");
//				log.error("instance info");
//				System.out.println("iiiiiiiiiii");
//			}
//			String ret = "";
//			try {
//				ret = new String(configError.getBytes("ISO-8859-1"),"utf-8");
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
			throw new Exception("没有查到出现日期内的报案记录");
		}
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("body");

		Element head = root.addElement("head");
		head.addElement("code").setText("1");
		head.addElement("message").setText("成功");

		Element body = root.addElement("FormInfo");
		body.addElement("ReportDate").setText("");//报案日期2013-03-25 13:12:38
		body.addElement("ReportContent").setText("");//事故信息
		body.addElement("CallUserName").setText("");//报案人
		body.addElement("CityId").setText("");//所属机构ID
		body.addElement("City").setText("");//所属机构中文
		body.addElement("CardType").setText("");//证件类型
		body.addElement("ChitName").setText("");//被保险人
		body.addElement("RecordNum").setText("");//报案号
		body.addElement("InsuranceType").setText("");//险种代码
		body.addElement("InsuranceName").setText("");//险种名称
		body.addElement("IsWB").setText("否");
		body.addElement("HappenDate").setText("");//出险日期
		body.addElement("CurrCaseState").setText("");//案件状态
		body.addElement("ChitCode").setText("");//保单号
		body.addElement("ContactPhone").setText("");//联系电话
		body.addElement("CardCode").setText("");//证件号码
		body.addElement("ReportReasonCode").setText("");//出险原因编码
		body.addElement("ReportReasonName").setText("");//出险原因名称
		body.addElement("UserEmail").setText("");//客户邮箱
		body.addElement("Channel").setText("");//案件来源 1
		body.addElement("FlightNo").setText("");//航班号
		body.addElement("FlightTime").setText("");//起飞时间
		body.addElement("AccountName").setText("");//户名
		body.addElement("BankCode").setText("");//开户行代码
		body.addElement("BankName").setText("");//开户行名称
		body.addElement("AccountBankNo").setText("");//银行账号
		body.addElement("BankProvinceCode").setText("");//开户行所在省代码
		body.addElement("BankProvinceName").setText("");//开户行所在省名称
		body.addElement("BankCityCode").setText("");//开户行所在市编码
		body.addElement("BankCityName").setText("");//市名称
		
		return doc.asXML();
	}
}
