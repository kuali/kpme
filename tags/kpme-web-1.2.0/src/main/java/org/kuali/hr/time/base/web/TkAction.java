/**
 * Copyright 2004-2012 The Kuali Foundation
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
package org.kuali.hr.time.base.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionRedirect;
import org.kuali.hr.time.util.TKContext;
import org.kuali.hr.time.util.TKUser;
import org.kuali.hr.time.util.TkConstants;
import org.kuali.rice.kns.web.struts.action.KualiAction;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.exception.AuthorizationException;
import org.kuali.rice.krad.util.GlobalVariables;

public class TkAction extends KualiAction {

    private static final Logger LOG = Logger.getLogger(TkAction.class);


    protected void checkTKAuthorization(ActionForm form, String methodToCall) throws AuthorizationException {
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String methodToCall = null;
            if (form instanceof TkForm) {
                methodToCall = ((TkForm)form).getMethodToCall();
            }
            checkTKAuthorization(form, methodToCall);
        } catch (AuthorizationException e) {
            LOG.error("User: " + TKContext.getPrincipalId() + " Target: " + TKContext.getTargetPrincipalId(), e);
            return mapping.findForward("unauthorized");
        }

        // Run our logic / security first - For some reason kuali
        // dispatches actions BEFORE checking the security...

        return super.execute(mapping, form, request, response);
    }

	public ActionForward userLogout(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TKContext.clear();
		request.getSession().invalidate();
		return new ActionRedirect(mapping.findForward("basic"));
	}

}