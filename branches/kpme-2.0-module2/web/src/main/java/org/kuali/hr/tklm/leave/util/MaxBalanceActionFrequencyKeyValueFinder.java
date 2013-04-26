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
package org.kuali.hr.tklm.leave.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.kuali.hr.tklm.common.TkConstants;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;

public class MaxBalanceActionFrequencyKeyValueFinder extends KeyValuesBase {

	@Override
	public List<KeyValue> getKeyValues() {
		List<KeyValue> keyValues = new ArrayList<KeyValue>();
		for (Map.Entry entry : TkConstants.MAX_BALANCE_ACTION_FREQUENCY.entrySet()) {
            keyValues.add(new ConcreteKeyValue((String)entry.getKey(), (String)entry.getValue()));
        }     		     		
        return keyValues;
	}

}
