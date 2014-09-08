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
package org.kuali.hr.lm.workflow.postprocessor;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.kuali.hr.lm.LMConstants;
import org.kuali.hr.lm.leaveblock.LeaveBlock;
import org.kuali.hr.lm.workflow.LeaveCalendarDocumentHeader;
import org.kuali.hr.time.calendar.Calendar;
import org.kuali.hr.time.calendar.CalendarEntries;
import org.kuali.hr.time.service.base.TkServiceLocator;
import org.kuali.hr.time.util.TKContext;
import org.kuali.rice.kew.api.document.DocumentStatus;
import org.kuali.rice.kew.framework.postprocessor.DocumentRouteStatusChange;
import org.kuali.rice.kew.framework.postprocessor.ProcessDocReport;
import org.kuali.rice.kew.postprocessor.DefaultPostProcessor;

public class LmPostProcessor extends DefaultPostProcessor {

	@Override
	public ProcessDocReport doRouteStatusChange(DocumentRouteStatusChange statusChangeEvent) throws Exception {		
		ProcessDocReport pdr = null;
		Long documentId = new Long(statusChangeEvent.getDocumentId());
		LeaveCalendarDocumentHeader document = TkServiceLocator.getLeaveCalendarDocumentHeaderService().getDocumentHeader(documentId.toString());
		if (document != null) {
			pdr = super.doRouteStatusChange(statusChangeEvent);
			// Only update the status if it's different.
			if (!document.getDocumentStatus().equals(statusChangeEvent.getNewRouteStatus())) {
                DocumentStatus newDocumentStatus = DocumentStatus.fromCode(statusChangeEvent.getNewRouteStatus());

				updateLeaveCalendarDocumentHeaderStatus(document, newDocumentStatus);
				
				calculateMaxCarryOver(document, newDocumentStatus);
			}
		}
		
		return pdr;
	}
	
	private void updateLeaveCalendarDocumentHeaderStatus(LeaveCalendarDocumentHeader leaveCalendarDocumentHeader, DocumentStatus newDocumentStatus) {
		leaveCalendarDocumentHeader.setDocumentStatus(newDocumentStatus.getCode());
		TkServiceLocator.getLeaveCalendarDocumentHeaderService().saveOrUpdate(leaveCalendarDocumentHeader);
	}
	
	private void calculateMaxCarryOver(LeaveCalendarDocumentHeader leaveCalendarDocumentHeader, DocumentStatus newDocumentStatus) {
		String documentId = leaveCalendarDocumentHeader.getDocumentId();
		String principalId = leaveCalendarDocumentHeader.getPrincipalId();
		Date endDate = leaveCalendarDocumentHeader.getEndDate();
		if (DocumentStatus.ENROUTE.equals(newDocumentStatus)) {
			//create pending carry over leave blocks.
			
			Calendar calendar = TkServiceLocator.getCalendarService().getCalendarByPrincipalIdAndDate(principalId, endDate, true);
			
			if (calendar != null) {
				CalendarEntries calendarEntry = TkServiceLocator.getCalendarEntriesService().getCalendarEntriesByIdAndPeriodEndDate(calendar.getHrCalendarId(), endDate);
				
				TkServiceLocator.getAccrualCategoryMaxCarryOverService().calculateMaxCarryOver(documentId, principalId, calendarEntry, endDate);
			}
		}
		else if (DocumentStatus.FINAL.equals(newDocumentStatus)) {
			//approve the carry over leave block.
			List<LeaveBlock> leaveBlocks = TkServiceLocator.getLeaveBlockService().getLeaveBlocks(principalId, leaveCalendarDocumentHeader.getBeginDate(), endDate);
			for(LeaveBlock lb : leaveBlocks) {
				if(StringUtils.equals(lb.getLeaveBlockType(),LMConstants.LEAVE_BLOCK_TYPE.CARRY_OVER_ADJUSTMENT)) {
					lb.setRequestStatus(LMConstants.REQUEST_STATUS.APPROVED);
					TkServiceLocator.getLeaveBlockService().updateLeaveBlock(lb, lb.getPrincipalIdModified());
				}
			}
		}
	}

}