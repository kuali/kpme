package org.kuali.hr.time.earngroup;

import java.util.LinkedHashMap;

import org.apache.commons.lang.StringUtils;
import org.kuali.hr.time.earncode.EarnCode;
import org.kuali.hr.time.service.base.TkServiceLocator;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

public class EarnGroupDefinition extends PersistableBusinessObjectBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8463674251885306591L;

	private String hrEarnGroupDefId;

	private String earnCode;

	private String hrEarnGroupId;

    private EarnCode earnCodeObj;

    // this is for the maintenance screen
    private String earnCodeDesc;

	public String getEarnCode() {
		return earnCode;
	}

	public void setEarnCode(String earnCode) {
		this.earnCode = earnCode;
	}

	public String getHrEarnGroupDefId() {
		return hrEarnGroupDefId;
	}

	public void setHrEarnGroupDefId(String hrEarnGroupDefId) {
		this.hrEarnGroupDefId = hrEarnGroupDefId;
	}

	public String getHrEarnGroupId() {
		return hrEarnGroupId;
	}

	public void setHrEarnGroupId(String hrEarnGroupId) {
		this.hrEarnGroupId = hrEarnGroupId;
	}

	public EarnCode getEarnCodeObj() {
		return earnCodeObj;
	}

	public void setEarnCodeObj(EarnCode earnCodeObj) {
		this.earnCodeObj = earnCodeObj;
	}
	
	// this is for the maintenance screen
	public String getEarnCodeDesc() {
		EarnCode earnCode = TkServiceLocator.getEarnCodeService().getEarnCode(this.earnCode, new java.sql.Date(System.currentTimeMillis()));
		
		if(earnCode != null && StringUtils.isNotBlank(earnCode.getDescription())) {
			return earnCode.getDescription();
		}
		return "";
	}
}