/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.action.analysis;

import gov.nih.nci.caintegrator.application.analysis.heatmap.HeatmapParameters;
import gov.nih.nci.caintegrator.application.study.StudyManagementService;
import gov.nih.nci.caintegrator.domain.application.CopyNumberCriterionTypeEnum;
import gov.nih.nci.caintegrator.domain.genomic.GenomeBuildVersionEnum;
import gov.nih.nci.caintegrator.domain.genomic.Platform;
import gov.nih.nci.caintegrator.web.SessionHelper;

import java.util.Set;

import javax.servlet.ServletContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.util.ServletContextAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
public class ViewAllHeatmapAction extends AbstractViewAllAction implements ServletContextAware {
    private static final long serialVersionUID = 1L;
    private static final String VIEW_HEATMAP = "viewHeatmap";
    private ServletContext context;
    private StudyManagementService studyManagementService;

    /**
     * {@inheritDoc}
     */
    @Override
    protected String viewAll() {
        HeatmapParameters heatmapParameters = new HeatmapParameters();
        heatmapParameters.setStudySubscription(getStudySubscription());
        heatmapParameters.setSessionId(ServletActionContext.getRequest().getRequestedSessionId());
        heatmapParameters.setUrlPrefix(SessionHelper.getHeatmapSessionUrl());
        heatmapParameters.setHeatmapJarUrlPrefix(SessionHelper.getCaIntegratorCommonUrl());
        heatmapParameters.setLargeBinsFile(SessionHelper.getHeatmapLargeBinsFile(context));
        heatmapParameters.setSmallBinsFile(SessionHelper.getHeatmapSmallBinsFile(context));
        heatmapParameters.setQuery(getQuery());
        heatmapParameters.setPlatform(getPlatforms().get(0));
        heatmapParameters.setViewAllData(false);
        heatmapParameters.setUseCGHCall(CopyNumberCriterionTypeEnum.CALLS_VALUE.equals(getCopyNumberType()));
        getDisplayableWorkspace().setHeatmapParameters(heatmapParameters);
        return VIEW_HEATMAP;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void addPlatform(Set<GenomeBuildVersionEnum> genomeBuild) {
        if (!StringUtils.isEmpty(getCopyNumberPlatformName())) {
            Platform platform = getArrayDataService().getPlatform(getCopyNumberPlatformName());
            getPlatforms().add(platform);
            if (platform.getGenomeVersion() != null) {
                genomeBuild.add(platform.getGenomeVersion());
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void init() {
        getQueryForm().setStudyManagementService(getStudyManagementService());
        getQueryForm().createQuery(getStudySubscription(), null, getCopyNumberPlatformsInStudy(), null);
        if (!getCopyNumberPlatformsInStudy().isEmpty()) {
            setCopyNumberPlatformName(getCopyNumberPlatformsInStudy().iterator().next());
        }
    }

    /**
     * {@inheritDoc}
     */
    public void setServletContext(ServletContext servletContext) {
        this.context = servletContext;

    }

    /**
     * @return the studyManagementService
     */
    public StudyManagementService getStudyManagementService() {
        return studyManagementService;
    }

    /**
     * @param studyManagementService the studyManagementService to set
     */
    @Autowired
    public void setStudyManagementService(StudyManagementService studyManagementService) {
        this.studyManagementService = studyManagementService;
    }
}
