/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.application.study;

import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.annotation.PermissibleValue;
import gov.nih.nci.caintegrator2.domain.annotation.mask.AbstractAnnotationMask;
import gov.nih.nci.caintegrator2.domain.annotation.mask.MaxNumberMask;
import gov.nih.nci.caintegrator2.domain.annotation.mask.NumericRangeMask;
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Contains the information about a particular annotation field prior to association to an
 * <code>AnnotationDefinition</code>.
 */
public class AnnotationFieldDescriptor extends AbstractCaIntegrator2Object implements
        Comparable<AnnotationFieldDescriptor> {

    private static final long serialVersionUID = 1L;
    private String name;
    private AnnotationFieldType type = AnnotationFieldType.ANNOTATION;
    private AnnotationDefinition definition;
    private boolean shownInBrowse = true;
    private boolean showInAuthorization = false;
    private Boolean hasValidationErrors = false;
    private Boolean usePermissibleValues = false;
    private AnnotationGroup annotationGroup;
    private String validationErrorMessage;
    private EntityTypeEnum annotationEntityType;
    private Set<AbstractAnnotationMask> annotationMasks = new HashSet<AbstractAnnotationMask>();

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the type
     */
    public AnnotationFieldType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(AnnotationFieldType type) {
        this.type = type;
    }

    /**
     * @return the definition
     */
    public AnnotationDefinition getDefinition() {
        return definition;
    }

    /**
     * @param definition the definition to set
     */
    public void setDefinition(AnnotationDefinition definition) {
        this.definition = definition;
    }

    /**
     * @return the shownInBrowse
     */
    public boolean isShownInBrowse() {
        return shownInBrowse;
    }

    /**
     * @param shownInBrowse the shownInBrowse to set
     */
    public void setShownInBrowse(boolean shownInBrowse) {
        this.shownInBrowse = shownInBrowse;
    }

    /**
     * @return the showInAuthorization
     */
    public boolean isShowInAuthorization() {
        return showInAuthorization;
    }

    /**
     * @param showInAuthorization the showInAuthorization to set
     */
    public void setShowInAuthorization(boolean showInAuthorization) {
        this.showInAuthorization = showInAuthorization;
    }

    /**
     * @return the hasValidationErrors
     */
    public Boolean isHasValidationErrors() {
        return hasValidationErrors;
    }

    /**
     * @param hasValidationErrors the hasValidationErrors to set
     */
    public void setHasValidationErrors(Boolean hasValidationErrors) {
        this.hasValidationErrors = hasValidationErrors;
    }

    /**
     * @return the annotationGroup
     */
    public AnnotationGroup getAnnotationGroup() {
        return annotationGroup;
    }

    /**
     * @param annotationGroup the annotationGroup to set
     */
    public void setAnnotationGroup(AnnotationGroup annotationGroup) {
        this.annotationGroup = annotationGroup;
    }

    /**
     * @return the usePermissibleValues
     */
    public Boolean isUsePermissibleValues() {
        return usePermissibleValues;
    }

    /**
     * @param usePermissibleValues the usePermissibleValues to set
     */
    public void setUsePermissibleValues(Boolean usePermissibleValues) {
        this.usePermissibleValues = usePermissibleValues;
    }

    /**
     *
     * @param newAnnotationGroup annotation group to switch to.
     */
    public void switchAnnotationGroup(AnnotationGroup newAnnotationGroup) {
        removeFromAnnotationGroup();
        if (newAnnotationGroup != null) {
            newAnnotationGroup.getAnnotationFieldDescriptors().add(this);
        }
        annotationGroup = newAnnotationGroup;
    }
    /**
     * Remove from existing annotation group.
     */
    public void removeFromAnnotationGroup() {
        if (this.annotationGroup != null) {
            annotationGroup.getAnnotationFieldDescriptors().remove(this);
        }
    }

    /**
     * @return the validationErrorMessage
     */
    public String getValidationErrorMessage() {
        return validationErrorMessage;
    }

    /**
     * @param validationErrorMessage the validationErrorMessage to set
     */
    public void setValidationErrorMessage(String validationErrorMessage) {
        this.validationErrorMessage = validationErrorMessage;
    }

    /**
     * @return the annotationEntityType
     */
    public EntityTypeEnum getAnnotationEntityType() {
        return annotationEntityType;
    }

    /**
     * @param annotationEntityType the annotationEntityType to set
     */
    public void setAnnotationEntityType(EntityTypeEnum annotationEntityType) {
        this.annotationEntityType = annotationEntityType;
    }

    /**
     * @return permissible values order by value
     */
    public List<PermissibleValue> getPermissibleValues() {
        List<PermissibleValue> results = new ArrayList<PermissibleValue>();
        if (definition != null) {
            results.addAll(definition.getPermissibleValueCollection());
            Collections.sort(results);
        }
        return results;
    }

    /**
     * @return the display name from Annotation Definition
     */
    public String getDisplayName() {
        return (definition == null || definition.getDisplayName() == null)
            ? "--Undefine--" : definition.getDisplayName();
    }

    /**
     * @return the annotationMasks
     */
    public Set<AbstractAnnotationMask> getAnnotationMasks() {
        return annotationMasks;
    }

    /**
     * @param annotationMasks the annotationMasks to set
     */
    @SuppressWarnings("unused") // For Hibernate.
    private void setAnnotationMasks(Set<AbstractAnnotationMask> annotationMasks) {
        this.annotationMasks = annotationMasks;
    }

    /**
     * Compare based on the Display Name.
     * {@inheritDoc}
     */
    @Override
    public int compareTo(AnnotationFieldDescriptor o) {
        return getDisplayName().compareTo(o.getDisplayName());
    }

    /**
     * Sets a max number mask equal to the given number.
     * @param maxNumber to set the maxNumber mask for.
     */
    public void setMaxNumber(Double maxNumber) {
        MaxNumberMask maxNumberMask = new MaxNumberMask();
        for (AbstractAnnotationMask mask : annotationMasks) {
            if (mask instanceof MaxNumberMask) {
                maxNumberMask = (MaxNumberMask) mask;
                break;
            }
        }
        maxNumberMask.setMaxNumber(maxNumber);
        annotationMasks.add(maxNumberMask);
    }

    /**
     * Removes the mask of the given type.
     * @param maskType to remove from this AFD.
     */
    public void clearMask(Class<? extends AbstractAnnotationMask> maskType) {
        AbstractAnnotationMask maskToRemove = null;
        for (AbstractAnnotationMask mask : annotationMasks) {
            if (maskType.equals(mask.getClass())) {
                maskToRemove = mask;
                break;
            }
        }
        if (maskToRemove != null) {
            annotationMasks.remove(maskToRemove);
        }
    }

    /**
     * Sets a numeric range mask equal to the given number.
     * @param numericRange to set the numericRange mask for.
     */
    public void setNumericRange(int numericRange) {
        NumericRangeMask numericRangeMask = new NumericRangeMask();
        for (AbstractAnnotationMask mask : annotationMasks) {
            if (mask instanceof NumericRangeMask) {
                numericRangeMask = (NumericRangeMask) mask;
                break;
            }
        }
        numericRangeMask.setNumericRange(numericRange);
        annotationMasks.add(numericRangeMask);
    }

    /**
     *
     * @return the displayable restrictions (via the masks applied).
     */
    public Set<String> getDisplayableRestrictions() {
        Set<String> restrictions = new HashSet<String>();
        for (AbstractAnnotationMask mask : getAnnotationMasks()) {
            restrictions.add(mask.getDisplayableRestriction());
        }
        if (restrictions.isEmpty()) {
            restrictions.add("None");
        }
        return restrictions;
    }
}
