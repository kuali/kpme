package org.kuali.hr.time.workflow.web;

import org.apache.commons.lang.StringUtils;
import org.kuali.hr.time.roles.TkUserRoles;
import org.kuali.hr.time.roles.UserRoles;
import org.kuali.hr.time.timesheet.TimesheetDocument;
import org.kuali.hr.time.util.TKContext;
import org.kuali.hr.time.util.TkConstants;
import org.kuali.hr.time.workflow.TimesheetDocumentHeader;
import org.kuali.rice.kew.doctype.SecuritySession;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;

public class WorkflowTagSupport {

    public String getDocumentId() {
        return TKContext.getCurrentTimesheetDocumentId();
    }

    public boolean isDisplayingRouteButton() {
        TkUserRoles roles = TkUserRoles.getUserRoles(GlobalVariables.getUserSession().getActualPerson().getPrincipalId());
        TimesheetDocument doc = TKContext.getCurrentTimesheetDocument();
        TimesheetDocumentHeader tdh = doc.getDocumentHeader();
        if(tdh.getDocumentStatus().equals("S") || tdh.getDocumentStatus().equals("I")){
            return roles.canSubmitTimesheet(doc);
        }
        return false;
    }

    /**
     * Disable the 'route' button if the document has already been routed.
     * @return true if the route button should render as enabled.
     */
    public boolean isRouteButtonEnabled() {
        TimesheetDocument doc = TKContext.getCurrentTimesheetDocument();
        TimesheetDocumentHeader tdh = doc.getDocumentHeader();
        return (tdh.getDocumentStatus().equals("I") || tdh.getDocumentStatus().equals("S"));
    }

    public boolean isDisplayingApprovalButtons() {
        UserRoles roles = TkUserRoles.getUserRoles(GlobalVariables.getUserSession().getPrincipalId());
        TimesheetDocument doc = TKContext.getCurrentTimesheetDocument();
        boolean tookActionAlready = KEWServiceLocator.getActionTakenService().hasUserTakenAction(TKContext.getPrincipalId(), doc.getDocumentId());
        return !tookActionAlready && roles.isApproverForTimesheet(doc) && !StringUtils.equals(doc.getDocumentHeader().getDocumentStatus(), "F");
    }

    public boolean isApprovalButtonsEnabled() {
        TimesheetDocument doc = TKContext.getCurrentTimesheetDocument();
        TimesheetDocumentHeader tdh = doc.getDocumentHeader();
        boolean isEnroute = tdh.getDocumentStatus().equals(TkConstants.ROUTE_STATUS.ENROUTE);
        if(isEnroute){
            DocumentRouteHeaderValue routeHeader = KEWServiceLocator.getRouteHeaderService().getRouteHeader(doc.getDocumentId());
            boolean authorized = KEWServiceLocator.getDocumentSecurityService().routeLogAuthorized(TKContext.getPrincipalId(), routeHeader, new SecuritySession(TKContext.getPrincipalId()));
            boolean tookActionAlready = KEWServiceLocator.getActionTakenService().hasUserTakenAction(TKContext.getPrincipalId(), doc.getDocumentId());
            return !tookActionAlready && authorized;
        }
        return false;
    }

    public String getRouteAction() { return TkConstants.DOCUMENT_ACTIONS.ROUTE; }
    public String getApproveAction() { return TkConstants.DOCUMENT_ACTIONS.APPROVE; }
    public String getDisapproveAction() { return TkConstants.DOCUMENT_ACTIONS.DISAPPROVE; }
}
