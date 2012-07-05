package org.kuali.hr.time.clock.location.validation;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.hr.time.authorization.AuthorizationValidationUtils;
import org.kuali.hr.time.authorization.DepartmentalRule;
import org.kuali.hr.time.authorization.DepartmentalRuleAuthorizer;
import org.kuali.hr.time.clock.location.ClockLocationRule;
import org.kuali.hr.time.clock.location.ClockLocationRuleIpAddress;
import org.kuali.hr.time.service.base.TkServiceLocator;
import org.kuali.hr.time.util.TkConstants;
import org.kuali.hr.time.util.ValidationUtils;
import org.kuali.rice.kns.document.MaintenanceDocument;
import org.kuali.rice.kns.maintenance.rules.MaintenanceDocumentRuleBase;
import org.kuali.rice.krad.bo.PersistableBusinessObject;

import java.util.List;

public class ClockLocationRuleRule extends MaintenanceDocumentRuleBase {

	private static Logger LOG = Logger.getLogger(ClockLocationRuleRule.class);

	public static boolean validateIpAddress(String ip) {
		LOG.debug("Validating IP address: " + ip);
		if(ip == null) {
			return false;
		}
		if(ip.isEmpty() || ip.length() > 15 || ip.endsWith(TkConstants.IP_SEPERATOR) || ip.startsWith(TkConstants.IP_SEPERATOR)) {
			return false;
		}
		String[] lst =  StringUtils.split(ip, TkConstants.IP_SEPERATOR);
		if(lst.length > 4 || (lst.length <4 && ip.indexOf(TkConstants.WILDCARD_CHARACTER)< 0)) {
			return false;
		}
		for(String str : lst) {
			if(!str.matches(TkConstants.IP_WILDCARD_PATTERN)) {
				return false;
			}
		}
		return true;
	}
	
	boolean validateIpAddresses(List<ClockLocationRuleIpAddress> ipAddresses) {
		for(ClockLocationRuleIpAddress ip : ipAddresses) {
			if(!validateIpAddress(ip.getIpAddress())) {
				return this.flagIPAddressError(ip.getIpAddress());
			}
		}
		return true;
	}
	
	boolean flagIPAddressError(String ip) {
		this.putFieldError("ipAddresses", "ipaddress.invalid.format", ip);
		return false;
	}

	boolean validateWorkArea(ClockLocationRule clr) {
		boolean valid = true;
		if (clr.getWorkArea() != null
				&& !ValidationUtils.validateWorkArea(clr.getWorkArea(), clr
						.getEffectiveDate())) {
			this.putFieldError("workArea", "error.existence", "workArea '"
					+ clr.getWorkArea() + "'");
			valid = false;
		} else if (clr.getWorkArea() != null
				&& !clr.getWorkArea().equals(TkConstants.WILDCARD_LONG)) {
			int count = TkServiceLocator.getWorkAreaService().getWorkAreaCount(clr.getDept(), clr.getWorkArea());
			valid = (count > 0);
			if (!valid) {
				this.putFieldError("workArea", "dept.workarea.invalid.sync",
						clr.getWorkArea() + "");
			}
		}
		return valid;
	}

	protected boolean validateDepartment(ClockLocationRule clr) {
        boolean ret = false;

        if (!StringUtils.isEmpty(clr.getDept())) {
    		if (!ValidationUtils.validateDepartment(clr.getDept(), clr.getEffectiveDate())) {
			    this.putFieldError("dept", "error.existence", "department '" + clr.getDept() + "'");
            } else if (!DepartmentalRuleAuthorizer.hasAccessToWrite(clr)) {
                this.putFieldError("dept", "error.department.permissions", clr.getDept());
            } else {
                ret = true;
            }
        }

        return ret;
	}

	protected boolean validateJobNumber(ClockLocationRule clr) {
		boolean valid = true;
		if (clr.getJobNumber() == null) {
			valid = false;
		} else if (!clr.getJobNumber().equals(TkConstants.WILDCARD_LONG)) {
			int count = TkServiceLocator.getJobService().getJobCount(clr.getPrincipalId(), clr.getJobNumber(),null);
			valid = (count > 0);
			if (!valid) {
				this.putFieldError("jobNumber", "principalid.job.invalid.sync",
						clr.getJobNumber() + "");
			}
		}
		return valid;
	}

	protected boolean validatePrincipalId(ClockLocationRule clr) {
		boolean valid = false;
		if (clr.getPrincipalId() == null) {
			valid = false;
		} else {
			valid = true;
		}
		return valid;
	}

    /**
     * This method will validate whether the wildcard combination and wild
     * carded areas for this DepartmentalRule are valid or not. Errors are added
     * to the field errors to report back to the user interface as well.
     *
     * @param clr The Departmental rule we are investigating.
     *
     * @return true if wild card setup is correct, false otherwise.
     */
    boolean validateWildcards(DepartmentalRule clr) {
        boolean valid = true;

        if (!ValidationUtils.validateWorkAreaDeptWildcarding(clr)) {
            // add error when work area defined, department is wild carded.
            this.putFieldError("dept", "error.wc.wadef", "department '" + clr.getDept() + "'");
            valid = false;
        }

        if (StringUtils.equals(clr.getDept(), TkConstants.WILDCARD_CHARACTER) &&
                !AuthorizationValidationUtils.canWildcardDepartment(clr)) {
            this.putFieldError("dept", "error.wc.dept.perm", "department '" + clr.getDept() + "'");
            valid = false;
        }

        if (clr!= null && clr.getWorkArea() != null && clr.getWorkArea().equals(TkConstants.WILDCARD_LONG) &&
                !AuthorizationValidationUtils.canWildcardWorkArea(clr)) {
            this.putFieldError("dept", "error.wc.wa.perm", "department '" + clr.getDept() + "'");
            valid = false;
        }

        return valid;
    }


	/**
	 * It looks like the method that calls this class doesn't actually care
	 * about the return type.
	 */
	@Override
	protected boolean processCustomRouteDocumentBusinessRules(MaintenanceDocument document) {
		boolean valid = false;

		PersistableBusinessObject pbo = (PersistableBusinessObject) this.getNewBo();
		if (pbo instanceof ClockLocationRule) {
			ClockLocationRule clr = (ClockLocationRule) pbo;
            valid = this.validateDepartment(clr);
            valid &= this.validateWorkArea(clr);
            valid &= this.validateWildcards(clr);
            valid &= this.validatePrincipalId(clr);
            valid &= this.validateJobNumber(clr);
            valid &= this.validateIpAddresses(clr.getIpAddresses());
		}

		return valid;
	}
}
