/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.study;

import gov.nih.nci.caintegrator.common.Cai2Util;
import gov.nih.nci.caintegrator.common.DateUtil;
import gov.nih.nci.caintegrator.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator.domain.application.TimeStampable;

import java.util.Date;
import java.util.List;

/**
 * Stores information about the sources of <code>Subject</code> annotation for this <code>Study</code> and provides
 * functionality to work with the configured sources.
 */
public abstract class AbstractClinicalSourceConfiguration extends AbstractCaIntegrator2Object implements TimeStampable {
    
    /**
     * Default serialize.
     */
    private static final long serialVersionUID = 1L;

    private StudyConfiguration studyConfiguration;
    private Date lastModifiedDate;
    private Status status = Status.NOT_LOADED;
    private String statusDescription;
    
    AbstractClinicalSourceConfiguration() {
        super();
    }

    AbstractClinicalSourceConfiguration(StudyConfiguration configuration) {
        this.studyConfiguration = configuration;
        configuration.addClinicalConfiguration(this);
    }
    
    abstract ValidationResult validate();

    abstract ClinicalSourceType getType();

    abstract void loadAnnotation() throws ValidationException;

    abstract void reLoadAnnotation() throws ValidationException;
    
    abstract void unloadAnnotation();
    
    /**
     * Returns a brief description of this clinical source.
     * 
     * @return the brief description;
     */
    public abstract String getDescription();

    /**
     * @return the annotationDescriptors
     */
    public abstract List<AnnotationFieldDescriptor> getAnnotationDescriptors();

    /**
     * @return the studyConfiguration
     */
    public StudyConfiguration getStudyConfiguration() {
        return studyConfiguration;
    }

    /**
     * 
     * @param studyConfiguration the studyConfiguration.
     */
    public void setStudyConfiguration(StudyConfiguration studyConfiguration) {
        this.studyConfiguration = studyConfiguration;
    }
    
    /**
     * @return the lastModifiedDate
     */
    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }
    
    /**
     * {@inheritDoc}
     */
    public String getDisplayableLastModifiedDate() {
        return DateUtil.getDisplayableTimeStamp(lastModifiedDate); 
    }

    /**
     * @param lastModifiedDate the lastModifiedDate to set
     */
    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
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
        this.statusDescription = Cai2Util.trimDescription(statusDescription);
    }

    /**
     * Indicates if the source has been configured and is prepared for loading.
     * 
     * @return whether source may be loaded.
     */
    public abstract boolean isLoadable();
    
    /**
     * Indicates if the source has been already loaded or not.
     * @return whether source is already loaded.
     */
    public abstract boolean isCurrentlyLoaded();

}
