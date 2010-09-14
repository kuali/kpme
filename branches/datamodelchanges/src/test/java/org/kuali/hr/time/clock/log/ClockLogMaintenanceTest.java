package org.kuali.hr.time.clock.log;

import java.util.Calendar;
import java.util.Random;

import org.junit.Test;
import org.kuali.hr.time.clocklog.ClockLog;
import org.kuali.hr.time.department.Department;
import org.kuali.hr.time.task.Task;
import org.kuali.hr.time.test.HtmlUnitUtil;
import org.kuali.hr.time.test.TkTestCase;
import org.kuali.hr.time.test.TkTestConstants;
import org.kuali.hr.time.workarea.WorkArea;
import org.kuali.rice.kns.service.KNSServiceLocator;

import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class ClockLogMaintenanceTest extends TkTestCase{
	private static final String TEST_CODE_ONE="TST";
	private static final String TEST_CODE_TWO="_";
	private static final Long TEST_ID=20L;		
	private static final java.sql.Timestamp TEST_TIMESTAMP=new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis());
	private static Long TEST_CODE_INVALID_TASK_ID =0L;
	private static Long TEST_CODE_INVALID_WORK_AREA_ID =0L;
	private static Long clockLogId;	
	
	@Test
	public void testClockLogMaint() throws Exception {
		HtmlPage clockLogLookUp = HtmlUnitUtil.gotoPageAndLogin(TkTestConstants.Urls.CLOCK_LOG_MAINT_URL);
		clockLogLookUp = HtmlUnitUtil.clickInputContainingText(clockLogLookUp, "search");
		assertTrue("Page contains test ClockLog", clockLogLookUp.asText().contains(TEST_CODE_ONE));		
		HtmlPage maintPage = HtmlUnitUtil.clickAnchorContainingText(clockLogLookUp, "edit",clockLogId.toString());		
		assertTrue("Maintenance Page contains test ClockLog",maintPage.asText().contains(TEST_CODE_ONE));		
	}
	
	@Test
	public void testClockLogMaintForErrorMessages() throws Exception {
		HtmlPage clockLogLookUp = HtmlUnitUtil.gotoPageAndLogin(TkTestConstants.Urls.CLOCK_LOG_MAINT_URL);
		clockLogLookUp = HtmlUnitUtil.clickInputContainingText(clockLogLookUp, "search");
		assertTrue("Page contains test ClockLog", clockLogLookUp.asText().contains(TEST_CODE_ONE));		
		HtmlPage maintPage = HtmlUnitUtil.clickAnchorContainingText(clockLogLookUp, "edit",clockLogId.toString());		
		assertTrue("Maintenance Page contains test ClockLog",maintPage.asText().contains(TEST_CODE_ONE));
		
		HtmlInput inputForDescription = HtmlUnitUtil.getInputContainingText(
				maintPage, "* Document Description");
		inputForDescription.setValueAttribute("Test_Description");
		HtmlPage resultantPageAfterEdit = HtmlUnitUtil
				.clickInputContainingText(maintPage, "submit");
		System.out.println(resultantPageAfterEdit.asText());
		
		assertTrue("Maintenance Page contains test Workarea ",
				resultantPageAfterEdit.asText().contains(
						"The specified Workarea '"
								+ TEST_CODE_INVALID_WORK_AREA_ID
								+ "' does not exist."));
		
		assertTrue("Maintenance Page contains test Task ",
				resultantPageAfterEdit.asText().contains(
						"The specified Task '"
								+ TEST_CODE_INVALID_TASK_ID
								+ "' does not exist."));
		
		
	}

	@Override
	public void setUp() throws Exception {
		super.setUp();
		ClockLog clocklog = new ClockLog();
		clocklog.setClockAction(TEST_CODE_TWO);
		clocklog.setClockTimestamp(TEST_TIMESTAMP);
		clocklog.setClockTimestampTimezone(TEST_CODE_ONE);
		clocklog.setIpAddress(TEST_CODE_ONE);
		clocklog.setJobNumber(TEST_ID);
		clocklog.setPrincipalId(TEST_CODE_ONE);		
		clocklog.setTimestamp(TEST_TIMESTAMP);
		clocklog.setUserPrincipalId(TEST_CODE_ONE);
				
		Random randomObj = new Random();
		for (;;) {
			long taskIndex = randomObj.nextInt();
			Task taskObj = KNSServiceLocator.getBusinessObjectService()
					.findBySinglePrimaryKey(Task.class, taskIndex);
			if (taskObj == null) {
				TEST_CODE_INVALID_TASK_ID = new Long(taskIndex);
				break;
			}
		}
		clocklog.setTkTaskId(TEST_CODE_INVALID_TASK_ID);
		//search for the WorkArea which doesn't exist
		for (;;) {
			long workAreaIndex = randomObj.nextInt();
			WorkArea workAreaObj = KNSServiceLocator.getBusinessObjectService()
					.findBySinglePrimaryKey(WorkArea.class, workAreaIndex);
			if (workAreaObj == null) {
				TEST_CODE_INVALID_WORK_AREA_ID = new Long(workAreaIndex);
				break;
			}
		}
		clocklog.setTkWorkAreaId(TEST_CODE_INVALID_WORK_AREA_ID);		
		KNSServiceLocator.getBusinessObjectService().save(clocklog);		
		clockLogId=clocklog.getTkClockLogId();	
	}

	@Override
	public void tearDown() throws Exception {		
		ClockLog clockLogObj= KNSServiceLocator.getBusinessObjectService().findBySinglePrimaryKey(ClockLog.class, clockLogId);			
		KNSServiceLocator.getBusinessObjectService().delete(clockLogObj);				
		super.tearDown();
	}
}