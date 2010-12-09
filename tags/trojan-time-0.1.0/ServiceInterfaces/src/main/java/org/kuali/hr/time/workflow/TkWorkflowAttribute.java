package org.kuali.hr.time.workflow;

import java.util.ArrayList;
import java.util.List;

import org.kuali.hr.time.assignment.Assignment;
import org.kuali.hr.time.roles.TkRole;
import org.kuali.hr.time.roles.service.TkRoleService;
import org.kuali.hr.time.service.base.TkServiceLocator;
import org.kuali.hr.time.timesheet.TimesheetDocument;
import org.kuali.hr.time.workarea.WorkArea;
import org.kuali.rice.kew.engine.RouteContext;
import org.kuali.rice.kew.identity.Id;
import org.kuali.rice.kew.identity.PrincipalId;
import org.kuali.rice.kew.routeheader.DocumentContent;
import org.kuali.rice.kew.rule.ResolvedQualifiedRole;
import org.kuali.rice.kew.rule.Role;
import org.kuali.rice.kew.rule.RoleAttribute;

public class TkWorkflowAttribute implements RoleAttribute {

	@Override
	public List<String> getQualifiedRoleNames(String roleName, DocumentContent documentContent) {
		List<String> roles = new ArrayList<String>();
		roles.add(roleName);
		return roles;
	}

	@Override
	public List<Role> getRoleNames() {
		// This is a list of "RoleNames" that this RoleAttribute supports -
		// we can use this to have branching logic in the resolveQualifiedRole()
		// method for different types of "Roles"
		// and have different principal Resolution.
		// ... Now whether or not we need this is another question
		throw new UnsupportedOperationException("Not supported in TkWorkflowAttribute");
	}

	@Override
	/**
	 * Role name is passed in in the routing rule.
	 */
	public ResolvedQualifiedRole resolveQualifiedRole(RouteContext routeContext, String roleName, String qualifiedRole) {
		ResolvedQualifiedRole rqr = new ResolvedQualifiedRole();
		List<Id> principals = new ArrayList<Id>();
		Long routeHeaderId = routeContext.getDocument().getRouteHeaderId();
		TimesheetDocument timesheetDocument = TkServiceLocator.getTimesheetService().getTimesheetDocument(routeHeaderId);
		TkRoleService roleService = TkServiceLocator.getTkRoleService();

		if (timesheetDocument != null) {
			List<Assignment> assignments = timesheetDocument.getAssignments();
			for (Assignment assignment : assignments) {
				WorkArea workArea = assignment.getWorkAreaObj();
				List<TkRole> roles = roleService.getWorkAreaRoles(workArea.getWorkArea(), roleName, workArea.getEffectiveDate());
				for (TkRole role : roles) {
					PrincipalId pid = new PrincipalId(role.getPrincipalId());
					if (!principals.contains(pid)) {
						principals.add(pid);
					}
				}
			}
		} else {
			// TODO Graceful Ballerina Dancing
			throw new RuntimeException("Handle this gracefully - placeholder exception due to missing timesheet document");
		}

		if (principals.size() == 0) 
			throw new RuntimeException("No principals to route to. Push to exception routing.");
		
		rqr.setRecipients(principals);
		return rqr;
	}

}
