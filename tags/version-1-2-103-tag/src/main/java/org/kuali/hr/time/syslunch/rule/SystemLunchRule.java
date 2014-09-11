package org.kuali.hr.time.syslunch.rule;

import java.util.LinkedHashMap;

import org.kuali.hr.time.rule.TkRule;

public class SystemLunchRule extends TkRule {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String tkSystemLunchRuleId;
	private Boolean showLunchButton = false;
	private boolean history;
	private String userPrincipalId;

	@SuppressWarnings({ "rawtypes" })
	@Override
	protected LinkedHashMap toStringMapper() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getTkSystemLunchRuleId() {
		return tkSystemLunchRuleId;
	}

	public void setTkSystemLunchRuleId(String tkSystemLunchRuleId) {
		this.tkSystemLunchRuleId = tkSystemLunchRuleId;
	}

	public boolean isHistory() {
		return history;
	}


	public void setHistory(boolean history) {
		this.history = history;
	}

	public String getUserPrincipalId() {
		return userPrincipalId;
	}

	public void setUserPrincipalId(String userPrincipalId) {
		this.userPrincipalId = userPrincipalId;
	}


	public Boolean getShowLunchButton() {
		return showLunchButton;
	}


	public void setShowLunchButton(Boolean showLunchButton) {
		this.showLunchButton = showLunchButton;
	}


	@Override
	public String getUniqueKey() {
		return "";
	}

	@Override
	public String getId() {
		return getTkSystemLunchRuleId();
	}

	@Override
	public void setId(String id) {
		setTkSystemLunchRuleId(id);
	}

}