/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.web.action.analysis;

import gov.nih.nci.caintegrator2.application.analysis.grid.gistic.GisticParameters;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Used for Struts representation of the currently configured analysis method.
 */
public class GisticAnalysisForm {
    
    private GisticParameters gisticParameters;
    
    private String selectedQuery = null;
    private String selectedPlatformName;
    private String excludeControlSampleSetName;
    
    // JSP Select List Options
    private final SortedMap<String, DisplayableQuery> clinicalQueries = new TreeMap<String, DisplayableQuery>();


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
    public SortedMap<String, DisplayableQuery> getClinicalQueries() {
        return clinicalQueries;
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

    /**
     * @return the selectedPlatformName
     */
    public String getSelectedPlatformName() {
        return selectedPlatformName;
    }

    /**
     * @param selectedPlatformName the selectedPlatformName to set
     */
    public void setSelectedPlatformName(String selectedPlatformName) {
        this.selectedPlatformName = selectedPlatformName;
    }
    
}
