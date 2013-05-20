--
-- Copyright 2004-2013 The Kuali Foundation
--
-- Licensed under the Educational Community License, Version 2.0 (the "License");
-- you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
--
-- http://www.opensource.org/licenses/ecl2.php
--
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
--

delete from PM_INSTITUTION_T where pm_institution_id >= 5000;
delete from KRLC_CMP_T where campus_cd = 'TS';
delete from PM_PSTN_RPT_TYP_T where pm_pstn_rpt_typ_id >= 5000;
delete from PM_PSTN_RPT_CAT_T where pm_pstn_rpt_cat_id >= 5000;
delete from PM_PSTN_RPT_SUB_CAT_T where pm_pstn_rpt_sub_cat_id >= 5000;
delete from PM_PSTN_RPT_GRP_T where pm_pstn_rpt_grp_id >= 5000;
delete from PM_PSTN_QLFCTN_VL_T where PM_PSTN_QLFCTN_VL_ID >= 5000;