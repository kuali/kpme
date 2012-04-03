package org.kuali.hr.lm.leavecode;

import java.util.LinkedHashMap;

import org.apache.commons.lang.StringUtils;
import org.kuali.hr.lm.accrual.AccrualCategory;
import org.kuali.hr.lm.leaveplan.LeavePlan;
import org.kuali.hr.time.HrBusinessObject;
import org.kuali.hr.time.earncode.EarnCode;
import org.kuali.hr.time.service.base.TkServiceLocator;

public class LeaveCode extends HrBusinessObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String lmLeaveCodeId;
	private String leavePlan;
	private String eligibleForAccrual;
	private String accrualCategory;
	private String earnCode;
	private String leaveCode;
	private String displayName;
	private String unitOfTime;
	private String fractionalTimeAllowed;
	private String roundingOption;
	private String allowScheduledLeave;
	private String fmla;
	private String workmansComp;
	private Long defaultAmountofTime;
	private Boolean employee;
	private Boolean approver;
	private Boolean departmentAdmin;
	private String allowNegativeAccrualBalance;
	private Boolean history;
	
	private LeavePlan leavePlanObj;
	private EarnCode earnCodeObj;
	private AccrualCategory accrualCategoryObj;

	public EarnCode getEarnCodeObj() {
		return earnCodeObj;
	}

	public void setEarnCodeObj(EarnCode earnCodeObj) {
		this.earnCodeObj = earnCodeObj;
	}

	public AccrualCategory getAccrualCategoryObj() {
		return accrualCategoryObj;
	}

	public void setAccrualCategoryObj(AccrualCategory accrualCategoryObj) {
		this.accrualCategoryObj = accrualCategoryObj;
	}

	public LeavePlan getLeavePlanObj() {
		return leavePlanObj;
	}

	public void setLeavePlanObj(LeavePlan leavePlanObj) {
		this.leavePlanObj = leavePlanObj;
	}

	public String getLmLeaveCodeId() {
		return lmLeaveCodeId;
	}

	public void setLmLeaveCodeId(String lmLeaveCodeId) {
		this.lmLeaveCodeId = lmLeaveCodeId;
	}
	
	public String getLeavePlan() {
		if (this.accrualCategoryObj == null && 
				(!StringUtils.isEmpty(this.accrualCategory) && this.effectiveDate != null)) {		
			accrualCategoryObj =  TkServiceLocator.getAccrualCategoryService().getAccrualCategory(accrualCategory, this.effectiveDate);
		}
		return (accrualCategoryObj != null) ? accrualCategoryObj.getLeavePlan() : "";
	}

	public void setLeavePlan(String leavePlan) {
		this.leavePlan = leavePlan;
	}

	public String getEligibleForAccrual() {
		return eligibleForAccrual;
	}

	public void setEligibleForAccrual(String eligibleForAccrual) {
		this.eligibleForAccrual = eligibleForAccrual;
	}

	public String getAccrualCategory() {
		return accrualCategory;
	}

	public void setAccrualCategory(String accrualCategory) {
		this.accrualCategory = accrualCategory;
	}

	public String getEarnCode() {
		return earnCode;
	}

	public void setEarnCode(String earnCode) {
		this.earnCode = earnCode;
	}

	public String getLeaveCode() {
		return leaveCode;
	}

	public void setLeaveCode(String leaveCode) {
		this.leaveCode = leaveCode;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getUnitOfTime() {
		return unitOfTime;
	}

	public void setUnitOfTime(String unitOfTime) {
		this.unitOfTime = unitOfTime;
	}

	public String getFractionalTimeAllowed() {
		return fractionalTimeAllowed;
	}

	public void setFractionalTimeAllowed(String fractionalTimeAllowed) {
		this.fractionalTimeAllowed = fractionalTimeAllowed;
	}

	public String getRoundingOption() {
		return roundingOption;
	}

	public void setRoundingOption(String roundingOption) {
		this.roundingOption = roundingOption;
	}

	public String getAllowScheduledLeave() {
		return allowScheduledLeave;
	}

	public void setAllowScheduledLeave(String allowScheduledLeave) {
		this.allowScheduledLeave = allowScheduledLeave;
	}

	public String getFmla() {
		return fmla;
	}

	public void setFmla(String fmla) {
		this.fmla = fmla;
	}

	public String getWorkmansComp() {
		return workmansComp;
	}

	public void setWorkmansComp(String workmansComp) {
		this.workmansComp = workmansComp;
	}

	public Long getDefaultAmountofTime() {
		return defaultAmountofTime;
	}

	public void setDefaultAmountofTime(Long defaultAmountofTime) {
		this.defaultAmountofTime = defaultAmountofTime;
	}

	public Boolean getEmployee() {
		return employee;
	}

	public void setEmployee(Boolean employee) {
		this.employee = employee;
	}

	public Boolean getApprover() {
		return approver;
	}

	public void setApprover(Boolean approver) {
		this.approver = approver;
	}

	public Boolean getDepartmentAdmin() {
		return departmentAdmin;
	}

	public void setDepartmentAdmin(Boolean departmentAdmin) {
		this.departmentAdmin = departmentAdmin;
	}

	public Boolean getHistory() {
		return history;
	}

	public void setHistory(Boolean history) {
		this.history = history;
	}

	@Override
	protected String getUniqueKey() {
		String leaveCodeKey = getLeaveCode() +"_"+ getLeavePlan() +"_"+ getAccrualCategory() +"_"+ getEarnCode();
		return leaveCodeKey;
	}

	@Override
	public String getId() {
		return getLmLeaveCodeId();
	}


	@Override
	protected LinkedHashMap toStringMapper() {
		// TODO Auto-generated method stub
		return null;
	}
    
    public String getLeaveCodeKeyForDisplay() {
        return lmLeaveCodeId;
    }
    
    public String getLeaveCodeValueForDisplay() {
        return leaveCode + " : " + displayName;
    }

	@Override
	public void setId(String id) {
		setLmLeaveCodeId(id);
	}

	public String getAllowNegativeAccrualBalance() {
		return allowNegativeAccrualBalance;
	}

	public void setAllowNegativeAccrualBalance(String allowNegativeAccrualBalance) {
		this.allowNegativeAccrualBalance = allowNegativeAccrualBalance;
	}
}