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
package org.kuali.hr.time.assignment.dao;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryFactory;
import org.apache.ojb.broker.query.ReportQueryByCriteria;
import org.kuali.hr.core.util.OjbSubQueryUtil;
import org.kuali.hr.time.assignment.Assignment;
import org.kuali.hr.time.service.base.TkServiceLocator;
import org.kuali.hr.time.util.TKContext;
import org.kuali.hr.time.util.TKUtils;
import org.kuali.hr.time.workarea.WorkArea;
import org.kuali.rice.core.framework.persistence.ojb.dao.PlatformAwareDaoBaseOjb;

public class AssignmentDaoSpringOjbImpl extends PlatformAwareDaoBaseOjb implements AssignmentDao {

    private static final Logger LOG = Logger.getLogger(AssignmentDaoSpringOjbImpl.class);
    private static final ImmutableList<String> ASSIGNMENT_EQUAL_TO_FIELD = new ImmutableList.Builder<String>()
            .add("jobNumber")
            .add("workArea")
            .add("task")
            .add("principalId")
            .build();

    @Override
    public void saveOrUpdate(Assignment assignment) {
        this.getPersistenceBrokerTemplate().store(assignment);
    }

    @Override
    public void saveOrUpdate(List<Assignment> assignments) {
        if (assignments != null) {
            for (Assignment assign : assignments) {
                this.getPersistenceBrokerTemplate().store(assign);
            }
        }
    }

    @Override
    public void delete(Assignment assignment) {
        if (assignment != null) {
            LOG.debug("Deleting assignment:" + assignment.getTkAssignmentId());
            this.getPersistenceBrokerTemplate().delete(assignment);
        } else {
            LOG.warn("Attempt to delete null assignment.");
        }
    }

    public Assignment getAssignment(String principalId, Long jobNumber, Long workArea, Long task, Date asOfDate) {
        Criteria root = new Criteria();

        root.addEqualTo("principalId", principalId);
        root.addEqualTo("jobNumber", jobNumber);
        root.addEqualTo("workArea", workArea);
        root.addEqualTo("task", task);
        root.addEqualTo("effectiveDate", OjbSubQueryUtil.getEffectiveDateSubQuery(Assignment.class, asOfDate, ASSIGNMENT_EQUAL_TO_FIELD, false));
        root.addEqualTo("timestamp", OjbSubQueryUtil.getTimestampSubQuery(Assignment.class, ASSIGNMENT_EQUAL_TO_FIELD, false));
        //root.addEqualTo("active", true);

        Criteria activeFilter = new Criteria(); // Inner Join For Activity
        activeFilter.addEqualTo("active", true);
        root.addAndCriteria(activeFilter);

        Query query = QueryFactory.newQuery(Assignment.class, root);
        Object o = this.getPersistenceBrokerTemplate().getObjectByQuery(query);

        return (Assignment) o;
    }


    @Override
    public Assignment getAssignment(Long job, Long workArea, Long task, Date asOfDate) {
        Criteria root = new Criteria();

        root.addEqualTo("jobNumber", job);
        root.addEqualTo("workArea", workArea);
        root.addEqualTo("task", task);
        root.addEqualTo("effectiveDate", OjbSubQueryUtil.getEffectiveDateSubQuery(Assignment.class, asOfDate, ASSIGNMENT_EQUAL_TO_FIELD, false));
        root.addEqualTo("timestamp", OjbSubQueryUtil.getTimestampSubQuery(Assignment.class, ASSIGNMENT_EQUAL_TO_FIELD, false));
        root.addEqualTo("principalId", TKContext.getTargetPrincipalId());
        //root.addEqualTo("active", true);

        Criteria activeFilter = new Criteria(); // Inner Join For Activity
        activeFilter.addEqualTo("active", true);
        root.addAndCriteria(activeFilter);

        Query query = QueryFactory.newQuery(Assignment.class, root);
        Object o = this.getPersistenceBrokerTemplate().getObjectByQuery(query);

        return (Assignment) o;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public List<Assignment> findAssignments(String principalId, Date asOfDate) {
        List<Assignment> assignments = new ArrayList<Assignment>();
        Criteria root = new Criteria();

        root.addEqualTo("principalId", principalId);
        root.addEqualTo("effectiveDate", OjbSubQueryUtil.getEffectiveDateSubQuery(Assignment.class, asOfDate, ASSIGNMENT_EQUAL_TO_FIELD, false));
        root.addEqualTo("timestamp", OjbSubQueryUtil.getTimestampSubQuery(Assignment.class, ASSIGNMENT_EQUAL_TO_FIELD, false));
        //root.addEqualTo("active", true);

        Criteria activeFilter = new Criteria(); // Inner Join For Activity
        activeFilter.addEqualTo("active", true);
        root.addAndCriteria(activeFilter);

        Query query = QueryFactory.newQuery(Assignment.class, root);
        Collection c = this.getPersistenceBrokerTemplate().getCollectionByQuery(query);

        if (c != null) {
            assignments.addAll(c);
        }

        return assignments;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public List<Assignment> findAssignmentsWithinPeriod(String principalId, Date startDate, Date endDate) {
        List<Assignment> assignments = new ArrayList<Assignment>();
        Criteria root = new Criteria();

        root.addGreaterOrEqualThan("effectiveDate", startDate);
        root.addLessOrEqualThan("effectiveDate", endDate);
        root.addEqualTo("principalId", principalId);
        root.addEqualTo("active", true);

        Query query = QueryFactory.newQuery(Assignment.class, root);
        Collection c = this.getPersistenceBrokerTemplate().getCollectionByQuery(query);

        if (c != null) {
            assignments.addAll(c);
        }

        return assignments;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public List<Assignment> getActiveAssignmentsInWorkArea(Long workArea, Date asOfDate) {
        List<Assignment> assignments = new ArrayList<Assignment>();
        Criteria root = new Criteria();

        root.addEqualTo("workArea", workArea);
        root.addEqualTo("effectiveDate", OjbSubQueryUtil.getEffectiveDateSubQuery(Assignment.class, asOfDate, ASSIGNMENT_EQUAL_TO_FIELD, true));
        root.addEqualTo("timestamp", OjbSubQueryUtil.getTimestampSubQuery(Assignment.class, ASSIGNMENT_EQUAL_TO_FIELD, true));
        root.addEqualTo("active", true);

        Criteria activeFilter = new Criteria(); // Inner Join For Activity
        activeFilter.addEqualTo("active", true);
        root.addAndCriteria(activeFilter);

        Query query = QueryFactory.newQuery(Assignment.class, root);
        Collection c = this.getPersistenceBrokerTemplate().getCollectionByQuery(query);

        if (c != null) {
            assignments.addAll(c);
        }

        return assignments;
    }

    public List<Assignment> getActiveAssignments(Date asOfDate) {
        List<Assignment> assignments = new ArrayList<Assignment>();
        
        Criteria root = new Criteria();
        root.addLessOrEqualThan("effectiveDate", asOfDate);

        Criteria timestamp = new Criteria();
        timestamp.addEqualToField("principalId", Criteria.PARENT_QUERY_PREFIX + "principalId");
        timestamp.addEqualToField("jobNumber", Criteria.PARENT_QUERY_PREFIX + "jobNumber");
        timestamp.addEqualToField("workArea", Criteria.PARENT_QUERY_PREFIX + "workArea");
        timestamp.addEqualToField("task", Criteria.PARENT_QUERY_PREFIX + "task");
        timestamp.addEqualToField("effectiveDate", Criteria.PARENT_QUERY_PREFIX + "effectiveDate");
        ReportQueryByCriteria timestampSubQuery = QueryFactory.newReportQuery(Assignment.class, timestamp);
        timestampSubQuery.setAttributes(new String[]{"max(timestamp)"});
        root.addEqualTo("timestamp", timestampSubQuery);
        
		Criteria activeFilter = new Criteria();
		activeFilter.addEqualTo("active", true);
		root.addAndCriteria(activeFilter);

        Query query = QueryFactory.newQuery(Assignment.class, root);
        Collection c = this.getPersistenceBrokerTemplate().getCollectionByQuery(query);

        if (c != null) {
            assignments.addAll(c);
        }

        return assignments;
    }

    public Assignment getAssignment(String tkAssignmentId) {
        Criteria crit = new Criteria();
        crit.addEqualTo("tkAssignmentId", tkAssignmentId);
        Query query = QueryFactory.newQuery(Assignment.class, crit);
        return (Assignment) this.getPersistenceBrokerTemplate().getObjectByQuery(query);
    }

    // KPME-1129 Kagata
    @SuppressWarnings({"rawtypes", "unchecked"})
    public List<Assignment> getActiveAssignmentsForJob(String principalId, Long jobNumber, Date asOfDate) {
        List<Assignment> assignments = new ArrayList<Assignment>();
        Criteria root = new Criteria();

        root.addEqualTo("principalId", principalId);
        root.addEqualTo("jobNumber", jobNumber);
        root.addEqualTo("effectiveDate", OjbSubQueryUtil.getEffectiveDateSubQuery(Assignment.class, asOfDate, ASSIGNMENT_EQUAL_TO_FIELD, false));
        root.addEqualTo("timestamp", OjbSubQueryUtil.getTimestampSubQuery(Assignment.class, ASSIGNMENT_EQUAL_TO_FIELD, false));
        root.addEqualTo("active", true);

        Criteria activeFilter = new Criteria(); // Inner Join For Activity
        activeFilter.addEqualTo("active", true);
        root.addAndCriteria(activeFilter);

        Query query = QueryFactory.newQuery(Assignment.class, root);
        Collection c = this.getPersistenceBrokerTemplate().getCollectionByQuery(query);

        if (c != null) {
            assignments.addAll(c);
        }

        return assignments;
    }

	@Override
    @SuppressWarnings("unchecked")
    public List<Assignment> searchAssignments(Date fromEffdt, Date toEffdt, String principalId, String jobNumber, String dept, String workArea, 
    										  String active, String showHistory) {

        List<Assignment> results = new ArrayList<Assignment>();
        
        Criteria root = new Criteria();

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
        
        if (StringUtils.isNotBlank(principalId)) {
            root.addLike("principalId", principalId);
        }

        if (StringUtils.isNotBlank(jobNumber)) {
            root.addLike("jobNumber", jobNumber);
        }

        if (StringUtils.isNotBlank(dept)) {
            Criteria workAreaCriteria = new Criteria();
            Date asOfDate = toEffdt != null ? toEffdt : TKUtils.getCurrentDate();
            Collection<WorkArea> workAreasForDept = TkServiceLocator.getWorkAreaService().getWorkAreas(dept,asOfDate);
            if (CollectionUtils.isNotEmpty(workAreasForDept)) {
                List<Long> longWorkAreas = new ArrayList<Long>();
                for(WorkArea cwa : workAreasForDept){
                    longWorkAreas.add(cwa.getWorkArea());
                }
                workAreaCriteria.addIn("workArea", longWorkAreas);
            }
            root.addAndCriteria(workAreaCriteria);
        }

        if (StringUtils.isNotBlank(workArea)) {
            root.addLike("workArea", workArea);
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

        if (StringUtils.equals(showHistory, "N")) {
            root.addEqualTo("effectiveDate", OjbSubQueryUtil.getEffectiveDateSubQueryWithFilter(Assignment.class, effectiveDateFilter, ASSIGNMENT_EQUAL_TO_FIELD, false));
            root.addEqualTo("timestamp", OjbSubQueryUtil.getTimestampSubQuery(Assignment.class, ASSIGNMENT_EQUAL_TO_FIELD, false));
        }
        
        Query query = QueryFactory.newQuery(Assignment.class, root);
        results.addAll(getPersistenceBrokerTemplate().getCollectionByQuery(query));
        
        return results;
    }
    
    @Override
    public Assignment getMaxTimestampAssignment(String principalId) {
    	Criteria root = new Criteria();
        Criteria crit = new Criteria();
        
        crit.addEqualTo("principalId", principalId);
        ReportQueryByCriteria timestampSubQuery = QueryFactory.newReportQuery(Assignment.class, crit);
        timestampSubQuery.setAttributes(new String[]{"max(timestamp)"});

        root.addEqualTo("principalId", principalId);
        root.addEqualTo("timestamp", timestampSubQuery);

        Query query = QueryFactory.newQuery(Assignment.class, root);
        return (Assignment) this.getPersistenceBrokerTemplate().getObjectByQuery(query);
    }

    public List<String> getPrincipalIds(List<String> workAreaList, Date effdt, Date startDate, Date endDate) {
    	List<Assignment> results = this.getAssignments(workAreaList, effdt, startDate, endDate);
        Set<String> pids = new HashSet<String>();
        for(Assignment anAssignment : results) {
        	if(anAssignment != null) {
        		pids.add(anAssignment.getPrincipalId());
        	}
        }
        List<String> ids = new ArrayList<String>();
        ids.addAll(pids);
     	return ids;
    }
    
    public List<Assignment> getAssignments(List<String> workAreaList, Date effdt, Date startDate, Date endDate) {
    	List<Assignment> results = new ArrayList<Assignment>();
		 
		Criteria activeRoot = new Criteria();	
     	Criteria activEeffdtCrit = new Criteria();
     	Criteria effdtCrit = new Criteria();
     	Criteria inactiveRoot = new Criteria();

        ReportQueryByCriteria effdtSubQuery = OjbSubQueryUtil.getEffectiveDateSubQueryWithoutFilter(Assignment.class, ASSIGNMENT_EQUAL_TO_FIELD, false);
        ReportQueryByCriteria activeEffdtSubQuery = OjbSubQueryUtil.getEffectiveDateSubQuery(Assignment.class, effdt, ASSIGNMENT_EQUAL_TO_FIELD, false);
        ReportQueryByCriteria timestampSubQuery = OjbSubQueryUtil.getTimestampSubQuery(Assignment.class, ASSIGNMENT_EQUAL_TO_FIELD, false);

        inactiveRoot.addEqualTo("active", "N");
        inactiveRoot.addIn("workArea", workAreaList);
        inactiveRoot.addGreaterOrEqualThan("effectiveDate", startDate);
        inactiveRoot.addLessOrEqualThan("effectiveDate", endDate);
        inactiveRoot.addEqualTo("effectiveDate", effdtSubQuery);
        inactiveRoot.addEqualTo("timestamp", timestampSubQuery);
         
        activeRoot.addIn("workArea", workAreaList);
        activeRoot.addEqualTo("active", "Y");
        activeRoot.addEqualTo("effectiveDate", activeEffdtSubQuery);
        activeRoot.addEqualTo("timestamp", timestampSubQuery);
        activeRoot.addOrCriteria(inactiveRoot);
         
        Query query = QueryFactory.newQuery(Assignment.class, activeRoot);
        Collection c = this.getPersistenceBrokerTemplate().getCollectionByQuery(query);
        if (c != null) {
        	results.addAll(c);
        }
        
        return results;
    }

}