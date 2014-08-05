package org.kuali.hr.time.paycalendar.validation;

import org.junit.Test;
import org.kuali.hr.time.test.HtmlUnitUtil;
import org.kuali.hr.time.test.TkTestCase;
import org.kuali.hr.time.test.TkTestConstants;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

public class PayCalendarEntriesMaintenaceTest extends TkTestCase {
	@Override
	public void setUp() throws Exception {
		super.setUp();
	}
	
	@Override
	public void tearDown() throws Exception {
		super.tearDown();
	}
	
	@Test
	//tests PayCalendarEntriesRule
	public void testSubmitPayCalendarEntriesMaint() throws Exception {
    	//String baseUrl = HtmlUnitUtil.getBaseURL() + "/kr/maintenance.do?businessObjectClassName=org.kuali.hr.time.paycalendar.PayCalendarEntries&methodToCall=start";
		String baseUrl = TkTestConstants.Urls.PAY_CALENDAR_ENTRIES_MAINT_NEW_URL;
    	HtmlPage page = HtmlUnitUtil.gotoPageAndLogin(baseUrl);
    	assertNotNull(page);

		HtmlTextInput text  = (HtmlTextInput) page.getHtmlElementById("document.documentHeader.documentDescription");
		text.setValueAttribute("test");
		text  = (HtmlTextInput) page.getHtmlElementById(TkTestConstants.DOC_NEW_ELEMENT_ID_PREFIX + "calendarGroup");
		text.setValueAttribute("TTT");	// set an invalid pay calendar		
		text  = (HtmlTextInput) page.getHtmlElementById(TkTestConstants.DOC_NEW_ELEMENT_ID_PREFIX + "beginPeriodDate");
		text.setValueAttribute("02/21/2011");		
		text  = (HtmlTextInput) page.getHtmlElementById(TkTestConstants.DOC_NEW_ELEMENT_ID_PREFIX + "beginPeriodTime");
		text.setValueAttribute("08:00 am");		
		text  = (HtmlTextInput) page.getHtmlElementById(TkTestConstants.DOC_NEW_ELEMENT_ID_PREFIX + "endPeriodDate");
		text.setValueAttribute("02/28/2011");		
		text  = (HtmlTextInput) page.getHtmlElementById(TkTestConstants.DOC_NEW_ELEMENT_ID_PREFIX + "endPeriodTime");
		text.setValueAttribute("08:00 am");    
		text  = (HtmlTextInput) page.getHtmlElementById(TkTestConstants.DOC_NEW_ELEMENT_ID_PREFIX + "batchInitiateDate");
		text.setValueAttribute("02/16/2011");		
		text  = (HtmlTextInput) page.getHtmlElementById(TkTestConstants.DOC_NEW_ELEMENT_ID_PREFIX + "batchInitiateTime");
		text.setValueAttribute("08:00 am");		
		text  = (HtmlTextInput) page.getHtmlElementById(TkTestConstants.DOC_NEW_ELEMENT_ID_PREFIX + "batchEndPayPeriodDate");
		text.setValueAttribute("02/21/2011");		
		text  = (HtmlTextInput) page.getHtmlElementById(TkTestConstants.DOC_NEW_ELEMENT_ID_PREFIX + "batchEndPayPeriodTime");
		text.setValueAttribute("08:00 am"); 
		text  = (HtmlTextInput) page.getHtmlElementById(TkTestConstants.DOC_NEW_ELEMENT_ID_PREFIX + "batchEmployeeApprovalDate");
		text.setValueAttribute("02/17/2011");		
		text  = (HtmlTextInput) page.getHtmlElementById(TkTestConstants.DOC_NEW_ELEMENT_ID_PREFIX + "batchEmployeeApprovalTime");
		text.setValueAttribute("08:00 am");
		text  = (HtmlTextInput) page.getHtmlElementById(TkTestConstants.DOC_NEW_ELEMENT_ID_PREFIX + "batchSupervisorApprovalDate");
		text.setValueAttribute("02/18/2011");		
		text  = (HtmlTextInput) page.getHtmlElementById(TkTestConstants.DOC_NEW_ELEMENT_ID_PREFIX + "batchSupervisorApprovalTime");
		text.setValueAttribute("08:00 am");
		
		HtmlElement element = page.getElementByName("methodToCall.route");
        HtmlPage page1 = element.click();
		// error for invalid pay calendar
        assertTrue("Maintenance Page contains error messages", page1.asText().contains("You must specify a valid Pay Calendar."));

		text  = (HtmlTextInput) page1.getHtmlElementById(TkTestConstants.DOC_NEW_ELEMENT_ID_PREFIX + "calendarGroup");
		text.setValueAttribute("BW-CAL");	// set a valid pay calendar	
        
		element = page1.getElementByName("methodToCall.route");
        HtmlPage page2 = element.click();
        assertTrue("Maintenance page is submitted successfully", page2.asText().contains("Document was successfully submitted."));
		assertTrue("Maintenance page is submitted successfully", page2.asText().contains("Status: 	 FINAL"));
	}
}