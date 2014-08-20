package org.kuali.hr.time.timesheet.web;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.json.JSONString;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.junit.Test;
import org.kuali.hr.time.assignment.Assignment;
import org.kuali.hr.time.detail.web.TimeDetailActionFormBase;
import org.kuali.hr.time.earncode.EarnCode;
import org.kuali.hr.time.paycalendar.PayCalendarEntries;
import org.kuali.hr.time.roles.TkRole;
import org.kuali.hr.time.service.base.TkServiceLocator;
import org.kuali.hr.time.test.HtmlUnitUtil;
import org.kuali.hr.time.test.TkTestCase;
import org.kuali.hr.time.test.TkTestConstants;
import org.kuali.hr.time.test.TkTestUtils;
import org.kuali.hr.time.timesheet.TimesheetDocument;
import org.kuali.hr.time.util.TKContext;
import org.kuali.hr.time.util.TimeDetailTestUtils;
import org.kuali.hr.time.util.TkConstants;
import org.kuali.hr.time.web.TkLoginFilter;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimesheetWorkflowIntegrationTest extends TimesheetWebTestBase {

    public static final String USER_PRINCIPAL_ID = "admin";
	private Date JAN_AS_OF_DATE = new Date((new DateTime(2010, 1, 1, 0, 0, 0, 0, TkConstants.SYSTEM_DATE_TIME_ZONE)).getMillis());


    /**
     * @throws Exception
     */
    public void setUp() throws Exception {
        super.setUp();
        // Data is loaded as part of database loading lifecycle
        // See: tk-test-data.sql
        // See: TimesheetWorkflowIntegrationTest.sql
        // See: TkTestCase.java
        //
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    /**
     * - create timesheet
     * - add two 8 hour time blocks
     * - submit timesheet for routing
     * - ## login as approver
     * - look for approval button
     * - approve timeblock
     * - verify approval button gone
     * - ## login as original user
     * - verify submit for routing button gone
     */
    public void testTimesheetSubmissionIntegration() throws Exception {
        Date asOfDate = new Date((new DateTime(2011, 3, 1, 12, 0, 0, 0, DateTimeZone.forID("EST"))).getMillis());
        PayCalendarEntries pcd = TkServiceLocator.getPayCalendarSerivce().getCurrentPayCalendarDates(USER_PRINCIPAL_ID, asOfDate);
        assertNotNull("No PayCalendarDates", pcd);
        TimesheetDocument tdoc = TkServiceLocator.getTimesheetService().openTimesheetDocument(USER_PRINCIPAL_ID, pcd);
        String tdocId = tdoc.getDocumentId();
        HtmlPage page = loginAndGetTimeDetailsHtmlPage("admin", tdocId);

        // 1. Obtain User Data
        List<Assignment> assignments = TkServiceLocator.getAssignmentService().getAssignments(TKContext.getPrincipalId(), JAN_AS_OF_DATE);
        Assignment assignment = assignments.get(0);
        List<EarnCode> earnCodes = TkServiceLocator.getEarnCodeService().getEarnCodes(assignment, JAN_AS_OF_DATE);
        EarnCode earnCode = earnCodes.get(0);

        // 2. Set Timeblock Start and End time
        // 3/02/2011 - 8:00a to 4:00pm
        DateTime start = new DateTime(2011, 3, 2, 8, 0, 0, 0, TkConstants.SYSTEM_DATE_TIME_ZONE);
        DateTime end = new DateTime(2011, 3, 3, 16, 0, 0, 0, TkConstants.SYSTEM_DATE_TIME_ZONE);

        HtmlForm form = page.getFormByName("TimeDetailActionForm");
        assertNotNull(form);

        // Build an action form - we're using it as a POJO, it ties into the
        // existing TK validation setup
        TimeDetailActionFormBase tdaf = TimeDetailTestUtils.buildDetailActionForm(tdoc, assignment, earnCode, start, end, null, true, null);
        List<String> errors = TimeDetailTestUtils.setTimeBlockFormDetails(form, tdaf);
        // Check for errors
        assertEquals("There should be no errors in this time detail submission", 0, errors.size());

        page = TimeDetailTestUtils.submitTimeDetails(TimesheetWebTestBase.getTimesheetDocumentUrl(tdocId), tdaf);
        assertNotNull(page);
        //HtmlUnitUtil.createTempFile(page, "TimeBlockPresent");

        // Verify block present on rendered page.
        String pageAsText = page.asText();

        // JSON
        //
        //
        // Grab the timeblock data from the text area. We can check specifics there
        // to be more fine grained in our validation.
        String dataText = page.getElementById("timeBlockString").getFirstChild().getNodeValue();
        JSONObject jsonData = (JSONObject)JSONValue.parse(dataText);
        assertTrue("TimeBlock Data Missing.", checkJSONValues(jsonData,
                new ArrayList<Map<String, Object>>() {{
                    add(new HashMap<String, Object>() {{
                        put("earnCode", "RGN");
                        put("hours", "8.0");
                        put("amount", null);
                    }});
                }},
                new HashMap<String, Object>() {{
                    put("earnCode", "RGN");
                    put("startNoTz", "2011-03-02T08:00:00");
                    put("endNoTz", "2011-03-02T16:00:00");
                    put("title", "SDR1 Work Area");
                    put("assignment", "30_30_30");
                }}
        ));

        // Check the Display Rendered Text for Time Block, Quick Check
        assertTrue("TimeBlock not Present.", pageAsText.contains("08:00 AM - 04:00 PM"));
        assertTrue("TimeBlock not Present.", pageAsText.contains("RGN - 8.00 hours"));

        //
        // Route Timesheet
        //
        // Routing is initiated via javascript, we need to extract the routing
        // action from the button element to perform this action.
        HtmlElement routeButton = page.getElementById("ts-route-button");
        String routeHref = TkTestUtils.getOnClickHref(routeButton);
        // The 'only' way to do the button click.
        page = HtmlUnitUtil.gotoPageAndLogin(TkTestConstants.BASE_URL + "/" + routeHref);
        //HtmlUnitUtil.createTempFile(page, "RouteClicked");
        pageAsText = page.asText();
        // Verify Route Status via UI
        assertTrue("Wrong Document Loaded.", pageAsText.contains(tdocId));
        assertTrue("Document not routed.", pageAsText.contains("Enroute"));
        routeButton = page.getElementById("ts-route-button");
        assertNull("Route button should not be present.", routeButton);
        HtmlElement approveButton = page.getElementById("ts-approve-button");
        assertNull("Approval button should not be present.", approveButton);

        //
        // Login as Approver, who is not 'admin'
        page = TimesheetWebTestBase.loginAndGetTimeDetailsHtmlPage("eric", tdocId);
        //HtmlUnitUtil.createTempFile(page, "2ndLogin");
        pageAsText = page.asText();
        assertTrue("Document not routed.", pageAsText.contains("Enroute"));
        approveButton = page.getElementById("ts-approve-button");
        assertNotNull("No approval button present.", approveButton);

        // Click Approve
        // And Verify
        //
        routeHref = TkTestUtils.getOnClickHref(approveButton);
        page = HtmlUnitUtil.gotoPageAndLogin(TkTestConstants.BASE_URL + "/" + routeHref);
        //HtmlUnitUtil.createTempFile(page, "ApproveClicked");
        pageAsText = page.asText();
        assertTrue("Wrong Document Loaded.", pageAsText.contains(tdocId));
        assertTrue("Login info not present.", pageAsText.contains("Employee Id:"));
        assertTrue("Login info not present.", pageAsText.contains("Employee, Eric"));
        assertTrue("Document not routed.", pageAsText.contains("Final"));
        approveButton = page.getElementById("ts-approve-button");
        assertNull("Approval button should not be present.", approveButton);

        //Kind of hacky to change this, as it changes for everything.
        //Change back because other tests may use this.
        TkLoginFilter.TEST_ID = "admin";
    }

}