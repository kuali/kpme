/**
 * Copyright 2004-2014 The Kuali Foundation
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
package org.kuali.hr.time.missedpunch;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.kuali.hr.time.assignment.AssignmentDescriptionKey;
import org.kuali.hr.time.clocklog.ClockLog;
import org.kuali.hr.time.clocklog.TkClockActionValuesFinder;
import org.kuali.hr.time.service.base.TkServiceLocator;
import org.kuali.hr.time.timesheet.TimesheetDocument;
import org.kuali.hr.time.util.TKUser;
import org.kuali.hr.time.util.TkConstants;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.core.api.util.RiceConstants;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kns.web.struts.action.KualiTransactionalDocumentActionBase;
import org.kuali.rice.krad.exception.ValidationException;

public class MissedPunchAction extends KualiTransactionalDocumentActionBase {

    @Override
    public ActionForward docHandler(ActionMapping mapping, ActionForm form,
                                    HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        //HACK!!!
        MissedPunchForm mpForm = (MissedPunchForm) form;
        String command = mpForm.getCommand();
        if (StringUtils.equals(KewApiConstants.INITIATE_COMMAND, command)) {
            ((MissedPunchForm) form).setDocId(null);
        }
        //END HACK!!!!

        ActionForward act = super.docHandler(mapping, form, request, response);
        MissedPunchDocument mpDoc = (MissedPunchDocument) mpForm.getDocument();
        //mpForm.setDocId(mpDoc.getTimesheetDocumentId());

        if (StringUtils.equals(request.getParameter("command"), "initiate")) {
            String tdocId = request.getParameter("tdocid");
            TimesheetDocument timesheetDocument = TkServiceLocator.getTimesheetService().getTimesheetDocument(tdocId);
            mpForm.setDocNum(mpDoc.getDocumentNumber());
            mpDoc.setPrincipalId(timesheetDocument.getPrincipalId());
            mpDoc.setTimesheetDocumentId(tdocId);
            // set default document description

            if (StringUtils.isEmpty(mpDoc.getDocumentHeader().getDocumentDescription())) {
                mpDoc.getDocumentHeader().setDocumentDescription("Missed Punch: " + timesheetDocument.getPrincipalId());
            }
        }
        if (StringUtils.equals(request.getParameter("command"), "displayDocSearchView")
                || StringUtils.equals(request.getParameter("command"), "displayActionListView")
                || StringUtils.equals(command, "displaySuperUserView")) {
            TKUser.setTargetPerson(mpDoc.getPrincipalId());
            mpForm.setDocId(mpDoc.getDocumentNumber());
        }
//      mpForm.setAssignmentReadOnly(true);
        TkClockActionValuesFinder finder = new TkClockActionValuesFinder();
        List<KeyValue> keyLabels = (List<KeyValue>) finder.getKeyValues();
        if (keyLabels.size() == 2) {
//        		&& !mpForm.getDocumentActions().containsKey(KNSConstants.KUALI_ACTION_CAN_EDIT)) {
            Set<String> actions = TkConstants.CLOCK_ACTION_TRANSITION_MAP.get(TkConstants.CLOCK_IN);
            boolean flag = true;
            for (String entry : actions) {
                if (!keyLabels.contains(new ConcreteKeyValue(entry, TkConstants.CLOCK_ACTION_STRINGS.get(entry)))) {
                    flag = false;
                }
            }
            if (flag) {
                mpForm.setAssignmentReadOnly(true);
            }
        } else if (keyLabels.size() == 1) {
            Set<String> actions = TkConstants.CLOCK_ACTION_TRANSITION_MAP.get(TkConstants.LUNCH_IN);
            boolean flag = true;
            for (String entry : actions) {
                if (!keyLabels.contains(new ConcreteKeyValue(entry, TkConstants.CLOCK_ACTION_STRINGS.get(entry)))) {
                    flag = false;
                }
            }
            if (flag) {
                mpForm.setAssignmentReadOnly(true);
            }
        }
        
        ClockLog lastClock = TkServiceLocator.getClockLogService().getLastClockLog(TKUser.getCurrentTargetPersonId());
        if (lastClock != null && !StringUtils.equals(lastClock.getClockAction(), TkConstants.CLOCK_OUT)) {
        	MissedPunchDocument lastDoc = TkServiceLocator.getMissedPunchService().getMissedPunchByClockLogId(lastClock.getTkClockLogId());
            // Default the assignment if last clock was a clock in.
            defaultMissedPunchAssignment(mpDoc, lastDoc, lastClock);
            mpForm.setAssignmentReadOnly(true);
        } else {
        	mpForm.setAssignmentReadOnly(false);
        }

        return act;
    }

    private void defaultMissedPunchAssignment(MissedPunchDocument mpDoc, MissedPunchDocument lastDoc, ClockLog lastClock) {
        if (lastDoc != null) {    // last action was a missed punch
            mpDoc.setAssignment(lastDoc.getAssignment());
        } else {    // last action was not a missed punch
            AssignmentDescriptionKey adk = new AssignmentDescriptionKey(lastClock.getJobNumber().toString(), lastClock.getWorkArea().toString(), lastClock.getTask().toString());
            mpDoc.setAssignment(adk.toAssignmentKeyString());
        }
    }

    @Override
    public ActionForward route(ActionMapping mapping, ActionForm form,
                               HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        MissedPunchForm mpForm = (MissedPunchForm) form;
        mpForm.setEditingMode(new HashMap());
        MissedPunchDocument mpDoc = (MissedPunchDocument) mpForm.getDocument();
        mpDoc.setDocumentStatus("R");
        request.setAttribute(TkConstants.DOCUMENT_ID_REQUEST_NAME, mpDoc.getDocumentNumber());
        request.setAttribute(TkConstants.TIMESHEET_DOCUMENT_ID_REQUEST_NAME, mpDoc.getTimesheetDocumentId());
        //TkServiceLocator.getMissedPunchService().addClockLogForMissedPunch(mpDoc);
        mpForm.setDocId(mpDoc.getDocumentNumber());
        mpForm.setAssignmentReadOnly(true);
        try {
            return super.route(mapping, mpForm, request, response);
        } catch (ValidationException e) {
            mpForm.setAssignmentReadOnly(false);
            return mapping.findForward(RiceConstants.MAPPING_BASIC);
        }
    }

    @Override
    public ActionForward approve(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        MissedPunchForm mpForm = (MissedPunchForm) form;
        MissedPunchDocument mpDoc = (MissedPunchDocument) mpForm.getDocument();
        mpDoc.setDocumentStatus("A");
        mpForm.setAssignmentReadOnly(true);
        request.setAttribute(TkConstants.DOCUMENT_ID_REQUEST_NAME, mpDoc.getDocumentNumber());
        request.setAttribute(TkConstants.TIMESHEET_DOCUMENT_ID_REQUEST_NAME, mpDoc.getTimesheetDocumentId());
        TkServiceLocator.getMissedPunchService().updateClockLogAndTimeBlockIfNecessary(mpDoc);
        return super.approve(mapping, form, request, response);
    }
    
  @Override
  public ActionForward reload(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	  MissedPunchForm mpForm = (MissedPunchForm) form;
      MissedPunchDocument mpDoc = (MissedPunchDocument) mpForm.getDocument();
      request.setAttribute(TkConstants.DOCUMENT_ID_REQUEST_NAME, mpDoc.getDocumentNumber());
      request.setAttribute(TkConstants.TIMESHEET_DOCUMENT_ID_REQUEST_NAME, mpDoc.getTimesheetDocumentId());
  	  return super.reload(mapping, form, request, response);
  }

    @Override
    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        MissedPunchForm mpForm = (MissedPunchForm) form;
        mpForm.setEditingMode(new HashMap());
        MissedPunchDocument mpDoc = (MissedPunchDocument) mpForm.getDocument();
        mpDoc.setDocumentStatus("S");
        request.setAttribute(TkConstants.DOCUMENT_ID_REQUEST_NAME, mpDoc.getDocumentNumber());
        request.setAttribute(TkConstants.TIMESHEET_DOCUMENT_ID_REQUEST_NAME, mpDoc.getTimesheetDocumentId());
        mpForm.setDocId(mpDoc.getDocumentNumber());
        mpForm.setAssignmentReadOnly(true);
        try {
            return super.save(mapping, mpForm, request, response);
        } catch (ValidationException e) {
            mpForm.setAssignmentReadOnly(false);
            return mapping.findForward(RiceConstants.MAPPING_BASIC);
        }

    }
}