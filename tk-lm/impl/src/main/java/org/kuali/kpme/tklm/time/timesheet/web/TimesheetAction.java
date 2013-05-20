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
package org.kuali.kpme.tklm.time.timesheet.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionRedirect;
import org.joda.time.LocalDate;
import org.kuali.kpme.core.bo.calendar.entry.CalendarEntry;
import org.kuali.kpme.core.service.HrServiceLocator;
import org.kuali.kpme.core.util.HrContext;
import org.kuali.kpme.core.web.KPMEAction;
import org.kuali.kpme.tklm.common.TkConstants;
import org.kuali.kpme.tklm.time.detail.web.ActionFormUtils;
import org.kuali.kpme.tklm.time.service.TkServiceLocator;
import org.kuali.kpme.tklm.time.timesheet.TimesheetDocument;
import org.kuali.kpme.tklm.time.workflow.TimesheetDocumentHeader;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.exception.AuthorizationException;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;

public class TimesheetAction extends KPMEAction {

	private static final Logger LOG = Logger.getLogger(TimesheetAction.class);

    @Override
    protected void checkTKAuthorization(ActionForm form, String methodToCall) throws AuthorizationException {
    	String principalId = GlobalVariables.getUserSession().getPrincipalId();
    	String documentId = HrContext.getCurrentTimesheetDocumentId();
    	TimesheetDocument timesheetDocument = TkServiceLocator.getTimesheetService().getTimesheetDocument(documentId);
        if (!HrServiceLocator.getHRPermissionService().canViewCalendarDocument(principalId, timesheetDocument)) {
            throw new AuthorizationException(principalId, "TimesheetAction: docid: " + timesheetDocument.getDocumentId(), "");
        }
    }

    @Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		TimesheetActionForm taForm = (TimesheetActionForm) form;
		String documentId = taForm.getDocumentId();

        if (StringUtils.equals(request.getParameter("command"), "displayDocSearchView")
        		|| StringUtils.equals(request.getParameter("command"), "displayActionListView") ) {
        	documentId = (String) request.getParameter("docId");
        }

        LOG.debug("DOCID: " + documentId);

        // Here - viewPrincipal will be the principal of the user we intend to
        // view, be it target user, backdoor or otherwise.
        String viewPrincipal = HrContext.getTargetPrincipalId();
		CalendarEntry payCalendarEntry = HrServiceLocator.getCalendarService().getCurrentCalendarDates(viewPrincipal, new LocalDate().toDateTimeAtStartOfDay());

        // By handling the prev/next in the execute method, we are saving one
        // fetch/construction of a TimesheetDocument. If it were broken out into
        // methods, we would first fetch the current document, and then fetch
        // the next one instead of doing it in the single action.
		TimesheetDocument td;
        if (StringUtils.isNotBlank(documentId)) {
            td = TkServiceLocator.getTimesheetService().getTimesheetDocument(documentId);
        } else {
            // Default to whatever is active for "today".
            if (payCalendarEntry == null) {
                Principal prin = KimApiServiceLocator.getIdentityService().getPrincipal(viewPrincipal);
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, "clock.error.missing.payCalendar", prin.getPrincipalName());
                return super.execute(mapping, form, request, response);
                //throw new RuntimeException("No pay calendar entry for " + viewPrincipal);
            }
            td = TkServiceLocator.getTimesheetService().openTimesheetDocument(viewPrincipal, payCalendarEntry);
        }

        // Set the HrContext for the current timesheet document id.
        if (td != null) {
           setupDocumentOnFormContext(taForm, td);
        } else {
            LOG.error("Null timesheet document in TimesheetAction.");
        }
        


        // Do this at the end, so we load the document first,
        // then check security permissions via the superclass execution chain.
		return super.execute(mapping, form, request, response);
	}

    public ActionForward docHandler(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionForward forward = mapping.findForward("basic");
    	String command = request.getParameter("command");
    	
    	if (StringUtils.equals(command, "displayDocSearchView") || StringUtils.equals(command, "displayActionListView")) {
        	String docId = (String) request.getParameter("docId");
        	TimesheetDocument timesheetDocument = TkServiceLocator.getTimesheetService().getTimesheetDocument(docId);
        	String timesheetPrincipalName = KimApiServiceLocator.getPersonService().getPerson(timesheetDocument.getPrincipalId()).getPrincipalName();
        	
        	String principalId = HrContext.getTargetPrincipalId();
        	String principalName = KimApiServiceLocator.getPersonService().getPerson(principalId).getPrincipalName();
        	
        	StringBuilder builder = new StringBuilder();
        	if (!StringUtils.equals(principalName, timesheetPrincipalName)) {
            	if (StringUtils.equals(command, "displayDocSearchView")) {
            		builder.append("changeTargetPerson.do?methodToCall=changeTargetPerson");
            		builder.append("&documentId=");
            		builder.append(docId);
            		builder.append("&principalName=");
            		builder.append(timesheetPrincipalName);
            		builder.append("&targetUrl=TimeDetail.do");
            		builder.append("?docmentId=" + docId);
            		builder.append("&returnUrl=TimeApproval.do");
            	} else {
            		builder.append("TimeApproval.do");
            	}
        	} else {
        		builder.append("TimeDetail.do");
        		builder.append("?docmentId=" + docId);
        	}

        	forward = new ActionRedirect(builder.toString());
        }
    	
    	return forward;
    }

    protected void setupDocumentOnFormContext(TimesheetActionForm taForm, TimesheetDocument td) throws Exception{
    	String viewPrincipal = HrContext.getTargetPrincipalId();
    	HrContext.setCurrentTimesheetDocumentId(td.getDocumentId());
        HrContext.setCurrentTimesheetDocument(td);
	    taForm.setTimesheetDocument(td);
	    taForm.setDocumentId(td.getDocumentId());
        TimesheetDocumentHeader prevTdh = TkServiceLocator.getTimesheetDocumentHeaderService().getPrevOrNextDocumentHeader(TkConstants.PREV_TIMESHEET, viewPrincipal);
        TimesheetDocumentHeader nextTdh = TkServiceLocator.getTimesheetDocumentHeaderService().getPrevOrNextDocumentHeader(TkConstants.NEXT_TIMESHEET, viewPrincipal);

        taForm.setPrevDocumentId(prevTdh != null ? prevTdh.getDocumentId() : null);
        taForm.setNextDocumentId(nextTdh != null ? nextTdh.getDocumentId() : null);
      
        taForm.setPayCalendarDates(td.getCalendarEntry());
        taForm.setOnCurrentPeriod(ActionFormUtils.getOnCurrentPeriodFlag(taForm.getPayCalendarDates()));
        
    }

}