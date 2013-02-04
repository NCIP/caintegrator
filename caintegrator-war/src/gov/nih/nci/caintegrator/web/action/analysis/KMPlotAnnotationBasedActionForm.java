/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.action.analysis;

import gov.nih.nci.caintegrator.application.study.AnnotationFieldDescriptor;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Form used to store input values for Annotation Based KM Plots. 
 */
public class KMPlotAnnotationBasedActionForm {
    
    private String annotationGroupSelection;
    private String selectedAnnotationId;
    private Collection <String> selectedValuesIds = new HashSet<String>();
    private boolean permissibleValuesNeedUpdate = false;
    
    
    // JSP Select List Options
    private Map<String, AnnotationFieldDescriptor> annotationFieldDescriptors = 
        new HashMap<String, AnnotationFieldDescriptor>();
    private Map<String, String> permissibleValues = new LinkedHashMap<String, String>();
    


    /**
     * Clears all the variables to null.
     */
    public void clear() {
        annotationGroupSelection = null;
        selectedAnnotationId = null;
        selectedValuesIds = new HashSet<String>();
        clearAnnotationDefinitions();
    }
    
    /**
     * Clears the annotation definitions.
     */
    public void clearAnnotationDefinitions() {
        annotationFieldDescriptors = new HashMap<String, AnnotationFieldDescriptor>();
        setSelectedAnnotationId(null);
        clearPermissibleValues();
    }
    
    /**
     * Clears the permissible values.
     */
    public void clearPermissibleValues() {
        permissibleValues = new LinkedHashMap<String, String>();
        getSelectedValuesIds().clear();
    }
    
    /**
     * @return the annotationTypeSelection
     */
    public String getAnnotationGroupSelection() {
        return annotationGroupSelection;
    }

    /**
     * @param annotationGroupSelection the annotationGroupSelection to set
     */
    public void setAnnotationGroupSelection(String annotationGroupSelection) {
        this.annotationGroupSelection = annotationGroupSelection;
    }

    /**
     * @return the selectedAnnotationId
     */
    public String getSelectedAnnotationId() {
        return selectedAnnotationId;
    }

    /**
     * @param selectedAnnotationId the selectedAnnotationId to set
     */
    public void setSelectedAnnotationId(String selectedAnnotationId) {
        this.selectedAnnotationId = selectedAnnotationId;
    }

    /**
     * @return the selectedValuesIds
     */
    public Collection<String> getSelectedValuesIds() {
        return selectedValuesIds;
    }


    /**
     * @param selectedValuesIds the selectedValuesIds to set
     */
    public void setSelectedValuesIds(Collection<String> selectedValuesIds) {
        this.selectedValuesIds = selectedValuesIds;
    }

    /**
     * @return the annotationFieldDescriptors
     */
    public Map<String, AnnotationFieldDescriptor> getAnnotationFieldDescriptors() {
        return annotationFieldDescriptors;
    }

    /**
     * @param annotationFieldDescriptors the annotationFieldDescriptors to set
     */
    public void setAnnotationFieldDescriptors(Map<String, AnnotationFieldDescriptor> annotationFieldDescriptors) {
        this.annotationFieldDescriptors = annotationFieldDescriptors;
    }

    /**
     * @return the permissibleValues
     */
    public Map<String, String> getPermissibleValues() {
        return permissibleValues;
    }

    /**
     * @return the permissibleValuesNeedUpdate
     */
    public boolean isPermissibleValuesNeedUpdate() {
        return permissibleValuesNeedUpdate;
    }

    /**
     * @param permissibleValuesNeedUpdate the permissibleValuesNeedUpdate to set
     */
    public void setPermissibleValuesNeedUpdate(boolean permissibleValuesNeedUpdate) {
        this.permissibleValuesNeedUpdate = permissibleValuesNeedUpdate;
    }

}
