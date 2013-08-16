package pharos.eht.autoClaim.vo;

import java.util.List;

public class PolicyVO {

	private String policyNo = "";
	private String endorseNo = "";
	private String riskCode = "";
	private String riskName = "";
	private String comCode = "";
	private String comName = "";
	private String insuredStart = "";
	private String insuredEnd = "";
	private String insuredName = "";
	private String insuredIDType = "";
	private String insuredIDNo = "";
	private String contactPhone = "";
	private String insuredEmail = "";
	
	private String nodeNo = "";
	
	private boolean airePolicy = true;
	
	private List<ConditionVO> cons;
	
	public String getPolicyNo() {
		if(policyNo==null) {
			policyNo = "";
		}
		return policyNo;
	}
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	
	public String getRiskCode() {
		if(riskCode==null) {
			riskCode = "";
		}
		return riskCode;
	}
	public void setRiskCode(String riskCode) {
		this.riskCode = riskCode;
	}
	public String getRiskName() {
		if(riskName==null) {
			riskName = "";
		}
		return riskName;
	}
	public void setRiskName(String riskName) {
		this.riskName = riskName;
	}
	public String getComCode() {
		if(comCode==null) {
			comCode = "";
		}
		return comCode;
	}
	public void setComCode(String comCode) {
		this.comCode = comCode;
	}
	public String getComName() {
		if(comName==null) {
			comName = "";
		}
		return comName;
	}
	public void setComName(String comName) {
		this.comName = comName;
	}
	public String getInsuredStart() {
		if(insuredStart==null) {
			insuredStart = "";
		}
		return insuredStart;
	}
	public void setInsuredStart(String insuredStart) {
		this.insuredStart = insuredStart;
	}
	public String getInsuredEnd() {
		if(insuredEnd==null) {
			insuredEnd = "";
		}
		return insuredEnd;
	}
	public void setInsuredEnd(String insuredEnd) {
		this.insuredEnd = insuredEnd;
	}
	public String getInsuredName() {
		if(insuredName==null) {
			insuredName = "";
		}
		return insuredName;
	}
	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}
	public String getInsuredIDType() {
		if(insuredIDType==null) {
			insuredIDType = "";
		}
		return insuredIDType;
	}
	public void setInsuredIDType(String insuredIDType) {
		this.insuredIDType = insuredIDType;
	}
	public String getInsuredIDNo() {
		if(insuredIDNo==null) {
			insuredIDNo = "";
		}
		return insuredIDNo;
	}
	public void setInsuredIDNo(String insuredIDNo) {
		this.insuredIDNo = insuredIDNo;
	}
	public String getContactPhone() {
		if(contactPhone==null) {
			contactPhone = "";
		}
		return contactPhone;
	}
	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}
	public String getInsuredEmail() {
		if(insuredEmail==null) {
			insuredEmail = "";
		}
		return insuredEmail;
	}
	public void setInsuredEmail(String insuredEmail) {
		this.insuredEmail = insuredEmail;
	}
	public List<ConditionVO> getCons() {
		return cons;
	}
	public void setCons(List<ConditionVO> cons) {
		this.cons = cons;
	}
	public String getEndorseNo() {
		if(endorseNo==null) {
			endorseNo = "";
		}
		return endorseNo;
	}
	public void setEndorseNo(String endorseNo) {
		this.endorseNo = endorseNo;
	}
	
	public PolicyVO clone() {
		PolicyVO pv = new PolicyVO();
		pv.setInsuredIDNo(this.getInsuredIDNo());
		pv.setInsuredIDType(this.getInsuredIDType());
		return pv;
	}
	public boolean isAirePolicy() {
		return airePolicy;
	}
	public void setAirePolicy(boolean airePolicy) {
		this.airePolicy = airePolicy;
	}
	public String getNodeNo() {
		if(nodeNo==null) {
			nodeNo = "";
		}
		return nodeNo;
	}
	public void setNodeNo(String nodeNo) {
		this.nodeNo = nodeNo;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((policyNo == null) ? 0 : policyNo.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PolicyVO other = (PolicyVO) obj;
		if (policyNo == null) {
			if (other.policyNo != null)
				return false;
		} else if (!policyNo.equals(other.policyNo))
			return false;
		return true;
	}
}
