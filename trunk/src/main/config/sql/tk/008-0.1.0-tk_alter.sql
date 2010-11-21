ALTER TABLE tk_sal_group_t CHANGE COLUMN `ACTIVE` `ACTIVE` VARCHAR(1) NULL DEFAULT NULL  ;
ALTER TABLE tk_grace_period_rl_t CHANGE COLUMN `ACTIVE` `ACTIVE` VARCHAR(1) NULL DEFAULT NULL  ;
ALTER TABLE tk_earn_code_t CHANGE COLUMN `ACTIVE` `ACTIVE` VARCHAR(1) NULL DEFAULT NULL  ;
ALTER TABLE tk_earn_code_t ADD COLUMN `ACCRUAL_CATEGORY` VARCHAR(3) NULL DEFAULT NULL  ;
ALTER TABLE tk_earn_code_t CHANGE COLUMN `record_time` `record_time` VARCHAR(1) NULL DEFAULT 'N'  , CHANGE COLUMN `record_amount` `record_amount` VARCHAR(1) NULL DEFAULT 'N'  , CHANGE COLUMN `record_hours` `record_hours` VARCHAR(1) NULL DEFAULT 'N'  ;
ALTER TABLE la_accrual_categories_t CHANGE COLUMN `ACTIVE` `ACTIVE` VARCHAR(1) NULL DEFAULT 'N'  ;
ALTER TABLE la_accruals_t CHANGE COLUMN `HOURS_ACCRUED` `HOURS_ACCRUED` DECIMAL(6,2) NOT NULL  , CHANGE COLUMN `HOURS_TAKEN` `HOURS_TAKEN` DECIMAL(6,2) NOT NULL  , CHANGE COLUMN `HOURS_ADJUST` `HOURS_ADJUST` DECIMAL(6,2) NOT NULL  ;

ALTER TABLE tk_dept_earn_code_t CHANGE COLUMN `employee` `employee` VARCHAR(1) NULL DEFAULT 'N'  , CHANGE COLUMN `approver` `approver` VARCHAR(1) NULL DEFAULT 'N'  , CHANGE COLUMN `org_admin` `org_admin` VARCHAR(1) NULL DEFAULT 'N'  , CHANGE COLUMN `active` `active` VARCHAR(1) NULL DEFAULT 'N'  ;
ALTER TABLE tk_earn_group_t CHANGE COLUMN `active` `active` VARCHAR(1) NULL DEFAULT NULL  ;
ALTER TABLE hr_job_t CHANGE COLUMN `active` `active` VARCHAR(1) NULL DEFAULT 'N'  ;

ALTER TABLE tk_earn_code_t ADD COLUMN `inflate_factor` DECIMAL(3,2) NOT NULL DEFAULT 1  AFTER `accrual_category` , ADD COLUMN `inflate_min_hours` DECIMAL(3,2) NOT NULL DEFAULT 0  AFTER `accrual_category` ;










