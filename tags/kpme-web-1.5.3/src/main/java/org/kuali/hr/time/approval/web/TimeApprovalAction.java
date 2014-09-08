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
package org.kuali.hr.time.approval.web;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.displaytag.tags.TableTagParameters;
import org.displaytag.util.ParamEncoder;
import org.hsqldb.lib.StringUtil;
import org.kuali.hr.core.document.calendar.CalendarDocumentContract;
import org.kuali.hr.time.assignment.Assignment;
import org.kuali.hr.time.base.web.ApprovalAction;
import org.kuali.hr.time.base.web.ApprovalForm;
import org.kuali.hr.time.calendar.Calendar;
import org.kuali.hr.time.calendar.CalendarEntries;
import org.kuali.hr.time.detail.web.ActionFormUtils;
import org.kuali.hr.time.service.base.TkServiceLocator;
import org.kuali.hr.time.timesheet.TimesheetDocument;
import org.kuali.hr.time.util.TKContext;
import org.kuali.hr.time.util.TKUser;
import org.kuali.hr.time.util.TKUtils;
import org.kuali.hr.time.util.TkConstants;
import org.kuali.hr.time.workarea.WorkArea;
import org.kuali.hr.time.workflow.TimesheetDocumentHeader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;

public class TimeApprovalAction extends ApprovalAction{
	
	public ActionForward searchResult(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		TimeApprovalActionForm taaf = (TimeApprovalActionForm)form;
		
        if (StringUtils.equals("documentId", taaf.getSearchField())) {
        	TimesheetDocumentHeader tdh = TkServiceLocator.getTimesheetDocumentHeaderService().getDocumentHeader(taaf.getSearchTerm());
        	taaf.setSearchTerm(tdh != null ? tdh.getPrincipalId() : StringUtils.EMPTY);
        }
        
    	taaf.setSearchField("principalId");
        List<String> principalIds = new ArrayList<String>();
        principalIds.add(taaf.getSearchTerm());
        
        if (principalIds.isEmpty()) {
        	taaf.setApprovalRows(new ArrayList<ApprovalTimeSummaryRow>());
        	taaf.setResultSize(0);
        } else {
        	taaf.setResultSize(principalIds.size());	
	        taaf.setApprovalRows(getApprovalRows(request, taaf, principalIds));
	        
        	CalendarEntries payCalendarEntries = TkServiceLocator.getCalendarEntriesService().getCalendarEntries(taaf.getHrPyCalendarEntriesId());
   	        taaf.setPayCalendarEntries(payCalendarEntries);
   	        taaf.setPayCalendarLabels(TkServiceLocator.getTimeSummaryService().getHeaderForSummary(payCalendarEntries, new ArrayList<Boolean>()));
        	
	        List<Assignment> assignments = TkServiceLocator.getAssignmentService().getAssignments(taaf.getSearchTerm(), payCalendarEntries.getEndPeriodDate());
	        if(!assignments.isEmpty()){
	        	 for(Long wa : taaf.getWorkAreaDescr().keySet()){
	        		for (Assignment assign : assignments) {
		             	if (assign.getWorkArea().toString().equals(wa.toString())) {
		             		taaf.setSelectedWorkArea(wa.toString());
		             		break;
		             	}
	        		}
	             }
	        }
        }
 
		return mapping.findForward("basic");
	}
	
    public ActionForward approve(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TimeApprovalActionForm taaf = (TimeApprovalActionForm) form;
        //let's save the page we are on!
        List<ApprovalTimeSummaryRow> lstApprovalRows = taaf.getApprovalRows();

        for (ApprovalTimeSummaryRow ar : lstApprovalRows) {
            if (ar.isApprovable() && StringUtils.equals(ar.getSelected(), "on")) {
                String documentNumber = ar.getDocumentId();
                TimesheetDocument tDoc = TkServiceLocator.getTimesheetService().getTimesheetDocument(documentNumber);
                TkServiceLocator.getTimesheetService().approveTimesheet(TKContext.getTargetPrincipalId(), tDoc);
            }
        }
        return mapping.findForward("basic");
    }
    
	public ActionForward selectNewDept(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		TimeApprovalActionForm taaf = (TimeApprovalActionForm)form;
		taaf.setSearchField(null);
		taaf.setSearchTerm(null);

        CalendarEntries payCalendarEntries = TkServiceLocator.getCalendarEntriesService().getCalendarEntries(taaf.getHrPyCalendarEntriesId());
        taaf.setPayCalendarEntries(payCalendarEntries);
        taaf.setPayCalendarLabels(TkServiceLocator.getTimeSummaryService().getHeaderForSummary(payCalendarEntries, new ArrayList<Boolean>()));

		taaf.getWorkAreaDescr().clear();
    	List<WorkArea> workAreas = TkServiceLocator.getWorkAreaService().getWorkAreas(taaf.getSelectedDept(), new java.sql.Date(taaf.getPayBeginDate().getTime()));
        for(WorkArea wa : workAreas){
        	if (TKUser.getApproverWorkAreas().contains(wa.getWorkArea())
        			|| TKUser.getReviewerWorkAreas().contains(wa.getWorkArea())) {
        		taaf.getWorkAreaDescr().put(wa.getWorkArea(),wa.getDescription()+"("+wa.getWorkArea()+")");
        	}
        }

        List<String> principalIds = this.getPrincipalIdsToPopulateTable(taaf); 
    	if (principalIds.isEmpty()) {
    		taaf.setApprovalRows(new ArrayList<ApprovalTimeSummaryRow>());
    		taaf.setResultSize(0);
    	}
    	else {
            List<ApprovalTimeSummaryRow> approvalTimeSummaryRows = getApprovalRows(request, taaf, principalIds);
            setFormSubsetOfApprovalRows(taaf, getPage(request, taaf), approvalTimeSummaryRows);
    	}
    	
    	this.populateCalendarAndPayPeriodLists(request, taaf);
		return mapping.findForward("basic");
	}
	
	public ActionForward selectNewWorkArea(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		TimeApprovalActionForm taaf = (TimeApprovalActionForm)form;
		taaf.setSearchField(null);
		taaf.setSearchTerm(null);

	    CalendarEntries payCalendarEntries = TkServiceLocator.getCalendarEntriesService().getCalendarEntries(taaf.getHrPyCalendarEntriesId());
        taaf.setPayCalendarLabels(TkServiceLocator.getTimeSummaryService().getHeaderForSummary(payCalendarEntries, new ArrayList<Boolean>()));
        
        List<String> principalIds = this.getPrincipalIdsToPopulateTable(taaf); 
		if (principalIds.isEmpty()) {
			taaf.setApprovalRows(new ArrayList<ApprovalTimeSummaryRow>());
			taaf.setResultSize(0);
		}
		else {
            List<ApprovalTimeSummaryRow> approvalTimeSummaryRows = getApprovalRows(request, taaf, principalIds);
            setFormSubsetOfApprovalRows(taaf, getPage(request, taaf), approvalTimeSummaryRows);
	    }
		return mapping.findForward("basic");
	}
	
	@Override
	public ActionForward loadApprovalTab(ActionMapping mapping, ActionForm form,
	HttpServletRequest request, HttpServletResponse response)
				throws Exception {
		ActionForward fwd = mapping.findForward("basic");
        TimeApprovalActionForm taaf = (TimeApprovalActionForm) form;
        Date currentDate = null;
        CalendarEntries payCalendarEntries = null;
        Calendar currentPayCalendar = null;
        //String page = request.getParameter((new ParamEncoder(TkConstants.APPROVAL_TABLE_ID).encodeParameterName(TableTagParameters.PARAMETER_PAGE)));
        String page = getPage(request, taaf);

        //reset state
        if(StringUtils.isBlank(taaf.getSelectedDept())){
        	resetState(form, request);
        }

        TimesheetDocument td = null;
        if (taaf.getDocumentId() != null) {
            td = TkServiceLocator.getTimesheetService().getTimesheetDocument(taaf.getDocumentId());
        }

        // Set calendar groups
        List<String> calGroups = TkServiceLocator.getPrincipalHRAttributeService().getUniqueTimePayGroups();
        taaf.setPayCalendarGroups(calGroups);

        if (StringUtils.isBlank(taaf.getSelectedPayCalendarGroup())) {
            if (td == null) {
                taaf.setSelectedPayCalendarGroup(calGroups.get(0));
            } else {
                taaf.setSelectedPayCalendarGroup(td.getCalendarEntry().getCalendarName());
            }
        }
        
        // Set current pay calendar entries if present. Decide if the current date should be today or the end period date
        if (taaf.getHrPyCalendarEntriesId() != null) {
        	payCalendarEntries = TkServiceLocator.getCalendarEntriesService().getCalendarEntries(taaf.getHrPyCalendarEntriesId());
        } else {
            if (td == null) {
                currentDate = TKUtils.getTimelessDate(null);
                currentPayCalendar = TkServiceLocator.getCalendarService().getCalendarByGroup(taaf.getSelectedPayCalendarGroup());
                payCalendarEntries = TkServiceLocator.getCalendarEntriesService().getCurrentCalendarEntriesByCalendarId(currentPayCalendar.getHrCalendarId(), currentDate);
            } else {
                payCalendarEntries = td.getCalendarEntry();
            }
        }
        taaf.setPayCalendarEntries(payCalendarEntries);
        
        
        if(taaf.getPayCalendarEntries() != null) {
	        populateCalendarAndPayPeriodLists(request, taaf);
        }

        setupDocumentOnFormContext(request,taaf,payCalendarEntries, page, td);
        return fwd;
	}

	@Override
	protected void setupDocumentOnFormContext(HttpServletRequest request, ApprovalForm form, CalendarEntries payCalendarEntries, String page, CalendarDocumentContract calDoc) {
		super.setupDocumentOnFormContext(request, form, payCalendarEntries, page, calDoc);
		TimeApprovalActionForm taaf = (TimeApprovalActionForm) form;
		taaf.setPayCalendarLabels(TkServiceLocator.getTimeSummaryService().getHeaderForSummary(payCalendarEntries, new ArrayList<Boolean>()));

		List<String> principalIds = this.getPrincipalIdsToPopulateTable(taaf);
		if (principalIds.isEmpty()) {
			taaf.setApprovalRows(new ArrayList<ApprovalTimeSummaryRow>());
			taaf.setResultSize(0);
		} else {
		    List<ApprovalTimeSummaryRow> approvalRows = getApprovalRows(request, taaf, principalIds);
            setFormSubsetOfApprovalRows(taaf, page, approvalRows);
		}


		taaf.setOnCurrentPeriod(ActionFormUtils.getOnCurrentPeriodFlag(taaf.getPayCalendarEntries()));
	}

    private void setFormSubsetOfApprovalRows(TimeApprovalActionForm taaf, String page, List<ApprovalTimeSummaryRow> approvalRows) {
        Integer beginIndex = StringUtils.isBlank(page) || StringUtils.equals(page, "1") ? 0 : (Integer.parseInt(page) - 1)*TkConstants.PAGE_SIZE;
        Integer endIndex = beginIndex + TkConstants.PAGE_SIZE > approvalRows.size() ? approvalRows.size() : beginIndex + TkConstants.PAGE_SIZE;
        if (beginIndex > endIndex
                || beginIndex >= approvalRows.size()) {
            beginIndex = StringUtils.isBlank(page) || StringUtils.equals(page, "1") ? 0 : (Integer.parseInt(page) - 1)*TkConstants.PAGE_SIZE;
            endIndex = beginIndex + TkConstants.PAGE_SIZE > approvalRows.size() ? approvalRows.size() : beginIndex + TkConstants.PAGE_SIZE;
        }
        taaf.setApprovalRows(approvalRows.subList(beginIndex, endIndex));
        taaf.setResultSize(approvalRows.size());
    }


	
	public ActionForward selectNewPayCalendar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// resets the common fields for approval pages
		super.resetMainFields(form);
		TimeApprovalActionForm taaf = (TimeApprovalActionForm)form;
		// KPME-909
        taaf.setApprovalRows(new ArrayList<ApprovalTimeSummaryRow>());
		return loadApprovalTab(mapping, form, request, response);
	}
	
    /**
     * Helper method to modify / manage the list of records needed to display approval data to the user.
     *
     * @param taaf
     * @return
     */
    protected List<ApprovalTimeSummaryRow> getApprovalRows(HttpServletRequest request, TimeApprovalActionForm taaf, List<String> assignmentPrincipalIds  ) {
        List<ApprovalTimeSummaryRow> approvalRows = TkServiceLocator.getTimeApproveService().getApprovalSummaryRows(taaf.getPayBeginDate(), taaf.getPayEndDate(), taaf.getSelectedPayCalendarGroup(), assignmentPrincipalIds, taaf.getPayCalendarLabels(), taaf.getPayCalendarEntries());
        final String sortField = getSortField(request, taaf);
        if (StringUtils.isEmpty(sortField) ||
                StringUtils.equals(sortField, "name")) {
            final boolean sortNameAscending = isAscending(request, taaf);
            Collections.sort(approvalRows, new Comparator<ApprovalTimeSummaryRow>() {
                @Override
                public int compare(ApprovalTimeSummaryRow row1, ApprovalTimeSummaryRow row2) {
                    if (sortNameAscending) {
                        return ObjectUtils.compare(StringUtils.lowerCase(row1.getName()), StringUtils.lowerCase(row2.getName()));
                    } else {
                        return ObjectUtils.compare(StringUtils.lowerCase(row2.getName()), StringUtils.lowerCase(row1.getName()));
                    }
                }
            });
        } else if (StringUtils.equals(sortField, "documentID")) {
            final boolean sortDocumentIdAscending = isAscending(request, taaf);
            Collections.sort(approvalRows, new Comparator<ApprovalTimeSummaryRow>() {
                @Override
                public int compare(ApprovalTimeSummaryRow row1, ApprovalTimeSummaryRow row2) {
                    if (sortDocumentIdAscending) {
                        return ObjectUtils.compare(NumberUtils.toInt(row1.getDocumentId()), NumberUtils.toInt(row2.getDocumentId()));
                    } else {
                        return ObjectUtils.compare(NumberUtils.toInt(row2.getDocumentId()), NumberUtils.toInt(row1.getDocumentId()));
                    }
                }
            });
        } else if (StringUtils.equals(sortField, "status")) {
            final boolean sortStatusIdAscending = isAscending(request, taaf);
            Collections.sort(approvalRows, new Comparator<ApprovalTimeSummaryRow>() {
                @Override
                public int compare(ApprovalTimeSummaryRow row1, ApprovalTimeSummaryRow row2) {
                    if (sortStatusIdAscending) {
                        return ObjectUtils.compare(StringUtils.lowerCase(row1.getApprovalStatus()), StringUtils.lowerCase(row2.getApprovalStatus()));
                    } else {
                        return ObjectUtils.compare(StringUtils.lowerCase(row2.getApprovalStatus()), StringUtils.lowerCase(row1.getApprovalStatus()));
                    }
                }
            });
        }
        return approvalRows;
    }
	
    public void resetState(ActionForm form, HttpServletRequest request) {
    	  TimeApprovalActionForm taaf = (TimeApprovalActionForm) form;
 	      //String page = request.getParameter((new ParamEncoder(TkConstants.APPROVAL_TABLE_ID).encodeParameterName(TableTagParameters.PARAMETER_PAGE)));
 	      String page = getPage(request, taaf);
 	      if (StringUtils.isBlank(page)) {
 			  taaf.getDepartments().clear();
 			  taaf.getWorkAreaDescr().clear();
 			  taaf.setApprovalRows(new ArrayList<ApprovalTimeSummaryRow>());
 			  taaf.setSelectedDept(null);
 			  taaf.setSearchField(null);
 			  taaf.setSearchTerm(null);
 	      }
	}
	
    @Override
    protected void populateCalendarAndPayPeriodLists(HttpServletRequest request, ApprovalForm taf) {
    	TimeApprovalActionForm taaf = (TimeApprovalActionForm)taf;
		// set calendar year list
		Set<String> yearSet = new HashSet<String>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		// if selected calendar year is passed in
		if(!StringUtils.isEmpty(request.getParameter("selectedCY"))) {
			taaf.setSelectedCalendarYear(request.getParameter("selectedCY").toString());
		} else {
			taaf.setSelectedCalendarYear(sdf.format(taaf.getPayCalendarEntries().getBeginPeriodDate()));
		}
		
		List<CalendarEntries> pcListForYear = new ArrayList<CalendarEntries>();
		List<CalendarEntries> pceList = TkServiceLocator.getTimeApproveService()
			.getAllPayCalendarEntriesForApprover(TKContext.getPrincipalId(), TKUtils.getTimelessDate(null));
	    for(CalendarEntries pce : pceList) {
	    	yearSet.add(sdf.format(pce.getBeginPeriodDate()));
	    	if(sdf.format(pce.getBeginPeriodDate()).equals(taaf.getSelectedCalendarYear())) {
	    		pcListForYear.add(pce);
	    	}
	    }
	    List<String> yearList = new ArrayList<String>(yearSet);
	    Collections.sort(yearList);
	    Collections.reverse(yearList);	// newest on top
	    taaf.setCalendarYears(yearList);
		
		// set pay period list contents
		if(!StringUtils.isEmpty(request.getParameter("selectedPP"))) {
			taaf.setSelectedPayPeriod(request.getParameter("selectedPP").toString());
		} else {
			taaf.setSelectedPayPeriod(taaf.getPayCalendarEntries().getHrCalendarEntriesId());
			taaf.setPayPeriodsMap(ActionFormUtils.getPayPeriodsMap(pcListForYear, null));
		}
		if(taaf.getPayPeriodsMap().isEmpty()) {
		    taaf.setPayPeriodsMap(ActionFormUtils.getPayPeriodsMap(pcListForYear, null));
		}
	}
    
    private List<String> getPrincipalIdsToPopulateTable(TimeApprovalActionForm taf) {
        if (taf.getPayBeginDate() == null
                && taf.getPayEndDate() == null) {
            return Collections.emptyList();
        }
        List<String> workAreaList = new ArrayList<String>();
        if(StringUtil.isEmpty(taf.getSelectedWorkArea())) {
        	for(Long aKey : taf.getWorkAreaDescr().keySet()) {
        		workAreaList.add(aKey.toString());
        	}
        } else {
        	workAreaList.add(taf.getSelectedWorkArea());
        }
        java.sql.Date endDate = new java.sql.Date(taf.getPayEndDate().getTime());
        java.sql.Date beginDate = new java.sql.Date(taf.getPayBeginDate().getTime());

        List<String> idList = TkServiceLocator.getTimeApproveService()
        		.getTimePrincipalIdsWithSearchCriteria(workAreaList, taf.getSelectedPayCalendarGroup(), endDate, beginDate, endDate);      
        return idList;
	}	
}