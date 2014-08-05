<%@ include file="/WEB-INF/jsp/TkTldHeader.jsp"%>
<%-- for time approval, set searchId to searchValue, for leave Approval, set searchId to leaveSearchValue 
the id is used in approval.js--%>
<%@ attribute name="searchId" required="true" type="java.lang.String" %>

<table class="navigation">   
        <tr>
            <td class="left">
                Search By :
                <label for="search field">
                    <select id="searchField" name="searchField">
                        <option value="principalName">Principal Id</option>
                        <option value="documentId">Document Id</option>
                    </select>
                </label>
                Value :
                <label for="search value">
           			<input id="${searchId}" name="${searchId}" type="text" value="${Form.searchTerm}" placeholder="enter at least 3 chars" />
                    <span id='loading-value' style="display:none;"><img src='images/ajax-loader.gif'></span>
                    <input type="button" id='search' value="Search"
                           class="ui-button ui-widget ui-state-default ui-corner-all"/>
                </label>
            </td>
            <td>
                <div style="text-align: center">
                    <c:if test="${Form.prevPayCalendarId ne null}">
                        <button id="nav_prev" class="ui-button ui-widget ui-state-default ui-corner-all ui-button-icon-only" role="button" title="Previous"
                                onclick="this.form.hrPyCalendarEntriesId.value='${Form.prevPayCalendarId}'; this.form.methodToCall.value='loadApprovalTab'; this.form.submit();">
                            <span class="ui-button-text">Previous</span>
                        </button>
                        <%--<input type="button" class="prev" value="Previous" name="Previous"--%>
                               <%--onclick="this.form.hrPyCalendarEntriesId.value='${Form.prevPayCalendarId}'; this.form.methodToCall.value='loadApprovalTab'; this.form.submit();"/>--%>
                    </c:if>
                    <span id="payBeginDate" style="font-size: 1.5em; vertical-align: middle;"><fmt:formatDate
                            value="${Form.payBeginDate}" pattern="MM/dd/yyyy"/></span> -
                    <span id="payEndDate" style="font-size: 1.5em; vertical-align: middle;"><fmt:formatDate
                            value="${Form.payEndDate}" pattern="MM/dd/yyyy"/></span>
                    <c:if test="${Form.nextPayCalendarId ne null}">
                        <button id="nav_next" class="ui-button ui-widget ui-state-default ui-corner-all ui-button-icon-only" role="button" title="Next"
                                onclick="this.form.hrPyCalendarEntriesId.value='${Form.nextPayCalendarId}'; this.form.methodToCall.value='loadApprovalTab'; this.form.submit();">
                            <span class="ui-button-text">Next</span>
                        </button>

                        <%--<input type="button" class="next" value="Next" name="Next"--%>
                               <%--onclick="this.form.hrPyCalendarEntriesId.value='${Form.nextPayCalendarId}'; this.form.methodToCall.value='loadApprovalTab'; this.form.submit();"/>--%>
                    </c:if>
                </div>
            </td>
            <td>
            	<tk:payCalendarSelect />
            </td>
            <td></td>
        </tr>
        <tr>
        	<td></td>
        	<c:if test="${!Form.onCurrentPeriod}" >
	        	<td align="center">
	        		<a href="${KualiForm.backLocation}?methodToCall=gotoCurrentPayPeriod"
	                  	 target="_self" id="cppLink">Go to Current Period</a>
	        	</td>
        	</c:if>
        </tr>
</table>