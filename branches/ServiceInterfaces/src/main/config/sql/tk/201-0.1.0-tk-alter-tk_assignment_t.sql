ALTER TABLE `tk_assignment_t`       ADD COLUMN `EARN_LINE`  BIGINT(10) DEFAULT NULL  AFTER `JOB_NUMBER` ;
ALTER TABLE `tk_assignment_t`       ADD COLUMN `START_DATE` DATE DEFAULT NULL AFTER `EARN_LINE` ;
ALTER TABLE `tk_assignment_t`       ADD COLUMN `END_DATE` DATE DEFAULT NULL AFTER `START_DATE` ;
ALTER TABLE `tk_assignment_t`       ADD COLUMN `RATE`     DECIMAL(6,2)  DEFAULT 0  AFTER `END_DATE` ;

