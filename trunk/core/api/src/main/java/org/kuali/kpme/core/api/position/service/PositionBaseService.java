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
package org.kuali.kpme.core.api.position.service;

import java.util.List;

import org.joda.time.LocalDate;
import org.kuali.kpme.core.api.position.PositionBaseContract;
import org.springframework.cache.annotation.Cacheable;

public interface PositionBaseService {
    @Cacheable(value= PositionBaseContract.CACHE_NAME, key="'hrPositionId=' + #p0")
	public PositionBaseContract getPosition(String hrPositionId);

    @Cacheable(value= PositionBaseContract.CACHE_NAME, key="'hrPositionNbr=' + #p0  + '|' + 'effectiveDate=' + #p1")
    public PositionBaseContract getPosition(String hrPositionNbr, LocalDate effectiveDate);
    
    List<? extends PositionBaseContract> getPositions(String positionNum, String descr, LocalDate fromEffdt, LocalDate toEffdt, String active, String showHist);
}
