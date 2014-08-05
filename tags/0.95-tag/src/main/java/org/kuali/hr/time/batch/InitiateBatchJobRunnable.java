package org.kuali.hr.time.batch;

import org.kuali.hr.time.paycalendar.PayCalendarEntries;
import org.kuali.hr.time.service.base.TkServiceLocator;
import org.kuali.rice.kew.exception.WorkflowException;
import org.mortbay.log.Log;

public class InitiateBatchJobRunnable extends BatchJobEntryRunnable {

    public InitiateBatchJobRunnable(BatchJobEntry entry) {
        super(entry);
    }

	@Override
	public void doWork() throws Exception {
		BatchJobEntry initiateBatchJobEntry = TkServiceLocator.getBatchJobEntryService().getBatchJobEntry(getTkBatchJobEntryId());
		String principalId = initiateBatchJobEntry.getPrincipalId();
		Long hrPyCalendarId = initiateBatchJobEntry.getPayCalendarEntryId();
		PayCalendarEntries payCalendarEntry = TkServiceLocator.getPayCalendarEntriesSerivce().getPayCalendarEntries(hrPyCalendarId);
		try {
			TkServiceLocator.getTimesheetService().openTimesheetDocument(principalId, payCalendarEntry);
		} catch (WorkflowException e) {
			Log.info("Workflow error found"+ e);
            throw e;
		}

	}

}