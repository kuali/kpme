package org.kuali.hr.time.graceperiod.rule;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.LinkedHashMap;

import org.kuali.hr.time.rule.TkRule;



public class GracePeriodRule extends TkRule {
    
	private static final long serialVersionUID = 1L;

	private Long tkGracePeriodRuleId;
	private Date effDt;
	private boolean active;
	private BigDecimal hourFactor;
	private String userPrincipalId;
	private Timestamp timestamp; 
	
	protected LinkedHashMap<String,Object> toStringMapper() {
		LinkedHashMap<String, Object> toStringMap = new LinkedHashMap<String,Object>();
		toStringMap.put("effDt", effDt);
		toStringMap.put("hourFactor", hourFactor);
		return toStringMap;
	}


	public Date getEffDt() {
	    return effDt;
	}

	public void setEffDt(Date effDt) {
	    this.effDt = effDt;
	}

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


	public Long getTkGracePeriodRuleId() {
		return tkGracePeriodRuleId;
	}


	public void setTkGracePeriodRuleId(Long tkGracePeriodRuleId) {
		this.tkGracePeriodRuleId = tkGracePeriodRuleId;
	}


}