<%@ include file="/WEB-INF/jsp/TkTldHeader.jsp"%>

<jsp:useBean id="tagSupport" class="org.kuali.hr.time.workflow.web.WorkflowTagSupport"/>
<%@ attribute name="appRow" required="true" type="org.kuali.hr.time.approval.web.ApprovalLeaveSummaryRow"%>

<div id="actions">
        <!-- Need to set the document ID so that TimesheetSubmit can find it -->
        <c:choose>
            <c:when test="${appRow.approvable}">
                <input type="button" id="ts-approve-button" class="button" value="Approve" name="approve" 
                	onclick="location.href='changeTargetPerson.do?${appRow.userTargetURLParams}&targetUrl=LeaveCalendarSubmit.do%3Faction=${tagSupport.approveAction}%26documentId=${appRow.documentId}%26methodToCall=approveApprovalTab%26${Form.pageId}'"/>
            </c:when>
            <c:otherwise>
                <input disabled id="ts-approve-button" type="button" class="button" value="Approve" name="approve"/>
            </c:otherwise>
        </c:choose>
</div>