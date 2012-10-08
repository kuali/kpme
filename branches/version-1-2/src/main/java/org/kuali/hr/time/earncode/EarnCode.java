/**
 * Copyright 2004-2012 The Kuali Foundation
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
package org.kuali.hr.time.earncode;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.*;

import org.kuali.hr.core.KPMEConstants;
import org.kuali.hr.time.accrual.AccrualCategory;
import org.kuali.hr.earncodesec.EarnCodeSecurity;
import org.kuali.hr.time.HrBusinessObject;
import org.kuali.hr.time.service.base.TkServiceLocator;
import org.kuali.rice.krad.service.KRADServiceLocator;

public class EarnCode extends HrBusinessObject {
    public static final String CACHE_NAME = KPMEConstants.APPLICATION_NAMESPACE_CODE + "/" + "EarnCode";
    private static final String[] PRIVATE_CACHES_FOR_FLUSH = {EarnCodeSecurity.CACHE_NAME, EarnCode.CACHE_NAME};
    public static final List<String> CACHE_FLUSH = Collections.unmodifiableList(Arrays.asList(PRIVATE_CACHES_FOR_FLUSH));
	/**
     *
     */
	private static final long serialVersionUID = 1L;

	private String hrEarnCodeId;
	private String earnCode;
	private String description;
	
    private Boolean ovtEarnCode;
	private String accrualCategory;
	private BigDecimal inflateMinHours;
	private BigDecimal inflateFactor;
    private Long defaultAmountofTime;
	private boolean history;

	private AccrualCategory accrualCategoryObj;

	private String recordMethod;
    
	public String getRecordMethod() {
		return recordMethod;
	}

	public void setRecordMethod(String recordMethod) {
		this.recordMethod = recordMethod;
	}

	public Long getDefaultAmountofTime() {
		return defaultAmountofTime;
	}

	public void setDefaultAmountofTime(Long defaultAmountofTime) {
		this.defaultAmountofTime = defaultAmountofTime;
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

	public boolean isHistory() {
		return history;
	}

	public void setHistory(boolean history) {
		this.history = history;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getHrEarnCodeId() {
		return hrEarnCodeId;
	}

	public void setHrEarnCodeId(String hrEarnCodeId) {
		this.hrEarnCodeId = hrEarnCodeId;
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
		if(accrualCategoryObj == null && !this.getAccrualCategory().isEmpty()) {
			this.assingAccrualCategoryObj();
		}
		return accrualCategoryObj;
	}
	public void assingAccrualCategoryObj() {
		Map<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("accrualCategory", getAccrualCategory());
		Collection c = KRADServiceLocator.getBusinessObjectService().findMatching(AccrualCategory.class, parameters);
		if(!c.isEmpty()) {
			this.setAccrualCategoryObj((AccrualCategory)c.toArray()[0]);
		}
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

    public Boolean getOvtEarnCode() {
        return ovtEarnCode;
    }

    public void setOvtEarnCode(Boolean ovtEarnCode) {
        this.ovtEarnCode = ovtEarnCode;
    }

    /**
	 * This is used by the timeblock json object.
	 * The purpose of this function is to create a string based on the record_* fields which can be used to render hour / begin(end) time input box
	 * @return String fieldType
	 */
	public String getEarnCodeType() {
//		if(getRecordHours()) {
//			return TkConstants.EARN_CODE_HOUR;
//		}
//		else if(getRecordTime()) {
//			return TkConstants.EARN_CODE_TIME;
//		}
//		else if(getRecordAmount()) {
//			return TkConstants.EARN_CODE_AMOUNT;
//		}
//		else {
//			return "";
//		}
		return this.recordMethod;
	}

	@Override
	public String getUniqueKey() {
		return earnCode;
	}

	@Override
	public String getId() {
		return getHrEarnCodeId();
	}

	@Override
	public void setId(String id) {
		setHrEarnCodeId(id);
	}
	
    public String getEarnCodeKeyForDisplay() {
//    	String unitTime = null;
//    	AccrualCategory acObj = null;
//    	if(this.accrualCategory != null) {
//    		acObj = TkServiceLocator.getAccrualCategoryService().getAccrualCategory(accrualCategory, this.effectiveDate);
//    	}
//    	unitTime = (acObj!= null ? acObj.getUnitOfTime() : this.recordMethod) ;
//        return hrEarnCodeId + ":" + unitTime;
    	return hrEarnCodeId;
    }
    
    public String getEarnCodeValueForDisplay() {
        return earnCode + " : " + description;
    }
}
