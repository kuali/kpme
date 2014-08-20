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
package org.kuali.hr.time.department.dao;

import org.kuali.hr.time.department.Department;

import java.sql.Date;
import java.util.List;

public interface DepartmentDao {
	public void saveOrUpdate(Department dept);
	public Department getDepartment(String department,Date asOfDate);
    public List<Department> getDepartments(String location, Date asOfDate);
    public Department getDepartment(String hrDeptId);
    public List<Department> getDepartmentByLocation(String location);
    public int getDepartmentCount(String department);

    List<Department> getDepartments(String department, String location, String descr, String active);
}