package org.kuali.hr.time.graceperiod.rule;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.LinkedHashMap;

import org.kuali.hr.core.KPMEConstants;
import org.kuali.hr.time.HrBusinessObject;



public class GracePeriodRule extends HrBusinessObject {
    public static final String CACHE_NAME = KPMEConstants.APPLICATION_NAMESPACE_CODE + "/" + "GracePeriodRule";
	private static final long serialVersionUID = 1L;

	private String tkGracePeriodRuleId;
	private BigDecimal hourFactor;
	private String userPrincipalId;


	public boolean isActive() {
	    return active;
	}

	public void setActive(boolean active) {
	    this.active = active;
	}

	public BigDecimal getHourFactor() {
	    return hourFactor;
	}

	public void setHourFactor(BigDecimal hourFactor) {
	    this.hourFactor = hourFactor;
	}


	public String getUserPrincipalId() {
	    return userPrincipalId;
	}


	public void setUserPrincipalId(String userPrincipalId) {
	    this.userPrincipalId = userPrincipalId;
	}


	public Timestamp getTimestamp() {
	    return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
	    this.timestamp = timestamp;
	}


	public String getTkGracePeriodRuleId() {
		return tkGracePeriodRuleId;
	}


	public void setTkGracePeriodRuleId(String tkGracePeriodRuleId) {
		this.tkGracePeriodRuleId = tkGracePeriodRuleId;
	}


	@Override
	public String getUniqueKey() {
		return hourFactor + "";
	}

	@Override
	public String getId() {
		return getTkGracePeriodRuleId();
	}

	@Override
	public void setId(String id) {
		setTkGracePeriodRuleId(id);
	}


}