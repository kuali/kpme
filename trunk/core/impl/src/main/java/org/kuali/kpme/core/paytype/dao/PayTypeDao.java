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
package org.kuali.kpme.core.paytype.dao;

import java.util.List;

import org.joda.time.LocalDate;
import org.kuali.kpme.core.paytype.PayTypeBo;

public interface PayTypeDao {

	public void saveOrUpdate(PayTypeBo payType);

	public void saveOrUpdate(List<PayTypeBo> payTypeList);

	public PayTypeBo getPayType(String payType, LocalDate effectiveDate);
	
	public PayTypeBo getPayType(String hrPayTypeId);
	
	public int getPayTypeCount(String payType);

    public List<PayTypeBo> getPayTypes(String payType, String regEarnCode, String descr, String groupKeyCode, String flsaStatus,
    		String payFrequency, LocalDate fromEffdt, LocalDate toEffdt, String active, String showHist);
}