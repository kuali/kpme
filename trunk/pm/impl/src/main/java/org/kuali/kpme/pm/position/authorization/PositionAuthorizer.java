package org.kuali.kpme.pm.position.authorization;

import org.apache.commons.lang.StringUtils;
import org.kuali.kpme.core.api.department.Department;
import org.kuali.kpme.core.api.namespace.KPMENamespace;
import org.kuali.kpme.core.authorization.KPMEMaintenanceDocumentViewAuthorizer;
import org.kuali.kpme.core.permission.KPMEPermissionTemplate;
import org.kuali.kpme.core.role.KPMERoleMemberAttribute;
import org.kuali.kpme.core.service.HrServiceLocator;
import org.kuali.kpme.pm.position.PositionBo;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.maintenance.MaintenanceDocumentBase;
import org.kuali.rice.krad.util.KRADConstants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mlemons on 6/23/14.
 */
public class PositionAuthorizer extends KPMEMaintenanceDocumentViewAuthorizer {

    private static final long serialVersionUID = 1362536674228377102L;

    public boolean canCopy(Document document, Person user) {
        //document.
        //MaintenanceDocument doc =
        if (document instanceof org.kuali.rice.krad.maintenance.MaintenanceDocumentBase)
        {
            Object dataObject = ((MaintenanceDocumentBase) document).getDocumentDataObject();

            if (dataObject != null)
            {
                Map<String, String> permissionDetails = new HashMap<String, String>();
                permissionDetails.put(KimConstants.AttributeConstants.DOCUMENT_TYPE_NAME, getDocumentDictionaryService().getMaintenanceDocumentTypeName(dataObject.getClass()));


                return isAuthorizedByTemplate(document, KPMENamespace.KPME_WKFLW.getNamespaceCode(),
                        "Copy Position", user.getPrincipalId(), permissionDetails, getRoleQualification(dataObject, user.getPrincipalId()));
            }
        }

        return super.canMaintain(document, user);
    }

    @Override
    protected void addRoleQualification(Object dataObject, Map<String, String> attributes) {
        super.addRoleQualification(dataObject, attributes);

        if (dataObject instanceof PositionBo) {
            PositionBo positionObj = (PositionBo) dataObject;

            if (positionObj != null) {
                Department departmentObj = HrServiceLocator.getDepartmentService().getDepartment(positionObj.getPrimaryDepartment(), positionObj.getGroupKeyCode(), positionObj.getEffectiveLocalDate());

                if (departmentObj != null) {
                    attributes.put(KPMERoleMemberAttribute.INSTITUION.getRoleMemberAttributeName(), departmentObj.getGroupKey().getInstitutionCode());

                    attributes.put(KPMERoleMemberAttribute.DEPARTMENT.getRoleMemberAttributeName(), departmentObj.getDept());

                    attributes.put(KPMERoleMemberAttribute.LOCATION.getRoleMemberAttributeName(), departmentObj.getGroupKey().getLocationId());

                    attributes.put(KPMERoleMemberAttribute.ORGANIZATION.getRoleMemberAttributeName(), departmentObj.getOrg());
                }
                attributes.put(KPMERoleMemberAttribute.GROUP_KEY_CODE.getRoleMemberAttributeName(), positionObj.getGroupKeyCode());
            }
        }
    }

    @Override
    public boolean canMaintain(Object dataObject, Person user) {

        Map<String, String> permissionDetails = new HashMap<String, String>();
        permissionDetails.put(KimConstants.AttributeConstants.DOCUMENT_TYPE_NAME, getDocumentDictionaryService().getMaintenanceDocumentTypeName(dataObject.getClass()));


        if (isAuthorizedByTemplate(dataObject, KPMENamespace.KPME_WKFLW.getNamespaceCode(), KPMEPermissionTemplate.EDIT_KPME_MAINTENANCE_DOCUMENT.getPermissionTemplateName(),
                user.getPrincipalId(), permissionDetails, getRoleQualification(dataObject, user.getPrincipalId())))
        {
            return true;
        }

        return false;
        //return super.canMaintain(dataObject, user);
    }
}
