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
package org.kuali.hr.lm.earncodesec.service;

import org.kuali.hr.lm.earncodesec.EarnCodeSecurity;
import org.kuali.hr.lm.earncodesec.dao.EarnCodeSecurityDao;

import java.sql.Date;
import java.util.List;

public class EarnCodeSecurityServiceImpl implements EarnCodeSecurityService {

    private EarnCodeSecurityDao earnCodeSecurityDao;

	public void setEarnCodeSecurityDao(EarnCodeSecurityDao earnCodeSecurityDao) {
		this.earnCodeSecurityDao = earnCodeSecurityDao;
	}

	@Override
	public List<EarnCodeSecurity> getEarnCodeSecurities(String department, String hrSalGroup, String location, java.util.Date asOfDate) {
		return earnCodeSecurityDao.getEarnCodeSecurities(department, hrSalGroup, location, asOfDate);
	}

	@Override
	public EarnCodeSecurity getEarnCodeSecurity(String hrEarnCodeSecId) {
		return earnCodeSecurityDao.getEarnCodeSecurity(hrEarnCodeSecId);
	}

	@Override
	public List<EarnCodeSecurity> searchEarnCodeSecurities(String dept,
			String salGroup, String earnCode, String location, Date fromEffdt,
			Date toEffdt, String active, String showHistory) {
		return earnCodeSecurityDao.searchEarnCodeSecurities(dept, salGroup, earnCode, location, fromEffdt,
								toEffdt, active, showHistory);
	}
	@Override
	public int getEarnCodeSecurityCount(String dept, String salGroup, String earnCode, String employee, String approver, String location,
			String active, java.sql.Date effdt,String hrDeptEarnCodeId) {
		return earnCodeSecurityDao.getEarnCodeSecurityCount(dept, salGroup, earnCode, employee, approver, location,
				active, effdt, hrDeptEarnCodeId);
	}
	@Override
	public int getNewerEarnCodeSecurityCount(String earnCode, java.util.Date effdt) {
		return earnCodeSecurityDao.getNewerEarnCodeSecurityCount(earnCode, effdt);
	}
}