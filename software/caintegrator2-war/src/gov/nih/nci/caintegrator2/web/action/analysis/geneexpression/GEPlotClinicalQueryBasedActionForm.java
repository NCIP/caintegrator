/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.analysis.geneexpression;

import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Form used to store input values for Clinical Query Based GE Plots. 
 */
public class GEPlotClinicalQueryBasedActionForm {
    
    private String geneSymbol;
    private String reporterType = ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET.getValue();
    private List<String> selectedQueryIDs = new ArrayList<String>();
    private List<String> unselectedQueryIDs = new ArrayList<String>();
    private boolean exclusiveGroups = false;
    private boolean addPatientsNotInQueriesGroup = false;
    private boolean addControlSamplesGroup = false;
    private String controlSampleSetName;
    private boolean resetSelected = false;
    
    // JSP Select List Options
    private Map<String, Query> selectedQueries = new HashMap<String, Query>();
    private Map<String, Query> unselectedQueries = new HashMap<String, Query>();
    


    /**
     * Clears all the variables to null.
     */
    public void clear() {
        geneSymbol = null;
        reporterType = ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET.getValue();
        selectedQueryIDs = new ArrayList<String>();
        unselectedQueryIDs = new ArrayList<String>();
        exclusiveGroups = false;
        addPatientsNotInQueriesGroup = false;
        addControlSamplesGroup = false;
        selectedQueries.clear();
        unselectedQueries.clear();
    }



    /**
     * @return the selectedQueryIDs
     */
    public List<String> getSelectedQueryIDs() {
        return selectedQueryIDs;
    }



    /**
     * @param selectedQueryIDs the selectedQueryIDs to set
     */
    public void setSelectedQueryIDs(List<String> selectedQueryIDs) {
        this.selectedQueryIDs = selectedQueryIDs;
    }



    /**
     * @return the unselectedQueryIDs
     */
    public List<String> getUnselectedQueryIDs() {
        return unselectedQueryIDs;
    }



    /**
     * @param unselectedQueryIDs the unselectedQueryIDs to set
     */
    public void setUnselectedQueryIDs(List<String> unselectedQueryIDs) {
        this.unselectedQueryIDs = unselectedQueryIDs;
    }



    /**
     * @return the exclusiveGroups
     */
    public boolean isExclusiveGroups() {
        return exclusiveGroups;
    }



    /**
     * @param exclusiveGroups the exclusiveGroups to set
     */
    public void setExclusiveGroups(boolean exclusiveGroups) {
        this.exclusiveGroups = exclusiveGroups;
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
     * @return the selectedQueries
     */
    public Map<String, Query> getSelectedQueries() {
        return selectedQueries;
    }



    /**
     * @param selectedQueries the selectedQueries to set
     */
    public void setSelectedQueries(Map<String, Query> selectedQueries) {
        this.selectedQueries = selectedQueries;
    }



    /**
     * @return the unselectedQueries
     */
    public Map<String, Query> getUnselectedQueries() {
        return unselectedQueries;
    }



    /**
     * @param unselectedQueries the unselectedQueries to set
     */
    public void setUnselectedQueries(Map<String, Query> unselectedQueries) {
        this.unselectedQueries = unselectedQueries;
    }



    /**
     * @return the resetSelected
     */
    public boolean isResetSelected() {
        return resetSelected;
    }



    /**
     * @param resetSelected the resetSelected to set
     */
    public void setResetSelected(boolean resetSelected) {
        this.resetSelected = resetSelected;
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
