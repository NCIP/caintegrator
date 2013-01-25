/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.analysis;

import java.util.Set;

import gov.nih.nci.caintegrator2.application.analysis.igv.IGVParameters;
import gov.nih.nci.caintegrator2.domain.application.CopyNumberCriterionTypeEnum;
import gov.nih.nci.caintegrator2.domain.genomic.GenomeBuildVersionEnum;
import gov.nih.nci.caintegrator2.domain.genomic.Platform;
import gov.nih.nci.caintegrator2.web.SessionHelper;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;

/**
 * 
 */
public class ViewAllIGVAction extends AbstractViewAllAction {
    
    private static final long serialVersionUID = 1L;

    private static final String VIEW_IGV = "viewIGV";
    private Set<String> expressionPlatformsInStudy;
    private String expressionPlatformName;

    /**
     * {@inheritDoc}
     */
    public void prepare() {
        super.prepare();
        expressionPlatformsInStudy = getQueryManagementService().retrieveGeneExpressionPlatformsForStudy(getStudy());
    }

    /**
     * {@inheritDoc}
     */
    protected String viewAll() {
        IGVParameters igvParameters = new IGVParameters();
        igvParameters.setStudySubscription(getStudySubscription());
        igvParameters.setSessionId(ServletActionContext.getRequest().getRequestedSessionId());
        igvParameters.setUrlPrefix(SessionHelper.getIgvSessionUrl());
        igvParameters.setQuery(getQuery());
        igvParameters.setPlatforms(getPlatforms());
        igvParameters.setViewAllData(true);
        igvParameters.setUseCGHCall(CopyNumberCriterionTypeEnum.CALLS_VALUE.equals(getCopyNumberType()));
        getDisplayableWorkspace().setIgvParameters(igvParameters);
        return VIEW_IGV;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void init() {
        getQueryForm().createQuery(getStudySubscription(), expressionPlatformsInStudy, getCopyNumberPlatformsInStudy(),
                null);
        if (!expressionPlatformsInStudy.isEmpty()) {
            expressionPlatformName = expressionPlatformsInStudy.iterator().next();
        }
        if (!getCopyNumberPlatformsInStudy().isEmpty()) {
            setCopyNumberPlatformName(getCopyNumberPlatformsInStudy().iterator().next());
        }
    }

    /**
     * @param genomeBuild capture all distinct genomeBuild.
     */
    protected void addPlatform(Set<GenomeBuildVersionEnum> genomeBuild) {
        if (!StringUtils.isEmpty(expressionPlatformName)) {
            retrieveAndAddPlatform(genomeBuild, expressionPlatformName);
        }
        if (!StringUtils.isEmpty(getCopyNumberPlatformName())) {
            retrieveAndAddPlatform(genomeBuild, getCopyNumberPlatformName());
        }
    }
    
    private void retrieveAndAddPlatform(Set<GenomeBuildVersionEnum> genomeBuild, String platformName) {
        Platform platform = getArrayDataService().getPlatform(platformName);
        getPlatforms().add(platform);
        if (platform.getGenomeVersion() != null) {
            genomeBuild.add(platform.getGenomeVersion());
        }
    }

    /**
     * @return the expressionPlatformsInStudy
     */
    public Set<String> getExpressionPlatformsInStudy() {
        return expressionPlatformsInStudy;
    }

    /**
     * @param expressionPlatformsInStudy the expressionPlatformsInStudy to set
     */
    public void setExpressionPlatformsInStudy(Set<String> expressionPlatformsInStudy) {
        this.expressionPlatformsInStudy = expressionPlatformsInStudy;
    }
    
    /**
     * @return the option label for expression platform selector
     */
    public String getExpressionPlatformOption() {
        return expressionPlatformsInStudy.isEmpty() ? NONE : "";
    }

    /**
     * @return the expressionPlatformName
     */
    public String getExpressionPlatformName() {
        return expressionPlatformName;
    }

    /**
     * @param expressionPlatformName the expressionPlatformName to set
     */
    public void setExpressionPlatformName(String expressionPlatformName) {
        this.expressionPlatformName = expressionPlatformName;
    }
}
