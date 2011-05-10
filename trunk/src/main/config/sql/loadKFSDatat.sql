/********************READ ME*******************************
Purpose: load KFS data in Rice workflow database

Steps:
	1. Make a folder called: 'C:\kfsDB\CSVfiles\'. You can use any folder you want. If you use a different folder, 
	make sure you change every "LOAD DATA INFILE" commands to use the folder you choose
	2. Copy all csv files from /src/main/config/sql/kfsData to 'C:\kfsDB\CSVfiles\' or the folder you choose
	3. Run this sql file into you krtt database

***********************************************************/

LOAD DATA INFILE 'C:\\kfsDB\\CSVfiles\\krns_nmspc_t.csv' INTO TABLE KRNS_NMSPC_T 
FIELDS TERMINATED BY ',' ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 LINES
(NMSPC_CD,NM,ACTV_IND,APPL_NMSPC_CD,OBJ_ID);

LOAD DATA INFILE 'C:\\kfsDB\\CSVfiles\\krns_parm_dtl_typ_t.csv' INTO TABLE KRNS_PARM_DTL_TYP_T 
FIELDS TERMINATED BY ',' ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 LINES
(NMSPC_CD,PARM_DTL_TYP_CD,OBJ_ID,VER_NBR,NM,ACTV_IND);

LOAD DATA INFILE 'C:\\kfsDB\\CSVfiles\\krns_parm_t.csv' INTO TABLE KRNS_PARM_T 
FIELDS TERMINATED BY ',' ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 LINES
(NMSPC_CD,PARM_DTL_TYP_CD,PARM_NM,OBJ_ID,VER_NBR,PARM_TYP_CD,TXT,PARM_DESC_TXT,CONS_CD,APPL_NMSPC_CD);

LOAD DATA INFILE 'C:\\kfsDB\\CSVfiles\\kr_county_t.csv' INTO TABLE KR_COUNTY_T 
FIELDS TERMINATED BY ',' ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 LINES
(COUNTY_CD,STATE_CD,POSTAL_CNTRY_CD,OBJ_ID,VER_NBR,COUNTY_NM,ACTV_IND);

LOAD DATA INFILE 'C:\\kfsDB\\CSVfiles\\kr_postal_code_t.csv' INTO TABLE KR_POSTAL_CODE_T 
FIELDS TERMINATED BY ','
LINES TERMINATED BY '\n'
IGNORE 1 LINES
(POSTAL_CD,POSTAL_CNTRY_CD,OBJ_ID,POSTAL_STATE_CD,POSTAL_CITY_NM,ACTV_IND);

LOAD DATA INFILE 'C:\\kfsDB\\CSVfiles\\krim_entity_t.csv' INTO TABLE KRIM_ENTITY_T 
FIELDS TERMINATED BY ',' ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 LINES
(ENTITY_ID,OBJ_ID,ACTV_IND);

LOAD DATA INFILE 'C:\\kfsDB\\CSVfiles\\krim_entity_nm_t.csv' INTO TABLE KRIM_ENTITY_NM_T 
FIELDS TERMINATED BY ',' ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 LINES
(ENTITY_NM_ID,OBJ_ID,ENTITY_ID,NM_TYP_CD,FIRST_NM,MIDDLE_NM,LAST_NM,SUFFIX_NM,TITLE_NM,DFLT_IND,ACTV_IND);

LOAD DATA INFILE 'C:\\kfsDB\\CSVfiles\\krim_prncpl_t.csv' INTO TABLE KRIM_PRNCPL_T 
FIELDS TERMINATED BY ',' ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 LINES
(PRNCPL_ID,OBJ_ID,PRNCPL_NM,ENTITY_ID,ACTV_IND);

LOAD DATA INFILE 'C:\\kfsDB\\CSVfiles\\krim_entity_ent_typ_t.csv' INTO TABLE KRIM_ENTITY_ENT_TYP_T 
FIELDS TERMINATED BY ',' ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 LINES
(ENT_TYP_CD,ENTITY_ID,OBJ_ID,ACTV_IND);

LOAD DATA INFILE 'C:\\kfsDB\\CSVfiles\\krim_entity_afltn_t.csv' INTO TABLE KRIM_ENTITY_AFLTN_T 
FIELDS TERMINATED BY ',' ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 LINES
(ENTITY_AFLTN_ID,OBJ_ID,ENTITY_ID,AFLTN_TYP_CD,CAMPUS_CD,DFLT_IND,ACTV_IND);

LOAD DATA INFILE 'C:\\kfsDB\\CSVfiles\\krim_entity_email_t.csv' INTO TABLE KRIM_ENTITY_EMAIL_T 
FIELDS TERMINATED BY ',' ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 LINES
(ENTITY_EMAIL_ID,OBJ_ID,ENTITY_ID,ENT_TYP_CD,EMAIL_TYP_CD,EMAIL_ADDR,DFLT_IND,ACTV_IND);

LOAD DATA INFILE 'C:\\kfsDB\\CSVfiles\\krim_entity_emp_info_t.csv' INTO TABLE KRIM_ENTITY_EMP_INFO_T 
FIELDS TERMINATED BY ',' ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 LINES
(ENTITY_EMP_ID,OBJ_ID,ENTITY_ID,ENTITY_AFLTN_ID,EMP_STAT_CD,EMP_TYP_CD,BASE_SLRY_AMT,PRMRY_IND,ACTV_IND,PRMRY_DEPT_CD,EMP_ID,EMP_REC_ID);

LOAD DATA INFILE 'C:\\kfsDB\\CSVfiles\\krim_entity_ext_id_t.csv' INTO TABLE KRIM_ENTITY_EXT_ID_T 
FIELDS TERMINATED BY ',' ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 LINES
(ENTITY_EXT_ID_ID,OBJ_ID,ENTITY_ID,EXT_ID_TYP_CD,EXT_ID);

LOAD DATA INFILE 'C:\\kfsDB\\CSVfiles\\krim_entity_priv_pref_t.csv' INTO TABLE KRIM_ENTITY_PRIV_PREF_T 
FIELDS TERMINATED BY ',' ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 LINES
(ENTITY_ID,OBJ_ID,SUPPRESS_NM_IND,SUPPRESS_EMAIL_IND,SUPPRESS_ADDR_IND,SUPPRESS_PHONE_IND,SUPPRESS_PRSNL_IND);

LOAD DATA INFILE 'C:\\kfsDB\\CSVfiles\\krim_attr_defn_t.csv' INTO TABLE KRIM_ATTR_DEFN_T 
FIELDS TERMINATED BY ',' ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 LINES
(KIM_ATTR_DEFN_ID,OBJ_ID,VER_NBR,NM,LBL,ACTV_IND,NMSPC_CD,CMPNT_NM);


LOAD DATA INFILE 'C:\\kfsDB\\CSVfiles\\krim_typ_t.csv' INTO TABLE KRIM_TYP_T 
FIELDS TERMINATED BY ',' ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 LINES
(KIM_TYP_ID,OBJ_ID,VER_NBR,NM,SRVC_NM,ACTV_IND,NMSPC_CD);


LOAD DATA INFILE 'C:\\kfsDB\\CSVfiles\\krim_typ_attr_t.csv' INTO TABLE KRIM_TYP_ATTR_T 
FIELDS TERMINATED BY ',' ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 LINES
(KIM_TYP_ATTR_ID,OBJ_ID,VER_NBR,SORT_CD,KIM_TYP_ID,KIM_ATTR_DEFN_ID,ACTV_IND);

/* success. I had to modified the file to add ||\n as line breaker */
LOAD DATA INFILE 'C:\\kfsDB\\CSVfiles\\krim_role_mbr_attr_data_t.csv' INTO TABLE KRIM_ROLE_MBR_ATTR_DATA_T 
FIELDS TERMINATED BY ',' ENCLOSED BY '"'
LINES TERMINATED BY '||\n'
IGNORE 1 LINES
(ATTR_DATA_ID,OBJ_ID,VER_NBR,ROLE_MBR_ID,KIM_TYP_ID,KIM_ATTR_DEFN_ID,ATTR_VAL);

LOAD DATA INFILE 'C:\\kfsDB\\CSVfiles\\krim_role_t-PART1.csv' INTO TABLE KRIM_ROLE_T 
FIELDS TERMINATED BY ',' ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 LINES
(ROLE_ID,OBJ_ID,VER_NBR,ROLE_NM,NMSPC_CD,DESC_TXT,KIM_TYP_ID,ACTV_IND);

/* Not able to load this file in yet, ERROR 1452 (23000): Cannot add or update a child row: a foreign key constraint fails (`krtt`.`krim_role_t`, CONSTRAINT `KRIM_ROLE_TR1` FOREIGN KEY (`KIM_TYP_ID`) REFERENCES `k
rim_typ_t` (`KIM_TYP_ID`)) 
partially loaded in the file, the bad records were saved under krim_role_t-badRecords.csv
*/
LOAD DATA LOCAL INFILE 'C:\\kfsDB\\CSVfiles\\krim_role_t-modified.csv' INTO TABLE KRIM_ROLE_T 
FIELDS TERMINATED BY ',' ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 LINES
(ROLE_ID,OBJ_ID,VER_NBR,ROLE_NM,NMSPC_CD,DESC_TXT,KIM_TYP_ID,ACTV_IND);


/* Not able to load this file in yet, needs krim_role_t loaded first 
ERROR 1452 (23000): Cannot add or update a child row: a foreign key constraint fails (`krtt`.`krim_role_mbr_t`, CONSTRAINT `KRIM_ROLE_MBR_TR1` FOREIGN KEY (`ROLE_ID`) REFERENC
ES `krim_role_t` (`ROLE_ID`) ON DELETE CASCADE
LOAD DATA local INFILE 'C:\\kfsDB\\CSVfiles\\krim_role_mbr_t.csv' INTO TABLE KRIM_ROLE_MBR_T 
FIELDS TERMINATED BY ',' ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 LINES
(ROLE_MBR_ID,OBJ_ID,ROLE_ID,MBR_ID,MBR_TYP_CD);
*/



/* Not able to load this file in yet, needs krim_role_t loaded first 
ERROR 1452 (23000): Cannot add or update a child row: a foreign key constraint fails (`krtt`.`krim_dlgn_t`, CONSTRAINT `KRIM_DLGN_TR1` FOREIGN KEY (`ROLE_ID`) REFERENCES `krim
_role_t` (`ROLE_ID`))
LOAD DATA INFILE 'C:\\kfsDB\\CSVfiles\\krim_dlgn_t.csv' INTO TABLE KRIM_DLGN_T 
FIELDS TERMINATED BY ',' ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 LINES
(DLGN_ID,VER_NBR,OBJ_ID,ROLE_ID,ACTV_IND,KIM_TYP_ID,DLGN_TYP_CD);
*/

/*  need to load krim_dlgn_t first 
LOAD DATA INFILE 'C:\\kfsDB\\CSVfiles\\krim_dlgn_mbr_t.csv' INTO TABLE KRIM_DLGN_MBR_T 
FIELDS TERMINATED BY ',' ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 LINES
(DLGN_MBR_ID,OBJ_ID,DLGN_ID,MBR_ID,MBR_TYP_CD,ROLE_MBR_ID);
*/

/* success */
LOAD DATA INFILE 'C:\\kfsDB\\CSVfiles\\krim_dlgn_mbr_attr_data_t.csv' INTO TABLE KRIM_DLGN_MBR_ATTR_DATA_T 
FIELDS TERMINATED BY ',' ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 LINES
(ATTR_DATA_ID,OBJ_ID,DLGN_MBR_ID,KIM_TYP_ID,KIM_ATTR_DEFN_ID,ATTR_VAL);

/* success */
LOAD DATA INFILE 'C:\\kfsDB\\CSVfiles\\krim_perm_tmpl_t.csv' INTO TABLE KRIM_PERM_TMPL_T 
FIELDS TERMINATED BY ',' ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 LINES
(PERM_TMPL_ID,OBJ_ID,VER_NBR,NMSPC_CD,NM,DESC_TXT,KIM_TYP_ID,ACTV_IND);

/* success. Had to take the first row out and put int in part1 file and run it in seperately
Not all records were loaded in. ERROR 1452 (23000): Cannot add or update a child row: a foreign key constraint fails (`krtt`.`krim_perm_t`, CONSTRAINT `KRIM_PERM_TR1` FOREIGN KEY (`PERM_TMPL_ID`) REFERENCES
`krim_perm_tmpl_t` (`PERM_TMPL_ID`)) */
LOAD DATA INFILE 'C:\\kfsDB\\CSVfiles\\krim_perm_t-PART1.csv' INTO TABLE KRIM_PERM_T 
FIELDS TERMINATED BY ',' ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 LINES
(PERM_ID,OBJ_ID,VER_NBR,PERM_TMPL_ID,NMSPC_CD,NM,DESC_TXT,ACTV_IND);

LOAD DATA local INFILE 'C:\\kfsDB\\CSVfiles\\krim_perm_t.csv' INTO TABLE KRIM_PERM_T 
FIELDS TERMINATED BY ',' ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 LINES
(PERM_ID,OBJ_ID,VER_NBR,PERM_TMPL_ID,NMSPC_CD,NM,DESC_TXT,ACTV_IND);


/* not able to load records in, 
ERROR 1452 (23000): Cannot add or update a child row: a foreign key constraint fails (`krtt`.`krim_perm_attr_data_t`, CONSTRAINT `KRIM_PERM_ATTR_DATA_TR3` FOREIGN KEY (`PERM_I
D`) REFERENCES `krim_perm_t` (`PERM_ID`) ON DELETE CASCADE)
LOAD DATA local INFILE 'C:\\kfsDB\\CSVfiles\\krim_perm_attr_data_t.csv' INTO TABLE KRIM_PERM_ATTR_DATA_T 
FIELDS TERMINATED BY ','
LINES TERMINATED BY '\n'
IGNORE 1 LINES
(ATTR_DATA_ID,OBJ_ID,VER_NBR,PERM_ID,KIM_TYP_ID,KIM_ATTR_DEFN_ID,ATTR_VAL);
*/


/* success. */
LOAD DATA local INFILE 'C:\\kfsDB\\CSVfiles\\krim_rsp_t.csv' INTO TABLE KRIM_RSP_T 
FIELDS TERMINATED BY ','
LINES TERMINATED BY '\n'
IGNORE 1 LINES
(RSP_ID,OBJ_ID,VER_NBR,RSP_TMPL_ID,NMSPC_CD,NM,DESC_TXT,ACTV_IND);

/* success. need krim_rsp_t loaded first */
LOAD DATA local INFILE 'C:\\kfsDB\\CSVfiles\\krim_role_rsp_t.csv' INTO TABLE KRIM_ROLE_RSP_T 
FIELDS TERMINATED BY ','
LINES TERMINATED BY '\n'
IGNORE 1 LINES
(ROLE_RSP_ID,OBJ_ID,ROLE_ID,RSP_ID,ACTV_IND);

/* success. */
LOAD DATA local INFILE 'C:\\kfsDB\\CSVfiles\\krim_role_rsp_actn_t.csv' INTO TABLE KRIM_ROLE_RSP_ACTN_T 
FIELDS TERMINATED BY ','
LINES TERMINATED BY '\n'
IGNORE 1 LINES
(ROLE_RSP_ACTN_ID,OBJ_ID,ACTN_TYP_CD,PRIORITY_NBR,ACTN_PLCY_CD,ROLE_MBR_ID,ROLE_RSP_ID,FRC_ACTN);


/* success. */
LOAD DATA local INFILE 'C:\\kfsDB\\CSVfiles\\krim_rsp_attr_data_t.csv' INTO TABLE KRIM_RSP_ATTR_DATA_T 
FIELDS TERMINATED BY ','
LINES TERMINATED BY '\n'
IGNORE 1 LINES
(ATTR_DATA_ID,OBJ_ID,VER_NBR,RSP_ID,KIM_TYP_ID,KIM_ATTR_DEFN_ID,ATTR_VAL);


/* not able to load file in because krim_perm_t is not loaded correctly
ERROR 1452 (23000): Cannot add or update a child row: a foreign key constraint fails (`krtt`.`krim_role_perm_t`, CONSTRAINT `KRIM_ROLE_PERM_TR1` FOREIGN KEY (`PERM_ID`) REFERE
NCES `krim_perm_t` (`PERM_ID`))

LOAD DATA local INFILE 'C:\\kfsDB\\CSVfiles\\krim_role_perm_t.csv' INTO TABLE KRIM_ROLE_PERM_T 
FIELDS TERMINATED BY ','
LINES TERMINATED BY '\n'
IGNORE 1 LINES
(ROLE_PERM_ID,OBJ_ID,ROLE_ID,PERM_ID,ACTV_IND);
*/

