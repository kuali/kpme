/**
 * Copyright 2004-2013 The Kuali Foundation
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
package org.kuali.hr.time.position.dao;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryFactory;
import org.apache.ojb.broker.query.ReportQueryByCriteria;
import org.kuali.hr.time.position.Position;
import org.kuali.hr.time.util.TKUtils;
import org.kuali.rice.core.framework.persistence.ojb.dao.PlatformAwareDaoBaseOjb;

public class PositionDaoSpringOjbImpl extends PlatformAwareDaoBaseOjb implements PositionDao {

    @Override
    public Position getPosition(String hrPositionId) {
        Criteria crit = new Criteria();
        crit.addEqualTo("hrPositionId", hrPositionId);

        Query query = QueryFactory.newQuery(Position.class, crit);
        return (Position) this.getPersistenceBrokerTemplate().getObjectByQuery(query);
    }


    @Override
    public Position getPositionByPositionNumber(String hrPositionNbr) {
        Criteria crit = new Criteria();
        crit.addEqualTo("position_nbr", hrPositionNbr);

        Query query = QueryFactory.newQuery(Position.class, crit);
        return (Position) this.getPersistenceBrokerTemplate().getObjectByQuery(query);
    }

	@Override
    @SuppressWarnings("unchecked")
    public List<Position> getPositions(String positionNum, String workArea, String description, Date fromEffdt, Date toEffdt, 
    								   String active, String showHistory) {
        
        List<Position> results = new ArrayList<Position>();
        
    	Criteria root = new Criteria();

        if (StringUtils.isNotBlank(positionNum)) {
            root.addLike("positionNumber", positionNum);
        }
        
        if (StringUtils.isNotBlank(workArea)) {
            root.addLike("workArea", workArea);
        }

        if (StringUtils.isNotBlank(description)) {
            root.addLike("description", description);
        }
        
        Criteria effectiveDateFilter = new Criteria();
        if (fromEffdt != null) {
            effectiveDateFilter.addGreaterOrEqualThan("effectiveDate", fromEffdt);
        }
        if (toEffdt != null) {
            effectiveDateFilter.addLessOrEqualThan("effectiveDate", toEffdt);
        }
        if (fromEffdt == null && toEffdt == null) {
            effectiveDateFilter.addLessOrEqualThan("effectiveDate", TKUtils.getCurrentDate());
        }
        root.addAndCriteria(effectiveDateFilter);
        
        if (StringUtils.isNotBlank(active)) {
        	Criteria activeFilter = new Criteria();
            if (StringUtils.equals(active, "Y")) {
                activeFilter.addEqualTo("active", true);
            } else if (StringUtils.equals(active, "N")) {
                activeFilter.addEqualTo("active", false);
            }
            root.addAndCriteria(activeFilter);
        }

        if (StringUtils.equals(showHistory, "N")) {
            Criteria effdt = new Criteria();
            effdt.addEqualToField("positionNumber", Criteria.PARENT_QUERY_PREFIX + "positionNumber");
            effdt.addAndCriteria(effectiveDateFilter);
            ReportQueryByCriteria effdtSubQuery = QueryFactory.newReportQuery(Position.class, effdt);
            effdtSubQuery.setAttributes(new String[]{"max(effectiveDate)"});
            root.addEqualTo("effectiveDate", effdtSubQuery);
            
            Criteria timestamp = new Criteria();
            timestamp.addEqualToField("positionNumber", Criteria.PARENT_QUERY_PREFIX + "positionNumber");
            timestamp.addAndCriteria(effectiveDateFilter);
            ReportQueryByCriteria timestampSubQuery = QueryFactory.newReportQuery(Position.class, timestamp);
            timestampSubQuery.setAttributes(new String[]{"max(timestamp)"});
            root.addEqualTo("timestamp", timestampSubQuery);
        }
        
        Query query = QueryFactory.newQuery(Position.class, root);
        results.addAll(getPersistenceBrokerTemplate().getCollectionByQuery(query));

        return results;
    }

    /*@Override
    public PositionNumber getNextUniquePositionNumber() {
        Criteria crit = new Criteria();
        ReportQueryByCriteria query = QueryFactory.newReportQuery(PositionNumber.class, crit);
        query.setAttributes(new String[]{"max(id)"});
        return (PositionNumber) this.getPersistenceBrokerTemplate().getObjectByQuery(query);
    }

    @Override
    public void saveOrUpdate(PositionNumber positionNumber) {
        this.getPersistenceBrokerTemplate().store(positionNumber);
    }*/

}