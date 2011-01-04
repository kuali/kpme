package org.kuali.hr.time.department;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.kuali.hr.time.roles.TkRole;
import org.kuali.kfs.coa.businessobject.Chart;
import org.kuali.kfs.coa.businessobject.Organization;
import org.kuali.rice.core.jaxb.SqlDateAdapter;
import org.kuali.rice.core.jaxb.SqlTimestampAdapter;
import org.kuali.rice.kns.bo.PersistableBusinessObjectBase;

/**
 * @author bsoohoo Add annotations for Web Services XML mapping
 * 
 */
@XmlAccessorType(value = XmlAccessType.NONE)
public class Department extends PersistableBusinessObjectBase implements
		Serializable {

	private static final long serialVersionUID = 1L;

	private Chart chartObj;
	private Organization orgObj;

	private Long tkDeptId;

	@XmlElement
	private String dept;

	@XmlElement
	private String description;

	@XmlElement
	private String chart;

	@XmlElement
	private String org;

	@XmlJavaTypeAdapter(value = SqlDateAdapter.class, type = Date.class)
	@XmlElement
	private Date effectiveDate;

	@XmlJavaTypeAdapter(value = SqlTimestampAdapter.class, type = Timestamp.class)
	@XmlElement
	private Timestamp timestamp;
    
    private List<TkRole> roles = new LinkedList<TkRole>();

	@XmlElement
	private Boolean active;

	@Override
	protected LinkedHashMap<String, Object> toStringMapper() {
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("dept", getDept());
		map.put("desc", getDescription());
		map.put("chart", getChart());
		map.put("org", getOrg());
		map.put("effectiveDate", getEffectiveDate());
		map.put("timestamp", getTimestamp());
		map.put("active", getActive());

		return map;
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

	public Long getTkDeptId() {
		return tkDeptId;
	}

	public void setTkDeptId(Long tkDeptId) {
		this.tkDeptId = tkDeptId;
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

}
