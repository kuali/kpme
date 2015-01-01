/**
 * Copyright 2004-2015 The Kuali Foundation
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
package org.kuali.kpme.pm.classification.duty.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryFactory;
import org.kuali.kpme.pm.classification.duty.ClassificationDuty;
import org.kuali.rice.core.framework.persistence.ojb.dao.PlatformAwareDaoBaseOjb;

public class ClassificationDutyDaoObjImpl extends PlatformAwareDaoBaseOjb implements ClassificationDutyDao{

	@Override
	public List<ClassificationDuty> getDutyListForClassification(String pmClassificationId) {
		List<ClassificationDuty> dutyList = new ArrayList<ClassificationDuty>();
		Criteria crit = new Criteria();
        crit.addEqualTo("pmPositionClassId", pmClassificationId);

        Query query = QueryFactory.newQuery(ClassificationDuty.class, crit);
        Collection c = this.getPersistenceBrokerTemplate().getCollectionByQuery(query);

        if (c != null) {
            dutyList.addAll(c);
        }
        return dutyList;
	}

}
