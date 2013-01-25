/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.study.management;

import gov.nih.nci.caintegrator2.web.SessionHelper;
import gov.nih.nci.security.exceptions.CSException;


/**
 * Edits a study (new or existing).
 */
public class EditStudyAction extends AbstractStudyAction {

    private static final long serialVersionUID = 1L;

    /**
     * {@inheritDoc}
     */
    @Override
    public String execute() {
        return SUCCESS;
    }
    
    /**
     * Used to get to the manage studies screen.
     * @return Struts result.
     */
    public String manageStudies() {
        try {
            getDisplayableWorkspace().getManagedStudies().clear();
            getDisplayableWorkspace().getManagedStudies().addAll(
                    getWorkspaceService().retrieveStudyConfigurationJobs(getDisplayableWorkspace().getUserWorkspace()));
        } catch (CSException e) {
            addActionError("Error accessing CSM to determine user privileges.");
        }
        return SUCCESS;
    }
    
    /**
     * @return SUCCESS
     */
    public String deleteStudy() {

        if (SessionHelper.getInstance().isAuthenticated()) {
            try {
                getDisplayableWorkspace().setCurrentStudyConfiguration(null);
                getStudyManagementService().delete(getStudyConfiguration());
            } catch (CSException e) {
                addActionError("There was a problem deleting the Study ProtectionElement from the CSM tables.");
            } catch (RuntimeException e) {
                addActionError("Study does not exist or cannot be deleted.");
            } 
        } else {
            addActionError("User is unauthenticated");
        }
        return SUCCESS;
    }
    
    /**
     * Returns the result type "studyLogoResult".
     * @return current study logo.
     */
    public String retrieveStudyLogoPreview() {
        getDisplayableWorkspace().setStudyLogo(
                getDisplayableWorkspace().getCurrentStudyConfiguration().getStudyLogo());
        return SUCCESS;
    }
}
