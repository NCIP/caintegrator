/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.ajax;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.application.analysis.AnalysisServiceStub;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.application.workspace.WorkspaceServiceStub;
import gov.nih.nci.caintegrator2.domain.application.AnalysisJobStatusEnum;
import gov.nih.nci.caintegrator2.domain.application.ComparativeMarkerSelectionAnalysisJob;
import gov.nih.nci.caintegrator2.domain.application.GenePatternAnalysisJob;
import gov.nih.nci.caintegrator2.domain.application.GisticAnalysisJob;
import gov.nih.nci.caintegrator2.domain.application.PrincipalComponentAnalysisJob;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.application.UserWorkspace;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.web.action.AbstractSessionBasedTest;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;

import org.directwebremoting.WebContextFactory;
import org.junit.Before;
import org.junit.Test;


public class PersistedAnalysisJobAjaxUpdaterTest extends AbstractSessionBasedTest {

    private PersistedAnalysisJobAjaxUpdater updater;
    private DwrUtilFactory dwrUtilFactory;
    private WorkspaceServiceGPJobStub workspaceService;
    private AnalysisServiceStub analysisService;
    private ComparativeMarkerSelectionAnalysisJob cmsJob;
    private GenePatternAnalysisJob gpJob;
    private PrincipalComponentAnalysisJob pcaJob;
    private GisticAnalysisJob gisticJob;

    @Before
    public void setUp() {
        super.setUp();
        updater = new PersistedAnalysisJobAjaxUpdater();
        dwrUtilFactory = new DwrUtilFactory();
        workspaceService = new WorkspaceServiceGPJobStub();
        analysisService = new AnalysisServiceStub();
        analysisService.clear();
        workspaceService.clear();
        updater.setWorkspaceService(workspaceService);
        updater.setDwrUtilFactory(dwrUtilFactory);
        updater.setAnalysisService(analysisService);
        WebContextFactory.setWebContextBuilder(new WebContextBuilderStub());
        setupJobs();
    }
    
    private void setupJobs() {
        cmsJob = new ComparativeMarkerSelectionAnalysisJob();
        cmsJob.setName("Job");
        cmsJob.setStatus(AnalysisJobStatusEnum.SUBMITTED);
        cmsJob.setCreationDate(new Date());
        cmsJob.setLastUpdateDate(new Date());
        cmsJob.setId(Long.valueOf(1));
        gpJob = new GenePatternAnalysisJob();
        gpJob.setName("Job");
        gpJob.setStatus(AnalysisJobStatusEnum.SUBMITTED);
        gpJob.setCreationDate(new Date());
        gpJob.setId(Long.valueOf(1));
        pcaJob = new PrincipalComponentAnalysisJob();
        pcaJob.setName("Job");
        pcaJob.setStatus(AnalysisJobStatusEnum.SUBMITTED);
        pcaJob.setCreationDate(new Date());
        pcaJob.setId(Long.valueOf(1));
        gisticJob = new GisticAnalysisJob();
        gisticJob.setName("Job");
        gisticJob.setStatus(AnalysisJobStatusEnum.SUBMITTED);
        gisticJob.setCreationDate(new Date());
        gisticJob.setId(Long.valueOf(1));
    }

    @Test
    public void testInitializeJsp() throws InterruptedException, ServletException, IOException {
        updater.initializeJsp();
        assertNotNull(dwrUtilFactory.retrieveAnalysisJobUtil("Test"));
    }
    
    @Test
    public void testRunJob() throws InterruptedException {
        UserWorkspace workspace = workspaceService.getWorkspace();
        StudySubscription subscription = new StudySubscription();
        subscription.setUserWorkspace(workspace);
        cmsJob.setSubscription(subscription);
        gpJob.setSubscription(subscription);
        pcaJob.setSubscription(subscription);
        gisticJob.setSubscription(subscription);
        updater.runJob(cmsJob);
        Thread.sleep(500);
        assertTrue(analysisService.executeComparativeMarkerSelectionJobCalled);
        assertTrue(AnalysisJobStatusEnum.COMPLETED.equals(cmsJob.getStatus()));
        
        updater.runJob(gpJob);
        Thread.sleep(500);
        assertTrue(analysisService.executeGenePatternJobCalled);
        assertTrue(AnalysisJobStatusEnum.COMPLETED.equals(gpJob.getStatus()));
        
        updater.runJob(pcaJob);
        Thread.sleep(500);
        assertTrue(analysisService.executePcaJobCalled);
        assertTrue(AnalysisJobStatusEnum.COMPLETED.equals(pcaJob.getStatus()));
        
        updater.runJob(gisticJob);
        Thread.sleep(500);
        assertTrue(analysisService.executeGisticJobCalled);
        assertTrue(AnalysisJobStatusEnum.PROCESSING_LOCALLY.equals(gisticJob.getStatus()));

    }
    
    private final class WorkspaceServiceGPJobStub extends WorkspaceServiceStub {
        @Override
        public UserWorkspace getWorkspace() {
            UserWorkspace workspace = new UserWorkspace();
            workspace.setUsername("Test");
            StudySubscription subscription = new StudySubscription();
            subscription.setId(Long.valueOf(1));
            subscription.setUserWorkspace(workspace);
            workspace.getSubscriptionCollection().add(subscription);
            cmsJob.setSubscription(subscription);
            gpJob.setSubscription(subscription);
            subscription.getAnalysisJobCollection().add(cmsJob);
            subscription.getAnalysisJobCollection().add(gpJob);
            subscription.setStudy(new Study());
            subscription.getStudy().setStudyConfiguration(new StudyConfiguration());
            return workspace;
        }
    }

}
