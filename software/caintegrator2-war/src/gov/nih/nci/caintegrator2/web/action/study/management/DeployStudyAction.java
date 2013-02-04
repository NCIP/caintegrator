/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.study.management;

import gov.nih.nci.caintegrator2.application.study.LogEntry;
import gov.nih.nci.caintegrator2.application.study.deployment.DeploymentService;
import gov.nih.nci.caintegrator2.web.ajax.IStudyDeploymentAjaxUpdater;

/**
 * Action that deploys a study.
 */
public class DeployStudyAction extends SaveStudyAction {

    private static final long serialVersionUID = 1L;
    private IStudyDeploymentAjaxUpdater ajaxUpdater;
    private DeploymentService deploymentService;

    /**
     * {@inheritDoc}
     */
    @Override
    public String execute() {
        setStudyLastModifiedByCurrentUser(null, LogEntry.getSystemLogDeploy(getStudyConfiguration().getStudy()));
        getDeploymentService().prepareForDeployment(getStudyConfiguration(), null);
        ajaxUpdater.runJob(getStudyConfiguration());
        return SUCCESS;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("PMD.UselessOverridingMethod") // In case we ever do other validation here, must still call super
    public void validate() {
        super.validate();
    }

    /**
     * @return the ajaxUpdater
     */
    public IStudyDeploymentAjaxUpdater getAjaxUpdater() {
        return ajaxUpdater;
    }

    /**
     * @param ajaxUpdater the ajaxUpdater to set
     */
    public void setAjaxUpdater(IStudyDeploymentAjaxUpdater ajaxUpdater) {
        this.ajaxUpdater = ajaxUpdater;
    }

    /**
     * @return the deploymentService
     */
    public DeploymentService getDeploymentService() {
        return deploymentService;
    }

    /**
     * @param deploymentService the deploymentService to set
     */
    public void setDeploymentService(DeploymentService deploymentService) {
        this.deploymentService = deploymentService;
    }
    
}
