/*
 * Copyright 2005-2007 The Kuali Foundation
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
package org.kuali.rice.kns.util;

import java.lang.reflect.InvocationTargetException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.service.EncryptionService;
import org.kuali.rice.core.util.ClassLoaderUtils;
import org.kuali.rice.core.util.KeyLabelPair;
import org.kuali.rice.kew.util.KEWConstants;
import org.kuali.rice.kim.bo.Person;
import org.kuali.rice.kns.authorization.FieldRestriction;
import org.kuali.rice.kns.bo.BusinessObject;
import org.kuali.rice.kns.bo.BusinessObjectRelationship;
import org.kuali.rice.kns.bo.Inactivateable;
import org.kuali.rice.kns.bo.KualiCode;
import org.kuali.rice.kns.bo.PersistableBusinessObject;
import org.kuali.rice.kns.datadictionary.BusinessObjectEntry;
import org.kuali.rice.kns.datadictionary.FieldDefinition;
import org.kuali.rice.kns.datadictionary.MaintainableCollectionDefinition;
import org.kuali.rice.kns.datadictionary.control.ApcSelectControlDefinition;
import org.kuali.rice.kns.datadictionary.control.ButtonControlDefinition;
import org.kuali.rice.kns.datadictionary.control.ControlDefinition;
import org.kuali.rice.kns.datadictionary.control.CurrencyControlDefinition;
import org.kuali.rice.kns.datadictionary.control.KualiUserControlDefinition;
import org.kuali.rice.kns.datadictionary.control.LinkControlDefinition;
import org.kuali.rice.kns.datadictionary.mask.MaskFormatter;
import org.kuali.rice.kns.document.authorization.MaintenanceDocumentRestrictions;
import org.kuali.rice.kns.exception.UnknownBusinessClassAttributeException;
import org.kuali.rice.kns.inquiry.Inquirable;
import org.kuali.rice.kns.lookup.HtmlData;
import org.kuali.rice.kns.lookup.HtmlData.AnchorHtmlData;
import org.kuali.rice.kns.lookup.LookupUtils;
import org.kuali.rice.kns.lookup.keyvalues.ApcValuesFinder;
import org.kuali.rice.kns.lookup.keyvalues.IndicatorValuesFinder;
import org.kuali.rice.kns.lookup.keyvalues.KeyValuesFinder;
import org.kuali.rice.kns.lookup.keyvalues.PersistableBusinessObjectValuesFinder;
import org.kuali.rice.kns.lookup.valueFinder.ValueFinder;
import org.kuali.rice.kns.service.BusinessObjectDictionaryService;
import org.kuali.rice.kns.service.BusinessObjectMetaDataService;
import org.kuali.rice.kns.service.DataDictionaryService;
import org.kuali.rice.kns.service.KNSServiceLocator;
import org.kuali.rice.kns.service.KualiModuleService;
import org.kuali.rice.kns.service.ModuleService;
import org.kuali.rice.kns.web.format.FormatException;
import org.kuali.rice.kns.web.format.Formatter;
import org.kuali.rice.kns.web.ui.Field;
import org.kuali.rice.kns.web.ui.PropertyRenderingConfigElement;
import org.kuali.rice.kns.web.ui.Row;
import org.kuali.rice.kns.web.ui.Section;


/**
 * This class is used to build Field objects from underlying data dictionary and general utility methods for handling fields.
 */
public class FieldUtils {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(FieldUtils.class);
    private static DataDictionaryService dataDictionaryService = null;
    private static BusinessObjectMetaDataService businessObjectMetaDataService = null;
    private static BusinessObjectDictionaryService businessObjectDictionaryService = null;
    private static KualiModuleService kualiModuleService = null;

    public static void setInquiryURL(Field field, BusinessObject bo, String propertyName) {
        HtmlData inquiryHref = new AnchorHtmlData(KNSConstants.EMPTY_STRING, KNSConstants.EMPTY_STRING);

        Boolean b = getBusinessObjectDictionaryService().noInquiryFieldInquiry(bo.getClass(), propertyName);
        if (b == null || !b.booleanValue()) {
            Class<Inquirable> inquirableClass = getBusinessObjectDictionaryService().getInquirableClass(bo.getClass());
            Boolean b2 = getBusinessObjectDictionaryService().forceLookupResultFieldInquiry(bo.getClass(), propertyName);
            Inquirable inq = null;
            try {
                if ( inquirableClass != null ) {
                    inq = inquirableClass.newInstance();
                } else {
                    inq = KNSServiceLocator.getKualiInquirable();
                    if ( LOG.isDebugEnabled() ) {
                        LOG.debug( "Default Inquirable Class: " + inq.getClass() );
        }
                }

                inquiryHref = inq.getInquiryUrl(bo, propertyName, null == b2 ? false : b2.booleanValue() );

            } catch ( Exception ex ) {
                LOG.error("unable to create inquirable to get inquiry URL", ex );
            }
        }

        field.setInquiryURL(inquiryHref);
    }
    
	/**
	 * Sets the control on the field based on the data dictionary definition
	 * 
	 * @param businessObjectClass
	 *            - business object class for the field attribute
	 * @param attributeName
	 *            - name of the attribute whose {@link Field} is being set
	 * @param convertForLookup
	 *            - whether the field is being build for lookup search which impacts the control chosen
	 * @param field
	 *            - {@link Field} to set control on
	 */
	public static void setFieldControl(Class businessObjectClass, String attributeName, boolean convertForLookup,
			Field field) {
		ControlDefinition control = getDataDictionaryService().getAttributeControlDefinition(businessObjectClass,
				attributeName);
		String fieldType = Field.TEXT;

		if (control != null) {
			if (control.isSelect()) {
				if (control.getScript() != null && control.getScript().length() > 0) {
					fieldType = Field.DROPDOWN_SCRIPT;
					field.setScript(control.getScript());
				} else {
					fieldType = Field.DROPDOWN;
				}
			}

			if (control.isMultiselect()) {
				fieldType = Field.MULTISELECT;
			}

			if (control.isApcSelect()) {
				fieldType = Field.DROPDOWN_APC;
			}

            if (control.isCheckbox()) {
                fieldType = Field.CHECKBOX;
                if (control.getScript() != null && control.getScript().length() > 0) {
                	field.setScript(control.getScript());
                }
            }

			if (control.isRadio()) {
				fieldType = Field.RADIO;
			}

			if (control.isHidden()) {
				fieldType = Field.HIDDEN;
			}

			if (control.isKualiUser()) {
				fieldType = Field.KUALIUSER;
				KualiUserControlDefinition kualiUserControl = (KualiUserControlDefinition) control;
				field.setUniversalIdAttributeName(kualiUserControl.getUniversalIdAttributeName());
				field.setUserIdAttributeName(kualiUserControl.getUserIdAttributeName());
				field.setPersonNameAttributeName(kualiUserControl.getPersonNameAttributeName());
			}

			if (control.isWorkflowWorkgroup()) {
				fieldType = Field.WORKFLOW_WORKGROUP;
			}

			if (control.isFile()) {
				fieldType = Field.FILE;
			}

			if (control.isTextarea() && !convertForLookup) {
				fieldType = Field.TEXT_AREA;
			}

			if (control.isLookupHidden()) {
				fieldType = Field.LOOKUP_HIDDEN;
			}

			if (control.isLookupReadonly()) {
				fieldType = Field.LOOKUP_READONLY;
			}

			if (control.isCurrency()) {
				fieldType = Field.CURRENCY;
			}

			if (control.isButton()) {
				fieldType = Field.BUTTON;
			}

			if (control.isLink()) {
				fieldType = Field.LINK;
			}

			if (Field.CURRENCY.equals(fieldType) && control instanceof CurrencyControlDefinition) {
				CurrencyControlDefinition currencyControl = (CurrencyControlDefinition) control;
				field.setStyleClass("amount");
				field.setSize(currencyControl.getSize());
				field.setFormattedMaxLength(currencyControl.getFormattedMaxLength());
			}

			// for text controls, set size attribute
			if (Field.TEXT.equals(fieldType)) {
				Integer size = control.getSize();
				if (size != null) {
					field.setSize(size.intValue());
				} else {
					field.setSize(30);
				}
				field.setDatePicker(control.isDatePicker());
				field.setRanged(control.isRanged());
			}

			if (Field.WORKFLOW_WORKGROUP.equals(fieldType)) {
				Integer size = control.getSize();
				if (size != null) {
					field.setSize(size.intValue());
				} else {
					field.setSize(30);
				}
			}

			// for text area controls, set rows and cols attributes
			if (Field.TEXT_AREA.equals(fieldType)) {
				Integer rows = control.getRows();
				if (rows != null) {
					field.setRows(rows.intValue());
				} else {
					field.setRows(3);
				}

				Integer cols = control.getCols();
				if (cols != null) {
					field.setCols(cols.intValue());
				} else {
					field.setCols(40);
				}
				field.setExpandedTextArea(control.isExpandedTextArea());
			}

			// for dropdown and radio, get instance of specified KeyValuesFinder and set field values
			if (Field.DROPDOWN.equals(fieldType) || Field.RADIO.equals(fieldType)
					|| Field.DROPDOWN_SCRIPT.equals(fieldType) || Field.DROPDOWN_APC.equals(fieldType)
					|| Field.MULTISELECT.equals(fieldType)) {
				String keyFinderClassName = control.getValuesFinderClass();

				if (StringUtils.isNotBlank(keyFinderClassName)) {
					try {
						Class keyFinderClass = ClassLoaderUtils.getClass(keyFinderClassName);
						KeyValuesFinder finder = (KeyValuesFinder) keyFinderClass.newInstance();

						if (finder != null) {
							if (finder instanceof ApcValuesFinder && control instanceof ApcSelectControlDefinition) {
								((ApcValuesFinder) finder).setParameterNamespace(((ApcSelectControlDefinition) control)
										.getParameterNamespace());
								((ApcValuesFinder) finder)
										.setParameterDetailType(((ApcSelectControlDefinition) control)
												.getParameterDetailType());
								((ApcValuesFinder) finder).setParameterName(((ApcSelectControlDefinition) control)
										.getParameterName());
							} else if (finder instanceof PersistableBusinessObjectValuesFinder) {
								((PersistableBusinessObjectValuesFinder) finder)
										.setBusinessObjectClass(ClassLoaderUtils.getClass(control
												.getBusinessObjectClass()));
								((PersistableBusinessObjectValuesFinder) finder).setKeyAttributeName(control
										.getKeyAttribute());
								((PersistableBusinessObjectValuesFinder) finder).setLabelAttributeName(control
										.getLabelAttribute());
								if (control.getIncludeBlankRow() != null) {
									((PersistableBusinessObjectValuesFinder) finder).setIncludeBlankRow(control
											.getIncludeBlankRow());
								}
								((PersistableBusinessObjectValuesFinder) finder).setIncludeKeyInDescription(control
										.getIncludeKeyInLabel());
							}
							field.setFieldValidValues(finder.getKeyValues());
							field.setFieldInactiveValidValues(finder.getKeyValues(false));
						}
					} catch (InstantiationException e) {
						LOG.error("Unable to get new instance of finder class: " + keyFinderClassName);
						throw new RuntimeException("Unable to get new instance of finder class: " + keyFinderClassName);
					} catch (IllegalAccessException e) {
						LOG.error("Unable to get new instance of finder class: " + keyFinderClassName);
						throw new RuntimeException("Unable to get new instance of finder class: " + keyFinderClassName);
					}
				}
			}

			if (Field.CHECKBOX.equals(fieldType) && convertForLookup) {
				fieldType = Field.RADIO;
				List<KeyLabelPair> keyLabelPairs = new ArrayList<KeyLabelPair>();
				for (KeyLabelPair keyLabelPair : IndicatorValuesFinder.INSTANCE
						.getKeyValues()) {
					if (StringUtils.equals(attributeName, "ovtEarnCode")
							&& StringUtils.equals(keyLabelPair.getKey()
									.toString(), "Y")) {
						keyLabelPairs.add(new KeyLabelPair("Yes", keyLabelPair
								.getLabel()));
						continue;
					}
					if (StringUtils.equals(attributeName, "ovtEarnCode")
							&& StringUtils.equals(keyLabelPair.getKey()
									.toString(), "N")) {
						keyLabelPairs.add(new KeyLabelPair("No", keyLabelPair
								.getLabel()));
						continue;
					}
					keyLabelPairs.add(keyLabelPair);
				}
				field.setFieldValidValues(keyLabelPairs);
			}

			// for button control
			if (Field.BUTTON.equals(fieldType)) {
				ButtonControlDefinition buttonControl = (ButtonControlDefinition) control;
				field.setImageSrc(buttonControl.getImageSrc());
				field.setStyleClass(buttonControl.getStyleClass());
			}

			// for link control
			if (Field.LINK.equals(fieldType)) {
				LinkControlDefinition linkControl = (LinkControlDefinition) control;
				field.setStyleClass(linkControl.getStyleClass());
				field.setTarget(linkControl.getTarget());
				field.setHrefText(linkControl.getHrefText());
			}

		}

		field.setFieldType(fieldType);
	}


    /**
     * Builds up a Field object based on the propertyName and business object class.
     *
     * See KULRICE-2480 for info on convertForLookup flag
     *
     * @param propertyName
     * @return Field
     */
    public static Field getPropertyField(Class businessObjectClass, String attributeName, boolean convertForLookup) {
        Field field = new Field();
        field.setPropertyName(attributeName);
        field.setFieldLabel(getDataDictionaryService().getAttributeLabel(businessObjectClass, attributeName));

        setFieldControl(businessObjectClass, attributeName, convertForLookup, field);

        Boolean fieldRequired = getBusinessObjectDictionaryService().getLookupAttributeRequired(businessObjectClass, attributeName);
        if (fieldRequired != null) {
            field.setFieldRequired(fieldRequired.booleanValue());
        }

        Integer maxLength = getDataDictionaryService().getAttributeMaxLength(businessObjectClass, attributeName);
        if (maxLength != null) {
            field.setMaxLength(maxLength.intValue());
        }

        Boolean upperCase = null;
        try {
            upperCase = getDataDictionaryService().getAttributeForceUppercase(businessObjectClass, attributeName);
        }
        catch (UnknownBusinessClassAttributeException t) {
        	// do nothing
        	LOG.warn( "UnknownBusinessClassAttributeException in fieldUtils.getPropertyField() : " + t.getMessage() );
        }
        if (upperCase != null) {
            field.setUpperCase(upperCase.booleanValue());
        }
        
		if (!businessObjectClass.isInterface()) {
			try {
				field.setFormatter(ObjectUtils.getFormatterWithDataDictionary(businessObjectClass.newInstance(),
						attributeName));
			} catch (InstantiationException e) {
				LOG.info("Unable to get new instance of business object class: " + businessObjectClass.getName(), e);
				// just swallow exception and leave formatter blank
			} catch (IllegalAccessException e) {
				LOG.info("Unable to get new instance of business object class: " + businessObjectClass.getName(), e);
				// just swallow exception and leave formatter blank
			}
		}

        // set Field help properties
        field.setBusinessObjectClassName(businessObjectClass.getName());
        field.setFieldHelpName(attributeName);
        field.setFieldHelpSummary(getDataDictionaryService().getAttributeSummary(businessObjectClass, attributeName));

        return field;
    }

	/**
	 * For attributes that are codes (determined by whether they have a
	 * reference to a KualiCode bo and similar naming) sets the name as an
	 * additional display property
	 * 
	 * @param businessObjectClass -
	 *            class containing attribute
	 * @param attributeName - 
	 *            name of attribute in the business object
	 * @param field - 
	 *            property display element
	 */
	public static void setAdditionalDisplayPropertyForCodes(Class businessObjectClass, String attributeName, PropertyRenderingConfigElement field) {
		try {
			BusinessObjectRelationship relationship = getBusinessObjectMetaDataService().getBusinessObjectRelationship(
					(BusinessObject) businessObjectClass.newInstance(), attributeName);

			if (relationship != null && attributeName.startsWith(relationship.getParentAttributeName())
					&& KualiCode.class.isAssignableFrom(relationship.getRelatedClass())) {
				field.setAdditionalDisplayPropertyName(relationship.getParentAttributeName() + "."
						+ KNSPropertyConstants.NAME);
			}
		} catch (Exception e) {
			throw new RuntimeException("Cannot get new instance of class to check for KualiCode references: "
					+ e.getMessage());
		}
	}


    /**
     * Wraps each Field in the list into a Row.
     *
     * @param fields
     * @return List of Row objects
     */
    public static List wrapFields(List fields) {
        return wrapFields(fields, KNSConstants.DEFAULT_NUM_OF_COLUMNS);
    }

    /**
     * This method is to implement multiple columns where the numberOfColumns is obtained from data dictionary.
     *
     * @param fields
     * @param numberOfColumns
     * @return
     */
    public static List<Row> wrapFields(List<Field> fields, int numberOfColumns) {

        List<Row> rows = new ArrayList();
        List<Field> fieldOnlyList = new ArrayList();

        List<Field> visableFields = getVisibleFields(fields);
    	List<Field> nonVisableFields = getNonVisibleFields(fields);

        int fieldsPosition = 0;
        for (Field element : visableFields) {
            if (Field.SUB_SECTION_SEPARATOR.equals(element.getFieldType()) || Field.CONTAINER.equals(element.getFieldType())) {
                fieldsPosition = createBlankSpace(fieldOnlyList, rows, numberOfColumns, fieldsPosition);
                List fieldList = new ArrayList();
                fieldList.add(element);
                rows.add(new Row(fieldList));
            }
            else {
                if (fieldsPosition < numberOfColumns) {
                    fieldOnlyList.add(element);
                    fieldsPosition++;
                }
                else {
                    rows.add(new Row(new ArrayList(fieldOnlyList)));
                    fieldOnlyList.clear();
                    fieldOnlyList.add(element);
                    fieldsPosition = 1;
                }
            }
        }
        createBlankSpace(fieldOnlyList, rows, numberOfColumns, fieldsPosition);

     // Add back the non Visible Rows
    	if(nonVisableFields != null && !nonVisableFields.isEmpty()){
    		Row nonVisRow = new Row();
    		nonVisRow.setFields(nonVisableFields);
    		rows.add(nonVisRow);
    	}


        return rows;
    }

    private static List<Field> getVisibleFields(List<Field> fields){
    	List<Field> rList = new ArrayList<Field>();

   		for(Field f: fields){
   			if(!Field.HIDDEN.equals(f.getFieldType()) &&  !Field.BLANK_SPACE.equals(f.getFieldType())){
   				rList.add(f);
   			}
   		}

    	return rList;
    }

    private static List<Field> getNonVisibleFields(List<Field> fields){
    	List<Field> rList = new ArrayList<Field>();

   		for(Field f: fields){
   			if(Field.HIDDEN.equals(f.getFieldType()) || Field.BLANK_SPACE.equals(f.getFieldType())){
   				rList.add(f);
   			}
   		}

    	return rList;
    }

    /**
     * This is a helper method to create and add a blank space to the fieldOnly List.
     *
     * @param fieldOnlyList
     * @param rows
     * @param numberOfColumns
     * @return fieldsPosition
     */
    private static int createBlankSpace(List<Field> fieldOnlyList, List<Row> rows, int numberOfColumns, int fieldsPosition) {
        int fieldOnlySize = fieldOnlyList.size();
        if (fieldOnlySize > 0) {
            for (int i = 0; i < (numberOfColumns - fieldOnlySize); i++) {
                Field empty = new Field();
                empty.setFieldType(Field.BLANK_SPACE);
                // Must be set or AbstractLookupableHelperServiceImpl::preprocessDateFields dies
                empty.setPropertyName(Field.BLANK_SPACE);
                fieldOnlyList.add(empty);
            }
            rows.add(new Row(new ArrayList(fieldOnlyList)));
            fieldOnlyList.clear();
            fieldsPosition = 0;
        }
        return fieldsPosition;
    }

    /**
     * Wraps list of fields into a Field of type CONTAINER
     *
     * @param name name for the field
     * @param label label for the field
     * @param fields list of fields that should be contained in the container
     * @return Field of type CONTAINER
     */
    public static Field constructContainerField(String name, String label, List fields) {
        return constructContainerField(name, label, fields, KNSConstants.DEFAULT_NUM_OF_COLUMNS);
    }

    /**
     * Wraps list of fields into a Field of type CONTAINER and arrange them into multiple columns.
     *
     * @param name name for the field
     * @param label label for the field
     * @param fields list of fields that should be contained in the container
     * @param numberOfColumns the number of columns for each row that the fields should be arranged into
     * @return Field of type CONTAINER
     */
    public static Field constructContainerField(String name, String label, List fields, int numberOfColumns) {
        Field containerField = new Field();
        containerField.setPropertyName(name);
        containerField.setFieldLabel(label);
        containerField.setFieldType(Field.CONTAINER);
        containerField.setNumberOfColumnsForCollection(numberOfColumns);

        List rows = wrapFields(fields, numberOfColumns);
        containerField.setContainerRows(rows);

        return containerField;
    }

    /**
     * Uses reflection to get the property names of the business object, then checks for a matching field property name. If found,
     * takes the value of the business object property and populates the field value. Iterates through for all fields in the list.
     *
     * @param fields list of Field object to populate
     * @param bo business object to get field values from
     * @return List of fields with values populated from business object.
     */
    public static List<Field> populateFieldsFromBusinessObject(List<Field> fields, BusinessObject bo) {
        List<Field> populatedFields = new ArrayList<Field>();

        if (bo instanceof PersistableBusinessObject) {
        	((PersistableBusinessObject) bo).refreshNonUpdateableReferences();
        }
        
        for (Iterator<Field> iter = fields.iterator(); iter.hasNext();) {
            Field element = iter.next();
            if (element.containsBOData()) {
                String propertyName = element.getPropertyName();

                // See: https://test.kuali.org/jira/browse/KULCOA-1185
                // Properties that could not possibly be set by the BusinessObject should be ignored.
                // (https://test.kuali.org/jira/browse/KULRNE-4354; this code was killing the src attribute of IMAGE_SUBMITs).
                if (isPropertyNested(propertyName) && !isObjectTreeNonNullAllTheWayDown(bo, propertyName) && ((!element.getFieldType().equals(Field.IMAGE_SUBMIT)) && !(element.getFieldType().equals(Field.CONTAINER)) && (!element.getFieldType().equals(Field.QUICKFINDER)))) {
                    element.setPropertyValue(null);
                }
                else if (PropertyUtils.isReadable(bo, propertyName)) {
                	populateReadableField(element, bo);
                }
                
    			if (StringUtils.isNotBlank(element.getAlternateDisplayPropertyName())) {
    				String alternatePropertyValue = ObjectUtils.getFormattedPropertyValueUsingDataDictionary(bo, element
    						.getAlternateDisplayPropertyName());
    				element.setAlternateDisplayPropertyValue(alternatePropertyValue);
    			}

    			if (StringUtils.isNotBlank(element.getAdditionalDisplayPropertyName())) {
    				String additionalPropertyValue = ObjectUtils.getFormattedPropertyValueUsingDataDictionary(bo, element
    						.getAdditionalDisplayPropertyName());
    				element.setAdditionalDisplayPropertyValue(additionalPropertyValue);
    			}
            }
            populatedFields.add(element);
        }

        return populatedFields;
    }

    public static void populateReadableField(Field field, BusinessObject businessObject){
		Object obj = ObjectUtils.getNestedValue(businessObject, field.getPropertyName());
		if (obj != null) {
			String formattedValue = ObjectUtils.getFormattedPropertyValueUsingDataDictionary(businessObject, field.getPropertyName());
			field.setPropertyValue(formattedValue);
        	
            // for user fields, attempt to pull the principal ID and person's name from the source object
            if ( field.getFieldType().equals(Field.KUALIUSER) ) {
            	// this is supplemental, so catch and log any errors
            	try {
            		if ( StringUtils.isNotBlank(field.getUniversalIdAttributeName()) ) {
            			Object principalId = ObjectUtils.getNestedValue(businessObject, field.getUniversalIdAttributeName());
            			if ( principalId != null ) {
            				field.setUniversalIdValue(principalId.toString());
            			}
            		}
            		if ( StringUtils.isNotBlank(field.getPersonNameAttributeName()) ) {
            			Object personName = ObjectUtils.getNestedValue(businessObject, field.getPersonNameAttributeName());
            			if ( personName != null ) {
            				field.setPersonNameValue( personName.toString() );
            			}
            		}
            	} catch ( Exception ex ) {
            		LOG.warn( "Unable to get principal ID or person name property in FieldBridge.", ex );
            	}
            }
        }
        
        populateSecureField(field, obj);
    }

    public static void populateSecureField(Field field, Object fieldValue){
        // set encrypted & masked value if user does not have permission to see real value in UI
        // element.isSecure() => a non-null AttributeSecurity object is set in the field
        if (field.isSecure()) {
            try {
                if (fieldValue != null && fieldValue.toString().endsWith(EncryptionService.HASH_POST_PREFIX)) {
                	field.setEncryptedValue(fieldValue.toString());
                }
                else {
                	field.setEncryptedValue(KNSServiceLocator.getEncryptionService().encrypt(fieldValue) + EncryptionService.ENCRYPTION_POST_PREFIX);
                }
            }
            catch (GeneralSecurityException e) {
                throw new RuntimeException("Unable to encrypt secure field " + e.getMessage());
            }
            //field.setDisplayMaskValue(field.getAttributeSecurity().getDisplayMaskValue(fieldValue));
        }
    }

    /**
     * This method indicates whether or not propertyName refers to a nested attribute.
     *
     * @param propertyName
     * @return true if propertyName refers to a nested property (e.g. "x.y")
     */
    static private boolean isPropertyNested(String propertyName) {
        return -1 != propertyName.indexOf('.');
    }

    /**
     * This method verifies that all of the parent objects of propertyName are non-null.
     *
     * @param bo
     * @param propertyName
     * @return true if all parents are non-null, otherwise false
     */

    static private boolean isObjectTreeNonNullAllTheWayDown(BusinessObject bo, String propertyName) {
        String[] propertyParts = propertyName.split("\\.");

        StringBuffer property = new StringBuffer();
        for (int i = 0; i < propertyParts.length - 1; i++) {

            property.append((0 == property.length()) ? "" : ".").append(propertyParts[i]);
            try {
                if (null == PropertyUtils.getNestedProperty(bo, property.toString())) {
                    return false;
                }
            }
            catch (Throwable t) {
                LOG.debug("Either getter or setter not specified for property \"" + property.toString() + "\"", t);
                return false;
            }
        }

        return true;

    }

    /**
     * @param bo
     * @param propertyName
     * @return true if one (or more) of the intermediate objects in the given propertyName is null
     */
    private static boolean containsIntermediateNull(Object bo, String propertyName) {
        boolean containsNull = false;

        if (StringUtils.contains(propertyName, ".")) {
            String prefix = StringUtils.substringBefore(propertyName, ".");
            Object propertyValue = ObjectUtils.getPropertyValue(bo, prefix);

            if (propertyValue == null) {
                containsNull = true;
            }
            else {
                String suffix = StringUtils.substringAfter(propertyName, ".");
                containsNull = containsIntermediateNull(propertyValue, suffix);
            }
        }

        return containsNull;
    }

    /**
     * Uses reflection to get the property names of the business object, then checks for the property name as a key in the passed
     * map. If found, takes the value from the map and sets the business object property.
     *
     * @param bo
     * @param fieldValues
     * @return Cached Values from any formatting failures
     */
    public static Map populateBusinessObjectFromMap(BusinessObject bo, Map fieldValues) {
        return populateBusinessObjectFromMap(bo, fieldValues, "");
    }

    /**
     * Uses reflection to get the property names of the business object, then checks for the property name as a key in the passed
     * map. If found, takes the value from the map and sets the business object property.
     *
     * @param bo
     * @param fieldValues
     * @param propertyNamePrefix this value will be prepended to all property names in the returned unformattable values map
     * @return Cached Values from any formatting failures
     */
    public static Map populateBusinessObjectFromMap(BusinessObject bo, Map<String, ?> fieldValues, String propertyNamePrefix) {
        Map cachedValues = new HashMap();
        MessageMap errorMap = GlobalVariables.getMessageMap();

        try {
            for (Iterator<String> iter = fieldValues.keySet().iterator(); iter.hasNext();) {
                String propertyName = iter.next();

                if (propertyName.endsWith(KNSConstants.CHECKBOX_PRESENT_ON_FORM_ANNOTATION)) {
                    // since checkboxes do not post values when unchecked, this code detects whether a checkbox was unchecked, and
                    // sets the value to false.
                    if (StringUtils.isNotBlank((String) fieldValues.get(propertyName))) {
                        String checkboxName = StringUtils.removeEnd(propertyName, KNSConstants.CHECKBOX_PRESENT_ON_FORM_ANNOTATION);
                        String checkboxValue = (String) fieldValues.get(checkboxName);
                        if (checkboxValue == null) {
                            // didn't find a checkbox value, assume that it is unchecked
                            if (PropertyUtils.isWriteable(bo, checkboxName)) {
                                Class type = ObjectUtils.easyGetPropertyType(bo, checkboxName);
                                if (type == Boolean.TYPE || type == Boolean.class) {
                                    // ASSUMPTION: unchecked means false
                                    ObjectUtils.setObjectProperty(bo, checkboxName, type, "false");
                                }
                            }
                        }
                    }
                    // else, if not null, then it has a value, and we'll let the rest of the code handle it when the param is processed on
                    // another iteration (may be before or after this iteration).
                }
                else if (PropertyUtils.isWriteable(bo, propertyName) && fieldValues.get(propertyName) != null ) {
                    // if the field propertyName is a valid property on the bo class
                    Class type = ObjectUtils.easyGetPropertyType(bo, propertyName);
                    try {
                    	Object fieldValue = fieldValues.get(propertyName);
                        ObjectUtils.setObjectProperty(bo, propertyName, type, fieldValue);
                    }
                    catch (FormatException e) {
                        cachedValues.put(propertyNamePrefix + propertyName, fieldValues.get(propertyName));
                        errorMap.putError(propertyNamePrefix + propertyName, e.getErrorKey(), e.getErrorArgs());
                    }
                }
            }
        }
        catch (IllegalAccessException e) {
            LOG.error("unable to populate business object" + e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
        catch (InvocationTargetException e) {
            LOG.error("unable to populate business object" + e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
        catch (NoSuchMethodException e) {
            LOG.error("unable to populate business object" + e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }

        return cachedValues;
    }

    /**
     * Does prefixing and read only settings of a Field UI for display in a maintenance document.
     *
     * @param field - the Field object to be displayed
     * @param keyFieldNames - Primary key property names for the business object being maintained.
     * @param namePrefix - String to prefix Field names with.
     * @param maintenanceAction - The maintenance action requested.
     * @param readOnly - Indicates whether all fields should be read only.
     * @return Field
     */
    public static Field fixFieldForForm(Field field, List keyFieldNames, String namePrefix, String maintenanceAction, boolean readOnly, MaintenanceDocumentRestrictions auths, String documentStatus, String documentInitiatorPrincipalId) {
        String propertyName = field.getPropertyName();
        // We only need to do the following processing if the field is not a sub section header
        if (field.containsBOData()) {

            // don't prefix submit fields, must start with dispatch parameter name
            if (!propertyName.startsWith(KNSConstants.DISPATCH_REQUEST_PARAMETER)) {
                // if the developer hasn't set a specific prefix use the one supplied
                if (field.getPropertyPrefix() == null || field.getPropertyPrefix().equals("")) {
                    field.setPropertyName(namePrefix + propertyName);
                }
                else {
                    field.setPropertyName(field.getPropertyPrefix() + "." + propertyName);
                }
            }

            if (readOnly) {
                field.setReadOnly(true);
            }

            // set keys read only for edit
            if ( KNSConstants.MAINTENANCE_EDIT_ACTION.equals(maintenanceAction) ) {
            	if (keyFieldNames.contains(propertyName) ) {
	                field.setReadOnly(true);
	                field.setKeyField(true);
	            } else if ( StringUtils.isNotBlank( field.getUniversalIdAttributeName() )
	            		&& keyFieldNames.contains(field.getUniversalIdAttributeName() ) ) {
	            	// special handling for when the principal ID is the PK field for a record
	            	// this causes locking down of the user ID field
	                field.setReadOnly(true);
	                field.setKeyField(true);
	            }
            }

            // apply any authorization restrictions to field availability on the UI
            applyAuthorization(field, maintenanceAction, auths, documentStatus, documentInitiatorPrincipalId);

            // if fieldConversions specified, prefix with new constant
            if (StringUtils.isNotBlank(field.getFieldConversions())) {
                String fieldConversions = field.getFieldConversions();
                String newFieldConversions = KNSConstants.EMPTY_STRING;
                String[] conversions = StringUtils.split(fieldConversions, KNSConstants.FIELD_CONVERSIONS_SEPARATOR);

                for (int l = 0; l < conversions.length; l++) {
                    String conversion = conversions[l];
                    //String[] conversionPair = StringUtils.split(conversion, KNSConstants.FIELD_CONVERSION_PAIR_SEPARATOR);
                    String[] conversionPair = StringUtils.split(conversion, KNSConstants.FIELD_CONVERSION_PAIR_SEPARATOR, 2);
                    String conversionFrom = conversionPair[0];
                    String conversionTo = conversionPair[1];
                    conversionTo = KNSConstants.MAINTENANCE_NEW_MAINTAINABLE + conversionTo;
                    newFieldConversions += (conversionFrom + KNSConstants.FIELD_CONVERSION_PAIR_SEPARATOR + conversionTo);

                    if (l < conversions.length) {
                        newFieldConversions += KNSConstants.FIELD_CONVERSIONS_SEPARATOR;
                    }
                }

                field.setFieldConversions(newFieldConversions);
            }

            // if inquiryParameters specified, prefix with new constant
            if (StringUtils.isNotBlank(field.getInquiryParameters())) {
                String inquiryParameters = field.getInquiryParameters();
                StringBuilder newInquiryParameters = new StringBuilder();
                String[] parameters = StringUtils.split(inquiryParameters, KNSConstants.FIELD_CONVERSIONS_SEPARATOR);

                for (int l = 0; l < parameters.length; l++) {
                    String parameter = parameters[l];
                    //String[] parameterPair = StringUtils.split(parameter, KNSConstants.FIELD_CONVERSION_PAIR_SEPARATOR);
                    String[] parameterPair = StringUtils.split(parameter, KNSConstants.FIELD_CONVERSION_PAIR_SEPARATOR, 2);
                    String conversionFrom = parameterPair[0];
                    String conversionTo = parameterPair[1];

                    // append the conversionFrom string, prefixed by document.newMaintainable
                    newInquiryParameters.append(KNSConstants.MAINTENANCE_NEW_MAINTAINABLE).append(conversionFrom);

                    newInquiryParameters.append(KNSConstants.FIELD_CONVERSION_PAIR_SEPARATOR).append(conversionTo);

                    if (l < parameters.length - 1) {
                        newInquiryParameters.append(KNSConstants.FIELD_CONVERSIONS_SEPARATOR);
                    }
                }

                field.setInquiryParameters(newInquiryParameters.toString());
            }

            if (Field.KUALIUSER.equals(field.getFieldType())) {
                // prefix the personNameAttributeName
            	int suffixIndex = field.getPropertyName().indexOf( field.getUserIdAttributeName() );
            	if ( suffixIndex != -1 ) {
            		field.setPersonNameAttributeName( field.getPropertyName().substring( 0, suffixIndex ) + field.getPersonNameAttributeName() );
            		field.setUniversalIdAttributeName( field.getPropertyName().substring( 0, suffixIndex ) + field.getUniversalIdAttributeName() );
            	} else {
            		field.setPersonNameAttributeName(namePrefix + field.getPersonNameAttributeName());
            		field.setUniversalIdAttributeName(namePrefix + field.getUniversalIdAttributeName());
            	}

                // TODO: do we need to prefix the universalIdAttributeName in Field as well?
            }

            // if lookupParameters specified, prefix with new constant
            if (StringUtils.isNotBlank(field.getLookupParameters())) {
                String lookupParameters = field.getLookupParameters();
                String newLookupParameters = KNSConstants.EMPTY_STRING;
                String[] conversions = StringUtils.split(lookupParameters, KNSConstants.FIELD_CONVERSIONS_SEPARATOR);

                for (int m = 0; m < conversions.length; m++) {
                    String conversion = conversions[m];
                    //String[] conversionPair = StringUtils.split(conversion, KNSConstants.FIELD_CONVERSION_PAIR_SEPARATOR);
                    String[] conversionPair = StringUtils.split(conversion, KNSConstants.FIELD_CONVERSION_PAIR_SEPARATOR, 2);                    
                    String conversionFrom = conversionPair[0];
                    String conversionTo = conversionPair[1];
                    conversionFrom = KNSConstants.MAINTENANCE_NEW_MAINTAINABLE + conversionFrom;
                    newLookupParameters += (conversionFrom + KNSConstants.FIELD_CONVERSION_PAIR_SEPARATOR + conversionTo);

                    if (m < conversions.length) {
                        newLookupParameters += KNSConstants.FIELD_CONVERSIONS_SEPARATOR;
                    }
                }

                field.setLookupParameters(newLookupParameters);
            }

            // CONTAINER field types have nested rows and fields that need setup for the form
            if (Field.CONTAINER.equals(field.getFieldType())) {
                List containerRows = field.getContainerRows();
                List fixedRows = new ArrayList();

                for (Iterator iter = containerRows.iterator(); iter.hasNext();) {
                    Row containerRow = (Row) iter.next();
                    List containerFields = containerRow.getFields();
                    List fixedFields = new ArrayList();

                    for (Iterator iterator = containerFields.iterator(); iterator.hasNext();) {
                        Field containerField = (Field) iterator.next();
                        containerField = fixFieldForForm(containerField, keyFieldNames, namePrefix, maintenanceAction, readOnly, auths, documentStatus, documentInitiatorPrincipalId);
                        fixedFields.add(containerField);
                    }

                    fixedRows.add(new Row(fixedFields));
                }

                field.setContainerRows(fixedRows);
            }
        }
        field.setFieldDirectInquiryEnabled(false);
        
        return field;
    }

    public static void applyAuthorization(Field field, String maintenanceAction, MaintenanceDocumentRestrictions auths, String documentStatus, String documentInitiatorPrincipalId) {
    	String fieldName = "";
    	FieldRestriction fieldAuth = null;
    	Person user = GlobalVariables.getUserSession().getPerson();
        // only apply this on the newMaintainable
        if (field.getPropertyName().startsWith(KNSConstants.MAINTENANCE_NEW_MAINTAINABLE)) {
            // get just the actual fieldName, with the document.newMaintainableObject, etc etc removed
            fieldName = field.getPropertyName().substring(KNSConstants.MAINTENANCE_NEW_MAINTAINABLE.length());

            // if the field is restricted somehow
            if (auths.hasRestriction(fieldName)) {
                fieldAuth = auths.getFieldRestriction(fieldName);
                if(KNSConstants.MAINTENANCE_NEW_ACTION.equals(maintenanceAction) || KNSConstants.MAINTENANCE_COPY_ACTION.equals(maintenanceAction)){
                	if((KEWConstants.ROUTE_HEADER_SAVED_CD.equals(documentStatus) || KEWConstants.ROUTE_HEADER_INITIATED_CD.equals(documentStatus))
                		&& user.getPrincipalId().equals(documentInitiatorPrincipalId)){

                		//user should be able to see the unmark value
                	}else{
                		if(fieldAuth.isPartiallyMasked()){
    	                	field.setSecure(true);
    	                	fieldAuth.setShouldBeEncrypted(true);
    	                	MaskFormatter maskFormatter = fieldAuth.getMaskFormatter();
    	                	String displayMaskValue = maskFormatter.maskValue(field.getPropertyValue());
    	                	field.setDisplayMaskValue(displayMaskValue);
    	                	populateSecureField(field, field.getPropertyValue());
                    	}
    	                else if(fieldAuth.isMasked()){
    	                	field.setSecure(true);
    	                	fieldAuth.setShouldBeEncrypted(true);
    	                	MaskFormatter maskFormatter = fieldAuth.getMaskFormatter();
    	                	String displayMaskValue = maskFormatter.maskValue(field.getPropertyValue());
    	                	field.setDisplayMaskValue(displayMaskValue);
    	                	populateSecureField(field, field.getPropertyValue());
    	                }
                	}
                }

                if (KNSConstants.MAINTENANCE_EDIT_ACTION.equals(maintenanceAction) || KNSConstants.MAINTENANCE_NEWWITHEXISTING_ACTION.equals(maintenanceAction)) {
                	// if there's existing data on the page that we're not going to clear out, then we will mask it out
                	if(fieldAuth.isPartiallyMasked()){
	                	field.setSecure(true);
	                	fieldAuth.setShouldBeEncrypted(true);
	                	MaskFormatter maskFormatter = fieldAuth.getMaskFormatter();
	                	String displayMaskValue = maskFormatter.maskValue(field.getPropertyValue());
	                	field.setDisplayMaskValue(displayMaskValue);
	                	populateSecureField(field, field.getPropertyValue());
                	}
	                else if(fieldAuth.isMasked()){
	                	field.setSecure(true);
	                	fieldAuth.setShouldBeEncrypted(true);
	                	MaskFormatter maskFormatter = fieldAuth.getMaskFormatter();
	                	String displayMaskValue = maskFormatter.maskValue(field.getPropertyValue());
	                	field.setDisplayMaskValue(displayMaskValue);
	                	populateSecureField(field, field.getPropertyValue());
	                }
                }

                if (Field.isInputField(field.getFieldType()) || field.getFieldType().equalsIgnoreCase(Field.CHECKBOX)) {
                	// if its an editable field, allow decreasing availability to readonly or hidden
                    // only touch the field if the restricted type is hidden or readonly
                    if (fieldAuth.isReadOnly()) {
                        if (!field.isReadOnly() && !fieldAuth.isMasked() && !fieldAuth.isPartiallyMasked()) {
                            field.setReadOnly(true);
                        }
                    }
                    else if (fieldAuth.isHidden()) {
                        if (field.getFieldType() != Field.HIDDEN) {
                            field.setFieldType(Field.HIDDEN);
                        }
                    }
                }

                if(Field.BUTTON.equalsIgnoreCase(field.getFieldType()) && fieldAuth.isHidden()){
                	field.setFieldType(Field.HIDDEN);
                }

                // if the field is readOnly, and the authorization says it should be hidden,
                // then restrict it
                if (field.isReadOnly() && fieldAuth.isHidden()) {
                    field.setFieldType(Field.HIDDEN);
                }

            }
            // special check for old maintainable - need to ensure that fields hidden on the
            // "new" side are also hidden on the old side
        }
        else if (field.getPropertyName().startsWith(KNSConstants.MAINTENANCE_OLD_MAINTAINABLE)) {
            // get just the actual fieldName, with the document.oldMaintainableObject, etc etc removed
            fieldName = field.getPropertyName().substring(KNSConstants.MAINTENANCE_OLD_MAINTAINABLE.length());
            // if the field is restricted somehow
            if (auths.hasRestriction(fieldName)) {
                fieldAuth = auths.getFieldRestriction(fieldName);
                if(fieldAuth.isPartiallyMasked()){
                    field.setSecure(true);
                    MaskFormatter maskFormatter = fieldAuth.getMaskFormatter();
                    String displayMaskValue = maskFormatter.maskValue(field.getPropertyValue());
                    field.setDisplayMaskValue(displayMaskValue);
                    field.setPropertyValue(displayMaskValue);
                    populateSecureField(field, field.getPropertyValue());

               }

               if(fieldAuth.isMasked()){
                    field.setSecure(true);
                    MaskFormatter maskFormatter = fieldAuth.getMaskFormatter();
                    String displayMaskValue = maskFormatter.maskValue(field.getPropertyValue());
                    field.setDisplayMaskValue(displayMaskValue);
                    field.setPropertyValue(displayMaskValue);
                    populateSecureField(field, field.getPropertyValue());
                }

                if (fieldAuth.isHidden()) {
                    field.setFieldType(Field.HIDDEN);
                }
            }
        }
    }

    /**
     * Merges together sections of the old maintainable and new maintainable.
     *
     * @param oldSections
     * @param newSections
     * @param keyFieldNames
     * @param maintenanceAction
     * @param readOnly
     * @return List of Section objects
     */
    public static List meshSections(List oldSections, List newSections, List keyFieldNames, String maintenanceAction, boolean readOnly, MaintenanceDocumentRestrictions auths, String documentStatus, String documentInitiatorPrincipalId) {
        List meshedSections = new ArrayList();

        for (int i = 0; i < newSections.size(); i++) {
            Section maintSection = (Section) newSections.get(i);
            List sectionRows = maintSection.getRows();
            Section oldMaintSection = (Section) oldSections.get(i);
            List oldSectionRows = oldMaintSection.getRows();
            List<Row> meshedRows = new ArrayList();
            meshedRows = meshRows(oldSectionRows, sectionRows, keyFieldNames, maintenanceAction, readOnly, auths, documentStatus, documentInitiatorPrincipalId);
            maintSection.setRows(meshedRows);
            if (StringUtils.isBlank(maintSection.getErrorKey())) {
                maintSection.setErrorKey(MaintenanceUtils.generateErrorKeyForSection(maintSection));
            }
            meshedSections.add(maintSection);
        }

        return meshedSections;
    }

    /**
     * Merges together rows of an old maintainable section and new maintainable section.
     *
     * @param oldRows
     * @param newRows
     * @param keyFieldNames
     * @param maintenanceAction
     * @param readOnly
     * @return List of Row objects
     */
    public static List meshRows(List oldRows, List newRows, List keyFieldNames, String maintenanceAction, boolean readOnly, MaintenanceDocumentRestrictions auths, String documentStatus, String documentInitiatorPrincipalId) {
        List<Row> meshedRows = new ArrayList<Row>();

        for (int j = 0; j < newRows.size(); j++) {
            Row sectionRow = (Row) newRows.get(j);
            List rowFields = sectionRow.getFields();
            Row oldSectionRow = null;
            List oldRowFields = new ArrayList();

            if (null != oldRows && oldRows.size() > j) {
                oldSectionRow = (Row) oldRows.get(j);
                oldRowFields = oldSectionRow.getFields();
            }

            List meshedFields = meshFields(oldRowFields, rowFields, keyFieldNames, maintenanceAction, readOnly, auths, documentStatus, documentInitiatorPrincipalId);
            if (meshedFields.size() > 0) {
                Row meshedRow = new Row(meshedFields);
                if (sectionRow.isHidden()) {
                    meshedRow.setHidden(true);
                }

                meshedRows.add(meshedRow);
            }
        }

        return meshedRows;
    }


    /**
     * Merges together fields and an old maintainble row and new maintainable row, for each field call fixFieldForForm.
     *
     * @param oldFields
     * @param newFields
     * @param keyFieldNames
     * @param maintenanceAction
     * @param readOnly
     * @return List of Field objects
     */
    public static List meshFields(List oldFields, List newFields, List keyFieldNames, String maintenanceAction, boolean readOnly, MaintenanceDocumentRestrictions auths, String documentStatus, String documentInitiatorPrincipalId) {
        List meshedFields = new ArrayList();

        List newFieldsToMerge = new ArrayList();
        List oldFieldsToMerge = new ArrayList();

        for (int k = 0; k < newFields.size(); k++) {
            Field newMaintField = (Field) newFields.get(k);
            String propertyName = newMaintField.getPropertyName();
            // If this is an add button, then we have to have only this field for the entire row.
            if (Field.IMAGE_SUBMIT.equals(newMaintField.getFieldType())) {
                meshedFields.add(newMaintField);
            }
            else if (Field.CONTAINER.equals(newMaintField.getFieldType())) {
                if (oldFields.size() > k) {
                    Field oldMaintField = (Field) oldFields.get(k);
                    newMaintField = meshContainerFields(oldMaintField, newMaintField, keyFieldNames, maintenanceAction, readOnly, auths, documentStatus, documentInitiatorPrincipalId);
                }
                else {
                    newMaintField = meshContainerFields(newMaintField, newMaintField, keyFieldNames, maintenanceAction, readOnly, auths, documentStatus, documentInitiatorPrincipalId);
                }
                meshedFields.add(newMaintField);
            }
            else {
                newMaintField = FieldUtils.fixFieldForForm(newMaintField, keyFieldNames, KNSConstants.MAINTENANCE_NEW_MAINTAINABLE, maintenanceAction, readOnly, auths, documentStatus, documentInitiatorPrincipalId);
                // add old fields for edit
                if (KNSConstants.MAINTENANCE_EDIT_ACTION.equals(maintenanceAction) || KNSConstants.MAINTENANCE_COPY_ACTION.equals(maintenanceAction)) {
                    Field oldMaintField = (Field) oldFields.get(k);

                    // compare values for change, and set new maintainable fields for highlighting
                    // no point in highlighting the hidden fields, since they won't be rendered anyways
                    if (!StringUtils.equalsIgnoreCase(newMaintField.getPropertyValue(), oldMaintField.getPropertyValue())
                            && !Field.HIDDEN.equals(newMaintField.getFieldType())) {
                        newMaintField.setHighlightField(true);
                    }

                    oldMaintField = FieldUtils.fixFieldForForm(oldMaintField, keyFieldNames, KNSConstants.MAINTENANCE_OLD_MAINTAINABLE, maintenanceAction, true, auths, documentStatus, documentInitiatorPrincipalId);
                    oldFieldsToMerge.add(oldMaintField);
                }

                newFieldsToMerge.add(newMaintField);

                for (Iterator iter = oldFieldsToMerge.iterator(); iter.hasNext();) {
                    Field element = (Field) iter.next();
                    meshedFields.add(element);
                }

                for (Iterator iter = newFieldsToMerge.iterator(); iter.hasNext();) {
                    Field element = (Field) iter.next();
                    meshedFields.add(element);
                }
            }
        }
        return meshedFields;
    }

    /**
     * Determines whether field level help is enabled for the field corresponding to the businessObjectClass and attribute name
     *
     * If this value is true, then the field level help will be enabled.
     * If false, then whether a field is enabled is determined by the value returned by {@link #isLookupFieldLevelHelpDisabled(Class, String)} and the system-wide
     * parameter setting.  Note that if a field is read-only, that may cause field-level help to not be rendered.
     *
     * @param businessObjectClass the looked up class
     * @param attributeName the attribute for the field
     * @return true if field level help is enabled, false if the value of this method should NOT be used to determine whether this method's return value
     * affects the enablement of field level help
     */
    protected static boolean isLookupFieldLevelHelpEnabled(Class businessObjectClass, String attributeName) {
        return false;
    }

    /**
     * Determines whether field level help is disabled for the field corresponding to the businessObjectClass and attribute name
     *
     * If this value is true and {@link #isLookupFieldLevelHelpEnabled(Class, String)} returns false,
     * then the field level help will not be rendered.  If both this and {@link #isLookupFieldLevelHelpEnabled(Class, String)} return false, then the system-wide
     * setting will determine whether field level help is enabled.  Note that if a field is read-only, that may cause field-level help to not be rendered.
     *
     * @param businessObjectClass the looked up class
     * @param attributeName the attribute for the field
     * @return true if field level help is disabled, false if the value of this method should NOT be used to determine whether this method's return value
     * affects the enablement of field level help
     */
    protected static boolean isLookupFieldLevelHelpDisabled(Class businessObjectClass, String attributeName) {
        return false;
    }

    public static List createAndPopulateFieldsForLookup(List<String> lookupFieldAttributeList, List<String> readOnlyFieldsList, Class businessObjectClass) throws InstantiationException, IllegalAccessException {
        List<Field> fields = new ArrayList<Field>();
        BusinessObjectEntry boe = getDataDictionaryService().getDataDictionary().getBusinessObjectEntry(businessObjectClass.getName());

        Map<String, Boolean> isHiddenMap = new HashMap<String, Boolean>();
        Map<String, Boolean> isReadOnlyMap = new HashMap<String, Boolean>();

        /*
    	 * Check if any field is hidden or read only.  This allows us to
    	 * set lookup criteria as hidden/readonly outside the controlDefinition.
    	 */
    	if(boe.hasLookupDefinition()){
    		List<FieldDefinition> fieldDefs = boe.getLookupDefinition().getLookupFields();
    		for(FieldDefinition field : fieldDefs){
				isReadOnlyMap.put(field.getAttributeName(), Boolean.valueOf(field.isReadOnly()));
				isHiddenMap.put(field.getAttributeName(), Boolean.valueOf(field.isHidden()));
    		}
    	}

        for( String attributeName : lookupFieldAttributeList )
        {
            Field field = FieldUtils.getPropertyField(businessObjectClass, attributeName, true);

            if(field.isDatePicker() && field.isRanged()) {

            	Field newDate = createRangeDateField(field);
            	fields.add(newDate);
            }

            BusinessObject newBusinessObjectInstance;
            if (ExternalizableBusinessObjectUtils.isExternalizableBusinessObjectInterface(businessObjectClass)) {
            	ModuleService moduleService = getKualiModuleService().getResponsibleModuleService(businessObjectClass);
            	newBusinessObjectInstance = (BusinessObject) moduleService.createNewObjectFromExternalizableClass(businessObjectClass);
            }
            else {
            	newBusinessObjectInstance = (BusinessObject) businessObjectClass.newInstance();
            }
            //quickFinder is synonymous with a field-based Lookup
            field = LookupUtils.setFieldQuickfinder(newBusinessObjectInstance, attributeName, field, lookupFieldAttributeList);
            field = LookupUtils.setFieldDirectInquiry(newBusinessObjectInstance, attributeName, field);
            field.setFieldDirectInquiryEnabled(false);
            // overwrite maxLength to allow for wildcards and ranges in the select, but only if it's not a mulitselect box, because maxLength determines the # of entries
            if (!Field.MULTISELECT.equals(field.getFieldType())) {
            	field.setMaxLength(100);
            }

            // if the attrib name is "active", and BO is Inactivatable, then set the default value to Y
            if (attributeName.equals(KNSPropertyConstants.ACTIVE) && Inactivateable.class.isAssignableFrom(businessObjectClass)) {
            	field.setPropertyValue(KNSConstants.YES_INDICATOR_VALUE);
            	field.setDefaultValue(KNSConstants.YES_INDICATOR_VALUE);
            }
            // set default value
            String defaultValue = getBusinessObjectMetaDataService().getLookupFieldDefaultValue(businessObjectClass, attributeName);
            if (defaultValue != null) {
                field.setPropertyValue(defaultValue);
                field.setDefaultValue(defaultValue);
            }

            Class defaultValueFinderClass = getBusinessObjectMetaDataService().getLookupFieldDefaultValueFinderClass(businessObjectClass, attributeName);
            //getBusinessObjectMetaDataService().getLookupFieldDefaultValue(businessObjectClass, attributeName)
            if (defaultValueFinderClass != null) {
                field.setPropertyValue(((ValueFinder) defaultValueFinderClass.newInstance()).getValue());
                field.setDefaultValue(((ValueFinder) defaultValueFinderClass.newInstance()).getValue());
            }
            if ( (readOnlyFieldsList != null && readOnlyFieldsList.contains(field.getPropertyName()))
            		|| ( isReadOnlyMap.containsKey(field.getPropertyName()) && isReadOnlyMap.get(field.getPropertyName()).booleanValue())
            	) {
                field.setReadOnly(true);
            }

            populateQuickfinderDefaultsForLookup(businessObjectClass, attributeName, field);

			if ((isHiddenMap.containsKey(field.getPropertyName()) && isHiddenMap.get(field.getPropertyName()).booleanValue())) {
				field.setFieldType(Field.HIDDEN);
			}
            
            boolean triggerOnChange = getBusinessObjectDictionaryService().isLookupFieldTriggerOnChange(businessObjectClass, attributeName);
            field.setTriggerOnChange(triggerOnChange);

            field.setFieldLevelHelpEnabled(isLookupFieldLevelHelpEnabled(businessObjectClass, attributeName));
            field.setFieldLevelHelpDisabled(isLookupFieldLevelHelpDisabled(businessObjectClass, attributeName));
            
            fields.add(field);
        }
        return fields;
    }


	/**
	 * This method ...
	 *
	 * @param businessObjectClass
	 * @param attributeName
	 * @param field
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	private static void populateQuickfinderDefaultsForLookup(
			Class businessObjectClass, String attributeName, Field field)
			throws InstantiationException, IllegalAccessException {
		// handle quickfinderParameterString / quickfinderParameterFinderClass
		String quickfinderParamString = getBusinessObjectMetaDataService().getLookupFieldQuickfinderParameterString(businessObjectClass, attributeName);
		Class<? extends ValueFinder> quickfinderParameterFinderClass =
			getBusinessObjectMetaDataService().getLookupFieldQuickfinderParameterStringBuilderClass(businessObjectClass, attributeName);
		if (quickfinderParameterFinderClass != null) {
			quickfinderParamString = quickfinderParameterFinderClass.newInstance().getValue();
		}

		if (!StringUtils.isEmpty(quickfinderParamString)) {
			String [] params = quickfinderParamString.split(",");
			if (params != null) for (String param : params) {
				if (param.contains(KNSConstants.LOOKUP_PARAMETER_LITERAL_DELIMITER)) {
					String[] paramChunks = param.split(KNSConstants.LOOKUP_PARAMETER_LITERAL_DELIMITER, 2);
					field.appendLookupParameters(
							KNSConstants.LOOKUP_PARAMETER_LITERAL_PREFIX+KNSConstants.LOOKUP_PARAMETER_LITERAL_DELIMITER+
							paramChunks[1]+":"+paramChunks[0]);
				}
			}
		}
	}


	/**
	 * creates an extra field for date from/to ranges
	 * @param field
	 * @return a new date field
	 */
	public static Field createRangeDateField(Field field) {
		Field newDate = (Field)ObjectUtils.deepCopy(field);
		newDate.setFieldLabel(newDate.getFieldLabel()+" "+KNSConstants.LOOKUP_DEFAULT_RANGE_SEARCH_LOWER_BOUND_LABEL);
		field.setFieldLabel(field.getFieldLabel()+" "+KNSConstants.LOOKUP_DEFAULT_RANGE_SEARCH_UPPER_BOUND_LABEL);
		newDate.setPropertyName(KNSConstants.LOOKUP_RANGE_LOWER_BOUND_PROPERTY_PREFIX+newDate.getPropertyName());
		return newDate;
	}

    private static Field meshContainerFields(Field oldMaintField, Field newMaintField, List keyFieldNames, String maintenanceAction, boolean readOnly, MaintenanceDocumentRestrictions auths, String documentStatus, String documentInitiatorPrincipalId) {
        List resultingRows = new ArrayList();
        resultingRows.addAll(meshRows(oldMaintField.getContainerRows(), newMaintField.getContainerRows(), keyFieldNames, maintenanceAction, readOnly, auths, documentStatus, documentInitiatorPrincipalId));
        Field resultingField = newMaintField;
        resultingField.setFieldType(Field.CONTAINER);

        // save the summary info
        resultingField.setContainerElementName(newMaintField.getContainerElementName());
        resultingField.setContainerDisplayFields(newMaintField.getContainerDisplayFields());
        resultingField.setNumberOfColumnsForCollection(newMaintField.getNumberOfColumnsForCollection());

        resultingField.setContainerRows(resultingRows);
        List resultingRowsList = newMaintField.getContainerRows();
        if (resultingRowsList.size() > 0) {
            List resultingFieldsList = ((Row) resultingRowsList.get(0)).getFields();
            if (resultingFieldsList.size() > 0) {
                // todo: assign the correct propertyName to the container in the first place. For now, I'm wary of the weird usages
                // of constructContainerField().
                String containedFieldName = ((Field) (resultingFieldsList.get(0))).getPropertyName();
                resultingField.setPropertyName(containedFieldName.substring(0, containedFieldName.lastIndexOf('.')));
            }
        }
        else {
            resultingField.setPropertyName(oldMaintField.getPropertyName());
        }
        return resultingField;
    }

    /**
     * This method modifies the passed in field so that it may be used to render a multiple values lookup button
     *
     * @param field this object will be modified by this method
     * @param parents
     * @param definition
     */
    static final public void modifyFieldToSupportMultipleValueLookups(Field field, String parents, MaintainableCollectionDefinition definition) {
        field.setMultipleValueLookedUpCollectionName(parents + definition.getName());
        field.setMultipleValueLookupClassName(definition.getSourceClassName().getName());
        field.setMultipleValueLookupClassLabel(getDataDictionaryService().getDataDictionary().getBusinessObjectEntry(definition.getSourceClassName().getName()).getObjectLabel());
    }

    /**
     * Returns whether the passed in collection has been properly configured in the maint doc dictionary to support multiple value
     * lookups.
     *
     * @param definition
     * @return
     */
    static final public boolean isCollectionMultipleLookupEnabled(MaintainableCollectionDefinition definition) {
        return definition.getSourceClassName() != null && definition.isIncludeMultipleLookupLine();
    }

    /**
     * This method removes any duplicating spacing (internal or on the ends) from a String, meant to be exposed as a tag library
     * function.
     *
     * @param s String to remove duplicate spacing from.
     * @return String without duplicate spacing.
     */
    public static String scrubWhitespace(String s) {
        return s.replaceAll("(\\s)(\\s+)", " ");
    }

    private static DataDictionaryService getDataDictionaryService() {
    	if (dataDictionaryService == null) {
    		dataDictionaryService = KNSServiceLocator.getDataDictionaryService();
    	}
    	return dataDictionaryService;
    }

    private static BusinessObjectMetaDataService getBusinessObjectMetaDataService() {
    	if (businessObjectMetaDataService == null) {
    		businessObjectMetaDataService = KNSServiceLocator.getBusinessObjectMetaDataService();
    	}
    	return businessObjectMetaDataService;
    }

    private static BusinessObjectDictionaryService getBusinessObjectDictionaryService() {
    	if (businessObjectDictionaryService == null) {
    		businessObjectDictionaryService = KNSServiceLocator.getBusinessObjectDictionaryService();
    	}
    	return businessObjectDictionaryService;
    }

    private static KualiModuleService getKualiModuleService() {
    	if (kualiModuleService == null) {
    		kualiModuleService = KNSServiceLocator.getKualiModuleService();
    	}
    	return kualiModuleService;
    }
}