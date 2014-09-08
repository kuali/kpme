--
-- Copyright 2005-2014 The Kuali Foundation
--
-- Licensed under the Educational Community License, Version 2.0 (the "License");
-- you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
--
-- http://www.opensource.org/licenses/ecl2.php
--
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
--




-- ===================================================================================
-- 2013-09-16--KULRICE-10453.sql (MySQL)
-- https://jira.kuali.org/browse/KULRICE-10453
-- ===================================================================================


INSERT INTO KRCR_PARM_T (OBJ_ID, NMSPC_CD, CMPNT_CD, PARM_NM, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID)
    SELECT UUID(), 'KR-KRAD', CMPNT_CD, PARM_NM, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID
      FROM KRCR_PARM_T
     WHERE NMSPC_CD = 'KR-NS'
       AND CMPNT_CD = 'Lookup'
       AND PARM_NM = 'RESULTS_LIMIT'
       AND APPL_ID NOT IN (SELECT APPL_ID FROM KRCR_PARM_T
                        WHERE NMSPC_CD = 'KR-KRAD'
                          AND CMPNT_CD = 'Lookup'
                          AND PARM_NM = 'RESULTS_LIMIT')
/


-- ===================================================================================
-- 2013-10-02--KULRICE-9252.sql (MySQL)
-- https://jira.kuali.org/browse/KULRICE-9252
-- ===================================================================================


-- KULRICE-9252 Configuration Parameter for default help URL for KRAD lookup views

INSERT INTO KRCR_PARM_T (NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID)
VALUES ('KR-KRAD', 'Lookup', 'DEFAULT_HELP_URL','69A9BABE4A0FBD56E0404F8189D82511', 1, 'HELP',
        'http://site.kuali.org/rice/latest/reference/html/Help.html#lookup', 'Default External Help Url', 'A', 'KUALI')
/


-- ===================================================================================
-- 2013-10-22--KULRICE-11052.sql (MySQL)
-- https://jira.kuali.org/browse/KULRICE-11052
-- ===================================================================================


-- KULRICE-11052 - Adding a static date to prevent impex from updating it every time

UPDATE KRIM_ROLE_T SET LAST_UPDT_DT = STR_TO_DATE( '20121128143720', '%Y%m%d%H%i%s' ) WHERE ROLE_ID = 'KR1001'
/



-- ===================================================================================
-- 2013-10-23--KULRICE-9086.sql (MySQL)
-- https://jira.kuali.org/browse/KULRICE-9086
-- ===================================================================================


-- KULRICE-9086 - Adding a parameter for the maximum number of rows that will be displayed in the lookup results

INSERT INTO KRCR_PARM_T (OBJ_ID, NMSPC_CD, CMPNT_CD, PARM_NM, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID)
    SELECT UUID(), 'KR-KRAD', CMPNT_CD, 'MULTIPLE_VALUE_RESULTS_LIMIT', PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID
      FROM KRCR_PARM_T
     WHERE NMSPC_CD = 'KR-NS'
       AND CMPNT_CD = 'Lookup'
       AND PARM_NM = 'MULTIPLE_VALUE_RESULTS_PER_PAGE'
       AND NOT EXISTS (SELECT '1' FROM KRCR_PARM_T
                        WHERE NMSPC_CD = 'KR-KRAD'
                          AND CMPNT_CD = 'Lookup'
                          AND PARM_NM = 'MULTIPLE_VALUE_RESULTS_LIMIT')
/


-- ===================================================================================
-- 2014-01-09--KULRICE-9198.sql (MySQL)
-- https://jira.kuali.org/browse/KULRICE-9198
-- ===================================================================================


-- KULRICE-9198 - krms_attr_defn_t.attr_defn_id is a varchar(255),
--                but referencing columns are a mixture of varchar(40) and varchar(255)
ALTER TABLE KRMS_TYP_ATTR_T DROP FOREIGN KEY KRMS_TYP_ATTR_FK1
/
ALTER TABLE KRMS_TYP_ATTR_T MODIFY ATTR_DEFN_ID VARCHAR(40)
/
ALTER TABLE KRMS_ACTN_ATTR_T DROP FOREIGN KEY KRMS_ACTN_ATTR_FK2
/
ALTER TABLE KRMS_AGENDA_ATTR_T DROP FOREIGN KEY KRMS_AGENDA_ATTR_FK2
/
ALTER TABLE KRMS_CNTXT_ATTR_T DROP FOREIGN KEY KRMS_CNTXT_ATTR_FK2
/
ALTER TABLE KRMS_RULE_ATTR_T DROP FOREIGN KEY KRMS_RULE_ATTR_FK2
/
ALTER TABLE KRMS_TERM_RSLVR_ATTR_T DROP FOREIGN KEY KRMS_TERM_RSLVR_ATTR_FK2
/
ALTER TABLE KRMS_NL_USAGE_ATTR_T DROP FOREIGN KEY KRMS_NL_USAGE_ATTR_FK2
/
ALTER TABLE KRMS_NL_TMPL_ATTR_T DROP FOREIGN KEY KRMS_NL_TMPL_ATTR_FK2
/
ALTER TABLE KRMS_ATTR_DEFN_T MODIFY ATTR_DEFN_ID VARCHAR(40)
/
ALTER TABLE KRMS_ACTN_ATTR_T ADD CONSTRAINT KRMS_AGENDA_ATTR_FK2
    FOREIGN KEY (ATTR_DEFN_ID)
    REFERENCES KRMS_ATTR_DEFN_T (ATTR_DEFN_ID)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
/
ALTER TABLE KRMS_AGENDA_ATTR_T ADD CONSTRAINT KRMS_ACTN_ATTR_FK2
    FOREIGN KEY (ATTR_DEFN_ID)
    REFERENCES KRMS_ATTR_DEFN_T (ATTR_DEFN_ID)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
/
ALTER TABLE KRMS_CNTXT_ATTR_T ADD CONSTRAINT KRMS_CNTXT_ATTR_FK2
    FOREIGN KEY (ATTR_DEFN_ID)
    REFERENCES KRMS_ATTR_DEFN_T (ATTR_DEFN_ID)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
/
ALTER TABLE KRMS_RULE_ATTR_T ADD CONSTRAINT KRMS_RULE_ATTR_FK2
    FOREIGN KEY (ATTR_DEFN_ID)
    REFERENCES KRMS_ATTR_DEFN_T (ATTR_DEFN_ID)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
/
ALTER TABLE KRMS_TERM_RSLVR_ATTR_T ADD CONSTRAINT KRMS_TERM_RSLVR_ATTR_FK2
    FOREIGN KEY (ATTR_DEFN_ID)
    REFERENCES KRMS_ATTR_DEFN_T (ATTR_DEFN_ID)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
/
ALTER TABLE KRMS_NL_USAGE_ATTR_T ADD CONSTRAINT KRMS_NL_USAGE_ATTR_FK2
    FOREIGN KEY (ATTR_DEFN_ID)
    REFERENCES KRMS_ATTR_DEFN_T (ATTR_DEFN_ID)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
/
ALTER TABLE KRMS_NL_TMPL_ATTR_T ADD CONSTRAINT KRMS_NL_TMPL_ATTR_FK2
    FOREIGN KEY (ATTR_DEFN_ID)
    REFERENCES KRMS_ATTR_DEFN_T (ATTR_DEFN_ID)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
/
ALTER TABLE KRMS_TYP_ATTR_T ADD CONSTRAINT KRMS_TYP_ATTR_FK1
    FOREIGN KEY (ATTR_DEFN_ID)
    REFERENCES KRMS_ATTR_DEFN_T (ATTR_DEFN_ID)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
/


-- ===================================================================================
-- 2014-03-20--KULRICE-8154.sql (MySQL)
-- https://jira.kuali.org/browse/KULRICE-8154
-- ===================================================================================


INSERT INTO KRCR_NMSPC_T (APPL_ID, NMSPC_CD, NM, ACTV_IND, OBJ_ID, VER_NBR)
  VALUES('RICE', 'KR-LOC', 'Kuali Location', 'Y', UUID(), 1)
/

UPDATE KRCR_PARM_T SET NMSPC_CD = 'KR-LOC' WHERE CMPNT_CD = 'All' AND PARM_NM = 'DEFAULT_COUNTRY'
/



-- ===================================================================================
-- 2014-04-09--KULRICE-12277.sql (MySQL)
-- https://jira.kuali.org/browse/KULRICE-12277
-- ===================================================================================


-- Create new KR-NTFCN:Channel KIM type
INSERT INTO krim_typ_t (kim_typ_id, obj_id, ver_nbr, nmspc_cd, nm, srvc_nm, actv_ind) VALUES ('KR1002', uuid(), 1, 'KR-NTFCN', 'Channel', '{http://rice.kuali.org/ken/v2_0}channelPermissionTypeService', 'Y')
/
INSERT INTO krim_attr_defn_t (kim_attr_defn_id, obj_id, ver_nbr, nmspc_cd, nm, lbl, actv_ind, cmpnt_nm) VALUES ('KR1002', uuid(), 1, 'KR-NTFCN', 'Channel ID', 'Channel ID', 'Y', NULL)
/
INSERT INTO krim_typ_attr_t (kim_typ_attr_id, obj_id, ver_nbr, sort_cd, kim_typ_id, kim_attr_defn_id, actv_ind) VALUES ('KR1005', uuid(), 1, 'a', (SELECT kim_typ_id FROM krim_typ_t WHERE nmspc_cd = 'KR-NTFCN' AND nm = 'Channel'), (SELECT kim_attr_defn_id FROM krim_attr_defn_t WHERE nmspc_cd = 'KR-NTFCN' AND nm = 'Channel ID'), 'Y')
/

-- Create KR-NTFCN:View Notification permission template
INSERT INTO krim_perm_tmpl_t (perm_tmpl_id, obj_id, ver_nbr, nmspc_cd, nm, desc_txt, kim_typ_id, actv_ind) VALUES ('KR1005', uuid(), 1, 'KR-NTFCN', 'View Notification', 'View KEN notifications', (SELECT kim_typ_id FROM krim_typ_t WHERE nmspc_cd = 'KR-NTFCN' AND nm = 'Channel'), 'Y')
/



-- ===================================================================================
-- 2014-04-09--KULRICE-12281.sql (MySQL)
-- https://jira.kuali.org/browse/KULRICE-12281
-- ===================================================================================


-- This creates a new permission template which controls which users can export results from a lookup and sets up a single permission which allows all users to export from all lookups
INSERT INTO krim_perm_tmpl_t (perm_tmpl_id, obj_id, ver_nbr, nmspc_cd, nm, desc_txt, kim_typ_id, actv_ind) VALUES ('KR1004', uuid(), '1', 'KR-NS', 'Export Records', 'Ability to export results from the lookup screen.', (SELECT kim_typ_id FROM krim_typ_t WHERE nmspc_cd = 'KR-NS' AND nm = 'Namespace or Component'), 'Y')
/
INSERT INTO krim_perm_t (perm_id, obj_id, ver_nbr, perm_tmpl_id, nmspc_cd, nm, desc_txt, actv_ind) VALUES ('KR1003', uuid(), '1', (SELECT perm_tmpl_id FROM krim_perm_tmpl_t WHERE nmspc_cd = 'KR-NS' AND nm = 'Export Records'), 'KR-NS', 'Export Any Record', 'Ability to export any record', 'Y')
/
INSERT INTO krim_perm_attr_data_t (attr_data_id, obj_id, ver_nbr, perm_id, kim_typ_id, kim_attr_defn_id, attr_val) VALUES ('KR1005', uuid(), '1', (SELECT perm_id FROM krim_perm_t WHERE nmspc_cd = 'KR-NS' AND nm = 'Export Any Record' AND perm_tmpl_id = (SELECT perm_tmpl_id FROM krim_perm_tmpl_t WHERE nmspc_cd = 'KR-NS' AND nm = 'Export Records')), (SELECT kim_typ_id FROM krim_typ_t WHERE nmspc_cd = 'KR-NS' AND nm = 'Namespace or Component'), (SELECT kim_attr_defn_id FROM krim_attr_defn_t WHERE nmspc_cd = 'KR-NS' AND nm = 'namespaceCode'), '*')
/
INSERT INTO krim_role_perm_t (role_perm_id, obj_id, ver_nbr, role_id, perm_id, actv_ind) VALUES ('KR1004', uuid(), '1', (SELECT role_id FROM krim_role_t WHERE nmspc_cd = 'KUALI' AND role_nm = 'User'), (SELECT perm_id FROM krim_perm_t WHERE nmspc_cd = 'KR-NS' AND nm = 'Export Any Record' AND perm_tmpl_id = (SELECT perm_tmpl_id FROM krim_perm_tmpl_t WHERE nmspc_cd = 'KR-NS' AND nm = 'Export Records')), 'Y')
/



-- ===================================================================================
-- 2014-04-09--KULRICE-12323.sql (MySQL)
-- https://jira.kuali.org/browse/KULRICE-12323
-- ===================================================================================


-- This alters the size of the address fields on the address and person maintenance document tables so they are a bit longer and match
ALTER TABLE krim_entity_addr_t CHANGE COLUMN ADDR_LINE_1 ADDR_LINE_1 VARCHAR(128)
/
ALTER TABLE krim_entity_addr_t CHANGE COLUMN ADDR_LINE_2 ADDR_LINE_2 VARCHAR(128)
/
ALTER TABLE krim_entity_addr_t CHANGE COLUMN ADDR_LINE_3 ADDR_LINE_3 VARCHAR(128)
/

ALTER TABLE krim_pnd_addr_mt CHANGE COLUMN ADDR_LINE_1 ADDR_LINE_1 VARCHAR(128)
/
ALTER TABLE krim_pnd_addr_mt CHANGE COLUMN ADDR_LINE_2 ADDR_LINE_2 VARCHAR(128)
/
ALTER TABLE krim_pnd_addr_mt CHANGE COLUMN ADDR_LINE_3 ADDR_LINE_3 VARCHAR(128)
/



-- ===================================================================================
-- 2014-04-09--KULRICE-12405.sql (MySQL)
-- https://jira.kuali.org/browse/KULRICE-12405
-- ===================================================================================


-- This index improves performance of KFS queries which need to fetch entities by employee ID
CREATE INDEX KRIM_ENTITY_EMP_INFO_TI3 ON KRIM_ENTITY_EMP_INFO_T(EMP_ID)
/



-- ===================================================================================
-- 2014-04-11--KULRICE-12155.sql (MySQL)
-- https://jira.kuali.org/browse/KULRICE-12155
-- ===================================================================================


--
-- KULRICE-12155 - To update the xml for widgets.xml, first delete the widgets stylesheet and then recreate it
-- with the updates.  The change is in the checkbox_render template when the checked variable is getting set.
--
-- IMPORTANT NOTE - For client upgrades, if the out-of-the-box widgets stylesheet is not used (i.e - ACTV_IND on
-- stylesheet 2020 is 0) then this SQL will create a second widgets stylesheet with an ACTV_IND of 1.  Instead of
-- running the SQL below, apply the fix to your active widgets stylesheet.  To do so, please see KULRICE-12155 for
-- a detailed description of the change.
--

DELETE FROM KRCR_STYLE_T WHERE STYLE_ID = '2020' AND NM = 'widgets'
/

INSERT INTO KRCR_STYLE_T (ACTV_IND,NM,OBJ_ID,STYLE_ID,VER_NBR,XML)
  VALUES (1, 'widgets', UUID(), '2020', 1, '<xsl:stylesheet xmlns:my-class="xalan://org.kuali.rice.edl.framework.util.EDLFunctions" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
				<xsl:output method="html" version="4.01"/>
				<xsl:variable name="globalReadOnly" select="/documentContent/documentState/editable != ''true''"/>
				<!-- determined by an appconstant -->
				<xsl:variable name="showAttachments" select="/documentContent/documentState/showAttachments"/>
				<xsl:strip-space elements="*"/>

				<xsl:template name="widget_render">
					<xsl:param name="fieldName"/>
					<xsl:param name="renderCmd"/>
					<xsl:param name="align"/>
					<xsl:param name="readOnly"/>
					<xsl:param name="customFunction"/>
					<xsl:param name="default"/>
					<xsl:for-each select="//fieldDef[@name=$fieldName]">
						<xsl:choose>
							<xsl:when test="position() != 1">
								<h4>
									<font color="#FF0000"> fieldDef Name:  <xsl:value-of select="$fieldName"/> is  duplicated ! </font>
								</h4>
							</xsl:when>
							<xsl:otherwise>
								<xsl:variable name="input_type" select="display/type"/>
								<xsl:variable name="render">
									<xsl:choose>
										<xsl:when test="$renderCmd">
											<xsl:value-of select="$renderCmd"/>
										</xsl:when>
										<xsl:otherwise>
											<xsl:value-of select="''all''"/>
										</xsl:otherwise>
									</xsl:choose>
								</xsl:variable>
								<xsl:variable name="vAlign">
									<xsl:choose>
										<xsl:when test="$align">
											<xsl:value-of select="$align"/>
										</xsl:when>
										<xsl:otherwise>
											<xsl:value-of select="''horizontal''"/>
										</xsl:otherwise>
									</xsl:choose>
								</xsl:variable>
								<xsl:comment>* For JavaScript validation</xsl:comment>
								<xsl:variable name="fieldDisplayName">
									<xsl:choose>
										<xsl:when test="@title">
											<xsl:value-of select="@title"/>
										</xsl:when>
										<xsl:otherwise>
											<xsl:value-of select="@name"/>
										</xsl:otherwise>
									</xsl:choose>
								</xsl:variable>
								<xsl:variable name="regex" select="my-class:escapeJavascript(validation/regex)"/>
								<xsl:variable name="customValidator" select="validation/customValidator"/>
								<xsl:variable name="validation_required" select="validation/@required = ''true''"/>
								<xsl:variable name="message">
									<!-- <xsl:if test="//edlContent/data/version[@current=''true'']/field[@name=current()/@name]"> -->
									<xsl:choose>
										<xsl:when test="//edlContent/data/version[@current=''true'']/field[@name=current()/@name]/errorMessage">
											<xsl:value-of select="//edlContent/data/version[@current=''true'']/field[@name=current()/@name]/errorMessage"/>
										</xsl:when>
										<xsl:when test="//documentState/fieldError[@key=current()/@name]">
											<xsl:value-of select="//documentState/fieldError[@key=current()/@name]"/>
										</xsl:when>
										<xsl:when test="validation/message">
											<xsl:value-of select="validation/message"/>
										</xsl:when>
										<xsl:when test="validation/regex">
											<xsl:value-of select="$fieldDisplayName"/> (<xsl:value-of select="@name"/>) <xsl:text> does not match ''</xsl:text> <xsl:value-of select="$regex"/> <xsl:text>''</xsl:text>
										</xsl:when>
										<xsl:otherwise>
									    <xsl:value-of select="//edlContent/data/version[@current=''true'']/field[@name]"/>
											<xsl:comment>* Dropped Through and Hit Otherwise</xsl:comment>
										</xsl:otherwise>
									</xsl:choose>
									<!-- </xsl:if> -->
								</xsl:variable>
								<xsl:variable name="custommessage">
									<xsl:choose>
										<xsl:when test="//edlContent/data/version[@current=''true'']/field[@name=current()/@name]/errorMessage">
											<xsl:value-of select="//edlContent/data/version[@current=''true'']/field[@name=current()/@name]/errorMessage"/>
										</xsl:when>
										<xsl:otherwise>NONE</xsl:otherwise>
									</xsl:choose>
								</xsl:variable>

								<xsl:comment>* custom message: <xsl:value-of select="$custommessage"/>
</xsl:comment>
								<xsl:comment>* validation/message: <xsl:value-of select="validation/message"/>
</xsl:comment>
								<xsl:comment>* message: <xsl:value-of select="$message"/>
</xsl:comment>
								<xsl:variable name="hasFieldError" select="//documentState/fieldError[@key=current()/@name]"/>
								<xsl:variable name="invalid" select="//edlContent/data/version[@current=''true'']/field[@name=current()/@name]/@invalid"/>
								<!--
									determine value to display: use the value specified in the current version	if it exists, otherwise use the ''default''
									value defined in the field or if specified use data from userSession
								-->
								<xsl:variable name="userValue" select="//edlContent/data/version[@current=''true'']/field[@name=current()/@name]/value"/>
								<xsl:variable name="hasUserValue" select="boolean($userValue)"/>
								<xsl:variable name="value">
									<xsl:choose>
										<xsl:when test="$hasUserValue">
											<xsl:value-of select="$userValue"/>
										</xsl:when>
										<xsl:when test="$default">
											<xsl:value-of select="$default"/>
										</xsl:when>
										<xsl:otherwise>
											<xsl:value-of select="value"/>
										</xsl:otherwise>
									</xsl:choose>
								</xsl:variable>
								<!-- message row -->
								<xsl:variable name="type">
									<xsl:choose>
										<xsl:when test="($invalid and $validation_required) or $hasFieldError">error</xsl:when>
										<xsl:when test="$invalid and not($validation_required)">warning</xsl:when>
										<xsl:otherwise>empty</xsl:otherwise>
									</xsl:choose>
								</xsl:variable>
								<xsl:comment>* type: <xsl:value-of select="$type"/>
</xsl:comment>
								<!--
									<tr class="{$type}_messageRow" id="{@name}_messageRow">
										<td class="{$type}_messageHeaderCell" id="{@name}_messageHeaderCell">
											<xsl:value-of select="$type"/>
										</td>
										<td class="{$type}_messageDataCell" id="{@name}_messageDataCell">
											<span id="{@name}_message">
												<xsl:value-of select="$message"/>
											</span>
										</td>
									</tr>
								-->


								<xsl:choose>
									<xsl:when test="$input_type=''text''">
										<xsl:comment>* input_type ''text''</xsl:comment>
										<xsl:call-template name="textbox_render">
											<xsl:with-param name="fieldName" select="$fieldName"/>
											<xsl:with-param name="renderCmd" select="$render"/>
											<xsl:with-param name="align" select="$vAlign"/>
											<xsl:with-param name="hasUserValue" select="$hasUserValue"/>
											<xsl:with-param name="value" select="$value"/>
											<xsl:with-param name="invalid" select="$invalid"/>
											<xsl:with-param name="regex" select="$regex"/>
											<xsl:with-param name="customValidator" select="$customValidator"/>
											<xsl:with-param name="message" select="$message"/>
											<xsl:with-param name="validation_required" select="$validation_required"/>
											<xsl:with-param name="readOnly">
                                                <xsl:choose>
                                                    <xsl:when test="//fieldDef[@name=$fieldName]/lookup/lookupReadOnly = ''true''">true</xsl:when>
                                                    <xsl:otherwise>
<xsl:value-of select="$readOnly"/>
</xsl:otherwise>
                                                </xsl:choose>
                                            </xsl:with-param>
											<xsl:with-param name="customFunction" select="$customFunction"/>
										</xsl:call-template>
									</xsl:when>
									<xsl:when test="$input_type=''password''">
										<xsl:call-template name="textbox_render">
											<xsl:with-param name="fieldName" select="$fieldName"/>
											<xsl:with-param name="renderCmd" select="$render"/>
											<xsl:with-param name="align" select="$vAlign"/>
											<xsl:with-param name="hasUserValue" select="$hasUserValue"/>
											<xsl:with-param name="value" select="$value"/>
											<xsl:with-param name="invalid" select="$invalid"/>
											<xsl:with-param name="regex" select="$regex"/>
											<xsl:with-param name="customValidator" select="$customValidator"/>
											<xsl:with-param name="message" select="$message"/>
											<xsl:with-param name="validation_required" select="$validation_required"/>
											<xsl:with-param name="customFunction" select="$customFunction"/>
										</xsl:call-template>
									</xsl:when>
									<xsl:when test="$input_type=''radio''">
										<xsl:call-template name="radio_render">
											<xsl:with-param name="fieldName" select="$fieldName"/>
											<xsl:with-param name="renderCmd" select="$render"/>
											<xsl:with-param name="align" select="$vAlign"/>
											<xsl:with-param name="hasUserValue" select="$hasUserValue"/>
											<xsl:with-param name="value" select="$value"/>
											<xsl:with-param name="invalid" select="$invalid"/>
											<xsl:with-param name="regex" select="$regex"/>
											<xsl:with-param name="customValidator" select="$customValidator"/>
											<xsl:with-param name="message" select="$message"/>
											<xsl:with-param name="validation_required" select="$validation_required"/>
											<xsl:with-param name="readOnly" select="$readOnly"/>
											<xsl:with-param name="customFunction" select="$customFunction"/>
										</xsl:call-template>
									</xsl:when>
									<xsl:when test="$input_type=''checkbox''">
										<xsl:call-template name="checkbox_render">
											<xsl:with-param name="fieldName" select="$fieldName"/>
											<xsl:with-param name="renderCmd" select="$render"/>
											<xsl:with-param name="align" select="$vAlign"/>
											<xsl:with-param name="hasUserValue" select="$hasUserValue"/>
											<xsl:with-param name="value" select="$value"/>
											<xsl:with-param name="invalid" select="$invalid"/>
											<xsl:with-param name="regex" select="$regex"/>
											<xsl:with-param name="customValidator" select="$customValidator"/>
											<xsl:with-param name="message" select="$message"/>
											<xsl:with-param name="validation_required" select="$validation_required"/>
											<xsl:with-param name="readOnly" select="$readOnly"/>
											<xsl:with-param name="customFunction" select="$customFunction"/>
										</xsl:call-template>
									</xsl:when>
									<xsl:when test="$input_type=''select''">
										<xsl:call-template name="select_input">
											<xsl:with-param name="fieldName" select="$fieldName"/>
											<xsl:with-param name="renderCmd" select="$render"/>
											<xsl:with-param name="align" select="$vAlign"/>
											<xsl:with-param name="hasUserValue" select="$hasUserValue"/>
											<xsl:with-param name="value" select="$value"/>
											<xsl:with-param name="invalid" select="$invalid"/>
											<xsl:with-param name="regex" select="$regex"/>
											<xsl:with-param name="customValidator" select="$customValidator"/>
											<xsl:with-param name="message" select="$message"/>
											<xsl:with-param name="validation_required" select="$validation_required"/>
											<xsl:with-param name="readOnly" select="$readOnly"/>
											<xsl:with-param name="customFunction" select="$customFunction"/>
										</xsl:call-template>
									</xsl:when>
									<xsl:when test="$input_type=''select_refresh''">
										<xsl:call-template name="select_input">
											<xsl:with-param name="fieldName" select="$fieldName"/>
											<xsl:with-param name="renderCmd" select="$render"/>
											<xsl:with-param name="align" select="$vAlign"/>
											<xsl:with-param name="hasUserValue" select="$hasUserValue"/>
											<xsl:with-param name="value" select="$value"/>
											<xsl:with-param name="invalid" select="$invalid"/>
											<xsl:with-param name="regex" select="$regex"/>
											<xsl:with-param name="customValidator" select="$customValidator"/>
											<xsl:with-param name="message" select="$message"/>
											<xsl:with-param name="validation_required" select="$validation_required"/>
											<xsl:with-param name="readOnly" select="$readOnly"/>
											<xsl:with-param name="refreshPage" select="''true''"/>
											<xsl:with-param name="customFunction" select="$customFunction"/>
										</xsl:call-template>
									</xsl:when>
									<xsl:when test="$input_type=''textarea''">
										<xsl:call-template name="textarea_input">
											<xsl:with-param name="fieldName" select="$fieldName"/>
											<xsl:with-param name="renderCmd" select="$renderCmd"/>
											<xsl:with-param name="align" select="$align"/>
											<xsl:with-param name="hasUserValue" select="$hasUserValue"/>
											<xsl:with-param name="value" select="$value"/>
											<xsl:with-param name="invalid" select="$invalid"/>
											<xsl:with-param name="regex" select="$regex"/>
											<xsl:with-param name="customValidator" select="$customValidator"/>
											<xsl:with-param name="message" select="$message"/>
											<xsl:with-param name="validation_required" select="$validation_required"/>
											<xsl:with-param name="readOnly" select="$readOnly"/>
											<xsl:with-param name="customFunction" select="$customFunction"/>
										</xsl:call-template>
									</xsl:when>
									<xsl:when test="$input_type=''button''">
										<xsl:call-template name="button_input">
											<xsl:with-param name="fieldName" select="$fieldName"/>
											<xsl:with-param name="renderCmd" select="$renderCmd"/>
											<xsl:with-param name="align" select="$align"/>
											<xsl:with-param name="hasUserValue" select="$hasUserValue"/>
											<xsl:with-param name="value" select="$value"/>
											<xsl:with-param name="invalid" select="$invalid"/>
											<xsl:with-param name="regex" select="$regex"/>
											<xsl:with-param name="customValidator" select="$customValidator"/>
											<xsl:with-param name="message" select="$message"/>
											<xsl:with-param name="validation_required" select="$validation_required"/>
										</xsl:call-template>
									</xsl:when>
									<xsl:when test="$input_type=''submit button''">
										<xsl:call-template name="submitbutton_input">
											<xsl:with-param name="fieldName" select="$fieldName"/>
											<xsl:with-param name="renderCmd" select="$renderCmd"/>
											<xsl:with-param name="align" select="$align"/>
											<xsl:with-param name="hasUserValue" select="$hasUserValue"/>
											<xsl:with-param name="value" select="$value"/>
											<xsl:with-param name="invalid" select="$invalid"/>
											<xsl:with-param name="regex" select="$regex"/>
											<xsl:with-param name="customValidator" select="$customValidator"/>
											<xsl:with-param name="message" select="$message"/>
											<xsl:with-param name="validation_required" select="$validation_required"/>
										</xsl:call-template>
									</xsl:when>
									<xsl:when test="$input_type=''hidden''">
										<xsl:call-template name="hidden_input">
											<xsl:with-param name="fieldName" select="$fieldName"/>
											<xsl:with-param name="value" select="$value"/>
										</xsl:call-template>
									</xsl:when>
								</xsl:choose>
								<xsl:if test="$renderCmd=''all'' or $renderCmd=''input''">
                                    <xsl:call-template name="lookup">
                                      <xsl:with-param name="fieldName" select="$fieldName"/>
                                      <xsl:with-param name="readOnly" select="$readOnly"/>
                                    </xsl:call-template>
									<span class="{$type}Message" id="{@name}_messageHeaderCell">
										<xsl:text> </xsl:text>
<xsl:value-of select="$type"/>
<xsl:text>: </xsl:text>
									</span>
									<span class="{$type}Message" id="{@name}_message">
										<xsl:value-of select="$message"/>
									</span>
									<xsl:if test="validation/regex or validation/customValidator or validation[@required=''true'']">
										<xsl:if test="not(validation/customValidator)">
											<script type="text/javascript">
												// register field for regex checking
												register("<xsl:value-of select="@name"/>","<xsl:value-of select="$fieldDisplayName"/>","<xsl:value-of select="$regex"/>","<xsl:value-of select="$message"/>","<xsl:value-of select="$validation_required"/>");
											</script>
										</xsl:if>
										<xsl:if test="validation/customValidator">
											<script type="text/javascript">
												// register field for custom field checking
												register_custom("<xsl:value-of select="@name"/>","<xsl:value-of select="$fieldDisplayName"/>","<xsl:value-of select="$message"/>", "<xsl:value-of select="$validation_required"/>", <xsl:value-of select="$customValidator"/>);
											</script>
										</xsl:if>
									</xsl:if>
								</xsl:if>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:for-each>
				</xsl:template>

				<xsl:template name="textbox_render">
					<xsl:param name="fieldName"/>
					<xsl:param name="renderCmd"/>
					<xsl:param name="align"/>
					<xsl:param name="hasUserValue"/>
					<xsl:param name="value"/>
					<xsl:param name="regex"/>
					<xsl:param name="message"/>
					<xsl:param name="validation_required"/>
					<xsl:param name="readOnly"/>
					<xsl:param name="customFunction"/>
					<xsl:if test="$renderCmd=''all'' or  $renderCmd=''title''">
						<xsl:value-of select="current()/@title"/>
					</xsl:if>
					<xsl:if test="$renderCmd=''all''">
						<xsl:if test="$align =''horizontal''">
							<xsl:text>          </xsl:text>
						</xsl:if>
						<xsl:if test="$align=''vertical''">
							<br/>
						</xsl:if>
					</xsl:if>
					<xsl:if test="$renderCmd=''all'' or $renderCmd=''input''">
						<xsl:if test="$globalReadOnly = ''true'' or $readOnly = ''true''">
							<xsl:call-template name="hidden_input">
								<xsl:with-param name="fieldName" select="$fieldName"/>
								<xsl:with-param name="value" select="$value"/>
							</xsl:call-template>
						</xsl:if>
						<input value="{$value}">
							<xsl:if test="$customFunction">
							  <xsl:variable name="customFunction_val" select="$customFunction"/>
							  <xsl:attribute name="onkeyup">
							    <xsl:value-of select="$customFunction"/>
							  </xsl:attribute>
							</xsl:if>
							<xsl:if test="$globalReadOnly = ''true'' or $readOnly = ''true''">
								<xsl:attribute name="disabled">
								  disabled
								</xsl:attribute>
							</xsl:if>
							<xsl:attribute name="type">
<xsl:value-of select="current()/display/type"/>
</xsl:attribute>
							<xsl:attribute name="name">
<xsl:value-of select="$fieldName"/>
</xsl:attribute>
							<xsl:attribute name="onkeydown">return replaceEnter(event)</xsl:attribute>
							<xsl:for-each select="current()/display/meta">
								<xsl:variable name="attrName">
									<xsl:value-of select="name"/>
								</xsl:variable>
								<xsl:variable name="attrValue">
									<xsl:value-of select="value"/>
								</xsl:variable>
								<xsl:attribute name="{$attrName}">
<xsl:value-of select="$attrValue"/>
</xsl:attribute>
							</xsl:for-each>
						</input>
					</xsl:if>
				</xsl:template>

				<xsl:template name="radio_render">
					<xsl:param name="fieldName"/>
					<xsl:param name="renderCmd"/>
					<xsl:param name="align"/>
					<xsl:param name="hasUserValue"/>
					<xsl:param name="value"/>
					<xsl:param name="readOnly"/>
					<xsl:param name="customFunction"/>
					<xsl:if test="$renderCmd=''all'' or  $renderCmd=''title''">
						<xsl:value-of select="current()/@title"/>
					</xsl:if>
					<xsl:if test="$renderCmd=''all''">
						<xsl:if test="$align =''horizontal''">
							<xsl:text>            </xsl:text>
						</xsl:if>
						<xsl:if test="$align=''vertical''">
							<br/>
						</xsl:if>
					</xsl:if>
					<xsl:if test="$globalReadOnly = ''true'' or $readOnly = ''true''">
						<xsl:call-template name="hidden_input">
							<xsl:with-param name="fieldName" select="$fieldName"/>
							<xsl:with-param name="value" select="$value"/>
						</xsl:call-template>
					</xsl:if>
					<xsl:if test="$renderCmd=''all'' or $renderCmd=''input''">
						<xsl:for-each select="current()/display/values">
							<xsl:variable name="title">
								<xsl:choose>
									<xsl:when test="@title">
										<xsl:value-of select="@title"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="@name"/>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:variable>
							<xsl:variable name="optionName">
								<xsl:value-of select="../../@name"/>
							</xsl:variable>
							<input name="{$optionName}" title="{$title}" type="{../type}" value="{.}">
							<xsl:if test="$customFunction">
							  <xsl:variable name="customFunction_val" select="$customFunction"/>
							  <xsl:attribute name="onClick">
							    <xsl:value-of select="$customFunction"/>
							  </xsl:attribute>
							</xsl:if>
								<xsl:if test="$globalReadOnly = ''true'' or $readOnly = ''true''">
									<xsl:attribute name="disabled">disabled</xsl:attribute>
								</xsl:if>
								<xsl:choose>
									<xsl:when test="$hasUserValue">
										<xsl:if test="//edlContent/data/version[@current=''true'']/field[@name=current()/../../@name and value=current()]">
											<xsl:attribute name="checked">checked</xsl:attribute>
										</xsl:if>
									</xsl:when>
									<xsl:otherwise>
										<!-- use the default if no user values are specified -->
										<xsl:if test=". = ../../value">
											<xsl:attribute name="checked">checked</xsl:attribute>
										</xsl:if>
									</xsl:otherwise>
								</xsl:choose>
							</input>
							<xsl:value-of select="$title"/>
							<xsl:if test="$align =''horizontal''">
								<xsl:text>           </xsl:text>
							</xsl:if>
							<xsl:if test="$align=''vertical''">
								<br/>
							</xsl:if>
						</xsl:for-each>
					</xsl:if>
				</xsl:template>

				<xsl:template name="checkbox_render">
					<xsl:param name="fieldName"/>
					<xsl:param name="renderCmd"/>
					<xsl:param name="align"/>
					<xsl:param name="hasUserValue"/>
					<xsl:param name="value"/>
					<xsl:param name="readOnly"/>
					<xsl:param name="customFunction"/>
					<xsl:if test="$renderCmd=''all'' or  $renderCmd=''title''">
						<xsl:value-of select="current()/@title"/>
					</xsl:if>
					<xsl:if test="$renderCmd=''all''">
						<xsl:if test="$align =''horizontal''">
							<xsl:text>          </xsl:text>
						</xsl:if>
						<xsl:if test="$align=''vertical''">
							<br/>
						</xsl:if>
					</xsl:if>
					<!--
						<xsl:if test="$globalReadOnly = ''true''  or $readOnly = ''true''">
							<xsl:call-template name="hidden_input">
								<xsl:with-param name="fieldName" select="$fieldName"/>
								<xsl:with-param name="value" select="$value"/>
							</xsl:call-template>
						</xsl:if>
					-->
					<xsl:if test="$renderCmd=''all'' or $renderCmd=''input''">
						<xsl:for-each select="current()/display/values">
							<xsl:variable name="title">
								<xsl:choose>
									<xsl:when test="@title">
										<xsl:value-of select="@title"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="@name"/>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:variable>
							<xsl:variable name="optionName">
								<xsl:value-of select="../../@name"/>
							</xsl:variable>
							<xsl:variable name="checked">
								<xsl:choose>
									<xsl:when test="$hasUserValue">true</xsl:when>
									<!-- use the default if no user values are specified -->
									<xsl:when test=". = ../../value">true</xsl:when>
									<xsl:otherwise>false</xsl:otherwise>
								</xsl:choose>
							</xsl:variable>
							<xsl:if test="($globalReadOnly = ''true''  or $readOnly = ''true'') and $checked = ''true'' ">
								<xsl:call-template name="hidden_input">
									<xsl:with-param name="fieldName" select="$fieldName"/>
									<xsl:with-param name="value" select="."/>
								</xsl:call-template>
							</xsl:if>
		  				<input name="{$optionName}" type="{../type}" value="{.}">
							<xsl:if test="$customFunction">
							  <xsl:variable name="customFunction_val" select="$customFunction"/>
							  <xsl:attribute name="onClick">
							    <xsl:value-of select="$customFunction"/>
							  </xsl:attribute>
							</xsl:if>
								<xsl:if test="$globalReadOnly = ''true''  or $readOnly = ''true''">
									<xsl:attribute name="disabled">disabled</xsl:attribute>
								</xsl:if>
								<xsl:if test="$checked = ''true''">
									<xsl:attribute name="checked">checked</xsl:attribute>
								</xsl:if>
							</input>
							<xsl:value-of select="$title"/>
							<xsl:if test="$align =''horizontal''">
								<xsl:text>           </xsl:text>
							</xsl:if>
							<xsl:if test="$align=''vertical''">
								<br/>
							</xsl:if>
						</xsl:for-each>
					</xsl:if>
				</xsl:template>

				<xsl:template name="select_input">
					<xsl:param name="fieldName"/>
					<xsl:param name="renderCmd"/>
					<xsl:param name="align"/>
					<xsl:param name="hasUserValue"/>
					<xsl:param name="value"/>
					<xsl:param name="readOnly"/>
					<xsl:param name="refreshPage"/>
					<xsl:param name="customFunction"/>
					<xsl:if test="$renderCmd=''title'' or $renderCmd=''all''">
						<xsl:value-of select="current()/@title"/>
					</xsl:if>
					<xsl:if test="$renderCmd=''all''">
						<xsl:choose>
							<xsl:when test="$align=''horizontal''">
								<xsl:text>       </xsl:text>
							</xsl:when>
							<xsl:otherwise>
								<br/>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:if>
					<xsl:if test="$globalReadOnly = ''true'' or $readOnly = ''true''">
						<xsl:call-template name="hidden_input">
							<xsl:with-param name="fieldName" select="$fieldName"/>
							<xsl:with-param name="value" select="$value"/>
						</xsl:call-template>
					</xsl:if>
					<xsl:if test="$renderCmd=''input'' or $renderCmd=''all''">
						<select name="{$fieldName}">
							<xsl:if test="$customFunction">
							  <xsl:variable name="customFunction_val" select="$customFunction"/>
							  <xsl:attribute name="onChange">
							    <xsl:value-of select="$customFunction"/>
							  </xsl:attribute>
							</xsl:if>
							<xsl:if test="$globalReadOnly = ''true''  or $readOnly = ''true''">
								<xsl:attribute name="disabled">disabled</xsl:attribute>
							</xsl:if>
							<xsl:for-each select="current()/display/values">
								<xsl:variable name="title">
									<xsl:choose>
										<xsl:when test="@title">
											<xsl:value-of select="@title"/>
										</xsl:when>
										<xsl:when test="@name">
											<xsl:value-of select="@name"/>
										</xsl:when>
										<xsl:otherwise>
											<xsl:value-of select="."/>
										</xsl:otherwise>
									</xsl:choose>
								</xsl:variable>
								<option title="{$title}" value="{.}">
									<xsl:choose>
										<xsl:when test="$hasUserValue">
											<xsl:if test="//edlContent/data/version[@current=''true'']/field[@name=current()/../../@name and value=current()]">
												<!-- <xsl:if test="$value = current()"> -->
												<xsl:attribute name="selected">selected</xsl:attribute>
											</xsl:if>
										</xsl:when>
										<xsl:otherwise>
											<!-- use the default if no user values are specified -->
											<xsl:if test=". = ../../value">
												<xsl:attribute name="selected">selected</xsl:attribute>
											</xsl:if>
										</xsl:otherwise>
									</xsl:choose>
									<xsl:if test=". = ../../value">
										<xsl:attribute name="selected">selected</xsl:attribute>
									</xsl:if>
									<xsl:value-of select="$title"/>
								</option>
							</xsl:for-each>
						</select>
						<xsl:if test="$refreshPage = ''true''">
						  <script type="text/javascript">
						  	// register additional onchange event, use prototype to hide the main form and show a message so as to prevent changes while refreshing.
						  	// programmers are to create the following divisions; html div; that wrap the main form and a seperate div wrapping the message that will show.
							register_onchange(''<xsl:value-of select="$fieldName"/>'', function() { $(''mainform-div'').hide(); $(''refresh-message'').show(); document.forms[0].submit(); });
						  </script>
						</xsl:if>
					</xsl:if>
				</xsl:template>

				<xsl:template name="textarea_input">
					<xsl:param name="fieldName"/>
					<xsl:param name="renderCmd"/>
					<xsl:param name="align"/>
					<xsl:param name="value"/>
					<xsl:param name="regex"/>
					<xsl:param name="message"/>
					<xsl:param name="validation_required"/>
					<xsl:param name="readOnly"/>
					<xsl:param name="customFunction"/>
					<xsl:if test="$renderCmd=''title'' or $renderCmd=''all''">
						<xsl:value-of select="current()/@title"/>
					</xsl:if>
					<xsl:if test="$globalReadOnly = ''true'' or $readOnly = ''true''">
						<xsl:call-template name="hidden_input">
							<xsl:with-param name="fieldName" select="$fieldName"/>
							<xsl:with-param name="value" select="$value"/>
						</xsl:call-template>
					</xsl:if>
					<xsl:if test="$renderCmd=''all''">
						<xsl:choose>
							<xsl:when test="$align=''horizontal''">
								<xsl:text>       </xsl:text>
							</xsl:when>
							<xsl:otherwise>
								<br/>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:if>
					<xsl:if test="$renderCmd=''input'' or $renderCmd=''all''">
						<xsl:variable name="metaCols" select="display/meta[name=''cols'']/value"/>
						<xsl:variable name="cols">
							<xsl:choose>
								<xsl:when test="$metaCols">
									<xsl:value-of select="$metaCols"/>
								</xsl:when>
								<xsl:otherwise>1</xsl:otherwise>
							</xsl:choose>
						</xsl:variable>
						<xsl:variable name="metaRows" select="display/meta[name=''rows'']/value"/>
						<xsl:variable name="rows">
							<xsl:choose>
								<xsl:when test="$metaRows">
									<xsl:value-of select="$metaRows"/>
								</xsl:when>
								<xsl:otherwise>1</xsl:otherwise>
							</xsl:choose>
						</xsl:variable>
						<textarea cols="{$cols}" name="{@name}" rows="{$rows}">
							<xsl:if test="$customFunction">
							  <xsl:variable name="customFunction_val" select="$customFunction"/>
							  <xsl:attribute name="onkeyup">
							    <xsl:value-of select="$customFunction"/>
							  </xsl:attribute>
							</xsl:if>
							<xsl:if test="$globalReadOnly = ''true''  or $readOnly = ''true''">
								<xsl:attribute name="disabled">disabled</xsl:attribute>
							</xsl:if>
							<!--
								force a space if value is empty, or browsers (firefox) set the rest of the literal body content as the value
								if the tag is a short-form close tag (!)
							-->
							<xsl:choose>
								<xsl:when test="string-length($value) &gt; 0">
									<xsl:value-of select="$value"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:text/>
								</xsl:otherwise>
							</xsl:choose>
						</textarea>
					</xsl:if>
				</xsl:template>

				<xsl:template name="button_input">
					<xsl:param name="fieldName"/>
					<xsl:param name="renderCmd"/>
					<xsl:param name="align"/>
					<xsl:param name="customFunction"/>
					<xsl:if test="$renderCmd=''title'' or $renderCmd=''all''">
						<xsl:value-of select="current()/@title"/>
					</xsl:if>
					<xsl:if test="$renderCmd=''all''">
						<xsl:choose>
							<xsl:when test="$align=''horizontal''">
								<xsl:text>    </xsl:text>
							</xsl:when>
							<xsl:otherwise>
								<br/>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:if>
					<xsl:if test="$renderCmd=''input'' or $renderCmd=''all''">
						<button name="{$fieldName}">
							<xsl:variable name="value" select="value"/>
							<xsl:if test="$value">
								<xsl:attribute name="value">
<xsl:value-of select="$value"/>
</xsl:attribute>
							</xsl:if>
							<xsl:for-each select="current()/display/meta">
								<xsl:variable name="attr_name">
									<xsl:value-of select="name"/>
								</xsl:variable>
								<xsl:variable name="attr_value">
									<xsl:value-of select="value"/>
								</xsl:variable>
								<xsl:attribute name="{$attr_name}">
<xsl:value-of select="$attr_value"/>
</xsl:attribute>
							</xsl:for-each>
							<xsl:value-of select="$value"/>
						</button>
					</xsl:if>
				</xsl:template>

				<xsl:template name="submitbutton_input">
					<xsl:param name="fieldName"/>
					<xsl:param name="renderCmd"/>
					<xsl:param name="align"/>
					<xsl:param name="customFunction"/>
					<xsl:if test="$renderCmd=''title'' or $renderCmd=''all''">
						<xsl:value-of select="current()/@title"/>
					</xsl:if>
					<xsl:if test="$renderCmd=''all''">
						<xsl:choose>
							<xsl:when test="$align=''horizontal''">
								<xsl:text>    </xsl:text>
							</xsl:when>
							<xsl:otherwise>
								<br/>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:if>
					<xsl:if test="$renderCmd=''input'' or $renderCmd=''all''">
						<input name="{$fieldName}" type="submit">
							<xsl:variable name="value" select="value"/>
							<xsl:if test="$value">
								<xsl:attribute name="value">
<xsl:value-of select="$value"/>
</xsl:attribute>
							</xsl:if>
							<xsl:for-each select="current()/display/meta">
								<xsl:variable name="attr_name">
									<xsl:value-of select="name"/>
								</xsl:variable>
								<xsl:variable name="attr_value">
									<xsl:value-of select="value"/>
								</xsl:variable>
								<xsl:attribute name="{$attr_name}">
<xsl:value-of select="$attr_value"/>
</xsl:attribute>
							</xsl:for-each>
						</input>
					</xsl:if>
				</xsl:template>

				<xsl:template name="hidden_input">
					<xsl:param name="fieldName"/>
					<xsl:param name="value"/>
					<xsl:variable name="finalValue">
					  <xsl:choose>
					    <xsl:when test="$value">
					      <xsl:value-of select="$value"/>
					    </xsl:when>
					    <xsl:otherwise>
						  <xsl:value-of select="//edlContent/data/version[@current=''true'']/field[@name=string($fieldName)]/value"/>
						</xsl:otherwise>
					  </xsl:choose>
					</xsl:variable>
					<input name="{$fieldName}" type="hidden" value="{$finalValue}"/>
					<!-- <xsl:comment>
					XPath: //edlContent/data/version[@current=''true'']/field[@name={$fieldName}]/value
					Escaped: <xsl:value-of select="my-class:escapeForXPath($fieldName)"/>
					What''s my value? <xsl:value-of select="//edlContent/data/version[@current=''true'']/field[@name=$fieldName]/value"/>
					What''s my value2? <xsl:value-of select="//edlContent/data/version[@current=''true'']/field[@name=my-class:escapeForXPath($fieldName)]/value"/>
					</xsl:comment> -->

				</xsl:template>

				<xsl:template name="page_button">
					<xsl:param name="pageName"/>
					<xsl:param name="value"/>
					<xsl:param name="readOnly"/>
					<xsl:param name="clickfunction"/>
					<xsl:param name="use_jsButton"/>
					<xsl:variable name="clickFunctionVal" select="boolean(normalize-space($clickfunction))"/>
					<xsl:choose>
						<xsl:when test="$readOnly=''true''">
						  <input disabled="disabled" name="edl.gotoPage:{$pageName}" type="submit" value="{$value}"/>
						</xsl:when>
						<xsl:when test="$clickFunctionVal">
						  <xsl:choose>
							<xsl:when test="$use_jsButton = ''true''">
								<input name="jsButton" onClick="{$clickfunction}" type="submit" value="{$value}"/>
							</xsl:when>
							<xsl:otherwise>
								<input name="edl.gotoPage:{$pageName}" onClick="{$clickfunction}" type="submit" value="{$value}"/>
							</xsl:otherwise>
						  </xsl:choose>
						</xsl:when>
						<xsl:otherwise>
						  <input name="edl.gotoPage:{$pageName}" type="submit" value="{$value}"/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:template>

				<xsl:template name="header">
					<table border="0" cellpadding="0" cellspacing="0" class="headertable" width="100%">
						<tr>
							<td align="left" valign="top" width="10%">
								<img alt="OneStart Workflow" height="21" hspace="5" src="images/wf-logo.gif" vspace="5" width="150"/>
							</td>
							<td align="right">
								<table border="0" cellpadding="0" cellspacing="0">
									<tr>
										<td align="right" class="thnormal">Document Type Name:</td>
										<td align="left" class="datacell1">
											<xsl:value-of select="/documentContent/documentState/docType"/>
										</td>
									</tr>
									<tr>
										<td align="right" class="thnormal">Document Status:</td>
										<td align="left" class="datacell1" width="50">
											<xsl:value-of select="//documentState/workflowDocumentState/status"/>
										</td>
									</tr>
									<tr>
										<td align="right" class="thnormal">Create Date:</td>
										<td align="left" class="datacell1">
											<xsl:comment>[transient start]</xsl:comment>
											<xsl:value-of select="//documentState/workflowDocumentState/createDate"/>
											<xsl:comment>[transient end]</xsl:comment>
										</td>
									</tr>
									<tr>
										<td align="right" class="thnormal">Document ID:</td>
										<td align="left" class="datacell1">
												<xsl:comment>[transient start]</xsl:comment>
												<xsl:value-of select="/documentContent/documentState/docId"/>
												<xsl:comment>[transient end]</xsl:comment>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</xsl:template>

				<xsl:template name="htmlHead">
					<!-- whether the FIELDS can be edited -->
					<!-- <xsl:variable name="globalReadOnly" select="/documentContent/documentState/editable != ''true''"/>-->
					<!-- whether the form can be acted upon -->
					<xsl:variable name="actionable" select="/documentContent/documentState/actionable = ''true''"/>
					<xsl:variable name="docId" select="/documentContent/documentState/docId"/>
					<xsl:variable name="def" select="/documentContent/documentState/definition"/>
					<xsl:variable name="docType" select="/documentContent/documentState/docType"/>
					<xsl:variable name="style" select="/documentContent/documentState/style"/>
					<xsl:variable name="annotatable" select="/documentContent/documentState/annotatable = ''true''"/>
					<xsl:variable name="docTitle">
						<xsl:choose>
							<xsl:when test="//edlContent/edl/@title">
								<xsl:value-of select="//edlContent/edl/@title"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="//edlContent/edl/@name"/>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:variable>
					<xsl:variable name="pageTitle">
						<xsl:choose>
							<xsl:when test="$globalReadOnly = ''true''">
								Viewing
              </xsl:when>
							<xsl:otherwise>
								Editing
              </xsl:otherwise>
						</xsl:choose>


          </xsl:variable>
					<title>
						<xsl:value-of select="$pageTitle"/> Document
					</title>
					<xsl:comment>* Meta data block for automation/testing</xsl:comment>
					<xsl:comment>* 	 [var editable_value=<xsl:value-of select="//documentState/editable"/>]</xsl:comment>
					<xsl:comment>* 	 [var annotatable_value=<xsl:value-of select="//documentState/annotatable"/>]</xsl:comment>
					<xsl:comment>* 	 [var globalReadOnly=<xsl:value-of select="$globalReadOnly"/>]</xsl:comment>
					<xsl:comment>* 	 [var annotatable=<xsl:value-of select="$annotatable"/>]</xsl:comment>
					<xsl:comment>* 	 [var annotation=<xsl:value-of select="//edlContent/data/version[@current=''true'']/annotation"/>]</xsl:comment>
					<xsl:comment>* 	 [transient start]</xsl:comment>
					<xsl:comment>* 	 [var docid=<xsl:value-of select="$docId"/>]</xsl:comment>
					<xsl:comment>* 	 [transient end]</xsl:comment>
					<xsl:comment>* 	 [var doctype=<xsl:value-of select="$docType"/>]</xsl:comment>
					<xsl:comment>* 	 [var def=<xsl:value-of select="$def"/>]</xsl:comment>
					<xsl:comment>* 	 [var style=<xsl:value-of select="$style"/>]</xsl:comment>
					<link href="css/screen.css" rel="stylesheet" type="text/css"/>
					<link href="css/edoclite1.css" rel="stylesheet" type="text/css"/>
					<script src="scripts/edoclite1.js" type="text/javascript"/>
					<script src="scripts/prototype.js" type="text/javascript"/>
					<xsl:if test="//edlContent/edl/javascript">
						<script type="text/javascript">
							<xsl:value-of select="//edlContent/edl/javascript"/>
						</script>
					</xsl:if>
				</xsl:template>

				<xsl:template name="instructions">
					<!-- <xsl:variable name="globalReadOnly" select="/documentContent/documentState/editable != ''true''"/> -->
					<xsl:variable name="docType" select="/documentContent/documentState/docType"/>
					<xsl:variable name="docTitle">
						<xsl:choose>
							<xsl:when test="//edlContent/edl/@title">
								<xsl:value-of select="//edlContent/edl/@title"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="//edlContent/edl/@name"/>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:variable>
					<xsl:variable name="instructions">
						<xsl:choose>
							<xsl:when test="//edlContent/edl/instructions">
								<xsl:value-of select="//edlContent/edl/instructions"/>
							</xsl:when>
							<xsl:otherwise>

								Reviewing Document
							</xsl:otherwise>
						</xsl:choose>
					</xsl:variable>
					<xsl:variable name="createInstructions">
						<xsl:choose>
							<xsl:when test="//edlContent/edl/createInstructions">
								<xsl:value-of select="//edlContent/edl/createInstructions"/>
							</xsl:when>
							<xsl:otherwise>

								Filling out new Document
							</xsl:otherwise>
						</xsl:choose>
					</xsl:variable>
					<xsl:variable name="pageTitle">
						<xsl:choose>
							<xsl:when test="$globalReadOnly = ''true''">
								Viewing
							</xsl:when>
							<xsl:otherwise>
								Editing
							</xsl:otherwise>
						</xsl:choose>


						Document
					</xsl:variable>
					<table align="center" border="0" cellpadding="10" cellspacing="0" width="80%">
						<tr>
							<td>
								<strong>
									<xsl:value-of select="$pageTitle"/>
								</strong>
							</td>
						</tr>
						<tr>
							<td>
								<!-- if ''save'' action is present then this is a "new" document that has not been routed, and therefore we should display the create instructions -->
								<xsl:choose>
									<xsl:when test="//documentState/actionsPossible/save">
										<xsl:value-of select="$createInstructions"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$instructions"/>
									</xsl:otherwise>
								</xsl:choose>
							</td>
						</tr>
					</table>
				</xsl:template>

				<xsl:template name="errors">
					<!--
						<style type="text/css">
							.error-message {
    						color: red;
    						text-align: center;
							}
						</style>
					-->
					<table align="center" border="0" cellpadding="10" cellspacing="0" width="80%">
						<xsl:for-each select="//documentState/error">
							<tr>
								<td>
									<div class="error-message">
										<xsl:value-of select="."/>
									</div>
								</td>
							</tr>
						</xsl:for-each>
					</table>
				</xsl:template>

				<xsl:template name="footer">
					<xsl:if test="//documentState/userSession/backdoorUser">
						<center>
							User
							<xsl:choose>
								<xsl:when test="//documentState/userSession/loggedInUser/displayName">
									<xsl:value-of select="//documentState/userSession/loggedInUser/displayName"/>
								</xsl:when>
								<xsl:when test="//documentState/userSession/loggedInUser/networkId">
									<xsl:value-of select="//documentState/userSession/loggedInUser/networkId"/>
								</xsl:when>
								<xsl:otherwise>
									??Unknown user??
								</xsl:otherwise>
							</xsl:choose>
							standing in for user
							<xsl:choose>
								<xsl:when test="//documentState/userSession/backdoorUser/backdoorDisplayName">
									<xsl:value-of select="//documentState/userSession/backdoorUser/backdoorDisplayName"/>
								</xsl:when>
								<xsl:when test="//documentState/userSession/backdoorUser/backdoorNetworkId">
									<xsl:value-of select="//documentState/userSession/backdoorUser/backdoorNetworkId"/>
								</xsl:when>
								<xsl:otherwise>
									??Unknown user??
								</xsl:otherwise>
							</xsl:choose>
						</center>
					</xsl:if>
				</xsl:template>

				<xsl:template name="hidden-params">
					<xsl:comment>* Hide this nastiness so we can concentrate on formating above</xsl:comment>
					<xsl:variable name="docId" select="/documentContent/documentState/docId"/>
					<xsl:variable name="def" select="/documentContent/documentState/definition"/>
					<xsl:variable name="docType" select="/documentContent/documentState/docType"/>
					<xsl:variable name="style" select="/documentContent/documentState/style"/>
					<xsl:variable name="incrementVersion" select="//edlContent/data/version[@current=''true'']/incrementVersion"/>
					<xsl:variable name="currentPage" select="//currentPage"/>
					<xsl:variable name="previousPage" select="//previousPage"/>
					<div style="display: none">
						<xsl:if test="$incrementVersion = ''true''">
							<input name="incrementVersion" type="hidden" value="{$incrementVersion}"/>
						</xsl:if>
						<xsl:choose>
							<xsl:when test="$docId">
								<!-- preserve the data for comparison without transient value -->
								<xsl:comment>* input name="docId" type="hidden"</xsl:comment>
								<!-- mark the entire input element transient because we can''t insert comments in the middle of a tag just to omit a certain attribute -->
								<xsl:comment>[transient start]</xsl:comment>
								<input name="docId" type="hidden" value="{$docId}"/>
								<xsl:comment>[transient end]</xsl:comment>
							</xsl:when>
							<xsl:otherwise>
								<xsl:if test="$docType">
									<input name="docType" type="hidden" value="{$docType}"/>
								</xsl:if>
								<xsl:if test="$def">
									<input name="def" type="hidden" value="{$def}"/>
								</xsl:if>
								<xsl:if test="$style">
									<input name="style" type="hidden" value="{$style}"/>
								</xsl:if>
							</xsl:otherwise>
						</xsl:choose>
						<xsl:if test="$currentPage">
						  <input name="edl.currentPage" type="hidden" value="{$currentPage}"/>
						</xsl:if>
						<xsl:if test="$previousPage">
						  <input name="edl.previousPage" type="hidden" value="{$previousPage}"/>
						</xsl:if>
					</div>
				</xsl:template>

				<xsl:template name="annotation">
					<xsl:variable name="annotation" select="//edlContent/data/version[@current=''true'']/annotation"/>
					<xsl:variable name="currentAnnotation" select="//edlContent/data/version[@current=''true'']/currentAnnotation"/>
					<xsl:variable name="annotatable" select="/documentContent/documentState/annotatable = ''true''"/>
					<xsl:if test="$annotatable or $annotation or $currentAnnotation">
						<table align="center" border="0" cellpadding="0" cellspacing="0" class="bord-r-t" width="80%">
							<tr>
								<td align="center" class="thnormal" colspan="2">
									<xsl:if test="$annotation">
										<div>
<h4>Annotations</h4>
											<xsl:for-each select="//edlContent/data/version[@current=''true'']/annotation">
												<div>
<xsl:value-of select="."/>
</div>
											</xsl:for-each>
										</div>
										<br/>
									</xsl:if>
									<xsl:if test="$annotatable">
										Set annotation:<br/>
										<textarea name="annotation">
											<xsl:value-of select="$currentAnnotation"/>
										</textarea>
									</xsl:if>
								</td>
							</tr>
						</table>
					</xsl:if>
				</xsl:template>

				<xsl:template name="buttons">
					<xsl:param name="fname"/>
					<xsl:param name="showRTP"/>
					<xsl:variable name="functionName" select="$fname"/>
					<xsl:variable name="fxname" select="boolean(normalize-space($fname))"/>
					<xsl:variable name="actionable" select="/documentContent/documentState/actionable = ''true''"/>
					<xsl:variable name="apos" select="&quot;''&quot;"/>
					<xsl:variable name="showRTPbutton" select="$showRTP"/>
					<xsl:if test="//documentState/actionsPossible/*">
						<table align="center" border="0" cellpadding="0" cellspacing="0" class="bord-r-t" width="80%">
							<tr>
								<td align="center" class="thnormal" colspan="2">
									<xsl:text>									</xsl:text>
										<xsl:for-each select="//documentState/actionsPossible/*[. != ''returnToPrevious'']">
											<xsl:variable name="actionTitle">
												<xsl:choose>
													<xsl:when test="@title">
														<xsl:value-of select="@title"/>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="local-name()"/>
													</xsl:otherwise>
												</xsl:choose>
											</xsl:variable>
										<xsl:if test="local-name() != ''returnToPrevious'' or local-name() = ''returnToPrevious'' and not($showRTPbutton = ''false'')">
											<input name="userAction" title="{$actionTitle}" type="submit">
												<xsl:if test="not($actionable)">
													<xsl:attribute name="disabled">disabled</xsl:attribute>
												</xsl:if>
											 <xsl:choose>
												<xsl:when test="$fxname">
												  <xsl:attribute name="onclick">
													  <xsl:value-of select="''buttonClick(''"/>
													  <xsl:value-of select="$apos"/>
													  <xsl:value-of select="$actionTitle"/>
													  <xsl:value-of select="$apos"/>
													  <xsl:value-of select="''); ''"/>
													  <xsl:value-of select="''buttonClickFunctionName(''"/>
													  <xsl:value-of select="$functionName"/>
													  <xsl:value-of select="'')''"/>
													</xsl:attribute>
												</xsl:when>
												<xsl:otherwise>
													<xsl:attribute name="onclick">
													  <xsl:value-of select="''buttonClick(''"/>
													  <xsl:value-of select="$apos"/>
													  <xsl:value-of select="$actionTitle"/>
													  <xsl:value-of select="$apos"/>
													  <xsl:value-of select="'')''"/>
													</xsl:attribute>
												</xsl:otherwise>
											  </xsl:choose>
											  <xsl:choose>
											    <xsl:when test="local-name() = ''route''">
											      <xsl:attribute name="value">
											        <xsl:value-of select="''submit''"/>
											      </xsl:attribute>
											    </xsl:when>
											    <xsl:when test="local-name() = ''blanketApprove''">
											      <xsl:attribute name="value">
											        <xsl:value-of select="''blanket approve''"/>
											      </xsl:attribute>
											    </xsl:when>
											    <xsl:when test="local-name() = ''returnToPrevious''">
											      <xsl:attribute name="value">
											        <xsl:value-of select="''return to previous''"/>
											      </xsl:attribute>
											    </xsl:when>
											    <xsl:otherwise>
											      <xsl:attribute name="value">
											        <xsl:value-of select="local-name()"/>
											      </xsl:attribute>
											    </xsl:otherwise>
											  </xsl:choose>
											</input>
										</xsl:if>
											<xsl:text>                 					 </xsl:text>
										</xsl:for-each>

								<xsl:if test="not($showRTPbutton = ''false'')">
									<xsl:if test="//documentState/actionsPossible/returnToPrevious">
										<select name="previousNode">
											<xsl:if test="not($actionable) or $showRTPbutton = ''false''">
												<xsl:attribute name="disabled">disabled</xsl:attribute>
											</xsl:if>
											<xsl:for-each select="//documentState/previousNodes/node">
												<option value="{.}">
													<xsl:value-of select="."/>
												</option>
											</xsl:for-each>
										</select>
										<xsl:text>                 					 </xsl:text>
									</xsl:if>
								</xsl:if>
								</td>
							</tr>
						</table>
					</xsl:if>
				</xsl:template>

				<xsl:template match="/">
					<xsl:choose>
						<xsl:when test="$overrideMain=''true''">
							<xsl:call-template name="mainForm"/>
						</xsl:when>
						<xsl:otherwise>
							<html>
								<head>
									<xsl:call-template name="htmlHead"/>
								</head>
								<body onload="onPageLoad()">
									<xsl:call-template name="header"/>
									<xsl:call-template name="instructions"/>
									<xsl:call-template name="errors"/>
									<xsl:variable name="formTarget" select="''EDocLite''"/>
									<form accept-charset="ISO-8859-1" action="{$formTarget}" enctype="multipart/form-data" id="edoclite" method="post" onsubmit="return validateOnSubmit(this)">
										<xsl:call-template name="hidden-params"/>
										<xsl:call-template name="mainBody"/>
										<xsl:call-template name="annotation"/>
										<xsl:call-template name="buttons"/>
										<br/>
										<xsl:call-template name="notes"/>
									</form>
									<xsl:call-template name="footer"/>
								</body>
							</html>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:template>

				<xsl:template name="notes">
					<xsl:if test="//NoteForm">
						<xsl:variable name="showEdit" select="//NoteForm/showEdit"/>
						<xsl:variable name="showAdd" select="//NoteForm/showAdd"/>
						<input name="showEdit" type="hidden">
							<xsl:attribute name="value">
<xsl:value-of select="//NoteForm/showEdit"/>
</xsl:attribute>
						</input>
						<input name="showAdd" type="hidden">
							<xsl:attribute name="value">
<xsl:value-of select="//NoteForm/showAdd"/>
</xsl:attribute>
						</input>
						<input name="methodToCall" type="hidden"/>
						<input name="sortNotes" type="hidden">
							<xsl:attribute name="value">
<xsl:value-of select="//NoteForm/sortNotes"/>
</xsl:attribute>
						</input>
						<input name="noteIdNumber" type="hidden">
							<xsl:attribute name="value">
<xsl:value-of select="//NoteForm/noteIdNumber"/>
</xsl:attribute>
						</input>
						<table align="center" border="0" cellpadding="0" cellspacing="0" class="bord-r-t" width="80%">
							<xsl:if test="$showAdd = ''true''">
								<tr>
									<td align="center" class="thnormal2" colspan="4" scope="col">
										<b>Create Note </b>
									</td>
								</tr>
								<tr>
									<td class="thnormal" scope="col">
										<div align="center">Author</div>
									</td>
									<td class="thnormal" scope="col">
										<div align="center">Date</div>
									</td>
									<td class="thnormal" scope="col">
										<div align="center">Note</div>
									</td>
									<td class="thnormal" scope="col">
										<div align="center">Action</div>
									</td>
								</tr>
								<tr valign="top">
									<td class="datacell">
										<xsl:if test="$globalReadOnly != ''true''">
											<xsl:value-of select="//NoteForm/currentUserName"/>
										</xsl:if>
									</td>
									<td class="datacell">
										<xsl:if test="$globalReadOnly != ''true''">
											<xsl:comment>[transient start]</xsl:comment>
											<xsl:value-of select="//NoteForm/currentDate"/>
            				  <xsl:comment>[transient end]</xsl:comment>
										</xsl:if>
									</td>
									<td class="datacell">
										<xsl:choose>
											<xsl:when test="$showEdit = ''yes''">
												<textarea cols="60" disabled="true" name="addText" rows="3"/>
												<xsl:if test="$showAttachments = ''true''">
													<br/>Attachment:	<input disabled="true" name="file" type="file"/>
												</xsl:if>
											</xsl:when>
											<xsl:otherwise>
												<textarea cols="60" name="addText" rows="3">
													<xsl:if test="$globalReadOnly = ''true''">
														<xsl:attribute name="disabled">disabled</xsl:attribute>
													</xsl:if>
												</textarea>
												<xsl:if test="$showAttachments = ''true''">
													<br/>
													Attachment:
													<input name="file" type="file">
														<xsl:if test="$globalReadOnly = ''true''">
															<xsl:attribute name="disabled">disabled</xsl:attribute>
														</xsl:if>
													</input>
												</xsl:if>
											</xsl:otherwise>
										</xsl:choose>
									</td>
									<td class="datacell">
										<xsl:choose>
											<xsl:when test="$showEdit = ''yes''">
												<div align="center">
													<img height="15" hspace="3" src="images/tinybutton-save-disable.gif" vspace="3" width="45"/>
												</div>
											</xsl:when>
											<xsl:otherwise>
												<div align="center">
													<xsl:choose>
														<xsl:when test="$globalReadOnly = ''true''">
															<img height="15" hspace="3" src="images/tinybutton-save-disable.gif" vspace="3" width="45"/>
														</xsl:when>
														<xsl:otherwise>
															<img border="0" height="15" hspace="3" onclick="document.forms[0].methodToCall.value=''save''; document.forms[0].submit();" src="images/tinybutton-save.gif" vspace="3" width="45"/>
														</xsl:otherwise>
													</xsl:choose>
												</div>
											</xsl:otherwise>
										</xsl:choose>
									</td>
								</tr>
							</xsl:if>
							<xsl:choose>
								<xsl:when test="//NoteForm/numberOfNotes &gt;0">
									<tr>
										<td align="center" class="thnormal2" colspan="4" scope="col">
											<b>View Notes </b>
										</td>
									</tr>
									<tr>
										<td class="thnormal" scope="col">
											<div align="center">Author</div>
										</td>
										<td class="thnormal" scope="col">
											<div align="center">
												 <xsl:choose>
													<xsl:when test="$globalReadOnly = ''true''">
														Date
													</xsl:when>
													<xsl:otherwise>
														<a href="javascript: document.forms[0].elements[''methodToCall''].value = ''sort''; document.forms[0].elements[''sortNotes''].value = ''true''; document.forms[0].submit();">Date</a>
														<img height="5" src="images/arrow-expcol-down.gif" width="9"/>
													</xsl:otherwise>
												</xsl:choose>
											</div>
										</td>
										<td class="thnormal" scope="col">
											<div align="center">Note</div>
										</td>
										<td class="thnormal" scope="col">
											<div align="center">Action</div>
										</td>
									</tr>
									<xsl:for-each select="//NoteForm/Notes/Note">
										<tr valign="top">
											<td class="datacell">
												<xsl:value-of select="noteAuthorFullName"/>
											</td>
											<td class="datacell">
            			      <xsl:comment>[transient start]</xsl:comment>
												<xsl:value-of select="formattedCreateDate"/>
              			    <xsl:comment>[transient end]</xsl:comment>
												<br/>
                			  <xsl:comment>[transient start]</xsl:comment>
												<xsl:value-of select="formattedCreateTime"/>
												<xsl:comment>[transient end]</xsl:comment>
											</td>
											<td class="datacell">
												 <xsl:choose>
													<xsl:when test="editingNote = ''true'' and authorizedToEdit = ''true''">
														<textarea cols="60" name="noteText" rows="3">
															<xsl:if test="$globalReadOnly = ''true''">
																<xsl:attribute name="disabled">disabled</xsl:attribute>
															</xsl:if>
															<xsl:value-of select="noteText"/>
														</textarea>
														<br/>
														<xsl:if test="$showAttachments = ''true''">
															<xsl:choose>
																<xsl:when test="attachments/attachment">
																	<xsl:for-each select="attachments/attachment">
																		<xsl:value-of select="fileName"/>   
																		<input name="idInEdit" type="hidden">
																			<xsl:attribute name="value">
<xsl:value-of select="../../noteId"/>
</xsl:attribute>
																		</input>
																		<xsl:choose>
																			<xsl:when test="$globalReadOnly = ''true''">
																			</xsl:when>
																			<xsl:otherwise>
																				<a href="javascript: document.forms[0].elements[''methodToCall''].value = ''deleteAttachment''; document.forms[0].submit();">delete</a>  
	                    									<xsl:variable name="hrefStr">attachment?attachmentId=<xsl:value-of select="attachmentId"/>
</xsl:variable>
																				<a href="{$hrefStr}">download</a>
	                     								</xsl:otherwise>
	                     							</xsl:choose>
																	</xsl:for-each>
																</xsl:when>
																<xsl:otherwise>
																	<br/>
																	Attachment:
																	<input name="file" type="file">
																		<xsl:if test="$globalReadOnly = ''true''">
																			<xsl:attribute name="disabled">disabled</xsl:attribute>
																		</xsl:if>
																	</input>
																</xsl:otherwise>
															</xsl:choose>
														</xsl:if>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="noteText"/>
														<br/>
														<br/>
														<xsl:if test="$showAttachments = ''true''">
															<xsl:for-each select="attachments/attachment">
																<xsl:value-of select="fileName"/>   
	                    					<xsl:variable name="hrefStr">attachment?attachmentId=<xsl:value-of select="attachmentId"/>
</xsl:variable>
																<a href="{$hrefStr}">download</a>
															</xsl:for-each>
														</xsl:if>
													</xsl:otherwise>
												</xsl:choose>
											</td>
											<td class="datacell">
												<xsl:choose>
													<xsl:when test="editingNote = ''true'' and authorizedToEdit = ''true'' and $globalReadOnly != ''true''">
														<div align="center">
															<img border="0" height="15" hspace="3" src="images/tinybutton-save.gif" vspace="3" width="40">
																<xsl:attribute name="onclick">document.forms[0].elements[''methodToCall''].value = ''save'';
																	document.forms[0].elements[''noteIdNumber''].value = <xsl:value-of select="noteId"/>;
																	document.forms[0].submit();
																</xsl:attribute>
															</img>
															<img border="0" height="15" hspace="3" onclick="document.forms[0].elements[''methodToCall''].value = ''cancel''; document.forms[0].submit();" src="images/tinybutton-cancel.gif" vspace="3" width="40"/>
														</div>
													</xsl:when>
													<xsl:otherwise>
														<xsl:choose>
															<xsl:when test="../../showEdit != ''yes'' and authorizedToEdit = ''true'' and $globalReadOnly !=''true''">
																<div align="center">
																	<img border="0" height="15" hspace="3" src="images/tinybutton-edit1.gif" vspace="3" width="40">
																		<xsl:attribute name="onclick">document.forms[0].elements[''methodToCall''].value = ''edit'';
																			document.forms[0].elements[''noteIdNumber''].value = <xsl:value-of select="noteId"/>;
																			document.forms[0].submit();
																		</xsl:attribute>
																	</img>
																	<img border="0" height="15" hspace="3" src="images/tinybutton-delete1.gif" vspace="3" width="40">
																		<xsl:attribute name="onclick">document.forms[0].elements[''methodToCall''].value = ''delete'';
																			document.forms[0].elements[''noteIdNumber''].value = <xsl:value-of select="noteId"/>;
																			document.forms[0].submit();
																		</xsl:attribute>
																	</img>
																</div>
															</xsl:when>
															<xsl:otherwise>
																<div align="center">
																	<img height="15" hspace="3" src="images/tinybutton-edit1-disable.gif" vspace="3" width="40"/>
																	<img height="15" src="images/tinybutton-delete1-disabled.gif" vspace="3" width="40"/>
																</div>
															</xsl:otherwise>
														</xsl:choose>
													</xsl:otherwise>
												</xsl:choose>
											</td>
										</tr>
									</xsl:for-each>
								</xsl:when>
								<xsl:otherwise>
									<xsl:if test="//NoteForm/showAdd != ''true''">
										<tr>
											<td class="thnormal2">
												<table border="0" cellpadding="0" cellspacing="0" height="100%" width="100%">
													<tbody>
														<tr>
															<td align="center" class="spacercell" valign="middle">
																<div align="center">
																	<br/>
																	<br/>
																	<br/>
																	<p>No notes available </p>
																	<xsl:if test="//NoteForm/authorizedToAdd = ''true''">
																		<p>
																			<img border="0" height="15" hspace="3" onclick="document.forms[0].elements[''methodToCall''].value = ''add''; document.forms[0].submit();" src="images/tinybutton-addnote.gif" vspace="3" width="66"/>
																		</p>
																	</xsl:if>
																</div>
															</td>
														</tr>
													</tbody>
												</table>
											</td>
										</tr>
									</xsl:if>
								</xsl:otherwise>
							</xsl:choose>
						</table>
					</xsl:if>
				</xsl:template>
                <xsl:template name="lookup">
                  <xsl:param name="fieldName"/>
                  <xsl:param name="readOnly"/>
                  <xsl:if test="$globalReadOnly != ''true'' and $readOnly != ''true''">
                    <xsl:if test="//fieldDef[@name=$fieldName]/lookup">
                      <input name="userAction.performLookup.{$fieldName}" onclick="buttonClick(''performLookup'');" src="images/searchicon.gif" type="image" value="performLookup.{$fieldName}"/>
                    </xsl:if>
                  </xsl:if>
                </xsl:template>
			</xsl:stylesheet>
')
/