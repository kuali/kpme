package org.kuali.hr.lm.leavecalendar;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.kuali.hr.core.document.calendar.CalendarDocumentContract;
import org.kuali.hr.lm.leaveblock.LeaveBlock;
import org.kuali.hr.lm.workflow.LeaveCalendarDocumentHeader;
import org.kuali.hr.time.assignment.Assignment;
import org.kuali.hr.time.calendar.CalendarEntries;

public class LeaveCalendarDocument implements CalendarDocumentContract {

	public static final String LEAVE_CALENDAR_DOCUMENT_TYPE = "LeaveCalendarDocument";
	public static final String LEAVE_CALENDAR_DOCUMENT_TITLE = "LeaveCalendarDocument";

	LeaveCalendarDocumentHeader documentHeader;
	List<LeaveBlock> leaveBlocks = new ArrayList<LeaveBlock>();
	private List<Assignment> assignments = new LinkedList<Assignment>();
	private CalendarEntries calendarEntry;

	public LeaveCalendarDocument(CalendarEntries calendarEntry) {
		this.calendarEntry = calendarEntry;
	}

	public LeaveCalendarDocument(
			LeaveCalendarDocumentHeader documentHeader) {
		this.documentHeader = documentHeader;
	}

    @Override
	public LeaveCalendarDocumentHeader getDocumentHeader() {
		return documentHeader;
	}

	public void setDocumentHeader(
            LeaveCalendarDocumentHeader documentHeader) {
		this.documentHeader = documentHeader;
	}

	public List<LeaveBlock> getLeaveBlocks() {
		return leaveBlocks;
	}

	public void setLeaveBlocks(List<LeaveBlock> leaveBlocks) {
		this.leaveBlocks = leaveBlocks;
	}

    @Override
	public CalendarEntries getCalendarEntry() {
		return calendarEntry;
	}

	public void setCalendarEntry(CalendarEntries calendarEntry) {
		this.calendarEntry = calendarEntry;
	}

	public String getPrincipalId() {
		return getDocumentHeader().getPrincipalId();
	}

	public String getDocumentId() {
		if (getDocumentHeader() != null) {
			return getDocumentHeader().getDocumentId();
		} else {
			return null;
		}
	}

    @Override
	public List<Assignment> getAssignments() {
		return assignments;
	}

	public void setAssignments(List<Assignment> assignments) {
		this.assignments = assignments;
	}

    @Override
    public java.sql.Date getAsOfDate(){
        return new java.sql.Date(getCalendarEntry().getBeginPeriodDateTime().getTime());
    }
	
	
}