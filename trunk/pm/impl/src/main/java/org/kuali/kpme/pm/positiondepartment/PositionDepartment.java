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
package org.kuali.kpme.pm.positiondepartment;

import org.kuali.kpme.core.api.departmentaffiliation.service.DepartmentAffiliationService;
import org.kuali.kpme.core.bo.HrBusinessObject;
import org.kuali.kpme.core.department.DepartmentBo;
import org.kuali.kpme.core.departmentaffiliation.DepartmentAffiliation;
import org.kuali.kpme.core.institution.Institution;
import org.kuali.kpme.core.location.Location;
import org.kuali.kpme.core.service.HrServiceLocator;
import org.kuali.kpme.pm.api.positiondepartment.PositionDepartmentContract;
import org.springframework.util.StringUtils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
public class PositionDepartment extends HrBusinessObject implements PositionDepartmentContract {
	
    private static final String DEPARTMENT = "department";

	//TODO reslove the issue with DepartmentAffiliation to implement  PositionDepartmentContract
	
	public static final ImmutableList<String> BUSINESS_KEYS = new ImmutableList.Builder<String>()
		    .add(DEPARTMENT)
		    .build();
	
	private static final long serialVersionUID = 1L;
	
	private String pmPositionDeptId;
	private String institution;
	private String location;
	private String department;
	private String deptAffl;

    private String hrPositionId;
	private Location locationObj;
	private Institution institutionObj;
	private DepartmentBo departmentObj;
	private DepartmentAffiliation deptAfflObj;

	@Override
	public ImmutableMap<String, Object> getBusinessKeyValuesMap() {
		return new ImmutableMap.Builder<String, Object>()
				.put(DEPARTMENT, this.getDepartment())
				.build();
	}	
	
	@Override
	public String getId() {
		return this.getPmPositionDeptId();
	}

	@Override
	public void setId(String id) {
		setPmPositionDeptId(id);
	}

	@Override
	protected String getUniqueKey() {
		return  getInstitution() + "_" + getLocation() + "_" + getDepartment() + "_" + getDeptAffl()	;
	}

	/**
	 * @return the DeptAffl
	 */
	public String getDeptAffl() {
		return deptAffl;
	}

	/**
	 * @param deptAffl the deptAffl to set
	 */
	public void setDeptAffl(String deptAffl) {
		this.deptAffl = deptAffl;
	}

	/**
	 * @return the deptAfflObj
	 */
	public DepartmentAffiliation getDeptAfflObj() {
		
		if (deptAfflObj == null) {
			if (!StringUtils.isEmpty(deptAffl)) {
				DepartmentAffiliationService pdaService = HrServiceLocator.getDepartmentAffiliationService();
				deptAfflObj = (DepartmentAffiliation)pdaService.getDepartmentAffiliationByType(deptAffl);
			}
		} 
		
		return deptAfflObj;
	}

	/**
	 * @param deptAfflObj the deptAfflObj to set
	 */
	public void setDeptAfflObj(
			DepartmentAffiliation deptAfflObj) {
		this.deptAfflObj = deptAfflObj;
	}

    /**
     * @return the hrPositionId
     */
    public String getHrPositionId() {
        return hrPositionId;
    }
    /**
     * @param hrPositionId the hrPositionId to set
     */
    public void setHrPositionId(String hrPositionId) {
        this.hrPositionId = hrPositionId;
    }


    /**
	 * @return the pmPositionDeptId
	 */
	public String getPmPositionDeptId() {
		return pmPositionDeptId;
	}

	/**
	 * @param pmPositionDeptId the pmPositionDeptId to set
	 */
	public void setPmPositionDeptId(String pmPositionDeptId) {
		this.pmPositionDeptId = pmPositionDeptId;
	}

	/**
	 * @return the institution
	 */
	public String getInstitution() {
		return institution;
	}

	/**
	 * @param institution the institution to set
	 */
	public void setInstitution(String institution) {
		this.institution = institution;
	}

	
	
	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * @return the locationObj
	 */
	public Location getLocationObj() {
		return locationObj;
	}

	/**
	 * @param locationObj the locationObj to set
	 */
	public void setLocationObj(Location locationObj) {
		this.locationObj = locationObj;
	}

	/**
	 * @return the department
	 */
	public String getDepartment() {
		return department;
	}

	/**
	 * @param department the department to set
	 */
	public void setDepartment(String department) {
		this.department = department;
	}

	/**
	 * @return the institutionObj
	 */
	public Institution getInstitutionObj() {
		return institutionObj;
	}

	/**
	 * @param institutionObj the institutionObj to set
	 */
	public void setInstitutionObj(Institution institutionObj) {
		this.institutionObj = institutionObj;
	}

	/**
	 * @return the departmentObj
	 */
	public DepartmentBo getDepartmentObj() {
		return departmentObj;
	}

	/**
	 * @param departmentObj the departmentObj to set
	 */
	public void setDepartmentObj(DepartmentBo departmentObj) {
		this.departmentObj = departmentObj;
	}

}