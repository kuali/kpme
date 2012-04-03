package org.kuali.hr.lm.leaveblock.service;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kuali.hr.lm.leaveblock.LeaveStatusHistory;
import org.kuali.hr.time.service.base.TkServiceLocator;
import org.kuali.hr.time.test.TkTestCase;

public class LeaveStatusHistoryServiceImplTest extends TkTestCase{

	private LeaveStatusHistoryService leaveStatusHistoryService;
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		leaveStatusHistoryService = TkServiceLocator.getLeaveStatusHistoryService();
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

	@Test
	public void testGetLeaveStatusByLmLeaveBlockId() {
		LeaveStatusHistory leaveStatusHistory = leaveStatusHistoryService.getLeaveStatusHistoryByLmLeaveBlockIdAndRequestStatus("1000", null);
		assertNotNull("Leave Status Object not found", leaveStatusHistory);
	}

}