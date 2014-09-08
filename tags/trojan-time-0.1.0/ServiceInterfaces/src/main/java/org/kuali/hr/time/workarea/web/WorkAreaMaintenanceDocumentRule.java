package org.kuali.hr.time.workarea.web;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.hr.time.task.Task;
import org.kuali.hr.time.workarea.WorkArea;
import org.kuali.hr.time.workarea.WorkAreaMaintenanceDocument;
import org.kuali.rice.core.util.KeyLabelPair;
import org.kuali.rice.kim.service.KIMServiceLocator;
import org.kuali.rice.kns.document.Document;
import org.kuali.rice.kns.rules.TransactionalDocumentRuleBase;
import org.kuali.rice.kns.service.DataDictionaryService;
import org.kuali.rice.kns.service.KNSServiceLocator;
import org.kuali.rice.kns.util.GlobalVariables;

public class WorkAreaMaintenanceDocumentRule extends TransactionalDocumentRuleBase {

	private static Logger LOG = Logger.getLogger(WorkAreaMaintenanceDocumentRule.class);

	private static final String WORK_AREA_FIELD_NAME_ADMIN_DESCR = "adminDescr";
	private static final String WORK_AREA_FIELD_NAME_DESCR = "description";
	private static final String TASK_FIELD_NAME_DESCR = "description";
	private static final String TASK_FIELD_NAME_ADMIN_DESCR = "administrativeDescription";
	
	private DataDictionaryService ddservice;
	
	public WorkAreaMaintenanceDocumentRule() {
		ddservice = KNSServiceLocator.getDataDictionaryService();	
	}
	
	protected boolean validateDepartmentId(String deptId) {
		boolean v = true;
		
		// Need to build department service // do department lookup for .
		
		return v;
	}

	protected boolean validateOvertimePreference(String ovtPref) {
		boolean v = true;

		// TODO: What is considered valid here?
		if (StringUtils.isBlank(ovtPref)) {
			v = false;
			addError("document.workArea.overtimePreference", "error.required", "overtime preference");
		}

		// KeyLabelPair does not implement equals, so we need to search over
		// each key
		if (v) {
			boolean found = false;
			for (KeyLabelPair klp : WorkAreaOvertimePreferenceValuesFinder.labels) {
				found |= klp.getKey().equals(ovtPref);
			}
			if (!found) {
				v = false;
				addError("document.workArea.overtimePreference", "error.existence", "overtime preference");
			}
		}

		return v;
	}
	
	@SuppressWarnings("unchecked")
	private boolean genericDescriptionValidation(String desc, Class clazz, String errorKeyPrefix, String fieldName) {
		boolean v = true;

		Integer maxLength = ddservice.getAttributeMaxLength(clazz, fieldName);
		String  label = ddservice.getAttributeErrorLabel(clazz, fieldName);
		
		if (v && StringUtils.isBlank(desc)) {
			v = false;
			addError(errorKeyPrefix+fieldName, "error.required", label);
		}
		
		if (v && desc.length() > maxLength) {
			v = false;
			addError(errorKeyPrefix+fieldName, "error.maxLength", label, maxLength.toString());
		}

		return v;		
	}

	protected boolean validateAdminDescription(String desc) {
		return genericDescriptionValidation(desc, WorkArea.class, "document.workArea.", WORK_AREA_FIELD_NAME_ADMIN_DESCR);
	}

	protected boolean validateDescription(String desc) {
		return genericDescriptionValidation(desc, WorkArea.class, "document.workArea.", WORK_AREA_FIELD_NAME_DESCR);
	}

	protected boolean validateTasks(List<Task> list) {
		boolean v = true;

		for (int i = 0; i < list.size(); i++) {
			Task task = list.get(i);
			v &= validateTaskGeneric("document.workArea.tasks[" + i + "]", task);
		}

		return v;
	}

	public boolean validate(WorkAreaMaintenanceDocument wamd) {
		boolean valid = false;

		WorkArea wa = (wamd != null) ? wamd.getWorkArea() : null;
		if (wa != null) {
			valid = true;
			valid &= this.validateDepartmentId(wa.getDept());
			//TODO add back if you need this
			//valid &= this.validateOvertimePreference(wa.getOvertimePreference());
			valid &= this.validateAdminDescription(wa.getAdminDescr());
			valid &= this.validateDescription(wa.getDescription());
			valid &= this.validateTasks(wa.getTasks());
		}

		return valid;
	}

	public boolean validateTaskAddition(Task task, List<Task> list) {
		return validateTaskGeneric("newTask", task);
	}

	public boolean validateTaskGeneric(String errorPrefix, Task task) {
		boolean v = true;

		if (task == null) {
			v = false;
			addError(errorPrefix, "error.required", "task");
		}

		if (v && StringUtils.isBlank(task.getDescription())) {
			v = false;
			addError(errorPrefix + ".description", "error.required", "description");
		}
		
		if (v) {
			v = genericDescriptionValidation(task.getDescription(), Task.class, errorPrefix+".", TASK_FIELD_NAME_DESCR);
		}
		
		if (v && StringUtils.isBlank(task.getAdministrativeDescription())) {
			v = false;
			addError(errorPrefix + ".adminDescription", "error.required", "admin description");
		}
		
		if (v) {
			v = genericDescriptionValidation(task.getAdministrativeDescription(), Task.class, errorPrefix+".", TASK_FIELD_NAME_ADMIN_DESCR);
		}

		return v;
	}
	
	@Override
	protected boolean processCustomSaveDocumentBusinessRules(Document document) {
		LOG.debug("Validation called from rice");
		WorkAreaMaintenanceDocument wamd;
		boolean v = true;

		if (!(document instanceof WorkAreaMaintenanceDocument))
			v = false;

		wamd = (WorkAreaMaintenanceDocument) document;
		v = this.validate(wamd);

		return v;
	}

	private void addError(String formId, String errorCode, String... params) {
		GlobalVariables.getMessageMap().putError(formId, errorCode, params);
	}
}