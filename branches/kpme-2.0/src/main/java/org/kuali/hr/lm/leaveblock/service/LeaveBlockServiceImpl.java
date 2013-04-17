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
package org.kuali.hr.lm.leaveblock.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeZone;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.kuali.hr.lm.LMConstants;
import org.kuali.hr.lm.leaveblock.LeaveBlock;
import org.kuali.hr.lm.leaveblock.LeaveBlockHistory;
import org.kuali.hr.lm.leaveblock.dao.LeaveBlockDao;
import org.kuali.hr.lm.workflow.LeaveCalendarDocumentHeader;
import org.kuali.hr.time.assignment.Assignment;
import org.kuali.hr.time.calendar.CalendarEntry;
import org.kuali.hr.time.earncode.EarnCode;
import org.kuali.hr.time.service.base.TkServiceLocator;
import org.kuali.hr.time.util.TKUtils;
import org.kuali.hr.time.util.TkConstants;
import org.kuali.hr.time.workflow.TimesheetDocumentHeader;
import org.kuali.rice.krad.service.KRADServiceLocator;

public class LeaveBlockServiceImpl implements LeaveBlockService {

    private static final Logger LOG = Logger.getLogger(LeaveBlockServiceImpl.class);

    private LeaveBlockDao leaveBlockDao;

    @Override
    public LeaveBlock getLeaveBlock(String leaveBlockId) {
        return leaveBlockDao.getLeaveBlock(leaveBlockId);
    }

    public LeaveBlockDao getLeaveBlockDao() {
        return leaveBlockDao;
    }

    public void setLeaveBlockDao(LeaveBlockDao leaveBlockDao) {
        this.leaveBlockDao = leaveBlockDao;
    }

    public List<LeaveBlock> getLeaveBlocksForDocumentId(String documentId) {
        return leaveBlockDao.getLeaveBlocksForDocumentId(documentId);
    }


    @Override
    public List<LeaveBlock> getLeaveBlocks(String principalId, LocalDate beginDate,
                                   LocalDate endDate) {
        return leaveBlockDao.getLeaveBlocks(principalId, beginDate, endDate);
    }

    @Override
    public List<LeaveBlock> getLeaveBlocksWithAccrualCategory(String principalId, LocalDate beginDate,
                                           LocalDate endDate, String accrualCategory) {
        return leaveBlockDao.getLeaveBlocksWithAccrualCategory(principalId, beginDate, endDate, accrualCategory);
    }

    @Override
    public List<LeaveBlock> getLeaveBlocksWithType(String principalId, LocalDate beginDate,
                                           LocalDate endDate, String leaveBlockType) {
        return leaveBlockDao.getLeaveBlocksWithType(principalId, beginDate, endDate, leaveBlockType);
    }

    @Override
    public List<LeaveBlock> getLeaveBlocksSinceCarryOver(String principalId, Map<String, LeaveBlock> carryOver, LocalDate endDate, boolean includeAllAccrualCategories) {
        return leaveBlockDao.getLeaveBlocksSinceCarryOver(principalId, carryOver, endDate, includeAllAccrualCategories);
    }

    @Override
    public Map<String, LeaveBlock> getLastCarryOverBlocks(String principalId, LocalDate asOfDate) {
        if (StringUtils.isEmpty(principalId)) {
            return Collections.emptyMap();
        }
        return leaveBlockDao.getLastCarryOverBlocks(principalId, LMConstants.LEAVE_BLOCK_TYPE.CARRY_OVER, asOfDate);
    }

    @Override
    public void saveLeaveBlocks(List<LeaveBlock> leaveBlocks) {
    	KRADServiceLocator.getBusinessObjectService().save(leaveBlocks);
    	
    	List<LeaveBlockHistory> leaveBlockHistories = new ArrayList<LeaveBlockHistory>();
        for (LeaveBlock leaveBlock : leaveBlocks) {
        	LeaveBlockHistory lbh = new LeaveBlockHistory(leaveBlock);
        	lbh.setAction(LMConstants.ACTION.ADD);
        	leaveBlockHistories.add(lbh);
        }
        
        KRADServiceLocator.getBusinessObjectService().save(leaveBlockHistories);
    }

    @Override
    public void deleteLeaveBlock(String leaveBlockId, String principalId) {
        LeaveBlock leaveBlock = getLeaveBlock(leaveBlockId);

        // Make entry into LeaveBlockHistory table
        LeaveBlockHistory leaveBlockHistory = new LeaveBlockHistory(leaveBlock);
        leaveBlockHistory.setPrincipalIdDeleted(principalId);
        leaveBlockHistory.setTimestampDeleted(TKUtils.getCurrentTimestamp());
        leaveBlockHistory.setAction(LMConstants.ACTION.DELETE);

        // deleting leaveblock
        KRADServiceLocator.getBusinessObjectService().delete(leaveBlock);
        
        // creating history
        KRADServiceLocator.getBusinessObjectService().save(leaveBlockHistory);
        
        
    }

    @Override
    public void saveLeaveBlock(LeaveBlock leaveBlock, String principalId) {

    	// first delete and create new entry in the database
    	KRADServiceLocator.getBusinessObjectService().delete(leaveBlock);
    	
    	// create new 
        leaveBlock.setLmLeaveBlockId(null);
    	leaveBlock.setTimestamp(TKUtils.getCurrentTimestamp());
    	leaveBlock.setPrincipalIdModified(principalId);
    	KRADServiceLocator.getBusinessObjectService().save(leaveBlock);

        // save history
        LeaveBlockHistory lbh = new LeaveBlockHistory(leaveBlock);
        lbh.setAction(LMConstants.ACTION.MODIFIED);
        TkServiceLocator.getLeaveBlockHistoryService().saveLeaveBlockHistory(lbh);
        
    }

    @Override
    public void addLeaveBlocks(DateTime beginDate, DateTime endDate, CalendarEntry ce, String selectedEarnCode,
    		BigDecimal hours, String description, Assignment selectedAssignment, String spanningWeeks, String leaveBlockType, String principalId) {
    	
    	DateTimeZone timezone = TkServiceLocator.getTimezoneService().getUserTimezoneWithFallback();
        DateTime calBeginDateTime = beginDate;
    	DateTime calEndDateTime = endDate;
    	
        if(ce != null) {
        	calBeginDateTime = ce.getBeginPeriodLocalDateTime().toDateTime();
        	calEndDateTime = ce.getEndPeriodLocalDateTime().toDateTime();
        } else {
            throw new RuntimeException("Calendar Entry parameter is null.");
        }
        
        Interval calendarInterval = new Interval(calBeginDateTime, calEndDateTime);
       
        // To create the correct interval by the given begin and end dates,
        // we need to plus one day on the end date to include that date

        Calendar startCal = Calendar.getInstance();
        startCal.setTimeInMillis(beginDate.getMillis());
        startCal.set(Calendar.HOUR_OF_DAY, 0);
        startCal.set(Calendar.MINUTE, 0);
        startCal.set(Calendar.SECOND, 0);
        startCal.set(Calendar.MILLISECOND, 0);
        

        Calendar endCal = Calendar.getInstance();
        endCal.setTimeInMillis(endDate.getMillis());
        endCal.add(Calendar.DATE, 1);
        endCal.set(Calendar.HOUR_OF_DAY, 0);
        endCal.set(Calendar.MINUTE, 0);
        endCal.set(Calendar.SECOND, 0);
        endCal.set(Calendar.MILLISECOND, 0);

        List<Interval> leaveBlockIntervals = TKUtils.createDaySpan(new DateTime(startCal.getTimeInMillis()), new DateTime(endCal.getTimeInMillis()), TKUtils.getSystemDateTimeZone());
//        List<Interval> leaveBlockIntervals = TKUtils.createDaySpan(beginDate.toDateMidnight().toDateTime(), endDate.plusDays(1).toDateMidnight().toDateTime(), TKUtils.getSystemDateTimeZone());

        // need to use beginDate and endDate of the calendar to find all leaveBlocks since LeaveCalendarDocument Id is not always available
        List<LeaveBlock> currentLeaveBlocks = getLeaveBlocks(principalId, calBeginDateTime.toLocalDate(), calEndDateTime.toLocalDate());
    
        // use the current calendar's begin and end date to figure out if this pay period has a leaveDocument
        LeaveCalendarDocumentHeader lcdh = TkServiceLocator.getLeaveCalendarDocumentHeaderService()
        		.getDocumentHeader(principalId, ce.getBeginPeriodLocalDateTime().toDateTime(), ce.getEndPeriodLocalDateTime().toDateTime());
        String docId = lcdh == null ? null : lcdh.getDocumentId();
        
        // TODO: need to integrate with the scheduled timeoff.
    	Interval firstDay = null;
    	DateTime currentDate = beginDate;
        for (Interval leaveBlockInt : leaveBlockIntervals) {
            if (calendarInterval.contains(leaveBlockInt)) {
            	// KPME-1446 if "Include weekends" check box is checked, don't add Sat and Sun to the leaveblock list
            	if (StringUtils.isEmpty(spanningWeeks) && 
                	(leaveBlockInt.getStart().getDayOfWeek() == DateTimeConstants.SATURDAY ||leaveBlockInt.getStart().getDayOfWeek() == DateTimeConstants.SUNDAY)) {
            		
            		// do nothing
            	} else {
            		
            		 // Currently, we store the accrual category value in the leave code table, but store accrual category id in the leaveBlock.
                    // That's why there is a two step server call to get the id. This might be changed in the future.

                    CalendarEntry calendarEntry = TkServiceLocator.getCalendarEntryService().getCurrentCalendarEntryByCalendarId(ce.getHrCalendarId(), new LocalDate().toDateTimeAtStartOfDay());
                    DateTime leaveBlockDate = new DateTime(leaveBlockInt.getStartMillis());
                    
                    String requestStatus = LMConstants.REQUEST_STATUS.USAGE;
                    if (TkServiceLocator.getLeaveApprovalService().isActiveAssignmentFoundOnJobFlsaStatus(principalId, TkConstants.FLSA_STATUS_NON_EXEMPT, true)) {
                    	TimesheetDocumentHeader tdh = TkServiceLocator.getTimesheetDocumentHeaderService().getDocumentHeaderForDate(principalId, leaveBlockDate);
                    	if (tdh != null) {
     	            	   if (DateUtils.isSameDay(leaveBlockDate.toDate(), tdh.getEndDate()) || leaveBlockDate.isAfter(new DateTime(tdh.getEndDate()))) {
     	            		  requestStatus = LMConstants.REQUEST_STATUS.PLANNED;
     	            	   }
     	               } else {
     	            	  requestStatus = LMConstants.REQUEST_STATUS.PLANNED;
     	               }
                    } else {
                    	if (DateUtils.isSameDay(leaveBlockDate.toDate(), calendarEntry.getEndPeriodDateTime()) || leaveBlockDate.isAfter(calendarEntry.getEndPeriodFullDateTime())) {
                    		requestStatus = LMConstants.REQUEST_STATUS.PLANNED;
                    	}
                    }
                    
                    EarnCode earnCodeObj = TkServiceLocator.getEarnCodeService().getEarnCode(selectedEarnCode, ce.getEndPeriodLocalDateTime().toDateTime().toLocalDate());
                    
                    if(earnCodeObj != null && earnCodeObj.getRecordMethod().equals(LMConstants.RECORD_METHOD.TIME)) {
	                    if (firstDay != null) {
	                    	if(!leaveBlockInt.contains(endDate)){
	                    		currentDate = leaveBlockInt.getStart();
	                    	} else if((leaveBlockInt.getStartMillis() - endDate.getMillis()) != 0){
	                    		
	                            hours = TKUtils.getHoursBetween(leaveBlockInt.getStartMillis(), endDate.getMillis());
                                hours = negateHoursIfNecessary(leaveBlockType, hours);
	                    		
	                    		LeaveBlock leaveBlock = buildLeaveBlock(new LocalDate(leaveBlockInt.getStartMillis()), docId, principalId, selectedEarnCode, hours, description, earnCodeObj.getAccrualCategory(), selectedAssignment, requestStatus, leaveBlockType, leaveBlockInt.getStart(), endDate);
	                            
			                    if (!currentLeaveBlocks.contains(leaveBlock)) {
			                        currentLeaveBlocks.add(leaveBlock);
			                    }
	                    		break;
	                    	}            		
	                    }
	                    if (leaveBlockInt.contains(currentDate)) {
	                    	
	                        firstDay = leaveBlockInt;

	                        if (leaveBlockInt.contains(endDate) || (endDate.getMillis() == leaveBlockInt.getEnd().getMillis())) {

	                        	hours = TKUtils.getHoursBetween(currentDate.getMillis(), endDate.getMillis());
                                hours = negateHoursIfNecessary(leaveBlockType, hours);
	                    		
	                    		LeaveBlock leaveBlock = buildLeaveBlock(new LocalDate(leaveBlockInt.getStartMillis()), docId, principalId, selectedEarnCode, hours, description, earnCodeObj.getAccrualCategory(), selectedAssignment, requestStatus, leaveBlockType, currentDate, endDate);
	                            
			                    if (!currentLeaveBlocks.contains(leaveBlock)) {
			                        currentLeaveBlocks.add(leaveBlock);
			                    }

	                            break;
	                            
	                        } else {
	                            // create a leave block that wraps the 24 hr day
	                        	hours = TKUtils.getHoursBetween(currentDate.getMillis(), firstDay.getEndMillis());
                                hours = negateHoursIfNecessary(leaveBlockType, hours);
	                    		
	                    		LeaveBlock leaveBlock = buildLeaveBlock(new LocalDate(leaveBlockInt.getStartMillis()), docId, principalId, selectedEarnCode, hours, description, earnCodeObj.getAccrualCategory(), selectedAssignment, requestStatus, leaveBlockType, currentDate, firstDay.getEnd());
	                            
			                    if (!currentLeaveBlocks.contains(leaveBlock)) {
			                        currentLeaveBlocks.add(leaveBlock);
			                    }

	                        }
	                    }
                    } else {
                        hours = negateHoursIfNecessary(leaveBlockType, hours);
		                LeaveBlock leaveBlock = buildLeaveBlock(new LocalDate(leaveBlockInt.getStartMillis()), docId, principalId, selectedEarnCode, hours, description, earnCodeObj.getAccrualCategory(), 
		                		selectedAssignment, requestStatus, leaveBlockType, null, null);
	                    if (!currentLeaveBlocks.contains(leaveBlock)) {
	                        currentLeaveBlocks.add(leaveBlock);
	                    }
                    }
            	}
            }
        }
        saveLeaveBlocks(currentLeaveBlocks);
    }

    private BigDecimal negateHoursIfNecessary(String leaveBlockType, BigDecimal hours) {
        if ((leaveBlockType.equals(LMConstants.LEAVE_BLOCK_TYPE.LEAVE_CALENDAR)
                || leaveBlockType.equals((LMConstants.LEAVE_BLOCK_TYPE.TIME_CALENDAR)))
                && BigDecimal.ZERO.compareTo(hours) < 0) {
            hours = hours.negate();
        }
        return hours;
    }
    
    
    public LeaveBlock buildLeaveBlock(LocalDate leaveDate, String docId, String principalId, String selectedEarnCode, 
    		BigDecimal hours, String description, String accrualCategory, Assignment selectedAssignment, String requestStatus, String leaveBlockType, DateTime beginDateTime, DateTime endDateTime) {
    	
    	LeaveBlock leaveBlock = new LeaveBlock.Builder(leaveDate, docId, principalId, selectedEarnCode, hours)
        .description(description)
        .principalIdModified(principalId)
        .timestamp(TKUtils.getCurrentTimestamp())
        .scheduleTimeOffId("0")
        .accrualCategory(accrualCategory)
        .workArea(selectedAssignment.getWorkArea())
        .jobNumber(selectedAssignment.getJobNumber())
        .task(selectedAssignment.getTask())
        .requestStatus(requestStatus)
        .leaveBlockType(leaveBlockType)
        .build();
    	
    	leaveBlock.setBeginDateTime(beginDateTime);
        leaveBlock.setEndDateTime(endDateTime);
    	
    	return leaveBlock;
    }
    // KPME-1447
    @Override
    public void updateLeaveBlock(LeaveBlock leaveBlock, String principalId) {
    	//verify that if leave block is usage, leave amount is negative
        if ((LMConstants.LEAVE_BLOCK_TYPE.LEAVE_CALENDAR.equals(leaveBlock.getLeaveBlockType())
                    || LMConstants.LEAVE_BLOCK_TYPE.LEAVE_CALENDAR.equals(leaveBlock.getLeaveBlockType()))
                && BigDecimal.ZERO.compareTo(leaveBlock.getLeaveAmount()) < 0) {
            leaveBlock.setLeaveAmount(leaveBlock.getLeaveAmount().negate());
        }

        // Make entry into LeaveBlockHistory table
        LeaveBlockHistory leaveBlockHistory = new LeaveBlockHistory(leaveBlock);
        leaveBlockHistory.setPrincipalIdDeleted(principalId);
        leaveBlockHistory.setTimestampDeleted(TKUtils.getCurrentTimestamp());
        leaveBlockHistory.setAction(LMConstants.ACTION.MODIFIED);

        KRADServiceLocator.getBusinessObjectService().save(leaveBlock);
        
        // creating history
        KRADServiceLocator.getBusinessObjectService().save(leaveBlockHistory); 
    }    

    public static List<Interval> createDaySpan(DateTime beginDateTime, DateTime endDateTime, DateTimeZone zone) {
        beginDateTime = beginDateTime.toDateTime(zone);
        endDateTime = endDateTime.toDateTime(zone);
        List<Interval> dayIntervals = new ArrayList<Interval>();

        DateTime currDateTime = beginDateTime;
        while (currDateTime.isBefore(endDateTime)) {
            DateTime prevDateTime = currDateTime;
            currDateTime = currDateTime.plusDays(1);
            Interval daySpan = new Interval(prevDateTime, currDateTime);
            dayIntervals.add(daySpan);
        }

        return dayIntervals;
    }

	@Override
	public List<LeaveBlock> getLeaveBlocks(String principalId, String leaveBlockType, String requestStatus, LocalDate currentDate) {
		return leaveBlockDao.getLeaveBlocks(principalId, leaveBlockType, requestStatus, currentDate);
	}

    @Override
    public List<LeaveBlock> getLeaveBlocks(String principalId, String leaveBlockType, String requestStatus, LocalDate beginDate, LocalDate endDate) {
        return leaveBlockDao.getLeaveBlocks(principalId, leaveBlockType, requestStatus, beginDate, endDate);
    }

	@Override
	public List<LeaveBlock> getLeaveBlocksForDate(String principalId, LocalDate leaveDate) {
		return leaveBlockDao.getLeaveBlocksForDate(principalId, leaveDate);
	}

	@Override
	public List<LeaveBlock> getNotAccrualGeneratedLeaveBlocksForDate(String principalId, LocalDate leaveDate) {
		return leaveBlockDao.getNotAccrualGeneratedLeaveBlocksForDate(principalId, leaveDate);
	}

	public List<LeaveBlock> getLeaveBlocksForTimeCalendar(String principalId, LocalDate beginDate, LocalDate endDate, List<String> assignmentKeys) {
		List<LeaveBlock> col = leaveBlockDao.getCalendarLeaveBlocks(principalId, beginDate, endDate);
		List<LeaveBlock> leaveBlocks = filterLeaveBlocksForTimeCalendar(col, assignmentKeys);
		return leaveBlocks;
	}
	
	public List<LeaveBlock> getLeaveBlocksForLeaveCalendar(String principalId, LocalDate beginDate, LocalDate endDate, List<String> assignmentKeys) {
		List<LeaveBlock> col = leaveBlockDao.getLeaveBlocks(principalId, beginDate, endDate);
		List<LeaveBlock> leaveBlocks = filterLeaveBlocksForLeaveCalendar(col, assignmentKeys);
		return leaveBlocks;
	}
	
	public List<LeaveBlock> filterLeaveBlocksForTimeCalendar(List<LeaveBlock> lbs, List<String> assignmentKeys) {
		if(CollectionUtils.isEmpty(assignmentKeys)) {
			return lbs;
		}
    	List<LeaveBlock> results = new ArrayList<LeaveBlock> ();
    	for(LeaveBlock lb : lbs) {
    		if(lb != null) {
	    		if (StringUtils.equals(lb.getLeaveBlockType(), LMConstants.LEAVE_BLOCK_TYPE.ACCRUAL_SERVICE)
	    				&& StringUtils.isNotEmpty(lb.getScheduleTimeOffId()) 
	    				&& lb.getLeaveAmount().compareTo(BigDecimal.ZERO) < 0) {
					// display usage leave blocks generated by system scheduled time off
					results.add(lb);
				} else if(StringUtils.isNotEmpty(lb.getAssignmentKey()) && assignmentKeys.contains(lb.getAssignmentKey())) {
	    			if (StringUtils.equals(lb.getLeaveBlockType(), LMConstants.LEAVE_BLOCK_TYPE.LEAVE_CALENDAR)) {
	    				// only add approved leave blocks that are created from leave calendar
	    				if (StringUtils.equals(lb.getRequestStatus(), LMConstants.REQUEST_STATUS.APPROVED)) {	
	    					results.add(lb);
	    				}
	    			} else if(StringUtils.equals(lb.getLeaveBlockType(), LMConstants.LEAVE_BLOCK_TYPE.TIME_CALENDAR)) {
	    				results.add(lb);
	    			}
	    		}
    		}
    	}
    	return results;
    }
	
	public List<LeaveBlock> filterLeaveBlocksForLeaveCalendar(List<LeaveBlock> lbs, List<String> assignmentKeys) {
		if(CollectionUtils.isEmpty(assignmentKeys)) {
			return lbs;
		}
		List<LeaveBlock> leaveBlocks = new ArrayList<LeaveBlock>();
		for(LeaveBlock lb : lbs) {
  		   if(lb != null) {
  			   if(lb.getLeaveBlockType().equals(LMConstants.LEAVE_BLOCK_TYPE.TIME_CALENDAR)) {
  				  if(StringUtils.isNotEmpty(lb.getAssignmentKey()) && assignmentKeys.contains(lb.getAssignmentKey())) {
  					  leaveBlocks.add(lb);
  				  }
  			   } else {
  				   leaveBlocks.add(lb);
  			   }
  		   }
  		   
  		  
  	   	}
    	return leaveBlocks;
    }

    @Override
    public void deleteLeaveBlocksForDocumentId(String documentId){
        leaveBlockDao.deleteLeaveBlocksForDocumentId(documentId);
    }
    
    
    @Override
    public List<LeaveBlock> getAccrualGeneratedLeaveBlocks(String principalId, LocalDate beginDate, LocalDate endDate) {
    	return leaveBlockDao.getAccrualGeneratedLeaveBlocks(principalId, beginDate, endDate);
    }
    
    @Override
    public List<LeaveBlock> getSSTOLeaveBlocks(String principalId, String sstoId, LocalDate accruledDate) {
    	return leaveBlockDao.getSSTOLeaveBlocks(principalId, sstoId, accruledDate);
    }
    
    @Override
    public List<LeaveBlock> getABELeaveBlocksSinceTime(String principalId, DateTime lastRanDateTime) {
    	return leaveBlockDao.getABELeaveBlocksSinceTime(principalId, lastRanDateTime);
    }
}
