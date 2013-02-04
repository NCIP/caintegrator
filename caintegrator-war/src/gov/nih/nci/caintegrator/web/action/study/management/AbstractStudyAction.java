/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.action.study.management;

import gov.nih.nci.caintegrator.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator.application.study.StudyManagementService;
import gov.nih.nci.caintegrator.domain.application.TimeStampable;
import gov.nih.nci.caintegrator.domain.translational.Study;
import gov.nih.nci.caintegrator.web.SessionHelper;
import gov.nih.nci.security.exceptions.CSSecurityException;

import com.opensymphony.xwork2.ModelDriven;

/**
 * Base class for actions that require retrieval of persistent <code>StudyConfigurations</code>.
 */
public abstract class AbstractStudyAction extends AbstractCai2ManagementAction 
implements ModelDriven<StudyConfiguration> {
    
    /**
     * Default serialize.
     */
    private static final long serialVersionUID = 1L;
    
    private StudyConfiguration studyConfiguration = new StudyConfiguration();
    private StudyManagementService studyManagementService;
    
    /**
     * {@inheritDoc}
     */
    public void prepare() {
        super.prepare();
        if (!SessionHelper.getInstance().isStudyManager()) {
            setAuthorizedPage(false);
        } else if (studyConfiguration.getId() != null) {
            try {
                studyConfiguration = studyManagementService.getRefreshedSecureStudyConfiguration(
                        SessionHelper.getUsername(), studyConfiguration.getId());
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
     * Sets the lastModifiedBy attribute of a StudyConfiguration to the current user's workspace.  The 
     * timeStampedStudyObject is the specific object that is being changed, if null then it will only 
     * set the timestamp on the current study configuration.  Otherwise it updates the timestamp of both
     * objects.
     * @param timeStampedStudyObject optional object, if non-null it will save the lastModifiedDate of this object
     * as the current time.
     * @param systemLogEntry optional object, if non-null will add a log entry to the study configuration with the
     * system log given.
     */
    protected void setStudyLastModifiedByCurrentUser(TimeStampable timeStampedStudyObject, String systemLogEntry) {
        getStudyManagementService().setStudyLastModifiedByCurrentUser(getStudyConfiguration(), getWorkspace(),
                timeStampedStudyObject, systemLogEntry);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected Study getStudy() {
        if (getStudyConfiguration() != null) {
            return getStudyConfiguration().getStudy();
        }
        return null;
    }
    
    /**
     * Remove malicious characters from the long and short study names.
     */
    public void cleanStudyName() {
        getStudyConfiguration().getStudy().setShortTitleText(removeHtmlChars(getStudyConfiguration().getStudy().
                getShortTitleText()));
        getStudyConfiguration().getStudy().setLongTitleText(removeHtmlChars(getStudyConfiguration().getStudy().
                getLongTitleText()));
    }

}
