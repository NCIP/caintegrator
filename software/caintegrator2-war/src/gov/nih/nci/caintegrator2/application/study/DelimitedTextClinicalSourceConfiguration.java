/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.study;

import java.util.List;

/**
 * Holds configuration information for annotation stored in a CSV text file.
 */
@SuppressWarnings("PMD.CyclomaticComplexity")   // see method createAnnotationValue
public class DelimitedTextClinicalSourceConfiguration extends AbstractClinicalSourceConfiguration {
    
    private static final long serialVersionUID = 1L;
    private AnnotationFile annotationFile;

    /**
     * Creates a new instance.
     */
    public DelimitedTextClinicalSourceConfiguration() {
        super();
    }
    
    DelimitedTextClinicalSourceConfiguration(AnnotationFile annotationFile, StudyConfiguration configuration) {
        super(configuration);
        this.annotationFile = annotationFile;
    }

    ValidationResult validate() {
        return getAnnotationFile().validate();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    void loadAnnotation() throws ValidationException {
        getAnnotationFile().loadAnnontation(new SubjectAnnotationHandler(this));
        setStatus(Status.LOADED);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    void reLoadAnnotation() throws ValidationException {
        if (isCurrentlyLoaded() || Status.PROCESSING.equals(getStatus())) {
            getAnnotationFile().loadAnnontation(new SubjectAnnotationHandler(this));
            setStatus(Status.LOADED);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    void unloadAnnotation() {
        getAnnotationFile().unloadAnnotation();
        setStatus(Status.NOT_LOADED);
    }

    /**
     * @return ClinicalSourceType
     */
    public ClinicalSourceType getType() {
        return ClinicalSourceType.DELIMITED_TEXT;
    }

    /**
     * @return the annotationFile
     */
    public AnnotationFile getAnnotationFile() {
        return annotationFile;
    }

    /**
     * @param annotationFile the annotationFile to set
     */
    public void setAnnotationFile(AnnotationFile annotationFile) {
        this.annotationFile = annotationFile;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription() {
        return getAnnotationFile().getFile().getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AnnotationFieldDescriptor> getAnnotationDescriptors() {
        return getAnnotationFile().getDescriptors();
    }

    /**
     * {@inheritDoc}
     */
    public boolean isLoadable() {
        return getAnnotationFile().isLoadable();
    }

    /**
     * {@inheritDoc}
     */
    public boolean isCurrentlyLoaded() {
        return Status.LOADED.equals(getStatus());
    }

}
