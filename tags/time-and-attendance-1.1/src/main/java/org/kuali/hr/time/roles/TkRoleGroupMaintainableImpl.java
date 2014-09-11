package org.kuali.hr.time.roles;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.kuali.hr.job.Job;
import org.kuali.hr.time.HrBusinessObject;
import org.kuali.hr.time.service.base.TkServiceLocator;
import org.kuali.hr.time.util.HrBusinessObjectMaintainableImpl;
import org.kuali.hr.time.util.TKContext;
import org.kuali.hr.time.util.TKUtils;
import org.kuali.hr.time.util.TkConstants;
import org.kuali.rice.kim.bo.types.dto.AttributeSet;
import org.kuali.rice.kim.service.KIMServiceLocator;
import org.kuali.rice.kns.bo.BusinessObject;
import org.kuali.rice.kns.document.MaintenanceDocument;
import org.kuali.rice.kns.maintenance.Maintainable;
import org.kuali.rice.kns.service.KNSServiceLocator;
import org.kuali.rice.kns.web.ui.Section;

public class TkRoleGroupMaintainableImpl extends HrBusinessObjectMaintainableImpl {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    public void saveBusinessObject() {
        BusinessObject bo = this.getBusinessObject();
        if (bo instanceof TkRoleGroup) {
            TkRoleGroup trg = (TkRoleGroup)bo;
            List<TkRole> roles = trg.getRoles();
    		List<TkRole> rolesCopy = new ArrayList<TkRole>();
    		rolesCopy.addAll(roles);
    		if (trg.getInactiveRoles() != null
    				&& trg.getInactiveRoles().size() > 0) {
    			for (TkRole role : trg.getInactiveRoles()) {
    				roles.add(role);
    			}
    		}
            for (TkRole role : roles) {
                if (StringUtils.equals(role.getRoleName(), TkConstants.ROLE_TK_SYS_ADMIN)) {
                    AttributeSet qualifier = new AttributeSet();
                    String principalId = role.getPrincipalId();
                    if(StringUtils.isBlank(principalId)){
                    	principalId = trg.getPrincipalId();
                    }
                    if(StringUtils.isBlank(principalId)){
                    	KIMServiceLocator.getRoleUpdateService().assignPrincipalToRole(principalId, TkConstants.ROLE_NAMESAPCE, role.getRoleName(), qualifier);
                    }
                }
                role.setPrincipalId(trg.getPrincipalId());
                role.setUserPrincipalId(TKContext.getUser().getPrincipalId());
                
                HrBusinessObject oldHrObj = this.getObjectById(role.getId());
                
    			if(oldHrObj!= null){
    				//if the effective dates are the same do not create a new row just inactivate the old one
    				if(role.getEffectiveDate().equals(oldHrObj.getEffectiveDate())){
    					oldHrObj.setActive(false);
    					oldHrObj.setTimestamp(TKUtils.subtractOneSecondFromTimestamp(new Timestamp(TKUtils.getCurrentDate().getTime()))); 
    				} else{
    					//if effective dates not the same add a new row that inactivates the old entry based on the new effective date
    					oldHrObj.setTimestamp(TKUtils.subtractOneSecondFromTimestamp(new Timestamp(TKUtils.getCurrentDate().getTime())));
    					oldHrObj.setEffectiveDate(role.getEffectiveDate());
    					oldHrObj.setActive(false);
    					oldHrObj.setId(null);
    				}
    				KNSServiceLocator.getBusinessObjectService().save(oldHrObj);
    				
    				role.setTimestamp(new Timestamp(System.currentTimeMillis()));
    				role.setId(null);
    			}
            }
     
            TkServiceLocator.getTkRoleService().saveOrUpdate(roles);
            trg.setRoles(rolesCopy);
        }
    }

	@Override
	public void processAfterEdit(MaintenanceDocument document,
			Map<String, String[]> parameters) {
		TkRoleGroup tkRoleGroup = (TkRoleGroup)document.getNewMaintainableObject().getBusinessObject();
		TkRoleGroup tkRoleGroupOld = (TkRoleGroup)document.getOldMaintainableObject().getBusinessObject();
		List<Job> jobs = TkServiceLocator.getJobSerivce().getJobs(tkRoleGroup.getPrincipalId(), TKUtils.getCurrentDate());
		List<TkRole> positionRoles = new ArrayList<TkRole>();
		List<TkRole> inactivePositionRoles = new ArrayList<TkRole>();
		Set<String> positionNumbers = new HashSet<String>(); 
		for(Job job : jobs){
			positionNumbers.add(job.getPositionNumber());
		}
		for(String pNo : positionNumbers){
			TkRole positionRole = TkServiceLocator.getTkRoleService().getRolesByPosition(pNo);
			if(positionRole != null)
				positionRoles.add(positionRole);
			TkRole inactivePositionRole = TkServiceLocator.getTkRoleService().getInactiveRolesByPosition(pNo);
			if(inactivePositionRole != null)
				inactivePositionRoles.add(inactivePositionRole);
		}
		tkRoleGroup.setInactivePositionRoles(inactivePositionRoles);
		tkRoleGroupOld.setInactivePositionRoles(inactivePositionRoles);
		tkRoleGroup.setPositionRoles(positionRoles);
		tkRoleGroupOld.setPositionRoles(positionRoles);
		
		TkServiceLocator.getTkRoleGroupService().populateRoles(tkRoleGroupOld);
		TkServiceLocator.getTkRoleGroupService().populateRoles(tkRoleGroup);
		super.processAfterEdit(document, parameters);
	}
	
	@Override
	public List getSections(MaintenanceDocument document,
			Maintainable oldMaintainable) {
		List sections = super.getSections(document, oldMaintainable);
		for (Object obj : sections) {
			Section sec = (Section) obj;
			if (document.isOldBusinessObjectInDocument()
					&& sec.getSectionId().equals("inactiveRoles")) {
				sec.setHidden(false);
			} else if (!document.isOldBusinessObjectInDocument()
					&& sec.getSectionId().equals("inactiveRoles")) {
				sec.setHidden(true);
			}
		}
		return sections;
	}

	@Override
	public HrBusinessObject getObjectById(String id) {
		return (HrBusinessObject)TkServiceLocator.getTkRoleService().getRole(id);
	}

}