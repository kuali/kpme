package org.kuali.hr.time.timeblock.dao;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryFactory;
import org.apache.ojb.broker.query.ReportQueryByCriteria;
import org.kuali.hr.time.assignment.Assignment;
import org.kuali.hr.time.task.Task;
import org.kuali.hr.time.timeblock.TimeBlock;

public interface TimeBlockDao {

	public void saveOrUpdate(TimeBlock timeBlock);

	public void saveOrUpdate(List<TimeBlock> timeBlockList);

	public void deleteTimeBlock(TimeBlock timeBlock);

	public TimeBlock getTimeBlock(Long timeBlockId);
	
	public List<TimeBlock> getTimeBlocks(Long documentId);
	
	public List<TimeBlock> getTimeBlocksForAssignment(Assignment assign);
	
	public void deleteTimeBlocksAssociatedWithDocumentId(String documentId);
	
	public List<TimeBlock> getTimeBlocksForClockLogId(Long tkClockLogId);
	
	public List<TimeBlock> getTimeBlocks();
	public List<TimeBlock> getLatestEndTimestamp();
}
