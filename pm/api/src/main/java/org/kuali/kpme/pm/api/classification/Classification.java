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
package org.kuali.kpme.pm.api.classification;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import javax.xml.bind.annotation.*;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.kuali.kpme.core.api.groupkey.HrGroupKey;
import org.kuali.kpme.core.api.groupkey.HrGroupKeyContract;
import org.kuali.kpme.core.api.mo.EffectiveKey;
import org.kuali.kpme.core.api.mo.EffectiveKeyContract;
import org.kuali.kpme.pm.api.classification.duty.ClassificationDuty;
import org.kuali.kpme.pm.api.classification.duty.ClassificationDutyContract;
import org.kuali.kpme.pm.api.classification.flag.ClassificationFlag;
import org.kuali.kpme.pm.api.classification.flag.ClassificationFlagContract;
import org.kuali.kpme.pm.api.classification.qual.ClassificationQualification;
import org.kuali.kpme.pm.api.classification.qual.ClassificationQualificationContract;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.core.api.mo.ModelObjectUtils;
import org.w3c.dom.Element;

@XmlRootElement(name = Classification.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = Classification.Constants.TYPE_NAME, propOrder = {
    Classification.Elements.POOL_ELIGIBLE,
    Classification.Elements.POSITION_TYPE,
    Classification.Elements.POSITION_REPORT_GROUP,
    Classification.Elements.LEAVE_ELIGIBLE,
    Classification.Elements.BENEFITS_ELIGIBLE,
    Classification.Elements.CLASSIFICATION_TITLE,
    Classification.Elements.POSITION_CLASS,
    Classification.Elements.PERCENT_TIME,
    Classification.Elements.SALARY_GROUP,
    Classification.Elements.TENURE_ELIGIBLE,
    Classification.Elements.EXTERNAL_REFERENCE,
    Classification.Elements.QUALIFICATION_LIST,
    Classification.Elements.PM_POSITION_CLASS_ID,
    Classification.Elements.FLAG_LIST,
    Classification.Elements.DUTY_LIST,
    Classification.Elements.LEAVE_PLAN,
    Classification.Elements.PAY_GRADE,
    CoreConstants.CommonElements.VERSION_NUMBER,
    CoreConstants.CommonElements.OBJECT_ID,
    Classification.Elements.ACTIVE,
    Classification.Elements.ID,
    Classification.Elements.EFFECTIVE_LOCAL_DATE,
    Classification.Elements.CREATE_TIME,
    Classification.Elements.USER_PRINCIPAL_ID,
    Classification.Elements.GROUP_KEY_CODE_SET,
    Classification.Elements.GROUP_KEY_SET,
    CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class Classification extends AbstractDataTransferObject implements ClassificationContract {

	private static final long serialVersionUID = 3022823678756071188L;
	
    @XmlElement(name = Elements.EFFECTIVE_KEY_SET, required = false)
    private final Set<EffectiveKey> effectiveKeySet;

    @XmlElement(name = Elements.POOL_ELIGIBLE, required = false)
    private final String poolEligible;
    @XmlElement(name = Elements.POSITION_TYPE, required = false)
    private final String positionType;
    @XmlElement(name = Elements.POSITION_REPORT_GROUP, required = false)
    private final String positionReportGroup;
    @XmlElement(name = Elements.LEAVE_ELIGIBLE, required = false)
    private final String leaveEligible;
    @XmlElement(name = Elements.BENEFITS_ELIGIBLE, required = false)
    private final String benefitsEligible;
    @XmlElement(name = Elements.CLASSIFICATION_TITLE, required = false)
    private final String classificationTitle;
    @XmlElement(name = Elements.POSITION_CLASS, required = false)
    private final String positionClass;
    @XmlElement(name = Elements.PERCENT_TIME, required = false)
    private final BigDecimal percentTime;
    @XmlElement(name = Elements.SALARY_GROUP, required = false)
    private final String salaryGroup;
    @XmlElement(name = Elements.TENURE_ELIGIBLE, required = false)
    private final String tenureEligible;
    @XmlElement(name = Elements.EXTERNAL_REFERENCE, required = false)
    private final String externalReference;
    @XmlElementWrapper(name = Elements.QUALIFICATION_LIST, required = false)
    @XmlElement(name = Elements.QUALIFICATION, required = false)
    private final List<ClassificationQualification> qualificationList;
    @XmlElement(name = Elements.PM_POSITION_CLASS_ID, required = false)
    private final String pmPositionClassId;
    @XmlElementWrapper(name = Elements.FLAG_LIST, required = false)
    @XmlElement(name = Elements.FLAG, required = false)
    private final List<ClassificationFlag> flagList;
    @XmlElementWrapper(name = Elements.DUTY_LIST, required = false)
    @XmlElement(name = Elements.DUTY, required = false)
    private final List<ClassificationDuty> dutyList;
    @XmlElement(name = Elements.LEAVE_PLAN, required = false)
    private final String leavePlan;
    @XmlElement(name = Elements.PAY_GRADE, required = false)
    private final String payGrade;
    @XmlElement(name = CoreConstants.CommonElements.VERSION_NUMBER, required = false)
    private final Long versionNumber;
    @XmlElement(name = CoreConstants.CommonElements.OBJECT_ID, required = false)
    private final String objectId;
    @XmlElement(name = Elements.ACTIVE, required = false)
    private final boolean active;
    @XmlElement(name = Elements.ID, required = false)
    private final String id;
    @XmlElement(name = Elements.EFFECTIVE_LOCAL_DATE, required = false)
    private final LocalDate effectiveLocalDate;
    @XmlElement(name = Elements.CREATE_TIME, required = false)
    private final DateTime createTime;
    @XmlElement(name = Elements.USER_PRINCIPAL_ID, required = false)
    private final String userPrincipalId;


    @XmlElement(name = Elements.GROUP_KEY_CODE_SET, required = false)
    private final Set<String> groupKeyCodeSet;
    @XmlElement(name = Elements.GROUP_KEY_SET, required = false)
    private final Set<HrGroupKey> groupKeySet;
    @SuppressWarnings("unused")


    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * Private constructor used only by JAXB.
     * 
     */
    private Classification() {
        this.effectiveKeySet = null;

        this.poolEligible = null;
        this.positionType = null;
        this.positionReportGroup = null;
        this.leaveEligible = null;
        this.benefitsEligible = null;
        this.classificationTitle = null;
        this.positionClass = null;
        this.percentTime = null;
        this.salaryGroup = null;
        this.tenureEligible = null;
        this.externalReference = null;
        this.qualificationList = null;
        this.pmPositionClassId = null;
        this.flagList = null;
        this.dutyList = null;
        this.leavePlan = null;
        this.payGrade = null;
        this.versionNumber = null;
        this.objectId = null;
        this.active = false;
        this.id = null;
        this.effectiveLocalDate = null;
        this.createTime = null;
        this.userPrincipalId = null;
        this.groupKeySet = null;
        this.groupKeyCodeSet = null;
    }

    private Classification(Builder builder) {
        this.effectiveKeySet = ModelObjectUtils.<EffectiveKey>buildImmutableCopy(builder.getEffectiveKeySet());
        this.poolEligible = builder.getPoolEligible();
        this.positionType = builder.getPositionType();
        this.positionReportGroup = builder.getPositionReportGroup();
        this.leaveEligible = builder.getLeaveEligible();
        this.benefitsEligible = builder.getBenefitsEligible();
        this.classificationTitle = builder.getClassificationTitle();
        this.positionClass = builder.getPositionClass();
        this.percentTime = builder.getPercentTime();
        this.salaryGroup = builder.getSalaryGroup();
        this.tenureEligible = builder.getTenureEligible();
        this.externalReference = builder.getExternalReference();
        this.qualificationList = ModelObjectUtils.<ClassificationQualification>buildImmutableCopy(builder.getQualificationList());
        this.pmPositionClassId = builder.getPmPositionClassId();
        this.flagList = ModelObjectUtils.<ClassificationFlag>buildImmutableCopy(builder.getFlagList());
        this.dutyList = ModelObjectUtils.<ClassificationDuty>buildImmutableCopy(builder.getDutyList());
        this.leavePlan = builder.getLeavePlan();
        this.payGrade = builder.getPayGrade();
        this.versionNumber = builder.getVersionNumber();
        this.objectId = builder.getObjectId();
        this.active = builder.isActive();
        this.id = builder.getId();
        this.effectiveLocalDate = builder.getEffectiveLocalDate();
        this.createTime = builder.getCreateTime();
        this.userPrincipalId = builder.getUserPrincipalId();
        this.groupKeyCodeSet = builder.getGroupKeyCodeSet();
        this.groupKeySet = ModelObjectUtils.<HrGroupKey>buildImmutableCopy(builder.getGroupKeySet());
    }

    @Override
    public Set<EffectiveKey> getEffectiveKeySet() {
        return this.effectiveKeySet;
    }

    @Override
    public String getPoolEligible() {
        return this.poolEligible;
    }

    @Override
    public String getPositionType() {
        return this.positionType;
    }

    @Override
    public String getPositionReportGroup() {
        return this.positionReportGroup;
    }

    @Override
    public String getLeaveEligible() {
        return this.leaveEligible;
    }

    @Override
    public String getBenefitsEligible() {
        return this.benefitsEligible;
    }

    @Override
    public String getClassificationTitle() {
        return this.classificationTitle;
    }

    @Override
    public String getPositionClass() {
        return this.positionClass;
    }


    @Override
    public BigDecimal getPercentTime() {
        return this.percentTime;
    }

    @Override
    public String getSalaryGroup() {
        return this.salaryGroup;
    }

    @Override
    public String getTenureEligible() {
        return this.tenureEligible;
    }

    @Override
    public String getExternalReference() {
        return this.externalReference;
    }

    @Override
    public List<ClassificationQualification> getQualificationList() {
        return this.qualificationList;
    }

    @Override
    public String getPmPositionClassId() {
        return this.pmPositionClassId;
    }


    @Override
    public List<ClassificationFlag> getFlagList() {
        return this.flagList;
    }

    @Override
    public List<ClassificationDuty> getDutyList() {
        return this.dutyList;
    }

    @Override
    public String getLeavePlan() {
        return this.leavePlan;
    }

    @Override
    public String getPayGrade() {
        return this.payGrade;
    }

    @Override
    public Long getVersionNumber() {
        return this.versionNumber;
    }

    @Override
    public String getObjectId() {
        return this.objectId;
    }

    @Override
    public boolean isActive() {
        return this.active;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public LocalDate getEffectiveLocalDate() {
        return this.effectiveLocalDate;
    }

    @Override
    public DateTime getCreateTime() {
        return this.createTime;
    }

    @Override
    public String getUserPrincipalId() {
        return this.userPrincipalId;
    }

    @Override
    public Set<String> getGroupKeyCodeSet() {
        return this.groupKeyCodeSet;
    }

    @Override
    public Set<HrGroupKey> getGroupKeySet() {
        return this.groupKeySet;
    }

    /**
     * A builder which can be used to construct {@link Classification} instances.  Enforces the constraints of the {@link ClassificationContract}.
     * 
     */
    public final static class Builder implements Serializable, ClassificationContract, ModelBuilder {

		private static final long serialVersionUID = -5298550128140145929L;
		
        private Set<EffectiveKey.Builder> effectiveKeySet;
        private String poolEligible;
        private String positionType;
        private String positionReportGroup;
        private String leaveEligible;
        private String benefitsEligible;
        private String classificationTitle;
        private String positionClass;
        private BigDecimal percentTime;
        private String salaryGroup;
        private String tenureEligible;
        private String externalReference;
        private List<ClassificationQualification.Builder> qualificationList;
        private String pmPositionClassId;
        private List<ClassificationFlag.Builder> flagList;
        private List<ClassificationDuty.Builder> dutyList;
        private String leavePlan;
        private String payGrade;
        private Long versionNumber;
        private String objectId;
        private boolean active;
        private String id;
        private LocalDate effectiveLocalDate;
        private DateTime createTime;
        private String userPrincipalId;
        private Set<String> groupKeyCodeSet;
        private Set<HrGroupKey.Builder> groupKeySet;

        
        private static final ModelObjectUtils.Transformer<ClassificationQualificationContract, ClassificationQualification.Builder> toClassificationQualificationBuilder = new ModelObjectUtils.Transformer<ClassificationQualificationContract, ClassificationQualification.Builder>() {
			public ClassificationQualification.Builder transform(ClassificationQualificationContract input) {
				return ClassificationQualification.Builder.create(input);
			}
		};
       
		private static final ModelObjectUtils.Transformer<ClassificationFlagContract, ClassificationFlag.Builder> toClassificationFlagBuilder = new ModelObjectUtils.Transformer<ClassificationFlagContract, ClassificationFlag.Builder>() {
			public ClassificationFlag.Builder transform(ClassificationFlagContract input) {
				return ClassificationFlag.Builder.create(input);
			}
		};
		
		private static final ModelObjectUtils.Transformer<ClassificationDutyContract, ClassificationDuty.Builder> toClassificationDutyBuilder = new ModelObjectUtils.Transformer<ClassificationDutyContract, ClassificationDuty.Builder>() {
			public ClassificationDuty.Builder transform(ClassificationDutyContract input) {
				return ClassificationDuty.Builder.create(input);
			}
		};

        private static final ModelObjectUtils.Transformer<EffectiveKeyContract, EffectiveKey.Builder> toEffectiveKeyBuilder
                = new ModelObjectUtils.Transformer<EffectiveKeyContract, EffectiveKey.Builder>() {
            public EffectiveKey.Builder transform(EffectiveKeyContract input) {
                return EffectiveKey.Builder.create(input);
            }
        };

        private static final ModelObjectUtils.Transformer<HrGroupKeyContract, HrGroupKey.Builder> toHrGroupKeyBuilder
                = new ModelObjectUtils.Transformer<HrGroupKeyContract, HrGroupKey.Builder>() {
            public HrGroupKey.Builder transform(HrGroupKeyContract input) {
                return HrGroupKey.Builder.create(input);
            }
        };

        private Builder() {
            // TODO modify this constructor as needed to pass any required values and invoke the appropriate 'setter' methods
        }

        public static Builder create() {

            return new Builder();
        }

        public static Builder create(ClassificationContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }

            Builder builder = create();
            builder.setEffectiveKeySet(ModelObjectUtils.transformSet(contract.getEffectiveKeySet(), toEffectiveKeyBuilder));

            builder.setPoolEligible(contract.getPoolEligible());
            builder.setPositionType(contract.getPositionType());
            builder.setPositionReportGroup(contract.getPositionReportGroup());
            builder.setLeaveEligible(contract.getLeaveEligible());
            builder.setBenefitsEligible(contract.getBenefitsEligible());
            builder.setClassificationTitle(contract.getClassificationTitle());
            builder.setPositionClass(contract.getPositionClass());
            builder.setPercentTime(contract.getPercentTime());
            builder.setSalaryGroup(contract.getSalaryGroup());
            builder.setTenureEligible(contract.getTenureEligible());
            builder.setExternalReference(contract.getExternalReference());
            builder.setQualificationList(ModelObjectUtils.transform(contract.getQualificationList(), toClassificationQualificationBuilder));
            builder.setPmPositionClassId(contract.getPmPositionClassId());
            builder.setFlagList(ModelObjectUtils.transform(contract.getFlagList(), toClassificationFlagBuilder));
            builder.setDutyList(ModelObjectUtils.transform(contract.getDutyList(), toClassificationDutyBuilder));
            builder.setLeavePlan(contract.getLeavePlan());
            builder.setPayGrade(contract.getPayGrade());
            builder.setVersionNumber(contract.getVersionNumber());
            builder.setObjectId(contract.getObjectId());
            builder.setActive(contract.isActive());
            builder.setId(contract.getId());
            builder.setEffectiveLocalDate(contract.getEffectiveLocalDate());
            builder.setCreateTime(contract.getCreateTime());
            builder.setUserPrincipalId(contract.getUserPrincipalId());
            builder.setGroupKeyCodeSet(contract.getGroupKeyCodeSet());
            builder.setGroupKeySet(ModelObjectUtils.transformSet(contract.getGroupKeySet(), toHrGroupKeyBuilder));
            return builder;
        }

        public Classification build() {
            return new Classification(this);
        }


        @Override
        public Set<EffectiveKey.Builder> getEffectiveKeySet() {
            return this.effectiveKeySet;
        }

        @Override
        public String getPoolEligible() {
            return this.poolEligible;
        }

        @Override
        public String getPositionType() {
            return this.positionType;
        }

        @Override
        public String getPositionReportGroup() {
            return this.positionReportGroup;
        }

        @Override
        public String getLeaveEligible() {
            return this.leaveEligible;
        }

        @Override
        public String getBenefitsEligible() {
            return this.benefitsEligible;
        }

        @Override
        public String getClassificationTitle() {
            return this.classificationTitle;
        }

        @Override
        public String getPositionClass() {
            return this.positionClass;
        }


        @Override
        public BigDecimal getPercentTime() {
            return this.percentTime;
        }

        @Override
        public String getSalaryGroup() {
            return this.salaryGroup;
        }

        @Override
        public String getTenureEligible() {
            return this.tenureEligible;
        }

        @Override
        public String getExternalReference() {
            return this.externalReference;
        }

        @Override
        public List<ClassificationQualification.Builder> getQualificationList() {
            return this.qualificationList;
        }

        @Override
        public String getPmPositionClassId() {
            return this.pmPositionClassId;
        }

        @Override
        public List<ClassificationFlag.Builder> getFlagList() {
            return this.flagList;
        }

        @Override
        public List<ClassificationDuty.Builder> getDutyList() {
            return this.dutyList;
        }

        @Override
        public String getLeavePlan() {
            return this.leavePlan;
        }

        @Override
        public String getPayGrade() {
            return this.payGrade;
        }

        @Override
        public Long getVersionNumber() {
            return this.versionNumber;
        }

        @Override
        public String getObjectId() {
            return this.objectId;
        }

        @Override
        public boolean isActive() {
            return this.active;
        }

        @Override
        public String getId() {
            return this.id;
        }

        @Override
        public LocalDate getEffectiveLocalDate() {
            return this.effectiveLocalDate;
        }

        @Override
        public DateTime getCreateTime() {
            return this.createTime;
        }

        @Override
        public String getUserPrincipalId() {
            return this.userPrincipalId;
        }

        @Override
        public Set<String> getGroupKeyCodeSet() {
            return this.groupKeyCodeSet;
        }

        @Override
        public Set<HrGroupKey.Builder> getGroupKeySet() {
            return this.groupKeySet;
        }

        public void setEffectiveKeySet(Set<EffectiveKey.Builder> effectiveKeySet) {

            this.effectiveKeySet = effectiveKeySet;
        }

        public void setPoolEligible(String poolEligible) {

            this.poolEligible = poolEligible;
        }

        public void setPositionType(String positionType) {

            this.positionType = positionType;
        }

        public void setPositionReportGroup(String positionReportGroup) {

            this.positionReportGroup = positionReportGroup;
        }

        public void setLeaveEligible(String leaveEligible) {

            this.leaveEligible = leaveEligible;
        }

        public void setBenefitsEligible(String benefitsEligible) {

            this.benefitsEligible = benefitsEligible;
        }

        public void setClassificationTitle(String classificationTitle) {

            this.classificationTitle = classificationTitle;
        }

        public void setPositionClass(String positionClass) {

            this.positionClass = positionClass;
        }

        public void setPercentTime(BigDecimal percentTime) {

            this.percentTime = percentTime;
        }

        public void setSalaryGroup(String salaryGroup) {

            this.salaryGroup = salaryGroup;
        }

        public void setTenureEligible(String tenureEligible) {

            this.tenureEligible = tenureEligible;
        }

        public void setExternalReference(String externalReference) {

            this.externalReference = externalReference;
        }

        public void setQualificationList(List<ClassificationQualification.Builder> qualificationList) {

            this.qualificationList = qualificationList;
        }

        public void setPmPositionClassId(String pmPositionClassId) {

            this.pmPositionClassId = pmPositionClassId;
        }

        public void setFlagList(List<ClassificationFlag.Builder> flagList) {

            this.flagList = flagList;
        }

        public void setDutyList(List<ClassificationDuty.Builder> dutyList) {

            this.dutyList = dutyList;
        }

        public void setLeavePlan(String leavePlan) {

            this.leavePlan = leavePlan;
        }

        public void setPayGrade(String payGrade) {

            this.payGrade = payGrade;
        }

        public void setVersionNumber(Long versionNumber) {

            this.versionNumber = versionNumber;
        }

        public void setObjectId(String objectId) {

            this.objectId = objectId;
        }

        public void setActive(boolean active) {

            this.active = active;
        }

        public void setId(String id) {

            this.id = id;
        }

        public void setEffectiveLocalDate(LocalDate effectiveLocalDate) {

            this.effectiveLocalDate = effectiveLocalDate;
        }

        public void setCreateTime(DateTime createTime) {

            this.createTime = createTime;
        }

        public void setUserPrincipalId(String userPrincipalId) {

            this.userPrincipalId = userPrincipalId;
        }

        public void setGroupKeyCodeSet(Set<String> groupKeyCodeSet) {

            this.groupKeyCodeSet = groupKeyCodeSet;
        }

        public void setGroupKeySet(Set<HrGroupKey.Builder> groupKeySet) {

            this.groupKeySet = groupKeySet;
        }
    }


    /**
     * Defines some internal constants used on this class.
     * 
     */
    static class Constants {

        final static String ROOT_ELEMENT_NAME = "classification";
        final static String TYPE_NAME = "ClassificationType";

    }


    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     * 
     */
    static class Elements {

        final static String EFFECTIVE_KEY_SET = "effectiveKeySet";

        final static String POOL_ELIGIBLE = "poolEligible";
        final static String POSITION_TYPE = "positionType";
        final static String POSITION_REPORT_GROUP = "positionReportGroup";
        final static String LEAVE_ELIGIBLE = "leaveEligible";
        final static String BENEFITS_ELIGIBLE = "benefitsEligible";
        final static String CLASSIFICATION_TITLE = "classificationTitle";
        final static String POSITION_CLASS = "positionClass";
        final static String PERCENT_TIME = "percentTime";
        final static String SALARY_GROUP = "salaryGroup";
        final static String TENURE_ELIGIBLE = "tenureEligible";
        final static String EXTERNAL_REFERENCE = "externalReference";
        final static String QUALIFICATION_LIST = "qualificationList";
        final static String QUALIFICATION = "qualification";
        final static String PM_POSITION_CLASS_ID = "pmPositionClassId";
        final static String FLAG_LIST = "flagList";
        final static String FLAG = "flag";
        final static String DUTY_LIST = "dutyList";
        final static String DUTY = "duty";
        final static String LEAVE_PLAN = "leavePlan";
        final static String PAY_GRADE = "payGrade";
        final static String ACTIVE = "active";
        final static String ID = "id";
        final static String EFFECTIVE_LOCAL_DATE = "effectiveLocalDate";
        final static String CREATE_TIME = "createTime";
        final static String USER_PRINCIPAL_ID = "userPrincipalId";

        final static String GROUP_KEY_CODE_SET = "groupKeyCodeSet";
        final static String GROUP_KEY_SET = "groupKeySet";
    }

}