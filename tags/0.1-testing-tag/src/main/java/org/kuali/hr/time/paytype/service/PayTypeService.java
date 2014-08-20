package org.kuali.hr.time.paytype.service;

import java.sql.Date;
import java.util.List;

import org.kuali.hr.time.paytype.PayType;

public interface PayTypeService {

	public void saveOrUpdate(PayType payType);
	public void saveOrUpdate(List<PayType> payTypeList);
	
	/**
	 * Provides access to the PayType.   The PayCalendar will be loaded as well.
	 * @param payTypeId
	 * @return A fully populated PayType.
	 */
	public PayType getPayType(String payType, Date effectiveDate);
}