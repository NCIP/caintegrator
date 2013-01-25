/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.domain.annotation;

import gov.nih.nci.caintegrator2.application.study.AnnotationTypeEnum;
import gov.nih.nci.caintegrator2.application.study.ValidationException;
import gov.nih.nci.caintegrator2.common.DateUtil;
import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 
 */
public class AnnotationDefinition extends AbstractCaIntegrator2Object {

    private static final long serialVersionUID = 1L;
    
    private String keywords;
    
    private CommonDataElement commonDataElement = new CommonDataElement();
    private Set<AbstractAnnotationValue> annotationValueCollection = new HashSet<AbstractAnnotationValue>();

    /**
     * Set default data type as string and commonDataElement.longName the same as the keyword.
     * @param name the keywords to set
     */
    public void setDefault(String name) {
        this.keywords = name;
        commonDataElement.setLongName(name);
        commonDataElement.getValueDomain().setDataTypeString("string");
    }

    /**
     * @return the keywords
     */
    public String getKeywords() {
        return keywords;
    }
    
    /**
     * @param keywords the keywords to set
     */
    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }
    
    /**
     * @return the commonDataElement
     */
    public CommonDataElement getCommonDataElement() {
        return commonDataElement;
    }
    
    /**
     * @param commonDataElement the commonDataElement to set
     */
    public void setCommonDataElement(CommonDataElement commonDataElement) {
        this.commonDataElement = commonDataElement;
    }
    
    /**
     * @return the annotationValueCollection
     */
    public Set<AbstractAnnotationValue> getAnnotationValueCollection() {
        return annotationValueCollection;
    }
    
    /**
     * @param annotationValueCollection the annotationValueCollection to set
     */
    @SuppressWarnings("unused")     // required by Hibernate
    private void setAnnotationValueCollection(Set<AbstractAnnotationValue> annotationValueCollection) {
        this.annotationValueCollection = annotationValueCollection;
    }
    
    /**
     * Validates that all the values associated with definition match the type.
     * @throws ValidationException if invalid values for type.
     */
    public void validateValuesWithType() throws ValidationException {
        for (AbstractAnnotationValue value : annotationValueCollection) {
            if (!getDataType().equals(value.getValidAnnotationType())) {
                throw new ValidationException(retrieveValidationError());
            }
        }
    }

    /**
     * The datatype from the valuedomain.
     * @return data type.
     */
    public AnnotationTypeEnum getDataType() {
        return commonDataElement.getValueDomain().getDataType();
    }
    
    /**
     * Sets the data type of the valueDomain.
     * @param dataType to set.
     */
    public void setDataType(AnnotationTypeEnum dataType) {
        commonDataElement.getValueDomain().setDataType(dataType);
    }
    
    /**
     * Gets the permissible values from the value domain.
     * @return permissible values.
     */
    public Set<PermissibleValue> getPermissibleValueCollection() {
        return commonDataElement.getValueDomain().getPermissibleValueCollection();
    }
    
    /**
     * The display name.
     * @return display name.
     */
    public String getDisplayName() {
        return commonDataElement.getLongName();
    }
    
    /**
     * @param displayName to set the name to.
     */
    public void setDisplayName(String displayName) {
        commonDataElement.setLongName(displayName);
    }
    
    private String retrieveValidationError() {
        return "Values for '" + commonDataElement.getLongName() + "' must be of the " + getDataType() + " type.";
    }

    /**
     * Add permissible values.
     * @param uniqueValues the values to use
     */
    public void addPermissibleValues(Set<Object> uniqueValues) {
        for (Object uniqueValue : uniqueValues) {
            if (uniqueValue == null) {
                continue;
            }
            PermissibleValue permissibleValue = new PermissibleValue();
            if (AnnotationTypeEnum.DATE.equals(commonDataElement.getValueDomain().getDataType())) {
                permissibleValue.setValue(DateUtil.toString((Date) uniqueValue));
            } else if (AnnotationTypeEnum.NUMERIC.equals(commonDataElement.getValueDomain().getDataType())) {
                permissibleValue.setValue(((Double) uniqueValue).toString());
            } else {
                permissibleValue.setValue(uniqueValue.toString());
            }
            commonDataElement.getValueDomain().getPermissibleValueCollection().add(permissibleValue);
        }
    }
}
