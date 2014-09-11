package org.kuali.hr.time.timesummary;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kuali.hr.time.util.TkConstants;

public class EarnGroupSection {
	private String earnGroup;
	private Map<String, EarnCodeSection> earnCodeToEarnCodeSectionMap = new HashMap<String, EarnCodeSection>();
	private List<EarnCodeSection> earnCodeSections = new ArrayList<EarnCodeSection>();
	private List<BigDecimal> totals = new ArrayList<BigDecimal>();
	public String getEarnGroup() {
		return earnGroup;
	}
	public void setEarnGroup(String earnGroup) {
		this.earnGroup = earnGroup;
	}

	public List<BigDecimal> getTotals() {
		return totals;
	}
	
	public void addEarnCodeSection(EarnCodeSection earnCodeSection, List<Boolean> dayArrangements){
		for(AssignmentRow assignRow : earnCodeSection.getAssignmentsRows()){
			for(int i = 0;i<(assignRow.getTotal().size()-1);i++){
				BigDecimal value = totals.get(i).add(assignRow.getTotal().get(i), TkConstants.MATH_CONTEXT);
				totals.set(i, value.setScale(TkConstants.BIG_DECIMAL_SCALE, TkConstants.BIG_DECIMAL_SCALE_ROUNDING));
			}
		}
		earnCodeToEarnCodeSectionMap.put(earnCodeSection.getEarnCode(), earnCodeSection);
		earnCodeSections.add(earnCodeSection);
		
		BigDecimal periodTotal = BigDecimal.ZERO;
		for(int i =0;i<totals.size()-2;i++){
			if(dayArrangements.get(i)){
				periodTotal = periodTotal.add(totals.get(i), TkConstants.MATH_CONTEXT);
			}
		}
		totals.set(totals.size()-1, periodTotal);
	}
	public Map<String, EarnCodeSection> getEarnCodeToEarnCodeSectionMap() {
		return earnCodeToEarnCodeSectionMap;
	}
	public void setEarnCodeToEarnCodeSectionMap(
			Map<String, EarnCodeSection> earnCodeToEarnCodeSectionMap) {
		this.earnCodeToEarnCodeSectionMap = earnCodeToEarnCodeSectionMap;
	}
	
	
	public void addToTotal(int index, BigDecimal hrs){
		BigDecimal total = getTotals().get(index);
		total = total.add(hrs, TkConstants.MATH_CONTEXT);
		getTotals().set(index, total);
	}
	public List<EarnCodeSection> getEarnCodeSections() {
		return earnCodeSections;
	}
	public void setEarnCodeSections(List<EarnCodeSection> earnCodeSections) {
		this.earnCodeSections = earnCodeSections;
	}
}