-- *********************************************************************
-- Update Database Script
-- *********************************************************************
-- Change Log: src/main/resources/org/kuali/kpme/kpme-db/db/db.changelog-main-upgrade.xml
-- Ran at: 9/23/14 8:43 AM
-- Against: TK@jdbc:oracle:thin:system/manager@localhost:1521:XE
-- Liquibase version: 3.1.1
-- *********************************************************************

SET DEFINE OFF
/

-- Changeset org/kuali/kpme/kpme-db/db/2.1.1/db.changelog-201408221100.xml::1::mlemons
ALTER TABLE TK_WEEKLY_OVERTIME_RL_T ADD APPLY_TO_ERN_GROUP VARCHAR2(10)
/

ALTER TABLE TK_WEEKLY_OVERTIME_RL_T ADD OVERRIDE_WORKAREA_DEFAULT VARCHAR2(1) DEFAULT 'N'
/

UPDATE TK_WEE /LY_OVERTIME_RL_T SET APPLY_TO_ERN_GROUP = CONVERT_FROM_ERN_GROUP
/


-- Changeset org/kuali/kpme/kpme-db/db/2.1.1/db.changelog-201409041150.xml::1::schoo
CREATE TABLE tk_remote_swipe_device_t (IP_ADDRESS VARCHAR2(20) DEFAULT '' NOT NULL, DEVICE_NM VARCHAR2(100) DEFAULT '' NOT NULL, PRINCIPAL_ID VARCHAR2(40) DEFAULT '' NOT NULL, LAST_COMMUNICATE_TIME VARCHAR2(50) DEFAULT '', active VARCHAR2(50) DEFAULT 'N' NOT NULL, CONSTRAINT PK_TK_REMOTE_SWIPE_DEVICE_T PRIMARY KEY (IP_ADDRESS))
/


-- Changeset org/kuali/kpme/kpme-db/db/2.1.1/db.changelog-201409101150.xml::1::jjhanso
ALTER TABLE TK_HOUR_DETAIL_T ADD TTL_MIN DECIMAL(8, 0)
/

ALTER TABLE TK_HOUR_DETAIL_HIST_T ADD TTL_MIN DECIMAL(8, 0)
/

ALTER TABLE TK_TIME_BLOCK_T ADD TTL_MIN DECIMAL(8, 0)
/

ALTER TABLE TK_TIME_BLOCK_HIST_T ADD TTL_MIN DECIMAL(8, 0)
/

UPDATE TK_HOUR_DETAIL_T SET TTL_MIN = (60*HOURS)
/

UPDATE TK_HOUR_DETAIL_HIST_T SET TTL_MIN = (60*HOURS)
/

UPDATE TK_TIME_BLOCK_T SET TTL_MIN = (60*HOURS)
/

UPDATE TK_TIME_BLOCK_HIST_T SET TTL_MIN = (60*HOURS)
/

