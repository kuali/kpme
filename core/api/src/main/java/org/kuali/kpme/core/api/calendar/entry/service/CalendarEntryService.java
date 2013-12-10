/**
 * Copyright 2004-2013 The Kuali Foundation
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
package org.kuali.kpme.core.api.calendar.entry.service;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.kuali.kpme.core.api.calendar.entry.CalendarEntryContract;
import org.kuali.kpme.core.api.calendar.entry.CalendarEntryPeriodType;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

public interface CalendarEntryService {

    /**
     * Method to directly access the CalendarEntry object by ID.
     *
     * @param hrCalendarEntryId The ID to retrieve.
     * @return a CalendarEntry object.
     */
    @Cacheable(value= CalendarEntryContract.CACHE_NAME, key="'hrCalendarEntryId=' + #p0")
    public CalendarEntryContract getCalendarEntry(String hrCalendarEntryId);

    /**
     * Method to obtain the current CalendarEntry object based on the
     * indicated calendar and asOfDate.
     * @param hrCalendarId The calendar to reference.
     * @param asOfDate The date reference point.
     * @return the current CalendarEntry effective by the asOfDate.
     */
    @Cacheable(value= CalendarEntryContract.CACHE_NAME, key="'hrCalendarId=' + #p0 + '|' + 'asOfDate=' + #p1")
    public CalendarEntryContract getCurrentCalendarEntryByCalendarId(String hrCalendarId, DateTime asOfDate);

    /**
     * Method to obtain the CalendarEntry object based in a date range
     * @param hrCalendarId The calendar to reference.
     * @param beginDate
     * @param endDate
     * @return the current CalendarEntry effective by the asOfDate.
     */
    @Cacheable(value= CalendarEntryContract.CACHE_NAME, key="'hrCalendarId=' + #p0 + '|' + 'beginDate=' + #p1 + '|' + 'endDate=' + #p2")
    public CalendarEntryContract getCalendarEntryByCalendarIdAndDateRange(String hrCalendarId, DateTime beginDate, DateTime endDate);

    @Cacheable(value= CalendarEntryContract.CACHE_NAME, key="'hrCalendarId=' + #p0 + '|' + 'endPeriodDate=' + #p1")
    public CalendarEntryContract getCalendarEntryByIdAndPeriodEndDate(String hrCalendarId, DateTime endPeriodDate);

    @Cacheable(value= CalendarEntryContract.CACHE_NAME, key="'{previousEntry}' + 'hrCalendarId=' + #p0 + '|' + 'pce=' + #p1.getHrCalendarEntryId()")
    public CalendarEntryContract getPreviousCalendarEntryByCalendarId(String hrCalendarId, CalendarEntryContract pce);
    @Cacheable(value= CalendarEntryContract.CACHE_NAME, key="'{nextEntry}' + 'hrCalendarId=' + #p0 + '|' + 'pce=' + #p1.getHrCalendarEntryId()")
    public CalendarEntryContract getNextCalendarEntryByCalendarId(String hrCalendarId, CalendarEntryContract pce);

    /**
     * Provides a list of CalendarEntry that are in the indicated window
     * of time from the as of date.
     * @param thresholdDays ± days from the asOfDate to form the window of time.
     * @param asOfDate The central date to query from.
     * @return A list of CalendarEntry.
     */
    @Cacheable(value= CalendarEntryContract.CACHE_NAME, key="'thresholdDays=' + #p0 + '|' + 'endPeriodDate=' + #p1")
    public List<? extends CalendarEntryContract> getCurrentCalendarEntriesNeedsScheduled(int thresholdDays, DateTime asOfDate);

    @CacheEvict(value={CalendarEntryContract.CACHE_NAME}, allEntries = true)
    public CalendarEntryContract createNextCalendarEntry(CalendarEntryContract calendarEntry, CalendarEntryPeriodType type);

    public List<? extends CalendarEntryContract> getFutureCalendarEntries(String hrCalendarId, DateTime currentDate, int numberOfEntries);

    public List<? extends CalendarEntryContract> getCalendarEntriesEndingBetweenBeginAndEndDate(String hrCalendarId, DateTime beginDate, DateTime endDate);

    @Cacheable(value= CalendarEntryContract.CACHE_NAME, key="'hrCalendarId=' + #p0")
    public List<? extends CalendarEntryContract> getAllCalendarEntriesForCalendarId(String hrCalendarId);

    @Cacheable(value= CalendarEntryContract.CACHE_NAME, key="'hrCalendarId=' + #p0 + '|' + 'year=' + #p1")
    public List<? extends CalendarEntryContract> getAllCalendarEntriesForCalendarIdAndYear(String hrCalendarId, String year);

    public List<? extends CalendarEntryContract> getAllCalendarEntriesForCalendarIdUpToPlanningMonths(String hrCalendarId, String principalId);

    public List<? extends CalendarEntryContract> getAllCalendarEntriesForCalendarIdUpToCutOffTime(String hrCalendarId, DateTime cutOffTime);
    //kpme-2396
    /**
     * Use this method to get CalendarEntry if you are passing in a "current date"
     * style of date, ie todays date. If you are in a logic situation where you would
     * pass EITHER todays date or a pay calendar date, pass the Pay period BEGIN date,
     * so that the retrieval logic will correctly place the date in the window.
	 *
	 * @param principalId
	 * @param currentDate
	 * @return
	 */
	public CalendarEntryContract getCurrentCalendarDates(String principalId, DateTime currentDate);
	
	 /**
     * Use this method to get CalendarEntry if you are passing in a date range
     *  ie being/end of a year.
     *
     * @param principalId
     * @param beginDate
     * @param endDate
     * @return
     */
    public CalendarEntryContract getCurrentCalendarDates(String principalId, DateTime beginDate, DateTime endDate);
    
    /**
     * A method to use specifically when you have a Timesheet Documents Pay Period
     * end date. Do not use if you are passing in a date known not to be the END period date.
     *
     * @param principalId
     * @param payEndDate
     * @return
     */
   
    @Cacheable(value= CalendarEntryContract.CACHE_NAME, key="'principalId=' + #p0 + '|' + 'payEndDate=' + #p1 + '|' + 'calendarType=' + #p2")
    public CalendarEntryContract getCalendarDatesByPayEndDate(String principalId, DateTime payEndDate, String calendarType);
    
    /**
    *
    * @param principalId
    * @param beginDate
    * @param endDate
    * @return
    */
   @Cacheable(value= CalendarEntryContract.CACHE_NAME, key="'principalId=' + #p0 + '|' + 'beginDate=' + #p1 + '|' + 'endDate=' + #p2")
   public CalendarEntryContract getCurrentCalendarDatesForLeaveCalendar(String principalId, DateTime beginDate, DateTime endDate);
   
   /**
	 * 
	 * @param principalId
	 * @param currentDate
	 * @return
	 */
   @Cacheable(value= CalendarEntryContract.CACHE_NAME, key="'principalId=' + #p0 + '|' + 'currentDate=' + #p1")
	public CalendarEntryContract getCurrentCalendarDatesForLeaveCalendar(String principalId, DateTime currentDate);

    public List<? extends CalendarEntryContract> getSearchResults(String calendarName, String calendarTypes, LocalDate fromBeginDate, LocalDate toBeginDate, LocalDate fromendDate, LocalDate toEndDate);

}