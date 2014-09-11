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
package org.kuali.hr.lm.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.kuali.hr.lm.LMConstants;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;

public class AccrualEarnIntervalKeyValueFinder extends KeyValuesBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1001630042078011259L;

	@Override
	public List<KeyValue> getKeyValues() {
		List<KeyValue> keyValues = new ArrayList<KeyValue>();
		// KPME-1347 Kagata Use LMConstant instead of TKConstant
		//for (Map.Entry entry : TkConstants.ACCRUAL_EARN_INTERVAL.entrySet()) {
		for (Map.Entry entry : LMConstants.ACCRUAL_EARN_INTERVAL_MAP.entrySet()) {
			keyValues.add(new ConcreteKeyValue((String)entry.getKey(), (String)entry.getValue()));
        }     		
        return keyValues;
	}

}