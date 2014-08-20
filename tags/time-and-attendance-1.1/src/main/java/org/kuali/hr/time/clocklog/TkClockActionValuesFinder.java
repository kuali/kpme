package org.kuali.hr.time.clocklog;

import org.apache.commons.lang.StringUtils;
import org.kuali.hr.time.missedpunch.MissedPunchDocument;
import org.kuali.hr.time.service.base.TkServiceLocator;
import org.kuali.hr.time.util.TKContext;
import org.kuali.hr.time.util.TKUser;
import org.kuali.hr.time.util.TkConstants;
import org.kuali.rice.core.util.KeyLabelPair;
import org.kuali.rice.kns.lookup.keyvalues.KeyValuesBase;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TkClockActionValuesFinder extends KeyValuesBase {

	@Override
	public List getKeyValues() {
		List<KeyLabelPair> keyLabels = new LinkedList<KeyLabelPair>();

        TKUser user = TKContext.getUser();
        
        // if it is an approver that is working on an employee's document, clock action should return all options
        if (user != null 
        		&& !(user.isApprover() && !user.getPrincipalId().equals(user.getTargetPrincipalId()))) {
            ClockLog lastClock = TkServiceLocator.getClockLogService().getLastClockLog(user.getTargetPrincipalId());

            Set<String> validEntries = lastClock != null ?
                    TkConstants.CLOCK_ACTION_TRANSITION_MAP.get(lastClock.getClockAction()) :
                    TkConstants.CLOCK_ACTION_TRANSITION_MAP.get(TkConstants.CLOCK_OUT); // Force CLOCK_IN as next valid action.

            for (String entry : validEntries) {
                keyLabels.add(new KeyLabelPair(entry, TkConstants.CLOCK_ACTION_STRINGS.get(entry)));
            }
        } else {
            // Default to adding all options.
            for (Map.Entry entry : TkConstants.CLOCK_ACTION_STRINGS.entrySet()) {
                keyLabels.add(new KeyLabelPair(entry.getKey(), (String)entry.getValue()));
            }
        }
        
        String mpDocId = (String)TKContext.getHttpServletRequest().getParameter(TkConstants.DOCUMENT_ID_REQUEST_NAME);
        if(StringUtils.isBlank(mpDocId)) {
        	mpDocId = (String)TKContext.getHttpServletRequest().getAttribute(TkConstants.DOCUMENT_ID_REQUEST_NAME);
        }
     // if the user is working on an existing missed punch doc, make sure the doc's action shows up in the list
        if(!StringUtils.isEmpty(mpDocId)) {
        	MissedPunchDocument mp = TkServiceLocator.getMissedPunchService().getMissedPunchByRouteHeader(mpDocId);
        	if(mp != null) {
        		String clockAction = mp.getClockAction();
        		if(!StringUtils.isEmpty(clockAction)) {
        			keyLabels.add(new KeyLabelPair(clockAction, TkConstants.CLOCK_ACTION_STRINGS.get(clockAction)));
        		}
        	}
        }

  		return keyLabels;
	}

}