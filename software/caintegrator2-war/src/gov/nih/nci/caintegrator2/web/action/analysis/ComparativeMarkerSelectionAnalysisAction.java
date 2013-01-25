/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.analysis;

import gov.nih.nci.caintegrator2.application.analysis.AnalysisService;
import gov.nih.nci.caintegrator2.application.analysis.grid.GridDiscoveryServiceJob;
import gov.nih.nci.caintegrator2.application.analysis.grid.comparativemarker.ComparativeMarkerSelectionParameters;
import gov.nih.nci.caintegrator2.application.analysis.grid.preprocess.PreprocessDatasetParameters;
import gov.nih.nci.caintegrator2.application.query.InvalidCriterionException;
import gov.nih.nci.caintegrator2.application.query.QueryManagementService;
import gov.nih.nci.caintegrator2.common.HibernateUtil;
import gov.nih.nci.caintegrator2.domain.application.AnalysisJobStatusEnum;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.QueryResult;
import gov.nih.nci.caintegrator2.domain.application.ResultRow;
import gov.nih.nci.caintegrator2.external.ServerConnectionProfile;
import gov.nih.nci.caintegrator2.web.Cai2WebUtil;
import gov.nih.nci.caintegrator2.web.action.AbstractDeployedStudyAction;
import gov.nih.nci.caintegrator2.web.ajax.IPersistedAnalysisJobAjaxUpdater;
import gridextensions.ComparativeMarkerSelectionParameterSet;
import gridextensions.PreprocessDatasetParameterSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * 
 */
public class ComparativeMarkerSelectionAnalysisAction  extends AbstractDeployedStudyAction {
    
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
    public String execute() {
        if (OPEN_ACTION.equals(getSelectedAction())) {
            return open();
        } else if (EXECUTE_ACTION.equals(getSelectedAction())) {
            return executeAnalysis();
        } else  {
            addActionError("Invalid action: " + getSelectedAction());
            return INPUT;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate() {
        super.validate();
        validateStudyHasGenomicData("Comparative Marker Selection Analysis");
        
        if (EXECUTE_ACTION.equals(getSelectedAction())) {
            validateExecuteAnalysis();
        }
    }

    private void validateExecuteAnalysis() {
        if (StringUtils.isBlank(getCurrentComparativeMarkerSelectionAnalysisJob().getName())) {
            addFieldError("currentComparativeMarkerSelectionAnalysisJob.name", 
                    getText("struts.messages.error.name.required", getArgs("Job")));
        }
        if (getComparativeMarkerSelectionAnalysisForm().getSelectedQueryNames().size() != 2) {
            addFieldError("comparativeMarkerSelectionAnalysisForm.unselectedQueryNames", 
                    getText("struts.messages.error.two.queries.required"));
        }
        if (isStudyHasMultiplePlatforms() 
                && StringUtils.isBlank(getComparativeMarkerSelectionAnalysisForm().getPlatformName())) {
            addFieldError("comparativeMarkerSelectionAnalysisForm.platformName", 
                    getText("struts.messages.error.name.required", getArgs("Platform")));
        }
    }
    
    /**
     * @return
     */
    private String open() {
        resetCurrentComparativeMarkerSelectionAnalysisJob();
        loadDefaultValues();
        return SUCCESS;
    }
    
    private void loadDefaultValues() {
        populateClinicalQueriesAndLists();
        getComparativeMarkerSelectionAnalysisForm().setPreprocessDatasetparameters(new PreprocessDatasetParameters());
        getComparativeMarkerSelectionAnalysisForm().setComparativeMarkerSelectionParameters(
                new ComparativeMarkerSelectionParameters());

        String fileName = "CMS-" + System.currentTimeMillis();
        getPreprocessDatasetParameters().setProcessedGctFilename(fileName + ".gct");
        getComparativeMarkerSelectionParameters().setClassificationFileName(fileName + ".cls");
    }

    private void populateClinicalQueriesAndLists() {
        getComparativeMarkerSelectionAnalysisForm().setDisplayableQueries(
                Cai2WebUtil.retrieveDisplayableQueries(getStudySubscription(), getQueryManagementService(), false));
        getComparativeMarkerSelectionAnalysisForm().getDisplayableQueryMap().clear();
        for (DisplayableQuery displayableQuery : getComparativeMarkerSelectionAnalysisForm().getDisplayableQueries()) {
            getComparativeMarkerSelectionAnalysisForm().getDisplayableQueryMap().put(displayableQuery.getDisplayName(),
                    displayableQuery);
        }
        getComparativeMarkerSelectionAnalysisForm().getUnselectedQueries().clear();
        for (DisplayableQuery query : getComparativeMarkerSelectionAnalysisForm().getDisplayableQueries()) {
            getComparativeMarkerSelectionAnalysisForm().getUnselectedQueries().put(query.getDisplayName(), query);
        }
    }
    
    private String executeAnalysis() {
        try {
            if (!loadParameters()) {
                return INPUT;
            }
        } catch (InvalidCriterionException e) {
            addActionError(e.getMessage());
            return INPUT;
        }
        getCurrentComparativeMarkerSelectionAnalysisJob().setCreationDate(new Date());
        getCurrentComparativeMarkerSelectionAnalysisJob().setStatus(AnalysisJobStatusEnum.SUBMITTED);
        getStudySubscription().getAnalysisJobCollection()
            .add(getCurrentComparativeMarkerSelectionAnalysisJob());
        getCurrentComparativeMarkerSelectionAnalysisJob().setSubscription(getStudySubscription());
        getWorkspaceService().saveUserWorkspace(getWorkspace());
        ajaxUpdater.runJob(getCurrentComparativeMarkerSelectionAnalysisJob());
        resetCurrentComparativeMarkerSelectionAnalysisJob();
        return STATUS_ACTION;
    }
    
    private boolean loadParameters() throws InvalidCriterionException {
        loadPlatforms();
        loadServers();
        return loadQueries();
    }
    
    private void loadPlatforms() {
        getComparativeMarkerSelectionAnalysisForm().getPreprocessDatasetparameters().setPlatformName(
                getComparativeMarkerSelectionAnalysisForm().getPlatformName());
    }
    
    private void loadServers() {
        ServerConnectionProfile server = new ServerConnectionProfile();
        server.setUrl(getCurrentComparativeMarkerSelectionAnalysisJob().getPreprocessDataSetUrl());
        getPreprocessDatasetParameters().setServer(server);
        server = new ServerConnectionProfile();
        server.setUrl(getCurrentComparativeMarkerSelectionAnalysisJob().getComparativeMarkerSelectionUrl());
        getComparativeMarkerSelectionParameters().setServer(server);
    }
    
    private boolean loadQueries() throws InvalidCriterionException {
        if (!getComparativeMarkerSelectionAnalysisForm().getSelectedQueryNames().isEmpty()) {
            getPreprocessDatasetParameters().getClinicalQueries().clear();
            getComparativeMarkerSelectionParameters().getClinicalQueries().clear();
            for (String name : getComparativeMarkerSelectionAnalysisForm().getSelectedQueryNames()) {
                Query currentQuery = getQuery(name);
                if (!validateQuerySampleCount(currentQuery)) {
                    return false;
                }
                getPreprocessDatasetParameters().getClinicalQueries().add(currentQuery);
                getComparativeMarkerSelectionParameters().getClinicalQueries().add(currentQuery);
            }
        }
        return true;
    }

    private boolean validateQuerySampleCount(Query currentQuery) throws InvalidCriterionException {
        int numSamples = 0;
        QueryResult results = queryManagementService.execute(currentQuery);
        for (ResultRow row : results.getRowCollection()) {
            if (!row.getSubjectAssignment().getSampleAcquisitionCollection().isEmpty()) {
                numSamples++;
            }
            if (numSamples >= 2) {
                break;
            }
        }
        return verifySampleCounts(currentQuery, numSamples);
    }

    private boolean verifySampleCounts(Query currentQuery, int numSamples) {
        if (numSamples < 2) {
            String invalidString = 
                "'" + currentQuery.getName() 
                    + "' is invalid because it contains less than 2 samples.";
            addActionError(currentQuery.isSubjectListQuery() ? "Subject List " + invalidString 
                    : "Query " + invalidString);
            return false;
        }
        return true;
    }
    
    private Query getQuery(String displayableQueryName) {
        Query query = getComparativeMarkerSelectionAnalysisForm().getDisplayableQueryMap().
            get(displayableQueryName).getQuery();
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
     * @return PreprocessDatasetParameters.
     */
    public PreprocessDatasetParameters getPreprocessDatasetParameters() {
        return getComparativeMarkerSelectionAnalysisForm().getPreprocessDatasetparameters();
    }

    /**
     * @return ComparativeMarkerSelectionParameters.
     */
    public ComparativeMarkerSelectionParameters getComparativeMarkerSelectionParameters() {
        return getComparativeMarkerSelectionAnalysisForm().getComparativeMarkerSelectionParameters();
    }
    
    /**
     * @return PreprocessDatasetParameters.
     */
    public PreprocessDatasetParameterSet getPreprocessDatasetParameterSet() {
        return getPreprocessDatasetParameters().getDatasetParameters();
    }

    /**
     * @return ComparativeMarkerSelectionParameters.
     */
    public ComparativeMarkerSelectionParameterSet getComparativeMarkerSelectionParameterSet() {
        return getComparativeMarkerSelectionParameters().getDatasetParameters();
    }

    /**
     * @return available PreprocessDataset services.
     */
    public Map<String, String> getPreprocessDatasetServices() {
        return GridDiscoveryServiceJob.getGridPreprocessServices();
    }

    /**
     * @return available ComparativeMarkerSelection services.
     */
    public Map<String, String> getComparativeMarkerSelectionServices() {
        return GridDiscoveryServiceJob.getGridCmsServices();
    }
    
    
    /**
     * Determines if study has multiple platforms.
     * @return T/F value if study has multiple platforms.
     */
    public boolean isStudyHasMultiplePlatforms() {
        return platformsInStudy.size() > 1;
    }

    /**
     * @return the platformsInStudy
     */
    public List<String> getPlatformsInStudy() {
        return platformsInStudy;
    }
}
