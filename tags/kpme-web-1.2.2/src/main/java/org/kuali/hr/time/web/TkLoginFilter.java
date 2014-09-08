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
package org.kuali.hr.time.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.kuali.hr.time.util.TKUtils;
import org.kuali.rice.core.api.config.property.ConfigContext;

public class TkLoginFilter implements Filter {

    private Filter dummyLoginFilter = new org.kuali.rice.kew.web.DummyLoginFilter();
    private static boolean testMode = false;
    public static String TEST_ID = "admin";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest hsRequest = (HttpServletRequest) request;
        if (getTestMode()) {
            hsRequest = new HttpServletRequestWrapper(hsRequest) {
                public String getRemoteUser() {
                    return TEST_ID;
                }
            };
            chain.doFilter(hsRequest, response);
        } else {
            applyRedirectHeader(request, response);
            dummyLoginFilter.doFilter(request, response, chain);
        }
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
        setTestMode();
        dummyLoginFilter.init(config);
    }

    @Override
    public void destroy() {
    	dummyLoginFilter.destroy();
    }

    protected static void setTestMode() {
        testMode = new Boolean(ConfigContext.getCurrentContextConfig().getProperty("test.mode"));
    }

    public static boolean getTestMode() {
        return testMode;
    }

    protected void applyRedirectHeader(ServletRequest request, ServletResponse response) {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        int sessionExpiredTime = TKUtils.getSessionTimeoutTime();

        if (!StringUtils.contains(httpRequest.getRequestURI(), "/SessionInvalidateAction")) {
            if (sessionExpiredTime > 0) {
                Integer pageRedirectTime = sessionExpiredTime;

                httpResponse.setHeader("Refresh", pageRedirectTime + ";URL=" + httpRequest.getContextPath() + "/SessionInvalidateAction.do?methodToCall=invalidateUserSession");
            }
        }
    }


}