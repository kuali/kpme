<%@include file="EdoTldHeader.jsp" %>

<edo:edoPageLayout>
    <edo:edoHeader></edo:edoHeader>

    <div style="width: 100%;">

        <edo:edoTreeNav />
        <div class="content">

            <h2>General Administrative Actions</h2>

         <%-- <edo:edoCurrentPermissions />

            Supp Doc Group List<br>

            <c:forEach var="grp" items="${groupList}">
                ${grp}<br>
            </c:forEach>
            <P>
                Member of any groups? ${isMember}
            </P> --%>

            <c:if test="${!empty missingGroups}">
                Missing Groups:<br><br>
                <c:forEach var="grp" items="${missingGroups}">
                    ${grp}<br>
                </c:forEach>
            </c:if>
            <c:if test="${empty missingGroups}">
                All Expected Groups Found<br>
            </c:if>

        </div>
        <br style="clear: both;" />
    </div>
    <edo:edoFooter/>
</edo:edoPageLayout>
