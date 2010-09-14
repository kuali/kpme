package org.kuali.hr.time.workarea;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.kuali.hr.time.role.assign.TkRoleAssign;
import org.kuali.hr.time.task.Task;
import org.kuali.rice.kns.bo.PersistableBusinessObjectBase;

public class WorkArea extends PersistableBusinessObjectBase {

    private static final long serialVersionUID = 1L;

    private Long tkWorkAreaId;
    private Long workArea;
    private Date effectiveDate;
    private boolean active = false;
    private String description;
    private String dept;
    private String defaultOvertimePreference;
    private String adminDescr;
    private String userPrincipalId;
    private Timestamp timestamp;

    private transient List<TkRoleAssign> roleAssignments = new ArrayList<TkRoleAssign>();
    private List<Task> tasks = new ArrayList<Task>();


    @SuppressWarnings("unchecked")
    @Override
    protected LinkedHashMap toStringMapper() {
	LinkedHashMap<String, Object> toStringMap = new LinkedHashMap<String,Object>();
	toStringMap.put("workAreaId", tkWorkAreaId);
	return toStringMap;
    }


    public Date getEffectiveDate() {
        return effectiveDate;
    }


    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }


    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }


    public String getAdminDescr() {
        return adminDescr;
    }


    public void setAdminDescr(String adminDescr) {
        this.adminDescr = adminDescr;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }


    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserPrincipalId() {
        return userPrincipalId;
    }


    public void setUserPrincipalId(String userPrincipalId) {
        this.userPrincipalId = userPrincipalId;
    }

    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }


	public List<TkRoleAssign> getRoleAssignments() {
		return roleAssignments;
	}


	public void setRoleAssignments(List<TkRoleAssign> roleAssignments) {
		this.roleAssignments = roleAssignments;
	}


	public List<Task> getTasks() {
	    return tasks;
	}


	public void setTasks(List<Task> tasks) {
	    this.tasks = tasks;
	}


	public Long getTkWorkAreaId() {
		return tkWorkAreaId;
	}


	public void setTkWorkAreaId(Long tkWorkAreaId) {
		this.tkWorkAreaId = tkWorkAreaId;
	}


	public Long getWorkArea() {
		return workArea;
	}


	public void setWorkArea(Long workArea) {
		this.workArea = workArea;
	}


	public String getDept() {
		return dept;
	}


	public void setDept(String dept) {
		this.dept = dept;
	}


	public String getDefaultOvertimePreference() {
		return defaultOvertimePreference;
	}


	public void setDefaultOvertimePreference(String defaultOvertimePreference) {
		this.defaultOvertimePreference = defaultOvertimePreference;
	}
}
