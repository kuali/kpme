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
package org.kuali.hr.lm.accrual.dao;

import org.kuali.hr.lm.accrual.AccrualCategory;

import java.sql.Date;
import java.util.List;

public interface AccrualCategoryDao {

	/**
	 * Fetch accrual category as of a particular date
	 * @param accrualCategory
	 * @param asOfDate
	 * @return
	 */
    public AccrualCategory getAccrualCategory(String accrualCategory, Date asOfDate);
    public void saveOrUpdate(AccrualCategory accrualCategory);
    /**
     * Fetch accrual category by a unique id
     * @param lmAccrualCategoryId
     * @return
     */
    public AccrualCategory getAccrualCategory(String lmAccrualCategoryId);
    /**
     * Fetch list of active accrual categories
     * @param asOfDate
     * @return
     */
    public List<AccrualCategory> getActiveAccrualCategories(Date asOfDate);

    List<AccrualCategory> getAccrualCategories(String accrualCategory, String descr, String leavePlan, String accrualEarnInterval, String unitOfTime, String minPercentWorked, Date fromEffdt, Date toEffdt, String active, String showHistory);

    /**
     * 
     * @param leavePlan
     * @param asOfDate
     * @return
     */
   public List<AccrualCategory> getActiveAccrualCategories(String leavePlan, Date asOfDate);
     
    public List<AccrualCategory> getActiveLeaveAccrualCategoriesForLeavePlan(String leavePlan, Date asOfDate);
	
    
    public List <AccrualCategory> getInActiveLeaveAccrualCategoriesForLeavePlan(String leavePlan, Date asOfDate);

}