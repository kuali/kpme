package org.kuali.hr.time.department;

import org.kuali.hr.location.Location;
import org.kuali.hr.time.HrBusinessObject;
import org.kuali.hr.time.roles.TkRole;
import org.kuali.kfs.coa.businessobject.Chart;
import org.kuali.kfs.coa.businessobject.Organization;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public class Department extends HrBusinessObject {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String hrDeptId;
    private String dept;
    private String description;
    private String chart;
    private String org;
    private String location;

    private Chart chartObj;
    private Organization orgObj;
    private Location locationObj;
    
    private List<TkRole> roles = new LinkedList<TkRole>();

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    protected LinkedHashMap toStringMapper() {
    	LinkedHashMap<String, String> lhm = new LinkedHashMap<String,String>();
    	lhm.put(dept, dept);
    	return lhm;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getChart() {
        return chart;
    }

    public void setChart(String chart) {
        this.chart = chart;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

	public String getHrDeptId() {
		return hrDeptId;
	}

	public void setHrDeptId(String hrDeptId) {
		this.hrDeptId = hrDeptId;
	}

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Chart getChartObj() {
		return chartObj;
	}

	public void setChartObj(Chart chartObj) {
		this.chartObj = chartObj;
	}

	public Organization getOrgObj() {
		return orgObj;
	}

	public void setOrgObj(Organization orgObj) {
		this.orgObj = orgObj;
	}

	public List<TkRole> getRoles() {
		return roles;
	}

	public void setRoles(List<TkRole> roles) {
		this.roles = roles;
	}

	@Override
	protected String getUniqueKey() {
		return getDept() + "_" + getOrg() + "_" + getChart() + getRoles().size();
	}

	@Override
	public String getId() {
		return getHrDeptId();
	}

	@Override
	public void setId(String id) {
		setHrDeptId(id);
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Location getLocationObj() {
		return locationObj;
	}

	public void setLocationObj(Location locationObj) {
		this.locationObj = locationObj;
	}
}