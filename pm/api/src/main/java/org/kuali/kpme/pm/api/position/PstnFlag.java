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
package org.kuali.kpme.pm.api.position;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.xml.bind.annotation.*;

import org.joda.time.LocalDate;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.w3c.dom.Element;

@XmlRootElement(name = PstnFlag.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = PstnFlag.Constants.TYPE_NAME, propOrder = {
	PstnFlag.Elements.HR_POSITION_ID,
    PstnFlag.Elements.EFFECTIVE_LOCAL_DATE_OF_OWNER,
    CoreConstants.CommonElements.VERSION_NUMBER,
    CoreConstants.CommonElements.OBJECT_ID,
    PstnFlag.Elements.CATEGORY,
    PstnFlag.Elements.NAMES,
    PstnFlag.Elements.PM_FLAG_ID,
    CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class PstnFlag extends AbstractDataTransferObject implements PstnFlagContract {

	private static final long serialVersionUID = 6134747794553428319L;
	
	@XmlElement(name = Elements.HR_POSITION_ID, required = false)
    private final String hrPositionId;
    @XmlElement(name = Elements.EFFECTIVE_LOCAL_DATE_OF_OWNER, required = false)
    private final LocalDate effectiveLocalDateOfOwner;
    @XmlElement(name = CoreConstants.CommonElements.VERSION_NUMBER, required = false)
    private final Long versionNumber;
    @XmlElement(name = CoreConstants.CommonElements.OBJECT_ID, required = false)
    private final String objectId;
    @XmlElement(name = Elements.CATEGORY, required = false)
    private final String category;
    @XmlElementWrapper(name = Elements.NAMES, required = false)
    @XmlElement(name = Elements.NAME, required = false)
    private final List<String> names;
    @XmlElement(name = Elements.PM_FLAG_ID, required = false)
    private final String pmFlagId;
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * Private constructor used only by JAXB.
     * 
     */
    private PstnFlag() {
        this.hrPositionId = null;
        this.effectiveLocalDateOfOwner = null;
        this.versionNumber = null;
        this.objectId = null;
        this.category = null;
        this.names = null;
        this.pmFlagId = null;
    }

    private PstnFlag(Builder builder) {
        this.hrPositionId = builder.getHrPositionId();
        this.effectiveLocalDateOfOwner = builder.getEffectiveLocalDateOfOwner();
        this.versionNumber = builder.getVersionNumber();
        this.objectId = builder.getObjectId();
        this.category = builder.getCategory();
        this.names = builder.getNames() == null ? Collections.<String>emptyList() : Collections.unmodifiableList(builder.getNames());
        this.pmFlagId = builder.getPmFlagId();
    }
    
    @Override
    public String getHrPositionId() {
        return this.hrPositionId;
    }

    @Override
    public LocalDate getEffectiveLocalDateOfOwner() {
        return this.effectiveLocalDateOfOwner;
    }

    @Override
    public Long getVersionNumber() {
        return this.versionNumber;
    }

    @Override
    public String getObjectId() {
        return this.objectId;
    }

    @Override
    public String getCategory() {
        return this.category;
    }

    @Override
    public List<String> getNames() {
    	// has been set to an unmodifiable list in the constructor
        return this.names;
    }

    @Override
    public String getPmFlagId() {
        return this.pmFlagId;
    }


    /**
     * A builder which can be used to construct {@link PstnFlag} instances.  Enforces the constraints of the {@link PstnFlagContract}.
     * 
     */
    public final static class Builder implements Serializable, PstnFlagContract, ModelBuilder {

    	private static final long serialVersionUID = -515866156444153425L;
		
    	private String hrPositionId;
        private LocalDate effectiveLocalDateOfOwner;
        private Long versionNumber;
        private String objectId;
        private String category;
        private List<String> names;
        private String pmFlagId;

        private Builder() {
        }

        public static Builder create() {
            return new Builder();
        }

        public static Builder create(PstnFlagContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            Builder builder = create();
            builder.setHrPositionId(contract.getHrPositionId());
            builder.setEffectiveLocalDateOfOwner(contract.getEffectiveLocalDateOfOwner());
            builder.setVersionNumber(contract.getVersionNumber());
            builder.setObjectId(contract.getObjectId());
            builder.setCategory(contract.getCategory());
            builder.setNames(contract.getNames());
            builder.setPmFlagId(contract.getPmFlagId());
            return builder;
        }

        public PstnFlag build() {
            return new PstnFlag(this);
        }
        
        @Override
        public String getHrPositionId() {
            return this.hrPositionId;
        }

        @Override
        public LocalDate getEffectiveLocalDateOfOwner() {
            return this.effectiveLocalDateOfOwner;
        }

        @Override
        public Long getVersionNumber() {
            return this.versionNumber;
        }

        @Override
        public String getObjectId() {
            return this.objectId;
        }

        @Override
        public String getCategory() {
            return this.category;
        }

        @Override
        public List<String> getNames() {
            return this.names;
        }

        @Override
        public String getPmFlagId() {
            return this.pmFlagId;
        }
        
        public void setHrPositionId(String hrPositionId) {
            this.hrPositionId = hrPositionId;
        }

        public void setEffectiveLocalDateOfOwner(LocalDate effectiveLocalDateOfOwner) {

            this.effectiveLocalDateOfOwner = effectiveLocalDateOfOwner;
        }

        public void setVersionNumber(Long versionNumber) {

            this.versionNumber = versionNumber;
        }

        public void setObjectId(String objectId) {

            this.objectId = objectId;
        }

        public void setCategory(String category) {

            this.category = category;
        }

        public void setNames(List<String> names) {

            this.names = names;
        }

        public void setPmFlagId(String pmFlagId) {

            this.pmFlagId = pmFlagId;
        }

    }


    /**
     * Defines some internal constants used on this class.
     * 
     */
    static class Constants {

        final static String ROOT_ELEMENT_NAME = "pstnFlag";
        final static String TYPE_NAME = "PstnFlagType";

    }


    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     * 
     */
    static class Elements {

        final static String HR_POSITION_ID = "hrPositionId";
        final static String EFFECTIVE_LOCAL_DATE_OF_OWNER = "effectiveLocalDateOfOwner";
        final static String CATEGORY = "category";
        final static String NAMES = "names";
        final static String NAME = "name";
        final static String PM_FLAG_ID = "pmFlagId";

    }

}