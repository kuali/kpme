package org.kuali.hr.time.timeblock;

import org.kuali.hr.time.util.TkConstants;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.math.BigDecimal;

public class TimeHourDetail extends PersistableBusinessObjectBase {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private String tkTimeHourDetailId;
	private String tkTimeBlockId;
	private String earnCode;
	private BigDecimal hours = TkConstants.BIG_DECIMAL_SCALED_ZERO;
	private BigDecimal amount = TkConstants.BIG_DECIMAL_SCALED_ZERO;

    public TimeHourDetail() {
    }

	public String getEarnCode() {
		return earnCode;
	}

	public void setEarnCode(String earnCode) {
		this.earnCode = earnCode;
	}

	public BigDecimal getHours() {
		return hours;
	}

	public void setHours(BigDecimal hours) {
		if(hours != null){
			this.hours = hours.setScale(TkConstants.BIG_DECIMAL_SCALE, TkConstants.BIG_DECIMAL_SCALE_ROUNDING);
		} else {
			this.hours = hours;
		}
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		if(amount != null){
			this.amount = amount.setScale(TkConstants.BIG_DECIMAL_SCALE, TkConstants.BIG_DECIMAL_SCALE_ROUNDING);
		} else {
			this.amount = amount;
		}
	}

	public String getTkTimeBlockId() {
		return tkTimeBlockId;
	}

	public void setTkTimeBlockId(String tkTimeBlockId) {
		this.tkTimeBlockId = tkTimeBlockId;
	}
	public void setTkTimeHourDetailId(String tkTimeHourDetailId) {
		this.tkTimeHourDetailId = tkTimeHourDetailId;
	}

	public String getTkTimeHourDetailId() {
		return tkTimeHourDetailId;
	}

    protected TimeHourDetail(TimeHourDetail t) {
        // All of the following are Immutable, be aware if new fields
        // are added.
        this.tkTimeHourDetailId = t.tkTimeHourDetailId;
        this.tkTimeBlockId = t.tkTimeBlockId;
        this.earnCode = t.earnCode;
        this.hours = t.hours;
        this.amount = t.amount;
    }

    public TimeHourDetail copy() {
        return new TimeHourDetail(this);
    }
}