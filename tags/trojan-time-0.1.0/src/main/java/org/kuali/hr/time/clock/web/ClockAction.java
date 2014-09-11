package org.kuali.hr.time.clock.web;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.kuali.hr.time.assignment.Assignment;
import org.kuali.hr.time.assignment.AssignmentDescriptionKey;
import org.kuali.hr.time.clocklog.ClockLog;
import org.kuali.hr.time.service.base.TkServiceLocator;
import org.kuali.hr.time.timeblock.TimeBlock;
import org.kuali.hr.time.timesheet.web.TimesheetAction;
import org.kuali.hr.time.util.TKContext;
import org.kuali.hr.time.util.TKUtils;
import org.kuali.hr.time.util.TkConstants;
import org.kuali.rice.kns.util.GlobalVariables;

public class ClockAction extends TimesheetAction {

    	private static final Logger LOG = Logger.getLogger(ClockAction.class);
    	public static final SimpleDateFormat SDF =  new SimpleDateFormat("EEE, MMMM d yyyy HH:mm:ss, zzzz");

    	@Override
    	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    		ActionForward forward = super.execute(mapping, form, request, response);
    	    ClockActionForm caf = (ClockActionForm) form;
    	    String principalId = TKContext.getUser().getPrincipalId();
    	    
    	    ClockLog lastClockLog = TkServiceLocator.getClockLogService().getLastClockLog(principalId);
    	    if (lastClockLog != null) {
    	    	Timestamp lastClockTimestamp = TkServiceLocator.getClockLogService().getLastClockLog(TKContext.getUser().getPrincipalId()).getClockTimestamp();
    	    	caf.setLastClockTimestamp(lastClockTimestamp);
    	    	caf.setLastClockAction(lastClockLog.getClockAction());
    	    }

    	    if(lastClockLog == null || StringUtils.equals(lastClockLog.getClockAction(), TkConstants.CLOCK_OUT)) {
    	    	caf.setCurrentClockAction(TkConstants.CLOCK_IN);
    	    }
	   	    else {
	   	    	
	   	    	Boolean isShowLunchButton = TkServiceLocator.getSystemLunchRuleService().getSystemLunchRule(new java.sql.Date(System.currentTimeMillis())).getShowLunchButton();
	   	    	
	   	    	if(StringUtils.equals(lastClockLog.getClockAction(),TkConstants.LUNCH_IN) && isShowLunchButton) {
	   	    		caf.setCurrentClockAction(TkConstants.LUNCH_OUT);
	   	    	}
//	   	    	else if(StringUtils.equals(lastClockLog.getClockAction(),TkConstants.LUNCH_OUT)) {
//	   	    		caf.setCurrentClockAction(TkConstants.LUNCH_IN);
//	   	    	}
	   	    	else {
	   	    		caf.setCurrentClockAction(TkConstants.CLOCK_OUT);
	   	    	}
    	    	// if the current clock action is clock out, displays only the clocked-in assignment
    	    	String selectedAssignment = new AssignmentDescriptionKey(lastClockLog.getJobNumber(), lastClockLog.getWorkArea(), lastClockLog.getTask()).toAssignmentKeyString();
    	    	Assignment assignment = TkServiceLocator.getAssignmentService().getAssignment(caf.getTimesheetDocument(), selectedAssignment);
    	    	Map<String,String> assignmentDesc = TkServiceLocator.getAssignmentService().getAssignmentDescriptions(assignment);
    	    	caf.setAssignmentDescriptions(assignmentDesc);

	   	    }
    	    return forward; 
    	}

    	public ActionForward clockAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	    ClockActionForm caf = (ClockActionForm)form;

    	    // TODO: Validate that clock action is valid for this user
    	    // TODO: this needs to be integrated with the error tag
    	    if(StringUtils.isBlank(caf.getSelectedAssignment())) {
    	    	throw new RuntimeException("no assignment selected");
    	    }
    	    
    	    // process rules
    	    Timestamp clockTimestamp = TkServiceLocator.getGracePeriodService().processGracePeriodRule(new Timestamp(System.currentTimeMillis()), new java.sql.Date(caf.getPayCalendarDates().getBeginPeriodDateTime().getTime()));
    	    ClockLog clockLog = TkServiceLocator.getClockLogService().buildClockLog(clockTimestamp, caf.getSelectedAssignment(),caf.getTimesheetDocument(),caf.getCurrentClockAction(), request.getRemoteAddr());
    	    TkServiceLocator.getClockLocationRuleService().processClockLocationRule(clockLog, TKUtils.getCurrentDate());
    	    
    	    TkServiceLocator.getClockLogService().saveClockLog(clockLog);
    	    caf.setClockLog(clockLog);
    	    
    	    if(StringUtils.equals(caf.getCurrentClockAction(), TkConstants.CLOCK_OUT)) {
    	    	
	    	    Timestamp lastClockTimestamp = TkServiceLocator.getClockLogService().getLastClockLog(TKContext.getUser().getPrincipalId(), TkConstants.CLOCK_IN).getClockTimestamp();
    			long beginTime = lastClockTimestamp.getTime();
    			Timestamp beginTimestamp = new Timestamp(beginTime);
    			Timestamp endTimestamp = caf.getClockLog().getClockTimestamp();
    			
    			Assignment assignment = TkServiceLocator.getAssignmentService().getAssignment(caf.getTimesheetDocument(), 
						caf.getSelectedAssignment());
    			
    			String earnCode = TKContext.getUser().isSynchronousAspect() ? assignment.getJob().getPayTypeObj().getRegEarnCode() : caf.getSelectedEarnCode();
    			
    			//create the list of timeblocks based on the range passed in
    			List<TimeBlock> lstNewTimeBlocks = TkServiceLocator.getTimeBlockService().buildTimeBlocks(assignment, 
    					earnCode, caf.getTimesheetDocument(),beginTimestamp, endTimestamp,BigDecimal.ZERO);
    			//concat delta of timeblocks (new and original)
    			lstNewTimeBlocks.addAll(caf.getTimesheetDocument().getTimeBlocks());
    			//TODO do any server side validation of adding checking for overlapping timeblocks etc
    			//return if any issues

    			//reset time hour details
    			lstNewTimeBlocks = TkServiceLocator.getTimeBlockService().resetTimeHourDetail(lstNewTimeBlocks);
    			//apply any rules for this action
    			lstNewTimeBlocks = TkServiceLocator.getTkRuleControllerService().applyRules(TkConstants.ACTIONS.CLOCK_OUT, lstNewTimeBlocks, caf.getPayCalendarDates(), caf.getTimesheetDocument());

    			//call persist method that only saves added/deleted/changed timeblocks
    			TkServiceLocator.getTimeBlockService().saveTimeBlocks(caf.getTimesheetDocument().getTimeBlocks(), lstNewTimeBlocks);
    	    	//TkServiceLocator.getTimeHourDetailService().saveTimeHourDetail(tb);
    	    	//TkServiceLocator.getTimeBlockHistoryService().saveTimeBlockHistory(caf);
    	    }
    	    return mapping.findForward("basic");
    	}
}