package org.kuali.hr.time.clock.location.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryFactory;
import org.kuali.hr.job.Job;
import org.kuali.hr.time.clock.location.ClockLocationRule;
import org.kuali.hr.time.util.TkConstants;
import org.kuali.hr.time.util.ValidationUtils;
import org.kuali.hr.time.workarea.WorkArea;
import org.kuali.rice.kns.bo.PersistableBusinessObject;
import org.kuali.rice.kns.document.MaintenanceDocument;
import org.kuali.rice.kns.maintenance.rules.MaintenanceDocumentRuleBase;

public class ClockLocationRuleRule extends MaintenanceDocumentRuleBase {

	// private static final String WILDCARD_CHARACTER = "\\*|%";
	private static final String WILDCARD_CHARACTER = "%";
	private static final String REGEX_IP_ADDRESS_STRING = "^(?:"
			+ WILDCARD_CHARACTER
			+ "|(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(?:"
			+ WILDCARD_CHARACTER
			+ "|(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(?:"
			+ WILDCARD_CHARACTER
			+ "|(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(?:"
			+ WILDCARD_CHARACTER
			+ "|(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)))))$";
	private static final Pattern REGEX_IP_ADDRESS_PATTERN = Pattern
			.compile(REGEX_IP_ADDRESS_STRING);

	private static Logger LOG = Logger.getLogger(ClockLocationRuleRule.class);

	boolean validateIpAddress(String ip) {
		boolean valid = false;

		LOG.debug("Validating IP address: " + ip);
		if (ip != null && !ip.equals("")) {
			Matcher matcher = REGEX_IP_ADDRESS_PATTERN.matcher(ip);
			valid = matcher.matches();
			if (!valid) {
				this.putFieldError("ipAddress", "ipaddress.invalid.format", ip);
			}
		}
		return valid;
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
			Criteria crit = new Criteria();
			crit.addEqualTo("dept", clr.getDept());
			crit.addEqualTo("workArea", clr.getWorkArea());
			Query query = QueryFactory.newQuery(WorkArea.class, crit);
			int count = PersistenceBrokerFactory.defaultPersistenceBroker()
					.getCount(query);
			valid = (count > 0);
			if (!valid) {
				this.putFieldError("workArea", "dept.workarea.invalid.sync",
						clr.getWorkArea() + "");
			}
		}
		return valid;
	}

	protected boolean validateDepartment(ClockLocationRule clr) {
		if (clr.getDept() != null
				&& !ValidationUtils.validateDepartment(clr.getDept(), clr
						.getEffectiveDate())) {
			this.putFieldError("dept", "error.existence", "department '"
					+ clr.getDept() + "'");
			return false;
		} else {
			return true;
		}
	}

	protected boolean validateJobNumber(ClockLocationRule clr) {
		boolean valid = false;
		if (clr.getJobNumber() == null) {
			valid = false;
		} else if (!clr.getJobNumber().equals(TkConstants.WILDCARD_LONG)) {
			Criteria crit = new Criteria();
			crit.addEqualTo("principalId", clr.getPrincipalId());
			crit.addEqualTo("jobNumber", clr.getJobNumber());
			Query query = QueryFactory.newQuery(Job.class, crit);
			int count = PersistenceBrokerFactory.defaultPersistenceBroker()
					.getCount(query);
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
	 * It looks like the method that calls this class doesn't actually care
	 * about the return type.
	 */
	@Override
	protected boolean processCustomSaveDocumentBusinessRules(
			MaintenanceDocument document) {
		boolean valid = false;

		LOG.debug("entering custom validation for ClockLocationRule");
		PersistableBusinessObject pbo = this.getNewBo();
		if (pbo instanceof ClockLocationRule) {
			ClockLocationRule clr = (ClockLocationRule) pbo;
			if (clr != null) {
				valid = true;
				valid &= this.validateDepartment(clr);
				valid &= this.validateWorkArea(clr);
				valid &= this.validatePrincipalId(clr);
				valid &= this.validateJobNumber(clr);
				valid &= this.validateIpAddress(clr.getIpAddress());
			}
		}

		return valid;
	}
}