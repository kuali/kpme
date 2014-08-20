<%--
 Copyright 2007-2009 The Kuali Foundation
 
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
<c:if test="${!empty UserSession.loggedInUserPrincipalName}">
    <c:set var="systemAdmin" value='<%=org.kuali.hr.time.roles.TkUserRoles.getUserRoles(org.kuali.rice.krad.util.GlobalVariables.getUserSession().getPrincipalId()).isSystemAdmin()%>' />
    <c:set var="globalViewOnly" value='<%=org.kuali.hr.time.roles.TkUserRoles.getUserRoles(org.kuali.rice.krad.util.GlobalVariables.getUserSession().getPrincipalId()).isGlobalViewOnly()%>' />
    <c:set var="locationAdmin" value='<%=org.kuali.hr.time.roles.TkUserRoles.getUserRoles(org.kuali.rice.krad.util.GlobalVariables.getUserSession().getPrincipalId()).isLocationAdmin()%>' />
</c:if>
<c:if test="${not systemAdmin && not globalViewOnly && not locationAdmin}">
    <jsp:forward page="portal.do?selectedTab=main"/>
</c:if>
<td class="content" valign="top">
    <admin:hrPayroll />
    <admin:timeKeeping />
    <admin:leaveMaintenance />
    <admin:administrative />
</td>
<td class="content" valign="top">
    <admin:inquiries />
    <admin:changeTargetPerson />
    <admin:deleteTimesheet />
    <admin:calculateLeaveAccruals />
</td>
<td class="content" valign="top">
    <admin:batchJob />
    <admin:carryOverBatchJob />
    <admin:calendarEntry />
</td>