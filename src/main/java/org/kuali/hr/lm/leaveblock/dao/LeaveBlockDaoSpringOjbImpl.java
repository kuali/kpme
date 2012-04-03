package org.kuali.hr.lm.leaveblock.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryFactory;
import org.kuali.hr.lm.leaveblock.LeaveBlock;
import org.springmodules.orm.ojb.support.PersistenceBrokerDaoSupport;

public class LeaveBlockDaoSpringOjbImpl extends PersistenceBrokerDaoSupport implements LeaveBlockDao {

    private static final Logger LOG = Logger.getLogger(LeaveBlockDaoSpringOjbImpl.class);

    @Override
    public LeaveBlock getLeaveBlock(Long leaveBlockId) {
        Criteria root = new Criteria();
        root.addEqualTo("lmLeaveBlockId", leaveBlockId);
//        root.addEqualTo("active", true);

        Query query = QueryFactory.newQuery(LeaveBlock.class, root);

        return (LeaveBlock) this.getPersistenceBrokerTemplate().getObjectByQuery(query);
    }

    @Override
    public void saveOrUpdate(LeaveBlock leaveBlock) {
        this.getPersistenceBrokerTemplate().store(leaveBlock);
    }

    @Override
    public List<LeaveBlock> getLeaveBlocksForDocumentId(String documentId) {
        List<LeaveBlock> leaveBlocks = new ArrayList<LeaveBlock>();
        Criteria root = new Criteria();
        root.addEqualTo("documentId", documentId);
//        root.addEqualTo("active", true);

        Query query = QueryFactory.newQuery(LeaveBlock.class, root);
        @SuppressWarnings("rawtypes")
        Collection c = this.getPersistenceBrokerTemplate().getCollectionByQuery(query);

        if (c != null) {
            leaveBlocks.addAll(c);
        }
        return leaveBlocks;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List<LeaveBlock> getLeaveBlocks(String principalId, Date beginDate, Date endDate) {
        List<LeaveBlock> leaveBlocks = new ArrayList<LeaveBlock>();
        Criteria root = new Criteria();
        root.addEqualTo("principalId", principalId);
        root.addGreaterOrEqualThan("leaveDate", beginDate);
        root.addLessOrEqualThan("leaveDate", endDate);
//        root.addEqualTo("active", true);

        Query query = QueryFactory.newQuery(LeaveBlock.class, root);
        Collection c = this.getPersistenceBrokerTemplate().getCollectionByQuery(query);

        if (c != null) {
        	leaveBlocks.addAll(c);
        }

        return leaveBlocks;
    }

	@Override
	public List<LeaveBlock> getLeaveBlocks(String principalId, String requestStatus, Date currentDate) {
	    List<LeaveBlock> leaveBlocks = new ArrayList<LeaveBlock>();
	    Criteria root = new Criteria();
	    root.addEqualTo("requestStatus", requestStatus);
	    root.addEqualTo("principalId", principalId);
	    if(currentDate != null) {
	    	root.addGreaterThan("leaveDate", currentDate);
	    }
	    Query query = QueryFactory.newQuery(LeaveBlock.class, root);
	    Collection c = this.getPersistenceBrokerTemplate().getCollectionByQuery(query);
	    if(c!= null) {
	    	leaveBlocks.addAll(c);
	    }
		return leaveBlocks;
	}

}