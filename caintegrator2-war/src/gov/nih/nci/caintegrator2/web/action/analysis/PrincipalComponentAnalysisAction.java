/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.analysis;

import gov.nih.nci.caintegrator2.application.analysis.AnalysisService;
import gov.nih.nci.caintegrator2.application.analysis.grid.GridDiscoveryServiceJob;
import gov.nih.nci.caintegrator2.application.analysis.grid.pca.PCAParameters;
import gov.nih.nci.caintegrator2.application.analysis.grid.preprocess.PreprocessDatasetParameters;
import gov.nih.nci.caintegrator2.application.query.QueryManagementService;
import gov.nih.nci.caintegrator2.common.HibernateUtil;
import gov.nih.nci.caintegrator2.domain.application.AnalysisJobStatusEnum;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.external.ServerConnectionProfile;
import gov.nih.nci.caintegrator2.web.Cai2WebUtil;
import gov.nih.nci.caintegrator2.web.action.AbstractDeployedStudyAction;
import gov.nih.nci.caintegrator2.web.ajax.IPersistedAnalysisJobAjaxUpdater;
import gridextensions.PreprocessDatasetParameterSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * Action that deals with Principal Component Analysis.
 */
public class PrincipalComponentAnalysisAction  extends AbstractDeployedStudyAction {

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

    /**
     * Indicates action should update the controls.
     */
    public static final String UPDATE_CONTROLS_ACTION = "updateControls";

    private AnalysisService analysisService;
    private QueryManagementService queryManagementService;
    private IPersistedAnalysisJobAjaxUpdater ajaxUpdater;
    private String selectedAction = OPEN_ACTION;
    private List<String> platformsInStudy = new ArrayList<String>();

    /**
     * {@inheritDoc}
     */
    @Override
    public void prepare() {
        super.prepare();
        platformsInStudy = new ArrayList<String>(
                getQueryManagementService().retrieveGeneExpressionPlatformsForStudy(getStudy()));
        Collections.sort(platformsInStudy);
    }

    /**
     * Cancel action.
     * @return struts result.
     */
    public String cancel() {
       return SUCCESS;
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
        } else if (UPDATE_CONTROLS_ACTION.equals(getSelectedAction())) {
            return updateControlSampleSets();
        } else  {
            addActionError(getText("struts.messages.error.invalid.action", getArgs(getSelectedAction())));
            return INPUT;
        }
    }

    private String updateControlSampleSets() {
        getPrincipalComponentAnalysisForm().getControlSampleSets().clear();
        if (StringUtils.isBlank(getPrincipalComponentAnalysisForm().getPlatformName())) {
            addActionError(getText("struts.messages.error.select.valid.platform"));
            return INPUT;
        }
        getPrincipalComponentAnalysisForm().setControlSampleSets(
                getStudy().getStudyConfiguration().getControlSampleSetNames(
                        getPrincipalComponentAnalysisForm().getPlatformName()));
        return INPUT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate() {
        super.validate();
        validateStudyHasGenomicData("Principal Component Analysis");

        if (EXECUTE_ACTION.equals(getSelectedAction())) {
            validateExecuteAnalysis();
        }
    }

    private void validateExecuteAnalysis() {
        if (StringUtils.isBlank(getCurrentPrincipalComponentAnalysisJob().getName())) {
            addFieldError("currentPrincipalComponentAnalysisJob.name",
                    getText("struts.messages.error.name.required", getArgs("Job")));
        }
        if (isStudyHasMultiplePlatforms()
                && StringUtils.isBlank(getPrincipalComponentAnalysisForm().getPlatformName())) {
            addFieldError("principalComponentAnalysisForm.platformName",
                    getText("struts.messages.error.name.required", getArgs("Platform")));
        }
    }

    /**
     * @return
     */
    private String open() {
        resetCurrentPrincipalComponentAnalysisJob();
        loadDefaultValues();
        return SUCCESS;
    }

    private void loadDefaultValues() {
        populateClinicalQueriesAndLists();
        getPrincipalComponentAnalysisForm().setPreprocessParameters(new PreprocessDatasetParameters());
        getPrincipalComponentAnalysisForm().setPcaParameters(new PCAParameters());

        String fileName = "PCA-" + System.currentTimeMillis();
        getPreprocessDatasetParameters().setProcessedGctFilename("PROCESSED-" + fileName + ".gct");
        getPcaParameters().setGctFileName(fileName + ".gct");
        getPcaParameters().setClassificationFileName(fileName + ".cls");
        getPcaParameters().setClusterBy(getPcaParameters().getClusterByOptions().get(1));
    }

    private void populateClinicalQueriesAndLists() {
        getPrincipalComponentAnalysisForm().getQueries().clear();
        for (DisplayableQuery query
                : Cai2WebUtil.retrieveDisplayableQueries(getStudySubscription(), getQueryManagementService(), false)) {
            getPrincipalComponentAnalysisForm().getQueries().put(query.getDisplayName(), query);
        }
    }

    private String executeAnalysis() {
        loadParameters();
        getCurrentPrincipalComponentAnalysisJob().setCreationDate(new Date());
        getCurrentPrincipalComponentAnalysisJob().setStatus(AnalysisJobStatusEnum.SUBMITTED);
        getStudySubscription().getAnalysisJobCollection()
            .add(getCurrentPrincipalComponentAnalysisJob());
        getCurrentPrincipalComponentAnalysisJob().setSubscription(getStudySubscription());
        if (!getCurrentPrincipalComponentAnalysisJob().getForm().isUsePreprocessDataset()) {
            getCurrentPrincipalComponentAnalysisJob().setPreprocessDataSetUrl(null);
        }
        getWorkspaceService().saveUserWorkspace(getWorkspace());
        ajaxUpdater.runJob(getCurrentPrincipalComponentAnalysisJob());
        resetCurrentPrincipalComponentAnalysisJob();
        return STATUS_ACTION;
    }

    private void loadParameters() {
        loadPlatforms();
        loadServers();
        loadQueries();
        loadExclude();
    }

    private void loadPlatforms() {
        getPrincipalComponentAnalysisForm().getPcaParameters().setPlatformName(
                getPrincipalComponentAnalysisForm().getPlatformName());
        getPrincipalComponentAnalysisForm().getPreprocessParameters().setPlatformName(
                getPrincipalComponentAnalysisForm().getPlatformName());
    }

    private void loadServers() {
        ServerConnectionProfile server = new ServerConnectionProfile();
        server.setUrl(getCurrentPrincipalComponentAnalysisJob().getPcaUrl());
        getPcaParameters().setServer(server);
        server = new ServerConnectionProfile();
        server.setUrl(getCurrentPrincipalComponentAnalysisJob().getPreprocessDataSetUrl());
        getPreprocessDatasetParameters().setServer(server);
    }

    private void loadQueries() {
        getPreprocessDatasetParameters().getClinicalQueries().clear();
        getPcaParameters().getClinicalQueries().clear();
        if (!StringUtils.isEmpty(getPrincipalComponentAnalysisForm().getSelectedQueryName())) {
            Query currentQuery = getQuery(getPrincipalComponentAnalysisForm().getSelectedQueryName());
            getPcaParameters().getClinicalQueries().add(currentQuery);
            getPreprocessDatasetParameters().getClinicalQueries().add(currentQuery);
        }
    }

    private void loadExclude() {
        String excludeControlSet = getPrincipalComponentAnalysisForm().getExcludeControlSampleSetName();
        if (!StringUtils.isEmpty(excludeControlSet)) {
            getPreprocessDatasetParameters().setExcludedControlSampleSet(
                    getCurrentStudy().getStudyConfiguration().getControlSampleSet(excludeControlSet));
            getPcaParameters().setExcludedControlSampleSet(
                    getPreprocessDatasetParameters().getExcludedControlSampleSet());
        }
    }

    private Query getQuery(String displayableQueryName) {
        DisplayableQuery displayableQuery = getPrincipalComponentAnalysisForm().getQueries().
            get(displayableQueryName);
        if (displayableQuery == null) {
            return null;
        }
        Query query = displayableQuery.getQuery();
        if (!query.isSubjectListQuery()) {
            query = getQueryManagementService().getRefreshedEntity(query);
            HibernateUtil.loadCollection(query);
        }
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
     *
     * @return the pcaParameters.
     */
    public PCAParameters getPcaParameters() {
        return getPrincipalComponentAnalysisForm().getPcaParameters();
    }

    /**
     * @return available PCA services.
     */
    public Map<String, String> getPcaServices() {
        return GridDiscoveryServiceJob.getGridPcaServices();
    }

    /**
     * @return available PreprocessDataset services.
     */
    public Map<String, String> getPreprocessDatasetServices() {
        return GridDiscoveryServiceJob.getGridPreprocessServices();
    }

    /**
     * @return PreprocessDatasetParameters.
     */
    public PreprocessDatasetParameterSet getPreprocessDatasetParameterSet() {
        return getPreprocessDatasetParameters().getDatasetParameters();
    }


    /**
     * @return PreprocessDatasetParameters.
     */
    public PreprocessDatasetParameters getPreprocessDatasetParameters() {
        return getPrincipalComponentAnalysisForm().getPreprocessParameters();
    }

    /**
     * Determines if study has multiple platforms.
     * @return T/F value if study has multiple platforms.
     */
    public boolean isStudyHasMultiplePlatforms() {
        return platformsInStudy.size() > 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getControlSampleSets() {
        return isStudyHasMultiplePlatforms() ? getPrincipalComponentAnalysisForm().getControlSampleSets()
                : super.getControlSampleSets();
    }

    /**
     * @return the platformsInStudy
     */
    public List<String> getPlatformsInStudy() {
        return platformsInStudy;
    }
}
