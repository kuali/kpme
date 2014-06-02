/**
 * Copyright 2004-2014 The Kuali Foundation
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
package org.kuali.kpme.core.earncode.security.web;

import org.kuali.kpme.core.api.earncode.security.EarnCodeSecurity;
import org.kuali.kpme.core.earncode.security.EarnCodeSecurityBo;
import org.kuali.kpme.core.lookup.KPMELookupableImpl;
import org.kuali.kpme.core.service.HrServiceLocator;
import org.kuali.kpme.core.util.HrConstants;
import org.kuali.kpme.core.util.TKUtils;
import org.kuali.rice.core.api.mo.ModelObjectUtils;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.web.form.LookupForm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EarnCodeSecurityLookupableImpl extends KPMELookupableImpl{

	private static final ModelObjectUtils.Transformer<EarnCodeSecurity, EarnCodeSecurityBo> toEarnCodeSecurityBo =
            new ModelObjectUtils.Transformer<EarnCodeSecurity, EarnCodeSecurityBo>() {
                public EarnCodeSecurityBo transform(EarnCodeSecurity input) {
                    return EarnCodeSecurityBo.from(input);
                };
            };

	@Override
	protected List<?> getSearchResults(LookupForm form, Map<String, String> searchCriteria, boolean unbounded) {
		String salGroup = searchCriteria.get("hrSalGroup");
        String dept = searchCriteria.get("dept");
        String earnCode = searchCriteria.get("earnCode");
        String location = searchCriteria.get("location");
        String fromEffdt = TKUtils.getFromDateString(searchCriteria.get("effectiveDate"));
        String toEffdt = TKUtils.getToDateString(searchCriteria.get("effectiveDate"));
        String active = searchCriteria.get("active");
        String showHist = searchCriteria.get("history");
        String earnCodeType = searchCriteria.get("earnCodeType");
        String groupKeyCode = searchCriteria.get("groupKeyCode");

        List<EarnCodeSecurityBo> searchResults = new ArrayList<EarnCodeSecurityBo>();
        List<EarnCodeSecurityBo> rawSearchResults = ModelObjectUtils.transform(HrServiceLocator.getEarnCodeSecurityService().getEarnCodeSecuritiesByType(GlobalVariables.getUserSession().getPrincipalId(), dept, salGroup, earnCode, location, TKUtils.formatDateString(fromEffdt), 
                        TKUtils.formatDateString(toEffdt), active, showHist, earnCodeType, groupKeyCode), toEarnCodeSecurityBo );
        
        if(rawSearchResults != null && !rawSearchResults.isEmpty()) {
                for(EarnCodeSecurityBo ecs : rawSearchResults) {
                        ecs.setEarnCodeType(HrConstants.EARN_CODE_SECURITY_TYPE.get(ecs.getEarnCodeType()));
                        searchResults.add(ecs);
                }
        }
        
        return searchResults;
	}

}