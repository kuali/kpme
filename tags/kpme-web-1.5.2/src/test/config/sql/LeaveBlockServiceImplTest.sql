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

/** create system scheduled time off data */
delete from lm_sys_schd_timeoff_t where LM_SYS_SCHD_TIMEOFF_ID = '3000';
delete from lm_sys_schd_timeoff_t where LM_SYS_SCHD_TIMEOFF_ID = '3001';
delete from lm_accrual_category_t where lm_accrual_category_id = '3000';
delete from lm_leave_block_t where lm_leave_block_id >= '1000';
delete from lm_leave_block_hist_t where lm_leave_block_hist_id = '1000';

insert into lm_sys_schd_timeoff_t values ('3000', 'testLP', 'testAC', 'testLC', '2012-01-01', NULL, 'testLocation','testSSTO', '3', null, null, null, 'testH', '2012-03-01', uuid(), '1', 'Y', now());
insert into lm_sys_schd_timeoff_t values ('3001', 'InactiveLP', 'testAC', 'testLC', '2012-01-01', NULL, 'testLocation','testSSTO', '3', null, null, null,'testH', '2012-03-01', uuid(), '1', 'N', now());
insert into lm_accrual_category_t (`lm_accrual_category_id`, `ACCRUAL_CATEGORY`, `LEAVE_PLAN`, `DESCR`, `ACCRUAL_INTERVAL_EARN`, `UNIT_OF_TIME`, `EFFDT`, `OBJ_ID`, `VER_NBR`, `PRORATION`, `DONATION`, `SHOW_ON_GRID`, `ACTIVE`, `TIMESTAMP`, `MIN_PERCENT_WORKED`, `EARN_CODE`, `HAS_RULES`) values('3000', 'testAC', 'testLP', 'test', '', '', '2010-01-01', '8421CD29-E1F4-4B9A-AE33-F3F4752505CE', '1', null, null, null, 'Y',now(), '1.5', 'LC-DEFAULT', 'Y');

/** Leave block */
insert into lm_leave_block_t (`lm_leave_block_id`, `leave_date`,`description`,  `principal_id` , `earn_code` ,`lm_sys_schd_timeoff_id` ,`accrual_category` ,`leave_amount`, `apply_to_ytd_used`, `document_id`, `accrual_generated`, `block_id`,`ver_nbr`, `obj_id`, `timestamp`,`principal_id_modified`, `tk_assignment_id`, `request_status`, `leave_block_type`, `job_number`, `work_area`, `task` ) values('1000', '2012-03-01', 'Test Description', 'admin', 'testLC', null, 'testAC', '8', 'Appy to ytd', '12546', 'A', '0','1','B2991ADA-E866-F28C-7E95-A897AC377D0C', '2010-01-01 08:08:08', 'admin',  '1100', 'P', 'LC', '0', '12345', '1');
insert into lm_leave_block_t (`lm_leave_block_id`, `leave_date`,`description`,  `principal_id` , `earn_code` ,`lm_sys_schd_timeoff_id` ,`accrual_category` ,`leave_amount`, `apply_to_ytd_used`, `document_id`, `accrual_generated`, `block_id`,`ver_nbr`, `obj_id`, `timestamp`,`principal_id_modified`, `tk_assignment_id`, `request_status`, `leave_block_type`, `job_number`, `work_area`, `task` ) values('1001', '2012-03-02', 'Test Description', 'admin', 'testLC', null, 'testAC', '8', 'Appy to ytd', '12546', 'A', '0','1','B2991ADA-E866-F28C-7E95-A897AC377D0C', '2010-01-01 08:08:08', 'admin',  '1100', 'P', 'LC', '0', '12345', '0');
insert into lm_leave_block_t (`lm_leave_block_id`, `leave_date`,`description`,  `principal_id` , `earn_code` ,`lm_sys_schd_timeoff_id` ,`accrual_category` ,`leave_amount`, `apply_to_ytd_used`, `document_id`, `accrual_generated`, `block_id`,`ver_nbr`, `obj_id`, `timestamp`,`principal_id_modified`, `tk_assignment_id`, `request_status`, `leave_block_type`, `job_number`, `work_area`, `task` ) values('1002', '2012-03-03', 'Test Description', 'admin', 'testLC', null, 'testAC', '8', 'Appy to ytd', '12546', 'A', '0','1','B2991ADA-E866-F28C-7E95-A897AC377D0C', '2010-01-01 08:08:08', 'admin',  '1100', 'P', 'TC', '0', '12345', '0');
insert into lm_leave_block_t (`lm_leave_block_id`, `leave_date`,`description`,  `principal_id` , `earn_code` ,`lm_sys_schd_timeoff_id` ,`accrual_category` ,`leave_amount`, `apply_to_ytd_used`, `document_id`, `accrual_generated`, `block_id`,`ver_nbr`, `obj_id`, `timestamp`,`principal_id_modified`, `tk_assignment_id`, `request_status`, `leave_block_type`, `job_number`, `work_area`, `task` ) values('1003', '2012-03-05', 'Test Description', 'admin', 'testLC', null, 'testAC', '8', 'Appy to ytd', '12546', 'A', '0','1','B2991ADA-E866-F28C-7E95-A897AC377D0C', '2010-01-01 08:08:08', 'admin',  '1100', 'P', 'TC', '1', '12345', '0');
insert into lm_leave_block_t (`lm_leave_block_id`, `leave_date`,`description`,  `principal_id` , `earn_code` ,`lm_sys_schd_timeoff_id` ,`accrual_category` ,`leave_amount`, `apply_to_ytd_used`, `document_id`, `accrual_generated`, `block_id`,`ver_nbr`, `obj_id`, `timestamp`,`principal_id_modified`, `tk_assignment_id`, `request_status`, `leave_block_type`, `job_number`, `work_area`, `task` ) values('1004', '2012-03-04', 'Test Description', 'admin', 'testLC', null, 'testAC', '8', 'Appy to ytd', '12546', 'A', '0','1','B2991ADA-E866-F28C-7E95-A897AC377D0C', '2010-01-01 08:08:08', 'admin',  '1100', 'P', 'AS', '2', '23456', '2');
insert into lm_leave_block_t (`lm_leave_block_id`, `leave_date`,`description`,  `principal_id` , `earn_code` ,`lm_sys_schd_timeoff_id` ,`accrual_category` ,`leave_amount`, `apply_to_ytd_used`, `document_id`, `accrual_generated`, `block_id`,`ver_nbr`, `obj_id`, `timestamp`,`principal_id_modified`, `tk_assignment_id`, `request_status`, `leave_block_type`, `job_number`, `work_area`, `task` ) values('1005', '2012-03-05', 'Test Description', 'admin', 'testLC', '12345', 'testAC', '-8', 'Appy to ytd', '12546', 'A', '0','1','B2991ADA-E866-F28C-7E95-A897AC377D0C', '2010-01-01 08:08:08', 'admin',  '1100', 'P', 'AS', null, null, null);

/** Carry Over Leave blocks */
insert into lm_leave_block_t (`lm_leave_block_id`, `leave_date`,`description`,  `principal_id` , `earn_code` ,`lm_sys_schd_timeoff_id` ,`accrual_category` ,`leave_amount`, `apply_to_ytd_used`, `document_id`, `accrual_generated`, `block_id`,`ver_nbr`, `obj_id`, `timestamp`,`principal_id_modified`, `tk_assignment_id`, `request_status`, `leave_block_type`, `job_number`, `work_area`, `task` ) values('1010', '2011-12-31', 'testAC Carryover', 'admin', 'testLC', null, 'testAC', '56', 'carry over', '12546', 'A', '0','1','B2991ADA-E866-F28C-7E95-A897AC377D0D', '2011-12-31 08:08:08', 'admin',  '1100', 'A', 'CO', null, null, null);


/** Leave Block history */
insert into lm_leave_block_hist_t (`LM_LEAVE_BLOCK_HIST_ID`, `LM_LEAVE_BLOCK_ID`, `LEAVE_DATE`, `DESCRIPTION`, `PRINCIPAL_ID`, `EARN_CODE`, `LM_SYS_SCHD_TIMEOFF_ID`, `ACCRUAL_CATEGORY`, `TK_ASSIGNMENT_ID`, `LEAVE_AMOUNT`, `APPLY_TO_YTD_USED`, `DOCUMENT_ID`, `PRINCIPAL_ID_MODIFIED`, `TIMESTAMP`, `PRINCIPAL_ID_DELETED`, `TIMESTAMP_DELETED`, `BLOCK_ID`, `ACCRUAL_GENERATED`, `REQUEST_STATUS`, `ACTION`, `VER_NBR`, `OBJ_ID`, `LEAVE_BLOCK_TYPE`) values ('1000','1000', '2012-03-01', 'Test Description', 'admin', 'testLC', 3000, 'testAC', 1100, '8', 'Apply to ytd', '12546', null,'2010-01-01 08:08:08', null, null,0, 'A', 'P', 'A', '1', 'B2991ADA-E866-F28C-7E95-A897AC377D0C', 'LC' );