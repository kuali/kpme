DROP TABLE IF EXISTS `tk_batch_job_s`;
CREATE TABLE `tk_batch_job_s` (
  `id` bigint(19) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=2085 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;


DROP TABLE IF EXISTS `tk_batch_job_t`;
CREATE TABLE `tk_batch_job_t` (
  `TK_BATCH_JOB_ID` bigint(20) NOT NULL,
  `BATCH_JOB_NAME` varchar(40) NOT NULL,
  `BATCH_JOB_STATUS` varchar(1) NOT NULL,
  `TK_PAY_CALENDAR_ENTRY_ID` BIGINT(20) NOT NULL,
  `TIME_ELAPSED` BIGINT(10) NOT NULL,
  `timestamp` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`TK_BATCH_JOB_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;