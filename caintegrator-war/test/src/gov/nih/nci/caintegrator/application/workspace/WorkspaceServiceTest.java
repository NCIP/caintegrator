/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.workspace;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import gov.nih.nci.caintegrator.AcegiAuthenticationStub;
import gov.nih.nci.caintegrator.application.study.ExternalLinkList;
import gov.nih.nci.caintegrator.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator.application.study.ValidationException;
import gov.nih.nci.caintegrator.application.study.Visibility;
import gov.nih.nci.caintegrator.application.workspace.WorkspaceService;
import gov.nih.nci.caintegrator.application.workspace.WorkspaceServiceImpl;
import gov.nih.nci.caintegrator.data.CaIntegrator2DaoStub;
import gov.nih.nci.caintegrator.data.StudyHelper;
import gov.nih.nci.caintegrator.domain.application.GeneList;
import gov.nih.nci.caintegrator.domain.application.GenePatternAnalysisJob;
import gov.nih.nci.caintegrator.domain.application.StudySubscription;
import gov.nih.nci.caintegrator.domain.application.SubjectList;
import gov.nih.nci.caintegrator.domain.application.UserWorkspace;
import gov.nih.nci.caintegrator.domain.genomic.SampleSet;
import gov.nih.nci.caintegrator.domain.translational.Study;
import gov.nih.nci.caintegrator.web.DisplayableGenomicSource;
import gov.nih.nci.caintegrator.web.DisplayableImageSource;
import gov.nih.nci.caintegrator.web.DisplayableStudySummary;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.acegisecurity.context.SecurityContextHolder;
import org.junit.Before;
import org.junit.Test;

public class WorkspaceServiceTest {

    private WorkspaceService workspaceService;
    private CaIntegrator2DaoStub daoStub;
    private AcegiAuthenticationStub authentication = new AcegiAuthenticationStub();

    @Before
    public void setUp() {
        daoStub = new CaIntegrator2DaoStub();
        WorkspaceServiceImpl workspaceSvc = new WorkspaceServiceImpl();
        workspaceSvc.setDao(daoStub);
        authentication.setUsername("validUserName");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        workspaceService = workspaceSvc;
    }

    @Test
    public void testGetWorkspace() {
        UserWorkspace workspace = workspaceService.getWorkspace();
        assertNotNull(workspace);
        authentication.setUsername(UserWorkspace.ANONYMOUS_USER_NAME);
        UserWorkspace workspaceAnonymous = workspaceService.getWorkspaceReadOnly();
        assertTrue(workspaceAnonymous.isAnonymousUser());

        WorkspaceServiceImpl workspaceService2 = (WorkspaceServiceImpl) workspaceService;
        workspaceService2.setDao(new DaoStubNoWorkspace());
        workspace = workspaceService2.getWorkspace();
        assertNotNull(workspace);
    }

    @Test
    public void testSubscribeAllStudies() {
        UserWorkspace workspace = workspaceService.getWorkspace();
        StudySubscription studySubscription = new StudySubscription();
        studySubscription.setId(1l);
        studySubscription.setStudy(new Study());
        workspace.getSubscriptionCollection().add(studySubscription);
        assertEquals(1, workspace.getSubscriptionCollection().size());
        workspaceService.subscribeAll(workspace);
        assertEquals(2, workspace.getSubscriptionCollection().size()); // daoStub has 1 public study.
        assertFalse(workspace.getSubscriptionCollection().contains(studySubscription));
    }

    @Test
    public void testSubscribeAllReadOnly() {
        authentication.setUsername(UserWorkspace.ANONYMOUS_USER_NAME);
        UserWorkspace workspace = workspaceService.getWorkspace();
        StudySubscription studySubscription = new StudySubscription();
        studySubscription.setStudy(new Study());
        workspace.getSubscriptionCollection().add(studySubscription);
        assertEquals(1, workspace.getSubscriptionCollection().size());
        workspaceService.subscribeAllReadOnly(workspace);
        assertEquals(1, workspace.getSubscriptionCollection().size());
        assertFalse(workspace.getSubscriptionCollection().contains(studySubscription));
    }

    @Test
    public void testSubscribe() {
        Study study = new Study();
        study.setId(1L);
        UserWorkspace workspace = workspaceService.getWorkspace();
        assertEquals(0, workspace.getSubscriptionCollection().size());
        workspaceService.subscribe(workspace, study, false);
        assertEquals(1, workspace.getSubscriptionCollection().size());
        workspaceService.subscribe(workspace, study, false);
        assertEquals(1, workspace.getSubscriptionCollection().size());
        workspaceService.unsubscribe(workspace, study);
        assertEquals(0, workspace.getSubscriptionCollection().size());
    }

    @Test
    public void testUnsubscribeAll() {
        workspaceService.unsubscribeAll(new Study());
        assertTrue(daoStub.retrieveAllSubscribedWorkspacesCalled);
    }

    @Test
    public void testCreateDisplayableStudySummary() {
        StudyHelper studyHelper = new StudyHelper();
        Study study = studyHelper.populateAndRetrieveStudyWithSourceConfigurations();
        DisplayableStudySummary summary = workspaceService.createDisplayableStudySummary(study);
        assertNotNull(summary);
        assertTrue(daoStub.retrieveNumberImagesCalled);
        assertTrue(daoStub.retrievePlatformsForGenomicSourceCalled);
        assertEquals(2, summary.getNumberSubjectAnnotationColumns());
        assertEquals(6, summary.getNumberSubjects());
        assertEquals(study.getShortTitleText(), summary.getStudyName());
        assertEquals(study.getLongTitleText(), summary.getStudyDescription());
        assertTrue(summary.isGenomicStudy());
        assertTrue(summary.isImagingStudy());
        List<DisplayableGenomicSource> genomicSources = summary.getGenomicDataSources();
        assertEquals(1, genomicSources.size());
        DisplayableGenomicSource genomicSource = genomicSources.get(0);
        assertEquals(2, genomicSource.getPlatforms().size());
        assertEquals(2, genomicSource.getNumberSamples());
        assertEquals(0, genomicSource.getNumberMappedSamples());
        assertEquals(0, genomicSource.getNumberControlSamples());
        assertFalse(genomicSource.isControlSamplesSet());
        assertEquals("experimentIdentifier", genomicSource.getExperimentName());
        assertNotNull(genomicSource.getGenomicDataSourceConfiguration());
        //currently the genomic data source hostname is not populated in our test data
        assertNull(genomicSource.getHostName());

        List<DisplayableImageSource> imageSources = summary.getImageDataSources();
        assertEquals(1, imageSources.size());
        DisplayableImageSource imageSource = imageSources.get(0);
        assertEquals(2, imageSource.getNumberImages());
        assertEquals(1, imageSource.getNumberImageSeries());
        assertEquals(1, imageSource.getNumberImageStudies());
        assertEquals("collection", imageSource.getCollectionName());

        GenomicDataSourceConfiguration gdsc = genomicSource.getGenomicDataSourceConfiguration();
        assertEquals("", gdsc.getControlSampleSetCommaSeparated());
        SampleSet sampleSet = new SampleSet();
        sampleSet.setName("Sample1");
        gdsc.getControlSampleSetCollection().add(sampleSet);
        sampleSet = new SampleSet();
        sampleSet.setName("Sample2");
        gdsc.getControlSampleSetCollection().add(sampleSet);

        assertTrue(gdsc.getControlSampleSetCommaSeparated().contains("Sample1: 0"));
        assertTrue(gdsc.getControlSampleSetCommaSeparated().contains("Sample2: 0"));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testCreateDisplayableStudySummaryInvalid() {
        workspaceService.createDisplayableStudySummary(null);
    }

    @Test
    public void testSavePersistedAnalysisJob() {
        workspaceService.savePersistedAnalysisJob(new GenePatternAnalysisJob());
        assertTrue(daoStub.saveCalled);
    }


    private final class DaoStubNoWorkspace extends CaIntegrator2DaoStub {

        @Override
        public UserWorkspace getWorkspace(String username) {
            return null;
        }
    }

    @Test
    public void testGetEntity() {
        ExternalLinkList externalLinkList = new ExternalLinkList();
        externalLinkList.setId(1L);
        assertNotNull(workspaceService.getRefreshedEntity(externalLinkList));
        assertTrue(daoStub.getCalled);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEntityNoId() {
        ExternalLinkList externalLinkList = new ExternalLinkList();
        workspaceService.getRefreshedEntity(externalLinkList);
    }

    @Test
    public void testCreateList() throws ValidationException {
        UserWorkspace workspace = workspaceService.getWorkspace();
        StudySubscription studySubscription = new StudySubscription();
        studySubscription.setStudy(new Study());
        studySubscription.getStudy().setStudyConfiguration(new StudyConfiguration());
        studySubscription.setUserWorkspace(workspace);
        workspace.getSubscriptionCollection().add(studySubscription);

        // Test Gene List
        GeneList geneList = new GeneList();
        geneList.setVisibility(Visibility.PRIVATE);
        geneList.setSubscription(studySubscription);
        Set<String> geneSymbols = new HashSet<String>();
        geneList.setName("Gene List 1");
        geneSymbols.add("egfr");
        geneSymbols.add("brca1");

        workspaceService.createGeneList(geneList, geneSymbols);
        assertEquals(1, studySubscription.getGeneLists().size());
        assertNotNull(studySubscription.getGeneList("Gene List 1"));
        assertNull(studySubscription.getGeneList("Gene List 2"));

        geneList = new GeneList();
        geneList.setVisibility(Visibility.PRIVATE);
        geneList.setSubscription(studySubscription);
        geneList.setName("Gene List 2");
        workspaceService.createGeneList(geneList, geneSymbols);
        assertEquals(2, studySubscription.getGeneLists().size());
        assertNotNull(studySubscription.getGeneList("Gene List 1"));
        assertNotNull(studySubscription.getGeneList("Gene List 2"));

        workspaceService.deleteAbstractList(studySubscription, geneList);
        assertEquals(1, studySubscription.getGeneLists().size());
        assertNotNull(studySubscription.getGeneList("Gene List 1"));
        assertNull(studySubscription.getGeneList("Gene List 2"));


        // Test Subject List
        SubjectList subjectList = new SubjectList();
        subjectList.setVisibility(Visibility.PRIVATE);
        subjectList.setSubscription(studySubscription);
        Set<String> subjects = new HashSet<String>();
        subjectList.setName("Subject List 1");
        subjects.add("100");
        subjects.add("200");
        workspaceService.createSubjectList(subjectList, subjects);
        assertEquals(1, studySubscription.getSubjectLists().size());
        assertNotNull(studySubscription.getSubjectList("Subject List 1"));
        assertNull(studySubscription.getSubjectList("Subject List 2"));

        subjectList = new SubjectList();
        subjectList.setVisibility(Visibility.PRIVATE);
        subjectList.setSubscription(studySubscription);
        subjectList.setName("Subject List 2");
        workspaceService.createSubjectList(subjectList, subjects);
        assertEquals(2, studySubscription.getSubjectLists().size());
        assertNotNull(studySubscription.getSubjectList("Subject List 1"));
        assertNotNull(studySubscription.getSubjectList("Subject List 2"));

        workspaceService.deleteAbstractList(studySubscription, subjectList);
        assertEquals(1, studySubscription.getSubjectLists().size());
        assertNotNull(studySubscription.getSubjectList("Subject List 1"));
        assertNull(studySubscription.getSubjectList("Subject List 2"));

        subjects.add("really long string to prove that identifiers can only be so long");
        try {
            workspaceService.createSubjectList(subjectList, subjects);
            fail("Shouldn't get here due to long identifier");
        } catch(ValidationException e) {
            // noop.
        }

        // Test Global Gene List
        geneList = new GeneList();
        geneList.setVisibility(Visibility.GLOBAL);
        geneList.setStudyConfiguration(studySubscription.getStudy().getStudyConfiguration());
        geneList.setSubscription(studySubscription);
        geneSymbols = new HashSet<String>();
        geneList.setName("Gene List 1");
        geneSymbols.add("egfr");
        geneSymbols.add("brca1");

        workspaceService.createGeneList(geneList, geneSymbols);
        assertEquals(1, studySubscription.getStudy().getStudyConfiguration().getGeneLists().size());
        assertNotNull(studySubscription.getStudy().getStudyConfiguration().getGeneList("Gene List 1"));
        assertNull(studySubscription.getStudy().getStudyConfiguration().getGeneList("Gene List 2"));

        geneList = new GeneList();
        geneList.setVisibility(Visibility.GLOBAL);
        geneList.setStudyConfiguration(studySubscription.getStudy().getStudyConfiguration());
        geneList.setSubscription(studySubscription);
        geneList.setName("Gene List 2");
        workspaceService.createGeneList(geneList, geneSymbols);
        assertEquals(2, studySubscription.getStudy().getStudyConfiguration().getGeneLists().size());
        assertNotNull(studySubscription.getStudy().getStudyConfiguration().getGeneList("Gene List 1"));
        assertNotNull(studySubscription.getStudy().getStudyConfiguration().getGeneList("Gene List 2"));

        workspaceService.deleteAbstractList(studySubscription, geneList);
        assertEquals(1, studySubscription.getStudy().getStudyConfiguration().getGeneLists().size());
        assertNotNull(studySubscription.getStudy().getStudyConfiguration().getGeneList("Gene List 1"));
        assertNull(studySubscription.getStudy().getStudyConfiguration().getGeneList("Gene List 2"));


        // Test Global Subject List
        subjectList = new SubjectList();
        subjectList.setVisibility(Visibility.GLOBAL);
        subjectList.setStudyConfiguration(studySubscription.getStudy().getStudyConfiguration());
        subjectList.setSubscription(studySubscription);
        subjects = new HashSet<String>();
        subjectList.setName("Subject List 1");
        subjects.add("100");
        subjects.add("200");
        workspaceService.createSubjectList(subjectList, subjects);
        assertEquals(1, studySubscription.getStudy().getStudyConfiguration().getSubjectLists().size());
        assertNotNull(studySubscription.getStudy().getStudyConfiguration().getSubjectList("Subject List 1"));
        assertNull(studySubscription.getStudy().getStudyConfiguration().getSubjectList("Subject List 2"));

        subjectList = new SubjectList();
        subjectList.setVisibility(Visibility.GLOBAL);
        subjectList.setStudyConfiguration(studySubscription.getStudy().getStudyConfiguration());
        subjectList.setSubscription(studySubscription);
        subjectList.setName("Subject List 2");
        workspaceService.createSubjectList(subjectList, subjects);
        assertEquals(2, studySubscription.getStudy().getStudyConfiguration().getSubjectLists().size());
        assertNotNull(studySubscription.getStudy().getStudyConfiguration().getSubjectList("Subject List 1"));
        assertNotNull(studySubscription.getStudy().getStudyConfiguration().getSubjectList("Subject List 2"));

        workspaceService.deleteAbstractList(studySubscription, subjectList);
        assertEquals(1, studySubscription.getStudy().getStudyConfiguration().getSubjectLists().size());
        assertNotNull(studySubscription.getStudy().getStudyConfiguration().getSubjectList("Subject List 1"));
        assertNull(studySubscription.getStudy().getStudyConfiguration().getSubjectList("Subject List 2"));
    }

}
