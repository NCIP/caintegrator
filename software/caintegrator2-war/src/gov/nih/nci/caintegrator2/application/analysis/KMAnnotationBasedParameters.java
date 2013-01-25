/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.analysis;

import gov.nih.nci.caintegrator2.domain.annotation.PermissibleValue;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;

import java.util.Collection;
import java.util.HashSet;

/**
 * Parameters used for creating an Annotation Based KaplanMeier plot. 
 */
public class KMAnnotationBasedParameters extends AbstractKMParameters {

    private AnnotationDefinition selectedAnnotation = new AnnotationDefinition();
    private final Collection <PermissibleValue> selectedValues = new HashSet<PermissibleValue>();    
    private EntityTypeEnum entityType;
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validate() {
        getErrorMessages().clear();
        boolean isValid = true;
        if (getSelectedAnnotation().getId() == null) {
            getErrorMessages().add("Selected Annotation is null, please select a valid Selected Annotation.");
            isValid = false;
        }
        if (getSelectedValues().size() < 2) {
            getErrorMessages().add("Must select at least 2 grouping values");
            isValid = false;
        }
        isValid = validateSurvivalValueDefinition(isValid);
        return isValid;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        setSelectedAnnotation(new AnnotationDefinition());
        getSelectedValues().clear();
    }
    
    /**
     * @return the selectedAnnotation
     */
    public AnnotationDefinition getSelectedAnnotation() {
        return selectedAnnotation;
    }

    /**
     * @param selectedAnnotation the selectedAnnotation to set
     */
    public void setSelectedAnnotation(AnnotationDefinition selectedAnnotation) {
        this.selectedAnnotation = selectedAnnotation;
    }

    /**
     * @return the selectedValues
     */
    public Collection<PermissibleValue> getSelectedValues() {
        return selectedValues;
    }

    /**
     * @return the entityType
     */
    public EntityTypeEnum getEntityType() {
        return entityType;
    }

    /**
     * @param entityType the entityType to set
     */
    public void setEntityType(EntityTypeEnum entityType) {
        this.entityType = entityType;
    }

}
