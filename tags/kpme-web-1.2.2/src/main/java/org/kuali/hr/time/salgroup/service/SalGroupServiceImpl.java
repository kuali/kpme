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
package org.kuali.hr.time.salgroup.service;

import org.kuali.hr.time.salgroup.SalGroup;
import org.kuali.hr.time.salgroup.dao.SalGroupDao;

import java.sql.Date;
import java.util.List;

public class SalGroupServiceImpl implements SalGroupService {

    private SalGroupDao salGroupDao;

    @Override
    public SalGroup getSalGroup(String salGroup, Date asOfDate) {
        return salGroupDao.getSalGroup(salGroup, asOfDate);
    }

    public void setSalGroupDao(SalGroupDao salGroupDao) {
        this.salGroupDao = salGroupDao;
    }

    @Override
    public SalGroup getSalGroup(String hrSalGroupId) {
        return salGroupDao.getSalGroup(hrSalGroupId);
    }

    @Override
    public int getSalGroupCount(String salGroup) {
        return salGroupDao.getSalGroupCount(salGroup);
    }

    @Override
    public List<SalGroup> getSalGroups(String salGroup, String descr, Date fromEffdt, Date toEffdt, String active, String showHist) {
        return salGroupDao.getSalGroups(salGroup, descr, fromEffdt, toEffdt, active, showHist);
    }
}