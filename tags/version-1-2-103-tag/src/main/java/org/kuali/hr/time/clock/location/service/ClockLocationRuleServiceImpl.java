package org.kuali.hr.time.clock.location.service;

import org.apache.commons.lang.StringUtils;
import org.kuali.hr.time.cache.CacheResult;
import org.kuali.hr.time.clock.location.ClockLocationRule;
import org.kuali.hr.time.clock.location.ClockLocationRuleIpAddress;
import org.kuali.hr.time.clock.location.dao.ClockLocationDao;
import org.kuali.hr.time.clocklog.ClockLog;
import org.kuali.hr.time.util.TkConstants;
import org.kuali.rice.kns.util.GlobalVariables;

import java.sql.Date;
import java.util.List;

public class ClockLocationRuleServiceImpl implements ClockLocationRuleService {
	private ClockLocationDao clockLocationDao;

	public ClockLocationDao getClockLocationDao() {
		return clockLocationDao;
	}

	public void setClockLocationDao(ClockLocationDao clockLocationDao) {
		this.clockLocationDao = clockLocationDao;
	}

	public void processClockLocationRule(ClockLog clockLog, Date asOfDate){
		List<ClockLocationRule> lstClockLocationRules = getClockLocationRule(clockLog.getJob().getDept(),
										clockLog.getWorkArea(), clockLog.getPrincipalId(), clockLog.getJobNumber(), asOfDate);
		if(lstClockLocationRules.isEmpty()){
			return;
		}
		for(ClockLocationRule clockLocationRule : lstClockLocationRules){
			List<ClockLocationRuleIpAddress> ruleIpAddresses = clockLocationRule.getIpAddresses();
			String ipAddressClock = clockLog.getIpAddress();
			for(ClockLocationRuleIpAddress ruleIp : ruleIpAddresses) {
				if(compareIpAddresses(ruleIp.getIpAddress(), ipAddressClock)){
					return;
				}
			}
		}
		GlobalVariables.getMessageMap().putWarning("property", "ipaddress.invalid.format", clockLog.getIpAddress());

	}

	private boolean compareIpAddresses(String ipAddressRule, String ipAddress){
		String[] rulePieces = StringUtils.split(ipAddressRule, ".");
        int ruleMax = rulePieces.length-1;

		String[] ipAddPieces = StringUtils.split(ipAddress,".");
		boolean match = true;
		for(int i=0; i<ipAddPieces.length; i++){
			if( ((i > ruleMax) && StringUtils.equals("%", rulePieces[ruleMax])) ||
                  ((i <= ruleMax) && ( StringUtils.equals(ipAddPieces[i], rulePieces[i]) || StringUtils.equals("%", rulePieces[i]) ))
                )
            {
				// we don't need to do anything.
			} else {
			    return false;
			}
		}
		return match;
	}

	@Override
	@CacheResult(secondsRefreshPeriod=TkConstants.DEFAULT_CACHE_TIME)
	public List<ClockLocationRule> getClockLocationRule(String dept, Long workArea,String principalId, Long jobNumber, Date asOfDate) {

        // 1 : dept, wa, principal, job
		List<ClockLocationRule> clockLocationRule = clockLocationDao.getClockLocationRule(dept, workArea,principalId,jobNumber,asOfDate);
		if(!clockLocationRule.isEmpty()){
			return clockLocationRule;
		}

        // 2 : dept, wa, principal, -1
		clockLocationRule = clockLocationDao.getClockLocationRule(dept, workArea, principalId, -1L, asOfDate);
		if(!clockLocationRule.isEmpty()){
			return clockLocationRule;
		}

        // 3 : dept, wa, %        , job
        clockLocationRule = clockLocationDao.getClockLocationRule(dept, workArea, "%", jobNumber, asOfDate);
        if(!clockLocationRule.isEmpty()){
            return clockLocationRule;
        }

        // 4 : dept, -1, principal, job
        clockLocationRule = clockLocationDao.getClockLocationRule(dept, -1L, principalId, jobNumber, asOfDate);
        if(!clockLocationRule.isEmpty()){
            return clockLocationRule;
        }

        // 5 : dept, wa, %        , -1
		clockLocationRule = clockLocationDao.getClockLocationRule(dept, workArea, "%", -1L, asOfDate);
		if(!clockLocationRule.isEmpty()){
			return clockLocationRule;
		}

        // 6 : dept, -1, principal, -1
        clockLocationRule = clockLocationDao.getClockLocationRule(dept, -1L, principalId, -1L, asOfDate);
        if(!clockLocationRule.isEmpty()){
            return clockLocationRule;
        }

        // 7 : dept, -1, %        , job
        clockLocationRule = clockLocationDao.getClockLocationRule(dept, -1L, "%", jobNumber, asOfDate);
        if(!clockLocationRule.isEmpty()){
            return clockLocationRule;
        }

        // 8 : dept, -1, %        , job
		clockLocationRule = clockLocationDao.getClockLocationRule(dept, -1L, "%", -1L, asOfDate);
		return clockLocationRule;
	}

	@Override
	@CacheResult(secondsRefreshPeriod=TkConstants.DEFAULT_CACHE_TIME)
	public List<ClockLocationRule> getNewerVersionClockLocationRule(
			String dept, Long workArea, String principalId, Long jobNumber,
			Date asOfDate) {
		 
		return clockLocationDao.getNewerVersionClockLocationRule(dept, workArea, principalId, jobNumber, asOfDate);
	}

	@Override
	@CacheResult(secondsRefreshPeriod=TkConstants.DEFAULT_CACHE_TIME)
	public ClockLocationRule getClockLocationRule(String tkClockLocationRuleId) {
		return clockLocationDao.getClockLocationRule(tkClockLocationRuleId);
	}
	
	public void populateIPAddressesForCLR(ClockLocationRule clr){
		clockLocationDao.populateIPAddressesForCLR(clr);
	}

}
