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
            "click .create" : "showLeaveBlockEntryDialog",
            "click img[id^=leaveBlockDelete]" : "deleteLeaveBlock"
        },

        initialize : function () {

            // This step binds the functions to the view so you can call the methods like : this.addOneEarnCode
//            _.bindAll(this, "addAllEarnCodes", "addAllOvertimeEarnCodes", "render");

            // Bind the events onto the earncode collection, so these events will be triggered
            // when an earncode collection is created.
//            EarnCodes.bind('add', this.addAllEarnCodes);
//            EarnCodes.bind('reset', this.addAllEarnCodes);

//            OvertimeEarnCodes.bind('add', this.addAllOvertimeEarnCodes);
//            OvertimeEarnCodes.bind('reset', this.addAllOvertimeEarnCodes);
        },

        render : function () {
            // If there is anything you want to render when the view is initiated, place them here.
            // A good convention is to return this at the end of render to enable chained calls.
            return this;
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
            // var isValid = this.checkPermissions();

            var self = this;
//            if (isValid) {
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
                        $("#startDate, #endDate").val(startDate.target.id);
                        // Check if there is only one assignment
                        // Placing this code block here will prevent fetching earn codes twice
                        // when showTimeEntryDialog() is called by showTimeBlock()
//                            if ($("#selectedAssignment").is("input")) {
//                                var dfd = $.Deferred();
//                                dfd.done(self.fetchEarnCode(_.getSelectedAssignmentValue()))
//                                        .done(self.showFieldByEarnCodeType());
//                            }
                    }

                },
                close : function () {
                    // reset values on the form
//                        self.resetTimeBlockDialog($("#timesheet-panel"));
//                        self.resetState($("#dialog-form"));
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

                        $('#acrossDays').val($('#acrossDays').is(':checked') ? 'y' : 'n');

                        $('#methodToCall').val(CONSTANTS.ACTIONS.ADD_LEAVE_BLOCK);
                        $('#leaveBlock-form').submit();
                        $(this).dialog("close");

                    },
                    Cancel : function () {
                        $(this).dialog("close");

                    }
                }
            });
//            }
        },

        deleteLeaveBlock : function (e) {
            var key = _(e).parseEventKey();
            console.log(key);
//            var timeBlock = timeBlockCollection.get(key.id);

//            if (this.checkPermissions()) {
                if (confirm('You are about to delete a leave block. Click OK to confirm the delete.')) {
                    window.location = "LeaveCalendar.do?methodToCall=deleteLeaveBlock&leaveBlockId=" + key.id;
                }
//            }
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
         * Fill in the time entry form by the timeblock
         * @param timeBlock
         */
        fillInForm : function (timeBlock) {
            $('#startDate').val(timeBlock.get("startDate"));
            $('#startTime').val(timeBlock.get("startTime"));
            $('#startTimeHourMinute').val(timeBlock.get("startTimeHourMinute"));
            $('#endDate').val(timeBlock.get("endDate"));
            $('#endTime').val(timeBlock.get("endTime"));
            $('#endTimeHourMinute').val(timeBlock.get("endTimeHourMinute"));
            $("#selectedAssignment option[value='" + timeBlock.get("assignment") + "']").attr("selected", "selected");
            $('#tkTimeBlockId').val(timeBlock.get("tkTimeBlockId"));
            $('#hours').val(timeBlock.get("hours"));
            $('#amount').val(timeBlock.get("amount"));
            $('#lunchDeleted').val(timeBlock.get("lunchDeleted"));

            if ($('#isVirtualWorkDay').val() == 'true') {
                var endDateTime = Date.parse($('#endDate').val() + " " + $('#endTime').val());
                $('#endDate').val(endDateTime.add(-1).days().toString(CONSTANTS.TIME_FORMAT.DATE_FOR_OUTPUT));
            }
        }
        /**
         * Provides a helper method to change the button name on the time entry dialog.
         * @param oriText
         * @param newText
         */
//        replaceDialogButtonText : function (oriText, newText) {
//            $(".ui-button-text:contains('" + oriText + "')").text(newText);
//        }
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

//    var selectedDays = [];
//    var selectingDays = [];
//    var beginPeriodDateTimeObj = $('#beginPeriodDate').val() !== undefined ? new Date($('#beginPeriodDate').val()) : d + '/' + m + '/' + y;
//    var endPeriodDateTimeObj = $('#endPeriodDate').val() !== undefined ? new Date($('#endPeriodDate').val()) : d + '/' + m + '/' + y;
//
//    $(".cal-table").selectable({
//        filter : "td",
//        distance : 1,
//        selected : function (event, ui) {
//            selectedDays.push(ui.selected.id);
//        },
//        selecting : function (event, ui) {
//            // get the index number of the selected td
//            $(".ui-selecting", this).each(function () {
//                selectingDays.push($(".cal-table td").index(this));
//            });
//
//        },
//
//        stop : function (event, ui) {
//            var currentDay = new Date(beginPeriodDateTimeObj);
//            var startDay = new Date(currentDay);
//            var endDay = new Date(currentDay);
//
//            startDay.addDays(parseInt(_.first(selectedDays).split("_")[1]));
//            endDay.addDays(parseInt(_.last(selectedDays).split("_")[1]));
//
//            startDay = Date.parse(startDay).toString(CONSTANTS.TIME_FORMAT.DATE_FOR_OUTPUT);
//            endDay = Date.parse(endDay).toString(CONSTANTS.TIME_FORMAT.DATE_FOR_OUTPUT);
//
//            app.showTimeEntryDialog(startDay, endDay);
//
//            // https://uisapp2.iu.edu/jira-prd/browse/TK-1593
//            if ($("#selectedAssignment").is("input")) {
//                app.fetchEarnCodeAndLoadFields();
//            }
//
//            selectedDays = [];
//        }
//    });
//
//    if ($('#docEditable').val() == 'false') {
//        $(".cal-table").selectable("destroy");
//    }
});