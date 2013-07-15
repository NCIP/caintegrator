/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.action.analysis;

import gov.nih.nci.caintegrator.application.analysis.AnalysisService;
import gov.nih.nci.caintegrator.application.arraydata.ArrayDataService;
import gov.nih.nci.caintegrator.application.query.QueryManagementService;
import gov.nih.nci.caintegrator.domain.application.CopyNumberCriterionTypeEnum;
import gov.nih.nci.caintegrator.domain.genomic.GenomeBuildVersionEnum;
import gov.nih.nci.caintegrator.domain.genomic.Platform;
import gov.nih.nci.caintegrator.web.action.AbstractDeployedStudyAction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 */
public abstract class AbstractViewAllAction extends AbstractDeployedStudyAction {

    private static final long serialVersionUID = 1L;

    private static final String VIEW_ALL = "viewAll";
    private static final String CANCEL_ACTION = "cancel";
    private static final String HOME_PAGE = "homePage";
    /**
     * NONE constant.
     */
    protected static final String NONE = "-- None Available --";
    private String selectedAction;
    private QueryManagementService queryManagementService;
    private ArrayDataService arrayDataService;
    private AnalysisService analysisService;
    private Set<String> copyNumberPlatformsInStudy;
    private String copyNumberPlatformName;
    private List<Platform> platforms;
    private CopyNumberCriterionTypeEnum copyNumberType = CopyNumberCriterionTypeEnum.SEGMENT_VALUE;

    /**
     * {@inheritDoc}
     */
    @Override
    public void prepare() {
        super.prepare();
        copyNumberPlatformsInStudy = getQueryManagementService().retrieveCopyNumberPlatformsForStudy(getStudy());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate() {
        super.validate();
        if (VIEW_ALL.equals(selectedAction)) {
            Set<GenomeBuildVersionEnum> genomeBuild = new HashSet<GenomeBuildVersionEnum>();
            platforms = new ArrayList<Platform>();
            addPlatform(genomeBuild);
            if (platforms.isEmpty()) {
                addActionError(getText("struts.messages.error.igv.no.platform"));
            }
            if (genomeBuild.size() > 1) {
                addActionError(getText("struts.messages.error.igv.mix.genome"));
            }
        }
    }

    abstract void addPlatform(Set<GenomeBuildVersionEnum> genomeBuild);

    /**
     * {@inheritDoc}
     */
    @Override
    public String execute() {
        if  (VIEW_ALL.equals(selectedAction)) {
            return viewAll();
        } else if (CANCEL_ACTION.equals(selectedAction)) {
            return HOME_PAGE;
        }
        init();
        return SUCCESS;
    }

    abstract void init();

    abstract String viewAll();

    /**
     * @return the selectedAction
     */
    public String getSelectedAction() {
        return selectedAction;
    }

    /**
     * @param selectedAction the selectedAction to set
     */
    public void setSelectedAction(String selectedAction) {
        this.selectedAction = selectedAction;
    }

    /**
     * @return the queryManagementService
     */
    public QueryManagementService getQueryManagementService() {
        return queryManagementService;
    }

    /**
     * @param queryManagementService the queryManagementService to set
     */
    @Autowired
    public void setQueryManagementService(QueryManagementService queryManagementService) {
        this.queryManagementService = queryManagementService;
    }

    /**
     * @return the copyNumberPlatformsInStudy
     */
    public Set<String> getCopyNumberPlatformsInStudy() {
        return copyNumberPlatformsInStudy;
    }

    /**
     * @param copyNumberPlatformsInStudy the copyNumberPlatformsInStudy to set
     */
    public void setCopyNumberPlatformsInStudy(Set<String> copyNumberPlatformsInStudy) {
        this.copyNumberPlatformsInStudy = copyNumberPlatformsInStudy;
    }

    /**
     * @return the copyNumberPlatformName
     */
    public String getCopyNumberPlatformName() {
        return copyNumberPlatformName;
    }

    /**
     * @param copyNumberPlatformName the copyNumberPlatformName to set
     */
    public void setCopyNumberPlatformName(String copyNumberPlatformName) {
        this.copyNumberPlatformName = copyNumberPlatformName;
    }

    /**
     * @return the analysisService
     */
    public AnalysisService getAnalysisService() {
        return analysisService;
    }

    /**
     * @param analysisService the analysisService to set
     */
    @Autowired
    public void setAnalysisService(AnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    /**
     * @return the arrayDataService
     */
    public ArrayDataService getArrayDataService() {
        return arrayDataService;
    }

    /**
     * @param arrayDataService the arrayDataService to set
     */
    @Autowired
    public void setArrayDataService(ArrayDataService arrayDataService) {
        this.arrayDataService = arrayDataService;
    }

    /**
     * @return the option label for copy number platform selector
     */
    public String getCopyNumberPlatformOption() {
        return copyNumberPlatformsInStudy.isEmpty() ? NONE : "";
    }

    /**
     * @return the platforms
     */
    protected List<Platform> getPlatforms() {
        return platforms;
    }

    /**
     * @param platforms the platforms to set
     */
    protected void setPlatforms(List<Platform> platforms) {
        this.platforms = platforms;
    }

    /**
     * @return the copyNumberType
     */
    public CopyNumberCriterionTypeEnum getCopyNumberType() {
        return copyNumberType;
    }

    /**
     * @param copyNumberType the copyNumberType to set
     */
    public void setCopyNumberType(CopyNumberCriterionTypeEnum copyNumberType) {
        this.copyNumberType = copyNumberType;
    }
}
