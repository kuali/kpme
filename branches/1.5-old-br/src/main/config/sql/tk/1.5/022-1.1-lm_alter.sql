alter table lm_leave_plan_t modify column lm_leave_plan_id varchar(60) NOT NULL;
alter table lm_accrual_category_t modify column lm_accrual_category_id varchar(60) NOT NULL;
alter table lm_accrual_category_rules_t modify column lm_accrual_category_rules_id varchar(60) NOT NULL;
alter table lm_leave_code_t modify column lm_leave_code_id varchar(60) NOT NULL;
alter table lm_sys_schd_timeoff_t modify column lm_sys_schd_timeoff_id varchar(60) NOT NULL;
alter table lm_ledger_t modify column lm_ledger_id varchar(60) NOT NULL;
alter table lm_leave_donation_t modify column lm_leave_donation_id varchar(60) NOT NULL;