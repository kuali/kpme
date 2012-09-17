<%@ include file="/WEB-INF/jsp/TkTldHeader.jsp" %>

<%@ attribute name="cal" required="true" type="org.kuali.hr.time.calendar.CalendarParent" %>
<%@ attribute name="docId" required="true" type="java.lang.String" %>
<%@ attribute name="calType" required="true" type="java.lang.String" %>

<div id="tkCal" class="ui-widget cal ${calType}" style="margin: 20px auto 20px auto; width:95%;">

	<tk:payCalendarSelect />

    <%-- Add Paging Controls for moving between Calendars --%>
    <table class="cal-header">
        <tbody>
        <tr>
            <td>

                <c:if test="${Form.prevDocumentId ne null || (calType eq 'leaveCalendar' && Form.prevCalEntryId ne null)}">
                    <button id="${calType == 'payCalendar' ? 'nav_prev' : 'nav_prev_lc' }"
                            class="ui-button ui-widget ui-state-default ui-corner-all ui-button-icon-only"
                            role="button" title="Previous">
                        <span class="ui-button-icon-primary ui-icon ui-icon-circle-triangle-w"></span>
                        <span class="ui-button-text">Previous</span>
                    </button>
                </c:if>
                <span class="header-title">${cal.calendarTitle}</span>
                <c:if test="${Form.nextDocumentId ne null || (calType eq 'leaveCalendar' && Form.nextCalEntryId ne null)}">
                    <button id="${calType == 'payCalendar' ? 'nav_next' : 'nav_next_lc' }"
                            class="ui-button ui-widget ui-state-default ui-corner-all ui-button-icon-only"
                            role="button" title="Next">
                        <span class="ui-button-icon-primary ui-icon ui-icon-circle-triangle-e"></span>
                        <span class="ui-button-text">Next</span>
                    </button>
                </c:if>
            </td>
        </tr>
        <tr>
        	<c:if test="${!Form.onCurrentPeriod}" >
	        	<td align="center">
	        		<a href="${KualiForm.backLocation}?methodToCall=gotoCurrentPayPeriod"
	                  	 target="_self" id="cppLink">Go to Current Period</a>
	        	</td>
        	</c:if>
        </tr>
        <c:if test="${calType eq 'payCalendar'}">
        <tr>
            <td align="right">
                <a href="${KualiForm.backLocation}?methodToCall=actualTimeInquiry&documentId=${Form.documentId}"
                   target="_blank" id="atiLink">Actual Time Inquiry</a>
            </td>

        </tr>
        </c:if>
    </table>

    <div class="global-error">
        <c:forEach var="warning" items="${Form.warnings}">
            ${warning} <br/>
        </c:forEach>
    </div>

    <div id="tkCalContent">
        <table class="cal-table ${calType}-table">
            <thead>
            <%-- Render Day Labels, starting at FLSA Start day --%>
            <tr>
                <c:forEach var="dayString" items="${cal.calendarDayHeadings}">
                    <th class="ui-state-default week-header">${dayString}</th>
                </c:forEach>
            </tr>
            </thead>

            <tbody>
            <%-- Generate Each Week --%>
            <c:forEach var="week" items="${cal.weeks}" varStatus="rowS">
                <tr style="height:100px;">
                        <%-- Generate Each Day --%>
                    <c:forEach var="day" items="${week.days}" varStatus="dayS">

                        <c:set var="dayStyle" value="width:14%;padding-bottom:20px;"/>
                        <c:set var="dayId" value="day_${day.dayNumberDelta}"/>
                        <c:set var="dayClass" value="create ui-state-default"/>
                        <c:if test="${day.gray}">
                            <c:set var="dayStyle" value="width:14%; background: rgb(224, 235, 225);"/>
                            <c:set var="dayId" value="gray_day"/>
                            <c:set var="dayClass" value="ui-state-default"/>
                        </c:if>


                        <td id="${dayId}" class="${dayClass}" style="${dayStyle}">
                                <%-- Day Number --%>
                            <div class="day-number">${day.dayNumberString}</div>
                                <%-- Render the Time Blocks --%>

                            <div>
                                <tk:payCalendar day="${day}"/>
                                <%--<div class="create" id="${day.dateString}" style="background-color:#cccccc; height:100%; padding-bottom:20px;"></div>--%>
                            </div>
                        </td>
                    </c:forEach>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>
