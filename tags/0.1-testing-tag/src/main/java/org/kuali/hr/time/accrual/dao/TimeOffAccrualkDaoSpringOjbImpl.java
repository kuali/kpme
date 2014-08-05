package org.kuali.hr.time.accrual.dao;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryFactory;
import org.apache.ojb.broker.query.ReportQueryByCriteria;
import org.kuali.hr.time.accrual.TimeOffAccrual;
import org.springmodules.orm.ojb.support.PersistenceBrokerDaoSupport;

public class TimeOffAccrualkDaoSpringOjbImpl extends PersistenceBrokerDaoSupport implements TimeOffAccrualDao {

	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(TimeOffAccrualkDaoSpringOjbImpl.class);

	public void saveOrUpdate(TimeOffAccrual timeOffAccrual) {
		this.getPersistenceBrokerTemplate().store(timeOffAccrual);
	}

	public void saveOrUpdate(List<TimeOffAccrual> timeOffAccrualList) {
		if (timeOffAccrualList != null) {
			for (TimeOffAccrual timeOffAccrual : timeOffAccrualList) {
				this.getPersistenceBrokerTemplate().store(timeOffAccrual);
			}
		}
	}
	
	public List<TimeOffAccrual> getTimeOffAccruals (String principalId) {
		
		List<TimeOffAccrual> timeOffAccruals = new LinkedList<TimeOffAccrual>();
		
		Criteria root = new Criteria();
		Criteria effdt = new Criteria();

		// OJB's awesome sub query setup part 1
		effdt.addEqualToField("principalId", Criteria.PARENT_QUERY_PREFIX + "principalId");
		effdt.addEqualToField("accrualCategory", Criteria.PARENT_QUERY_PREFIX + "accrualCategory");
		ReportQueryByCriteria effdtSubQuery = QueryFactory.newReportQuery(TimeOffAccrual.class, effdt);
		effdtSubQuery.setAttributes(new String[] { "max(effdt)" });

		root.addEqualTo("principalId", principalId);
		root.addEqualTo("effectiveDate", effdtSubQuery);

		Query query = QueryFactory.newQuery(TimeOffAccrual.class, root);
		Collection c = this.getPersistenceBrokerTemplate().getCollectionByQuery(query);
		
		if (c != null) {
			timeOffAccruals.addAll(c);
		}

		return timeOffAccruals;
	}
	
}