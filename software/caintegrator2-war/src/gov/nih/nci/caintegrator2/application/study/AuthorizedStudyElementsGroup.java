/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.study;

import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.security.authorization.domainobjects.Group;

import java.util.ArrayList;
import java.util.List;

/**
 * A grouping of the parts of a study that are restricted and need to be authorized.
 */
public class AuthorizedStudyElementsGroup extends AbstractCaIntegrator2Object {
    private static final long serialVersionUID = 1L;
    private Group authorizedGroup;
    private StudyConfiguration studyConfiguration;

    private List<AuthorizedAnnotationFieldDescriptor> authorizedAnnotationFieldDescriptors =
                                                new ArrayList<AuthorizedAnnotationFieldDescriptor>();

    private List<AuthorizedGenomicDataSourceConfiguration> authorizedGenomicDataSourceConfigurations =
                                                new ArrayList<AuthorizedGenomicDataSourceConfiguration>();

    private List<AuthorizedQuery> authorizedQuerys =
                                                new ArrayList<AuthorizedQuery>();

    /**
     * @return the authorizedGroup
     */
    public Group getAuthorizedGroup() {
        return authorizedGroup;
    }

    /**
     * @param authorizedGroup the authorizedGroup to set
     */
    public void setAuthorizedGroup(Group authorizedGroup) {
        this.authorizedGroup = authorizedGroup;
    }

    /**
     * @return the studyConfiguration
     */
    public StudyConfiguration getStudyConfiguration() {
        return studyConfiguration;
    }

    /**
     * @param studyConfiguration the studyConfiguration to set
     */
    public void setStudyConfiguration(StudyConfiguration studyConfiguration) {
        this.studyConfiguration = studyConfiguration;
    }

    /**
     * @return the authorizedAnnotationFieldDescriptors
     */
    public List<AuthorizedAnnotationFieldDescriptor> getAuthorizedAnnotationFieldDescriptors() {
        return authorizedAnnotationFieldDescriptors;
    }

    /**
     * @param authorizedAnnotationFieldDescriptors the authorizedAnnotationFieldDescriptors to set
     */
    public void setAuthorizedAnnotationFieldDescriptors(
                List<AuthorizedAnnotationFieldDescriptor> authorizedAnnotationFieldDescriptors) {
        this.authorizedAnnotationFieldDescriptors = authorizedAnnotationFieldDescriptors;
    }

    /**
     * @param authorizedGenomicDataSourceConfigurations the authorizedGenomicDataSourceConfigurations to set
     */
    public void setAuthorizedGenomicDataSourceConfigurations(
                List<AuthorizedGenomicDataSourceConfiguration> authorizedGenomicDataSourceConfigurations) {
        this.authorizedGenomicDataSourceConfigurations = authorizedGenomicDataSourceConfigurations;
    }

    /**
     * @return the authorizedGenomicDataSourceConfigurations
     */
    public List<AuthorizedGenomicDataSourceConfiguration> getAuthorizedGenomicDataSourceConfigurations() {
        return authorizedGenomicDataSourceConfigurations;
    }

    /**
     * @param authorizedQuerys the authorizedQuerys to set
     */
    public void setAuthorizedQuerys(List<AuthorizedQuery> authorizedQuerys) {
        this.authorizedQuerys = authorizedQuerys;
    }

    /**
     * @return the authorizedQuerys
     */
    public List<AuthorizedQuery> getAuthorizedQuerys() {
        return authorizedQuerys;
    }
}
