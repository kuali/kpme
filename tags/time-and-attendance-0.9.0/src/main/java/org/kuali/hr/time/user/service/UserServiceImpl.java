package org.kuali.hr.time.user.service;

import org.kuali.hr.time.assignment.Assignment;
import org.kuali.hr.time.assignment.service.AssignmentService;
import org.kuali.hr.time.roles.TkRole;
import org.kuali.hr.time.roles.TkUserRoles;
import org.kuali.hr.time.roles.service.TkRoleService;
import org.kuali.hr.time.service.base.TkServiceLocator;
import org.kuali.hr.time.util.TKUser;
import org.kuali.hr.time.util.TKUtils;
import org.kuali.rice.kim.bo.Person;
import org.kuali.rice.kim.service.KIMServiceLocator;

import java.sql.Date;
import java.util.List;

public class UserServiceImpl implements UserService {

    @Override
    public TKUser buildTkUser(String actualPrincipalId, Date asOfDate) {
        Person person = KIMServiceLocator.getPersonService().getPerson(actualPrincipalId);
        return buildTkUser(person, null, null, asOfDate);
    }

    @Override
    public TKUser buildTkUser(Person actual, Person backdoor, Person target, Date asOfDate) {
        TKUser tkUser = new TKUser();

        if (actual == null) {
            throw new RuntimeException("Can not create user with empty principal id.");
        }

        tkUser.setActualPerson(actual);
        tkUser.setBackdoorPerson(backdoor);
        tkUser.setTargetPerson(target);

        loadRoles(tkUser);

        return tkUser;
    }

    /**
	 * Helper method to load roles.
	 *
	 * We are looking looking for the roles with the most recent effective
	 * date.
	 *
	 * @param user
	 */
	public static void loadRoles(TKUser user) {
		TkRoleService roleService = TkServiceLocator.getTkRoleService();
		AssignmentService assignmentService = TkServiceLocator.getAssignmentService();

		Date asOfDate = TKUtils.getCurrentDate();
		Date payPeriodBeginDate = TKUtils.getCurrentDate();

		if (user.getBackdoorPerson() != null) {
			List<TkRole> roles = TkServiceLocator.getTkRoleService().getRoles(user.getBackdoorPerson().getPrincipalId(), asOfDate);
			List<Assignment> assignments = assignmentService.getAssignments(user.getBackdoorPerson().getPrincipalId(), payPeriodBeginDate);
			user.setBackdoorPersonRoles(new TkUserRoles(user.getBackdoorPerson().getPrincipalId(), roles,assignments));
		}

        if (user.getTargetPerson() != null) {
            List<TkRole> roles = TkServiceLocator.getTkRoleService().getRoles(user.getTargetPerson().getPrincipalId(), asOfDate);
            List<Assignment> assignments = assignmentService.getAssignments(user.getTargetPerson().getPrincipalId(), payPeriodBeginDate);
            user.setTargetPersonRoles(new TkUserRoles(user.getTargetPerson().getPrincipalId(), roles,assignments));
        }

		List<TkRole> roles = roleService.getRoles(user.getActualPerson().getPrincipalId(), asOfDate);
		List<Assignment> assignments = assignmentService.getAssignments(user.getActualPerson().getPrincipalId(), payPeriodBeginDate);
		user.setActualPersonRoles(new TkUserRoles(user.getActualPerson().getPrincipalId(), roles, assignments));
	}

}