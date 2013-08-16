package com.test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import pharos.eht.autoClaim.CTMInfoService;
import pharos.eht.autoClaim.ClaimInfoService;
import pharos.eht.autoClaim.vo.PolicyVO;

public class TestService {

	/**
	 * 测试保单接口
	 * 
	 * 522732197808050049
	 * 
	 * 9019146611613245144
	 *	E00415895
	 *
	 *	w55654159
	 * 
	 */
	@Test
	public void testGetCTMInfo() {
		CTMInfoService s = new CTMInfoService();
		String ret = s.getCTMInfo(null, "01","w55654159",  "autoClaim01", "autoClaim01");
		System.out.println(ret);
	}
	
	/**
	 * 测试理赔接口
	 */
	@Test
	public void testGetCLMinfo() {
		ClaimInfoService s = new ClaimInfoService();
		String ret = s.getClaimInfo("insuranceNo", "certificatetype", "8888", 
				"happendate", "autoClaim01", "autoClaim01");
		System.out.println(ret);
	}
	
	/**
	 * 测试数组中的次数
	 */
	@Test
	public void testTimesInArray() {
		List<String> arrs = new ArrayList<String>();
		arrs.add("a");
		arrs.add("a");
		arrs.add("b");
		arrs.add("c");
		
		System.out.println(arrs);
	}
	
	@Test
	public void testSet() {
		PolicyVO vo = new PolicyVO();
		vo.setPolicyNo("110");
		
		PolicyVO vo1 = new PolicyVO();
		vo1.setPolicyNo("110");
		
		PolicyVO vo2 = new PolicyVO();
		vo2.setPolicyNo("111");
		
		PolicyVO vo3 = new PolicyVO();
		vo3.setPolicyNo("113");
		Set<PolicyVO> s = new HashSet<PolicyVO>();
		s.add(vo);
		s.add(vo1);
		s.add(vo2);
		s.add(vo3);
		System.out.println(s.size());
		for (PolicyVO obj : s) {
			System.out.println(obj.getPolicyNo());
		}
	}
	
	@Test
	public void testToCase() {
		String xx = "W55654159";
		String yy = xx.toLowerCase();
		System.out.println(xx);
		
		System.out.println(yy);
	}
}
