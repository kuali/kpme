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
package org.kuali.hr.time.paytype.service;

import org.kuali.hr.time.paytype.PayType;
import org.kuali.hr.time.util.TKUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.Date;
import java.util.List;

public interface PayTypeService {
	/**
	 * Save or Update a Paytype
	 * @param payType
	 */
    @CacheEvict(value={PayType.CACHE_NAME}, allEntries = true)
	public void saveOrUpdate(PayType payType);
	/**
	 * Save or Update a List of PayType objects
	 * @param payTypeList
	 */
    @CacheEvict(value={PayType.CACHE_NAME}, allEntries = true)
	public void saveOrUpdate(List<PayType> payTypeList);
	
	/**
	 * Provides access to the PayType.   The PayCalendar will be loaded as well.
	 * @param payType
	 * @return A fully populated PayType.
	 */
    @Cacheable(value= PayType.CACHE_NAME, key="'payType=' + #p0 + '|' + 'effectiveDate=' + #p1")
	public PayType getPayType(String payType, Date effectiveDate);

    @Cacheable(value= PayType.CACHE_NAME, key="'hrPayTypeId=' + #p0")
	public PayType getPayType(String hrPayTypeId);
	
	/**
	 * get count of pay type with give payType
	 * @param payType
	 * @return int
	 */
	public int getPayTypeCount(String payType);

    List<PayType> getPayTypes(String payType, String regEarnCode, String descr, Date fromEffdt,
     Date toEffdt, String active, String showHist);
}