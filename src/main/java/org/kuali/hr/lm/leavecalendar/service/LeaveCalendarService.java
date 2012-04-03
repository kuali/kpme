package org.kuali.hr.lm.leavecalendar.service;

import org.kuali.hr.lm.leavecalendar.LeaveCalendarDocument;
import org.kuali.hr.time.calendar.CalendarEntries;
import org.kuali.rice.kew.exception.WorkflowException;

public interface LeaveCalendarService {
    public LeaveCalendarDocument openLeaveCalendarDocument(String principalId, CalendarEntries calEntry) throws WorkflowException;
    public LeaveCalendarDocument getLeaveCalendarDocument(String documentId);

}