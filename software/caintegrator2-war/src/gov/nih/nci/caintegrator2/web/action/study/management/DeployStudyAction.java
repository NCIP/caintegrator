/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.study.management;

import gov.nih.nci.caintegrator2.application.analysis.heatmap.HeatmapParameters;
import gov.nih.nci.caintegrator2.application.study.LogEntry;
import gov.nih.nci.caintegrator2.application.study.deployment.DeploymentService;
import gov.nih.nci.caintegrator2.web.SessionHelper;
import gov.nih.nci.caintegrator2.web.ajax.IStudyDeploymentAjaxUpdater;

import javax.servlet.ServletContext;

import org.apache.struts2.util.ServletContextAware;

/**
 * Action that deploys a study.
 */
public class DeployStudyAction extends SaveStudyAction implements ServletContextAware {

    private static final long serialVersionUID = 1L;
    private IStudyDeploymentAjaxUpdater ajaxUpdater;
    private DeploymentService deploymentService;
    private ServletContext context;

    /**
     * {@inheritDoc}
     */
    @Override
    public String execute() {
        cleanStudyName();
        setStudyLastModifiedByCurrentUser(null, LogEntry.getSystemLogDeploy(getStudyConfiguration().getStudy()));
        getDeploymentService().prepareForDeployment(getStudyConfiguration());
        ajaxUpdater.runJob(getStudyConfiguration(), createHeatmapParameters());
        return SUCCESS;
    }
    
    private HeatmapParameters createHeatmapParameters() {
        HeatmapParameters heatmapParameters = new HeatmapParameters();
        heatmapParameters.setViewAllData(true);
        heatmapParameters.setLargeBinsFile(SessionHelper.getHeatmapLargeBinsFile(context));
        heatmapParameters.setSmallBinsFile(SessionHelper.getHeatmapSmallBinsFile(context));
        return heatmapParameters;
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

    /**
     * {@inheritDoc}
     */
    public void setServletContext(ServletContext servletContext) {
        this.context = servletContext;
        
    }
    
}
