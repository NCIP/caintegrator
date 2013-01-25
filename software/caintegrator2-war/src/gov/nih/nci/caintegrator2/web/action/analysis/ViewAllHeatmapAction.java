/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.analysis;

import gov.nih.nci.caintegrator2.application.analysis.heatmap.HeatmapParameters;
import gov.nih.nci.caintegrator2.domain.application.CopyNumberCriterionTypeEnum;
import gov.nih.nci.caintegrator2.domain.genomic.GenomeBuildVersionEnum;
import gov.nih.nci.caintegrator2.domain.genomic.Platform;
import gov.nih.nci.caintegrator2.web.SessionHelper;

import java.util.Set;

import javax.servlet.ServletContext;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.util.ServletContextAware;

/**
 * 
 */
public class ViewAllHeatmapAction extends AbstractViewAllAction implements ServletContextAware {
    private static final long serialVersionUID = 1L;
    private static final String VIEW_HEATMAP = "viewHeatmap";
    private ServletContext context;
    
    /**
     * {@inheritDoc}
     */
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
        heatmapParameters.setViewAllData(true);
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
}
