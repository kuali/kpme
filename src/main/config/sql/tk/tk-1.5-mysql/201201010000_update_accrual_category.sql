alter table lm_accrual_category_t modify column ACCRUAL_INTERVAL_EARN varchar(1) NOT NULL DEFAULT "M";
alter table lm_accrual_category_t drop column PLANNING_MONTHS;