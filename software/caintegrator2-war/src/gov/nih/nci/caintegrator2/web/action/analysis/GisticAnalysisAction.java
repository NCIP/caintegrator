/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.analysis;

import gov.nih.nci.caintegrator2.application.analysis.AnalysisService;
import gov.nih.nci.caintegrator2.application.analysis.grid.GridDiscoveryServiceJob;
import gov.nih.nci.caintegrator2.application.analysis.grid.gistic.GisticParameters;
import gov.nih.nci.caintegrator2.application.analysis.grid.gistic.GisticRefgeneFileEnum;
import gov.nih.nci.caintegrator2.application.query.InvalidCriterionException;
import gov.nih.nci.caintegrator2.application.query.QueryManagementService;
import gov.nih.nci.caintegrator2.application.study.StudyManagementService;
import gov.nih.nci.caintegrator2.common.Cai2Util;
import gov.nih.nci.caintegrator2.common.ConfigurationHelper;
import gov.nih.nci.caintegrator2.common.ConfigurationParameter;
import gov.nih.nci.caintegrator2.common.GenePatternUtil;
import gov.nih.nci.caintegrator2.common.HibernateUtil;
import gov.nih.nci.caintegrator2.domain.application.AnalysisJobStatusEnum;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.ResultTypeEnum;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.domain.genomic.Sample;
import gov.nih.nci.caintegrator2.web.action.AbstractDeployedStudyAction;
import gov.nih.nci.caintegrator2.web.ajax.IPersistedAnalysisJobAjaxUpdater;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

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
        if (EXECUTE_ACTION.equals(getSelectedAction())) {
            validateExecuteAnalysis();
        }
    }

    private void validateExecuteAnalysis() {
        if (StringUtils.isBlank(getCurrentGisticAnalysisJob().getName())) {
            addFieldError("currentGisticAnalysisJob.name", "Job name required.");
        }
        checkNegativeValue("gisticParameters.amplificationsThreshold",
                getGisticParameters().getAmplificationsThreshold());
        checkNegativeValue("gisticParameters.deletionsThreshold",
            getGisticParameters().getDeletionsThreshold());
        checkNegativeValue("gisticParameters.joinSegmentSize",
                getGisticParameters().getJoinSegmentSize());
        checkNegativeValue("gisticParameters.qvThresh",
            getGisticParameters().getQvThresh());
    }

    private void checkNegativeValue(String field, Integer value) {
        if (value < 0) {
            addFieldError(field, "Value must be positive.");
        }
    }
    
    private void checkNegativeValue(String field, Float value) {
        if (value < 0) {
            addFieldError(field, "Value must be positive.");
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
        getGisticAnalysisForm().setClinicalQueries(new HashMap<String, String>());
        addClinnicalQueries();
        getGisticAnalysisForm().setGisticParameters(new GisticParameters());
        setWebServiceUrl(getConfigurationHelper().getString(ConfigurationParameter.GENE_PATTERN_URL));
    }

    private void addClinnicalQueries() {
        for (Query query 
                : getStudySubscription().getQueryCollection()) {
            if (ResultTypeEnum.CLINICAL.equals(query.getResultType()) 
                && !Cai2Util.isCompoundCriterionGenomic(query.getCompoundCriterion())) {
                getGisticAnalysisForm().getClinicalQueries().put(query.getId().toString(), query.getName());
            }
        }
    }
    
    private String executeAnalysis() {
        try {
            if (!loadParameters()) {
                return INPUT;
            }
            storeCnvSegmentsToIgnoreFile();
        } catch (InvalidCriterionException e) {
            addActionError(e.getMessage());
            return INPUT;
        } catch (IOException e) {
            addActionError("Unable to save uploaded CNV ignore file.");
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
        } else {
            getGisticParameters().getServer().setUrl(getGridServiceUrl());
        }
        loadQueries();
        return loadRefgeneFileParameter();
    }

    private boolean loadRefgeneFileParameter() throws InvalidCriterionException {
        Set<Sample> samples = GenePatternUtil.getSamplesForGistic(getStudySubscription(), 
                getQueryManagementService(), getGisticParameters());
        Set<String> genomeVersions = getGenomeVersions(samples);
        if (samples.isEmpty()) {
            addActionError("There are no samples selected.");
            return false;
        } else if (genomeVersions.isEmpty()) {
            addActionError("The samples selected are not related to any copy number data.");
            return false;
        } else if (genomeVersions.size() > 1) {
            addActionError("The samples selected have copy number data loaded for multiple genome build versions.");
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
        addActionError("Copy number data is related to an unsupported genome build for GISTIC analysis. "
                + "Valid values are Hg16, Hg17 or Hg18. Data was loaded for array annoted with genome build " 
                + genomeVersion);
        return false;
    }

    private Set<String> getGenomeVersions(Set<Sample> samples) {
        Set<String> genomeVersions = new HashSet<String>();
        for (Sample sample : samples) {
            for (ArrayData arrayData : sample.getArrayDatas(ReporterTypeEnum.DNA_ANALYSIS_REPORTER)) {
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
    
    private Query getQuery(String id) {
        for (Query query 
                : getStudySubscription().getQueryCollection()) {
            if (id.equals(query.getId().toString())) {
                HibernateUtil.loadCollection(query);
                return query;
            }
        }
        return null;
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
}
