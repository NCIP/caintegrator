/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.domain.application;

import gov.nih.nci.caintegrator.web.action.analysis.GisticAnalysisForm;

/**
 * Object representing a single gene pattern analysis job.
 */
public class GisticAnalysisJob extends AbstractPersistedAnalysisJob {

    private static final long serialVersionUID = 1L;
    
    private final transient GisticAnalysisForm gisticAnalysisForm = new GisticAnalysisForm();
    private ResultsZipFile resultsZipFile;
    private ServerConnectionTypeEnum connectionType = ServerConnectionTypeEnum.UNKNOWN;

    /**
     * Default Constructor.
     */
    public GisticAnalysisJob() {
        this.setJobType(AnalysisJobTypeEnum.GISTIC);
        this.setMethod(AnalysisJobTypeEnum.GISTIC.getValue());
    }

    /**
     * @return the form
     */
    public GisticAnalysisForm getGisticAnalysisForm() {
        return gisticAnalysisForm;
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
     * @return the connectionType
     */
    public ServerConnectionTypeEnum getConnectionType() {
        return connectionType;
    }

    /**
     * @param connectionType the connectionType to set
     */
    public void setConnectionType(ServerConnectionTypeEnum connectionType) {
        this.connectionType = connectionType;
    }

    /**
     * @return true if a grid service invocation, false if web service invocation.
     */
    public boolean isGridServiceCall() {
        return getGisticAnalysisForm().isGridServiceCall();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(retrieveHeader("Gistic Analysis"));
        sb.append(gisticAnalysisForm.getGisticParameters().toString());
        return sb.toString();
    }
}
