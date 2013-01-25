/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.study.management;

import gov.nih.nci.caintegrator2.application.study.deployment.DeploymentService;
import gov.nih.nci.caintegrator2.web.ajax.IStudyDeploymentAjaxUpdater;

/**
 * Action that deploys a study.
 */
public class DeployStudyAction extends AbstractStudyAction {

    private static final long serialVersionUID = 1L;
    private IStudyDeploymentAjaxUpdater ajaxUpdater;
    private DeploymentService deploymentService;

    /**
     * {@inheritDoc}
     */
    @Override
    public String execute() {
        setLastModifiedByCurrentUser();
        getDeploymentService().prepareForDeployment(getStudyConfiguration(), null);
        ajaxUpdater.runJob(getStudyConfiguration());
        return SUCCESS;
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
