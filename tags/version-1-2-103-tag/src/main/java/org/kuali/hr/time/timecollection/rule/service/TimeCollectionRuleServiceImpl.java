package org.kuali.hr.time.timecollection.rule.service;

import org.codehaus.plexus.util.StringUtils;
import org.kuali.hr.time.cache.CacheResult;
import org.kuali.hr.time.collection.rule.TimeCollectionRule;
import org.kuali.hr.time.timecollection.rule.dao.TimeCollectionRuleDaoService;
import org.kuali.hr.time.util.TkConstants;

import java.sql.Date;
import java.util.List;

public class TimeCollectionRuleServiceImpl implements TimeCollectionRuleService{
	private TimeCollectionRuleDaoService timeCollectRuleDao;
	
	@CacheResult(secondsRefreshPeriod=TkConstants.DEFAULT_CACHE_TIME)
	public TimeCollectionRule getTimeCollectionRule(String dept, Long workArea, String payType, Date asOfDate){
		return timeCollectRuleDao.getTimeCollectionRule(dept, workArea, payType, asOfDate);
	}
	
	@CacheResult(secondsRefreshPeriod=TkConstants.DEFAULT_CACHE_TIME)
	public TimeCollectionRule getTimeCollectionRule(String dept, Long workArea,Date asOfDate){
		return timeCollectRuleDao.getTimeCollectionRule(dept, workArea, asOfDate);
	}

	public TimeCollectionRuleDaoService getTimeCollectRuleDao() {
		return timeCollectRuleDao;
	}

	public void setTimeCollectRuleDao(
			TimeCollectionRuleDaoService timeCollectRuleDao) {
		this.timeCollectRuleDao = timeCollectRuleDao;
	}

	@Override
	public TimeCollectionRule getTimeCollectionRule(String tkTimeCollectionRuleId) {
		return timeCollectRuleDao.getTimeCollectionRule(tkTimeCollectionRuleId);
	}

    @Override
    public List<TimeCollectionRule> getTimeCollectionRules(String dept, String workArea, String payType, String active) {
        Long workAreaToSearch = StringUtils.isEmpty(workArea) ? null : Long.parseLong(workArea);
        return timeCollectRuleDao.getTimeCollectionRules(dept, workAreaToSearch , payType, active, null);
    }
}