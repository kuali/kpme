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
package org.kuali.hr.lm.accrual.service;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.joda.time.Interval;
import org.kuali.hr.lm.LMConstants;
import org.kuali.hr.lm.accrual.AccrualCategory;
import org.kuali.hr.lm.accrual.AccrualCategoryRule;
import org.kuali.hr.lm.employeeoverride.EmployeeOverride;
import org.kuali.hr.lm.leaveblock.LeaveBlock;
import org.kuali.hr.time.calendar.Calendar;
import org.kuali.hr.time.calendar.CalendarEntries;
import org.kuali.hr.time.principal.PrincipalHRAttributes;
import org.kuali.hr.time.service.base.TkServiceLocator;
import org.kuali.hr.time.util.TKUtils;
import org.kuali.rice.krad.util.ObjectUtils;

import edu.emory.mathcs.backport.java.util.Collections;

public class AccrualCategoryMaxBalanceServiceImpl implements AccrualCategoryMaxBalanceService {

	@Override
	public Map<String, Set<LeaveBlock>> getMaxBalanceViolations(CalendarEntries entry, String principalId) {

		Map<String, Set<LeaveBlock>> maxBalanceViolations = new HashMap<String,Set<LeaveBlock>>();
		
		Map<String, Set<LeaveBlock>> eligibilities = new HashMap<String, Set<LeaveBlock>>();
		
		eligibilities.put(LMConstants.MAX_BAL_ACTION_FREQ.LEAVE_APPROVE, new HashSet<LeaveBlock>());
		eligibilities.put(LMConstants.MAX_BAL_ACTION_FREQ.YEAR_END, new HashSet<LeaveBlock>());
		eligibilities.put(LMConstants.MAX_BAL_ACTION_FREQ.ON_DEMAND, new HashSet<LeaveBlock>());
		
		Interval thisEntryInterval = new Interval(entry.getBeginPeriodDate().getTime(),entry.getEndPeriodDate().getTime());

		Date asOfDate = TKUtils.getCurrentDate();
		
		if(!thisEntryInterval.contains(asOfDate.getTime()))
			asOfDate = new Date(DateUtils.addDays(entry.getEndPeriodDate(),-1).getTime());

		PrincipalHRAttributes pha = TkServiceLocator.getPrincipalHRAttributeService().getPrincipalCalendar(principalId, asOfDate);
		
		if(pha == null)
			return eligibilities;
		
		Calendar cal = pha.getLeaveCalObj();
		
		if(cal == null)
			return eligibilities;
		
		List<CalendarEntries> leaveCalEntries = TkServiceLocator.getCalendarEntriesService().getCalendarEntriesEndingBetweenBeginAndEndDate(cal.getHrCalendarId(), entry.getBeginPeriodDate(), entry.getEndPeriodDate());
		CalendarEntries leaveLeaveEntry = null;
		if(!leaveCalEntries.isEmpty()) {
			for(CalendarEntries leaveEntry : leaveCalEntries) {
				if(StringUtils.equals(cal.getCalendarName(), leaveEntry.getCalendarName())) {
					if(leaveEntry.getEndPeriodDate().compareTo(entry.getBeginPeriodDate()) > 0)
						leaveLeaveEntry = leaveEntry;
				}
			}
		}
		
		Interval leavePeriodInterval = null;
		Interval yearEndPeriodInterval = null;
		if(leaveLeaveEntry != null) {
			leavePeriodInterval = new Interval(entry.getBeginPeriodDate().getTime(),leaveLeaveEntry.getEndPeriodDate().getTime());
			if(TkServiceLocator.getLeavePlanService().isLastCalendarPeriodOfLeavePlan(leaveLeaveEntry, pha.getLeavePlan(), asOfDate))
				yearEndPeriodInterval = leavePeriodInterval;
		}
		
		List<AccrualCategory> accrualCategories = TkServiceLocator.getAccrualCategoryService().getActiveAccrualCategoriesForLeavePlan(pha.getLeavePlan(), asOfDate);

		if(!accrualCategories.isEmpty()) {
			
			List<LeaveBlock> leaveBlocks = new ArrayList<LeaveBlock>();
			Map<String, BigDecimal> accruedBalance = new HashMap<String, BigDecimal>();

			for(AccrualCategory accrualCategory : accrualCategories) {
				leaveBlocks.addAll(TkServiceLocator.getLeaveBlockService().getLeaveBlocksWithAccrualCategory(principalId, pha.getServiceDate(), DateUtils.addDays(asOfDate,1), accrualCategory.getAccrualCategory()));
				accruedBalance.put(accrualCategory.getLmAccrualCategoryId(), BigDecimal.ZERO);
/*	Un-comment to consider service interval end-point changes. i.e. when defining a new action frequency - "ON_SERVICE_MILESTONE"
 * 
 * 				List<AccrualCategoryRule> accrualRules = TkServiceLocator.getAccrualCategoryRuleService().getActiveAccrualCategoryRules(accrualCategory.getLmAccrualCategoryId());
				for(AccrualCategoryRule rule : accrualRules) {
					String serviceUnits = rule.getServiceUnitOfTime();
					Date rollOverDate = null;
					//TODO: Accrual Category Rules' start and end field allow only whole integer values. This should be reflected in storage.
					if(StringUtils.equals(serviceUnits, "M")) {
						rollOverDate = new java.sql.Date(DateUtils.addMonths(pha.getServiceDate(), (new BigDecimal(rule.getEnd()).intValue())).getTime());
					}
					else if(StringUtils.equals(serviceUnits, "Y")) {
						rollOverDate = new java.sql.Date(DateUtils.addYears(pha.getServiceDate(), (new BigDecimal(rule.getEnd()).intValue())).getTime());
					}
					if(ObjectUtils.isNotNull(rollOverDate)) {
						if(thisEntryInterval.contains(DateUtils.addDays(rollOverDate,-1).getTime())) {
							//Add a max balance allocation leave block.
							LeaveBlock allocation = new LeaveBlock();
							allocation.setAccrualCategory(accrualCategory.getAccrualCategory());
							allocation.setLeaveDate(new java.sql.Date(DateUtils.addDays(rollOverDate,-1).getTime()));
							allocation.setLeaveAmount(BigDecimal.ZERO);
							allocation.setPrincipalId(principalId);
							leaveBlocks.add(allocation);
						}
					}
				}
				*/

				//Add a max balance allocation leave block.
				LeaveBlock allocation = new LeaveBlock();
				allocation.setAccrualCategory(accrualCategory.getAccrualCategory());
				
				if(thisEntryInterval.contains(TKUtils.getCurrentDate().getTime()))
					allocation.setLeaveDate(TKUtils.getCurrentDate());
				else
					allocation.setLeaveDate(new java.sql.Date(DateUtils.addDays(entry.getEndPeriodDate(),-1).getTime()));
				
				allocation.setAccrualCategoryObj(accrualCategory);
				allocation.setLeaveAmount(BigDecimal.ZERO);
				allocation.setPrincipalId(principalId);
				allocation.setLeaveBlockType("allocation");
				leaveBlocks.add(allocation);
				
				if(ObjectUtils.isNotNull(leaveLeaveEntry)) {
						//if entry belongs to a time calendar, check the balances at the end date for the calendar year that ends
						//within entry's interval.
						//if entry belongs to a leave calendar, this empty block will simply duplicate and override
						//the block created above.
						allocation = new LeaveBlock();
						allocation.setAccrualCategory(accrualCategory.getAccrualCategory());
						allocation.setLeaveDate(new java.sql.Date(DateUtils.addDays(leaveLeaveEntry.getEndPeriodDate(),-1).getTime()));
						allocation.setAccrualCategoryObj(accrualCategory);
						allocation.setLeaveAmount(BigDecimal.ZERO);
						allocation.setPrincipalId(principalId);
						allocation.setLeaveBlockType("allocation");
						leaveBlocks.add(allocation);
				}
				
				if(!leaveBlocks.isEmpty()) {
					Collections.sort(leaveBlocks, new Comparator() {
						
						@Override
						public int compare(Object o1, Object o2) {
							LeaveBlock l1 = (LeaveBlock) o1;
							LeaveBlock l2 = (LeaveBlock) o2;
							return l1.getLeaveDate().compareTo(l2.getLeaveDate());
						}
						
					});
				}
			}
			
			for(LeaveBlock lb : leaveBlocks) {
				if(StringUtils.equals(lb.getRequestStatus(),LMConstants.REQUEST_STATUS.DISAPPROVED) || StringUtils.equals(lb.getRequestStatus(),LMConstants.REQUEST_STATUS.DEFERRED))
					continue;
				AccrualCategory accrualCategory = TkServiceLocator.getAccrualCategoryService().getAccrualCategory(lb.getAccrualCategory(), lb.getLeaveDate());
				BigDecimal tally = accruedBalance.get(accrualCategory.getLmAccrualCategoryId());
				tally = tally.add(lb.getLeaveAmount());
				
				AccrualCategoryRule asOfLeaveDateRule = TkServiceLocator.getAccrualCategoryRuleService().getAccrualCategoryRuleForDate(accrualCategory, lb.getLeaveDate(), pha.getServiceDate());

				//Employee overrides...
				if(ObjectUtils.isNotNull(asOfLeaveDateRule)) {
					if(StringUtils.equals(asOfLeaveDateRule.getMaxBalFlag(),"Y")) {
						if(StringUtils.isNotBlank(asOfLeaveDateRule.getActionAtMaxBalance())) {
							if(ObjectUtils.isNotNull(asOfLeaveDateRule.getMaxBalanceActionFrequency())) {
								
								if(maxBalanceViolations.get(asOfLeaveDateRule.getLmAccrualCategoryId()) == null)
									maxBalanceViolations.put(asOfLeaveDateRule.getLmAccrualCategoryId(), new HashSet<LeaveBlock>());
								
								BigDecimal maxBalance = asOfLeaveDateRule.getMaxBalance();

								BigDecimal fte = TkServiceLocator.getJobService().getFteSumForAllActiveLeaveEligibleJobs(principalId, TKUtils.getCurrentDate());
								BigDecimal adjustedMaxBalance = maxBalance.multiply(fte);

								BigDecimal maxAnnualCarryOver = null;
								if(ObjectUtils.isNotNull(asOfLeaveDateRule.getMaxCarryOver()))
									maxAnnualCarryOver = new BigDecimal(asOfLeaveDateRule.getMaxCarryOver());

								BigDecimal adjustedMaxAnnualCarryOver = null;
								if(ObjectUtils.isNotNull(maxAnnualCarryOver)) {
									adjustedMaxAnnualCarryOver = maxAnnualCarryOver.multiply(fte);
								}

								List<EmployeeOverride> overrides = TkServiceLocator.getEmployeeOverrideService().getEmployeeOverrides(principalId, lb.getLeaveDate());
								for(EmployeeOverride override : overrides) {
									if(StringUtils.equals(override.getAccrualCategory(),lb.getAccrualCategory())) {
										//Do not pro-rate override values for FTE.
										if(StringUtils.equals(override.getOverrideType(),"MB"))
											adjustedMaxBalance = new BigDecimal(override.getOverrideValue());
										if(StringUtils.equals(override.getOverrideType(),"MAC"))
											adjustedMaxAnnualCarryOver = new BigDecimal(override.getOverrideValue());
									}
								}

								boolean yearEndCandidate = false;
								if(StringUtils.equals(asOfLeaveDateRule.getMaxBalanceActionFrequency(), LMConstants.MAX_BAL_ACTION_FREQ.YEAR_END)
										&& adjustedMaxAnnualCarryOver != null) {
									yearEndCandidate = true;
								}
								//callers must determine if the violation exists on the calendar prior to the leave plan's calendar year end date.
								if(tally.compareTo(adjustedMaxBalance) > 0
										|| (yearEndCandidate && tally.compareTo(adjustedMaxAnnualCarryOver) > 0)) {
									if(maxBalanceViolations.get(asOfLeaveDateRule.getLmAccrualCategoryId()).isEmpty()) {
										maxBalanceViolations.get(asOfLeaveDateRule.getLmAccrualCategoryId()).add(lb);
									}
									else {
										Set<LeaveBlock> eligibleLeaveBlocks = maxBalanceViolations.get(asOfLeaveDateRule.getLmAccrualCategoryId());
										LeaveBlock previousInfraction = retreivePreviousInfraction(eligibleLeaveBlocks,lb,leavePeriodInterval,yearEndPeriodInterval,thisEntryInterval,asOfLeaveDateRule);

										if(previousInfraction != null)
											eligibleLeaveBlocks.remove(previousInfraction);
											/* Replace the leave block with the most recent infraction that occurred so long as the infraction takes place
											 * before the end of the [underlying] leave period. This includes accrual categories that undergo a rule change
											 * as the result of the employee crossing a service milestone.
											 * Doing so will correctly trigger the action on timesheets that stagger a leave
											 * period end date, since the block would exist on a leave period whose end date is not contained within
											 * the time period. For on-demand frequencies, we want to replace the block with the latest no matter what.
											 * Doing so will ensure that the latest rule is used to initiate the action.
											 * 
											 */
										eligibleLeaveBlocks.add(lb);
										maxBalanceViolations.put(asOfLeaveDateRule.getLmAccrualCategoryId(), eligibleLeaveBlocks);
									}
								}
								else	// the leave amount (usage), as of lb's leave date, is under the balance limit.
									if(!maxBalanceViolations.get(asOfLeaveDateRule.getLmAccrualCategoryId()).isEmpty()) {
										//if there was a previous infraction, it must be removed so long as the leave date of lb lies within
										//the same period as the previous infraction.
										Set<LeaveBlock> eligibleLeaveBlocks = maxBalanceViolations.get(asOfLeaveDateRule.getLmAccrualCategoryId());
										LeaveBlock previousInfraction = retreivePreviousInfraction(eligibleLeaveBlocks,lb,leavePeriodInterval,yearEndPeriodInterval,thisEntryInterval,asOfLeaveDateRule);
										if(previousInfraction != null) {
											eligibleLeaveBlocks.remove(previousInfraction);
											maxBalanceViolations.put(asOfLeaveDateRule.getLmAccrualCategoryId(), eligibleLeaveBlocks);
										}
								}
							}
						}
					}
				}
				accruedBalance.put(accrualCategory.getLmAccrualCategoryId(), tally);
			}
		}
		
		for(Entry<String,Set<LeaveBlock>> entries : maxBalanceViolations.entrySet()) {
			for(LeaveBlock lb : entries.getValue()) {
				AccrualCategoryRule aRule = TkServiceLocator.getAccrualCategoryRuleService().getAccrualCategoryRule(lb.getAccrualCategoryRuleId());
				eligibilities.get(aRule.getMaxBalanceActionFrequency()).add(lb);
			}
		}
		
		return eligibilities;
	}
	
	protected LeaveBlock retreivePreviousInfraction(Set<LeaveBlock> eligibleLeaveBlocks, LeaveBlock lb, Interval leavePeriodInterval, Interval yearEndPeriodInterval, Interval thisEntryInterval, AccrualCategoryRule asOfLeaveDateRule) {
		LeaveBlock tempLB = null;
		for(LeaveBlock block : eligibleLeaveBlocks) {
			AccrualCategoryRule blockRule = TkServiceLocator.getAccrualCategoryRuleService().getAccrualCategoryRule(block.getAccrualCategoryRuleId());
			if(StringUtils.equals(asOfLeaveDateRule.getLmAccrualCategoryRuleId(),blockRule.getLmAccrualCategoryRuleId())) {
				if((StringUtils.equals(asOfLeaveDateRule.getMaxBalanceActionFrequency(),LMConstants.MAX_BAL_ACTION_FREQ.ON_DEMAND)
						&& StringUtils.equals(blockRule.getMaxBalanceActionFrequency(),LMConstants.MAX_BAL_ACTION_FREQ.ON_DEMAND))
						|| ( leavePeriodInterval != null
							&& leavePeriodInterval.contains(lb.getLeaveDate().getTime())
							&& leavePeriodInterval.contains(block.getLeaveDate().getTime()))
						|| (leavePeriodInterval == null
							&& thisEntryInterval.contains(block.getLeaveDate().getTime())
							&& thisEntryInterval.contains(lb.getLeaveDate().getTime()))
						|| (StringUtils.equals(asOfLeaveDateRule.getMaxBalanceActionFrequency(),LMConstants.MAX_BAL_ACTION_FREQ.YEAR_END)
							&& StringUtils.equals(blockRule.getMaxBalanceActionFrequency(),LMConstants.MAX_BAL_ACTION_FREQ.YEAR_END)
							&& (yearEndPeriodInterval == null ||
								(yearEndPeriodInterval.contains(block.getLeaveDate().getTime())
										&& yearEndPeriodInterval.contains(lb.getLeaveDate().getTime())))
							//year end we replace if the two infractions are not within the same leave period.
							//if yearEndPeriodInterval != null, the conditional for leave approve should have already evaluated to true.
							//i.e. yearEndPeriodInterval != null will never be evaluated.
						|| block.getLeaveDate().before(thisEntryInterval.getStart().toDate()))) {
					tempLB = block;
					break;
				}
			}
			else
				if(StringUtils.equals(blockRule.getMaxBalanceActionFrequency(),LMConstants.MAX_BAL_ACTION_FREQ.ON_DEMAND)
						//always supersede on-demand action frequencies
						|| (StringUtils.equals(blockRule.getMaxBalanceActionFrequency(),LMConstants.MAX_BAL_ACTION_FREQ.LEAVE_APPROVE)
								&& ((leavePeriodInterval != null
										&& leavePeriodInterval.contains(block.getLeaveDate().getTime())
										&& leavePeriodInterval.contains(lb.getLeaveDate().getTime()))
									|| (leavePeriodInterval == null
										&& thisEntryInterval.contains(block.getLeaveDate().getTime())
										&& thisEntryInterval.contains(lb.getLeaveDate().getTime()))))
						//leave approve is replaced only if the replacement lies within the same leave period.
						|| (StringUtils.equals(blockRule.getMaxBalanceActionFrequency(), LMConstants.MAX_BAL_ACTION_FREQ.YEAR_END)
								&& (yearEndPeriodInterval == null
									|| (yearEndPeriodInterval.contains(block.getLeaveDate().getTime())
										&& yearEndPeriodInterval.contains(lb.getLeaveDate().getTime()))))
						//year end is superseded only if the new rule goes into effect before the current leave plan calendar year end date.
						|| block.getLeaveDate().before(thisEntryInterval.getStart().toDate())) {
					tempLB = block;
					break;
				}
		}
		return tempLB;
	}

}