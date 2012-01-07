package org.kuali.hr.time.batch;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.hr.time.calendar.CalendarEntries;
import org.kuali.hr.time.service.base.TkServiceLocator;
import org.kuali.hr.time.util.TkConstants;
import org.kuali.hr.time.workflow.TimesheetDocumentHeader;

public class SupervisorApprovalBatchJob extends BatchJob {
    private Logger LOG = Logger.getLogger(PayPeriodEndBatchJob.class);
    private CalendarEntries payCalendarEntry;

    public SupervisorApprovalBatchJob(String hrPyCalendarEntryId) {
        super();
        this.setBatchJobName(TkConstants.BATCH_JOB_NAMES.SUPERVISOR_APPROVAL);
        this.setPayCalendarEntryId(hrPyCalendarEntryId);
        this.payCalendarEntry = TkServiceLocator.getCalendarEntriesSerivce().getCalendarEntries(hrPyCalendarEntryId);
    }

    @Override
    public void doWork() {
		Date payBeginDate = payCalendarEntry.getBatchEmployeeApprovalDate();
		List<TimesheetDocumentHeader> documentHeaders = TkServiceLocator.getTimesheetDocumentHeaderService().getDocumentHeaders(payBeginDate);
		for(TimesheetDocumentHeader timesheetDocumentHeader : documentHeaders){
			populateBatchJobEntry(timesheetDocumentHeader);
		}
    }


    @Override
    protected void populateBatchJobEntry(Object o) {
    	TimesheetDocumentHeader tdh = (TimesheetDocumentHeader)o;
        String ip = this.getNextIpAddressInCluster();
        if(StringUtils.isNotBlank(ip)){
            //insert a batch job entry here
            BatchJobEntry entry = this.createBatchJobEntry(this.getBatchJobName(), ip, tdh.getPrincipalId(), tdh.getDocumentId(),null);
            TkServiceLocator.getBatchJobEntryService().saveBatchJobEntry(entry);
        } else {
            LOG.info("No ip found in cluster to assign batch jobs");
        }
    }

}
