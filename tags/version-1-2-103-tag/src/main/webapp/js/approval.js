$(document).ready(function () {

    // select All
    $('#selectAll').click(function () {
        $("input[name=selectedEmpl]").each(function () {
            // only select the rows where the docs are in route
            if ($(this).prop("disabled") !== true) {
                this.checked = true;
                $(this).parent().parent().find("td").addClass("highlight");
            }
        });

        if ($(this).prop("checked") == false) {
            $("#approvals-table tr td").removeClass("highlight");
            $("input[name=selectedEmpl]").attr("checked", false);
        }
    });


    // sort by the clicked column
//    $('#approvals-table tr th').click(function() {
//    	// replace all occurrence of white space, including space, tab, newline, etc
//        var field = $(this).text().replace(/\s/g, '');
//        //no sorting for notes and warnings pop-up
//        if(field == "Creator" || field == "CreatedDate" || field == "Content" || field == "Notes:" || field == "Warnings:") {
//        	return false;
//        }
//        var rows = $('#approvals-table tbody tr').length;
//        var isAscending = getParameterByName("ascending");
//
//        if (isAscending == null) {
//            isAscending = true;
//        } else if (isAscending == "true") {
//            isAscending = false;
//        } else {
//            isAscending = true;
//        }
//
//        // submit the page for sorting
//        if (field != 'Action' && field != 'Select') {
//            window.location = 'TimeApproval.do?sortField=' + field + '&ascending=' + isAscending + '&rowsToShow=' + rows + "&hrPyCalendarId=" + $("#pcid").val() + "&hrPyCalendarEntriesId=" + $("#pceid").val() + "&selectedPayCalendarGroup=" + $("#selectedPayCalendarGroup").val();
//        }
//    });

    // this is to determine which fields are sortable
//    $('#approvals-table tr th').filter(
//            function(index) {
//                return $(this).html().replace(/ /, '') == "DocumentId" || $(this).html().replace(/ /, '') == "PrincipalName" || $(this).html().replace(/ /, '') == "Status";
//            }).addClass("sort");
//
//    // add acs/desc icon to a sorted field
//    if (getParameterByName("ascending") != '') {
//        var klass = getParameterByName("ascending") == "true" ? 'headerSortDown' : 'headerSortUp';
//
//        $('#approvals-table tr th').filter(
//                function(index) {
//                    return $(this).html().replace(/ /, '') == getParameterByName("sortField");
//                }).addClass(klass);
//    }

    /**
     * This is to create auto complete w/o using jQuery UI.
     * The code below hasn't been finished yet, but it's commentted out in case we need this in the future.
     */
//    $('#search').click(function () {
//        var searchField = $("#searchField").val();
//        var searchValue = $("#searchValue").val();
//        var beginDate = $("#beginDate").html();
//        var endDate = $("#endDate").html();
//        var hrPyCalendarEntriesId = $("#pceid").val();
//        var selectedPayCalendarGroup = $("#selectedPayCalendarGroup").val();
//
//        $.ajax({
//            url:'TimeApprovalWS.do?methodToCall=searchApprovalRows&searchField=' + searchField + '&searchTerm=' + searchValue +
//                    "&payBeginDateForSearch=" + beginDate + "&payEndDateForSearch=" + endDate +
//                    '&selectedPayCalendarGroup=' + selectedPayCalendarGroup,
//            dataType:"json",
//            success:function (data) {
//                console.log("Test1");
//                // create a div for the result
//                var topPosition = $("#searchValue").offset().top;
//                var leftPosition = $("#searchValue").offset().left;
//                var width = $("#searchValue").width();
//                var height = $("#searchValue").height();
//                var cssObj = {
//                    'top':topPosition + height + 7,
//                    'left':leftPosition,
//                    'width':width + "px",
//                    'height':height + "px"
//                };
//
//                var menu = $("<ul></ul>")
//                        .addClass("ui-autocomplete ui-menu ui-widget ui-widget-content ui-corner-all")
//                        .css(cssObj)
//
////                <li class="ui-menu-item" role="menuitem"><a class="ui-corner-all" tabindex="-1">JavaScript</a></li>
//
//                console.log(menu.data());
//                menu.appendTo("body");
//
//                var json = jQuery.parseJSON(data);
//                $.each(data, function () {
//                    $.each(this, function (id, result) {
//                        var menuItems = $("<a></a>")
//                                .addClass("ui-corner-all");
//                    });
//                });
//
//            }
//        });
//    });

    $('#searchValue').autocomplete({
        source:function (request, response) {
            $('#loading-value').ajaxStart(function () {
                $(this).show();
            });
            $('#loading-value').ajaxStop(function () {
                $(this).hide();
            });
            //var payBeginDate = $('#payBeginDateForSearch').val();
            //var payEndDate = $('#payEndDateForSearch').val();
            var hrPyCalendarEntriesId = $("#pceid").val();
            var selectedPayCalendarGroup = $("#selectedPayCalendarGroup").val();

            $.ajax({
                url:'TimeApprovalWS.do?methodToCall=searchApprovalRows&searchField=' + $('#searchField').val() + '&searchTerm=' + request.term + "&payBeginDateForSearch=" + $("#payBeginDate").html() + "&payEndDateForSearch=" + $("#payEndDate").html() +
                        '&selectedPayCalendarGroup=' + selectedPayCalendarGroup + '&selectedDept=' + $('#selectedDept').val() + '&selectedWorkArea=' + $('#selectedWorkArea').val(),
                dataType:"json",
                success:function (data) {
                    response($.map(data, function (item) {
                        return {
                            value:item.id,
                            id:item.result
                        };
                    }));
                }
            });
        },
        minLength:3,
        select:function (event, data) {
            var rows = $('#approvals-table tbody tr').length;
            var isAscending = getParameterByName("ascending");
            window.location = 'TimeApproval.do?methodToCall=searchResult&searchField=principalName&searchTerm=' + data.item.id;
        },
        open:function () {
            $(this).removeClass("ui-corner-all");
        },
        close:function () {
            $(this).removeClass("ui-corner-top");
        }
    });

    // check if the approve button is disabled. if so, disable the select checkbox as well
    //  $('#actions input[type=button]').filter(
    //        function(index) {
    //          return $(this).prop("disabled");
    //    }).parent().parent().parent().find("input[type=checkbox]").attr("disabled", "disabled");

    // select the whole row when the select checkbox is checked
    $('.selectedEmpl').click(function () {
        $(this).parent().parent().find("td").toggleClass("highlight")
    });

    $('#checkAllAuto').click(
            function () {
                $("INPUT[type='checkbox']:enabled").attr('checked', $('#checkAllAuto').is(':checked'));
            }
    )

    // buttons for prev / next pay calendar entries
    $('.prev').button({
        icons:{
            primary:"ui-icon-circle-triangle-w"
        },
        text:false
    });

    $('.next').button({
        icons:{
            primary:"ui-icon-circle-triangle-e"
        },
        text:false
    });

    $('.approve').button({
        text:false
    });

    // display warning and notes
    $(" .approvals-warning, .approvals-note").tooltip({ effect:'slide'});

//    $('span[id^=showDetailButton]').click(function (e) {
//
//        var docId = e.target.id.split("_")[1];
//
//        $.get("TimeApprovalWS.do?methodToCall=getTimeSummary&documentId=" + docId, function(data) {
//
//            console.log(data);
//        });
//    });

    // toggle the button for the assignment details
//    $('.rowInfo').click(function () {
//        // figure out the columns in the approval table
//        var columns = $(".approvals-table > tbody > tr.odd > td").length;
//        // get the row id
//        var seq = $(this).attr("id").split("_")[1];
//
//        if ($(this).hasClass('ui-icon-plus')) {
//            /**
//             * The code below is a DOM manipulation which grabs the
//             * current time summary layout and rerender it to match
//             * with the layout / styles of the approval table.
//             *
//             * The original layout comes from the same code that
//             * renders the time summary table on the time detail
//             * page.
//             */
//            $timeSummaryRow = undefined;
//            // create a clone of the time summary row
//            $timeSummaryRow = $('.timeSummaryRow_' + seq).clone(true);
//            // remove the date header
//            $(".ui-state-default, tbody tr:first", $timeSummaryRow).remove();
//            // find the time summary table
//            var parent = $('.timeSummaryRow_' + seq).closest("tr");
//            // grab all the TRs from the summary table
//            $trs = $("table tr", $timeSummaryRow);
//            // add a unique id in order to show / hide the new summary table
//            $trs.attr("class", "timeHourDetail_" + seq);
//            // merge columns
//            $("td:nth-child(1)", $trs).attr("colspan", 3);
//
//            // append the new summary table after the total row
//            parent.after($trs);
//
//            // change the icon from - to +
//            $(this).removeClass('ui-icon-plus').addClass('ui-icon-minus');
//        }
//        else {
//            // remove the summary when - button is clicked
//            $('.timeHourDetail_' + seq).remove();
//            // change the icon from + to -
//            $(this).removeClass('ui-icon-minus').addClass('ui-icon-plus');
//        }
//    });

    $("#refresh").click(function(){
       // location.reload();
        location.replace('TimeApproval.do?methodToCall=loadApprovalTab');
    });

    // add css styles to the note and warning buttons
//    $(".rowInfo").hover(function() {
//        $(this).addClass("ui-state-hover");
//    }, function() {
//        $(this).removeClass("ui-state-hover");
//    });


//    $("#selectedPayCalendarGroup").change(function() {
//    	$.post("TimeApproval.do?methodToCall=selectNewPayCalendarGroup&selectedPayCalendarGroup=" + $("#selectedPayCalendarGroup").val());
//    });
});