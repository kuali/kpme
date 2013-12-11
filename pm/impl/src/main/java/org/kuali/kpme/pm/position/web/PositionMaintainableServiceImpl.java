/**
 * Copyright 2004-2013 The Kuali Foundation
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
package org.kuali.kpme.pm.position.web;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.kpme.core.bo.HrBusinessObject;
import org.kuali.kpme.core.bo.HrBusinessObjectMaintainableImpl;
import org.kuali.kpme.core.departmentaffiliation.DepartmentAffiliation;
import org.kuali.kpme.core.util.ValidationUtils;
import org.kuali.kpme.pm.position.Position;
import org.kuali.kpme.pm.position.PositionDuty;
import org.kuali.kpme.pm.position.PositionQualification;
import org.kuali.kpme.pm.position.PstnFlag;
import org.kuali.kpme.pm.position.funding.PositionFunding;
import org.kuali.kpme.pm.positiondepartment.PositionDepartment;
import org.kuali.kpme.pm.positionresponsibility.PositionResponsibility;
import org.kuali.kpme.pm.service.base.PmServiceLocator;
import org.kuali.rice.kew.api.document.DocumentStatus;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.krad.bo.DocumentHeader;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.service.DocumentService;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.uif.container.CollectionGroup;
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.web.form.MaintenanceDocumentForm;

public class PositionMaintainableServiceImpl extends HrBusinessObjectMaintainableImpl {

	private static final long serialVersionUID = 1L;

	@Override
	public HrBusinessObject getObjectById(String id) {
		return (HrBusinessObject) PmServiceLocator.getPositionService().getPosition(id);
	}
	
	@Override
	public void customSaveLogic(HrBusinessObject hrObj){
		Position aPosition = (Position) hrObj;
		for(PositionQualification aQual : aPosition.getQualificationList()) {
			aQual.setHrPositionId(aPosition.getHrPositionId());
			aQual.setPmQualificationId(null);
		}
		for(PositionDuty aDuty : aPosition.getDutyList()) {
			aDuty.setHrPositionId(aPosition.getHrPositionId());
			aDuty.setPmDutyId(null);
		}
		for(PstnFlag aFlag : aPosition.getFlagList()) {
			aFlag.setHrPositionId(aPosition.getHrPositionId());
			aFlag.setPmFlagId(null);
		}
		for(PositionFunding aFunding : aPosition.getFundingList()) {
			aFunding.setHrPositionId(aPosition.getHrPositionId());
			aFunding.setPmPositionFunctionId(null);
		}
        for(PositionDepartment aDepartment : aPosition.getDepartmentList()) {
            aDepartment.setHrPositionId(aPosition.getHrPositionId());
            aDepartment.setPmPositionDeptId(null);
        }
        for(PositionResponsibility aResponsibility : aPosition.getPositionResponsibilityList()) {
        	aResponsibility.setHrPositionId(aPosition.getHrPositionId());
        	aResponsibility.setPositionResponsibilityId(null);
        }
        
        // KPME-3016 populate institution and location here
        // We should be able to do this in addNewLineToCollection, but all the components are in "pages" now with the layout change, 
        // not on the form, and addNewLineToCollection doesn't get called. 
        if (aPosition.getDepartmentList() != null) {
        	for(PositionDepartment aPositionDepartment : aPosition.getDepartmentList()) {
        		if(aPositionDepartment != null && aPositionDepartment.getDeptAffl() != null) {
        			DepartmentAffiliation pda = (DepartmentAffiliation)aPositionDepartment.getDeptAfflObj();
        			if (pda.isPrimaryIndicator()) {
        				aPosition.setLocation(aPositionDepartment.getLocation());
        				aPosition.setInstitution(aPositionDepartment.getInstitution());
        				break;
        			}
        		}
        	}
        }

	}
	
	@Override
    protected boolean performAddLineValidation(View view, CollectionGroup collectionGroup, Object model,
            Object addLine) {
        boolean isValid = super.performAddLineValidation(view, collectionGroup, model, addLine);
        if (model instanceof MaintenanceDocumentForm) {
	        MaintenanceDocumentForm maintenanceForm = (MaintenanceDocumentForm) model;
	        MaintenanceDocument document = maintenanceForm.getDocument();
	        if (document.getNewMaintainableObject().getDataObject() instanceof Position) {
	        	Position aPosition = (Position) document.getNewMaintainableObject().getDataObject();
	        	// Duty line validation
		        if (addLine instanceof PositionDuty) {
		        	PositionDuty pd = (PositionDuty) addLine;
		        	boolean results = this.validateDutyListPercentage(pd, aPosition);
		        	if(!results) {
		        		return false;
		        	}
		        }
	        	// Funding line validation
		        if (addLine instanceof PositionFunding) {
		        	PositionFunding pf = (PositionFunding) addLine;
		        	boolean results = this.validateAddFundingLine(pf, aPosition);
		        	if(!results) {
		        		return false;
		        	}
		        }

                /*

                PositionDepartment -- if needed

                // Department line validation
                if (addLine instanceof PositionDepartment) {
                    //PositionDepartment pdpt = (PositionDepartment) addLine;
                    boolean results = this.validateAddDepartmentLine(aPosition);
                    if(!results) {
                        return false;
                    }
                }
                                 */
	        }
        }

        return isValid;
    }
	
	private boolean validateDutyListPercentage(PositionDuty pd, Position aPosition) {
		if(CollectionUtils.isNotEmpty(aPosition.getDutyList()) && pd.getPercentage() != null) {
			BigDecimal sum = pd.getPercentage();
			for(PositionDuty aDuty : aPosition.getDutyList()) {
				if(aDuty != null && aDuty.getPercentage() != null) {
					sum = sum.add(aDuty.getPercentage());
				}
			}
			if(sum.compareTo(new BigDecimal(100)) > 0) {
				GlobalVariables.getMessageMap().putError("Position-duties", "duty.percentage.exceedsMaximum", sum.toString());
				return false;
			}
		}		
		return true;
	}
	
	protected boolean validateAddFundingLine(PositionFunding pf, Position aPosition) {
    	if(pf.getEffectiveDate() != null && aPosition.getEffectiveDate() != null) {
    		if(pf.getEffectiveDate().compareTo(aPosition.getEffectiveDate()) < 0) {
    			String[] parameters = new String[2];
    			parameters[0] = pf.getEffectiveDate().toString();
    			parameters[1] = aPosition.getEffectiveDate().toString();
    			// using section id as the error key because KRAD does not support error matching on property names for collections as in 2.3M2
    			GlobalVariables.getMessageMap().putError("Position-fundings","error.funding.effdt.invalid", parameters);
   			 	return false;
    		}
    	}
    	if(StringUtils.isNotEmpty(pf.getAccount())) {
    		boolean results = ValidationUtils.validateAccount(pf.getChart(), pf.getAccount());
    		if(!results) {
    			GlobalVariables.getMessageMap().putError("Position-fundings", "error.existence", "Account '" + pf.getAccount() + "'");
    			return results;
    		}
    	}
    	if(StringUtils.isNotEmpty(pf.getSubAccount())) {
    		boolean results = ValidationUtils.validateSubAccount(pf.getSubAccount(), pf.getAccount(), pf.getChart());
    		if(!results) {
	   			 GlobalVariables.getMessageMap().putError("Position-fundings","error.existence", "SubAccount '" + pf.getSubAccount() + "'");
	   			 return results;
    		}
    	}
    	if(StringUtils.isNotEmpty(pf.getObjectCode())) {
    		boolean results = ValidationUtils.validateObjectCode(pf.getObjectCode(), pf.getChart(), Integer.valueOf(pf.getEffectiveLocalDate().getYear()));
    		if(!results) {
      			 GlobalVariables.getMessageMap().putError("Position-fundings","error.existence", "Objecpublic PositionDepartment getPositionDepartmentById(String pmPositionDeptId);tCode '" + pf.getObjectCode() + "'");
      			 return results;
    		}
    	}
    	if(StringUtils.isNotEmpty(pf.getSubObjectCode())) {
    		boolean results = ValidationUtils.validateSubObjectCode(String.valueOf(pf.getEffectiveLocalDate().getYear()),
    				pf.getChart(),
    				pf.getAccount(),
    				pf.getObjectCode(),
    				pf.getSubObjectCode());
    		if(!results) {
      			 GlobalVariables.getMessageMap().putError("Position-fundings","error.existence", "SubObjectCode '" + pf.getSubObjectCode() + "'");
      			 return results;
    		}
    	}
    	return true;
    
	}

     /*

     PositionDepartment -- if needed

    private boolean validateAddDepartmentLine(Position aPosition)  {
        if(CollectionUtils.isNotEmpty(aPosition.getDepartmentList())) {

            for(PositionDepartment aPosDept : aPosition.getDepartmentList()) {
                if(aPosDept != null && aPosDept.getEffectiveDate() != null) {
                      if(aPosDept.getHrPositionId() == null) {
                          return false;
                      }
                }
            }
        }
        return true;
     }
         */
	
	
	// KPME-3016
	//set document description here so it passes validation.  It will get overriden in doRouteStatusChange method
	@Override
	public void processAfterEdit(MaintenanceDocument document, Map<String, String[]> requestParameters) {
        document.getDocumentHeader().setDocumentDescription("New Position");
        super.processAfterEdit(document, requestParameters);
    }
	@Override 
	public void processAfterNew(MaintenanceDocument document, Map<String, String[]> requestParameters) {
        document.getDocumentHeader().setDocumentDescription("Edit Position");
		super.processAfterNew(document, requestParameters);
	} 
	@Override
    public void doRouteStatusChange(DocumentHeader documentHeader) {

		String docDescription = null;
		Position position = (Position)this.getDataObject();
		DocumentStatus documentStatus = documentHeader.getWorkflowDocument().getStatus();
	
		//Set document description for real here
		if (StringUtils.isEmpty(position.getPositionNumber())) {
			docDescription = "Status: " + position.getProcess();
		} else {
			docDescription = "Position Number: " + position.getPositionNumber() + " Status: " + position.getProcess();;
		}

		if (DocumentStatus.ENROUTE.equals(documentStatus)) {
			try {
				MaintenanceDocument md = (MaintenanceDocument)KRADServiceLocatorWeb.getDocumentService().getByDocumentHeaderId(documentHeader.getDocumentNumber());
		        md.getDocumentHeader().setDocumentDescription(docDescription);
		        md.getNewMaintainableObject().setDataObject(position);
		        KRADServiceLocatorWeb.getDocumentService().saveDocument(md);
			} catch (WorkflowException e) {
	            LOG.error("caught exception while handling doRouteStatusChange -> documentService.getByDocumentHeaderId(" + documentHeader.getDocumentNumber() + "). ", e);
	            throw new RuntimeException("caught exception while handling doRouteStatusChange -> documentService.getByDocumentHeaderId(" + documentHeader.getDocumentNumber() + "). ", e);
	        }
		}
    }

    //KPME-2624 added logic to save current logged in user to UserPrincipal id for collections
    @Override
    public void prepareForSave() {
        Position position = (Position)this.getDataObject();
        for (PositionFunding positionFunding : position.getFundingList()) {
            positionFunding.setUserPrincipalId(GlobalVariables.getUserSession().getPrincipalId());
        }
        for (PositionDepartment positionDepartment : position.getDepartmentList()) {
            positionDepartment.setUserPrincipalId(GlobalVariables.getUserSession().getPrincipalId());
        }
        for (PositionResponsibility positionResponsibility : position.getPositionResponsibilityList()) {
            positionResponsibility.setUserPrincipalId(GlobalVariables.getUserSession().getPrincipalId());
        }
        super.prepareForSave();
    }
}
