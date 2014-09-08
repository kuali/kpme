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
package org.kuali.hr.lm.leavecalendar.web;

import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.kuali.hr.lm.balancetransfer.BalanceTransfer;
import org.kuali.hr.lm.leaveSummary.LeaveSummary;
import org.kuali.hr.lm.leavecalendar.LeaveCalendarDocument;
import org.kuali.hr.time.base.web.TkCommonCalendarForm;
import org.kuali.hr.time.calendar.CalendarEntries;
import org.kuali.hr.time.calendar.LeaveCalendar;
import org.kuali.hr.time.util.TKUtils;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class LeaveCalendarForm extends TkCommonCalendarForm {

    private static final DateTimeFormatter SDF_NO_TZ = DateTimeFormat.forPattern("EEE MMM d HH:mm:ss yyyy");

    private String documentId;
    private LeaveCalendar leaveCalendar;
    private LeaveCalendarDocument leaveCalendarDocument;
    CalendarEntries calendarEntry;
    private String startDate;
    private String endDate;
    private String selectedEarnCode;
    private BigDecimal leaveAmount;
    private String description;
    private String leaveBlockId;
    private String prevDocumentId;
    private String nextDocumentId;
    private String prevCalEntryId;
    private String nextCalEntryId;
    private String calEntryId;
	/** String (concat(job number, work_area, task)) -> Formatted Assignment Descriptions */
	private Map<String,String>  assignmentDescriptions;
	private String selectedAssignment;
	private String spanningWeeks; // KPME-1446
	private String leaveBlockString;  // KPME-1447
	private boolean isDocEditable;
	private String currentPayCalStartDate;
	private String currentPayCalEndDate;
	private DateTime currentPayCalStart;
	private DateTime currentPayCalEnd;
	private LeaveSummary leaveSummary;
    private boolean leavePlanningCalendar;
    private List<BalanceTransfer> forfeitures;
	private String startTime;
	private String endTime;
    
    public boolean isCurrentLeavePeriod () {
    	return (TKUtils.getCurrentDate().compareTo(calendarEntry.getBeginPeriodDate()) >= 0 && TKUtils.getCurrentDate().compareTo(calendarEntry.getEndPeriodDate()) < 0);
    }
	public DateTime getCurrentPayCalStart() {
		return currentPayCalStart;
	}

	public void setCurrentPayCalStart(DateTime currentPayCalStart) {
		this.currentPayCalStart = currentPayCalStart;
	}

	public DateTime getCurrentPayCalEnd() {
		return currentPayCalEnd;
	}

	public void setCurrentPayCalEnd(DateTime currentPayCalEnd) {
		this.currentPayCalEnd = currentPayCalEnd;
	}

	public String getCurrentPayCalStartDate() {
		if(currentPayCalStart != null) {
			return this.currentPayCalStart.toString(SDF_NO_TZ);
		} else {
			return null;
		}
	}

	public void setCurrentPayCalStartDate(String currentPayCalStartDate) {
		this.currentPayCalStartDate = currentPayCalStartDate;
	}

	public String getCurrentPayCalEndDate() {
		if(currentPayCalEnd != null) {
			return this.currentPayCalEnd.toString(SDF_NO_TZ);
		} else {
			return null;
		}
	}

	public void setCurrentPayCalEndDate(String currentPayCalEndDate) {
		this.currentPayCalEndDate = currentPayCalEndDate;
	}

	public boolean isDocEditable() {
		return isDocEditable;
	}

	public void setDocEditable(boolean isDocEditable) {
		this.isDocEditable = isDocEditable;
	}

    public boolean isLeavePlanningCalendar() {
        return leavePlanningCalendar;
    }

    public void setLeavePlanningCalendar(boolean leavePlanningCalendar) {
        this.leavePlanningCalendar = leavePlanningCalendar;
    }

    public String getLeaveBlockString() {
		return leaveBlockString;
	}

	public void setLeaveBlockString(String leaveBlockString) {
		this.leaveBlockString = leaveBlockString;
	}

	public String getSpanningWeeks() {
		return spanningWeeks;
	}

	public void setSpanningWeeks(String spanningWeeks) {
		this.spanningWeeks = spanningWeeks;
	}
		
    public String getSelectedAssignment() {
		return selectedAssignment;
	}

	public void setSelectedAssignment(String selectedAssignment) {
		this.selectedAssignment = selectedAssignment;
	}

	public Map<String, String> getAssignmentDescriptions() {
		return assignmentDescriptions;
	}

	public void setAssignmentDescriptions(Map<String, String> assignmentDescriptions) {
		this.assignmentDescriptions = assignmentDescriptions;
	}

	public String getPrevCalEntryId() {
		return prevCalEntryId;
	}

	public void setPrevCalEntryId(String prevCalEntryId) {
		this.prevCalEntryId = prevCalEntryId;
	}

	public String getNextCalEntryId() {
		return nextCalEntryId;
	}

	public void setNextCalEntryId(String nextCalEntryId) {
		this.nextCalEntryId = nextCalEntryId;
	}

	public String getCalEntryId() {
		return calEntryId;
	}

	public void setCalEntryId(String calEntryId) {
		this.calEntryId = calEntryId;
	}

	@Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
    }

    public LeaveCalendar getLeaveCalendar() {
        return leaveCalendar;
    }

    public void setLeaveCalendar(LeaveCalendar leaveCalendar) {
        this.leaveCalendar = leaveCalendar;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public CalendarEntries getCalendarEntry() {
        return calendarEntry;
    }

    public void setCalendarEntry(CalendarEntries calendarEntry) {
        this.calendarEntry = calendarEntry;
    }

    public LeaveCalendarDocument getLeaveCalendarDocument() {
        return leaveCalendarDocument;
    }

    public void setLeaveCalendarDocument(LeaveCalendarDocument leaveCalendarDocument) {
        this.leaveCalendarDocument = leaveCalendarDocument;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

	public BigDecimal getLeaveAmount() {
		return leaveAmount;
	}

	public void setLeaveAmount(BigDecimal leaveAmount) {
		this.leaveAmount = leaveAmount;
	}

	public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLeaveBlockId() {
        return leaveBlockId;
    }

    public void setLeaveBlockId(String leaveBlockId) {
        this.leaveBlockId = leaveBlockId;
    }

    public String getNextDocumentId() {
        return nextDocumentId;
    }

    public void setNextDocumentId(String nextDocumentId) {
        this.nextDocumentId = nextDocumentId;
    }

    public String getPrevDocumentId() {
        return prevDocumentId;
    }

    public void setPrevDocumentId(String prevDocumentId) {
        this.prevDocumentId = prevDocumentId;
    }

    public String getBeginPeriodDateTime() {
        return leaveCalendar.getBeginDateTime().toString(SDF_NO_TZ);
    }

    public String getEndPeriodDateTime() {
        return leaveCalendar.getEndDateTime().toString(SDF_NO_TZ);
    }

	public String getSelectedEarnCode() {
		return selectedEarnCode;
	}

	public void setSelectedEarnCode(String selectedEarnCode) {
		this.selectedEarnCode = selectedEarnCode;
	}

	public LeaveSummary getLeaveSummary() {
		return leaveSummary;
	}

	public void setLeaveSummary(LeaveSummary leaveSummary) {
		this.leaveSummary = leaveSummary;
	}

	public List<BalanceTransfer> getForfeitures() {
		return forfeitures;
	}

	public void setForfeitures(List<BalanceTransfer> forfeitures) {
		this.forfeitures = forfeitures;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
}