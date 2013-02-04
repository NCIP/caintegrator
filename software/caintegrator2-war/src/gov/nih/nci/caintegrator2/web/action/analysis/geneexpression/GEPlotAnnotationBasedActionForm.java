/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.analysis.geneexpression;

import gov.nih.nci.caintegrator2.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Form used to store input values for Annotation Based GE Plots. 
 */
public class GEPlotAnnotationBasedActionForm {
    
    private String annotationGroupSelection;
    private String selectedAnnotationId;
    private Collection <String> selectedValuesIds = new HashSet<String>();
    private boolean permissibleValuesNeedUpdate = false;
    private boolean addPatientsNotInQueriesGroup = false;
    private boolean addControlSamplesGroup = false;
    private String controlSampleSetName;
    private String platformName;
    private String geneSymbol;
    private String reporterType = ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET.getValue();
    private List<String> controlSampleSets = new ArrayList<String>();
    
    
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
        addPatientsNotInQueriesGroup = false;
        addControlSamplesGroup = false;
        geneSymbol = null;
        reporterType = ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET.getValue();
        platformName = null;
        controlSampleSets = new ArrayList<String>();
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
     * @return the platformName
     */
    public String getPlatformName() {
        return platformName;
    }

    /**
     * @param platformName the platformName to set
     */
    public void setPlatformName(String platformName) {
        this.platformName = platformName;
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

    /**
     * @return the controlSampleSets
     */
    public List<String> getControlSampleSets() {
        return controlSampleSets;
    }

    /**
     * @param controlSampleSets the controlSampleSets to set
     */
    public void setControlSampleSets(List<String> controlSampleSets) {
        this.controlSampleSets = controlSampleSets;
    }

}
