<%--

    Copyright 2004-2013 The Kuali Foundation

    Licensed under the Educational Community License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.opensource.org/licenses/ecl2.php

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

--%>
<%@ include file="/WEB-INF/jsp/TkTldHeader.jsp" %>
<c:set var="Form" value="${LeaveApprovalActionForm}" scope="request"/>
<jsp:useBean id="tagSupport" class="org.kuali.kpme.tklm.common.TagSupport"/>


<tk:tkHeader tabId="leaveApprovals">
<html:form action="/LeaveApproval.do" method="POST">
<html:hidden property="methodToCall" value=""/>
<html:hidden styleId="rit" property="rowsInTotal" value="${fn:length(Form.leaveApprovalRows)}"/>
<html:hidden styleId="pcid" property="hrPyCalendarId" value="${Form.hrPyCalendarId}"/>
<html:hidden styleId="pceid" property="hrPyCalendarEntryId" value="${Form.hrPyCalendarEntryId}"/>
<html:hidden styleId="payBeginDateForSearch" property="payBeginDateForSearch" value="${Form.payBeginDateForSearch}"/>
<html:hidden styleId="payEndDateForSearch" property="payEndDateForSearch" value="${Form.payEndDateForSearch}"/>
<html:hidden property="prevPayCalendarId" value="${Form.prevPayCalendarId}"/>
<html:hidden property="nextPayCalendarId" value="${Form.nextPayCalendarId}"/>
<html:hidden styleId="roleName" property="roleName" value="${Form.roleName}"/>
<html:hidden styleId="outputString" property="outputString" value="${Form.outputString}"/>

<script src="js/underscore-1.3.1.min.js"></script>
<script src="js/underscore.string-2.0.0.js"></script>
<script src="js/backbone-0.9.1.min.js"></script>
<script src="js/common.calendar.backbone.js"></script>
<script src="js/lm.approval.backbone.js"></script>
<script src="js/common.fullcalendar.js"></script>


<div class="approvals">
    <table class="navigation">
       	<tbody>
       	<tr>
       		<td>
				<%-- pay calendar group, department and work area filters --%>
				<tk:approvalFilter />
			</td>
			
			<td align="right">
				<%-- Leave Request Approval page --%>
				<a href="LeaveRequestApproval.do" target="_self" id="lraLink">Leave Request Approval</a>
			</td>
		</tr>
		</tbody>
     </table>

    <tk:approvalSearch calType="leaveCalendar" searchId="leaveSearchValue" />

    <lm:leaveApproval />

    <c:if test="${fn:length(Form.leaveApprovalRows) != 0}">
		<tk:approvalButtons refreshId="leaveRefresh" approvable="${Form.anyApprovalRowApprovable}" />
	</c:if>

</div>
</html:form>
<link type="text/css" href='css/fullcalendar.css' rel='stylesheet' />


</tk:tkHeader>

<%-- Leave Calendar detail template --%>
<script type="text/template" id="leaveDetail-template">   	
    <tr class="leaveDetailRow_<@= docId @>">
		 
        <td colspan="3" class="<@= section.cssClass @>">
			<b><@= section.accrualCategory @></b>
      	</td>
		<td><@= section.periodUsage @></td>
		<td><@= section.availableBalance @></td>
    </tr>
</script>

