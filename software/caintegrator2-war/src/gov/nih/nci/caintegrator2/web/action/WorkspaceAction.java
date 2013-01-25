/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action;

/**
 * Struts 2 action for user workspace access and management.
 */
public class WorkspaceAction extends AbstractCaIntegrator2Action {

    private static final long serialVersionUID = 1L;

    private static final String WORKSPACE_STUDY = "workspaceStudy";
    private static final String WORKSPACE_NO_STUDY = "workspaceNoStudy";
    private boolean registrationSuccess = false;
    private boolean invalidAccess = false;
    
    /**
     * Opens the current user's workspace.
     * 
     * @return forward to workspace.
     */
    public String openWorkspace() {
        clearForms();
        addRegistrationMessage();
        addErrorMessages();
        if (getStudySubscription() != null  && getActionErrors().isEmpty()) {
            return checkDefaultSubscription();
        }
        return WORKSPACE_NO_STUDY;
    }

    private String checkDefaultSubscription() {
        if (getWorkspace().getDefaultSubscription() == null
                || !getWorkspace().getDefaultSubscription().getStudy().isDeployed()) {
            if (getStudySubscription().getStudy().isDeployed()) {
                getWorkspace().setDefaultSubscription(getStudySubscription());
            } else {
                return WORKSPACE_NO_STUDY;
            }
        }
        getWorkspaceService().saveUserWorkspace(getWorkspace());
        return WORKSPACE_STUDY;
    }
    
    private void addRegistrationMessage() {
        if (registrationSuccess) {
            addActionMessage("Registration request sent successfully!");
        }
    }
    
    private void addErrorMessages() {
        if (invalidAccess) {
            addActionError("You are seeing this page because you are trying to access a restricted area that you "
                    + "do not have authorization to view (possibly because of a timed out session).");
        }
    }

    private void clearForms() {
        getKmPlotForm().clear();
        getGePlotForm().clear();
    }

    /**
     * @return the currentStudySubscriptionId
     */
    public Long getCurrentStudySubscriptionId() {
        return getDisplayableWorkspace().getCurrentStudySubscriptionId();
    }

    /**
     * @param currentStudySubscriptionId the currentStudySubscriptionId to set
     */
    public void setCurrentStudySubscriptionId(Long currentStudySubscriptionId) {
        getDisplayableWorkspace().setCurrentStudySubscriptionId(currentStudySubscriptionId);
    }
    
    /**
     * Returns the result type "studyLogoResult".
     * @return current study logo.
     */
    public String retrieveStudyLogo() {
        getDisplayableWorkspace().setStudyLogo(getStudy().getStudyConfiguration().getStudyLogo());
        return SUCCESS;
    }

    /**
     * @return the registrationSuccess
     */
    public boolean isRegistrationSuccess() {
        return registrationSuccess;
    }

    /**
     * @param registrationSuccess the registrationSuccess to set
     */
    public void setRegistrationSuccess(boolean registrationSuccess) {
        this.registrationSuccess = registrationSuccess;
    }

    /**
     * @return the invalidAccess
     */
    public boolean isInvalidAccess() {
        return invalidAccess;
    }

    /**
     * @param invalidAccess the invalidAccess to set
     */
    public void setInvalidAccess(boolean invalidAccess) {
        this.invalidAccess = invalidAccess;
    }

}
