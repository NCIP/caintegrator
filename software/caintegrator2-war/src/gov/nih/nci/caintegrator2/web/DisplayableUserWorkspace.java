/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web;

import gov.nih.nci.caintegrator2.application.study.Status;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.application.study.StudyLogo;
import gov.nih.nci.caintegrator2.application.workspace.WorkspaceService;
import gov.nih.nci.caintegrator2.common.ConfigurationParameter;
import gov.nih.nci.caintegrator2.domain.application.ComparativeMarkerSelectionAnalysisJob;
import gov.nih.nci.caintegrator2.domain.application.GeneList;
import gov.nih.nci.caintegrator2.domain.application.GenePatternAnalysisJob;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataQueryResult;
import gov.nih.nci.caintegrator2.domain.application.GisticAnalysisJob;
import gov.nih.nci.caintegrator2.domain.application.PrincipalComponentAnalysisJob;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.application.SubjectList;
import gov.nih.nci.caintegrator2.domain.application.UserWorkspace;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.external.cabio.CaBioDisplayablePathway;
import gov.nih.nci.caintegrator2.external.ncia.NCIADicomJob;
import gov.nih.nci.caintegrator2.web.action.analysis.KMPlotForm;
import gov.nih.nci.caintegrator2.web.action.analysis.geneexpression.GEPlotForm;
import gov.nih.nci.caintegrator2.web.action.platform.form.PlatformForm;
import gov.nih.nci.caintegrator2.web.action.query.DisplayableQueryResult;
import gov.nih.nci.caintegrator2.web.action.query.form.QueryForm;
import gov.nih.nci.caintegrator2.web.action.study.management.DataElementSearchObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * Session singleton object used for displaying user workspace items such as Study's, Queries, and Lists.
 */
@SuppressWarnings({"PMD.TooManyFields", "PMD.ExcessiveClassLength" }) // Keep track of all current jobs
public class DisplayableUserWorkspace {

    /**
     * Value to use in UI for no study in drop down list.
     */
    public static final Long NO_STUDY_SELECTED_ID = Long.valueOf(-1L);
    
    private static final String PUBLIC_STUDIES_HEADER = "-- Public Studies --";
    private static final String USER_WORKSPACE_VALUE_STACK_KEY = "workspace";
    private static final String CURRENT_STUDY_SUBSCRIPTION_VALUE_STACK_KEY = "studySubscription";
    private static final String CURRENT_STUDY_VALUE_STACK_KEY = "study";
    private static final String CURRENT_QUERY_RESULT_VALUE_STACK_KEY = "queryResult";
    private static final String CURRENT_GENOMIC_RESULT_VALUE_STACK_KEY = "genomicDataQueryResult";
    private static final String CURRENT_STUDY_LOGO_KEY = "studyLogo";
    
    private Long currentStudySubscriptionId;
    private GenePatternAnalysisJob currentGenePatternAnalysisJob = new GenePatternAnalysisJob();
    private ComparativeMarkerSelectionAnalysisJob currentComparativeMarkerSelectionAnalysisJob
        = new ComparativeMarkerSelectionAnalysisJob();
    private PrincipalComponentAnalysisJob currentPrincipalComponentAnalysisJob
        = new PrincipalComponentAnalysisJob();
    private GisticAnalysisJob currentGisticAnalysisJob = new GisticAnalysisJob();
    private final PlatformForm platformForm = new PlatformForm();
    private final QueryForm queryForm = new QueryForm();
    private final KMPlotForm kmPlotForm = new KMPlotForm();
    private final GEPlotForm gePlotForm = new GEPlotForm();
    private DisplayableQueryResult queryResult;
    private GenomicDataQueryResult genomicDataQueryResult;
    private NCIADicomJob dicomJob;
    private final DataElementSearchObject dataElementSearchObject = new DataElementSearchObject();
    private boolean createPlotSelected = false;
    private boolean createPlotRunning = false;
    private DownloadableFile temporaryDownloadFile;
    private final Set<StudyConfiguration> managedStudies = new HashSet<StudyConfiguration>();
    private StudyConfiguration currentStudyConfiguration;
    private List<CaBioDisplayablePathway> caBioPathways = new ArrayList<CaBioDisplayablePathway>();
    
    /**
     * Refreshes the workspace for this session, ensuring it is attached to the current Hibernate request.
     *
     * @param workspaceService service used to
     * @param isStudyNeedRefresh determines if we need to refresh study on the stack or not. 
     */
    public void refresh(WorkspaceService workspaceService, boolean isStudyNeedRefresh) {
            setUserWorkspace(retrieveUserWorkspace(workspaceService));
            subscribeAllStudies(workspaceService);
            if (isStudyNeedRefresh) {
                refreshStudyObjects();    
            }
    }
    
    private void subscribeAllStudies(WorkspaceService workspaceService) {
        if (SessionHelper.isAnonymousUser()) {
            workspaceService.subscribeAllReadOnly(getUserWorkspace());
        } else {
            workspaceService.subscribeAll(getUserWorkspace());
        }
    }

    private UserWorkspace retrieveUserWorkspace(WorkspaceService workspaceService) {
        if (SessionHelper.isAnonymousUser()) {
            if (SessionHelper.getAnonymousUserWorkspace() == null) {
                SessionHelper.setAnonymousUserWorkspace(workspaceService.getWorkspaceReadOnly());
            }
            workspaceService.refreshWorkspaceStudies(SessionHelper.getAnonymousUserWorkspace());
            return SessionHelper.getAnonymousUserWorkspace();
        }
        return workspaceService.getWorkspace();
    }
    
    private void refreshStudyObjects() {
        if (getCurrentStudySubscriptionId() == null  && getUserWorkspace().getDefaultSubscription() != null) {
            currentStudySubscriptionId = getUserWorkspace().getDefaultSubscription().getId();
        }
        putCurrentStudyOnValueStack();
        refreshQueryForm();
        putResultObjectsOnValueStack();
    }

    private void refreshQueryForm() {
        if (queryForm.getQuery() != null) {
            queryForm.getQuery().setSubscription(getCurrentStudySubscription());
        }
    }

    /**
     * @return the userWorkspace
     */
    public UserWorkspace getUserWorkspace() {
        return (UserWorkspace) getValueStack().findValue(USER_WORKSPACE_VALUE_STACK_KEY);
    }

    private ValueStack getValueStack() {
        return ActionContext.getContext().getValueStack();
    }

    private void setUserWorkspace(UserWorkspace userWorkspace) {
        getValueStack().set(USER_WORKSPACE_VALUE_STACK_KEY, userWorkspace);
    }
    
    /**
     * @return the studyLogo
     */
    public StudyLogo getStudyLogo() {
        return (StudyLogo) getValueStack().findValue(CURRENT_STUDY_LOGO_KEY);
    }
    
    /**
     * Sets current study logo.
     * @param studyLogo to set.
     */
    public void setStudyLogo(StudyLogo studyLogo) {
        getValueStack().set(CURRENT_STUDY_LOGO_KEY, studyLogo);
    }

    /**
     * @return the currentStudySubscription
     */
    public StudySubscription getCurrentStudySubscription() {
        return (StudySubscription) getValueStack().findValue(CURRENT_STUDY_SUBSCRIPTION_VALUE_STACK_KEY);
    }

    /**
     * @param currentStudySubscription the currentStudySubscription to set
     */
    public void setCurrentStudySubscription(StudySubscription currentStudySubscription) {
        if (currentStudySubscription == null) {
            setCurrentStudySubscriptionId(null);
        } else {
            setCurrentStudySubscriptionId(currentStudySubscription.getId());
        }
    }

    /**
     * @return the currentStudySubscriptionId
     */
    public Long getCurrentStudySubscriptionId() {
        return currentStudySubscriptionId;
    }

    /**
     * @param currentStudySubscriptionId the currentStudySubscriptionId to set
     */
    public void setCurrentStudySubscriptionId(Long currentStudySubscriptionId) {
        if (currentStudySubscriptionId == null || currentStudySubscriptionId.equals(NO_STUDY_SELECTED_ID)) {
            setQueryResult(null);
            setGenomicDataQueryResult(null);
            getQueryForm().setQuery(null, null);
        }
        this.currentStudySubscriptionId = currentStudySubscriptionId;
        putCurrentStudyOnValueStack();
    }

    private void putCurrentStudyOnValueStack() {
        if (getUserWorkspace() == null) {
            return;
        }
        StudySubscription currentStudySubscription = null;
        Study currentStudy = null;
        for (StudySubscription subscription : getUserWorkspace().getSubscriptionCollection()) {
            if (subscription.getId().equals(getCurrentStudySubscriptionId())) {
                currentStudySubscription = subscription;
                currentStudy = subscription.getStudy();
            }
        }
        getValueStack().set(CURRENT_STUDY_SUBSCRIPTION_VALUE_STACK_KEY, currentStudySubscription);
        getValueStack().set(CURRENT_STUDY_VALUE_STACK_KEY, currentStudy);
    }
    
    /**
     * @return the subscriptionCollection
     */
    public List<StudySubscription> getOrderedSubscriptionList() {
        List<StudySubscription> orderedSubscriptionCollection = new ArrayList<StudySubscription>();
        List<StudySubscription> publicSubscriptionCollection = new ArrayList<StudySubscription>();
        if (getUserWorkspace().getSubscriptionCollection() != null) {
            for (StudySubscription studySubscription : getUserWorkspace().getSubscriptionCollection()) {
                if (Status.DEPLOYED.equals(studySubscription.getStudy().getStudyConfiguration().getStatus())) {
                    if (studySubscription.isPublicSubscription()) {
                        publicSubscriptionCollection.add(studySubscription);
                    } else {
                        orderedSubscriptionCollection.add(studySubscription);
                    }
                }
            }
        }
        sortStudySubscriptions(orderedSubscriptionCollection);
        addPublicSubscriptions(orderedSubscriptionCollection, publicSubscriptionCollection);
        return orderedSubscriptionCollection;
    }

    private void addPublicSubscriptions(List<StudySubscription> orderedSubscriptionCollection,
            List<StudySubscription> publicSubscriptionCollection) {
        if (!publicSubscriptionCollection.isEmpty()) {
            if (!orderedSubscriptionCollection.isEmpty()) {
                StudySubscription publicStudyHeader = new StudySubscription();
                publicStudyHeader.setId(NO_STUDY_SELECTED_ID);
                publicStudyHeader.setStudy(new Study());
                publicStudyHeader.getStudy().setShortTitleText(PUBLIC_STUDIES_HEADER);
                sortStudySubscriptions(publicSubscriptionCollection);
                orderedSubscriptionCollection.add(publicStudyHeader);
            }
            orderedSubscriptionCollection.addAll(publicSubscriptionCollection);
        }
    }

    private void sortStudySubscriptions(List<StudySubscription> orderedSubscriptionCollection) {
        Comparator<StudySubscription> nameComparator = new Comparator<StudySubscription>() {
            public int compare(StudySubscription subscription1, StudySubscription subscription2) {
                return subscription1.getStudy().getShortTitleText().
                       compareToIgnoreCase(subscription2.getStudy().getShortTitleText());
            }
        };
        Collections.sort(orderedSubscriptionCollection, nameComparator);
    }
    
    private void putResultObjectsOnValueStack() {
        getValueStack().set(CURRENT_QUERY_RESULT_VALUE_STACK_KEY, getQueryResult());
        getValueStack().set(CURRENT_GENOMIC_RESULT_VALUE_STACK_KEY, getGenomicDataQueryResult());
    }

    /**
     * @return the queryResult
     */
    public DisplayableQueryResult getQueryResult() {
        return queryResult;
    }

    /**
     * @param queryResult the queryResult to set
     */
    public void setQueryResult(DisplayableQueryResult queryResult) {
        this.queryResult = queryResult;
        getValueStack().set(CURRENT_QUERY_RESULT_VALUE_STACK_KEY, queryResult);
    }

    /**
     * @return the genomicDataQueryResult
     */
    public GenomicDataQueryResult getGenomicDataQueryResult() {
        return genomicDataQueryResult;
    }

    /**
     * @param genomicDataQueryResult the genomicDataQueryResult to set
     */
    public void setGenomicDataQueryResult(GenomicDataQueryResult genomicDataQueryResult) {
        this.genomicDataQueryResult = genomicDataQueryResult;
        getValueStack().set(CURRENT_GENOMIC_RESULT_VALUE_STACK_KEY, genomicDataQueryResult);
    }
    
    /**
     * Retrieve the URL for CGAP.
     * @return - URL for CGAP.
     */
    public String getCgapUrl() {
        return ConfigurationParameter.CGAP_URL.getDefaultValue();
    }

    /**
     * @return the queryForm
     */
    public QueryForm getQueryForm() {
        return queryForm;
    }
    
    /**
     * @return the user's queries, ordered by name.
     */
    public List<Query> getUserQueries() {
        List<Query> queries = new ArrayList<Query>();
        if (getCurrentStudySubscription() != null) {
            queries.addAll(getCurrentStudySubscription().getQueryCollection());
        }
        Comparator<Query> nameComparator = new Comparator<Query>() {
            public int compare(Query query1, Query query2) {
                return query1.getName().compareToIgnoreCase(query2.getName());
            }
        };
        Collections.sort(queries, nameComparator);
        return queries;
    }
    
    /**
     * @return the saved gene lists.
     */
    public List<GeneList> getGeneLists() {
        return (getCurrentStudySubscription() == null)
            ? null : getCurrentStudySubscription().getGeneLists();
    }
    
    /**
     * @return the saved subject lists.
     */
    public List<SubjectList> getSubjectLists() {
        return (getCurrentStudySubscription() == null)
            ? null : getCurrentStudySubscription().getSubjectLists();
    }
    
    /**
     * @return the saved gene lists.
     */
    public List<GeneList> getGlobalGeneLists() {
        return (getCurrentStudySubscription() == null)
            ? null : getCurrentStudySubscription().getStudy().getStudyConfiguration().getGeneLists();
    }
    
    /**
     * @return the saved subject lists.
     */
    public List<SubjectList> getGlobalSubjectLists() {
        return (getCurrentStudySubscription() == null)
            ? null : getCurrentStudySubscription().getStudy().getStudyConfiguration().getSubjectLists();
    }

    /**
     * @return the dicomJob
     */
    public NCIADicomJob getDicomJob() {
        return dicomJob;
    }

    /**
     * @param dicomJob the dicomJob to set
     */
    public void setDicomJob(NCIADicomJob dicomJob) {
        this.dicomJob = dicomJob;
    }

    /**
     * @return the kmPlotForm
     */
    public KMPlotForm getKmPlotForm() {
        return kmPlotForm;
    }
    
    /**
     * @return the gePlotForm
     */
    public GEPlotForm getGePlotForm() {
        return gePlotForm;
    }
    
    /**
     * @return the dataElementSearchObject
     */
    public DataElementSearchObject getDataElementSearchObject() {
        return dataElementSearchObject;
    }

    /**
     * @return the createPlotSelected
     */
    public boolean isCreatePlotSelected() {
        return createPlotSelected;
    }

    /**
     * @param createPlotSelected the createPlotSelected to set
     */
    public void setCreatePlotSelected(boolean createPlotSelected) {
        this.createPlotSelected = createPlotSelected;
    }

    /**
     * @return the createPlotRunning
     */
    public boolean isCreatePlotRunning() {
        return createPlotRunning;
    }

    /**
     * @param createPlotRunning the createPlotRunning to set
     */
    public void setCreatePlotRunning(boolean createPlotRunning) {
        this.createPlotRunning = createPlotRunning;
    }

    /**
     * @return the currentAnalysisJob
     */
    public GenePatternAnalysisJob getCurrentGenePatternAnalysisJob() {
        return currentGenePatternAnalysisJob;
    }

    /**
     * @param currentGenePatternAnalysisJob the currentGenePatternAnalysisJob to set
     */
    public void setCurrentGenePatternAnalysisJob(GenePatternAnalysisJob currentGenePatternAnalysisJob) {
        this.currentGenePatternAnalysisJob = currentGenePatternAnalysisJob;
    }

    /**
     * @return the currentComparativeMarkerSelectionAnalysisJob
     */
    public ComparativeMarkerSelectionAnalysisJob getCurrentComparativeMarkerSelectionAnalysisJob() {
        return currentComparativeMarkerSelectionAnalysisJob;
    }

    /**
     * @param currentComparativeMarkerSelectionAnalysisJob the currentComparativeMarkerSelectionAnalysisJob to set
     */
    public void setCurrentComparativeMarkerSelectionAnalysisJob(
            ComparativeMarkerSelectionAnalysisJob currentComparativeMarkerSelectionAnalysisJob) {
        this.currentComparativeMarkerSelectionAnalysisJob = currentComparativeMarkerSelectionAnalysisJob;
    }

    /**
     * @return the temporaryDownloadFile
     */
    public DownloadableFile getTemporaryDownloadFile() {
        return temporaryDownloadFile;
    }

    /**
     * @param temporaryDownloadFile the temporaryDownloadFile to set
     */
    public void setTemporaryDownloadFile(DownloadableFile temporaryDownloadFile) {
        this.temporaryDownloadFile = temporaryDownloadFile;
    }

    /**
     * @return the currentPrincipalComponentAnalysisJob
     */
    public PrincipalComponentAnalysisJob getCurrentPrincipalComponentAnalysisJob() {
        return currentPrincipalComponentAnalysisJob;
    }

    /**
     * @param currentPrincipalComponentAnalysisJob the currentPrincipalComponentAnalysisJob to set
     */
    public void setCurrentPrincipalComponentAnalysisJob(
            PrincipalComponentAnalysisJob currentPrincipalComponentAnalysisJob) {
        this.currentPrincipalComponentAnalysisJob = currentPrincipalComponentAnalysisJob;
    }

    /**
     * @return the currentGisticAnalysisJob
     */
    public GisticAnalysisJob getCurrentGisticAnalysisJob() {
        return currentGisticAnalysisJob;
    }

    /**
     * @param currentGisticAnalysisJob the currentGisticAnalysisJob to set
     */
    public void setCurrentGisticAnalysisJob(
            GisticAnalysisJob currentGisticAnalysisJob) {
        this.currentGisticAnalysisJob = currentGisticAnalysisJob;
    }

    /**
     * @return the platformForm
     */
    public PlatformForm getPlatformForm() {
        return platformForm;
    }

    /**
     * @return the managedStudies
     */
    public Set<StudyConfiguration> getManagedStudies() {
        return managedStudies;
    }

    /**
     * @return the currentStudyConfiguration
     */
    public StudyConfiguration getCurrentStudyConfiguration() {
        return currentStudyConfiguration;
    }

    /**
     * @param currentStudyConfiguration the currentStudyConfiguration to set
     */
    public void setCurrentStudyConfiguration(StudyConfiguration currentStudyConfiguration) {
        this.currentStudyConfiguration = currentStudyConfiguration;
    }

    /**
     * @return the caBioPathways
     */
    public List<CaBioDisplayablePathway> getCaBioPathways() {
        return caBioPathways;
    }

    /**
     * @param caBioPathways the caBioPathways to set
     */
    public void setCaBioPathways(List<CaBioDisplayablePathway> caBioPathways) {
        this.caBioPathways = caBioPathways;
    }
    
    /**
     * Clears the analysis form cache.
     */
    public void clearAnalysisCache() {
        gePlotForm.clear();
        kmPlotForm.clear();
    }


}
