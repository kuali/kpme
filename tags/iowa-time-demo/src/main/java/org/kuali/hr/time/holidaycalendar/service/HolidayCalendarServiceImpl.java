package org.kuali.hr.time.holidaycalendar.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.kuali.hr.job.Job;
import org.kuali.hr.time.assignment.Assignment;
import org.kuali.hr.time.holidaycalendar.HolidayCalendar;
import org.kuali.hr.time.holidaycalendar.HolidayCalendarDateEntry;
import org.kuali.hr.time.holidaycalendar.dao.HolidayCalendarDao;
import org.kuali.hr.time.util.TkConstants;

public class HolidayCalendarServiceImpl implements HolidayCalendarService {
	private HolidayCalendarDao holidayCalendarDao;
	
	
	@Override
	public HolidayCalendar getHolidayCalendarByGroup(String holidayCalendarGroup) {
		return holidayCalendarDao.getHolidayCalendarByGroup(holidayCalendarGroup);
	}


	public HolidayCalendarDao getHolidayCalendarDao() {
		return holidayCalendarDao;
	}


	public void setHolidayCalendarDao(HolidayCalendarDao holidayCalendarDao) {
		this.holidayCalendarDao = holidayCalendarDao;
	}


	@Override
	public List<HolidayCalendarDateEntry> getHolidayCalendarDateEntriesForPayPeriod(
			Long holidayCalendarId, Date startDate, Date endDate) {
		return holidayCalendarDao.getHolidayCalendarDateEntriesForPayPeriod(holidayCalendarId, startDate, endDate);
	}


	@Override
	public Assignment getAssignmentToApplyHolidays() {
		return null;
	}


	@Override
	public BigDecimal calculateHolidayHours(Job job, BigDecimal holidayHours) {
		BigDecimal fte = job.getStandardHours().divide(new BigDecimal(40.0),TkConstants.BIG_DECIMAL_SCALE);
		return fte.multiply(holidayHours);
	}

}