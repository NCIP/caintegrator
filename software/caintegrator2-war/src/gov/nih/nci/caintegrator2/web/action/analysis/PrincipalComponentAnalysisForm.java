/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.analysis;

import gov.nih.nci.caintegrator2.application.analysis.grid.pca.PCAParameters;
import gov.nih.nci.caintegrator2.application.analysis.grid.preprocess.PreprocessDatasetParameters;
import gov.nih.nci.caintegrator2.external.ServerConnectionProfile;

import java.util.HashMap;
import java.util.Map;

/**
 * Used for Struts representation of the currently configured analysis method.
 */
public class PrincipalComponentAnalysisForm {
    
    private PCAParameters pcaParameters;
    private PreprocessDatasetParameters preprocessParameters;
    
    private final ServerConnectionProfile server = new ServerConnectionProfile();
    private boolean usePreprocessDataset = false;

    private String selectedQueryID;
    private String excludeControlSampleSetName;
    
    // JSP Select List Options
    private Map<String, String> queries = new HashMap<String, String>();

    /**
     * 
     * @return the server.
     */
    public ServerConnectionProfile getServer() {
        return server;
    }

    /**
     * @return the selectedQueryIDs
     */
    public String getSelectedQueryID() {
        return selectedQueryID;
    }

    /**
     * @param selectedQueryID the selectedQueryID to set
     */
    public void setSelectedQueryID(String selectedQueryID) {
        this.selectedQueryID = selectedQueryID;
    }

    /**
     * @return the queries
     */
    public Map<String, String> getQueries() {
        return queries;
    }

    /**
     * @param queries the queries to set
     */
    public void setQueries(Map<String, String> queries) {
        this.queries = queries;
    }

    /**
     * @return the pcaParameters
     */
    public PCAParameters getPcaParameters() {
        return pcaParameters;
    }

    /**
     * @param pcaParameters the pcaParameters to set
     */
    public void setPcaParameters(PCAParameters pcaParameters) {
        this.pcaParameters = pcaParameters;
    }

    /**
     * @return the preprocessParameters
     */
    public PreprocessDatasetParameters getPreprocessParameters() {
        return preprocessParameters;
    }

    /**
     * @param preprocessParameters the preprocessParameters to set
     */
    public void setPreprocessParameters(PreprocessDatasetParameters preprocessParameters) {
        this.preprocessParameters = preprocessParameters;
    }

    /**
     * @return the usePreprocessDataset
     */
    public boolean isUsePreprocessDataset() {
        return usePreprocessDataset;
    }

    /**
     * @param usePreprocessDataset the usePreprocessDataset to set
     */
    public void setUsePreprocessDataset(boolean usePreprocessDataset) {
        this.usePreprocessDataset = usePreprocessDataset;
    }

    /**
     * @return the controlSampleSetName
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
}
