package org.kuali.hr.time.overtime.daily.rule.service;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kuali.hr.time.assignment.Assignment;
import org.kuali.hr.time.overtime.daily.rule.DailyOvertimeRule;
import org.kuali.hr.time.overtime.daily.rule.dao.DailyOvertimeRuleDao;
import org.kuali.hr.time.service.base.TkServiceLocator;
import org.kuali.hr.time.timeblock.TimeBlock;
import org.kuali.hr.time.timesheet.TimesheetDocument;
import org.kuali.hr.time.util.TkConstants;
import org.kuali.hr.time.util.TkTimeBlockAggregate;
import org.kuali.hr.time.workschedule.WorkSchedule;

public class DailyOvertimeRuleServiceImpl implements DailyOvertimeRuleService {
	
	private DailyOvertimeRuleDao dailyOvertimeRuleDao = null;
	
	public void saveOrUpdate(DailyOvertimeRule dailyOvertimeRule) {
		dailyOvertimeRuleDao.saveOrUpdate(dailyOvertimeRule);
	}
	
	public void saveOrUpdate(List<DailyOvertimeRule> dailyOvertimeRules) {
		dailyOvertimeRuleDao.saveOrUpdate(dailyOvertimeRules);
	}
	
	@Override
	/**
	 * For now this will be implemented with all possible cases - need to improve this, though caching will alleviate 
	 * A lot of this insanity.
	 * 
	 * We have a binary permutation 2^n of our n independent variables, (in this case n=3; dept, work area, task). 
	 */
	public DailyOvertimeRule getDailyOvertimeRule(String dept, Long workArea, Long task, Date asOfDate) {
		DailyOvertimeRule dailyOvertimeRule = null;
		
		// department, workarea, task
		if (dailyOvertimeRule == null)
			dailyOvertimeRule = dailyOvertimeRuleDao.findDailyOvertimeRule(dept, workArea, task, asOfDate);
		
		// department, workarea, -1
		if (dailyOvertimeRule == null)
			dailyOvertimeRule = dailyOvertimeRuleDao.findDailyOvertimeRule(dept, workArea, -1L, asOfDate);
		
		// department, -1, task
		if (dailyOvertimeRule == null)
			dailyOvertimeRule = dailyOvertimeRuleDao.findDailyOvertimeRule(dept, -1L, task, asOfDate);
		
		// department, -1, -1
		if (dailyOvertimeRule == null)
			dailyOvertimeRule = dailyOvertimeRuleDao.findDailyOvertimeRule(dept, -1L, -1L, asOfDate);
		
		// *, workarea, task
		if (dailyOvertimeRule == null)
			dailyOvertimeRule = dailyOvertimeRuleDao.findDailyOvertimeRule("*", workArea, task, asOfDate);
		
		// *, workarea, -1
		if (dailyOvertimeRule == null)
			dailyOvertimeRule = dailyOvertimeRuleDao.findDailyOvertimeRule("*", workArea, -1L, asOfDate);
		
		// This is not a valid case.
		// *, -1, task
		//if (dailyOvertimeRule == null)
		//	dailyOvertimeRule = dailyOvertimeRuleDao.findDailyOvertimeRules("*", -1L, task, asOfDate);
		
		// *, -1, -1
		if (dailyOvertimeRule == null)
			dailyOvertimeRule = dailyOvertimeRuleDao.findDailyOvertimeRule("*", -1L, -1L, asOfDate);
		
		// Do anything else we have to do to the list, which is probably nothing.
		// ...
		//
		
		return dailyOvertimeRule;
	}



	public void setDailyOvertimeRuleDao(DailyOvertimeRuleDao dailyOvertimeRuleDao) {
		this.dailyOvertimeRuleDao = dailyOvertimeRuleDao;
	}
	
	public List<TimeBlock> processDailyOvertimeRule(TkTimeBlockAggregate timeBlockAggregate, TimesheetDocument timesheetDocument){
		List<TimeBlock> lstTimeBlocks = new ArrayList<TimeBlock>();
		Map<String, DailyOvertimeRule> assignKeyToDailyOverTimeRuleList = new HashMap<String, DailyOvertimeRule>();
		Map<String, List<WorkSchedule>> assignKeyToWorkScheduleList = new HashMap<String, List<WorkSchedule>>();
		//iterate over all assignments and place the list of rules if any in map
		for(Assignment assign : timesheetDocument.getAssignments()){
			DailyOvertimeRule dailyOvertimeRule = getDailyOvertimeRule(assign.getJob().getDept(), assign.getWorkArea(), assign.getTask(), timesheetDocument.getAsOfDate());
	
			if(dailyOvertimeRule !=null) {
				String assignKey = assign.getJobNumber()+"_"+assign.getWorkArea()+"_"+assign.getTask();
				assignKeyToDailyOverTimeRuleList.put(assignKey, dailyOvertimeRule);
				List<WorkSchedule> workScheduleList = TkServiceLocator.getWorkScheduleService().getWorkSchedules(timesheetDocument.getPrincipalId(), assign.getJob().getDept(), 
														assign.getWorkArea(), timesheetDocument.getAsOfDate());
				
				if(workScheduleList!=null && !workScheduleList.isEmpty()){
					assignKeyToWorkScheduleList.put(assignKey, workScheduleList);
				}
			}
		}
		//if no daily overtime rules found for this person bail out
		if(assignKeyToDailyOverTimeRuleList.isEmpty()){
			return timeBlockAggregate.getFlattenedTimeBlockList();
		}

		for(List<TimeBlock> lstDayTimeBlocks : timeBlockAggregate.getDayTimeBlockList()){
			Map<String,BigDecimal> assignKeyToDailyTotals = new HashMap<String,BigDecimal>();
			for(TimeBlock timeBlock : lstDayTimeBlocks){
				String assignKey = timeBlock.getJobNumber()+"_"+timeBlock.getWorkArea()+"_"+timeBlock.getTask();
				BigDecimal currVal = assignKeyToDailyTotals.get(assignKey);
				if(currVal == null){
					currVal = new BigDecimal(0);
				}
				currVal = currVal.add(timeBlock.getHours(), TkConstants.MATH_CONTEXT);
				assignKeyToDailyTotals.put(assignKey, currVal);
			}
			//TODO iterate over the daily totals that were just calculated and compare with rules for entry
			//TODO match up assignments to rules based on hours worked
			//TODO if work schedule entry for this day then ignore daily ovt rule as this may need to be impl specific logic 
			//TODO modify lstTimeBlocks accordingly
		}
		
		
		return lstTimeBlocks;
	}

}
