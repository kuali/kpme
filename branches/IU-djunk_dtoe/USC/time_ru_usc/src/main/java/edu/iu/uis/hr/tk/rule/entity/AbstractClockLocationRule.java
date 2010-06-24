package edu.iu.uis.hr.tk.rule.entity;

/*******************************************************************************
 * DO NOT MODIFY THIS FILE - MODIFY THE MODEL AND REGENERATE
 * Generated on: Fri Dec 28 10:16:42 EST 2007
 * Generated by: HR/bdo version 1.52
/******************************************************************************/

import java.math.BigDecimal;
import java.util.Date;

import edu.iu.uis.hr.EffectiveDatedObject;
import edu.iu.uis.hr.EffectiveSequencedObject;
import edu.iu.uis.hr.EffectiveStatusedObject;
import edu.iu.uis.hr.Timestamp;
import edu.iu.uis.hr.entity.AbstractPersistentDatabaseEntity;

public abstract class AbstractClockLocationRule extends AbstractPersistentDatabaseEntity implements EffectiveDatedObject, EffectiveSequencedObject, EffectiveStatusedObject {
	private String department;
	private BigDecimal workArea;
	private String universityId;
	private BigDecimal employeeRecord;
	private Date effectiveDate;
	private BigDecimal effectiveSequence;
	private boolean active;
	private String ipAddress;
	private String userUniversityId;
	private Timestamp timestamp;

	public AbstractClockLocationRule() {
		super();
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public BigDecimal getWorkArea() {
		return workArea;
	}

	public void setWorkArea(BigDecimal workArea) {
		this.workArea = workArea;
	}

	public String getUniversityId() {
		return universityId;
	}

	public void setUniversityId(String universityId) {
		this.universityId = universityId;
	}

	public BigDecimal getEmployeeRecord() {
		return employeeRecord;
	}

	public void setEmployeeRecord(BigDecimal employeeRecord) {
		this.employeeRecord = employeeRecord;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public BigDecimal getEffectiveSequence() {
		return effectiveSequence;
	}

	public void setEffectiveSequence(BigDecimal effectiveSequence) {
		this.effectiveSequence = effectiveSequence;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getUserUniversityId() {
		return userUniversityId;
	}

	public void setUserUniversityId(String userUniversityId) {
		this.userUniversityId = userUniversityId;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

}