package org.kuali.kpme.edo.role.service;

import org.kuali.rice.kns.kim.role.RoleTypeServiceBase;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService(name = "edoCandidateRoleTypeService", targetNamespace = "EDO")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)

public class EDOCandidateRoleTypeService extends RoleTypeServiceBase {
	
	
}
