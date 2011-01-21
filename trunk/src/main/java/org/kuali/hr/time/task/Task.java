package org.kuali.hr.time.task;

import org.kuali.rice.kns.bo.PersistableBusinessObjectBase;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.LinkedHashMap;

public class Task extends PersistableBusinessObjectBase {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Long tkTaskId;
    private Long task;
    private Long workArea;
    private Long tkWorkAreaId;
    private String description;
    private String userPrincipalId;
    private String administrativeDescription;
    private Date effectiveDate;
    private boolean active = true;
    private Timestamp timestamp;

	@SuppressWarnings("unchecked")
	@Override
	protected LinkedHashMap toStringMapper() {
		LinkedHashMap<String, Object> toStringMap = new LinkedHashMap<String,Object>();
		toStringMap.put("tkTaskId", tkTaskId);
		toStringMap.put("task", task);
		toStringMap.put("workArea", workArea);
		toStringMap.put("tkWorkAreaId", tkWorkAreaId);
		toStringMap.put("description", description);
		toStringMap.put("userPrincipalId", userPrincipalId);
		toStringMap.put("administrativeDescription", administrativeDescription);
		return toStringMap;
	}

    public String getDescription()
    {
    return description;
    }

    public void setDescription(String desc)
    {
    	this.description = desc;
    }

    public String getUserPrincipalId(){
    	return userPrincipalId;
    }

    public void setUserPrincipalId(String pid)
    {
    	this.userPrincipalId = pid;
    }

    public String getAdministrativeDescription(){
    	return administrativeDescription;
    }

    public void setAdministrativeDescription(String addesc)
    {
    	this.administrativeDescription = addesc;
    }

	public Long getTkTaskId() {
		return tkTaskId;
	}

	public void setTkTaskId(Long tkTaskId) {
		this.tkTaskId = tkTaskId;
	}

	public Long getTask() {
		return task;
	}

	public void setTask(Long task) {
		this.task = task;
	}

	public Long getWorkArea() {
		return workArea;
	}

	public void setWorkArea(Long workArea) {
		this.workArea = workArea;
	}

	public Long getTkWorkAreaId() {
		return tkWorkAreaId;
	}

	public void setTkWorkAreaId(Long tkWorkAreaId) {
		this.tkWorkAreaId = tkWorkAreaId;
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

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

}
