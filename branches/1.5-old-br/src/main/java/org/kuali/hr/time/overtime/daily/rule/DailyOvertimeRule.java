package org.kuali.hr.time.overtime.daily.rule;

import org.kuali.hr.location.Location;
import org.kuali.hr.time.department.Department;
import org.kuali.hr.time.earncode.EarnCode;
import org.kuali.hr.time.earngroup.EarnGroup;
import org.kuali.hr.time.paytype.PayType;
import org.kuali.hr.time.rule.TkRule;
import org.kuali.hr.time.task.Task;
import org.kuali.hr.time.workarea.WorkArea;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.LinkedHashMap;


public class DailyOvertimeRule extends TkRule {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private Long tkDailyOvertimeRuleId;

	private String fromEarnGroup;
	private String earnCode;

	private String location;
	private String paytype;
	private String dept;
	private Long workArea;

	private BigDecimal maxGap;
	private BigDecimal minHours;
	private String userPrincipalId;
	private boolean history;
	private Boolean ovtEarnCode;

	private Long tkWorkAreaId;
	private Long hrDeptId;
	private Long hrLocationId;
	
	private Task taskObj;
	private WorkArea workAreaObj;
	private Department departmentObj;
	private PayType payTypeObj;

	private EarnGroup fromEarnGroupObj;
	private EarnCode earnCodeObj;
	private Location locationObj;


	@SuppressWarnings({ "rawtypes" })
	@Override
	protected LinkedHashMap toStringMapper() {
		return null;
	}

	public Long getTkDailyOvertimeRuleId() {
		return tkDailyOvertimeRuleId;
	}

	public void setTkDailyOvertimeRuleId(Long tkDailyOvertimeRuleId) {
		this.tkDailyOvertimeRuleId = tkDailyOvertimeRuleId;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public BigDecimal getMaxGap() {
		return maxGap;
	}

	public void setMaxGap(BigDecimal maxGap) {
		this.maxGap = maxGap;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public String getUserPrincipalId() {
		return userPrincipalId;
	}

	public void setUserPrincipalId(String userPrincipalId) {
		this.userPrincipalId = userPrincipalId;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Department getDepartmentObj() {
		return departmentObj;
	}

	public void setDepartmentObj(Department departmentObj) {
		this.departmentObj = departmentObj;
	}

	public String getPaytype() {
		return paytype;
	}

	public void setPaytype(String paytype) {
		this.paytype = paytype;
	}

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public Task getTaskObj() {
		return taskObj;
	}

	public void setTaskObj(Task taskObj) {
		this.taskObj = taskObj;
	}

	public WorkArea getWorkAreaObj() {
		return workAreaObj;
	}

	public void setWorkAreaObj(WorkArea workAreaObj) {
		this.workAreaObj = workAreaObj;
	}

	public void setWorkArea(Long workArea) {
		this.workArea = workArea;
	}

	public Long getWorkArea() {
		return workArea;
	}

	public PayType getPayTypeObj() {
		return payTypeObj;
	}

	public void setPayTypeObj(PayType payTypeObj) {
		this.payTypeObj = payTypeObj;
	}

	public String getFromEarnGroup() {
		return fromEarnGroup;
	}

	public void setFromEarnGroup(String fromEarnGroup) {
		this.fromEarnGroup = fromEarnGroup;
	}

	public String getEarnCode() {
		return earnCode;
	}

	public void setEarnCode(String earnCode) {
		this.earnCode = earnCode;
	}

	public BigDecimal getMinHours() {
		return minHours;
	}

	public void setMinHours(BigDecimal minHours) {
		this.minHours = minHours;
	}

	public EarnGroup getFromEarnGroupObj() {
		return fromEarnGroupObj;
	}

	public void setFromEarnGroupObj(EarnGroup fromEarnGroupObj) {
		this.fromEarnGroupObj = fromEarnGroupObj;
	}

	public EarnCode getEarnCodeObj() {
		return earnCodeObj;
	}

	public void setEarnCodeObj(EarnCode earnCodeObj) {
		this.earnCodeObj = earnCodeObj;
	}

	public Location getLocationObj() {
		return locationObj;
	}

	public void setLocationObj(Location locationObj) {
		this.locationObj = locationObj;
	}

	public boolean isHistory() {
		return history;
	}

	public void setHistory(boolean history) {
		this.history = history;
	}

	public Boolean getOvtEarnCode() {
		return ovtEarnCode;
	}

	public void setOvtEarnCode(Boolean ovtEarnCode) {
		this.ovtEarnCode = ovtEarnCode;
	}

	public Long getTkWorkAreaId() {
		return tkWorkAreaId;
	}

	public void setTkWorkAreaId(Long tkWorkAreaId) {
		this.tkWorkAreaId = tkWorkAreaId;
	}

	public Long getHrDeptId() {
		return hrDeptId;
	}

	public void setHrDeptId(Long hrDeptId) {
		this.hrDeptId = hrDeptId;
	}

	public Long getHrLocationId() {
		return hrLocationId;
	}

	public void setHrLocationId(Long hrLocationId) {
		this.hrLocationId = hrLocationId;
	}

	@Override
	protected String getUniqueKey() {
		return location + "_" + dept + "_" + workArea + "_" + paytype;
	}

	@Override
	public Long getId() {
		return getTkDailyOvertimeRuleId();
	}

	@Override
	public void setId(Long id) {
		setTkDailyOvertimeRuleId(id);
	}

}
