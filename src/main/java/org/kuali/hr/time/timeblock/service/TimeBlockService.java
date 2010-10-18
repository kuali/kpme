package org.kuali.hr.time.timeblock.service;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.kuali.hr.time.assignment.Assignment;
import org.kuali.hr.time.timeblock.TimeBlock;
import org.kuali.hr.time.timesheet.TimesheetDocument;

public interface TimeBlockService {

	public TimeBlock getTimeBlock(String timeBlockId);
	public List<Map<String,Object>> getTimeBlocksForOurput(List<TimeBlock> timeBlocks);
	public void deleteTimeBlock(TimeBlock timeBlock);
	public List<TimeBlock> buildTimeBlocks(Assignment assignment, String earnCode, TimesheetDocument timesheetDocument, 
											Timestamp beginTimestamp, Timestamp endTimestamp);
	public void saveTimeBlocks(List<TimeBlock> oldTimeBlocks, List<TimeBlock> newTimeBlocks);
	public List<TimeBlock> resetTimeHourDetail(List<TimeBlock> origTimeBlocks);
	public List<TimeBlock> getTimeBlocks(Long documentId);
	public List<TimeBlock> buildTimeBlocksSpanDates(Assignment assignment, String earnCode, TimesheetDocument timesheetDocument, 
			Date startSpanDate, Date endSpanDate, Timestamp beginTimestamp, Timestamp endTimestamp);
}
