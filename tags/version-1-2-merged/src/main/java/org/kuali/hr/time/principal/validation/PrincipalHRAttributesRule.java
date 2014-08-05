package org.kuali.hr.time.principal.validation;

import org.kuali.hr.time.principal.PrincipalHRAttributes;
import org.kuali.hr.time.util.ValidationUtils;
import org.kuali.rice.kns.document.MaintenanceDocument;
import org.kuali.rice.kns.maintenance.rules.MaintenanceDocumentRuleBase;
import org.kuali.rice.krad.bo.PersistableBusinessObject;

public class PrincipalHRAttributesRule extends MaintenanceDocumentRuleBase {

	private boolean validatePrincipalId(PrincipalHRAttributes principalHRAttr) {
		if (principalHRAttr.getPrincipalId() != null
				&& !ValidationUtils.validatePrincipalId(principalHRAttr
						.getPrincipalId())) {
			this.putFieldError("principalId", "error.existence",
					"principalId '" + principalHRAttr.getPrincipalId() + "'");
			return false;
		} else {
			return true;
		}
	}
	
	private boolean validatePayCalendar(PrincipalHRAttributes principalHRAttr) {
		if (principalHRAttr.getPayCalendar() != null
				&& !ValidationUtils.validateCalendarByType(principalHRAttr.getPayCalendar(), "Pay")) {
			this.putFieldError("payCalendar", "error.existence",
					"Pay Calendar '" + principalHRAttr.getPayCalendar() + "'");
			return false;
		} else {
			return true;
		}
	}
	
	private boolean validateLeaveCalendar(PrincipalHRAttributes principalHRAttr) {
		if (principalHRAttr.getLeaveCalendar() != null
				&& !ValidationUtils.validateCalendarByType(principalHRAttr.getLeaveCalendar(), "Leave")) {
			this.putFieldError("leaveCalendar", "error.existence",
					"Leave Calendar '" + principalHRAttr.getLeaveCalendar() + "'");
			return false;
		} else {
			return true;
		}
	}

	
	boolean validateEffectiveDate(PrincipalHRAttributes principalHRAttr) {
		boolean valid = true;
		if (principalHRAttr.getEffectiveDate() != null && !ValidationUtils.validateOneYearFutureDate(principalHRAttr.getEffectiveDate())) {
			this.putFieldError("effectiveDate", "error.date.exceed.year", "Effective Date");
			valid = false;
		}
		return valid;
	}
	
	@Override
	protected boolean processCustomRouteDocumentBusinessRules(
			MaintenanceDocument document) {
		boolean valid = false;

		LOG.debug("entering custom validation for Job");
		PersistableBusinessObject pbo = (PersistableBusinessObject) this.getNewBo();
		if (pbo instanceof PrincipalHRAttributes) {
			PrincipalHRAttributes principalHRAttr = (PrincipalHRAttributes) pbo;
			if (principalHRAttr != null) {
				valid = true;
				valid &= this.validatePrincipalId(principalHRAttr);
				// KPME-1442 Kagata
				//valid &= this.validateEffectiveDate(principalHRAttr);
				valid &= this.validatePayCalendar(principalHRAttr);
			}
		}
		return valid;
	}

}