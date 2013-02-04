/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.study.management;

import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.application.study.StudyManagementService;
import gov.nih.nci.caintegrator2.web.SessionHelper;
import gov.nih.nci.security.exceptions.CSSecurityException;

import com.opensymphony.xwork2.ModelDriven;

/**
 * Base class for actions that require retrieval of persistent <code>StudyConfigurations</code>.
 */
public abstract class AbstractStudyAction extends AbstractStudyManagementAction 
implements ModelDriven<StudyConfiguration> {
    
    private StudyConfiguration studyConfiguration = new StudyConfiguration();
    private StudyManagementService studyManagementService;
    
    /**
     * {@inheritDoc}
     */
    public void prepare() {
        super.prepare();
        if (studyConfiguration.getId() != null) {
            try {
                studyConfiguration = studyManagementService.getRefreshedSecureStudyConfiguration(
                        SessionHelper.getInstance().getUsername(), studyConfiguration.getId());
            } catch (CSSecurityException e) {
                setAuthorizedPage(false);
            }
            getDisplayableWorkspace().setCurrentStudyConfiguration(studyConfiguration);
        }
    }

    /**
     * {@inheritDoc}
     */
    public final StudyConfiguration getModel() {
        return getStudyConfiguration();
    }

    /**
     * @return the studyConfiguration
     */
    public final StudyConfiguration getStudyConfiguration() {
        return studyConfiguration;
    }

    /**
     * @param studyConfiguration the studyConfiguration to set
     */
    public final void setStudyConfiguration(StudyConfiguration studyConfiguration) {
        this.studyConfiguration = studyConfiguration;
    }

    /**
     * @return the studyManagementService
     */
    public final StudyManagementService getStudyManagementService() {
        return studyManagementService;
    }

    /**
     * @param studyManagementService the studyManagementService to set
     */
    public final void setStudyManagementService(StudyManagementService studyManagementService) {
        this.studyManagementService = studyManagementService;
    }
    
    /**
     * Sets the lastModifiedBy attribute of a StudyConfiguration to the current user's workspace.
     */
    protected void setLastModifiedByCurrentUser() {
        getStudyManagementService().setLastModifiedByCurrentUser(getStudyConfiguration(), getWorkspace());
    }

}
