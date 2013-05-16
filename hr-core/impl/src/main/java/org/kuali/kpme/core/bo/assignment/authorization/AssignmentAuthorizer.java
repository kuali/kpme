package org.kuali.kpme.core.bo.assignment.authorization;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.kuali.kpme.core.authorization.KPMEMaintenanceDocumentAuthorizerBase;
import org.kuali.kpme.core.bo.assignment.Assignment;
import org.kuali.kpme.core.bo.department.Department;
import org.kuali.kpme.core.role.KPMERoleMemberAttribute;
import org.kuali.kpme.core.service.HrServiceLocator;

@SuppressWarnings("deprecation")
public class AssignmentAuthorizer extends KPMEMaintenanceDocumentAuthorizerBase {

	private static final long serialVersionUID = 1962036374728477204L;

	protected void addRoleQualification(Object dataObject, Map<String, String> attributes) {
		super.addRoleQualification(dataObject, attributes);

		String location = StringUtils.EMPTY;
		
		if (dataObject instanceof Assignment) {
			Assignment assignmentObj = (Assignment) dataObject;
			
			if (assignmentObj != null) {
				Department departmentObj = HrServiceLocator.getDepartmentService().getDepartment(assignmentObj.getDept(), assignmentObj.getEffectiveLocalDate());
				
				if (departmentObj != null) {
					location = departmentObj.getLocation();
				}
			}
		}
		
		attributes.put(KPMERoleMemberAttribute.LOCATION.getRoleMemberAttributeName(), location);
	}

}
