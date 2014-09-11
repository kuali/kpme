/**
 * Copyright 2004-2013 The Kuali Foundation
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
package org.kuali.hr.lm.balancetransfer.service;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.time.DateUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kuali.hr.lm.LMConstants;
import org.kuali.hr.lm.accrual.AccrualCategoryRule;
import org.kuali.hr.lm.balancetransfer.BalanceTransfer;
import org.kuali.hr.lm.employeeoverride.EmployeeOverride;
import org.kuali.hr.lm.leaveSummary.LeaveSummary;
import org.kuali.hr.lm.leaveSummary.LeaveSummaryRow;
import org.kuali.hr.lm.leaveblock.LeaveBlock;
import org.kuali.hr.lm.leavecalendar.LeaveCalendarDocument;
import org.kuali.hr.test.KPMETestCase;
import org.kuali.hr.time.calendar.CalendarEntries;
import org.kuali.hr.time.service.base.TkServiceLocator;
import org.kuali.hr.time.timesheet.TimesheetDocument;
import org.kuali.hr.time.util.TKUtils;
import org.kuali.rice.krad.util.ObjectUtils;

public class BalanceTransferServiceTest extends KPMETestCase {

	/**
	 * Leave Calendar Document Test data
	 */
	private final String USER_ID = "testUser1";
	
	private LeaveCalendarDocument janLCD;
	private CalendarEntries janEntry;
	private LeaveCalendarDocument decLCD;
	private CalendarEntries decEntry;
	
	private Date janStart;
	private Date janEnd;
	private Date decStart;
	private Date decEnd;
	
	private final String JAN_ID = "5001";
	private final String DEC_ID = "5000";
	
	/**
	 * Timesheet Document Test Data;
	 */
	
	private final String TS_USER_ID = "testUser2";	
	
	private TimesheetDocument endJanTSD;
	private CalendarEntries endJanTSDEntry;
	private TimesheetDocument midJanTSD;
	private CalendarEntries midJanTSDEntry;
	private TimesheetDocument endDecTSD;
	private CalendarEntries endDecTSDEntry;
	private TimesheetDocument midDecTSD;
	private CalendarEntries midDecTSDEntry;
	
	private Date midJanStart;
	private Date midJanEnd;
	private Date endDecStart;
	private Date endDecEnd;
	private Date midDecStart;
	private Date midDecEnd;
	
	private final String TSD_MID_DEC_PERIOD_ID = "5000";
	private final String TSD_END_DEC_PERIOD_ID = "5001";
	private final String TSD_MID_JAN_PERIOD_ID = "5002";
	private final String TSD_END_JAN_PERIOD_ID = "5003";

	/**
	 *  Common data
	 */
	
	private final String OD_XFER = "5000";
	private final String YE_XFER = "5001";
	private final String LA_XFER = "5002";
	private final String OD_XFER_MAC = "5003";
	private final String YE_XFER_MAC = "5004";
	private final String LA_XFER_MAC = "5005";
	private final String OD_LOSE = "5006";
	private final String YE_LOSE = "5007";
	private final String LA_LOSE = "5008";
	private final String OD_LOSE_MAC = "5009";
	private final String YE_LOSE_MAC = "5010";
	private final String LA_LOSE_MAC = "5011";
	private final String YE_XFER_EO = "5012";
	private final java.sql.Date LM_FROM = TKUtils.formatDateString("11/01/2012");
	private final java.sql.Date LM_TO = TKUtils.formatDateString("02/01/2013");
	private final java.sql.Date TK_FROM = TKUtils.formatDateString("11/01/2011");
	private final java.sql.Date TK_TO = TKUtils.formatDateString("02/01/2012");
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		TkServiceLocator.getAccrualService().runAccrual(USER_ID,LM_FROM,LM_TO,true,USER_ID);
		janLCD = TkServiceLocator.getLeaveCalendarService().getLeaveCalendarDocument(JAN_ID);
		janEntry = janLCD.getCalendarEntry();
		janStart = janEntry.getBeginPeriodDate();
		janEnd = janEntry.getEndPeriodDate();
		decLCD = TkServiceLocator.getLeaveCalendarService().getLeaveCalendarDocument(DEC_ID);
		decEntry = decLCD.getCalendarEntry();
		decStart = decEntry.getBeginPeriodDate();
		decEnd = decEntry.getEndPeriodDate();
	}
	
	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}
	
	/*****************************
	 * Use-case specific testing *
	 ****************************/
	
	//
	// ACTION_AT_MAX_BALANCE = TRANSFER
	//
	
	@Test
	public void testInitializeTransferNullAccrualRule() throws Exception {
		BalanceTransfer bt = new BalanceTransfer();

		java.sql.Date effectiveDate = new java.sql.Date(DateUtils.addDays(decStart,3).getTime());
		bt = TkServiceLocator.getBalanceTransferService().initializeTransfer(USER_ID, null, BigDecimal.ZERO, effectiveDate);
		assertNull(bt);
	}
	
	@Test
	public void testInitializeTransferNullLeaveSummary() throws Exception {
		BalanceTransfer bt = new BalanceTransfer();

		bt = TkServiceLocator.getBalanceTransferService().initializeTransfer(USER_ID, OD_XFER, null, TKUtils.getCurrentDate());
		assertNull(bt);
	}
	
	@Test
	public void testInitializeTransferNullAccrualRuleNullLeaveSummary() {
		BalanceTransfer bt = new BalanceTransfer();
		bt = TkServiceLocator.getBalanceTransferService().initializeTransfer(USER_ID, null, null, TKUtils.getCurrentDate());
		assertNull(bt);
	}
	
	@Test
	public void testInitializeTransferOnDemand() throws Exception {
		BalanceTransfer bt = new BalanceTransfer();
		LeaveSummary summary = TkServiceLocator.getLeaveSummaryService().getLeaveSummary(USER_ID, decEntry);
		LeaveSummaryRow aRow = summary.getLeaveSummaryRowForAccrualCategory(OD_XFER);
		java.sql.Date effectiveDate = new java.sql.Date(DateUtils.addDays(decStart,3).getTime());
		bt = TkServiceLocator.getBalanceTransferService().initializeTransfer(USER_ID, OD_XFER, aRow.getAccruedBalance(), effectiveDate);
		assertEquals("transferOnDemand transfer amount", (new BigDecimal(1)).longValue(), bt.getTransferAmount().longValue());
		assertEquals("transferOnDemand forfeited amount",(new BigDecimal(0)).longValue(), bt.getForfeitedAmount().longValue());
		assertEquals("transferOnDemand amount transferred", (new BigDecimal(0.5)).longValue(), bt.getAmountTransferred().longValue());
	}
	
	@Test
	public void testInitializeTransferOnDemandWithForfeiture() throws Exception {
		BalanceTransfer bt = new BalanceTransfer();
		LeaveSummary summary = TkServiceLocator.getLeaveSummaryService().getLeaveSummary(USER_ID, janEntry);
		LeaveSummaryRow aRow = summary.getLeaveSummaryRowForAccrualCategory(OD_XFER);
		java.sql.Date effectiveDate = new java.sql.Date(DateUtils.addDays(janStart,3).getTime());
		bt = TkServiceLocator.getBalanceTransferService().initializeTransfer(USER_ID, OD_XFER, aRow.getAccruedBalance(), effectiveDate);
		assertEquals("transferOnDemand transfer amount", (new BigDecimal(10)).longValue(), bt.getTransferAmount().longValue());
		assertEquals("transferOnDemand forfeited amount", (new BigDecimal(7)).longValue(), bt.getForfeitedAmount().longValue());
		assertEquals("transferOnDemand amount transferred", (new BigDecimal(5)).longValue(), bt.getAmountTransferred().longValue());
	}
	
	@Test
	public void testInitializeTransferOnYearEnd() throws Exception {
		BalanceTransfer bt = new BalanceTransfer();
		TkServiceLocator.getLeaveBlockService().deleteLeaveBlocksForDocumentId(DEC_ID);
		LeaveSummary summary = TkServiceLocator.getLeaveSummaryService().getLeaveSummary(USER_ID, janEntry);
		LeaveSummaryRow aRow = summary.getLeaveSummaryRowForAccrualCategory(YE_XFER);
		java.sql.Date effectiveDate = new java.sql.Date(DateUtils.addDays(janStart,3).getTime());
		bt = TkServiceLocator.getBalanceTransferService().initializeTransfer(USER_ID, YE_XFER, aRow.getAccruedBalance(), effectiveDate);
		assertEquals("transferOnDemand transfer amount", (new BigDecimal(1)).longValue(), bt.getTransferAmount().longValue());
		assertEquals("transferOnDemand forfeited amount",(new BigDecimal(0)).longValue(), bt.getForfeitedAmount().longValue());
		assertEquals("transferOnDemand amount transferred", (new BigDecimal(0.5)).longValue(), bt.getAmountTransferred().longValue());
	}
	
	@Test
	public void testInitializeTransferOnYearEndWithForfeiture() throws Exception {
		BalanceTransfer bt = new BalanceTransfer();
		LeaveSummary summary = TkServiceLocator.getLeaveSummaryService().getLeaveSummary(USER_ID, janEntry);
		LeaveSummaryRow aRow = summary.getLeaveSummaryRowForAccrualCategory(YE_XFER);
		java.sql.Date effectiveDate = new java.sql.Date(DateUtils.addDays(janStart,3).getTime());
		bt = TkServiceLocator.getBalanceTransferService().initializeTransfer(USER_ID, YE_XFER, aRow.getAccruedBalance(), effectiveDate);
		assertEquals("transferOnDemand transfer amount", (new BigDecimal(10)).longValue(), bt.getTransferAmount().longValue());
		assertEquals("transferOnDemand forfeited amount", (new BigDecimal(7)).longValue(), bt.getForfeitedAmount().longValue());
		assertEquals("transferOnDemand amount transferred", (new BigDecimal(5)).longValue(), bt.getAmountTransferred().longValue());
	}
	
	@Test
	public void testInitializeTransferOnLeaveApprove() throws Exception {
		BalanceTransfer bt = new BalanceTransfer();
		LeaveSummary summary = TkServiceLocator.getLeaveSummaryService().getLeaveSummary(USER_ID, decEntry);
		LeaveSummaryRow aRow = summary.getLeaveSummaryRowForAccrualCategory(LA_XFER);
		java.sql.Date effectiveDate = new java.sql.Date(DateUtils.addDays(decStart,3).getTime());
		bt = TkServiceLocator.getBalanceTransferService().initializeTransfer(USER_ID, LA_XFER, aRow.getAccruedBalance(), effectiveDate);
		assertEquals("transferOnDemand transfer amount", (new BigDecimal(1)).longValue(), bt.getTransferAmount().longValue());
		assertEquals("transferOnDemand forfeited amount",(new BigDecimal(0)).longValue(), bt.getForfeitedAmount().longValue());
		assertEquals("transferOnDemand amount transferred", (new BigDecimal(0.5)).longValue(), bt.getAmountTransferred().longValue());
	}
	
	@Test
	public void testInitializeTransferOnLeaveApproveWithForfeiture() throws Exception {
		BalanceTransfer bt = new BalanceTransfer();
		LeaveSummary summary = TkServiceLocator.getLeaveSummaryService().getLeaveSummary(USER_ID, janEntry);
		LeaveSummaryRow aRow = summary.getLeaveSummaryRowForAccrualCategory(LA_XFER);
		java.sql.Date effectiveDate = new java.sql.Date(DateUtils.addDays(janStart,3).getTime());
		bt = TkServiceLocator.getBalanceTransferService().initializeTransfer(USER_ID, LA_XFER, aRow.getAccruedBalance(), effectiveDate);
		assertEquals("transferOnDemand transfer amount", (new BigDecimal(10)).longValue(), bt.getTransferAmount().longValue());
		assertEquals("transferOnDemand forfeited amount", (new BigDecimal(7)).longValue(), bt.getForfeitedAmount().longValue());
		assertEquals("transferOnDemand amount transferred", (new BigDecimal(5)).longValue(), bt.getAmountTransferred().longValue());
	}
	
	@Test
	public void testInitializeTransferOnDemandMaxCarryOver() throws Exception {
		//N/A - Max Carry Over on Year End transfers.
		BalanceTransfer bt = new BalanceTransfer();
		LeaveSummary summary = TkServiceLocator.getLeaveSummaryService().getLeaveSummary(USER_ID, decEntry);
		LeaveSummaryRow aRow = summary.getLeaveSummaryRowForAccrualCategory(OD_XFER_MAC);
		java.sql.Date effectiveDate = new java.sql.Date(DateUtils.addDays(decStart,3).getTime());
		bt = TkServiceLocator.getBalanceTransferService().initializeTransfer(USER_ID, OD_XFER_MAC, aRow.getAccruedBalance(), effectiveDate);
		assertEquals("transferOnDemand transfer amount", (new BigDecimal(1)).longValue(), bt.getTransferAmount().longValue());
		assertEquals("transferOnDemand forfeited amount",(new BigDecimal(0)).longValue(), bt.getForfeitedAmount().longValue());
		assertEquals("transferOnDemand amount transferred", (new BigDecimal(0.5)).longValue(), bt.getAmountTransferred().longValue());
	}
	
	@Test
	public void testInitializeTransferOnYearEndMaxCarryOver() throws Exception {
		/**
		 * decEntry is not the last calendar entry in the leave plan. Want to check amounts for this action & action frequency
		 * without exceeding the transfer limit.
		 * 
		 * max transfer amount = 10
		 * leave balance = 16
		 * max balance = 15
		 * max carry over = 10
		 * 
		 * all excess should be transferrable. 1 unit of time for excess over max balance, 5 units of time for
		 * excess over max carry over.
		 * 
		 */
		BalanceTransfer bt = new BalanceTransfer();
		LeaveSummary summary = TkServiceLocator.getLeaveSummaryService().getLeaveSummary(USER_ID, decEntry);
		LeaveSummaryRow aRow = summary.getLeaveSummaryRowForAccrualCategory(YE_XFER_MAC);
		java.sql.Date effectiveDate = new java.sql.Date(DateUtils.addDays(decStart,3).getTime());
		bt = TkServiceLocator.getBalanceTransferService().initializeTransfer(USER_ID, YE_XFER_MAC, aRow.getAccruedBalance(), effectiveDate);
		assertEquals("transferOnDemand transfer amount", (new BigDecimal(6)).longValue(), bt.getTransferAmount().longValue());
		assertEquals("transferOnDemand forfeited amount", (new BigDecimal(0)).longValue(), bt.getForfeitedAmount().longValue());
		assertEquals("transferOnDemand amount transferred", (new BigDecimal(3)).longValue(), bt.getAmountTransferred().longValue());
	}
	
/*	@Test
	public void testInitializeTransferUnderMaxBalanceOnYearEndMaxCarryOver() throws Exception {
		//Create a leave block that will bring the available balance for january down to 14.
		//this balance would be under the max available balance (15), but over the max annual carry over amount.
		//i.o.w., this transfer would not due to max balance limit, but max annual carry over.
		//could also simply change the accrual amount.
		LeaveBlock usage = new LeaveBlock();
		usage.setAccrualCategory(YE_XFER_MAC);
		usage.setLeaveDate(new java.sql.Date(DateUtils.addDays(janStart,5).getTime()));
		usage.setLeaveAmount(new BigDecimal(-18));
		usage.setPrincipalId(USER_ID);
		usage.setAccrualGenerated(false);
		usage.setRequestStatus(LMConstants.REQUEST_STATUS.APPROVED);
		usage.setDocumentId(janLCD.getDocumentId());
		usage.setLmLeaveBlockId("99999");
		usage.setEarnCode("EC5");
		usage.setBlockId(0L);
		usage.setLeaveBlockType(LMConstants.LEAVE_BLOCK_TYPE.LEAVE_CALENDAR);
		List<LeaveBlock> leaveBlocks = new ArrayList<LeaveBlock>();
		leaveBlocks.add(usage);
		TkServiceLocator.getLeaveBlockService().saveLeaveBlocks(leaveBlocks);
		
		BalanceTransfer bt = new BalanceTransfer();
		janLCD = TkServiceLocator.getLeaveCalendarService().getLeaveCalendarDocument(JAN_ID);
		LeaveSummary summary = TkServiceLocator.getLeaveSummaryService().getLeaveSummary(USER_ID, janLCD.getCalendarEntry());
		java.sql.Date effectiveDate = new java.sql.Date(DateUtils.addDays(janStart,3).getTime());
		bt = TkServiceLocator.getBalanceTransferService().initializeTransfer(USER_ID, YE_XFER_MAC, aRow.getAccruedBalance(), effectiveDate);
		assertEquals("transferOnDemand transfer amount", (new BigDecimal(4)).longValue(), bt.getTransferAmount().longValue());
		assertEquals("transferOnDemand forfeited amount", (new BigDecimal(0)).longValue(), bt.getForfeitedAmount().longValue());
		assertEquals("transferOnDemand amount transferred", (new BigDecimal(2)).longValue(), bt.getAmountTransferred().longValue());
	}*/
	
	@Test
	public void testInitializeTransferOnYearEndMaxCarryOverWithForfeiture() throws Exception {
		//max bal limit reached and max annual carry over triggererd.
		BalanceTransfer bt = new BalanceTransfer();
		LeaveSummary summary = TkServiceLocator.getLeaveSummaryService().getLeaveSummary(USER_ID, janEntry);
		LeaveSummaryRow aRow = summary.getLeaveSummaryRowForAccrualCategory(YE_XFER_MAC);
		java.sql.Date effectiveDate = new java.sql.Date(DateUtils.addDays(janStart,3).getTime());
		bt = TkServiceLocator.getBalanceTransferService().initializeTransfer(USER_ID, YE_XFER_MAC, aRow.getAccruedBalance(), effectiveDate);
		assertEquals("transferOnDemand transfer amount", (new BigDecimal(10)).longValue(), bt.getTransferAmount().longValue());
		assertEquals("transferOnDemand forfeited amount", (new BigDecimal(12)).longValue(), bt.getForfeitedAmount().longValue());
		assertEquals("transferOnDemand amount transferred", (new BigDecimal(5)).longValue(), bt.getAmountTransferred().longValue());
	}
	
	@Test
	public void testInitializeTransferOnLeaveApproveMaxCarryOver() throws Exception {
		BalanceTransfer bt = new BalanceTransfer();
		LeaveSummary summary = TkServiceLocator.getLeaveSummaryService().getLeaveSummary(USER_ID, decEntry);
		LeaveSummaryRow aRow = summary.getLeaveSummaryRowForAccrualCategory(LA_XFER_MAC);
		java.sql.Date effectiveDate = new java.sql.Date(DateUtils.addDays(decStart,3).getTime());
		bt = TkServiceLocator.getBalanceTransferService().initializeTransfer(USER_ID, LA_XFER_MAC, aRow.getAccruedBalance(), effectiveDate);
		assertEquals("transferOnDemand transfer amount", (new BigDecimal(1)).longValue(), bt.getTransferAmount().longValue());
		assertEquals("transferOnDemand forfeited amount",(new BigDecimal(0)).longValue(), bt.getForfeitedAmount().longValue());
		assertEquals("transferOnDemand amount transferred", (new BigDecimal(0.5)).longValue(), bt.getAmountTransferred().longValue());
	}
	
	//
	// ACTION_AT_MAX_BALANCE = LOSE
	//
	@Test
	public void testInitializeLoseOnDemand() throws Exception {
		BalanceTransfer bt = new BalanceTransfer();
		LeaveSummary summary = TkServiceLocator.getLeaveSummaryService().getLeaveSummary(USER_ID, decEntry);
		LeaveSummaryRow aRow = summary.getLeaveSummaryRowForAccrualCategory(OD_LOSE);
		java.sql.Date effectiveDate = new java.sql.Date(DateUtils.addDays(decStart,3).getTime());
		bt = TkServiceLocator.getBalanceTransferService().initializeTransfer(USER_ID, OD_LOSE, aRow.getAccruedBalance(), effectiveDate);
		assertEquals("transferOnDemand transfer amount", (new BigDecimal(0)).longValue(), bt.getTransferAmount().longValue());
		assertEquals("transferOnDemand forfeited amount",(new BigDecimal(1)).longValue(), bt.getForfeitedAmount().longValue());
		assertEquals("transferOnDemand amount transferred", (new BigDecimal(0)).longValue(), bt.getAmountTransferred().longValue());
	}
	
	@Test
	public void testInitializeLoseOnYearEnd() throws Exception {
		BalanceTransfer bt = new BalanceTransfer();
		LeaveSummary summary = TkServiceLocator.getLeaveSummaryService().getLeaveSummary(USER_ID, janEntry);
		LeaveSummaryRow aRow = summary.getLeaveSummaryRowForAccrualCategory(YE_LOSE);
		java.sql.Date effectiveDate = new java.sql.Date(DateUtils.addDays(janStart,3).getTime());
		bt = TkServiceLocator.getBalanceTransferService().initializeTransfer(USER_ID, YE_LOSE, aRow.getAccruedBalance(), effectiveDate);
		assertEquals("transferOnDemand transfer amount", (new BigDecimal(0)).longValue(), bt.getTransferAmount().longValue());
		assertEquals("transferOnDemand forfeited amount",(new BigDecimal(17)).longValue(), bt.getForfeitedAmount().longValue());
		assertEquals("transferOnDemand amount transferred", (new BigDecimal(0)).longValue(), bt.getAmountTransferred().longValue());
	}
	
	@Test
	public void testInitializeLoseOnLeaveApprove() throws Exception {
		BalanceTransfer bt = new BalanceTransfer();
		LeaveSummary summary = TkServiceLocator.getLeaveSummaryService().getLeaveSummary(USER_ID, janEntry);
		LeaveSummaryRow aRow = summary.getLeaveSummaryRowForAccrualCategory(LA_LOSE);
		java.sql.Date effectiveDate = new java.sql.Date(DateUtils.addDays(janStart,3).getTime());
		bt = TkServiceLocator.getBalanceTransferService().initializeTransfer(USER_ID, LA_LOSE, aRow.getAccruedBalance(), effectiveDate);
		assertEquals("transferOnDemand transfer amount", (new BigDecimal(0)).longValue(), bt.getTransferAmount().longValue());
		assertEquals("transferOnDemand forfeited amount",(new BigDecimal(17)).longValue(), bt.getForfeitedAmount().longValue());
		assertEquals("transferOnDemand amount transferred", (new BigDecimal(0)).longValue(), bt.getAmountTransferred().longValue());
	}
	
	@Test
	public void testInitializeLoseOnDemandMaxCarryOver() throws Exception {
		BalanceTransfer bt = new BalanceTransfer();
		LeaveSummary summary = TkServiceLocator.getLeaveSummaryService().getLeaveSummary(USER_ID, janEntry);
		LeaveSummaryRow aRow = summary.getLeaveSummaryRowForAccrualCategory(OD_LOSE_MAC);
		java.sql.Date effectiveDate = new java.sql.Date(DateUtils.addDays(janStart,3).getTime());
		bt = TkServiceLocator.getBalanceTransferService().initializeTransfer(USER_ID, OD_LOSE_MAC, aRow.getAccruedBalance(), effectiveDate);
		assertEquals("transferOnDemand transfer amount", (new BigDecimal(0)).longValue(), bt.getTransferAmount().longValue());
		assertEquals("transferOnDemand forfeited amount",(new BigDecimal(17)).longValue(), bt.getForfeitedAmount().longValue());
		assertEquals("transferOnDemand amount transferred", (new BigDecimal(0)).longValue(), bt.getAmountTransferred().longValue());
	}
	
	@Test
	public void testInitializeLoseOnYearEndMaxCarryOver() throws Exception {
		BalanceTransfer bt = new BalanceTransfer();
		LeaveSummary summary = TkServiceLocator.getLeaveSummaryService().getLeaveSummary(USER_ID, janEntry);
		LeaveSummaryRow aRow = summary.getLeaveSummaryRowForAccrualCategory(YE_LOSE_MAC);
		java.sql.Date effectiveDate = new java.sql.Date(DateUtils.addDays(janStart,3).getTime());
		bt = TkServiceLocator.getBalanceTransferService().initializeTransfer(USER_ID, YE_LOSE_MAC, aRow.getAccruedBalance(), effectiveDate);
		assertEquals("transferOnDemand transfer amount", (new BigDecimal(0)).longValue(), bt.getTransferAmount().longValue());
		assertEquals("transferOnDemand forfeited amount",(new BigDecimal(22)).longValue(), bt.getForfeitedAmount().longValue());
		assertEquals("transferOnDemand amount transferred", (new BigDecimal(0)).longValue(), bt.getAmountTransferred().longValue());
	}
	
	@Test
	public void testInitializeLoseOnLeaveApproveMaxCarryOver() throws Exception {
		BalanceTransfer bt = new BalanceTransfer();
		LeaveSummary summary = TkServiceLocator.getLeaveSummaryService().getLeaveSummary(USER_ID, janEntry);
		LeaveSummaryRow aRow = summary.getLeaveSummaryRowForAccrualCategory(LA_LOSE_MAC);
		java.sql.Date effectiveDate = new java.sql.Date(DateUtils.addDays(janStart,3).getTime());
		bt = TkServiceLocator.getBalanceTransferService().initializeTransfer(USER_ID, LA_LOSE_MAC, aRow.getAccruedBalance(), effectiveDate);
		assertEquals("transferOnDemand transfer amount", (new BigDecimal(0)).longValue(), bt.getTransferAmount().longValue());
		assertEquals("transferOnDemand forfeited amount",(new BigDecimal(17)).longValue(), bt.getForfeitedAmount().longValue());
		assertEquals("transferOnDemand amount transferred", (new BigDecimal(0)).longValue(), bt.getAmountTransferred().longValue());
	}
	
	@Test
	public void testInitializeTransferWithOverrides() throws Exception {
		BalanceTransfer bt = new BalanceTransfer();
		LeaveSummary summary = TkServiceLocator.getLeaveSummaryService().getLeaveSummary(USER_ID, janEntry);
		LeaveSummaryRow aRow = summary.getLeaveSummaryRowForAccrualCategory(YE_XFER_EO);
		java.sql.Date effectiveDate = new java.sql.Date(DateUtils.addDays(janStart,3).getTime());
		bt = TkServiceLocator.getBalanceTransferService().initializeTransfer(USER_ID, YE_XFER_EO, aRow.getAccruedBalance(), effectiveDate);
		assertEquals("transferOnDemand transfer amount", (new BigDecimal(7)).longValue(), bt.getTransferAmount().longValue());
		assertEquals("transferOnDemand forfeited amount",(new BigDecimal(20)).longValue(), bt.getForfeitedAmount().longValue());
		// max balance transfer conversion factor is undefined for YE_XFER_EO
		assertEquals("transferOnDemand amount transferred", (new BigDecimal(7)).longValue(), bt.getAmountTransferred().longValue());
	}
	/**
	 * End Use-case testing
	 */
	
	@Test
	public void testTransferNullBalanceTransfer() {
		BalanceTransfer balanceTransfer = null;
		try {
			balanceTransfer = TkServiceLocator.getBalanceTransferService().transfer(balanceTransfer);
		} catch (RuntimeException re) {
			assertTrue(re.getMessage().contains("did not supply a valid BalanceTransfer object"));
		}
	}
	
	@Test
	public void testTransferWithZeroTransferAmount() throws Exception {
		BalanceTransfer bt = new BalanceTransfer();
		LeaveSummary summary = TkServiceLocator.getLeaveSummaryService().getLeaveSummary(USER_ID, janEntry);
		LeaveSummaryRow aRow = summary.getLeaveSummaryRowForAccrualCategory(YE_LOSE);
		java.sql.Date effectiveDate = new java.sql.Date(DateUtils.addDays(janStart,3).getTime());
		bt = TkServiceLocator.getBalanceTransferService().initializeTransfer(USER_ID, YE_LOSE, aRow.getAccruedBalance(), effectiveDate);
		bt = TkServiceLocator.getBalanceTransferService().transfer(bt);
		LeaveBlock forfeitedLeaveBlock = TkServiceLocator.getLeaveBlockService().getLeaveBlock(bt.getForfeitedLeaveBlockId());
		LeaveBlock accruedLeaveBlock = TkServiceLocator.getLeaveBlockService().getLeaveBlock(bt.getAccruedLeaveBlockId());
		LeaveBlock debitedLeaveBlock = TkServiceLocator.getLeaveBlockService().getLeaveBlock(bt.getDebitedLeaveBlockId());
		assertEquals("forfeited leave block leave amount incorrect", (new BigDecimal(-17)).longValue(), forfeitedLeaveBlock.getLeaveAmount().longValue());
		assertTrue("accrued leave block should not exist",ObjectUtils.isNull(accruedLeaveBlock));
		assertTrue("debited leave block should not exist",ObjectUtils.isNull(debitedLeaveBlock));
	}
	
	@Test
	public void testTransferWithNoAmountTransferred() throws Exception {
		BalanceTransfer bt = new BalanceTransfer();
		LeaveSummary summary = TkServiceLocator.getLeaveSummaryService().getLeaveSummary(USER_ID, janEntry);
		LeaveSummaryRow aRow = summary.getLeaveSummaryRowForAccrualCategory(YE_LOSE);
		java.sql.Date effectiveDate = new java.sql.Date(DateUtils.addDays(janStart,3).getTime());
		bt = TkServiceLocator.getBalanceTransferService().initializeTransfer(USER_ID, YE_LOSE, aRow.getAccruedBalance(), effectiveDate);
		bt.setAmountTransferred(null);
		bt = TkServiceLocator.getBalanceTransferService().transfer(bt);
		LeaveBlock forfeitedLeaveBlock = TkServiceLocator.getLeaveBlockService().getLeaveBlock(bt.getForfeitedLeaveBlockId());
		LeaveBlock accruedLeaveBlock = TkServiceLocator.getLeaveBlockService().getLeaveBlock(bt.getAccruedLeaveBlockId());
		LeaveBlock debitedLeaveBlock = TkServiceLocator.getLeaveBlockService().getLeaveBlock(bt.getDebitedLeaveBlockId());
		assertEquals("forfeited leave block leave amount incorrect",(new BigDecimal(-17)).longValue(), forfeitedLeaveBlock.getLeaveAmount().longValue());
		assertTrue("accrued leave block should not exist",ObjectUtils.isNull(accruedLeaveBlock));
		assertTrue("debited leave block should not exist",ObjectUtils.isNull(debitedLeaveBlock));	
	}
	
	@Test
	public void testTransferWithZeroForfeiture() throws Exception {
		BalanceTransfer bt = new BalanceTransfer();
		LeaveSummary summary = TkServiceLocator.getLeaveSummaryService().getLeaveSummary(USER_ID, decEntry);
		LeaveSummaryRow aRow = summary.getLeaveSummaryRowForAccrualCategory(OD_XFER);
		java.sql.Date effectiveDate = new java.sql.Date(DateUtils.addDays(decStart,3).getTime());
		bt = TkServiceLocator.getBalanceTransferService().initializeTransfer(USER_ID, OD_XFER, aRow.getAccruedBalance(), effectiveDate);
		bt = TkServiceLocator.getBalanceTransferService().transfer(bt);
		LeaveBlock forfeitedLeaveBlock = TkServiceLocator.getLeaveBlockService().getLeaveBlock(bt.getForfeitedLeaveBlockId());
		LeaveBlock accruedLeaveBlock = TkServiceLocator.getLeaveBlockService().getLeaveBlock(bt.getAccruedLeaveBlockId());
		LeaveBlock debitedLeaveBlock = TkServiceLocator.getLeaveBlockService().getLeaveBlock(bt.getDebitedLeaveBlockId());
		assertEquals("accrued leave block leave amount incorrect", (new BigDecimal(0.5)).longValue(), accruedLeaveBlock.getLeaveAmount().longValue());
		assertTrue("forfeited leave block should not exist",ObjectUtils.isNull(forfeitedLeaveBlock));
		assertEquals("transfered leave block leave amount incorrect", (new BigDecimal(-1)).longValue(), debitedLeaveBlock.getLeaveAmount().longValue());
	}
	
	@Test
	public void testTransferWithThreeLeaveBlocks() throws Exception {
		BalanceTransfer bt = new BalanceTransfer();
		LeaveSummary summary = TkServiceLocator.getLeaveSummaryService().getLeaveSummary(USER_ID, janEntry);
		LeaveSummaryRow aRow = summary.getLeaveSummaryRowForAccrualCategory(YE_XFER);
		java.sql.Date effectiveDate = new java.sql.Date(DateUtils.addDays(janStart,3).getTime());
		bt = TkServiceLocator.getBalanceTransferService().initializeTransfer(USER_ID, YE_XFER, aRow.getAccruedBalance(), effectiveDate);
		bt = TkServiceLocator.getBalanceTransferService().transfer(bt);
		LeaveBlock forfeitedLeaveBlock = TkServiceLocator.getLeaveBlockService().getLeaveBlock(bt.getForfeitedLeaveBlockId());
		LeaveBlock accruedLeaveBlock = TkServiceLocator.getLeaveBlockService().getLeaveBlock(bt.getAccruedLeaveBlockId());
		LeaveBlock debitedLeaveBlock = TkServiceLocator.getLeaveBlockService().getLeaveBlock(bt.getDebitedLeaveBlockId());
		assertEquals("forfeited leave block leave amount incorrect", (new BigDecimal(-7)).longValue(), forfeitedLeaveBlock.getLeaveAmount().longValue());
		assertEquals((new BigDecimal(5)).longValue(), accruedLeaveBlock.getLeaveAmount().longValue());
		assertEquals((new BigDecimal(-10)).longValue(), debitedLeaveBlock.getLeaveAmount().longValue());
	}
	
	//TODO: write tests for adjusted max balance cases - i.e. FTE < 1, employee override's w/ type MAX_BALANCE
	
	@Test
	public void testGetEligibleTransfersLeaveApprove() throws Exception {
		Map<String, ArrayList<String>> eligibleTransfers = TkServiceLocator.getBalanceTransferService().getEligibleTransfers(janEntry, USER_ID);
		assertEquals(4, eligibleTransfers.get(LMConstants.MAX_BAL_ACTION_FREQ.LEAVE_APPROVE).size());
		assertEquals(5, eligibleTransfers.get(LMConstants.MAX_BAL_ACTION_FREQ.YEAR_END).size());
		assertEquals(4, eligibleTransfers.get(LMConstants.MAX_BAL_ACTION_FREQ.ON_DEMAND).size());		List<AccrualCategoryRule> rules = new ArrayList<AccrualCategoryRule>();
		for(String eligibleTransfer : eligibleTransfers.get(LMConstants.MAX_BAL_ACTION_FREQ.LEAVE_APPROVE))
			rules.add(TkServiceLocator.getAccrualCategoryRuleService().getAccrualCategoryRule(eligibleTransfer));
			
		LeaveSummary summary = TkServiceLocator.getLeaveSummaryService().getLeaveSummary(USER_ID, janEntry);
		for(AccrualCategoryRule aRule : rules) {
			LeaveSummaryRow row = summary.getLeaveSummaryRowForAccrualCategory(aRule.getLmAccrualCategoryId());
			assertNotNull("eligible accrual category has no balance limit",ObjectUtils.isNotNull(aRule.getMaxBalance()));
			assertTrue("accrual category not eligible for transfer",row.getAccruedBalance().compareTo(aRule.getMaxBalance()) > 0);
		}
	}
	
	@Test
	public void testGetEligibleTransfersYearEnd() throws Exception {
		Map<String, ArrayList<String>> eligibleTransfers = TkServiceLocator.getBalanceTransferService().getEligibleTransfers(janEntry, USER_ID);
		assertEquals(4, eligibleTransfers.get(LMConstants.MAX_BAL_ACTION_FREQ.LEAVE_APPROVE).size());
		assertEquals(5, eligibleTransfers.get(LMConstants.MAX_BAL_ACTION_FREQ.YEAR_END).size());
		assertEquals(4, eligibleTransfers.get(LMConstants.MAX_BAL_ACTION_FREQ.ON_DEMAND).size());
		List<AccrualCategoryRule> rules = new ArrayList<AccrualCategoryRule>();
		for(String eligibleTransfer : eligibleTransfers.get(LMConstants.MAX_BAL_ACTION_FREQ.YEAR_END))
			rules.add(TkServiceLocator.getAccrualCategoryRuleService().getAccrualCategoryRule(eligibleTransfer));
		
		// Set should contain an accrual category whose rule's max balance is trumped by an employee override.
		// Comparing accrued balance to a rule's defined max balance is insufficient for testing
		// whether or not an accrual category is indeed over it's balance limit. Same can be said for FTE-proration.
		LeaveSummary summary = TkServiceLocator.getLeaveSummaryService().getLeaveSummary(USER_ID, janEntry);
		for(AccrualCategoryRule aRule : rules) {
			LeaveSummaryRow row = summary.getLeaveSummaryRowForAccrualCategory(aRule.getLmAccrualCategoryId());
			BigDecimal maxBalance = aRule.getMaxBalance();
			EmployeeOverride mbOverride = TkServiceLocator.getEmployeeOverrideService().getEmployeeOverride(USER_ID,
					"testLP",
					row.getAccrualCategory(),
					"MB",
					janEntry.getBeginPeriodDate());
			if(ObjectUtils.isNotNull(mbOverride))
				maxBalance = new BigDecimal(mbOverride.getOverrideValue());
			assertNotNull("eligible accrual category has no balance limit",maxBalance);
			assertTrue("accrual category not eligible for transfer",row.getAccruedBalance().compareTo(maxBalance) > 0);
		}
	}
	
	@Test
	public void testGetEligibleTransfersOnDemand() throws Exception {
		Map<String, ArrayList<String>> eligibleTransfers = TkServiceLocator.getBalanceTransferService().getEligibleTransfers(janEntry, USER_ID);
		assertEquals(4, eligibleTransfers.get(LMConstants.MAX_BAL_ACTION_FREQ.LEAVE_APPROVE).size());
		assertEquals(5, eligibleTransfers.get(LMConstants.MAX_BAL_ACTION_FREQ.YEAR_END).size());
		assertEquals(4, eligibleTransfers.get(LMConstants.MAX_BAL_ACTION_FREQ.ON_DEMAND).size());
		List<AccrualCategoryRule> rules = new ArrayList<AccrualCategoryRule>();
		for(String eligibleTransfer : eligibleTransfers.get(LMConstants.MAX_BAL_ACTION_FREQ.ON_DEMAND))
			rules.add(TkServiceLocator.getAccrualCategoryRuleService().getAccrualCategoryRule(eligibleTransfer));

		LeaveSummary summary = TkServiceLocator.getLeaveSummaryService().getLeaveSummary(USER_ID, janEntry);
		for(AccrualCategoryRule aRule : rules) {
			LeaveSummaryRow row = summary.getLeaveSummaryRowForAccrualCategory(aRule.getLmAccrualCategoryId());
			assertNotNull("eligible accrual category has no balance limit",ObjectUtils.isNotNull(aRule.getMaxBalance()));
			assertTrue("accrual category not eligible for transfer",row.getAccruedBalance().compareTo(aRule.getMaxBalance()) > 0);
		}
	}
	
	@Test
	public void testGetEligibleTransfersOnYearEndCaseOne() throws Exception {
		//calendar entry is not the last calendar entry of the leave plan's calendar year.
		Map<String, ArrayList<String>> eligibleTransfers = TkServiceLocator.getBalanceTransferService().getEligibleTransfers(decEntry, USER_ID);
		assertEquals(4, eligibleTransfers.get(LMConstants.MAX_BAL_ACTION_FREQ.LEAVE_APPROVE).size());
		assertEquals(0, eligibleTransfers.get(LMConstants.MAX_BAL_ACTION_FREQ.YEAR_END).size());
		assertEquals(4, eligibleTransfers.get(LMConstants.MAX_BAL_ACTION_FREQ.ON_DEMAND).size());
		List<AccrualCategoryRule> rules = new ArrayList<AccrualCategoryRule>();
		for(String eligibleTransfer : eligibleTransfers.get(LMConstants.MAX_BAL_ACTION_FREQ.YEAR_END))
			rules.add(TkServiceLocator.getAccrualCategoryRuleService().getAccrualCategoryRule(eligibleTransfer));

		LeaveSummary summary = TkServiceLocator.getLeaveSummaryService().getLeaveSummary(USER_ID, decEntry);
		for(AccrualCategoryRule aRule : rules) {
			LeaveSummaryRow row = summary.getLeaveSummaryRowForAccrualCategory(aRule.getLmAccrualCategoryId());
			assertNotNull("eligible accrual category has no balance limit",ObjectUtils.isNotNull(aRule.getMaxBalance()));
			assertTrue("accrual category not eligible for transfer",row.getAccruedBalance().compareTo(aRule.getMaxBalance()) > 0);
		}
	}
	
	@Test
	public void testGetEligibleTransfersOnYearEndCaseTwo() throws Exception {
		//calendar entry is the last calendar entry of the leave plan's calendar year.
		Map<String, ArrayList<String>> eligibleTransfers = TkServiceLocator.getBalanceTransferService().getEligibleTransfers(janEntry, USER_ID);
		assertEquals(4, eligibleTransfers.get(LMConstants.MAX_BAL_ACTION_FREQ.LEAVE_APPROVE).size());
		assertEquals(5, eligibleTransfers.get(LMConstants.MAX_BAL_ACTION_FREQ.YEAR_END).size());
		assertEquals(4, eligibleTransfers.get(LMConstants.MAX_BAL_ACTION_FREQ.ON_DEMAND).size());
		List<AccrualCategoryRule> rules = new ArrayList<AccrualCategoryRule>();
		for(String eligibleTransfer : eligibleTransfers.get(LMConstants.MAX_BAL_ACTION_FREQ.YEAR_END))
			rules.add(TkServiceLocator.getAccrualCategoryRuleService().getAccrualCategoryRule(eligibleTransfer));

		LeaveSummary summary = TkServiceLocator.getLeaveSummaryService().getLeaveSummary(USER_ID, janEntry);
		for(AccrualCategoryRule aRule : rules) {
			LeaveSummaryRow row = summary.getLeaveSummaryRowForAccrualCategory(aRule.getLmAccrualCategoryId());
			BigDecimal maxBalance = aRule.getMaxBalance();
			EmployeeOverride mbOverride = TkServiceLocator.getEmployeeOverrideService().getEmployeeOverride(USER_ID,
					"testLP",
					row.getAccrualCategory(),
					"MB",
					janEntry.getBeginPeriodDate());
			EmployeeOverride macOverride = TkServiceLocator.getEmployeeOverrideService().getEmployeeOverride(USER_ID,
					"testLP",
					row.getAccrualCategory(),
					"MAC",
					janEntry.getBeginPeriodDate());
			if(ObjectUtils.isNotNull(mbOverride) && ObjectUtils.isNotNull(macOverride))
				maxBalance = new BigDecimal(Math.min(mbOverride.getOverrideValue(), macOverride.getOverrideValue()));
			else {
				if(ObjectUtils.isNotNull(macOverride))
					maxBalance = new BigDecimal(macOverride.getOverrideValue());
				if(ObjectUtils.isNotNull(mbOverride))
					maxBalance = new BigDecimal(mbOverride.getOverrideValue());
			}
			assertNotNull("eligible accrual category has no balance limit",ObjectUtils.isNotNull(maxBalance));
			assertTrue("accrual category not eligible for transfer",row.getAccruedBalance().compareTo(maxBalance) > 0);
		}
	}
	
	@Test
	public void testSubmitToWorkflow() {
		assertNull(null);
	}
	
	/**
	 * 
	 * TIMESHEET ELIGIBLE TESTS
	 * 
	 */
	
	@Test
	public void testGetEligibleTransfersLeaveApproveForTimesheetCaseOne() throws Exception {
		//Timesheet does not contain the leave calendar end period
		TkServiceLocator.getAccrualService().runAccrual(TS_USER_ID,TK_FROM,TK_TO,true,TS_USER_ID);
		midDecTSD = TkServiceLocator.getTimesheetService().getTimesheetDocument(TSD_MID_DEC_PERIOD_ID);
		midDecTSDEntry = midDecTSD.getCalendarEntry();

		Map<String, ArrayList<String>> eligibleTransfers = TkServiceLocator.getBalanceTransferService().getEligibleTransfers(midDecTSDEntry, TS_USER_ID);
		//Assert correct number of transfer eligible for frequency
		assertEquals(4, eligibleTransfers.get(LMConstants.MAX_BAL_ACTION_FREQ.LEAVE_APPROVE).size());

		//Assert that the accrual categories returned by BT service are in fact over their balance limit,
		//according to their rules. - does not consider FTE.
		List<AccrualCategoryRule> rules = new ArrayList<AccrualCategoryRule>();
		for(String eligibleTransfer : eligibleTransfers.get(LMConstants.MAX_BAL_ACTION_FREQ.LEAVE_APPROVE))
			rules.add(TkServiceLocator.getAccrualCategoryRuleService().getAccrualCategoryRule(eligibleTransfer));

		LeaveSummary summary = TkServiceLocator.getLeaveSummaryService().getLeaveSummary(TS_USER_ID, midDecTSDEntry);
		for(AccrualCategoryRule aRule : rules) {
			LeaveSummaryRow row = summary.getLeaveSummaryRowForAccrualCategory(aRule.getLmAccrualCategoryId());
			assertNotNull("eligible accrual category has no balance limit",ObjectUtils.isNotNull(aRule.getMaxBalance()));
			assertTrue("accrual category not eligible for transfer",row.getAccruedBalance().compareTo(aRule.getMaxBalance()) > 0);
		}
	}
	
	@Test
	public void testGetEligibleTransfersYearEndForTimesheetCaseOne() throws Exception {
		//Timesheet does not include the leave calendar end period
		TkServiceLocator.getAccrualService().runAccrual(TS_USER_ID,TK_FROM,TK_TO,true,TS_USER_ID);
		midDecTSD = TkServiceLocator.getTimesheetService().getTimesheetDocument(TSD_MID_DEC_PERIOD_ID);
		midDecTSDEntry = midDecTSD.getCalendarEntry();

		Map<String, ArrayList<String>> eligibleTransfers = TkServiceLocator.getBalanceTransferService().getEligibleTransfers(midDecTSDEntry, TS_USER_ID);
		//Assert correct number of transfer eligible for frequency
		assertEquals(0, eligibleTransfers.get(LMConstants.MAX_BAL_ACTION_FREQ.YEAR_END).size());
		
		/**
		 * No eligible transfers to test balance limit.
		 */
/*		//Assert that the accrual categories returned by BT service are in fact over their balance limit,
		//according to their rules.
		List<AccrualCategoryRule> rules = new ArrayList<AccrualCategoryRule>();
		for(String eligibleTransfer : eligibleTransfers.get(LMConstants.MAX_BAL_ACTION_FREQ.YEAR_END))
			rules.add(TkServiceLocator.getAccrualCategoryRuleService().getAccrualCategoryRule(eligibleTransfer));
			
		// Set should contain an accrual category whose rule's max balance is trumped by an employee override.
		// Comparing accrued balance to a rule's defined max balance is insufficient for testing
		// whether or not an accrual category is indeed over it's balance limit. Same can be said for FTE-proration.
		// However, in this case, using an employee override will 
		LeaveSummary summary = TkServiceLocator.getLeaveSummaryService().getLeaveSummary(TS_USER_ID, midDecTSDEntry);
		for(AccrualCategoryRule aRule : rules) {
			LeaveSummaryRow row = summary.getLeaveSummaryRowForAccrualCategory(aRule.getLmAccrualCategoryId());
			BigDecimal maxBalance = aRule.getMaxBalance();
			EmployeeOverride mbOverride = TkServiceLocator.getEmployeeOverrideService().getEmployeeOverride(TS_USER_ID,
					"testLP",
					row.getAccrualCategory(),
					"MB",
					janEntry.getBeginPeriodDate());
			if(ObjectUtils.isNotNull(mbOverride))
				maxBalance = new BigDecimal(mbOverride.getOverrideValue());
			//Don't care about employee override existence, this is not the leave plan's roll-over period.
			assertNotNull("eligible accrual category has no balance limit",maxBalance);
			assertTrue("accrual category not eligible for transfer",row.getAccruedBalance().compareTo(maxBalance) > 0);
		}*/
	}
	
	@Test
	public void testGetEligibleTransfersOnDemandForTimesheetCaseOne() throws Exception {
		//Timesheet does not include the leave calendar end period
		TkServiceLocator.getAccrualService().runAccrual(TS_USER_ID,TK_FROM,TK_TO,true,TS_USER_ID);
		midDecTSD = TkServiceLocator.getTimesheetService().getTimesheetDocument(TSD_MID_DEC_PERIOD_ID);
		midDecTSDEntry = midDecTSD.getCalendarEntry();

		Map<String, ArrayList<String>> eligibleTransfers = TkServiceLocator.getBalanceTransferService().getEligibleTransfers(midDecTSDEntry, TS_USER_ID);
		//Assert correct number of transfer eligible for frequency
		assertEquals(4, eligibleTransfers.get(LMConstants.MAX_BAL_ACTION_FREQ.ON_DEMAND).size());

		//Assert that the accrual categories returned by BT service are in fact over their balance limit,
		//according to their rules. - does not consider FTE.
		List<AccrualCategoryRule> rules = new ArrayList<AccrualCategoryRule>();
		for(String eligibleTransfer : eligibleTransfers.get(LMConstants.MAX_BAL_ACTION_FREQ.ON_DEMAND))
			rules.add(TkServiceLocator.getAccrualCategoryRuleService().getAccrualCategoryRule(eligibleTransfer));

		LeaveSummary summary = TkServiceLocator.getLeaveSummaryService().getLeaveSummary(TS_USER_ID, midDecTSDEntry);
		for(AccrualCategoryRule aRule : rules) {
			LeaveSummaryRow row = summary.getLeaveSummaryRowForAccrualCategory(aRule.getLmAccrualCategoryId());
			assertNotNull("eligible accrual category has no balance limit",ObjectUtils.isNotNull(aRule.getMaxBalance()));
			assertTrue("accrual category not eligible for transfer",row.getAccruedBalance().compareTo(aRule.getMaxBalance()) > 0);
		}
	}
	
	@Test
	public void testGetEligibleTransfersLeaveApproveForTimesheetCaseTwo() throws Exception {
		//Timesheet includes the leave calendar end period, but does not include the leave plan's start date.
		TkServiceLocator.getAccrualService().runAccrual(TS_USER_ID,TK_FROM,TK_TO,true,TS_USER_ID);
		endDecTSD = TkServiceLocator.getTimesheetService().getTimesheetDocument(TSD_END_DEC_PERIOD_ID);
		endDecTSDEntry = endDecTSD.getCalendarEntry();

		Map<String, ArrayList<String>> eligibleTransfers = TkServiceLocator.getBalanceTransferService().getEligibleTransfers(endDecTSDEntry, TS_USER_ID);
		//Assert correct number of transfer eligible for frequency
		assertEquals(4, eligibleTransfers.get(LMConstants.MAX_BAL_ACTION_FREQ.LEAVE_APPROVE).size());
		
		//Assert that the accrual categories returned by BT service are in fact over their balance limit,
		//according to their rules. - does not consider FTE.
		List<AccrualCategoryRule> rules = new ArrayList<AccrualCategoryRule>();
		for(String eligibleTransfer : eligibleTransfers.get(LMConstants.MAX_BAL_ACTION_FREQ.LEAVE_APPROVE))
			rules.add(TkServiceLocator.getAccrualCategoryRuleService().getAccrualCategoryRule(eligibleTransfer));

		LeaveSummary summary = TkServiceLocator.getLeaveSummaryService().getLeaveSummary(TS_USER_ID, endDecTSDEntry);
		for(AccrualCategoryRule aRule : rules) {
			LeaveSummaryRow row = summary.getLeaveSummaryRowForAccrualCategory(aRule.getLmAccrualCategoryId());
			assertNotNull("eligible accrual category has no balance limit",ObjectUtils.isNotNull(aRule.getMaxBalance()));
			assertTrue("accrual category not eligible for transfer",row.getAccruedBalance().compareTo(aRule.getMaxBalance()) > 0);
		}
	}
	
	@Test
	public void testGetEligibleTransfersYearEndForTimesheetCaseTwo() throws Exception {
		//Timesheet includes the leave calendar end period, but does not include the leave plan's start date.
		TkServiceLocator.getAccrualService().runAccrual(TS_USER_ID,TK_FROM,TK_TO,true,TS_USER_ID);
		endDecTSD = TkServiceLocator.getTimesheetService().getTimesheetDocument(TSD_END_DEC_PERIOD_ID);
		endDecTSDEntry = endDecTSD.getCalendarEntry();

		Map<String, ArrayList<String>> eligibleTransfers = TkServiceLocator.getBalanceTransferService().getEligibleTransfers(endDecTSDEntry, TS_USER_ID);
		//Assert correct number of transfer eligible for frequency
		assertEquals(0, eligibleTransfers.get(LMConstants.MAX_BAL_ACTION_FREQ.YEAR_END).size());
		
		//Assert that the accrual categories returned by BT service are in fact over their balance limit,
		//according to their rules. - does not consider FTE.
		List<AccrualCategoryRule> rules = new ArrayList<AccrualCategoryRule>();
		for(String eligibleTransfer : eligibleTransfers.get(LMConstants.MAX_BAL_ACTION_FREQ.YEAR_END))
			rules.add(TkServiceLocator.getAccrualCategoryRuleService().getAccrualCategoryRule(eligibleTransfer));

		// Set should contain an accrual category whose rule's max balance is trumped by an employee override.
		// Comparing accrued balance to a rule's defined max balance is insufficient for testing
		// whether or not an accrual category is indeed over it's balance limit. Same can be said for FTE-proration.
		LeaveSummary summary = TkServiceLocator.getLeaveSummaryService().getLeaveSummary(TS_USER_ID, endDecTSDEntry);
		for(AccrualCategoryRule aRule : rules) {
			LeaveSummaryRow row = summary.getLeaveSummaryRowForAccrualCategory(aRule.getLmAccrualCategoryId());
			BigDecimal maxBalance = aRule.getMaxBalance();
			EmployeeOverride mbOverride = TkServiceLocator.getEmployeeOverrideService().getEmployeeOverride(TS_USER_ID,
					"testLP",
					row.getAccrualCategory(),
					"MB",
					janEntry.getBeginPeriodDate());
			if(ObjectUtils.isNotNull(mbOverride))
				maxBalance = new BigDecimal(mbOverride.getOverrideValue());
			assertNotNull("eligible accrual category has no balance limit",maxBalance);
			assertTrue("accrual category not eligible for transfer",row.getAccruedBalance().compareTo(maxBalance) > 0);
		}
	}
	
	@Test
	public void testGetEligibleTransfersOnDemandForTimesheetCaseTwo() throws Exception {
		//Timesheet includes the leave calendar end period, but does not include the leave plan's start date.
		TkServiceLocator.getAccrualService().runAccrual(TS_USER_ID,TK_FROM,TK_TO,true,TS_USER_ID);
		endDecTSD = TkServiceLocator.getTimesheetService().getTimesheetDocument(TSD_END_DEC_PERIOD_ID);
		endDecTSDEntry = endDecTSD.getCalendarEntry();


		Map<String, ArrayList<String>> eligibleTransfers = TkServiceLocator.getBalanceTransferService().getEligibleTransfers(endDecTSDEntry, TS_USER_ID);
		//Assert correct number of transfer eligible for frequency
		assertEquals(4, eligibleTransfers.get(LMConstants.MAX_BAL_ACTION_FREQ.ON_DEMAND).size());
		
		//Assert that the accrual categories returned by BT service are in fact over their balance limit,
		//according to their rules. - does not consider FTE.
		List<AccrualCategoryRule> rules = new ArrayList<AccrualCategoryRule>();
		for(String eligibleTransfer : eligibleTransfers.get(LMConstants.MAX_BAL_ACTION_FREQ.ON_DEMAND))
			rules.add(TkServiceLocator.getAccrualCategoryRuleService().getAccrualCategoryRule(eligibleTransfer));

		LeaveSummary summary = TkServiceLocator.getLeaveSummaryService().getLeaveSummary(TS_USER_ID,endDecTSDEntry);
		for(AccrualCategoryRule aRule : rules) {
			LeaveSummaryRow row = summary.getLeaveSummaryRowForAccrualCategory(aRule.getLmAccrualCategoryId());
			assertNotNull("eligible accrual category has no balance limit",ObjectUtils.isNotNull(aRule.getMaxBalance()));
			assertTrue("accrual category not eligible for transfer",row.getAccruedBalance().compareTo(aRule.getMaxBalance()) > 0);
		}
	}
	
	@Test
	public void testGetEligibleTransfersYearEndForTimesheetCaseThree() throws Exception {
		//Timesheet includes the leave calendar end period, which is the leave plan's roll-over date.
		TkServiceLocator.getAccrualService().runAccrual(TS_USER_ID,TK_FROM,TK_TO,true,TS_USER_ID);
		endJanTSD = TkServiceLocator.getTimesheetService().getTimesheetDocument(TSD_END_JAN_PERIOD_ID);
		endJanTSDEntry = endJanTSD.getCalendarEntry();

		Map<String, ArrayList<String>> eligibleTransfers = TkServiceLocator.getBalanceTransferService().getEligibleTransfers(endJanTSDEntry, TS_USER_ID);
		//Assert correct number of transfer eligible for frequency
		assertEquals(3, eligibleTransfers.get(LMConstants.MAX_BAL_ACTION_FREQ.YEAR_END).size());
		
		//Assert that the accrual categories returned by BT service are in fact over their balance limit,
		//according to their rules. - does not consider FTE.
		List<AccrualCategoryRule> rules = new ArrayList<AccrualCategoryRule>();
		for(String eligibleTransfer : eligibleTransfers.get(LMConstants.MAX_BAL_ACTION_FREQ.YEAR_END)) {
			rules.add(TkServiceLocator.getAccrualCategoryRuleService().getAccrualCategoryRule(eligibleTransfer));
        }

		LeaveSummary summary = TkServiceLocator.getLeaveSummaryService().getLeaveSummary(TS_USER_ID,endJanTSDEntry);
		for(AccrualCategoryRule aRule : rules) {
			LeaveSummaryRow row = summary.getLeaveSummaryRowForAccrualCategory(aRule.getLmAccrualCategoryId());
			BigDecimal maxBalance = aRule.getMaxBalance();
			EmployeeOverride mbOverride = TkServiceLocator.getEmployeeOverrideService().getEmployeeOverride(TS_USER_ID,
					"testLP",
					row.getAccrualCategory(),
					"MB",
					janEntry.getBeginPeriodDate());
			EmployeeOverride macOverride = TkServiceLocator.getEmployeeOverrideService().getEmployeeOverride(TS_USER_ID,
					"testLP",
					row.getAccrualCategory(),
					"MAC",
					janEntry.getBeginPeriodDate());
			if(ObjectUtils.isNotNull(mbOverride) && ObjectUtils.isNotNull(macOverride)) {
				maxBalance = new BigDecimal(Math.min(mbOverride.getOverrideValue(), macOverride.getOverrideValue()));
            } else {
				if(ObjectUtils.isNotNull(macOverride)) {
					maxBalance = new BigDecimal(macOverride.getOverrideValue());
                }
				if(ObjectUtils.isNotNull(mbOverride)) {
					maxBalance = new BigDecimal(mbOverride.getOverrideValue());
                }
			}
			assertNotNull("eligible accrual category has no balance limit",ObjectUtils.isNotNull(maxBalance));
			assertTrue("accrual category " + aRule.getLmAccrualCategoryId() + " not eligible for transfer",row.getAccruedBalance().compareTo(maxBalance) > 0);
		}
	}
}