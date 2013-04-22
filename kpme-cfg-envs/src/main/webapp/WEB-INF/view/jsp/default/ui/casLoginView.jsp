<%--
 Copyright 2008 The Kuali Foundation.
 
 Licensed under the Educational Community License, Version 1.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 
 http://www.opensource.org/licenses/ecl1.php
 
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
--%>
<%@ page pageEncoding="UTF-8" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<jsp:directive.include file="includes/top.jsp" />
<p/> 
<p>
<%-- 
Kuali modification to warn about demo authentication 
--%>
<c:set var="showPasswordField" scope="request" value="${configProperties['org.kuali.cas.auth.showPasswordField']}"/>                    
<c:if test="${not requestScope.showPasswordField}">
<font face="Arial,Helvetica">Enter your UserID below; then click on the <b>Login</b> button to continue.  A password is not required, because this is a </font>
<font face="Arial,Helvetica" color="red"><b>DEMO ONLY</b></font><font face="Arial,Helvetica"> authentication application.</font>
</p>
</c:if>
			<form:form method="post" id="fm1" cssClass="fm-v clearfix" commandName="${commandName}" htmlEscape="true">
			    <form:errors path="*" cssClass="errors" id="status" element="div" />
                <div class="box" id="login">
                <%-- <spring:message code="screen.welcome.welcome" /> --%>
                	<%-- 
                    Kuali modification to remove default instructions 
                    --%>
                    <%--<h2><spring:message code="screen.welcome.instructions" /></h2> --%>
                    <div class="row">
                        <label for="username"><spring:message code="screen.welcome.label.netid" /></label>
						<c:if test="${not empty sessionScope.openIdLocalId}">
						<strong>${sessionScope.openIdLocalId}</strong>
						<input type="hidden" id="username" name="username" value="${sessionScope.openIdLocalId}" />
						</c:if>
						
						<c:if test="${empty sessionScope.openIdLocalId}">
						<spring:message code="screen.welcome.label.netid.accesskey" var="userNameAccessKey" />
						<form:input cssClass="required" cssErrorClass="error" id="username" size="25" tabindex="1" accesskey="${userNameAccessKey}" path="username" autocomplete="false" htmlEscape="true" />
						</c:if>
                    </div>
                    <%-- 
                    Kuali modification to remove password field for demo authentication 
                    --%>
                    <c:if test="${not requestScope.showPasswordField}">
                    <input type="hidden" id="password" name="password" value="demo" />
					</c:if>
                    <c:if test="${requestScope.showPasswordField}">
                    <div class="row">
                        <label for="password"><spring:message code="screen.welcome.label.password" /></label>
						<%--
						NOTE: Certain browsers will offer the option of caching passwords for a user.  There is a non-standard attribute,
						"autocomplete" that when set to "off" will tell certain browsers not to prompt to cache credentials.  For more
						information, see the following web page:
						http://www.geocities.com/technofundo/tech/web/ie_autocomplete.html
						--%>
						<spring:message code="screen.welcome.label.password.accesskey" var="passwordAccessKey" />
						<form:password cssClass="required" cssErrorClass="error" id="password" size="25" tabindex="2" path="password"  accesskey="${passwordAccessKey}" htmlEscape="true" autocomplete="off" />
                    </div>
                    </c:if>
                    <%-- 
                    Kuali modification to remove warn me checkbox
                    --%>
                    <%--  
                    <div class="row check">
                        <input id="warn" name="warn" value="true" tabindex="3" accesskey="<spring:message code="screen.welcome.label.warn.accesskey" />" type="checkbox" />
                        <label for="warn"><spring:message code="screen.welcome.label.warn" /></label>
                    </div>
                    --%>
                    <div class="row btn-row">
						<input type="hidden" name="lt" value="${flowExecutionKey}" />
						<input type="hidden" name="_eventId" value="submit" />

                        <input class="btn-submit" name="submit" accesskey="l" value="<spring:message code="screen.welcome.button.login" />" tabindex="4" type="submit" />
                        <input class="btn-reset" name="reset" accesskey="c" value="<spring:message code="screen.welcome.button.clear" />" tabindex="5" type="reset" />
                    </div>
                </div>
               
	            <div id="sidebar">
	                <p><spring:message code="screen.welcome.security" /></p>
	                <div id="list-languages">
						<c:set var="query" value="<%=request.getQueryString() == null ? \"\" : request.getQueryString().replaceAll(\"&locale=([A-Za-z][A-Za-z]_)?[A-Za-z][A-Za-z]|^locale=([A-Za-z][A-Za-z]_)?[A-Za-z][A-Za-z]\", \"\")%>" />
						<c:set var="loginUrl" value="login?${query}${not empty query ? '&' : ''}locale=" />
	                    <h3>Languages:</h3>
						<ul
							><li class="first"><a href="login?${query}${not empty query ? '&' : ''}locale=en">English</a></li
							><li><a href="${loginUrl}es">Spanish</a></li				
							><li><a href="${loginUrl}fr">French</a></li
							><li><a href="${loginUrl}ru">Russian</a></li
							><li><a href="${loginUrl}nl">Nederlands</a></li
							><li><a href="${loginUrl}sv">Svenskt</a></li
							><li><a href="${loginUrl}it">Italiano</a></li
							><li><a href="${loginUrl}ur">Urdu</a></li
							><li><a href="${loginUrl}zh_CN">Chinese (Simplified)</a></li
							><li><a href="${loginUrl}de">Deutsch</a></li
							><li><a href="${loginUrl}ja">Japanese</a></li
							><li><a href="${loginUrl}hr">Croatian</a></li
							><li><a href="${loginUrl}cs">Czech</a></li
							><li><a href="${loginUrl}sl">Slovenian</a></li
							><li class="last"><a href="${loginUrl}pl">Polish</a></li
						></ul>
	                </div>
	            </div>
        	</form:form>
<jsp:directive.include file="includes/bottom.jsp" />
