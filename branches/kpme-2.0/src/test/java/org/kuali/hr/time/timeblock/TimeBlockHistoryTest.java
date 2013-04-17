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
package org.kuali.hr.time.timeblock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.kuali.hr.test.KPMETestCase;
import org.kuali.hr.time.service.base.TkServiceLocator;
import org.kuali.hr.time.test.TkTestUtils;
import org.kuali.hr.time.timesheet.TimesheetDocument;
import org.kuali.hr.time.util.TKContext;
import org.kuali.hr.time.util.TKUtils;
import org.kuali.hr.time.util.TkConstants;

public class TimeBlockHistoryTest extends KPMETestCase {

    private static DateTime DEFAULT_EFFDT =new DateTime(2010, 1, 1, 0, 0, 0, 0, TKUtils.getSystemDateTimeZone());
    private static DateTime beginDateTime = new DateTime(2010, 1, 1, 8, 0, 0, 0, TKUtils.getSystemDateTimeZone());
    private static DateTime endDateTime = new DateTime(2010, 1, 1, 16, 0, 0, 0, TKUtils.getSystemDateTimeZone());

    @Test
    public void testTimeBlockHistory() throws Exception {
        TimesheetDocument td = TkTestUtils.populateBlankTimesheetDocument(DEFAULT_EFFDT);
        List<TimeBlock> tbs = TkServiceLocator.getTimeBlockService().buildTimeBlocks(td.getAssignments().get(0), "RGH", td, beginDateTime, endDateTime, new BigDecimal("8"), BigDecimal.ZERO, true, false, TKContext.getPrincipalId());
        TkServiceLocator.getTimeBlockService().saveTimeBlocks(td.getTimeBlocks(), tbs, TKContext.getPrincipalId());

        List<TimeBlockHistory> tbhs = new ArrayList<TimeBlockHistory>();
        for(TimeBlock tb : td.getTimeBlocks()) {
            tbhs.add(TkServiceLocator.getTimeBlockHistoryService().getTimeBlockHistoryByTkTimeBlockId(tb.getTkTimeBlockId()));
        }

        for(TimeBlockHistory tbh : tbhs) {
        	Assert.assertEquals(TkConstants.ACTIONS.ADD_TIME_BLOCK, tbh.getActionHistory());
        }
    }
}
