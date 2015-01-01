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
package org.kuali.kpme.core.earncode.security.valuesfinder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;

import org.kuali.kpme.core.util.HrConstants;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.kns.util.KNSGlobalVariables;
import org.kuali.rice.kns.web.struts.form.KualiMaintenanceForm;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;

public class EarnCodeTypeKeyValueFinder extends KeyValuesBase {

	@Override
	public List<KeyValue> getKeyValues() {
				
		List<KeyValue> delegationTypes = new ArrayList<KeyValue>();
		List<KeyValue> delegationTypesForMaintDocs = new ArrayList<KeyValue>();

		// for non maintenance documents, add an "all" option
		for(Entry<String,String> type : HrConstants.EARN_CODE_SECURITY_TYPE.entrySet()) {
	        delegationTypes.add(new ConcreteKeyValue(type.getKey(), type.getValue()));
	        delegationTypesForMaintDocs.add(new ConcreteKeyValue(type.getKey(), type.getValue()));
		}
		
		delegationTypes.add(new ConcreteKeyValue("A","All"));

		Collections.sort(delegationTypes, new Comparator<KeyValue>() {

			@Override
			public int compare(KeyValue o1, KeyValue o2) {
			
				return o2.getKey().compareTo(o1.getKey());
				//T,L,B,A
			}
			
		});
		
		Collections.sort(delegationTypesForMaintDocs, new Comparator<KeyValue>() {

			@Override
			public int compare(KeyValue o1, KeyValue o2) {
			
				return o2.getKey().compareTo(o1.getKey());
				//T,L,B
			}
			
		});
		
		return (KNSGlobalVariables.getKualiForm() instanceof KualiMaintenanceForm) ? delegationTypesForMaintDocs : delegationTypes;
	}

}
