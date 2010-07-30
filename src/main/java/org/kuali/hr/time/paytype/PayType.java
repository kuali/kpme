package org.kuali.hr.time.paytype;

import java.sql.Date;
import java.sql.Time;
import java.util.LinkedHashMap;

import org.kuali.rice.kns.bo.PersistableBusinessObjectBase;

public class PayType extends PersistableBusinessObjectBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long payTypeId;
	private String payType;
	private String descr;
	private String calendarGroup;
	private String regEarnCode;
	private Date effectiveDate;
	private Time timestamp;
	private String holidayCalendar;

	@SuppressWarnings("unchecked")
	@Override
	protected LinkedHashMap toStringMapper() {
		return null;
	}

	public Long getPayTypeId() {
		return payTypeId;
	}

	public void setPayTypeId(Long payTypeId) {
		this.payTypeId = payTypeId;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public String getCalendarGroup() {
		return calendarGroup;
	}

	public void setCalendarGroup(String calendarGroup) {
		this.calendarGroup = calendarGroup;
	}

	public String getRegEarnCode() {
		return regEarnCode;
	}

	public void setRegEarnCode(String regEarnCode) {
		this.regEarnCode = regEarnCode;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public String getHolidayCalendar() {
		return holidayCalendar;
	}

	public void setHolidayCalendar(String holidayCalendar) {
		this.holidayCalendar = holidayCalendar;
	}

	public void setTimestamp(Time timestamp) {
		this.timestamp = timestamp;
	}

	public Time getTimestamp() {
		return timestamp;
	}

	
}
