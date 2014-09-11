package org.kuali.hr.time.assignment.validation;

import java.io.FileOutputStream;
import java.sql.Date;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.kuali.hr.job.Job;
import org.kuali.hr.time.assignment.Assignment;
import org.kuali.hr.time.assignment.AssignmentAccount;
import org.kuali.hr.time.earncode.EarnCode;
import org.kuali.hr.time.service.base.TkServiceLocator;
import org.kuali.hr.time.timeblock.TimeBlock;
import org.kuali.hr.time.util.ValidationUtils;
import org.kuali.kfs.coa.businessobject.Account;
import org.kuali.kfs.coa.businessobject.ObjectCode;
import org.kuali.kfs.coa.businessobject.SubObjectCode;
import org.kuali.rice.kim.service.KIMServiceLocator;
import org.kuali.rice.kns.bo.PersistableBusinessObject;
import org.kuali.rice.kns.document.MaintenanceDocument;
import org.kuali.rice.kns.maintenance.rules.MaintenanceDocumentRuleBase;
import org.kuali.rice.kns.service.KNSServiceLocator;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class AssignmentRule extends MaintenanceDocumentRuleBase {

	protected boolean validateWorkArea(Assignment assignment) {
		boolean valid = ValidationUtils.validateWorkArea(assignment
				.getWorkArea(), assignment.getEffectiveDate());

		if (!valid) {
			this.putFieldError("workArea", "error.existence", "work area '"
					+ assignment.getWorkArea() + "'");
		}
		return valid;
	}

	protected boolean validateTask(Assignment assignment) {
		boolean valid = ValidationUtils.validateTask(assignment.getTask(),
				assignment.getEffectiveDate());

		if (!valid) {
			this.putFieldError("task", "error.existence", "task '"
					+ assignment.getTask() + "'");
		}

		return valid;
	}

	protected boolean validateJob(Assignment assignment) {
		boolean valid = false;
		LOG.debug("Validating job: " + assignment.getJob());
		Job job = TkServiceLocator.getJobSerivce().getJob(
				assignment.getPrincipalId(), assignment.getJobNumber(),
				assignment.getEffectiveDate());
		// Job job =
		// KNSServiceLocator.getBusinessObjectService().findBySinglePrimaryKey(Job.class,
		// assignment.getJob().getHrJobId());
		if (job != null) {
			valid = true;

			LOG.debug("found job.");
		} else {
			this.putFieldError("jobNumber", "error.existence", "jobNumber '"
					+ assignment.getJobNumber() + "'");
		}
		return valid;
	}

	protected boolean validateActiveAccountTotalPercentage(Assignment assignment) {
		boolean valid = false;
		LOG.debug("Validating ActiveAccountTotalPercentage: ");
		List<AssignmentAccount> assignmentAccounts = assignment
				.getAssignmentAccounts();
		if (assignmentAccounts != null) {
			int percent = 0;
			for (AssignmentAccount account : assignmentAccounts) {
				if (account.isActive() && account.getPercent() != null) {
					percent += account.getPercent().toBigInteger().intValue();
				}
			}
			if (percent == 100) {
				valid = true;
			} else {
				this.putGlobalError("error.active.account.percentage");
			}
		} else {
			valid = true;
		}
		return valid;
	}

	protected boolean validatePercentagePerEarnCode(Assignment assignment) {
		boolean valid = true;
		LOG.debug("Validating PercentagePerEarnCode: ");
		List<AssignmentAccount> assignmentAccounts = assignment
				.getAssignmentAccounts();
		if (assignmentAccounts != null) {
			Map<String, Integer> earnCodePercent = new HashMap<String, Integer>();
			for (AssignmentAccount account : assignmentAccounts) {
				if (account.getPercent() != null) {
					int percent = 0;
					if (earnCodePercent.containsKey(account.getAccountNbr())) {
						percent = earnCodePercent.get(account.getAccountNbr());
					}
					percent += account.getPercent().toBigInteger().intValue();
					earnCodePercent.put(account.getAccountNbr(), percent);
				}
			}
			Iterator<String> itr = earnCodePercent.keySet().iterator();
			while (itr.hasNext()) {
				String acctNumber = itr.next();
				if (earnCodePercent.get(acctNumber) != 100) {
					valid = false;
				}
			}
		}
		if (!valid) {
			this.putGlobalError("error.percentage.earncode");
		}
		return valid;
	}

	protected boolean validateEarnCode(AssignmentAccount assignmentAccount) {
		boolean valid = false;
		LOG.debug("Validating EarnCode: " + assignmentAccount.getEarnCode());
		Date date = new Date(Calendar.getInstance().getTimeInMillis());
		EarnCode earnCode = TkServiceLocator.getEarnCodeService().getEarnCode(
				assignmentAccount.getEarnCode(), date);
		if (earnCode != null) {

			valid = true;
			LOG.debug("found earnCode.");
		} else {
			this.putGlobalError("error.existence", "earn code '"
					+ assignmentAccount.getEarnCode() + "'");
		}
		return valid;
	}

	protected boolean validateAccount(AssignmentAccount assignmentAccount) {
		boolean valid = false;
		LOG.debug("Validating Account: " + assignmentAccount.getAccountNbr());
		Collection account = KNSServiceLocator.getBusinessObjectDao().findAll(Account.class);
		Iterator<Account> itr = account.iterator();
		while (itr.hasNext()) {
			Account accountObj = itr.next();
			if (accountObj.getAccountNumber().equals(assignmentAccount.getAccountNbr())) {
				valid = true;
				LOG.debug("found account number.");
			}
		}
		 if(!valid) {
			this.putGlobalError("error.existence", "Account Number '"
					+ assignmentAccount.getAccountNbr() + "'");
		}
		return valid;
	}

	protected boolean validateObjectCode(AssignmentAccount assignmentAccount) {
//		boolean valid = false;
//		LOG.debug("Validating ObjectCode: " + assignmentAccount.getFinObjectCd());
//		Collection objectCode = KNSServiceLocator.getBusinessObjectDao().findAll(ObjectCode.class);
//		Iterator<ObjectCode> itr = objectCode.iterator();
//		while (itr.hasNext()) {
//			ObjectCode objectCodeObj = itr.next();
//			if (objectCodeObj.getObjectId().equals(assignmentAccount.getFinObjectCd())) {
//				valid = true;
//				LOG.debug("found object code.");
//			}
//		}
//		 if(!valid) {
//			this.putGlobalError("error.existence", "Object Code '"
//					+ assignmentAccount.getFinObjectCd() + "'");
//		}
//		return valid;
		return true;
	}

	protected boolean validateSubObjectCode(AssignmentAccount assignmentAccount) {
	//	boolean valid = false;
//		LOG.debug("Validating SubObjectCode: " + assignmentAccount.getFinSubObjCd());
//		Collection subObjectCode = KNSServiceLocator.getBusinessObjectDao().findAll(SubObjectCode.class);
//		Iterator<SubObjectCode> itr = subObjectCode.iterator();
//		while (itr.hasNext()) {
//			SubObjectCode subObjectCodeObj = itr.next();
//			if (subObjectCodeObj.getObjectId().equals(assignmentAccount.getFinSubObjCd())) {
//				valid = true;
//				LOG.debug("found sub object code.");
//			}
//		}
//		 if(!valid) {
//			this.putGlobalError("error.existence", "SubObject Code '"
//					+ assignmentAccount.getFinSubObjCd() + "'");
//		}
//		return valid;
		return true;
	}
	/**
	 * It looks like the method that calls this class doesn't actually care
	 * about the return type.
	 */
	@Override
	protected boolean processCustomSaveDocumentBusinessRules(
			MaintenanceDocument document) {
		boolean valid = false;
		LOG.debug("entering custom validation for DeptLunchRule");
		PersistableBusinessObject pbo = this.getNewBo();
		if (pbo instanceof Assignment) {
			Assignment assignment = (Assignment) pbo;
			if (assignment != null) {
				valid = true;
				valid &= this.validateWorkArea(assignment);
				valid &= this.validateJob(assignment);
				valid &= this.validateTask(assignment);
				valid &= this.validateActiveAccountTotalPercentage(assignment);
				valid &= this.validatePercentagePerEarnCode(assignment);
			}
		}

		return valid;
	}

	@Override
	protected boolean processCustomApproveDocumentBusinessRules(
			MaintenanceDocument document) {
		return super.processCustomApproveDocumentBusinessRules(document);
	}

	@Override
	protected boolean processCustomRouteDocumentBusinessRules(
			MaintenanceDocument document) {
		return super.processCustomRouteDocumentBusinessRules(document);
	}

	@Override
	public boolean processCustomAddCollectionLineBusinessRules(
			MaintenanceDocument document, String collectionName,
			PersistableBusinessObject line) {
		boolean valid = false;
		LOG.debug("entering custom validation for DeptLunchRule");
		PersistableBusinessObject pbo = line;
		if (pbo instanceof AssignmentAccount) {

			AssignmentAccount assignmentAccount = (AssignmentAccount) pbo;
			if (assignmentAccount != null) {
				valid = true;
				valid &= this.validateEarnCode(assignmentAccount);
				valid &= this.validateAccount(assignmentAccount);
				valid &= this.validateObjectCode(assignmentAccount);
				valid &= this.validateSubObjectCode(assignmentAccount);
			}
		}
		return valid;
	}

}