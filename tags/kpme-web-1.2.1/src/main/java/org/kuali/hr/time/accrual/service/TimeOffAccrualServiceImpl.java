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
package org.kuali.hr.time.accrual.service;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.kuali.hr.time.accrual.AccrualCategory;
import org.kuali.hr.time.accrual.TimeOffAccrual;
import org.kuali.hr.time.accrual.dao.TimeOffAccrualDao;
import org.kuali.hr.time.earncode.EarnCode;
import org.kuali.hr.time.service.base.TkServiceLocator;
import org.kuali.hr.time.timeblock.TimeBlock;
import org.kuali.hr.time.timesheet.TimesheetDocument;
import org.kuali.hr.time.util.TkConstants;

public class TimeOffAccrualServiceImpl implements TimeOffAccrualService {

	public static final String ACCRUAL_CATEGORY_KEY = "accrualCategory";
	public static final String ACCRUAL_NAME_KEY = "accrualName";
	public static final String YEARLY_CARRYOVER_KEY = "yearlyCarryover";
	public static final String HOURS_ACCRUED_KEY = "hoursAccrued";
	public static final String HOURS_TAKEN_KEY = "hoursTaken";
	public static final String HOURS_ADJUST_KEY = "hoursAdjust";
	public static final String TOTAL_HOURS_KEY = "totalHours";
	public static final String EFF_DATE_KEY = "effdt";

	private TimeOffAccrualDao timeOffAccrualDao;

	public void setTimeOffAccrualDao(TimeOffAccrualDao timeOffAccrualDao) {
		this.timeOffAccrualDao = timeOffAccrualDao;
	}

	@Override
	public List<TimeOffAccrual> getTimeOffAccruals(String principalId, Date asOfDate) {
		return timeOffAccrualDao.getActiveTimeOffAccruals(principalId, asOfDate);
	}

	@Override
	public List<Map<String, Object>> getTimeOffAccrualsCalc(String principalId, Date asOfDate) {

		List<Map<String, Object>> timeOffAccrualsCalc = new ArrayList<Map<String, Object>>();
		Map<String,String> accrualCatToDescr = new HashMap<String, String>();

		for (TimeOffAccrual timeOffAccrual : getTimeOffAccruals(principalId, asOfDate)) {
			String accrualCatDescr = accrualCatToDescr.get(timeOffAccrual.getAccrualCategory());
			//if no accrual cat description found look up accrual category and find one
			if (StringUtils.isBlank(accrualCatDescr)){
				AccrualCategory accrualCat = TkServiceLocator.getAccrualCategoryService().getAccrualCategory(timeOffAccrual.getAccrualCategory(), asOfDate);
				if (accrualCat != null) {
					accrualCatDescr = accrualCat.getDescr();
					accrualCatToDescr.put(accrualCat.getAccrualCategory(), accrualCatDescr);
				}
			}
			Map<String, Object> output = new LinkedHashMap<String, Object>();
			output.put(ACCRUAL_CATEGORY_KEY, accrualCatDescr + "("+timeOffAccrual.getAccrualCategory()+")");
			output.put(ACCRUAL_NAME_KEY, timeOffAccrual.getAccrualCategory());
			output.put(YEARLY_CARRYOVER_KEY, timeOffAccrual.getYearlyCarryover());
			output.put(HOURS_ACCRUED_KEY, timeOffAccrual.getHoursAccrued());
			output.put(HOURS_TAKEN_KEY, timeOffAccrual.getHoursTaken());
			output.put(HOURS_ADJUST_KEY, timeOffAccrual.getHoursAdjust());
			BigDecimal totalHours = timeOffAccrual.getYearlyCarryover().add(timeOffAccrual.getHoursAccrued().subtract(timeOffAccrual.getHoursTaken()).add(timeOffAccrual.getHoursAdjust()));
			output.put(TOTAL_HOURS_KEY, totalHours);
			output.put(EFF_DATE_KEY, timeOffAccrual.getEffectiveDate());

			timeOffAccrualsCalc.add(output);
		}

		return timeOffAccrualsCalc;
	}

	public List<String> validateAccrualHoursLimit(TimesheetDocument timesheetDocument) {
    	 String pId = "";
         if (timesheetDocument != null) {
             pId = timesheetDocument.getPrincipalId();
         }
         
     	return validateAccrualHoursLimit(pId, timesheetDocument.getTimeBlocks(),  timesheetDocument.getAsOfDate());
        
    }
	
	public List<String> validateAccrualHoursLimit(String pId, List<TimeBlock> tbList, Date asOfDate) {
		 List<String> warningMessages = new ArrayList<String>();

         List<Map<String, Object>> calcList = this.getTimeOffAccrualsCalc(pId, asOfDate);
         
           if (tbList.isEmpty()) {
             return warningMessages;
         }
         List<String> accruals = new ArrayList<String>();
         for (Map<String, Object> aMap : calcList) {
    		accruals.add((String) aMap.get(ACCRUAL_CATEGORY_KEY));
    	 }
         for (Map<String, Object> aMap : calcList) {
             String accrualCategory = (String) aMap.get(ACCRUAL_NAME_KEY);
             List<TimeBlock> warningTbs = new ArrayList<TimeBlock>();
             BigDecimal totalForAccrCate = this.totalForAccrCate(accrualCategory, tbList, warningTbs);
             //if there is no timeblocks for this category no warning is necessary 
             if(totalForAccrCate.compareTo(BigDecimal.ZERO)==0){
            	 continue;
             }
             BigDecimal balanceHrs = (((BigDecimal)aMap.get(YEARLY_CARRYOVER_KEY)).add((BigDecimal)aMap.get(HOURS_ACCRUED_KEY)).subtract((BigDecimal)aMap.get(HOURS_TAKEN_KEY)).add((BigDecimal)aMap.get(HOURS_ADJUST_KEY)));
             
             if (totalForAccrCate.compareTo(balanceHrs) == 1) {
             	String msg = "Warning: Total hours entered (" + totalForAccrCate.toString() + ") for Accrual Category \"" + (String) aMap.get(ACCRUAL_CATEGORY_KEY) + "\" has exceeded balance (" + balanceHrs.toString() + "). Problem Time Blocks are:<br/>";
             	for(TimeBlock tb : warningTbs) {
             		msg += "Earn code: " + tb.getEarnCode()+ " Hours: " + tb.getHours().toString() + " on Date " + (tb.getBeginTimeDisplay() != null ? tb.getBeginTimeDisplay().toString(TkConstants.DT_BASIC_DATE_FORMAT) : "") + "<br/>";
             	}
                warningMessages.add(msg);

             }
         }
         return warningMessages;
    }
	public List<String> validateAccrualHoursLimitByEarnCode(TimesheetDocument timesheetDocument, String earnCode) {
		 List<String> warningMessages = new ArrayList<String>();
   	 String pId = "";
        if (timesheetDocument != null) {
            pId = timesheetDocument.getPrincipalId();
        }
        List<Map<String, Object>> calcList = this.getTimeOffAccrualsCalc(pId, timesheetDocument.getAsOfDate());

        List<TimeBlock> tbList = timesheetDocument.getTimeBlocks();
        if (tbList.isEmpty()) {
            return warningMessages;
        }
        List<String> accruals = new ArrayList<String>();
        for (Map<String, Object> aMap : calcList) {
   		accruals.add((String) aMap.get(ACCRUAL_CATEGORY_KEY));
   	 }
        
        List<AccrualCategory> accrualCategories = TkServiceLocator.getAccrualCategoryService().getActiveAccrualCategories(timesheetDocument.getAsOfDate());
    
        for(AccrualCategory accrualCategory : accrualCategories){
       	 if(!accruals.contains(accrualCategory.getAccrualCategory()) && !StringUtils.equals(TkConstants.HOLIDAY_EARN_CODE, accrualCategory.getAccrualCategory())){
       		Map<String, Object> accrualData = new LinkedHashMap<String, Object>();
    			accrualData.put(ACCRUAL_CATEGORY_KEY, accrualCategory.getAccrualCategory());
    			accrualData.put(YEARLY_CARRYOVER_KEY, new BigDecimal(0.00));
    			accrualData.put(HOURS_ACCRUED_KEY, new BigDecimal(0.00));
    			accrualData.put(HOURS_TAKEN_KEY, new BigDecimal(0.00));
    			accrualData.put(HOURS_ADJUST_KEY, new BigDecimal(0.00));
    			calcList.add(accrualData);
       	 }
        }
        for (Map<String, Object> aMap : calcList) {
            String accrualCategory = (String) aMap.get(ACCRUAL_CATEGORY_KEY);
            List<TimeBlock> warningTbs = new ArrayList<TimeBlock>();
            BigDecimal totalForAccrCate = this.totalForAccrCate(accrualCategory, tbList, warningTbs);
            BigDecimal balanceHrs = (((BigDecimal)aMap.get(YEARLY_CARRYOVER_KEY)).add((BigDecimal)aMap.get(HOURS_ACCRUED_KEY)).subtract((BigDecimal)aMap.get(HOURS_TAKEN_KEY)).add((BigDecimal)aMap.get(HOURS_ADJUST_KEY)));
            
            if (totalForAccrCate.compareTo(balanceHrs) == 1) {
            	if (accrualCategory.equals(earnCode)) {
	            	String msg = "Warning: Total hours entered (" + totalForAccrCate.toString() + ") for Accrual Category \"" + (String) aMap.get(ACCRUAL_CATEGORY_KEY) + "\" has exceeded balance (" + balanceHrs.toString() + "). Problem Time Blocks are: ";
	            	for(TimeBlock tb : warningTbs) {
	            		msg += "Earn code: " + tb.getEarnCode()+ " Hours: " + tb.getHours().toString() + " on Date " + (tb.getBeginTimeDisplay() != null ? tb.getBeginTimeDisplay().toString(TkConstants.DT_BASIC_DATE_FORMAT) : "") + " ";
	            	}
	            	
	               warningMessages.add(msg);
            	}

            }
        }
        return warningMessages;
   }

    public BigDecimal totalForAccrCate(String accrualCategory, List<TimeBlock> tbList, List<TimeBlock> warningTbs) {
        BigDecimal total = BigDecimal.ZERO;
        for (TimeBlock tb : tbList) {
            String earnCode = tb.getEarnCode();
            Date asOfDate = new java.sql.Date(tb.getBeginTimestamp().getTime());
            EarnCode ec = TkServiceLocator.getEarnCodeService().getEarnCode(earnCode, asOfDate);
            String accrCate = "";
            if (ec != null) {
                accrCate = ec.getAccrualCategory();
                if (accrCate != null) {
                    if (accrCate.equals(accrualCategory)) {
                        total = total.add(tb.getHours());
                        warningTbs.add(tb);
                    }
                }
            }
        }
        return total;
    }

	@Override
	public TimeOffAccrual getTimeOffAccrual(Long laTimeOffAccrualId) {
		return timeOffAccrualDao.getTimeOffAccrual(laTimeOffAccrualId);
	}
	
	@Override
	public int getTimeOffAccrualCount(String accrualCategory, Date effectiveDate, String principalId, String lmAccrualId) {
		return timeOffAccrualDao.getTimeOffAccrualCount(accrualCategory, effectiveDate, principalId, lmAccrualId);
	}

    @Override
    public  List<TimeOffAccrual> getTimeOffAccruals(String principalId, String accrualCategory){
        return timeOffAccrualDao.getTimeOffAccruals(principalId, accrualCategory);
    }
}