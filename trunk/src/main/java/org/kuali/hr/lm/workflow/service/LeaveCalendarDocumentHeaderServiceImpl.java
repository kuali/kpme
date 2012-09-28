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
package org.kuali.hr.lm.workflow.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.kuali.hr.lm.leavecalendar.LeaveCalendarDocument;
import org.kuali.hr.lm.workflow.LeaveCalendarDocumentHeader;
import org.kuali.hr.lm.workflow.dao.LeaveCalendarDocumentHeaderDao;
import org.kuali.hr.time.util.TKContext;
import org.kuali.hr.time.util.TkConstants;

public class LeaveCalendarDocumentHeaderServiceImpl implements LeaveCalendarDocumentHeaderService {

    private LeaveCalendarDocumentHeaderDao leaveCalendarDocumentHeaderDao;

    public LeaveCalendarDocumentHeaderDao getLeaveCalendarDocumentHeaderDao() {
        return leaveCalendarDocumentHeaderDao;
    }

    public void setLeaveCalendarDocumentHeaderDao(LeaveCalendarDocumentHeaderDao leaveCalendarDocumentHeaderDao) {
        this.leaveCalendarDocumentHeaderDao = leaveCalendarDocumentHeaderDao;
    }

    @Override
    public LeaveCalendarDocumentHeader getDocumentHeader(String documentId) {
        return leaveCalendarDocumentHeaderDao.getLeaveCalendarDocumentHeader(documentId);
    }

    @Override
    public LeaveCalendarDocumentHeader getDocumentHeader(String principalId, Date beginDate, Date endDate) {
        return leaveCalendarDocumentHeaderDao.getLeaveCalendarDocumentHeader(principalId,  beginDate,  endDate);
    }

    @Override
    public void saveOrUpdate(LeaveCalendarDocumentHeader leaveCalendarDocumentHeader) {
        leaveCalendarDocumentHeaderDao.saveOrUpdate(leaveCalendarDocumentHeader);
    }

	@Override
	public LeaveCalendarDocumentHeader getPrevOrNextDocumentHeader(
			String prevOrNext, String principalId) {
		    LeaveCalendarDocument currentLeaveCalendar = TKContext.getCurrentLeaveCalendarDocument();
	        LeaveCalendarDocumentHeader lcdh;
	        if (StringUtils.equals(prevOrNext, TkConstants.PREV_TIMESHEET)) {
	        	lcdh = leaveCalendarDocumentHeaderDao.getPreviousDocumentHeader(principalId, currentLeaveCalendar.getDocumentHeader().getBeginDate());
	        } else {
	        	lcdh = leaveCalendarDocumentHeaderDao.getNextDocumentHeader(principalId, currentLeaveCalendar.getDocumentHeader().getEndDate());
	        }
	        return lcdh;
	}
	
	@Override
	public LeaveCalendarDocumentHeader getMaxEndDateApprovedLeaveCalendar(String principalId) {
		return leaveCalendarDocumentHeaderDao.getMaxEndDateApprovedLeaveCalendar(principalId);
	}

	@Override
	public LeaveCalendarDocumentHeader getMinBeginDatePendingLeaveCalendar(String principalId) {
		return leaveCalendarDocumentHeaderDao.getMinBeginDatePendingLeaveCalendar(principalId);
	}
	
	@Override
	public List<LeaveCalendarDocumentHeader> getAllDocumentHeadersForPricipalId(String principalId){
		return leaveCalendarDocumentHeaderDao.getAllDocumentHeadersForPricipalId(principalId);
	}
	@Override
	public List<LeaveCalendarDocumentHeader> getAllDelinquentDocumentHeadersForPricipalId(String principalId){
		return leaveCalendarDocumentHeaderDao.getAllDelinquentDocumentHeadersForPricipalId(principalId);
	}

    public void deleteLeaveCalendarHeader(String documentId){
        leaveCalendarDocumentHeaderDao.deleteLeaveCalendarHeader(documentId);
    }
}
