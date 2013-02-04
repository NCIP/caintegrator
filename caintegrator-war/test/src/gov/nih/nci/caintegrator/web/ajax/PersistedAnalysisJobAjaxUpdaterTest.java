/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.ajax;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import gov.nih.nci.caintegrator.application.analysis.AnalysisMethodInvocation;
import gov.nih.nci.caintegrator.application.analysis.StatusUpdateListener;
import gov.nih.nci.caintegrator.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator.domain.application.AnalysisJobStatusEnum;
import gov.nih.nci.caintegrator.domain.application.ComparativeMarkerSelectionAnalysisJob;
import gov.nih.nci.caintegrator.domain.application.GenePatternAnalysisJob;
import gov.nih.nci.caintegrator.domain.application.GisticAnalysisJob;
import gov.nih.nci.caintegrator.domain.application.PrincipalComponentAnalysisJob;
import gov.nih.nci.caintegrator.domain.application.StudySubscription;
import gov.nih.nci.caintegrator.domain.application.UserWorkspace;
import gov.nih.nci.caintegrator.domain.translational.Study;
import gov.nih.nci.caintegrator.external.ServerConnectionProfile;
import gov.nih.nci.caintegrator.web.action.AbstractSessionBasedTest;
import gov.nih.nci.caintegrator.web.ajax.DwrUtilFactory;
import gov.nih.nci.caintegrator.web.ajax.PersistedAnalysisJobAjaxUpdater;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;

import org.directwebremoting.WebContextFactory;
import org.junit.Before;
import org.junit.Test;


public class PersistedAnalysisJobAjaxUpdaterTest extends AbstractSessionBasedTest {

    private PersistedAnalysisJobAjaxUpdater updater;
    private DwrUtilFactory dwrUtilFactory;
    private ComparativeMarkerSelectionAnalysisJob cmsJob;
    private GenePatternAnalysisJob gpJob;
    private PrincipalComponentAnalysisJob pcaJob;
    private GisticAnalysisJob gisticJob;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        updater = new PersistedAnalysisJobAjaxUpdater();
        dwrUtilFactory = new DwrUtilFactory();
        updater.setWorkspaceService(workspaceService);
        updater.setDwrUtilFactory(dwrUtilFactory);
        updater.setAnalysisService(analysisService);
        WebContextFactory.setWebContextBuilder(webContextBuilder);
        setupJobs();

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
        when(workspaceService.getWorkspace()).thenReturn(workspace);
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
    public void testRunJob() throws Exception {
        UserWorkspace workspace = workspaceService.getWorkspace();
        StudySubscription subscription = new StudySubscription();
        subscription.setUserWorkspace(workspace);
        cmsJob.setSubscription(subscription);
        gpJob.setSubscription(subscription);
        pcaJob.setSubscription(subscription);
        gisticJob.setSubscription(subscription);
        updater.runJob(cmsJob);
        Thread.sleep(500);
        verify(analysisService, times(1)).executeGridPreprocessComparativeMarker(any(StatusUpdateListener.class), any(ComparativeMarkerSelectionAnalysisJob.class));
        assertTrue(AnalysisJobStatusEnum.COMPLETED.equals(cmsJob.getStatus()));

        updater.runJob(gpJob);
        Thread.sleep(500);
        verify(analysisService, times(1)).executeGenePatternJob(any(ServerConnectionProfile.class), any(AnalysisMethodInvocation.class));
        assertTrue(AnalysisJobStatusEnum.COMPLETED.equals(gpJob.getStatus()));

        updater.runJob(pcaJob);
        Thread.sleep(500);
        verify(analysisService, times(1)).executeGridPCA(any(StatusUpdateListener.class), any(PrincipalComponentAnalysisJob.class));
        assertTrue(AnalysisJobStatusEnum.COMPLETED.equals(pcaJob.getStatus()));

        updater.runJob(gisticJob);
        Thread.sleep(500);
        verify(analysisService, times(1)).executeGridGistic(any(StatusUpdateListener.class), any(GisticAnalysisJob.class));
        assertTrue(AnalysisJobStatusEnum.PROCESSING_LOCALLY.equals(gisticJob.getStatus()));

    }
}
