/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.domain.application;

import gov.nih.nci.caintegrator2.web.action.analysis.GenePatternAnalysisForm;

/**
 * Object representing a single gene pattern analysis job.
 */
public class GenePatternAnalysisJob extends AbstractPersistedAnalysisJob {

    private static final long serialVersionUID = 1L;
    
    private final transient GenePatternAnalysisForm genePatternAnalysisForm = new GenePatternAnalysisForm();
    private String jobUrl;
    private int gpJobNumber;

    /**
     * Default Constructor.
     */
    public GenePatternAnalysisJob() {
        this.setJobType(AnalysisJobTypeEnum.GENE_PATTERN.getValue());
    }
    /**
     * @return the analysisForm
     */
    public GenePatternAnalysisForm getGenePatternAnalysisForm() {
        return genePatternAnalysisForm;
    }

    /**
     * @return the jobUrl
     */
    public String getJobUrl() {
        return jobUrl;
    }

    /**
     * @param jobUrl the jobUrl to set
     */
    public void setJobUrl(String jobUrl) {
        this.jobUrl = jobUrl;
    }

    /**
     * @return the gpJobNumber
     */
    public int getGpJobNumber() {
        return gpJobNumber;
    }

    /**
     * @param gpJobNumber the gpJobNumber to set
     */
    public void setGpJobNumber(int gpJobNumber) {
        this.gpJobNumber = gpJobNumber;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return null;
    }

}
