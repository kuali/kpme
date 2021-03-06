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
package org.kuali.kpme.core.api.leaveplan;

import java.sql.Time;

import org.joda.time.LocalTime;
import org.kuali.kpme.core.api.bo.HrBusinessObjectContract;
import org.kuali.kpme.core.api.mo.Effective;
import org.kuali.kpme.core.api.mo.KpmeEffectiveDataTransferObject;
import org.kuali.kpme.core.api.mo.UserModified;
import org.kuali.kpme.core.api.util.HrApiConstants;
import org.kuali.rice.core.api.mo.common.GloballyUnique;
import org.kuali.rice.core.api.mo.common.Identifiable;
import org.kuali.rice.core.api.mo.common.Versioned;
import org.kuali.rice.core.api.mo.common.active.Inactivatable;

/**
 * <p>LeavePlanContract interface.</p>
 *
 */
public interface LeavePlanContract extends KpmeEffectiveDataTransferObject {
	
	public static final String CACHE_NAME = HrApiConstants.CacheNamespace.NAMESPACE_PREFIX + "LeavePlan";
	
	/**
	 * The date batch job should run to create a carry over leave block 
	 * for each accrual category balance from the prior year under a LeavePlan
	 * 
	 * <p>
	 * batchPriorYearCarryOverStartDate of a LeavePlan
	 * <p>
	 * 
	 * @return batchPriorYearCarryOverStartDate for LeavePlan
	 */
	public String getBatchPriorYearCarryOverStartDate();
	
	/**
	 * The Time batch job should run to create a carry over leave block 
	 * for each accrual category balance from the prior year under a LeavePlan
	 * 
	 * <p>
	 * batchPriorYearCarryOverStartTime of a LeavePlan
	 * <p>
	 * 
	 * @return batchPriorYearCarryOverStartTime for LeavePlan
	 */
	public LocalTime getBatchPriorYearCarryOverStartLocalTime();
	
	/**
	 * The Number of months to build accruals for for a LeavePlan
	 * 
	 * <p>
	 * planningMonths of a LeavePlan
	 * <p>
	 * 
	 * @return planningMonths for LeavePlan
	 */
	public String getPlanningMonths();

	/**
	 * The Primary Key of a LeavePlan entry saved in a database
	 * 
	 * <p>
	 * lmLeavePlanId of an LeavePlan
	 * <p>
	 * 
	 * @return lmLeavePlanId for LeavePlan
	 */
	public String getLmLeavePlanId();
	
	/**
	 * The name of a LeavePlan
	 * 
	 * <p>
	 * leavePlan field of a LeavePlan
	 * <p>
	 * 
	 * @return leavePlan for LeavePlan
	 */
	public String getLeavePlan();

	/**
	 * The description of a LeavePlan
	 * 
	 * <p>
	 * description of a LeavePlan
	 * <p>
	 * 
	 * @return description for LeavePlan
	 */
	public String getDescr();
	
	/**
	 * The Month and Day (MM/DD) of the start of the year for a LeavePlan
	 * 
	 * <p>
	 * calendarYearStart of a LeavePlan
	 * <p>
	 * 
	 * @return calendarYearStart for LeavePlan
	 */
	public String getCalendarYearStart() ;

}
