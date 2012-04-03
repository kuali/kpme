package org.kuali.hr.lm;

import java.util.HashMap;
import java.util.Map;


public class LMConstants {
	public static final String SERVICE_TIME_YEAR = "year";
	public static final String SERVICE_TIME_MONTHS = "month";
	
	public static final class ACCRUAL_EARN_INTERVAL{
		public static final String DAILY = "daily";
		public static final String WEEKLY = "weekly";
		public static final String BI_WEEKLY = "biweekly";
		public static final String SEMI_MONTHLY = "semimonthly";
		public static final String MONTHLY = "monthly";
		public static final String YEARLY = "yearly";
		public static final String NO_ACCRUAL = "noaccrual";
	}
	
	// Action history
	public static final class ACTION{
		public static final String DELETE = "D";
		public static final String ADD = "A";
		public static final String MODIFIED = "M";
	}
	
	// Request status
	public static final class REQUEST_STATUS{
		public static final String PLANNED="P";
		public static final String REQUESTED="R";
		public static final String APPROVED="A";
		public static final String RECORDED = "C";
		public static final String DISAPPROVED = "D";
		public static final String DEFERRED="F";
		public static final String ACCURAL = "Accural";
		public static final String USAGE = "USAGE";
	}
	
	 public static final Map<String, String> REQUEST_STATUS_STRINGS = new HashMap<String, String>(6);

	    static {
	    	REQUEST_STATUS_STRINGS.put(REQUEST_STATUS.PLANNED, "Planned");
	    	REQUEST_STATUS_STRINGS.put(REQUEST_STATUS.REQUESTED, "Requested");
	    	REQUEST_STATUS_STRINGS.put(REQUEST_STATUS.APPROVED, "Approved"); 
	    	REQUEST_STATUS_STRINGS.put(REQUEST_STATUS.RECORDED, "Recorded"); 
	    	REQUEST_STATUS_STRINGS.put(REQUEST_STATUS.DISAPPROVED, "Disapproved");
	    	REQUEST_STATUS_STRINGS.put(REQUEST_STATUS.DEFERRED, "Deferred"); 
	    }
	
}