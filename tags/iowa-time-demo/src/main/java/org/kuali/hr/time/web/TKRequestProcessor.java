package org.kuali.hr.time.web;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.kuali.hr.time.assignment.Assignment;
import org.kuali.hr.time.assignment.service.AssignmentService;
import org.kuali.hr.time.exceptions.UnauthorizedException;
import org.kuali.hr.time.roles.TkRole;
import org.kuali.hr.time.roles.TkUserRoles;
import org.kuali.hr.time.roles.service.TkRoleService;
import org.kuali.hr.time.service.base.TkServiceLocator;
import org.kuali.hr.time.util.TKContext;
import org.kuali.hr.time.util.TKUser;
import org.kuali.hr.time.util.TKUtils;
import org.kuali.rice.core.config.ConfigContext;
import org.kuali.rice.kew.web.UserLoginFilter;
import org.kuali.rice.kew.web.session.UserSession;
import org.kuali.rice.kim.bo.Person;
import org.kuali.rice.kim.service.KIMServiceLocator;
import org.kuali.rice.kns.service.KNSServiceLocator;
import org.kuali.rice.kns.web.struts.action.KualiRequestProcessor;

public class TKRequestProcessor extends KualiRequestProcessor {
	private static final Logger LOG = Logger.getLogger(TKRequestProcessor.class);

	@Override
	public void process(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		TKContext.setHttpServletRequest(request);
		super.process(request, response);
	}
	
	@Override
	/**
	 * This method calls into our backdoor and TKUser setup.
	 */
	protected boolean processPreprocess(HttpServletRequest request, HttpServletResponse response) {
		boolean status = super.processPreprocess(request, response);
		
		setUserOnContext(request);
		
		return status;
	}
	
	/**
	 * This method exists because the UnitTests need to set the request as well.
	 * 
	 * @param request
	 */
	public void setUserOnContext(HttpServletRequest request) {
		if (request != null) {
			UserSession userSession = UserLoginFilter.getUserSession(request);
			TKUser tkUser = new TKUser();
			Person person = null;
			Person backdoorPerson = null;
			if(userSession!=null){
				backdoorPerson = userSession.getBackdoorPerson();
				person = userSession.getActualPerson();
			}
			
			// Check for test mode; if not test mode check for backDoor validity.
			if (new Boolean(ConfigContext.getCurrentContextConfig().getProperty("test.mode"))) {
				request.setAttribute("principalName", TkLoginFilter.TEST_ID);
				person = KIMServiceLocator.getPersonService().getPerson(TkLoginFilter.TEST_ID);
				backdoorPerson = null;
			} else {
				if (backdoorPerson != null) {
					LOG.debug("Backdoor user in use:" + backdoorPerson.getPrincipalId());
					if (KNSServiceLocator.getKualiConfigurationService().isProductionEnvironment()) {
						userSession.clearBackdoor();
						// TODO : we could simply clear the backdoor as well.
						throw new UnauthorizedException("Cannot backdoor in production environment");
					}
				}
			}
			
			tkUser.setBackdoorPerson(backdoorPerson);
			tkUser.setActualPerson(person);
			tkUser.setUserPreference(TkServiceLocator.getUserPreferenceService().getUserPreferences(tkUser.getPrincipalId()));
			loadRoles(tkUser);
			TKContext.setUser(tkUser);
		} else {
			// Bail with Exception
			throw new RuntimeException("Null HttpServletRequest while setting user.");
		}		
	}
	
	/**
	 * Helper method to load roles.  
	 * 
	 * TODO : Do we want to load both backdoor and Regular roles?  In most
	 * situations if there is a backdoor user, we are looking at the backdoor
	 * roles.
	 * 
	 * We are looking looking for the roles with the most recent effective
	 * date.
	 * 
	 * @param user
	 */
	private void loadRoles(TKUser user) {
		TkRoleService roleService = TkServiceLocator.getTkRoleService();
		AssignmentService assignmentService = TkServiceLocator.getAssignmentService();
		
		Date asOfDate = TKUtils.getCurrentDate();
		Date payPeriodBeginDate = TKUtils.getCurrentDate(); // TODO : Fix this!
		
		if (user.getBackdoorPerson() != null) {
			List<TkRole> roles = roleService.getRoles(user.getBackdoorPerson().getPrincipalId(), asOfDate);
			List<Assignment> assignments = assignmentService.getAssignments(user.getBackdoorPerson().getPrincipalId(), payPeriodBeginDate);
			user.setBackdoorPersonRoles(new TkUserRoles(roles,assignments));
		}
		
		List<TkRole> roles = roleService.getRoles(user.getActualPerson().getPrincipalId(), asOfDate);
		// TODO - we need to handle the Assignment / Employee role.
		// 
		// This seems expensive/excessive, unless it's cached.
		List<Assignment> assignments = assignmentService.getAssignments(user.getActualPerson().getPrincipalId(), payPeriodBeginDate);
		
		// 
		user.setActualPersonRoles(new TkUserRoles(roles, assignments));
	}
}