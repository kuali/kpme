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
package org.kuali.hr.location.dao;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang.StringUtils;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryFactory;
import org.apache.ojb.broker.query.ReportQueryByCriteria;
import org.kuali.hr.core.util.OjbSubQueryUtil;
import org.kuali.hr.location.Location;
import org.kuali.rice.core.framework.persistence.ojb.dao.PlatformAwareDaoBaseOjb;

public class LocationDaoSpringObjImpl extends PlatformAwareDaoBaseOjb implements LocationDao {
    private static final ImmutableList<String> EQUAL_TO_FIELDS = new ImmutableList.Builder<String>()
            .add("location")
            .build();
	@Override
	public Location getLocation(String location, Date asOfDate) {
		Criteria root = new Criteria();

		root.addEqualTo("location", location);
        root.addEqualTo("effectiveDate", OjbSubQueryUtil.getEffectiveDateSubQuery(Location.class, new java.sql.Date(asOfDate.getTime()), EQUAL_TO_FIELDS, false));
        root.addEqualTo("timestamp", OjbSubQueryUtil.getTimestampSubQuery(Location.class, EQUAL_TO_FIELDS, false));
		Criteria activeFilter = new Criteria(); // Inner Join For Activity
		activeFilter.addEqualTo("active", true);
		root.addAndCriteria(activeFilter);

		
		Query query = QueryFactory.newQuery(Location.class, root);
		
		Location l = (Location)this.getPersistenceBrokerTemplate().getObjectByQuery(query);
		
		return l;
	}

	@Override
	public Location getLocation(String hrLocationId) {
		Criteria crit = new Criteria();
		crit.addEqualTo("hrLocationId", hrLocationId);
		
		Query query = QueryFactory.newQuery(Location.class, crit);
		return (Location)this.getPersistenceBrokerTemplate().getObjectByQuery(query);
	}
	
	@Override
	public int getLocationCount(String location) {
		Criteria crit = new Criteria();
		crit.addEqualTo("location", location);
		Query query = QueryFactory.newQuery(Location.class, crit);
		return this.getPersistenceBrokerTemplate().getCount(query);
	}

	@Override
    @SuppressWarnings("unchecked")
    public List<Location> searchLocations(String location, String locationDescr, String active, String showHistory) {
        List<Location> results = new ArrayList<Location>();
    	
        Criteria root = new Criteria();

        if (StringUtils.isNotBlank(location)) {
            root.addLike("location", location);
        }
        
        if (StringUtils.isNotBlank(locationDescr)) {
            root.addLike("description", locationDescr);
        }
        
        if (StringUtils.isNotBlank(active)) {
        	Criteria activeFilter = new Criteria();
            if (StringUtils.equals(active, "Y")) {
                activeFilter.addEqualTo("active", true);
            } else if (StringUtils.equals(active, "N")) {
                activeFilter.addEqualTo("active", false);
            }
            root.addAndCriteria(activeFilter);
        }
        
        Query query = QueryFactory.newQuery(Location.class, root);
        results.addAll(getPersistenceBrokerTemplate().getCollectionByQuery(query));
        
        return results;
    }

}