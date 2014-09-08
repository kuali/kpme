package org.kuali.hr.time.calendar.dao;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.apache.ojb.broker.query.QueryFactory;
import org.apache.ojb.broker.query.ReportQueryByCriteria;
import org.joda.time.DateTime;
import org.kuali.hr.time.calendar.CalendarEntries;
import org.kuali.rice.core.framework.persistence.ojb.dao.PlatformAwareDaoBaseOjb;

public class CalendarEntriesDaoSpringOjbImpl extends PlatformAwareDaoBaseOjb implements CalendarEntriesDao {


    public void saveOrUpdate(CalendarEntries calendarEntries) {
        this.getPersistenceBrokerTemplate().store(calendarEntries);
    }

    public CalendarEntries getCalendarEntries(String hrCalendarEntriesId) {
        Criteria currentRecordCriteria = new Criteria();
        currentRecordCriteria.addEqualTo("hrCalendarEntriesId", hrCalendarEntriesId);

        return (CalendarEntries) this.getPersistenceBrokerTemplate().getObjectByQuery(QueryFactory.newQuery(CalendarEntries.class, currentRecordCriteria));
    }

    @Override
    public CalendarEntries getCalendarEntriesByIdAndPeriodEndDate(String hrCalendarId, Date endPeriodDate) {
        Criteria root = new Criteria();
        root.addEqualTo("hrCalendarId", hrCalendarId);
        root.addEqualTo("endPeriodDateTime", endPeriodDate);

        Query query = QueryFactory.newQuery(CalendarEntries.class, root);
        CalendarEntries pce = (CalendarEntries) this.getPersistenceBrokerTemplate().getObjectByQuery(query);
        return pce;
    }

    @Override
    public CalendarEntries getCurrentCalendarEntriesByCalendarId(
            String hrCalendarId, Date currentDate) {
        Criteria root = new Criteria();
//		Criteria beginDate = new Criteria();
//		Criteria endDate = new Criteria();

//		beginDate.addEqualToField("hrPyCalendarId", Criteria.PARENT_QUERY_PREFIX + "hrPyCalendarId");
//		beginDate.addLessOrEqualThan("beginPeriodDateTime", currentDate);
//		ReportQueryByCriteria beginDateSubQuery = QueryFactory.newReportQuery(CalendarEntries.class, beginDate);
//		beginDateSubQuery.setAttributes(new String[] { "max(beginPeriodDateTime)" });

//		endDate.addEqualToField("hrPyCalendarId", Criteria.PARENT_QUERY_PREFIX + "hrPyCalendarId");
//		endDate.addGreaterOrEqualThan("endPeriodDateTime", currentDate);
//		ReportQueryByCriteria endDateSubQuery = QueryFactory.newReportQuery(CalendarEntries.class, endDate);
//		endDateSubQuery.setAttributes(new String[] { "min(endPeriodDateTime)" });

        root.addEqualTo("hrCalendarId", hrCalendarId);
        //root.addEqualTo("beginPeriodDateTime", beginDateSubQuery);
        root.addLessOrEqualThan("beginPeriodDateTime", currentDate);
        root.addGreaterThan("endPeriodDateTime", currentDate);
//		root.addEqualTo("endPeriodDateTime", endDateSubQuery);

        Query query = QueryFactory.newQuery(CalendarEntries.class, root);

        CalendarEntries pce = (CalendarEntries) this.getPersistenceBrokerTemplate().getObjectByQuery(query);
        return pce;
    }

    @Override
    public CalendarEntries getNextCalendarEntriesByCalendarId(String hrCalendarId, CalendarEntries calendarEntries) {
        Criteria root = new Criteria();
        Criteria beginDate = new Criteria();
        Criteria endDate = new Criteria();

        beginDate.addEqualToField("hrCalendarId", Criteria.PARENT_QUERY_PREFIX + "hrCalendarId");
        beginDate.addGreaterThan("beginPeriodDateTime", calendarEntries.getBeginPeriodDateTime());
        ReportQueryByCriteria beginDateSubQuery = QueryFactory.newReportQuery(CalendarEntries.class, beginDate);
        beginDateSubQuery.setAttributes(new String[]{"min(beginPeriodDateTime)"});

        endDate.addEqualToField("hrCalendarId", Criteria.PARENT_QUERY_PREFIX + "hrCalendarId");
        endDate.addGreaterThan("endPeriodDateTime", calendarEntries.getEndPeriodDateTime());
        ReportQueryByCriteria endDateSubQuery = QueryFactory.newReportQuery(CalendarEntries.class, endDate);
        endDateSubQuery.setAttributes(new String[]{"min(endPeriodDateTime)"});

        root.addEqualTo("hrCalendarId", hrCalendarId);
        root.addEqualTo("beginPeriodDateTime", beginDateSubQuery);
        root.addEqualTo("endPeriodDateTime", endDateSubQuery);

        Query query = QueryFactory.newQuery(CalendarEntries.class, root);

        CalendarEntries pce = (CalendarEntries) this.getPersistenceBrokerTemplate().getObjectByQuery(query);
        return pce;
    }

    @Override
    public CalendarEntries getPreviousCalendarEntriesByCalendarId(String hrCalendarId, CalendarEntries calendarEntries) {
        Criteria root = new Criteria();
        Criteria beginDate = new Criteria();
        Criteria endDate = new Criteria();

        beginDate.addEqualToField("hrCalendarId", Criteria.PARENT_QUERY_PREFIX + "hrCalendarId");
        beginDate.addLessThan("beginPeriodDateTime", calendarEntries.getBeginPeriodDateTime());
        ReportQueryByCriteria beginDateSubQuery = QueryFactory.newReportQuery(CalendarEntries.class, beginDate);
        beginDateSubQuery.setAttributes(new String[]{"max(beginPeriodDateTime)"});

        endDate.addEqualToField("hrCalendarId", Criteria.PARENT_QUERY_PREFIX + "hrCalendarId");
        endDate.addLessThan("endPeriodDateTime", calendarEntries.getEndPeriodDateTime());
        ReportQueryByCriteria endDateSubQuery = QueryFactory.newReportQuery(CalendarEntries.class, endDate);
        endDateSubQuery.setAttributes(new String[]{"max(endPeriodDateTime)"});

        root.addEqualTo("hrCalendarId", hrCalendarId);
        root.addEqualTo("beginPeriodDateTime", beginDateSubQuery);
        root.addEqualTo("endPeriodDateTime", endDateSubQuery);

        Query query = QueryFactory.newQuery(CalendarEntries.class, root);

        CalendarEntries pce = (CalendarEntries) this.getPersistenceBrokerTemplate().getObjectByQuery(query);
        return pce;
    }

    public List<CalendarEntries> getCurrentCalendarEntryNeedsScheduled(int thresholdDays, Date asOfDate) {
        DateTime current = new DateTime(asOfDate.getTime());
        DateTime windowStart = current.minusDays(thresholdDays);
        DateTime windowEnd = current.plusDays(thresholdDays);

        Criteria root = new Criteria();

        root.addGreaterOrEqualThan("beginPeriodDateTime", windowStart.toDate());
        root.addLessOrEqualThan("beginPeriodDateTime", windowEnd.toDate());

        Query query = QueryFactory.newQuery(CalendarEntries.class, root);
        Collection c = this.getPersistenceBrokerTemplate().getCollectionByQuery(query);

        List<CalendarEntries> pce = new ArrayList<CalendarEntries>(c.size());
        pce.addAll(c);

        return pce;
    }

    public List<CalendarEntries> getFutureCalendarEntries(String hrCalendarId, Date currentDate, int numberOfEntries) {
        Criteria root = new Criteria();
        root.addEqualTo("hrCalendarId", hrCalendarId);
        root.addGreaterOrEqualThan("beginPeriodDateTime", currentDate);
        QueryByCriteria q = QueryFactory.newReportQuery(CalendarEntries.class, root);
        q.addOrderByAscending("beginPeriodDateTime");
        q.setStartAtIndex(1);
        q.setEndAtIndex(numberOfEntries);
        List<CalendarEntries> calendarEntries = new ArrayList<CalendarEntries>(this.getPersistenceBrokerTemplate().getCollectionByQuery(q));
        return calendarEntries;
    }

    public CalendarEntries getCalendarEntriesByBeginAndEndDate(Date beginPeriodDate, Date endPeriodDate) {
        Criteria root = new Criteria();
        root.addEqualTo("beginPeriodDateTime", beginPeriodDate);
        root.addEqualTo("endPeriodDateTime", endPeriodDate);
        Query query = QueryFactory.newQuery(CalendarEntries.class, root);

        return (CalendarEntries) this.getPersistenceBrokerTemplate().getObjectByQuery(query);
    }
    
    public List<CalendarEntries> getAllCalendarEntriesForCalendarId(String hrCalendarId) {
    	Criteria root = new Criteria();
        root.addEqualTo("hrCalendarId", hrCalendarId);
        Query query = QueryFactory.newQuery(CalendarEntries.class, root);
        List<CalendarEntries> ceList = new ArrayList<CalendarEntries> (this.getPersistenceBrokerTemplate().getCollectionByQuery(query));
        return ceList;
    }
    
    public List<CalendarEntries> getAllCalendarEntriesForCalendarIdAndYear(String hrCalendarId, String year) {        
        Criteria crit = new Criteria();
        List<CalendarEntries> ceList = new ArrayList<CalendarEntries>();
        try {
	    	 crit.addEqualTo("hrCalendarId", hrCalendarId);
	    	 DateFormat df = new SimpleDateFormat("yyyy");
	    	 Date cYear = df.parse(year);
	    	 String nextYear = Integer.toString((Integer.parseInt(year) + 1));
	    	 Date nYear = df.parse(nextYear);
	    	 
	    	 crit.addGreaterOrEqualThan("beginPeriodDateTime", cYear);
	    	 crit.addLessThan("beginPeriodDateTime", nYear );
	    	 QueryByCriteria query = new QueryByCriteria(CalendarEntries.class, crit);
	    	 Collection c = this.getPersistenceBrokerTemplate().getCollectionByQuery(query);
	    	 if (c != null) {
		    	ceList.addAll(c);
	    	 }
		  } catch (ParseException e) {
				e.printStackTrace();
		  }
		  return ceList;
    }


}