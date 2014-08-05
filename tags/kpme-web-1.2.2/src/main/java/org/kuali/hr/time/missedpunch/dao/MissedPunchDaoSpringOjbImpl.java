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
package org.kuali.hr.time.missedpunch.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryFactory;
import org.kuali.hr.time.batch.BatchJobEntry;
import org.kuali.hr.time.calendar.CalendarEntries;
import org.kuali.hr.time.missedpunch.MissedPunchDocument;
import org.kuali.hr.time.service.base.TkServiceLocator;
import org.kuali.hr.time.util.TkConstants;
import org.kuali.hr.time.workflow.TimesheetDocumentHeader;
import org.kuali.rice.core.framework.persistence.ojb.dao.PlatformAwareDaoBaseOjb;

public class MissedPunchDaoSpringOjbImpl extends PlatformAwareDaoBaseOjb implements MissedPunchDao {

    @Override
    public MissedPunchDocument getMissedPunchByRouteHeader(String headerId) {
    	MissedPunchDocument mp = null;

        Criteria root = new Criteria();
        root.addEqualTo("documentNumber", headerId);
        Query query = QueryFactory.newQuery(MissedPunchDocument.class, root);
        mp = (MissedPunchDocument)this.getPersistenceBrokerTemplate().getObjectByQuery(query);

        return mp;
    }
    
    @Override
    public MissedPunchDocument getMissedPunchByClockLogId(String clockLogId) {
    	MissedPunchDocument mp = null;

        Criteria root = new Criteria();
        root.addEqualTo("tkClockLogId", clockLogId);
        Query query = QueryFactory.newQuery(MissedPunchDocument.class, root);
        mp = (MissedPunchDocument)this.getPersistenceBrokerTemplate().getObjectByQuery(query);

        return mp;
    }
    
    @Override
    public List<MissedPunchDocument> getMissedPunchDocsByBatchJobEntry(BatchJobEntry batchJobEntry) {
    	List<MissedPunchDocument> results = new ArrayList<MissedPunchDocument>();
		Criteria root = new Criteria();
		
		root.addEqualTo("principalId", batchJobEntry.getPrincipalId());
	    root.addEqualTo("documentStatus", TkConstants.ROUTE_STATUS.ENROUTE);
	    Query query = QueryFactory.newQuery(MissedPunchDocument.class, root);
	    List<MissedPunchDocument> aList = (List<MissedPunchDocument>) this.getPersistenceBrokerTemplate().getCollectionByQuery(query);
	    
	    String pcdId = batchJobEntry.getHrPyCalendarEntryId();
	    CalendarEntries pcd = TkServiceLocator.getCalendarEntriesService().getCalendarEntries(pcdId.toString());
	    if(pcd != null) {
		    for(MissedPunchDocument aDoc : aList) {
		    	String tscId = aDoc.getTimesheetDocumentId();
		    	TimesheetDocumentHeader tsdh = TkServiceLocator.getTimesheetDocumentHeaderService().getDocumentHeader(tscId);
		    	if(tsdh != null) {
		    		if(tsdh.getPayBeginDate().equals(pcd.getBeginPeriodDate()) && tsdh.getPayEndDate().equals(pcd.getEndPeriodDate())) {
		    			results.add(aDoc);
		    		}
		    	}
		    }
	    }
	    
    	return results;
    }
    
    @Override
    public List<MissedPunchDocument> getMissedPunchDocsByTimesheetDocumentId(String timesheetDocumentId) {
    	List<MissedPunchDocument> missedPunchDocuments = new ArrayList<MissedPunchDocument>();

        Criteria root = new Criteria();
        root.addEqualTo("timesheetDocumentId", timesheetDocumentId);
        Query query = QueryFactory.newQuery(MissedPunchDocument.class, root);
        missedPunchDocuments.addAll(this.getPersistenceBrokerTemplate().getCollectionByQuery(query));

        return missedPunchDocuments;
    }
    
}