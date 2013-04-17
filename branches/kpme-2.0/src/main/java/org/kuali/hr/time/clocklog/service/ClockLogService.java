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
package org.kuali.hr.time.clocklog.service;

import java.sql.Timestamp;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.kuali.hr.time.assignment.Assignment;
import org.kuali.hr.time.calendar.CalendarEntry;
import org.kuali.hr.time.clocklog.ClockLog;
import org.kuali.hr.time.timeblock.TimeBlock;
import org.kuali.hr.time.timesheet.TimesheetDocument;

public interface ClockLogService {
	/**
	 * Save clock log 
	 * @param clockLog
	 */
    public void saveClockLog(ClockLog clockLog);
    /**
     * Fetch last clock log for principal id
     * @param principalId
     * @return
     */
    public ClockLog getLastClockLog(String principalId);
    
    /**
     * Fetch last clock log for principal id and clock action
     * @param principalId
     * @param clockAction
     * @return
     */
    public ClockLog getLastClockLog(String principalId, String clockAction);
    
	/**
	 * Fetch last clock log for principal id, assignment details, and calendar entry
	 * @param principalId
	 * @param jobNumber
	 * @param workArea
	 * @param task
	 * @param calendarEntry
	 * @return
	 */
	public ClockLog getLastClockLog(String principalId, String jobNumber, String workArea, String task, CalendarEntry calendarEntry);

    /**
     * Build ClockLog based on criteria passed in
     * @param clockDateTime  -- time with grace rule applied if there is one
     * @param originalTimestamp  -- time without grace rule applied
     * @param assignment
     * @param timesheetDocument
     * @param clockAction
     * @param ip
     * @return
     */
	public ClockLog buildClockLog(DateTime clockDateTime, Timestamp originalTimestamp, Assignment assignment, TimesheetDocument timesheetDocument, String clockAction, String ip);
	
	/**
	 * Process clock log created
	 * @param clockDateTime
	 * @param assignment
	 * @param pe
	 * @param ip
	 * @param asOfDate
	 * @param td
	 * @param clockAction
	 * @param principalId
	 * @return
	 */
    ClockLog processClockLog(DateTime clockDateTime, Assignment assignment, CalendarEntry pe, String ip, LocalDate asOfDate, TimesheetDocument td, String clockAction, String principalId);
    
    /**
     * Fetch clock log by id
     * @param tkClockLogId
     * @return
     */
    public ClockLog getClockLog(String tkClockLogId);

    ClockLog buildClockLog(DateTime clockDateTime, Timestamp originalTimestamp, Assignment assignment, TimesheetDocument timesheetDocument, String clockAction, String ip, String userPrincipalId);

    ClockLog processClockLog(DateTime clockDateTime, Assignment assignment, CalendarEntry pe, String ip, LocalDate asOfDate, TimesheetDocument td, String clockAction, String principalId, String userPrincipalId);
    
    /**
     * Get warning messages for clock actions taken from unapproved IP address on given timesheet document
     * @param timeBlocks
     * @return List<String>
     */
    public List<String> getUnapprovedIPWarning(List<TimeBlock> timeBlocks);
    
    /** 
     * Build an unapproved IP address warning message for  based on given ClockLog 
     * @param cl
     * @return
     */
    public String buildUnapprovedIPWarning(ClockLog cl);
}
