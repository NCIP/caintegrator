/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.web.action.analysis;

import gov.nih.nci.caintegrator2.application.analysis.grid.comparativemarker.ComparativeMarkerSelectionParameters;
import gov.nih.nci.caintegrator2.application.analysis.grid.preprocess.PreprocessDatasetParameters;
import gov.nih.nci.caintegrator2.external.ServerConnectionProfile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Used for Struts representation of the currently configured analysis method.
 */
public class ComparativeMarkerSelectionAnalysisForm {
    
    private PreprocessDatasetParameters preprocessDatasetparameters;
    private ComparativeMarkerSelectionParameters comparativeMarkerSelectionParameters;
    
    private final ServerConnectionProfile server = new ServerConnectionProfile();
    private List<DisplayableQuery> displayableQueries = new ArrayList<DisplayableQuery>();
    private Map<String, DisplayableQuery> displayableQueryMap = new HashMap<String, DisplayableQuery>();
    private List<String> selectedQueryNames = new ArrayList<String>();
    private List<String> unselectedQueryNames = new ArrayList<String>();
    private String platformName = null;

    // JSP Select List Options
    private final Map<String, DisplayableQuery> selectedQueries = new HashMap<String, DisplayableQuery>();
    private final SortedMap<String, DisplayableQuery> unselectedQueries = new TreeMap<String, DisplayableQuery>();

    /**
     * 
     * @return the server.
     */
    public ServerConnectionProfile getServer() {
        return server;
    }

    /**
     * @return the displayableQueries
     */
    public List<DisplayableQuery> getDisplayableQueries() {
        return displayableQueries;
    }

    /**
     * @param displayableQueries the displayableQueries to set
     */
    public void setDisplayableQueries(List<DisplayableQuery> displayableQueries) {
        this.displayableQueries = displayableQueries;
    }

    /**
     * @return the displayableQueryMap
     */
    public Map<String, DisplayableQuery> getDisplayableQueryMap() {
        return displayableQueryMap;
    }

    /**
     * @param displayableQueryMap the displayableQueryMap to set
     */
    public void setDisplayableQueryMap(Map<String, DisplayableQuery> displayableQueryMap) {
        this.displayableQueryMap = displayableQueryMap;
    }

    /**
     * @return the preprocessDatasetparameters
     */
    public PreprocessDatasetParameters getPreprocessDatasetparameters() {
        return preprocessDatasetparameters;
    }

    /**
     * @param preprocessDatasetparameters the preprocessDatasetparameters to set
     */
    public void setPreprocessDatasetparameters(PreprocessDatasetParameters preprocessDatasetparameters) {
        this.preprocessDatasetparameters = preprocessDatasetparameters;
    }

    /**
     * @return the comparativeMarkerSelectionParameters
     */
    public ComparativeMarkerSelectionParameters getComparativeMarkerSelectionParameters() {
        return comparativeMarkerSelectionParameters;
    }

    /**
     * @param comparativeMarkerSelectionParameters the comparativeMarkerSelectionParameters to set
     */
    public void setComparativeMarkerSelectionParameters(
            ComparativeMarkerSelectionParameters comparativeMarkerSelectionParameters) {
        this.comparativeMarkerSelectionParameters = comparativeMarkerSelectionParameters;
    }

    /**
     * @return the selectedQueryNames
     */
    public List<String> getSelectedQueryNames() {
        return selectedQueryNames;
    }

    /**
     * @param selectedQueryNames the selectedQueryNames to set
     */
    public void setSelectedQueryNames(List<String> selectedQueryNames) {
        this.selectedQueryNames = selectedQueryNames;
    }

    /**
     * @return the unselectedQueryNames
     */
    public List<String> getUnselectedQueryNames() {
        return unselectedQueryNames;
    }

    /**
     * @param unselectedQueryNames the unselectedQueryNames to set
     */
    public void setUnselectedQueryNames(List<String> unselectedQueryNames) {
        this.unselectedQueryNames = unselectedQueryNames;
    }

    /**
     * @return the selectedQueries
     */
    public Map<String, DisplayableQuery> getSelectedQueries() {
        return selectedQueries;
    }

    /**
     * @return the unselectedQueries
     */
    public SortedMap<String, DisplayableQuery> getUnselectedQueries() {
        return unselectedQueries;
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
}
