$(function () {
    /**
     *
     * README:
     *
     * TODO:
     * 1) explain how the flow works
     *
     * Notes:
     * 1) When editing a timeblock, why didn't we use a template?
     *
     *    A Underscore template is surrounded by <script type="text/template">.
     *    The advantage of using that is we can use the custom syntax : <%= foo %>
     *    to fill out the values in the template, e.g.
     *
     *    this.$('#timesheet-panel').html(this.timeEntrytemplate({
     *      foo = "test"
     *    });
     *
     *    This approach works fine if there is no extra javascript in the template.
     *    In our case, we have a jQuery datepicker and an hour entering helper on
     *    the form. They won't work in the template since a template is considered
     *    as a big chunk of text.
     *
     * 2) We don't use Backbone's router here. See the reasons here: http://bit.ly/Ajbnog
     */

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
     * Timeblock
     */
        // Create a time block model
    TimeBlock = Backbone.Model;

    // Create a time block collection that holds multiple time blocks. This is essentially a list of hashmaps.
    TimeBlockCollection = Backbone.Collection.extend({
        model : TimeBlock
    });

    // Convert the time block json string to a json object
    var timeBlockJson = jQuery.parseJSON($("#timeBlockString").val());
    // Cass in the json object to create a Backbone time block collection
    var timeBlockCollection = new TimeBlockCollection(timeBlockJson);

    /**
     * Earn Code
     */
        // Create an earn code model
    EarnCode = Backbone.Model;

    // Create a collection for earn codes
    EarnCodeCollection = Backbone.Collection.extend({
        model : EarnCode,
        url : "TimeDetailWS.do?methodToCall=getEarnCodeJson"
    });

    var EarnCodes = new EarnCodeCollection;

    /**
     * Overtime Earn Code
     */

        // create an overtime earn code model
    OvertimeEarnCode = Backbone.Model;

    // Create a collecton for overtime earn codes
    OvertimeEarnCodeCollection = Backbone.Collection.extend({
        model : OvertimeEarnCode,
        url : "TimeDetailWS.do?methodToCall=getOvertimeEarnCodes"
    });

    OvertimeEarnCodes = new OvertimeEarnCodeCollection;

    /**
     * ====================
     * Views
     * ====================
     */

        // create a timeblock view
    var TimeBlockView = Backbone.View.extend({
        // Set the element that our dialog form wants to bind to. This setting is necessary.
        el : $("body"),

        // Events are in the jQuery format.
        // The first part is an event and the second part is a jQuery selector.
        // Check out this page for more information about the jQuery selectors : http://api.jquery.com/category/selectors/
        events : {
            "click div[id*=show]" : "showTimeBlock",
            "click img[id*=delete]" : "deleteTimeBlock",
            // .create is the div that fills up the white sapce and .day-number is the div with the day number on it.
            // <div class="create"></div> is in calendar.tag.
            // We want to trigger the show event on any white space areas.
            "click .create, .day-number" : "showTimeEntryDialog",
            "click span[id*=overtime]" : "showOverTimeDialog",
            "blur #startTimeHourMinute, #endTimeHourMinute" : "formatTime",
            // TODO: figure out how to chain the events
            "change #selectedAssignment" : "fetchEarnCode",
            "keypress #selectedAssignment" : "fetchEarnCode",
            "change #selectedEarnCode" : "showFieldByEarnCodeType",
            "keypress #selectedEarnCode" : "showFieldByEarnCodeType"
        },

        initialize : function () {
            // This step binds the functions to the view so you can call the methods like : this.addOneEarnCode
            _.bindAll(this, "addAllEarnCodes", "addAllOvertimeEarnCodes", "render");

            // Bind the events onto the earncode collection, so these events will be triggered
            // when an earncode collection is created.
            EarnCodes.bind('add', this.addAllEarnCodes);
            EarnCodes.bind('reset', this.addAllEarnCodes);

            OvertimeEarnCodes.bind('add', this.addAllOvertimeEarnCodes);
            OvertimeEarnCodes.bind('reset', this.addAllOvertimeEarnCodes);
        },

        render : function () {
            // If there is anything you want to render when the view is initiated, place them here.
            // A good convention is to return this at the end of render to enable chained calls.
            return this;
        },

        formatTime : function (e) {

            var id = e.target.id;
            var value = e.target.value;
            // Use Datejs to parse the value
            var dateTime = Date.parse(value);
            // console.log(dateTime);
            if (_.isNull(dateTime)) {
                // Date.js returns null if it couldn't understand the format from user's input.
                // If that's the case, clear the values on the form and make the border red.
                $("#" + id).addClass("block-error").val("");
                $("#" + id.split("Hour")[0]).val("");
            }
            // This magic line first finds the element by the id.
            // Uses Datejs (a 3rd party js lib) to parse user's input and update the value by the specifed format.
            // See the list of the formats in tk.js.
            $("#" + id).val(dateTime.toString(CONSTANTS.TIME_FORMAT.TIME_FOR_OUTPUT));

            // set the value to the military format on a different field for further timeblock actions.
            $("#" + id.split("Hour")[0]).val(dateTime.toString(CONSTANTS.TIME_FORMAT.TIME_FOR_SYSTEM));
        },

        validateAndSubmitTimeBlock : function () {
            var bValid = true;
            bValid &= app.checkEmptyField($("#selectedAssignment"), "Assignment");
            bValid &= app.validateEarnCode();

            if (bValid) {
                //TODO // compare the original values with the modified ones. if there is no change, close the form instead of submitting it

                var docId = $('#documentId').val();
                // validate timeblocks
                $.ajax({
                    async : false,
                    url : "TimeDetailWS.do?methodToCall=validateTimeEntry&documentId=" + docId,
                    data : params,
                    cache : false,
                    success : function (data) {
                        //var match = data.match(/\w{1,}|/g);
                        var json = jQuery.parseJSON(data);
                        // if there is no error message, submit the form to add the time block
                        if (json.length == 0) {
                            $('#time-detail').submit();
                            $(this).dialog("close");
                        }
                        else {
                            // if there is any error, grab error messages (json) and display them
                            var json = jQuery.parseJSON(data);
                            var errorMsgs = '';
                            $.each(json, function (index) {
                                errorMsgs += "Error : " + json[index] + "\n";
                            });

                            app.updateTips(errorMsgs);
                            return false;
                        }
                    },
                    error : function () {
                        app.updateTips("Error: Can't save data.");
                        return false;
                    }
                });
            }
            return bValid;
        },

        showTimeEntryDialog : function (event) {

            // TODO: create a reset value method and call here.

            $("#dialog-form").dialog({
                autoOpen : true,
                height : 'auto',
                width : '450',
                modal : true,
                open : function () {
                    // Set the selected date on start/end time fields
                    if (!_.isUndefined(event)) {
                        $("#startDate, #endDate").val(event.target.id);
                    }
                },
                close : function () {
                    // reset values on the form
                    _.resetTimeBlockDialog($("#timesheet-panel"));
                },
                buttons : {
                    "Add" : function () {
                        /**
                         * In case we have more needs to auto-adjust user's input, we should consider move them to a separate method.
                         */

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

                        $('#acrossDays').val($('#acrossDaysField').is(':checked') ? 'y' : 'n');
                        $('#methodToCall').val(CONSTANTS.ACTIONS.ADD_TIME_BLOCK);
                        $('#time-detail').submit();
                        $(this).dialog("close");
                    },
                    Cancel : function () {
                        $(this).dialog("close");

                    }
                }
            });

            //this.addAllEarnCodes();
        },

        showOverTimeDialog : function (e) {
            var self = this;

            var key = _(e.target.id).parseEventKey();
            var timeBlock = timeBlockCollection.get(key.id);
            var currentOvertimePref = _.trim($("#" + e.target.id).text());
            var dfd = $.Deferred();
            // Fill in the values. See the note above regarding why we didn't use a template
            dfd.done(self.fetchOvertimeEarnCode())
                    .done(_(timeBlock).fillInForm())
                    .done($("#overtimePref option[value='" + currentOvertimePref + "']").attr("selected", "selected"));

            $("#overtime-section").dialog({
                autoOpen : true,
                height : 'auto',
                width : '450',
                modal : true,
                open : function () {
                },
                beforeClose : function () {
                    // TODO: create a method to reset the values instead of spreading this type of code all over the place.
                    _(new TimeBlock).fillInForm();
                },
                buttons : {
                    "Add" : function () {
                        $('#methodToCall').val(CONSTANTS.ACTIONS.ADD_TIME_BLOCK);
                        $('#time-detail').submit();
                        $(this).dialog("close");
                    },
                    Cancel : function () {
                        $(this).dialog("close");

                    }
                }
            });
        },

        showTimeBlock : function (e) {
            var key = _(e.target.id).parseEventKey();
            this.showTimeEntryDialog();
            // Retrieve the selected timeblock
            var timeBlock = timeBlockCollection.get(key.id);
            // Deferred is a jQuery method which makes sure things happen in the order you want.
            // For more informaiton : http://api.jquery.com/category/deferred-object/
            // Here we want to fire the ajax call first to grab the earn codes.
            // After that is done, we fill out the form and make the entry field show / hide based on the earn code type.
            var dfd = $.Deferred();
            // Fill in the values. See the note above regarding why we didn't use a template
            dfd.done(this.fetchEarnCode(timeBlock.get("assignment")))
                    .done(_(timeBlock).fillInForm())
                    .done(this.showFieldByEarnCodeType());
        },

        deleteTimeBlock : function (e) {
            var key = _(e.target.id).parseEventKey();
            var timeBlock = timeBlockCollection.get(key.id);

            if (confirm('You are about to delete a time block. Click OK to confirm the delete.')) {
                window.location = "TimeDetail.do?methodToCall=deleteTimeBlock&documentId=" + timeBlock.get("documentId") + "&tkTimeBlockId=" + key.action;
            }
        },

        validateEarnCode : function () {
            var bValid = true;
            bValid &= app.checkEmptyField($("#earnCode"), "Earn Code");
            var earnCodeType = _.getEarnCodeType(EarnCodes.toJSON(), $("#selectedEarnCode option:selected").val());

            if (earnCodeType === CONSTANTS.EARNCODE_TYPE.TIME) {
                // the format has to be like "12:00 AM"
                bValid &= app.checkLength($('#startTime'), "Time entry", 8, 8);
                bValid &= app.checkLength($('#endTime'), "Time entry", 8, 8);
            }
            else if (earnCodeType === CONSTANTS.EARNCODE_TYPE.HOUR) {
                var hours = $('#hoursField');
                bValid &= app.checkEmptyField(hours, "Hour") && app.checkMinLength(hours, "Hour", 1) && app.checkRegexp(hours, '/0/', 'Hours cannot be zero');
            }
            else {
                var amount = $('#amountField');
                bValid &= app.checkEmptyField(amount, "Amount") && app.checkMinLength(amount, "Amount", 1) && app.checkRegexp(amount, '/0/', 'Amount cannot be zero');
            }
            return bValid;
        },

        fetchEarnCode : function (event) {
            // When the method is called with a passed in value, the assignment is whatever that value is;
            // If the method is called WITHOUT a passed in value, the assignment is an event.
            // We want to be able to use this method in creating and editing timeblocks.
            // If assignment is not a string, we'll grab the selected assignment on the form.
            var assignment = _.isString(event) ? event : this.$("#selectedAssignment option:selected").val();

            // Fetch earn codes based on the selected assignment
            // The fetch function is provided by backbone.js which also supports jQuery.ajax options.
            // See here for more information: http://documentcloud.github.com/backbone/#Collection-fetch
            this.$("#selectedEarnCode").html("");
            EarnCodes.fetch({
                // Make the ajax call not async to be able to mark the earn code selected
                async : false,
                data : {
                    selectedAssignment : assignment
                }
            });
        },


        addAllEarnCodes : function () {
            var view = new EarnCodeView({collection : EarnCodes});
            // Append the earn code to <select>
            $("#earnCode-section").append(view.render().el);

        },

        fetchOvertimeEarnCode : function () {
            // Fetch earn codes based on the selected assignment
            // The fetch function is provided by backbone.js which also supports jQuery.ajax options.
            // See here for more information: http://documentcloud.github.com/backbone/#Collection-fetch
            this.$("#overtimePref").html("");
            OvertimeEarnCodes.fetch({
                async : false
            });
        },

        addAllOvertimeEarnCodes : function () {
            var view = new OvertimeEarnCodeView({collection : OvertimeEarnCodes});
            // Append the earn code to <select>
            this.$("#overtime-section").append(view.render().el);
        },

        showFieldByEarnCodeType : function () {
            var earnCodeType = _.getEarnCodeType(EarnCodes.toJSON(), $("#selectedEarnCode option:selected").val());
            var fields = [".clockInSection", ".clockOutSection", ".hourSection", ".amountSection"];

            // There might be a better way doing this, but we can revisit this later.
            // Currently, the fields variable contains a list of the entry field classes.
            // The Underscore.js _.without function returns an array except the ones you speficied.
            if (earnCodeType == CONSTANTS.EARNCODE_TYPE.HOUR) {
                $(_.without(fields, ".hourSection").join(",")).hide();
                $(fields[2]).show();
            } else if (earnCodeType == CONSTANTS.EARNCODE_TYPE.AMOUNT) {
                $(_.without(fields, ".amountSection").join(",")).hide();
                $(fields[3]).show();
            } else {
                $(_.without(fields, ".clockInSection", ".clockOutSection").join(",")).hide();
                $(fields[0] + "," + fields[1]).show();
            }
        },

        checkLength : function (o, n, min, max) {
            if (o.val().length > max || o.val().length < min) {
                o.addClass('ui-state-error');
                updateTips(n + " field cannot be empty");
                return false;
            }
            return true;
        },

        checkEmptyField : function (o, n) {
            var val = o.val();
            if (val == '') {
                o.addClass('ui-state-error');
                updateTips(n + " field cannot be empty");
                return false;
            }
            return true;
        },

        checkRegexp : function (o, regexp, n) {
            if (( o.val().match(regexp) )) {
                o.addClass('ui-state-error');
                updateTips(n);
                return false;
            }
            return true;
        },

        checkSpecificValue : function (o, value, n) {
            if (o.val() != value) {
                o.addClass('ui-state-error');
                updateTips(n);
                return false;
            }
            return true;
        },

        updateTips : function (t) {
            validation.text(t)
                    .addClass('ui-state-error')
                    .css({'color' : 'red', 'font-weight' : 'bold'});
        },

        checkMinLength : function (o, n, min) {
            if (o.val().length < min) {
                o.addClass('ui-state-error');
                updateTips(n + " field's value is incorrect");
                return false;
            }
            return true;
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
            this.collection.each(function (earnCode) {
                $(self.el).append(self.template(earnCode.toJSON()));
            });

            return this;
        }
    });

    var OvertimeEarnCodeView = Backbone.View.extend({
        el : $("#overtimePref"),

        template : _.template($('#earnCode-template').html()),

        initialize : function () {
            _.bindAll(this, "render");
        },

        render : function () {
            var self = this;
            this.collection.each(function (earnCode) {
                $(self.el).append(self.template(earnCode.toJSON()));
            });

            return this;
        }
    });


    // Initialize the view. This is the kick-off point.
    var app = new TimeBlockView;

    /**
     * Custom util functions.
     * This is the section where you can create your own util methods and inject them to Underscore.
     *
     * The usage of the custom functions is like _(event.target.id).parseEventKey();
     */
    _.mixin({
        /**
         * Parse the div id to get the timeblock id and the action.
         * @param string
         */
        parseEventKey : function (string) {
            return {
                action : string.split("_")[0],
                id : string.split("_")[1]
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
            $("#selectedEarnCode option[value='" + timeBlock.get("earnCode") + "']").attr("selected", "selected");
            $('#tkTimeBlockId').val(timeBlock.get("tkTimeBlockId"));
            $('#hours').val(timeBlock.get("hours"));
            $('#amount').val(timeBlock.get("amount"));
        },

        /**
         * This method takes an earn code json, find the matched earn code, and returns the earn code type.
         * @param earnCodeJson
         * @param earnCode
         */
        getEarnCodeType : function (earnCodeJson, earnCode) {
            var type = "";

            $.each(earnCodeJson, function (i) {
                if (earnCodeJson[i]["earnCode"] == earnCode) {
                    type = earnCodeJson[i]["type"];
                }
            });

            return type;
        },
        /**
         * Provides a helper method to change the button name on the time entry dialog.
         * @param string
         */
        updateDialogButtonName : function (string) {

        },
        /**
         * Reset the values on the timeblock entry form.
         * @param fields
         */
        resetTimeBlockDialog : function (timeBlockDiv) {
            $("input, select", timeBlockDiv).val("");
            // This is not the best solution, but we can probably live with this for now.
            $("#selectedEarnCode").html("<option value=''> -- selecte an earn code --");
        }

    });

    var selectedDays = [];
    var selectingDays = [];
    var beginPeriodDateTimeObj = $('#beginPeriodDate').val() !== undefined ? new Date($('#beginPeriodDate').val()) : d + '/' + m + '/' + y;
    var endPeriodDateTimeObj = $('#endPeriodDate').val() !== undefined ? new Date($('#endPeriodDate').val()) : d + '/' + m + '/' + y;

    $(".cal-table").selectable({
//      $(".another-test").selectable({
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
            var beginDay = new Date(currentDay);
            var endDay = new Date(currentDay);

            beginDay.addDays(parseInt(selectedDays[0].split("_")[1]));
            endDay.addDays(parseInt(selectedDays[selectedDays.length - 1].split("_")[1]));

            app.showTimeEntryDialog(beginDay, endDay);
            selectedDays = [];
        }
    });

    if ($('#docEditable').val() == 'false') {
        $(".cal-table").selectable("destroy");
    }
});
