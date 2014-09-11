package org.kuali.hr.time.earncode.service;

import org.kuali.hr.time.HrEffectiveDateActiveLookupableHelper;
import org.kuali.hr.time.earncode.EarnCode;
import org.kuali.hr.time.util.TKContext;
import org.kuali.rice.kns.bo.BusinessObject;
import org.kuali.rice.kns.lookup.HtmlData;

import java.util.List;

public class EarnCodeLookupableHelper extends HrEffectiveDateActiveLookupableHelper {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("rawtypes")
	@Override
	public List<HtmlData> getCustomActionUrls(BusinessObject businessObject,
			List pkNames) {
		List<HtmlData> customActionUrls = super.getCustomActionUrls(
				businessObject, pkNames);
		if (TKContext.getUser().getCurrentRoles().isSystemAdmin()) {
			EarnCode earnCodeObj = (EarnCode) businessObject;
			final String className = this.getBusinessObjectClass().getName();
			final String earnCode = earnCodeObj.getEarnCode();
			HtmlData htmlData = new HtmlData() {

				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public String constructCompleteHtmlTag() {
					return "<a target=\"_blank\" href=\"inquiry.do?businessObjectClassName="
							+ className
							+ "&methodToCall=start&earnCode="
							+ earnCode + "&hrEarnCodeId=\">view</a>";
				}
			};
			customActionUrls.add(htmlData);
		} else if (customActionUrls.size() != 0) {
			customActionUrls.remove(0);
		}
		return customActionUrls;
	}
}