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
package org.kuali.hr.lm.approval.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hsqldb.lib.StringUtil;
import org.json.simple.JSONValue;
import org.kuali.hr.lm.workflow.LeaveCalendarDocumentHeader;
import org.kuali.hr.time.base.web.TkAction;
import org.kuali.hr.time.base.web.ApprovalForm;
import org.kuali.hr.time.person.TKPerson;
import org.kuali.hr.time.service.base.TkServiceLocator;

public class LeaveApprovalWSAction extends TkAction {

	 public ActionForward getLeaveSummary(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	LeaveApprovalWSActionForm laaf = (LeaveApprovalWSActionForm) form;
    	String docId = laaf.getDocumentId();
    	LeaveCalendarDocumentHeader lcdh = TkServiceLocator.getLeaveCalendarDocumentHeaderService().getDocumentHeader(docId);
    	if(lcdh != null) {
    		List<Map<String, Object>> detailMap = TkServiceLocator.getLeaveApprovalService().getLeaveApprovalDetailSections(lcdh);
    		
    		String jsonString = JSONValue.toJSONString(detailMap);
    		laaf.setOutputString(jsonString);
    	}
    	
    	return mapping.findForward("ws");
	 }
	 
	  public ActionForward searchApprovalRows(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		  LeaveApprovalWSActionForm laaf = (LeaveApprovalWSActionForm) form;
	        List<Map<String, String>> results = new LinkedList<Map<String, String>>();
	        if(StringUtils.isNotEmpty(laaf.getPayBeginDateForSearch()) 
	        		&& StringUtils.isNotEmpty(laaf.getPayEndDateForSearch()) ) {
		        Date beginDate = new SimpleDateFormat("MM/dd/yyyy").parse(laaf.getPayBeginDateForSearch());
		        Date endDate = new SimpleDateFormat("MM/dd/yyyy").parse(laaf.getPayEndDateForSearch());
		        
		        List<String> workAreaList = new ArrayList<String>();
		        if(StringUtil.isEmpty(laaf.getSelectedWorkArea())) {
		        	for(Long aKey : laaf.getWorkAreaDescr().keySet()) {
		        		workAreaList.add(aKey.toString());
		        	}
		        } else {
		        	workAreaList.add(laaf.getSelectedWorkArea());
		        } 
		        List<String> principalIds = TkServiceLocator.getLeaveApprovalService()
        			.getLeavePrincipalIdsWithSearchCriteria(workAreaList, laaf.getSelectedPayCalendarGroup(),
        					new java.sql.Date(endDate.getTime()), new java.sql.Date(beginDate.getTime()), new java.sql.Date(endDate.getTime())); 
		        
		        List<TKPerson> persons = TkServiceLocator.getPersonService().getPersonCollection(principalIds);
		        
		        if (StringUtils.equals(laaf.getSearchField(), ApprovalForm.ORDER_BY_PRINCIPAL)) {
		            for (String id : principalIds) {
		                if(StringUtils.contains(id, laaf.getSearchTerm())) {
		                    Map<String, String> labelValue = new HashMap<String, String>();
		                    labelValue.put("id", id);
		                    labelValue.put("result", id);
		                    results.add(labelValue);
		                }
		            }
		        } else if (StringUtils.equals(laaf.getSearchField(), ApprovalForm.ORDER_BY_DOCID)) {
		            Map<String, LeaveCalendarDocumentHeader> principalDocumentHeaders =
		                    TkServiceLocator.getLeaveApprovalService().getPrincipalDocumehtHeader(persons, beginDate, endDate);
	
		            for (Map.Entry<String,LeaveCalendarDocumentHeader> entry : principalDocumentHeaders.entrySet()) {
		                if (StringUtils.contains(entry.getValue().getDocumentId(), laaf.getSearchTerm())) {
		                    Map<String, String> labelValue = new HashMap<String, String>();
		                    labelValue.put("id", entry.getValue().getDocumentId() + " (" + entry.getValue().getPrincipalId() + ")");
		                    labelValue.put("result", entry.getValue().getPrincipalId());
		                    results.add(labelValue);
		                }
		            }
		        }
	        }
		
	      laaf.setOutputString(JSONValue.toJSONString(results));
	        
	      return mapping.findForward("ws");
	    }
}