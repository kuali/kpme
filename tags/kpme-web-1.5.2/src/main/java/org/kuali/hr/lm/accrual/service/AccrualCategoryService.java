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
package org.kuali.hr.lm.accrual.service;

import org.kuali.hr.lm.accrual.AccrualCategory;
import org.springframework.cache.annotation.Cacheable;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

public interface AccrualCategoryService {

	/**
	 * Get an AccrualCategory as of a particular date
	 * @param accrualCategory
	 * @param asOfDate
	 * @return
	 */
    @Cacheable(value= AccrualCategory.CACHE_NAME, key="'accrualCategory=' + #p0 + '|' + 'asOfDate=' + #p1")
    public AccrualCategory getAccrualCategory(String accrualCategory, Date asOfDate);
    /**
     * Save or Update an accrual category
     * @param accrualCategory
     */
    public void saveOrUpdate(AccrualCategory accrualCategory);
    
    /**
     * Fetch accrual category by unique id
     * @param lmAccrualCategoryId
     * @return
     */
    @Cacheable(value= AccrualCategory.CACHE_NAME, key="'accrualCategoryId=' + #p0")
    public AccrualCategory getAccrualCategory(String lmAccrualCategoryId);
    
    /**
     * Fetch list of active accrual categories as of a particular date
     * @param asOfDate
     * @return
     */
    @Cacheable(value= AccrualCategory.CACHE_NAME, key="'asOfDate=' + #p0")
    public List <AccrualCategory> getActiveAccrualCategories(Date asOfDate);

    List <AccrualCategory> getAccrualCategories(String accrualCategory, String accrualCatDescr, String leavePlan, String accrualEarnInterval, String unitOfTime, String minPercentWorked, Date fromEffdt, Date toEffdt, String active, String showHistory);
    
    /**
     * Fetch list of active accrual categories with given leavePlan and date
     * @param leavePlan
     * @param asOfDate
     * @return List <AccrualCategory>
     */
   public List <AccrualCategory> getActiveAccrualCategoriesForLeavePlan(String leavePlan, Date asOfDate);
     
    public List <AccrualCategory> getActiveLeaveAccrualCategoriesForLeavePlan(String leavePlan, Date asOfDate);

    
    public List <AccrualCategory> getInActiveLeaveAccrualCategoriesForLeavePlan(String leavePlan, Date asOfDate);
    
    /**
     * Retreives the principal's balance on the current calendar for the given accrual category through the date supplied.
     * @param principalId The id of the principal 
     * @param accrualCategory The accrual category the balance is being requested of
     * @param asOfDate 
     * @return
     * @throws Exception 
     */
	public BigDecimal getAccruedBalanceForPrincipal(String principalId, AccrualCategory accrualCategory, Date asOfDate);
	
	public BigDecimal getApprovedBalanceForPrincipal(String principalId, AccrualCategory accrualCategory, Date asOfDate);
	
}