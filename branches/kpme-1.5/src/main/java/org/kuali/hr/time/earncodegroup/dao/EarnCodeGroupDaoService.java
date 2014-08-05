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
package org.kuali.hr.time.earncodegroup.dao;

import org.kuali.hr.time.earncodegroup.EarnCodeGroup;

import java.sql.Date;

public interface EarnCodeGroupDaoService {
	public EarnCodeGroup getEarnCodeGroup(String earnCodeGroup, Date asOfDate);
	public EarnCodeGroup getEarnCodeGroupForEarnCode(String earnCode, Date asOfDate);
	public EarnCodeGroup getEarnCodeGroupSummaryForEarnCode(String earnCode, Date asOfDate);
	public EarnCodeGroup getEarnCodeGroup(String hrEarnGroupId);
	public int getEarnCodeGroupCount(String earnGroup);
	public int getNewerEarnCodeGroupCount(String earnGroup, Date effdt);
}