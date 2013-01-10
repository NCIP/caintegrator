/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.web.action.analysis;

import gov.nih.nci.caintegrator2.application.analysis.grid.pca.PCAParameters;
import gov.nih.nci.caintegrator2.application.analysis.grid.preprocess.PreprocessDatasetParameters;
import gov.nih.nci.caintegrator2.external.ServerConnectionProfile;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Used for Struts representation of the currently configured analysis method.
 */
public class PrincipalComponentAnalysisForm {
    
    private PCAParameters pcaParameters;
    private PreprocessDatasetParameters preprocessParameters;
    private List<String> controlSampleSets = new ArrayList<String>();
    private final ServerConnectionProfile server = new ServerConnectionProfile();
    private boolean usePreprocessDataset = false;
    private String selectedQueryName;
    private String excludeControlSampleSetName;
    private String platformName = null;
    
    // JSP Select List Options
    private final SortedMap<String, DisplayableQuery> queries = new TreeMap<String, DisplayableQuery>();

    /**
     * 
     * @return the server.
     */
    public ServerConnectionProfile getServer() {
        return server;
    }

    /**
     * @return the selectedQueryName
     */
    public String getSelectedQueryName() {
        return selectedQueryName;
    }

    /**
     * @param selectedQueryName the selectedQueryName to set
     */
    public void setSelectedQueryName(String selectedQueryName) {
        this.selectedQueryName = selectedQueryName;
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

    /**
     * @return the queries
     */
    public SortedMap<String, DisplayableQuery> getQueries() {
        return queries;
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
