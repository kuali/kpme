/**
 * Copyright 2004-2012 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.hr.time.earngroup.dao;

import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryFactory;
import org.apache.ojb.broker.query.ReportQueryByCriteria;
import org.kuali.hr.time.earngroup.EarnGroup;
import org.kuali.hr.time.earngroup.EarnGroupDefinition;
import org.kuali.rice.core.framework.persistence.ojb.dao.PlatformAwareDaoBaseOjb;
import org.springmodules.orm.ojb.support.PersistenceBrokerDaoSupport;

import java.sql.Date;

public class EarnGroupDaoServiceImpl extends PlatformAwareDaoBaseOjb implements EarnGroupDaoService {

	@Override
	public EarnGroup getEarnGroup(String earnGroup, Date asOfDate) {
		Criteria root = new Criteria();
		Criteria effdt = new Criteria();
		Criteria timestamp = new Criteria();

		effdt.addEqualToField("earnGroup", Criteria.PARENT_QUERY_PREFIX + "earnGroup");
		effdt.addLessOrEqualThan("effectiveDate", asOfDate);
//		effdt.addEqualTo("active", true);
		ReportQueryByCriteria effdtSubQuery = QueryFactory.newReportQuery(EarnGroup.class, effdt);
		effdtSubQuery.setAttributes(new String[] { "max(effdt)" });

		timestamp.addEqualToField("earnGroup", Criteria.PARENT_QUERY_PREFIX + "earnGroup");
		timestamp.addEqualToField("effectiveDate", Criteria.PARENT_QUERY_PREFIX + "effectiveDate");
//		timestamp.addEqualTo("active", true);
		ReportQueryByCriteria timestampSubQuery = QueryFactory.newReportQuery(EarnGroup.class, timestamp);
		timestampSubQuery.setAttributes(new String[] { "max(timestamp)" });

		root.addEqualTo("earnGroup", earnGroup);
		root.addEqualTo("effectiveDate", effdtSubQuery);
		root.addEqualTo("timestamp", timestampSubQuery);
//		root.addEqualTo("active", true);
		//do not include the summary setup earn groups

		Criteria activeFilter = new Criteria(); // Inner Join For Activity
		activeFilter.addEqualTo("active", true);
		root.addAndCriteria(activeFilter);
				
		Query query = QueryFactory.newQuery(EarnGroup.class, root);
		EarnGroup earnGroupObj  = (EarnGroup)this.getPersistenceBrokerTemplate().getObjectByQuery(query);
		return earnGroupObj;
	}

	@Override
	public EarnGroup getEarnGroupSummaryForEarnCode(String earnCode, Date asOfDate) {
		Criteria root = new Criteria();
		Criteria effdt = new Criteria();
		Criteria timestamp = new Criteria();
		Criteria earnCodeJoin = new Criteria();
		
		effdt.addEqualToField("earnGroup", Criteria.PARENT_QUERY_PREFIX + "earnGroup");
		effdt.addLessOrEqualThan("effectiveDate", asOfDate);
//		effdt.addEqualTo("active", true);
		ReportQueryByCriteria effdtSubQuery = QueryFactory.newReportQuery(EarnGroup.class, effdt);
		effdtSubQuery.setAttributes(new String[] { "max(effdt)" });

		timestamp.addEqualToField("earnGroup", Criteria.PARENT_QUERY_PREFIX + "earnGroup");
		timestamp.addEqualToField("effectiveDate", Criteria.PARENT_QUERY_PREFIX + "effectiveDate");
//		timestamp.addEqualTo("active", true);
		ReportQueryByCriteria timestampSubQuery = QueryFactory.newReportQuery(EarnGroup.class, timestamp);
		timestampSubQuery.setAttributes(new String[] { "max(timestamp)" });
		
		earnCodeJoin.addEqualToField("hrEarnGroupId", Criteria.PARENT_QUERY_PREFIX + "hrEarnGroupId");
		earnCodeJoin.addEqualTo("earnCode", earnCode);
		ReportQueryByCriteria earnCodeSubQuery = QueryFactory.newReportQuery(EarnGroupDefinition.class, earnCodeJoin);
		earnCodeSubQuery.setAttributes(new String[]{"hr_earn_group_id"});
		
		root.addEqualTo("hrEarnGroupId",earnCodeSubQuery);
		root.addEqualTo("effectiveDate", effdtSubQuery);
		root.addEqualTo("timestamp", timestampSubQuery);
//		root.addEqualTo("active", true);
		root.addEqualTo("showSummary", true);
		
		Criteria activeFilter = new Criteria(); // Inner Join For Activity
		activeFilter.addEqualTo("active", true);
		root.addAndCriteria(activeFilter);

		Query query = QueryFactory.newQuery(EarnGroup.class, root);
		EarnGroup earnGroupObj  = (EarnGroup)this.getPersistenceBrokerTemplate().getObjectByQuery(query);
		return earnGroupObj;
	}

	@Override
	public EarnGroup getEarnGroupForEarnCode(String earnCode, Date asOfDate) {
		Criteria root = new Criteria();
		Criteria effdt = new Criteria();
		Criteria timestamp = new Criteria();
		Criteria earnCodeJoin = new Criteria();
		
		effdt.addEqualToField("earnGroup", Criteria.PARENT_QUERY_PREFIX + "earnGroup");
		effdt.addLessOrEqualThan("effectiveDate", asOfDate);
//		effdt.addEqualTo("active", true);
		ReportQueryByCriteria effdtSubQuery = QueryFactory.newReportQuery(EarnGroup.class, effdt);
		effdtSubQuery.setAttributes(new String[] { "max(effdt)" });

		timestamp.addEqualToField("earnGroup", Criteria.PARENT_QUERY_PREFIX + "earnGroup");
		timestamp.addEqualToField("effectiveDate", Criteria.PARENT_QUERY_PREFIX + "effectiveDate");
//		timestamp.addEqualTo("active", true);
		ReportQueryByCriteria timestampSubQuery = QueryFactory.newReportQuery(EarnGroup.class, timestamp);
		timestampSubQuery.setAttributes(new String[] { "max(timestamp)" });
		
		earnCodeJoin.addEqualToField("hrEarnGroupId", Criteria.PARENT_QUERY_PREFIX + "hrEarnGroupId");
		earnCodeJoin.addEqualTo("earnCode", earnCode);
		ReportQueryByCriteria earnCodeSubQuery = QueryFactory.newReportQuery(EarnGroupDefinition.class, earnCodeJoin);
		earnCodeSubQuery.setAttributes(new String[]{"hr_earn_group_id"});
		
		root.addEqualTo("hrEarnGroupId",earnCodeSubQuery);
		root.addEqualTo("effectiveDate", effdtSubQuery);
		root.addEqualTo("timestamp", timestampSubQuery);
//		root.addEqualTo("active", true);

		Criteria activeFilter = new Criteria(); // Inner Join For Activity
		activeFilter.addEqualTo("active", true);
		root.addAndCriteria(activeFilter);
		
		Query query = QueryFactory.newQuery(EarnGroup.class, root);
		EarnGroup earnGroupObj  = (EarnGroup)this.getPersistenceBrokerTemplate().getObjectByQuery(query);
		return earnGroupObj;
	}

	@Override
	public EarnGroup getEarnGroup(String hrEarnGroupId) {
		Criteria crit = new Criteria();
		crit.addEqualTo("hrEarnGroupId", hrEarnGroupId);
		
		Query query = QueryFactory.newQuery(EarnGroup.class, crit);
		return (EarnGroup)this.getPersistenceBrokerTemplate().getObjectByQuery(query);
	}
	
	@Override
	public int getEarnGroupCount(String earnGroup) {
		Criteria crit = new Criteria();
	    crit.addEqualTo("earnGroup", earnGroup);
	    Query query = QueryFactory.newQuery(EarnGroup.class, crit);
	    return this.getPersistenceBrokerTemplate().getCount(query);
	}
	@Override
	public int getNewerEarnGroupCount(String earnGroup, Date effdt) {
		Criteria crit = new Criteria();
		crit.addEqualTo("earnGroup", earnGroup);
		crit.addEqualTo("active", "Y");
		crit.addGreaterThan("effectiveDate", effdt);
		Query query = QueryFactory.newQuery(EarnGroup.class, crit);
       	return this.getPersistenceBrokerTemplate().getCount(query);
	}
}