package org.kuali.hr.time.workarea;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.kuali.hr.time.roles.TkRole;
import org.kuali.hr.time.task.Task;
import org.kuali.hr.time.util.jaxb.DateAdapter;
import org.kuali.rice.kns.bo.PersistableBusinessObjectBase;

@XmlAccessorType(value = XmlAccessType.NONE)
public class WorkArea extends PersistableBusinessObjectBase {
    private static final long serialVersionUID = 1L;
    
    @XmlElement
    private Long tkWorkAreaId;
    
    @XmlElement(required=true, nillable=false)
    private Long workArea;
    
    @XmlElement(required=true, nillable=false)
    @XmlJavaTypeAdapter(DateAdapter.class)
    private Date effectiveDate;
    
    @XmlElement
    private boolean active = false;
    
    @XmlElement
    private String description;
    
    @XmlElement
    private String dept;
    
    @XmlElement
    private String defaultOvertimePreference;
    
    @XmlElement
    private String adminDescr;
    
    @XmlElement
    private String userPrincipalId;
    
    private Timestamp timestamp;

    private transient List<TkRole> roles = new ArrayList<TkRole>();
    private List<Task> tasks = new ArrayList<Task>();
    private List<WorkAreaOvertimePref> overTimePrefs = new ArrayList<WorkAreaOvertimePref>();


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


	public List<TkRole> getRoles() {
		return roles;
	}


	public void setRoles(List<TkRole> roles) {
		this.roles = roles;
	}


	public List<WorkAreaOvertimePref> getOverTimePrefs() {
		return overTimePrefs;
	}


	public void setOverTimePrefs(List<WorkAreaOvertimePref> overTimePrefs) {
		this.overTimePrefs = overTimePrefs;
	}
}