$(function () {
    /**
     * ====================
     * Settings
     * ====================
     */

        // The default template variable is <%= var %>, but it conflicts with jsp, so we use <@= var @> instead
    _.templateSettings = {
        interpolate : /\<\@\=(.+?)\@\>/gim,
        evaluate : /\<\@(.+?)\@\>/gim
    };

    /**
     * ====================
     * Models
     * ====================
     */

    /**
     * Leave Block
     */
        // Create a leave block model
    LeaveBlock = Backbone.Model;

    // Create a leave block collection that holds multiple time blocks. This is essentially a list of hashmaps.
    LeaveBlockCollection = Backbone.Collection.extend({
        model : LeaveBlock
    });
    
    // Convert the leave block json string to a json object
    var leaveBlockJson = jQuery.parseJSON($("#leaveBlockString").val());
    var leaveBlockCollection = new LeaveBlockCollection(leaveBlockJson);

//    LeaveCode = Backbone.Model.extend({
//    	 url : "LeaveCalendarWS.do?methodToCall=getLeaveCodeInfo"
//    });
//  
//    var leaveCodeObj = new LeaveCode;
    
    //EarnCode = Backbone.Model.extend({
   	// url : "LeaveCalendarWS.do?methodToCall=getEarnCodeInfo"
    //});

    /**
     * Earn Code
     */
        // Create an earn code model
    EarnCode = Backbone.Model;

    // Create a collection for earn codes
    EarnCodeCollection = Backbone.Collection.extend({
        model : EarnCode,
        url : "LeaveCalendarWS.do?methodToCall=getEarnCodeJson"
    });

    var EarnCodes = new EarnCodeCollection;

    var earnCodeObj = new EarnCode;

    /**
     * ====================
     * Views
     * ====================
     */

    // create a timeblock view
    var LeaveBlockView = Backbone.View.extend({
        // Set the element that our dialog form wants to bind to. This setting is necessary.
        el : $("body"),

        // Events are in the jQuery format.
        // The first part is an event and the second part is a jQuery selector.
        // Check out this page for more information about the jQuery selectors : http://api.jquery.com/category/selectors/
        events : {
        	"click div[id*=show]" : "showTimeBlock", // KPME-1447
            "click .create" : "showLeaveBlockEntryDialog",
            "click img[id^=leaveBlockDelete]" : "deleteLeaveBlock",
            "click .leaveBlock" : "doNothing",
            "change #selectedEarnCode" : "showFieldByEarnCodeType",
            "keypress #selectedEarnCode" : "showFieldByEarnCodeType",
            "change #selectedAssignment" : "changeAssignment",
            "keypress #selectedAssignment" : "changeAssignment"
        },

        initialize : function () {

            // This step binds the functions to the view so you can call the methods like : this.addOneEarnCode
            _.bindAll(this, "addAllEarnCodes", "render");

            // Bind the events onto the earncode collection, so these events will be triggered
            // when an earncode collection is created.
            EarnCodes.bind('add', this.addAllEarnCodes);
            EarnCodes.bind('reset', this.addAllEarnCodes);

//            OvertimeEarnCodes.bind('add', this.addAllOvertimeEarnCodes);
//            OvertimeEarnCodes.bind('reset', this.addAllOvertimeEarnCodes);
        },

        render : function () {
            // If there is anything you want to render when the view is initiated, place them here.
            // A good convention is to return this at the end of render to enable chained calls.
            return this;
        },

        doNothing : function (e) {
            e.stopPropagation();
        },

        formatTime : function (e) {

            var id = (e.target || e.srcElement).id;
            var value = (e.target || e.srcElement).value;
            // Use Datejs to parse the value
            var dateTime = Date.parse(value);
            if (_.isNull(dateTime)) {
                // Date.js returns null if it couldn't understand the format from user's input.
                // If that's the case, clear the values on the form and make the border red.
                $("#" + id).addClass("block-error").val("");
                $("#" + id.split("Hour")[0]).val("");
            } else {
                // Remove the red border if user enters something
                $("#" + id).removeClass("block-error").val("");
            }
            // This magic line first finds the element by the id.
            // Uses Datejs (a 3rd party js lib) to parse user's input and update the value by the specifed format.
            // See the list of the formats in tk.js.
            $("#" + id).val(dateTime.toString(CONSTANTS.TIME_FORMAT.TIME_FOR_OUTPUT));

            // set the value to the military format on a different field for further timeblock actions.
            $("#" + id.split("Hour")[0]).val(dateTime.toString(CONSTANTS.TIME_FORMAT.TIME_FOR_SYSTEM));
        },

        showLeaveBlockEntryDialog : function (startDate, endDate) {
            // check user permmissions before opening the dialog.
            var isValid = this.checkPermissions(startDate, endDate);
            var self = this;
            if (isValid) {
              $("#dialog-form").dialog({
                title : "Add Leave Blocks : ",
                closeOnEscape : true,
                autoOpen : true,
                height : 'auto',
                width : '450',
                modal : true,
                open : function () {
                    // Set the selected date on start/end time fields
                    // This statmement can tell is showTimeEntryDialog() by other methods or triggered directly by backbone.

                    if (!_.isUndefined(startDate) && !_.isUndefined(endDate)) {
                        $("#startDate").val(startDate);
                        $("#endDate").val(endDate);
                    } else {
                        // If this is triggered directly by backbone, i.e. user clicked on the white area to create a new timeblock,
                        // Set the date by grabbing the div id.
                        var currentDay = new Date(beginPeriodDateTimeObj);
                        var targetDay = currentDay.addDays(parseInt((startDate.target.id).split("_")[1]));
                        $("#startDate, #endDate").val(Date.parse(targetDay).toString(CONSTANTS.TIME_FORMAT.DATE_FOR_OUTPUT));
                       
                        // Check if there is only one assignment
                        // Placing this code block here will prevent fetching earn codes twice
                        // when showTimeEntryDialog() is called by showTimeBlock()
//                            if ($("#selectedAssignment").is("input")) {
//                                var dfd = $.Deferred();
//                                dfd.done(self.fetchEarnCode(_.getSelectedAssignmentValue()))
//                                        .done(self.showFieldByEarnCodeType());
//                            }
                    }
                    if ($("#selectedAssignment").is("input")) {
                        var dfd = $.Deferred();
                        dfd.done(self.fetchEarnCode(_.getSelectedAssignmentValue()))
                            .done(self.showFieldByEarnCodeType());
                    }


                },
                close : function () {
                	$('.cal-table td').removeClass('ui-selected');
                    //reset values on the form
                    self.resetLeaveBlockDialog($("#timesheet-panel"));
                    self.resetState($("#dialog-form"));
                },
                buttons : {
                    "Add" : function () {
                        /**
                         * In case we have more needs to auto-adjust user's input, we should consider moving them to a separate method.
                         */
                        if (!_.isEmpty($("#endTime").val())) {
                            // If the end time is 12:00 am, change the end date to the next day
                            var midnight = Date.parse($('#endDate').val()).set({
                                hour : 0,
                                minute : 0,
                                second : 0,
                                millisecond : 0
                            });

                            // Parse the date by the format like 1/2/2011 8:0
                            var endDateTime = Date.parse($('#endDate').val() + " " + $('#endTime').val());

                            // Compare and see if the end time is at midnight
                            if (Date.compare(midnight, endDateTime) == 0) {
                                $('#endDate').val(endDateTime.add(1).days().toString(CONSTANTS.TIME_FORMAT.DATE_FOR_OUTPUT));
                            }
                        }
                        var isValid = true;
                        $('#acrossDays').val($('#acrossDays').is(':checked') ? 'y' : 'n');
                        $('#spanningWeeks').val($('#spanningWeeks').is(':checked') ? 'y' : 'n');  // KPME-1446
                        isValid = self.validateLeaveBlock();
                        if (!_.isEmpty($("#leaveBlockId").val())) {
                        	$('#methodToCall').val(CONSTANTS.ACTIONS.UPDATE_LEAVE_BLOCK);	
                        } else {
                        	$("#methodToCall").val(CONSTANTS.ACTIONS.ADD_LEAVE_BLOCK);
                        }
                        
                        if (isValid) {
                            $('#leaveBlock-form').submit();
                            $(this).dialog("close");
                        }
                    },
                    Cancel : function () {
                    	$('.cal-table td').removeClass('ui-selected');
                        $(this).dialog("close");

                    }
                }
             });
            }
        },


        showTimeBlock : function (e) {
            var key = _(e).parseEventKey();
            
            // Retrieve the selected leaveblock
            var leaveBlock = leaveBlockCollection.get(key.id);
            
            // We only have one date in leave block
            //this.showTimeEntryDialog(leaveBlock.get("startDate"), leaveBlock.get("endDate"));
            this.showLeaveBlockEntryDialog(leaveBlock.get("leaveDate"), leaveBlock.get("leaveDate"));
            _.replaceDialogButtonText("Add", "Update");
            
            // Deferred is a jQuery method which makes sure things happen in the order you want.
            // For more informaiton : http://api.jquery.com/category/deferred-object/
            // Here we want to fire the ajax call first to grab the earn codes.
            // After that is done, we fill out the form and make the entry field show / hide based on the earn code type.
            var dfd = $.Deferred();
            dfd.done($("#selectedEarnCode option[value='" + leaveBlock.get("earnCodeId") + "']").attr("selected", "selected"))
            .done(_(leaveBlock).fillInForm());
        },
        
        deleteLeaveBlock : function (e) {
            var key = _(e).parseEventKey();
//            var timeBlock = timeBlockCollection.get(key.id);
            
//            if (this.checkPermissions()) {
            	if (confirm('You are about to delete a leave block. Click OK to confirm the delete.')) {
            		window.location = "LeaveCalendar.do?methodToCall=deleteLeaveBlock&leaveBlockId=" + key.id;
            	}
//            }
        },
        
        /**
         * Reset the values on the leaveblock entry form.
         * @param fields
         */
        resetLeaveBlockDialog : function (timeBlockDiv) {
        	 $("#leaveAmount").val("");
        	 $("#selectedEarnCode").val("");
        	 $("#description").val("");
        	 $("#leaveBlockId").val("");
        },
        
        /**
         * Remove the error class from the given fields.
         * @param fields
         */
        resetState : function (fields) {
            // Remove the error / warning texts
            $("#validation").text("").removeClass("error-messages");

            // Remove the error classs
            $("[class*=error]", fields).each(function () {
                $(this).removeClass("ui-state-error");
            });

            // Remove the sylte for multi-day selection
            $('.cal-table td').removeClass('ui-selected');

            // Remove all the readonly / disabled states
            $("input, select", "#timesheet-panel").attr("disabled", false);
            $("input, select", "#timesheet-panel").attr("readonly", false);

            // This is mainly to solve the issue where the change event on the assignment was unbound
            // when the user can only change the assignment on the timeblock.
            // Reset the events by calling the built-in delegateEvents function.
            this.delegateEvents(this.events);

            // show date pickers
            $(".ui-datepicker-trigger").show();
        },

        changeAssignment : function () {
            this.fetchEarnCodeAndLoadFields();
        },

        fetchEarnCodeAndLoadFields : function () {
            var dfd = $.Deferred();
            dfd.done(this.fetchEarnCode(_.getSelectedAssignmentValue()))
                .done(this.showFieldByEarnCodeType());
        },

        validateEarnCode : function () {
            var isValid = true;
            isValid = isValid && this.checkEmptyField($("#selectedEarnCode option:selected"), "Earn Code");

            // couldn't find an easier way to get the earn code json, so we validate by the field id
            // The method below will get a list of not hidden fields' ids
            var ids = $("#dialog-form input").not(":hidden").map(
                    function () {
                        return this.id;
                    }).get();
             if (_.contains(ids, "leaveAmount")) {
                var hours = $('#leaveAmount');
                isValid = isValid && (this.checkEmptyField(hours, "Leave amount")  && this.checkRegexp(hours, '/0/', 'Leave amount cannot be zero'));
                if(isValid) {
                	var type = this.getEarnCodeUnit(earnCodeObj.toJSON());
                	if (type == 'D') {
                		isValid = isValid && (this.checkRangeValue(hours, 1, "Leave amount Days"));
                	} else if (type == 'H') {
                		isValid = isValid && (this.checkRangeValue(hours, 24, "Leave amount Hours"));
                	}
                	var fieldLength = 0;
                	var fracLength = 0;
                	// check fraction digit
                	if(isValid) {
                		var fraction = this.getEarnCodeFractionalAllowedTime(earnCodeObj.toJSON());
                		if(typeof fraction != 'undefined' && fraction != '') {
                			var fractionAr = fraction.split(".");
                			var hoursAr = hours.val().split(".");
                			if(hoursAr.length > 1) {
                				fieldLength = hoursAr[1].length;
                			}
                			if(fractionAr.length > 1) {
                				fracLength = fractionAr[1].length;
                			}
//                			alert('field length' +fieldLength);
//                			alert('field length' +fracLength);
                			if(fieldLength > fracLength) {
                				isValid = false;
                				this.displayErrorMessages("Leave Amount field should be in the format of "+fraction);
                			}
                		}
                	}
                }
            }
            return isValid;
        },

        fetchEarnCode : function (e, isTimeBlockReadOnly) {

            isTimeBlockReadOnly = _.isUndefined(isTimeBlockReadOnly) ? false : isTimeBlockReadOnly;

            // When the method is called with a passed in value, the assignment is whatever that value is;
            // If the method is called WITHOUT a passed in value, the assignment is an event.
            // We want to be able to use this method in creating and editing timeblocks.
            // If assignment is not a string, we'll grab the selected assignment on the form.
            var assignment = _.isString(e) ? e : this.$("#selectedAssignment option:selected").val();
            var startDate = this.$("#startDate").val();

            // Fetch earn codes based on the selected assignment
            // The fetch function is provided by backbone.js which also supports jQuery.ajax options.
            // For more information: http://documentcloud.github.com/backbone/#Collection-fetch
            EarnCodes.fetch({
                // Make the ajax call not async to be able to mark the earn code selected
                async : false,
                data : {
                    selectedAssignment : assignment,
                    startDate : startDate,
                    timeBlockReadOnly : isTimeBlockReadOnly
                }
            });
        },


        addAllEarnCodes : function () {
            var view = new EarnCodeView({collection : EarnCodes});
            // Append the earn code to <select>
            $("#earnCode-section").append(view.render().el);

        },

        changeEarnCode : function(e) {
            // validate leaveblocks
        	var earnCodeString = _.isString(e) ? e : this.$("#selectedEarnCode option:selected").val();
        	earnCodeObj.fetch({
                // Make the ajax call not async to be able to mark the earn code selected
                async : false,
                data : {
                	selectedEarnCode : earnCodeString
                }  
            });
        	this.showFieldByEarnCodeType();
        },
        
        showFieldByEarnCode : function() {
        	var key = $("#selectedEarnCode option:selected").val();
        	var type = key.split(":")[1];
        	if (type == 'D') {
        		$('#unitOfTime').text('* Days');
        	} else if (type == 'H') {
        		$('#unitOfTime').text('* Hours');
        	}
        },
        
        
        showFieldByEarnCodeType : function () {
            var earnCodeType = this.getEarnCodeUnit(earnCodeObj.toJSON());
            if (earnCodeType == 'D') {
        		$('#unitOfTime').text('* Days');
        	} else if (earnCodeType == 'H') {
        		$('#unitOfTime').text('* Hours');
        	}
            var unitOfTime = this.getEarnCodeDefaultTime(earnCodeObj.toJSON());
            $('#leaveAmount').val(unitOfTime);
            
        },
        
        getEarnCodeUnit : function (earnCodeJson) {
           return earnCodeJson.unitOfTime;
        },
        
        getEarnCodeDefaultTime : function (earnCodeJson) {
           return earnCodeJson.defaultAmountofTime;
        },
        
        getEarnCodeFractionalAllowedTime : function (earnCodeJson) {
           return earnCodeJson.fractionalTimeAllowed;
        },

        validateLeaveBlock : function () {
            var self = this;
            var isValid = true;
//            isValid = isValid && this.checkEmptyField($("#selectedAssignment"), "Assignment");
            isValid = isValid && this.validateEarnCode();
            
            if (isValid) {
           
                var docId = $('#documentId').val();
                var params = {};
                params['startDate'] = $('#startDate').val();
                params['endDate'] = $('#endDate').val();
                params['leaveAmount'] = $('#leaveAmount').val();
                params['selectedEarnCode'] = $('#selectedEarnCode option:selected').val();
                params['spanningWeeks'] = $('#spanningWeeks').is(':checked') ? 'y' : 'n'; // KPME-1446
                params['leaveBlockId'] = $('#leaveBlockId').val();

                // validate leaveblocks
                $.ajax({
                    async : false,
                    url : "LeaveCalendarWS.do?methodToCall=validateLeaveEntry&documentId=" + docId,
                    data : params,
                    cache : false,
                    type : "post",
                    success : function (data) {
                        //var match = data.match(/\w{1,}|/g);
                        var json = jQuery.parseJSON(data);
                        // if there is no error message, submit the form to add the time block
                        if (json.length == 0) {
                            return true;
                        }
                        else {
                            // if there is any error, grab error messages (json) and display them
                            var json = jQuery.parseJSON(data);
                            var errorMsgs = '';
                            $.each(json, function (index) {
                                errorMsgs += "Error : " + json[index] + "<br/>";
                            });

                            self.displayErrorMessages(errorMsgs);
                            isValid = false;
                        }
                    },
                    error : function () {
                        self.displayErrorMessages("Error: Can't save data.");
                        isValid = false;
                    }
                });
            }
            
            return isValid;
        },
        
        checkLength : function (o, n, min, max) {
            if (o.val().length > max || o.val().length < min) {
                this.displayErrorMessages(n + " field cannot be empty");
                return false;
            }
            return true;
        },

        checkEmptyField : function (o, field) {
            var val = o.val();
            if (val == '' || val == undefined) {
                this.displayErrorMessages(field + " field cannot be empty", o);
                return false;
            }
            return true;
        },

        checkRegexp : function (o, regexp, n) {
            if (( o.val().match(regexp) )) {
                this.displayErrorMessages(n);
                return false;
            }
            return true;
        },

        checkSpecificValue : function (o, value, n) {
            if (o.val() != value) {
                this.displayErrorMessages(n);
                return false;
            }
            return true;
        },
        
        checkRangeValue : function (o, value1, field) {
            if (o.val() > value1) {
            	this.displayErrorMessages(field + " field should not exceed " + value1, o);
            	return false;
            }
            return true;
        },

        displayErrorMessages : function (t, object) {
            // add the error class ane messages
            $('#validation').html(t)
                    .addClass('error-messages');

            // highlight the field
            if (!_.isUndefined(object)) {
                object.addClass('ui-state-error');
            }
        },
        
        checkPermissions : function(startDate, endDate) {
			var isValid = false;
			var curStartDate = $("#currentPayCalStartDate").val();
			var curEndDate = $("#currentPayCalEndDate").val();
			var currentDay = new Date(beginPeriodDateTimeObj);
			var targetDay;
			if(!_.isUndefined(startDate) && !_.isUndefined(endDate)) {
    			targetDay = new Date(startDate);
            } else {
            	targetDay = currentDay.addDays(parseInt((startDate.target.id).split("_")[1]));
            }
			
			var clickDate = new Date(targetDay).getTime();
			// Can't add a new timeblock is the doc is not editable.
			if ($('#docEditable').val() == "false") {
				if(!_.isUndefined(curStartDate) && !_.isUndefined(curEndDate)){
					   var sd = new Date(curStartDate).getTime();
					   var ed = new Date(curEndDate).getTime();
						if(!_.isUndefined(clickDate) && (clickDate >= sd) && (clickDate <= ed)) {
							isValid = true;
						} else {
							isValid = false;
						}
				} 
			} else {
				isValid = true;
			}
			if(!isValid) {
			 $('.cal-table td').removeClass('ui-selected');
			}
			return isValid;
		}

    });

    var EarnCodeView = Backbone.View.extend({
        el : $("#selectedEarnCode"),

        template : _.template($('#earnCode-template').html()),

        initialize : function () {
            _.bindAll(this, "render");
        },

        render : function () {
            var self = this;
            $("#selectedEarnCode").html("");
            this.collection.each(function (earnCode) {
                $(self.el).append(self.template(earnCode.toJSON()));
            });

            return this;
        }
    });

    // Initialize the view. This is the kick-off point.
    var app = new LeaveBlockView;

    /**
     * Custom util functions.
     * This is the section where you can create your own util methods and inject them to Underscore.
     *
     * The usage of the custom functions is like _(event.target.id).parseEventKey();
     */
    _.mixin({
        /**
         * Parse the div id to get the timeblock id and the action.
         * @param event
         */
        parseEventKey : function (e) {
            var id = (e.target || e.srcElement).id;

            return {
                action : id.split("_")[0],
                id : id.split("_")[1]
            };
        },
        /**
         * Fill in the leave entry form by the leaveblock
         * @param leaveBlock
         */
        fillInForm : function (leaveBlock) {
            $('#startDate').val(leaveBlock.get("leaveDate"));
            $('#endDate').val(leaveBlock.get("leaveDate"));
            $('#leaveAmount').val(leaveBlock.get("leaveAmount"));
            $('#leaveBlockId').val(leaveBlock.get("lmLeaveBlockId"));
            $("#selectedAssignment option[value='" + leaveBlock.get("assignment") + "']").attr("selected", "selected");
            $("#selectedEarnCode option[value='" + leaveBlock.get("earnCode") + "']").attr("selected", "selected");
            $('#description').val(leaveBlock.get("description"));
            if (leaveBlock.get("editable") == false) {
                $('#startDate').attr('disabled', 'disabled');
                $('#endDate').attr('disabled', 'disabled');
                $('#selectedAssignment').attr('disabled', 'disabled');
                $('#selectedEarnCode').attr('disabled', 'disabled');
                $('#leaveAmount').attr('disabled', 'disabled');
                $('#leaveBlockId').attr('disabled', 'disabled');
                $('#description').attr('disabled', 'disabled');
                $('#spanningWeeks').attr('disabled', 'disabled');
            }
        },
        /**
         * Provides a helper method to change the button name on the time entry dialog.
         * @param oriText
         * @param newText
         */
        replaceDialogButtonText : function (oriText, newText) {
            $(".ui-button-text:contains('" + oriText + "')").text(newText);
        },

        /**
         * The selected assignment field can be a hidden text field if there is only one assignment, or a dropdown if there are multiple assignments
         * This helper method will check which type of field is presented and return the value
         */
        getSelectedAssignmentValue : function () {
            var $selectedAssignment = $("#selectedAssignment")
            if ($selectedAssignment.is("input")) {
                return $selectedAssignment.val();
            } else {
                return $("#selectedAssignment option:selected").val();
            }
        }
    });

    /**
     * Make the calendar cell selectable
     */
    // When making a mouse selection, it creates a "lasso" effect which we want to get rid of.
    // In the future version of jQuery UI, lasso is going to one of the options where it can be enabled / disabled.
    // For now, the way to disable it is to modify the css.
    //
    // .ui-selectable-helper { border:none; }
    //
    // This discussion thread on stackoverflow was helpful:
    // http://bit.ly/fvRW4X

    var selectedDays = [];
    var selectingDays = [];
    var beginPeriodDateTimeObj = $('#beginPeriodDateTime').val() !== undefined ? new Date($('#beginPeriodDateTime').val()) : d + '/' + m + '/' + y;
    var endPeriodDateTimeObj = $('#endPeriodDateTime').val() !== undefined ? new Date($('#endPeriodDateTime').val()) : d + '/' + m + '/' + y;

    $(".cal-table").selectable({
        filter : "td",
        distance : 1,
        selected : function (event, ui) {
            selectedDays.push(ui.selected.id);
        },
        selecting : function (event, ui) {
            // get the index number of the selected td
            $(".ui-selecting", this).each(function () {
                selectingDays.push($(".cal-table td").index(this));
            });

        },

        stop : function (event, ui) {
            var currentDay = new Date(beginPeriodDateTimeObj);
            var startDay = new Date(currentDay);
            var endDay = new Date(currentDay);

            startDay.addDays(parseInt(_.first(selectedDays).split("_")[1]));
            endDay.addDays(parseInt(_.last(selectedDays).split("_")[1]));

            startDay = Date.parse(startDay).toString(CONSTANTS.TIME_FORMAT.DATE_FOR_OUTPUT);
            endDay = Date.parse(endDay).toString(CONSTANTS.TIME_FORMAT.DATE_FOR_OUTPUT);
            app.showLeaveBlockEntryDialog(startDay, endDay);

            // https://uisapp2.iu.edu/jira-prd/browse/TK-1593
//            if ($("#selectedAssignment").is("input")) {
//                app.fetchEarnCodeAndLoadFields();
//            }
            
            selectedDays = [];
        }
    });

	
	
});
