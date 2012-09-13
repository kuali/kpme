delete from lm_leave_block_t where lm_leave_block_id >= '1000';
delete from hr_calendar_entries_t where hr_calendar_entry_id = '50001';

insert into lm_leave_block_t (`lm_leave_block_id`, `leave_date`,`description`,  `principal_id` , `earn_code` ,`hr_earn_code_id` ,`lm_sys_schd_timeoff_id` ,`lm_accrual_category_id` ,`leave_amount`, `apply_to_ytd_used`, `document_id`, `accrual_generated`, `block_id`,`ver_nbr`, `obj_id`, `timestamp`,`principal_id_modified`, `tk_assignment_id`, `request_status`, `leave_block_type` ) values('1000', '2012-03-01', 'Test Description', 'admin', 'testLC', '1000','3000', '3000', '8', 'Appy to ytd', '12546', 'A', '0','1','B2991ADA-E866-F28C-7E95-A897AC377D0C', '2010-01-01 08:08:08', 'admin',  '1100', 'P', 'LC');
insert into lm_leave_block_t (`lm_leave_block_id`, `leave_date`,`description`,  `principal_id` , `earn_code` ,`hr_earn_code_id` ,`lm_sys_schd_timeoff_id` ,`lm_accrual_category_id` ,`leave_amount`, `apply_to_ytd_used`, `document_id`, `accrual_generated`, `block_id`,`ver_nbr`, `obj_id`, `timestamp`,`principal_id_modified`, `tk_assignment_id`, `request_status`, `leave_block_type` ) values('1001', '2012-03-02', 'Test Description', 'admin', 'testLC', '1000','3000', '3000', '8', 'Appy to ytd', '12546', 'A', '0','1','B2991ADA-E866-F28C-7E95-A897AC377D0C', '2010-01-01 08:08:08', 'admin',  '1100', 'P', 'LC');

insert into hr_calendar_entries_t (`hr_calendar_entry_id`,`hr_calendar_id`, `calendar_name`, `begin_period_date`, `end_period_date`, `initiate_date`, `initiate_time`, `end_pay_period_date`, `end_pay_period_time`, `employee_approval_date`, `employee_approval_time`,`supervisor_approval_date`,`supervisor_approval_time` ) values ('50001', '2', 'BWS-CAL', '2012-03-01 00:00:00', '2012-03-15 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);