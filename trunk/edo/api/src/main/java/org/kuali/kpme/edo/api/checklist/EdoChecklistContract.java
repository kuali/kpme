package org.kuali.kpme.edo.api.checklist;

import org.kuali.kpme.core.api.mo.KpmeEffectiveKeyedDataTransferObject;

public interface EdoChecklistContract extends KpmeEffectiveKeyedDataTransferObject {
	
	/**
	 * The identifier of the EdoChecklist
	 * 
	 * <p>
	 * edoChecklistId of the EdoChecklist
	 * <p>
	 * 
	 * @return edoChecklistId for EdoChecklist
	 */
	public String getEdoChecklistId();

	/**
	 * The dossier type code of the EdoChecklist
	 * 
	 * <p>
	 * dossierTypeCode of the EdoChecklist
	 * <p>
	 * 
	 * @return dossierTypeCode for EdoChecklist
	 */
	public String getDossierTypeCode();

	/**
	 * The department id that this EdoChecklist is associated with
	 * 
	 * <p>
	 * departmentID of the EdoChecklist
	 * <p>
	 * 
	 * @return departmentID for EdoChecklist
	 */
	public String getDepartmentID();
	
	/**
	 * The organization code that this EdoChecklist is associated with
	 * 
	 * <p>
	 * organizationCode of the EdoChecklist
	 * <p>
	 * 
	 * @return organizationCode for EdoChecklist
	 */
	public String getOrganizationCode();

	/**
	 * The description of the EdoChecklist
	 * 
	 * <p>
	 * description of the EdoChecklist
	 * <p>
	 * 
	 * @return description for EdoChecklist
	 */
	public String getDescription();

}