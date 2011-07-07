package org.kuali.hr.time.roles.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.kuali.hr.time.roles.TkRoleGroup;
import org.kuali.hr.time.roles.dao.TkRoleGroupDao;
import org.kuali.hr.time.service.base.TkServiceLocator;
import org.kuali.hr.time.util.TKUtils;

public class TkRoleGroupServiceImpl implements TkRoleGroupService {

    private static final Logger LOG = Logger.getLogger(TkRoleGroupServiceImpl.class);

	private TkRoleGroupDao tkRoleGroupDao;

	public void setTkRoleGroupDao(TkRoleGroupDao tkRoleGroupDao) {
		this.tkRoleGroupDao = tkRoleGroupDao;
	}

	@Override
	public void saveOrUpdate(List<TkRoleGroup> roleGroups) {
		this.tkRoleGroupDao.saveOrUpdateRoleGroups(roleGroups);
	}

	@Override
	public void saveOrUpdate(TkRoleGroup roleGroup) {
		this.tkRoleGroupDao.saveOrUpdateRoleGroup(roleGroup);
	}

	@Override
	public TkRoleGroup getRoleGroup(String principalId) {
		return tkRoleGroupDao.getRoleGroup(principalId);
	}

	@Override
	public void populateRoles(TkRoleGroup tkRoleGroup) {
		if (tkRoleGroup != null) {
			tkRoleGroup.setRoles(TkServiceLocator.getTkRoleService().getRoles(tkRoleGroup.getPrincipalId(), TKUtils.getCurrentDate()));
			tkRoleGroup.setInactiveRoles(TkServiceLocator.getTkRoleService().getInActiveRoles(tkRoleGroup.getPrincipalId(), TKUtils.getCurrentDate()));
		}
	}
}
