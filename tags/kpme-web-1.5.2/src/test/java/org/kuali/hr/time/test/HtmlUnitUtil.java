/**
 * Copyright 2004-2013 The Kuali Foundation
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
package org.kuali.hr.time.test;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import org.apache.log4j.Logger;
import org.kuali.rice.core.api.config.property.ConfigContext;

import java.io.File;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HtmlUnitUtil {
    private static final Logger LOG = Logger.getLogger(HtmlUnitUtil.class);

    /**
     * 
     * @param url
     * @return htmlpage without js enabled
     * @throws Exception
     */
    public static HtmlPage gotoPageAndLogin(WebClient webClient, String url) throws Exception {
    	return gotoPageAndLogin(webClient, url, false);
    }
    
    /**
     * 
     * @param url
     * @param enableJavascript
     * @return htmlpage with js enabled
     * @throws Exception
     */
    public static HtmlPage gotoPageAndLogin(WebClient webClient, String url, boolean enableJavascript) throws Exception {
    	LOG.debug("URL: " + url);

    	// this is required and needs to set to true, otherwise the values set by the onClick event won't be triggered, e.g. methodToCall
        webClient.setJavaScriptEnabled(enableJavascript);
    	return (HtmlPage) webClient.getPage(new URL(url));
    }

    public static boolean pageContainsText(HtmlPage page, String text) {
	return page.asText().indexOf(text) >= 0;
    }

	public static HtmlPage clickInputContainingText(HtmlPage page, String...values) throws Exception {
		page = (HtmlPage)getInputContainingText(page, values).click();
		return page;
	}

    public static HtmlInput getInputContainingText(HtmlPage page, String... values) throws Exception {
		List<HtmlForm> forms = page.getForms();
		for (HtmlForm form : forms){
			for(HtmlElement element : form.getHtmlElementDescendants()) {
				if (element instanceof HtmlInput) {
					if (elementContainsValues(element, values)) {
						return (HtmlInput)element;
					}
				}
			}
		}
		return null;
	}


    public static List<HtmlInput> getInputsContainingText(HtmlPage page, String... values) throws Exception {
		List<HtmlInput> inputs = new ArrayList<HtmlInput>();
		List<HtmlForm> forms = page.getForms();
		for (HtmlForm form : forms){

			for(HtmlElement element : form.getHtmlElementDescendants()) {
				if (element instanceof HtmlInput) {
					if (elementContainsValues(element, values)) {
						inputs.add((HtmlInput)element);
					}
				}
			}
		}
		return inputs;
	}

	protected static boolean elementContainsValues(HtmlElement element, String... values) {
		for (String value : values) {
			if (element.toString().indexOf(value) == -1) {
				return false;
			}
        }
		return true;
	}

	public static HtmlPage clickAnchorContainingText(HtmlPage page, String... values) throws Exception {
		return (HtmlPage) getAnchorContainingText(page, values).click();
	}

	@SuppressWarnings("unchecked")
	public static HtmlAnchor getAnchorContainingText(HtmlPage page, String... values) throws Exception {
		for (Iterator iterator = page.getAnchors().iterator(); iterator.hasNext();) {
			HtmlAnchor anchor = (HtmlAnchor) iterator.next();
			if (elementContainsValues(anchor, values)) {
				return anchor;
			}
		}
		return null;
	}

    public static String getBaseURL() {
	    return ConfigContext.getCurrentContextConfig().getProperty("application.url");
    }
    
    public static String getContext() {
    	return "/" + ConfigContext.getCurrentContextConfig().getProperty("app.context.name");
    }

    public static String getTempDir() {
	return ConfigContext.getCurrentContextConfig().getProperty("temp.dir");
    }

    public static Integer getPort() {
	return new Integer(ConfigContext.getCurrentContextConfig().getProperty("kns.test.port"));
    }

    public static void createTempFile(HtmlPage page) throws Exception {
	createTempFile(page, null);
    }

    public static void createTempFile(HtmlPage page, String name) throws Exception {
	name = name == null ? "TestOutput" : name;
	File temp = File.createTempFile(name, ".html", new File(HtmlUnitUtil.getTempDir()));
	FileOutputStream fos = new FileOutputStream(temp);
	String xml = page.asXml();
	StringReader xmlReader = new StringReader(xml);
	int i;
	while ((i = xmlReader.read()) != -1) {
	    fos.write(i);
	}
	try {
	    fos.close();
	} catch (Exception e) {
	}
	try {
	    xmlReader.close();
	} catch (Exception e) {
	}
    }

    public static HtmlInput getInputContainingText(HtmlForm form, String text) throws Exception {

		for (HtmlElement element : form.getHtmlElementDescendants()) {
			if (element instanceof HtmlInput) {
				HtmlInput i = (HtmlInput) element;
				if (element.toString().contains(text)) {
					return i;
				}
			}

		}
		return null;
    }
    
	public static HtmlForm getDefaultForm(HtmlPage htmlPage) {
		if (htmlPage.getForms().size() == 1) {
			return (HtmlForm)htmlPage.getForms().get(0);
		}
		return (HtmlForm)htmlPage.getForms().get(1);
	}

}