package org.kuali.hr.time.workschedule.service;

import java.sql.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;
import org.kuali.hr.time.service.base.TkServiceLocator;
import org.kuali.hr.time.test.TkTestCase;
import org.kuali.hr.time.workschedule.WorkSchedule;

public class WorkScheduleServiceTest extends TkTestCase {

	@Test
	public void testGetWorkSchedules() throws Exception {
		WorkScheduleService wss = TkServiceLocator.getWorkScheduleService();
		String dept = null;
		String principalId = null;
		Long workScheduleId = null;
		Long workArea = null;
		Date asOfDate = new Date((new DateTime(2010, 1, 1, 12, 0, 0, 0, DateTimeZone.forID("EST"))).getMillis());
		List<WorkSchedule> schedules = null;
		
		// principal, department, workarea
		workScheduleId = 1L;
		dept = "TEST-DEPT";
		principalId = "admin";
		workArea = 1234L;
		schedules = wss.getWorkSchedules(principalId, dept, workArea, asOfDate);
		assertNotNull("Null return list", schedules);
		assertEquals("Wrong number of elements returned.", 1, schedules.size());
		assertEquals("Wrong ID returned", workScheduleId, (schedules.get(0)).getHrWorkScheduleId());
		assertEquals("Incorrect number of Work Schedule Entries", 2, (schedules.get(0)).getWorkScheduleEntries().size());
		
		// principal, department, -1
		workScheduleId = 2L;
		dept = "TEST-DEPT";
		principalId = "admin";
		workArea = -999L;
		schedules = wss.getWorkSchedules(principalId, dept, workArea, asOfDate);
		assertNotNull("Null return list", schedules);
		assertEquals("Wrong number of elements returned.", 1, schedules.size());
		assertEquals("Wrong ID returned", workScheduleId, (schedules.get(0)).getHrWorkScheduleId());
		assertEquals("Incorrect number of Work Schedule Entries", 2, (schedules.get(0)).getWorkScheduleEntries().size());
		
		// principal, *, workarea
		workScheduleId = 3L;
		dept = "NOTFOUND";
		principalId = "admin";
		workArea = 1234L;
		schedules = wss.getWorkSchedules(principalId, dept, workArea, asOfDate);
		assertNotNull("Null return list", schedules);
		assertEquals("Wrong number of elements returned.", 1, schedules.size());
		assertEquals("Wrong ID returned", workScheduleId, (schedules.get(0)).getHrWorkScheduleId());
		assertEquals("Incorrect number of Work Schedule Entries", 2, (schedules.get(0)).getWorkScheduleEntries().size());
		
		// principal, *, -1
		workScheduleId = 4L;
		dept = "NOTFOUND";
		principalId = "admin";
		workArea = -999L;
		schedules = wss.getWorkSchedules(principalId, dept, workArea, asOfDate);
		assertNotNull("Null return list", schedules);
		assertEquals("Wrong number of elements returned.", 1, schedules.size());
		assertEquals("Wrong ID returned", workScheduleId, (schedules.get(0)).getHrWorkScheduleId());
		assertEquals("Incorrect number of Work Schedule Entries", 2, (schedules.get(0)).getWorkScheduleEntries().size());
		
		// *, department, workarea
		workScheduleId = 5L;
		dept = "TEST-DEPT";
		principalId = "nobodyweknow";
		workArea = 1234L;
		schedules = wss.getWorkSchedules(principalId, dept, workArea, asOfDate);
		assertNotNull("Null return list", schedules);
		assertEquals("Wrong number of elements returned.", 1, schedules.size());
		assertEquals("Wrong ID returned", workScheduleId, (schedules.get(0)).getHrWorkScheduleId());
		assertEquals("Incorrect number of Work Schedule Entries", 2, (schedules.get(0)).getWorkScheduleEntries().size());
		
		// *, department, -1
		workScheduleId = 6L;
		dept = "TEST-DEPT";
		principalId = "nobodyweknow";
		workArea = -999L;
		schedules = wss.getWorkSchedules(principalId, dept, workArea, asOfDate);
		assertNotNull("Null return list", schedules);
		assertEquals("Wrong number of elements returned.", 1, schedules.size());
		assertEquals("Wrong ID returned", workScheduleId, (schedules.get(0)).getHrWorkScheduleId());
		assertEquals("Incorrect number of Work Schedule Entries", 2, (schedules.get(0)).getWorkScheduleEntries().size());
		
		// *, *, workarea
		workScheduleId = 7L;
		dept = "NOTFOUND";
		principalId = "nobodyweknow";
		workArea = 1234L;
		schedules = wss.getWorkSchedules(principalId, dept, workArea, asOfDate);
		assertNotNull("Null return list", schedules);
		assertEquals("Wrong number of elements returned.", 1, schedules.size());
		assertEquals("Wrong ID returned", workScheduleId, (schedules.get(0)).getHrWorkScheduleId());
		assertEquals("Incorrect number of Work Schedule Entries", 2, (schedules.get(0)).getWorkScheduleEntries().size());
		
		// *, *, -1
		workScheduleId = 8L;
		dept = "NOTFOUND";
		principalId = "nobodyweknow";
		workArea = -999L;
		schedules = wss.getWorkSchedules(principalId, dept, workArea, asOfDate);
		assertNotNull("Null return list", schedules);
		assertEquals("Wrong number of elements returned.", 1, schedules.size());
		assertEquals("Wrong ID returned", workScheduleId, (schedules.get(0)).getHrWorkScheduleId());
		assertEquals("Incorrect number of Work Schedule Entries", 2, (schedules.get(0)).getWorkScheduleEntries().size());
		
	}
}
