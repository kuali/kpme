package org.kuali.hr.time.detail.web;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import org.kuali.hr.time.timeblock.TimeBlock;
import org.kuali.hr.time.timesheet.web.TimesheetActionForm;
import org.kuali.hr.time.timesummary.TimeSummary;
import org.kuali.hr.time.util.TKContext;
import org.kuali.hr.time.util.TKUtils;
import org.kuali.hr.time.util.TkConstants;

public class TimeDetailActionForm extends TimesheetActionForm {

    /**
     *
     */
    private static final long serialVersionUID = 5277197287612035236L;

	private java.util.Date beginPeriodDateTime;
	private java.util.Date endPeriodDateTime;
	
	// this is for the ajax call
	private String outputString;
	
	private Long tkTimeBlockId;
	private String startTime;
	private String endTime;
	private String acrossDays;
	private TimeBlock timeBlock;
	private String clockAction;
	private BigDecimal hours;
	private BigDecimal amount;
	private String startDate;
	private String endDate;
	private String serverTimezone;
	private String userTimezone;
	
	private TimeSummary timeSummary;
	
	public String getOutputString() {
		return outputString;
	}

	public void setOutputString(String outputString) {
		this.outputString = outputString;
	}

	public Long getTkTimeBlockId() {
		return tkTimeBlockId;
	}

	public void setTkTimeBlockId(Long tkTimeBlockId) {
		this.tkTimeBlockId = tkTimeBlockId;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getAcrossDays() {
		return acrossDays;
	}

	public void setAcrossDays(String acrossDays) {
		this.acrossDays = acrossDays;
	}

	public List<TimeBlock> getTimeBlockList() {
		return this.getTimesheetDocument().getTimeBlocks();
	}

	public TimeBlock getTimeBlock() {
		return timeBlock;
	}

	public void setTimeBlock(TimeBlock timeBlock) {
		this.timeBlock = timeBlock;
	}
	
	public String getClockAction() {
		return clockAction;
	}

	public void setClockAction(String clockAction) {
		this.clockAction = clockAction;
	}

	public BigDecimal getHours() {
		return hours;
	}

	public void setHours(BigDecimal hours) {
		this.hours = hours;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public java.util.Date getBeginPeriodDateTime() {
		return beginPeriodDateTime;
	}

	public void setBeginPeriodDateTime(java.util.Date beginPeriodDateTime) {
		this.beginPeriodDateTime = beginPeriodDateTime;
	}

	public java.util.Date getEndPeriodDateTime() {
		return endPeriodDateTime;
	}

	public void setEndPeriodDateTime(java.util.Date endPeriodDateTime) {
		this.endPeriodDateTime = endPeriodDateTime;
	}

	public String getIsVirtualWorkDay() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getBeginPeriodDateTime());
		return Boolean.toString(TKUtils.isVirtualWorkDay(cal));
	}

	public TimeSummary getTimeSummary() {
		return timeSummary;
	}

	public void setTimeSummary(TimeSummary timeSummary) {
		this.timeSummary = timeSummary;
	}

	public String getServerTimezone() {
		return TkConstants.SYSTEM_TIME_ZONE;
	}

	public String getUserTimezone() {
		return TKContext.getUser().getUserPreference().getTimezone();
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
}