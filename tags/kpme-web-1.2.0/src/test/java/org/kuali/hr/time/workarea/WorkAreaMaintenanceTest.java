/**
 * Copyright 2004-2012 The Kuali Foundation
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
package org.kuali.hr.time.workarea;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.junit.Assert;
import org.junit.Test;
import org.kuali.hr.test.KPMETestCase;
import org.kuali.hr.time.service.base.TkServiceLocator;
import org.kuali.hr.time.test.HtmlUnitUtil;
import org.kuali.hr.time.util.TKUtils;
import org.kuali.hr.time.util.TkConstants;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.sql.Date;

public class WorkAreaMaintenanceTest extends KPMETestCase {

	@Test
	public void testWorkAreaMaintenanceScreen() throws Exception{
    	String baseUrl = HtmlUnitUtil.getBaseURL() + "/kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.hr.time.workarea.WorkArea&returnLocation=" + HtmlUnitUtil.getBaseURL() + "/portal.do&hideReturnLink=true&docFormKey=88888888";
    	HtmlPage page = HtmlUnitUtil.gotoPageAndLogin(baseUrl);
    	setFieldValue(page,"workArea","30");
    	page = HtmlUnitUtil.clickInputContainingText(page, "search");
    	page = HtmlUnitUtil.clickAnchorContainingText(page, "edit");
    	Assert.assertTrue("Test that maintenance screen rendered", page.asText().contains("30"));
	}

	@Override
	public void setUp() throws Exception {
		super.setUp();
		WorkArea workArea = new WorkArea();
		workArea.setTkWorkAreaId("1111");
		workArea.setWorkArea(4444L);
		workArea.setOvertimeEditRole(TkConstants.ROLE_TK_EMPLOYEE);
		workArea.setEffectiveDate(new Date(System.currentTimeMillis()));
		KRADServiceLocator.getBusinessObjectService().save(workArea);
	}

	@Override
	public void tearDown() throws Exception {
		WorkArea workArea = (WorkArea)KRADServiceLocator.getBusinessObjectService().findBySinglePrimaryKey(WorkArea.class, 1111);
		KRADServiceLocator.getBusinessObjectService().delete(workArea);
		super.tearDown();
	}

	@Test
	public void testWorkAreaFetch() throws Exception{
		WorkArea workArea = TkServiceLocator.getWorkAreaService().getWorkArea(1234L, TKUtils.getCurrentDate());
		Assert.assertTrue("Work area is not null and valid", workArea != null && workArea.getWorkArea().longValue() == 1234L);
	}

}