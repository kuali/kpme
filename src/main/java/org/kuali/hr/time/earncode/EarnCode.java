package org.kuali.hr.time.earncode;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.LinkedHashMap;

import org.kuali.hr.time.accrual.AccrualCategory;
import org.kuali.rice.kns.bo.PersistableBusinessObjectBase;

public class EarnCode extends PersistableBusinessObjectBase {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	private Long tkEarnCodeId;
	private String earnCode;
	private String description;
	private Boolean recordHours;
	private Boolean recordTime;
	private Boolean recordAmount;
	private String accrualCategory;
	private BigDecimal inflateMinHours;
	private BigDecimal inflateFactor;
	
	private Timestamp timestamp;
	private Date effectiveDate;
	private boolean active;
	
	private AccrualCategory accrualCategoryObj;

	@SuppressWarnings("unchecked")
	@Override
	protected LinkedHashMap toStringMapper() {
		return null;
	}

	public String getEarnCode() {
		return earnCode;
	}

	public void setEarnCode(String earnCode) {
		this.earnCode = earnCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Long getTkEarnCodeId() {
		return tkEarnCodeId;
	}

	public void setTkEarnCodeId(Long tkEarnCodeId) {
		this.tkEarnCodeId = tkEarnCodeId;
	}

	public Boolean getRecordHours() {
		return recordHours;
	}

	public void setRecordHours(Boolean recordHours) {
		this.recordHours = recordHours;
	}

	public Boolean getRecordTime() {
		return recordTime;
	}

	public void setRecordTime(Boolean recordTime) {
		this.recordTime = recordTime;
	}

	public Boolean getRecordAmount() {
		return recordAmount;
	}

	public void setRecordAmount(Boolean recordAmount) {
		this.recordAmount = recordAmount;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public String getAccrualCategory() {
		return accrualCategory;
	}

	public void setAccrualCategory(String accrualCategory) {
		this.accrualCategory = accrualCategory;
	}

	public AccrualCategory getAccrualCategoryObj() {
		return accrualCategoryObj;
	}

	public void setAccrualCategoryObj(AccrualCategory accrualCategoryObj) {
		this.accrualCategoryObj = accrualCategoryObj;
	}

	public BigDecimal getInflateMinHours() {
		return inflateMinHours;
	}

	public void setInflateMinHours(BigDecimal inflateMinHours) {
		this.inflateMinHours = inflateMinHours;
	}

	public BigDecimal getInflateFactor() {
		return inflateFactor;
	}

	public void setInflateFactor(BigDecimal inflateFactor) {
		this.inflateFactor = inflateFactor;
	}

}
