/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.external.ncia;

import gov.nih.nci.caintegrator2.external.ServerConnectionProfile;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * Object used to represent an NCIA Dicom Retrieval job.
 */
public class NCIADicomJob implements NCIAImageAggregator {
    
    private final Set <String> imageSeriesIDs = new HashSet<String>();
    private final Set <String> imageStudyIDs = new HashSet<String>();
    private String jobId;
    private ServerConnectionProfile serverConnection = new ServerConnectionProfile();
    private boolean completed = false;
    private File dicomFile;
    private boolean currentlyRunning = false;
    private NCIAImageAggregationTypeEnum imageAggregationType;
    
    /**
     * Sets the default server connection (this is only temporary until we solve the solution of figuring out a server
     * from the image series).
     */
    public NCIADicomJob() {
        serverConnection.setUrl("http://imaging.nci.nih.gov/wsrf/services/cagrid/NCIACoreService");
    }
    
    /**
     * @return the jobId
     */
    public String getJobId() {
        return jobId;
    }
    /**
     * @param jobId the jobId to set
     */
    public void setJobId(String jobId) {
        this.jobId = jobId;
    }
    /**
     * @return the serverConnection
     */
    public ServerConnectionProfile getServerConnection() {
        return serverConnection;
    }
    /**
     * @param serverConnection the serverConnection to set
     */
    public void setServerConnection(ServerConnectionProfile serverConnection) {
        this.serverConnection = serverConnection;
    }

    /**
     * @return the completed
     */
    public boolean isCompleted() {
        return completed;
    }
    /**
     * @param completed the completed to set
     */
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    /**
     * @return the imageSeriesIDs
     */
    public Set<String> getImageSeriesIDs() {
        return imageSeriesIDs;
    }
    /**
     * @return the imageStudyIDs
     */
    public Set<String> getImageStudyIDs() {
        return imageStudyIDs;
    }
    
    /**
     * Validates if a job has imaging ID data. 
     * @return T/F value.
     */
    public boolean hasData() {
        if (imageSeriesIDs.isEmpty() && imageStudyIDs.isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * @return the dicomFile
     */
    public File getDicomFile() {
        return dicomFile;
    }

    /**
     * @param dicomFile the dicomFile to set
     */
    public void setDicomFile(File dicomFile) {
        this.dicomFile = dicomFile;
    }

    /**
     * @return the currentlyRunning
     */
    public boolean isCurrentlyRunning() {
        return currentlyRunning;
    }

    /**
     * @param currentlyRunning the currentlyRunning to set
     */
    public void setCurrentlyRunning(boolean currentlyRunning) {
        this.currentlyRunning = currentlyRunning;
    }

    /**
     * @return the imageAggregationType
     */
    public NCIAImageAggregationTypeEnum getImageAggregationType() {
        return imageAggregationType;
    }

    /**
     * @param imageAggregationType the imageAggregationType to set
     */
    public void setImageAggregationType(NCIAImageAggregationTypeEnum imageAggregationType) {
        this.imageAggregationType = imageAggregationType;
    }
}
