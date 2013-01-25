/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.study;

import java.util.List;
import java.util.Set;

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

    void loadDescriptors() {
        Set<AnnotationFieldDescriptor> existingDescriptors = getStudyConfiguration().getAllExistingDescriptors();
        getAnnotationFile().loadDescriptors(existingDescriptors);
        getAnnotationDescriptors().addAll(getAnnotationFile().getDescriptors());
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    void loadAnnontation() throws ValidationException {
        getAnnotationFile().loadAnnontation(new SubjectAnnotationHandler(this));
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    void reLoadAnnontation() throws ValidationException {
        if (isCurrentlyLoaded()) {
            getAnnotationFile().loadAnnontation(new SubjectAnnotationHandler(this));
        }
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
        return Boolean.valueOf(getAnnotationFile().getCurrentlyLoaded());
    }

}
