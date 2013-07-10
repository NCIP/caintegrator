/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.action;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Struts 2 action for user workspace access and management.
 */
@Component
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
public class WorkspaceAction extends AbstractCaIntegrator2Action {

    private static final long serialVersionUID = 1L;

    private static final String WORKSPACE_STUDY = "workspaceStudy";
    private static final String WORKSPACE_NO_STUDY = "workspaceNoStudy";
    private boolean registrationSuccess = false;
    private boolean invalidAccess = false;
    private boolean sessionTimeout = false;
    private String selectedPage;

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
            addActionMessage(getText("struts.messages.registration.successful"));
        }
    }

    private void addErrorMessages() {
        if (invalidAccess) {
            addActionError(getText("struts.messages.error.invalid.access"));
        }
        if (sessionTimeout) {
            addActionError(getText("struts.messages.error.session.timeout"));
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

    /**
     * @return the sessionTimeout
     */
    public boolean isSessionTimeout() {
        return sessionTimeout;
    }

    /**
     * @param sessionTimeout the sessionTimeout to set
     */
    public void setSessionTimeout(boolean sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    /**
     * @return the selectedPage
     */
    public String getSelectedPage() {
        return selectedPage;
    }

    /**
     * @param selectedPage the selectedPage to set
     */
    public void setSelectedPage(String selectedPage) {
        this.selectedPage = selectedPage;
    }
}
