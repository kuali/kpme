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
package org.kuali.hr.time.task;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.kuali.hr.time.service.base.TkServiceLocator;
import org.kuali.hr.time.util.TKUtils;
import org.kuali.rice.kns.inquiry.KualiInquirableImpl;
import org.kuali.rice.krad.bo.BusinessObject;

public class TaskInquirableImpl extends KualiInquirableImpl {
	
	@Override
	public BusinessObject getBusinessObject(Map fieldValues) {
        Task task = null;
        if (StringUtils.isNotBlank((String)fieldValues.get("tkTaskId"))) {
        	task = TkServiceLocator.getTaskService().getTask((String) fieldValues.get("tkTaskId"));
        } else if (fieldValues.containsKey("task") && fieldValues.containsKey("effectiveDate")) {
            String taskString = (String)fieldValues.get("task");
            Long taskNumber = taskString != null ? Long.parseLong(taskString) : null;
            task = TkServiceLocator.getTaskService().getTask(taskNumber,
                    new java.sql.Date(TKUtils.convertDateStringToTimestampNoTimezone((String)fieldValues.get("effectiveDate")).getTime()));
        } else {
        	task = (Task) super.getBusinessObject(fieldValues);
        }

		return task;
	}

}