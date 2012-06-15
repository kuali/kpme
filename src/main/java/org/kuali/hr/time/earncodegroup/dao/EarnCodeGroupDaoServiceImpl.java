package org.kuali.hr.time.earncodegroup.dao;

import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryFactory;
import org.apache.ojb.broker.query.ReportQueryByCriteria;
import org.kuali.hr.time.earncodegroup.EarnCodeGroup;
import org.kuali.hr.time.earncodegroup.EarnCodeGroupDefinition;
import org.springmodules.orm.ojb.support.PersistenceBrokerDaoSupport;

import java.sql.Date;

public class EarnCodeGroupDaoServiceImpl extends PersistenceBrokerDaoSupport implements EarnCodeGroupDaoService {

	@Override
	public EarnCodeGroup getEarnCodeGroup(String earnGroup, Date asOfDate) {
		Criteria root = new Criteria();
		Criteria effdt = new Criteria();
		Criteria timestamp = new Criteria();

		effdt.addEqualToField("earnCodeGroup", Criteria.PARENT_QUERY_PREFIX + "earnCodeGroup");
		effdt.addLessOrEqualThan("effectiveDate", asOfDate);
//		effdt.addEqualTo("active", true);
		ReportQueryByCriteria effdtSubQuery = QueryFactory.newReportQuery(EarnCodeGroup.class, effdt);
		effdtSubQuery.setAttributes(new String[] { "max(effdt)" });

		timestamp.addEqualToField("earnCodeGroup", Criteria.PARENT_QUERY_PREFIX + "earnCodeGroup");
		timestamp.addEqualToField("effectiveDate", Criteria.PARENT_QUERY_PREFIX + "effectiveDate");
//		timestamp.addEqualTo("active", true);
		ReportQueryByCriteria timestampSubQuery = QueryFactory.newReportQuery(EarnCodeGroup.class, timestamp);
		timestampSubQuery.setAttributes(new String[] { "max(timestamp)" });

		root.addEqualTo("earnCodeGroup", earnGroup);
		root.addEqualTo("effectiveDate", effdtSubQuery);
		root.addEqualTo("timestamp", timestampSubQuery);
//		root.addEqualTo("active", true);
		//do not include the summary setup earn groups

		Criteria activeFilter = new Criteria(); // Inner Join For Activity
		activeFilter.addEqualTo("active", true);
		root.addAndCriteria(activeFilter);
				
		Query query = QueryFactory.newQuery(EarnCodeGroup.class, root);
		EarnCodeGroup earnGroupObj  = (EarnCodeGroup)this.getPersistenceBrokerTemplate().getObjectByQuery(query);
		return earnGroupObj;
	}

	@Override
	public EarnCodeGroup getEarnCodeGroupSummaryForEarnCode(String earnCode, Date asOfDate) {
		Criteria root = new Criteria();
		Criteria effdt = new Criteria();
		Criteria timestamp = new Criteria();
		Criteria earnCodeJoin = new Criteria();
		
		effdt.addEqualToField("earnCodeGroup", Criteria.PARENT_QUERY_PREFIX + "earnCodeGroup");
		effdt.addLessOrEqualThan("effectiveDate", asOfDate);
//		effdt.addEqualTo("active", true);
		ReportQueryByCriteria effdtSubQuery = QueryFactory.newReportQuery(EarnCodeGroup.class, effdt);
		effdtSubQuery.setAttributes(new String[] { "max(effdt)" });

		timestamp.addEqualToField("earnCodeGroup", Criteria.PARENT_QUERY_PREFIX + "earnCodeGroup");
		timestamp.addEqualToField("effectiveDate", Criteria.PARENT_QUERY_PREFIX + "effectiveDate");
//		timestamp.addEqualTo("active", true);
		ReportQueryByCriteria timestampSubQuery = QueryFactory.newReportQuery(EarnCodeGroup.class, timestamp);
		timestampSubQuery.setAttributes(new String[] { "max(timestamp)" });
		
		earnCodeJoin.addEqualToField("hrEarnCodeGroupId", Criteria.PARENT_QUERY_PREFIX + "hrEarnCodeGroupId");
		earnCodeJoin.addEqualTo("earnCode", earnCode);
		ReportQueryByCriteria earnCodeSubQuery = QueryFactory.newReportQuery(EarnCodeGroupDefinition.class, earnCodeJoin);
		earnCodeSubQuery.setAttributes(new String[]{"hr_earn_code_group_id"});
		
		root.addEqualTo("hrEarnCodeGroupId",earnCodeSubQuery);
		root.addEqualTo("effectiveDate", effdtSubQuery);
		root.addEqualTo("timestamp", timestampSubQuery);
//		root.addEqualTo("active", true);
		root.addEqualTo("showSummary", true);
		
		Criteria activeFilter = new Criteria(); // Inner Join For Activity
		activeFilter.addEqualTo("active", true);
		root.addAndCriteria(activeFilter);

		Query query = QueryFactory.newQuery(EarnCodeGroup.class, root);
		EarnCodeGroup earnGroupObj  = (EarnCodeGroup)this.getPersistenceBrokerTemplate().getObjectByQuery(query);
		return earnGroupObj;
	}

	@Override
	public EarnCodeGroup getEarnCodeGroupForEarnCode(String earnCode, Date asOfDate) {
		Criteria root = new Criteria();
		Criteria effdt = new Criteria();
		Criteria timestamp = new Criteria();
		Criteria earnCodeJoin = new Criteria();
		
		effdt.addEqualToField("earnCodeGroup", Criteria.PARENT_QUERY_PREFIX + "earnCodeGroup");
		effdt.addLessOrEqualThan("effectiveDate", asOfDate);
//		effdt.addEqualTo("active", true);
		ReportQueryByCriteria effdtSubQuery = QueryFactory.newReportQuery(EarnCodeGroup.class, effdt);
		effdtSubQuery.setAttributes(new String[] { "max(effdt)" });

		timestamp.addEqualToField("earnCodeGroup", Criteria.PARENT_QUERY_PREFIX + "earnCodeGroup");
		timestamp.addEqualToField("effectiveDate", Criteria.PARENT_QUERY_PREFIX + "effectiveDate");
//		timestamp.addEqualTo("active", true);
		ReportQueryByCriteria timestampSubQuery = QueryFactory.newReportQuery(EarnCodeGroup.class, timestamp);
		timestampSubQuery.setAttributes(new String[] { "max(timestamp)" });
		
		earnCodeJoin.addEqualToField("hrEarnCodeGroupId", Criteria.PARENT_QUERY_PREFIX + "hrEarnCodeGroupId");
		earnCodeJoin.addEqualTo("earnCode", earnCode);
		ReportQueryByCriteria earnCodeSubQuery = QueryFactory.newReportQuery(EarnCodeGroupDefinition.class, earnCodeJoin);
		earnCodeSubQuery.setAttributes(new String[]{"hr_earn_code_group_id"});
		
		root.addEqualTo("hrEarnCodeGroupId",earnCodeSubQuery);
		root.addEqualTo("effectiveDate", effdtSubQuery);
		root.addEqualTo("timestamp", timestampSubQuery);
//		root.addEqualTo("active", true);

		Criteria activeFilter = new Criteria(); // Inner Join For Activity
		activeFilter.addEqualTo("active", true);
		root.addAndCriteria(activeFilter);
		
		Query query = QueryFactory.newQuery(EarnCodeGroup.class, root);
		EarnCodeGroup earnGroupObj  = (EarnCodeGroup)this.getPersistenceBrokerTemplate().getObjectByQuery(query);
		return earnGroupObj;
	}

	@Override
	public EarnCodeGroup getEarnCodeGroup(String hrEarnCodeGroupId) {
		Criteria crit = new Criteria();
		crit.addEqualTo("hrEarnCodeGroupId", hrEarnCodeGroupId);
		
		Query query = QueryFactory.newQuery(EarnCodeGroup.class, crit);
		return (EarnCodeGroup)this.getPersistenceBrokerTemplate().getObjectByQuery(query);
	}
	
	@Override
	public int getEarnCodeGroupCount(String earnCodeGroup) {
		Criteria crit = new Criteria();
	    crit.addEqualTo("earnCodeGroup", earnCodeGroup);
	    Query query = QueryFactory.newQuery(EarnCodeGroup.class, crit);
	    return this.getPersistenceBrokerTemplate().getCount(query);
	}
	@Override
	public int getNewerEarnCodeGroupCount(String earnCodeGroup, Date effdt) {
		Criteria crit = new Criteria();
		crit.addEqualTo("earnCodeGroup", earnCodeGroup);
		crit.addEqualTo("active", "Y");
		crit.addGreaterThan("effectiveDate", effdt);
		Query query = QueryFactory.newQuery(EarnCodeGroup.class, crit);
       	return this.getPersistenceBrokerTemplate().getCount(query);
	}
}