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
package org.kuali.hr.time.accrual.service;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.kuali.hr.lm.LMConstants;
import org.kuali.hr.lm.leaveblock.LeaveBlock;
import org.kuali.hr.lm.leaveblock.LeaveBlockHistory;
import org.kuali.hr.test.KPMETestCase;
import org.kuali.hr.time.service.base.TkServiceLocator;
import org.kuali.hr.time.util.TKUtils;

public class AccrualServiceTest extends KPMETestCase {
	 Date START_DATE = new Date((new DateTime(2012, 2, 20, 0, 0, 0, 0, TKUtils.getSystemDateTimeZone())).getMillis());
	 Date END_DATE = new Date((new DateTime(2012, 5, 3, 0, 0, 0, 0, TKUtils.getSystemDateTimeZone())).getMillis());
	
	 String PRINCIPAL_ID = "testUser";
	 String PRINCIPAL_ID_2 = "testUser2";
	 String PRINCIPAL_ID_3 = "testUser3";
	 String PRINCIPAL_ID_4 = "testUser4";
	 String PRINCIPAL_ID_5 = "testUser5";
	 String PRINCIPAL_ID_6 = "testUser6";
	 String PRINCIPAL_ID_7 = "testUser7";
	 String PRINCIPAL_ID_8 = "testUser8";
	  
	@Test
	/*the employee has job A starts from 03/01/2012, ends on 04/01/2012 with standard hours of 40, so fte is 1.0
	 * job B starts on 04/01/2012, no end to it with standard hours of 20, fte is 0.5
	 * on 04/10/2012, there's a system scheduled time off of 8 hrs
	 */
	public void testRunAccrualForStatusChange() {
		
		 List<LeaveBlock> leaveBlockList = (List<LeaveBlock>) TkServiceLocator.getLeaveBlockService().getLeaveBlocks(PRINCIPAL_ID, START_DATE, END_DATE);
		 Assert.assertTrue("There are leave blocks before runAccrual for princiapl id " + PRINCIPAL_ID, leaveBlockList.isEmpty());
		 
		 // first time to run accrual from 02/20/2012 to 05/03/2012
		 TkServiceLocator.getLeaveAccrualService().runAccrual(PRINCIPAL_ID, START_DATE, END_DATE, false);
		 verifyLeaveBlocksForStatusChange();
		 
		 List<LeaveBlockHistory> historyList = TkServiceLocator.getLeaveBlockHistoryService().getLeaveBlockHistories(PRINCIPAL_ID, null);
		 Assert.assertTrue("There should be 5 leave block history for emplyee " + PRINCIPAL_ID + ", not " + historyList.size(), historyList.size()== 5);
		 LeaveBlockHistory lbh = historyList.get(0);
		 Assert.assertTrue("Leave Block Type of leave block history should be " + LMConstants.LEAVE_BLOCK_TYPE.ACCRUAL_SERVICE + ", not " + lbh.getLeaveBlockType()
					, lbh.getLeaveBlockType().equals(LMConstants.LEAVE_BLOCK_TYPE.ACCRUAL_SERVICE));
		 
		 // second time to run accrual from 02/20/2012 to 05/03/2012, should get the same results as first run
		 TkServiceLocator.getLeaveAccrualService().runAccrual(PRINCIPAL_ID, START_DATE, END_DATE, false);
		 verifyLeaveBlocksForStatusChange();
		 
		 historyList = TkServiceLocator.getLeaveBlockHistoryService().getLeaveBlockHistories(PRINCIPAL_ID, null);
		 Assert.assertTrue("There should be 15 leave block history for employee " + PRINCIPAL_ID + ", not " + historyList.size(), historyList.size()== 15);
	}
	private void verifyLeaveBlocksForStatusChange() {
		List<LeaveBlock> leaveBlockList;
		leaveBlockList = TkServiceLocator.getLeaveBlockService().getLeaveBlocks(PRINCIPAL_ID, START_DATE, END_DATE);
		Assert.assertFalse("No leave blocks created by runAccrual for princiapl id " + PRINCIPAL_ID, leaveBlockList.isEmpty());
		Assert.assertTrue("There should be 5 leave blocks for emplyee 'testUser', not " + leaveBlockList.size(), leaveBlockList.size()== 5);
		 
		// there should be one leave block of 16 hours on 03/31/2012
		Date intervalDate = new Date((new DateTime(2012, 3, 31, 5, 0, 0, 0, TKUtils.getSystemDateTimeZone())).getMillis());
		leaveBlockList = TkServiceLocator.getLeaveBlockService().getLeaveBlocksForDate(PRINCIPAL_ID, intervalDate);
		Assert.assertTrue("There should be 1 leave block for date 03/31/2012.", leaveBlockList.size()==1);
		LeaveBlock lb = leaveBlockList.get(0);
		Assert.assertTrue("Hours of the leave block for date 03/31/2012 should be 16, not " + lb.getLeaveAmount().toString(), lb.getLeaveAmount().equals(new BigDecimal(16)));
		Assert.assertNull("lm_sys_schd_timeoff_id should be null for regular accrual leave block", lb.getScheduleTimeOffId());
		Assert.assertTrue("Leave Block Type of leave block should be " + LMConstants.LEAVE_BLOCK_TYPE.ACCRUAL_SERVICE + ", not " + lb.getLeaveBlockType()
				, lb.getLeaveBlockType().equals(LMConstants.LEAVE_BLOCK_TYPE.ACCRUAL_SERVICE));
		Assert.assertTrue("Requst status of leave block should be " + LMConstants.REQUEST_STATUS.APPROVED + ", not " + lb.getRequestStatus()
				, lb.getRequestStatus().equals(LMConstants.REQUEST_STATUS.APPROVED));
		
		// employee changed status on 04/01, fte is changed from 1 to 0.5
		// there should be an empty leave block for status change on 04/01/2012
		intervalDate = new Date((new DateTime(2012, 4, 01, 5, 0, 0, 0, TKUtils.getSystemDateTimeZone())).getMillis());
		leaveBlockList = TkServiceLocator.getLeaveBlockService().getLeaveBlocksForDate(PRINCIPAL_ID, intervalDate);
		Assert.assertTrue("There should be 1 leave block for date 04/01/2012.", leaveBlockList.size()==1);
		lb = leaveBlockList.get(0);
		Assert.assertTrue("Hours of the leave block for date 04/01/2012 should be 0, not " + lb.getLeaveAmount().toString(), lb.getLeaveAmount().equals(BigDecimal.ZERO));
		Assert.assertTrue("Leave Code of the leave block for date 04/01/2012 should be " + LMConstants.STATUS_CHANGE_EARN_CODE + ", not " + lb.getEarnCode()
				, lb.getEarnCode().equals(LMConstants.STATUS_CHANGE_EARN_CODE));
		Assert.assertNull("accrual_category should be null for empty status change leave block", lb.getAccrualCategory()); 
		Assert.assertTrue("Leave Block Type of leave block should be " + LMConstants.LEAVE_BLOCK_TYPE.ACCRUAL_SERVICE + ", not " + lb.getLeaveBlockType()
				, lb.getLeaveBlockType().equals(LMConstants.LEAVE_BLOCK_TYPE.ACCRUAL_SERVICE));
		Assert.assertTrue("Requst status of leave block should be " + LMConstants.REQUEST_STATUS.APPROVED + ", not " + lb.getRequestStatus()
				, lb.getRequestStatus().equals(LMConstants.REQUEST_STATUS.APPROVED));
		
		// there should be two holiday leave blocks of 4 hours on 04/10/2012, one positive, one negative
		intervalDate = new Date((new DateTime(2012, 4, 10, 5, 0, 0, 0, TKUtils.getSystemDateTimeZone())).getMillis());
		leaveBlockList = TkServiceLocator.getLeaveBlockService().getLeaveBlocksForDate(PRINCIPAL_ID, intervalDate);
		Assert.assertTrue("There should be 2 leave block for date 04/10/2012.", leaveBlockList.size()==2);
		for(LeaveBlock aLeaveBlock : leaveBlockList) {
			Assert.assertNotNull("lm_sys_schd_timeoff_id should NOT be null for holiday accrual leave block", aLeaveBlock.getScheduleTimeOffId());
			Assert.assertTrue("Leave Block Type of leave block should be " + LMConstants.LEAVE_BLOCK_TYPE.ACCRUAL_SERVICE + ", not " + aLeaveBlock.getLeaveBlockType()
					, aLeaveBlock.getLeaveBlockType().equals(LMConstants.LEAVE_BLOCK_TYPE.ACCRUAL_SERVICE));
			Assert.assertTrue("Requst status of leave block should be " + LMConstants.REQUEST_STATUS.APPROVED + ", not " + aLeaveBlock.getRequestStatus()
					, aLeaveBlock.getRequestStatus().equals(LMConstants.REQUEST_STATUS.APPROVED));
			if(!aLeaveBlock.getLeaveAmount().equals(new BigDecimal(4)) && !aLeaveBlock.getLeaveAmount().equals(new BigDecimal(-4))) {
				Assert.fail("Hours of the leave blocks for date 04/10/2012 should be either 4 or -4, not " + aLeaveBlock.getLeaveAmount().toString());
			} 
		}
		
		// there should be 1 leave blocks on 04/30/2012, regular accrual of 8 hours
		intervalDate = new Date((new DateTime(2012, 4, 30, 5, 0, 0, 0, TKUtils.getSystemDateTimeZone())).getMillis());
		leaveBlockList = TkServiceLocator.getLeaveBlockService().getLeaveBlocksForDate(PRINCIPAL_ID, intervalDate);
		Assert.assertTrue("There should be 1 leave block for date 04/30/2012.", leaveBlockList.size()==1);
		lb = leaveBlockList.get(0);
		Assert.assertTrue("Hours of the regular accrual leave block for date 04/30/2012 should be 8, not " + lb.getLeaveAmount().toString(), lb.getLeaveAmount().equals(new BigDecimal(8)));
		Assert.assertNull("lm_sys_schd_timeoff_id should be null for regular accrual leave block", lb.getScheduleTimeOffId());
		Assert.assertTrue("Leave Block Type of leave block should be " + LMConstants.LEAVE_BLOCK_TYPE.ACCRUAL_SERVICE + ", not " + lb.getLeaveBlockType()
				, lb.getLeaveBlockType().equals(LMConstants.LEAVE_BLOCK_TYPE.ACCRUAL_SERVICE));
		Assert.assertTrue("Requst status of leave block should be " + LMConstants.REQUEST_STATUS.APPROVED + ", not " + lb.getRequestStatus()
				, lb.getRequestStatus().equals(LMConstants.REQUEST_STATUS.APPROVED));
	}
	
	@Test
	/* testUser's leavePlan "testLP" has planning month of 12
	 * after calculateFutureAccrualUsingPlanningMonth, try to get leaveBlock for 18 months in the future
	 * should still get 12 leave blocks. The accrual service also goes back 1 year for accrual runs.
	 */
	public void testCalculateFutureAccrualUsingPlanningMonth() {
		// the planning month of this leave plan is set to 12
		Date currentDate = TKUtils.getCurrentDate();		
		TkServiceLocator.getLeaveAccrualService().calculateFutureAccrualUsingPlanningMonth(PRINCIPAL_ID, currentDate);		
		Calendar aCal = Calendar.getInstance();
		aCal.setTime(currentDate);
		aCal.add(Calendar.MONTH, 18);
		Date endDate = new java.sql.Date(aCal.getTime().getTime());
		// lookup future leave blocks up to 18 months in the future
		List<LeaveBlock> leaveBlockList = TkServiceLocator.getLeaveBlockService().getLeaveBlocks(PRINCIPAL_ID, currentDate, aCal.getTime());
		
		int futureSize = 12;
		int allSize = 17;
		if(aCal.getActualMaximum(Calendar.DAY_OF_MONTH) == aCal.get(Calendar.DATE)) {
			futureSize = 13;
			allSize = 18;
		}
		Assert.assertFalse("No leave blocks created by calculateF?utureAccrualUsingPlanningMonth for princiapl id " + PRINCIPAL_ID, leaveBlockList.isEmpty());
		Assert.assertTrue("There should be " + futureSize + " leave blocks for employee 'testUser', not " + leaveBlockList.size(), leaveBlockList.size()== futureSize);
		
		aCal.setTime(currentDate);
		aCal.add(Calendar.MONTH, -5);
		Date startDate = new java.sql.Date(aCal.getTime().getTime());
		// lookup leave blocks including past and future
		leaveBlockList = TkServiceLocator.getLeaveBlockService().getLeaveBlocks(PRINCIPAL_ID, startDate, endDate);
		Assert.assertTrue("There should be  " + allSize + " leave blocks for employee 'testUser', not " + leaveBlockList.size(), leaveBlockList.size()== allSize);
	}
	
	@Test
	/*
	  *testUser2 has two accrual category rules, rule 1 goes from 0 month to 5 with accrual rate of 16
	  *rule 2 goes from 5 to 900 with accrual rate of 24
	  *run accrual for testUser2 for 15 months
	 */
	public void testRunAccrualForRuleChanges() {
		
		 Calendar aCal = Calendar.getInstance();
		 aCal.setTime(START_DATE);
		 aCal.add(Calendar.MONTH, 15);
		 Date endDate = new java.sql.Date(aCal.getTime().getTime());
		 
		 List<LeaveBlock> leaveBlockList = (List<LeaveBlock>) TkServiceLocator.getLeaveBlockService().getLeaveBlocks(PRINCIPAL_ID_2, START_DATE, endDate);
		 Assert.assertTrue("There are leave blocks before runAccrual for princiapl id " + PRINCIPAL_ID_2, leaveBlockList.isEmpty());
		
		 TkServiceLocator.getLeaveAccrualService().runAccrual(PRINCIPAL_ID_2, START_DATE, endDate, false);
		 
		 leaveBlockList = TkServiceLocator.getLeaveBlockService().getLeaveBlocks(PRINCIPAL_ID_2, START_DATE, endDate);
		 Assert.assertTrue("There should be 14 leave blocks for emplyee " + PRINCIPAL_ID_2 + ", not " + leaveBlockList.size(), leaveBlockList.size()== 14);
		 
		 // July of 2012 is the 5th month of this user's employment, the accrual rate should be 16
		 Date intervalDate = new Date((new DateTime(2012, 7, 31, 5, 0, 0, 0, TKUtils.getSystemDateTimeZone())).getMillis());
		 leaveBlockList = TkServiceLocator.getLeaveBlockService().getLeaveBlocksForDate(PRINCIPAL_ID_2, intervalDate);
		 Assert.assertTrue("There should be 1 leave block for employee " + PRINCIPAL_ID_2 + " for date 07/31/2012.", leaveBlockList.size()==1);
		 LeaveBlock lb = leaveBlockList.get(0);
		 Assert.assertTrue("Hours of the leave block on date 07/31/2012 for employee " + PRINCIPAL_ID_2 + " should be 16, not " + lb.getLeaveAmount().toString()
				 , lb.getLeaveAmount().equals(new BigDecimal(16)));
		 
		 // August of 2012 is the 6th month of this user's employment, the accrual rate should be 24 from now on
		 intervalDate = new Date((new DateTime(2012, 8, 31, 5, 0, 0, 0, TKUtils.getSystemDateTimeZone())).getMillis());
		 leaveBlockList = TkServiceLocator.getLeaveBlockService().getLeaveBlocksForDate(PRINCIPAL_ID_2, intervalDate);
		 Assert.assertTrue("There should be 1 leave block for employee " + PRINCIPAL_ID_2 + " for date 08/31/2012.", leaveBlockList.size()==1);
		 lb = leaveBlockList.get(0);
		 Assert.assertTrue("Hours of the leave block for date 08/31/2012 for employee " + PRINCIPAL_ID_2 + " should be 24, not " + lb.getLeaveAmount().toString()
				 , lb.getLeaveAmount().equals(new BigDecimal(24)));
		 
	}
	@Test
	/* testUser3's leavePlan has 2 accrual categories with different accrual intervals
	 * testAC3 has accrual interval semi-monthly
	 * testAC4 has accrual interval monthly
	 */
	public void testRunAccrualWithDifferentAccrualIntervals() {
		 
		 List<LeaveBlock> leaveBlockList = (List<LeaveBlock>) TkServiceLocator.getLeaveBlockService().getLeaveBlocks(PRINCIPAL_ID_3, START_DATE, END_DATE);
		 Assert.assertTrue("There are leave blocks before runAccrual for princiapl id " + PRINCIPAL_ID_3, leaveBlockList.isEmpty());
		 
		 TkServiceLocator.getLeaveAccrualService().runAccrual(PRINCIPAL_ID_3, START_DATE, END_DATE, false);
		 leaveBlockList = (List<LeaveBlock>) TkServiceLocator.getLeaveBlockService().getLeaveBlocks(PRINCIPAL_ID_3, START_DATE, END_DATE);
		 Assert.assertTrue("There should be 6 leave blocks for emplyee " + PRINCIPAL_ID_3 + ", not " + leaveBlockList.size(), leaveBlockList.size()== 6);
		 
		 Date semiMonthlyDate = new Date((new DateTime(2012, 3, 15, 5, 0, 0, 0, TKUtils.getSystemDateTimeZone())).getMillis());
		 Date monthlyDate = new Date((new DateTime(2012, 3, 31, 5, 0, 0, 0, TKUtils.getSystemDateTimeZone())).getMillis());
		 this.verifyLeaveBlocksWithDifferentAccrualIntervals(semiMonthlyDate, monthlyDate);

		 semiMonthlyDate = new Date((new DateTime(2012, 4, 15, 5, 0, 0, 0, TKUtils.getSystemDateTimeZone())).getMillis());
		 monthlyDate = new Date((new DateTime(2012, 4, 30, 5, 0, 0, 0, TKUtils.getSystemDateTimeZone())).getMillis());
		 this.verifyLeaveBlocksWithDifferentAccrualIntervals(semiMonthlyDate, monthlyDate);
		 
	}
	public void verifyLeaveBlocksWithDifferentAccrualIntervals(Date semiMonthlyDate, Date monthlyDate) {
		 List<LeaveBlock> leaveBlockList = TkServiceLocator.getLeaveBlockService().getLeaveBlocksForDate(PRINCIPAL_ID_3, semiMonthlyDate);
		 Assert.assertTrue("There should be 1 leave block for date " + semiMonthlyDate.toString() + " for emplyee " + PRINCIPAL_ID_3, leaveBlockList.size()==1);
		 LeaveBlock lb = leaveBlockList.get(0);
		 Assert.assertTrue("Hours of the leave block on date " + semiMonthlyDate.toString() + " should be 8, not " + lb.getLeaveAmount().toString(), lb.getLeaveAmount().equals(new BigDecimal(8)));
		 Assert.assertTrue("Leave code of the leave block on date " + semiMonthlyDate.toString() + " should be EC1, not " + lb.getEarnCode(), lb.getEarnCode().equals("EC1"));
		 Assert.assertTrue("accrual_category of the leave block on date " + semiMonthlyDate.toString() + " should be testAC3, not " + lb.getAccrualCategory(), lb.getAccrualCategory().equals("testAC3"));
		
		
		 leaveBlockList = TkServiceLocator.getLeaveBlockService().getLeaveBlocksForDate(PRINCIPAL_ID_3, monthlyDate);
		 Assert.assertTrue("There should be 2 leave block for date " + monthlyDate.toString() + " for emplyee " + PRINCIPAL_ID_3, leaveBlockList.size()==2);
		 for(LeaveBlock aLeaveBlock : leaveBlockList) {
			 if(aLeaveBlock.getAccrualCategory().equals("testAC3")) {
				 Assert.assertTrue("Leave code of the leave block with accrualCategory testAC3 on date " + monthlyDate.toString() + " should be EC1, not " + aLeaveBlock.getEarnCode()
						 , aLeaveBlock.getEarnCode().equals("EC1")); 
				 Assert.assertTrue("Hours of the leave block with accrualCategory testAC4 on date " + monthlyDate.toString() + " should be 8, not " + aLeaveBlock.getLeaveAmount().toString()
						 , aLeaveBlock.getLeaveAmount().equals(new BigDecimal(8)));
			 } else if(aLeaveBlock.getAccrualCategory().equals("testAC4")) {
				 Assert.assertTrue("Leave code of the leave block with accrualCategory testAC4 on date " + monthlyDate.toString() + " should be EC2, not " + aLeaveBlock.getEarnCode()
						 , aLeaveBlock.getEarnCode().equals("EC2")); 
				 Assert.assertTrue("Hours of the leave block with accrualCategory testAC4 on date " + monthlyDate.toString() + " should be 24, not " + aLeaveBlock.getLeaveAmount().toString()
						 , aLeaveBlock.getLeaveAmount().equals(new BigDecimal(24)));
			 } else {
				 Assert.fail("Leave block on date " + monthlyDate.toString() + " should not have accrual_category " + aLeaveBlock.getAccrualCategory());
			 }
		 }
	}
	
	@Test
	/* testUser4 has PrincipalHRAttributes that's associated with 2 accrual categories, one is testAC5, the other is testAC6
	 * testAC5 has minimum accrual of 0.5, proration = true
	 * testAC6 has minimum accrual of 0
	 * testUser4 has records of principalHRAttributes, the employment is from 03/18/2012 to 08/08/2012
	 * There should not be accrual for testAC5 in March and August of 2012 since minimum percentage is not reached
	 */
	public void testMinNotReachedProrationTrueFirstLastPeriod() {
		 Date end = new Date((new DateTime(2012, 9, 25, 0, 0, 0, 0, TKUtils.getSystemDateTimeZone())).getMillis());
		 List<LeaveBlock> leaveBlockList = (List<LeaveBlock>) TkServiceLocator.getLeaveBlockService().getLeaveBlocks(PRINCIPAL_ID_4, START_DATE, end);
		 Assert.assertTrue("There are leave blocks before runAccrual for princiapl id " + PRINCIPAL_ID_4, leaveBlockList.isEmpty());
		 
		 TkServiceLocator.getLeaveAccrualService().runAccrual(PRINCIPAL_ID_4, START_DATE, end, false);
		 leaveBlockList = (List<LeaveBlock>) TkServiceLocator.getLeaveBlockService().getLeaveBlocks(PRINCIPAL_ID_4, START_DATE, end);
		 Assert.assertTrue("There should be 10 leave blocks for emplyee " + PRINCIPAL_ID_4 + ", not " + leaveBlockList.size(), leaveBlockList.size()== 10);
		 
		 Date monthlyDate = new Date((new DateTime(2012, 3, 31, 5, 0, 0, 0, TKUtils.getSystemDateTimeZone())).getMillis());
		 leaveBlockList = TkServiceLocator.getLeaveBlockService().getLeaveBlocksForDate(PRINCIPAL_ID_4, monthlyDate);
		 Assert.assertTrue("There should be 1 leave block for date " + monthlyDate.toString() + " for emplyee " + PRINCIPAL_ID_4, leaveBlockList.size()==1);
		 LeaveBlock lb = leaveBlockList.get(0);
		 Assert.assertTrue("Leave block on date " + monthlyDate.toString() + " should have accrual_category testAC6, not " + lb.getAccrualCategory()
				 , lb.getAccrualCategory().equals("testAC6"));
		 Assert.assertTrue("Leave block on date " + monthlyDate.toString() + " should have 7 hours, not " + lb.getLeaveAmount()
				 , lb.getLeaveAmount().equals(new BigDecimal(7)));
		
		 monthlyDate = new Date((new DateTime(2012, 4, 30, 5, 0, 0, 0, TKUtils.getSystemDateTimeZone())).getMillis());
		 leaveBlockList = TkServiceLocator.getLeaveBlockService().getLeaveBlocksForDate(PRINCIPAL_ID_4, monthlyDate);
		 Assert.assertTrue("There should be 2 leave block for date " + monthlyDate.toString() + " for emplyee " + PRINCIPAL_ID_4, leaveBlockList.size()==2);
		 
// should the accrual of the last days show up on the end day or the interval day of the last pay period?????
		 monthlyDate = new Date((new DateTime(2012, 8, 8, 5, 0, 0, 0, TKUtils.getSystemDateTimeZone())).getMillis());
		 leaveBlockList = TkServiceLocator.getLeaveBlockService().getLeaveBlocksForDate(PRINCIPAL_ID_4, monthlyDate);
		 Assert.assertTrue("There should be 1 leave block for date " + monthlyDate.toString() + " for emplyee " + PRINCIPAL_ID_4, leaveBlockList.size()==1);
		 lb = leaveBlockList.get(0);
		 Assert.assertTrue("Leave block on date " + monthlyDate.toString() + " should have accrual_category testAC6, not " + lb.getAccrualCategory()
				 , lb.getAccrualCategory().equals("testAC6"));
		 Assert.assertTrue("Leave block on date " + monthlyDate.toString() + " should have 4 hours, not " + lb.getLeaveAmount()
				 , lb.getLeaveAmount().equals(new BigDecimal(4)));
		 
		 monthlyDate = new Date((new DateTime(2012, 8, 31, 5, 0, 0, 0, TKUtils.getSystemDateTimeZone())).getMillis());
		 leaveBlockList = TkServiceLocator.getLeaveBlockService().getLeaveBlocksForDate(PRINCIPAL_ID_4, monthlyDate);
		 Assert.assertTrue("There should NOT be any leave blocks for date " + monthlyDate.toString() + " for emplyee " + PRINCIPAL_ID_4, leaveBlockList.size()==0);
	}
	@Test
	/* testUser9 has PrincipalHRAttributes that's associated with accrual categories "testAC12"
	 * testAC12 has minimum accrual of 0.5, proration = true, fte of the ac rule is 8
	 * testUser9 has records of principalHRAttributes, the employment is from 03/10/2012 to 08/20/2012
	 * There should be partial fte accrued for testAC12 in March and August of 2012 since minimum percentage is reached
	 */
	public void testMinReachedProrationTrueFirstLastPeriod() {
		String principal_id = "testUser9";
		 Date end = new Date((new DateTime(2012, 9, 25, 0, 0, 0, 0, TKUtils.getSystemDateTimeZone())).getMillis());
		 List<LeaveBlock> leaveBlockList = (List<LeaveBlock>) TkServiceLocator.getLeaveBlockService().getLeaveBlocks(principal_id, START_DATE, end);
		 Assert.assertTrue("There are leave blocks before runAccrual for princiapl id " + principal_id, leaveBlockList.isEmpty());
		 
		 TkServiceLocator.getLeaveAccrualService().runAccrual(principal_id, START_DATE, end, false);
		 leaveBlockList = (List<LeaveBlock>) TkServiceLocator.getLeaveBlockService().getLeaveBlocks(principal_id, START_DATE, end);
		 Assert.assertTrue("There should be 6 leave blocks for emplyee " + principal_id + ", not " + leaveBlockList.size(), leaveBlockList.size()== 6);
		 
		 Date monthlyDate = new Date((new DateTime(2012, 3, 31, 5, 0, 0, 0, TKUtils.getSystemDateTimeZone())).getMillis());
		 leaveBlockList = TkServiceLocator.getLeaveBlockService().getLeaveBlocksForDate(principal_id, monthlyDate);
		 Assert.assertTrue("There should be 1 leave block for date " + monthlyDate.toString() + " for emplyee " + principal_id, leaveBlockList.size()==1);
		 LeaveBlock lb = leaveBlockList.get(0);
		 Assert.assertTrue("Leave block on date " + monthlyDate.toString() + " should have 5 hours, not " + lb.getLeaveAmount()
				 , lb.getLeaveAmount().equals(new BigDecimal(5)));
		
		 monthlyDate = new Date((new DateTime(2012, 4, 30, 5, 0, 0, 0, TKUtils.getSystemDateTimeZone())).getMillis());
		 leaveBlockList = TkServiceLocator.getLeaveBlockService().getLeaveBlocksForDate(principal_id, monthlyDate);
		 Assert.assertTrue("There should be 1 leave block for date " + monthlyDate.toString() + " for emplyee " + principal_id, leaveBlockList.size()==1);
		 lb = leaveBlockList.get(0);
		 Assert.assertTrue("Leave block on date " + monthlyDate.toString() + " should have 8 hours, not " + lb.getLeaveAmount()
				 , lb.getLeaveAmount().equals(new BigDecimal(8)));
		 
		 monthlyDate = new Date((new DateTime(2012, 8, 20, 5, 0, 0, 0, TKUtils.getSystemDateTimeZone())).getMillis());
		 leaveBlockList = TkServiceLocator.getLeaveBlockService().getLeaveBlocksForDate(principal_id, monthlyDate);
		 Assert.assertTrue("There should be 1 leave block for date " + monthlyDate.toString() + " for emplyee " + principal_id, leaveBlockList.size()==1);
		 lb = leaveBlockList.get(0);
		 Assert.assertTrue("Leave block on date " + monthlyDate.toString() + " should have 5 hours, not " + lb.getLeaveAmount()
				 , lb.getLeaveAmount().equals(new BigDecimal(5)));
		 
		 monthlyDate = new Date((new DateTime(2012, 8, 31, 5, 0, 0, 0, TKUtils.getSystemDateTimeZone())).getMillis());
		 leaveBlockList = TkServiceLocator.getLeaveBlockService().getLeaveBlocksForDate(principal_id, monthlyDate);
		 Assert.assertTrue("There should NOT be any leave blocks for date " + monthlyDate.toString() + " for emplyee " + principal_id, leaveBlockList.size()==0);
	}
	@Test
	/* testUser10 has PrincipalHRAttributes that's associated with accrual categories "testAC13"
	 * testAC13 has minimum accrual of 0.5, proration = false
	 * testUser10 has records of principalHRAttributes, the employment is from 03/10/2012 to 08/20/2012
	 * There should be whole fte accrued for testAC13 in March and August of 2012 since minimum percentage is reached
	 */
	public void testMinReachedProrationFalseFirstLastPeriod() {
		String principal_id = "testUser10";
		Date end = new Date((new DateTime(2012, 9, 25, 0, 0, 0, 0, TKUtils.getSystemDateTimeZone())).getMillis());
		List<LeaveBlock> leaveBlockList = (List<LeaveBlock>) TkServiceLocator.getLeaveBlockService().getLeaveBlocks(principal_id, START_DATE, end);
		Assert.assertTrue("There are leave blocks before runAccrual for princiapl id " + principal_id, leaveBlockList.isEmpty());
			 
		TkServiceLocator.getLeaveAccrualService().runAccrual(principal_id, START_DATE, end, false);
		leaveBlockList = (List<LeaveBlock>) TkServiceLocator.getLeaveBlockService().getLeaveBlocks(principal_id, START_DATE, end);
		Assert.assertTrue("There should be 6 leave blocks for emplyee " + principal_id + ", not " + leaveBlockList.size(), leaveBlockList.size()== 6);
			 
		Date monthlyDate = new Date((new DateTime(2012, 3, 31, 5, 0, 0, 0, TKUtils.getSystemDateTimeZone())).getMillis());
		leaveBlockList = TkServiceLocator.getLeaveBlockService().getLeaveBlocksForDate(principal_id, monthlyDate);
		Assert.assertTrue("There should be 1 leave block for date " + monthlyDate.toString() + " for emplyee " + principal_id, leaveBlockList.size()==1);
		LeaveBlock lb = leaveBlockList.get(0);
		Assert.assertTrue("Leave block on date " + monthlyDate.toString() + " should have 8 hours, not " + lb.getLeaveAmount()
			 , lb.getLeaveAmount().equals(new BigDecimal(8)));
		
		monthlyDate = new Date((new DateTime(2012, 4, 30, 5, 0, 0, 0, TKUtils.getSystemDateTimeZone())).getMillis());
		leaveBlockList = TkServiceLocator.getLeaveBlockService().getLeaveBlocksForDate(principal_id, monthlyDate);
		Assert.assertTrue("There should be 1 leave block for date " + monthlyDate.toString() + " for emplyee " + principal_id, leaveBlockList.size()==1);
		lb = leaveBlockList.get(0);
		Assert.assertTrue("Leave block on date " + monthlyDate.toString() + " should have 8 hours, not " + lb.getLeaveAmount()
			 , lb.getLeaveAmount().equals(new BigDecimal(8)));
		
		monthlyDate = new Date((new DateTime(2012, 8, 20, 5, 0, 0, 0, TKUtils.getSystemDateTimeZone())).getMillis());
		leaveBlockList = TkServiceLocator.getLeaveBlockService().getLeaveBlocksForDate(principal_id, monthlyDate);
		Assert.assertTrue("There should be 1 leave block for date " + monthlyDate.toString() + " for emplyee " + principal_id, leaveBlockList.size()==1);
		lb = leaveBlockList.get(0);
		Assert.assertTrue("Leave block on date " + monthlyDate.toString() + " should have 8 hours, not " + lb.getLeaveAmount()
				, lb.getLeaveAmount().equals(new BigDecimal(8)));
			 
		monthlyDate = new Date((new DateTime(2012, 8, 31, 5, 0, 0, 0, TKUtils.getSystemDateTimeZone())).getMillis());
		leaveBlockList = TkServiceLocator.getLeaveBlockService().getLeaveBlocksForDate(principal_id, monthlyDate);
		Assert.assertTrue("There should NOT be any leave blocks for date " + monthlyDate.toString() + " for emplyee " + principal_id, leaveBlockList.size()==0);		
	}
	
	@Test
	/* testUser11 has PrincipalHRAttributes that's associated with accrual categories "testAC14"
	 * testAC14 has minimum accrual of 0.5, proration = false
	 * testUser11 has records of principalHRAttributes, the employment is from 03/20/2012 to 08/10/2012
	 * There should NOT be accrual leave blocks for testAC14 in March and August of 2012 since minimum percentage is NOT reached
	 */
	public void testMinNotReachedProrationFalseFirstLastPeriod() {
		String principal_id = "testUser11";
		Date end = new Date((new DateTime(2012, 9, 25, 0, 0, 0, 0, TKUtils.getSystemDateTimeZone())).getMillis());
		List<LeaveBlock> leaveBlockList = (List<LeaveBlock>) TkServiceLocator.getLeaveBlockService().getLeaveBlocks(principal_id, START_DATE, end);
		Assert.assertTrue("There are leave blocks before runAccrual for princiapl id " + principal_id, leaveBlockList.isEmpty());
			 
		TkServiceLocator.getLeaveAccrualService().runAccrual(principal_id, START_DATE, end, false);
		leaveBlockList = (List<LeaveBlock>) TkServiceLocator.getLeaveBlockService().getLeaveBlocks(principal_id, START_DATE, end);
		Assert.assertTrue("There should be 4 leave blocks for emplyee " + principal_id + ", not " + leaveBlockList.size(), leaveBlockList.size()== 4);
			 
		Date monthlyDate = new Date((new DateTime(2012, 3, 31, 5, 0, 0, 0, TKUtils.getSystemDateTimeZone())).getMillis());
		leaveBlockList = TkServiceLocator.getLeaveBlockService().getLeaveBlocksForDate(principal_id, monthlyDate);
		Assert.assertTrue("There should be 0 leave block for date " + monthlyDate.toString() + " for emplyee " + principal_id, leaveBlockList.isEmpty());
		
		monthlyDate = new Date((new DateTime(2012, 4, 30, 5, 0, 0, 0, TKUtils.getSystemDateTimeZone())).getMillis());
		leaveBlockList = TkServiceLocator.getLeaveBlockService().getLeaveBlocksForDate(principal_id, monthlyDate);
		Assert.assertTrue("There should be 1 leave block for date " + monthlyDate.toString() + " for emplyee " + principal_id, leaveBlockList.size()==1);
		LeaveBlock lb = leaveBlockList.get(0);
		Assert.assertTrue("Leave block on date " + monthlyDate.toString() + " should have 8 hours, not " + lb.getLeaveAmount()
			 , lb.getLeaveAmount().equals(new BigDecimal(8)));
		
		monthlyDate = new Date((new DateTime(2012, 8, 20, 5, 0, 0, 0, TKUtils.getSystemDateTimeZone())).getMillis());
		leaveBlockList = TkServiceLocator.getLeaveBlockService().getLeaveBlocksForDate(principal_id, monthlyDate);
		Assert.assertTrue("There should be 0 leave block for date " + monthlyDate.toString() + " for emplyee " + principal_id, leaveBlockList.isEmpty());
					 
		monthlyDate = new Date((new DateTime(2012, 8, 31, 5, 0, 0, 0, TKUtils.getSystemDateTimeZone())).getMillis());
		leaveBlockList = TkServiceLocator.getLeaveBlockService().getLeaveBlocksForDate(principal_id, monthlyDate);
		Assert.assertTrue("There should NOT be any leave blocks for date " + monthlyDate.toString() + " for emplyee " + principal_id, leaveBlockList.size()==0);		
	}
	@Test
	/* testUser5's service date is 2012-03-10
	 * testUser5 has two accrual category rules, rule 1 goes from 0 month to 12 with accrual rate of 16
	 * rule 2 goes from 12 to 900 with accrual rate of 24
	 * accrual category associated with the two rules has Proration=false, and minimum percentage of 0.5
	 * run accrual for testUser5 for 18 months
	 */
	public void testMinReachedProrationFalseAndRuleChange() {
		 Calendar aCal = Calendar.getInstance();
		 aCal.setTime(START_DATE);
		 aCal.add(Calendar.MONTH, 18);
		 Date endDate = new java.sql.Date(aCal.getTime().getTime());
		 
		 List<LeaveBlock> leaveBlockList = (List<LeaveBlock>) TkServiceLocator.getLeaveBlockService().getLeaveBlocks(PRINCIPAL_ID_5, START_DATE, endDate);
		 Assert.assertTrue("There are leave blocks before runAccrual for princiapl id " + PRINCIPAL_ID_5, leaveBlockList.isEmpty());
		
		 TkServiceLocator.getLeaveAccrualService().runAccrual(PRINCIPAL_ID_5, START_DATE, endDate, false);
		 
		 leaveBlockList = TkServiceLocator.getLeaveBlockService().getLeaveBlocks(PRINCIPAL_ID_5, START_DATE, endDate);
		 Assert.assertTrue("There should be 17 leave blocks for emplyee " + PRINCIPAL_ID_5 + ", not " + leaveBlockList.size(), leaveBlockList.size()== 17);
		 
		 // 03/31/2012 is the first accrual interval date, service starts on 2012-03-10, so minimum percentage is reached for that month
		 // since proration is false, the whole accrual rate is created for the first pay period
		 Date intervalDate = new Date((new DateTime(2012, 3, 31, 5, 0, 0, 0, TKUtils.getSystemDateTimeZone())).getMillis());
		 leaveBlockList = TkServiceLocator.getLeaveBlockService().getLeaveBlocksForDate(PRINCIPAL_ID_5, intervalDate);
		 Assert.assertTrue("There should be 1 leave block for date " + intervalDate.toString() + " for emplyee " + PRINCIPAL_ID_5, leaveBlockList.size()==1);
		 LeaveBlock lb = leaveBlockList.get(0);
		 Assert.assertTrue("Leave block on date " + intervalDate.toString() + " should have 16 hours, not " + lb.getLeaveAmount()
				 , lb.getLeaveAmount().equals(new BigDecimal(16)));
		 
		 // 02/28/2013 is the 12th month of this user's employment, the accrual rate should be 16
		 intervalDate = new Date((new DateTime(2013, 2, 28, 5, 0, 0, 0, TKUtils.getSystemDateTimeZone())).getMillis());
		 leaveBlockList = TkServiceLocator.getLeaveBlockService().getLeaveBlocksForDate(PRINCIPAL_ID_5, intervalDate);
		 Assert.assertTrue("There should be 1 leave block for employee " + PRINCIPAL_ID_5 + " for date 02/28/2013.", leaveBlockList.size()==1);
		 lb = leaveBlockList.get(0);
		 Assert.assertTrue("Hours of the leave block on date " + intervalDate.toString() + " should be 16, not " + lb.getLeaveAmount().toString()
				 , lb.getLeaveAmount().equals(new BigDecimal(16)));
		 
		 // 03/31/2013 is the 13th month of this user's employment, since the minimum percentage of days is meet for
		 // that month (03/10 - 03/31) and proration = false, the accrual rate should be 24 from now on
		 intervalDate = new Date((new DateTime(2013, 3, 31, 5, 0, 0, 0, TKUtils.getSystemDateTimeZone())).getMillis());
		 leaveBlockList = TkServiceLocator.getLeaveBlockService().getLeaveBlocksForDate(PRINCIPAL_ID_5, intervalDate);
		 Assert.assertTrue("There should be 1 leave block for employee " + PRINCIPAL_ID_5 + " for date " + intervalDate.toString(), leaveBlockList.size()==1);
		 lb = leaveBlockList.get(0);
		 Assert.assertTrue("Hours of the leave block for date  " + intervalDate.toString() + " should be 24, not " + lb.getLeaveAmount().toString()
				 , lb.getLeaveAmount().equals(new BigDecimal(24)));
	}
	@Test
	/* testUser12's service date is 2012-03-20
	 * testUser12 has two accrual category rules, rule 1 goes from 6 month to 12 with accrual rate of 16
	 * rule 2 goes from 6 to 900 with accrual rate of 24
	 * accrual category "testAC15" is associated with the two rules. testAC15 has Proration=false, and minimum percentage of 0.5
	 * run accrual for testUser12 for 12 months
	 * for 2012-09-30, the accrual should still be 16 since the minimum is NOT reached for the rule 2
	 */
	public void testMinNotReachedProrationFalseAndRuleChange() {
		String principal_id = "testUser12";
		Calendar aCal = Calendar.getInstance();
		aCal.setTime(START_DATE);
		aCal.add(Calendar.MONTH, 18);
		Date endDate = new java.sql.Date(aCal.getTime().getTime());
		 
		List<LeaveBlock> leaveBlockList = (List<LeaveBlock>) TkServiceLocator.getLeaveBlockService().getLeaveBlocks(principal_id, START_DATE, endDate);
		 Assert.assertTrue("There are leave blocks before runAccrual for princiapl id " + principal_id, leaveBlockList.isEmpty());
		
		 TkServiceLocator.getLeaveAccrualService().runAccrual(principal_id, START_DATE, endDate, false);
		 
		 leaveBlockList = TkServiceLocator.getLeaveBlockService().getLeaveBlocks(principal_id, START_DATE, endDate);
		 Assert.assertTrue("There should be 17 leave blocks for emplyee " + principal_id + ", not " + leaveBlockList.size(), leaveBlockList.size()== 17);
		 // 08/31/2013 
		 Date intervalDate = new Date((new DateTime(2012, 8, 31, 5, 0, 0, 0, TKUtils.getSystemDateTimeZone())).getMillis());
		 leaveBlockList = TkServiceLocator.getLeaveBlockService().getLeaveBlocksForDate(principal_id, intervalDate);
		 Assert.assertTrue("There should be 1 leave block for employee " + principal_id + " for date " + intervalDate.toString(), leaveBlockList.size()==1);
		 LeaveBlock lb = leaveBlockList.get(0);
		 Assert.assertTrue("Hours of the leave block for date  " + intervalDate.toString() + " should be 16, not " + lb.getLeaveAmount().toString()
				 , lb.getLeaveAmount().equals(new BigDecimal(16)));		
		 
		 // 09/30/2012 is the 6th month of this user's employment, since minimum percentage is not reached, the accrual rate should be 16
		 intervalDate = new Date((new DateTime(2012, 9, 30, 5, 0, 0, 0, TKUtils.getSystemDateTimeZone())).getMillis());
		 leaveBlockList = TkServiceLocator.getLeaveBlockService().getLeaveBlocksForDate(principal_id, intervalDate);
		 Assert.assertTrue("There should be 1 leave block for date " + intervalDate.toString() + " for emplyee " + principal_id, leaveBlockList.size()==1);
		 lb = leaveBlockList.get(0);
		 Assert.assertTrue("Leave block on date " + intervalDate.toString() + " should have 16 hours, not " + lb.getLeaveAmount()
				 , lb.getLeaveAmount().equals(new BigDecimal(16)));
		 
		 // 10/31/2013 
		 intervalDate = new Date((new DateTime(2012, 10, 31, 5, 0, 0, 0, TKUtils.getSystemDateTimeZone())).getMillis());
		 leaveBlockList = TkServiceLocator.getLeaveBlockService().getLeaveBlocksForDate(principal_id, intervalDate);
		 Assert.assertTrue("There should be 1 leave block for employee " + principal_id + " for date " + intervalDate.toString(), leaveBlockList.size()==1);
		 lb = leaveBlockList.get(0);
		 Assert.assertTrue("Hours of the leave block on date " + intervalDate.toString() + " should be 24, not " + lb.getLeaveAmount().toString()
				 , lb.getLeaveAmount().equals(new BigDecimal(24)));
	}
	@Test
	/* testUser13's service date is 2012-03-10
	 * testUser13 has two accrual category rules, rule 1 goes from 0 month to 6 with accrual rate of 16
	 * rule 2 goes from 6 to 900 with accrual rate of 24
	 * accrual category "testAC16" is associated with the two rules. testAC16 has Proration=true, and minimum percentage of 0.5
	 * run accrual for testUser13 for 12 months
	 * for 2012-09-30, the accrual should be based on actual work days for those two rules, ie 22 because the minimum is reached for rule 2
	 */
	public void testMinReachedProrationTrueAndRuleChange() {
		String principal_id = "testUser13";
		Calendar aCal = Calendar.getInstance();
		aCal.setTime(START_DATE);
		aCal.add(Calendar.MONTH, 18);
		Date endDate = new java.sql.Date(aCal.getTime().getTime());
		 
		List<LeaveBlock> leaveBlockList = (List<LeaveBlock>) TkServiceLocator.getLeaveBlockService().getLeaveBlocks(principal_id, START_DATE, endDate);
		Assert.assertTrue("There are leave blocks before runAccrual for princiapl id " + principal_id, leaveBlockList.isEmpty());
		
		TkServiceLocator.getLeaveAccrualService().runAccrual(principal_id, START_DATE, endDate, false);
		 
		leaveBlockList = TkServiceLocator.getLeaveBlockService().getLeaveBlocks(principal_id, START_DATE, endDate);
		Assert.assertTrue("There should be 17 leave blocks for emplyee " + principal_id + ", not " + leaveBlockList.size(), leaveBlockList.size()== 17);
		// 08/31/2013 
		Date intervalDate = new Date((new DateTime(2012, 8, 31, 5, 0, 0, 0, TKUtils.getSystemDateTimeZone())).getMillis());
		leaveBlockList = TkServiceLocator.getLeaveBlockService().getLeaveBlocksForDate(principal_id, intervalDate);
		Assert.assertTrue("There should be 1 leave block for employee " + principal_id + " for date " + intervalDate.toString(), leaveBlockList.size()==1);
		LeaveBlock lb = leaveBlockList.get(0);
		Assert.assertTrue("Hours of the leave block for date  " + intervalDate.toString() + " should be 16, not " + lb.getLeaveAmount().toString()
				 , lb.getLeaveAmount().equals(new BigDecimal(16)));		
		 
		// 09/30/2012 is the 6th month of this user's employment, since minimum percentage is reached, the accrual rate is 16 for 6 work days
		// and 24 hrs for 14 work days, so the final accrual hrs is 22
		intervalDate = new Date((new DateTime(2012, 9, 30, 5, 0, 0, 0, TKUtils.getSystemDateTimeZone())).getMillis());
		leaveBlockList = TkServiceLocator.getLeaveBlockService().getLeaveBlocksForDate(principal_id, intervalDate);
		Assert.assertTrue("There should be 1 leave block for date " + intervalDate.toString() + " for emplyee " + principal_id, leaveBlockList.size()==1);
		lb = leaveBlockList.get(0);
		Assert.assertTrue("Leave block on date " + intervalDate.toString() + " should have 22 hours, not " + lb.getLeaveAmount()
			 , lb.getLeaveAmount().equals(new BigDecimal(22)));
		 
		// 10/31/2013 
		intervalDate = new Date((new DateTime(2012, 10, 31, 5, 0, 0, 0, TKUtils.getSystemDateTimeZone())).getMillis());
		leaveBlockList = TkServiceLocator.getLeaveBlockService().getLeaveBlocksForDate(principal_id, intervalDate);
		Assert.assertTrue("There should be 1 leave block for employee " + principal_id + " for date " + intervalDate.toString(), leaveBlockList.size()==1);
		lb = leaveBlockList.get(0);
		Assert.assertTrue("Hours of the leave block on date " + intervalDate.toString() + " should be 24, not " + lb.getLeaveAmount().toString()
			 , lb.getLeaveAmount().equals(new BigDecimal(24)));		
	}
	@Test
	/* testUser14's service date is 2012-03-20
	 * testUser14 has two accrual category rules, rule 1 goes from 0 month to 6 with accrual rate of 16
	 * rule 2 goes from 6 to 900 with accrual rate of 24
	 * accrual category "testAC17" is associated with the two rules. testAC17 has Proration=true, and minimum percentage of 0.5
	 * run accrual for testUser14 for 12 months
	 * for 2012-09-30, the accrual should be 16 since the minimum is NOT reached for the rule 2
	 */
	public void testMinNotReachedProrationTrueAndRuleChange() {
		String principal_id = "testUser14";
		Calendar aCal = Calendar.getInstance();
		aCal.setTime(START_DATE);
		aCal.add(Calendar.MONTH, 18);
		Date endDate = new java.sql.Date(aCal.getTime().getTime());
		 
		List<LeaveBlock> leaveBlockList = (List<LeaveBlock>) TkServiceLocator.getLeaveBlockService().getLeaveBlocks(principal_id, START_DATE, endDate);
		Assert.assertTrue("There are leave blocks before runAccrual for princiapl id " + principal_id, leaveBlockList.isEmpty());
		
		TkServiceLocator.getLeaveAccrualService().runAccrual(principal_id, START_DATE, endDate, false);
		// only 16 leave blocks since the first interval 03/31/2012 does not have accruals due to minimum not reached
		leaveBlockList = TkServiceLocator.getLeaveBlockService().getLeaveBlocks(principal_id, START_DATE, endDate);
		Assert.assertTrue("There should be 16 leave blocks for emplyee " + principal_id + ", not " + leaveBlockList.size(), leaveBlockList.size()== 16);
		
		// 03/31/2013 
		Date intervalDate = new Date((new DateTime(2012, 3, 31, 5, 0, 0, 0, TKUtils.getSystemDateTimeZone())).getMillis());
		leaveBlockList = TkServiceLocator.getLeaveBlockService().getLeaveBlocksForDate(principal_id, intervalDate);
		Assert.assertTrue("There should be 0 leave block for employee " + principal_id + " for date " + intervalDate.toString(), leaveBlockList.isEmpty());
			
		// 08/31/2013 
		intervalDate = new Date((new DateTime(2012, 8, 31, 5, 0, 0, 0, TKUtils.getSystemDateTimeZone())).getMillis());
		leaveBlockList = TkServiceLocator.getLeaveBlockService().getLeaveBlocksForDate(principal_id, intervalDate);
		Assert.assertTrue("There should be 1 leave block for employee " + principal_id + " for date " + intervalDate.toString(), leaveBlockList.size()==1);
		LeaveBlock lb = leaveBlockList.get(0);
		Assert.assertTrue("Hours of the leave block for date  " + intervalDate.toString() + " should be 16, not " + lb.getLeaveAmount().toString()
				 , lb.getLeaveAmount().equals(new BigDecimal(16)));		
		 
		// 09/30/2012 is the 6th month of this user's employment, since minimum percentage is NOT reached, the accrual rate should be 16
		intervalDate = new Date((new DateTime(2012, 9, 30, 5, 0, 0, 0, TKUtils.getSystemDateTimeZone())).getMillis());
		leaveBlockList = TkServiceLocator.getLeaveBlockService().getLeaveBlocksForDate(principal_id, intervalDate);
		Assert.assertTrue("There should be 1 leave block for date " + intervalDate.toString() + " for emplyee " + principal_id, leaveBlockList.size()==1);
		lb = leaveBlockList.get(0);
		Assert.assertTrue("Leave block on date " + intervalDate.toString() + " should have 16 hours, not " + lb.getLeaveAmount()
			 , lb.getLeaveAmount().equals(new BigDecimal(16)));
		 
		// 10/31/2013 
		intervalDate = new Date((new DateTime(2012, 10, 31, 5, 0, 0, 0, TKUtils.getSystemDateTimeZone())).getMillis());
		leaveBlockList = TkServiceLocator.getLeaveBlockService().getLeaveBlocksForDate(principal_id, intervalDate);
		Assert.assertTrue("There should be 1 leave block for employee " + principal_id + " for date " + intervalDate.toString(), leaveBlockList.size()==1);
		lb = leaveBlockList.get(0);
		Assert.assertTrue("Hours of the leave block on date " + intervalDate.toString() + " should be 24, not " + lb.getLeaveAmount().toString()
			 , lb.getLeaveAmount().equals(new BigDecimal(24)));			
	}
	@Test
	/* testUser6's service date is 2012-03-25
	 * testUser6 has two accrual category rules, rule 1 goes from 0 to 6 month with accrual rate of 16
	 * rule 2 goes from 6 month to 900 with accrual rate of 24
	 * accrual category testAC8 associated with the two rules has Proration=false, and minimum percentage of 0.5 and earn interval=semi_monthly
	 * run accrual for testUser5 for 10 months
	 */
	public void testMinNOTReachedProrationFalseAndRuleChange() {
		Calendar aCal = Calendar.getInstance();
		 aCal.setTime(START_DATE);	// 02/10/2012	
		 aCal.add(Calendar.MONTH, 10);	// 12/10/2012
		 Date endDate = new java.sql.Date(aCal.getTime().getTime());
		 
		 List<LeaveBlock> leaveBlockList = (List<LeaveBlock>) TkServiceLocator.getLeaveBlockService().getLeaveBlocks(PRINCIPAL_ID_6, START_DATE, endDate);
		 Assert.assertTrue("There are leave blocks before runAccrual for princiapl id " + PRINCIPAL_ID_6, leaveBlockList.isEmpty());
		
		 TkServiceLocator.getLeaveAccrualService().runAccrual(PRINCIPAL_ID_6, START_DATE, endDate, false);
		 
		 leaveBlockList = TkServiceLocator.getLeaveBlockService().getLeaveBlocks(PRINCIPAL_ID_6, START_DATE, endDate);
		 Assert.assertTrue("There should be 17 leave blocks for emplyee " + PRINCIPAL_ID_6 + ", not " + leaveBlockList.size(), leaveBlockList.size()== 17);
		 
		 // 03/31/2012 is the first accrual interval date, since minimum percentage is not reached (03/25-03/31) and proration=false
		 // there should not be leave blocks
		 Date intervalDate = new Date((new DateTime(2012, 3, 31, 5, 0, 0, 0, TKUtils.getSystemDateTimeZone())).getMillis());
		 leaveBlockList = TkServiceLocator.getLeaveBlockService().getLeaveBlocksForDate(PRINCIPAL_ID_6, intervalDate);
		 Assert.assertTrue("There should be 0 leave block for date " + intervalDate.toString(), leaveBlockList.isEmpty());
		 
		 // 04/15/2012 should have the first leave block for testUser6 and the accrual hours should be the full 16 
		 intervalDate = new Date((new DateTime(2012, 4, 15, 5, 0, 0, 0, TKUtils.getSystemDateTimeZone())).getMillis());
		 leaveBlockList = TkServiceLocator.getLeaveBlockService().getLeaveBlocksForDate(PRINCIPAL_ID_6, intervalDate);
		 Assert.assertTrue("There should be 1 leave block for date " + intervalDate.toString() + " for emplyee " + PRINCIPAL_ID_6, leaveBlockList.size()==1);
		 LeaveBlock lb = leaveBlockList.get(0);
		 Assert.assertTrue("Leave block on date " + intervalDate.toString() + " should have 16 hours, not " + lb.getLeaveAmount()
				 , lb.getLeaveAmount().equals(new BigDecimal(16)));
		 
		 // accrual rate for 09/15/2012 should still be 16
		 intervalDate = new Date((new DateTime(2012, 9, 15, 5, 0, 0, 0, TKUtils.getSystemDateTimeZone())).getMillis());
		 leaveBlockList = TkServiceLocator.getLeaveBlockService().getLeaveBlocksForDate(PRINCIPAL_ID_6, intervalDate);
		 Assert.assertTrue("There should be 1 leave block for employee " + PRINCIPAL_ID_6 + " for date " + intervalDate.toString(), leaveBlockList.size()==1);
		 lb = leaveBlockList.get(0);
		 Assert.assertTrue("Hours of the leave block on date " + intervalDate.toString() + " should be 16, not " + lb.getLeaveAmount().toString()
				 , lb.getLeaveAmount().equals(new BigDecimal(16)));
		 
		 // 09/30/2013 is the first pay interval of rule 2, since the minimum percentage is not reached (09/25-09/30)
		 // the accrual rate should still be 16
		 intervalDate = new Date((new DateTime(2012, 9, 30, 5, 0, 0, 0, TKUtils.getSystemDateTimeZone())).getMillis());
		 leaveBlockList = TkServiceLocator.getLeaveBlockService().getLeaveBlocksForDate(PRINCIPAL_ID_6, intervalDate);
		 Assert.assertTrue("There should be 1 leave block for employee " + PRINCIPAL_ID_6 + " for date " + intervalDate.toString(), leaveBlockList.size()==1);
		 lb = leaveBlockList.get(0);
		 Assert.assertTrue("Hours of the leave block for date  " + intervalDate.toString() + " should be 16, not " + lb.getLeaveAmount().toString()
				 , lb.getLeaveAmount().equals(new BigDecimal(16)));
		 
		 // 10/15/2013, should use new rate of 24
		 intervalDate = new Date((new DateTime(2012, 10, 15, 5, 0, 0, 0, TKUtils.getSystemDateTimeZone())).getMillis());
		 leaveBlockList = TkServiceLocator.getLeaveBlockService().getLeaveBlocksForDate(PRINCIPAL_ID_6, intervalDate);
		 Assert.assertTrue("There should be 1 leave block for employee " + PRINCIPAL_ID_6 + " for date " + intervalDate.toString(), leaveBlockList.size()==1);
		 lb = leaveBlockList.get(0);
		 Assert.assertTrue("Hours of the leave block for date  " + intervalDate.toString() + " should be 24, not " + lb.getLeaveAmount().toString()
				 , lb.getLeaveAmount().equals(new BigDecimal(24)));
	}
	
	@Test
	/* testUser7's service date is 2012-03-10
	 * testUser7 has two leave blocks scheduled on 04/26/2012 for 2 hours and 05/08/2012 for 5 hours, they both are NOT eligible for accrual
	 * testUser7 has one leave block scheduled on 05/22/1012 of 8 horus, it's eligible for accrual
	 * testUser7 has one leave block scheduled on 06/12/1012 of 15 horus, it's NOT eligible for accrual
	 * testUser7 has one accrual category rule of 32 hours of accrual rate, two jobs are eligible for leave with total of 40 standard hours
	 * run accrual for testUser7 for 5 months
	 */
	public void testNotEligibleForAccrualAdjustment() {
		Calendar aCal = Calendar.getInstance();
		 aCal.setTime(START_DATE);	// 02/20/2012	
		 aCal.add(Calendar.MONTH, 5);	// 7/20/2012
		 Date endDate = new java.sql.Date(aCal.getTime().getTime());
		 
		 List<LeaveBlock> leaveBlockList = (List<LeaveBlock>) TkServiceLocator.getLeaveBlockService().getLeaveBlocks(PRINCIPAL_ID_7, START_DATE, endDate);
		 Assert.assertTrue("There should be 4 leave blocks before runAccrual for princiapl id " + PRINCIPAL_ID_7, leaveBlockList.size() == 4);
		
		 TkServiceLocator.getLeaveAccrualService().runAccrual(PRINCIPAL_ID_7, START_DATE, endDate, false);
		 
		 leaveBlockList = TkServiceLocator.getLeaveBlockService().getLeaveBlocks(PRINCIPAL_ID_7, START_DATE, endDate);
		 Assert.assertTrue("There should be 10 leave blocks for emplyee " + PRINCIPAL_ID_7 + ", not " + leaveBlockList.size(), leaveBlockList.size()== 10);
		 
		 // 03/31/2012, 
		 Date intervalDate = new Date((new DateTime(2012, 3, 31, 5, 0, 0, 0, TKUtils.getSystemDateTimeZone())).getMillis());
		 leaveBlockList = TkServiceLocator.getLeaveBlockService().getLeaveBlocksForDate(PRINCIPAL_ID_7, intervalDate);
		 Assert.assertTrue("There should be 1 leave block for date " + intervalDate.toString(), leaveBlockList.size() == 1);
		 LeaveBlock lb = leaveBlockList.get(0);
		 Assert.assertTrue("Hours of the leave block for date  " + intervalDate.toString() + " should be 22, not " + lb.getLeaveAmount().toString()
				 , lb.getLeaveAmount().equals(new BigDecimal(22)));
		 
		 // 04/30/2012, 
		 intervalDate = new Date((new DateTime(2012, 4, 30, 5, 0, 0, 0, TKUtils.getSystemDateTimeZone())).getMillis());
		 leaveBlockList = TkServiceLocator.getLeaveBlockService().getLeaveBlocksForDate(PRINCIPAL_ID_7, intervalDate);
		 Assert.assertTrue("There should be 1 leave block for date " + intervalDate.toString(), leaveBlockList.size() == 1);
		 lb = leaveBlockList.get(0);
		 Assert.assertTrue("Hours of the leave block for date  " + intervalDate.toString() + " should be 32, not " + lb.getLeaveAmount().toString()
				 , lb.getLeaveAmount().equals(new BigDecimal(32)));
		 
		 //05/31/2012
		 intervalDate = new Date((new DateTime(2012, 5, 31, 5, 0, 0, 0, TKUtils.getSystemDateTimeZone())).getMillis());
		 leaveBlockList = TkServiceLocator.getLeaveBlockService().getLeaveBlocksForDate(PRINCIPAL_ID_7, intervalDate);
		 Assert.assertTrue("There should be 2 leave block for date " + intervalDate.toString(), leaveBlockList.size() == 2);
		 for(LeaveBlock aLeaveBlock : leaveBlockList) {
			 if(aLeaveBlock.getLeaveAmount().equals(new BigDecimal(-1))) {
				 Assert.assertTrue("Accrual category of the leave block for date  " + intervalDate.toString() + " should be 'testAC9' , not " + aLeaveBlock.getAccrualCategory()
						 , aLeaveBlock.getAccrualCategory().equals("testAC9")); 
			 } else if(aLeaveBlock.getLeaveAmount().equals(new BigDecimal(32))) {
				 Assert.assertTrue("Accrual category of the leave block for date  " + intervalDate.toString() + " should be 'testAC9' , not " + aLeaveBlock.getAccrualCategory()
						 , aLeaveBlock.getAccrualCategory().equals("testAC9")); 
			 } else {
				 Assert.fail("Hours of the leave block for date  " + intervalDate.toString() + " should be either 32 or -1, not " + aLeaveBlock.getLeaveAmount().toString());
			 }
		 }
		 
		 //06/30/2012
		 intervalDate = new Date((new DateTime(2012, 6, 30, 5, 0, 0, 0, TKUtils.getSystemDateTimeZone())).getMillis());
		 leaveBlockList = TkServiceLocator.getLeaveBlockService().getLeaveBlocksForDate(PRINCIPAL_ID_7, intervalDate);
		 Assert.assertTrue("There should be 2 leave block for date " + intervalDate.toString(), leaveBlockList.size() == 2);
		 for(LeaveBlock aLeaveBlock : leaveBlockList) {
			 if(aLeaveBlock.getLeaveAmount().equals(new BigDecimal(-3))) {
				 Assert.assertTrue("Accrual category of the leave block for date  " + intervalDate.toString() + " should be 'testAC9' , not " + lb.getAccrualCategory()
						 , lb.getAccrualCategory().equals("testAC9")); 
			 } else if(aLeaveBlock.getLeaveAmount().equals(new BigDecimal(32))) {
				 Assert.assertTrue("Accrual category of the leave block for date  " + intervalDate.toString() + " should be 'testAC9' , not " + lb.getAccrualCategory()
						 , lb.getAccrualCategory().equals("testAC9")); 
			 } else {
				 Assert.fail("Hours of the leave block for date  " + intervalDate.toString() + " should be either 32 or -3, not " + lb.getLeaveAmount().toString());
			 }
		 }
	}
	
	@Test
	/* testUser8's service date is 2012-03-10
	 * testUser8 has one accrual category, two entries of the same accrual category. 
	 * The first entry has effectiveDate = 2012-03-01. The rule associated with it has 16 as the accrual rate
	 * the second entry has effectiveDate = 2012-5-01. The rule associated with it has 32 as the accrual rate
	 * run accrual for testUser8 for 6 months
	 */
	public void testAccrualCategoryChanges() {
		Calendar aCal = Calendar.getInstance();
		 aCal.setTime(START_DATE);	// 02/20/2012	
		 aCal.add(Calendar.MONTH, 6);	// 8/20/2012
		 Date endDate = new java.sql.Date(aCal.getTime().getTime());
		 
		 List<LeaveBlock> leaveBlockList = (List<LeaveBlock>) TkServiceLocator.getLeaveBlockService().getLeaveBlocks(PRINCIPAL_ID_8, START_DATE, endDate);
		 Assert.assertTrue("There are leave blocks before runAccrual for princiapl id " + PRINCIPAL_ID_8, leaveBlockList.isEmpty());
		
		 TkServiceLocator.getLeaveAccrualService().runAccrual(PRINCIPAL_ID_8, START_DATE, endDate, false);
		 
		 leaveBlockList = TkServiceLocator.getLeaveBlockService().getLeaveBlocks(PRINCIPAL_ID_8, START_DATE, endDate);
		 Assert.assertTrue("There should be 5 leave blocks for emplyee " + PRINCIPAL_ID_8 + ", not " + leaveBlockList.size(), leaveBlockList.size()== 5);		 
		 
		 // 03/31/2012, 
		 Date intervalDate = new Date((new DateTime(2012, 3, 31, 5, 0, 0, 0, TKUtils.getSystemDateTimeZone())).getMillis());
		 leaveBlockList = TkServiceLocator.getLeaveBlockService().getLeaveBlocksForDate(PRINCIPAL_ID_8, intervalDate);
		 Assert.assertTrue("There should be 1 leave block for date " + intervalDate.toString(), leaveBlockList.size() == 1);
		 LeaveBlock lb = leaveBlockList.get(0);
		 Assert.assertTrue("Hours of the leave block for date  " + intervalDate.toString() + " should be 11, not " + lb.getLeaveAmount().toString()
				 , lb.getLeaveAmount().equals(new BigDecimal(11)));
		 
		 // 04/30/2012, 
		 intervalDate = new Date((new DateTime(2012, 4, 30, 5, 0, 0, 0, TKUtils.getSystemDateTimeZone())).getMillis());
		 leaveBlockList = TkServiceLocator.getLeaveBlockService().getLeaveBlocksForDate(PRINCIPAL_ID_8, intervalDate);
		 Assert.assertTrue("There should be 1 leave block for date " + intervalDate.toString(), leaveBlockList.size() == 1);
		 lb = leaveBlockList.get(0);
		 Assert.assertTrue("Hours of the leave block for date  " + intervalDate.toString() + " should be 16, not " + lb.getLeaveAmount().toString()
				 , lb.getLeaveAmount().equals(new BigDecimal(16)));
		 
		 //05/31/2012
		 intervalDate = new Date((new DateTime(2012, 5, 31, 5, 0, 0, 0, TKUtils.getSystemDateTimeZone())).getMillis());
		 leaveBlockList = TkServiceLocator.getLeaveBlockService().getLeaveBlocksForDate(PRINCIPAL_ID_8, intervalDate);
		 Assert.assertTrue("There should be 1 leave block for date " + intervalDate.toString(), leaveBlockList.size() == 1);
		 lb = leaveBlockList.get(0);
		 Assert.assertTrue("Hours of the leave block for date  " + intervalDate.toString() + " should be 32, not " + lb.getLeaveAmount().toString()
				 , lb.getLeaveAmount().equals(new BigDecimal(32)));
		 
	}
	
	@Test
	/*	testUser15's service Date is 2012-03-10
	 * 	testUser15 has one accrual category, the effectiveDate of the AC is 2012-03-01
	 *  The rule associated with the AC has 24 as the accrual rate
	 *  There's Leave Calendar document for calendar entry 2012-04-01 -- 2012-05-01
	 *  run accrual for testUser15 for 6 months
	 */
	public void testLeaveBlocksWithLeaveCalendarDocId() {
		String principal_id = "testUser15";
		Calendar aCal = Calendar.getInstance();
		aCal.setTime(START_DATE);
		aCal.add(Calendar.MONTH, 6);
		Date endDate = new java.sql.Date(aCal.getTime().getTime());
		 
		List<LeaveBlock> leaveBlockList = (List<LeaveBlock>) TkServiceLocator.getLeaveBlockService().getLeaveBlocks(principal_id, START_DATE, endDate);
		Assert.assertTrue("There are leave blocks before runAccrual for princiapl id " + principal_id, leaveBlockList.isEmpty());
		
		TkServiceLocator.getLeaveAccrualService().runAccrual(principal_id, START_DATE, endDate, false);
		
		// 04/30/2012 
		Date intervalDate = new Date((new DateTime(2012, 4, 30, 5, 0, 0, 0, TKUtils.getSystemDateTimeZone())).getMillis());
		leaveBlockList = TkServiceLocator.getLeaveBlockService().getLeaveBlocksForDate(principal_id, intervalDate);
		Assert.assertTrue("There should be 1 leave block for employee " + principal_id + " for date " + intervalDate.toString(), leaveBlockList.size()==1);
		LeaveBlock lb = leaveBlockList.get(0);
		Assert.assertTrue("DocumentId of the leave block for date  " + intervalDate.toString() + " should be 5000, not " + lb.getDocumentId()
				 , lb.getDocumentId().equals("5000"));		
		 
	}
	
	@Test
	/*	testUser16's service Date is 2012-03-26 which is a Monday
	 * 	testUser16 has one accrual category "testAC19" with effectiveDate of 2012-03-01
	 *  testAC19 has proration = false, minimum percentage = 0
	 *  testAC19 has "pay calendar" as the earn interval, so the accrual interval will be based on the pay calendar entries of testUser16
	 *  testUser16 has "BI-WE" as the pay calendar, it's bi-weekly with start day as Sunday
	 *  The rule associated with the AC has 24 as the accrual rate
	 *  run accrual for testUser16 for 6 months
	 */
	public void testPayCalAsEarnInterval() {
		String principal_id = "testUser16";
		Calendar aCal = Calendar.getInstance();
		aCal.setTime(START_DATE);
		aCal.add(Calendar.MONTH, 6);
		Date endDate = new java.sql.Date(aCal.getTime().getTime());
		 
		List<LeaveBlock> leaveBlockList = (List<LeaveBlock>) TkServiceLocator.getLeaveBlockService().getLeaveBlocks(principal_id, START_DATE, endDate);
		Assert.assertTrue("There are leave blocks before runAccrual for princiapl id " + principal_id, leaveBlockList.isEmpty());
		
		TkServiceLocator.getLeaveAccrualService().runAccrual(principal_id, START_DATE, endDate, false);
		
		leaveBlockList = TkServiceLocator.getLeaveBlockService().getLeaveBlocks(principal_id, START_DATE, endDate);
		Assert.assertTrue("There should be 11 leave blocks for emplyee " + principal_id + ", not " + leaveBlockList.size(), leaveBlockList.size()== 11);	
		
		// 03/31/2012, testAC19 has proration= false, minimum percentage = 0, so whole FTE of 24 hours is given to the first interval
		Date intervalDate = new Date((new DateTime(2012, 3, 31, 5, 0, 0, 0, TKUtils.getSystemDateTimeZone())).getMillis());
		leaveBlockList = TkServiceLocator.getLeaveBlockService().getLeaveBlocksForDate(principal_id, intervalDate);
		Assert.assertTrue("There should be 1 leave block for employee " + principal_id + " for date " + intervalDate.toString(), leaveBlockList.size()==1);
		LeaveBlock lb = leaveBlockList.get(0);		
		Assert.assertTrue("Hours of the leave block for date  " + intervalDate.toString() + " should be 24, not " + lb.getLeaveAmount().toString()
				 , lb.getLeaveAmount().equals(new BigDecimal(24)));
		
		// 04/28/2012
		intervalDate = new Date((new DateTime(2012, 4, 28, 5, 0, 0, 0, TKUtils.getSystemDateTimeZone())).getMillis());
		leaveBlockList = TkServiceLocator.getLeaveBlockService().getLeaveBlocksForDate(principal_id, intervalDate);
		Assert.assertTrue("There should be 1 leave block for employee " + principal_id + " for date " + intervalDate.toString(), leaveBlockList.size()==1);
		lb = leaveBlockList.get(0);		
		Assert.assertTrue("Hours of the leave block for date  " + intervalDate.toString() + " should be 24, not " + lb.getLeaveAmount().toString()
				 , lb.getLeaveAmount().equals(new BigDecimal(24)));
		// 05/12/2012
		intervalDate = new Date((new DateTime(2012, 5, 12, 5, 0, 0, 0, TKUtils.getSystemDateTimeZone())).getMillis());
		leaveBlockList = TkServiceLocator.getLeaveBlockService().getLeaveBlocksForDate(principal_id, intervalDate);
		Assert.assertTrue("There should be 1 leave block for employee " + principal_id + " for date " + intervalDate.toString(), leaveBlockList.size()==1);
		// 08/04/2012
		intervalDate = new Date((new DateTime(2012, 8, 4, 5, 0, 0, 0, TKUtils.getSystemDateTimeZone())).getMillis());
		leaveBlockList = TkServiceLocator.getLeaveBlockService().getLeaveBlocksForDate(principal_id, intervalDate);
		Assert.assertTrue("There should be 1 leave block for employee " + principal_id + " for date " + intervalDate.toString(), leaveBlockList.size()==1);
		// 08/18/2012
		intervalDate = new Date((new DateTime(2012, 8, 18, 5, 0, 0, 0, TKUtils.getSystemDateTimeZone())).getMillis());
		leaveBlockList = TkServiceLocator.getLeaveBlockService().getLeaveBlocksForDate(principal_id, intervalDate);
		Assert.assertTrue("There should be 1 leave block for employee " + principal_id + " for date " + intervalDate.toString(), leaveBlockList.size()==1);
	}
	
	@Test
	/*	testUser17's service Date is 2012-03-26 which is a Monday
	 * 	testUser17 has one accrual category "testAC20" with effectiveDate of 2012-03-01
	 *  testAC20 has proration = true, minimum percentage = 0.5
	 *  testAC20 has "pay calendar" as the earn interval, so the accrual interval will be based on the pay calendar entries of testUser17
	 *  testUser17 has "BI-WE" as the pay calendar, it's bi-weekly with start day as Sunday
	 *  The rule associated with the AC has 24 as the accrual rate
	 *  run accrual for testUser17 for 6 months
	 */
	public void testPayCalAsEarnIntervalProrationFalseMinReached() {
		String principal_id = "testUser17";
		Calendar aCal = Calendar.getInstance();
		aCal.setTime(START_DATE);
		aCal.add(Calendar.MONTH, 6);
		Date endDate = new java.sql.Date(aCal.getTime().getTime());
		 
		List<LeaveBlock> leaveBlockList = (List<LeaveBlock>) TkServiceLocator.getLeaveBlockService().getLeaveBlocks(principal_id, START_DATE, endDate);
		Assert.assertTrue("There are leave blocks before runAccrual for princiapl id " + principal_id, leaveBlockList.isEmpty());
		
		TkServiceLocator.getLeaveAccrualService().runAccrual(principal_id, START_DATE, endDate, false);
		
		leaveBlockList = TkServiceLocator.getLeaveBlockService().getLeaveBlocks(principal_id, START_DATE, endDate);
		Assert.assertTrue("There should be 11 leave blocks for emplyee " + principal_id + ", not " + leaveBlockList.size(), leaveBlockList.size()== 11);	
		
		// 03/31/2012, testAC20 has proration= true, minimum percentage = 0.5, so only 12 hours is given to the first interval
		Date intervalDate = new Date((new DateTime(2012, 3, 31, 5, 0, 0, 0, TKUtils.getSystemDateTimeZone())).getMillis());
		leaveBlockList = TkServiceLocator.getLeaveBlockService().getLeaveBlocksForDate(principal_id, intervalDate);
		Assert.assertTrue("There should be 1 leave block for employee " + principal_id + " for date " + intervalDate.toString(), leaveBlockList.size()==1);
		LeaveBlock lb = leaveBlockList.get(0);		
		Assert.assertTrue("Hours of the leave block for date  " + intervalDate.toString() + " should be 12, not " + lb.getLeaveAmount().toString()
				 , lb.getLeaveAmount().equals(new BigDecimal(12)));
		
		// 04/28/2012
		intervalDate = new Date((new DateTime(2012, 4, 28, 5, 0, 0, 0, TKUtils.getSystemDateTimeZone())).getMillis());
		leaveBlockList = TkServiceLocator.getLeaveBlockService().getLeaveBlocksForDate(principal_id, intervalDate);
		Assert.assertTrue("There should be 1 leave block for employee " + principal_id + " for date " + intervalDate.toString(), leaveBlockList.size()==1);
		lb = leaveBlockList.get(0);		
		Assert.assertTrue("Hours of the leave block for date  " + intervalDate.toString() + " should be 24, not " + lb.getLeaveAmount().toString()
				 , lb.getLeaveAmount().equals(new BigDecimal(24)));
		// 05/12/2012
		intervalDate = new Date((new DateTime(2012, 5, 12, 5, 0, 0, 0, TKUtils.getSystemDateTimeZone())).getMillis());
		leaveBlockList = TkServiceLocator.getLeaveBlockService().getLeaveBlocksForDate(principal_id, intervalDate);
		Assert.assertTrue("There should be 1 leave block for employee " + principal_id + " for date " + intervalDate.toString(), leaveBlockList.size()==1);
		// 08/04/2012
		intervalDate = new Date((new DateTime(2012, 8, 4, 5, 0, 0, 0, TKUtils.getSystemDateTimeZone())).getMillis());
		leaveBlockList = TkServiceLocator.getLeaveBlockService().getLeaveBlocksForDate(principal_id, intervalDate);
		Assert.assertTrue("There should be 1 leave block for employee " + principal_id + " for date " + intervalDate.toString(), leaveBlockList.size()==1);
		// 08/18/2012
		intervalDate = new Date((new DateTime(2012, 8, 18, 5, 0, 0, 0, TKUtils.getSystemDateTimeZone())).getMillis());
		leaveBlockList = TkServiceLocator.getLeaveBlockService().getLeaveBlocksForDate(principal_id, intervalDate);
		Assert.assertTrue("There should be 1 leave block for employee " + principal_id + " for date " + intervalDate.toString(), leaveBlockList.size()==1);
	}

}
