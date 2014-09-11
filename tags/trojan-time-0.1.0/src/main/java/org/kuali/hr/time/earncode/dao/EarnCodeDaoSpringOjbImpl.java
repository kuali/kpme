package org.kuali.hr.time.earncode.dao;

import java.sql.Date;
import java.util.List;

import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryFactory;
import org.apache.ojb.broker.query.ReportQueryByCriteria;
import org.kuali.hr.time.earncode.EarnCode;
import org.springmodules.orm.ojb.support.PersistenceBrokerDaoSupport;

import uk.ltd.getahead.dwr.util.Logger;

public class EarnCodeDaoSpringOjbImpl extends PersistenceBrokerDaoSupport implements EarnCodeDao {

	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(EarnCodeDaoSpringOjbImpl.class);

	public void saveOrUpdate(EarnCode earnCode) {
		this.getPersistenceBrokerTemplate().store(earnCode);
	}

	public void saveOrUpdate(List<EarnCode> earnCodeList) {
		if (earnCodeList != null) {
			for (EarnCode earnCode : earnCodeList) {
				this.getPersistenceBrokerTemplate().store(earnCode);
			}
		}
	}

	public EarnCode getEarnCodeById(Long earnCodeId) {
		Criteria crit = new Criteria();
		crit.addEqualTo("earnCodeId", earnCodeId);
		return (EarnCode) this.getPersistenceBrokerTemplate().getObjectByQuery(QueryFactory.newQuery(EarnCode.class, crit));
	}

	@Override
	public EarnCode getEarnCode(String earnCode, Date asOfDate) {
		EarnCode ec = null;

		Criteria root = new Criteria();
		Criteria effdt = new Criteria();
		Criteria timestamp = new Criteria();

		// OJB's awesome sub query setup part 1
		effdt.addEqualToField("earnCode", Criteria.PARENT_QUERY_PREFIX + "earnCode");
		effdt.addLessOrEqualThan("effectiveDate", asOfDate);
		effdt.addEqualTo("active", true);
		ReportQueryByCriteria effdtSubQuery = QueryFactory.newReportQuery(EarnCode.class, effdt);
		effdtSubQuery.setAttributes(new String[] { "max(effdt)" });

		// OJB's awesome sub query setup part 2
		timestamp.addEqualToField("earnCode", Criteria.PARENT_QUERY_PREFIX + "earnCode");
		timestamp.addEqualToField("effectiveDate", Criteria.PARENT_QUERY_PREFIX + "effectiveDate");
		timestamp.addEqualTo("active", true);
		ReportQueryByCriteria timestampSubQuery = QueryFactory.newReportQuery(EarnCode.class, timestamp);
		timestampSubQuery.setAttributes(new String[] { "max(timestamp)" });

		root.addEqualTo("earnCode", earnCode);
		root.addEqualTo("effectiveDate", effdtSubQuery);
		root.addEqualTo("timestamp", timestampSubQuery);
		root.addEqualTo("active", true);

		Query query = QueryFactory.newQuery(EarnCode.class, root);
		Object obj = this.getPersistenceBrokerTemplate().getObjectByQuery(query);

		if (obj != null) {
			ec = (EarnCode) obj;
		}

		return ec;
	}
	
	
	// for WebService
	public EarnCode getEarnCode(String earnCode){
		Criteria crit = new Criteria();
		crit.addEqualTo("earnCode", earnCode);
		return (EarnCode) this.getPersistenceBrokerTemplate().getObjectByQuery(QueryFactory.newQuery(EarnCode.class,crit));
	}
	
	


}