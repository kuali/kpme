<%@ include file="/WEB-INF/jsp/TkTldHeader.jsp"%>

<div id="calendar-payPeriod">

    <table class="cal-header">
		<tbody>
			<tr>
				<td>
					<span class="header-title">Calendar Years</span>
	                <select id="selectedCalendarYear" name="selectedCalendarYear">
	                    <c:forEach var="calendarYear" items="${Form.calendarYears}">
	                        <c:choose>
	                            <c:when test="${Form.selectedCalendarYear eq calendarYear}">
	                                <option value="${calendarYear}" selected="selected">${calendarYear}</option>
	                            </c:when>
	                            <c:otherwise>
	                                <option value="${calendarYear}">${calendarYear}</option>
	                            </c:otherwise>
	                        </c:choose>
	                    </c:forEach>
	                </select>
	                <%--
	                <div id="payPeriod-section">
	                 --%>
	        			<span class="header-title">Pay Periods</span>
                        <select id="selectedPayPeriod" name="selectedPayPeriod">
                           <option value=''>-- select a pay period --</option>
                           <c:forEach var="payPeriod" items="${Form.payPeriodsMap}">
		                      <c:choose>
		                          <c:when test="${Form.selectedPayPeriod eq payPeriod.key}">
		                              <option value="${payPeriod.key}" selected="selected">${payPeriod.value}</option>
		                          </c:when>
		                          <c:otherwise>
		                              <option value="${payPeriod.key}">${payPeriod.value}</option>
		                          </c:otherwise>
		                      </c:choose>
                 		 </c:forEach>
                        </select>
                   <%--
                    </div>
                     --%>
		        </td>
			</tr>
		</tbody>
	</table>

</div>