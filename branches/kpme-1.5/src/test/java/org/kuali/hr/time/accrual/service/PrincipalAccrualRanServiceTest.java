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
package org.kuali.hr.time.accrual.service;

import java.sql.Date;
import java.text.SimpleDateFormat;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.kuali.hr.lm.accrual.PrincipalAccrualRan;
import org.kuali.hr.test.KPMETestCase;
import org.kuali.hr.time.service.base.TkServiceLocator;
import org.kuali.hr.time.util.TKUtils;
import org.kuali.hr.time.util.TkConstants;

public class PrincipalAccrualRanServiceTest extends KPMETestCase {
	
	@Test
	public void testUpdateInfo() {
		// the database has an entry for principalId testUser dated 2012-05-01
		// run accrual service for testUser
		// the principalAccrualRan entry in database should be changed to today's timestamp
		PrincipalAccrualRan par = TkServiceLocator.getPrincipalAccrualRanService().getLastPrincipalAccrualRan("testUser");
		Assert.assertNotNull("There should be one entry in PrincipalAccrualRan table for 'testUser'", par);
		Date aDate = new Date(par.getLastRanTs().getTime());
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		Assert.assertTrue("Date of the original entry in PrincipalAccrualRan for 'testUser' should be 05/01/2012"
				, formatter.format(aDate).equals("05/01/2012"));
		
		Date startDate = new Date((new DateTime(2012, 2, 20, 0, 0, 0, 0, TKUtils.getSystemDateTimeZone())).getMillis());
		Date endDate = new Date((new DateTime(2012, 5, 3, 0, 0, 0, 0, TKUtils.getSystemDateTimeZone())).getMillis());
		
		boolean rerunFlag = TkServiceLocator.getLeaveAccrualService().statusChangedSinceLastRun("testUser");
		Assert.assertTrue("Status should be changed for 'testUser'", rerunFlag);
		
		TkServiceLocator.getLeaveAccrualService().runAccrual("testUser", startDate, endDate, true);
		par = TkServiceLocator.getPrincipalAccrualRanService().getLastPrincipalAccrualRan("testUser");
		aDate = new Date(par.getLastRanTs().getTime());
		Assert.assertTrue("Date of the original entry in PrincipalAccrualRan for 'testUser' should be current date"
				, formatter.format(aDate).equals(formatter.format(TKUtils.getCurrentDate())));
		
	}

}
