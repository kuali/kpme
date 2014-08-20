package org.kuali.hr.time.department.lunch.rule;

import org.junit.Test;
import org.kuali.hr.time.dept.lunch.DeptLunchRule;
import org.kuali.hr.time.service.base.TkServiceLocator;
import org.kuali.hr.time.test.TkTestCase;
import org.kuali.hr.time.test.TkTestUtils;
import org.kuali.hr.time.timeblock.TimeBlock;
import org.kuali.hr.time.timesheet.TimesheetDocument;
import org.kuali.hr.time.util.TKUtils;
import org.kuali.hr.time.util.TkConstants;
import org.kuali.rice.kns.service.KNSServiceLocator;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Calendar;

public class DepartmentLunchRuleTest extends TkTestCase {
	@Test
	public void testDepartmentLunchRuleFetch() throws Exception{
		DeptLunchRule deptLunchRule = new DeptLunchRule();
		deptLunchRule.setActive(true);
		deptLunchRule.setDept("TEST");
		deptLunchRule.setWorkArea(1234L);
		deptLunchRule.setEffectiveDate(new Date(System.currentTimeMillis()));
		deptLunchRule.setJobNumber(0L);
		deptLunchRule.setPrincipalId("admin");
		deptLunchRule.setDeductionMins(new BigDecimal(30));
		deptLunchRule.setShiftHours(new BigDecimal(6));

		KNSServiceLocator.getBusinessObjectService().save(deptLunchRule);

		deptLunchRule = TkServiceLocator.getDepartmentLunchRuleService().getDepartmentLunchRule("TEST",
											1234L, "admin", 0L, new Date(System.currentTimeMillis()));
		assertTrue("dept lunch rule fetched ", deptLunchRule!=null);

	}

	/**
	 * Test if the minute deduction rule is applied correctly if there is a valid department lunch rule
	 */

	@Test
	public void testDepartmentLunchRule() throws Exception {
		// create a dept lunch rule
		DeptLunchRule deptLunchRule = new DeptLunchRule();
		deptLunchRule.setActive(true);
		deptLunchRule.setDept("TEST-DEPT");
		deptLunchRule.setWorkArea(1234L);
		Calendar cal = Calendar.getInstance();
		cal.set(2010, 1, 1);
		deptLunchRule.setEffectiveDate(new java.sql.Date(cal.getTime().getTime()));
		deptLunchRule.setJobNumber(1L);
		deptLunchRule.setPrincipalId("admin");
		deptLunchRule.setDeductionMins(new BigDecimal(30));
		deptLunchRule.setShiftHours(new BigDecimal(6));

		KNSServiceLocator.getBusinessObjectService().save(deptLunchRule);

		deptLunchRule = TkServiceLocator.getDepartmentLunchRuleService().getDepartmentLunchRule("TEST-DEPT",
											1234L, "admin", 1L, new Date(System.currentTimeMillis()));
		assertTrue("dept lunch rule fetched ", deptLunchRule!=null);

		TimesheetDocument doc = TkTestUtils.populateTimesheetDocument(TKUtils.getCurrentDate());
		TkServiceLocator.getTkRuleControllerService().applyRules(TkConstants.ACTIONS.ADD_TIME_BLOCK, doc.getTimeBlocks(), doc.getPayCalendarEntry(), doc);
		for(TimeBlock tb : doc.getTimeBlocks()) {
			if(tb.getHours().compareTo(deptLunchRule.getShiftHours()) == 1) {
				// this assumes the hours for the dummy timeblocks are always 10
				assertEquals(new BigDecimal(9.50).setScale(2), tb.getHours());
			}
		}

	}
}