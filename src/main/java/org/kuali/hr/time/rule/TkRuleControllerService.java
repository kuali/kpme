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
package org.kuali.hr.time.rule;

import java.util.List;

import org.kuali.hr.lm.leaveblock.LeaveBlock;
import org.kuali.hr.time.calendar.CalendarEntries;
import org.kuali.hr.time.timeblock.TimeBlock;
import org.kuali.hr.time.timesheet.TimesheetDocument;

public interface TkRuleControllerService {

    /**
     * This method mutates the List<TimeBlock> that is passed in. If you need
     * to reference old vs. new changes, be sure to copy/clone the list before
     * passing it to this method.
     */
	public void applyRules(String action, List<TimeBlock> timeBlocks, List<LeaveBlock> leaveBlocks, CalendarEntries payEntry, TimesheetDocument timesheetDocument, String principalId);
}
