package org.kuali.hr.time.earncode.service;

import org.apache.commons.lang.StringUtils;
import org.kuali.hr.job.Job;
import org.kuali.hr.time.assignment.Assignment;
import org.kuali.hr.time.cache.CacheResult;
import org.kuali.hr.time.dept.earncode.DepartmentEarnCode;
import org.kuali.hr.time.earncode.EarnCode;
import org.kuali.hr.time.earncode.dao.EarnCodeDao;
import org.kuali.hr.time.service.base.TkServiceLocator;
import org.kuali.hr.time.util.TKContext;
import org.kuali.hr.time.util.TKUser;
import org.kuali.hr.time.util.TkConstants;
import org.kuali.hr.time.workarea.WorkArea;

import java.sql.Date;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class EarnCodeServiceImpl implements EarnCodeService {

	private EarnCodeDao earnCodeDao;

	public void setEarnCodeDao(EarnCodeDao earnCodeDao) {
		this.earnCodeDao = earnCodeDao;
	}

	@Override
	public List<EarnCode> getEarnCodes(Assignment a, Date asOfDate) {
		List<EarnCode> earnCodes = new LinkedList<EarnCode>();

        // Note: https://jira.kuali.org/browse/KPME-689
        // We are grabbing a TkUser from the current thread local context here.
        //

        TKUser user = TKContext.getUser();
        if (user == null) {
            // TODO: Determine how to fail if there is no TkUser
            throw new RuntimeException("No User on context.");
        }

		if (a == null)
			throw new RuntimeException("Can not get earn codes for null assignment");
		Job job = a.getJob();
		if (job == null || job.getPayTypeObj() == null)
			throw new RuntimeException("Null job/job paytype on assignment!");

		EarnCode regularEc = getEarnCode(job.getPayTypeObj().getRegEarnCode(), asOfDate);
		if (regularEc == null)
			throw new RuntimeException("No regular earn code defined.");
		earnCodes.add(regularEc);
		List<DepartmentEarnCode> decs = TkServiceLocator.getDepartmentEarnCodeService().getDepartmentEarnCodes(job.getDept(), job.getHrSalGroup(), job.getLocation(), asOfDate);
		for (DepartmentEarnCode dec : decs) {
            boolean addEc = false;

            // Check employee flag
            if (dec.isEmployee() && 
               	(StringUtils.equals(user.getCurrentTargetPerson().getEmployeeId(), user.getCurrentPerson().getEmployeeId()))) {
                addEc = true;
            }

            // Check approver flag
            if (!addEc && dec.isApprover()) {
                Set<Long> workAreas = user.getCurrentRoles().getApproverWorkAreas();
                for (Long wa : workAreas) {
                    WorkArea workArea = TkServiceLocator.getWorkAreaService().getWorkArea(wa, asOfDate);
                    if (workArea!= null && a.getWorkArea().compareTo(workArea.getWorkArea())==0) {
                        // TODO: All Good, and then Break
                        addEc = true;
                        break;
                    }
                }
            }

            if (addEc) {
			    EarnCode ec = getEarnCode(dec.getEarnCode(), asOfDate);
			    if(ec!=null){
			    	earnCodes.add(ec);
			    }
            }
		}

		return earnCodes;
	}

	public EarnCode getEarnCode(String earnCode, Date asOfDate) {
		EarnCode ec = null;

		ec = earnCodeDao.getEarnCode(earnCode, asOfDate);

		return ec;
	}

    @Override
    @CacheResult(secondsRefreshPeriod=TkConstants.DEFAULT_CACHE_TIME)
    public String getEarnCodeType(String earnCode, Date asOfDate) {
        EarnCode earnCodeObj = getEarnCode(earnCode, asOfDate);
        return earnCodeObj.getEarnCodeType();
    }

	@Override
	@CacheResult(secondsRefreshPeriod=TkConstants.DEFAULT_CACHE_TIME)
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

}