package org.kuali.hr.time.earncodegroup.validation;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.kuali.hr.time.earncodegroup.EarnCodeGroup;
import org.kuali.hr.time.earncodegroup.EarnCodeGroupDefinition;
import org.kuali.hr.time.service.base.TkServiceLocator;
import org.kuali.hr.time.util.ValidationUtils;
import org.kuali.rice.kns.document.MaintenanceDocument;
import org.kuali.rice.kns.maintenance.rules.MaintenanceDocumentRuleBase;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

public class EarnCodeGroupValidation  extends MaintenanceDocumentRuleBase{

	@Override
	protected boolean processCustomRouteDocumentBusinessRules(MaintenanceDocument document) {
		EarnCodeGroup earnGroup = (EarnCodeGroup)this.getNewBo();
		Set<String> earnCodes = new HashSet<String>();
		int index = 0;
		if(earnGroup.getEarnCodeGroups().size() < 1){
			this.putGlobalError("earncode.required");
			return false;
		}
		for(EarnCodeGroupDefinition earnGroupDef : earnGroup.getEarnCodeGroups()){
			if(earnCodes.contains(earnGroupDef.getEarnCode())){
				this.putFieldError("earnCodeGroups["+index+"].earnCode", "earngroup.duplicate.earncode",earnGroupDef.getEarnCode());

			}
			if(earnGroup.getShowSummary()) {
				validateEarnCode(earnGroupDef.getEarnCode().toUpperCase(), index, earnGroup);
			}
			if (!ValidationUtils.validateEarnCode(earnGroupDef.getEarnCode(), earnGroup.getEffectiveDate())) {
				this.putFieldError("earnCodeGroups["+index+"].earnCode", "error.existence", "Earncode '" + earnGroupDef.getEarnCode()+ "'");
			}
			earnCodes.add(earnGroupDef.getEarnCode());
			index++;
		}
		int count = TkServiceLocator.getEarnCodeGroupService().getNewerEarnCodeGroupCount(earnGroup.getEarnCodeGroup(), earnGroup.getEffectiveDate());
		if(count > 0) {
			this.putFieldError("effectiveDate", "earngroup.effectiveDate.newr.exists");
			return false;
		}
		return true;
	}

    protected void validateEarnCode(String earnCode, int index, EarnCodeGroup editedEarnGroup) {
    	BusinessObjectService businessObjectService = KRADServiceLocator.getBusinessObjectService();
    	Map<String,Object> criteria = new HashMap<String,Object>();
		criteria.put("showSummary", "Y");
		criteria.put("active", "Y");
    	Collection aCol = businessObjectService.findMatching(EarnCodeGroup.class, criteria);
		Iterator<EarnCodeGroup> itr = aCol.iterator();
		while (itr.hasNext()) {
			EarnCodeGroup earnGroup = itr.next();
			if(!earnGroup.getHrEarnCodeGroupId().equals(editedEarnGroup.getHrEarnCodeGroupId())) {
				criteria = new HashMap<String,Object>();
				criteria.put("hrEarnCodeGroupId", earnGroup.getHrEarnCodeGroupId());

				Collection earnGroupDefs = businessObjectService.findMatching(EarnCodeGroupDefinition.class, criteria);
				Iterator<EarnCodeGroupDefinition> iterator = earnGroupDefs.iterator();
				while (iterator.hasNext()) {
					EarnCodeGroupDefinition def = iterator.next();
					if(StringUtils.equals(earnCode, def.getEarnCode().toUpperCase())) {
						String[] parameters = new String[2];
						parameters[0] = earnCode;
						parameters[1] = earnGroup.getDescr();
						this.putFieldError("earnCodeGroups["+index+"].earnCode", "earngroup.earncode.already.used", parameters);
					}
				}
			}
		}
    }

 }