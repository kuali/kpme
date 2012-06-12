<%--
  Copyright 2011 The Kuali Foundation Licensed under the Educational Community
  License, Version 2.0 (the "License"); you may not use this file except in
  compliance with the License. You may obtain a copy of the License at
  http://www.osedu.org/licenses/ECL-2.0 Unless required by applicable law or
  agreed to in writing, software distributed under the License is distributed
  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
  express or implied. See the License for the specific language governing
  permissions and limitations under the License.
--%>


<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="kme" uri="http://kuali.org/mobility" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>



<spring:message code="maps.title" var="title"/>
<c:set var="localeCode" value="${pageContext.response.locale}" />


<kme:page title="${title}" id="mapslocation" backButton="true" homeButton="true" cssFilename="location" jsFilename="maps" usesGoogleMaps="true" mapLocale="${localeCode}">
	<kme:content>
	
<div id="map_canvas"></div>
	
<script type="text/javascript">
/* Maps */

var markersArray = [];
var userMarkersArray = [];

$('#mapslocation').live("pageshow", function() {
	setContextPath("${pageContext.request.contextPath}");
/* 	$('#map_canvas').gmap({'center': getLatLng(), 'callback': function() {
	
	}); */
	//var buildingCode = $('#map_canvas').jqmData('code');
	var map = initialize("map_canvas", 39.17, -86.5);
	deleteOverlays(markersArray);
	resizeMap();
	var buildingCode = getParameterByName("id");
	if (buildingCode) {
		showBuildingByCode(map, buildingCode);	
	} else {
		var latitude = getParameterByName("latitude");
		var longitude = getParameterByName("longitude");
		if (latitude && longitude) {
			showLocationByCoordinates(map, markersArray, latitude, longitude);	
		}
	}
	//drawUserLocation(map, markersArray, userMarkersArray);
	
	//alert(buildingCode);

});



</script>
	</kme:content>
</kme:page>
