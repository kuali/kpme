package org.kuali.hr.time.base.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.kuali.hr.time.util.TKContext;
import org.kuali.hr.time.util.TKUser;
import org.kuali.rice.kew.web.UserLoginFilter;
import org.kuali.rice.kew.web.session.UserSession;
import org.kuali.rice.kns.util.GlobalVariables;
import org.kuali.rice.kns.web.struts.action.KualiAction;

public class TkAction extends KualiAction {

	/**
	 * Action to clear the current users back door setting.  Clears both
	 * workflow and TK backdoor settings.
	 */
	public ActionForward clearBackdoor(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		UserSession userSession = UserLoginFilter.getUserSession(request);

		// There are two different UserSession objects in rice.
		// We will clear them both.
		if (userSession != null) {
			userSession.clearBackdoor();
			GlobalVariables.getUserSession().clearBackdoorUser();
		}

		TKUser tkUser = TKContext.getUser();
		if (tkUser != null) {
			tkUser.clearBackdoorUser();
		}

		return mapping.findForward("basic");
	}
	
	public ActionForward clearTargetUser(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return mapping.findForward("basic");
	}
	
	public ActionForward userLogout(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {		
		request.getSession().invalidate();
		
		return mapping.findForward("basic");
	}

}