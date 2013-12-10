/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.action.analysis;

import gov.nih.nci.caintegrator.application.analysis.igv.IGVParameters;
import gov.nih.nci.caintegrator.application.study.StudyManagementService;
import gov.nih.nci.caintegrator.domain.application.CopyNumberCriterionTypeEnum;
import gov.nih.nci.caintegrator.domain.genomic.GenomeBuildVersionEnum;
import gov.nih.nci.caintegrator.domain.genomic.Platform;
import gov.nih.nci.caintegrator.web.SessionHelper;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
public class ViewAllIGVAction extends AbstractViewAllAction {

    private static final long serialVersionUID = 1L;

    private static final String VIEW_IGV = "viewIGV";
    private Set<String> expressionPlatformsInStudy;
    private String expressionPlatformName;
    private StudyManagementService studyManagementService;

    /**
     * {@inheritDoc}
     */
    @Override
    public void prepare() {
        super.prepare();
        expressionPlatformsInStudy = getQueryManagementService().retrieveGeneExpressionPlatformsForStudy(getStudy());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String viewAll() {
        IGVParameters igvParameters = new IGVParameters();
        igvParameters.setStudySubscription(getStudySubscription());
        igvParameters.setSessionId(ServletActionContext.getRequest().getRequestedSessionId());
        igvParameters.setUrlPrefix(SessionHelper.getIgvSessionUrl());
        igvParameters.setPlatforms(getPlatforms());
        igvParameters.setViewAllData(false);
        igvParameters.setUseCGHCall(CopyNumberCriterionTypeEnum.CALLS_VALUE.equals(getCopyNumberType()));
        igvParameters.setQuery(getQuery());
        igvParameters.getQuery().setCopyNumberPlatform(getArrayDataService().getPlatform(getCopyNumberPlatformName()));
        igvParameters.getQuery()
            .setGeneExpressionPlatform(getArrayDataService().getPlatform(getExpressionPlatformName()));
        getDisplayableWorkspace().setIgvParameters(igvParameters);
        return VIEW_IGV;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void init() {
        getQueryForm().setStudyManagementService(getStudyManagementService());
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
    @Override
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
