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
package org.kuali.kpme.pm.positiondepartment.validation;

import org.apache.commons.lang.StringUtils;
import org.kuali.kpme.core.bo.utils.ValidationUtils;
import org.kuali.kpme.pm.positiondepartment.PositionDepartment;
import org.kuali.kpme.pm.util.PmValidationUtils;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;

@SuppressWarnings("deprecation")
public class PositionDepartmentValidation extends MaintenanceDocumentRuleBase  {
	@Override
	protected boolean processCustomRouteDocumentBusinessRules(MaintenanceDocument document) {
		boolean valid = false;
		LOG.debug("entering custom validation for Position Department");
		PositionDepartment positionDepartment = (PositionDepartment) this.getNewDataObject();
		
		if (positionDepartment != null) {
			valid = true;
			valid &= this.validateInstitution(positionDepartment);
			valid &= this.validateCampus(positionDepartment);
			valid &= this.validateDepartment(positionDepartment);
			valid &= this.validateAffiliation(positionDepartment);
		}
		return valid;
	}
	
	private boolean validateInstitution(PositionDepartment positionDepartment) {
		if (StringUtils.isNotEmpty(positionDepartment.getInstitution())
				&& !PmValidationUtils.validateInstitution(positionDepartment.getInstitution(), positionDepartment.getEffectiveLocalDate())) {
			this.putFieldError("institution", "error.existence", "Institution '"
					+ positionDepartment.getInstitution() + "'");
			return false;
		} else {
			return true;
		}
	}
	
	private boolean validateCampus(PositionDepartment positionDepartment) {
		if (StringUtils.isNotEmpty(positionDepartment.getCampus())
				&& !PmValidationUtils.validateCampus(positionDepartment.getCampus())) {
			this.putFieldError("campus", "error.existence", "Campus '"
					+ positionDepartment.getCampus() + "'");
			return false;
		} else {
			return true;
		}
	}
	
	private boolean validateDepartment(PositionDepartment positionDepartment) {
		if (StringUtils.isNotEmpty(positionDepartment.getCampus())
				&& !ValidationUtils.validateDepartment(positionDepartment.getDepartment(), positionDepartment.getEffectiveLocalDate())) {
			this.putFieldError("department", "error.existence", "Department '"
					+ positionDepartment.getDepartment() + "'");
			return false;
		} else {
			return true;
		}
	}
	
	private boolean validateAffiliation(PositionDepartment positionDepartment) {
		if (StringUtils.isNotEmpty(positionDepartment.getPositionDeptAffl())
				&& !PmValidationUtils.validateAffiliation(positionDepartment.getPositionDeptAffl(), positionDepartment.getEffectiveLocalDate())) {
			this.putFieldError("positionDeptAffl", "error.existence", "Affiliation '"
					+ positionDepartment.getPositionDeptAffl() + "'");
			return false;
		} else {
			return true;
		}
	}
}
