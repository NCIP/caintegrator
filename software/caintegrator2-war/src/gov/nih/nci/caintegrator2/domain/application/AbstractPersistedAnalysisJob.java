/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.domain.application;

import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.httpclient.util.DateUtil;

/**
 * 
 */
public abstract class AbstractPersistedAnalysisJob extends AbstractCaIntegrator2Object 
    implements Comparable<AbstractPersistedAnalysisJob> {
    
    private String name;
    private AnalysisJobStatusEnum status = AnalysisJobStatusEnum.NOT_SUBMITTED;
    private Date creationDate;
    private Date lastUpdateDate;
    private String jobType;
    private StudySubscription subscription;
    private String statusDescription;
    private ResultsZipFile inputZipFile;

    private static final int DESCRIPTION_LENGTH = 250;
    
    /**
     * @return the statusDescription
     */
    public String getStatusDescription() {
        return statusDescription;
    }

    /**
     * @param statusDescription the statusDescription to set
     */
    public void setStatusDescription(String statusDescription) {
        if (statusDescription != null && statusDescription.length() > DESCRIPTION_LENGTH) {
            this.statusDescription = statusDescription.substring(0, DESCRIPTION_LENGTH);
        } else {
            this.statusDescription = statusDescription;
        }
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the status
     */
    public AnalysisJobStatusEnum getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(AnalysisJobStatusEnum status) {
        this.status = status;
    }

    /**
     * @return the creationDate
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * @param creationDate the creationDate to set
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * @return the lastUpdateDate
     */
    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    /**
     * @param lastUpdateDate the lastUpdateDate to set
     */
    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    /**
     * @return the subscription
     */
    public StudySubscription getSubscription() {
        return subscription;
    }

    /**
     * @param subscription the subscription to set
     */
    public void setSubscription(StudySubscription subscription) {
        this.subscription = subscription;
    }

    /**
     * {@inheritDoc}
     */
    public UserWorkspace getUserWorkspace() {
        if (getSubscription() != null) {
            return getSubscription().getUserWorkspace();
        }
        return null;
    }
    
    /**
     * {@inheritDoc}
     */
    public int compareTo(AbstractPersistedAnalysisJob o) {
        return this.getCreationDate().compareTo(o.getCreationDate()) * -1;
    }

    /**
     * @return the jobType
     */
    public String getJobType() {
        return jobType;
    }

    /**
     * @param jobType the jobType to set
     */
    public void setJobType(String jobType) {
        this.jobType = jobType;
    }
    
    /**
     * @return the inputZipFile
     */
    public ResultsZipFile getInputZipFile() {
        return inputZipFile;
    }

    /**
     * @param inputZipFile the inputZipFile to set
     */
    public void setInputZipFile(ResultsZipFile inputZipFile) {
        this.inputZipFile = inputZipFile;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public abstract String toString();
    
    /**
     * Writes the job description to the given file.
     * @param file to write to.
     * @throws IOException if unable to write to file.
     */
    public void writeJobDescriptionToFile(File file) throws IOException {
        FileWriter fw = new FileWriter(file);
        fw.append(toString());
        fw.flush();
        fw.close();
    }
    
    /**
     * To be overridden if there is a resultsZipFile.
     * @return the results file for the job.
     */
    @SuppressWarnings("PMD.EmptyMethodInAbstractClassShouldBeAbstract") // empty default implementation
    public ResultsZipFile getResultsZipFile() {
        // default implementation is null; override if appropriate
        return null;
    }
    
    /**
     * Retrieves the header for the toString() of subclasses.
     * @param type of job.
     * @return the header
     */
    protected String retrieveHeader(String type) {
        String nl = "\n";
        StringBuffer sb = new StringBuffer();
        sb.append("Job Type: ").append(type).append(nl);
        sb.append("Job ID: ").append(getId()).append(nl);
        sb.append("Date: ").append(DateUtil.formatDate(new Date(), DateUtil.PATTERN_ASCTIME)).append(nl);
        if (getSubscription() != null && getSubscription().getStudy() != null) {
            sb.append("Study Name: ").append(getSubscription().getStudy().getLongTitleText()).append(nl);
        }
        return sb.toString();
    }
}
