package org.kuali.hr.time.holidaycalendar.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import org.kuali.hr.job.Job;
import org.kuali.hr.time.assignment.Assignment;
import org.kuali.hr.time.exceptions.TkException;
import org.kuali.hr.time.holidaycalendar.HolidayCalendar;
import org.kuali.hr.time.holidaycalendar.HolidayCalendarDateEntry;

/**
 * 
 * @author crivera
 * 
 */
@WebService(name = "holidayCalendarServiceSOAP", targetNamespace = "/tk")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface HolidayCalendarService {

	/**
	 * 
	 * @param holidayCalendarGroup
	 * @return
	 * @throws TkException
	 */
	@WebMethod(exclude = true)
	public HolidayCalendar getHolidayCalendarByGroup(String holidayCalendarGroup)
			throws TkException;

	/**
	 * 
	 * @param principalId
	 * @throws TkException
	 */
	@WebMethod
	public List<HolidayCalendarDateEntry> getHolidaysByPrincipalId(
			@WebParam(name = "principalId") String principalId)
			throws TkException;

	public List<HolidayCalendarDateEntry> getHolidayCalendarDateEntriesForPayPeriod(Long holidayCalendarId, Date startDate, Date endDate);
	public Assignment getAssignmentToApplyHolidays();
	public BigDecimal calculateHolidayHours(Job job, BigDecimal holidayHours);
}
