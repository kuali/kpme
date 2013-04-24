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
package org.kuali.hr.time.authorization;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.kuali.hr.core.role.KPMERole;
import org.kuali.hr.time.department.Department;
import org.kuali.hr.time.service.base.TkServiceLocator;
import org.kuali.hr.time.util.TKContext;
import org.kuali.hr.time.util.TkConstants;
import org.kuali.rice.krad.util.GlobalVariables;

public class AuthorizationValidationUtils {
	
    private static final Logger LOG = Logger.getLogger(AuthorizationValidationUtils.class);

    /**
     * Indicates whether or not the current user can wildcard the work area
     * of the specified DepartmentalRule.
     *
     * @param dr The DepartmentalRule we are investigating.
     *
     * @return true if you can wildcard the WorkArea, false otherwise.
     */
    public static boolean canWildcardWorkArea(DepartmentalRule departmentalRule) {
    	boolean canWildcardWorkArea = false;
    	
    	if (TKContext.isSystemAdmin()) {
        	return true;
    	}
    	
    	if (departmentalRule != null) {
	    	String principalId = GlobalVariables.getUserSession().getPrincipalId();
	    	String department = departmentalRule.getDept();
	    	Department departmentObj = TkServiceLocator.getDepartmentService().getDepartment(department, LocalDate.now());
			String location = departmentObj != null ? departmentObj.getLocation() : null;
	    	
	        if (!TkConstants.WILDCARD_CHARACTER.equals(department)) {
	        	canWildcardWorkArea = TkServiceLocator.getTKRoleService().principalHasRoleInDepartment(principalId, KPMERole.TIME_DEPARTMENT_ADMINISTRATOR.getRoleName(), department, new DateTime())
	        			|| TkServiceLocator.getLMRoleService().principalHasRoleInDepartment(principalId, KPMERole.LEAVE_DEPARTMENT_ADMINISTRATOR.getRoleName(), department, new DateTime())
	        			|| TkServiceLocator.getTKRoleService().principalHasRoleInLocation(principalId, KPMERole.TIME_LOCATION_ADMINISTRATOR.getRoleName(), location, new DateTime())
	        			|| TkServiceLocator.getLMRoleService().principalHasRoleInLocation(principalId, KPMERole.LEAVE_LOCATION_ADMINISTRATOR.getRoleName(), location, new DateTime());
	        }
    	}
        
        return canWildcardWorkArea;
    }

    /**
     * Can the current user use a wildcard for the department?
     *
     * @param dr The DepartmentalRule we are examining.
     *
     * @return true if so, false otherwise.
     */
    public static boolean canWildcardDepartment(DepartmentalRule departmentalRule) {
        return TKContext.isSystemAdmin();
    }
    
    public static boolean hasAccessToWrite(DepartmentalRule departmentalRule) {
        boolean hasAccessToWrite = false;
        
        if (TKContext.isSystemAdmin()) {
        	return true;
    	}
        
        if (departmentalRule != null) {
	    	String principalId = GlobalVariables.getUserSession().getPrincipalId();
	    	String department = departmentalRule.getDept();
	    	Department departmentObj = TkServiceLocator.getDepartmentService().getDepartment(department, LocalDate.now());
			String location = departmentObj != null ? departmentObj.getLocation() : null;
	        
	        if (!TkConstants.WILDCARD_CHARACTER.equals(department)) {
	        	hasAccessToWrite = TkServiceLocator.getTKRoleService().principalHasRoleInDepartment(principalId, KPMERole.TIME_DEPARTMENT_ADMINISTRATOR.getRoleName(), department, new DateTime())
	        			|| TkServiceLocator.getLMRoleService().principalHasRoleInDepartment(principalId, KPMERole.LEAVE_DEPARTMENT_ADMINISTRATOR.getRoleName(), department, new DateTime())
	        			|| TkServiceLocator.getTKRoleService().principalHasRoleInLocation(principalId, KPMERole.TIME_LOCATION_ADMINISTRATOR.getRoleName(), location, new DateTime())
	        			|| TkServiceLocator.getLMRoleService().principalHasRoleInLocation(principalId, KPMERole.LEAVE_LOCATION_ADMINISTRATOR.getRoleName(), location, new DateTime());
	        }
        }

        return hasAccessToWrite;
    }
    
}