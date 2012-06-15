package org.kuali.hr.lm.leavecode.service;

import java.math.BigDecimal;

import org.junit.Test;
import org.kuali.hr.lm.leavecode.LeaveCode;
import org.kuali.hr.time.service.base.TkServiceLocator;
import org.kuali.hr.time.test.TkTestCase;

public class LeaveCodeServiceTest extends TkTestCase {
	
	@Test
	public void testRoundHrsWithLeaveCode() {
		LeaveCode leaveCode = new LeaveCode();
		leaveCode.setLeaveCode("testLC");
		leaveCode.setEligibleForAccrual("Y");
		leaveCode.setAccrualCategory("testAC");
		leaveCode.setLeavePlan("testLP");
		leaveCode.setUnitOfTime("M");
		
		leaveCode.setFractionalTimeAllowed("99.999");
		// Traditional rounding option
		leaveCode.setRoundingOption("T");
		
		BigDecimal hours = new BigDecimal("12.3846");
		BigDecimal roundedHours = TkServiceLocator.getLeaveCodeService().roundHrsWithLeaveCode(hours, leaveCode);
		assertTrue("Rounded hours should be 12.385", roundedHours.equals(new BigDecimal("12.385")));
		hours = new BigDecimal("5.1234");
		roundedHours = TkServiceLocator.getLeaveCodeService().roundHrsWithLeaveCode(hours, leaveCode);
		assertTrue("Rounded hours should be 5.123", roundedHours.equals(new BigDecimal("5.123")));
		hours = new BigDecimal("3.0");
		roundedHours = TkServiceLocator.getLeaveCodeService().roundHrsWithLeaveCode(hours, leaveCode);
		assertTrue("Rounded hours should be 3.000", roundedHours.equals(new BigDecimal("3.000")));
		
		// Truncate rounding option
		leaveCode.setRoundingOption("R");
		
		hours = new BigDecimal("12.3846");
		roundedHours = TkServiceLocator.getLeaveCodeService().roundHrsWithLeaveCode(hours, leaveCode);
		assertTrue("Rounded hours should be 12.384", roundedHours.equals(new BigDecimal("12.384")));
		hours = new BigDecimal("5.1234");
		roundedHours = TkServiceLocator.getLeaveCodeService().roundHrsWithLeaveCode(hours, leaveCode);
		assertTrue("Rounded hours should be 5.123", roundedHours.equals(new BigDecimal("5.123")));
		hours = new BigDecimal("3.0");
		roundedHours = TkServiceLocator.getLeaveCodeService().roundHrsWithLeaveCode(hours, leaveCode);
		assertTrue("Rounded hours should be 3.000", roundedHours.equals(new BigDecimal("3.000")));
	}

}