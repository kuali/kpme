package org.kuali.hr.time.base.web;

import org.kuali.hr.time.assignment.Assignment;
import org.kuali.hr.time.principal.PrincipalHRAttributes;
import org.kuali.hr.time.service.base.TkServiceLocator;
import org.kuali.hr.time.util.TKContext;
import org.kuali.hr.time.util.TKUser;
import org.kuali.hr.time.util.TKUtils;
import org.kuali.hr.time.util.TkConstants;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.kns.web.struts.form.KualiForm;

import java.sql.Date;
import java.util.List;

public class TkForm extends KualiForm {

    /**
     *
     */
    private static final long serialVersionUID = -3945893347262537122L;

    private String methodToCall;
    private String principalId;
    private String documentId;

    public String getMethodToCall() {
        return methodToCall;
    }

    public void setMethodToCall(String methodToCall) {
        this.methodToCall = methodToCall;
    }

    public TKUser getUser() {
        return TKContext.getUser();
    }

    public String getPrincipalId() {
        return principalId;
    }

    public void setPrincipalId(String principalId) {
        this.principalId = principalId;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getWorkflowUrl(){
        return ConfigContext.getCurrentContextConfig().getProperty("workflow.url");
    }


    public String getDocumentIdFromContext(){
        return TKContext.getCurrentTimesheetDocumentId();
    }

    public String getDocumentStatus() {
        return TKContext.getCurrentTimesheetDoucment().getDocumentHeader().getDocumentStatus();
    }

    public boolean getLeaveEnabled() {
        boolean canViewLeaveTab= false;
        canViewLeaveTab = this.getViewLeaveTabsWithNEStatus() || this.canViewLeaveTabsWithEStatus();
        return canViewLeaveTab;
    }

    public boolean getTimeEnabled() {
        return this.canViewTimeTabs();
    }

    private boolean canViewTimeTabs() {
        boolean canViewTimeTabs = false;
        Date asOfDate = TKUtils.getTimelessDate(null);
        String flsaStatus = TkConstants.FLSA_STATUS_NON_EXEMPT;
        // find active assignments as of currentDate
        String principalId = TKUser.getCurrentTargetPerson().getPrincipalId();
        if(isActiveAssignmentFoundOnJobFlsaStatus(principalId, flsaStatus, false)) {
            //find timecalendar defined
            canViewTimeTabs = isCalendarDefined("payCalendar", principalId, asOfDate, false);
        }
        return canViewTimeTabs;
    }

    public boolean getViewLeaveTabsWithNEStatus() {
        boolean canViewLeaveTabs = false;
        Date asOfDate = TKUtils.getTimelessDate(null);
        String flsaStatus = TkConstants.FLSA_STATUS_NON_EXEMPT;
        // find active assignments as of currentDate
        String principalId = TKUser.getCurrentTargetPerson().getPrincipalId();
        boolean activeAss = isActiveAssignmentFoundOnJobFlsaStatus(principalId, flsaStatus, true);
        // chk leave plan defined
        boolean leaveCalNPlanDefined = isCalendarDefined("leaveCalendar", principalId, asOfDate, true);
        boolean timeCalDefined = isCalendarDefined("payCalendar", principalId, asOfDate, false);
        canViewLeaveTabs = activeAss && leaveCalNPlanDefined && timeCalDefined;
        return canViewLeaveTabs;
    }


    private boolean canViewLeaveTabsWithEStatus() {
        boolean canViewLeaveTabs = false;
        String principalId = TKUser.getCurrentTargetPerson().getPrincipalId();
        Date asOfDate = TKUtils.getTimelessDate(null);
        boolean leaveCalNPlanDefined = isCalendarDefined("leaveCalendar", principalId, asOfDate, true);
        String flsaStatus = TkConstants.FLSA_STATUS_EXEMPT;
        boolean activeAss = isActiveAssignmentFoundOnJobFlsaStatus(principalId, flsaStatus, true);
        canViewLeaveTabs = activeAss && leaveCalNPlanDefined;
        return canViewLeaveTabs;
    }


    private boolean isActiveAssignmentFoundOnJobFlsaStatus(String principalId, String flsaStatus, boolean chkForLeaveEligible) {
        boolean isActiveAssFound = false;
        Date asOfDate = TKUtils.getTimelessDate(null);
        List<Assignment> activeAssignments = TkServiceLocator.getAssignmentService().getAssignments(principalId, asOfDate);
        if(activeAssignments != null && !activeAssignments.isEmpty()) {
            for(Assignment assignment : activeAssignments) {
                if(assignment != null && assignment.getJob() != null && assignment.getJob().getFlsaStatus() != null && assignment.getJob().getFlsaStatus().equalsIgnoreCase(flsaStatus)) {
                    isActiveAssFound = true;
                    break;
                }
            }
        }
        return isActiveAssFound;
    }

    private boolean isCalendarDefined(String calendarType, String principalId, Date asOfDate, boolean chkForLeavePlan){
        boolean calDefined = false;
        PrincipalHRAttributes principalHRAttributes = TkServiceLocator.getPrincipalHRAttributeService().getPrincipalCalendar(principalId, asOfDate);
        if(principalHRAttributes != null) {
            if(calendarType.equalsIgnoreCase("payCalendar")) {
                calDefined = principalHRAttributes.getPayCalendar() != null ? true : false;
            } else if(calendarType.equalsIgnoreCase("leaveCalendar")) {
                calDefined = principalHRAttributes.getLeaveCalendar() != null ? true : false;
                if(calDefined && chkForLeavePlan) {
                    calDefined = principalHRAttributes.getLeavePlan() != null ? true : false;
                }
            }
        }
        return calDefined;
    }
}