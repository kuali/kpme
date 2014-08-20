package org.kuali.hr.time.timeblock.dao;

import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryFactory;
import org.kuali.hr.time.timeblock.TimeBlock;
import org.springmodules.orm.ojb.support.PersistenceBrokerDaoSupport;

public class TimeBlockDaoSpringOjbImpl extends PersistenceBrokerDaoSupport implements TimeBlockDao {

	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(TimeBlockDaoSpringOjbImpl.class);

	public void saveOrUpdate(TimeBlock timeBlock) {
		this.getPersistenceBrokerTemplate().store(timeBlock);
	}

	public void saveOrUpdate(List<TimeBlock> timeBlockList) {
		if (timeBlockList != null) {
			for (TimeBlock timeBlock : timeBlockList) {
				this.getPersistenceBrokerTemplate().store(timeBlock);
			}
		}
	}

	public TimeBlock getTimeBlock(String timeBlockId) {
		Criteria currentRecordCriteria = new Criteria();
		currentRecordCriteria.addEqualTo("tkTimeBlockId", timeBlockId);

		return (TimeBlock)this.getPersistenceBrokerTemplate().getObjectByQuery(QueryFactory.newQuery(TimeBlock.class, currentRecordCriteria));
	}

	@SuppressWarnings("unchecked")
	public List<TimeBlock> getTimeBlocks(Long documentId){
		Criteria currentRecordCriteria = new Criteria();
		currentRecordCriteria.addEqualTo("documentId", documentId);
		Query query = QueryFactory.newQuery(TimeBlock.class, currentRecordCriteria);
		List<TimeBlock> timeBlocks = (List<TimeBlock>)this.getPersistenceBrokerTemplate().getCollectionByQuery(query);
		return timeBlocks == null || timeBlocks.size() == 0 ? Collections.EMPTY_LIST : timeBlocks;
	}

	public void deleteTimeBlock(TimeBlock timeBlock) {
		this.getPersistenceBrokerTemplate().delete(timeBlock);
	}
}