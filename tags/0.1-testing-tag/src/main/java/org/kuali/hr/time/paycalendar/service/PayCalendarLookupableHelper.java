package org.kuali.hr.time.paycalendar.service;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.kuali.hr.time.paycalendar.PayCalendar;
import org.kuali.rice.kns.bo.BusinessObject;
import org.kuali.rice.kns.lookup.KualiLookupableHelperServiceImpl;

public class PayCalendarLookupableHelper extends
		KualiLookupableHelperServiceImpl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	public List<? extends BusinessObject> getSearchResults(
			Map<String, String> fieldValues) {
		String flsaTime = null;
		if(fieldValues.containsKey("flsaBeginTime")){
			flsaTime = fieldValues.get("flsaBeginTime");
			fieldValues.remove("flsaBeginTime");
		}
		List<? extends BusinessObject> objectList = super.getSearchResults(fieldValues);
		if(!objectList.isEmpty()  && flsaTime != null && StringUtils.isNotBlank(flsaTime)){
			SimpleDateFormat sdFormat = new SimpleDateFormat("hh:mm aa");
			Time flsaBeginTime = null;
			try {
				flsaBeginTime = new Time(sdFormat.parse(flsaTime).getTime());
				Iterator itr = objectList.iterator();
				while(itr.hasNext()){
					PayCalendar pc = (PayCalendar)itr.next();
					if(pc.getFlsaBeginTime()!= null && !pc.getFlsaBeginTime().equals(flsaBeginTime)){
						itr.remove();
					}
				}
			} catch (ParseException e) {
			}	
		}
		return objectList;
	}

}