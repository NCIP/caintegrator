/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action;

/**
 * Struts 2 action for user workspace access and management.
 */
public class WorkspaceAction extends AbstractCaIntegrator2Action {

    private static final long serialVersionUID = 1L;

    private static final String WORKSPACE_STUDY = "workspaceStudy";
    private static final String WORKSPACE_NO_STUDY = "workspaceNoStudy";
    
    /**
     * Opens the current user's workspace.
     * 
     * @return forward to workspace.
     */
    public String openWorkspace() {
        clearForms();
        if (getStudySubscription() != null) {
            if (getWorkspace().getDefaultSubscription() == null) {
                getWorkspace().setDefaultSubscription(getStudySubscription());
            }
            getWorkspaceService().saveUserWorkspace(getWorkspace());
            return WORKSPACE_STUDY;
        } else {
            return WORKSPACE_NO_STUDY;
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

}
