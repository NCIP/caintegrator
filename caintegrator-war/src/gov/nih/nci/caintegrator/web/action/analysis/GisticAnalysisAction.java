/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.web.action.analysis;

import gov.nih.nci.caintegrator.application.analysis.AnalysisService;
import gov.nih.nci.caintegrator.application.analysis.grid.GridDiscoveryServiceJob;
import gov.nih.nci.caintegrator.application.analysis.grid.gistic.GisticParameters;
import gov.nih.nci.caintegrator.application.analysis.grid.gistic.GisticRefgeneFileEnum;
import gov.nih.nci.caintegrator.application.query.InvalidCriterionException;
import gov.nih.nci.caintegrator.application.query.QueryManagementService;
import gov.nih.nci.caintegrator.application.study.StudyManagementService;
import gov.nih.nci.caintegrator.common.ConfigurationHelper;
import gov.nih.nci.caintegrator.common.ConfigurationParameter;
import gov.nih.nci.caintegrator.common.GenePatternUtil;
import gov.nih.nci.caintegrator.common.HibernateUtil;
import gov.nih.nci.caintegrator.domain.application.AnalysisJobStatusEnum;
import gov.nih.nci.caintegrator.domain.application.Query;
import gov.nih.nci.caintegrator.domain.application.ServerConnectionTypeEnum;
import gov.nih.nci.caintegrator.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator.domain.genomic.Sample;
import gov.nih.nci.caintegrator.web.Cai2WebUtil;
import gov.nih.nci.caintegrator.web.action.AbstractDeployedStudyAction;
import gov.nih.nci.caintegrator.web.ajax.IPersistedAnalysisJobAjaxUpdater;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

/**
 *
 */
public class GisticAnalysisAction  extends AbstractDeployedStudyAction {

    private static final long serialVersionUID = 1L;

    /**
     * Indicates action should open the analysis page.
     */
    public static final String OPEN_ACTION = "open";

    /**
     * Indicates action should execute the analysis.
     */
    public static final String EXECUTE_ACTION = "execute";

    /**
     * Indicates action should go to the status page.
     */
    public static final String STATUS_ACTION = "status";

    private AnalysisService analysisService;
    private QueryManagementService queryManagementService;
    private StudyManagementService studyManagementService;
    private IPersistedAnalysisJobAjaxUpdater ajaxUpdater;
    private String selectedAction = OPEN_ACTION;
    private String webServiceUrl;
    private String gridServiceUrl;
    private boolean useWebService = true;
    private ConfigurationHelper configurationHelper;
    private List<String> platformsInStudy = new ArrayList<String>();

    /**
     * {@inheritDoc}
     */
    @Override
    public void prepare() {
        super.prepare();
        platformsInStudy = new ArrayList<String>(
                getQueryManagementService().retrieveCopyNumberPlatformsForStudy(getStudy()));
        Collections.sort(platformsInStudy);
    }

    /**
     * @return the useWebService
     */
    public boolean getUseWebService() {
        return useWebService;
    }

    /**
     * @param useWebService the useWebService to set
     */
    public void setUseWebService(boolean useWebService) {
        this.useWebService = useWebService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String execute() {
        if (OPEN_ACTION.equals(getSelectedAction())) {
            return open();
        } else if (EXECUTE_ACTION.equals(getSelectedAction())) {
            return executeAnalysis();
        } else  {
            addActionError(getText("struts.messages.error.invalid.action", getArgs(getSelectedAction())));
            return INPUT;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate() {
        super.validate();
        if (EXECUTE_ACTION.equals(getSelectedAction())) {
            validateExecuteAnalysis();
        }
    }

    private void validateExecuteAnalysis() {
        checkName();
        checkSelectedPlatform();
        checkNegativeValue("gisticParameters.amplificationsThreshold",
                getGisticParameters().getAmplificationsThreshold());
        checkNegativeValue("gisticParameters.deletionsThreshold",
            getGisticParameters().getDeletionsThreshold());
        checkNegativeValue("gisticParameters.joinSegmentSize",
                getGisticParameters().getJoinSegmentSize());
        checkNegativeValue("gisticParameters.qvThresh",
            getGisticParameters().getQvThresh());
    }

    private boolean validateConnection() {
        if (getUseWebService()
                && !analysisService.validateGenePatternConnection(
                getCurrentGisticAnalysisJob().getGisticAnalysisForm().getGisticParameters().getServer())) {
            addActionError(getText("struts.messages.error.gistic.unable.to.connect"));
            return false;
        }
        return true;
    }

    private void checkName() {
        if (StringUtils.isBlank(getCurrentGisticAnalysisJob().getName())) {
            addFieldError("currentGisticAnalysisJob.name",
                    getText("struts.messages.error.name.required", getArgs("Job")));
        }
    }

    private void checkSelectedPlatform() {
        if (StringUtils.isBlank(getGisticAnalysisForm().getSelectedQuery())
                && isStudyHasMultiplePlatforms()
                && StringUtils.isBlank(getGisticAnalysisForm().getSelectedPlatformName())) {
            addFieldError("gisticAnalysisForm.selectedPlatformNames",
                    getText("struts.messages.error.platform.required"));
        } else if (StringUtils.isBlank(getGisticAnalysisForm().getSelectedPlatformName())) {
            getGisticAnalysisForm().setSelectedPlatformName(getPlatformsInStudy().get(0));
        }
    }

    private void checkNegativeValue(String field, Integer value) {
        if (value < 0) {
            addFieldError(field, getText("struts.messages.error.value.must.be.positive"));
        }
    }

    private void checkNegativeValue(String field, Float value) {
        if (value < 0) {
            addFieldError(field, getText("struts.messages.error.value.must.be.positive"));
        }
    }

    /**
     * @return
     */
    private String open() {
        resetCurrentGisticAnalysisJob();
        loadDefaultValues();
        return SUCCESS;
    }

    private void loadDefaultValues() {
        populateClinicalQueriesAndLists();
        getGisticAnalysisForm().setGisticParameters(new GisticParameters());
        setWebServiceUrl(getConfigurationHelper().getString(ConfigurationParameter.GENE_PATTERN_URL));
    }

    private void populateClinicalQueriesAndLists() {
        getGisticAnalysisForm().getClinicalQueries().clear();
        for (DisplayableQuery query
                : Cai2WebUtil.retrieveDisplayableQueries(getStudySubscription(), getQueryManagementService(), false)) {
            getGisticAnalysisForm().getClinicalQueries().put(query.getDisplayName(), query);
        }
    }

    private String executeAnalysis() {
        try {
            if (!loadParameters()) {
                return INPUT;
            }
            if (!validateConnection()) {
                return INPUT;
            }
            storeCnvSegmentsToIgnoreFile();
        } catch (InvalidCriterionException e) {
            addActionError(e.getMessage());
            return INPUT;
        } catch (IOException e) {
            addActionError(getText("struts.messages.error.uploading.unable.to.save"));
            return INPUT;
        }
        getCurrentGisticAnalysisJob().setCreationDate(new Date());
        getCurrentGisticAnalysisJob().setStatus(AnalysisJobStatusEnum.SUBMITTED);
        getStudySubscription().getAnalysisJobCollection()
            .add(getCurrentGisticAnalysisJob());
        getCurrentGisticAnalysisJob().setSubscription(getStudySubscription());
        getWorkspaceService().saveUserWorkspace(getWorkspace());
        ajaxUpdater.runJob(getCurrentGisticAnalysisJob());
        resetCurrentGisticAnalysisJob();
        return STATUS_ACTION;
    }

    private void storeCnvSegmentsToIgnoreFile() throws IOException {
        GisticParameters gisticParams = getCurrentGisticAnalysisJob().getGisticAnalysisForm().getGisticParameters();
        File currentCnvFile = gisticParams.getCnvSegmentsToIgnoreFile();
        if (currentCnvFile != null) {
            gisticParams
                    .setCnvSegmentsToIgnoreFile(getStudyManagementService().saveFileToStudyDirectory(
                            getCurrentStudy().getStudyConfiguration(),
                            currentCnvFile));

        }
    }

    private boolean loadParameters() throws InvalidCriterionException {
        if (getUseWebService()) {
            getGisticParameters().getServer().setUrl(getWebServiceUrl());
            getCurrentGisticAnalysisJob().setConnectionType(ServerConnectionTypeEnum.WEB);
        } else {
            getGisticParameters().getServer().setUrl(getGridServiceUrl());
            getCurrentGisticAnalysisJob().setConnectionType(ServerConnectionTypeEnum.GRID);
        }
        loadQueries();
        return loadRefgeneFileParameter();
    }

    private boolean loadRefgeneFileParameter() throws InvalidCriterionException {
        Set<Sample> samples = GenePatternUtil.getSamplesForGistic(getStudySubscription(),
                getQueryManagementService(), getGisticParameters());
        Set<String> genomeVersions = getGenomeVersions(samples);
        if (samples.isEmpty()) {
            addActionError(getText("struts.messages.error.gistic.no.samples.selected"));
            return false;
        } else if (genomeVersions.isEmpty()) {
            addActionError(getText("struts.messages.error.gistic.samples.not.copy.number"));
            return false;
        } else if (genomeVersions.size() > 1) {
            addActionError(getText("struts.messages.error.gistic.samples.have.multiple.builds"));
            return false;
        } else {
            return loadRefgeneFileParameter(genomeVersions.iterator().next());
        }
    }

    private boolean loadRefgeneFileParameter(String genomeVersion) {
        for (GisticRefgeneFileEnum refgeneFile : GisticRefgeneFileEnum.values()) {
            if (refgeneFile.getValue().toLowerCase(Locale.getDefault()).endsWith(
                    genomeVersion.toLowerCase(Locale.getDefault()))) {
                getGisticParameters().setRefgeneFile(refgeneFile);
                return true;
            }
        }
        addActionError(getText("struts.messages.error.gistic.unsupported.genome.build", getArgs(genomeVersion)));
        return false;
    }

    private Set<String> getGenomeVersions(Set<Sample> samples) {
        Set<String> genomeVersions = new HashSet<String>();
        for (Sample sample : samples) {
            for (ArrayData arrayData : sample.getArrayDatas(ReporterTypeEnum.DNA_ANALYSIS_REPORTER, null)) {
                for (ReporterList reporterList : arrayData.getReporterLists()) {
                    genomeVersions.add(reporterList.getGenomeVersion());
                }
            }
        }
        return genomeVersions;
    }

    private void loadQueries() {
        if (!StringUtils.isBlank(getGisticAnalysisForm().getSelectedQuery())) {
            getGisticParameters().setClinicalQuery(getQuery(getGisticAnalysisForm().getSelectedQuery()));
        }
    }

    private Query getQuery(String displayableQueryName) {
        DisplayableQuery displayableQuery = getGisticAnalysisForm().getClinicalQueries().
            get(displayableQueryName);
        if (displayableQuery == null) {
            return null;
        }
        Query query = displayableQuery.getQuery();
        if (!query.isSubjectListQuery()) {
            query = getQueryManagementService().getRefreshedEntity(query);
        } else {
            query.setSubscription(getQueryManagementService().getRefreshedEntity(query.getSubscription()));
        }
        HibernateUtil.loadCollection(query);
        return query;
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
    public void setAnalysisService(AnalysisService analysisService) {
        this.analysisService = analysisService;
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
    public void setQueryManagementService(QueryManagementService queryManagementService) {
        this.queryManagementService = queryManagementService;
    }

    /**
     * @return the ajaxUpdater
     */
    public IPersistedAnalysisJobAjaxUpdater getAjaxUpdater() {
        return ajaxUpdater;
    }

    /**
     * @param ajaxUpdater the ajaxUpdater to set
     */
    public void setAjaxUpdater(IPersistedAnalysisJobAjaxUpdater ajaxUpdater) {
        this.ajaxUpdater = ajaxUpdater;
    }

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
     * @return GisticParameters.
     */
    public GisticParameters getGisticParameters() {
        return getGisticAnalysisForm().getGisticParameters();
    }

    /**
     * @return available Gistic services.
     */
    public Map<String, String> getGisticServices() {
        return GridDiscoveryServiceJob.getGridGisticServices();
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
    public void setStudyManagementService(StudyManagementService studyManagementService) {
        this.studyManagementService = studyManagementService;
    }

    /**
     * @return the webServiceUrl
     */
    public String getWebServiceUrl() {
        return webServiceUrl;
    }

    /**
     * @param webServiceUrl the webServiceUrl to set
     */
    public void setWebServiceUrl(String webServiceUrl) {
        this.webServiceUrl = webServiceUrl;
    }

    /**
     * @return the gridServiceUrl
     */
    public String getGridServiceUrl() {
        return gridServiceUrl;
    }

    /**
     * @param gridServiceUrl the gridServiceUrl to set
     */
    public void setGridServiceUrl(String gridServiceUrl) {
        this.gridServiceUrl = gridServiceUrl;
    }

    /**
     * @return the configurationHelper
     */
    public ConfigurationHelper getConfigurationHelper() {
        return configurationHelper;
    }

    /**
     * @param configurationHelper the configurationHelper to set
     */
    public void setConfigurationHelper(ConfigurationHelper configurationHelper) {
        this.configurationHelper = configurationHelper;
    }

    /**
     * Display web service parameters.
     * @return whether to display the web service parameters.
     */
    public String getUseWebServiceOn() {
        return (useWebService) ? "display: block;" : "display: none;";
    }

    /**
     * @return the platformsInStudy
     */
    public List<String> getPlatformsInStudy() {
        return platformsInStudy;
    }

    /**
     * @param platformsInStudy the platformsInStudy to set
     */
    public void setPlatformsInStudy(List<String> platformsInStudy) {
        this.platformsInStudy = platformsInStudy;
    }

    /**
     * Determines if study has multiple platforms.
     * @return T/F value if study has multiple platforms.
     */
    public boolean isStudyHasMultiplePlatforms() {
        return platformsInStudy.size() > 1;
    }
}
