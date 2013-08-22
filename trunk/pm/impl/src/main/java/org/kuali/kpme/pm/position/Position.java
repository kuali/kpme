/**
 * Copyright 2004-2013 The Kuali Foundation
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.math.BigDecimal;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.rice.location.impl.campus.CampusBo;
import org.kuali.kpme.core.position.PositionBase;
import org.kuali.kpme.core.util.HrConstants;
import org.kuali.kpme.pm.api.position.PositionContract;
import org.kuali.kpme.pm.classification.duty.ClassificationDuty;
import org.kuali.kpme.pm.classification.flag.ClassificationFlag;
import org.kuali.kpme.pm.classification.qual.ClassificationQualification;
import org.kuali.kpme.pm.position.funding.PositionFunding;
import org.kuali.kpme.pm.positionresponsibility.PositionResponsibility;
import org.kuali.kpme.pm.service.base.PmServiceLocator;

public class Position extends PositionBase implements PositionContract {
	private static final long serialVersionUID = 1L;
	
    public static final String CACHE_NAME = HrConstants.CacheNamespace.NAMESPACE_PREFIX + "Position";
    private static final String[] PRIVATE_CACHES_FOR_FLUSH = {PositionBase.CACHE_NAME, Position.CACHE_NAME};
    public static final List<String> CACHE_FLUSH = Collections.unmodifiableList(Arrays.asList(PRIVATE_CACHES_FOR_FLUSH));
	
	private List<PositionQualification> qualificationList = new LinkedList<PositionQualification>();
    private List<PositionDuty> dutyList = new LinkedList<PositionDuty>();
    private List<PstnFlag> flagList = new LinkedList<PstnFlag>();
    private List<PositionResponsibility> positionResponsibilityList = new LinkedList<PositionResponsibility>();
    private List<PositionFunding> fundingList = new ArrayList<PositionFunding>();

    private String institution;
    private String campus;
    private String salaryGroup;
    private String pmPositionClassId;
    private String classificationTitle;
    private String workingPositionTitle;
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

    private CampusBo campusObj;
    
    private String category;		// used to determine what fields should show when editing an existing maint doc
    
    private List<ClassificationQualification> requiredQualList = new ArrayList<ClassificationQualification>(); 	// read only required qualifications that comes from assiciated Classification

    public List<PositionDuty> getDutyList() {
    	if(CollectionUtils.isEmpty(dutyList) && StringUtils.isNotEmpty(this.getPmPositionClassId())) {
    		List<ClassificationDuty> aList = PmServiceLocator.getClassificationDutyService().getDutyListForClassification(this.getPmPositionClassId());
    		if(CollectionUtils.isNotEmpty(aList)) {
    			List<PositionDuty> pDutyList = new ArrayList<PositionDuty>();
    			// copy basic information from classificaton duty list
    			for(ClassificationDuty aDuty : aList) {
    				PositionDuty pDuty = new PositionDuty();
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

	public List<PositionResponsibility> getPositionResponsibilityList() {
		return positionResponsibilityList;
	}

	public void setPositionResponsibilityList(
			List<PositionResponsibility> positionResponsibilityList) {
		this.positionResponsibilityList = positionResponsibilityList;
	}
	public void setDutyList(List<PositionDuty> dutyList) {
		this.dutyList = dutyList;
	}

	public List<PositionQualification> getQualificationList() {
		return qualificationList;
	}

	public void setQualificationList(List<PositionQualification> qualificationList) {
		this.qualificationList = qualificationList;
	}

	public List<PstnFlag> getFlagList() {
		if(CollectionUtils.isEmpty(flagList) && StringUtils.isNotEmpty(this.getPmPositionClassId())) {
    		List<ClassificationFlag> aList = PmServiceLocator.getClassificationFlagService().getFlagListForClassification(this.getPmPositionClassId());
    		if(CollectionUtils.isNotEmpty(aList)) {
    			List<PstnFlag> pFlagList = new ArrayList<PstnFlag>();
    			// copy basic information from classificaton flag list
    			for(ClassificationFlag aFlag : aList) {
    				PstnFlag pFlag = new PstnFlag();
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

	public void setFlagList(List<PstnFlag> flagList) {
		this.flagList = flagList;
	}
	
	public String getPmPositionClassId() {
		return pmPositionClassId;
	}

	public void setPmPositionClassId(String id) {
		this.pmPositionClassId = id;
	}
	
	public List<ClassificationQualification> getRequiredQualList() {
		if(StringUtils.isNotEmpty(this.getPmPositionClassId())) {
			// when Position Classification Id is changed, change the requiredQualList with it
			if(CollectionUtils.isEmpty(requiredQualList) ||
					(CollectionUtils.isNotEmpty(requiredQualList) 
							&& !requiredQualList.get(0).getPmPositionClassId().equals(this.getPmPositionClassId()))) {
				List<ClassificationQualification> aList = PmServiceLocator.getClassificationQualService()
						.getQualListForClassification(this.getPmPositionClassId());
				if(CollectionUtils.isNotEmpty(aList))
					this.setRequiredQualList(aList);
			} else {
				
			}
		}
 		return requiredQualList;
	}
	
	public void setRequiredQualList(List<ClassificationQualification> aList) {
			requiredQualList = aList;
	}

	public List<PositionFunding> getFundingList() {
		return fundingList;
	}

	public void setFundingList(List<PositionFunding> fundingList) {
		this.fundingList = fundingList;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public CampusBo getCampusObj() {
        return campusObj;
    }

    public void setCampusObj(CampusBo campusObj) {
        this.campusObj = campusObj;
    }

    public String getSalaryGroup() {
        return salaryGroup;
    }

    public void setSalaryGroup(String salaryGroup) {
        this.salaryGroup = salaryGroup;
    }

    public String getClassificationTitle() {
        return classificationTitle;
    }

    public void setClassificationTitle(String classificationTitle) {
        this.classificationTitle = classificationTitle;
    }

    public String getWorkingPositionTitle() {
        return workingPositionTitle;
    }

    public void setWorkingPositionTitle(String workingPositionTitle) {
        this.workingPositionTitle = workingPositionTitle;
    }

    public BigDecimal getPercentTime() {
        return percentTime;
    }

    public void setPercentTime(BigDecimal percentTime) {
        this.percentTime = percentTime;
    }

    public String getBenefitsEligible() {
        return benefitsEligible;
    }

    public void setBenefitsEligible(String benefitsEligible) {
        this.benefitsEligible = benefitsEligible;
    }

    public String getLeaveEligible() {
        return leaveEligible;
    }

    public void setLeaveEligible(String leaveEligible) {
        this.leaveEligible = leaveEligible;
    }

    public String getLeavePlan() {
        return leavePlan;
    }

    public void setLeavePlan(String leavePlan) {
        this.leavePlan = leavePlan;
    }

    public String getPositionReportGroup() {
        return positionReportGroup;
    }

    public void setPositionReportGroup(String positionReportGroup) {
        this.positionReportGroup = positionReportGroup;
    }

    public String getPositionType() {
        return positionType;
    }

    public void setPositionType(String positionType) {
        this.positionType = positionType;
    }

    public String getPoolEligible() {
        return poolEligible;
    }

    public void setPoolEligible(String poolEligible) {
        this.poolEligible = poolEligible;
    }

    public int getMaxPoolHeadCount() {
        return maxPoolHeadCount;
    }

    public void setMaxPoolHeadCount(int maxPoolHeadCount) {
        this.maxPoolHeadCount = maxPoolHeadCount;
    }

    public String getTenureEligible() {
        return tenureEligible;
    }

    public void setTenureEligible(String tenureEligible) {
        this.tenureEligible = tenureEligible;
    }

    public int getWorkMonths() {
        return workMonths;
    }

    public void setWorkMonths(int workMonths) {
        this.workMonths = workMonths;
    }
}
