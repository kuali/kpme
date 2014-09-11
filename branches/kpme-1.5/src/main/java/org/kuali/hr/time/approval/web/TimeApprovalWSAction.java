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
package org.kuali.hr.time.approval.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.collect.Lists;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hsqldb.lib.StringUtil;
import org.json.simple.JSONValue;
import org.kuali.hr.lm.leaveblock.LeaveBlock;
import org.kuali.hr.time.assignment.Assignment;
import org.kuali.hr.time.base.web.ApprovalForm;
import org.kuali.hr.time.base.web.TkAction;
import org.kuali.hr.time.detail.web.ActionFormUtils;
import org.kuali.hr.time.service.base.TkServiceLocator;
import org.kuali.hr.time.timeblock.TimeBlock;
import org.kuali.hr.time.timesheet.TimesheetDocument;
import org.kuali.hr.time.timesummary.AssignmentColumn;
import org.kuali.hr.time.timesummary.AssignmentRow;
import org.kuali.hr.time.timesummary.EarnCodeSection;
import org.kuali.hr.time.timesummary.EarnGroupSection;
import org.kuali.hr.time.timesummary.TimeSummary;
import org.kuali.hr.time.util.TKContext;
import org.kuali.hr.time.util.TKUtils;
import org.kuali.hr.time.workflow.TimesheetDocumentHeader;

public class TimeApprovalWSAction extends TkAction {

    private static final Logger LOG = Logger.getLogger(TimeApprovalWSAction.class);

    /**
     * Action called via AJAX. (ajaj really...)
     * <p/>
     * This search returns quick-results to the search box for the user to further
     * refine upon. The end value can then be form submitted.
     */
    public ActionForward searchApprovalRows(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TimeApprovalActionForm taaf = (TimeApprovalActionForm) form;
        List<Map<String, String>> results = new LinkedList<Map<String, String>>();
        if(StringUtils.isNotEmpty(taaf.getPayBeginDateForSearch()) 
        		&& StringUtils.isNotEmpty(taaf.getPayEndDateForSearch()) ) {
	        Date beginDate = new SimpleDateFormat("MM/dd/yyyy").parse(taaf.getPayBeginDateForSearch());
	        Date endDate = new SimpleDateFormat("MM/dd/yyyy").parse(taaf.getPayEndDateForSearch());
            //the endDate we get here is coming from approval.js and is extracted from html. we need to add a day to cover the last day in the pay period.
            endDate = DateUtils.addDays(endDate,1);
            List<String> workAreaList = new ArrayList<String>();
	        if(StringUtil.isEmpty(taaf.getSelectedWorkArea())) {
                Set<Long> workAreas = TkServiceLocator.getTkRoleService().getWorkAreasForApprover(TKContext.getTargetPrincipalId(), TKUtils.getCurrentDate());
	        	for(Long workArea : workAreas) {    //taaf.getWorkAreaDescr().keySet()
	        		workAreaList.add(workArea.toString());
	        	}
	        } else {
	        	workAreaList.add(taaf.getSelectedWorkArea());
	        }
	        List<String> principalIds = TkServiceLocator.getTimeApproveService()
				.getTimePrincipalIdsWithSearchCriteria(workAreaList, taaf.getSelectedPayCalendarGroup(),
					new java.sql.Date(endDate.getTime()), new java.sql.Date(beginDate.getTime()), new java.sql.Date(endDate.getTime())); 
	        
	        
	        if (StringUtils.equals(taaf.getSearchField(), ApprovalForm.ORDER_BY_PRINCIPAL)) {
	            for (String id : principalIds) {
	                if(StringUtils.contains(id, taaf.getSearchTerm())) {
	                    Map<String, String> labelValue = new HashMap<String, String>();
	                    labelValue.put("id", id);
	                    labelValue.put("result", id);
	                    results.add(labelValue);
	                }
	            }
	        } else if (StringUtils.equals(taaf.getSearchField(), ApprovalForm.ORDER_BY_DOCID)) {
	            Map<String, TimesheetDocumentHeader> principalDocumentHeaders =
	            		TkServiceLocator.getTimeApproveService().getPrincipalDocumentHeader(principalIds, beginDate, endDate);
	            
	            for (Map.Entry<String,TimesheetDocumentHeader> entry : principalDocumentHeaders.entrySet()) {
	                if (StringUtils.contains(entry.getValue().getDocumentId(), taaf.getSearchTerm())) {
	                    Map<String, String> labelValue = new HashMap<String, String>();
//                        labelValue.put("id", entry.getValue().getDocumentId() + " (" + entry.getValue().getPrincipalId() + ")");
                        labelValue.put("id", entry.getValue().getDocumentId());
	                    labelValue.put("result", entry.getValue().getPrincipalId());
	                    results.add(labelValue);
	                }
	            }
	        }
        }
     
        taaf.setOutputString(JSONValue.toJSONString(results));
        return mapping.findForward("ws");
    }
    
    public ActionForward getTimeSummary(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TimeApprovalActionForm taaf = (TimeApprovalActionForm) form;
        TimesheetDocument td = TkServiceLocator.getTimesheetService().getTimesheetDocument(taaf.getDocumentId());
		TimeSummary ts = TkServiceLocator.getTimeSummaryService().getTimeSummary(td);
        List<Assignment> assignments = td.getAssignments();
        List<String> assignmentKeys = new ArrayList<String>();
        for(Assignment assignment : assignments) {
        	assignmentKeys.add(assignment.getAssignmentKey());
        }
		List<TimeBlock> timeBlocks = td.getTimeBlocks();
        List<LeaveBlock> leaveBlocks = TkServiceLocator.getLeaveBlockService().getLeaveBlocksForTimeCalendar(td.getPrincipalId(), td.getAsOfDate(), td.getDocEndDate(), assignmentKeys);
        Map<String, String> aMap = ActionFormUtils.buildAssignmentStyleClassMap(timeBlocks, leaveBlocks);
        // set css classes for each assignment row
        for (EarnGroupSection earnGroupSection : ts.getSections()) {
            for (EarnCodeSection section : earnGroupSection.getEarnCodeSections()) {
                for (AssignmentRow assignRow : section.getAssignmentsRows()) {
                	String assignmentCssStyle = MapUtils.getString(aMap, assignRow.getAssignmentKey());
                	assignRow.setCssClass(assignmentCssStyle);
                	for (AssignmentColumn assignmentColumn : assignRow.getAssignmentColumns()) {
                		assignmentColumn.setCssClass(assignmentCssStyle);
                	}
                }
            }
        }

        //reverse sections for javascripts $parent.after (always inserting directly after parent element, which reverses order)

        ts.setSections(Lists.reverse(ts.getSections()));
        taaf.setOutputString(ts.toJsonString());
        return mapping.findForward("ws");
    }
        
}