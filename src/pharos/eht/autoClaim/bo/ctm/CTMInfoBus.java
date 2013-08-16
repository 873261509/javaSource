package pharos.eht.autoClaim.bo.ctm;

import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import pharos.eht.autoClaim.vo.ConditionVO;
import pharos.eht.autoClaim.vo.PolicyVO;

public class CTMInfoBus {
	
	private final static Log log = LogFactory.getLog(CTMInfoBus.class);
	
	/**
	 * 面向外部接口的统一处理保单的方法，通过指定的参数进行查询保单，并组织对应报文进行返回
	 * @param insuranceNo		保单号
	 * @param certificatetype	证件类型
	 * @param certificatecode	证件号码
	 * @return
	 * @throws Exception
	 */
	public String getXML(String insuranceNo, String certificatetype, String certificatecode) throws Exception{
		
		CTMInfoBO bo = new CTMInfoBO();
		Set<PolicyVO> policys = bo.buildPolicyInfo(insuranceNo, certificatetype, certificatecode);
		if(policys==null || policys.size()==0) {
			log.error("没有找到任何保单，无法构建成功报文,证件类型：" + certificatetype + ",证件号码：" + certificatecode);
			throw new Exception("没有找到任何保单");
		}
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("root");

		Element head = root.addElement("head");
		Element code = head.addElement("code");
		Element message = head.addElement("message");
		code.setText("1");
		message.setText("成功");

		Element body = root.addElement("body");
		Element policyDtoList = body.addElement("policyDtoList");
		for (PolicyVO policyVO : policys) {
			Element policyDto = policyDtoList.addElement("policyDto");
			policyDto.addElement("policyNo").setText(policyVO.getPolicyNo());
			policyDto.addElement("riskCode").setText(policyVO.getRiskCode());
			policyDto.addElement("riskName").setText(policyVO.getRiskName());
			policyDto.addElement("comCode").setText(policyVO.getComCode());
			policyDto.addElement("comName").setText(policyVO.getComName());
			policyDto.addElement("insureStart").setText(policyVO.getInsuredStart());
			policyDto.addElement("insureEnd").setText(policyVO.getInsuredEnd());
			policyDto.addElement("chitName").setText(policyVO.getInsuredName());
			policyDto.addElement("certificatetype").setText(policyVO.getInsuredIDType());
			policyDto.addElement("certificatecode").setText(policyVO.getInsuredIDNo());
			policyDto.addElement("contactPhone").setText(policyVO.getContactPhone());
			policyDto.addElement("userEmail").setText("");
			Element dutyDtoList = policyDto.addElement("dutyDtoList");
			
			List<ConditionVO> cons = policyVO.getCons();
			if(cons==null || cons.size()==0) {
				log.warn("保单没有找到任何责任，保单号：" +policyVO.getPolicyNo() + "批单号："+policyVO.getEndorseNo());
				Element dutyDto = dutyDtoList.addElement("dutyDto");
				dutyDto.addElement("insureDutyId").setText("");
				dutyDto.addElement("insureDuty").setText("");
				dutyDto.addElement("insureMoney").setText("");
				dutyDto.addElement("insureDutyInfo");
			} else {
				for (ConditionVO conditionVO : cons) {
					Element dutyDto = dutyDtoList.addElement("dutyDto");
					dutyDto.addElement("insureDutyId").setText(conditionVO.getConditionCode());
					dutyDto.addElement("insureDuty").setText(conditionVO.getConditionName());
					dutyDto.addElement("insureMoney").setText(conditionVO.getConditionAmout());
					dutyDto.addElement("insureDutyInfo").setText(conditionVO.getConditionName());
				}
			}
		}
		return doc.asXML();
	}
}
