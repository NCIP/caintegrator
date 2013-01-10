/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.web.action.study.management;

import gov.nih.nci.caintegrator2.application.study.LogEntry;
import gov.nih.nci.caintegrator2.web.SessionHelper;
import gov.nih.nci.security.exceptions.CSException;


/**
 * Edits a study (new or existing).
 */
public class EditStudyAction extends AbstractStudyAction {

    private static final long serialVersionUID = 1L;
    private static final String STUDY = "Study";

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
            addActionError(getText("struts.messages.error.csm.access"));
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
                addActionError(getText("struts.messages.error.csm.error.deleting", getArgs(STUDY)));
            } catch (RuntimeException e) {
                addActionError(getText("struts.messages.error.deleting", getArgs(STUDY)));
            }
        } else {
            addActionError("User is unauthenticated");
        }
        return SUCCESS;
    }

    /**
     * @return SUCCESS
     */
    public String disableStudy() {

        if (SessionHelper.getInstance().isAuthenticated()) {
            try {
                getStudyConfiguration().getStudy().setEnabled(false);
                getStudyManagementService().save(getStudyConfiguration());
                setStudyLastModifiedByCurrentUser(null,
                        LogEntry.getSystemLogDisable(getStudyConfiguration().getStudy()));
            } catch (RuntimeException e) {
                addActionError(getText("struts.messages.error.disabling", getArgs(STUDY)));
            }
        } else {
            addActionError("User is unauthenticated");
        }
        return SUCCESS;
    }

    /**
     * @return SUCCESS
     */
    public String enableStudy() {

        if (SessionHelper.getInstance().isAuthenticated()) {
            try {
                validate();
                if (!this.hasActionErrors()) {
                    getStudyConfiguration().getStudy().setEnabled(true);
                    getStudyManagementService().save(getStudyConfiguration());
                    setStudyLastModifiedByCurrentUser(null,
                            LogEntry.getSystemLogEnable(getStudyConfiguration().getStudy()));
                }
            } catch (RuntimeException e) {
                addActionError(getText("struts.messages.error.enabling", getArgs(STUDY)));
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate() {
        if (getStudyManagementService().isDuplicateStudyName(getStudyConfiguration().getStudy(),
                getWorkspace().getUsername())) {
            String studyName = getStudyConfiguration().getStudy().getShortTitleText();
            addActionError(getText("struts.messages.error.duplicate.name", getArgs("Study",
                    studyName)));
        }
    }
}
