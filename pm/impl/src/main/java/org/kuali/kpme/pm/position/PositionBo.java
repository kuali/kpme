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
package org.kuali.kpme.pm.position;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.kuali.kpme.core.api.position.PositionBaseContract;
import org.kuali.kpme.core.departmentaffiliation.DepartmentAffiliationBo;
import org.kuali.kpme.core.groupkey.HrGroupKeyBo;
import org.kuali.kpme.core.position.PositionBaseBo;
import org.kuali.kpme.pm.api.classification.ClassificationContract;
import org.kuali.kpme.pm.api.classification.duty.ClassificationDutyContract;
import org.kuali.kpme.pm.api.classification.flag.ClassificationFlagContract;
import org.kuali.kpme.pm.api.position.Position;
import org.kuali.kpme.pm.api.position.PositionContract;
import org.kuali.kpme.pm.api.position.funding.PositionFunding;
import org.kuali.kpme.pm.classification.qual.ClassificationQualificationBo;
import org.kuali.kpme.pm.position.funding.PositionFundingBo;
import org.kuali.kpme.pm.positiondepartment.PositionDepartmentBo;
import org.kuali.kpme.pm.positionresponsibility.PositionResponsibilityBo;
import org.kuali.kpme.pm.service.base.PmServiceLocator;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.mo.ModelObjectUtils;
import org.kuali.rice.core.api.util.Truth;

import com.google.common.collect.ImmutableList;

public class PositionBo extends PositionBaseBo implements PositionContract {
	private static final long serialVersionUID = 1L;
	
    public static final ImmutableList<String> CACHE_FLUSH = new ImmutableList.Builder<String>()
            .add(PositionBaseContract.CACHE_NAME)
            .add(PositionContract.CACHE_NAME)
            .build();

    public static final ImmutableList<String> BUSINESS_KEYS = new ImmutableList.Builder<String>()
            .add(KeyFields.POSITION_NUMBER)
            .build();

	private List<PositionQualificationBo> qualificationList = new LinkedList<PositionQualificationBo>();
    private List<PositionDutyBo> dutyList = new LinkedList<PositionDutyBo>();
    private List<PstnFlagBo> flagList = new LinkedList<PstnFlagBo>();
    private List<PositionResponsibilityBo> positionResponsibilityList = new LinkedList<PositionResponsibilityBo>();
    private List<PositionFundingBo> fundingList = new ArrayList<PositionFundingBo>();
    private List<PositionDepartmentBo> departmentList = new ArrayList<PositionDepartmentBo>();

    private String salaryGroup;
    private String pmPositionClassId;
    private transient String positionClass;
    private String classificationTitle;

    private BigDecimal percentTime;
    private int workMonths;
    private String benefitsEligible;
    private String leaveEligible;
    private String leavePlan;
    private String positionReportGroup;
    private String positionType;
    private String poolEligible;
    private int maxPoolHeadCount;
    private String tenureEligible;
     
    private String process;
    private String positionStatus;
    private String primaryDepartment;
    private String appointmentType;
    private String reportsToPositionId;
    private String reportsToPrincipalId;
    private Date expectedEndDate;
    private String renewEligible;
    private String temporary;
    private String contract;
    private String contractType;
    private String payGrade;
    private String payStep;
    
    private String category;		// used to determine what fields should show when editing an existing maint doc
    
    private String reportsToWorkingTitle; // KPME-3269
    
    private List<ClassificationQualificationBo> requiredQualList = new ArrayList<ClassificationQualificationBo>(); 	// read only required qualifications that comes from assiciated Classification
    /*private transient boolean displayQualifications;
    private transient boolean displayDuties;
    private transient boolean displayFlags;
    private transient boolean displayResponsibility;
    private transient boolean displayFunding;
    private transient boolean displayAdHocRecipients;*/

    @Override
    public List<PositionDutyBo> getDutyList() {
    	if(CollectionUtils.isEmpty(dutyList) && StringUtils.isNotEmpty(this.getPmPositionClassId())) {
    		List<? extends ClassificationDutyContract> aList = PmServiceLocator.getClassificationDutyService().getDutyListForClassification(this.getPmPositionClassId());
    		if(CollectionUtils.isNotEmpty(aList)) {
    			List<PositionDutyBo> pDutyList = new ArrayList<PositionDutyBo>();
    			// copy basic information from classificaton duty list
    			for(ClassificationDutyContract aDuty : aList) {
    				PositionDutyBo pDuty = new PositionDutyBo();
    				pDuty.setName(aDuty.getName());
    				pDuty.setDescription(aDuty.getDescription());
    				pDuty.setPercentage(aDuty.getPercentage());
    				pDuty.setPmDutyId(null);
    				pDuty.setHrPositionId(this.getHrPositionId());
    				pDutyList.add(pDuty);
    			}
    			this.setDutyList(pDutyList);
    		}
    	}
		return dutyList;
	}

    @Override
	public List<PositionResponsibilityBo> getPositionResponsibilityList() {
		return positionResponsibilityList;
	}

	public void setPositionResponsibilityList(
			List<PositionResponsibilityBo> positionResponsibilityList) {
		this.positionResponsibilityList = positionResponsibilityList;
	}
	public void setDutyList(List<PositionDutyBo> dutyList) {
		this.dutyList = dutyList;
	}

    @Override
	public List<PositionQualificationBo> getQualificationList() {
		return qualificationList;
	}

	public void setQualificationList(List<PositionQualificationBo> qualificationList) {
		this.qualificationList = qualificationList;
	}

    @Override
	public List<PstnFlagBo> getFlagList() {
		if(CollectionUtils.isEmpty(flagList) && StringUtils.isNotEmpty(this.getPmPositionClassId())) {
    		List<? extends ClassificationFlagContract> aList = PmServiceLocator.getClassificationFlagService().getFlagListForClassification(this.getPmPositionClassId());
    		if(CollectionUtils.isNotEmpty(aList)) {
    			List<PstnFlagBo> pFlagList = new ArrayList<PstnFlagBo>();
    			// copy basic information from classificaton flag list
    			for(ClassificationFlagContract aFlag : aList) {
    				PstnFlagBo pFlag = new PstnFlagBo();
    				pFlag.setCategory(aFlag.getCategory());
    				pFlag.setNames(aFlag.getNames());
    				pFlag.setPmFlagId(null);
    				pFlag.setHrPositionId(this.getHrPositionId());
    				pFlagList.add(pFlag);
    			}
    			this.setFlagList(pFlagList);
    		}
		}
		return flagList;
	}

	public void setFlagList(List<PstnFlagBo> flagList) {
		this.flagList = flagList;
	}

    @Override
	public String getPmPositionClassId() {
		return pmPositionClassId;
	}

	public void setPmPositionClassId(String id) {
		this.pmPositionClassId = id;
	}

    @Override
    public String getPositionClass() {
        if (StringUtils.isBlank(positionClass) && StringUtils.isNotBlank(pmPositionClassId)) {
            ClassificationContract classification = PmServiceLocator.getClassificationService().getClassificationById(this.pmPositionClassId);
            positionClass = classification != null ? classification.getPositionClass() : null;
        }

        return positionClass;
    }

    public void setPositionClass(String positionClass) {
        this.positionClass = positionClass;
    }

    @SuppressWarnings("unchecked")
    @Override
	public List<ClassificationQualificationBo> getRequiredQualList() {
		if(StringUtils.isNotEmpty(this.getPmPositionClassId())) {
			// when Position Classification Id is changed, change the requiredQualList with it
			if(CollectionUtils.isEmpty(requiredQualList) ||
					(CollectionUtils.isNotEmpty(requiredQualList) 
							&& !requiredQualList.get(0).getPmPositionClassId().equals(this.getPmPositionClassId()))) {
				List<ClassificationQualificationBo> aList = (List<ClassificationQualificationBo>)PmServiceLocator.getClassificationQualService()
						.getQualListForClassification(this.getPmPositionClassId());
				if(CollectionUtils.isNotEmpty(aList))
					this.setRequiredQualList(aList);
			} else {
				
			}
		}
 		return requiredQualList;
	}
	
	public void setRequiredQualList(List<ClassificationQualificationBo> aList) {
			requiredQualList = aList;
	}

    @Override
	public List<PositionFundingBo> getFundingList() {
		return fundingList;
	}

	public void setFundingList(List<PositionFundingBo> fundingList) {
		this.fundingList = fundingList;
	}

    @Override
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

    @Override
    public String getSalaryGroup() {
        return salaryGroup;
    }

    public void setSalaryGroup(String salaryGroup) {
        this.salaryGroup = salaryGroup;
    }

    @Override
    public String getClassificationTitle() {
        return classificationTitle;
    }

    public void setClassificationTitle(String classificationTitle) {
        this.classificationTitle = classificationTitle;
    }


    @Override
    public BigDecimal getPercentTime() {
        return percentTime;
    }

    public void setPercentTime(BigDecimal percentTime) {
        this.percentTime = percentTime;
    }

    @Override
    public String getBenefitsEligible() {
        return benefitsEligible;
    }

    public void setBenefitsEligible(String benefitsEligible) {
        this.benefitsEligible = benefitsEligible;
    }

    @Override
    public String getLeaveEligible() {
        return leaveEligible;
    }

    public void setLeaveEligible(String leaveEligible) {
        this.leaveEligible = leaveEligible;
    }

    @Override
    public String getLeavePlan() {
        return leavePlan;
    }

    public void setLeavePlan(String leavePlan) {
        this.leavePlan = leavePlan;
    }

    @Override
    public String getPositionReportGroup() {
        return positionReportGroup;
    }

    public void setPositionReportGroup(String positionReportGroup) {
        this.positionReportGroup = positionReportGroup;
    }

    @Override
    public String getPositionType() {
        return positionType;
    }

    public void setPositionType(String positionType) {
        this.positionType = positionType;
    }

    @Override
    public String getPoolEligible() {
        return poolEligible;
    }

    public void setPoolEligible(String poolEligible) {
        this.poolEligible = poolEligible;
    }

    @Override
    public int getMaxPoolHeadCount() {
        return maxPoolHeadCount;
    }

    public void setMaxPoolHeadCount(int maxPoolHeadCount) {
        this.maxPoolHeadCount = maxPoolHeadCount;
    }

    @Override
    public String getTenureEligible() {
        return tenureEligible;
    }

    public void setTenureEligible(String tenureEligible) {
        this.tenureEligible = tenureEligible;
    }

    @Override
    public int getWorkMonths() {
        return workMonths;
    }

    public void setWorkMonths(int workMonths) {
        this.workMonths = workMonths;
    }

    @Override
    public List<PositionDepartmentBo> getDepartmentList() {
        return departmentList;
    }

    public void setDepartmentList(List<PositionDepartmentBo> departmentList) {
        this.departmentList = departmentList;
    }

    @Override
	public String getProcess() {
		return process;
	}

	public void setProcess(String process) {
		this.process = process;
	}

    @Override
	public String getPositionStatus() {
		return positionStatus;
	}

	public void setPositionStatus(String positionStatus) {
		this.positionStatus = positionStatus;
	}

    @Override
	public String getPrimaryDepartment() {

		if (this.primaryDepartment == null && this.departmentList != null && this.departmentList.size() > 0) {
			for (PositionDepartmentBo department: this.departmentList) {
				DepartmentAffiliationBo pda = department.getDeptAfflObj();
				if (pda.isPrimaryIndicator()) {
					primaryDepartment = department.getDepartment();
					break;
				} 
			}
		}
		
		return primaryDepartment;
	}

    public void setPrimaryDepartment (String primaryDepartment) {
        this.primaryDepartment = primaryDepartment;
    }

    @Override
	public String getReportsToPositionId() {
		return reportsToPositionId;
	}

	public void setReportsToPositionId(String reportsToPositionId) {
		this.reportsToPositionId = reportsToPositionId;
	}

    @Override
	public String getReportsToPrincipalId() {
		return reportsToPrincipalId;
	}

	public void setReportsToPrincipalId(String reportsToPrincipalId) {
		this.reportsToPrincipalId = reportsToPrincipalId;
	}

	public Date getExpectedEndDate() {
		return expectedEndDate;
	}

	public void setExpectedEndDate(Date expectedEndDate) {
		this.expectedEndDate = expectedEndDate;
	}

    @Override
	public String getRenewEligible() {
		return renewEligible;
	}

	public void setRenewEligible(String renewEligible) {
		this.renewEligible = renewEligible;
	}

    @Override
	public String getTemporary() {
		return temporary;
	}

	public void setTemporary(String temporary) {
		this.temporary = temporary;
	}

    @Override
	public String getContract() {
		return contract;
	}

	public void setContract(String contract) {
		this.contract = contract;
	}

    @Override
	public String getContractType() {
		return contractType;
	}

	public void setContractType(String contractType) {
		this.contractType = contractType;
	}

    @Override
	public String getAppointmentType() {
		return appointmentType;
	}

	public void setAppointmentType(String appointmentType) {
		this.appointmentType = appointmentType;
	}

    @Override
	public String getPayGrade() {
		return payGrade;
	}

	public void setPayGrade(String payGrade) {
		this.payGrade = payGrade;
	}

    @Override
	public String getPayStep() {
		return payStep;
	}

	public void setPayStep(String payStep) {
		this.payStep = payStep;
	}

    @Override
	public String getReportsToWorkingTitle() {
		return reportsToWorkingTitle;
	}

	public void setReportsToWorkingTitle(String reportsToWorkingTitle) {
		this.reportsToWorkingTitle = reportsToWorkingTitle;
	}

	public boolean isDisplayQualifications() {
        String status = ConfigContext.getCurrentContextConfig().getProperty("kpme.pm.position.display.qualifications");
        return Truth.strToBooleanIgnoreCase(status, Boolean.FALSE);
	}


	public boolean isDisplayDuties() {
        String status = ConfigContext.getCurrentContextConfig().getProperty("kpme.pm.position.display.duties");
        return Truth.strToBooleanIgnoreCase(status, Boolean.FALSE);
	}

	public boolean isDisplayFlags() {
        String status = ConfigContext.getCurrentContextConfig().getProperty("kpme.pm.position.display.flags");
        return Truth.strToBooleanIgnoreCase(status, Boolean.FALSE);
	}


	public boolean isDisplayResponsibility() {
        String status = ConfigContext.getCurrentContextConfig().getProperty("kpme.pm.position.display.responsibility");
        return Truth.strToBooleanIgnoreCase(status, Boolean.FALSE);
	}

	public boolean isDisplayFunding() {
        String status = ConfigContext.getCurrentContextConfig().getProperty("kpme.pm.position.display.funding");
        return Truth.strToBooleanIgnoreCase(status, Boolean.FALSE);
	}


	public boolean isDisplayAdHocRecipients() {
        String status = ConfigContext.getCurrentContextConfig().getProperty("kpme.pm.position.display.adhocrecipients");
        return Truth.strToBooleanIgnoreCase(status, Boolean.FALSE);
	}

	@Override
	public DateTime getExpectedEndDateTime() {
		DateTime retVal = null;
		if(getExpectedEndDate() != null) {
			retVal = new DateTime(getExpectedEndDate());
		}
		return retVal;
	}
	
	public static PositionBo from(Position im) {
				if (im == null) {
					return null;
				}
				PositionBo retVal = new PositionBo();
		
				retVal.setHrPositionId(im.getHrPositionId());
				retVal.setPositionNumber(im.getPositionNumber());
				retVal.setGroupKey(HrGroupKeyBo.from(im.getGroupKey()));
				retVal.setGroupKeyCode(im.getGroupKeyCode());
				copyCommonFields(retVal, im);
				
				retVal.setBenefitsEligible(im.getBenefitsEligible());
				retVal.setPercentTime(im.getPercentTime());
				retVal.setClassificationTitle(im.getClassificationTitle());
				retVal.setSalaryGroup(im.getSalaryGroup());
				
				List<PositionQualificationBo> qualifications= ModelObjectUtils.transform(im.getQualificationList(), PositionQualificationBo.toBo);
				PositionQualificationBo.setOwnerOfDerivedCollection(retVal, qualifications);
				retVal.setQualificationList(qualifications);
				
				retVal.setReportsToPrincipalId(im.getReportsToPrincipalId());
				retVal.setLeaveEligible(im.getLeaveEligible());
				retVal.setPositionReportGroup(im.getPositionReportGroup());
				retVal.setPositionType(im.getPositionType());
				retVal.setPoolEligible(im.getPoolEligible());
				retVal.setMaxPoolHeadCount(im.getMaxPoolHeadCount());
				retVal.setTenureEligible(im.getTenureEligible());
                retVal.setPositionClass(im.getPositionClass());
                retVal.setAppointmentType(im.getAppointmentType());
                retVal.setReportsToWorkingTitle(im.getReportsToWorkingTitle());
                retVal.setPrimaryDepartment(im.getPrimaryDepartment());
                retVal.setProcess(im.getProcess());

				List<PositionDepartmentBo> departments = ModelObjectUtils.transform(im.getDepartmentList(), PositionDepartmentBo.toBo);
				PositionDepartmentBo.setOwnerOfDerivedCollection(retVal, departments);
				retVal.setDepartmentList(departments);
				
				retVal.setPositionStatus(im.getPositionStatus());
				retVal.setContractType(im.getContractType());
				retVal.setRenewEligible(im.getRenewEligible());
				retVal.setReportsToPositionId(im.getReportsToPositionId());
				
				retVal.setRequiredQualList(ModelObjectUtils.transform(im.getRequiredQualList(), ClassificationQualificationBo.toBo));
				
				List<PositionResponsibilityBo> responsibilities = ModelObjectUtils.transform(im.getPositionResponsibilityList(),PositionResponsibilityBo.toBo);
				PositionResponsibilityBo.setOwnerOfDerivedCollection(retVal, responsibilities);
				retVal.setPositionResponsibilityList(responsibilities);
				
				retVal.setPmPositionClassId(im.getPmPositionClassId());
				
				List<PositionFundingBo> fundings = ModelObjectUtils.transform(im.getFundingList(),PositionFundingBo.toBo);
				PositionFundingBo.setOwnerOfDerivedCollection(retVal, fundings);
				retVal.setFundingList(fundings);
				
				retVal.setWorkMonths(im.getWorkMonths());
				retVal.setTemporary(im.getTemporary());
				retVal.setCategory(im.getCategory());
				retVal.setLeavePlan(im.getLeavePlan());
				
				List<PstnFlagBo> flags = ModelObjectUtils.transform(im.getFlagList(), PstnFlagBo.toBo);
				PstnFlagBo.setOwnerOfDerivedCollection(retVal, flags);
				retVal.setFlagList(flags);
				
				retVal.setPayStep(im.getPayStep());
				retVal.setPayGrade(im.getPayGrade());
				
				List<PositionDutyBo> duties = ModelObjectUtils.transform(im.getDutyList(), PositionDutyBo.toBo);
				PositionDutyBo.setOwnerOfDerivedCollection(retVal, duties);
				retVal.setDutyList(duties);
				
				retVal.setContract(im.getContract());
				retVal.setDescription(im.getDescription());
				retVal.setId(im.getId());
				retVal.setEffectiveLocalDate(im.getEffectiveLocalDate());
				return retVal;
			}
		
			public static Position to(PositionBo bo) {
				if (bo == null) {
					return null;
				}
				return Position.Builder.create(bo).build();
			}
		
			public static final ModelObjectUtils.Transformer<PositionBo, Position> toImmutable = new ModelObjectUtils.Transformer<PositionBo, Position>() {
				public Position transform(PositionBo input) {
					return PositionBo.to(input);
				};
			};
		
			public static final ModelObjectUtils.Transformer<Position, PositionBo> toBo = new ModelObjectUtils.Transformer<Position, PositionBo>() {
				public PositionBo transform(Position input) {
					return PositionBo.from(input);
				};
			};
	
}