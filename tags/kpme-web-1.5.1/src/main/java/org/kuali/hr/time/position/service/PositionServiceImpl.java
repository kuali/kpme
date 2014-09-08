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
package org.kuali.hr.time.position.service;

import org.kuali.hr.time.position.Position;
import org.kuali.hr.time.position.dao.PositionDao;

import java.sql.Date;
import java.util.List;

public class PositionServiceImpl implements PositionService {

	private PositionDao positionDao;
	
	@Override
	public Position getPosition(String hrPositionId) {
		return positionDao.getPosition(hrPositionId);
	}

    @Override
    public Position getPosition(String hrPositionNbr, Date effectiveDate) {
        return positionDao.getPosition(hrPositionNbr, effectiveDate);
    }

    @Override
    public List<Position> getPositions(String positionNum, String workArea,
                                       String positionDescr, Date fromEffdt, Date toEffdt, String active, String showHistory) {
        return positionDao.getPositions(positionNum, workArea, positionDescr, fromEffdt, toEffdt, active, showHistory);
    }


    public PositionDao getPositionDao() {
		return positionDao;
	}

	public void setPositionDao(PositionDao positionDao) {
		this.positionDao = positionDao;
	}

}