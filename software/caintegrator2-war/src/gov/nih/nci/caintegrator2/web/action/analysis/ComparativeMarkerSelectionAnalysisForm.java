/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.analysis;

import gov.nih.nci.caintegrator2.application.analysis.grid.comparativemarker.ComparativeMarkerSelectionParameters;
import gov.nih.nci.caintegrator2.application.analysis.grid.preprocess.PreprocessDatasetParameters;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.external.ServerConnectionProfile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Used for Struts representation of the currently configured analysis method.
 */
public class ComparativeMarkerSelectionAnalysisForm {
    
    private PreprocessDatasetParameters preprocessDatasetparameters;
    private ComparativeMarkerSelectionParameters comparativeMarkerSelectionParameters;
    
    private final ServerConnectionProfile server = new ServerConnectionProfile();

    private List<String> selectedQueryIDs = new ArrayList<String>();
    private List<String> unselectedQueryIDs = new ArrayList<String>();
    
    // JSP Select List Options
    private Map<String, Query> selectedQueries = new HashMap<String, Query>();
    private Map<String, Query> unselectedQueries = new HashMap<String, Query>();

    /**
     * 
     * @return the server.
     */
    public ServerConnectionProfile getServer() {
        return server;
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
    
    
}
