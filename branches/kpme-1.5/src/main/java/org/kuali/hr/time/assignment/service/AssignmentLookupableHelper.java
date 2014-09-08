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
package org.kuali.hr.time.assignment.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.kuali.hr.time.HrEffectiveDateActiveLookupableHelper;
import org.kuali.hr.time.assignment.Assignment;
import org.kuali.hr.time.department.Department;
import org.kuali.hr.time.service.base.TkServiceLocator;
import org.kuali.hr.time.util.TKContext;
import org.kuali.hr.time.util.TKUser;
import org.kuali.hr.time.util.TKUtils;
import org.kuali.rice.kns.lookup.HtmlData;
import org.kuali.rice.kns.lookup.HtmlData.AnchorHtmlData;
import org.kuali.rice.krad.bo.BusinessObject;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.UrlFactory;

public class AssignmentLookupableHelper extends HrEffectiveDateActiveLookupableHelper {

	private static final long serialVersionUID = 774015772672806415L;

	@Override
    public List<HtmlData> getCustomActionUrls(BusinessObject businessObject, List pkNames) {
    	List<HtmlData> customActionUrls = new ArrayList<HtmlData>();
		
		List<HtmlData> defaultCustomActionUrls = super.getCustomActionUrls(businessObject, pkNames);        
        
		Assignment assignment = (Assignment) businessObject;
        String tkAssignmentId = assignment.getTkAssignmentId();
        Department dept = TkServiceLocator.getDepartmentService().getDepartment(assignment.getDept(), TKUtils.getCurrentDate());
        String location = dept == null ? null : dept.getLocation();
        String department = assignment.getDept();
        
        boolean systemAdmin = TKUser.isSystemAdmin();
		boolean locationAdmin = TKUser.getLocationAdminAreas().contains(location);
		boolean departmentAdmin = TKUser.getDepartmentAdminAreas().contains(department);
		
		for (HtmlData defaultCustomActionUrl : defaultCustomActionUrls){
			if (StringUtils.equals(defaultCustomActionUrl.getMethodToCall(), "edit")) {
				if (systemAdmin || locationAdmin || departmentAdmin) {
					customActionUrls.add(defaultCustomActionUrl);
				}
			} else {
				customActionUrls.add(defaultCustomActionUrl);
			}
		}
		
		Properties params = new Properties();
		params.put(KRADConstants.BUSINESS_OBJECT_CLASS_ATTRIBUTE, getBusinessObjectClass().getName());
		params.put(KRADConstants.DISPATCH_REQUEST_PARAMETER, KRADConstants.MAINTENANCE_NEW_METHOD_TO_CALL);
		params.put("tkAssignmentId", tkAssignmentId);
		AnchorHtmlData viewUrl = new AnchorHtmlData(UrlFactory.parameterizeUrl(KRADConstants.INQUIRY_ACTION, params), "view");
		viewUrl.setDisplayText("view");
		viewUrl.setTarget(AnchorHtmlData.TARGET_BLANK);
		customActionUrls.add(viewUrl);
		
		return customActionUrls;
    }

    @Override
    public List<? extends BusinessObject> getSearchResults(Map<String, String> fieldValues) {
        String fromEffdt = TKUtils.getFromDateString(fieldValues.get("effectiveDate"));
        String toEffdt = TKUtils.getToDateString(fieldValues.get("effectiveDate"));
        String principalId = fieldValues.get("principalId");
        String jobNumber = fieldValues.get("jobNumber");
        String dept = fieldValues.get("dept");
        String workArea = fieldValues.get("workArea");
        String active = fieldValues.get("active");
        String showHist = fieldValues.get("history");

        return TkServiceLocator.getAssignmentService().searchAssignments(TKUtils.formatDateString(fromEffdt), TKUtils.formatDateString(toEffdt), principalId, 
        		jobNumber, dept, workArea, active, showHist);
    }

}