/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.ajax;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import gov.nih.nci.caintegrator.application.study.ImageDataSourceConfiguration;
import gov.nih.nci.caintegrator.application.study.ImageDataSourceMappingTypeEnum;
import gov.nih.nci.caintegrator.application.study.Status;
import gov.nih.nci.caintegrator.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator.application.study.StudyManagementServiceStub;
import gov.nih.nci.caintegrator.data.StudyHelper;
import gov.nih.nci.caintegrator.domain.application.UserWorkspace;
import gov.nih.nci.caintegrator.web.SessionHelper;
import gov.nih.nci.caintegrator.web.action.AbstractSessionBasedTest;
import gov.nih.nci.caintegrator.web.ajax.DwrUtilFactory;
import gov.nih.nci.caintegrator.web.ajax.ImagingDataSourceAjaxUpdater;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;

import org.directwebremoting.WebContextFactory;
import org.junit.Before;
import org.junit.Test;


public class ImagingDataSourceAjaxUpdaterTest extends AbstractSessionBasedTest {

    private ImagingDataSourceAjaxUpdater updater;
    private DwrUtilFactory dwrUtilFactory;
    private StudyManagementServiceStub studyManagementServiceStub;
    private StudyConfiguration studyConfiguration;
    private ImageDataSourceConfiguration imagingDataSource;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        updater = new ImagingDataSourceAjaxUpdater();
        dwrUtilFactory = new DwrUtilFactory();
        studyManagementServiceStub = new StudyManagementServiceStub();
        studyManagementServiceStub.clear();
        updater.setWorkspaceService(workspaceService);
        updater.setDwrUtilFactory(dwrUtilFactory);
        updater.setStudyManagementService(studyManagementServiceStub);
        WebContextFactory.setWebContextBuilder(webContextBuilder);
        StudyHelper studyHelper = new StudyHelper();
        studyConfiguration = studyHelper.populateAndRetrieveStudyWithSourceConfigurations().getStudyConfiguration();
        studyConfiguration.setId(Long.valueOf(1));
        SessionHelper.getInstance().getDisplayableUserWorkspace().setCurrentStudyConfiguration(studyConfiguration);
        imagingDataSource = studyConfiguration.getImageDataSources().get(0);
        imagingDataSource.setId(Long.valueOf(1));
        imagingDataSource.setStudyConfiguration(studyConfiguration);
        studyManagementServiceStub.refreshedImageSource = imagingDataSource;
        imagingDataSource.setStatus(Status.PROCESSING);

        UserWorkspace workspace = new UserWorkspace();
        workspace.setUsername("Test");
        workspace.getSubscriptionCollection().add(getStudySubscription());
        studyConfiguration.setUserWorkspace(workspace);
        when(workspaceService.getWorkspace()).thenReturn(workspace);
    }

    @Test
    public void testInitializeJsp() throws InterruptedException, ServletException, IOException {
        updater.initializeJsp();
        assertNotNull(dwrUtilFactory.retrieveGenomicDataSourceUtil("Test"));
    }

    @Test
    public void testRunJob() throws InterruptedException {
        // Test load image clinical mapping
        studyConfiguration.setUserWorkspace(workspaceService.getWorkspace());
        studyConfiguration.setLastModifiedBy(workspaceService.getWorkspace());
        File dummyImageClinicalMappingFile = new File("");
        updater.runJob(1l, dummyImageClinicalMappingFile , ImageDataSourceMappingTypeEnum.AUTO, false, false);
        Thread.sleep(500);
        assertTrue(studyManagementServiceStub.loadImageSourceCalled);
        assertTrue(studyManagementServiceStub.mapImageSeriesCalled);
        assertTrue(studyManagementServiceStub.getRefreshedImageSourceCalled);
        studyManagementServiceStub.clear();

        studyManagementServiceStub.throwValidationException = true;
        updater.runJob(1l, dummyImageClinicalMappingFile, ImageDataSourceMappingTypeEnum.AUTO, false, false);
        Thread.sleep(500);
        assertEquals(Status.ERROR, imagingDataSource.getStatus());
        assertTrue(studyManagementServiceStub.daoSaveCalled);

        studyManagementServiceStub.clear();
        studyManagementServiceStub.throwIOException = true;
        updater.runJob(1l, dummyImageClinicalMappingFile, ImageDataSourceMappingTypeEnum.AUTO, false, false);
        Thread.sleep(500);
        assertEquals(Status.ERROR, imagingDataSource.getStatus());
        assertTrue(studyManagementServiceStub.daoSaveCalled);
    }

    @Test
    public void testRunJobAIM() throws InterruptedException {
        // Test load AIM annotation
        studyConfiguration.setUserWorkspace(workspaceService.getWorkspace());
        studyConfiguration.setLastModifiedBy(workspaceService.getWorkspace());
        updater.runJob(1l, null, null, false, true);
        Thread.sleep(500);
        assertTrue(studyManagementServiceStub.loadAimAnnotationsCalled);
        assertTrue(studyManagementServiceStub.getRefreshedImageSourceCalled);
        studyManagementServiceStub.clear();

        studyManagementServiceStub.throwValidationException = true;
        updater.runJob(1l, null, null, false, true);
        Thread.sleep(500);
        assertEquals(Status.ERROR, imagingDataSource.getStatus());
        assertTrue(studyManagementServiceStub.daoSaveCalled);

        studyManagementServiceStub.clear();
        studyManagementServiceStub.throwConnectionException = true;
        updater.runJob(1l, null, null, false, true);
        Thread.sleep(500);
        assertEquals(Status.ERROR, imagingDataSource.getStatus());
        assertTrue(studyManagementServiceStub.daoSaveCalled);
    }
}
