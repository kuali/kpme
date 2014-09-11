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
package org.kuali.kpme.core.tkmdocument.web;


import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.kuali.kpme.core.api.assignment.AssignmentContract;
import org.kuali.kpme.core.api.job.Job;
import org.kuali.kpme.core.api.job.JobContract;
import org.kuali.kpme.core.api.namespace.KPMENamespace;
import org.kuali.kpme.core.assignment.AssignmentBo;
import org.kuali.kpme.core.assignment.account.AssignmentAccountBo;
import org.kuali.kpme.core.bo.HrKeyedBusinessObject;
import org.kuali.kpme.core.cache.CacheUtils;
import org.kuali.kpme.core.job.JobBo;
import org.kuali.kpme.core.principal.PrincipalHRAttributesBo;
import org.kuali.kpme.core.role.KPMERole;
import org.kuali.kpme.core.service.HrServiceLocator;
import org.kuali.kpme.core.tkmdocument.KhrEmployeeDocument;
import org.kuali.kpme.core.tkmdocument.tkmjob.KhrEmployeeJob;
import org.kuali.kpme.core.tkmdocument.validation.KhrEmployeeDocumentValidation;
import org.kuali.kpme.core.util.HrContext;
import org.kuali.kpme.core.util.TKUtils;
import org.kuali.rice.core.api.mo.ModelObjectUtils;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.maintenance.MaintainableImpl;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.uif.container.CollectionGroup;
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.web.form.MaintenanceDocumentForm;

import de.danielbechler.diff.Configuration;
import de.danielbechler.diff.ObjectDiffer;
import de.danielbechler.diff.ObjectDifferFactory;
import de.danielbechler.diff.node.Node;
import de.danielbechler.diff.path.PropertyPath;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class KhrEmployeeMaintainableImpl extends MaintainableImpl {
    
	private static final long serialVersionUID = 1140415771858326498L;
	
	protected static Logger LOG = Logger.getLogger(KhrEmployeeMaintainableImpl.class);
	
	
    @Override
    public Object retrieveObjectForEditOrCopy(MaintenanceDocument document, Map<String, String> dataObjectKeys) {
        KhrEmployeeDocument tkmDocument = new KhrEmployeeDocument();
        String principalId = dataObjectKeys.get("principalId");

        LOG.info("Data Object Keys: " + dataObjectKeys.toString());
        //set person section
        Person person = KimApiServiceLocator.getPersonService().getPerson(principalId);
        tkmDocument.setPrincipalId(principalId);
        tkmDocument.setName(person.getName());
        //TODO: figure out Kim Override start and endDates
        tkmDocument.setStartDate(TKUtils.convertDateStringToDateTime("01/01/2010", "00:00:00").toDate());
        tkmDocument.setEndDate(null);

        //set editable department list
        tkmDocument.setEditableDepartmentList(HrServiceLocator.getKPMERoleService().getDepartmentsForPrincipalInRole(HrContext.getTargetPrincipalId(), KPMENamespace.KPME_TK.getNamespaceCode(), KPMERole.TIME_DEPARTMENT_ADMINISTRATOR.getRoleName(), LocalDate.now().toDateTimeAtStartOfDay(), true));

        //set job section
        CacheUtils.flushCache(JobContract.CACHE_NAME);
        List<Job> jobList = HrServiceLocator.getJobService().getJobs(principalId, LocalDate.now());

        List<KhrEmployeeJob> tkmJobs = new ArrayList<KhrEmployeeJob>();
        for (Job job : jobList) {
            KhrEmployeeJob tkmJob = createTKMJob(job);
            //TODO set end date

            Job nextInactiveJob = HrServiceLocator.getJobService().getNextInactiveJob(job);
            if (nextInactiveJob != null) {
                tkmJob.setEndDate(nextInactiveJob.getEffectiveLocalDate().toDate());
            }

            //add assignment list to job
            CacheUtils.flushCache(AssignmentContract.CACHE_NAME);
            tkmJob.setAssignments(ModelObjectUtils.transform(HrServiceLocator.getAssignmentService().getActiveAssignmentsForJob(principalId, tkmJob.getJobNumber(), LocalDate.now()), AssignmentBo.toAssignmentBo));
            tkmJobs.add(tkmJob);

        }

        tkmDocument.setJobList(tkmJobs);
        return tkmDocument;
    }

    @Override
    public void saveDataObject() {
        KhrEmployeeDocument tkmDocument = (KhrEmployeeDocument) this.getDataObject();

        List<KhrEmployeeJob> tkmJobList = tkmDocument.getJobList();

        // iterate thru the job list versioning those that are not being newly added, and have been changed
        for (KhrEmployeeJob currentTkmJob : tkmJobList) {
        	boolean saveCurrentInNewRow = true; // this flag will determine if we actually save this job bo or not
    		if(currentTkmJob.getId() != null){
    			// first get the corresponding old job BO to compare
    			JobBo oldJob = this.getBusinessObjectService().findBySinglePrimaryKey(JobBo.class, currentTkmJob.getId());
    			if(oldJob!= null) {
    				if( !(representTheSameJob(oldJob, currentTkmJob)) ) {
    					//if the effective dates are the same do not create a new row just inactivate the old one
    					if(currentTkmJob.getEffectiveDate().equals(oldJob.getEffectiveDate())){
    						oldJob.setActive(false);
    						oldJob.setTimestamp(TKUtils.subtractOneSecondFromTimestamp(new Timestamp(DateTime.now().getMillis())));
    					} 
    					else {
	    					//if effective dates not the same, add a new row that inactivates the old entry based on the new effective date
	    					oldJob.setTimestamp(TKUtils.subtractOneSecondFromTimestamp(new Timestamp(DateTime.now().getMillis())));
	    					oldJob.setEffectiveDate(currentTkmJob.getEffectiveDate());
	    					oldJob.setActive(false);
	    					oldJob.setId(null);
    					}
    					// save the older version - will trigger either an UPDATE or an INSERT depending on effective date remaining same or not.
    					KRADServiceLocator.getBusinessObjectService().save(oldJob);
    				}
    				else {
    					// the old and new are the same, so no need to save as new row
    					saveCurrentInNewRow = false;
    				}
    			}
    		}
    		// finally save the new job object as a new row, unless not needed
    		if(saveCurrentInNewRow) {
    			currentTkmJob.setTimestamp(new Timestamp(System.currentTimeMillis()));
    			currentTkmJob.setId(null);
    			getBusinessObjectService().save(currentTkmJob);
    			currentTkmJob.setUserPrincipalId(GlobalVariables.getUserSession().getPrincipalId());
    		}


            JobBo nextInactiveJob = JobBo.from(HrServiceLocator.getJobService().getNextInactiveJob(JobBo.to(currentTkmJob)));
            //if current job end date is blank and there is a future inactive job, delete that future inactive job
            if (currentTkmJob.getEndDate() == null) {
            	if(nextInactiveJob != null) {
            		getBusinessObjectService().delete(nextInactiveJob);
            	}
            }
            //if end date is present create/update an inactive job
            else {
            	//if inactive job exists update it 
                if (nextInactiveJob != null) {
                    nextInactiveJob.setEffectiveDate(currentTkmJob.getEndDate());
                    getBusinessObjectService().linkAndSave(nextInactiveJob);
                } 
                // else create new
                else {
                	KhrEmployeeJob inactiveJob = createTKMJob(currentTkmJob);
                    inactiveJob.setUserPrincipalId(GlobalVariables.getUserSession().getPrincipalId());
                    inactiveJob.setHrJobId(null);
                    inactiveJob.setObjectId(null);
                    inactiveJob.setVersionNumber(null);
                    inactiveJob.setActive(false);
                    inactiveJob.setEffectiveDate(currentTkmJob.getEndDate());
                	getBusinessObjectService().linkAndSave(inactiveJob);
                }
            }
            
        	
            for(AssignmentBo assignment: currentTkmJob.getAssignments()) {
                getBusinessObjectService().linkAndSave(assignment);
            }
        }

        //edit hr principal attributes
        List<PrincipalHRAttributesBo> activeAttributes = ModelObjectUtils.transform(HrServiceLocator.getPrincipalHRAttributeService().getAllActivePrincipalHrAttributesForPrincipalId(tkmDocument.getPrincipalId(), LocalDate.now()), PrincipalHRAttributesBo.toBo);

        if (CollectionUtils.isEmpty(activeAttributes)) {
            PrincipalHRAttributesBo newAttributes = new PrincipalHRAttributesBo();
            newAttributes.setEffectiveDate(LocalDate.now().toDate());
            newAttributes.setPrincipalId(tkmDocument.getPrincipalId());
            //todo create parameter for default calendar
            newAttributes.setPayCalendar("ISU-SEMI-MNTHLY");
            newAttributes.setActive(true);

            getBusinessObjectService().linkAndSave(newAttributes);
        }

    }


    protected boolean representTheSameJob(JobBo oldJob, KhrEmployeeJob currentTkmJob) {
    	boolean retVal = true;
    	KhrEmployeeJob oldTkmJob = createTKMJob(oldJob);
    	final Configuration configuration = new Configuration();
    	configuration.withoutProperty(PropertyPath.buildWith("businessKeyValuesMap"));
        configuration.withoutProperty(PropertyPath.buildWith("relativeEffectiveDate"));
        configuration.withoutProperty(PropertyPath.buildWith("effectiveLocalDate"));
        configuration.withoutProperty(PropertyPath.buildWith("userPrincipalId"));
        
        configuration.withoutProperty(PropertyPath.buildWith("groupKey"));
        configuration.withoutProperty(PropertyPath.buildWith("locationObj"));
        configuration.withoutProperty(PropertyPath.buildWith("location"));        
        configuration.withoutProperty(PropertyPath.buildWith("institutionObj"));
        configuration.withoutProperty(PropertyPath.buildWith("institution"));
        
        configuration.withoutProperty(PropertyPath.buildWith("assignments"));
        configuration.withoutProperty(PropertyPath.buildWith("endDate"));
        
        configuration.withoutProperty(PropertyPath.buildWith("fte"));
        configuration.withoutProperty(PropertyPath.buildWith("deptObj"));
        configuration.withoutProperty(PropertyPath.buildWith("payTypeObj"));
        configuration.withoutProperty(PropertyPath.buildWith("payGradeObj"));
        configuration.withoutProperty(PropertyPath.buildWith("payTypeObj"));
        configuration.withoutProperty(PropertyPath.buildWith("salaryGroupObj"));
        configuration.withoutProperty(PropertyPath.buildWith("positionObj"));
        configuration.withoutProperty(PropertyPath.buildWith("principalName"));
        configuration.withoutProperty(PropertyPath.buildWith("name"));
        
    	ObjectDiffer objectDiffer = ObjectDifferFactory.getInstance(configuration);
        final Node root = objectDiffer.compare(currentTkmJob, oldTkmJob);
        if(root.hasChanges()) {
        	retVal = false;
        }
        return retVal;
	}
    
    

	@Override
    protected void processAfterAddLine(View view, CollectionGroup collectionGroup, Object model, Object addLine,
                                       boolean isValidLine) {
        super.processAfterAddLine(view, collectionGroup, model, addLine, isValidLine);

        MaintenanceDocumentForm maintenanceDocument = (MaintenanceDocumentForm) model;
        KhrEmployeeDocument tkmDocument = (KhrEmployeeDocument) maintenanceDocument.getDocument().getNewMaintainableObject().getDataObject();

        //add additional fields to job
        if (addLine instanceof KhrEmployeeJob) {
            KhrEmployeeJob addJob = (KhrEmployeeJob) addLine;
            addJob.setPrincipalId(tkmDocument.getPrincipalId());
            addJob.setActive(true);

            //set job number
            Job maxJob = HrServiceLocator.getJobService().getMaxJob(tkmDocument.getPrincipalId());
            if(maxJob != null) {
                addJob.setJobNumber(maxJob.getJobNumber() +1);
            } else {
                addJob.setJobNumber(0L);
            }
        }

        //add additional assignment fields
        if (addLine instanceof AssignmentBo) {
            AssignmentBo addAssignment = (AssignmentBo) addLine;

            //determine which job the assignment is being added to
            long jobNumber = 0L;
            for (KhrEmployeeJob job : tkmDocument.getJobList()) {
                if (job.getAssignments().contains(addLine)) {
                    jobNumber = job.getJobNumber();
                }
            }

            addAssignment.setPrincipalId(tkmDocument.getPrincipalId());
            addAssignment.setJobNumber(jobNumber);
            addAssignment.setActive(true);
        }

    }
    
    // set the khr employee job's field values that are not set via the "add job" user interface, but are needed for authorization.
    protected void processBeforeAddLine(View view, CollectionGroup collectionGroup, Object model, Object addLine) {
    	 if (addLine instanceof HrKeyedBusinessObject) {
    		 ((HrKeyedBusinessObject) addLine).setGroupKeyCode("ISU-IA");
    		 ((HrKeyedBusinessObject) addLine).setUserPrincipalId(GlobalVariables.getUserSession().getPrincipalId());
    	 }
    	 else if(addLine instanceof AssignmentAccountBo) {
    		 ((AssignmentAccountBo) addLine).setUserPrincipalId(GlobalVariables.getUserSession().getPrincipalId());
    	 }
    }

    @Override
    protected void processAfterDeleteLine(View view, CollectionGroup collectionGroup, Object model, int lineIndex) {
        //since the delete action is only available for new lines, we don't need to delete the line from the old data object
        //which was causing errors in sub collections
    }

    @Override
    protected boolean performAddLineValidation(View view, CollectionGroup collectionGroup, Object model, Object addLine) {
        boolean isValid = true;

        if (addLine instanceof KhrEmployeeJob) {
            KhrEmployeeJob tkmJob = (KhrEmployeeJob) addLine;
            //check permission reusing the rules class
            if ( !(((new KhrEmployeeDocumentValidation())).canMaintainJob(tkmJob)) ) {
                String[] params = new String[1];
                params[0] = tkmJob.getDept();
                GlobalVariables.getMessageMap().putError("newCollectionLines['document.newMaintainableObject.dataObject.jobList'].dept","tkmdocument.job.permissions",params);
                isValid = false;
            }
        }

        return isValid;
    }
    private KhrEmployeeJob createTKMJob(JobContract job) {
        KhrEmployeeJob tkmJob = new KhrEmployeeJob();

        //copy job to tkmjob
        tkmJob.setGroupKeyCode(job.getGroupKeyCode());
        tkmJob.setHrPayType(job.getHrPayType());
        tkmJob.setPayGrade(job.getPayGrade());
        tkmJob.setStandardHours(job.getStandardHours());
        tkmJob.setHrJobId(job.getHrJobId());
        tkmJob.setPrincipalId(job.getPrincipalId());
        tkmJob.setFirstName(job.getFirstName());
        tkmJob.setLastName(job.getLastName());
        tkmJob.setPrincipalName(job.getPrincipalName());
        tkmJob.setJobNumber(job.getJobNumber());
        tkmJob.setDept(job.getDept());
        tkmJob.setHrSalGroup(job.getHrSalGroup());
        tkmJob.setPrimaryIndicator(job.isPrimaryJob());
        tkmJob.setCompRate(job.getCompRate());
        tkmJob.setPositionNumber(job.getPositionNumber());
        tkmJob.setEligibleForLeave(job.isEligibleForLeave());
        tkmJob.setFlsaStatus(job.getFlsaStatus());
        tkmJob.setEffectiveDate(job.getEffectiveLocalDate().toDate());
        tkmJob.setVersionNumber(job.getVersionNumber());
        tkmJob.setObjectId(job.getObjectId());
        tkmJob.setActive(job.isActive());
        tkmJob.setUserPrincipalId(job.getUserPrincipalId());

        return tkmJob;
    }
}