/*
 * Copyright 2004-2012 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
$(function () {
	
    // create a CalendarSelect view
    var CalendarSelectView = Backbone.View.extend({
    	 el : $("body"),
    	 events : {
             "change #selectedCalendarYear" : "changeCalendarYear",
             "change #selectedPayPeriod" : "changePayPeriod"
         },
         initialize : function () {
        	 return this;
         },
         render : function () {
             return this;
         },
    
         changeCalendarYear : function () {
         	var selectedCY = $("#selectedCalendarYear option:selected").text();
         	var docId = $('#documentId').val();
         	var newLoc = window.location.pathname + '?methodToCall=changeCalendarYear&selectedCY=' + selectedCY + '&documentId=' + docId;
             window.location = newLoc;
             
         },
         
         changePayPeriod : function () {
          	var selectedPP = $("#selectedPayPeriod option:selected").val();
        	var newLoc = window.location.pathname + '?methodToCall=changePayPeriod&selectedPP=' + selectedPP ;
            window.location = newLoc ;
         }
    
    });
    
    var app = new CalendarSelectView;
    
//    _.mixin({
//    	getSelectedCalendarYear : function () {
//	        return $("#selectedCalendarYear option:selected").val();
//    	},
//    	getSelectedPayPeriod : function () {
//	        return $("#selectedPayPeriod option:selected").val();
//    	}
//});
	
});