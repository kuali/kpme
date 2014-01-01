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
package org.kuali.kpme.core.api.earncode.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.joda.time.LocalDate;
import org.kuali.kpme.core.api.assignment.AssignmentContract;
import org.kuali.kpme.core.api.earncode.EarnCodeContract;
import org.kuali.kpme.core.api.earncode.security.EarnCodeSecurityContract;
import org.springframework.cache.annotation.Cacheable;

public interface EarnCodeService {

	/**
	 * Fetch a list of earn codes for Leave usage, for a particular assignment as of a particular date
     * @param a
     * @param asOfDate
     * @param isLeavePlanningCalendar
	 * @return
	 */
    @Cacheable(value=EarnCodeContract.CACHE_NAME, key="'{getEarnCodesForLeave}' + 'principalId=' + T(org.kuali.kpme.core.util.HrContext).getPrincipalId() + '|' + 'targetId=' + T(org.kuali.kpme.core.util.HrContext).getTargetPrincipalId() + '|' + 'a=' + #p0.getTkAssignmentId() + '|' + 'asOfDate=' + #p1 + '|' + 'isLeavePlanningCalendar=' +#p2")
    public List<? extends EarnCodeContract> getEarnCodesForLeave(AssignmentContract a, LocalDate asOfDate, boolean isLeavePlanningCalendar);

    /**
     * Fetch a list of earn codes based on principal ID as of a particular date
     * @param principalId
     * @param asOfDate
     * @param isLeavePlanningCalendar
     * @return
     */
    @Cacheable(value=EarnCodeContract.CACHE_NAME, key="'principalId=' + #p0 + '|' + 'asOfDate=' + #p1 + '|' + 'isLeavePlanningCalendar=' +#p2")
    public List<? extends EarnCodeContract> getEarnCodesForPrincipal(String principalId, LocalDate asOfDate, boolean isLeavePlanningCalendar);

    /**
     * Fetch an EarnCode as of a particular date
     * @param earnCode
     * @param asOfDate
     * @return
     */
    @Cacheable(value=EarnCodeContract.CACHE_NAME, key="'earnCode=' + #p0 + '|' + 'asOfDate=' + #p1")
	public EarnCodeContract getEarnCode(String earnCode, LocalDate asOfDate);

    /**
     * Fetch the earn code type for a particular date
     * @param earnCode
     * @param asOfDate
     * @return
     */
    @Cacheable(value=EarnCodeContract.CACHE_NAME, key="'{getEarnCodeType}' + 'earnCode=' + #p0 + '|' + 'asOfDate=' + #p1")
    String getEarnCodeType(String earnCode, LocalDate asOfDate);
    
    /**
     * Fetch earn code by id
     * @param earnCodeId
     * @return
     */
    @Cacheable(value=EarnCodeContract.CACHE_NAME, key="'earnCodeId=' + #p0")
    public EarnCodeContract getEarnCodeById(String earnCodeId);
    
    /**
     * Fetch list of system defined overtime earn codes
     * @param asOfDate
     * @return
     */
    @Cacheable(value=EarnCodeContract.CACHE_NAME, key="'{getOvertimeEarnCodes}' + 'asOfDate=' + #p0")
    public List<? extends EarnCodeContract> getOvertimeEarnCodes(LocalDate asOfDate);


    /**
     * Fetch list of system defined overtime earn codes as strings
     * @param asOfDate
     * @return
     */
    @Cacheable(value=EarnCodeContract.CACHE_NAME, key="'{getOvertimeEarnCodesStrs}' + 'asOfDate=' + #p0")
    public List<String> getOvertimeEarnCodesStrs(LocalDate asOfDate);

    /**
	 * get count of earn code with give earnCode
	 * @param earnCode
	 * @return int
	 */
    public int getEarnCodeCount(String earnCode);

    /**
	 * get count of newer version of earn code with give earnCode and date
	 * @param earnCode
	 * @param effdt
	 * @return int
	 */
    public int getNewerEarnCodeCount(String earnCode, LocalDate effdt);
    
    /**
     * roundHrsWithLEarnCode
     * @param hours
     * @param earnCode
     * @return
     */
    public BigDecimal roundHrsWithEarnCode(BigDecimal hours, EarnCodeContract earnCode);

    /**
     * @param principalId
     * @param isLeavePlanningCalendar
     * @return
     */
    @Cacheable(value= EarnCodeContract.CACHE_NAME, key="'{getEarnCodesForDisplay}' + 'principalId=' + #p0 + '|' + 'isLeavePlanningCalendar=' +#p1")
    public Map<String, String> getEarnCodesForDisplay(String principalId, boolean isLeavePlanningCalendar);

    /**
     * @param principalId
     * @param asOfDate
     * @return
     */
    @Cacheable(value= EarnCodeContract.CACHE_NAME, key="'{getEarnCodesForDisplayWithEffectiveDate}' + 'principalId=' + #p0 + '|' + 'asOfDate=' + #p1 + '|' + 'isLeavePlanningCalendar=' +#p2")
    public Map<String, String> getEarnCodesForDisplayWithEffectiveDate(String principalId, LocalDate asOfDate, boolean isLeavePlanningCalendar);

    List<? extends EarnCodeContract> getEarnCodes(String earnCode, String ovtEarnCode, String leavePlan, String accrualCategory, String descr, LocalDate fromEffdt, LocalDate toEffdt, String active, String showHist);
    
    public boolean addEarnCodeBasedOnEmployeeApproverSettings(EarnCodeSecurityContract security, AssignmentContract a, LocalDate asOfDate);
}
