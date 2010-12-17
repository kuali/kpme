#
# HR Job
DELETE FROM `hr_job_s`;
INSERT INTO `hr_job_s` (`ID`)	VALUES	(1000);
DELETE FROM `hr_job_t`;
INSERT INTO `hr_job_t` (`HR_JOB_ID`,`PRINCIPAL_ID`,`JOB_NUMBER`,`EFFDT`,`active`,`dept`,`TK_SAL_GROUP`,`PAY_GRADE`,`TIMESTAMP`,`OBJ_ID`,`VER_NBR`,`location`,`std_hours`,`fte`,`hr_paytype`) VALUES
  (1,  'admin', 30, '2010-01-01', 'Y', 'TEST-DEPT', 'SD1', 'SD1', '2010-08-1 16:00:13', 'A9225D4A-4871-4277-5638-4C7880A57621', '1', 'SD1', '40.00', NULL, 'BW'),
	(12, 'admin', 1, '2010-08-01', 'Y', 'TEST-DEPT', 'A10', NULL, '2010-08-1 16:00:13', 'A9225D4A-4871-4277-5638-4C7880A57621', '1', '', '40.00', NULL, 'BW'),
	(13, 'admin', 2, '2010-08-01', 'Y', 'TEST-DEPT', 'A12', NULL, '2010-08-10 16:00:13', 'A9225D4A-4871-4277-5638-4C7880A57621', '1', '', '40.00', NULL, 'BW'),
	(14, 'admin', 2, '2010-08-11', 'Y', 'TEST-DEPT', 'A12', NULL, '2010-08-11 16:00:14', 'A9225D4A-4871-4277-5638-4C7880A57621', '1', '', '40.00', NULL, 'BW'),
 (16, 'admin', 2, '2010-08-12', 'Y', 'TEST-DEPT', 'A12', NULL, '2010-08-12 16:00:13', 'A9225D4A-4871-4277-5638-4C7880A57621', '1', '', '40.00', NULL, 'BW'),
	(17, 'eric', 1, '2010-08-12', 'Y', 'TEST-DEPT', '10', NULL, '2010-08-12 16:00:13', 'A9225D4A-4871-4277-5638-4C7880A57621', '1', '', '40.00', NULL, 'BW'),
	(18, 'eric', 1, '2010-08-12', 'Y', 'TEST-DEPT', '10', NULL, '2010-08-12 16:00:13', 'A9225D4A-4871-4277-5638-4C7880A57621', '1', '', '40.00', NULL, 'BW'),
	(19, 'eric', 2, '2010-08-10', 'Y', 'TEST-DEPT', '10', NULL, '2010-08-10 16:22:13', 'A9225D4A-4871-4277-5638-4C7880A57621', '1', '', '40.00', NULL, 'BW'),
	(20, 'eric', 2, '2010-08-13', 'Y', 'TEST-DEPT', '11', NULL, '2010-08-13 16:22:22', 'A9225D4A-4871-4277-5638-4C7880A57621', '1', '', '40.00', NULL, 'BW'),
	(21, 'admin', 3, '2010-08-15', 'Y', 'NODEP', 'NOP', NULL, '2010-08-12 16:00:13', 'A9225D4A-4871-4277-5638-4C7880A57621', '1', '', '40.00', NULL, 'BW'),
	(22, 'admin', 4, '2010-08-15', 'Y', 'NODEP', 'A10', NULL, '2010-08-12 16:00:13', 'A9225D4A-4871-4277-5638-4C7880A57621', '1', '', '40.00', NULL, 'BW'),
	(23, 'admin', 5, '2010-08-15', 'Y', 'LORA-DEPT', 'A10', NULL, '2010-08-12 16:00:13', 'A9225D4A-4871-4277-5638-4C7880A57621', '1', '', '40.00', NULL, 'BW'),
	(24, 'admin', 6, '2010-01-01', 'Y', 'SHFT-DEPT', 'A10', NULL, '2010-08-12 16:00:13', 'A9225D4A-4871-4277-5638-4C7880A57621', '1', '', '40.00', NULL, 'BW');

#
# Departments
DELETE FROM `tk_dept_s`;
INSERT INTO `tk_dept_s` (`ID`) VALUES (1000);
DELETE FROM `tk_dept_t`;
INSERT INTO `tk_dept_t` (`tk_dept_id`,`dept`,`DESCRIPTION`,`ORG`,`CHART`,`EFFDT`,`TIMESTAMP`,`ACTIVE`) VALUES
    (100 , 'TEST-DEPT'  , 'test department'  , 'TEST' , 'DEPT' , '2010-01-31' , '2010-07-27 10:25:13' , 'Y')  ,
    (101 , 'TEST-DEPT1' , 'test department1' , 'TEST' , 'DEPT' , '2010-01-31' , '2010-07-27 10:25:13' , 'Y')  ,
    (102 , 'TEST-DEPT2' , 'test department2' , 'TEST' , 'DEPT' , '2010-01-31' , '2010-07-27 10:25:13' , 'Y')  ,
    (103 , 'TEST-DEPT3' , 'test department3' , 'TEST' , 'DEPT' , '2010-01-31' , '2010-07-27 10:25:13' , 'Y')  ,
    (104 , 'TEST-DEPT4' , 'test department4' , 'TEST' , 'DEPT' , '2010-01-31' , '2010-07-27 10:25:13' , 'Y')  ,
    (105 , 'TEST-DEPT5' , 'test department5' , 'TEST' , 'DEPT' , '2010-01-31' , '2010-07-27 10:25:13' , 'Y')  ,
    (106 , 'TEST-DEPT6' , 'test department6' , 'TEST' , 'DEPT' , '2010-01-31' , '2010-07-27 10:25:13' , 'Y')  ,
    (107 , 'NODEP'      , 'test department7' , 'NODEP', 'DEPT' , '2010-01-31' , '2010-07-27 10:25:13' ,'Y'),
    (108 , 'TEST-DEPT7' , 'test department7' , 'TEST' , 'DEPT' , '2010-01-31' , '2010-07-27 10:25:13' , 'Y'),
    (109 , 'LORA-DEPT'  , 'loras department' , 'LORA' , 'DEPT' , '2010-01-31' , '2010-07-27 10:25:13' , 'Y'),
    (110 , 'SHFT-DEPT'  , 'shift department' , 'SHFT' , 'DEPT' , '2010-01-01' , '2010-07-27 10:25:13' , 'Y');

#
# Work Areas
DELETE FROM `tk_work_area_s`;
INSERT INTO `tk_work_area_s` (`ID`) VALUES (1000);
DELETE FROM `tk_work_area_t`;
INSERT INTO `tk_work_area_t` (`TK_WORK_AREA_ID`, `WORK_AREA`, `EFFDT`,`ACTIVE`,`DESCR`,`DEPT`,`DEFAULT_OVERTIME_PREFERENCE`,`ADMIN_DESCR`,`USER_PRINCIPAL_ID`,`TIMESTAMP`,`OBJ_ID`,`VER_NBR`) VALUES
    (1  , 30,    '2010-01-01', 'Y', 'SDR1 Work Area',     'TEST-DEPT', NULL, 'work area admin description', 'admin', '2010-07-27 10:25:13', '7EE387AB-26B0-B6A6-9C4C-5B5F687F0E97', '20'),
    (100,'1234', '2010-01-05', 'Y', 'work area description', 'TEST-DEPT', 'OT1', 'work area admin description', 'admin', '2010-07-27 10:25:13', '7EE387AB-26B0-B6A6-9C4C-5B5F687F0E97', '20'),
    (101, '2345', '2010-01-05', 'Y', 'work area description2', 'TEST-DEPT2', 'OT1', 'work area admin description2', 'admin', '2010-07-27 10:25:13', '7EE387AB-26B0-B6A6-9C4C-5B5F687F0E97', '20'),
    (102, '3456', '2010-01-05', 'Y', 'work area description3', 'TEST-DEPT3', 'OT1', 'work area admin description2', 'admin', '2010-07-27 10:25:13', '7EE387AB-26B0-B6A6-9C4C-5B5F687F0E97', '20'),
    (103, '4567', '2010-01-05', 'Y', 'work area description4', 'TEST-DEPT4', 'OT1', 'work area admin description2', 'admin', '2010-07-27 10:25:13', '7EE387AB-26B0-B6A6-9C4C-5B5F687F0E97', '20'),
    (104, '1000', '2010-01-05', 'Y', 'loras work area', 'LORA-DEPT', 'OT1', 'work area admin description2', 'admin', '2010-07-27 10:25:13', '7EE387AB-26B0-B6A6-9C4C-5B5F687F0E97', '20'),
    (105, '1100', '2010-01-01', 'Y', 'shift-workarea', 'SHFT-DEPT', 'OT1', 'work area admin description2', 'admin', '2010-07-27 10:25:13', '7EE387AB-26B0-B6A6-9C4C-5B5F687F0E97', '20');

#
# Task
DELETE FROM `tk_task_s`;
INSERT INTO `tk_task_s` (`ID`) VALUES ('1000');
DELETE FROM `tk_task_t`;
INSERT INTO `tk_task_t`(`tk_task_id`,`task`,`work_area`,`tk_work_area_id`,`descr`,`admin_descr`,`obj_id`, `ver_nbr`,`USER_PRINCIPAL_ID`)  VALUES
    (1,  30, 30,     1  ,'SDR1 task'    , 'admin description 1', '8421CD29-E1F4-4B9A-AE33-F3F4752505CE', '1', 'admin'),
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
    (1  , 'admin' , 30, '2010-01-01' , 30 , 30, '8421CD29-E1F4-4B9A-AE33-F3F4752505CE' , '2010-07-27 10:25:13' , '1' , 'Y')  ,
    (2  , 'admin' , 30, '2010-01-01' , 5555, 30, '8421CD29-E1F4-4B9A-AE33-F3F4752505CE' , '2010-07-27 10:25:13' , '1' , 'Y')  ,
    (10 , 'admin' , 1 , '2010-08-01' , 1234 , 1 , '8421CD29-E1F4-4B9A-AE33-F3F4752505CE' , '2010-07-27 10:25:13' , '1' , 'Y')  ,
    (11 , 'admin' , 2 , '2010-08-01' , 1234 , 2 , '8421CD29-E1F4-4B9A-AE33-F3F4752505CE' , '2010-07-27 10:25:13' , '1' , 'Y')  ,
    (12 , 'eric'  , 1 , '2010-08-01' , 2345 , 1 , '8421CD29-E1F4-4B9A-AE33-F3F4752505CE' , '2010-07-27 10:25:13' , '1' , 'Y')  ,
    (13 , 'eric'  , 2 , '2010-08-01' , 3456 , 1 , '8421CD29-E1F4-4B9A-AE33-F3F4752505CE' , '2010-07-27 10:25:13' , '1' , 'Y')  ,
    (14 , 'admin' , 3 , '2010-08-15' , 1234 , 1 , '8421CD29-E1F4-4B9A-AE33-F3F4752505CE' , '2010-08-27 10:25:13' , '1' , 'Y')  ,
    (15 , 'admin' , 1 , '2010-08-04' , 1234 , 1 , '8421CD29-E1F4-4B9A-AE33-F3F4752505CE' , '2010-08-27 10:25:13' , '1' , 'Y')  ,
    (16 , 'admin' , 4 , '2010-08-15' , 1234 , 1 , '8421CD29-E1F4-4B9A-AE33-F3F4752505CE' , '2010-08-27 10:25:13' , '1' , 'Y')  ,
    (17 , 'admin' , 5 , '2010-08-15' , 1000 , 4 , '8421CD29-E1F4-4B9A-AE33-F3F4752505CE' , '2010-08-27 10:25:13' , '1' , 'Y')  ,
    (18 , 'admin' , 6 , '2010-01-01' , 1100 , 5 , '8421CD29-E1F4-4B9A-AE33-F3F4752505CE' , '2010-08-27 10:25:13' , '1' , 'Y')  ;

#
# dept earn code
DELETE FROM `tk_dept_earn_code_s`;
INSERT INTO `tk_dept_earn_code_s` VALUES('1000');
DELETE FROM `tk_dept_earn_code_t`;
INSERT INTO `TK_DEPT_EARN_CODE_T`
    (`TK_DEPT_EARN_CODE_ID` , `DEPT`       , `TK_SAL_GROUP` , `EARN_CODE` , `EMPLOYEE` , `APPROVER` , `ORG_ADMIN` , `EFFDT`      , `TIMESTAMP`           , `ACTIVE`) VALUES
    (1                    , 'TEST-DEPT'  , 'SD1'          , 'RGH'       , 1          , 1          , 1           , '2010-08-01' , '2010-01-01 08:08:08' , 'Y')               ,
    (2                    , 'TEST-DEPT'  , 'SD1'          , 'RGN'       , 1          , 1          , 1           , '2010-08-01' , '2010-01-01 08:08:08' , 'Y')               ,
    (3                    , 'INVALID'  	 , 'INVALID'      , 'INV'       , 0          , 1          , 0           , '2010-08-01' , '2010-01-01 08:08:08' , 'Y')       		,
    (10                   , 'TEST-DEPT'  , 'A10'          , 'RGH'       , 1          , 1          , 1           , '2010-08-01' , '2010-01-01 08:08:08' , 'Y')               ,
    (11                   , 'TEST-DEPT'  , 'A10'          , 'SCK'       , 1          , 1          , 1           , '2010-08-01' , '2010-01-01 08:08:08' , 'Y')               ,
    (12                   , 'TEST-DEPT'  , 'A10'          , 'VAC'       , 1          , 1          , 1           , '2010-08-01' , '2010-01-01 08:08:08' , 'Y')               ,
    (13                   , 'TEST-DEPT'  , 'A10'          , 'WEP'       , 1          , 1          , 1           , '2010-08-01' , '2010-01-01 08:08:08' , 'Y')               ,
    (14                   , 'TEST-DEPT2' , 'A10'          , 'HAZ'       , 1          , 1          , 1           , '2010-08-01' , '2010-01-01 08:08:08' , 'Y')               ,
    (15                   , 'TEST-DEPT2' , 'A10'          , 'HIP'       , 1          , 1          , 1           , '2010-08-01' , '2010-01-01 08:08:08' , 'Y')               ,
    (16                   , 'TEST-DEPT2' , 'A10'          , 'OC1'       , 1          , 1          , 1           , '2010-08-01' , '2010-01-01 08:08:08' , 'Y')               ,
    (17                   , 'TEST-DEPT2' , 'A10'          , 'OC2'       , 1          , 1          , 1           , '2010-08-01' , '2010-01-01 08:08:08' , 'Y')               ,
    (18                   , '*'          , 'A10'          , 'XYZ'       , 1          , 1          , 1           , '2010-08-01' , '2010-01-01 08:08:08' , 'Y')               ,
    (19                   , 'TEST-DEPT'  , '*'            , 'XYY'       , 1          , 1          , 1           , '2010-08-01' , '2010-01-01 08:08:08' , 'Y')               ,
    (20                   , '*'          , '*'            , 'XZZ'       , 1          , 1          , 1           , '2010-08-01' , '2010-01-01 08:08:08' , 'Y')               ,
    (21                   , 'LORA-DEPT'  , 'A10'          , 'RGH'       , 1          , 1          , 1           , '2010-08-01' , '2010-01-01 08:08:08' , 'Y')               ,
    (22                   , 'LORA-DEPT'  , 'A10'          , 'SCK'       , 1          , 1          , 1           , '2010-08-01' , '2010-01-01 08:08:08' , 'Y')               ,
    (23                   , 'LORA-DEPT'  , 'A10'          , 'VAC'       , 1          , 1          , 1           , '2010-08-01' , '2010-01-01 08:08:08' , 'Y')       		;

#
# earn code
DELETE FROM `tk_earn_code_s`;
INSERT INTO `tk_earn_code_s` VALUES('1000');
DELETE FROM `tk_earn_code_T`;
INSERT INTO `TK_EARN_CODE_T` (`TK_EARN_CODE_ID`, `EARN_CODE`, `DESCR`, `RECORD_TIME`,`RECORD_HOURS`,`RECORD_AMOUNT`,`EFFDT`, `TIMESTAMP`, `ACTIVE`) VALUES
  (1  , 'SDR' , 'SHIFT DIFF'        , '1','0','0', '2010-01-01' , '2010-01-01 08:08:08' , 'Y') ,
	(9  , 'RGN' , 'REGULAR'           , '1','0','0', '2010-01-01' , '2010-01-01 08:08:08' , 'Y') ,
	(10 , 'RGH' , 'REGULAR HOURLY'    , '1','0','0', '2010-01-01' , '2010-01-01 08:08:08' , 'Y') ,
	(11 , 'SCK' , 'SICK'              , '1','0','0', '2010-01-01' , '2010-01-01 08:08:08' , 'Y') ,
	(12 , 'VAC' , 'VACATION'          , '1','0','0', '2010-01-01' , '2010-01-01 08:08:08' , 'Y') ,
	(13 , 'WEP' , 'EMERGENCY'         , '1','0','0', '2010-01-01' , '2010-01-01 08:08:08' , 'Y') ,
	(14 , 'HAZ' , 'HAZARD DAY'        , '1','0','0', '2010-01-01' , '2010-01-01 08:08:08' ,'Y') ,
	(15 , 'HIP' , 'HOLIDAY INCENTIVE' , '1','0','0', '2010-01-01' , '2010-01-01 08:08:08' , 'Y') ,
	(16 , 'OC1' , 'ON CALL - 1.50'    , '1','0','0', '2010-01-01' , '2010-01-01 08:08:08' , 'Y') ,
	(17 , 'OC2' , 'ON CALL - 2.00'    , '1','0','0', '2010-01-01' , '2010-01-01 08:08:08' ,'Y') ,
	(18 , 'PRM' , 'PREMIUM'           , '1','0','0', '2010-01-01' , '2010-01-01 08:08:08' , 'Y') ,
	(19 , 'XYZ' , 'XYZ'               , '1','0','0', '2010-01-01' , '2010-01-01 08:08:08' , 'Y') ,
	(20 , 'XYY' , 'XYY'               , '1','0','0', '2010-01-01' , '2010-01-01 08:08:08' , 'Y') ,
	(21 , 'XZZ' , 'XZZ'               , '1','0','0', '2010-01-01' , '2010-01-01 08:08:08' , 'Y') ;

# Sal Group
DELETE FROM `tk_sal_group_s`;
INSERT INTO `tk_sal_group_S` (`ID`) VALUES ('1000');
DELETE FROM `tk_sal_group_t`;
INSERT INTO `tk_sal_group_t` (`TK_SAL_GROUP_ID`, `TK_SAL_GROUP`, `EFFDT`, `TIMESTAMP`, `ACTIVE`) VALUES
    (1,  'SD1', '2010-01-01', '2010-01-01 01:01:01' , 'Y'),
    (10, 'A10', '2010-01-01', '2010-01-01 08:08:08' , 'Y'),
    (11, 'S10', '2010-01-01', '2010-01-01 08:08:08' , 'Y'),
    (12, 'A12', '2010-01-01', '2010-01-01 08:08:08' , 'Y'),
    (13, 'S12', '2010-01-01', '2010-01-01 08:08:08' , 'Y'),
    (14, 'NOP', '2010-01-01', '2010-01-01 08:08:08' , 'Y');

# HR Pay Types
DELETE FROM `hr_paytype_s`;
INSERT INTO `hr_paytype_s` (`ID`)	VALUES	('1000');
DELETE FROM `hr_paytype_t`;
INSERT INTO `hr_paytype_t` (`HR_PAYTYPE_ID`,`PAYTYPE`,`DESCR`,`REG_ERN_CODE`,`EFFDT`,`TIMESTAMP`,`OBJ_ID`,`VER_NBR`,`ACTIVE`) VALUES
	(1, 'BW', 'description', 'RGN', '2010-08-01', '2010-01-01 16:01:07', '47326FEA-46E7-7D89-0B13-85DFA45EA8C1', '1','Y'),
	(2, 'BW', 'description', 'RGN', '2010-01-01', '2010-01-01 16:01:07', '47326FEA-46E7-7D89-0B13-85DFA45EA8C1', '1','Y');


DELETE FROM `tk_earn_group_s`;
INSERT INTO `tk_earn_group_s` VALUES ('1000');
DELETE FROM `tk_earn_group_t`;
INSERT INTO `tk_earn_group_t` (`tk_earn_group_id`,`earn_group`,`descr`,`effdt`,`active`,`OBJ_ID`,`VER_NBR`,`timestamp`) VALUES
  (  1,'SD1','Test SD1', '2010-01-01', 'Y', '', 1, '2010-01-01 01:01:01'),
  (  2,'SD2','Test SD2', '2010-01-01', 'Y', '', 1, '2010-01-01 01:01:01'),
  (  3,'SD3','Test SD3', '2010-01-01', 'Y', '', 1, '2010-01-01 01:01:01'),
	(100,'REG','Regular', '2010-01-01','Y','7EE387AB-26B0-B6A6-9C4C-5B5F687F0E97','20','2010-07-27 10:25:13' ),
	(101,'OVT','Overtime', '2010-01-01','Y','7EE387AB-26B0-B6A6-9C4C-5B5F687F0E97','20','2010-07-27 10:25:13' ),
	(102,'TC1','Regular', '2010-01-01','Y','7EE387AB-26B0-B6A6-9C4C-5B5F687F0E97','20','2010-07-27 10:25:13' ),
	(103,'TC2','Regular', '2010-01-01','Y','7EE387AB-26B0-B6A6-9C4C-5B5F687F0E97','20','2010-07-27 10:25:13' ),
	(104,'TC3','Regular', '2010-01-01','Y','7EE387AB-26B0-B6A6-9C4C-5B5F687F0E97','20','2010-07-27 10:25:13' );

DELETE FROM `tk_earn_group_def_s`;
INSERT INTO `tk_earn_group_def_s` VALUES ('1000');
DELETE FROM `tk_earn_group_def_t`;
INSERT INTO `tk_earn_group_def_t` (`tk_earn_group_def_id`, `tk_earn_group_id`,`earn_code`,`OBJ_ID`,`VER_NBR`) VALUES
  (  1,  1,'REG','7EE387AB-26B0-B6A6-9C4C-5B5F687F0E97',1),
  (  2,  1,'RGN','7EE387AB-26B0-B6A6-9C4C-5B5F687F0E97',1),
  (  3,  2,'ABC', '7EE387AB-26B0-B6A6-9C4C-5B5F687F0E97',1),
  (  4,  3,'XYZ','7EE387AB-26B0-B6A6-9C4C-5B5F687F0E97',1),
	(100,100,'REG','7EE387AB-26B0-B6A6-9C4C-5B5F687F0E97',1),
	(101,100,'RGN','7EE387AB-26B0-B6A6-9C4C-5B5F687F0E97',1),
	(102,100,'RGH','7EE387AB-26B0-B6A6-9C4C-5B5F687F0E97',1),
	(109,101,'OVT','7EE387AB-26B0-B6A6-9C4C-5B5F687F0E97',1);

DELETE FROM `tk_roles_s`;
INSERT INTO `tk_roles_s` VALUES ('1000');
DELETE FROM `tk_roles_t`;
INSERT INTO `tk_roles_t` (`TK_ROLES_ID`, `PRINCIPAL_ID`, `ROLE_NAME`, `USER_PRINCIPAL_ID`, `WORK_AREA`, `DEPT`, `EFFDT`, `TIMESTAMP`, `ACTIVE`) VALUES
    ('1', 'admin', 'TK_APPROVER', 'admin', '999', NULL, '2010-08-01', '2010-08-01 15:10:57', 'Y'),
    ('2', 'admin', 'TK_APPROVER', 'admin', '999', NULL, '2010-08-10', '2010-08-10 15:10:57', 'Y'),
    ('3', 'admin', 'TK_APPROVER', 'admin', '999', NULL, '2010-08-20', '2010-08-20 15:10:57', 'Y'),
    ('4', 'admin', 'TK_APPROVER', 'admin', '999', NULL, '2010-08-20', '2010-08-20 15:11:57', 'Y'),
    ('5', 'admin', 'TK_APPROVER', 'admin', '999', NULL, '2010-08-20', '2010-08-20 15:12:57', 'Y'),
    ('6', 'eric',  'TK_APPROVER', 'admin', '999', NULL, '2010-08-01', '2010-08-01 16:10:57', 'Y'),
    ('7', 'eric',  'TK_APPROVER', 'admin', '999', NULL, '2010-08-10', '2010-08-10 16:10:57', 'Y'),
    ('8', 'eric',  'TK_APPROVER', 'admin', '999', NULL, '2010-08-20', '2010-08-20 16:10:57', 'Y'),
    ('9', 'eric',  'TK_APPROVER', 'admin', '999', NULL, '2010-08-20', '2010-08-20 16:11:57', 'Y'),
    ('10','eric',  'TK_APPROVER', 'admin', '999', NULL, '2010-08-20', '2010-08-20 16:12:57', 'Y'),
    ('11','admin', 'TK_ORG_ADMIN', 'admin', '999', NULL, '2010-08-01', '2010-08-01 15:10:57', 'Y'),
    ('12','admin', 'TK_ORG_ADMIN', 'admin', '999', NULL, '2010-08-10', '2010-08-10 15:10:57', 'Y'),
    ('13','admin', 'TK_ORG_ADMIN', 'admin', '999', NULL, '2010-08-20', '2010-08-20 15:10:57', 'Y'),
    ('14','admin', 'TK_ORG_ADMIN', 'admin', '999', NULL, '2010-08-20', '2010-08-20 15:11:57', 'Y'),
    ('15','admin', 'TK_ORG_ADMIN', 'admin', '999', NULL, '2010-08-20', '2010-08-20 15:12:57', 'Y'),
    ('16','eric',  'TK_ORG_ADMIN', 'admin', '999', NULL, '2010-08-01', '2010-08-01 16:10:57', 'Y'),
    ('17','eric',  'TK_ORG_ADMIN', 'admin', '999', NULL, '2010-08-10', '2010-08-10 16:10:57', 'Y'),
    ('18','eric',  'TK_ORG_ADMIN', 'admin', '999', NULL, '2010-08-20', '2010-08-20 16:10:57', 'Y'),
    ('19','eric',  'TK_ORG_ADMIN', 'admin', '999', NULL, '2010-08-20', '2010-08-20 16:11:57', 'Y'),
    ('20','eric',  'TK_ORG_ADMIN', 'admin', '999', NULL, '2010-08-20', '2010-08-20 16:12:57', 'Y'),
    ('21','eric',  'TK_APPROVER', 'admin', '999', NULL, '2010-08-20', '2010-08-20 16:13:57', 'N'),
    ('22','admin', 'TK_APPROVER', 'admin', '1234', NULL, '2010-01-05', '2010-01-05 15:12:57', 'Y');

#
# Pay Calendar
DELETE FROM `tk_py_calendar_s`;
INSERT INTO `tk_py_calendar_s`	(`ID`)	VALUES	(1000);
DELETE FROM `tk_py_calendar_t`;
INSERT INTO `tk_py_calendar_t`	(`tk_py_calendar_id`,	`calendar_group`,`flsa_begin_day`, `flsa_begin_time`)	VALUES
	(1,  'BWN-CAL', 'Sun', '0:00:00'),
	(2,  'BWS-CAL', 'Sun', '0:00:00');

#
# Pay Calendar Dates
DELETE FROM `tk_py_calendar_entries_s`;
INSERT INTO `tk_py_calendar_entries_s`	(`ID`)	VALUES	(1000);
DELETE FROM `tk_py_calendar_entries_t`;
INSERT INTO `tk_py_calendar_entries_t` (`tk_py_calendar_entry_id`,`tk_py_calendar_id`,`begin_period_date`,`end_period_date`,`initiate_date`,`initiate_time`,`end_pay_period_date`,`end_pay_period_time`,`employee_approval_date`,`employee_approval_time`,`supervisor_approval_date`,`supervisor_approval_time`) VALUES
	(1,  2, '2010-08-01 00:00:00', '2010-08-15 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
	(2,  2, '2010-08-15 00:00:00', '2010-09-01 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
	(3,  2, '2010-09-01 00:00:00', '2010-09-15 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
	(4,  2, '2010-09-15 00:00:00', '2010-10-01 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
	(5,  2, '2010-10-01 00:00:00', '2010-10-15 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
	(8,  2, '2010-10-15 00:00:00', '2010-11-01 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
	(9,  2, '2010-11-01 00:00:00', '2010-11-15 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
	(10, 2, '2010-11-15 00:00:00', '2010-12-01 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
	(11, 2, '2010-01-01 00:00:00', '2010-01-15 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
	(12, 2, '2010-01-15 00:00:00', '2010-02-01 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
  (13, 2, '2010-02-01 00:00:00', '2010-02-15 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
  (14, 2, '2010-02-15 00:00:00', '2010-03-01 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
  (15, 2, '2010-03-01 00:00:00', '2010-03-15 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
  (16, 2, '2010-03-15 00:00:00', '2010-04-01 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
  (17, 2, '2010-04-01 00:00:00', '2010-04-15 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
  (18, 2, '2010-04-15 00:00:00', '2010-05-01 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
  (19, 2, '2010-05-01 00:00:00', '2010-05-15 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
  (20, 2, '2010-05-15 00:00:00', '2010-06-01 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
  (21, 2, '2010-06-01 00:00:00', '2010-06-15 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
  (22, 2, '2010-06-15 00:00:00', '2010-07-01 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
  (23, 2, '2010-07-01 00:00:00', '2010-07-15 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
  (24, 2, '2010-07-15 00:00:00', '2010-08-01 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
  (25, 2, '2010-12-01 00:00:00', '2010-12-15 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
  (26, 2, '2010-12-15 00:00:00', '2011-01-01 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
  (27, 1, '2010-01-01 12:00:00', '2010-01-16 12:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
  (28, 1, '2010-01-16 12:00:00', '2010-02-01 12:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);


DELETE FROM `tk_daily_overtime_rl_s`;
INSERT INTO `tk_daily_overtime_rl_s` (`ID`) VALUES (1000);

DELETE FROM `HR_WORK_SCHEDULE_S`;
INSERT INTO `HR_WORK_SCHEDULE_S` (`ID`) VALUES (1000);

DELETE FROM `HR_WORK_SCHEDULE_ENTRY_S`;
INSERT INTO `HR_WORK_SCHEDULE_ENTRY_S` (`ID`) VALUES (1000);

insert into hr_work_schedule_t values(1,'test-schedule','2010-01-01','TEST-DEPT',
											1234,'admin','Y',uuid(),1,now());
insert into hr_work_schedule_t values(2,'test-schedule','2010-01-01','TEST-DEPT',
											-1,'admin','Y',uuid(),1,now());
insert into hr_work_schedule_t values(3,'test-schedule','2010-01-01','*',
											-1,'admin','Y',uuid(),1,now());
insert into hr_work_schedule_t values(4,'test-schedule','2010-01-01','TEST-DEPT',
											1234,'*','Y',uuid(),1,now());
insert into hr_work_schedule_t values(5,'test-schedule','2010-01-01','*',
											-1,'*','Y',uuid(),1,now());
insert into hr_work_schedule_t values(11,'test-schedule','2010-01-01','INVALID',
											1234,'*','Y',uuid(),1,now());
insert into hr_work_schedule_t values(12,'test-schedule','2010-01-01','TEST-DEPT',
											-1,'*','Y',uuid(),1,now());


DELETE FROM `tk_time_collection_rl_s`;
INSERT INTO `tk_time_collection_rl_s` VALUES('1000');
DELETE FROM `tk_time_collection_rl_t`;
INSERT INTO `tk_time_collection_rl_t` (`TK_TIME_COLL_RULE_ID`,`DEPT`,`WORK_AREA`,`EFFDT`,`CLOCK_USERS_FL`,`HRS_DISTRIBUTION_FL`,`USER_PRINCIPAL_ID`,`TIMESTAMP`,`ACTIVE`) VALUES
  ('1' , 'TEST-DEPT' , 1234 , '2010-01-01' , 1 , 1 , 'admin' , '2010-01-01 08:08:08' , 'Y')  ,
  ('2' , '*'         , 1234 , '2010-01-01' , 1 , 1 , 'admin' , '2010-01-01 08:08:08' , 'Y')  ,
  ('3' , 'TEST-DEPT' , -1   , '2010-01-01' , 1 , 1 , 'admin' , '2010-01-01 08:08:08' , 'Y')  ,
  ('4' , '*'         , -1   , '2010-01-01' , 1 , 1 , 'admin' , '2010-01-01 08:08:08' , 'Y');

DELETE FROM `tk_clock_log_s`;
INSERT INTO `tk_clock_log_s` VALUES('1000');
DELETE FROM `tk_clock_log_t`;
INSERT INTO `tk_clock_log_t` (`TK_CLOCK_LOG_ID`,`PRINCIPAL_ID`,`JOB_NUMBER`,`WORK_AREA`,`TASK`,`TK_WORK_AREA_ID`,`TK_TASK_ID`,`CLOCK_TS`,`CLOCK_TS_TZ`,`CLOCK_ACTION`,`IP_ADDRESS`,`USER_PRINCIPAL_ID`,`TIMESTAMP`,`HR_JOB_ID`) VALUES
  ('1' , 'admin' , 30 , 9999 ,9999, 1 , 1 , '2010-01-01 08:08:08', "TEST" , "_","TEST" ,'admin' , '2010-01-01 08:08:08' , 1)  ;

DELETE FROM `tk_document_header_t`;
INSERT INTO `tk_document_header_t` (`DOCUMENT_ID`,`PRINCIPAL_ID`,`DOCUMENT_STATUS`) VALUES
  ('1','admin',"I")  ;

DELETE FROM `tk_dept_lunch_rl_t`;
INSERT INTO `tk_dept_lunch_rl_t` (`TK_DEPT_LUNCH_RL_ID`,`DEPT`,`WORK_AREA`, `PRINCIPAL_ID`, `JOB_NUMBER`, `EFFDT`, `REQUIRED_CLOCK_FL`, `MAX_MINS`, `USER_PRINCIPAL_ID`, `TIMESTAMP`, `ACTIVE`, `SHIFT_HOURS`, `DEDUCTION_MINS`) VALUES
  ('1','INVALID','1234','admin','20','2010-01-01','TST', '30', 'admin', '2010-01-01 08:08:08', 'Y', '2', '30') ,
  ('2','TEST-DEPT','1234','admin','20','2010-01-01','TST', '30', 'admin', '2010-01-01 08:08:08', 'Y', '2', '30') ,
  ('3','TEST-DEPT','9999','admin','20','2010-01-01','TST', '30', 'admin', '2010-01-01 08:08:08', 'Y', '2', '30') ;

DELETE FROM `tk_holiday_calendar_t`;
INSERT INTO `tk_holiday_calendar_T` (`HOLIDAY_CALENDAR_ID`,`HOLIDAY_CALENDAR_GROUP`,`DESCR`) VALUES
  (1,'REG', "Regular");

DELETE FROM `la_accruals_t`;
INSERT INTO `la_accruals_T` (`LA_ACCRUALS_ID`,`PRINCIPAL_ID`,`ACCRUAL_CATEGORY`) VALUES
  (1,'admin', "TEX");

DELETE FROM `tk_time_block_s`;
INSERT INTO `tk_time_block_s` VALUES ('1000');

DELETE FROM `tk_hour_detail_s`;
INSERT INTO `tk_hour_detail_s` VALUES ('1000');

insert into tk_principal_calendar_t values('admin','BWS-CAL','HOL','2010-01-01', now(),uuid(),1);
insert into tk_principal_calendar_t values('eric','BW-CAL1','HOL','2010-01-01', now(),uuid(),1);

insert into tk_daily_overtime_rl_t values(1,'BL','BW','2010-01-01','admin',now(),'TEST-DEPT',1234,0,2,3,'OVT','Y','REG','OVT');
insert into tk_daily_overtime_rl_t values(2,'BL','BW','2010-01-01','admin',now(),'TEST-DEPT',1234,-1,2,3,'OVT','Y','REG','OVT');
insert into tk_daily_overtime_rl_t values(3,'BL','BW','2010-01-01','admin',now(),'TEST-DEPT',-1,0,2,3,'OVT','Y','REG','OVT');
insert into tk_daily_overtime_rl_t values(4,'BL','BW','2010-01-01','admin',now(),'TEST-DEPT',-1,-1,2,3,'OVT','Y','REG','OVT');
insert into tk_daily_overtime_rl_t values(5,'BL','BW','2010-01-01','admin',now(),'INVALID',-1,-1,2,3,'OVT','Y','REG','OVT');

insert into tk_system_lunch_rl_t (`TK_SYSTEM_LUNCH_RL_ID`,`EFFDT`,`ACTIVE`,`USER_PRINCIPAL_ID`,`SHOW_LUNCH_BUTTON`) values
(1, '2010-01-01', 'Y', 'admin', 'Y');

insert into tk_user_pref_t values('admin','America/Chicago');

ALTER TABLE tk_shift_differential_rl_t add column `from_earn_group` varchar(10) NULL DEFAULT NULL;

insert into tk_user_pref_t values('admin','America/Chicago');

INSERT INTO `la_accruals_t` VALUES (1,'admin',1,'2010-10-10 00:00:00','1.00','2.00','3.00','7EE387AB-26B0-B6A6-9C4C-5B5F687F0E97',1);
INSERT INTO `la_accrual_categories_t` VALUES  (1,'TST','DESR','2010-10-10','7EE387AB-26B0-B6A6-9C4C-5B5F687F0E97',1,'Y','2010-10-10 00:00:00');
INSERT INTO `tk_system_lunch_rl_t` VALUES  (1,'2010-11-11','2010-11-11 00:00:00','Y','7EE387AB-26B0-B6A6-9C4C-5B5F687F0E97',1,'admin','Y');

insert into tk_weekly_ovt_group_t values(1);

