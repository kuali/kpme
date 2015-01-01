/**
 * Copyright 2004-2015 The Kuali Foundation
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
package org.kuali.kpme.tklm.time.clocklog.web;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.kuali.kpme.core.inquirable.KPMEInquirableImpl;
import org.kuali.kpme.tklm.time.clocklog.ClockLog;
import org.kuali.kpme.tklm.time.missedpunch.MissedPunch;
import org.kuali.kpme.tklm.time.service.TkServiceLocator;
import org.kuali.rice.kns.inquiry.KualiInquirableImpl;
import org.kuali.rice.krad.bo.BusinessObject;

public class ClockLogInquirableImpl extends KualiInquirableImpl {
	/**
	 * 
	 */
	private static final long serialVersionUID = -972713168985512037L;

	
	@Override
	public BusinessObject getBusinessObject(Map fieldValues) {
		ClockLog clocklog = null;
		
		if(StringUtils.isNotBlank((String)fieldValues.get("tkClockLogId"))) {
			clocklog = TkServiceLocator.getClockLogService().getClockLog((String)fieldValues.get("tkClockLogId"));
			if(clocklog != null) {
				clocklog.setClockedByMissedPunch(TkServiceLocator.getClockLogService().isClockLogCreatedByMissedPunch(clocklog.getTkClockLogId()));
			}
		}
		

		return clocklog;
	}
		
	


}
