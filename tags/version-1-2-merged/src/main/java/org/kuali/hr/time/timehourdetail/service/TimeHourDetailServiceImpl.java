package org.kuali.hr.time.timehourdetail.service;

import org.kuali.hr.time.timeblock.TimeBlock;
import org.kuali.hr.time.timeblock.TimeHourDetail;
import org.kuali.hr.time.timehourdetail.dao.TimeHourDetailDao;

import java.util.List;

public class TimeHourDetailServiceImpl implements TimeHourDetailService {

	TimeHourDetailDao timeHourDetailDao;

	@Override
	public TimeHourDetail getTimeHourDetail(String timeHourDetailId) {
		return timeHourDetailDao.getTimeHourDetail(timeHourDetailId);
	}

	@Override
	public TimeHourDetail saveTimeHourDetail(TimeBlock tb) {

		TimeHourDetail td = new TimeHourDetail();

		td.setTkTimeBlockId(tb.getTkTimeBlockId());
		td.setEarnCode(tb.getEarnCode());
		td.setHours(tb.getHours());
		tb.setAmount(tb.getAmount());

		timeHourDetailDao.saveOrUpdate(td);

		return td;
	}

	public void setTimeHourDetailDao(TimeHourDetailDao timeHourDetailDao) {
		this.timeHourDetailDao = timeHourDetailDao;
	}
	@Override
	public List<TimeHourDetail> getTimeHourDetailsForTimeBlock(String timeBlockId) {
		return this.timeHourDetailDao.getTimeHourDetailsForTimeBlock(timeBlockId);
	}

    public void removeTimeHourDetails(String timeBlockId) {
        this.timeHourDetailDao.remove(timeBlockId);
    }
    
    @Override
    public void removeTimeHourDetail(String timeHourDetailId) {
        timeHourDetailDao.removeById(timeHourDetailId);
    }
}