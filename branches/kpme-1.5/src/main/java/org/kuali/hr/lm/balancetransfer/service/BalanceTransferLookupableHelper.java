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
package org.kuali.hr.lm.balancetransfer.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.kuali.hr.lm.balancetransfer.BalanceTransfer;
import org.kuali.hr.time.HrEffectiveDateActiveLookupableHelper;
import org.kuali.rice.kns.lookup.HtmlData;
import org.kuali.rice.kns.lookup.HtmlData.AnchorHtmlData;
import org.kuali.rice.krad.bo.BusinessObject;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.UrlFactory;

public class BalanceTransferLookupableHelper extends
		HrEffectiveDateActiveLookupableHelper {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6910172165048825489L;

	@SuppressWarnings("deprecation")
	@Override
	public List<HtmlData> getCustomActionUrls(BusinessObject businessObject, List pkNames) {
		List<HtmlData> customActionUrls = super.getCustomActionUrls(businessObject, pkNames);
		
		List<HtmlData> copyOfUrls = new ArrayList<HtmlData>();
		copyOfUrls.addAll(customActionUrls);
		for(HtmlData aData : copyOfUrls) {
			if(aData.getMethodToCall().equals(KRADConstants.MAINTENANCE_EDIT_METHOD_TO_CALL)) {
				customActionUrls.remove(aData);
			}
		}
		BalanceTransfer bt = (BalanceTransfer) businessObject;
		String btId = bt.getBalanceTransferId();
		
		Properties params = new Properties();
		params.put(KRADConstants.BUSINESS_OBJECT_CLASS_ATTRIBUTE, getBusinessObjectClass().getName());
		params.put(KRADConstants.DISPATCH_REQUEST_PARAMETER, KRADConstants.MAINTENANCE_NEW_METHOD_TO_CALL);
		params.put("balanceTransferId", btId);
		AnchorHtmlData viewUrl = new AnchorHtmlData(UrlFactory.parameterizeUrl(KRADConstants.INQUIRY_ACTION, params), "view");
		viewUrl.setDisplayText("view");
		viewUrl.setTarget(AnchorHtmlData.TARGET_BLANK);
		customActionUrls.add(viewUrl);
		
		return customActionUrls;
	}

	@Override
	public List<? extends BusinessObject> getSearchResults(
			Map<String, String> fieldValues) {
		// TODO Auto-generated method stub
		return super.getSearchResults(fieldValues);
	}

}