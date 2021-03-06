package org.kuali.kpme.edo.admin.web;

import org.kuali.kpme.edo.base.web.EdoForm;



public class EdoChangeTargetPersonForm extends EdoForm {

	private static final long serialVersionUID = -8307585413793442914L;

	private String principalName;
    
    private String targetUrl;
    private String returnUrl;
    
	public String getPrincipalName() {
		return principalName;
	}

	public void setPrincipalName(String principalName) {
		this.principalName = principalName;
	}
	
    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

	public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

}