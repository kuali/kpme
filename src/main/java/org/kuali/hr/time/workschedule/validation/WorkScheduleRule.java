package org.kuali.hr.time.workschedule.validation;

import org.kuali.rice.kns.maintenance.rules.MaintenanceDocumentRuleBase;

public class WorkScheduleRule extends MaintenanceDocumentRuleBase {

//	protected boolean validateWorkArea(WorkSchedule workSchedule ) {
//		boolean valid = false;
//		LOG.debug("Validating workarea: " + workSchedule.getWorkArea());
//		WorkArea workArea = KNSServiceLocator.getBusinessObjectService()
//				.findBySinglePrimaryKey(WorkArea.class, workSchedule.getWorkArea());
//		if (workArea != null) {
//
//			valid = true;
//			LOG.debug("found workarea.");
//		} else {
//			this.putFieldError("workArea", "error.existence", "workarea '"
//					+ workSchedule.getWorkArea()+ "'");
//		}
//		return valid;
//	}
//
//	protected boolean validateDepartment(WorkSchedule workSchedule) {
//		boolean valid = false;
//
//		if (workSchedule.getDept().equals(TkConstants.WILDCARD_CHARACTER)) {
//			valid = true;
//		} else {
//			LOG.debug("Validating dept: " + workSchedule.getDept());
//			// TODO: We may need a full DAO that handles bo lookups at some point,
//			// but we can use the provided one:
//			Department dept = KNSServiceLocator.getBusinessObjectService()
//					.findBySinglePrimaryKey(Department.class, workSchedule.getDept());
//			if (dept != null) {
//				valid = true;
//				LOG.debug("found department.");
//			} else {
//				this.putFieldError("deptId", "error.existence", "department '"
//						+ workSchedule.getDept() + "'");
//			}
//		}
//		return valid;
//	}
//
//	/**
//	 * It looks like the method that calls this class doesn't actually care
//	 * about the return type.
//	 */
//	@Override
//	protected boolean processCustomRouteDocumentBusinessRules(
//			MaintenanceDocument document) {
//		boolean valid = false;
//
//		LOG.debug("entering custom validation for WorkSchedule");
//		PersistableBusinessObject pbo = this.getNewBo();
//		if (pbo instanceof WorkSchedule) {
//			WorkSchedule workSchedule = (WorkSchedule) pbo;
//
//			if (workSchedule != null) {
//				valid = true;
//				valid &= this.validateWorkArea(workSchedule);
//				valid &= this.validateDepartment(workSchedule);
//
//			}
//		}
//		return valid;
//	}
//
//	@Override
//	protected boolean processCustomApproveDocumentBusinessRules(
//			MaintenanceDocument document) {
//		return super.processCustomApproveDocumentBusinessRules(document);
//	}
//
//	@Override
//	protected boolean processCustomRouteDocumentBusinessRules(
//			MaintenanceDocument document) {
//		return super.processCustomRouteDocumentBusinessRules(document);
//	}
}