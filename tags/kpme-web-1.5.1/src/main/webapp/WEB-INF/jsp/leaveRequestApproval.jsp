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

<c:set var="Form" value="${LeaveRequestApprovalActionForm}" scope="request"/>
<c:set var="KualiForm" value="${LeaveRequestApprovalActionForm}" scope="request"/>

<tk:tkHeader tabId="leaveRequestApproval">
    <script src="js/underscore-1.3.1.min.js"></script>
    <script src="js/backbone-0.9.1.min.js"></script>
    <script src="js/tk.ui.js"></script>
    <script src="js/tk.js"></script>
    <script src="js/lm.requestApproval.backbone.js"></script>

    <html:form action="/LeaveRequestApproval.do" styleId="leaveRequestApproval">

        <html:hidden property="methodToCall" value="" styleId="methodToCall"/>
        <div class="approvals">
            <table class="navigation">
                <tbody>
                <tr>
                    <td>
                            <%-- pay calendar group, department and work area filters --%>
                        <tk:approvalFilter/>
                    </td>
                    <td align="right">
						<span>
			    			<a href="LeaveApproval.do?">Team Calendar</a>
				    	</span>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>

        <div id="leave-req-app">
        <c:choose>
            <c:when test="${not empty Form.employeeRows}">
                <c:forEach var="employeeRow" items="${Form.employeeRows}">
                    <%-- only display for employees that have leave requests --%>
                    <c:if test="${not empty employeeRow.leaveRequestList}">
                        <html:hidden property="employeeName" value="${employeeRow.employeeName}"
                                     styleId="employeeName"/>
                        <%-- name of the employee links to the employee's leave calendar tab --%>
				<span>
	      			Employee: <a
                        href="changeTargetPerson.do?${employeeRow.userTargetURLParams}&targetUrl=LeaveCalendar.do&returnUrl=LeaveRequestApproval.do">
                        ${employeeRow.employeeName}</a>
	   			</span>
                        <%-- display error messages generated by "take action" --%>
                        <p id="validation_${employeeRow.principalId}" name="validation_${employeeRow.principalId}"
                           class="validation_${employeeRow.principalId}"></p>

                        <%-- the ids of the the fields are used in lm.requestApproval.js, if you need to make changes to the name/id of any fileds
                        make sure you make same changes in the javascript file
                        --%>
                        <c:set var="tableId" value="approvalTable_${employeeRow.principalId}"/>
                        <table class="cal-table" name="${tableId}" id=${tableId}">
				  <thead>
					<tr>
					   <th>Requested Date</th>
					   <th>Requested Hours</th>
					   <th>Description</th>
					   <th>Earn Code</th>
					   <th>Date & Time Submitted</th>
					   <th>Approve <br/>
					   	   Select All <input type="checkbox" name="checkAllApprove_${employeeRow.principalId}" id="checkAllApprove_${employeeRow.principalId}"></input>
                        </th>
                        <th>Disapprove</th>
                        <th>Defer</th>
                        <th>No Action</th>
                        <th>Reason</th>
                        </tr>
                        </thead>
                        <c:forEach var="requestRow" items="${employeeRow.leaveRequestList}">
                            <tr>
                                <td>${requestRow.requestedDate}</td>
                                <td>${requestRow.requestedHours}</td>
                                <td>${requestRow.description}</td>
                                <td>${requestRow.leaveCode}</td>
                                <td>${requestRow.submittedTime}</td>

                                <c:set var="requestId"
                                       value="${employeeRow.principalId}_${requestRow.leaveRequestDocId}"/>

                                    <%-- radio buttons --%>
                                <td><input type="radio" name="action_${requestId}" id="action_${requestId}"
                                           value="approve"></td>
                                <td><input type="radio" name="action_${requestId}" id="action_${requestId}"
                                           value="disapprove"></td>
                                <td><input type="radio" name="action_${requestId}" id="action_${requestId}"
                                           value="defer"></td>
                                <td><input type="radio" name="action_${requestId}" id="action_${requestId}"
                                           value="noAction" checked="checked"></td>
                                    <%-- when approve/noAction selected, reason field is disabled --%>
                                <td><input type="text" name="reason_${requestId}" id="reason_${requestId}" size="100"
                                           disabled="disabled" class="ui-state-disabled"></td>
                            </tr>
                        </c:forEach>
                        <tr class="noborder">
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td>
                                <input type="submit" class="approve" value="Take Action"
                                       name="takeAction_${employeeRow.principalId}"
                                       id="takeAction_${employeeRow.principalId}"/>
                            </td>
                        </tr>
                        </table>
                    </c:if>
                </c:forEach>
            </c:when>
            <c:otherwise>
                There are no pending requests at the moment.
            </c:otherwise>
        </c:choose>
        </div>
    </html:form>
</tk:tkHeader>