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
package org.kuali.hr.time.timesheet.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.kuali.hr.job.Job;
import org.kuali.hr.lm.LMConstants;
import org.kuali.hr.lm.balancetransfer.BalanceTransfer;
import org.kuali.hr.lm.leaveblock.LeaveBlock;
import org.kuali.hr.lm.leavepayout.LeavePayout;
import org.kuali.hr.lm.timeoff.SystemScheduledTimeOff;
import org.kuali.hr.time.assignment.Assignment;
import org.kuali.hr.time.calendar.CalendarEntries;
import org.kuali.hr.time.principal.PrincipalHRAttributes;
import org.kuali.hr.time.service.base.TkServiceLocator;
import org.kuali.hr.time.timeblock.TimeBlock;
import org.kuali.hr.time.timesheet.TimesheetDocument;
import org.kuali.hr.time.util.TKContext;
import org.kuali.hr.time.util.TKUser;
import org.kuali.hr.time.util.TKUtils;
import org.kuali.hr.time.util.TkConstants;
import org.kuali.hr.time.workflow.TimesheetDocumentHeader;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kew.api.note.Note;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;

public class TimesheetServiceImpl implements TimesheetService {

    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(TimesheetServiceImpl.class);

    @Override
    public void routeTimesheet(String principalId, TimesheetDocument timesheetDocument) {
        routeTimesheet(principalId, timesheetDocument, TkConstants.DOCUMENT_ACTIONS.ROUTE);
    }

    @Override
    public void routeTimesheet(String principalId, TimesheetDocument timesheetDocument, String action) {
        timesheetAction(action, principalId, timesheetDocument);
    }

    @Override
    public void approveTimesheet(String principalId, TimesheetDocument timesheetDocument) {
        timesheetAction(TkConstants.DOCUMENT_ACTIONS.APPROVE, principalId, timesheetDocument);
    }

    @Override
    public void approveTimesheet(String principalId, TimesheetDocument timesheetDocument, String action) {
        timesheetAction(action, principalId, timesheetDocument);
    }

    @Override
    public void disapproveTimesheet(String principalId, TimesheetDocument timesheetDocument) {
        timesheetAction(TkConstants.DOCUMENT_ACTIONS.DISAPPROVE, principalId, timesheetDocument);
    }

    protected void timesheetAction(String action, String principalId, TimesheetDocument timesheetDocument) {
        WorkflowDocument wd = null;
        if (timesheetDocument != null) {
            String rhid = timesheetDocument.getDocumentId();
            wd = WorkflowDocumentFactory.loadDocument(principalId, rhid);

            if (StringUtils.equals(action, TkConstants.DOCUMENT_ACTIONS.ROUTE)) {
                wd.route("Routing for Approval");
            } else if (StringUtils.equals(action, TkConstants.BATCH_JOB_ACTIONS.BATCH_JOB_ROUTE)) {
            	Note.Builder builder = Note.Builder.create(rhid, principalId);
                builder.setCreateDate(new DateTime());
                builder.setText("Routed via Employee Approval batch job");
            	KewApiServiceLocator.getNoteService().createNote(builder.build());
            	
            	wd.route("Batch job routing timesheet");
            } else if (StringUtils.equals(action, TkConstants.DOCUMENT_ACTIONS.APPROVE)) {
                if (TKContext.getUser().getCurrentTargetRoles().isSystemAdmin() &&
                        !TKContext.getUser().getCurrentTargetRoles().isApproverForTimesheet(timesheetDocument)) {
                    wd.superUserBlanketApprove("Superuser approving timesheet.");
                } else {
                    wd.approve("Approving timesheet.");
                }
            } else if (StringUtils.equals(action, TkConstants.BATCH_JOB_ACTIONS.BATCH_JOB_APPROVE)) {
            	Note.Builder builder = Note.Builder.create(rhid, principalId);
           	 	builder.setCreateDate(new DateTime());
           	 	builder.setText("Approved via Supervisor Approval batch job");
           	 	KewApiServiceLocator.getNoteService().createNote(builder.build());
            	
            	wd.superUserBlanketApprove("Batch job approving timesheet.");
            } else if (StringUtils.equals(action, TkConstants.DOCUMENT_ACTIONS.DISAPPROVE)) {
                if (TKContext.getUser().getCurrentTargetRoles().isSystemAdmin()
                        && !TKContext.getUser().getCurrentTargetRoles().isApproverForTimesheet(timesheetDocument)) {
                    wd.superUserDisapprove("Superuser disapproving timesheet.");
                } else {
                    wd.disapprove("Disapproving timesheet.");
                }
            }
        }
    }

    @Override
    public TimesheetDocument openTimesheetDocument(String principalId, CalendarEntries calendarDates) throws WorkflowException {
        TimesheetDocument timesheetDocument = null;

        Date begin = calendarDates.getBeginPeriodDateTime();
        Date end = calendarDates.getEndPeriodDateTime();

        TimesheetDocumentHeader header = TkServiceLocator.getTimesheetDocumentHeaderService().getDocumentHeader(principalId, begin, end);

        if (header == null) {
            List<Assignment> activeAssignments = TkServiceLocator.getAssignmentService().getAssignmentsByCalEntryForTimeCalendar(principalId, calendarDates);
            //TkServiceLocator.getAssignmentService().getAssignments(principalId, TKUtils.getTimelessDate(payCalendarDates.getEndPeriodDate()));
            if (activeAssignments.size() == 0) {
                LOG.warn("No active assignments for " + principalId + " for " + calendarDates.getEndPeriodDate());
                return null;
                //throw new RuntimeException("No active assignments for " + principalId + " for " + calendarDates.getEndPeriodDate());
            }
            
            Person person = KimApiServiceLocator.getPersonService().getPerson(principalId);
            String principalName = person != null ? person.getName() : StringUtils.EMPTY;
            String endDateString = TKUtils.formatDate(new java.sql.Date(end.getTime()));
            String timesheetDocumentTitle = TimesheetDocument.TIMESHEET_DOCUMENT_TYPE + " - " + principalName + " (" + principalId + ") - " + endDateString;
            
            timesheetDocument = this.initiateWorkflowDocument(principalId, begin, end, calendarDates, TimesheetDocument.TIMESHEET_DOCUMENT_TYPE, timesheetDocumentTitle);
            //timesheetDocument.setPayCalendarEntry(calendarDates);
            //this.loadTimesheetDocumentData(timesheetDocument, principalId, calendarDates);
            //TODO switch this to scheduled time offs
            //this.loadHolidaysOnTimesheet(timesheetDocument, principalId, begin, end);
        } else {
            timesheetDocument = this.getTimesheetDocument(header.getDocumentId());
            timesheetDocument.setCalendarEntry(calendarDates);
        }

        timesheetDocument.setTimeSummary(TkServiceLocator.getTimeSummaryService().getTimeSummary(timesheetDocument));
        return timesheetDocument;
    }

    public void loadHolidaysOnTimesheet(TimesheetDocument timesheetDocument, String principalId, Date beginDate, Date endDate) {
        PrincipalHRAttributes principalCalendar = TkServiceLocator.getPrincipalHRAttributeService().getPrincipalCalendar(principalId, new java.sql.Date(beginDate.getTime()));
        if (principalCalendar != null && StringUtils.isNotEmpty(principalCalendar.getLeavePlan())) {
        	List<SystemScheduledTimeOff> sstoList = TkServiceLocator.getSysSchTimeOffService()
        		.getSystemScheduledTimeOffForPayPeriod(principalCalendar.getLeavePlan(), beginDate, endDate);
        	Assignment sstoAssign = TkServiceLocator.getAssignmentService().getAssignmentToApplyScheduledTimeOff(timesheetDocument, TKUtils.getTimelessDate(endDate));
        	if (sstoAssign != null) {
        		for(SystemScheduledTimeOff ssto : sstoList) {
                  BigDecimal sstoCalcHours = TkServiceLocator.getSysSchTimeOffService().calculateSysSchTimeOffHours(sstoAssign.getJob(), ssto.getAmountofTime());
                  TimeBlock timeBlock = TkServiceLocator.getTimeBlockService().createTimeBlock(timesheetDocument, new Timestamp(ssto.getScheduledTimeOffDate().getTime()),
                          new Timestamp(ssto.getScheduledTimeOffDate().getTime()), sstoAssign, TkConstants.HOLIDAY_EARN_CODE, sstoCalcHours, BigDecimal.ZERO, false, false, TKContext.getPrincipalId());
                  timesheetDocument.getTimeBlocks().add(timeBlock);
              }
	            //If system scheduled time off are loaded will need to save them to the database
		        if (CollectionUtils.isNotEmpty(sstoList)) {
		           TkServiceLocator.getTimeBlockService().saveTimeBlocks(new LinkedList<TimeBlock>(), timesheetDocument.getTimeBlocks(), TKContext.getPrincipalId());
		        }
        	}
        }
    }

    protected TimesheetDocument initiateWorkflowDocument(String principalId, Date payBeginDate,  Date payEndDate, CalendarEntries calendarEntries, String documentType, String title) throws WorkflowException {
        TimesheetDocument timesheetDocument = null;
        WorkflowDocument workflowDocument = null;

        workflowDocument = WorkflowDocumentFactory.createDocument(principalId, documentType, title);

        String status = workflowDocument.getStatus().getCode();
        TimesheetDocumentHeader documentHeader = new TimesheetDocumentHeader(workflowDocument.getDocumentId(), principalId, payBeginDate, payEndDate, status);

        documentHeader.setDocumentId(workflowDocument.getDocumentId().toString());
        documentHeader.setDocumentStatus("I");

        TkServiceLocator.getTimesheetDocumentHeaderService().saveOrUpdate(documentHeader);
        timesheetDocument = new TimesheetDocument(documentHeader);
        timesheetDocument.setCalendarEntry(calendarEntries);
        loadTimesheetDocumentData(timesheetDocument, principalId, calendarEntries);
        TkServiceLocator.getTkSearchableAttributeService().updateSearchableAttribute(timesheetDocument, payEndDate);

        if (TkServiceLocator.getLeaveApprovalService().isActiveAssignmentFoundOnJobFlsaStatus(principalId, TkConstants.FLSA_STATUS_NON_EXEMPT, true)) {
        	deleteNonApprovedLeaveBlocks(principalId, calendarEntries.getBeginPeriodDate(), calendarEntries.getEndPeriodDate());
        }
        
        return timesheetDocument;
    }
    
    private void deleteNonApprovedLeaveBlocks(String principalId, Date beginDate, Date endDate) {
    	String batchUserPrincipalId = getBatchUserPrincipalId();
        
        if (batchUserPrincipalId != null) {
	    	List<LeaveBlock> leaveBlocks = TkServiceLocator.getLeaveBlockService().getLeaveBlocks(principalId, beginDate, endDate);
	
	    	for (LeaveBlock leaveBlock : leaveBlocks) {
	    		if (!StringUtils.equals(leaveBlock.getRequestStatus(), LMConstants.REQUEST_STATUS.APPROVED)) {
	    			TkServiceLocator.getLeaveBlockService().deleteLeaveBlock(leaveBlock.getLmLeaveBlockId(), batchUserPrincipalId);
	    		}
	    	}
        } else {
        	String principalName = ConfigContext.getCurrentContextConfig().getProperty(TkConstants.BATCH_USER_PRINCIPAL_NAME);
        	LOG.error("Could not delete leave request blocks due to missing batch user " + principalName);
        }
    }
    
    private String getBatchUserPrincipalId() {
    	String principalId = null;
    	
    	String principalName = ConfigContext.getCurrentContextConfig().getProperty(TkConstants.BATCH_USER_PRINCIPAL_NAME);
        Person person = KimApiServiceLocator.getPersonService().getPersonByPrincipalName(principalName);
        if (person != null) {
        	principalId = person.getPrincipalId();
        }
        
        return principalId;
    }

    public List<TimeBlock> getPrevDocumentTimeBlocks(String principalId, Date payBeginDate) {
        TimesheetDocumentHeader prevTdh = TkServiceLocator.getTimesheetDocumentHeaderService().getPreviousDocumentHeader(principalId, payBeginDate);
        if (prevTdh == null) {
            return new ArrayList<TimeBlock>();
        }
        return TkServiceLocator.getTimeBlockService().getTimeBlocks(prevTdh.getDocumentId());
    }

    @Override
    public TimesheetDocument getTimesheetDocument(String documentId) {
        TimesheetDocument timesheetDocument = null;
        TimesheetDocumentHeader tdh = TkServiceLocator.getTimesheetDocumentHeaderService().getDocumentHeader(documentId);

        if (tdh != null) {
            timesheetDocument = new TimesheetDocument(tdh);
            CalendarEntries pce = TkServiceLocator.getCalendarService().getCalendarDatesByPayEndDate(tdh.getPrincipalId(), tdh.getEndDate(), TkConstants.PAY_CALENDAR_TYPE);
            loadTimesheetDocumentData(timesheetDocument, tdh.getPrincipalId(), pce);

            timesheetDocument.setCalendarEntry(pce);
        } else {
            throw new RuntimeException("Could not find TimesheetDocumentHeader for DocumentID: " + documentId);
        }
        return timesheetDocument;
    }

    protected void loadTimesheetDocumentData(TimesheetDocument tdoc, String principalId, CalendarEntries payCalEntry) {
        List<Assignment> assignments = TkServiceLocator.getAssignmentService().getAssignmentsByCalEntryForTimeCalendar(principalId, payCalEntry);
        List<Job> jobs = TkServiceLocator.getJobService().getJobs(principalId, TKUtils.getTimelessDate(payCalEntry.getEndPeriodDate()));
        List<TimeBlock> timeBlocks = TkServiceLocator.getTimeBlockService().getTimeBlocks(tdoc.getDocumentHeader().getDocumentId());

        tdoc.setAssignments(assignments);
        tdoc.setJobs(jobs);
        tdoc.setTimeBlocks(timeBlocks);
    }

    public boolean isSynchronousUser() {
        List<Assignment> assignments = TkServiceLocator.getAssignmentService().getAssignments(TKUser.getCurrentTargetPerson().getPrincipalId(), TKUtils.getCurrentDate());
        boolean isSynchronousUser = true;
        for (Assignment assignment : assignments) {
            isSynchronousUser &= assignment.isSynchronous();
        }
        return isSynchronousUser;
    }

    //this is an admin function used for testing
    public void deleteTimesheet(String documentId) {
        TkServiceLocator.getTimeBlockService().deleteTimeBlocksAssociatedWithDocumentId(documentId);
        TkServiceLocator.getTimesheetDocumentHeaderService().deleteTimesheetHeader(documentId);
    }

    public TimeBlock resetWorkedHours(TimeBlock timeBlock) {
        if (timeBlock.getBeginTime() != null && timeBlock.getEndTime() != null && StringUtils.equals(timeBlock.getEarnCodeType(), TkConstants.EARN_CODE_TIME)) {
            BigDecimal hours = TKUtils.getHoursBetween(timeBlock.getBeginTime().getTime(), timeBlock.getEndTime().getTime());
            timeBlock.setHours(hours);
        }
        return timeBlock;
    }

    @Override
    public void resetTimeBlock(List<TimeBlock> timeBlocks) {
        for (TimeBlock tb : timeBlocks) {
            resetWorkedHours(tb);
        }
        TkServiceLocator.getTimeBlockService().resetTimeHourDetail(timeBlocks);
    }

	@Override
	public boolean isReadyToApprove(TimesheetDocument document) {
        if (document == null) {
            return false;
        }
        List<LeaveBlock> leaveBlocks = TkServiceLocator.getLeaveBlockService().getLeaveBlocksWithType(document.getPrincipalId(),
        		document.getCalendarEntry().getBeginPeriodDate(), document.getCalendarEntry().getEndPeriodDate(), LMConstants.LEAVE_BLOCK_TYPE.BALANCE_TRANSFER);
        leaveBlocks.addAll(TkServiceLocator.getLeaveBlockService().getLeaveBlocksWithType(document.getPrincipalId(),
        		document.getCalendarEntry().getBeginPeriodDate(), document.getCalendarEntry().getEndPeriodDate(), LMConstants.LEAVE_BLOCK_TYPE.LEAVE_PAYOUT));
        for(LeaveBlock lb : leaveBlocks) {
        	if(!StringUtils.equals(lb.getRequestStatus(),LMConstants.REQUEST_STATUS.APPROVED) &&
        			!StringUtils.equals(lb.getRequestStatus(), LMConstants.REQUEST_STATUS.DISAPPROVED))
        		return false;
        }
        return true;
/*        List<BalanceTransfer> balanceTransfers = TkServiceLocator.getBalanceTransferService().getBalanceTransfers(document.getPrincipalId(),
                document.getCalendarEntry().getBeginPeriodDate(),
                document.getCalendarEntry().getEndPeriodDate());
        if (!CollectionUtils.isEmpty(balanceTransfers))   {
	        for(BalanceTransfer balanceTransfer : balanceTransfers) {
	        	if(StringUtils.equals(TkConstants.DOCUMENT_STATUS.get(balanceTransfer.getStatus()), TkConstants.ROUTE_STATUS.ENROUTE))
	        		return false;
	            if (!StringUtils.equals(LMConstants.REQUEST_STATUS.APPROVED, balanceTransfer.getStatus())
	                    && !StringUtils.equals(LMConstants.REQUEST_STATUS.DISAPPROVED, balanceTransfer.getStatus())) {
	                return false;
	            }
	        }
        }
        List<LeavePayout> leavePayouts = TkServiceLocator.getLeavePayoutService().getLeavePayouts(document.getPrincipalId(),
        		document.getCalendarEntry().getBeginPeriodDate(),
        		document.getCalendarEntry().getEndPeriodDate());
        if (!CollectionUtils.isEmpty(leavePayouts)) {
        	for(LeavePayout payout : leavePayouts) {
	        	if(StringUtils.equals(TkConstants.DOCUMENT_STATUS.get(payout.getStatus()), TkConstants.ROUTE_STATUS.ENROUTE))
	        		return false;
	            if (!StringUtils.equals(LMConstants.REQUEST_STATUS.APPROVED, payout.getStatus())
	                    && !StringUtils.equals(LMConstants.REQUEST_STATUS.DISAPPROVED, payout.getStatus())) {
	                return false;
	            }
        	}
        }
        return true;*/
	}

}