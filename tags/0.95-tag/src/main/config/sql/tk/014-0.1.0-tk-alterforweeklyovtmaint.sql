CREATE TABLE tk_weekly_ovt_group_t 
( `TK_WEEKLY_OVERTIME_GROUP_ID` BIGINT(20) NOT NULL,PRIMARY KEY (`TK_WEEKLY_OVERTIME_GROUP_ID`)) 
ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

insert into tk_weekly_ovt_group_t values(1);

ALTER TABLE `tk_weekly_overtime_rl_t` ADD COLUMN `TK_WEEKLY_OVT_GROUP_ID` BIGINT(20) NOT NULL DEFAULT 1  AFTER `ACTIVE` ;