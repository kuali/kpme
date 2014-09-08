/**
 * Copyright 2004-2012 The Kuali Foundation
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
package org.kuali.hr.time.warning;

import org.kuali.hr.time.service.base.TkServiceLocator;
import org.kuali.hr.time.timeblock.TimeBlock;
import org.kuali.hr.time.timesheet.TimesheetDocument;

import java.sql.Date;
import java.util.List;

public class TkWarningServiceImpl implements TkWarningService {
    /**
     * This is used for perpetual warnings that need to stick to the timesheet
     */
    @Override
    public List<String> getWarnings(String documentNumber) {
        TimesheetDocument td = TkServiceLocator.getTimesheetService().getTimesheetDocument(documentNumber);
        //Validate accrual hours
        List<String> warnings;
        warnings = TkServiceLocator.getTimeOffAccrualService().validateAccrualHoursLimit(td);

        return warnings;
    }
    
    public List<String> getWarnings(String pId, List<TimeBlock> tbList, Date asOfDate) {
        //Validate accrual hours
        List<String> warnings;
        warnings = TkServiceLocator.getTimeOffAccrualService().validateAccrualHoursLimit(pId, tbList, asOfDate);

        return warnings;
    }
    
    @Override
    public List<String> getWarnings(TimesheetDocument td) {
        //Validate accrual hours
        List<String> warnings;
        warnings = TkServiceLocator.getTimeOffAccrualService().validateAccrualHoursLimit(td);

        return warnings;
    }

}