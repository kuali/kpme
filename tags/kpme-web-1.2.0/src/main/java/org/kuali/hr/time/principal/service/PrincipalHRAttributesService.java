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
package org.kuali.hr.time.principal.service;

import java.util.Date;
import java.util.List;
import org.kuali.hr.time.principal.PrincipalHRAttributes;
import org.springframework.cache.annotation.Cacheable;

public interface PrincipalHRAttributesService {
	/**
	 * Fetch PrincipalCalendar object at a particular date
	 * @param principalId
	 * @param asOfDate
	 * @return
	 */
    @Cacheable(value= PrincipalHRAttributes.CACHE_NAME, key="'principalId=' + #p0 + '|' + 'asOfDate=' + #p1")
	public PrincipalHRAttributes getPrincipalCalendar(String principalId, Date asOfDate);

	/**
	 * Fetch inactive PrincipalHRAttributes object at a particular date
	 * @param principalId
	 * @param asOfDate
	 * @return
	 */
    public PrincipalHRAttributes getInactivePrincipalHRAttributes(String principalId, Date asOfDate);
	/**
	 * Fetch PrincipalHRAttributes object with given id
	 * @param hrPrincipalAttributeId
	 * @return
	 */
    public PrincipalHRAttributes getPrincipalHRAttributes(String hrPrincipalAttributeId);
    
    public List<PrincipalHRAttributes> getAllActivePrincipalHrAttributesForPrincipalId(String principalId, Date asOfDate);
    
    public List<PrincipalHRAttributes> getAllInActivePrincipalHrAttributesForPrincipalId(String principalId, Date asOfDate);
    
    public PrincipalHRAttributes getMaxTimeStampPrincipalHRAttributes(String principalId);
    
    /*
     * Fetch list of PrincipalHRAttributes that become active for given principalId and date range
     */
    public List<PrincipalHRAttributes> getActivePrincipalHrAttributesForRange(String principalId, Date startDate, Date endDate);
    /*
     * Fetch list of PrincipalHRAttributes that become inactive for given principalId and date range
     */
    public List<PrincipalHRAttributes> getInactivePrincipalHRAttributesForRange(String principalId, Date startDate, Date endDate);
}