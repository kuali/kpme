package org.kuali.hr.time.principal.calendar.service;

import java.sql.Date;

import org.kuali.hr.time.principal.calendar.PrincipalCalendar;

public interface PrincipalCalendarService {
	public PrincipalCalendar getPrincipalCalendar(String principalId, Date asOfDate);
}