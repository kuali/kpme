<%@ include file="/WEB-INF/jsp/TkTldHeader.jsp"%>
<c:set var="Form" value="${TimeDetailWSActionForm}" scope="request"/>
<c:set var="TimeApprovalWSForm" value="${TimeApprovalWSActionForm}" scope="request"/>
<c:set var="LeaveCalendarWSForm" value="${LeaveCalendarWSForm}" scope="request"/>
${Form.outputString}
${TimeApprovalWSForm.outputString}
${LeaveCalendarWSForm.outputString}