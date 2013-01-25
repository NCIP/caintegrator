/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.domain.application;

import gov.nih.nci.caintegrator2.web.action.analysis.PrincipalComponentAnalysisForm;

/**
 * Object representing a single gene pattern analysis job.
 */
public class PrincipalComponentAnalysisJob extends AbstractPersistedAnalysisJob {

    private static final long serialVersionUID = 1L;
    
    private final transient PrincipalComponentAnalysisForm form = new PrincipalComponentAnalysisForm();
    private String preprocessDataSetUrl;
    private ResultsZipFile resultsZipFile;
    private String pcaUrl;

    /**
     * Default Constructor.
     */
    public PrincipalComponentAnalysisJob() {
        this.setJobType(AnalysisJobTypeEnum.PCA);
        this.setMethod(AnalysisJobTypeEnum.PCA.getValue());
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
     * @return the form
     */
    public PrincipalComponentAnalysisForm getForm() {
        return form;
    }

    /**
     * @return the pcaUrl
     */
    public String getPcaUrl() {
        return pcaUrl;
    }

    /**
     * @param pcaUrl the pcaUrl to set
     */
    public void setPcaUrl(String pcaUrl) {
        this.pcaUrl = pcaUrl;
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
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(retrieveHeader("Principal Component Analysis"));
        if (getForm().isUsePreprocessDataset()) {
            sb.append(getForm().getPreprocessParameters().toString());
        }
        sb.append(getForm().getPcaParameters().toString());
        return sb.toString();
    }
}
