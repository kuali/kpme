ALTER TABLE `hr_work_schedule_entry_t` CHANGE COLUMN `HR_WORK_SCHEDULE_ID` `HR_WORK_SCHEDULE` BIGINT(20) NOT NULL  ;


ALTER TABLE `hr_work_schedule_t` ADD COLUMN `HR_WORK_SCHEDULE` BIGINT(20) NOT NULL  AFTER `HR_WORK_SCHEDULE_ID` ;


DROP TABLE IF EXISTS `hr_work_schedule_assign_t`;
CREATE TABLE `hr_work_schedule_assign_t` (
  `HR_WORK_SCHEDULE_ASSIGNMENT_ID` bigint(20) NOT NULL,
  `HR_WORK_SCHEDULE` bigint(20) NOT NULL,
  `DEPT` varchar(21) COLLATE utf8_bin NOT NULL,
  `WORK_AREA` bigint(20),
  `PRINCIPAL_ID` varchar(40),
  `USER_PRINCIPAL_ID` varchar(40),
  `ACTIVE` VARCHAR(1) NOT NULL DEFAULT 'Y',
  `EFFDT` date NOT NULL DEFAULT '0000-00-00',
  `TIMESTAMP` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `OBJ_ID` varchar(36) COLLATE utf8_bin DEFAULT NULL,
  `VER_NBR` bigint(20) NOT NULL DEFAULT '1',
  PRIMARY KEY (`HR_WORK_SCHEDULE_ASSIGNMENT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;



DROP TABLE IF EXISTS `hr_work_schedule_assign_s`;
CREATE TABLE `hr_work_schedule_assign_s` (
  `ID` bigint(19) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM AUTO_INCREMENT=1023 DEFAULT CHARSET=latin1;
