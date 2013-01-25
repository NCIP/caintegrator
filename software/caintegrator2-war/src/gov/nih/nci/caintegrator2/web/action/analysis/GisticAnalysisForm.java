/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.analysis;

import gov.nih.nci.caintegrator2.application.analysis.grid.gistic.GisticParameters;

import java.util.HashMap;
import java.util.Map;

/**
 * Used for Struts representation of the currently configured analysis method.
 */
public class GisticAnalysisForm {
    
    private GisticParameters gisticParameters;
    
    private String selectedQuery = null;
    private String excludeControlSampleSetName;
    
    // JSP Select List Options
    private Map<String, String> clinicalQueries = new HashMap<String, String>();


    /**
     * @return the gisticParameters
     */
    public GisticParameters getGisticParameters() {
        return gisticParameters;
    }

    /**
     * @param gisticParameters the gisticParameters to set
     */
    public void setGisticParameters(GisticParameters gisticParameters) {
        this.gisticParameters = gisticParameters;
    }

    /**
     * @return the selectedQueryIDs
     */
    public String getSelectedQuery() {
        return selectedQuery;
    }

    /**
     * @param selectedQuery the selectedQueryIDs to set
     */
    public void setSelectedQuery(String selectedQuery) {
        this.selectedQuery = selectedQuery;
    }

    /**
     * @return the unselectedQueries
     */
    public Map<String, String> getClinicalQueries() {
        return clinicalQueries;
    }

    /**
     * @param clinicalQueries the clinicalQueries to set
     */
    public void setClinicalQueries(Map<String, String> clinicalQueries) {
        this.clinicalQueries = clinicalQueries;
    }

    /**
     * @return the excludeControlSampleSetName
     */
    public String getExcludeControlSampleSetName() {
        return excludeControlSampleSetName;
    }

    /**
     * @param excludeControlSampleSetName the excludeControlSampleSetName to set
     */
    public void setExcludeControlSampleSetName(String excludeControlSampleSetName) {
        this.excludeControlSampleSetName = excludeControlSampleSetName;
    }

    /**
     * @return true if a grid service invocation, false if web service invocation.
     */
    public boolean isGridServiceCall() {
        return getGisticParameters().getServer().getUrl().endsWith("/Gistic");
    }
    
}
