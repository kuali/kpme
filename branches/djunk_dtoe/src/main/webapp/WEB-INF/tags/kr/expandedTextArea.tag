<%--
 Copyright 2005-2008 The Kuali Foundation
 
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
<%@ include file="/kr/WEB-INF/jsp/tldHeader.jsp"%>
<%@ attribute name="textAreaFieldName" required="true" %>
<%@ attribute name="action" required="true" %>
<%@ attribute name="textAreaLabel" required="true" %>
<%@ attribute name="disabled" required="false" %>
<%@ attribute name="formKey" required="false" %>
<%@ attribute name="sessionDocument" required="false" %>
<%@ attribute name="title" required="false" %>

<c:if test="${empty formKey}">
  <c:set var="formKey" value="88888888" />
</c:if>


  <c:choose>
    <c:when test="${disabled}">
      <img class="nobord" 
           src="${ConfigProperties.kr.externalizable.images.url}pencil_add1.png"
           alt="Expand Text Area">
    </c:when>
    <c:otherwise>
       <html:image property="methodToCall.updateTextArea.((#${textAreaFieldName}:${action}:${textAreaLabel}#))"
                   src='${ConfigProperties.kr.externalizable.images.url}pencil_add.png'
                   onclick="javascript: textAreaPop('${textAreaFieldName}','${action}','${textAreaLabel}',${formKey});return false"
                   styleClass="tinybutton"
                   title="${title}"
                   alt="Expanded Text Area"/>
    </c:otherwise>
  </c:choose>
