package org.kuali.hr.time.paytype;

import org.kuali.hr.core.KPMEConstants;
import org.kuali.hr.time.HrBusinessObject;
import org.kuali.hr.time.earncode.EarnCode;

public class PayType extends HrBusinessObject {
    public static final String CACHE_NAME = KPMEConstants.APPLICATION_NAMESPACE_CODE + "/" + "PayType";
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String hrPayTypeId;
    private String payType;
    private String descr;
    private String regEarnCode;
    /** Used for lookup */
    private String hrEarnCodeId;
    private EarnCode regEarnCodeObj;
    private String history;
    private Boolean ovtEarnCode;

    public EarnCode getRegEarnCodeObj() {
        return regEarnCodeObj;
    }

    public void setRegEarnCodeObj(EarnCode regEarnCodeObj) {
        this.regEarnCodeObj = regEarnCodeObj;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }
    public String getRegEarnCode() {
        return regEarnCode;
    }

    public void setRegEarnCode(String regEarnCode) {
        this.regEarnCode = regEarnCode;
    }
    public String getHrPayTypeId() {
        return hrPayTypeId;
    }


    public void setHrPayTypeId(String hrPayTypeId) {
        this.hrPayTypeId = hrPayTypeId;
    }



    public String getHrEarnCodeId() {
        return hrEarnCodeId;
    }

    public void setHrEarnCodeId(String hrEarnCodeId) {
        this.hrEarnCodeId = hrEarnCodeId;
    }

    @Override
    public String getUniqueKey() {
        return payType;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public Boolean getOvtEarnCode() {
        return ovtEarnCode;
    }

    public void setOvtEarnCode(Boolean ovtEarnCode) {
        this.ovtEarnCode = ovtEarnCode;
    }

    @Override
    public String getId() {
        return getHrPayTypeId();
    }

    @Override
    public void setId(String id) {
        setHrPayTypeId(id);
    }

}