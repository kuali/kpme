/**
 * If you need to change the theme base, the css file is: jquery-ui-1.8.1.custom.css
 */

$(document).ready(function() {

    // tabs
    /**
     * This is the default tab function provided by jQuery
     */
    // var $tabs = $('#tabs').tabs({ selected: 4});
    /**
     * the default tab function provided by jQuery doesn't work well for us.
     * It works very well if you have all the contents in the same page or load contents through ajax.
     * Since we want the direct link for each tab, the function below takes advantages from the jQuery css theme/
     *
     * Noted that round corners for the tabs and the panel work in every browser except IE 7 and 8.
     */
    $("#tabs li").hover(function() {
        $(this).addClass("ui-state-hover");
    }, function() {
        $(this).removeClass("ui-state-hover");
    });

    var tabId = $("#tabId").val();
    if (tabId != undefined) {
        $("#tab-section > #" + tabId)
                .addClass("ui-tabs-selected ui-state-focus ui-state-active");
    }
    // end of tab

    // buttons
    $('.button').button();

    $('button').button({
                icons : {
                    primary : 'ui-icon-help'
                },
                text : false
            });

    $('.expand').button({
                icons: {
                    primary: 'ui-icon-plus'
                }
            });

    // datepicker
    $('#date-range-begin, #date-range-end, #bdRow1, #edRow1, #bdRow2, #edRow2').datepicker({
                changeMonth : true,
                changeYear : true,
                showOn : 'button',
                showAnim : 'fadeIn',
                buttonImage : 'kr/static/images/cal.gif',
                buttonImageOnly : true,
                buttonText : 'Select a date',
                showButtonPanel : true,
                //numberOfMonths : 2,
                // set default month based on the current browsing month
                // appendText : '<br/>format: mm/dd/yyyy',
                constrainInput : true,
                minDate : new Date($('#beginPeriodDate').val()),
                maxDate : new Date($('#endPeriodDate').val())
            });

    // hide the date picker by default
    // https://jira.kuali.org/browse/KPME-395
    $('#ui-datepicker-div').css('display', 'none');

    // clock
    var currentServerTime = parseFloat($("#currentServerTime").val());
    var options = {
        format : '%I:%M:%S %p',
        utc: true,
        utc_offset: -(new Date(currentServerTime).getTimezoneOffset() / 60)
    };
    $(".jClock").jclock(options);

    // elapsed time
    // http://keith-wood.name/countdown.html
//    if($("#clock-button").val() == "Clock Out") {
//        $("#lastClockedInTime").val("");
//        $("#elapsed-time").val("00:00:00");
//    }
    var lastClockedInTime = $("#lastClockedInTime").val();
    var clockAction = $("#clockAction").val();
    var startTime = clockAction == 'CO' ? new Date(lastClockedInTime) : new Date(currentServerTime);

    $('.elapsedTime').countdown({
                since : startTime,
                compact : true,
                format : 'dHMS',
                description : ''
            })

    
    // tooltip
    // http://flowplayer.org/tools/tooltip/index.html
    $(" .holidayNameHelp").tooltip({ effect: 'slide'});

    $("#beginTimeHelp, #endTimeHelp, #beginTimeHelp1, #endTimeHelp1, #hourHelp" ).tooltip({

                // place tooltip on the right edge
                position : "center right",

                // a little tweaking of the position
                offset : [-2, 10],

                // use the built-in fadeIn/fadeOut effect
                effect : "fade",

                // custom opacity setting
                opacity : 0.7,

                fadeInSpeed : 500
            });

    // note accordion
    $("#note, #routeLog").accordion({
                collapsible : true,
                active : 2
            });

    // person detail accordion
    $("#person-detail-accordion").accordion({
                collapsible : true,
                active : 0,
                autoHeight: false
            });

    // apply time entry widget to the tabular view
    $(".timesheet-table-week1 :input, .timesheet-table-week2 :input").blur(
            function() {
                magicTime(this);
            }).focus(function() {
                if (this.className != 'error') this.select();
            });

    // Button for iFrame show/hide to show the missed punch items
    // The iFrame is added to the missed-punch-dialog as a child element.
    // tdocid is a variable that is set from the form value in 'clock.jsp'
    $('#missed-punch-iframe-button').click(function() {

        $('#missed-punch-dialog').empty();
        $('#missed-punch-dialog').append('<iframe width="800" height="500" src="missedPunch.do?methodToCall=docHandler&command=initiate&docTypeName=MissedPunchDocumentType&tdocid=' + tdocid + '"></iframe>');

        $('#missed-punch-dialog').dialog({
                    autoOpen: true,
                    height: 'auto',
                    width: 'auto',
                    modal: true,
                    buttons: {
                        //"test" : function() {
                        //}
                    }
                });
    });

    $("#bdRow1, #edRow1").change(function() {
        $(this).removeClass('ui-state-error');
        recalculateHrs(1);
    });

    $("#bdRow2, #edRow2").change(function() {
        $(this).removeClass('ui-state-error');
        recalculateHrs(2);
    });
    
    $("#btRow1, #etRow1").change(function() {
        $(this).removeClass('ui-state-error');
        magicTime($(this));
        recalculateHrs(1);
    });
    
    $("#btRow2, #etRow2").change(function() {
        $(this).removeClass('ui-state-error');
        magicTime($(this));
        recalculateHrs(2);
    });


    $('#saveTimeBlock').click(function() {
//	   function updateTips(t) {
//		   $('#validation').text(t)
//		   			.addClass('ui-state-error')
//		   			.css({'color':'red','font-weight':'bold'});
//	   }
        var validFlag = true;
        var validation = $('#validation');

        function updateValidationMessage(t) {
            validation.text(t)
                    .addClass('ui-state-error')
                    .css({'color':'red','font-weight':'bold'});
        }

//	   function checkLength(o, n, min, max) {
//	         if (o.val().length > max || o.val().length < min) {
//	             o.addClass('ui-state-error');
//	             updateValidationMessage(n + " field is not valid");
//	             return false;
//	         }
//	         return true;
//	   }
        var tbl = document.getElementById('tblNewTimeBlocks');
        var rowLength = tbl.rows.length;
        var assignValueCol = '';
        var beginDateCol = '';
        var endDateCol = '';
        var beginTimeCol = '';
        var endTimeCol = '';
        var hrsCol = '';
        var valueSeperator = '****';
        var timeBlockId = $("#tbId").val();
        var originalHrs = $("#originHrs").val();


        var errorMsgs = '';
        var totalHrs = 0;
        if (rowLength <= 2) {
            updateTips("Please use Add button to add entries or click Cancel to close the window.");
            return false;
        }
        var form1 = document.forms[0];
        var originalEndDateTime = new Date(form1.endTimestamp.value);
        var originalBeginDateTime = new Date(form1.beginTimestamp.value);
        
        for (var i = 1; i < rowLength - 1; i++) {
            var assignValue = $("#assignmentRow" + i).val();
            var beginDate = $("#bdRow" + i).val();
            var endDate = $("#edRow" + i).val();
            var beginTime = $("#btRow" + i).val();
            var endTime = $("#etRow" + i).val();
            var hrs = $("#hrRow" + i).val();

            assignValueCol += assignValue + valueSeperator;
            beginDateCol += beginDate + valueSeperator;
            endDateCol += endDate + valueSeperator;
            hrsCol += hrs + valueSeperator;

            validFlag &= checkLength($("#bdRow" + i), "Date/Time", 10, 10);
            validFlag &= checkLength($("#edRow" + i), "Date/Time", 10, 10);
            validFlag &= checkLength($("#btRow" + i), "Date/Time", 8, 8);
            validFlag &= checkLength($("#etRow" + i), "Date/Time", 8, 8);

            if (validFlag) {
                var dateString = beginDate + ' ' + beginTime;
                var beginTimeTemp = new Date(dateString);
                beginTimeCol += bTimeFormated + valueSeperator;

                dateString = endDate + ' ' + endTime;
                var endTimeTemp = new Date(dateString);
                var eTimeFormated = endTimeTemp.getHours() + ':' + endTimeTemp.getMinutes();
                endTimeCol += eTimeFormated + valueSeperator;

                if(i == rowLength - 2) { // endTime of last row should include seconds of originalEndTime
        	    	var eTimeFormated = endDate + ' ' + endTimeTemp.getHours() + ':' + endTimeTemp.getMinutes() + ':' + originalEndDateTime.getSeconds();
        	    	var newEndDate = new Date(eTimeFormated);
        	    	var hrsDifferent = newEndDate - beginTimeTemp;
        	    } else if(i == 1){ // beginTime of first row should use begin time plus seconds from originalBeginTime 
        	    	var bTimeFormated = beginDate + ' ' + beginTimeTemp.getHours() + ':' + beginTimeTemp.getMinutes() + ':' + originalBeginDateTime.getSeconds();
        	    	var newBeginDate = new Date(bTimeFormated);
        	    	var hrsDifferent = endTimeTemp - newBeginDate;
        	    } else {
        	    	var hrsDifferent = endTimeTemp - beginTimeTemp;
        	    }
                
                if (hrsDifferent <= 0) {
                    updateTips("Hours for item " + i + "not valid");
                    var hrs = hrsDifferent / 3600000;
                    $("#hrRow" + i).val(hrs);
                    return false;
                }
                var hrs = Math.round(hrsDifferent * 100 / 3600000) / 100;
                $("#hrRow" + i).val(hrs);
                totalHrs += hrs;
            }// end of if
            else {
                return false;
            }

        }// end of for loop
        if (totalHrs != originalHrs) {
            updateTips("Total Hours entered not equal to the hours of the original time block");
            return false;
        }

        // for form submit
        $("#newAssignDesCol").val(assignValueCol);
        $("#newBDCol").val(beginDateCol);
        $("#newEDCol").val(endDateCol);
        $("#newBTCol").val(beginTimeCol);
        $("#newETCol").val(endTimeCol);
        $("#newHrsCol").val(hrsCol);
        $("#tbId").val(timeBlockId);

        var params = {};
        params['newAssignDesCol'] = assignValueCol;
        params['newBDCol'] = beginDateCol;
        params['newEDCol'] = endDateCol;
        params['newBTCol'] = beginTimeCol;
        params['newETCol'] = endTimeCol;
        params['newHrsCol'] = hrsCol;
        params['tbId'] = timeBlockId;

        $.ajax({
                    url: "Clock.do?methodToCall=validateNewTimeBlock",
                    data: params,
                    cache: false,
                    success: function(data) {
                        var json = jQuery.parseJSON(data);
                        // if there is no error message, submit the form and save the new time blocks
        				if(json == null || json.length == 0) {
	            			$.ajax({ 
	            				url: "Clock.do?methodToCall=saveNewTimeBlocks",
	                            data: params,
	                            cache: false,
	                            success: function(data) {
	            					// save is successful, close the window
	            					window.close();
	            					return false;
	            				},
	            				error: function() {
	            					updateTips("Cannot save the time blocks");
	            		            return false;
	            		        }
	            				
	            			});
                        } else {
                            // if there is any error, grab error messages (json) and put them in the error message
                            var json = jQuery.parseJSON(data);
                            $.each(json, function (index) {
                                errorMsgs += "Error : " + json[index] + "\n";
                            });
                            updateTips(errorMsgs);
                            return false;
                        }
                    },
                    error: function() {
              			updateTips("Validation failed");
                        return false;
                    }
                })

    });

    // submit the form for changing the ip address in the batch job page
    $('.changeIpAddress').click(function() {
        var value = $(this).siblings().val().split("_");
        var ipToChange = value[0];
        var tkBatchJobEntryId = value[1];

        window.location = "BatchJob.do?methodToCall=changeIpAddress&ipToChange=" + ipToChange + "&tkBatchJobEntryId=" + tkBatchJobEntryId;
    });
    
    
    $('#pay_period_prev').button({
        icons: {
            primary: 'ui-icon-circle-triangle-w'
        },
        text: false
    });
    
    $('#pay_period_next').button({
        icons: {
            primary: "ui-icon-circle-triangle-e"
        },
        text: false
    });
    
});

$.fn.parseTime = function() {
    var parsedTime = new Array();
    var timeAndAmPm = $(this).val().split(" ");
    var time = timeAndAmPm[0].split(":");

    if (timeAndAmPm == '') {
        parsedTime['hour'] = 00;
        parsedTime['minute'] = 00;
    }
    else {
        if (timeAndAmPm[1] == 'PM') {
            parsedTime['hour'] = Number(time[0]) == 12 ? time[0] : Number(time[0]) + 12;
        }
        else {
            parsedTime['hour'] = Number(time[0]);
        }
        parsedTime['minute'] = Number(time[1]);
    }
    return parsedTime;
}
function extractUrlBase() {
    url = window.location.href;
    pathname = window.location.pathname;
    idx1 = url.indexOf(pathname);
    idx2 = url.indexOf("/", idx1 + 1);
    extractUrl = url.substr(0, idx2);
    return extractUrl;
}

function checkLength(o, n, min, max) {
    if (o.val().length > max || o.val().length < min) {
        o.addClass('ui-state-error');
        updateValidationMessage(n + " field is not valid");
        return false;
    }
    return true;
}

function updateTips(t) {
    $('#validation').text(t)
            .addClass('ui-state-error')
            .css({'color':'red','font-weight':'bold'});
}

function addTimeBlockRow(form, tempArr) {

    var tbl = document.getElementById('tblNewTimeBlocks');
    var lastRow = tbl.rows.length;
    // if there's no header row in the table, then iteration = lastRow + 1
    var iteration = lastRow - 1;

    var row = tbl.insertRow(iteration);

    //Assignment Dropdown list
    var cellCount = row.insertCell(0);
    var textNode = document.createTextNode(iteration);
    cellCount.appendChild(textNode);

    var cellAssignment = row.insertCell(1);
    var sel = document.createElement('select');
    var idString = 'assignmentRow' + iteration;
    sel.name = idString;
    sel.id = idString;

//	var initString = form.assignmentList.value;
//	var subString1 = initString.substring(1, initString.length-1);
//	var assignList= subString1.split(',');

//	for(var i=0; i< assignList.length; i++) {
//		var tempString = ltrim(assignList[i]);
//		tempString = rtrim(tempString);
//		sel.options[i] = new Option(tempString, tempString);
//		if(tempString == originalAssign) {
//			sel.options[i].selected=true;
//		}
//	}

    var originalAssign = form.originalAssignment.value;
    var initString = form.distributeAssignList.value;
    var string1 = initString.substring(1, initString.length - 1);
    var keyValueList = string1.split(',');
    for (var i = 0; i < keyValueList.length; i++) {
        var aSet = keyValueList[i].split('=');
        var trimedKey = ltrim(aSet[0]);
        trimedKey = rtrim(trimedKey);
        var trimedVal = ltrim(aSet[1]);
        trimedVal = rtrim(trimedVal);
        sel.options[i] = new Option(trimedKey, trimedVal);
        if (trimedKey == originalAssign) {
            sel.options[i].selected = true;
        }
    }

    cellAssignment.appendChild(sel);

    // begin date/time
    var cellBeginDate = row.insertCell(2);
    var el = document.createElement('input');
    idString = 'bdRow' + iteration;
    el.type = "text";
    el.name = idString;
    el.id = idString;
    el.size = 10;
    el.value = form.beginDateOnly.value;
    var datePickerId = '#' + idString;
    cellBeginDate.appendChild(el);

    var cellBeginTime = row.insertCell(3);
    var el = document.createElement('input');
    idString = 'btRow' + iteration;
    el.name = idString;
    el.id = idString;
    el.size = 10;
    cellBeginTime.appendChild(el);
    var timeChangeId = '#' + idString;
    var timeFormatMessage = "Supported formats:<br/>9a, 9 am, 9 a.m.,  9:00a, 9:45a, 3p, 0900, 15:30, 1530";
// help button for time format
    el = document.createElement('input');
    el.type = 'button';
    el.style.width = "20px";
    el.style.height = "23px";
    el.title = timeFormatMessage;
    el.id = "beginTimeHelp" + iteration;
    el.value = "?";
    cellBeginTime.appendChild(el);
    el.removeAttribute("tabindex");
    var timeHelpId = '#' + el.id; 	// for tooltip

    //end date/time
    var cellEndDate = row.insertCell(4);
    var el = document.createElement('input');
    idString = 'edRow' + iteration;
    el.name = idString;
    el.id = idString;
    el.size = 10;
    el.value = form.endDateOnly.value;
    datePickerId += ', #' + idString;
    cellEndDate.appendChild(el);
    

    var cellEndTime = row.insertCell(5);
    var el = document.createElement('input');
    idString = 'etRow' + iteration;
    el.name = idString;
    el.id = idString;
    el.size = 10;
    el.value = form.endTimeOnly.value;
    cellEndTime.appendChild(el);
    timeChangeId += ', #' + idString;

    el = document.createElement('input');
    el.type = 'button';
    el.style.width = "20px";
    el.style.height = "23px";
    el.title = timeFormatMessage;
    el.id = "endTimeHelp" + iteration;
    el.removeAttribute("tabindex");
//	el.tabindex="999";
    el.value = "?";
    cellEndTime.appendChild(el);
    timeHelpId += ', #' + el.id; 	// for tooltip

    var cellHours = row.insertCell(6);
    var el = document.createElement('input');
    idString = 'hrRow' + iteration;
    el.name = idString;
    el.id = idString;
    el.size = 5;
//    el.value = form.hours.value;
    el.readOnly = true;
    cellHours.appendChild(el);
    var hrId = '#' + idString;

    row.insertCell(7);
    recalculateTotal();

    // datepicker
    $(datePickerId).datepicker({
                changeMonth : true,
                changeYear : true,
                showOn : 'button',
                showAnim : 'fadeIn',
                buttonImage : 'kr/static/images/cal.gif',
                buttonImageOnly : true,
                buttonText : 'Select a date',
                showButtonPanel : true,
                constrainInput : true,
                minDate : new Date($('#beginDate').val()),
                maxDate : new Date($('#endDate').val())
            });

    //time format helper
    $(timeHelpId).tooltip({
                // place tooltip on the right edge
                position : "center right",
                // a little tweaking of the position
                offset : [-2, 10],
                // use the built-in fadeIn/fadeOut effect
                effect : "fade",
                // custom opacity setting
                opacity : 0.7,
                fadeInSpeed : 100
            });

    $(datePickerId).change(function() {
        $(this).removeClass('ui-state-error');
        recalculateHrs(iteration);
    });

    $(timeChangeId).change(function() {
        $(this).removeClass('ui-state-error');
        magicTime($(this));
        recalculateHrs(iteration);
    });

}

function recalculateHrs(itr) {
    var beginDate = $("#bdRow" + itr).val();
    var endDate = $("#edRow" + itr).val();
    var beginTime = $("#btRow" + itr).val();
    var endTime = $("#etRow" + itr).val();
    var validFlag = true;
    var tbl = document.getElementById('tblNewTimeBlocks');
    var rowLength = tbl.rows.length;
    var form1 = document.forms[0];
    var originalEndDateTime = new Date(form1.endTimestamp.value);
    var originalBeginDateTime = new Date(form1.beginTimestamp.value);
    
    validFlag &= checkLength($("#bdRow" + itr), "Date/Time", 10, 10);
    validFlag &= checkLength($("#edRow" + itr), "Date/Time", 10, 10);
    validFlag &= checkLength($("#btRow" + itr), "Date/Time", 8, 8);
    validFlag &= checkLength($("#etRow" + itr), "Date/Time", 8, 8);

    if (validFlag) {
        var dateString = beginDate + ' ' + beginTime;
        var beginTimeTemp = new Date(dateString);
        dateString = endDate + ' ' + endTime;
        var endTimeTemp = new Date(dateString);
        
	    if(itr == rowLength - 2) { // endTime of last row should include seconds of originalEndTime
	    	var eTimeFormated = endDate + ' ' + endTimeTemp.getHours() + ':' + endTimeTemp.getMinutes() + ':' + originalEndDateTime.getSeconds();
	    	var newEndDate = new Date(eTimeFormated);
	    	var hrsDifferent = newEndDate - beginTimeTemp;
	    } else if(itr == 1){ // beginTime of first row should use begin time plus seconds from originalBeginTime 
	    	var bTimeFormated = beginDate + ' ' + beginTimeTemp.getHours() + ':' + beginTimeTemp.getMinutes() + ':' + originalBeginDateTime.getSeconds();
	    	var newBeginDate = new Date(bTimeFormated);
	    	var hrsDifferent = endTimeTemp - newBeginDate;
	    } else {
	    	var hrsDifferent = endTimeTemp - beginTimeTemp;
	    }
	    
        if (hrsDifferent <= 0) {
            updateTips("Hours for item " + itr + "not valid");
            var hrs = hrsDifferent / 3600000;
            hrs = Math.round(hrs*100)/100;
            $("#hrRow" + itr).val(hrs);
            return false;
        }
        var hrs = Math.round(hrsDifferent * 100 / 3600000) / 100;
        $("#hrRow" + itr).val(hrs);

        recalculateTotal();
    }
    else {
        return false;
    }
}

function recalculateTotal() {
    var tbl = document.getElementById('tblNewTimeBlocks');
    var rowLength = tbl.rows.length;
    var totalHrs = 0;
    for (var i = 1; i < rowLength - 1; i++) {
        var hrs = $("#hrRow" + i).val();
        if(hrs != undefined && hrs != "") {
        	totalHrs += parseFloat(hrs);
        }
    }
    $("#hrsTotal").val(totalHrs);
}

function ltrim(str) {
    return str.replace(/^\s+/, '');
}
function rtrim(str) {
    return str.replace(/\s+$/, '');
}

function getParameterByName(name) {
    name = name.replace(/[\[]/, "\\\[").replace(/[\]]/, "\\\]");
    var regexS = "[\\?&]" + name + "=([^&#]*)";
    var regex = new RegExp(regexS);
    var results = regex.exec(window.location.href);
    if (results == null)
        return "";
    else
        return decodeURIComponent(results[1].replace(/\+/g, " "));
}

function toCamelCase(str) {
    return str.replace(/^.?/g, function(match) {
        return match.toLowerCase();
    });
}







