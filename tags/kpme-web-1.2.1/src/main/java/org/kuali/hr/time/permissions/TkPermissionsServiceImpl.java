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
package org.kuali.hr.time.permissions;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.hr.earncodesec.EarnCodeSecurity;
import org.kuali.hr.job.Job;
import org.kuali.hr.time.assignment.Assignment;
import org.kuali.hr.time.assignment.AssignmentDescriptionKey;
import org.kuali.hr.time.authorization.DepartmentalRule;
import org.kuali.hr.time.authorization.DepartmentalRuleAuthorizer;
import org.kuali.hr.time.collection.rule.TimeCollectionRule;
import org.kuali.hr.time.paytype.PayType;
import org.kuali.hr.time.principal.PrincipalHRAttributes;
import org.kuali.hr.time.roles.TkUserRoles;
import org.kuali.hr.time.roles.UserRoles;
import org.kuali.hr.time.service.base.TkServiceLocator;
import org.kuali.hr.time.timeblock.TimeBlock;
import org.kuali.hr.time.timesheet.TimesheetDocument;
import org.kuali.hr.time.util.TKContext;
import org.kuali.hr.time.util.TKUser;
import org.kuali.hr.time.util.TKUtils;
import org.kuali.hr.time.util.TkConstants;
import org.kuali.hr.time.workarea.WorkArea;
import org.kuali.hr.time.workflow.TimesheetDocumentHeader;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.doctype.SecuritySession;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;

public class TkPermissionsServiceImpl implements TkPermissionsService {
    private static final Logger LOG = Logger
            .getLogger(DepartmentalRuleAuthorizer.class);

    @Override
    public boolean canAddTimeBlock() {
        boolean addTimeBlock = false;

        if (TKContext.getUser().isSystemAdmin()) {
            addTimeBlock = true;
        } else {
            boolean docFinal = TKContext.getCurrentTimesheetDocument()
                    .getDocumentHeader().getDocumentStatus()
                    .equals(TkConstants.ROUTE_STATUS.FINAL);
            if (!docFinal) {
                if (StringUtils
                        .equals(TKContext.getCurrentTimesheetDocument().getPrincipalId(),
                                GlobalVariables.getUserSession().getPrincipalId())
                        || TkUserRoles.getUserRoles(GlobalVariables.getUserSession().getPrincipalId()).isSystemAdmin()
                        || TKContext.getUser().isLocationAdmin()
//                        || TKContext.getUser().isDepartmentAdmin()
                        || TKContext.getUser().isReviewer()
                        || TKContext.getUser().isApprover()) {
                    addTimeBlock = true;
                }
            }
        }
        return addTimeBlock;
    }

    @Override
    public boolean canEditTimeBlockAllFields(TimeBlock tb) {
        String userId = GlobalVariables.getUserSession().getPrincipalId();

        if (userId != null) {

            if (TKContext.getUser().isSystemAdmin()) {
                return true;
            }

            Job job = TkServiceLocator.getJobService().getJob(
                    TKContext.getTargetPrincipalId(), tb.getJobNumber(),
                    tb.getEndDate());
            PayType payType = TkServiceLocator.getPayTypeService().getPayType(
                    job.getHrPayType(), tb.getEndDate());

            if (TKContext.getUser().isTimesheetApprover()
                    && TKContext.getUser().getApproverWorkAreas().contains(tb.getWorkArea())
                    || TKContext.getUser().isTimesheetReviewer()
                    && TKContext.getUser().getReviewerWorkAreas().contains(tb.getWorkArea())) {

                if (StringUtils.equals(payType.getRegEarnCode(),
                        tb.getEarnCode())) {
                    return true;
                }

                List<EarnCodeSecurity> deptEarnCodes = TkServiceLocator
                        .getEarnCodeSecurityService().getEarnCodeSecurities(
                                job.getDept(), job.getHrSalGroup(),
                                job.getLocation(), tb.getEndDate());
                for (EarnCodeSecurity dec : deptEarnCodes) {
                    if (dec.isApprover()
                            && StringUtils.equals(dec.getEarnCode(),
                            tb.getEarnCode())) {
                        return true;
                    }
                }
            }

            if (userId.equals(TKContext.getTargetPrincipalId())
                    && !tb.getClockLogCreated()) {
                if (StringUtils.equals(payType.getRegEarnCode(),
                        tb.getEarnCode())) {
                    return true;
                }

                List<EarnCodeSecurity> deptEarnCodes = TkServiceLocator
                        .getEarnCodeSecurityService().getEarnCodeSecurities(
                                job.getDept(), job.getHrSalGroup(),
                                job.getLocation(), tb.getEndDate());
                for (EarnCodeSecurity dec : deptEarnCodes) {
                    if (dec.isEmployee()
                            && StringUtils.equals(dec.getEarnCode(),
                            tb.getEarnCode())) {
                        return true;
                    }
                }
            }

        }

        return false;
    }

    @Override
    public boolean canEditTimeBlock(TimeBlock tb) {
        String userId = GlobalVariables.getUserSession().getPrincipalId();

        if (userId != null) {

        	// if the sys admin user is working on his own time block, do not grant edit permission without further checking
            if (TKContext.getUser().isSystemAdmin()&& !tb.getPrincipalId().equals(userId)) {	
            	return true;
            }
            Job job = TkServiceLocator.getJobService().getJob(
                    TKContext.getTargetPrincipalId(), tb.getJobNumber(),
                    tb.getEndDate());
            PayType payType = TkServiceLocator.getPayTypeService().getPayType(
                    job.getHrPayType(), tb.getEndDate());
            
            if (TKContext.getUser().isTimesheetApprover()
                    && TKContext.getUser().getApproverWorkAreas().contains(tb.getWorkArea())
                    || TKContext.getUser().isTimesheetReviewer()
                    && TKContext.getUser().getReviewerWorkAreas().contains(tb.getWorkArea())) {

                if (StringUtils.equals(payType.getRegEarnCode(),
                        tb.getEarnCode())) {
                    return true;
                }

                List<EarnCodeSecurity> deptEarnCodes = TkServiceLocator
                        .getEarnCodeSecurityService().getEarnCodeSecurities(
                                job.getDept(), job.getHrSalGroup(),
                                job.getLocation(), tb.getEndDate());
                for (EarnCodeSecurity dec : deptEarnCodes) {
                    if (dec.isApprover()
                            && StringUtils.equals(dec.getEarnCode(),
                            tb.getEarnCode())) {
                        return true;
                    }
                }
            }
            
            // if the time block is generated by clock actions, do not allow it to be edited/deleted
			if(tb.getClockLogCreated()) {
					return false;
			}

            if (userId.equals(TKContext.getTargetPrincipalId())) {

                if (StringUtils.equals(payType.getRegEarnCode(),
                        tb.getEarnCode())) {
                    TimeCollectionRule tcr = TkServiceLocator.getTimeCollectionRuleService().getTimeCollectionRule(job.getDept(),tb.getWorkArea(),job.getHrPayType(),tb.getBeginDate());

                    //If you are a clock user and you have only one assignment you should not be allowed to change the assignment
                    //TODO eventually move this logic to one concise place for editable portions of the timeblock
                    List<Assignment> assignments = TkServiceLocator.getAssignmentService().getAssignments(TKContext.getPrincipalId(),tb.getBeginDate());
                    if(assignments.size() == 1){
                        if(!tcr.isClockUserFl() ){
                            return true;
                        }  else{
                            return false;
                        }
                    }   else {
                        return true;
                    }



                }

                List<EarnCodeSecurity> deptEarnCodes = TkServiceLocator
                        .getEarnCodeSecurityService().getEarnCodeSecurities(
                                job.getDept(), job.getHrSalGroup(),
                                job.getLocation(), tb.getEndDate());
                for (EarnCodeSecurity dec : deptEarnCodes) {
                    if (dec.isEmployee()
                            && StringUtils.equals(dec.getEarnCode(),
                            tb.getEarnCode())) {
                        return true;
                    }
                }
            }

        }

        return false;
    }

    @Override
    public boolean canDeleteTimeBlock(TimeBlock tb) {
        String userId = GlobalVariables.getUserSession().getPrincipalId();

        if (userId != null) {

        	// if the sys admin user is working on his own time block, do not grant delete permission without further checking
            if (TKContext.getUser().isSystemAdmin()&& !tb.getPrincipalId().equals(userId)) {	
            	return true;
            }
            Job job = TkServiceLocator.getJobService().getJob(
                    TKContext.getTargetPrincipalId(), tb.getJobNumber(),
                    tb.getEndDate());
            PayType payType = TkServiceLocator.getPayTypeService().getPayType(
                    job.getHrPayType(), tb.getEndDate());

            TimeCollectionRule tcr = TkServiceLocator.getTimeCollectionRuleService().getTimeCollectionRule(job.getDept(),tb.getWorkArea(),payType.getPayType(),tb.getEndDate());

            if (TKContext.getUser().isTimesheetApprover()
                    && TKContext.getUser().getApproverWorkAreas().contains(tb.getWorkArea())
                    || TKContext.getUser().isTimesheetReviewer()
                    && TKContext.getUser().getReviewerWorkAreas().contains(tb.getWorkArea())) {

                if (StringUtils.equals(payType.getRegEarnCode(),
                        tb.getEarnCode())) {
                    return true;
                }

                List<EarnCodeSecurity> deptEarnCodes = TkServiceLocator
                        .getEarnCodeSecurityService().getEarnCodeSecurities(
                                job.getDept(), job.getHrSalGroup(),
                                job.getLocation(), tb.getEndDate());
                for (EarnCodeSecurity dec : deptEarnCodes) {
                    if (dec.isApprover()
                            && StringUtils.equals(dec.getEarnCode(),
                            tb.getEarnCode())) {
                        return true;
                    }
                }
            }

//            // If the timeblock was created by the employee himeself and is a sync timeblock,
//            // the user can't delete the timeblock
//            if (userId.equals(TKContext.getTargetPrincipalId())
//                    && tb.getClockLogCreated()) {
//                return false;
//            // But if the timeblock was created by the employee himeself and is an async timeblock,
//            // the user should be able to delete that timeblock
//            } else if (userId.equals(TKContext.getTargetPrincipalId()) && !tb.getClockLogCreated() ) {
//                return true;
//            } else {
            
            // if the time block is generated by clock actions, do not allow it to be edited/deleted
			if(tb.getClockLogCreated()) {
					return false;
			}

                //if on a regular earncode
                if (StringUtils.equals(payType.getRegEarnCode(),
                        tb.getEarnCode())) {
                    //and the user is a clock user and this is the users timesheet do not allow to be deleted
                    if(tcr.isClockUserFl() && StringUtils.equals(userId,TKContext.getTargetPrincipalId())) {
                        return false;
                    }  else {
                        return true;
                    }

                }

                List<EarnCodeSecurity> deptEarnCodes = TkServiceLocator
                        .getEarnCodeSecurityService().getEarnCodeSecurities(
                                job.getDept(), job.getHrSalGroup(),
                                job.getLocation(), tb.getEndDate());
                for (EarnCodeSecurity dec : deptEarnCodes) {
                    if (dec.isEmployee()
                            && StringUtils.equals(dec.getEarnCode(),
                            tb.getEarnCode())) {
                        return true;
                    }
                }

        }

        return false;
    }



    @Override
    public boolean canViewAdminTab() {
        UserRoles ur = TKContext.getUser().getCurrentTargetRoles();

        boolean viewAdminTab = ur.isSystemAdmin() || ur.isLocationAdmin()
                || ur.isDepartmentAdmin() || ur.isGlobalViewOnly();

        return viewAdminTab;
    }

    @Override
    public boolean canViewApproverTab() {
        UserRoles ur = TKContext.getUser().getCurrentTargetRoles();

        boolean viewApproverTab = ur.isSystemAdmin()
                || ur.isTimesheetApprover() || ur.isTimesheetReviewer();

        return viewApproverTab;
    }

    @Override
    public boolean canViewClockTab() {
        UserRoles ur = TKContext.getUser().getCurrentTargetRoles();

        return ur.isActiveEmployee() && ur.isSynchronous();
    }

    @Override
    public boolean canViewBatchJobsTab() {
        UserRoles ur = TKContext.getUser().getCurrentTargetRoles();

        return ur.isSystemAdmin();
    }

    @Override
    public boolean canViewPersonInfoTab() {
        return true;
    }

    @Override
    public boolean canViewTimeDetailTab() {
        UserRoles ur = TKContext.getUser().getCurrentTargetRoles();

        return ur.isActiveEmployee();
    }

    @Override
    public boolean canViewLeaveAccrualTab() {
        UserRoles ur = TKContext.getUser().getCurrentTargetRoles();

        return ur.isActiveEmployee();
    }

    @Override
    public boolean canViewTimesheet(String documentId) {
        boolean viewTimeSheet = false;

        if (documentId != null) {
            return canViewTimesheet(TkServiceLocator.getTimesheetService()
                    .getTimesheetDocument(documentId));
        }

        return viewTimeSheet;
    }

    @Override
    public boolean canViewTimesheet(TimesheetDocument document) {
        boolean viewTimeSheet = false;
        UserRoles ur = TKContext.getUser().getCurrentTargetRoles();

        if (document == null)
            return viewTimeSheet;

        // Sysadmin
        viewTimeSheet = ur.isSystemAdmin();
        // Owner
        viewTimeSheet |= StringUtils.equals(ur.getPrincipalId(),
                document.getPrincipalId());
        // Global VO
        viewTimeSheet |= ur.isGlobalViewOnly();

        if (!viewTimeSheet) {
            // Departmental View Only? || Reviewer || Org Admin || Approver
            // (document object iteration)
            List<Assignment> assignments = document.getAssignments();
            for (Assignment assignment : assignments) {
                String dept = assignment.getDept();
                Long wa = assignment.getWorkArea();

                viewTimeSheet |= ur.getOrgAdminDepartments().contains(dept);
                viewTimeSheet |= ur.getApproverWorkAreas().contains(wa);
                viewTimeSheet |= ur.getReviewerWorkAreas().contains(wa);
                viewTimeSheet |= ur.getDepartmentViewOnlyDepartments()
                        .contains(dept);
            }
        }

        return viewTimeSheet;
    }

    @Override
    public boolean canEditTimesheet(TimesheetDocument document) {
        boolean editTimeSheet = false;
        UserRoles ur = TKContext.getUser().getCurrentTargetRoles();

        // Quick escape.
        if (document == null)
            return editTimeSheet;

        // Sysadmin
        editTimeSheet = ur.isSystemAdmin();
        // Owner (and not enroute/final)
        editTimeSheet |= (StringUtils.equals(ur.getPrincipalId(),
                document.getPrincipalId()) && (StringUtils.equals(
                TkConstants.ROUTE_STATUS.INITIATED, document
                .getDocumentHeader().getDocumentStatus())
                || StringUtils.equals(TkConstants.ROUTE_STATUS.SAVED, document
                .getDocumentHeader().getDocumentStatus()) || (StringUtils
                .equals(TkConstants.ROUTE_STATUS.ENROUTE, document
                        .getDocumentHeader().getDocumentStatus()))));

        if (!editTimeSheet) {
            // Departmental View Only? || Reviewer || Org Admin || Approver
            // (document object iteration)
            List<Assignment> assignments = document.getAssignments();
            for (Assignment assignment : assignments) {
                String dept = assignment.getDept();
                Long wa = assignment.getWorkArea();

                editTimeSheet |= ur.getOrgAdminDepartments().contains(dept);
                editTimeSheet |= ur.getApproverWorkAreas().contains(wa);
                editTimeSheet |= ur.getReviewerWorkAreas().contains(wa);
            }
        }

        return editTimeSheet;
    }

    @Override
    public boolean canEditTimesheet(String documentId) {
        return canEditTimesheet(TkServiceLocator.getTimesheetService()
                .getTimesheetDocument(documentId));
    }

    @Override
    public boolean canApproveTimesheet(TimesheetDocument doc) {

        TimesheetDocumentHeader docHeader = TkServiceLocator
                .getTimesheetDocumentHeaderService().getDocumentHeader(
                        doc.getDocumentId());
        boolean isEnroute = StringUtils.equals(docHeader.getDocumentStatus(),
                "ENROUTE");

        if (isEnroute) {
            DocumentRouteHeaderValue routeHeader = KEWServiceLocator
                    .getRouteHeaderService().getRouteHeader(doc.getDocumentId());
            boolean authorized = KEWServiceLocator.getDocumentSecurityService()
                    .routeLogAuthorized(TKContext.getPrincipalId(),
                            routeHeader,
                            new SecuritySession(TKContext.getPrincipalId()));
            if (authorized) {
                List<String> principalsToApprove = KEWServiceLocator
                        .getActionRequestService()
                        .getPrincipalIdsWithPendingActionRequestByActionRequestedAndDocId(
                                KewApiConstants.ACTION_REQUEST_APPROVE_REQ,
                                routeHeader.getDocumentId());
                if (!principalsToApprove.isEmpty()
                        && principalsToApprove.contains(TKContext
                        .getPrincipalId())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean canSubmitTimesheet(TimesheetDocument doc) {
        UserRoles ur = TKContext.getUser().getCurrentTargetRoles();

        if (StringUtils
                .equals(TKContext.getPrincipalId(), doc.getPrincipalId())) {
            return true;
        }

        if (ur.isApproverForTimesheet(doc)) {
            return true;
        }

        // System admins can route the document as well as the employee
        if (ur.isSystemAdmin()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean canSubmitTimesheet(String docId) {
        TimesheetDocument doc = TkServiceLocator.getTimesheetService()
                .getTimesheetDocument(docId);
        return canSubmitTimesheet(doc);
    }

    @Override
    public boolean canViewLinkOnMaintPages() {
        return TKContext.getUser().isSystemAdmin()
                || TKContext.getUser().isGlobalViewOnly();
    }

    @Override
    public boolean canViewDeptMaintPages() {
        UserRoles ur = TKContext.getUser().getCurrentTargetRoles();

        return ur.isSystemAdmin() || ur.isGlobalViewOnly()
                || ur.getOrgAdminCharts().size() > 0
                || ur.getOrgAdminDepartments().size() > 0
                || ur.getDepartmentViewOnlyDepartments().size() > 0
                || ur.isAnyApproverActive();
    }

    @Override
    public boolean canViewDeptMaintPages(DepartmentalRule dr) {
        boolean ret = false;
        if (TKContext.getUser().isSystemAdmin() || TKContext.getUser().isGlobalViewOnly())
            return true;

        if (dr != null) {
            // dept | workArea | meaning
            // ---------|------------|
            // 1: % , -1 , any dept/work area valid roles
            // *2: % , <defined> , must have work area <-- *
            // 3: <defined>, -1 , must have dept, any work area
            // 4: <defined>, <defined> , must have work area or department
            // defined
            //
            // * Not permitted.

            if (StringUtils
                    .equals(dr.getDept(), TkConstants.WILDCARD_CHARACTER)
                    && dr.getWorkArea().equals(TkConstants.WILDCARD_LONG)) {
                // case 1
                ret = TKContext.getUser().isApprover()
                        || TKContext.getUser().getLocationAdminAreas().size() > 0
                        || TKContext.getUser().getDepartmentAdminAreas().size() > 0;
            } else if (StringUtils.equals(dr.getDept(),
                    TkConstants.WILDCARD_CHARACTER)) {
                // case 2 *
                // Should not encounter this case.
                LOG.error("Invalid case encountered while scanning business objects: Wildcard Department & Defined workArea.");
            } else if (dr.getWorkArea().equals(TkConstants.WILDCARD_LONG)) {
                // case 3
                ret = TKContext.getUser().getDepartmentAdminAreas().contains(dr.getDept());
            } else {
                ret = TKContext.getUser().getApproverWorkAreas().contains(dr.getWorkArea())
                        || TKContext.getUser().getDepartmentAdminAreas().contains(dr.getDept());
            }
        }

        return ret;
    }

    @Override
    public boolean canEditDeptMaintPages() {
        UserRoles ur = TKContext.getUser().getCurrentTargetRoles();
        return ur.isSystemAdmin() || ur.getOrgAdminCharts().size() > 0
                || ur.getOrgAdminDepartments().size() > 0;
    }

    @Override
    public boolean canEditDeptMaintPages(DepartmentalRule dr) {
        boolean ret = false;
        if (TKContext.getUser().isSystemAdmin())
            return true;

        if (dr != null && TKContext.getUser().getDepartmentAdminAreas().size() > 0) {
            String dept = dr.getDept();
            if (StringUtils.equals(dept, TkConstants.WILDCARD_CHARACTER)) {
                // Must be system administrator
                ret = false;
            } else {
                // Must have parent Department
                ret = TKContext.getUser().getDepartmentAdminAreas().contains(dr.getDept());
            }
        }

        return ret;
    }

    @Override
    public boolean canWildcardWorkAreaInDeptRule(DepartmentalRule dr) {
        // Sysadmins and (Departmental OrgAdmins for their Department)
        if (TKContext.getUser().isSystemAdmin())
            return true;

        String dept = dr.getDept();
        if (StringUtils.equals(dept, TkConstants.WILDCARD_CHARACTER)) {
            // Only system administrators can wildcard the work area if the
            // department also has a wildcard.
            return TKContext.getUser().isSystemAdmin();
        } else {
            return TKContext.getUser().getDepartmentAdminAreas().contains(dept);
        }
    }

    @Override
    public boolean canWildcardDeptInDeptRule(DepartmentalRule dr) {
        return TKContext.getUser().isSystemAdmin();
    }

    @Override
    public boolean canEditOvertimeEarnCode(TimeBlock tb) {
        WorkArea workArea = TkServiceLocator.getWorkAreaService().getWorkArea(tb.getWorkArea(), new java.sql.Date(tb.getEndTimestamp().getTime()));
        if (StringUtils.equals(workArea.getOvertimeEditRole(), TkConstants.ROLE_TK_EMPLOYEE)) {
            return true;
        } else if (StringUtils.equals(workArea.getOvertimeEditRole(), TkConstants.ROLE_TK_APPROVER) ||
                StringUtils.equals(workArea.getOvertimeEditRole(), TkConstants.ROLE_TK_APPROVER_DELEGATE)) {
            return TKContext.getUser().getApproverWorkAreas().contains(workArea.getWorkArea());
        } else {
            return TKContext.getUser().getDepartmentAdminAreas().contains(workArea.getDepartment());
        }
    }
    
    /*
     * @see org.kuali.hr.time.permissions.TkPermissionsService#canEditRegEarnCode(org.kuali.hr.time.timeblock.TimeBlock)
     * this method is used in calendar.tag
     * it's only used when a user is working on its own timesheet, regular earn code cannot be editable on clock entered time block
     */
    @Override
    public boolean canEditRegEarnCode(TimeBlock tb) {
    	AssignmentDescriptionKey adk = new AssignmentDescriptionKey(tb.getJobNumber().toString(), tb.getWorkArea().toString(), tb.getTask().toString());
        Assignment anAssignment = TkServiceLocator.getAssignmentService().getAssignment(adk, tb.getBeginDate());
        if(anAssignment != null) {
        	// use timesheet's end date to get Time Collection Rule
        	TimesheetDocumentHeader tdh = TkServiceLocator.getTimesheetDocumentHeaderService().getDocumentHeader(tb.getDocumentId());
        	Date aDate =  tb.getBeginDate();
        	if(tdh != null && tdh.getPayEndDate() != null) {
        		aDate = new java.sql.Date(tdh.getPayEndDate().getTime());
        	}
        	TimeCollectionRule tcr = TkServiceLocator.getTimeCollectionRuleService()
        								.getTimeCollectionRule(anAssignment.getDept(), anAssignment.getWorkArea()
        										, anAssignment.getJob().getHrPayType(), aDate);
        	if(tcr != null && tcr.isClockUserFl()) {
        		// use assignment to get the payType object, then check if the regEarnCode of the paytyep matches the earn code of the timeblock
        		// if they do match, then return false
        		PayType pt = TkServiceLocator.getPayTypeService().getPayType(anAssignment.getJob().getHrPayType(), anAssignment.getJob().getEffectiveDate());
        		if(pt != null && pt.getRegEarnCode().equals(tb.getEarnCode())) {
        			return false;
        		}
        	}
        }
    	return true;
    }

    @Override
    public boolean canDeleteDeptLunchDeduction() {
        return TKContext.getUser().isAnyApproverActive();
    }

    @Override
    public boolean canAddSystemLevelRole() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean canAddLocationLevelRoles() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean canAddDepartmentLevelRoles() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean canAddWorkareaLevelRoles() {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean hasManagerialRolesOnWorkArea(TimeBlock tb) {
        return TKContext.getUser().getApproverWorkAreas().contains(tb.getWorkArea())
               || TKContext.getUser().getReviewerWorkAreas().contains(tb.getWorkArea());
    }
    
    @Override
    public boolean canViewTimeTabs() {
    	boolean canViewTimeTabs = false;
    	Date asOfDate = TKUtils.getTimelessDate(null);
    	String flsaStatus = TkConstants.FLSA_STATUS_NON_EXEMPT;
    	// find active assignments as of currentDate
    	String principalId = TKUser.getCurrentTargetPerson().getPrincipalId();
    	if(isActiveAssignmentFoundOnJobFlsaStatus(principalId, flsaStatus)) {
    		//find timecalendar defined
    		canViewTimeTabs = isCalendarDefined(principalId, asOfDate);
    	}
    	return canViewTimeTabs;
    }
    
    private boolean isActiveAssignmentFoundOnJobFlsaStatus(String principalId, String flsaStatus) {
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
    
    private boolean isCalendarDefined(String principalId, Date asOfDate){
    	boolean calDefined = false;
    	PrincipalHRAttributes principalHRAttributes = TkServiceLocator.getPrincipalHRAttributeService().getPrincipalCalendar(principalId, asOfDate);
    	if(principalHRAttributes != null) {
    		calDefined = principalHRAttributes.getPayCalendar() != null ? true : false;
    	}
    	return calDefined;
    }

}