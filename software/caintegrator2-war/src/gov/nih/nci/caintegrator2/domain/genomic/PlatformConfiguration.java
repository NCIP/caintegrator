/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.domain.genomic;

import gov.nih.nci.caintegrator2.application.arraydata.AbstractPlatformSource;
import gov.nih.nci.caintegrator2.application.arraydata.PlatformTypeEnum;
import gov.nih.nci.caintegrator2.application.study.Status;
import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;

import java.util.Date;

/**
 * 
 */
public class PlatformConfiguration extends AbstractCaIntegrator2Object {
    
    private static final long serialVersionUID = 1L;
    private transient AbstractPlatformSource platformSource;
    private transient boolean inUse = false;
    private Platform platform;
    private PlatformTypeEnum platformType;
    private String name;
    private Date deploymentStartDate;
    private Date deploymentFinishDate;
    private Status status = Status.NOT_LOADED;
    private String statusDescription;
    
    
    /**
     * Default Constructor (not sure if it's needed, for hibernate maybe?).
     */
    public PlatformConfiguration() {
        // For hibernate.
    }
    
    /**
     * Public constructor.
     * @param platformSource source to load platform with.
     */
    public PlatformConfiguration(AbstractPlatformSource platformSource) {
        this.deploymentStartDate = new Date();
        this.platformSource = platformSource;
    }
    
    /**
     * @return the platformSource
     */
    public AbstractPlatformSource getPlatformSource() {
        return platformSource;
    }
    
    /**
     * @param platformSource the platformSource to set
     */
    public void setPlatformSource(AbstractPlatformSource platformSource) {
        this.platformSource = platformSource;
    }
    
    /**
     * @return the platform
     */
    public Platform getPlatform() {
        return platform;
    }
    
    /**
     * @param platform the platform to set
     */
    public void setPlatform(Platform platform) {
        this.platform = platform;
    }
    
    /**
     * @return the deploymentStartDate
     */
    public Date getDeploymentStartDate() {
        return deploymentStartDate;
    }
    
    /**
     * @param deploymentStartDate the deploymentStartDate to set
     */
    public void setDeploymentStartDate(Date deploymentStartDate) {
        this.deploymentStartDate = deploymentStartDate;
    }
    
    /**
     * @return the deploymentFinishDate
     */
    public Date getDeploymentFinishDate() {
        return deploymentFinishDate;
    }
    
    /**
     * @param deploymentFinishDate the deploymentFinishDate to set
     */
    public void setDeploymentFinishDate(Date deploymentFinishDate) {
        this.deploymentFinishDate = deploymentFinishDate;
    }
    
    /**
     * @return the status
     */
    public Status getStatus() {
        return status;
    }
    
    /**
     * @param status the status to set
     */
    public void setStatus(Status status) {
        this.status = status;
    }
    
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
        this.statusDescription = statusDescription;
    }

    /**
     * @return the inUse
     */
    public boolean isInUse() {
        return inUse;
    }

    /**
     * @param inUse the inUse to set
     */
    public void setInUse(boolean inUse) {
        this.inUse = inUse;
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
     * @return the platformType
     */
    public PlatformTypeEnum getPlatformType() {
        return platformType;
    }

    /**
     * @param platformType the platformType to set
     */
    public void setPlatformType(PlatformTypeEnum platformType) {
        this.platformType = platformType;
    }     
}
