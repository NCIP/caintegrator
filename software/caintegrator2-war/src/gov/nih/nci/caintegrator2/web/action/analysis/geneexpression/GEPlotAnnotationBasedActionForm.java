/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.analysis.geneexpression;

import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Form used to store input values for Annotation Based GE Plots. 
 */
public class GEPlotAnnotationBasedActionForm {
    
    private String annotationTypeSelection;
    private String selectedAnnotationId;
    private Collection <String> selectedValuesIds = new HashSet<String>();
    private boolean permissibleValuesNeedUpdate = false;
    private boolean addPatientsNotInQueriesGroup = false;
    private boolean addControlSamplesGroup = false;
    private String controlSampleSetName;
    private String geneSymbol;
    private String reporterType = ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET.getValue();
    
    
    // JSP Select List Options
    private Map<String, AnnotationDefinition> annotationDefinitions = new HashMap<String, AnnotationDefinition>();
    private Map<String, String> permissibleValues = new HashMap<String, String>();
    


    /**
     * Clears all the variables to null.
     */
    public void clear() {
        annotationTypeSelection = null;
        selectedAnnotationId = null;
        selectedValuesIds = new HashSet<String>();
        addPatientsNotInQueriesGroup = false;
        addControlSamplesGroup = false;
        geneSymbol = null;
        reporterType = ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET.getValue();
        clearAnnotationDefinitions();
    }
    
    /**
     * Clears the annotation definitions.
     */
    public void clearAnnotationDefinitions() {
        annotationDefinitions = new HashMap<String, AnnotationDefinition>();
        setSelectedAnnotationId(null);
        clearPermissibleValues();
    }
    
    /**
     * Clears the permissible values.
     */
    public void clearPermissibleValues() {
        permissibleValues = new HashMap<String, String>();
        getSelectedValuesIds().clear();
    }
    
    /**
     * @return the annotationTypeSelection
     */
    public String getAnnotationTypeSelection() {
        return annotationTypeSelection;
    }

    /**
     * @param annotationTypeSelection the annotationTypeSelection to set
     */
    public void setAnnotationTypeSelection(String annotationTypeSelection) {
        this.annotationTypeSelection = annotationTypeSelection;
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
     * @return the annotationDefinitions
     */
    public Map<String, AnnotationDefinition> getAnnotationDefinitions() {
        return annotationDefinitions;
    }

    /**
     * @param annotationDefinitions the annotationDefinitions to set
     */
    public void setAnnotationDefinitions(Map<String, AnnotationDefinition> annotationDefinitions) {
        this.annotationDefinitions = annotationDefinitions;
    }

    /**
     * @return the permissibleValues
     */
    public Map<String, String> getPermissibleValues() {
        return permissibleValues;
    }

    /**
     * @param permissibleValues the permissibleValues to set
     */
    public void setPermissibleValues(Map<String, String> permissibleValues) {
        this.permissibleValues = permissibleValues;
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

    /**
     * @return the addPatientsNotInQueriesGroup
     */
    public boolean isAddPatientsNotInQueriesGroup() {
        return addPatientsNotInQueriesGroup;
    }

    /**
     * @param addPatientsNotInQueriesGroup the addPatientsNotInQueriesGroup to set
     */
    public void setAddPatientsNotInQueriesGroup(boolean addPatientsNotInQueriesGroup) {
        this.addPatientsNotInQueriesGroup = addPatientsNotInQueriesGroup;
    }

    /**
     * @return the geneSymbol
     */
    public String getGeneSymbol() {
        return geneSymbol;
    }

    /**
     * @param geneSymbol the geneSymbol to set
     */
    public void setGeneSymbol(String geneSymbol) {
        this.geneSymbol = geneSymbol;
    }

    /**
     * @return the reporterType
     */
    public String getReporterType() {
        return reporterType;
    }

    /**
     * @param reporterType the reporterType to set
     */
    public void setReporterType(String reporterType) {
        this.reporterType = reporterType;
    }

    /**
     * @return the addControlSamplesGroup
     */
    public boolean isAddControlSamplesGroup() {
        return addControlSamplesGroup;
    }

    /**
     * @param addControlSamplesGroup the addControlSamplesGroup to set
     */
    public void setAddControlSamplesGroup(boolean addControlSamplesGroup) {
        this.addControlSamplesGroup = addControlSamplesGroup;
    }

    /**
     * @return the controlSampleSetName
     */
    public String getControlSampleSetName() {
        return controlSampleSetName;
    }

    /**
     * @param controlSampleSetName the controlSampleSetName to set
     */
    public void setControlSampleSetName(String controlSampleSetName) {
        this.controlSampleSetName = controlSampleSetName;
    }

}
