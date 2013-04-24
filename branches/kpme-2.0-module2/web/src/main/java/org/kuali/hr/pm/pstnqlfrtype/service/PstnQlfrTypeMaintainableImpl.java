package org.kuali.hr.pm.pstnqlfrtype.service;

import org.kuali.hr.core.HrBusinessObject;
import org.kuali.hr.core.HrBusinessObjectMaintainableImpl;
import org.kuali.hr.pm.service.base.PmServiceLocator;

public class PstnQlfrTypeMaintainableImpl extends HrBusinessObjectMaintainableImpl{

	private static final long serialVersionUID = 1L;

	@Override
	public HrBusinessObject getObjectById(String id) {
		return PmServiceLocator.getPstnQlfrTypeServiceService().getPstnQlfrTypeById(id);
	}

}
