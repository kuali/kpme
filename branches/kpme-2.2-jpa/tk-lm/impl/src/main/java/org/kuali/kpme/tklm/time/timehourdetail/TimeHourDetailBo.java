/**
 * Copyright 2004-2014 The Kuali Foundation
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
package org.kuali.kpme.tklm.time.timehourdetail;

import java.math.BigDecimal;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.kuali.kpme.core.api.util.KpmeUtils;
import org.kuali.kpme.core.util.HrConstants;
import org.kuali.kpme.tklm.api.time.timehourdetail.TimeHourDetail;
import org.kuali.kpme.tklm.api.time.timehourdetail.TimeHourDetailContract;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;
import org.kuali.rice.krad.data.jpa.PortableSequenceGenerator;

@Entity
@Table(name = "TK_HOUR_DETAIL_T")
public class TimeHourDetailBo extends PersistableBusinessObjectBase implements TimeHourDetailContract {

    private static final long serialVersionUID = 1L;

    @PortableSequenceGenerator(name = "TK_HOUR_DETAIL_S")
    @GeneratedValue(generator = "TK_HOUR_DETAIL_S")
    @Id
    @Column(name = "TK_HOUR_DETAIL_ID", length = 60)
    private String tkTimeHourDetailId;

    @Column(name = "TK_TIME_BLOCK_ID", length = 60)
    private String tkTimeBlockId;

    @Column(name = "EARN_CODE", nullable = false, length = 15)
    private String earnCode;

    @Column(name = "HOURS", nullable = false, precision = 5, scale = 2)
    private BigDecimal hours = HrConstants.BIG_DECIMAL_SCALED_ZERO;

    @Column(name = "AMOUNT", nullable = false, precision = 6, scale = 2)
    private BigDecimal amount = HrConstants.BIG_DECIMAL_SCALED_ZERO;

    public TimeHourDetailBo() {
    }

    protected TimeHourDetailBo(TimeHourDetailBo t) {
        // All of the following are Immutable, be aware if new fields  
        // are added.  
        this.tkTimeHourDetailId = t.tkTimeHourDetailId;
        this.tkTimeBlockId = t.tkTimeBlockId;
        this.earnCode = t.earnCode;
        this.hours = t.hours;
        this.amount = t.amount;
    }

    public TimeHourDetailBo copy() {
        return new TimeHourDetailBo(this);
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
        if (hours != null) {
            this.hours = hours.setScale(HrConstants.BIG_DECIMAL_SCALE, HrConstants.BIG_DECIMAL_SCALE_ROUNDING);
        } else {
            this.hours = hours;
        }
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        if (amount != null) {
            this.amount = amount.setScale(HrConstants.BIG_DECIMAL_SCALE, HrConstants.BIG_DECIMAL_SCALE_ROUNDING);
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

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        TimeHourDetailBo timeHourDetail = (TimeHourDetailBo) obj;
        return new EqualsBuilder().append(earnCode, timeHourDetail.earnCode).append(KpmeUtils.nullSafeCompare(amount, timeHourDetail.amount), 0).append(KpmeUtils.nullSafeCompare(hours, timeHourDetail.hours), 0).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31).append(earnCode).append(amount).append(hours).toHashCode();
    }

    public static TimeHourDetailBo from(TimeHourDetail im) {
        TimeHourDetailBo tb = new TimeHourDetailBo();
        tb.setTkTimeBlockId(im.getTkTimeBlockId());
        tb.setTkTimeHourDetailId(im.getTkTimeHourDetailId());
        tb.setHours(im.getHours());
        tb.setAmount(im.getAmount());
        tb.setEarnCode(im.getEarnCode());
        tb.setObjectId(im.getObjectId());
        tb.setVersionNumber(im.getVersionNumber());
        return tb;
    }

    public static TimeHourDetail to(TimeHourDetailBo bo) {
        if (bo == null) {
            return null;
        }
        return TimeHourDetail.Builder.create(bo).build();
    }
}
