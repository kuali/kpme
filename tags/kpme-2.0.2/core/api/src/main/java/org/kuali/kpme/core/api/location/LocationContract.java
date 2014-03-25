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
package org.kuali.kpme.core.api.location;

import java.util.List;

import org.kuali.kpme.core.api.bo.HrBusinessObjectContract;
import org.kuali.kpme.core.api.role.location.LocationPrincipalRoleMemberBoContract;

/**
 * <p>LocationContract interface.</p>
 *
 */
public interface LocationContract extends HrBusinessObjectContract {
	
	/**
	 * The Primary Key of a Location entry saved in a database
	 * 
	 * <p>
	 * hrLocationId of Location
	 * <p>
	 * 
	 * @return hrLocationId for Location
	 */
	public String getHrLocationId();

	/**
	 * Text field used to identify the location
	 * 
	 * <p>
	 * location of Location
	 * <p>
	 * 
	 * @return location for Location
	 */
	public String getLocation();

	/**
	 * Indicates the timezone for this location
	 * 
	 * <p>
	 * timezone of Location
	 * <p>
	 * 
	 * @return timezone for Location
	 */
	public String getTimezone();

	/**
	 * Text which describes the location code
	 * 
	 * <p>
	 * description of Location
	 * <p>
	 * 
	 * @return description for Location
	 */
	public String getDescription();	
	
	// TODO: not sure if this field is needed...
	public String getUserPrincipalId();

	/**
	 * History flag for Location lookups 
	 * 
	 * <p>
	 * history of Location
	 * </p>
	 * 
	 * @return Y if want to show history, N if not
	 */
	public String getHistory();
	
	/**
	 * Active Role member list for the Location 
	 * 
	 * <p>
	 * roleMembers of Location
	 * </p>
	 * 
	 * @return roleMembers for Location
	 */
	public List<? extends LocationPrincipalRoleMemberBoContract> getRoleMembers();
	
	/**
	 * Inactive Role member list for the Location 
	 * 
	 * <p>
	 * roleMembers of Location
	 * </p>
	 * 
	 * @return roleMembers for Location
	 */
	public List<? extends LocationPrincipalRoleMemberBoContract> getInactiveRoleMembers();
	
}
