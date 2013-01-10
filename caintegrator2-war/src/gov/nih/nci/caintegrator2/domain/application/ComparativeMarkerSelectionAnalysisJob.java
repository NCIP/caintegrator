/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.domain.application;

import gov.nih.nci.caintegrator2.web.action.analysis.ComparativeMarkerSelectionAnalysisForm;

/**
 * Object representing a single gene pattern analysis job.
 */
public class ComparativeMarkerSelectionAnalysisJob extends AbstractPersistedAnalysisJob {

    private static final long serialVersionUID = 1L;
    
    private final transient ComparativeMarkerSelectionAnalysisForm comparativeMarkerSelectionAnalysisForm
        = new ComparativeMarkerSelectionAnalysisForm();
    private String preprocessDataSetUrl;
    private String comparativeMarkerSelectionUrl;
    private ResultsZipFile resultsZipFile;
    
    /**
     * Default Constructor.
     */
    public ComparativeMarkerSelectionAnalysisJob() {
        this.setJobType(AnalysisJobTypeEnum.CMS);
        this.setMethod(AnalysisJobTypeEnum.CMS.getValue());
    }

    /**
     * @return the analysisForm
     */
    public ComparativeMarkerSelectionAnalysisForm getForm() {
        return comparativeMarkerSelectionAnalysisForm;
    }

    /**
     * @return the preprocessDataSetUrl
     */
    public String getPreprocessDataSetUrl() {
        return preprocessDataSetUrl;
    }

    /**
     * @param preprocessDataSetUrl the preprocessDataSetUrl to set
     */
    public void setPreprocessDataSetUrl(String preprocessDataSetUrl) {
        this.preprocessDataSetUrl = preprocessDataSetUrl;
    }

    /**
     * @return the comparativeMarkerSelectionUrl
     */
    public String getComparativeMarkerSelectionUrl() {
        return comparativeMarkerSelectionUrl;
    }

    /**
     * @param comparativeMarkerSelectionUrl the comparativeMarkerSelectionUrl to set
     */
    public void setComparativeMarkerSelectionUrl(String comparativeMarkerSelectionUrl) {
        this.comparativeMarkerSelectionUrl = comparativeMarkerSelectionUrl;
    }

    /**
     * @return the resultsZipFile
     */
    public ResultsZipFile getResultsZipFile() {
        return resultsZipFile;
    }

    /**
     * @param resultsZipFile the resultsZipFile to set
     */
    public void setResultsZipFile(ResultsZipFile resultsZipFile) {
        this.resultsZipFile = resultsZipFile;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(retrieveHeader("Comparative Marker Selection"));
        sb.append(getForm().getPreprocessDatasetparameters().toString());
        sb.append(getForm().getComparativeMarkerSelectionParameters().toString());
        return sb.toString();
    }

}
