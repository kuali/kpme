ALTER TABLE `lm_leave_block_t` CHANGE COLUMN `description` `description` VARCHAR(255) NULL  , CHANGE COLUMN `apply_to_ytd_used` `apply_to_ytd_used` VARCHAR(255) NULL  , CHANGE COLUMN `PRINCIPAL_ID_MODIFIED` `PRINCIPAL_ID_MODIFIED` VARCHAR(40) NULL  , CHANGE COLUMN `REQUEST_STATUS` `REQUEST_STATUS` VARCHAR(1) NULL  ;
ALTER TABLE `lm_leave_block_hist_t` CHANGE COLUMN `description` `description` VARCHAR(255) NULL  , CHANGE COLUMN `apply_to_ytd_used` `apply_to_ytd_used` VARCHAR(255) NULL  , CHANGE COLUMN `principal_id_modified` `principal_id_modified` VARCHAR(40) NULL  , CHANGE COLUMN `request_status` `request_status` VARCHAR(1) NULL  ;