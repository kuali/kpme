package org.kuali.hr.time.util;

import org.joda.time.DateTimeZone;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

public class TkConstants {

	public static final String WILDCARD_CHARACTER = "%";
	public static final Long WILDCARD_LONG = -1L;

    public static final String CLOCK_IN = "CI";
    public static final String CLOCK_OUT = "CO";
    public static final String LUNCH_IN = "LI"; // Coming back from Lunch
    public static final String LUNCH_OUT = "LO"; // Leaving for Lunch
    public static final String BREAK_IN = "BI";
    public static final String BREAK_OUT = "BO";

    // action history
    public static final String DELETE = "DELETE";
    public static final String ADD = "ADD";
    public static final String MODIFIED = "MODIFIED";

    // earn code type
    public static final String EARN_CODE_HOUR = "HOUR";
    public static final String EARN_CODE_TIME = "TIME";
    public static final String EARN_CODE_AMOUNT = "AMOUNT";

    public static final String   GMT_TIME_ZONE_ID = "Etc/GMT";
    public static final TimeZone GMT_TIME_ZONE    = TimeZone.getTimeZone(GMT_TIME_ZONE_ID);
    public static final String   SYSTEM_TIME_ZONE = "America/Indianapolis";

    public static final DateTimeZone SYSTEM_DATE_TIME_ZONE = DateTimeZone.forID(TkConstants.SYSTEM_TIME_ZONE);
    public static final SimpleDateFormat SDF = new SimpleDateFormat("MM/dd/yyyy");

    public static final int BIG_DECIMAL_SCALE = 2;
    public static final RoundingMode BIG_DECIMAL_SCALE_ROUNDING = RoundingMode.HALF_EVEN;
    public static final BigDecimal BIG_DECIMAL_SCALED_ZERO = BigDecimal.ZERO.setScale(TkConstants.BIG_DECIMAL_SCALE, TkConstants.BIG_DECIMAL_SCALE_ROUNDING);
    public static final BigDecimal BIG_DECIMAL_NEGATIVE_ONE = new BigDecimal("-1");

    public static final MathContext MATH_CONTEXT = new MathContext(5, BIG_DECIMAL_SCALE_ROUNDING);
    public static final BigDecimal BIG_DECIMAL_1000 = BigDecimal.TEN.multiply(BigDecimal.TEN).multiply(BigDecimal.TEN);
    public static final BigDecimal BIG_DECIMAL_60 = BigDecimal.TEN.multiply(new BigDecimal("6"));
    public static final BigDecimal BIG_DECIMAL_MS_IN_H = BIG_DECIMAL_1000.multiply(BIG_DECIMAL_60).multiply(BIG_DECIMAL_60);

    public static final String SUPER_USER = TkConstants.ROLE_TK_SYS_ADMIN;

    //Used to map job locations to specify the timezone for display purposes
    public static final Map<String,String> LOCATION_TO_TIME_ZONE_MAP = new HashMap<String,String>();
    static {
    	LOCATION_TO_TIME_ZONE_MAP.put("NW", "America/Chicago");
    }

    /**
     * The following ROLE_* constants need to match what is in the workflow database.  They will be
     * used internally to obtain a reference to the underlying IDs in the workflow system.
     *
     * Used in *ValuesFinder classes for maintenance page dropdowns.
     */
    public static final String ROLE_NAMESAPCE    = "KUALI";
    public static final String ROLE_TK_GLOBAL_VO = "TK_GLOBAL_VO";
    public static final String ROLE_TK_DEPT_VO   = "TK_DEPT_VO";
    public static final String ROLE_TK_REVIEWER  = "TK_REVIEWER";
    public static final String ROLE_TK_APPROVER  = "TK_APPROVER";
    public static final String ROLE_TK_EMPLOYEE  = "TK_EMPLOYEE";
    public static final String ROLE_TK_ORG_ADMIN = "TK_ORG_ADMIN";
    public static final String ROLE_TK_SYS_ADMIN = "TK_SYS_ADMIN";
    public static final String ROLE_WORK_AREA_QUALIFIER_ID = "workArea";
    public static final List<String> ROLE_ASSIGNMENT_FOR_WORK_AREA = new ArrayList<String>(2);
    public static final List<String> ROLE_ASSIGNMENT_FOR_USER_ROLES = new ArrayList<String>(6);
    public static final Map<String,String> ALL_ROLES_MAP = new HashMap<String,String>();
    public static final List<String> ROLE_ASSIGNMENT_FOR_WORK_AREA_OT_EDIT = new ArrayList<String>(3);
    static {
    	ROLE_ASSIGNMENT_FOR_WORK_AREA.add(TkConstants.ROLE_TK_APPROVER);

    	ROLE_ASSIGNMENT_FOR_USER_ROLES.add(TkConstants.ROLE_TK_GLOBAL_VO);
    	ROLE_ASSIGNMENT_FOR_USER_ROLES.add(TkConstants.ROLE_TK_DEPT_VO);
    	ROLE_ASSIGNMENT_FOR_USER_ROLES.add(TkConstants.ROLE_TK_REVIEWER);
    	ROLE_ASSIGNMENT_FOR_USER_ROLES.add(TkConstants.ROLE_TK_APPROVER);
    	ROLE_ASSIGNMENT_FOR_USER_ROLES.add(TkConstants.ROLE_TK_ORG_ADMIN);
    	ROLE_ASSIGNMENT_FOR_USER_ROLES.add(TkConstants.ROLE_TK_SYS_ADMIN);

        ROLE_ASSIGNMENT_FOR_WORK_AREA_OT_EDIT.add(TkConstants.ROLE_TK_EMPLOYEE);
        ROLE_ASSIGNMENT_FOR_WORK_AREA_OT_EDIT.add(TkConstants.ROLE_TK_APPROVER);
        ROLE_ASSIGNMENT_FOR_WORK_AREA_OT_EDIT.add(TkConstants.ROLE_TK_ORG_ADMIN);

    	ALL_ROLES_MAP.put(TkConstants.ROLE_TK_REVIEWER,  "Reviewer");
    	ALL_ROLES_MAP.put(TkConstants.ROLE_TK_GLOBAL_VO, "Global View Only");
    	ALL_ROLES_MAP.put(TkConstants.ROLE_TK_DEPT_VO,   "Department View Only");
    	ALL_ROLES_MAP.put(TkConstants.ROLE_TK_APPROVER,  "Approver");
    	ALL_ROLES_MAP.put(TkConstants.ROLE_TK_EMPLOYEE,  "Employee");
    	ALL_ROLES_MAP.put(TkConstants.ROLE_TK_ORG_ADMIN, "Org Admin");
    	ALL_ROLES_MAP.put(TkConstants.ROLE_TK_SYS_ADMIN, "System Admin");
    }

    public static final class ACTIONS {
    	public static final String CLOCK_IN = "clockIn";
    	public static final String CLOCK_OUT = "clockOut";
    	public static final String ADD_TIME_BLOCK = "addTimeBlock";
        public static final String UPDATE_TIME_BLOCK = "updateTimeBlock";
        public static final String DELETE_TIME_BLOCK = "deleteTimeBlock";
    }

    public static final List<String> ClOCK_ACTIONS = new ArrayList<String>();
    static{
    	ClOCK_ACTIONS.add("CI");
    	ClOCK_ACTIONS.add("CO");
    	ClOCK_ACTIONS.add("LI"); // Coming back for Lunch
    	ClOCK_ACTIONS.add("LO"); // Leaving for Lunch
    }

    public static final String ASSIGNMENT_KEY_DELIMITER = "_";
    public static final String HOLIDAY_EARN_CODE = "HOL";
    // Special System earn code to represent lunch deductions.
    public static final String LUNCH_EARN_CODE = "LUN";
}