package org.kuali.hr.time.accrual.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.kuali.hr.time.accrual.AccrualCategory;
import org.kuali.rice.kns.bo.BusinessObject;
import org.kuali.rice.kns.lookup.KualiLookupableHelperServiceImpl;

public class AccrualCategoryLookupableHelper extends
		KualiLookupableHelperServiceImpl {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public List<? extends BusinessObject> getSearchResults(
			Map<String, String> fieldValues) {
		String showHistory = null;
		if (fieldValues.containsKey("history")) {
			showHistory = fieldValues.get("history");
			fieldValues.remove("history");
		}
		List<? extends BusinessObject> objectList = super
				.getSearchResults(fieldValues);
		if (!objectList.isEmpty() && showHistory != null
				&& StringUtils.equals(showHistory, "N")) {
			Collections.sort(objectList, new Comparator<BusinessObject>() {

				@Override
				public int compare(BusinessObject bo1, BusinessObject bo2) {
					int result = 0;
					if(bo1 instanceof AccrualCategory){
						AccrualCategory acc1 = (AccrualCategory) bo1;
						AccrualCategory acc2 = (AccrualCategory) bo2;
						if (acc1.getTimestamp().before(acc2.getTimestamp())) {
							result = 1;
						}
					}
					return result;
				}
			});
			List<BusinessObject> objectListWithoutHistory = new ArrayList<BusinessObject>();
			objectListWithoutHistory.add(objectList.get(0));
			return objectListWithoutHistory;
		}
		return objectList;
	}
}
