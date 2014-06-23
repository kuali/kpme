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
package org.kuali.kpme.edo.checklist.validation;

import java.util.List;

import org.kuali.kpme.edo.api.checklist.EdoChecklistItem;
import org.kuali.kpme.edo.api.checklist.EdoChecklistSection;
import org.kuali.kpme.edo.checklist.EdoChecklistItemBo;
import org.kuali.kpme.edo.service.EdoServiceLocator;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;

public class EdoChecklistItemValidation extends MaintenanceDocumentRuleBase {

	@Override
	protected boolean processCustomRouteDocumentBusinessRules(MaintenanceDocument document) {
		
		boolean isValid = super.processCustomRouteDocumentBusinessRules(document);
		LOG.debug("entering custom validation for edo checklist item");
		
		EdoChecklistItemBo checklistItem = (EdoChecklistItemBo) this.getNewDataObject();
		
		if (checklistItem != null) {
			isValid &= validateSectionID(checklistItem);
			isValid &= validateChecklistOrdinal(checklistItem);
		}
		return isValid;
	}
	
	private boolean validateSectionID(EdoChecklistItemBo checklistItem) {
		
		EdoChecklistSection aSection = EdoServiceLocator.getChecklistSectionService().getChecklistSectionByID(checklistItem.getEdoChecklistSectionID()) ;
		String errorMes = "Checklist Section '"+ checklistItem.getEdoChecklistSectionID() + "'";
		if(aSection == null) {
			this.putFieldError("dataObject.edoChecklistSection", "error.existence", errorMes);
			return false;
		} 
		
		return true;
	}
	
	private boolean validateChecklistOrdinal(EdoChecklistItemBo checklistItem) {

		List<EdoChecklistItem> listItems = EdoServiceLocator.getChecklistItemService().getChecklistItemsBySectionID(checklistItem.getEdoChecklistSectionID(), checklistItem.getEffectiveLocalDate());
		for (EdoChecklistItem listItem : listItems) {
			if (listItem.getChecklistItemOrdinal() == checklistItem.getChecklistItemOrdinal()) {
				// error.checklist.ordinal.exist ={0} '{1}' is already in use.
				String[] params = new String[2];
				params[0] = "Checklist Item Ordenal";
				params[1] = checklistItem.getChecklistItemOrdinal()+"";
				this.putFieldError("dataObject.edoChecklistItem", "error.checklist.ordinal.exist", params);
				return false;
			}
		}
		
		return true;
		
	}
}
