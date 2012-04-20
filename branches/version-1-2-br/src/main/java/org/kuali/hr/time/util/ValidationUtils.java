package org.kuali.hr.time.util;

import org.apache.commons.lang.StringUtils;
import org.kuali.hr.location.Location;
import org.kuali.hr.paygrade.PayGrade;
import org.kuali.hr.time.accrual.AccrualCategory;
import org.kuali.hr.time.accrual.TimeOffAccrual;
import org.kuali.hr.time.authorization.DepartmentalRule;
import org.kuali.hr.time.department.Department;
import org.kuali.hr.time.dept.earncode.DepartmentEarnCode;
import org.kuali.hr.time.earncode.EarnCode;
import org.kuali.hr.time.earngroup.EarnGroup;
import org.kuali.hr.time.earngroup.EarnGroupDefinition;
import org.kuali.hr.time.paytype.PayType;
import org.kuali.hr.time.salgroup.SalGroup;
import org.kuali.hr.time.service.base.TkServiceLocator;
import org.kuali.hr.time.task.Task;
import org.kuali.hr.time.workarea.WorkArea;
import org.kuali.kfs.coa.businessobject.Chart;
import org.kuali.rice.kim.bo.Person;
import org.kuali.rice.kim.service.KIMServiceLocator;
import org.kuali.rice.kns.service.KNSServiceLocator;

import java.sql.Date;

/**
 * A few methods to assist with various validation tasks.
 */
public class ValidationUtils {

    /**
     * For DepartmentalRule objects, if a work area is defined, you can not
     * leave the department field with a wildcard. Permission for wildcarding
     * will be checked with other methods.
     *
     * @param dr The DepartmentalRule to examine.
     * @return true if valid, false otherwise.
     */
    public static boolean validateWorkAreaDeptWildcarding(DepartmentalRule dr) {
        boolean ret = true;

        if (StringUtils.equals(dr.getDept(), TkConstants.WILDCARD_CHARACTER)) {
            ret = dr.getWorkArea().equals(TkConstants.WILDCARD_LONG);
        }

        return ret;
    }

	/**
	 * Most basic validation: Only checks for presence in the database.
	 */
	public static boolean validateWorkArea(Long workArea) {
		return validateWorkArea(workArea, null);
	}

	/**
	 * Most basic validation: Only checks for presence in the database.
	 */
	public static boolean validateDepartment(String department) {
		return validateDepartment(department, null);
	}

	/**
	 * Most basic validation: Only checks for presence in the database.
	 */
	public static boolean validateAccrualCategory(String accrualCategory) {
		return validateAccrualCategory(accrualCategory, null);
	}


	public static boolean validateSalGroup(String salGroup, Date asOfDate) {
		boolean valid = false;

		if (StringUtils.equals(salGroup, TkConstants.WILDCARD_CHARACTER)) {
			valid = true;
		} else if (asOfDate != null) {
			SalGroup sg = TkServiceLocator.getSalGroupService().getSalGroup(salGroup, asOfDate);
			valid = (sg != null);
		} else {
			int count = TkServiceLocator.getSalGroupService().getSalGroupCount(salGroup);
			valid = (count > 0);
		}

		return valid;
	}

	public static boolean validateEarnCode(String earnCode, Date asOfDate) {
		boolean valid = false;

		if (asOfDate != null) {
			EarnCode ec = TkServiceLocator.getEarnCodeService().getEarnCode(earnCode, asOfDate);
			valid = (ec != null);
		} else {
			int count = TkServiceLocator.getEarnCodeService().getEarnCodeCount(earnCode);
			valid = (count > 0);
		}

		return valid;
	}

	public static boolean validateLocation(String location, Date asOfDate) {
		boolean valid = false;

		if (asOfDate != null) {
			Location l = TkServiceLocator.getLocationService().getLocation(location, asOfDate);
			valid = (l != null);
		} else {
			int count = TkServiceLocator.getLocationService().getLocationCount(location);
			valid = (count > 0);
		}

		return valid;
	}

	public static boolean validatePayType(String payType, Date asOfDate) {
		boolean valid = false;

		if (asOfDate != null) {
			PayType pt = TkServiceLocator.getPayTypeSerivce().getPayType(payType, asOfDate);
			valid = (pt != null);
		} else {
			int count = TkServiceLocator.getPayTypeSerivce().getPayTypeCount(payType);
			valid = (count > 0);
		}

		return valid;
	}


	public static boolean validatePayGrade(String payGrade, Date asOfDate) {
		boolean valid = false;

		if (asOfDate != null) {
			PayGrade pg = TkServiceLocator.getPayGradeService().getPayGrade(payGrade, asOfDate);
			valid = (pg != null);
		} else {
			int count = TkServiceLocator.getPayGradeService().getPayGradeCount(payGrade);
			valid = (count > 0);
		}

		return valid;
	}

    /**
     *
     * @param earnCode
     * @param otEarnCode If true, earn code is valid ONLY if it is an overtime earn code.
     * @param asOfDate
     * @return
     */
    public static boolean validateEarnCode(String earnCode, boolean otEarnCode, Date asOfDate) {
        boolean valid = false;

        if (asOfDate != null) {
            EarnCode ec = TkServiceLocator.getEarnCodeService().getEarnCode(earnCode, asOfDate);
            valid = (ec != null) && (otEarnCode ? ec.getOvtEarnCode().booleanValue() : true);
        }

        return valid;
    }

	/**
	 * Checks for row presence of a department, and optionally whether or not
	 * it is active as of the specified date.
	 */
	public static boolean validateDepartment(String department, Date asOfDate) {
		boolean valid = false;

        if (StringUtils.isEmpty(department)) {
          // do nothing, let false be returned.
        } else if (asOfDate != null) {
			Department d = TkServiceLocator.getDepartmentService().getDepartment(department, asOfDate);
		    valid = (d != null);
		} else {
			int count = TkServiceLocator.getDepartmentService().getDepartmentCount(department);
			valid = (count > 0);
		}

		return valid;
	}

    public static boolean validateChart(String chart) {
        boolean valid = false;

        if (!StringUtils.isEmpty(chart)) {
            Object o = KNSServiceLocator.getBusinessObjectService().findBySinglePrimaryKey(Chart.class, chart);
            valid = (o instanceof Chart);
        }

        return valid;
    }

	/**
	 * Checks for row presence of a work area, and optionally whether or not
	 * it is active as of the specified date.
	 */
    public static boolean validateWorkArea(Long workArea, Date asOfDate) {
        return ValidationUtils.validateWorkArea(workArea, null, asOfDate);
    }

	public static boolean validateWorkArea(Long workArea, String dept, Date asOfDate) {
		boolean valid = false;

		if (workArea == null) {
			valid = false;
		} else if (workArea.equals(TkConstants.WILDCARD_LONG)) {
			valid = true;
		} else if (asOfDate != null) {
			WorkArea wa = TkServiceLocator.getWorkAreaService().getWorkArea(workArea, asOfDate);
            if (wa != null && dept != null) {
                valid = StringUtils.equalsIgnoreCase(dept, wa.getDept());
            } else {
			    valid = (wa != null);
            }
		} else {
            // Not valid if no date is passed.
		}

		return valid;
	}
	/**
	 * Checks for row presence of a Accrual Category, and optionally whether or not
	 * it is active as of the specified date.
	 */
	public static boolean validateAccrualCategory(String accrualCategory, Date asOfDate) {
		boolean valid = false;

		if (StringUtils.equals(accrualCategory, TkConstants.WILDCARD_CHARACTER)) {
			valid = true;
		} else if (asOfDate != null) {
			AccrualCategory ac = TkServiceLocator.getAccrualCategoryService().getAccrualCategory(accrualCategory, asOfDate);
			valid = (ac != null);
		}

		return valid;
	}

	/**
	 * Checks for row presence of a principal Id, and optionally whether or not
	 * it is active as of the specified date.
	 */
	public static boolean validatePrincipalId(String principalId) {
		boolean valid = false;
		if (principalId != null) {
			Person p = KIMServiceLocator.getPersonService().getPerson(principalId);
		    valid = (p != null);
		}
		return valid;
	}

    /**
     * No wildcarding is accounted for in this method.
     * @param task Task "Long Name"
     * @param asOfDate Can be null, if we just want to look for the general case.
     * @return True if the task is present / valid.
     */
    public static boolean validateTask(Long task, Date asOfDate) {
        boolean valid = false;

        if (task != null && asOfDate != null) {
            Task t = TkServiceLocator.getTaskService().getTask(task, asOfDate);
            valid = (t != null);
        } else if (task != null) {
        	int count = TkServiceLocator.getTaskService().getTaskCount(task);
            valid = (count > 0);
        }

        return valid;
    }

    /**
     * No wildcarding is accounted for in this method.
     * @param earnGroup EarnGroup
     * @param asOfDate Can be null, if we just want to look for the general case.
     * @return True if the EarnGroup is present / valid.
     */
    public static boolean validateEarnGroup(String earnGroup, Date asOfDate) {
        boolean valid = false;

        if (earnGroup != null && asOfDate != null) {
            EarnGroup eg = TkServiceLocator.getEarnGroupService().getEarnGroup(earnGroup, asOfDate);
            valid = (eg != null);
        } else if (earnGroup != null) {
        	int count = TkServiceLocator.getEarnGroupService().getEarnGroupCount(earnGroup);
            valid = (count > 0);
        }

        return valid;
    }
    
    /**
     * @param earnGroup EarnGroup
     * @param asOfDate
     * @return True if the EarnGroup has overtime earn codes
     */
    public static boolean earnGroupHasOvertimeEarnCodes(String earnGroup, Date asOfDate) {
         if (earnGroup != null && asOfDate != null) {
             EarnGroup eg = TkServiceLocator.getEarnGroupService().getEarnGroup(earnGroup, asOfDate);
             if(eg != null) {
            	for(EarnGroupDefinition egd : eg.getEarnGroups()) {
            		if(egd.getEarnCode() != null) {
            			EarnCode ec = TkServiceLocator.getEarnCodeService().getEarnCode(egd.getEarnCode(), asOfDate);
            			if(ec != null && ec.getOvtEarnCode()) {
            				return true;
            			}
            		}
            	}
             }
         }

        return false;
    }

	/**
	 * Checks for row presence of a pay calendar
	 */
	public static boolean validatePayCalendar(String pyCalendarGroup) {
		boolean valid = false;
		int count = TkServiceLocator.getPayCalendarSerivce().getPyCalendarGroupCount(pyCalendarGroup);
        valid = (count > 0);
        return valid;
	}

   public static boolean duplicateDeptEarnCodeExists(DepartmentEarnCode deptEarnCode) {
	   boolean valid = false;
	   int count = TkServiceLocator.getDepartmentEarnCodeService().getDepartmentEarnCodeCount
	   				(deptEarnCode.getDept(), deptEarnCode.getHrSalGroup(), deptEarnCode.getEarnCode(), deptEarnCode.isEmployee() ? "1" : "0", 
	   				 deptEarnCode.isApprover()? "1" : "0", deptEarnCode.getLocation(), deptEarnCode.getActive() ? "Y" : "N", deptEarnCode.getEffectiveDate(), null);
       if(count == 1) {
    	   valid = true;
    	   count = TkServiceLocator.getDepartmentEarnCodeService().getDepartmentEarnCodeCount
    	   			(deptEarnCode.getDept(), deptEarnCode.getHrSalGroup(), deptEarnCode.getEarnCode(), deptEarnCode.isEmployee() ? "1" : "0", 
   					deptEarnCode.isApprover()? "1" : "0", deptEarnCode.getLocation(), deptEarnCode.getActive() ? "Y" : "N", deptEarnCode.getEffectiveDate(), deptEarnCode.getHrDeptEarnCodeId());
    	   if(count == 1) {
    		   valid = false;
    	   }
       } else if(count > 1) {
    	   valid = true;
       }

	   return valid;
   }
   
   public static boolean duplicateTimeOffAccrual (TimeOffAccrual timeOffAccrual) {
	   boolean valid = false;
	   int count = TkServiceLocator.getTimeOffAccrualService().getTimeOffAccrualCount
	   				(timeOffAccrual.getAccrualCategory(), timeOffAccrual.getEffectiveDate(), timeOffAccrual.getPrincipalId(), null);
	   if(count == 1) {
    	   valid = true;
    	   count = TkServiceLocator.getTimeOffAccrualService().getTimeOffAccrualCount
  					(timeOffAccrual.getAccrualCategory(), timeOffAccrual.getEffectiveDate(), timeOffAccrual.getPrincipalId(), timeOffAccrual.getLmAccrualId());
    	   if(count == 1) {
    		   valid = false;
    	   }
       } else if(count > 1) {
    	   valid = true;
       }
	   return valid;
   }


}
