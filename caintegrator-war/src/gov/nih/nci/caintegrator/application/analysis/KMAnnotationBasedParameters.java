/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.analysis;

import gov.nih.nci.caintegrator.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator.domain.annotation.PermissibleValue;

import java.util.Collection;
import java.util.HashSet;

/**
 * Parameters used for creating an Annotation Based KaplanMeier plot. 
 */
public class KMAnnotationBasedParameters extends AbstractKMParameters {

    private AnnotationFieldDescriptor selectedAnnotation = new AnnotationFieldDescriptor();
    private final Collection <PermissibleValue> selectedValues = new HashSet<PermissibleValue>();    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validate() {
        getErrorMessages().clear();
        boolean isValid = true;
        if (getSelectedAnnotation() == null 
             || getSelectedAnnotation().getId() == null) {
            getErrorMessages().add("Selected Annotation is null, please select a valid annotation.");
            isValid = false;
        }
        if (getSelectedValues().size() < 2) {
            getErrorMessages().add("Must select at least 2 Patient Groups");
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
        setSelectedAnnotation(new AnnotationFieldDescriptor());
        getSelectedValues().clear();
    }
    
    /**
     * @return the selectedAnnotation
     */
    public AnnotationFieldDescriptor getSelectedAnnotation() {
        return selectedAnnotation;
    }

    /**
     * @param selectedAnnotation the selectedAnnotation to set
     */
    public void setSelectedAnnotation(AnnotationFieldDescriptor selectedAnnotation) {
        this.selectedAnnotation = selectedAnnotation;
    }

    /**
     * @return the selectedValues
     */
    public Collection<PermissibleValue> getSelectedValues() {
        return selectedValues;
    }

}
