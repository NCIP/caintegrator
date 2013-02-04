/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.analysis;

import edu.mit.broad.genepattern.gp.services.GenePatternServiceException;
import gov.nih.nci.caintegrator2.application.analysis.AnalysisMethod;
import gov.nih.nci.caintegrator2.application.analysis.AnalysisService;
import gov.nih.nci.caintegrator2.application.query.InvalidCriterionException;
import gov.nih.nci.caintegrator2.application.query.QueryManagementService;
import gov.nih.nci.caintegrator2.common.ConfigurationHelper;
import gov.nih.nci.caintegrator2.common.ConfigurationParameter;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.application.AnalysisJobStatusEnum;
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.ResultTypeEnum;
import gov.nih.nci.caintegrator2.web.action.AbstractDeployedStudyAction;
import gov.nih.nci.caintegrator2.web.ajax.IPersistedAnalysisJobAjaxUpdater;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

/**
 * Action to configure and run GenePattern analysis jobs.
 */
public class GenePatternAnalysisAction extends AbstractDeployedStudyAction {
    
    private static final long serialVersionUID = 1L;
    private String analysisType = "genePatternModules";

    /**
     * Indicates action should open the analysis page.
     */
    public static final String OPEN_ACTION = "open";

    /**
     * Indicates action should connect to GenePattern.
     */
    public static final String CONNECT_ACTION = "connect";
    
    /**
     * Indicates user has changed the analysis method.
     */
    public static final String CHANGE_METHOD_ACTION = "change";

    /**
     * Indicates action should execute the analysis.
     */
    public static final String EXECUTE_ACTION = "execute";
    
    /**
     * Indicates action should go to the status page.
     */
    public static final String STATUS_ACTION = "status";

    private Long jobId;
    private AnalysisService analysisService;
    private QueryManagementService queryManagementService;
    private ConfigurationHelper configurationHelper;
    private IPersistedAnalysisJobAjaxUpdater ajaxUpdater;
    private String selectedAction = OPEN_ACTION;
    private String analysisMethodName;

    /**
     * {@inheritDoc}
     */
    @Override
    public String execute() {
        if (OPEN_ACTION.equals(getSelectedAction())) {
            return open();
        } else if (CONNECT_ACTION.equals(getSelectedAction())) {
            return connect();
        } else if (CHANGE_METHOD_ACTION.equals(getSelectedAction())) {
            return changeMethod();
        } else if (EXECUTE_ACTION.equals(getSelectedAction())) {
            return executeAnalysis();
        } else  {
            addActionError("Invalid action: " + getSelectedAction());
            return INPUT;
        }
    }
    
    /**
     * Cancel action.
     * @return struts result.
     */
    public String cancel() {
       return SUCCESS; 
    }
    
    /**
     * Opens to the status page.
     * @return Status string.
     */
    public String showStatusPage() {
        return STATUS_ACTION;
    }
    
    /**
     * Delete a job.
     * @return Status string.
     */
    public String deleteAnalysisJob() {
        getAnalysisService().deleteAnalysisJob(jobId);
        return SUCCESS;
    }
    
    /**
     * @return
     */
    private String open() {
        resetCurrentGenePatternAnalysisJob();
        getGenePatternAnalysisForm().setUrl(configurationHelper.getString(ConfigurationParameter.GENE_PATTERN_URL));
        getGenePatternAnalysisForm().setGenomicQueries(getGenomicQueries());
        Collection<AnnotationDefinition> subjectClassifications = 
            getClassificationAnnotations(getStudy().getStudyConfiguration()
                    .getVisibleSubjectAnnotationCollection());
        Collection<AnnotationDefinition> imageSeriesClassifications = 
            getClassificationAnnotations(getStudy().getStudyConfiguration()
                    .getVisibleImageSeriesAnnotationCollection());
        Collection<AnnotationDefinition> sampleClassifications = 
            getClassificationAnnotations(getStudy().getSampleAnnotationCollection());
        getGenePatternAnalysisForm().clearClassificationAnnotations();
        getGenePatternAnalysisForm().addClassificationAnnotations(subjectClassifications,
                EntityTypeEnum.SUBJECT);
        getGenePatternAnalysisForm().addClassificationAnnotations(imageSeriesClassifications,
                EntityTypeEnum.IMAGESERIES);
        getGenePatternAnalysisForm().addClassificationAnnotations(sampleClassifications,
                EntityTypeEnum.SAMPLE);
        setAnalysisMethodName(getGenePatternAnalysisForm().getAnalysisMethodName());
        return SUCCESS;
    }

    private Collection<AnnotationDefinition> getClassificationAnnotations(
            Collection<AnnotationDefinition> annotationCollection) {
        Set<AnnotationDefinition> classificationAnnotations = new HashSet<AnnotationDefinition>();
        for (AnnotationDefinition annotation : annotationCollection) {
            if (annotation.getAnnotationValueCollection() != null 
                    && !annotation.getAnnotationValueCollection().isEmpty()) {
                classificationAnnotations.add(annotation);
            }
        }
        return classificationAnnotations;
    }

    /**
     * @return
     */
    private List<Query> getGenomicQueries() {
        List<Query> queries = new ArrayList<Query>();
        for (Query query : getStudySubscription().getQueryCollection()) {
            if (ResultTypeEnum.GENOMIC.equals(query.getResultType())) {
                queries.add(query);
            }
        }
        return queries;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate() {
        super.validate();
        validateStudyHasGenomicData("GenePattern Analysis");
        if (CONNECT_ACTION.equals(getSelectedAction())) {
            validateConnect();
        } else if (EXECUTE_ACTION.equals(getSelectedAction())) {
            validateExecuteAnalysis();
        }
    }
    
    private void validateConnect() {
        if (StringUtils.isEmpty(getGenePatternAnalysisForm().getUrl())) {
            addFieldError("analysisForm.url", "URL is required");
        } else {
            validateUrl();
        }
        if (StringUtils.isEmpty(getGenePatternAnalysisForm().getUsername())) {
            addFieldError("analysisForm.username", "Username is required");
        }
    }

    private void validateUrl() {
        try {
            new URL(getGenePatternAnalysisForm().getUrl());
        } catch (MalformedURLException e) {
            addFieldError("analysisForm.url", "Invalid URL format");
        }
    }

    private void validateExecuteAnalysis() {
        if (StringUtils.isBlank(getCurrentGenePatternAnalysisJob().getName())) {
            addFieldError("currentAnalysisJob.name", "Job name required.");
        }
        getGenePatternAnalysisForm().validate(this);
    }

    /**
     * @return
     */
    private String changeMethod() {
        if (!getAnalysisMethodName().equals(getGenePatternAnalysisForm().getAnalysisMethodName())) {
            getGenePatternAnalysisForm().setAnalysisMethodName(getAnalysisMethodName());
        }
        return SUCCESS;
    }

    private String connect() {
        try {
            if (!StringUtils.isBlank(getGenePatternAnalysisForm().getServer().getUrl())) {
                getGenePatternAnalysisForm().setAnalysisMethods(
                        getAnalysisService().getGenePatternMethods(getGenePatternAnalysisForm().getServer()));
                setAnalysisMethodName(getGenePatternAnalysisForm().getAnalysisMethodName());
            }
            return SUCCESS;
        } catch (GenePatternServiceException e) { 
            getGenePatternAnalysisForm().setAnalysisMethods(new ArrayList<AnalysisMethod>());
            addActionError("Couldn't retrieve GenePattern analysis method information: " + e.getMessage());
            return ERROR;
        }
    }
    
    private String executeAnalysis() {
        try {
            configureInvocationParameters();
        } catch (InvalidCriterionException e) {
            addActionError(e.getMessage());
            return ERROR;
        }
        getCurrentGenePatternAnalysisJob().setCreationDate(new Date());
        getCurrentGenePatternAnalysisJob().setStatus(AnalysisJobStatusEnum.SUBMITTED);
        getStudySubscription().getAnalysisJobCollection().add(getCurrentGenePatternAnalysisJob());
        getCurrentGenePatternAnalysisJob().setSubscription(getStudySubscription());
        getWorkspaceService().saveUserWorkspace(getWorkspace());
        ajaxUpdater.runJob(getCurrentGenePatternAnalysisJob());
        return STATUS_ACTION;
    }

    private void configureInvocationParameters() throws InvalidCriterionException {
        for (AbstractAnalysisFormParameter formParameter : getGenePatternAnalysisForm().getParameters()) {
            formParameter.configureForInvocation(getStudySubscription(), getQueryManagementService());
        }
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
     * @return the analysisMethodName
     */
    public String getAnalysisMethodName() {
        return analysisMethodName;
    }

    /**
     * @param analysisMethodName the analysisMethodName to set
     */
    public void setAnalysisMethodName(String analysisMethodName) {
        this.analysisMethodName = analysisMethodName;
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
     * @return the jobId
     */
    public Long getJobId() {
        return jobId;
    }

    /**
     * @param jobId the jobId to set
     */
    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }
    
    /**
     * @return a list if available analysis types for this study.
     */
    public Map<String, String> getAnalysisTypes() {
        Map<String, String> types = new LinkedHashMap<String, String>();
        types.put("genePatternModules", "Gene Pattern Modules");
        types.put("principalComponentAnalysis", "Principal Component Analysis (Grid Service)");
        if (getCurrentStudy().hasExpressionData()) {
            types.put("comparativeMarkerSelection", "Comparative Marker Selection (Grid Service)");
        }
        if (getCurrentStudy().hasCopyNumberData()) {
            types.put("gistic", "GISTIC (Grid Service)");
        }
        return types;
    }

    /**
     * @return the analysisType
     */
    public String getAnalysisType() {
        return analysisType;
    }

    /**
     * @param analysisType the analysisType to set
     */
    public void setAnalysisType(String analysisType) {
        this.analysisType = analysisType;
    }

}
