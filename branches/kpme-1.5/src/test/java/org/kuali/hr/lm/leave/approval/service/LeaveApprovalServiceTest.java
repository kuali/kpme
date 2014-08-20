/**
 * Copyright 2004-2014 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.hr.lm.leave.approval.service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Test;
import org.kuali.hr.lm.leaveblock.LeaveBlock;
import org.kuali.hr.lm.workflow.LeaveCalendarDocumentHeader;
import org.kuali.hr.test.KPMETestCase;
import org.kuali.hr.time.approval.web.ApprovalLeaveSummaryRow;
import org.kuali.hr.time.calendar.CalendarEntries;
import org.kuali.hr.time.service.base.TkServiceLocator;

public class LeaveApprovalServiceTest extends KPMETestCase {
	
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yy");
	
	@Test
	public void testGetLeaveApprovalSummaryRows() {
		CalendarEntries ce = TkServiceLocator.getCalendarEntriesService().getCalendarEntries("55");
		List<Date> leaveSummaryDates = TkServiceLocator.getLeaveSummaryService().getLeaveSummaryDates(ce);
		List<String> testPrincipalIds = new ArrayList<String>();
		testPrincipalIds.add("admin");
		List<ApprovalLeaveSummaryRow> rows = TkServiceLocator.getLeaveApprovalService().getLeaveApprovalSummaryRows(testPrincipalIds, ce, leaveSummaryDates);
		Assert.assertTrue("Rows should not be empty. ", CollectionUtils.isNotEmpty(rows));
		
		ApprovalLeaveSummaryRow aRow = rows.get(0);
		Map<Date, Map<String, BigDecimal>> aMap = aRow.getEarnCodeLeaveHours();
		Assert.assertTrue("Leave Approval Summary Rows should have 14 items, not " + aMap.size(), aMap.size() == 14);
	}
	
	@Test
	public void testGetPrincipalDocumentHeader() {
		CalendarEntries ce = TkServiceLocator.getCalendarEntriesService().getCalendarEntries("55");
		List<String> testPrincipalIds = new ArrayList<String>();
		testPrincipalIds.add("admin");
		Map<String, LeaveCalendarDocumentHeader> lvCalHdr = TkServiceLocator.getLeaveApprovalService().getPrincipalDocumentHeader(testPrincipalIds, ce.getBeginPeriodDateTime(), ce.getEndPeriodDateTime());
		Assert.assertTrue("Header should not be empty. ", CollectionUtils.isNotEmpty(lvCalHdr.values()));

	}
	
	@Test
	public void testGetEarnCodeLeaveHours() throws Exception {
		CalendarEntries ce = TkServiceLocator.getCalendarEntriesService().getCalendarEntries("55");
		List<Date> leaveSummaryDates = TkServiceLocator.getLeaveSummaryService().getLeaveSummaryDates(ce);
		
		List<LeaveBlock> lbList = TkServiceLocator.getLeaveBlockService().getLeaveBlocks("admin", ce.getBeginPeriodDateTime(), ce.getEndPeriodDateTime());
		Assert.assertTrue("Leave Block list should not be empty. ", CollectionUtils.isNotEmpty(lbList));
		Map<Date, Map<String, BigDecimal>> aMap = TkServiceLocator.getLeaveApprovalService().getEarnCodeLeaveHours(lbList, leaveSummaryDates);
		
		Assert.assertTrue("Map should have 14 entries, not " + aMap.size(), aMap.size() == 14);
		Map<String, BigDecimal> dayMap = aMap.get(DATE_FORMAT.parse("03/05/2012"));
		Assert.assertTrue("Map on day 03/05 should have 1 entries, not " + dayMap.size(), dayMap.size() == 1);
		Assert.assertTrue("EC on day 03/05 should have 8 hours, not " + dayMap.get("EC6|P|AS"), dayMap.get("EC6|P|AS").equals(new BigDecimal(8)));
	}
	
	@Test
	public void testGetAccrualCategoryLeaveHours() throws Exception {
		CalendarEntries ce = TkServiceLocator.getCalendarEntriesService().getCalendarEntries("55");
		List<Date> leaveSummaryDates = TkServiceLocator.getLeaveSummaryService().getLeaveSummaryDates(ce);
		
		List<LeaveBlock> lbList = TkServiceLocator.getLeaveBlockService().getLeaveBlocks("admin", ce.getBeginPeriodDateTime(), ce.getEndPeriodDateTime());
		Assert.assertTrue("Leave Block list should not be empty. ", CollectionUtils.isNotEmpty(lbList));
		Map<Date, Map<String, BigDecimal>> aMap = TkServiceLocator.getLeaveApprovalService().getAccrualCategoryLeaveHours(lbList, leaveSummaryDates);
		
		Assert.assertTrue("Map should have 14 entries, not " + aMap.size(), aMap.size() == 14);
		Map<String, BigDecimal> dayMap = aMap.get(DATE_FORMAT.parse("03/05/2012"));
		Assert.assertTrue("Map on day 03/05 should have 1 entries, not " + dayMap.size(), dayMap.size() == 1);
		Assert.assertTrue("testAC on day 03/05 should have 8 hours, not " + dayMap.get("testAC"), dayMap.get("testAC").equals(new BigDecimal(8)));
	}
	
	@Test
	public void testGetLeavePrincipalIdsWithSearchCriteria() throws ParseException {
		List<String> workAreaList = new ArrayList<String>();
		String calendarGroup = "leaveCal";
		java.sql.Date beginDate = new java.sql.Date(DATE_FORMAT.parse("03/01/2012").getTime());
		java.sql.Date endDate = new java.sql.Date(DATE_FORMAT.parse("03/30/2012").getTime());
		
		List<String> idList = TkServiceLocator.getLeaveApprovalService()
			.getLeavePrincipalIdsWithSearchCriteria(workAreaList, calendarGroup, endDate, beginDate, endDate);		
		Assert.assertTrue("There should be 0 principal ids when searching with empty workarea list, not " + idList.size(), idList.isEmpty());
		
		workAreaList.add("1111");
		workAreaList.add("2222");
		idList = TkServiceLocator.getLeaveApprovalService()
			.getLeavePrincipalIdsWithSearchCriteria(workAreaList, calendarGroup, endDate, beginDate, endDate);		
		Assert.assertTrue("There should be 2 principal ids when searching with both workareas, not " + idList.size(), idList.size() == 2);
		// there's an principal id '1033' in setup that is not eligible for leave, so it should not be in the search results
		for(String anId : idList) {
			if(!(anId.equals("1011") || anId.equals("1022"))) {
				Assert.fail("PrincipalIds searched with both workareas should be either '1011' or '1022', not " + anId);
			}
		}
		
		workAreaList = new ArrayList<String>();
		workAreaList.add("1111");
		idList = TkServiceLocator.getLeaveApprovalService()
			.getLeavePrincipalIdsWithSearchCriteria(workAreaList, calendarGroup, endDate, beginDate, endDate);		
		Assert.assertTrue("There should be 1 principal ids for workArea '1111', not " + idList.size(), idList.size() == 1);
		Assert.assertTrue("Principal id for workArea '1111' should be principalA, not " + idList.get(0), idList.get(0).equals("1011"));
		
		workAreaList = new ArrayList<String>();
		workAreaList.add("2222");
		idList = TkServiceLocator.getLeaveApprovalService()
			.getLeavePrincipalIdsWithSearchCriteria(workAreaList, calendarGroup, endDate, beginDate, endDate);		
		Assert.assertTrue("There should be 1 principal ids for workArea '2222', not " + idList.size(), idList.size() == 1);
		Assert.assertTrue("Principal id for workArea '2222' should be principalB, not " + idList.get(0), idList.get(0).equals("1022"));
	}


}