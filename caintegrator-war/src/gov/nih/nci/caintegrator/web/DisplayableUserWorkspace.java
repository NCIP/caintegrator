/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.web;

import gov.nih.nci.caintegrator.application.analysis.heatmap.HeatmapParameters;
import gov.nih.nci.caintegrator.application.analysis.igv.IGVParameters;
import gov.nih.nci.caintegrator.application.study.Status;
import gov.nih.nci.caintegrator.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator.application.study.StudyLogo;
import gov.nih.nci.caintegrator.application.workspace.WorkspaceService;
import gov.nih.nci.caintegrator.common.ConfigurationParameter;
import gov.nih.nci.caintegrator.domain.analysis.AbstractCopyNumberAnalysis;
import gov.nih.nci.caintegrator.domain.analysis.GisticAnalysis;
import gov.nih.nci.caintegrator.domain.application.ComparativeMarkerSelectionAnalysisJob;
import gov.nih.nci.caintegrator.domain.application.GeneList;
import gov.nih.nci.caintegrator.domain.application.GenePatternAnalysisJob;
import gov.nih.nci.caintegrator.domain.application.GenomicDataQueryResult;
import gov.nih.nci.caintegrator.domain.application.GisticAnalysisJob;
import gov.nih.nci.caintegrator.domain.application.PrincipalComponentAnalysisJob;
import gov.nih.nci.caintegrator.domain.application.Query;
import gov.nih.nci.caintegrator.domain.application.StudySubscription;
import gov.nih.nci.caintegrator.domain.application.SubjectList;
import gov.nih.nci.caintegrator.domain.application.UserWorkspace;
import gov.nih.nci.caintegrator.domain.translational.Study;
import gov.nih.nci.caintegrator.external.cabio.CaBioDisplayablePathway;
import gov.nih.nci.caintegrator.external.ncia.NCIADicomJob;
import gov.nih.nci.caintegrator.web.action.analysis.KMPlotForm;
import gov.nih.nci.caintegrator.web.action.analysis.geneexpression.GEPlotForm;
import gov.nih.nci.caintegrator.web.action.platform.form.PlatformForm;
import gov.nih.nci.caintegrator.web.action.query.DisplayableCopyNumberQueryResult;
import gov.nih.nci.caintegrator.web.action.query.DisplayableQueryResult;
import gov.nih.nci.caintegrator.web.action.query.form.QueryForm;
import gov.nih.nci.caintegrator.web.action.study.management.DataElementSearchObject;

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
    private static final String CURRENT_COPY_NUMBER_RESULT_VALUE_STACK_KEY = "copyNumberQueryResult";
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
    private IGVParameters igvParameters = new IGVParameters();
    private HeatmapParameters heatmapParameters = new HeatmapParameters();
    private DisplayableQueryResult queryResult;
    private GenomicDataQueryResult genomicDataQueryResult;
    private DisplayableCopyNumberQueryResult copyNumberQueryResult;
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
            setCopyNumberQueryResult(null);
            getQueryForm().setQuery(null, null, null, null);
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
                if (!studySubscription.getStudy().isEnabled()) {
                    continue;
                }

                checkDeployedStatus(studySubscription, orderedSubscriptionCollection, publicSubscriptionCollection);
            }
        }
        sortStudySubscriptions(orderedSubscriptionCollection);
        addPublicSubscriptions(orderedSubscriptionCollection, publicSubscriptionCollection);
        return orderedSubscriptionCollection;
    }

    private void checkDeployedStatus(StudySubscription studySubscription, List<StudySubscription>
        orderedSubscriptionCollection, List<StudySubscription> publicSubscriptionCollection) {
        if (Status.DEPLOYED.equals(studySubscription.getStudy().getStudyConfiguration().getStatus())) {
            if (studySubscription.isPublicSubscription()) {
                publicSubscriptionCollection.add(studySubscription);
            } else {
                orderedSubscriptionCollection.add(studySubscription);
            }
        }
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
            @Override
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
        getValueStack().set(CURRENT_COPY_NUMBER_RESULT_VALUE_STACK_KEY, getCopyNumberQueryResult());
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
     * @return the copyNumberQueryResult
     */
    public DisplayableCopyNumberQueryResult getCopyNumberQueryResult() {
        return copyNumberQueryResult;
    }

    /**
     * @param copyNumberQueryResult the copyNumberQueryResult to set
     */
    public void setCopyNumberQueryResult(DisplayableCopyNumberQueryResult copyNumberQueryResult) {
        this.copyNumberQueryResult = copyNumberQueryResult;
        getValueStack().set(CURRENT_COPY_NUMBER_RESULT_VALUE_STACK_KEY, getCopyNumberQueryResult());
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
            @Override
            public int compare(Query query1, Query query2) {
                return query1.getName().compareToIgnoreCase(query2.getName());
            }
        };
        Collections.sort(queries, nameComparator);
        return queries;
    }

    /**
     * @return the user's GISTIC analysis jobs, ordered by name.
     */
    public List<GisticAnalysis> getUserGisticAnalysisList() {
        List<GisticAnalysis> gisticAnalysisList = new ArrayList<GisticAnalysis>();
        if (getCurrentStudySubscription() != null) {
            for (AbstractCopyNumberAnalysis copyNumberAnalysis
                    : getCurrentStudySubscription().getCopyNumberAnalysisCollection()) {
                if (copyNumberAnalysis instanceof GisticAnalysis) {
                    gisticAnalysisList.add((GisticAnalysis) copyNumberAnalysis);
                }
            }
        }
        Comparator<GisticAnalysis> nameComparator = new Comparator<GisticAnalysis>() {
            @Override
            public int compare(GisticAnalysis gisticAnalysis1, GisticAnalysis gisticAnalysis2) {
                return gisticAnalysis1.getName().compareToIgnoreCase(gisticAnalysis2.getName());
            }
        };
        Collections.sort(gisticAnalysisList, nameComparator);
        return gisticAnalysisList;
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

    /**
     * @return the igvParameters
     */
    public IGVParameters getIgvParameters() {
        return igvParameters;
    }

    /**
     * @param igvParameters the igvParameters to set
     */
    public void setIgvParameters(IGVParameters igvParameters) {
        this.igvParameters = igvParameters;
    }

    /**
     * @return the igvParameters
     */
    public HeatmapParameters getHeatmapParameters() {
        return heatmapParameters;
    }

    /**
     * @param heatmapParameters the heatmapParameters to set
     */
    public void setHeatmapParameters(HeatmapParameters heatmapParameters) {
        this.heatmapParameters = heatmapParameters;
    }


}
