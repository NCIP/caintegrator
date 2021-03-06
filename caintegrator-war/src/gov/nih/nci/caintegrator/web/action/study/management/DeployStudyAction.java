/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.action.study.management;

import gov.nih.nci.caintegrator.application.analysis.heatmap.HeatmapParameters;
import gov.nih.nci.caintegrator.application.study.LogEntry;
import gov.nih.nci.caintegrator.application.study.deployment.DeploymentService;
import gov.nih.nci.caintegrator.web.SessionHelper;
import gov.nih.nci.caintegrator.web.ajax.IStudyDeploymentAjaxUpdater;

import javax.servlet.ServletContext;

import org.apache.struts2.util.ServletContextAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Action that deploys a study.
 */
@Component
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
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
     * @return the ajaxUpdater
     */
    public IStudyDeploymentAjaxUpdater getAjaxUpdater() {
        return ajaxUpdater;
    }

    /**
     * @param ajaxUpdater the ajaxUpdater to set
     */
    @Autowired
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
    @Autowired
    public void setDeploymentService(DeploymentService deploymentService) {
        this.deploymentService = deploymentService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setServletContext(ServletContext servletContext) {
        this.context = servletContext;
    }
}
