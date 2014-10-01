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
package org.kuali.kpme.core.tkmdocument.authorization;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.kew.api.document.DocumentStatus;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.maintenance.MaintenanceDocumentAuthorizerBase;

/**
 * Created by mlemons on 9/10/14.
 */

public class KhrEmployeeDocumentMaintenanceDocumentAuthorizer extends MaintenanceDocumentAuthorizerBase {

	private static final long serialVersionUID = 4641364720543081082L;

	@Override
    public boolean canCancel(Document document, Person user) {
        DocumentStatus status = document.getDocumentHeader().getWorkflowDocument().getStatus();

        String statusCode = status.getCode();
        if (StringUtils.equals(statusCode,"R"))
        {
            return false;
        }

        return super.canCancel(document, user);
    }
}