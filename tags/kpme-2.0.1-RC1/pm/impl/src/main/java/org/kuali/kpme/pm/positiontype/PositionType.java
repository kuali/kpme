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
package org.kuali.kpme.pm.positiontype;

import org.kuali.kpme.core.bo.HrBusinessObject;
import org.kuali.kpme.core.institution.Institution;
import org.kuali.kpme.core.location.Location;
import org.kuali.kpme.pm.api.positiontype.PositionTypeContract;

import com.google.common.collect.ImmutableList;

public class PositionType extends HrBusinessObject implements PositionTypeContract {
	//KPME-2273/1965 Primary Business Keys List.	
	public static final ImmutableList<String> EQUAL_TO_FIELDS = new ImmutableList.Builder<String>()
		    .add("positionType")
		    .build();

	private static final long serialVersionUID = 1L;
	
	private String pmPositionTypeId;
	private String positionType;
	private String description;
	private String institution;
	private String location;
	
	private Location locationObj;
	private Institution institutionObj;

	@Override
	public String getId() {
		return this.getPmPositionTypeId();
	}

	@Override
	public void setId(String id) {
		setPmPositionTypeId(id);
	}

	@Override
	protected String getUniqueKey() {
		return getPositionType() + "_" + getInstitution() + "_" + getLocation();
	}

	public String getPmPositionTypeId() {
		return pmPositionTypeId;
	}

	public void setPmPositionTypeId(String pmPositionTypeId) {
		this.pmPositionTypeId = pmPositionTypeId;
	}

	public String getPositionType() {
		return positionType;
	}

	public void setPositionType(String PositionType) {
		this.positionType = PositionType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getInstitution() {
		return institution;
	}

	public void setInstitution(String institution) {
		this.institution = institution;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Location getLocationObj() {
		return locationObj;
	}

	public void setLocationObj(Location locationObj) {
		this.locationObj = locationObj;
	}

	public Institution getInstitutionObj() {
		return institutionObj;
	}

	public void setInstitutionObj(Institution institutionObj) {
		this.institutionObj = institutionObj;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}