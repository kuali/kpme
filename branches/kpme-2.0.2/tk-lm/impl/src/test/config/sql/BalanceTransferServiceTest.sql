--
-- Copyright 2004-2014 The Kuali Foundation
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

delete from lm_accrual_category_rules_t where lm_accrual_category_rules_id >= 5000;
delete from lm_leave_plan_t where lm_leave_plan_id >= 80000;
delete from hr_principal_attributes_t where hr_principal_attribute_id >= 5000;
delete from lm_accrual_category_t where lm_accrual_category_id >= 5000;
delete from hr_job_t where hr_job_id >= 5000;
delete from lm_leave_document_header_t where document_id >= 5000;
delete from hr_earn_code_t where hr_earn_code_id >= 5000;
delete from hr_calendar_entries_t where hr_calendar_entry_id >= 5000;
delete from lm_leave_block_t where lm_leave_block_id >= 5000;
delete from lm_employee_override_t where lm_employee_override_id >= 3000;
delete from tk_document_header_t where document_id >= 5000;

-- testUser1 employee overrides
insert into lm_employee_override_t values ('3000', 'testUser1', 'ye-xfer-eo', 'testLP', 'MB', 15, NULL,'Y', now(), uuid(), '1', '2011-02-03 12:10:23');
insert into lm_employee_override_t values ('3001', 'testUser1', 'ye-xfer-eo', 'testLP', 'MAC', 5, NULL,'Y', now(), uuid(), '1', '2011-02-03 12:10:23');
insert into lm_employee_override_t values ('3002', 'testUser1', 'ye-xfer-eo', 'testLP', 'MTA', 7, NULL,'Y', now(), uuid(), '1', '2011-02-03 12:10:23');

-- testUser2 employee overrides
insert into lm_employee_override_t values ('3003', 'testUser2', 'ye-xfer-eo', 'testLP', 'MB', 15, NULL,'Y', now(), uuid(), '1', '2011-02-03 12:10:23');
insert into lm_employee_override_t values ('3004', 'testUser2', 'ye-xfer-eo', 'testLP', 'MAC', 5, NULL,'Y', now(), uuid(), '1', '2011-02-03 12:10:23');
insert into lm_employee_override_t values ('3005', 'testUser2', 'ye-xfer-eo', 'testLP', 'MTA', 7, NULL,'Y', now(), uuid(), '1', '2011-02-03 12:10:23');

-- accrual category rules for ACTION = TRANSFER
insert into lm_accrual_category_rules_t (`lm_accrual_category_rules_id`, `SERVICE_UNIT_OF_TIME`, `START_ACC`, `END_ACC`, `ACCRUAL_RATE`, `MAX_BAL`, `MAX_BAL_ACTION_FREQUENCY`, `ACTION_AT_MAX_BAL`, `MAX_BAL_TRANS_ACC_CAT`, `MAX_BAL_TRANS_CONV_FACTOR`, `MAX_TRANS_AMOUNT`, `MAX_PAYOUT_AMOUNT`, `MAX_PAYOUT_EARN_CODE`, `MAX_USAGE`, `MAX_CARRY_OVER`, `LM_ACCRUAL_CATEGORY_ID`, `OBJ_ID`, `VER_NBR`, `ACTIVE`, `TIMESTAMP`, `MAX_BAL_FLAG`) values ('5000', 'M', 0, 888, 16, 15.00, 'OD', 'T', 'od-xfer-dep', 0.5, 10.00, NULL, NULL, NULL, NULL, '5000', 'DEDC243D-4E51-CCDE-1326-E1700B2631E1', '1', 'Y', '2010-02-03 12:10:23', 'Y');
insert into lm_accrual_category_rules_t (`lm_accrual_category_rules_id`, `SERVICE_UNIT_OF_TIME`, `START_ACC`, `END_ACC`, `ACCRUAL_RATE`, `MAX_BAL`, `MAX_BAL_ACTION_FREQUENCY`, `ACTION_AT_MAX_BAL`, `MAX_BAL_TRANS_ACC_CAT`, `MAX_BAL_TRANS_CONV_FACTOR`, `MAX_TRANS_AMOUNT`, `MAX_PAYOUT_AMOUNT`, `MAX_PAYOUT_EARN_CODE`, `MAX_USAGE`, `MAX_CARRY_OVER`, `LM_ACCRUAL_CATEGORY_ID`, `OBJ_ID`, `VER_NBR`, `ACTIVE`, `TIMESTAMP`, `MAX_BAL_FLAG`) values ('5001', 'M', 0, 888, 16, 15.00, 'YE', 'T', 'ye-xfer-dep', 0.5, 10.00, NULL, NULL, NULL, NULL, '5001', 'DEDC243D-4E51-CCDE-1326-E1700B2631E2', '1', 'Y', '2010-02-03 12:10:23', 'Y');
insert into lm_accrual_category_rules_t (`lm_accrual_category_rules_id`, `SERVICE_UNIT_OF_TIME`, `START_ACC`, `END_ACC`, `ACCRUAL_RATE`, `MAX_BAL`, `MAX_BAL_ACTION_FREQUENCY`, `ACTION_AT_MAX_BAL`, `MAX_BAL_TRANS_ACC_CAT`, `MAX_BAL_TRANS_CONV_FACTOR`, `MAX_TRANS_AMOUNT`, `MAX_PAYOUT_AMOUNT`, `MAX_PAYOUT_EARN_CODE`, `MAX_USAGE`, `MAX_CARRY_OVER`, `LM_ACCRUAL_CATEGORY_ID`, `OBJ_ID`, `VER_NBR`, `ACTIVE`, `TIMESTAMP`, `MAX_BAL_FLAG`) values ('5002', 'M', 0, 888, 16, 15.00, 'LA', 'T', 'la-xfer-dep', 0.5, 10.00, NULL, NULL, NULL, NULL, '5002', 'DEDC243D-4E51-CCDE-1326-E1700B2631E3', '1', 'Y', '2010-02-03 12:10:23', 'Y');
insert into lm_accrual_category_rules_t (`lm_accrual_category_rules_id`, `SERVICE_UNIT_OF_TIME`, `START_ACC`, `END_ACC`, `ACCRUAL_RATE`, `MAX_BAL`, `MAX_BAL_ACTION_FREQUENCY`, `ACTION_AT_MAX_BAL`, `MAX_BAL_TRANS_ACC_CAT`, `MAX_BAL_TRANS_CONV_FACTOR`, `MAX_TRANS_AMOUNT`, `MAX_PAYOUT_AMOUNT`, `MAX_PAYOUT_EARN_CODE`, `MAX_USAGE`, `MAX_CARRY_OVER`, `LM_ACCRUAL_CATEGORY_ID`, `OBJ_ID`, `VER_NBR`, `ACTIVE`, `TIMESTAMP`, `MAX_BAL_FLAG`) values ('5003', 'M', 0, 888, 16, 15.00, 'OD', 'T', 'od-xfer-mac-dep', 0.5, 10.00, NULL, NULL, NULL, 10.00, '5003', 'DEDC243D-4E51-CCDE-1326-E1700B2631E4', '1', 'Y', '2010-02-03 12:10:23', 'Y');
insert into lm_accrual_category_rules_t (`lm_accrual_category_rules_id`, `SERVICE_UNIT_OF_TIME`, `START_ACC`, `END_ACC`, `ACCRUAL_RATE`, `MAX_BAL`, `MAX_BAL_ACTION_FREQUENCY`, `ACTION_AT_MAX_BAL`, `MAX_BAL_TRANS_ACC_CAT`, `MAX_BAL_TRANS_CONV_FACTOR`, `MAX_TRANS_AMOUNT`, `MAX_PAYOUT_AMOUNT`, `MAX_PAYOUT_EARN_CODE`, `MAX_USAGE`, `MAX_CARRY_OVER`, `LM_ACCRUAL_CATEGORY_ID`, `OBJ_ID`, `VER_NBR`, `ACTIVE`, `TIMESTAMP`, `MAX_BAL_FLAG`) values ('5004', 'M', 0, 888, 16, 15.00, 'YE', 'T', 'ye-xfer-mac-dep', 0.5, 10.00, NULL, NULL, NULL, 10.00, '5004', 'DEDC243D-4E51-CCDE-1326-E1700B2631E5', '1', 'Y', '2010-02-03 12:10:23', 'Y');
insert into lm_accrual_category_rules_t (`lm_accrual_category_rules_id`, `SERVICE_UNIT_OF_TIME`, `START_ACC`, `END_ACC`, `ACCRUAL_RATE`, `MAX_BAL`, `MAX_BAL_ACTION_FREQUENCY`, `ACTION_AT_MAX_BAL`, `MAX_BAL_TRANS_ACC_CAT`, `MAX_BAL_TRANS_CONV_FACTOR`, `MAX_TRANS_AMOUNT`, `MAX_PAYOUT_AMOUNT`, `MAX_PAYOUT_EARN_CODE`, `MAX_USAGE`, `MAX_CARRY_OVER`, `LM_ACCRUAL_CATEGORY_ID`, `OBJ_ID`, `VER_NBR`, `ACTIVE`, `TIMESTAMP`, `MAX_BAL_FLAG`) values ('5005', 'M', 0, 888, 16, 15.00, 'LA', 'T', 'la-xfer-mac-dep', 0.5, 10.00, NULL, NULL, NULL, 10.00, '5005', 'DEDC243D-4E51-CCDE-1326-E1700B2631E6', '1', 'Y', '2010-02-03 12:10:23', 'Y');
-- accrual category rules for ACTION = LOSE
insert into lm_accrual_category_rules_t (`lm_accrual_category_rules_id`, `SERVICE_UNIT_OF_TIME`, `START_ACC`, `END_ACC`, `ACCRUAL_RATE`, `MAX_BAL`, `MAX_BAL_ACTION_FREQUENCY`, `ACTION_AT_MAX_BAL`, `MAX_BAL_TRANS_ACC_CAT`, `MAX_BAL_TRANS_CONV_FACTOR`, `MAX_TRANS_AMOUNT`, `MAX_PAYOUT_AMOUNT`, `MAX_PAYOUT_EARN_CODE`, `MAX_USAGE`, `MAX_CARRY_OVER`, `LM_ACCRUAL_CATEGORY_ID`, `OBJ_ID`, `VER_NBR`, `ACTIVE`, `TIMESTAMP`, `MAX_BAL_FLAG`) values ('5006', 'M', 0, 888, 16, 15.00, 'OD', 'L', 'od-lose-dep', 0.5, 10.00, NULL, NULL, NULL, NULL, '5006', 'DEDC243D-4E51-CCDE-1326-E1700B2631E7', '1', 'Y', '2010-02-03 12:10:23', 'Y');
insert into lm_accrual_category_rules_t (`lm_accrual_category_rules_id`, `SERVICE_UNIT_OF_TIME`, `START_ACC`, `END_ACC`, `ACCRUAL_RATE`, `MAX_BAL`, `MAX_BAL_ACTION_FREQUENCY`, `ACTION_AT_MAX_BAL`, `MAX_BAL_TRANS_ACC_CAT`, `MAX_BAL_TRANS_CONV_FACTOR`, `MAX_TRANS_AMOUNT`, `MAX_PAYOUT_AMOUNT`, `MAX_PAYOUT_EARN_CODE`, `MAX_USAGE`, `MAX_CARRY_OVER`, `LM_ACCRUAL_CATEGORY_ID`, `OBJ_ID`, `VER_NBR`, `ACTIVE`, `TIMESTAMP`, `MAX_BAL_FLAG`) values ('5007', 'M', 0, 888, 16, 15.00, 'YE', 'L', 'ye-lose-dep', 0.5, 10.00, NULL, NULL, NULL, NULL, '5007', 'DEDC243D-4E51-CCDE-1326-E1700B2631E8', '1', 'Y', '2010-02-03 12:10:23', 'Y');
insert into lm_accrual_category_rules_t (`lm_accrual_category_rules_id`, `SERVICE_UNIT_OF_TIME`, `START_ACC`, `END_ACC`, `ACCRUAL_RATE`, `MAX_BAL`, `MAX_BAL_ACTION_FREQUENCY`, `ACTION_AT_MAX_BAL`, `MAX_BAL_TRANS_ACC_CAT`, `MAX_BAL_TRANS_CONV_FACTOR`, `MAX_TRANS_AMOUNT`, `MAX_PAYOUT_AMOUNT`, `MAX_PAYOUT_EARN_CODE`, `MAX_USAGE`, `MAX_CARRY_OVER`, `LM_ACCRUAL_CATEGORY_ID`, `OBJ_ID`, `VER_NBR`, `ACTIVE`, `TIMESTAMP`, `MAX_BAL_FLAG`) values ('5008', 'M', 0, 888, 16, 15.00, 'LA', 'L', 'la-lose-dep', 0.5, 10.00, NULL, NULL, NULL, NULL, '5008', 'DEDC243D-4E51-CCDE-1326-E1700B2631E9', '1', 'Y', '2010-02-03 12:10:23', 'Y');
insert into lm_accrual_category_rules_t (`lm_accrual_category_rules_id`, `SERVICE_UNIT_OF_TIME`, `START_ACC`, `END_ACC`, `ACCRUAL_RATE`, `MAX_BAL`, `MAX_BAL_ACTION_FREQUENCY`, `ACTION_AT_MAX_BAL`, `MAX_BAL_TRANS_ACC_CAT`, `MAX_BAL_TRANS_CONV_FACTOR`, `MAX_TRANS_AMOUNT`, `MAX_PAYOUT_AMOUNT`, `MAX_PAYOUT_EARN_CODE`, `MAX_USAGE`, `MAX_CARRY_OVER`, `LM_ACCRUAL_CATEGORY_ID`, `OBJ_ID`, `VER_NBR`, `ACTIVE`, `TIMESTAMP`, `MAX_BAL_FLAG`) values ('5009', 'M', 0, 888, 16, 15.00, 'OD', 'L', 'od-lose-mac-dep', 0.5, 10.00, NULL, NULL, NULL, 10.00, '5009', 'DEDC243D-4E51-CCDE-1326-E1700B2631F1', '1', 'Y', '2010-02-03 12:10:23', 'Y');
insert into lm_accrual_category_rules_t (`lm_accrual_category_rules_id`, `SERVICE_UNIT_OF_TIME`, `START_ACC`, `END_ACC`, `ACCRUAL_RATE`, `MAX_BAL`, `MAX_BAL_ACTION_FREQUENCY`, `ACTION_AT_MAX_BAL`, `MAX_BAL_TRANS_ACC_CAT`, `MAX_BAL_TRANS_CONV_FACTOR`, `MAX_TRANS_AMOUNT`, `MAX_PAYOUT_AMOUNT`, `MAX_PAYOUT_EARN_CODE`, `MAX_USAGE`, `MAX_CARRY_OVER`, `LM_ACCRUAL_CATEGORY_ID`, `OBJ_ID`, `VER_NBR`, `ACTIVE`, `TIMESTAMP`, `MAX_BAL_FLAG`) values ('5010', 'M', 0, 888, 16, 15.00, 'YE', 'L', 'ye-lose-mac-dep', 0.5, 10.00, NULL, NULL, NULL, 10.00, '5010', 'DEDC243D-4E51-CCDE-1326-E1700B2631F3', '1', 'Y', '2010-02-03 12:10:23', 'Y');
insert into lm_accrual_category_rules_t (`lm_accrual_category_rules_id`, `SERVICE_UNIT_OF_TIME`, `START_ACC`, `END_ACC`, `ACCRUAL_RATE`, `MAX_BAL`, `MAX_BAL_ACTION_FREQUENCY`, `ACTION_AT_MAX_BAL`, `MAX_BAL_TRANS_ACC_CAT`, `MAX_BAL_TRANS_CONV_FACTOR`, `MAX_TRANS_AMOUNT`, `MAX_PAYOUT_AMOUNT`, `MAX_PAYOUT_EARN_CODE`, `MAX_USAGE`, `MAX_CARRY_OVER`, `LM_ACCRUAL_CATEGORY_ID`, `OBJ_ID`, `VER_NBR`, `ACTIVE`, `TIMESTAMP`, `MAX_BAL_FLAG`) values ('5011', 'M', 0, 888, 16, 15.00, 'LA', 'L', 'la-lose-mac-dep', 0.5, 10.00, NULL, NULL, NULL, 10.00, '5011', 'DEDC243D-4E51-CCDE-1326-E1700B2631F4', '1', 'Y', '2010-02-03 12:10:23', 'Y');
-- accrual category rule whose values get overriden by employee overrides.
insert into lm_accrual_category_rules_t (`lm_accrual_category_rules_id`, `SERVICE_UNIT_OF_TIME`, `START_ACC`, `END_ACC`, `ACCRUAL_RATE`, `MAX_BAL`, `MAX_BAL_ACTION_FREQUENCY`, `ACTION_AT_MAX_BAL`, `MAX_BAL_TRANS_ACC_CAT`, `MAX_BAL_TRANS_CONV_FACTOR`, `MAX_TRANS_AMOUNT`, `MAX_PAYOUT_AMOUNT`, `MAX_PAYOUT_EARN_CODE`, `MAX_USAGE`, `MAX_CARRY_OVER`, `LM_ACCRUAL_CATEGORY_ID`, `OBJ_ID`, `VER_NBR`, `ACTIVE`, `TIMESTAMP`, `MAX_BAL_FLAG`) values ('5012', 'M', 0, 888, 16, 100.00, 'YE', 'T', 'ye-xfer-eo-dep', NULL, NULL, NULL, NULL, NULL, 10.00, '5012', 'DEDC243D-4E51-CCDE-1326-E1700B2631F5', '1', 'Y', '2010-02-03 12:10:23', 'Y');

-- setup leave plan, principal hr attributes, leave eligible jobs, leave calendar.
insert into lm_leave_plan_t (`lm_leave_plan_id`, `LEAVE_PLAN`, `DESCR`, `CAL_YEAR_START`, `EFFDT`, `OBJ_ID`, `VER_NBR`, `ACTIVE`, `TIMESTAMP`, `PLANNING_MONTHS`) values ('80000', 'testLP', 'Test Leave Plan', '02/01', '2010-02-01', '', '1', 'Y', '2010-02-06 11:59:46', '80');

-- Exempt Employee
insert into hr_principal_attributes_t (`hr_principal_attribute_id`, `principal_id`, `pay_calendar`, `leave_plan`, `service_date`, `fmla_eligible`, `workers_eligible`, `timezone`, `EFFDT`, `TIMESTAMP`, `OBJ_ID`, `VER_NBR`, `active`, `leave_calendar`) values('5000', 'testUser1', 'BWS-LM', 'testLP', '2012-03-10', 'Y', 'Y', null, '2012-03-10', now(), uuid(), '1', 'Y', 'BWS-LM');
-- Non-Exempt leave eligible employee
insert into hr_principal_attributes_t (`hr_principal_attribute_id`, `principal_id`, `pay_calendar`, `leave_plan`, `service_date`, `fmla_eligible`, `workers_eligible`, `timezone`, `EFFDT`, `TIMESTAMP`, `OBJ_ID`, `VER_NBR`, `active`, `leave_calendar`) values('5001', 'testUser2', 'BWS-LM', 'testLP', '2010-03-10', 'Y', 'Y', null, '2010-03-10', now(), uuid(), '1', 'Y', 'BWS-CAL');

-- These accrual categories are for testing max balances. Each accrual category has a unique rule defined above.
insert into lm_accrual_category_t (`lm_accrual_category_id`, `ACCRUAL_CATEGORY`, `LEAVE_PLAN`, `DESCR`, `ACCRUAL_INTERVAL_EARN`, `UNIT_OF_TIME`, `EFFDT`, `OBJ_ID`, `VER_NBR`, `PRORATION`, `DONATION`, `SHOW_ON_GRID`, `ACTIVE`, `TIMESTAMP`, `MIN_PERCENT_WORKED`, `EARN_CODE`, `HAS_RULES`) values('5000', 'od-xfer', 'testLP', 'test', 'M', '40', '2010-03-01', '8421CD29-E1F4-4B9A-AE33-F3F4752505CF', '1', 'N', null, 'Y', 'Y',now(), '0', 'EC1', 'Y');
insert into lm_accrual_category_t (`lm_accrual_category_id`, `ACCRUAL_CATEGORY`, `LEAVE_PLAN`, `DESCR`, `ACCRUAL_INTERVAL_EARN`, `UNIT_OF_TIME`, `EFFDT`, `OBJ_ID`, `VER_NBR`, `PRORATION`, `DONATION`, `SHOW_ON_GRID`, `ACTIVE`, `TIMESTAMP`, `MIN_PERCENT_WORKED`, `EARN_CODE`, `HAS_RULES`) values('5001', 'ye-xfer', 'testLP', 'test', 'M', '40', '2010-03-01', '8421CD29-E1F4-4B9A-AE33-F3F4752505D0', '1', 'N', null, 'Y', 'Y',now(), '0', 'EC2', 'Y');
insert into lm_accrual_category_t (`lm_accrual_category_id`, `ACCRUAL_CATEGORY`, `LEAVE_PLAN`, `DESCR`, `ACCRUAL_INTERVAL_EARN`, `UNIT_OF_TIME`, `EFFDT`, `OBJ_ID`, `VER_NBR`, `PRORATION`, `DONATION`, `SHOW_ON_GRID`, `ACTIVE`, `TIMESTAMP`, `MIN_PERCENT_WORKED`, `EARN_CODE`, `HAS_RULES`) values('5002', 'la-xfer', 'testLP', 'test', 'M', '40', '2010-03-01', '8421CD29-E1F4-4B9A-AE33-F3F4752505D1', '1', 'N', null, 'Y', 'Y',now(), '0', 'EC3', 'Y');
insert into lm_accrual_category_t (`lm_accrual_category_id`, `ACCRUAL_CATEGORY`, `LEAVE_PLAN`, `DESCR`, `ACCRUAL_INTERVAL_EARN`, `UNIT_OF_TIME`, `EFFDT`, `OBJ_ID`, `VER_NBR`, `PRORATION`, `DONATION`, `SHOW_ON_GRID`, `ACTIVE`, `TIMESTAMP`, `MIN_PERCENT_WORKED`, `EARN_CODE`, `HAS_RULES`) values('5003', 'od-xfer-mac', 'testLP', 'test', 'M', '40', '2010-03-01', '8421CD29-E1F4-4B9A-AE33-F3F4752505D2', '1', 'N', null, 'Y', 'Y',now(), '0', 'EC4', 'Y');
insert into lm_accrual_category_t (`lm_accrual_category_id`, `ACCRUAL_CATEGORY`, `LEAVE_PLAN`, `DESCR`, `ACCRUAL_INTERVAL_EARN`, `UNIT_OF_TIME`, `EFFDT`, `OBJ_ID`, `VER_NBR`, `PRORATION`, `DONATION`, `SHOW_ON_GRID`, `ACTIVE`, `TIMESTAMP`, `MIN_PERCENT_WORKED`, `EARN_CODE`, `HAS_RULES`) values('5004', 'ye-xfer-mac', 'testLP', 'test', 'M', '40', '2010-03-01', '8421CD29-E1F4-4B9A-AE33-F3F4752505D3', '1', 'N', null, 'Y', 'Y',now(), '0', 'EC5', 'Y');
insert into lm_accrual_category_t (`lm_accrual_category_id`, `ACCRUAL_CATEGORY`, `LEAVE_PLAN`, `DESCR`, `ACCRUAL_INTERVAL_EARN`, `UNIT_OF_TIME`, `EFFDT`, `OBJ_ID`, `VER_NBR`, `PRORATION`, `DONATION`, `SHOW_ON_GRID`, `ACTIVE`, `TIMESTAMP`, `MIN_PERCENT_WORKED`, `EARN_CODE`, `HAS_RULES`) values('5005', 'la-xfer-mac', 'testLP', 'test', 'M', '40', '2010-03-01', '8421CD29-E1F4-4B9A-AE33-F3F4752505D4', '1', 'N', null, 'Y', 'Y',now(), '0', 'EC6', 'Y');
insert into lm_accrual_category_t (`lm_accrual_category_id`, `ACCRUAL_CATEGORY`, `LEAVE_PLAN`, `DESCR`, `ACCRUAL_INTERVAL_EARN`, `UNIT_OF_TIME`, `EFFDT`, `OBJ_ID`, `VER_NBR`, `PRORATION`, `DONATION`, `SHOW_ON_GRID`, `ACTIVE`, `TIMESTAMP`, `MIN_PERCENT_WORKED`, `EARN_CODE`, `HAS_RULES`) values('5006', 'od-lose', 'testLP', 'test', 'M', '40', '2010-03-01', '8421CD29-E1F4-4B9A-AE33-F3F4752505D5', '1', 'N', null, 'Y', 'Y',now(), '0', 'EC7', 'Y');
insert into lm_accrual_category_t (`lm_accrual_category_id`, `ACCRUAL_CATEGORY`, `LEAVE_PLAN`, `DESCR`, `ACCRUAL_INTERVAL_EARN`, `UNIT_OF_TIME`, `EFFDT`, `OBJ_ID`, `VER_NBR`, `PRORATION`, `DONATION`, `SHOW_ON_GRID`, `ACTIVE`, `TIMESTAMP`, `MIN_PERCENT_WORKED`, `EARN_CODE`, `HAS_RULES`) values('5007', 'ye-lose', 'testLP', 'test', 'M', '40', '2010-03-01', '8421CD29-E1F4-4B9A-AE33-F3F4752505D6', '1', 'N', null, 'Y', 'Y',now(), '0', 'EC8', 'Y');
insert into lm_accrual_category_t (`lm_accrual_category_id`, `ACCRUAL_CATEGORY`, `LEAVE_PLAN`, `DESCR`, `ACCRUAL_INTERVAL_EARN`, `UNIT_OF_TIME`, `EFFDT`, `OBJ_ID`, `VER_NBR`, `PRORATION`, `DONATION`, `SHOW_ON_GRID`, `ACTIVE`, `TIMESTAMP`, `MIN_PERCENT_WORKED`, `EARN_CODE`, `HAS_RULES`) values('5008', 'la-lose', 'testLP', 'test', 'M', '40', '2010-03-01', '8421CD29-E1F4-4B9A-AE33-F3F4752505D7', '1', 'N', null, 'Y', 'Y',now(), '0', 'EC9', 'Y');
insert into lm_accrual_category_t (`lm_accrual_category_id`, `ACCRUAL_CATEGORY`, `LEAVE_PLAN`, `DESCR`, `ACCRUAL_INTERVAL_EARN`, `UNIT_OF_TIME`, `EFFDT`, `OBJ_ID`, `VER_NBR`, `PRORATION`, `DONATION`, `SHOW_ON_GRID`, `ACTIVE`, `TIMESTAMP`, `MIN_PERCENT_WORKED`, `EARN_CODE`, `HAS_RULES`) values('5009', 'od-lose-mac', 'testLP', 'test', 'M', '40', '2010-03-01', '8421CD29-E1F4-4B9A-AE33-F3F4752505D8', '1', 'N', null, 'Y', 'Y',now(), '0', 'EC10', 'Y');
insert into lm_accrual_category_t (`lm_accrual_category_id`, `ACCRUAL_CATEGORY`, `LEAVE_PLAN`, `DESCR`, `ACCRUAL_INTERVAL_EARN`, `UNIT_OF_TIME`, `EFFDT`, `OBJ_ID`, `VER_NBR`, `PRORATION`, `DONATION`, `SHOW_ON_GRID`, `ACTIVE`, `TIMESTAMP`, `MIN_PERCENT_WORKED`, `EARN_CODE`, `HAS_RULES`) values('5010', 'ye-lose-mac', 'testLP', 'test', 'M', '40', '2010-03-01', '8421CD29-E1F4-4B9A-AE33-F3F4752505D9', '1', 'N', null, 'Y', 'Y',now(), '0', 'EC11', 'Y');
insert into lm_accrual_category_t (`lm_accrual_category_id`, `ACCRUAL_CATEGORY`, `LEAVE_PLAN`, `DESCR`, `ACCRUAL_INTERVAL_EARN`, `UNIT_OF_TIME`, `EFFDT`, `OBJ_ID`, `VER_NBR`, `PRORATION`, `DONATION`, `SHOW_ON_GRID`, `ACTIVE`, `TIMESTAMP`, `MIN_PERCENT_WORKED`, `EARN_CODE`, `HAS_RULES`) values('5011', 'la-lose-mac', 'testLP', 'test', 'M', '40', '2010-03-01', '8421CD29-E1F4-4B9A-AE33-F3F4752505E0', '1', 'N', null, 'Y', 'Y',now(), '0', 'EC12', 'Y');

-- These accrual categories are for deposits. No rules are currently defined.
insert into lm_accrual_category_t (`lm_accrual_category_id`, `ACCRUAL_CATEGORY`, `LEAVE_PLAN`, `DESCR`, `ACCRUAL_INTERVAL_EARN`, `UNIT_OF_TIME`, `EFFDT`, `OBJ_ID`, `VER_NBR`, `PRORATION`, `DONATION`, `SHOW_ON_GRID`, `ACTIVE`, `TIMESTAMP`, `MIN_PERCENT_WORKED`, `EARN_CODE`, `HAS_RULES`) values('6001', 'od-xfer-dep', 'testLP', 'test', 'M', '40', '2010-03-01', '8421CD29-E1F4-4B9A-AE33-F3F4752505E1', '1', 'N', null, 'Y', 'Y',now(), '0', 'EC1a', 'N');
insert into lm_accrual_category_t (`lm_accrual_category_id`, `ACCRUAL_CATEGORY`, `LEAVE_PLAN`, `DESCR`, `ACCRUAL_INTERVAL_EARN`, `UNIT_OF_TIME`, `EFFDT`, `OBJ_ID`, `VER_NBR`, `PRORATION`, `DONATION`, `SHOW_ON_GRID`, `ACTIVE`, `TIMESTAMP`, `MIN_PERCENT_WORKED`, `EARN_CODE`, `HAS_RULES`) values('6002', 'ye-xfer-dep', 'testLP', 'test', 'M', '40', '2010-03-01', '8421CD29-E1F4-4B9A-AE33-F3F4752505E2', '1', 'N', null, 'Y', 'Y',now(), '0', 'EC2a', 'N');
insert into lm_accrual_category_t (`lm_accrual_category_id`, `ACCRUAL_CATEGORY`, `LEAVE_PLAN`, `DESCR`, `ACCRUAL_INTERVAL_EARN`, `UNIT_OF_TIME`, `EFFDT`, `OBJ_ID`, `VER_NBR`, `PRORATION`, `DONATION`, `SHOW_ON_GRID`, `ACTIVE`, `TIMESTAMP`, `MIN_PERCENT_WORKED`, `EARN_CODE`, `HAS_RULES`) values('6003', 'la-xfer-dep', 'testLP', 'test', 'M', '40', '2010-03-01', '8421CD29-E1F4-4B9A-AE33-F3F4752505E3', '1', 'N', null, 'Y', 'Y',now(), '0', 'EC3a', 'N');
insert into lm_accrual_category_t (`lm_accrual_category_id`, `ACCRUAL_CATEGORY`, `LEAVE_PLAN`, `DESCR`, `ACCRUAL_INTERVAL_EARN`, `UNIT_OF_TIME`, `EFFDT`, `OBJ_ID`, `VER_NBR`, `PRORATION`, `DONATION`, `SHOW_ON_GRID`, `ACTIVE`, `TIMESTAMP`, `MIN_PERCENT_WORKED`, `EARN_CODE`, `HAS_RULES`) values('6004', 'od-xfer-mac-dep', 'testLP', 'test', 'M', '40', '2010-03-01', '8421CD29-E1F4-4B9A-AE33-F3F4752505E4', '1', 'N', null, 'Y', 'Y',now(), '0', 'EC4a', 'N');
insert into lm_accrual_category_t (`lm_accrual_category_id`, `ACCRUAL_CATEGORY`, `LEAVE_PLAN`, `DESCR`, `ACCRUAL_INTERVAL_EARN`, `UNIT_OF_TIME`, `EFFDT`, `OBJ_ID`, `VER_NBR`, `PRORATION`, `DONATION`, `SHOW_ON_GRID`, `ACTIVE`, `TIMESTAMP`, `MIN_PERCENT_WORKED`, `EARN_CODE`, `HAS_RULES`) values('6005', 'ye-xfer-mac-dep', 'testLP', 'test', 'M', '40', '2010-03-01', '8421CD29-E1F4-4B9A-AE33-F3F4752505E5', '1', 'N', null, 'Y', 'Y',now(), '0', 'EC5a', 'N');
insert into lm_accrual_category_t (`lm_accrual_category_id`, `ACCRUAL_CATEGORY`, `LEAVE_PLAN`, `DESCR`, `ACCRUAL_INTERVAL_EARN`, `UNIT_OF_TIME`, `EFFDT`, `OBJ_ID`, `VER_NBR`, `PRORATION`, `DONATION`, `SHOW_ON_GRID`, `ACTIVE`, `TIMESTAMP`, `MIN_PERCENT_WORKED`, `EARN_CODE`, `HAS_RULES`) values('6006', 'la-xfer-mac-dep', 'testLP', 'test', 'M', '40', '2010-03-01', '8421CD29-E1F4-4B9A-AE33-F3F4752505E6', '1', 'N', null, 'Y', 'Y',now(), '0', 'EC6a', 'N');
insert into lm_accrual_category_t (`lm_accrual_category_id`, `ACCRUAL_CATEGORY`, `LEAVE_PLAN`, `DESCR`, `ACCRUAL_INTERVAL_EARN`, `UNIT_OF_TIME`, `EFFDT`, `OBJ_ID`, `VER_NBR`, `PRORATION`, `DONATION`, `SHOW_ON_GRID`, `ACTIVE`, `TIMESTAMP`, `MIN_PERCENT_WORKED`, `EARN_CODE`, `HAS_RULES`) values('6007', 'od-lose-dep', 'testLP', 'test', 'M', '40', '2010-03-01', '8421CD29-E1F4-4B9A-AE33-F3F4752505E7', '1', 'N', null, 'Y', 'Y',now(), '0', 'EC7a', 'N');
insert into lm_accrual_category_t (`lm_accrual_category_id`, `ACCRUAL_CATEGORY`, `LEAVE_PLAN`, `DESCR`, `ACCRUAL_INTERVAL_EARN`, `UNIT_OF_TIME`, `EFFDT`, `OBJ_ID`, `VER_NBR`, `PRORATION`, `DONATION`, `SHOW_ON_GRID`, `ACTIVE`, `TIMESTAMP`, `MIN_PERCENT_WORKED`, `EARN_CODE`, `HAS_RULES`) values('6008', 'ye-lose-dep', 'testLP', 'test', 'M', '40', '2010-03-01', '8421CD29-E1F4-4B9A-AE33-F3F4752505E8', '1', 'N', null, 'Y', 'Y',now(), '0', 'EC8a', 'N');
insert into lm_accrual_category_t (`lm_accrual_category_id`, `ACCRUAL_CATEGORY`, `LEAVE_PLAN`, `DESCR`, `ACCRUAL_INTERVAL_EARN`, `UNIT_OF_TIME`, `EFFDT`, `OBJ_ID`, `VER_NBR`, `PRORATION`, `DONATION`, `SHOW_ON_GRID`, `ACTIVE`, `TIMESTAMP`, `MIN_PERCENT_WORKED`, `EARN_CODE`, `HAS_RULES`) values('6009', 'la-lose-dep', 'testLP', 'test', 'M', '40', '2010-03-01', '8421CD29-E1F4-4B9A-AE33-F3F4752505E9', '1', 'N', null, 'Y', 'Y',now(), '0', 'EC9a', 'N');
insert into lm_accrual_category_t (`lm_accrual_category_id`, `ACCRUAL_CATEGORY`, `LEAVE_PLAN`, `DESCR`, `ACCRUAL_INTERVAL_EARN`, `UNIT_OF_TIME`, `EFFDT`, `OBJ_ID`, `VER_NBR`, `PRORATION`, `DONATION`, `SHOW_ON_GRID`, `ACTIVE`, `TIMESTAMP`, `MIN_PERCENT_WORKED`, `EARN_CODE`, `HAS_RULES`) values('6010', 'od-lose-mac-dep', 'testLP', 'test', 'M', '40', '2010-03-01', '8421CD29-E1F4-4B9A-AE33-F3F4752505F0', '1', 'N', null, 'Y', 'Y',now(), '0', 'EC10a', 'N');
insert into lm_accrual_category_t (`lm_accrual_category_id`, `ACCRUAL_CATEGORY`, `LEAVE_PLAN`, `DESCR`, `ACCRUAL_INTERVAL_EARN`, `UNIT_OF_TIME`, `EFFDT`, `OBJ_ID`, `VER_NBR`, `PRORATION`, `DONATION`, `SHOW_ON_GRID`, `ACTIVE`, `TIMESTAMP`, `MIN_PERCENT_WORKED`, `EARN_CODE`, `HAS_RULES`) values('6011', 'ye-lose-mac-dep', 'testLP', 'test', 'M', '40', '2010-03-01', '8421CD29-E1F4-4B9A-AE33-F3F4752505F1', '1', 'N', null, 'Y', 'Y',now(), '0', 'EC11a', 'N');
insert into lm_accrual_category_t (`lm_accrual_category_id`, `ACCRUAL_CATEGORY`, `LEAVE_PLAN`, `DESCR`, `ACCRUAL_INTERVAL_EARN`, `UNIT_OF_TIME`, `EFFDT`, `OBJ_ID`, `VER_NBR`, `PRORATION`, `DONATION`, `SHOW_ON_GRID`, `ACTIVE`, `TIMESTAMP`, `MIN_PERCENT_WORKED`, `EARN_CODE`, `HAS_RULES`) values('6012', 'la-lose-mac-dep', 'testLP', 'test', 'M', '40', '2010-03-01', '8421CD29-E1F4-4B9A-AE33-F3F4752505F2', '1', 'N', null, 'Y', 'Y',now(), '0', 'EC12a', 'N');

-- Transfer From and Transfer To accrual categories for use with employee overrides
insert into lm_accrual_category_t (`lm_accrual_category_id`, `ACCRUAL_CATEGORY`, `LEAVE_PLAN`, `DESCR`, `ACCRUAL_INTERVAL_EARN`, `UNIT_OF_TIME`, `EFFDT`, `OBJ_ID`, `VER_NBR`, `PRORATION`, `DONATION`, `SHOW_ON_GRID`, `ACTIVE`, `TIMESTAMP`, `MIN_PERCENT_WORKED`, `EARN_CODE`, `HAS_RULES`) values('5012', 'ye-xfer-eo', 'testLP', 'test', 'M', '40', '2010-03-01', '8421CD29-E1F4-4B9A-AE33-F3F4752505F3', '1', 'N', null, 'Y', 'Y',now(), '0', 'EC13', 'Y');
insert into lm_accrual_category_t (`lm_accrual_category_id`, `ACCRUAL_CATEGORY`, `LEAVE_PLAN`, `DESCR`, `ACCRUAL_INTERVAL_EARN`, `UNIT_OF_TIME`, `EFFDT`, `OBJ_ID`, `VER_NBR`, `PRORATION`, `DONATION`, `SHOW_ON_GRID`, `ACTIVE`, `TIMESTAMP`, `MIN_PERCENT_WORKED`, `EARN_CODE`, `HAS_RULES`) values('6013', 'ye-xfer-eo-dep', 'testLP', 'test', 'M', '40', '2010-03-01', '8421CD29-E1F4-4B9A-AE33-F3F4752505F4', '1', 'N', null, 'Y', 'Y',now(), '0', 'EC13a', 'Y');

-- One job for exempt EE, one for Non-Exempt EE.
insert into hr_job_t (`hr_job_id`, `PRINCIPAL_ID`, `JOB_NUMBER`, `EFFDT`, `dept`, `HR_SAL_GROUP`, `pay_grade`, `TIMESTAMP`, `OBJ_ID`, `VER_NBR`, `comp_rate`, `location`, `std_hours`, `hr_paytype`, `active`, `primary_indicator`, `position_nbr`, `eligible_for_leave`, `FLSA_STATUS`) values ('5000', 'testUser1', '19', '2012-03-01', 'TEST-DEPT', 'SD1', 'SD1', now(), uuid(), '1', '0.000000', 'SD1', '40.00', 'BW', 'Y',  'Y', 'N', 'Y', null);
insert into hr_job_t (`hr_job_id`, `PRINCIPAL_ID`, `JOB_NUMBER`, `EFFDT`, `dept`, `HR_SAL_GROUP`, `pay_grade`, `TIMESTAMP`, `OBJ_ID`, `VER_NBR`, `comp_rate`, `location`, `std_hours`, `hr_paytype`, `active`, `primary_indicator`, `position_nbr`, `eligible_for_leave`, `FLSA_STATUS`) values ('5001', 'testUser2', '19', '2010-03-01', 'TEST-DEPT', 'SD1', 'SD1', now(), uuid(), '1', '0.000000', 'SD1', '40.00', 'BW', 'Y',  'Y', 'N', 'Y', 'NE');

-- "Mock" document headers
INSERT INTO lm_leave_document_header_t (`document_id`,`principal_id`,`begin_date`,`end_date`,`document_status`,`obj_id`,`ver_nbr`) values ('5000', 'testUser1', '2012-12-01 00:00:00','2013-01-01 00:00:00', 'S', '7EE387AB-26B0-B6A6-9C4C-5B5F687F0E97', '1');
INSERT INTO lm_leave_document_header_t (`document_id`,`principal_id`,`begin_date`,`end_date`,`document_status`,`obj_id`,`ver_nbr`) values ('5001', 'testUser1', '2013-01-01 00:00:00','2013-02-01 00:00:00', 'S', '7EE387AB-26B0-B6A6-9C4C-5B5F687F0E98', '1');
INSERT INTO lm_leave_document_header_t (`document_id`,`principal_id`,`begin_date`,`end_date`,`document_status`,`obj_id`,`ver_nbr`) values ('5002', 'testUser1', '2012-11-01 00:00:00','2012-12-01 00:00:00', 'S', '7EE387AB-26B0-B6A6-9C4C-5B5F687F0E96', '1');

-- Custom calendar entries for testUser1
insert into hr_calendar_entries_t (`hr_calendar_entry_id`,`hr_calendar_id`,`calendar_name`,`begin_period_date`,`end_period_date`) values ('5000','3','BWS-LM','2012-11-01 00:00:00','2012-12-01 00:00:00');
insert into hr_calendar_entries_t (`hr_calendar_entry_id`,`hr_calendar_id`,`calendar_name`,`begin_period_date`,`end_period_date`) values ('5001','3','BWS-LM','2012-12-01 00:00:00','2013-01-01 00:00:00');
insert into hr_calendar_entries_t (`hr_calendar_entry_id`,`hr_calendar_id`,`calendar_name`,`begin_period_date`,`end_period_date`) values ('5002','3','BWS-LM','2013-01-01 00:00:00','2013-02-01 00:00:00');

-- Timesheet test document headers
insert into tk_document_header_t (`document_id`, `principal_id`, `pay_end_dt`, `document_status`, `pay_begin_dt`, `obj_id`, `ver_nbr`) values ('5003', 'testUser2', '2012-02-05 00:00:00', 'I', '2011-01-22 00:00:00', NULL, '1');
insert into tk_document_header_t (`document_id`, `principal_id`, `pay_end_dt`, `document_status`, `pay_begin_dt`, `obj_id`, `ver_nbr`) values ('5002', 'testUser2', '2012-01-22 00:00:00', 'I', '2011-01-08 00:00:00', NULL, '1');
insert into tk_document_header_t (`document_id`, `principal_id`, `pay_end_dt`, `document_status`, `pay_begin_dt`, `obj_id`, `ver_nbr`) values ('5000', 'testUser2', '2011-12-25 00:00:00', 'I', '2011-12-11 00:00:00', NULL, '1');
insert into tk_document_header_t (`document_id`, `principal_id`, `pay_end_dt`, `document_status`, `pay_begin_dt`, `obj_id`, `ver_nbr`) values ('5001', 'testUser2', '2012-01-08 00:00:00', 'I', '2011-12-25 00:00:00', NULL, '1');

-- Calendar entries defined in src/test/config/db/test/hr_calendar_entries_t.csv

-- underlying earn codes for Transfer From accrual categories
insert into hr_earn_code_t values('5001', 'EC1', 'test', '2010-02-01', 'Y', 'Y', 'B2991ADA-E866-F28C-7E95-A897AC377D0C', '1', now(), 'od-xfer', '1.5', '1.5', 'Hours', 'testLP', 'A', '99', 'T', 'N', 'Y', 'Y', 'Y', 'Y', 'test', null, 'N', 'I', 'N');
insert into hr_earn_code_t values('5002', 'EC2', 'test', '2010-02-01', 'Y', 'Y', 'B2991ADA-E866-F28C-7E95-A897AC377D0C', '1', now(), 'ye-xfer', '1.5', '1.5', 'Hours', 'testLP', 'A', '99', 'T', 'N', 'Y', 'Y', 'Y', 'Y', 'test', null, 'N', 'I', 'N');
insert into hr_earn_code_t values('5003', 'EC3', 'test', '2010-02-01', 'Y', 'Y', 'B2991ADA-E866-F28C-7E95-A897AC377D0C', '1', now(), 'la-xfer', '1.5', '1.5', 'Hours', 'testLP', 'A', '99', 'T', 'N', 'Y', 'Y', 'Y', 'Y', 'test', null, 'N', 'I', 'N');
insert into hr_earn_code_t values('5004', 'EC4', 'test', '2010-02-01', 'Y', 'Y', 'B2991ADA-E866-F28C-7E95-A897AC377D0C', '1', now(), 'od-xfer-mac', '1.5', '1.5', 'Hours', 'testLP', 'A', '99', 'T', 'N', 'Y', 'Y', 'Y', 'Y', 'test', null, 'N', 'I', 'N');
insert into hr_earn_code_t values('5005', 'EC5', 'test', '2010-02-01', 'Y', 'Y', 'B2991ADA-E866-F28C-7E95-A897AC377D0C', '1', now(), 'ye-xfer-mac', '1.5', '1.5', 'Hours', 'testLP', 'A', '99', 'T', 'N', 'Y', 'Y', 'Y', 'Y', 'test', null, 'N', 'I', 'N');
insert into hr_earn_code_t values('5006', 'EC6', 'test', '2010-02-01', 'Y', 'Y', 'B2991ADA-E866-F28C-7E95-A897AC377D0C', '1', now(), 'la-xfer-mac', '1.5', '1.5', 'Hours', 'testLP', 'A', '99', 'T', 'N', 'Y', 'Y', 'Y', 'Y', 'test', null, 'N', 'I', 'N');
insert into hr_earn_code_t values('5007', 'EC7', 'test', '2010-02-01', 'Y', 'Y', 'B2991ADA-E866-F28C-7E95-A897AC377D0C', '1', now(), 'od-lose', '1.5', '1.5', 'Hours', 'testLP', 'A', '99', 'T', 'N', 'Y', 'Y', 'Y', 'Y', 'test', null, 'N', 'I', 'N');
insert into hr_earn_code_t values('5008', 'EC8', 'test', '2010-02-01', 'Y', 'Y', 'B2991ADA-E866-F28C-7E95-A897AC377D0C', '1', now(), 'ye-lose', '1.5', '1.5', 'Hours', 'testLP', 'A', '99', 'T', 'N', 'Y', 'Y', 'Y', 'Y', 'test', null, 'N', 'I', 'N');
insert into hr_earn_code_t values('5009', 'EC9', 'test', '2010-02-01', 'Y', 'Y', 'B2991ADA-E866-F28C-7E95-A897AC377D0C', '1', now(), 'la-lose', '1.5', '1.5', 'Hours', 'testLP', 'A', '99', 'T', 'N', 'Y', 'Y', 'Y', 'Y', 'test', null, 'N', 'I', 'N');
insert into hr_earn_code_t values('5010', 'EC10', 'test', '2010-02-01', 'Y', 'Y', 'B2991ADA-E866-F28C-7E95-A897AC377D0C', '1', now(), 'od-lose-mac', '1.5', '1.5', 'Hours', 'testLP', 'A', '99', 'T', 'N', 'Y', 'Y', 'Y', 'Y', 'test', null, 'N', 'I', 'N');
insert into hr_earn_code_t values('5011', 'EC11', 'test', '2010-02-01', 'Y', 'Y', 'B2991ADA-E866-F28C-7E95-A897AC377D0C', '1', now(), 'ye-lose-mac', '1.5', '1.5', 'Hours', 'testLP', 'A', '99', 'T', 'N', 'Y', 'Y', 'Y', 'Y', 'test', null, 'N', 'I', 'N');
insert into hr_earn_code_t values('5012', 'EC12', 'test', '2010-02-01', 'Y', 'Y', 'B2991ADA-E866-F28C-7E95-A897AC377D0C', '1', now(), 'la-lose-mac', '1.5', '1.5', 'Hours', 'testLP', 'A', '99', 'T', 'N', 'Y', 'Y', 'Y', 'Y', 'test', null, 'N', 'I', 'N');

-- Underlying earn codes for transfer TO accrual categories.
insert into hr_earn_code_t values('6001', 'EC1a', 'test', '2010-02-01', 'Y', 'Y', 'B2991ADA-E866-F28C-7E95-A897AC377D0C', '1', now(), 'od-xfer-dep', '1.5', '1.5', 'Hours', 'testLP', 'A', '99', 'T', 'N', 'Y', 'Y', 'Y', 'Y', 'test', null, 'N', 'I', 'N');
insert into hr_earn_code_t values('6002', 'EC2a', 'test', '2010-02-01', 'Y', 'Y', 'B2991ADA-E866-F28C-7E95-A897AC377D0C', '1', now(), 'ye-xfer-dep', '1.5', '1.5', 'Hours', 'testLP', 'A', '99', 'T', 'N', 'Y', 'Y', 'Y', 'Y', 'test', null, 'N', 'I', 'N');
insert into hr_earn_code_t values('6003', 'EC3a', 'test', '2010-02-01', 'Y', 'Y', 'B2991ADA-E866-F28C-7E95-A897AC377D0C', '1', now(), 'la-xfer-dep', '1.5', '1.5', 'Hours', 'testLP', 'A', '99', 'T', 'N', 'Y', 'Y', 'Y', 'Y', 'test', null, 'N', 'I', 'N');
insert into hr_earn_code_t values('6004', 'EC4a', 'test', '2010-02-01', 'Y', 'Y', 'B2991ADA-E866-F28C-7E95-A897AC377D0C', '1', now(), 'od-xfer-mac-dep', '1.5', '1.5', 'Hours', 'testLP', 'A', '99', 'T', 'N', 'Y', 'Y', 'Y', 'Y', 'test', null, 'N', 'I', 'N');
insert into hr_earn_code_t values('6005', 'EC5a', 'test', '2010-02-01', 'Y', 'Y', 'B2991ADA-E866-F28C-7E95-A897AC377D0C', '1', now(), 'ye-xfer-mac-dep', '1.5', '1.5', 'Hours', 'testLP', 'A', '99', 'T', 'N', 'Y', 'Y', 'Y', 'Y', 'test', null, 'N', 'I', 'N');
insert into hr_earn_code_t values('6006', 'EC6a', 'test', '2010-02-01', 'Y', 'Y', 'B2991ADA-E866-F28C-7E95-A897AC377D0C', '1', now(), 'la-xfer-mac-dep', '1.5', '1.5', 'Hours', 'testLP', 'A', '99', 'T', 'N', 'Y', 'Y', 'Y', 'Y', 'test', null, 'N', 'I', 'N');
insert into hr_earn_code_t values('6007', 'EC7a', 'test', '2010-02-01', 'Y', 'Y', 'B2991ADA-E866-F28C-7E95-A897AC377D0C', '1', now(), 'od-lose-dep', '1.5', '1.5', 'Hours', 'testLP', 'A', '99', 'T', 'N', 'Y', 'Y', 'Y', 'Y', 'test', null, 'N', 'I', 'N');
insert into hr_earn_code_t values('6008', 'EC8a', 'test', '2010-02-01', 'Y', 'Y', 'B2991ADA-E866-F28C-7E95-A897AC377D0C', '1', now(), 'ye-lose-dep', '1.5', '1.5', 'Hours', 'testLP', 'A', '99', 'T', 'N', 'Y', 'Y', 'Y', 'Y', 'test', null, 'N', 'I', 'N');
insert into hr_earn_code_t values('6009', 'EC9a', 'test', '2010-02-01', 'Y', 'Y', 'B2991ADA-E866-F28C-7E95-A897AC377D0C', '1', now(), 'la-lose-dep', '1.5', '1.5', 'Hours', 'testLP', 'A', '99', 'T', 'N', 'Y', 'Y', 'Y', 'Y', 'test', null, 'N', 'I', 'N');
insert into hr_earn_code_t values('6010', 'EC10a', 'test', '2010-02-01', 'Y', 'Y', 'B2991ADA-E866-F28C-7E95-A897AC377D0C', '1', now(), 'od-lose-mac-dep', '1.5', '1.5', 'Hours', 'testLP', 'A', '99', 'T', 'N', 'Y', 'Y', 'Y', 'Y', 'test', null, 'N', 'I', 'N');
insert into hr_earn_code_t values('6011', 'EC11a', 'test', '2010-02-01', 'Y', 'Y', 'B2991ADA-E866-F28C-7E95-A897AC377D0C', '1', now(), 'ye-lose-mac-dep', '1.5', '1.5', 'Hours', 'testLP', 'A', '99', 'T', 'N', 'Y', 'Y', 'Y', 'Y', 'test', null, 'N', 'I', 'N');
insert into hr_earn_code_t values('6012', 'EC12a', 'test', '2010-02-01', 'Y', 'Y', 'B2991ADA-E866-F28C-7E95-A897AC377D0C', '1', now(), 'la-lose-mac-dep', '1.5', '1.5', 'Hours', 'testLP', 'A', '99', 'T', 'N', 'Y', 'Y', 'Y', 'Y', 'test', null, 'N', 'I', 'N');

-- Underlying earn code for use with accrual categories being overriden by employee overrides. 
insert into hr_earn_code_t values('7000', 'EC13', 'test', '2010-02-01', 'Y', 'Y', 'B2991ADA-E866-F28C-7E95-A897AC377D0C', '1', now(), 'ye-xfer-eo', '1.5', '1.5', 'Hours', 'testLP', 'A', '99', 'T', 'N', 'Y', 'Y', 'Y', 'Y', 'test', null, 'N', 'I', 'N');