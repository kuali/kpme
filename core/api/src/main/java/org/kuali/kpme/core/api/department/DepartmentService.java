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
package org.kuali.kpme.core.api.department;

import java.util.List;

import org.joda.time.LocalDate;
import org.kuali.kpme.core.api.department.DepartmentContract;
import org.springframework.cache.annotation.Cacheable;

public interface DepartmentService {
	
    /**
     * Fetch department by id
     * @param hrDeptId
     * @return
     */
    @Cacheable(value=DepartmentContract.CACHE_NAME, key="'hrDeptId=' + #p0")
    Department getDepartment(String hrDeptId);
    
    List<Department> getDepartments(String userPrincipalId, String department, String location, String descr, String active, String showHistory, String payrollApproval);
    
    /**
	 * get count of department with given department
	 * @param department
	 * @return int
	 */
	int getDepartmentCount(String department);
	
	/**
	 * Get Department as of a particular date passed in
	 * @param department
	 * @param asOfDate
	 * @return
	 */
    //@Cacheable(value=DepartmentContract.CACHE_NAME, key="'department=' + #p0 + '|' + 'asOfDate=' + #p1")
    //Department getDepartmentWithDeptAndLocation(String department, LocalDate asOfDate);

    /**
     * Fetch department by id without sub kim role member data
     * @param department
     * @param asOfDate
     * @return Department
     */
    @Cacheable(value=DepartmentContract.CACHE_NAME, key="'{getDepartment}' + 'department=' + #p0 + '|' + 'asOfDate=' + #p1")
    Department getDepartment(String department, LocalDate asOfDate);

    /**
     * Fetches a list of Department objects as of the specified date all of which
     * belong to the indicated location.
     *
     * @param location The search criteria
     * @param asOfDate Effective date
     * @return A List<Department> object.
     */
    @Cacheable(value=DepartmentContract.CACHE_NAME, key="'location=' + #p0 + '|' + 'asOfDate=' + #p1")
    List<Department> getDepartmentsWithLocation(String location, LocalDate asOfDate);

    /**
     * Fetches a list of departments as of the specified date all of which
     * belong to the indicated location.
     *
     * @param location The search criteria
     * @param asOfDate Effective date
     * @return A List<String> object.
     */
    @Cacheable(value=DepartmentContract.CACHE_NAME, key="'{getDepartmentsForLocation}' + 'location=' + #p0 + '|' + 'asOfDate=' + #p1")
    List<String> getDepartmentValuesWithLocation(String location, LocalDate asOfDate);

    /**
     * Fetches a list of departments as of the specified date all of which
     * belong to the indicated locations.
     *
     * @param locations The search criteria
     * @param asOfDate Effective date
     * @return A List<String> object.
     */
    @Cacheable(value=DepartmentContract.CACHE_NAME, key="'{getDepartmentsForLocations}' + 'location=' + T(org.kuali.rice.core.api.cache.CacheKeyUtils).key(#p0) + '|' + 'asOfDate=' + #p1")
    List<String> getDepartmentValuesWithLocations(List<String> locations, LocalDate asOfDate);
    
	/**
	 * get count of department with given department
	 * @param department
	 * @return int
	 */
	List<Department> getDepartments(String department);
	
	/**
     * Fetch department by department, location and effective date
     * @param department
     * @param location
     * @param asOfDate
     * @return Department
     */
    @Cacheable(value=DepartmentContract.CACHE_NAME, key="'department=' + #p0 + '|' + 'location=' + #p1 + '|' + 'asOfDate=' + #p2")
    Department getDepartmentWithDeptAndLocation(String department, String location, LocalDate asOfDate);

    /**
     * Fetches a list of locations as of the specified date all of which
     * belong to the indicated institution.
     *
     * @param institution The search criteria
     * @param asOfDate Effective date
     * @return A List<String> object.
     */
    @Cacheable(value=DepartmentContract.CACHE_NAME, key="'{getDepartmentsForInstitution}' + 'institution=' + #p0 + '|' + 'asOfDate=' + #p1")
    List<String> getLocationsValuesWithInstitution(String institution, LocalDate asOfDate);
}