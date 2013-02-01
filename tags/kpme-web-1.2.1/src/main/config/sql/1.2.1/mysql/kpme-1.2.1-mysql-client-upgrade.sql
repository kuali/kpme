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

--  *********************************************************************
--  Update Database Script
--  *********************************************************************
--  Change Log: src/main/config/db/db.changelog-main-upgrade.xml
--  Ran at: 1/31/13 8:16 AM
--  Against: kpmedev@localhost@jdbc:mysql://localhost:3306/kpmedev
--  Liquibase version: 2.0.5
--  *********************************************************************

--  Changeset src/main/config/db/1.2.1/db.changelog-201210081204.xml::1::dgodfrey::(Checksum: 3:ed0c25abdf0760d1dfff0411a00cac41)
--  Remove unneeded column TK_GRACE_PERIOD_RL_T.GRACE_MINS
ALTER TABLE `TK_GRACE_PERIOD_RL_T` DROP COLUMN `GRACE_MINS`;

--  Changeset src/main/config/db/1.2.1/db.changelog-201210091100.xml::1::yingzhou::(Checksum: 3:1f9b64f55f155e48db5115c07f817d89)
--  KPME-1424
ALTER TABLE `TK_CLOCK_LOG_T` ADD `UNAPPROVED_IP` VARCHAR(1) DEFAULT 'N';

--  Changeset src/main/config/db/1.2.1/db.changelog-201210101049.xml::1::dgodfrey::(Checksum: 3:d4b1e9b4e55396bb83025b4416da2191)
--  Remove Org_admin from HR_EARN_CODE_SECURITY_T
ALTER TABLE `HR_EARN_CODE_SECURITY_T` DROP COLUMN `ORG_ADMIN`;

--  Changeset src/main/config/db/1.2.1/db.changelog-201210181416.xml::1::kbtaylor::(Checksum: 3:b60c272e92082465927ab7dc11285e13)
--  KPME-1490: Remove foreign key ids from linkages requiring effective date matchings
ALTER TABLE `HR_ROLES_T` DROP COLUMN `TK_WORK_AREA_ID`;

ALTER TABLE `TK_CLOCK_LOG_T` DROP COLUMN `HR_JOB_ID`;

ALTER TABLE `TK_CLOCK_LOG_T` DROP COLUMN `TK_TASK_ID`;

ALTER TABLE `TK_CLOCK_LOG_T` DROP COLUMN `TK_WORK_AREA_ID`;

ALTER TABLE `TK_TASK_T` DROP COLUMN `TK_WORK_AREA_ID`;

ALTER TABLE `TK_TIME_BLOCK_T` DROP COLUMN `HR_JOB_ID`;

ALTER TABLE `TK_TIME_BLOCK_T` DROP COLUMN `TK_TASK_ID`;

ALTER TABLE `TK_TIME_BLOCK_T` DROP COLUMN `TK_WORK_AREA_ID`;

ALTER TABLE `TK_TIME_BLOCK_HIST_T` DROP COLUMN `HR_JOB_ID`;

ALTER TABLE `TK_TIME_BLOCK_HIST_T` DROP COLUMN `TK_TASK_ID`;

ALTER TABLE `TK_TIME_BLOCK_HIST_T` DROP COLUMN `TK_WORK_AREA_ID`;

--  Changeset src/main/config/db/1.2.1/db.changelog-201210301519.xml::1::kbtaylor::(Checksum: 3:0cb7f51bdae3756dfa411663955e585d)
--  KPME-1893: Adding indexes for performance enhancement
CREATE UNIQUE INDEX `HR_EARN_CODE_SECURITY_IDX1` ON `HR_EARN_CODE_SECURITY_T`(`DEPT`, `HR_SAL_GROUP`, `LOCATION`, `EARN_CODE`, `EFFDT`, `TIMESTAMP`);

CREATE UNIQUE INDEX `HR_ROLES_IDX1` ON `HR_ROLES_T`(`PRINCIPAL_ID`, `ROLE_NAME`, `WORK_AREA`, `DEPT`, `CHART`, `EFFDT`, `TIMESTAMP`);

CREATE INDEX `HR_ROLES_IDX2` ON `HR_ROLES_T`(`POSITION_NBR`, `ROLE_NAME`, `WORK_AREA`, `DEPT`, `CHART`, `EFFDT`, `TIMESTAMP`);

CREATE UNIQUE INDEX `TK_ASSIGNMENT_IDX1` ON `TK_ASSIGNMENT_T`(`PRINCIPAL_ID`, `JOB_NUMBER`, `WORK_AREA`, `TASK`, `EFFDT`, `TIMESTAMP`);

CREATE UNIQUE INDEX `TK_ASSIGNMENT_IDX2` ON `TK_ASSIGNMENT_T`(`PRINCIPAL_ID`, `JOB_NUMBER`, `WORK_AREA`, `TASK`, `EFFDT`, `TIMESTAMP`, `ACTIVE`);

CREATE INDEX `TK_ASSIGN_ACCT_IDX1` ON `TK_ASSIGN_ACCT_T`(`TK_ASSIGNMENT_ID`);

CREATE UNIQUE INDEX `TK_CLOCK_LOG_IDX1` ON `TK_CLOCK_LOG_T`(`PRINCIPAL_ID`, `JOB_NUMBER`, `WORK_AREA`, `TASK`, `CLOCK_TS`, `CLOCK_TS_TZ`, `CLOCK_ACTION`, `TIMESTAMP`);

CREATE INDEX `TK_DOCUMENT_HEADER_IDX1` ON `TK_DOCUMENT_HEADER_T`(`PRINCIPAL_ID`, `PAY_END_DT`, `PAY_BEGIN_DT`);

CREATE UNIQUE INDEX `TK_GRACE_PERIOD_RL_IDX1` ON `TK_GRACE_PERIOD_RL_T`(`EFFDT`, `TIMESTAMP`);

CREATE INDEX `TK_HOUR_DETAIL_IDX1` ON `TK_HOUR_DETAIL_T`(`TK_TIME_BLOCK_ID`);

CREATE INDEX `TK_SHIFT_DIFFERENTIAL_RL_IDX1` ON `TK_SHIFT_DIFFERENTIAL_RL_T`(`LOCATION`, `HR_SAL_GROUP`, `PAY_GRADE`, `EARN_CODE`, `EFFDT`, `TIMESTAMP`);

CREATE UNIQUE INDEX `TK_SYSTEM_LUNCH_RL_IDX1` ON `TK_SYSTEM_LUNCH_RL_T`(`EFFDT`, `TIMESTAMP`);

CREATE UNIQUE INDEX `TK_TASK_IDX1` ON `TK_TASK_T`(`TASK`, `WORK_AREA`, `EFFDT`, `TIMESTAMP`);

CREATE UNIQUE INDEX `TK_TASK_IDX2` ON `TK_TASK_T`(`TASK`, `WORK_AREA`, `EFFDT`, `TIMESTAMP`, `ACTIVE`);

CREATE UNIQUE INDEX `TK_TIME_BLOCK_IDX1` ON `TK_TIME_BLOCK_T`(`DOCUMENT_ID`, `JOB_NUMBER`, `WORK_AREA`, `TASK`, `EARN_CODE`, `BEGIN_TS`, `END_TS`, `TIMESTAMP`, `CLOCK_LOG_CREATED`);

CREATE INDEX `TK_TIME_BLOCK_IDX2` ON `TK_TIME_BLOCK_T`(`PRINCIPAL_ID`);

CREATE INDEX `TK_TIME_BLOCK_IDX3` ON `TK_TIME_BLOCK_T`(`END_TS`);

CREATE INDEX `TK_TIME_BLOCK_HIST_IDX1` ON `TK_TIME_BLOCK_HIST_T`(`TK_TIME_BLOCK_ID`);

CREATE INDEX `TK_TIME_BLOCK_HIST_IDX2` ON `TK_TIME_BLOCK_HIST_T`(`DOCUMENT_ID`);

CREATE INDEX `TK_TIME_BLOCK_HIST_DETAIL_IDX1` ON `TK_TIME_BLOCK_HIST_DETAIL_T`(`TK_TIME_BLOCK_HIST_ID`);

CREATE UNIQUE INDEX `TK_TIME_COLLECTION_RL_IDX1` ON `TK_TIME_COLLECTION_RL_T`(`DEPT`, `WORK_AREA`, `EFFDT`, `TIMESTAMP`);

CREATE UNIQUE INDEX `TK_WORK_AREA_IDX1` ON `TK_WORK_AREA_T`(`WORK_AREA`, `EFFDT`, `TIMESTAMP`);

CREATE UNIQUE INDEX `TK_WORK_AREA_IDX2` ON `TK_WORK_AREA_T`(`WORK_AREA`, `EFFDT`, `TIMESTAMP`, `ACTIVE`);
