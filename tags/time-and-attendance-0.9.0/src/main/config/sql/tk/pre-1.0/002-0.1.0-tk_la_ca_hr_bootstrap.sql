#
# HR Job
DELETE FROM `hr_job_s`;
INSERT INTO `hr_job_s` (`ID`)	VALUES	('1100');
DELETE FROM `hr_job_t`;
INSERT INTO `hr_job_t` (`HR_JOB_ID`,`PRINCIPAL_ID`,`JOB_NUMBER`,`EFFDT`,`active`,`dept`,`hr_sal_group`,`PAY_GRADE`,`TIMESTAMP`,`OBJ_ID`,`VER_NBR`,`location`,`std_hours`,`fte`,`hr_paytype`) VALUES
	('1012', 'admin', '1', '2010-08-01', 1, 'TEST-DEPT', 'A10', NULL, '2010-08-1 16:00:13', 'A9225D4A-4871-4277-5638-4C7880A57621', '1', NULL, '40.00', NULL, 'BW'),
	('1013', 'admin', '2', '2010-08-01', 1, 'TEST-DEPT', 'A12', NULL, '2010-08-10 16:00:13', 'A9225D4A-4871-4277-5638-4C7880A57621', '1', NULL, '40.00', NULL, 'BW'),
	('1014', 'admin', '2', '2010-08-11', 1, 'TEST-DEPT', 'A12', NULL, '2010-08-11 16:00:14', 'A9225D4A-4871-4277-5638-4C7880A57621', '1', NULL, '40.00', NULL, 'BW'),
	('1015', 'admin', '2', '2010-08-11', 1, 'TEST-DEPT', 'A12', NULL, '2010-08-11 16:00:15', 'A9225D4A-4871-4277-5638-4C7880A57621', '1', NULL, '40.00', NULL, 'BW'),
	('1016', 'admin', '2', '2010-08-12', 1, 'TEST-DEPT', 'A12', NULL, '2010-08-12 16:00:13', 'A9225D4A-4871-4277-5638-4C7880A57621', '1', NULL, '40.00', NULL, 'BW'),
	('1017', 'eric', '1', '2010-08-12', 1, 'TEST-DEPT', '10', NULL, '2010-08-12 16:00:13', 'A9225D4A-4871-4277-5638-4C7880A57621', '1', NULL, '40.00', NULL, 'BW'),
	('1018', 'eric', '1', '2010-08-12', 1, 'TEST-DEPT', '10', NULL, '2010-08-12 16:00:13', 'A9225D4A-4871-4277-5638-4C7880A57621', '1', NULL, '40.00', NULL, 'BW'),
	('1019', 'eric', '2', '2010-08-10', 1, 'TEST-DEPT', '10', NULL, '2010-08-10 16:22:13', 'A9225D4A-4871-4277-5638-4C7880A57621', '1', NULL, '40.00', NULL, 'BW'),
	('1020', 'eric', '2', '2010-08-13', 1, 'TEST-DEPT', '11', NULL, '2010-08-13 16:22:22', 'A9225D4A-4871-4277-5638-4C7880A57621', '1', NULL, '40.00', NULL, 'BW'),
	('1021', 'admin', '3', '2010-08-15', 1, 'NODEP', 'NOP', NULL, '2010-08-12 16:00:13', 'A9225D4A-4871-4277-5638-4C7880A57621', '1', NULL, '40.00', NULL, 'BW'),
	('1022', 'admin', '4', '2010-08-15', 1, 'NODEP', 'A10', NULL, '2010-08-12 16:00:13', 'A9225D4A-4871-4277-5638-4C7880A57621', '1', NULL, '40.00', NULL, 'BW'),
	('1023', 'admin', '5', '2010-08-15', 1, 'LORA-DEPT', 'A10', NULL, '2010-08-12 16:00:13', 'A9225D4A-4871-4277-5638-4C7880A57621', '1', NULL, '40.00', NULL, 'BW'),
	('1024', 'admin', '6', '2010-01-01', 1, 'SHFT-DEPT', 'A10', NULL, '2010-08-12 16:00:13', 'A9225D4A-4871-4277-5638-4C7880A57621', '1', NULL, '40.00', NULL, 'BW');

#
# Departments
DELETE FROM `tk_dept_s`;
INSERT INTO `tk_dept_s` (`ID`) VALUES ('1000');
DELETE FROM `hr_dept_t`;
INSERT INTO `hr_dept_t` (`hr_dept_id`,`dept`,`DESCRIPTION`,`ORG`,`CHART`,`EFFDT`,`TIMESTAMP`,`ACTIVE`) VALUES
    (100 , 'TEST-DEPT'  , 'test department'  , 'TEST' , 'DEPT' , '2010-01-31' , '2010-07-27 10:25:13' , 'Y')  ,
    (101 , 'TEST-DEPT1' , 'test department1' , 'TEST' , 'DEPT' , '2010-01-31' , '2010-07-27 10:25:13' , 'Y')  ,
    (102 , 'TEST-DEPT2' , 'test department2' , 'TEST' , 'DEPT' , '2010-01-31' , '2010-07-27 10:25:13' , 'Y')  ,
    (103 , 'TEST-DEPT3' , 'test department3' , 'TEST' , 'DEPT' , '2010-01-31' , '2010-07-27 10:25:13' , 'Y')  ,
    (104 , 'TEST-DEPT4' , 'test department4' , 'TEST' , 'DEPT' , '2010-01-31' , '2010-07-27 10:25:13' , 'Y')  ,
    (105 , 'TEST-DEPT5' , 'test department5' , 'TEST' , 'DEPT' , '2010-01-31' , '2010-07-27 10:25:13' , 'Y')  ,
    (106 , 'TEST-DEPT6' , 'test department6' , 'TEST' , 'DEPT' , '2010-01-31' , '2010-07-27 10:25:13' , 'Y')  ,
    (107 , 'NODEP'      , 'test department7' , 'NODEP', 'DEPT' , '2010-01-31' , '2010-07-27 10:25:13' , 'Y'),
    (108 , 'TEST-DEPT7' , 'test department7' , 'TEST' , 'DEPT' , '2010-01-31' , '2010-07-27 10:25:13' , 'Y'),
    (109 , 'LORA-DEPT'  , 'lora\'s department' , 'LORA' , 'DEPT' , '2010-01-31' , '2010-07-27 10:25:13' , 'Y'),
    (110 , 'SHFT-DEPT'  , 'shift department' , 'SHFT' , 'DEPT' , '2010-01-01' , '2010-07-27 10:25:13' , 'Y');

#
# Work Areas
DELETE FROM `tk_work_area_s`;
INSERT INTO `tk_work_area_s` (`ID`) VALUES ('1000');
DELETE FROM `tk_work_area_t`;
INSERT INTO `tk_work_area_t` (`TK_WORK_AREA_ID`, `WORK_AREA`, `EFFDT`,`ACTIVE`,`DESCR`,`DEPT`,`DEFAULT_OVERTIME_PREFERENCE`,`ADMIN_DESCR`,`USER_PRINCIPAL_ID`,`TIMESTAMP`,`OBJ_ID`,`VER_NBR`) VALUES
    (100,'1234', '2010-01-05', 'Y', 'work area description', 'TEST-DEPT', 'OT1', 'work area admin description', 'admin', '2010-07-27 10:25:13', '7EE387AB-26B0-B6A6-9C4C-5B5F687F0E97', '20'),
    (101, '2345', '2010-01-05', 'Y', 'work area description2', 'TEST-DEPT2', 'OT1', 'work area admin description2', 'admin', '2010-07-27 10:25:13', '7EE387AB-26B0-B6A6-9C4C-5B5F687F0E97', '20'),
    (102, '3456', '2010-01-05', 'Y', 'work area description3', 'TEST-DEPT3', 'OT1', 'work area admin description2', 'admin', '2010-07-27 10:25:13', '7EE387AB-26B0-B6A6-9C4C-5B5F687F0E97', '20'),
    (103, '4567', '2010-01-05', 'Y', 'work area description4', 'TEST-DEPT4', 'OT1', 'work area admin description2', 'admin', '2010-07-27 10:25:13', '7EE387AB-26B0-B6A6-9C4C-5B5F687F0E97', '20'),
    (104, '1000', '2010-01-05', 'Y', 'lora\'s work area', 'LORA-DEPT', 'OT1', 'work area admin description2', 'admin', '2010-07-27 10:25:13', '7EE387AB-26B0-B6A6-9C4C-5B5F687F0E97', '20'),
    (105, '1100', '2010-01-01', 'Y', 'shift-workarea', 'SHFT-DEPT', 'OT1', 'work area admin description2', 'admin', '2010-07-27 10:25:13', '7EE387AB-26B0-B6A6-9C4C-5B5F687F0E97', '20');

#
# Task
DELETE FROM `tk_task_s`;
INSERT INTO `tk_task_s` (`ID`) VALUES ('1000');
DELETE FROM `tk_task_t`;
INSERT INTO `tk_task_t`(`tk_task_id`,`task`,`work_area`,`tk_work_area_id`,`descr`,`admin_descr`,`obj_id`, `ver_nbr`,`USER_PRINCIPAL_ID`)  VALUES
    (100, 1, '1234', 100,'description 1', 'admin description 1', '8421CD29-E1F4-4B9A-AE33-F3F4752505CE', '1', 'admin'),
    (101, 2, '1234', 100,'description 2', 'admin description 2', '8421CD29-E1F4-4B9A-AE33-F3F4752505CE', '1', 'admin'),
    (102, 3, '1234', 100,'description 3', 'admin description 3', '8421CD29-E1F4-4B9A-AE33-F3F4752505CE', '1', 'admin'),
    (103, 4, '1000', 104,'task 4', 'admin description 4', '8421CD29-E1F4-4B9A-AE33-F3F4752505CE', '1', 'admin'),
    (104, 5, '1100', 105,'task 5', 'admin description 4', '8421CD29-E1F4-4B9A-AE33-F3F4752505CE', '1', 'admin');

#
# Assignments
DELETE FROM `tk_assignment_s`;
INSERT INTO `tk_assignment_s` (`ID`) VALUES ('1000');
DELETE FROM `tk_assignment_t`;
INSERT INTO `tk_assignment_t` (`TK_ASSIGNMENT_ID`,`PRINCIPAL_ID`,`JOB_NUMBER`,`EFFDT`,`WORK_AREA`,`TASK`,`OBJ_ID`,`TIMESTAMP`,`VER_NBR`,`active`) VALUES
    (10 , 'admin' , 1 , '2010-08-01' , 1234 , 1 , '8421CD29-E1F4-4B9A-AE33-F3F4752505CE' , '2010-07-27 10:25:13' , '1' , 1)  ,
    (11 , 'admin' , 2 , '2010-08-01' , 1234 , 2 , '8421CD29-E1F4-4B9A-AE33-F3F4752505CE' , '2010-07-27 10:25:13' , '1' , 1)  ,
    (12 , 'eric'  , 1 , '2010-08-01' , 2345 , 1 , '8421CD29-E1F4-4B9A-AE33-F3F4752505CE' , '2010-07-27 10:25:13' , '1' , 1)  ,
    (13 , 'eric'  , 2 , '2010-08-01' , 3456 , 1 , '8421CD29-E1F4-4B9A-AE33-F3F4752505CE' , '2010-07-27 10:25:13' , '1' , 1)  ,
    (14 , 'admin' , 3 , '2010-08-15' , 1234 , 1 , '8421CD29-E1F4-4B9A-AE33-F3F4752505CE' , '2010-08-27 10:25:13' , '1' , 1)  ,
    (15 , 'admin' , 1 , '2010-08-04' , 1234 , 1 , '8421CD29-E1F4-4B9A-AE33-F3F4752505CE' , '2010-08-27 10:25:13' , '1' , 1)  ,
    (16 , 'admin' , 4 , '2010-08-15' , 1234 , 1 , '8421CD29-E1F4-4B9A-AE33-F3F4752505CE' , '2010-08-27 10:25:13' , '1' , 1)  ,
    (17 , 'admin' , 5 , '2010-08-15' , 1000 , 4 , '8421CD29-E1F4-4B9A-AE33-F3F4752505CE' , '2010-08-27 10:25:13' , '1' , 1)  ,
    (18 , 'admin' , 6 , '2010-01-01' , 1100 , 5 , '8421CD29-E1F4-4B9A-AE33-F3F4752505CE' , '2010-08-27 10:25:13' , '1' , 1)  ;

#
# dept earn code
DELETE FROM `tk_dept_earn_code_s`;
INSERT INTO `tk_dept_earn_code_s` VALUES('1000');
DELETE FROM `hr_dept_earn_code_t`;
INSERT INTO `hr_dept_earn_code_t`
    (`hr_dept_earn_code_id` , `DEPT`       , `hr_sal_group` , `EARN_CODE` , `EMPLOYEE` , `APPROVER` , `ORG_ADMIN` , `EFFDT`      , `TIMESTAMP`           , `ACTIVE`) VALUES
    ('10'                   , 'TEST-DEPT'  , 'A10'          , 'RGH'       , 1          , 1          , 1           , '2010-08-01' , '2010-01-01 08:08:08' , 1)               ,
    ('11'                   , 'TEST-DEPT'  , 'A10'          , 'SCK'       , 1          , 1          , 1           , '2010-08-01' , '2010-01-01 08:08:08' , 1)               ,
    ('12'                   , 'TEST-DEPT'  , 'A10'          , 'VAC'       , 1          , 1          , 1           , '2010-08-01' , '2010-01-01 08:08:08' , 1)               ,
    ('13'                   , 'TEST-DEPT'  , 'A10'          , 'WEP'       , 1          , 1          , 1           , '2010-08-01' , '2010-01-01 08:08:08' , 1)               ,
    ('14'                   , 'TEST-DEPT2' , 'A10'          , 'HAZ'       , 1          , 1          , 1           , '2010-08-01' , '2010-01-01 08:08:08' , 1)               ,
    ('15'                   , 'TEST-DEPT2' , 'A10'          , 'HIP'       , 1          , 1          , 1           , '2010-08-01' , '2010-01-01 08:08:08' , 1)               ,
    ('16'                   , 'TEST-DEPT2' , 'A10'          , 'OC1'       , 1          , 1          , 1           , '2010-08-01' , '2010-01-01 08:08:08' , 1)               ,
    ('17'                   , 'TEST-DEPT2' , 'A10'          , 'OC2'       , 1          , 1          , 1           , '2010-08-01' , '2010-01-01 08:08:08' , 1)               ,
    ('18'                   , '*'          , 'A10'          , 'XYZ'       , 1          , 1          , 1           , '2010-08-01' , '2010-01-01 08:08:08' , 1)               ,
    ('19'                   , 'TEST-DEPT'  , '*'            , 'XYY'       , 1          , 1          , 1           , '2010-08-01' , '2010-01-01 08:08:08' , 1)               ,
    ('20'                   , '*'          , '*'            , 'XZZ'       , 1          , 1          , 1           , '2010-08-01' , '2010-01-01 08:08:08' , 1)               ,
    ('21'                   , 'LORA-DEPT'  , 'A10'          , 'RGH'       , 1          , 1          , 1           , '2010-08-01' , '2010-01-01 08:08:08' , 1)               ,
    ('22'                   , 'LORA-DEPT'  , 'A10'          , 'SCK'       , 1          , 1          , 1           , '2010-08-01' , '2010-01-01 08:08:08' , 1)               ,
    ('23'                   , 'LORA-DEPT'  , 'A10'          , 'VAC'       , 1          , 1          , 1           , '2010-08-01' , '2010-01-01 08:08:08' , 1)  ;

#
# earn code
DELETE FROM `tk_earn_code_s`;
INSERT INTO `tk_earn_code_s` VALUES('1000');
DELETE FROM `hr_earn_code_t`;
INSERT INTO `hr_earn_code_t` (`hr_earn_code_id`, `EARN_CODE`, `DESCR`, `RECORD_TIME`,`RECORD_HOURS`,`RECORD_AMOUNT`,`EFFDT`, `TIMESTAMP`, `ACTIVE`) VALUES
	('9'  , 'RGN' , 'REGULAR'           , 'y','n','n', '2010-01-01' , '2010-01-01 08:08:08' , 'Y') ,
	('10' , 'RGH' , 'REGULAR HOURLY'    , 'y','n','n', '2010-01-01' , '2010-01-01 08:08:08' , 'Y') ,
	('11' , 'SCK' , 'SICK'              , 'y','n','n', '2010-01-01' , '2010-01-01 08:08:08' , 'Y') ,
	('12' , 'VAC' , 'VACATION'          , 'y','n','n', '2010-01-01' , '2010-01-01 08:08:08' , 'Y') ,
	('13' , 'WEP' , 'EMERGENCY'         , 'y','n','n', '2010-01-01' , '2010-01-01 08:08:08' , 'Y') ,
	('14' , 'HAZ' , 'HAZARD DAY'        , 'y','n','n', '2010-01-01' , '2010-01-01 08:08:08' , 'Y') ,
	('15' , 'HIP' , 'HOLIDAY INCENTIVE' , 'y','n','n', '2010-01-01' , '2010-01-01 08:08:08' , 'Y') ,
	('16' , 'OC1' , 'ON CALL - 1.50'    , 'y','n','n', '2010-01-01' , '2010-01-01 08:08:08' , 'Y') ,
	('17' , 'OC2' , 'ON CALL - 2.00'    , 'y','n','n', '2010-01-01' , '2010-01-01 08:08:08' , 'Y') ,
	('18' , 'PRM' , 'PREMIUM'           , 'y','n','n', '2010-01-01' , '2010-01-01 08:08:08' , 'Y') ,
	('19' , 'XYZ' , 'XYZ'               , 'y','n','n', '2010-01-01' , '2010-01-01 08:08:08' , 'Y') ,
	('20' , 'XYY' , 'XYY'               , 'y','n','n', '2010-01-01' , '2010-01-01 08:08:08' , 'Y') ,
	('21' , 'XZZ' , 'XZZ'               , 'y','n','n', '2010-01-01' , '2010-01-01 08:08:08' , 'Y') ;

# Sal Group
DELETE FROM `hr_sal_group_s`;
INSERT INTO `hr_sal_group_S` (`ID`) VALUES ('1000');
DELETE FROM `hr_sal_group_t`;
INSERT INTO `hr_sal_group_t` (`hr_sal_group_ID`, `hr_sal_group`, `EFFDT`, `TIMESTAMP`, `ACTIVE`) VALUES
    ('10', 'A10', '2010-01-01', '2010-01-01 08:08:08' , 'Y'),
    ('11', 'S10', '2010-01-01', '2010-01-01 08:08:08' , 'Y'),
    ('12', 'A12', '2010-01-01', '2010-01-01 08:08:08' , 'Y'),
    ('13', 'S12', '2010-01-01', '2010-01-01 08:08:08' , 'Y'),
    ('14', 'NOP', '2010-01-01', '2010-01-01 08:08:08' , 'Y');

# HR Pay Types
DELETE FROM `hr_paytype_s`;
INSERT INTO `hr_paytype_s` (`ID`)	VALUES	('1000');
DELETE FROM `hr_paytype_t`;
INSERT INTO `hr_paytype_t` (`HR_PAYTYPE_ID`,`PAYTYPE`,`DESCR`,`CALENDAR_GROUP`,`REG_ERN_CODE`,`EFFDT`,`TIMESTAMP`,`HOLIDAY_CALENDAR_GROUP`,`OBJ_ID`,`VER_NBR`,`ACTIVE`) VALUES
	(1, 'BW', 'description', 'BW-CAL1', 'RGN', '2010-08-01', '2010-08-01 16:01:07', 'HOL', '47326FEA-46E7-7D89-0B13-85DFA45EA8C1', '1',1),
	(2, 'BW', 'description', 'TST-CAL', 'RGN', '2010-01-01', '2010-08-01 16:01:07', 'HOL', '47326FEA-46E7-7D89-0B13-85DFA45EA8C1', '1',1);

# time collection rule
DELETE FROM `tk_time_collection_rl_s`;
INSERT INTO `tk_time_collection_rl_s` VALUES('1000');
DELETE FROM `tk_time_collection_rl_t`;
INSERT INTO `tk_time_collection_rl_t` (`TK_TIME_COLL_RULE_ID`,`DEPT`,`WORK_AREA`,`EFFDT`,`CLOCK_USERS_FL`,`HRS_DISTRIBUTION_FL`,`USER_PRINCIPAL_ID`,
`TIMESTAMP`,`ACTIVE`) VALUES
	('1' , 'TEST-DEPT' , 1234 , '2010-01-01' , 1 , 1 , 'admin' , '2010-01-01 08:08:08' , 1)  ,
	('2' , '*'         , 1234 , '2010-01-01' , 1 , 1 , 'admin' , '2010-01-01 08:08:08' , 1)  ,
	('3' , 'TEST-DEPT' , -1   , '2010-01-01' , 1 , 1 , 'admin' , '2010-01-01 08:08:08' , 1)  ,
	('4' , '*'         , -1   , '2010-01-01' , 1 , 1 , 'admin' , '2010-01-01 08:08:08' , 1);


DELETE FROM `tk_time_block_s`;
INSERT INTO `tk_time_block_s` VALUES ('5000');
DELETE FROM `tk_time_block_t`;
INSERT INTO `tk_time_block_t` (`TK_TIME_BLOCK_ID`,`DOCUMENT_ID`,`JOB_NUMBER`,`WORK_AREA`,`TASK`,`TK_WORK_AREA_ID`,
	`HR_JOB_ID`,`TK_TASK_ID`,`EARN_CODE`,`BEGIN_TS`,`BEGIN_TS_TZ`,`END_TS`,`END_TS_TZ`,
	`CLOCK_LOG_CREATED`,`HOURS`,`amount`,`USER_PRINCIPAL_ID`,`TIMESTAMP`,`OBJ_ID`,`VER_NBR`) VALUES
		(2000,'4145',1,1234,0,100,1012,100,'RGN','2010-09-19 08:08:08', 'EST', '2010-09-19 10:08:08', 'EST',0,2,0,'admin','2010-09-19 08:08:08','38382',1),
		(2001,'4145',1,1234,0,100,1012,100,'RGN','2010-09-20 08:08:08', 'EST', '2010-09-20 10:08:08', 'EST',0,2,0,'admin','2010-09-20 08:08:08','38382',1);

DELETE FROM `tk_hour_detail_s`;
INSERT INTO `tk_hour_detail_s` VALUES ('1000');
DELETE FROM `tk_hour_detail_t`;
INSERT INTO `tk_hour_detail_t` (`TK_HOUR_DETAIL_ID`,`TK_TIME_BLOCK_ID`,`EARN_CODE`,`HOURS`, `amount`,`OBJ_ID`,`VER_NBR`) VALUES
	(100,2000,'RGN',2,0,'888223f',1),
	(101,2001,'RGN',2,0,'23432ff',1);

DELETE FROM `tk_earn_group_s`;
INSERT INTO `tk_earn_group_s` VALUES ('1000');
DELETE FROM `hr_earn_group_t`;
INSERT INTO `hr_earn_group_t` (`hr_earn_group_id`,`earn_group`,`descr`,`effdt`,`active`,`OBJ_ID`,`VER_NBR`,`timestamp`) VALUES
	(100,'REG','Regular', '2010-01-01','Y','7EE387AB-26B0-B6A6-9C4C-5B5F687F0E97','20','2010-07-27 10:25:13' ),
	(101,'OVT','Overtime', '2010-01-01','Y','7EE387AB-26B0-B6A6-9C4C-5B5F687F0E97','20','2010-07-27 10:25:13' ),
	(102,'TC1','Regular', '2010-01-01','Y','7EE387AB-26B0-B6A6-9C4C-5B5F687F0E97','20','2010-07-27 10:25:13' ),
	(103,'TC2','Regular', '2010-01-01','Y','7EE387AB-26B0-B6A6-9C4C-5B5F687F0E97','20','2010-07-27 10:25:13' ),
	(104,'TC3','Regular', '2010-01-01','Y','7EE387AB-26B0-B6A6-9C4C-5B5F687F0E97','20','2010-07-27 10:25:13' );

DELETE FROM `tk_earn_group_def_s`;
INSERT INTO `tk_earn_group_def_s` VALUES ('1000');
DELETE FROM `hr_earn_group_def_t`;
INSERT INTO `hr_earn_group_def_t` (`hr_earn_group_def_id`, `hr_earn_group_id`,`earn_code`,`OBJ_ID`,`VER_NBR`) VALUES
	(100,100,'REG','7EE387AB-26B0-B6A6-9C4C-5B5F687F0E97',1),
	(101,100,'RGN','7EE387AB-26B0-B6A6-9C4C-5B5F687F0E97',1),
	(102,100,'RGH','7EE387AB-26B0-B6A6-9C4C-5B5F687F0E97',1),
	(109,101,'OVT','7EE387AB-26B0-B6A6-9C4C-5B5F687F0E97',1);

DELETE FROM `tk_roles_s`;
INSERT INTO `tk_roles_s` VALUES ('1000');
DELETE FROM `hr_roles_t`;
INSERT INTO `hr_roles_t` (`TK_ROLES_ID`, `PRINCIPAL_ID`, `ROLE_NAME`, `USER_PRINCIPAL_ID`, `WORK_AREA`, `DEPT`, `EFFDT`, `TIMESTAMP`, `ACTIVE`) VALUES
    ('1', 'admin', 'TK_APPROVER', 'admin', '999', NULL, '2010-08-01', '2010-08-01 15:10:57', 1),
    ('2', 'admin', 'TK_APPROVER', 'admin', '999', NULL, '2010-08-10', '2010-08-10 15:10:57', 1),
    ('3', 'admin', 'TK_APPROVER', 'admin', '999', NULL, '2010-08-20', '2010-08-20 15:10:57', 1),
    ('4', 'admin', 'TK_APPROVER', 'admin', '999', NULL, '2010-08-20', '2010-08-20 15:11:57', 1),
    ('5', 'admin', 'TK_APPROVER', 'admin', '999', NULL, '2010-08-20', '2010-08-20 15:12:57', 1),
    ('6', 'eric',  'TK_APPROVER', 'admin', '999', NULL, '2010-08-01', '2010-08-01 16:10:57', 1),
    ('7', 'eric',  'TK_APPROVER', 'admin', '999', NULL, '2010-08-10', '2010-08-10 16:10:57', 1),
    ('8', 'eric',  'TK_APPROVER', 'admin', '999', NULL, '2010-08-20', '2010-08-20 16:10:57', 1),
    ('9', 'eric',  'TK_APPROVER', 'admin', '999', NULL, '2010-08-20', '2010-08-20 16:11:57', 1),
    ('10','eric',  'TK_APPROVER', 'admin', '999', NULL, '2010-08-20', '2010-08-20 16:12:57', 1),
    ('11','admin', 'TK_ORG_ADMIN', 'admin', '999', NULL, '2010-08-01', '2010-08-01 15:10:57', 1),
    ('12','admin', 'TK_ORG_ADMIN', 'admin', '999', NULL, '2010-08-10', '2010-08-10 15:10:57', 1),
    ('13','admin', 'TK_ORG_ADMIN', 'admin', '999', NULL, '2010-08-20', '2010-08-20 15:10:57', 1),
    ('14','admin', 'TK_ORG_ADMIN', 'admin', '999', NULL, '2010-08-20', '2010-08-20 15:11:57', 1),
    ('15','admin', 'TK_ORG_ADMIN', 'admin', '999', NULL, '2010-08-20', '2010-08-20 15:12:57', 1),
    ('16','eric',  'TK_ORG_ADMIN', 'admin', '999', NULL, '2010-08-01', '2010-08-01 16:10:57', 1),
    ('17','eric',  'TK_ORG_ADMIN', 'admin', '999', NULL, '2010-08-10', '2010-08-10 16:10:57', 1),
    ('18','eric',  'TK_ORG_ADMIN', 'admin', '999', NULL, '2010-08-20', '2010-08-20 16:10:57', 1),
    ('19','eric',  'TK_ORG_ADMIN', 'admin', '999', NULL, '2010-08-20', '2010-08-20 16:11:57', 1),
    ('20','eric',  'TK_ORG_ADMIN', 'admin', '999', NULL, '2010-08-20', '2010-08-20 16:12:57', 1),
    ('21','eric',  'TK_APPROVER', 'admin', '999', NULL, '2010-08-20', '2010-08-20 16:13:57', 0),
    ('22','admin', 'TK_APPROVER', 'admin', '1234', NULL, '2010-01-05', '2010-01-05 15:12:57', 1);

#
# Pay Calendar
DELETE FROM `tk_py_calendar_s`;
INSERT INTO `tk_py_calendar_s`	(`ID`)	VALUES	('30');
DELETE FROM `hr_py_calendar_t`;
INSERT INTO `hr_py_calendar_t`	(`hr_py_calendar_id`,	`py_calendar_group`,	`chart`,	`begin_date`,	`begin_time`,	`end_date`,	`end_time`)	VALUES
	('1',  'TST-CAL', 'CHART1', '2010-01-01', '12:00:00', '2011-01-01', '12:00:00'),
	('20', 'BW-CAL1', 'CHART1', '2010-01-01', '00:00:00', '2010-12-31', '23:59:59'),
	('21', 'BW-CAL2', 'CHART2', '2010-01-01', '00:00:00', '2010-12-31', '23:59:59'),
	('22', 'BW-CAL3', 'CHART3', '2010-01-01', '00:00:00', '2010-12-31', '23:59:59');

#
# Pay Calendar Dates
DELETE FROM `tk_py_calendar_dates_s`;
INSERT INTO `tk_py_calendar_dates_s`	(`ID`)	VALUES	('30');
DELETE FROM `tk_py_calendar_dates_t`;
INSERT INTO `tk_py_calendar_dates_t` (`tk_py_calendar_dates_id`,`hr_py_calendar_id`,`begin_period_date`,`end_period_date`,`initiate_date`,`initiate_time`,`end_pay_period_date`,`end_pay_period_time`,`employee_approval_date`,`employee_approval_time`,`supervisor_approval_date`,`supervisor_approval_time`) VALUES
	('1', '20', '2010-08-01 00:00:00', '2010-08-14 23:59:59', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
	('2', '20', '2010-08-15 00:00:00', '2010-08-31 23:59:59', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
	('3', '20', '2010-09-01 00:00:00', '2010-09-14 23:59:59', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
	('4', '20', '2010-09-15 00:00:00', '2010-09-30 23:59:59', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
	('5', '20', '2010-10-01 00:00:00', '2010-10-15 23:59:59', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
	('6', '1',  '2010-01-01 12:00:00', '2010-01-16 12:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
	('7', '1',  '2010-01-16 12:00:00', '2010-01-31 12:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
	('8', '20', '2010-10-16 00:00:00', '2010-10-31 23:59:59', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

#
# Daily Overtime Rule
#
# Note - Do not change IDs, Dept, WorkArea or Task.  These values are used in
# the test cases.  Other values may be modified for now, until the test cases
# start looking at them as well
DELETE FROM `tk_daily_overtime_rl_s`;
INSERT INTO `tk_daily_overtime_rl_s` (`ID`) VALUES (1000);
DELETE FROM `tk_daily_overtime_rl_t`;
INSERT INTO `tk_daily_overtime_rl_t` (`tk_daily_overtime_rl_id`,`dept`,`work_area`,`task`,`location`,`paytype`,`max_gap`,`shift_hours`,`overtime_preference`,`user_principal_id`,`effdt`,`active`,`timestamp`) VALUES
    (1,  'TEST-DEPT', 1234, 1, 'IN', 'BW', -1, -1, 'OVT', 'admin', '2010-01-01', 1, '2010-08-20 16:13:57'),
    (2,  'TEST-DEPT', 1234,-1, 'IN', 'BW', -1, -1, 'OVT', 'admin', '2010-01-01', 1, '2010-08-20 16:13:57'),
    (3,  'TEST-DEPT',   -1, 1, 'IN', 'BW', -1, -1, 'OVT', 'admin', '2010-01-01', 1, '2010-08-20 16:13:57'),
    (4,  'TEST-DEPT',   -1,-1, 'IN', 'BW', -1, -1, 'OVT', 'admin', '2010-01-01', 1, '2010-08-20 16:13:57'),
    (5,  '*',         1234, 1, 'IN', 'BW', -1, -1, 'OVT', 'admin', '2010-01-01', 1, '2010-08-20 16:13:57'),
    (6,  '*',         1234,-1, 'IN', 'BW', -1, -1, 'OVT', 'admin', '2010-01-01', 1, '2010-08-20 16:13:57'),
    (7,  '*',           -1, 1, 'IN', 'BW', -1, -1, 'OVT', 'admin', '2010-01-01', 1, '2010-08-20 16:13:57'),
    (8,  '*',           -1,-1, 'IN', 'BW', -1, -1, 'OVT', 'admin', '2010-01-01', 1, '2010-08-20 16:13:57'),
    (9,  'TEST-DEPT2',  -1,-1, 'IN', 'BW', -1, -1, 'OVT', 'admin', '2010-01-01', 1, '2010-08-20 16:13:57');
# Task ID 7 above doesn't really make sense, the lookup form is not supported.

#
# Hr Work Schedule
#
# 1-8: Basic wildcard test data
DELETE FROM `HR_WORK_SCHEDULE_S`;
INSERT INTO `HR_WORK_SCHEDULE_S` (`ID`) VALUES (1000);
DELETE FROM `HR_WORK_SCHEDULE_T`;
INSERT INTO `HR_WORK_SCHEDULE_T` (`HR_WORK_SCHEDULE_ID`,`WORK_SCHEDULE_DESC`,`PRINCIPAL_ID`,`DEPT`,`WORK_AREA`,`ACTIVE`,`EFFDT`,`TIMESTAMP`,`OBJ_ID`,`VER_NBR`) VALUES
    (1 , 'used for testing' , 'admin' , 'TEST-DEPT' , 1234 , 1 , '2010-01-01' , '2010-01-01 12:00:00' , 'uuid' , 1)  ,
    (2 , 'used for testing' , 'admin' , 'TEST-DEPT' , -1   , 1 , '2010-01-01' , '2010-01-01 12:00:00' , 'uuid' , 1)  ,
    (3 , 'used for testing' , 'admin' , '*'         , 1234 , 1 , '2010-01-01' , '2010-01-01 12:00:00' , 'uuid' , 1)  ,
    (4 , 'used for testing' , 'admin' , '*'         , -1   , 1 , '2010-01-01' , '2010-01-01 12:00:00' , 'uuid' , 1)  ,
    (5 , 'used for testing' , '*'     , 'TEST-DEPT' , 1234 , 1 , '2010-01-01' , '2010-01-01 12:00:00' , 'uuid' , 1)  ,
    (6 , 'used for testing' , '*'     , 'TEST-DEPT' , -1   , 1 , '2010-01-01' , '2010-01-01 12:00:00' , 'uuid' , 1)  ,
    (7 , 'used for testing' , '*'     , '*'         , 1234 , 1 , '2010-01-01' , '2010-01-01 12:00:00' , 'uuid' , 1)  ,
    (8 , 'used for testing' , '*'     , '*'         , -1   , 1 , '2010-01-01' , '2010-01-01 12:00:00' , 'uuid' , 1);

#
# 1-16: Basic entries, 2 per Work schedule for wildcard testing
DELETE FROM `HR_WORK_SCHEDULE_ENTRY_S`;
INSERT INTO `HR_WORK_SCHEDULE_ENTRY_S` (`ID`) VALUES (1000);
DELETE FROM `HR_WORK_SCHEDULE_ENTRY_T`;
INSERT INTO `HR_WORK_SCHEDULE_ENTRY_T` (`HR_WORK_SCHEDULE_ENTRY_ID`, `HR_WORK_SCHEDULE_ID`, `CALENDAR_GROUP`, `DAY_OF_PERIOD`, `REG_HOURS`, `OBJ_ID`, `VER_NBR`) VALUES
	(1  , 1 , 'BW-CAL1' , 1 , 40 , 'uuid' , 1)  ,
	(2  , 1 , 'BW-CAL1' , 1 , 40 , 'uuid' , 1)  ,
	(3  , 2 , 'BW-CAL1' , 1 , 40 , 'uuid' , 1)  ,
	(4  , 2 , 'BW-CAL1' , 1 , 40 , 'uuid' , 1)  ,
	(5  , 3 , 'BW-CAL1' , 1 , 40 , 'uuid' , 1)  ,
	(6  , 3 , 'BW-CAL1' , 1 , 40 , 'uuid' , 1)  ,
	(7  , 4 , 'BW-CAL1' , 1 , 40 , 'uuid' , 1)  ,
	(8  , 4 , 'BW-CAL1' , 1 , 40 , 'uuid' , 1)  ,
	(9  , 5 , 'BW-CAL1' , 1 , 40 , 'uuid' , 1)  ,
	(10 , 5 , 'BW-CAL1' , 1 , 40 , 'uuid' , 1)  ,
	(11 , 6 , 'BW-CAL1' , 1 , 40 , 'uuid' , 1)  ,
	(12 , 6 , 'BW-CAL1' , 1 , 40 , 'uuid' , 1)  ,
	(13 , 7 , 'BW-CAL1' , 1 , 40 , 'uuid' , 1)  ,
	(14 , 7 , 'BW-CAL1' , 1 , 40 , 'uuid' , 1)  ,
	(15 , 8 , 'BW-CAL1' , 1 , 40 , 'uuid' , 1)  ,
	(16 , 8 , 'BW-CAL1' , 1 , 40 , 'uuid' , 1);

DELETE FROM `TK_WEEKLY_OVERTIME_RL_S`;
INSERT INTO `TK_WEEKLY_OVERTIME_RL_S` (`ID`) VALUES (1000);
DELETE FROM `TK_WEEKLY_OVERTIME_RL_T`;
#INSERT INTO `TK_WEEKLY_OVERTIME_RL_T` (`tk_weekly_overtime_rl_id`,`max_hrs_ern_group`,`convert_from_ern_group`,`convert_to_erncd`,`step`,`max_hrs`,`user_principal_id`,`effdt`,`active`,`timestamp`,`obj_id`,`ver_nbr`) VALUES
#    ();