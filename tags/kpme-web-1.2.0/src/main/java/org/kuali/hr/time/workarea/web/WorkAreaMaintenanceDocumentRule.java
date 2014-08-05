/**
 * Copyright 2004-2012 The Kuali Foundation
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
package org.kuali.hr.time.workarea.web;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.hr.time.assignment.Assignment;
import org.kuali.hr.time.authorization.DepartmentalRule;
import org.kuali.hr.time.authorization.DepartmentalRuleAuthorizer;
import org.kuali.hr.time.roles.TkRole;
import org.kuali.hr.time.service.base.TkServiceLocator;
import org.kuali.hr.time.task.Task;
import org.kuali.hr.time.util.TKContext;
import org.kuali.hr.time.util.TKUtils;
import org.kuali.hr.time.util.TkConstants;
import org.kuali.hr.time.util.ValidationUtils;
import org.kuali.hr.time.workarea.WorkArea;
import org.kuali.rice.kns.document.MaintenanceDocument;
import org.kuali.rice.kns.maintenance.rules.MaintenanceDocumentRuleBase;
import org.kuali.rice.krad.bo.PersistableBusinessObject;
import org.kuali.rice.krad.util.GlobalVariables;

public class WorkAreaMaintenanceDocumentRule extends
		MaintenanceDocumentRuleBase {

	private static Logger LOG = Logger
			.getLogger(WorkAreaMaintenanceDocumentRule.class);

	boolean validateDepartment(String dept, Date asOfDate) {
		boolean valid = ValidationUtils.validateDepartment(dept, asOfDate);
		if (!valid) {
			this.putFieldError("dept", "dept.notfound");
		}
		return valid;
	}

	boolean validateRoles(List<TkRole> roles, Date effectiveDate) {
		boolean valid = false;
		
		if (roles != null && roles.size() > 0) {
			int pos = 0;
			for (TkRole role : roles) {
				valid |= role.isActive();
				if(role.getRoleName().equalsIgnoreCase(TkConstants.ROLE_TK_APPROVER_DELEGATE)){
					StringBuffer prefix = new StringBuffer("roles[");
		            prefix.append(pos).append("].");
					if (role.getExpirationDate() == null) {
						this.putFieldError(prefix + "expirationDate",
								"error.role.expiration.required");
					} else if (role.getEffectiveDate().compareTo(role.getExpirationDate()) >= 0) {
						this.putFieldError(prefix + "expirationDate",
								"error.role.expiration");
					} else if (TKUtils.getDaysBetween(role.getEffectiveDate(), role.getExpirationDate()) > 180) {
		        		   this.putFieldError(prefix + "expirationDate",
		     						"error.role.expiration.duration");
		     				valid = false;
		        	}
				}
				pos++;
			}
		}

		if (!valid) {
			this.putGlobalError("role.required");
		}

		return valid;
	}

	boolean validateTask(List<TkRole> roles) {
		boolean valid = false;

		if (roles != null && roles.size() > 0) {
			for (TkRole role : roles) {
				valid |= role.isActive()
						&& StringUtils.equals(role.getRoleName(),
								TkConstants.ROLE_TK_APPROVER);
			}
		}

		if (!valid) {
			this.putGlobalError("role.required");
		}

		return valid;
	}
	
	boolean validateTask(Task task, List<Task> tasks) {
		boolean valid = true;
		
		for (Task t : tasks) {
			if (t.getTask().equals(task.getTask()) ) {
				this.putGlobalError("error.duplicate.entry", "task '" + task.getTask() + "'");
				valid = false;
			}
		}
		return valid;
	}
	
	boolean validateDefaultOTEarnCode(String earnCode, Date asOfDate) {
		// defaultOvertimeEarnCode is a nullable field. 
		if (earnCode != null
				&& !ValidationUtils.validateEarnCode(earnCode, asOfDate)) {
			this.putFieldError("defaultOvertimeEarnCode", "error.existence", "earnCode '"
					+ earnCode + "'");
			return false;
		} else {
			if (earnCode != null
					&& !ValidationUtils.validateEarnCode(earnCode, true, asOfDate)) {
				this.putFieldError("defaultOvertimeEarnCode", "earncode.ovt.required",
						earnCode);
				return false;
			}
			return true;
		}
	}

	@Override
	protected boolean processCustomRouteDocumentBusinessRules(
			MaintenanceDocument document) {
		boolean valid = false;

		PersistableBusinessObject pbo = (PersistableBusinessObject) this.getNewBo();
		if (pbo instanceof WorkArea) {
			WorkArea wa = (WorkArea) pbo;
			valid = validateDepartment(wa.getDept(), wa.getEffectiveDate());
			if(!DepartmentalRuleAuthorizer.hasAccessToWrite((DepartmentalRule)pbo)) {
				String[] params = new String[]{GlobalVariables.getUserSession().getPrincipalName(), wa.getDept()};
				this.putFieldError("dept", "dept.user.unauthorized", params);
			}
			valid &= validateRoles(wa.getRoles(), wa.getEffectiveDate());
			// defaultOvertimeEarnCode is a nullable field. 
			if ( wa.getDefaultOvertimeEarnCode() != null ){
				valid &= validateDefaultOTEarnCode(wa.getDefaultOvertimeEarnCode(),
						wa.getEffectiveDate());
			}
			
			if(!wa.isActive()){
				List<Assignment> assignments = TkServiceLocator.getAssignmentService().getActiveAssignmentsForWorkArea(wa.getWorkArea(), wa.getEffectiveDate());
				for(Assignment assignment: assignments){
					if(assignment.getWorkArea().equals(wa.getWorkArea())){
						this.putGlobalError("workarea.active.required");
						valid = false;
						break;
					}
				}
			}else{
				List<Long> inactiveTasks = new ArrayList<Long>();
				for (Task task : wa.getTasks()) {
					if(!task.isActive()){
						inactiveTasks.add(task.getTask());
					}
				}
				
				if(!inactiveTasks.isEmpty()){
					List<Assignment> assignments = TkServiceLocator.getAssignmentService().getActiveAssignmentsForWorkArea(wa.getWorkArea(), wa.getEffectiveDate());
					for(Assignment assignment : assignments){
						for(Long inactiveTask : inactiveTasks){
							if(inactiveTask.equals(assignment.getTask())){
								this.putGlobalError("task.active.required", inactiveTask.toString());
								valid = false;
							}
						}
					}
				}
				
			}
		}

		return valid;
	}

	@Override
	public boolean processCustomAddCollectionLineBusinessRules(
			MaintenanceDocument document, String collectionName,
			PersistableBusinessObject line) {
		boolean valid = false;
		LOG.debug("entering custom validation for Task");
		PersistableBusinessObject pboWorkArea = document.getDocumentBusinessObject();
		PersistableBusinessObject pbo = line;
		if (pbo instanceof Task && pboWorkArea instanceof WorkArea) {
			WorkArea wa = (WorkArea) pboWorkArea;
			
			Task task = (Task) pbo;
			
			if (task != null && wa.getTasks() != null) {
				valid = true;
				valid &= this.validateTask(task, wa.getTasks());
				// KPME-870
				if ( valid ){
					if (task.getTask() == null){
						Long maxTaskNumberInTable = this.getTaskNumber(wa);
						Long maxTaskNumberOnPage = 0L;
						if(wa.getTasks().size() > 0){
							maxTaskNumberOnPage = wa.getTasks().get((wa.getTasks().size()) -1 ).getTask();
						}
						
						if ( maxTaskNumberOnPage.compareTo(maxTaskNumberInTable) >= 0){
							task.setTask(maxTaskNumberOnPage + 1);
						} else {
							task.setTask(maxTaskNumberInTable);
						}
						task.setWorkArea(wa.getWorkArea());
						task.setTkWorkAreaId(wa.getTkWorkAreaId());
					}
				}
			}
		} else if ((pbo instanceof TkRole && pboWorkArea instanceof WorkArea)) {
			TkRole tkRole = (TkRole)pbo;
			valid = true;
			if(StringUtils.isEmpty(tkRole.getPrincipalId())) {
				if (tkRole.getPositionNumber() == null || StringUtils.isEmpty(tkRole.getPositionNumber().toString())){
					this.putGlobalError("principal.position.required");
				}
			}
		}
		
		return valid;
	}

	public Long getTaskNumber(WorkArea workArea) {
		Long task = new Long("0");
		
		Task maxTaskInTable = TkServiceLocator.getTaskService().getMaxTask();
		if(maxTaskInTable != null) {
			// get the max of task number of the collection
			task = maxTaskInTable.getTask() +1;
		} else {
			task = new Long("100");
		}
		
		return task;
	}
}