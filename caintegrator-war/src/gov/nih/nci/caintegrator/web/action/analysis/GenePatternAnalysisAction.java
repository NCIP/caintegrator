/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.action.analysis;

import gov.nih.nci.caintegrator.application.analysis.AnalysisMethod;
import gov.nih.nci.caintegrator.application.analysis.AnalysisService;
import gov.nih.nci.caintegrator.application.query.QueryManagementService;
import gov.nih.nci.caintegrator.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator.application.study.AuthorizedGenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator.application.study.AuthorizedStudyElementsGroup;
import gov.nih.nci.caintegrator.application.study.StudyManagementService;
import gov.nih.nci.caintegrator.common.ConfigurationHelper;
import gov.nih.nci.caintegrator.common.ConfigurationParameter;
import gov.nih.nci.caintegrator.domain.application.AnalysisJobStatusEnum;
import gov.nih.nci.caintegrator.domain.application.Query;
import gov.nih.nci.caintegrator.domain.application.ResultTypeEnum;
import gov.nih.nci.caintegrator.web.SessionHelper;
import gov.nih.nci.caintegrator.web.action.AbstractDeployedStudyAction;
import gov.nih.nci.caintegrator.web.ajax.IPersistedAnalysisJobAjaxUpdater;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.genepattern.webservice.WebServiceException;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

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
    private StudyManagementService studyManagementService;
    private ConfigurationHelper configurationHelper;
    private IPersistedAnalysisJobAjaxUpdater ajaxUpdater;
    private String selectedAction = OPEN_ACTION;
    private String analysisMethodName;

    /**
     * {@inheritDoc}
     */
    @Override
    public void prepare() {
        super.prepare();
        List<String> platformsInStudy = new ArrayList<String>(
                getQueryManagementService().retrieveGeneExpressionPlatformsForStudy(getStudy()));
        Collections.sort(platformsInStudy);
        if (platformsInStudy.size() > 1) {
            getGenePatternAnalysisForm().setMultiplePlatformsInStudy(true);
            getGenePatternAnalysisForm().getPlatformNames().clear();
            getGenePatternAnalysisForm().getPlatformNames().addAll(platformsInStudy);
        }
    }

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
            addActionError(getText("struts.messages.error.invalid.action", getArgs(getSelectedAction())));
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
        getGenePatternAnalysisForm().setGenomicQueries(getGeneExpressionQueries());
        Collection<AnnotationFieldDescriptor> fieldDescriptors =
            getClassificationAnnotations(getStudy().getAllVisibleAnnotationFieldDescriptors());
        getGenePatternAnalysisForm().clearClassificationAnnotations();
        getGenePatternAnalysisForm().addClassificationAnnotations(fieldDescriptors);
        setAnalysisMethodName(getGenePatternAnalysisForm().getAnalysisMethodName());
        return SUCCESS;
    }

    private Collection<AnnotationFieldDescriptor> getClassificationAnnotations(
            Collection<AnnotationFieldDescriptor> annotationCollection) {
        Set<AnnotationFieldDescriptor> classificationAnnotations = new HashSet<AnnotationFieldDescriptor>();
        for (AnnotationFieldDescriptor annotation : annotationCollection) {
            if (annotation.getDefinition() != null
                && !annotation.getDefinition().getAnnotationValueCollection().isEmpty()) {
                classificationAnnotations.add(annotation);
            }
        }
        return classificationAnnotations;
    }

    /**
     * @return
     */
    private List<Query> getGeneExpressionQueries() {
        List<Query> queries = new ArrayList<Query>();
        for (Query query : getStudySubscription().getQueryCollection()) {
            if (ResultTypeEnum.GENE_EXPRESSION.equals(query.getResultType())) {
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
            addFieldError("genePatternAnalysisForm.url", getText("struts.messages.error.url.required", getArgs("")));
        } else {
            validateUrl();
        }
        if (StringUtils.isEmpty(getGenePatternAnalysisForm().getUsername())) {
            addFieldError("genePatternAnalysisForm.username", getText("struts.messages.error.username.required"));
        }
    }

    private void validateUrl() {
        try {
            new URL(getGenePatternAnalysisForm().getUrl());
        } catch (MalformedURLException e) {
            addFieldError("genePatternAnalysisForm.url", getText("struts.messages.error.url.invalid.format"));
        }
    }

    private void validateExecuteAnalysis() {
        if (StringUtils.isBlank(getCurrentGenePatternAnalysisJob().getName())) {
            addFieldError("currentGenePatternAnalysisJob.name",
                    getText("struts.messages.error.name.required", getArgs("Job")));
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
        } catch (WebServiceException e) {
            getGenePatternAnalysisForm().setAnalysisMethods(new ArrayList<AnalysisMethod>());
            addActionError("Couldn't retrieve GenePattern analysis method information: " + e.getMessage());
            return ERROR;
        }
    }

    private String executeAnalysis() {
        getCurrentGenePatternAnalysisJob().setMethod(getAnalysisMethodName());
        getCurrentGenePatternAnalysisJob().setCreationDate(new Date());
        getCurrentGenePatternAnalysisJob().setStatus(AnalysisJobStatusEnum.SUBMITTED);
        getStudySubscription().getAnalysisJobCollection().add(getCurrentGenePatternAnalysisJob());
        getCurrentGenePatternAnalysisJob().setSubscription(getStudySubscription());
        getWorkspaceService().saveUserWorkspace(getWorkspace());
        ajaxUpdater.runJob(getCurrentGenePatternAnalysisJob());
        return STATUS_ACTION;
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
        //Only show GISTIC if the study has copy number data and the current user is authorized to see it.
        boolean copyNumberDataAuthorized =
                copyNumberDataAuthorized(getCurrentStudy().getStudyConfiguration().getAuthorizedStudyElementsGroups(),
                        getCurrentStudy().getId());
        if (getCurrentStudy().hasCopyNumberData() && copyNumberDataAuthorized) {
            types.put("gistic", "GISTIC (Grid Service)");
        }
        return types;
    }

    private boolean copyNumberDataAuthorized(List<AuthorizedStudyElementsGroup> allAuthGroups, Long studyConfigId) {
       if (CollectionUtils.isEmpty(allAuthGroups)) {
           return true;
       }
       List<AuthorizedStudyElementsGroup> userAuthGroups =
               getStudyManagementService().getAuthorizedStudyElementsGroups(SessionHelper.getUsername(), studyConfigId);
       List<AuthorizedGenomicDataSourceConfiguration> allGenomicConfigs = Lists.newArrayList();
       for (AuthorizedStudyElementsGroup authGroup : userAuthGroups) {
           allGenomicConfigs.addAll(authGroup.getAuthorizedGenomicDataSourceConfigurations());
       }
       Collection<AuthorizedGenomicDataSourceConfiguration> authorizedCopyNumberData =
               Collections2.filter(allGenomicConfigs, new Predicate<AuthorizedGenomicDataSourceConfiguration>() {
                   @Override
                   public boolean apply(AuthorizedGenomicDataSourceConfiguration gdsc) {
                       return gdsc.getGenomicDataSourceConfiguration().isCopyNumberData();
                   }
               });
       return CollectionUtils.isNotEmpty(authorizedCopyNumberData);
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

}
