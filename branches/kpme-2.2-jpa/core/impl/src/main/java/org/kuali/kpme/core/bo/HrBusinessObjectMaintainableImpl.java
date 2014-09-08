/**
 * Copyright 2004-2014 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.kpme.core.bo;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.eclipse.persistence.sessions.CopyGroup;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.kuali.kpme.core.api.bo.dao.EffectiveObjectDao;
import org.kuali.kpme.core.cache.CacheUtils;
import org.kuali.kpme.core.service.HrServiceLocator;
import org.kuali.kpme.core.util.TKUtils;
import org.kuali.rice.kns.document.MaintenanceDocument;
import org.kuali.rice.kns.maintenance.KualiMaintainableImpl;
import org.kuali.rice.kns.service.KNSServiceLocator;
import org.kuali.rice.krad.data.DataObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.ObjectUtils;

public abstract class HrBusinessObjectMaintainableImpl extends KualiMaintainableImpl {
    protected static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(HrBusinessObjectMaintainableImpl.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void saveDataObject() {
        HrBusinessObject hrObj = (HrBusinessObject) this.getDataObject();

		if(hrObj.getId()!=null){
			HrBusinessObject oldHrObj = (HrBusinessObject)ObjectUtils.deepCopy(this.getObjectById(hrObj.getId()));
			if(oldHrObj!= null){
				//if the effective dates are the same do not create a new row just inactivate the old one
				if(hrObj.getEffectiveDate().equals(oldHrObj.getEffectiveDate())){
					oldHrObj.setActive(false);
					oldHrObj.setTimestamp(TKUtils.subtractOneSecondFromTimestamp(new Timestamp(DateTime.now().getMillis())));
				} else{
					//if effective dates not the same add a new row that inactivates the old entry based on the new effective date
					oldHrObj.setTimestamp(TKUtils.subtractOneSecondFromTimestamp(new Timestamp(DateTime.now().getMillis())));
					oldHrObj.setEffectiveDate(hrObj.getEffectiveDate());
					oldHrObj.setActive(false);
					oldHrObj.setId(null);
                    oldHrObj.setVersionNumber(null);
                    customInactiveSaveLogicNewEffective(oldHrObj);
				}
                KRADServiceLocatorWeb.getLegacyDataAdapter().linkAndSave(oldHrObj);
			}
		}
        HrBusinessObject newBo = (HrBusinessObject)ObjectUtils.deepCopy(hrObj);
        newBo.setTimestamp(new Timestamp(System.currentTimeMillis()));
        newBo.setId(null);
        newBo.setVersionNumber(null);

		
		customSaveLogic(newBo);
        //DataObjectService dos = KRADServiceLocator.getDataObjectService();
        //if (dos.supports(hrObj.getClass())) {
        //    EffectiveObjectDao effectiveObjectDao = (EffectiveObjectDao)HrServiceLocator.getBean("effectiveDao");
        //    effectiveObjectDao.cloneAndPersistDataObject(hrObj);
        //} else {

            KRADServiceLocatorWeb.getLegacyDataAdapter().linkAndSave(newBo);
        //}

        //cache clearing?!?!
        try {
            List<String> cacheNames = (List<String>)hrObj.getClass().getDeclaredField("CACHE_FLUSH").get(hrObj);
            CacheUtils.flushCaches(cacheNames);
        } catch (NoSuchFieldException e) {
            try {
                String cacheName = (String)hrObj.getClass().getDeclaredField("CACHE_NAME").get(hrObj);
                CacheUtils.flushCache(cacheName);
            } catch (NoSuchFieldException | IllegalAccessException ex) {
                // no cache name found
                LOG.warn("No cache name found for object: " + hrObj.getClass().getName());
            }
            // no cache name found
            //LOG.warn("No cache name found for object: " + hrObj.getClass().getName());
        } catch (IllegalAccessException e) {
            LOG.warn("No caches found for object: " + hrObj.getClass().getName());
        }
    }
	
	public abstract HrBusinessObject getObjectById(String id);
	public void customSaveLogic(HrBusinessObject hrObj){}
    public void customInactiveSaveLogicNewEffective(HrBusinessObject oldHrObj) {}

    @Override
    public void prepareForSave() {
        HrBusinessObject hrObj = (HrBusinessObject) this.getDataObject();
        hrObj.setUserPrincipalId(GlobalVariables.getUserSession().getPrincipalId());
    }
}