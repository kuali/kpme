package org.kuali.hr.time.approval.web;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.displaytag.tags.TableTagParameters;
import org.displaytag.util.ParamEncoder;
import org.kuali.hr.time.assignment.Assignment;
import org.kuali.hr.time.base.web.TkAction;
import org.kuali.hr.time.detail.web.ActionFormUtils;
import org.kuali.hr.time.paycalendar.PayCalendar;
import org.kuali.hr.time.paycalendar.PayCalendarEntries;
import org.kuali.hr.time.person.TKPerson;
import org.kuali.hr.time.roles.UserRoles;
import org.kuali.hr.time.service.base.TkServiceLocator;
import org.kuali.hr.time.timesheet.TimesheetDocument;
import org.kuali.hr.time.util.TKContext;
import org.kuali.hr.time.util.TKUser;
import org.kuali.hr.time.util.TKUtils;
import org.kuali.hr.time.util.TkConstants;
import org.kuali.hr.time.workarea.WorkArea;
import org.kuali.hr.time.workflow.TimesheetDocumentHeader;
import org.kuali.rice.kns.exception.AuthorizationException;

public class TimeApprovalAction extends TkAction{
	
	public ActionForward searchResult(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		TimeApprovalActionForm taaf = (TimeApprovalActionForm)form;
		String searchField = taaf.getSearchField();
		String searchTerm = taaf.getSearchTerm();
		String principalId;
		
        if(StringUtils.equals("documentId", searchField)){
        	TimesheetDocumentHeader tdh = TkServiceLocator.getTimesheetDocumentHeaderService().getDocumentHeader(searchTerm);
        	principalId = tdh.getPrincipalId();
        } else {
        	principalId = searchTerm;
        }
    	taaf.setSearchField("principalId");
    	taaf.setSearchTerm(principalId);
       
        List<String> principalIds = new ArrayList<String>();
        principalIds.add(principalId);
        List<TKPerson> persons = TkServiceLocator.getPersonService().getPersonCollection(principalIds);
        if (persons.isEmpty()) {
        	taaf.setApprovalRows(new ArrayList<ApprovalTimeSummaryRow>());
        	taaf.setResultSize(0);
        } else {
        	taaf.setResultSize(persons.size());	
	        taaf.setApprovalRows(getApprovalRows(taaf, persons));
	        
        	PayCalendarEntries payCalendarEntries = TkServiceLocator.getPayCalendarEntriesSerivce().getPayCalendarEntries(taaf.getHrPyCalendarEntriesId());
   	        taaf.setPayCalendarEntries(payCalendarEntries);
   	        taaf.setPayCalendarLabels(TkServiceLocator.getTimeSummaryService().getHeaderForSummary(payCalendarEntries, new ArrayList<Boolean>()));
        	
	        List<Assignment> assignments = TkServiceLocator.getAssignmentService().getAssignments(principalId, payCalendarEntries.getEndPeriodDate());
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
        List<ApprovalTimeSummaryRow> lstApprovalRows = taaf.getApprovalRows();
        for (ApprovalTimeSummaryRow ar : lstApprovalRows) {
            if (ar.isApprovable() && StringUtils.equals(ar.getSelected(), "on")) {
                String documentNumber = ar.getDocumentId();
                TimesheetDocument tDoc = TkServiceLocator.getTimesheetService().getTimesheetDocument(documentNumber);
                TkServiceLocator.getTimesheetService().approveTimesheet(TKContext.getPrincipalId(), tDoc);
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

        PayCalendarEntries payCalendarEntries = TkServiceLocator.getPayCalendarEntriesSerivce().getPayCalendarEntries(taaf.getHrPyCalendarEntriesId());
        taaf.setPayCalendarEntries(payCalendarEntries);
        taaf.setPayCalendarLabels(TkServiceLocator.getTimeSummaryService().getHeaderForSummary(payCalendarEntries, new ArrayList<Boolean>()));

		taaf.getWorkAreaDescr().clear();
    	List<WorkArea> workAreas = TkServiceLocator.getWorkAreaService().getWorkAreas(taaf.getSelectedDept(), new java.sql.Date(taaf.getPayBeginDate().getTime()));
        for(WorkArea wa : workAreas){
        	if (TKContext.getUser().getCurrentRoles().getApproverWorkAreas().contains(wa.getWorkArea())
        			|| TKContext.getUser().getCurrentRoles().getReviewerWorkAreas().contains(wa.getWorkArea())) {
        		taaf.getWorkAreaDescr().put(wa.getWorkArea(),wa.getDescription()+"("+wa.getWorkArea()+")");
        	}
        }
	
    	List<String> principalIds = TkServiceLocator.getTimeApproveService().getPrincipalIdsByDeptWorkAreaRolename(taaf.getRoleName(), taaf.getSelectedDept(), taaf.getSelectedWorkArea(), new java.sql.Date(taaf.getPayBeginDate().getTime()), new java.sql.Date(taaf.getPayEndDate().getTime()), taaf.getSelectedPayCalendarGroup());
    	if (principalIds.isEmpty()) {
    		taaf.setApprovalRows(new ArrayList<ApprovalTimeSummaryRow>());
    		taaf.setResultSize(0);
    	}
    	else {
	        List<TKPerson> persons = TkServiceLocator.getPersonService().getPersonCollection(principalIds);
	        Collections.sort(persons);
	        taaf.setApprovalRows(getApprovalRows(taaf, getSubListPrincipalIds(request, persons)));
	        taaf.setResultSize(persons.size());
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

	    PayCalendarEntries payCalendarEntries = TkServiceLocator.getPayCalendarEntriesSerivce().getPayCalendarEntries(taaf.getHrPyCalendarEntriesId());
        taaf.setPayCalendarLabels(TkServiceLocator.getTimeSummaryService().getHeaderForSummary(payCalendarEntries, new ArrayList<Boolean>()));
        
        List<String> principalIds = TkServiceLocator.getTimeApproveService().getPrincipalIdsByDeptWorkAreaRolename(taaf.getRoleName(), taaf.getSelectedDept(), taaf.getSelectedWorkArea(), new java.sql.Date(taaf.getPayBeginDate().getTime()), new java.sql.Date(taaf.getPayEndDate().getTime()), taaf.getSelectedPayCalendarGroup());
		if (principalIds.isEmpty()) {
			taaf.setApprovalRows(new ArrayList<ApprovalTimeSummaryRow>());
			taaf.setResultSize(0);
		}
		else {
	        List<TKPerson> persons = TkServiceLocator.getPersonService().getPersonCollection(principalIds);
	        Collections.sort(persons);
	        taaf.setApprovalRows(getApprovalRows(taaf, getSubListPrincipalIds(request, persons)));
	        taaf.setResultSize(persons.size());
		}
		return mapping.findForward("basic");
	}
	
	public ActionForward loadApprovalTab(ActionMapping mapping, ActionForm form,
	HttpServletRequest request, HttpServletResponse response)
				throws Exception {
		ActionForward fwd = mapping.findForward("basic");
		TKUser user = TKContext.getUser();
        TimeApprovalActionForm taaf = (TimeApprovalActionForm) form;
        Date currentDate = null;
        PayCalendarEntries payCalendarEntries = null;
        PayCalendar currentPayCalendar = null;
        String page = request.getParameter((new ParamEncoder(TkConstants.APPROVAL_TABLE_ID).encodeParameterName(TableTagParameters.PARAMETER_PAGE)));
        
        //reset state
        if(StringUtils.isBlank(taaf.getSelectedDept())){
        	resetState(form, request);
        }
        // Set calendar groups
        List<String> calGroups = TkServiceLocator.getTimeApproveService().getUniquePayGroups();
        taaf.setPayCalendarGroups(calGroups);

        if (StringUtils.isBlank(taaf.getSelectedPayCalendarGroup())) {
            taaf.setSelectedPayCalendarGroup(calGroups.get(0));
        }
        
        // Set current pay calendar entries if present. Decide if the current date should be today or the end period date
        if (taaf.getHrPyCalendarEntriesId() != null) {
        	payCalendarEntries = TkServiceLocator.getPayCalendarEntriesSerivce().getPayCalendarEntries(taaf.getHrPyCalendarEntriesId());
            currentDate = payCalendarEntries.getEndPeriodDate();
        } else {
            currentDate = TKUtils.getTimelessDate(null);
            currentPayCalendar = TkServiceLocator.getPayCalendarSerivce().getPayCalendarByGroup(taaf.getSelectedPayCalendarGroup());
            payCalendarEntries = TkServiceLocator.getPayCalendarEntriesSerivce().getCurrentPayCalendarEntriesByPayCalendarId(currentPayCalendar.getHrPyCalendarId(), currentDate);
        }
        taaf.setPayCalendarEntries(payCalendarEntries);
        
        
        if(taaf.getPayCalendarEntries() != null) {
	        populateCalendarAndPayPeriodLists(request, taaf);
        }
        setupDocumentOnFormContext(request,taaf,payCalendarEntries, page);
        return fwd;
	}

	private void populateCalendarAndPayPeriodLists(HttpServletRequest request, TimeApprovalActionForm taaf) {
		// set calendar year list
		Set<String> yearSet = new HashSet<String>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		// if selected calendar year is passed in
		if(!StringUtils.isEmpty(request.getParameter("selectedCY"))) {
			taaf.setSelectedCalendarYear(request.getParameter("selectedCY").toString());
		} else {
			taaf.setSelectedCalendarYear(sdf.format(taaf.getPayCalendarEntries().getBeginPeriodDate()));
		}
		
		List<PayCalendarEntries> pcListForYear = new ArrayList<PayCalendarEntries>();
		List<PayCalendarEntries> pceList = TkServiceLocator.getTimeApproveService()
			.getAllPayCalendarEntriesForApprover(TKContext.getPrincipalId(), TKUtils.getTimelessDate(null));
	    for(PayCalendarEntries pce : pceList) {
	    	yearSet.add(sdf.format(pce.getBeginPeriodDate()));
	    	if(sdf.format(pce.getBeginPeriodDate()).equals(taaf.getSelectedCalendarYear())) {
	    		pcListForYear.add(pce);
	    	}
	    }
	    List<String> yearList = new ArrayList<String>(yearSet);
	    Collections.sort(yearList);
	    taaf.setCalendarYears(yearList);
		
		// set pay period list contents
		if(!StringUtils.isEmpty(request.getParameter("selectedPP"))) {
			taaf.setSelectedPayPeriod(request.getParameter("selectedPP").toString());
		} else {
			taaf.setSelectedPayPeriod(taaf.getPayCalendarEntries().getHrPyCalendarEntriesId());
			taaf.setPayPeriodsMap(ActionFormUtils.getPayPeriodsMap(pcListForYear));
		}
		if(taaf.getPayPeriodsMap().isEmpty()) {
		    taaf.setPayPeriodsMap(ActionFormUtils.getPayPeriodsMap(pcListForYear));
		}
	}

	private void setupDocumentOnFormContext(HttpServletRequest request,TimeApprovalActionForm taaf,PayCalendarEntries payCalendarEntries, String page) {
		if(payCalendarEntries == null) {
			return;
		}
		taaf.setHrPyCalendarId(payCalendarEntries.getHrPyCalendarId());
		taaf.setHrPyCalendarEntriesId(payCalendarEntries.getHrPyCalendarEntriesId());
		taaf.setPayBeginDate(payCalendarEntries.getBeginPeriodDateTime());
		taaf.setPayEndDate(payCalendarEntries.getEndPeriodDateTime());
		
		PayCalendarEntries prevPayCalendarEntries = TkServiceLocator.getPayCalendarEntriesSerivce().getPreviousPayCalendarEntriesByPayCalendarId(taaf.getHrPyCalendarId(), payCalendarEntries);
		if (prevPayCalendarEntries != null) {
		    taaf.setPrevPayCalendarId(prevPayCalendarEntries.getHrPyCalendarEntriesId());
		} else {
		    taaf.setPrevPayCalendarId(null);
		}
		
		PayCalendarEntries nextPayCalendarEntries = TkServiceLocator.getPayCalendarEntriesSerivce().getNextPayCalendarEntriesByPayCalendarId(taaf.getHrPyCalendarId(), payCalendarEntries);
		if (nextPayCalendarEntries != null) {
		    taaf.setNextPayCalendarId(nextPayCalendarEntries.getHrPyCalendarEntriesId());
		} else {
		    taaf.setNextPayCalendarId(null);
		}
		taaf.setPayCalendarLabels(TkServiceLocator.getTimeSummaryService().getHeaderForSummary(payCalendarEntries, new ArrayList<Boolean>()));
		
		if (StringUtils.isBlank(page)) {
		    List<String> depts = new ArrayList<String>(TKContext.getUser().getReportingApprovalDepartments().keySet());
		    if ( depts.isEmpty() ) {
		    	return;
		    }
		    Collections.sort(depts);
		    taaf.setDepartments(depts);
		    
		    if (taaf.getDepartments().size() == 1 || taaf.getSelectedDept() != null) {
		    	if (StringUtils.isEmpty(taaf.getSelectedDept()))
		    		taaf.setSelectedDept(taaf.getDepartments().get(0));
		    	
		    	List<WorkArea> workAreas = TkServiceLocator.getWorkAreaService().getWorkAreas(taaf.getSelectedDept(), new java.sql.Date(taaf.getPayBeginDate().getTime()));
		        for(WorkArea wa : workAreas){
		        	if (TKContext.getUser().getCurrentRoles().getApproverWorkAreas().contains(wa.getWorkArea())
		        			|| TKContext.getUser().getCurrentRoles().getReviewerWorkAreas().contains(wa.getWorkArea())) {
		        		taaf.getWorkAreaDescr().put(wa.getWorkArea(),wa.getDescription()+"("+wa.getWorkArea()+")");
		        	}
		        }
		    }
		}

		List<String> principalIds = new ArrayList<String>();
		principalIds = TkServiceLocator.getTimeApproveService().getPrincipalIdsByDeptWorkAreaRolename(taaf.getRoleName(), taaf.getSelectedDept(), taaf.getSelectedWorkArea(), new java.sql.Date(taaf.getPayBeginDate().getTime()), new java.sql.Date(taaf.getPayEndDate().getTime()), taaf.getSelectedPayCalendarGroup());
		if (principalIds.isEmpty()) {
			taaf.setApprovalRows(new ArrayList<ApprovalTimeSummaryRow>());
			taaf.setResultSize(0);
		}
		else {
		    List<TKPerson> persons = TkServiceLocator.getPersonService().getPersonCollection(principalIds);
		    Collections.sort(persons);
		    taaf.setApprovalRows(getApprovalRows(taaf, getSubListPrincipalIds(request, persons)));
		    taaf.setResultSize(persons.size());
		}
		
		taaf.setOnCurrentPeriod(ActionFormUtils.getOnCurrentPeriodFlag(taaf.getPayCalendarEntries()));
	}
	
	public ActionForward selectNewPayCalendar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		TimeApprovalActionForm taaf = (TimeApprovalActionForm)form;
		taaf.setSearchField(null);
		taaf.setSearchTerm(null);
		taaf.setSelectedWorkArea(null);
		taaf.setSelectedDept(null);
		taaf.setPayBeginDate(null);
		taaf.setPayEndDate(null);
		taaf.setHrPyCalendarEntriesId(null);
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
    protected List<ApprovalTimeSummaryRow> getApprovalRows(TimeApprovalActionForm taaf, List<TKPerson> assignmentPrincipalIds) {
        return TkServiceLocator.getTimeApproveService().getApprovalSummaryRows(taaf.getPayBeginDate(), taaf.getPayEndDate(), taaf.getSelectedPayCalendarGroup(), assignmentPrincipalIds, taaf.getPayCalendarLabels(), taaf.getPayCalendarEntries());
    }
	
    public void resetState(ActionForm form, HttpServletRequest request) {
    	  TimeApprovalActionForm taaf = (TimeApprovalActionForm) form;
 	      String page = request.getParameter((new ParamEncoder(TkConstants.APPROVAL_TABLE_ID).encodeParameterName(TableTagParameters.PARAMETER_PAGE)));
 	      
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
    protected void checkTKAuthorization(ActionForm form, String methodToCall) throws AuthorizationException {
        TKUser user = TKContext.getUser();
        UserRoles roles = user.getCurrentRoles();

        if (!roles.isTimesheetReviewer() && !roles.isAnyApproverActive() && !roles.isSystemAdmin() && !roles.isLocationAdmin() && !roles.isGlobalViewOnly() && !roles.isDeptViewOnly() && !roles.isDepartmentAdmin()) {
            throw new AuthorizationException(user.getPrincipalId(), "TimeApprovalAction", "");
        }
    }
    
    protected String getSortField(HttpServletRequest request) {
        return request.getParameter((new ParamEncoder(TkConstants.APPROVAL_TABLE_ID).encodeParameterName(TableTagParameters.PARAMETER_SORT)));
    }

    protected Boolean isAscending(HttpServletRequest request) {
        // returned value 1 = ascending; 2 = descending
        String ascending = request.getParameter((new ParamEncoder(TkConstants.APPROVAL_TABLE_ID).encodeParameterName(TableTagParameters.PARAMETER_ORDER)));
        return StringUtils.equals(ascending, "1") ? true : false;
    }

    // move this to the service layer
    protected List<TKPerson> getSubListPrincipalIds(HttpServletRequest request, List<TKPerson> assignmentPrincipalIds) {
        String page = request.getParameter((new ParamEncoder(TkConstants.APPROVAL_TABLE_ID).encodeParameterName(TableTagParameters.PARAMETER_PAGE)));
        // The paging index begins from 1, but the sublist index begins from 0.
        // So the logic below sets the sublist begin index to 0 if the page number is null or equals 1
        Integer beginIndex = StringUtils.isBlank(page) || StringUtils.equals(page, "1") ? 0 : (Integer.parseInt(page) - 1)*TkConstants.PAGE_SIZE;
        Integer endIndex = beginIndex + TkConstants.PAGE_SIZE > assignmentPrincipalIds.size() ? assignmentPrincipalIds.size() : beginIndex + TkConstants.PAGE_SIZE;

        return assignmentPrincipalIds.subList(beginIndex, endIndex);
    } 
    
    public ActionForward gotoCurrentPayPeriod(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	String page = request.getParameter((new ParamEncoder(TkConstants.APPROVAL_TABLE_ID).encodeParameterName(TableTagParameters.PARAMETER_PAGE)));
         
    	TimeApprovalActionForm taaf = (TimeApprovalActionForm) form;
    	Date currentDate = TKUtils.getTimelessDate(null);
        PayCalendar currentPayCalendar = TkServiceLocator.getPayCalendarSerivce().getPayCalendarByGroup(taaf.getSelectedPayCalendarGroup());
        PayCalendarEntries payCalendarEntries = TkServiceLocator.getPayCalendarEntriesSerivce().getCurrentPayCalendarEntriesByPayCalendarId(currentPayCalendar.getHrPyCalendarId(), currentDate);
        taaf.setPayCalendarEntries(payCalendarEntries);
        taaf.setSelectedCalendarYear(new SimpleDateFormat("yyyy").format(payCalendarEntries.getBeginPeriodDate()));
        taaf.setSelectedPayPeriod(payCalendarEntries.getHrPyCalendarEntriesId());
        populateCalendarAndPayPeriodLists(request, taaf);
        setupDocumentOnFormContext(request, taaf, payCalendarEntries, page);
        return mapping.findForward("basic");
    }
    
    // Triggered by changes of calendar year drop down list, reloads the pay period list
    public ActionForward changeCalendarYear(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	TimeApprovalActionForm taaf = (TimeApprovalActionForm) form;
    	if(!StringUtils.isEmpty(request.getParameter("selectedCY"))) {
    		taaf.setSelectedCalendarYear(request.getParameter("selectedCY").toString());
    		populateCalendarAndPayPeriodLists(request, taaf);
    	}
    	return mapping.findForward("basic");
    }
    
    // Triggered by changes of pay period drop down list, reloads the whole page based on the selected pay period
    public ActionForward changePayPeriod(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
      String page = request.getParameter((new ParamEncoder(TkConstants.APPROVAL_TABLE_ID).encodeParameterName(TableTagParameters.PARAMETER_PAGE)));
      TimeApprovalActionForm taaf = (TimeApprovalActionForm) form;
  	  if(!StringUtils.isEmpty(request.getParameter("selectedPP"))) {
  		  taaf.setSelectedPayPeriod(request.getParameter("selectedPP").toString());
  		  PayCalendarEntries pce = TkServiceLocator.getPayCalendarEntriesSerivce()
  		  	.getPayCalendarEntries(request.getParameter("selectedPP").toString());
  		  if(pce != null) {
  			  taaf.setPayCalendarEntries(pce);
  			  setupDocumentOnFormContext(request, taaf, pce, page);
  		  }
  	  }
  	  return mapping.findForward("basic");
    }
    
}