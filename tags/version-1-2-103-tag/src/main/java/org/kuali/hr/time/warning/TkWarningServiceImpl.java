package org.kuali.hr.time.warning;

import java.sql.Date;
import java.util.List;

import org.kuali.hr.time.service.base.TkServiceLocator;
import org.kuali.hr.time.timeblock.TimeBlock;
import org.kuali.hr.time.timesheet.TimesheetDocument;

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
    
    @Override
    public List<String> getWarnings(String pId, List<TimeBlock> tbList, Date asOfDate) {
        //Validate accrual hours
        List<String> warnings;
        warnings = TkServiceLocator.getTimeOffAccrualService().validateAccrualHoursLimit(pId, tbList, asOfDate);

        return warnings;
    }

}