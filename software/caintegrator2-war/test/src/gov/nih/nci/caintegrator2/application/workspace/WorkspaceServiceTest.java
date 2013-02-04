/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.workspace;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.application.study.ExternalLinkList;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.data.CaIntegrator2DaoStub;
import gov.nih.nci.caintegrator2.data.StudyHelper;
import gov.nih.nci.caintegrator2.domain.application.GenePatternAnalysisJob;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.application.UserWorkspace;
import gov.nih.nci.caintegrator2.domain.genomic.SampleSet;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.web.DisplayableGenomicSource;
import gov.nih.nci.caintegrator2.web.DisplayableImageSource;
import gov.nih.nci.caintegrator2.web.DisplayableStudySummary;

import java.util.HashSet;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class WorkspaceServiceTest {
    
    private WorkspaceService workspaceService;
    private CaIntegrator2DaoStub daoStub;

    @Before
    public void setUp() {
        ApplicationContext context = new ClassPathXmlApplicationContext("workspaceservice-test-config.xml", WorkspaceServiceTest.class); 
        workspaceService = (WorkspaceService) context.getBean("WorkspaceService"); 
        daoStub = (CaIntegrator2DaoStub) context.getBean("dao");
        daoStub.clear();
    }

    @Test
    public void testGetWorkspace() {
        UserWorkspace workspace = workspaceService.getWorkspace();
        assertNotNull(workspace);
        
        WorkspaceServiceImpl workspaceService2 = (WorkspaceServiceImpl) workspaceService;
        workspaceService2.setDao(new DaoStubNoWorkspace());
        workspace = workspaceService2.getWorkspace();
        assertNotNull(workspace);
    }
    
    @Test
    public void testSubscribeAllStudies() {
        UserWorkspace workspace = workspaceService.getWorkspace();
        workspace.setSubscriptionCollection(new HashSet<StudySubscription>());
        StudySubscription studySubscription = new StudySubscription();
        studySubscription.setStudy(new Study());
        workspace.getSubscriptionCollection().add(studySubscription);
        assertEquals(1, workspace.getSubscriptionCollection().size());
        workspaceService.subscribeAll(workspace);
        assertEquals(1, workspace.getSubscriptionCollection().size());
        assertFalse(workspace.getSubscriptionCollection().contains(studySubscription));
    }
    
    @Test
    public void testSubscribe() {
        Study study = new Study();
        study.setId(1L);
        UserWorkspace workspace = workspaceService.getWorkspace();
        assertEquals(0, workspace.getSubscriptionCollection().size());
        workspaceService.subscribe(workspace, study);
        assertEquals(1, workspace.getSubscriptionCollection().size());
        workspaceService.subscribe(workspace, study);
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
        assertEquals(1, summary.getNumberSubjectAnnotationColumns());
        assertEquals(5, summary.getNumberSubjects());
        assertEquals(study.getShortTitleText(), summary.getStudyName());
        assertEquals(study.getLongTitleText(), summary.getStudyDescription());
        assertTrue(summary.isGenomicStudy());
        assertTrue(summary.isImagingStudy());
        List<DisplayableGenomicSource> genomicSources = summary.getGenomicDataSources();
        assertEquals(1, genomicSources.size());
        DisplayableGenomicSource genomicSource = genomicSources.get(0);
        assertEquals(2, genomicSource.getPlatforms().size());
        assertEquals(2, genomicSource.getNumberSamples());
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
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetEntityIllegalClass() {
        Object object = new Object();
        workspaceService.getRefreshedEntity(object);
    }

}
