<%@ include file="/WEB-INF/jsp/TkTldHeader.jsp" %>

<%@ attribute name="day" required="true" type="org.kuali.hr.time.calendar.CalendarDay" %>

<c:forEach var="block" items="${day.blockRenderers}" varStatus="status">
    <c:if test="${block.timeBlock.earnCode ne TkConstants.LUNCH_EARN_CODE}">
        <c:choose>
            <c:when test="${status.last}">
                <c:set var="last" value="last-event"/>
            </c:when>
            <c:otherwise>
                <c:set var="last" value=""/>
            </c:otherwise>
        </c:choose>


        <div class="event ${last} ${block.assignmentClass}">
            <c:set var="editableClass" value="event-title-false"/>
            <c:if test="${Form.docEditable}">
                <c:set var="editableClass" value="event-title-true"/>
            </c:if>

            <div id="timeblock_${block.timeBlock.tkTimeBlockId}"
                 class="${editableClass}">
                <c:if test="${block.timeBlock.deleteable}">
                    <div><img id="timeblockDelete_${block.timeBlock.tkTimeBlockId}"
                              class='event-delete'
                              src='images/delete.png'/>
                    </div>
                </c:if>

                <div id="show_${block.timeBlock.tkTimeBlockId}">${block.title}</div>
            </div>
                ${block.timeRange}
            <div>
                <c:if test="${block.earnCodeType ne TkConstants.EARN_CODE_AMOUNT}">
                    <c:forEach var="thdr" items="${block.detailRenderers}">
                        <c:if test="${thdr.title ne TkConstants.LUNCH_EARN_CODE}">
                            <div id="${thdr.timeHourDetail.tkTimeHourDetailId}"
                                 class="event-content">
                                <c:choose>
                                    <c:when test="${thdr.hours ne ''}">
                                        <c:set var="title" value="${thdr.title}"/>
                                        <c:choose>
                                            <c:when test="${thdr.overtimeEarnCode and block.timeBlock.overtimeEditable and !(thdr.title eq 'DOT')}">
                                                <c:set var="title"
                                                       value="<span id='overtime_${block.timeBlock.tkTimeBlockId}' class='overtime'>${thdr.title}</span>"/>
                                            </c:when>
                                            <c:otherwise>
                                                <c:set var="title"
                                                       value="<span>${thdr.title}</span>"/>
                                            </c:otherwise>
                                        </c:choose>

                                        <c:if test="${thdr.title eq TkConstants.HOLIDAY_EARN_CODE}">
                                            <div><a id="holidayNameHelp"
                                                    title="${thdr.holidayName}"
                                                    style="color:white; cursor:pointer;">${title}
                                                - ${thdr.hours} hours</a></div>
                                        </c:if>
                                        <c:if test="${thdr.title ne TkConstants.HOLIDAY_EARN_CODE}">
                                            ${title} - ${thdr.hours} hours
                                        </c:if>
                                    </c:when>
                                    <c:otherwise>
                                        <c:if test="${thdr.title eq TkConstants.HOLIDAY_EARN_CODE}">
                                            <div><a id="holidayNameHelp"
                                                    title="${thdr.holidayName}"
                                                    style="color:white; cursor:pointer;">${thdr.title}
                                                - $${thdr.hours}</a></div>
                                        </c:if>
                                        <c:if test="${thdr.title ne TkConstants.HOLIDAY_EARN_CODE}">
                                            ${thdr.title} - $${thdr.hours}
                                        </c:if>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </c:if>
                    </c:forEach>
                </c:if>
            </div>
            <div class="lunch">
                <c:if test="${block.lunchLabel ne '' and block.timeBlock.deleteable}">
                    <div><img id="lunchDelete_${block.lunchLabelId}"
                              class='event-delete'
                              src='images/delete.png'/>
                    </div>
                </c:if>
                    ${block.lunchLabel}
            </div>
                ${block.amount}
        </div>
    </c:if>
</c:forEach>