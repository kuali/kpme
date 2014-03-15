/**
 * Copyright 2004-2014 The Kuali Foundation
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
package org.kuali.kpme.tklm.time.clocklog.web;

import java.util.List;

import org.kuali.kpme.core.lookup.KPMELookupableImpl;
import org.kuali.kpme.tklm.time.clocklog.ClockLog;
import org.kuali.rice.krad.inquiry.Inquirable;
import org.kuali.rice.krad.uif.element.Action;
import org.kuali.rice.krad.uif.view.LookupView;
import org.kuali.rice.krad.uif.widget.Inquiry;
import org.kuali.rice.krad.web.form.LookupForm;

public class ClockLogLookupableImpl extends KPMELookupableImpl {


	@Override
    public void initSuppressAction(LookupForm lookupForm) {
        ((LookupView) lookupForm.getView()).setSuppressActions(false);
    }
		
}
