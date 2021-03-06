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
package org.kuali.kpme.tklm.time.missedpunch.web;

import org.kuali.kpme.tklm.time.missedpunch.MissedPunchBo;
import org.kuali.rice.krad.web.form.TransactionalDocumentFormBase;

public class MissedPunchForm extends TransactionalDocumentFormBase {

	private static final long serialVersionUID = -5511083730204963887L;
	
	private MissedPunchBo missedPunch = new MissedPunchBo();
	
	private boolean assignmentReadOnly;
	
	private boolean missedPunchSubmitted;
	
	private String ipAddress;
	
	@Override
	public String getDocTypeName() {
		return "MissedPunchDocumentType";
	}

	public MissedPunchBo getMissedPunch() {
		return missedPunch;
	}

	public void setMissedPunch(MissedPunchBo missedPunch) {
		this.missedPunch = missedPunch;
	}

	public boolean isAssignmentReadOnly() {
		return assignmentReadOnly;
	}

	public void setAssignmentReadOnly(boolean assignmentReadOnly) {
		this.assignmentReadOnly = assignmentReadOnly;
	}

	public boolean isMissedPunchSubmitted() {
		return missedPunchSubmitted;
	}

	public void setMissedPunchSubmitted(boolean missedPunchSubmitted) {
		this.missedPunchSubmitted = missedPunchSubmitted;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

}
