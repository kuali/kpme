package org.kuali.hr.time.dept.earncode.validation;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.kuali.hr.time.dept.earncode.DepartmentEarnCode;
import org.kuali.hr.time.service.base.TkServiceLocator;
import org.kuali.hr.time.timeblock.TimeBlock;
import org.kuali.hr.time.util.TkConstants;
import org.kuali.hr.time.util.ValidationUtils;
import org.kuali.rice.kns.bo.PersistableBusinessObject;
import org.kuali.rice.kns.document.MaintenanceDocument;
import org.kuali.rice.kns.maintenance.rules.MaintenanceDocumentRuleBase;


public class DepartmentEarnCodeRule extends MaintenanceDocumentRuleBase {

	boolean validateSalGroup(DepartmentEarnCode departmentEarnCode ) {
		if (!ValidationUtils.validateSalGroup(departmentEarnCode.getHrSalGroup(), departmentEarnCode.getEffectiveDate())) {
			this.putFieldError("hrSalGroup", "error.existence", "Salgroup '" + departmentEarnCode.getHrSalGroup()+ "'");
			return false;
		} else {
			return true;
		}
	}

	boolean validateDept(DepartmentEarnCode clr) {
		if (!ValidationUtils.validateDepartment(clr.getDept(), clr.getEffectiveDate()) && !StringUtils.equals(clr.getDept(), TkConstants.WILDCARD_CHARACTER)) {
			this.putFieldError("dept", "error.existence", "department '" + clr.getDept() + "'");
			return false;
		} else {
			return true;
		}
	}

	boolean validateEarnCode(DepartmentEarnCode departmentEarnCode ) {
		if (!ValidationUtils.validateEarnCode(departmentEarnCode.getEarnCode(), departmentEarnCode.getEffectiveDate())) {
			this.putFieldError("earnCode", "error.existence", "Earncode '" + departmentEarnCode.getEarnCode()+ "'");
			return false;
		} else {
			return true;
		}
	}

	boolean validateDuplication(DepartmentEarnCode departmentEarnCode) {
		if(ValidationUtils.duplicateDeptEarnCodeExists(departmentEarnCode)) {
			this.putFieldError("effectiveDate", "deptEarncode.duplicate.exists");
			return false;
		} else {
			return true;
		}
	}

	boolean validateLocation(DepartmentEarnCode departmentEarnCode) {
		if (departmentEarnCode.getLocation() != null
				&& !ValidationUtils.validateLocation(departmentEarnCode.getLocation(), null) && 
				!StringUtils.equals(departmentEarnCode.getLocation(), TkConstants.WILDCARD_CHARACTER)) {
			this.putFieldError("location", "error.existence", "location '"
					+ departmentEarnCode.getLocation() + "'");
			return false;
		} else {
			return true;
		}
	}
	
	boolean isEarnCodeUsedByActiveTimeBlocks(DepartmentEarnCode departmentEarnCode){
		// KPME-1106 can not inactivation of a department earn code if it used in active time blocks
		boolean valid = true;
		List<TimeBlock> latestEndTimestampTimeBlocks =  TkServiceLocator.getTimeBlockService().getLatestEndTimestamp();
		
		if ( !departmentEarnCode.isActive() && departmentEarnCode.getEffectiveDate().before(latestEndTimestampTimeBlocks.get(0).getEndDate()) ){
			List<TimeBlock> activeTimeBlocks = new ArrayList<TimeBlock>();
			activeTimeBlocks = TkServiceLocator.getTimeBlockService().getTimeBlocks();
			for(TimeBlock activeTimeBlock : activeTimeBlocks){
				if ( departmentEarnCode.getEarnCode().equals(activeTimeBlock.getEarnCode())){
					this.putFieldError("active", "deptEarncode.deptEarncode.inactivate", departmentEarnCode.getEarnCode());
					return  false;
				}
			}
		} 
		
		return valid;
		
	}

	/**
	 * It looks like the method that calls this class doesn't actually care
	 * about the return type.
	 */
	@Override
	protected boolean processCustomRouteDocumentBusinessRules(
			MaintenanceDocument document) {
		boolean valid = false;

		LOG.debug("entering custom validation for DepartmentEarnCode");
		PersistableBusinessObject pbo = this.getNewBo();
		if (pbo instanceof DepartmentEarnCode) {
			DepartmentEarnCode departmentEarnCode = (DepartmentEarnCode) pbo;

			if (departmentEarnCode != null) {
				valid = true;
				valid &= this.validateSalGroup(departmentEarnCode);
				valid &= this.validateDept(departmentEarnCode);
				valid &= this.validateEarnCode(departmentEarnCode);
				valid &= this.validateDuplication(departmentEarnCode);
				valid &= this.validateLocation(departmentEarnCode);
				valid &= this.isEarnCodeUsedByActiveTimeBlocks(departmentEarnCode);
			}

		}

		return valid;
	}

}