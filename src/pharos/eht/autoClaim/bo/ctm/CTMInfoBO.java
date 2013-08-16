package pharos.eht.autoClaim.bo.ctm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import pharos.eht.autoClaim.utils.ConstConfigName;
import pharos.eht.autoClaim.utils.JDBCHelper;
import pharos.eht.autoClaim.utils.PropertiesReader;
import pharos.eht.autoClaim.vo.ConditionVO;
import pharos.eht.autoClaim.vo.PolicyVO;

public class CTMInfoBO {
	
	private final static Log log = LogFactory.getLog(CTMInfoBO.class);
	
	/**
	 * 构建保单信息，并返回查询到的所有保单集合
	 * @param insuranceNo		保单号	可以为空
	 * @param certificatetype	证件类型
	 * @param certificatecode	证件号码
	 * @return
	 */
	public Set<PolicyVO> buildPolicyInfo(String insuranceNo, String certificatetype, String certificatecode) {
		Set<PolicyVO> rets = new HashSet<PolicyVO>();
		//根基，只存基本信息，不加入返回队列
		PolicyVO retRoot = new PolicyVO();
		Connection conn = null;
		try {
			conn = JDBCHelper.getInstance().getConnection();
			
			List<String> partyCodes = getPartyInfo(conn, certificatetype, certificatecode);
			retRoot.setInsuredIDNo(certificatecode);
			retRoot.setInsuredIDType(certificatetype);
			
			if(partyCodes==null || partyCodes.size()==0) {
				log.error("没有找到客户信息，证件类型：" + certificatetype + "证件号码:" + certificatecode);
			}
			
			for (String partyCode : partyCodes) {
				
				getClientInfo(conn, partyCode, retRoot, rets, insuranceNo);
				if(rets.size()==0)continue;
				if(insuranceNo!=null && !"".equals(insuranceNo)) {
					if(!insuranceNo.equals(retRoot.getPolicyNo())) {
						log.error("给定的保单号和查询的不一致，用户输入的保单号：" + insuranceNo +",查询的保单号：" + retRoot.getPolicyNo());
					}
				}
				
				List<PolicyVO> airelessPolicy = new ArrayList<PolicyVO>();
				
				for (PolicyVO policyVO : rets) {
					boolean isNeedNext = getPolicyInfo(conn, policyVO);
					if(!isNeedNext) {
						airelessPolicy.add(policyVO);
						continue;
					}
					getConditionInfo(conn, policyVO);
				}
				rets.removeAll(airelessPolicy);
//				break;
			}
			
			
		}finally{
			JDBCHelper.getInstance().closeConn(conn);
		}
	
		return rets;
	}
	
	/**
	 * 根据身份证找到可以对应的编码信息
	 * @param conn
	 * @param govType
	 * @param govCode
	 * @return
	 */
	public List<String> getPartyInfo(Connection conn, String govType, String govCode) {
		List<String> ret = new ArrayList<String>();
		String sql = "select t.partycode " +
				" from htyw_paods.t_partycertid t  " +
				" where t.govissuredcode=?";
		//and t.govissuredtype=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, govCode);
			//pstmt.setString(2, govType);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				ret.add(rs.getString(1));
			}
		}catch (Exception e) {
			log.error("查询客户信息异常1，证件类型:" + govType + "证件号码：" + govCode);
			log.error("查询客户信息异常2，查询脚本:"+sql);
			log.error("查询客户信息异常3，异常信息:" + e.getLocalizedMessage());
			log.error("查询客户信息异常4，异常信息:" + e.getMessage());
			log.error("查询客户信息异常5，异常信息:" + e.getStackTrace());
		}finally{ 
			JDBCHelper.getInstance().closeResultSet(rs);
			JDBCHelper.getInstance().closePstmt(pstmt);
		}
		return ret;
	}
	
	/**
	 * 根据客户编码找客户基本信息以及部分保单信息
	 * @param conn
	 * @param partyCode
	 * @return
	 */
	public void getClientInfo(Connection conn, String partyCode, PolicyVO rootRet, Set<PolicyVO> rets, String insuranceNo) {
		String sql = "select t.policyno, t.endorseno, t.phone, t.partyname, t.nodeno " +
				"from htyw_paods.t_policyinsure t where t.partycode=?";
		boolean hasInsuranceNo = false;
		if(insuranceNo!=null && !insuranceNo.equals("")) {
			hasInsuranceNo = true;
			sql += " and t.policyno=?";
		}
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, partyCode);
			if(hasInsuranceNo) {
				pstmt.setString(2, insuranceNo);
			}
			rs = pstmt.executeQuery();
			while(rs.next()) {
				PolicyVO ret = rootRet.clone();
				ret.setPolicyNo(rs.getString(1));
				ret.setEndorseNo(rs.getString(2));
				ret.setContactPhone(rs.getString(3));
				ret.setInsuredName(rs.getString(4));
				ret.setNodeNo(rs.getString(5));
				rets.add(ret);
			}
		}catch (Exception e) {
			log.error("查询客户保单信息异常1，客户编码:" + partyCode + "保单号：" + insuranceNo);
			log.error("查询客户保单信息异常2，查询脚本:"+sql);
			log.error("查询客户保单信息异常3，异常信息:" + e.getLocalizedMessage());
			log.error("查询客户保单信息异常4，异常信息:" + e.getMessage());
			log.error("查询客户保单信息异常5，异常信息:" + e.getStackTrace());
		}finally{ 
			JDBCHelper.getInstance().closeResultSet(rs);
			JDBCHelper.getInstance().closePstmt(pstmt);
		}
	}
	
	/**
	 * 根据保单号查找保单详细信息
	 * @param conn
	 * @param policyNo
	 * @param endorseNo
	 * @return
	 */
	public boolean getPolicyInfo(Connection conn, PolicyVO policyVO) {
		String sql = "select  t.insurancecode, t.AcquisitionBranch , " +
				"t.startdate, t.enddate, t.insuredname, t.productcode" +
				" from htyw_paods.t_policy t " +
				"where t.policyno=? and t.endorseno=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean ret = true;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, policyVO.getPolicyNo());
			pstmt.setString(2, policyVO.getEndorseNo());
			rs = pstmt.executeQuery();
			while(rs.next()) {
				String proCode = rs.getString(6);
				List<String> pcs = PropertiesReader.getAirProductCodes();
				if(pcs.size()>0 && !PropertiesReader.getAirProductCodes().contains(proCode)) {
					policyVO.setAirePolicy(false);
					ret = false;
					continue;
				}
				policyVO.setRiskCode(rs.getString(1));
				String riskName  = getParametervalueInfo(conn,ConstConfigName.PV_CODETYPE_INSURANCE,rs.getString(1), true);
				policyVO.setRiskName(riskName);
				policyVO.setComCode(rs.getString(2));
				policyVO.setComName(getBranchName(conn, rs.getString(2)));
				policyVO.setInsuredStart(rs.getString(3));
				policyVO.setInsuredEnd(rs.getString(4));
				if(!policyVO.getInsuredName().equals(rs.getString(5))) {
					//log 保单名字不一致
				}
				ret = true;
				policyVO.setAirePolicy(true);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally{ 
			JDBCHelper.getInstance().closeResultSet(rs);
			JDBCHelper.getInstance().closePstmt(pstmt);
		}
		return ret;
	}
	
	/**
	 * 通过参数表获取险种名称
	 * 如codetype=HT300007 code=4660  
	 * 
	 * @param conn
	 * @param insurancecode
	 * @return
	 */
	private String getParametervalueInfo(Connection conn, String codeType, String insurancecode, boolean isName) {
		String sql = "select t.name, t.localname from htyw_paods.t_parametervalue t where t.codetype=? and t.code=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, codeType);
			pstmt.setString(2, insurancecode);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				if(isName) {
					return rs.getString(1);
				} else {
					return rs.getString(2);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally{ 
			JDBCHelper.getInstance().closeResultSet(rs);
			JDBCHelper.getInstance().closePstmt(pstmt);
		}
		return "";
	} 
	
	/**
	 * 获取机构名称
	 * @param conn
	 * @param branchNo
	 * @return
	 */
	private String getBranchName(Connection conn, String branchNo) {
		String sql = "select t.name from htyw_paods.T_Branchold t where t.branchno=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, branchNo);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				return rs.getString(1);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally{ 
			JDBCHelper.getInstance().closeResultSet(rs);
			JDBCHelper.getInstance().closePstmt(pstmt);
		}
		return "";
	}
	
	
	/**
	 * 获取机构名称
	 * @param conn
	 * @param branchNo
	 * @return
	 */
	public void getConditionInfo(Connection conn, PolicyVO policyVO) {
		String sql = "select t.liabilitycode, t.coverage, t.coveragecurrency " +
				"from htyw_paods.t_liability t " +
				"where t.policyno=? and t.endorseno=? and t.nodeno=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<ConditionVO> cons = new ArrayList<ConditionVO>();
		policyVO.setCons(cons);
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, policyVO.getPolicyNo());
			pstmt.setString(2, policyVO.getEndorseNo());
			pstmt.setString(3, policyVO.getNodeNo());
			rs = pstmt.executeQuery();
			while(rs.next()) {
				ConditionVO con = new ConditionVO();
//				Map<String, String> con = new HashMap<String, String>();
				con.setConditionCode(rs.getString(1));
				String conName = getConditionInfoAdditional(conn, rs.getString(1));
				con.setConditionName(conName);
				con.setConditionInfo(conName);
				String amount = getParametervalueInfo(conn, 
						ConstConfigName.PV_CODETYPE_CURRENCY, rs.getString(3), false)+rs.getString(2);
				con.setConditionAmout(amount);
//				con.put(ConstConfigName.LIABILITY_CODE, rs.getString(1));
//				con.put(ConstConfigName.LIABILITY_NAME, getConditionInfoAdditional(conn, rs.getString(1)));
//				con.put(ConstConfigName.COVERAGE, );
				cons.add(con);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally{ 
			JDBCHelper.getInstance().closeResultSet(rs);
			JDBCHelper.getInstance().closePstmt(pstmt);
		}
	}
	
	/**
	 * 获取责任名称
	 * @param conn
	 * @param liabilitycode
	 * @return
	 */
	private String getConditionInfoAdditional(Connection conn, String liabilitycode) {
		String sql = "select t.liabilityname from  htyw_paods.T_LiabilityCode t where t.liabilitycode=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, liabilitycode);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				return rs.getString(1);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally{ 
			JDBCHelper.getInstance().closeResultSet(rs);
			JDBCHelper.getInstance().closePstmt(pstmt);
		}
		return "";
	}
	
	public static void main(String[] args) {
		CTMInfoBO info = new CTMInfoBO();
		Connection conn = null;
		try {
			conn = JDBCHelper.getInstance().getConnection();
			
//			String partyCode = info.getPartyInfo(conn, "01", "320683198211220623");
//			System.out.println("partyCode=" + partyCode);
			
//			Map<String, String> clientInfo =  info.getClientInfo(conn, partyCode);
//			for (String key : clientInfo.keySet()) {
//				System.out.println(key + "=" + clientInfo.get(key));
//			}
			
//			Map<String, String> policyInfo = info.getPolicyInfo(conn, "0120001110600002582", "0120001110600002582");
//			for (String key : policyInfo.keySet()) {
//				System.out.println(key + "=" + policyInfo.get(key));
//			}
			
//			List<Map<String, String>> conditions = info.getConditionInfo(conn, "6168345012010000046", "6168345012010000046");
//			for (Map<String, String> map : conditions) {
//				for (String key : map.keySet()) {
//					System.out.println(key + "=" + map.get(key));
//				}
//			}
			
		}finally{
			JDBCHelper.getInstance().closeConn(conn);
		}
	}
}
