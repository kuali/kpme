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
		$("#tabs ul > #" + tabId)
				.addClass("ui-tabs-selected ui-state-focus ui-state-active");
	}
	// end of tab

	// buttons
	$(".button").button();

	$("button").button({
				icons : {
					primary : 'ui-icon-help'
				},
				text : false
			});

	// calendar
	var date = new Date();
	var d = date.getDate();
	var m = date.getMonth();
	var y = date.getFullYear();
    var beginPeriodDate = $("#beginPeriodDate").val() != undefined ? $("#beginPeriodDate").val() : d + "/" + m + "/" + y;
    var endPeriodDate = $("#endPeriodDate").val() != undefined ? $("#endPeriodDate").val() : d + "/" + m + "/" + y;

    var calendar = $('#cal').fullCalendar({
            beginPeriodDate : beginPeriodDate,
            endPeriodDate : endPeriodDate,
            theme : true,
            aspectRatio : 5, // the value here is just to match the height with the add time block panel
            allDaySlot : false,
            header: {
                  left : 'prev, today',
                  center : 'title',
                  right : ''
            },
            selectable: true,
            selectHelper: true,
            select: function(start, end, allDay) {

                // clear any existing values
                $('#beginTimeField, #endTimeField, #hoursField').val("");

                $('#dialog-form').dialog('enable');

                var form = $('#dialog-form').dialog('open');

                form.dialog({
                    beforeclose: function(event, ui) {
	                    var title;
	                    var startTime = $('#beginTimeField');
	                    var endTime = $('#endTimeField');
                        var hours = $('#hoursField');

                        if(hours.val() != '') {
                            title = $('#assignment').val() + " - " + $('#earnCode').val();

                            start.setHours(9);
                            start.setMinutes(0);

                            end.setHours(9+Number(hours.val()));
                            end.setMinutes(0);

                            calendar.fullCalendar('renderEvent',
                              {
                                  title: title,
                                  start: start,
                                  end: end,
                                  allDay: false
                              },
                              true // make the event "stick"
                            );
                            calendar.fullCalendar('unselect');
                        }

	                    if(startTime.val() != '' && endTime.val() != '') {

	                        startTime = $('#beginTimeField').parseTime();
	                        endTime = $('#endTimeField').parseTime();

	                        title = $('#assignment').val() + " - " + $('#earnCode').val();

	                        start.setHours(startTime['hour']);
	                        start.setMinutes(startTime['minute']);

	                        end.setHours(endTime['hour']);
	                        end.setMinutes(endTime['minute']);

	                        calendar.fullCalendar('renderEvent',
	                          {
	                              title: title,
	                              start: start,
	                              end: end,
	                              allDay: false
	                          },
	                          true // make the event "stick"
	                        );
	                        calendar.fullCalendar('unselect');
	                    }
                    }
                });
            },
            editable: true,
           events : [{
                          title : 'HRMS Java developer: RGN',
                          start : new Date(y, m, d, 8, 00),
                          end : new Date(y, m, d, 17, 00),
                          allDay : false
                      }, {
                          title : 'HRMS PS developer: RGN',
                          start : new Date(y, m, d, 12, 0),
                          end : new Date(y, m, d, 13, 0),
                          allDay : false
                      }
            ]
        });

    $("#dialog-form").dialog({
        autoOpen: false,
        height: 420,
        width: 400,
        modal: true,
        buttons: {
            Add: function() {
                $(this).dialog('close');
            },
            Cancel: function() {
                $('#beginTimeField').val("");
                $('#endTimeField').val("");
                $(this).dialog('close');
            }
        }
    });

	// datepicker
	$('#timesheet-beginDate, #timesheet-endDate').datepicker({
				changeMonth : true,
				changeYear : true,
				showOn : 'button',
				showAnim : 'fadeIn',
				buttonImage : 'kr/static/images/cal.gif',
				buttonImageOnly : true,
				buttonText : 'Choose a date',
				showButtonPanel : true,
				numberOfMonths : 2,
				// set default month based on the current browsing month
				// appendText : '<br/>format: mm/dd/yyyy',
				constrainInput : false,
				minDate : -7,
				maxDate : +7
			});

	// select All
	$('#selectAll').click(function() {
		var checked_status = this.checked;
		$("input[name=selectedEmpl]").each(function() {
			this.checked = checked_status;
			$("input[name=selectedEmpl]").parent().parent()
					.addClass("ui-state-focus");
		});

		if (checked_status == false) {
			$("input[name=selectedEmpl]").parent().parent()
					.removeClass("ui-state-focus");
		}
	});

	// highlight row
	$("input[name=selectedEmpl]").click(function() {

				var checked_status = this.checked;

				if (checked_status) {
					$(this).parent().parent().addClass("ui-state-focus");
				} else {
					$(this).parent().parent().removeClass("ui-state-focus");
				}
			});

	// clock
	var options = {
		format : '%I:%M:%S %p'
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

//    console.log(lastClockedInTime);
//    console.log(clockAction);

    var startTime = clockAction == 'CO' ?  new Date(lastClockedInTime) : new Date() ;

//    console.log(startTime);

	$('.elapsedTime').countdown({
				since : startTime,
				compact : true,
				format : 'dHMS',
				description : ''
	})

	// tooltip
    // http://flowplayer.org/tools/tooltip/index.html
	$("#beginTimeHelp, #endTimeHelp").tooltip({

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

	// note
	$("#note").accordion({
		collapsible : true,
		active : 2
	});

    // summary table
    $('a#basic').click(function(){
        var options = {};
        $('#timesheet-table-basic').fadeIn();
        $('#timesheet-table-advance').hide();
    });

    $('a#advance').click(function(){
        var options = {};
        $('#timesheet-table-advance').fadeIn();
        $('#timesheet-table-basic').hide();
    });

    // demo
    $("#tabs-demo").tabs();

    // apply time entry widget to the tabular view
    $(".timesheet-table-week1 :input, .timesheet-table-week2 :input").blur(function(){
        magicTime(this);
    }).focus(function(){
        if(this.className != 'error') this.select();
    });

    // earn code
    $("select#earnCode").change(function(){

		$('#hoursField').attr('readonly',false).css('background',"white").val("");

        if($(this).val() == 'SCK' || $(this).val() == 'VAC') {
			$('#beginTimeField,#endTimeField').val("");
            $('#clockIn, #clockOut').hide();
            $('#hours').show();

            if($(this).val() == 'SCK') {
                $('#hoursField').val('8');
                $('#hoursField').attr('readonly',true).css('background',"#EEEEEE");
            }
        }
        else {
            $('#hours').val("");
            $('#clockIn, #clockOut').show();
            $('#hours').hide();
        }

        $("select#earnCode option[value='" + $(this).val() +"']").attr("selected", "selected");
    });

});

$.fn.parseTime= function() {
    var parsedTime = new Array();
    var timeAndAmPm = $(this).val().split(" ");
    var time = timeAndAmPm[0].split(":");

    if(timeAndAmPm[1] == 'PM') {
        parsedTime['hour'] = Number(time[0]) == 12 ? time[0] : Number(time[0]) + 12;
    }
    else {
        parsedTime['hour'] = Number(time[0]);
    }

    parsedTime['minute'] = Number(time[1]);

    return parsedTime;
}