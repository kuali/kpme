/**
 * Copyright 2004-2012 The Kuali Foundation
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
package org.kuali.hr.time.earncode.service;

import com.google.common.collect.Ordering;
import org.apache.commons.lang.StringUtils;
import org.kuali.hr.job.Job;
import org.kuali.hr.lm.LMConstants;
import org.kuali.hr.lm.accrual.AccrualCategory;
import org.kuali.hr.lm.earncodesec.EarnCodeSecurity;
import org.kuali.hr.lm.earncodesec.EarnCodeType;
import org.kuali.hr.time.assignment.Assignment;
import org.kuali.hr.time.earncode.EarnCode;
import org.kuali.hr.time.earncode.dao.EarnCodeDao;
import org.kuali.hr.time.principal.PrincipalHRAttributes;
import org.kuali.hr.time.roles.TkUserRoles;
import org.kuali.hr.time.service.base.TkServiceLocator;
import org.kuali.hr.time.util.TKUser;
import org.kuali.hr.time.util.TKUtils;
import org.kuali.hr.time.workarea.WorkArea;
import org.kuali.rice.krad.util.GlobalVariables;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.*;

public class EarnCodeServiceImpl implements EarnCodeService {

	private EarnCodeDao earnCodeDao;

	public void setEarnCodeDao(EarnCodeDao earnCodeDao) {
		this.earnCodeDao = earnCodeDao;
	}

    public List<EarnCode> getEarnCodesForLeaveAndTime(Assignment a, Date asOfDate) {
        //  This method combining both leave calendar and timesheet calendar earn codes may never be used, but it is available.
        //  It was specified in kpme-1745, "Implement getEarnCodesForLeaveAndTime and call both of the above methods and return in one collection."
        List<EarnCode> earnCodes = getEarnCodesForTime(a, asOfDate);
        List<EarnCode> leaveEarnCodes = getEarnCodesForLeave(a, asOfDate);
        //  the following list processing does work as hoped, comparing the objects' data, rather than their references to memory structures.
        earnCodes.removeAll(leaveEarnCodes); //ensures no overlap during the addAll
        earnCodes.addAll(leaveEarnCodes);

        return earnCodes;
    }

    public List<EarnCode> getEarnCodesForTime(Assignment a, Date asOfDate) {
        if (a == null) throw new RuntimeException("No assignment parameter.");
        Job job = a.getJob();
        if (job == null || job.getPayTypeObj() == null) throw new RuntimeException("Null job or null job pay type on assignment.");

        List<EarnCode> earnCodes = new LinkedList<EarnCode>();
        String earnTypeCode = EarnCodeType.TIME.getCode();

        EarnCode regularEarnCode = getEarnCode(job.getPayTypeObj().getRegEarnCode(), asOfDate);
        if (regularEarnCode == null) {
            throw new RuntimeException("No regular earn code defined for job pay type.");
        } else {
            earnCodes.add(regularEarnCode);
        }
        List<EarnCodeSecurity> decs = TkServiceLocator.getEarnCodeSecurityService().getEarnCodeSecurities(job.getDept(), job.getHrSalGroup(), job.getLocation(), asOfDate);
        for (EarnCodeSecurity dec : decs) {
            if (earnTypeCode.equals(dec.getEarnCodeType())
                    || EarnCodeType.BOTH.getCode().equals(dec.getEarnCodeType())) {

                boolean addEarnCode = addEarnCodeBasedOnEmployeeApproverSettings(dec, a, asOfDate);
                if (addEarnCode) {
                    EarnCode ec = getEarnCode(dec.getEarnCode(), asOfDate);
                    if(ec!=null){
                        earnCodes.add(ec);
                    }
                }
            }
        }

        return earnCodes;
    }


    public List<EarnCode> getEarnCodesForLeave(Assignment a, Date asOfDate) {
        if (a == null) throw new RuntimeException("No assignment parameter.");
        Job job = a.getJob();
        if (job == null || job.getPayTypeObj() == null) throw new RuntimeException("Null job or null job pay type on assignment.");

        List<EarnCode> earnCodes = new LinkedList<EarnCode>();
        String earnTypeCode = EarnCodeType.LEAVE.getCode();
        // skip getting the regular earn code for Leave Calendar

        List<String> listAccrualCategories = new LinkedList<String>();
        String accrualCategory;

        //  first make a list of the accrual categories available to the user's leave plan, for later comparison.
        PrincipalHRAttributes principalHRAttributes = TkServiceLocator.getPrincipalHRAttributeService().getPrincipalCalendar(job.getPrincipalId(), asOfDate);
        boolean fmlaEligible = principalHRAttributes.isFmlaEligible();
        boolean workersCompEligible = principalHRAttributes.isWorkersCompEligible();
        String leavePlan = principalHRAttributes.getLeavePlan();
        if(leavePlan != null){
            for (AccrualCategory accrualCategories : TkServiceLocator.getAccrualCategoryService().getActiveAccrualCategoriesForLeavePlan(leavePlan, asOfDate)) {
                accrualCategory = accrualCategories.getAccrualCategory();
                if(accrualCategory != null) {
                    listAccrualCategories.add(accrualCategory);
                }
            }
        }

        //  get all earn codes by user security, then we'll filter on accrual category first as we process them.
        List<EarnCodeSecurity> decs = TkServiceLocator.getEarnCodeSecurityService().getEarnCodeSecurities(job.getDept(), job.getHrSalGroup(), job.getLocation(), asOfDate);
        for (EarnCodeSecurity dec : decs) {

            //  allow types Leave AND Both
            if (earnTypeCode.equals(dec.getEarnCodeType()) || EarnCodeType.BOTH.getCode().equals(dec.getEarnCodeType())) {
                EarnCode ec = getEarnCode(dec.getEarnCode(), asOfDate);
                if(ec!=null){

                    //  now that we have a list of security earn codes, compare their accrual categories to the user's accrual category list.
                    //  we also allow earn codes that have no accrual category assigned.
                    if (listAccrualCategories.contains(ec.getAccrualCategory()) || ec.getAccrualCategory() == null) {

                        //  if the user's fmla flag is Yes, that means we are not restricting codes based on this flag, so any code is shown.
                        //    if the fmla flag on a code is yes they can see it.    (allow)
                        //    if the fmla flag on a code is no they should see it.  (allow)
                        //  if the user's fmla flag is No,
                        //    they can see any codes which are fmla=no.             (allow)
                        //    they can not see codes with fmla=yes.                 (exclude earn code)
                        //  the fmla earn codes=no do not require any exclusion
                        //  the only action required is if the fmla user flag=no: exclude those codes with fmla=yes.

                        if ( (fmlaEligible || ec.getFmla().equals("N")) ) {
                            // go on, we are allowing these three combinations: YY, YN, NN

                            //  Apply the same logic as FMLA to the Worker Compensation flags.

                            if ( (workersCompEligible || ec.getWorkmansComp().equals("N")) ){
                                // go on, we are allowing these three combinations: YY, YN, NN.

                                //  now process the scheduled leave flag, but only for the Planning Calendar, not for the Recording Calendar.
                                //  determine if the the planning calendar is in effect. This might only be appropriate in a different layer.
                                //  if the allow_schd_leave flag=yes, continue towards adding to the earn code list, otherwise, exclude the earn code.

                                //if (inPlanningClendar) {
                                    if (ec.getAllowScheduledLeave().equals("Y")) {
                                        boolean addEarnCode = addEarnCodeBasedOnEmployeeApproverSettings(dec, a, asOfDate);
                                        if (addEarnCode) {
                                            earnCodes.add(ec);
                                        }
                                    } else {
                                        //  do not add this earn code. Earn code allowed scheduled leave flag=no.
                                        //earnCodes.add(ec); // go ahead and it for now, until the planning calendar aspect to this is fleshed out.
                                    }
                                //}
                            } else {
                                //  do not add this earn code. User WC flag=no and earn code WC flag=yes.
                            }
                        } else {
                            //  do not add this earn code. User FMLA flag=no and earn code FMLA flag=yes.
                        }
                    }
                }
            }
        }

        return earnCodes;
    }

    private boolean addEarnCodeBasedOnEmployeeApproverSettings(EarnCodeSecurity security, Assignment a, Date asOfDate) {
        boolean addEarnCode = false;
        if (security.isEmployee() &&
                (StringUtils.equals(TKUser.getCurrentTargetPerson().getEmployeeId(), GlobalVariables.getUserSession().getPerson().getEmployeeId()))) {
            addEarnCode = true;
        }
        // Check approver flag
        if (!addEarnCode && security.isApprover()) {
            Set<Long> workAreas = TkUserRoles.getUserRoles(GlobalVariables.getUserSession().getPrincipalId()).getApproverWorkAreas();
            for (Long wa : workAreas) {
                WorkArea workArea = TkServiceLocator.getWorkAreaService().getWorkArea(wa, asOfDate);
                if (workArea!= null && a.getWorkArea().compareTo(workArea.getWorkArea())==0) {
                    addEarnCode = true;
                    break;
                }
            }
        }
        return addEarnCode;
    }

    @Override
    public List<EarnCode> getEarnCodesForPrincipal(String principalId, Date asOfDate) {
        List<EarnCode> earnCodes = new LinkedList<EarnCode>();
        List<Assignment> assignments = TkServiceLocator.getAssignmentService().getAssignments(principalId, asOfDate);
        for (Assignment assignment : assignments) {
            List<EarnCode> assignmentEarnCodes = getEarnCodesForLeave(assignment, asOfDate);
            //  the following list processing does work as hoped, comparing the objects' data, rather than their references to memory structures.
            earnCodes.removeAll(assignmentEarnCodes); //ensures no overlap during the addAll
            earnCodes.addAll(assignmentEarnCodes);
        }

        return earnCodes;
    }

    public EarnCode getEarnCode(String earnCode, Date asOfDate) {
		return earnCodeDao.getEarnCode(earnCode, asOfDate);
	}

    @Override
    public String getEarnCodeType(String earnCode, Date asOfDate) {
        EarnCode earnCodeObj = getEarnCode(earnCode, asOfDate);
        return earnCodeObj.getEarnCodeType();
    }

	@Override
	public EarnCode getEarnCodeById(String earnCodeId) {
		return earnCodeDao.getEarnCodeById(earnCodeId);
	}
	
	public List<EarnCode> getOvertimeEarnCodes(Date asOfDate){
		return earnCodeDao.getOvertimeEarnCodes(asOfDate);
	}
	
	public List<String> getOvertimeEarnCodesStrs(Date asOfDate){
		List<String> ovtEarnCodeStrs = new ArrayList<String>();
		List<EarnCode> ovtEarnCodes = getOvertimeEarnCodes(asOfDate);
		if(ovtEarnCodes != null){
			for(EarnCode ovtEc : ovtEarnCodes){
				ovtEarnCodeStrs.add(ovtEc.getEarnCode());
			}
		}
		return ovtEarnCodeStrs;
	}
	
	@Override
	public int getEarnCodeCount(String earnCode) {
		return earnCodeDao.getEarnCodeCount(earnCode);
	}
	
	@Override
	public int getNewerEarnCodeCount(String earnCode, Date effdt) {
		return earnCodeDao.getNewerEarnCodeCount(earnCode, effdt);
	}

	@Override
	public BigDecimal roundHrsWithEarnCode(BigDecimal hours, EarnCode earnCode) {
		String roundOption = LMConstants.ROUND_OPTION_MAP.get(earnCode.getRoundingOption());
		BigDecimal fractScale = new BigDecimal(earnCode.getFractionalTimeAllowed());
		if(roundOption == null) {
			throw new RuntimeException("Rounding option of Earn Code " + earnCode.getEarnCode() + " is not recognized.");
		}
		BigDecimal roundedHours = hours;
		if(roundOption.equals("Traditional")) {
			roundedHours = hours.setScale(fractScale.scale(), BigDecimal.ROUND_HALF_EVEN);
		} else if(roundOption.equals("Truncate")) {
			roundedHours = hours.setScale(fractScale.scale(), BigDecimal.ROUND_DOWN);
		}
		return roundedHours;
	}
	
	@Override
	public Map<String, String> getEarnCodesForDisplay(String principalId) {
		return getEarnCodesForDisplayWithEffectiveDate(principalId, TKUtils.getCurrentDate());
	}

    @Override
    public Map<String, String> getEarnCodesForDisplayWithEffectiveDate(String principalId, Date asOfDate) {
        List<EarnCode> earnCodes = this.getEarnCodesForPrincipal(principalId, asOfDate);

        Date currentDate = TKUtils.getCurrentDate();
        boolean futureDate = asOfDate.after(currentDate);
        List<EarnCode> copyList = new ArrayList<EarnCode>();
        copyList.addAll(earnCodes);
        for (EarnCode earnCode : copyList) {
            if ( futureDate
                    && !earnCode.getAllowScheduledLeave().equalsIgnoreCase("Y")) {
                earnCodes.remove(earnCode);
            }
        }
        Comparator<EarnCode> earnCodeComparator = new Comparator<EarnCode>() {
            @Override
            public int compare(EarnCode ec1, EarnCode ec2) {
                return ec1.getEarnCode().compareToIgnoreCase(ec2.getEarnCode());
            }
        };
        // Order by leaveCode ascending
        Ordering<EarnCode> ordering = Ordering.from(earnCodeComparator);

        Map<String, String> earnCodesForDisplay = new LinkedHashMap<String, String>();
        for (EarnCode earnCode : ordering.sortedCopy(earnCodes)) {
            earnCodesForDisplay.put(earnCode.getEarnCodeKeyForDisplay(), earnCode.getEarnCodeValueForDisplay());
        }
        return earnCodesForDisplay;
    }

    /* not using yet, may not be needed
    @Override
    public Map<String, String> getEarnCod e s ForDisplayWithAssignment(Assignment assignment, Date asOfDate) {
        List<EarnCode> earnCodes = this.getEarnCod e s ( assignment, asOfDate);

        Date currentDate = TKUtils.getCurrentDate();
        boolean futureDate = asOfDate.after(currentDate);
        List<EarnCode> copyList = new ArrayList<EarnCode>();
        copyList.addAll(earnCodes);
        for (EarnCode earnCode : copyList) {
            if ( futureDate
                    && !earnCode.getAllowScheduledLeave().equalsIgnoreCase("Y")) {
                earnCodes.remove(earnCode);
            }
        }
        Comparator<EarnCode> earnCodeComparator = new Comparator<EarnCode>() {
            @Override
            public int compare(EarnCode ec1, EarnCode ec2) {
                return ec1.getEarnCode().compareToIgnoreCase(ec2.getEarnCode());
            }
        };
        // Order by leaveCode ascending
        Ordering<EarnCode> ordering = Ordering.from(earnCodeComparator);

        Map<String, String> earnCodesForDisplay = new LinkedHashMap<String, String>();
        for (EarnCode earnCode : ordering.sortedCopy(earnCodes)) {
            earnCodesForDisplay.put(earnCode.getEarnCodeKeyForDisplay(), earnCode.getEarnCodeValueForDisplay());
        }
        return earnCodesForDisplay;
    }

    */
}
