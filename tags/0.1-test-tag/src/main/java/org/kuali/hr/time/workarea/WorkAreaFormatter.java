package org.kuali.hr.time.workarea;

import org.kuali.rice.kns.web.format.Formatter;

public class WorkAreaFormatter extends Formatter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Object format(Object value) {
		if(value != null){
			Long val = (Long)value;
			if(val == -1L){
				return "%";
			}
			return val.toString();
		}
		return super.format(value);
	}

}