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
package org.kuali.hr.location;

import org.kuali.hr.core.KPMEConstants;
import org.kuali.hr.time.HrBusinessObject;

public class Location extends HrBusinessObject {

	private static final long serialVersionUID = 9015089510044249197L;

	public static final String CACHE_NAME = KPMEConstants.APPLICATION_NAMESPACE_CODE + "/" + "Location";

	private String hrLocationId;
	private String location;
	private String timezone;
	private String description;
	private String userPrincipalId;
	private String history;

	public String getHrLocationId() {
		return hrLocationId;
	}

	public void setHrLocationId(String hrLocationId) {
		this.hrLocationId = hrLocationId;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUserPrincipalId() {
		return userPrincipalId;
	}

	public void setUserPrincipalId(String userPrincipalId) {
		this.userPrincipalId = userPrincipalId;
	}

	@Override
	public String getUniqueKey() {
		return location;
	}

	public String getHistory() {
		return history;
	}

	public void setHistory(String history) {
		this.history = history;
	}
	
	@Override
	public String getId() {
		return getHrLocationId();
	}

	@Override
	public void setId(String id) {
		setHrLocationId(id);
	}

}
