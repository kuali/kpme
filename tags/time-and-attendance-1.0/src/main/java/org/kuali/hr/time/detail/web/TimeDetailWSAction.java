package org.kuali.hr.time.detail.web;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import org.kuali.hr.job.Job;
import org.kuali.hr.time.assignment.Assignment;
import org.kuali.hr.time.assignment.AssignmentDescriptionKey;
import org.kuali.hr.time.earncode.EarnCode;
import org.kuali.hr.time.paycalendar.PayCalendarEntries;
import org.kuali.hr.time.service.base.TkServiceLocator;
import org.kuali.hr.time.timeblock.TimeBlock;
import org.kuali.hr.time.timesheet.web.TimesheetAction;
import org.kuali.hr.time.util.TKContext;
import org.kuali.hr.time.util.TKUtils;
import org.kuali.hr.time.util.TkConstants;
import org.kuali.rice.kns.web.struts.form.KualiMaintenanceForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class TimeDetailWSAction extends TimesheetAction {

    private static final Logger LOG = Logger.getLogger(TimeDetailWSAction.class);

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return super.execute(mapping, form, request, response);
    }

    /**
     * This is an ajax call triggered after a user submits the time entry form.
     * If there is any error, it will return error messages as a json object.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return jsonObj
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public ActionForward validateTimeEntry(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        TimeDetailActionFormBase tdaf = (TimeDetailActionFormBase) form;

        JSONArray errorMsgList = new JSONArray();
        PayCalendarEntries payCalEntry = tdaf.getTimesheetDocument().getPayCalendarEntry();
        java.sql.Date asOfDate = payCalEntry.getEndPeriodDate();

        if (tdaf.getStartDate() == null) {
            errorMsgList.add("The start date is blank.");
            tdaf.setOutputString(JSONValue.toJSONString(errorMsgList));
            return mapping.findForward("ws");
        }

        if (tdaf.getEndDate() == null) {
            errorMsgList.add("The end date is blank.");
            tdaf.setOutputString(JSONValue.toJSONString(errorMsgList));
            return mapping.findForward("ws");
        }

        // These methods use the UserTimeZone.
        Long startTime = TKUtils.convertDateStringToTimestamp(tdaf.getStartDate(), tdaf.getStartTime()).getTime();
        Long endTime = TKUtils.convertDateStringToTimestamp(tdaf.getEndDate(), tdaf.getEndTime()).getTime();


        LocalDateTime pcb_ldt = payCalEntry.getBeginLocalDateTime();
        LocalDateTime pce_ldt = payCalEntry.getEndLocalDateTime();
        DateTimeZone utz = TkServiceLocator.getTimezoneService().getUserTimezoneWithFallback();
        DateTime p_cal_b_dt = pcb_ldt.toDateTime(utz);
        DateTime p_cal_e_dt = pce_ldt.toDateTime(utz);


        Interval payInterval = new Interval(p_cal_b_dt, p_cal_e_dt);
        if (!payInterval.contains(startTime)) {
            errorMsgList.add("The start date/time is outside the pay period");
            tdaf.setOutputString(JSONValue.toJSONString(errorMsgList));
            return mapping.findForward("ws");
        }
        if (!payInterval.contains(endTime) && p_cal_e_dt.getMillis() != endTime) {
            errorMsgList.add("The end date/time is outside the pay period");
            tdaf.setOutputString(JSONValue.toJSONString(errorMsgList));
            return mapping.findForward("ws");
        }

        if (StringUtils.isNotBlank(tdaf.getSelectedEarnCode())) {
            EarnCode earnCode = TkServiceLocator.getEarnCodeService().getEarnCode(tdaf.getSelectedEarnCode(), asOfDate);
            if (earnCode != null && earnCode.getRecordTime()) {
                if (tdaf.getStartTime() == null) {
                    errorMsgList.add("The start time is blank.");
                    tdaf.setOutputString(JSONValue.toJSONString(errorMsgList));
                    return mapping.findForward("ws");
                }

                if (tdaf.getEndTime() == null) {
                    errorMsgList.add("The end time is blank.");
                    tdaf.setOutputString(JSONValue.toJSONString(errorMsgList));
                    return mapping.findForward("ws");
                }


                if (startTime - endTime == 0) {
                    errorMsgList.add("Start time and end time cannot be equivalent");
                    tdaf.setOutputString(JSONValue.toJSONString(errorMsgList));
                    return mapping.findForward("ws");
                }
            }
        }

        DateTime startTemp = new DateTime(startTime);
        DateTime endTemp = new DateTime(endTime);


        if (StringUtils.equals(tdaf.getAcrossDays(), "n")) {
            Hours hrs = Hours.hoursBetween(startTemp, endTemp);
            if (hrs.getHours() >= 24) {
                errorMsgList.add("One timeblock cannot exceed 24 hours");
                tdaf.setOutputString(JSONValue.toJSONString(errorMsgList));
                return mapping.findForward("ws");
            }
        }

        //Check that assignment is valid for both days
        AssignmentDescriptionKey assignKey = TkServiceLocator.getAssignmentService().getAssignmentDescriptionKey(tdaf.getSelectedAssignment());
        Assignment assign = TkServiceLocator.getAssignmentService().getAssignment(assignKey, new Date(startTime));
        if (assign == null) {
            errorMsgList.add("Assignment is not valid for " + TKUtils.formatDate(new Date(startTime)));
            tdaf.setOutputString(JSONValue.toJSONString(errorMsgList));
            return mapping.findForward("ws");
        }
        assign = TkServiceLocator.getAssignmentService().getAssignment(assignKey, new Date(endTime));
        if (assign == null) {
            errorMsgList.add("Assignment is not valid for " + TKUtils.formatDate(new Date(endTime)));
            tdaf.setOutputString(JSONValue.toJSONString(errorMsgList));
            return mapping.findForward("ws");
        }

        //------------------------
        // some of the simple validations are in the js side in order to reduce the server calls
        // 1. check if the begin / end time is empty - tk.calenadr.js
        // 2. check the time format - timeparse.js
        // 3. only allows decimals to be entered in the hour field
        //------------------------

        //------------------------
        // check if the begin / end time are valid
        //------------------------
        if ((startTime.compareTo(endTime) > 0 || endTime.compareTo(startTime) < 0)) {
            errorMsgList.add("The time or date is not valid.");
            tdaf.setOutputString(JSONValue.toJSONString(errorMsgList));
            return mapping.findForward("ws");
        }

        //------------------------
        // check if the overnight shift is across days
        //------------------------
        if (StringUtils.equals(tdaf.getAcrossDays(), "y") && tdaf.getHours() == null && tdaf.getAmount() == null) {
            if (startTemp.getHourOfDay() >= endTemp.getHourOfDay()
                    && !(endTemp.getDayOfYear() - startTemp.getDayOfYear() <= 1
                    || endTemp.getHourOfDay() == 0)) {
                errorMsgList.add("The \"apply to each day\" box should not be checked.");
                tdaf.setOutputString(JSONValue.toJSONString(errorMsgList));
                return mapping.findForward("ws");
            }

        }
        //------------------------
        // Amount cannot be zero
        //------------------------
        if (tdaf.getAmount() != null) {
            if (tdaf.getAmount().equals(BigDecimal.ZERO)) {
                errorMsgList.add("Amount cannot be zero.");
                tdaf.setOutputString(JSONValue.toJSONString(errorMsgList));
                return mapping.findForward("ws");
            }
            if (tdaf.getAmount().scale() > 2) {
                errorMsgList.add("Amount cannot have more than two digits after decimal point.");
                tdaf.setOutputString(JSONValue.toJSONString(errorMsgList));
                return mapping.findForward("ws");
            }
        }

        //------------------------
        // check if the hours entered for hourly earn code is greater than 24 hours per day
        // Hours cannot be zero
        //------------------------
        if (tdaf.getHours() != null) {
            if (tdaf.getHours().equals(BigDecimal.ZERO)) {
                errorMsgList.add("Hours cannot be zero.");
                tdaf.setOutputString(JSONValue.toJSONString(errorMsgList));
                return mapping.findForward("ws");
            }
            if (tdaf.getHours().scale() > 2) {
                errorMsgList.add("Hours cannot have more than two digits after decimal point.");
                tdaf.setOutputString(JSONValue.toJSONString(errorMsgList));
                return mapping.findForward("ws");
            }
            int dayDiff = endTemp.getDayOfYear() - startTemp.getDayOfYear() + 1;
            if (tdaf.getHours().compareTo(new BigDecimal(dayDiff * 24)) == 1) {
                errorMsgList.add("Cannot enter more than 24 hours per day.");
                tdaf.setOutputString(JSONValue.toJSONString(errorMsgList));
                return mapping.findForward("ws");
            }
        }


        //------------------------
        // check if time blocks overlap with each other. Note that the tkTimeBlockId is used to
        // determine is it's updating an existing time block or adding a new one
        //------------------------
        Interval addedTimeblockInterval = new Interval(startTime, endTime);
        List<Interval> dayInt = new ArrayList<Interval>();

        if (StringUtils.equals(tdaf.getAcrossDays(), "y")) {
            DateTime start = new DateTime(startTime);
            DateTime end = new DateTime(TKUtils.convertDateStringToTimestamp(tdaf.getStartDate(), tdaf.getEndTime()).getTime());
            if (endTemp.getDayOfYear() - startTemp.getDayOfYear() < 1) {
                end = new DateTime(endTime);
            }
            DateTime groupEnd = new DateTime(endTime);
            Long startLong = start.getMillis();
            Long endLong = end.getMillis();
            //create interval span if start is before the end and the end is after the start except
            //for when the end is midnight ..that converts to midnight of next day
            DateMidnight midNight = new DateMidnight(endLong);
            while (start.isBefore(groupEnd.getMillis()) && ((endLong >= startLong) || end.isEqual(midNight))) {
                Interval tempInt = null;
                if (end.isEqual(midNight)) {
                    tempInt = addedTimeblockInterval;
                } else {
                    tempInt = new Interval(startLong, endLong);
                }
                dayInt.add(tempInt);
                start = start.plusDays(1);
                end = end.plusDays(1);
                startLong = start.getMillis();
                endLong = end.getMillis();
            }
        } else {
            dayInt.add(addedTimeblockInterval);
        }

        for (TimeBlock timeBlock : tdaf.getTimesheetDocument().getTimeBlocks()) {
            if (StringUtils.equals(timeBlock.getEarnCodeType(), "TIME")) {
                Interval timeBlockInterval = new Interval(timeBlock.getBeginTimestamp().getTime(), timeBlock.getEndTimestamp().getTime());
                for (Interval intv : dayInt) {
                    if (timeBlockInterval.overlaps(intv) && (tdaf.getTkTimeBlockId() == null || tdaf.getTkTimeBlockId().compareTo(timeBlock.getTkTimeBlockId()) != 0)) {
                        errorMsgList.add("The time block you are trying to add overlaps with an existing time block.");
                        tdaf.setOutputString(JSONValue.toJSONString(errorMsgList));
                        return mapping.findForward("ws");
                    }
                }
            }
        }

        ActionFormUtils.validateHourLimit(tdaf);

        tdaf.setOutputString(JSONValue.toJSONString(errorMsgList));

        return mapping.findForward("ws");
    }

    //this is an ajax call for the assignment maintenance page
    public ActionForward getDepartmentForJobNumber(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        KualiMaintenanceForm kualiForm = (KualiMaintenanceForm) form;

        String principalId = (String) request.getAttribute("principalId");
        Long jobNumber = (Long) request.getAttribute("jobNumber");

        Job job = TkServiceLocator.getJobSerivce().getJob(principalId, jobNumber, TKUtils.getCurrentDate());
        kualiForm.setAnnotation(job.getDept());

        return mapping.findForward("ws");
    }

    // this is an ajax call
    public ActionForward getEarnCodes(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TimeDetailWSActionForm tdaf = (TimeDetailWSActionForm) form;
        StringBuilder earnCodeString = new StringBuilder();
        if (StringUtils.isBlank(tdaf.getSelectedAssignment())) {
            earnCodeString.append("<option value=''>-- select an assignment first --</option>");
        } else {
            List<Assignment> assignments = tdaf.getTimesheetDocument().getAssignments();
            AssignmentDescriptionKey key = new AssignmentDescriptionKey(tdaf.getSelectedAssignment());
            for (Assignment assignment : assignments) {
                if (assignment.getJobNumber().compareTo(key.getJobNumber()) == 0 &&
                        assignment.getWorkArea().compareTo(key.getWorkArea()) == 0 &&
                        assignment.getTask().compareTo(key.getTask()) == 0) {
                    List<EarnCode> earnCodes = TkServiceLocator.getEarnCodeService().getEarnCodes(assignment, tdaf.getTimesheetDocument().getAsOfDate());
                    for (EarnCode earnCode : earnCodes) {
                        if (earnCode.getEarnCode().equals(TkConstants.HOLIDAY_EARN_CODE)
                                && !(TKContext.getUser().getCurrentRoles().isSystemAdmin() || TKContext.getUser().getCurrentRoles().isTimesheetApprover())) {
                            continue;
                        }
                        if (!(assignment.getTimeCollectionRule().isClockUserFl() &&
                                StringUtils.equals(assignment.getJob().getPayTypeObj().getRegEarnCode(), earnCode.getEarnCode()) && StringUtils.equals(TKContext.getPrincipalId(), assignment.getPrincipalId()))) {
                            earnCodeString.append("<option value='").append(earnCode.getEarnCode()).append("_").append(earnCode.getEarnCodeType()).append("'>");
                            earnCodeString.append(earnCode.getEarnCode()).append(" : ").append(earnCode.getDescription());
                            earnCodeString.append("</option>");
                        }
                    }
                }
            }
        }
        LOG.info(tdaf.toString());
        tdaf.setOutputString(earnCodeString.toString());
        return mapping.findForward("ws");
    }

    public ActionForward getOvertimeEarnCodes(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TimeDetailWSActionForm tdaf = (TimeDetailWSActionForm) form;
        StringBuilder earnCodeString = new StringBuilder();
        List<EarnCode> overtimeEarnCodes = TkServiceLocator.getEarnGroupService().getEarnCodeMapForOvertimeEarnGroup();

        for (EarnCode earnCode : overtimeEarnCodes) {
            earnCodeString.append("<option value='").append(earnCode.getEarnCode()).append("_").append(TkConstants.EARN_CODE_OVT).append("'>");
            earnCodeString.append(earnCode.getEarnCode()).append(" : ").append(earnCode.getDescription());
            earnCodeString.append("</option>");
        }

        LOG.info(tdaf.toString());
        tdaf.setOutputString(earnCodeString.toString());
        return mapping.findForward("ws");
    }

}