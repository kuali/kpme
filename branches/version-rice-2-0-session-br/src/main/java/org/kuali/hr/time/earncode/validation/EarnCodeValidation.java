package org.kuali.hr.time.earncode.validation;

import org.apache.commons.lang.StringUtils;
import org.kuali.hr.time.earncode.EarnCode;
import org.kuali.hr.time.service.base.TkServiceLocator;
import org.kuali.hr.time.timeblock.TimeBlock;
import org.kuali.hr.time.util.TkConstants;
import org.kuali.hr.time.util.ValidationUtils;
import org.kuali.rice.kns.document.MaintenanceDocument;
import org.kuali.rice.kns.maintenance.rules.MaintenanceDocumentRuleBase;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

public class EarnCodeValidation extends MaintenanceDocumentRuleBase{
	
	boolean validateRollupToEarnCode(String earnCode, Date asOfDate) {
		boolean valid = true;
		if (!StringUtils.isEmpty(earnCode) && !ValidationUtils.validateEarnCode(earnCode, asOfDate)) {
			this.putFieldError("rollupToEarnCode", "earncode.rollupToEarnCode.notfound", "Roll up to Earn code "
					+ earnCode + "'");
			valid = false;
		}
		return valid;
	}
	
	boolean validateDefaultAmountOfTime(Long defaultAmountofTime) {
		boolean valid = true;
		if ( defaultAmountofTime != null ){
			if (defaultAmountofTime.compareTo(new Long(24)) > 0  || defaultAmountofTime.compareTo(new Long(0)) < 0) {
				this.putFieldError("defaultAmountofTime", "error.leaveCode.hours", "Default Amount of Time '"
						+ defaultAmountofTime + "'");
				valid = false;
			}
		}
		return valid;
	}
	
	boolean validateRecordMethod(String recordMethod, String accrualCategory, Date asOfDate){
		boolean valid = true;
		if(recordMethod != null) {
			if(StringUtils.isNotEmpty(accrualCategory)) {
				valid = ValidationUtils.validateRecordMethod(recordMethod, accrualCategory, asOfDate);
				if(!valid) {
					this.putFieldError("recordMethod", "earncode.recordMethod.invalid", "Record Method");
				}
			}
		}
		return valid;
	}
	
	@Override
	protected boolean processCustomRouteDocumentBusinessRules(MaintenanceDocument document) {
		EarnCode earnCode = (EarnCode)this.getNewBo();
		EarnCode oldEarnCode = (EarnCode)this.getOldBo();
		if ((StringUtils.equals(oldEarnCode.getEarnCode(), TkConstants.LUNCH_EARN_CODE) 
				|| StringUtils.equals(oldEarnCode.getEarnCode(), TkConstants.HOLIDAY_EARN_CODE))
					&& !earnCode.isActive()) {
			this.putFieldError("active", "earncode.inactivate.locked", earnCode
					.getEarnCode());
		}
		//if earn code is not designated how to record then throw error
		if (earnCode.getHrEarnCodeId() == null) {
			if (ValidationUtils.validateEarnCode(earnCode.getEarnCode(), null)) {
				// If there IS an earn code, ie, it is valid, we need to report
				// an error as earn codes must be unique.			
				this.putFieldError("earnCode", "earncode.earncode.unique");
				return false;
			}
		}
		
//		if(earnCode.getRecordAmount() == false && 
//			earnCode.getRecordHours() == false && 
//			earnCode.getRecordTime() == false){
//			this.putFieldError("recordTime", "earncode.record.not.specified");
//			return false;
//		}
//		//confirm that only one of the check boxes is checked
//		//if more than one are true then throw an error
//		if(earnCode.getRecordAmount() && earnCode.getRecordHours() && earnCode.getRecordTime()){
//			this.putFieldError("recordTime", "earncode.record.unique");
//			return false;
//		}
//		boolean result = earnCode.getRecordAmount() ^ earnCode.getRecordHours() ^ earnCode.getRecordTime();
//		if(!result){
//			this.putFieldError("recordTime", "earncode.record.unique");
//			return false;
//		}

		//check if the effective date of the accrual category is prior to effective date of the earn code 
		//accrual category is an optional field
		if(StringUtils.isNotEmpty(earnCode.getAccrualCategory())){
			if (!ValidationUtils.validateAccrualCategory(earnCode.getAccrualCategory(), earnCode.getEffectiveDate())) {
				this.putFieldError("accrualCategory", "earncode.accrualCategory.invalid", earnCode.getAccrualCategory());
				return false;
			}
		}
		
		// check if there's a newer version of the Earn Code
		int count = TkServiceLocator.getEarnCodeService().getNewerEarnCodeCount(earnCode.getEarnCode(), earnCode.getEffectiveDate());
		if(count > 0) {
			this.putFieldError("effectiveDate", "earncode.effectiveDate.newer.exists");
			return false;
		}
		
//		//check if the ovtEarnCode and InflateMinHours is equal to 0
//		if((earnCode.getRecordAmount() || earnCode.getOvtEarnCode()) && earnCode.getInflateMinHours().compareTo(new BigDecimal(0))!=0){
//			this.putFieldError("inflateMinHours", "earncode.inflateminhours.should.be.zero");
//			return false;
//		}
//		
//		//check if the RecordAmount and AccrualCategory has no value
//		if(earnCode.getRecordAmount() && StringUtils.isNotBlank(earnCode.getAccrualCategory())){
//			this.putFieldError("accrualCategory", "earncode.accrualcategory.should.be.blank");
//			return false;
//		}
//		
//		//check if the RecordAmount and InflateFactor is equal to 1
//		if(earnCode.getRecordAmount() && (earnCode.getInflateFactor().compareTo(new BigDecimal(1))!=0)){
//			this.putFieldError("inflateFactor", "earncode.inflatefactor.should.be.one");
//			return false;
//		}
		
		// kpme-937 can not inactivation of a earn code if it used in active timeblocks
		List<TimeBlock> latestEndTimestampTimeBlocks =  TkServiceLocator.getTimeBlockService().getLatestEndTimestamp();
		
		if ( !earnCode.isActive() && earnCode.getEffectiveDate().before(latestEndTimestampTimeBlocks.get(0).getEndDate()) ){
			List<TimeBlock> activeTimeBlocks = TkServiceLocator.getTimeBlockService().getTimeBlocksWithEarnCode(earnCode.getEarnCode(), earnCode.getEffectiveDate());
			if(activeTimeBlocks != null && !activeTimeBlocks.isEmpty()) {
				this.putFieldError("earnCode", "earncode.earncode.inactivate", earnCode.getEarnCode());
				return false;
			}
		}
		
		if(!(this.validateDefaultAmountOfTime(earnCode.getDefaultAmountofTime()))) {
			return false;
		}
		
		if(earnCode.getRollupToEarnCode() != null && !StringUtils.isEmpty(earnCode.getRollupToEarnCode())) {
			if(!(this.validateRollupToEarnCode(earnCode.getRollupToEarnCode(), earnCode.getEffectiveDate()))) {
				return false;
			}
		}
		
		if(!validateRecordMethod(earnCode.getRecordMethod(), earnCode.getAccrualCategory(), earnCode.getEffectiveDate())) {
			return false;
		}
		
		return true;
	}

}
