/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.action.study.management;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import gov.nih.nci.caintegrator.TestDataFiles;
import gov.nih.nci.caintegrator.application.study.ImageAnnotationConfiguration;
import gov.nih.nci.caintegrator.application.study.ImageDataSourceMappingTypeEnum;
import gov.nih.nci.caintegrator.application.study.StudyManagementServiceStub;
import gov.nih.nci.caintegrator.external.ConnectionException;
import gov.nih.nci.caintegrator.external.InvalidImagingCollectionException;
import gov.nih.nci.caintegrator.external.ServerConnectionProfile;
import gov.nih.nci.caintegrator.web.action.AbstractSessionBasedTest;
import gov.nih.nci.caintegrator.web.action.study.management.EditImagingSourceAction;
import gov.nih.nci.caintegrator.web.ajax.IImagingDataSourceAjaxUpdater;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import com.opensymphony.xwork2.Action;

/**
 *
 */
public class EditImagingSourceActionTest extends AbstractSessionBasedTest {

    private EditImagingSourceAction action;
    private StudyManagementServiceStub studyManagementServiceStub;
    private IImagingDataSourceAjaxUpdater updater;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        updater = mock(IImagingDataSourceAjaxUpdater.class);

        studyManagementServiceStub = new StudyManagementServiceStub();
        action = new EditImagingSourceAction();
        action.setStudyManagementService(studyManagementServiceStub);
        action.setWorkspaceService(workspaceService);
        action.setUpdater(updater);
        action.setNciaFacade(nciaFacade);
    }

    @Test
    public void testValidate() {
        action.getImageSourceConfiguration().getServerProfile().setUrl("http://test, http://test2");
        action.setCancelAction(true);
        action.validate();
        assertEquals("http://test, http://test2", action.getImageSourceConfiguration().getServerProfile().getUrl());
        action.setCancelAction(false);
        action.validate();
        assertEquals("http://test", action.getImageSourceConfiguration().getServerProfile().getUrl());

    }

    @Test
    public void testExecute() {
        assertEquals(Action.SUCCESS, action.execute());
    }

    @Test
    public void testPrepare() {
        action.getImageSourceConfiguration().setImageAnnotationConfiguration(new ImageAnnotationConfiguration());
        action.getImageSourceConfiguration().setId(1L);
        action.getImageSourceConfiguration().getImageAnnotationConfiguration().setId(1L);
        action.prepare();
        assertTrue(action.isFileUpload());
        assertFalse(action.hasActionErrors());
        assertTrue(studyManagementServiceStub.getRefreshedStudyEntityCalled);
    }

    private void validateAddSource() {
        action.saveImagingSource();
        assertTrue(action.hasFieldErrors());

        action.getImageSourceConfiguration().getServerProfile().setUrl("Fake URL");
        action.setMappingType(ImageDataSourceMappingTypeEnum.AUTO.getValue());
        action.clearErrorsAndMessages();
        action.saveImagingSource();
        assertTrue(action.hasFieldErrors());

        action.clearErrorsAndMessages();
        action.getImageSourceConfiguration().getServerProfile().setWebUrl("http://someurl.nci.nih.gov/");
        action.setMappingType(ImageDataSourceMappingTypeEnum.AUTO.getValue());
        action.clearErrorsAndMessages();
        action.saveImagingSource();
        assertTrue(action.hasFieldErrors());

        action.clearErrorsAndMessages();
        action.getImageSourceConfiguration().getServerProfile().setWebUrl("http://someurl.nci.nih.gov/ncia");
        action.setMappingType(ImageDataSourceMappingTypeEnum.AUTO.getValue());
        action.clearErrorsAndMessages();
        action.saveImagingSource();
        assertTrue(action.hasFieldErrors());

        action.clearErrorsAndMessages();
        action.saveImagingSource();
        assertTrue(action.hasFieldErrors());

        // test with INvalid input files
        action.clearErrorsAndMessages();
        action.saveImagingSource();
        assertTrue(action.hasFieldErrors());

        action.setImageClinicalMappingFile(null);
        action.setMappingType(ImageDataSourceMappingTypeEnum.IMAGE_SERIES.getValue());
        action.clearErrorsAndMessages();
        action.saveImagingSource();
        assertTrue(action.hasFieldErrors());


        // test with valid input files
        action.getImageSourceConfiguration().setCollectionName("Fake Collection Name");
        action.setImageClinicalMappingFile(TestDataFiles.VALID_FILE);
        action.clearErrorsAndMessages();
        action.saveImagingSource();
        assertFalse(action.hasFieldErrors());
    }


    @Test
    public void testSaveImagingSource() {
        validateAddSource();
        action.setImageClinicalMappingFile(TestDataFiles.VALID_FILE);
        assertEquals(Action.SUCCESS, action.saveImagingSource());
        verify(updater, atLeastOnce()).runJob(anyLong(), any(File.class), any(ImageDataSourceMappingTypeEnum.class),
                anyBoolean(), anyBoolean());
        action.getImageSourceConfiguration().setId(1l);
        assertEquals(Action.SUCCESS, action.saveImagingSource());
        assertTrue(studyManagementServiceStub.deleteCalled);
        verify(updater, atLeast(2)).runJob(anyLong(), any(File.class), any(ImageDataSourceMappingTypeEnum.class),
                anyBoolean(), anyBoolean());
    }

    @Test
    public void saveConnectionError() throws Exception {
        validateAddSource();
        action.setImageClinicalMappingFile(TestDataFiles.VALID_FILE);
        assertEquals(Action.SUCCESS, action.saveImagingSource());
        verify(updater, atLeastOnce()).runJob(anyLong(), any(File.class), any(ImageDataSourceMappingTypeEnum.class),
                anyBoolean(), anyBoolean());
        doThrow(new ConnectionException("Invalid Connection."))
            .when(nciaFacade).validateImagingSourceConnection(any(ServerConnectionProfile.class), anyString());
        assertEquals(Action.INPUT, action.saveImagingSource());
    }

    public void saveIvalidImagingCollectionError() throws Exception {
        validateAddSource();
        action.setImageClinicalMappingFile(TestDataFiles.VALID_FILE);
        assertEquals(Action.SUCCESS, action.saveImagingSource());
        verify(updater, times(1)).runJob(anyLong(), any(File.class), any(ImageDataSourceMappingTypeEnum.class),
                anyBoolean(), anyBoolean());
        doThrow(new InvalidImagingCollectionException("Invalid Imaging Collection"))
            .when(nciaFacade).validateImagingSourceConnection(any(ServerConnectionProfile.class), anyString());
        assertEquals(Action.INPUT, action.saveImagingSource());
    }

    @Test
    public void testLoadImageAnnotations() {
        assertEquals(Action.SUCCESS, action.loadImageAnnotations());
        assertTrue(studyManagementServiceStub.loadImageAnnotationCalled);
        studyManagementServiceStub.throwValidationException = true;
        assertEquals(Action.ERROR, action.loadImageAnnotations());
    }

    @Test
    public void testDelete() {
        assertEquals(Action.SUCCESS, action.delete());
        assertTrue(studyManagementServiceStub.deleteCalled);
        studyManagementServiceStub.throwValidationException = true;
        assertEquals(Action.ERROR, action.delete());
    }

    @Test
    public void testMapImagingSource() {
        action.setImageClinicalMappingFile(null);
        action.setMappingType(ImageDataSourceMappingTypeEnum.IMAGE_SERIES.getValue());
        assertEquals(Action.INPUT, action.mapImagingSource());

        action.setImageClinicalMappingFile(TestDataFiles.VALID_FILE);
        action.setImageClinicalMappingFileFileName("valid");
        action.clearErrorsAndMessages();
        studyManagementServiceStub.clear();
        assertEquals(Action.SUCCESS, action.mapImagingSource());
        verify(updater, times(1)).runJob(anyLong(), any(File.class), any(ImageDataSourceMappingTypeEnum.class),
                anyBoolean(), anyBoolean());
    }

    @Test
    public void testGetSetMappingType() {
        action.setMappingType(null);
        assertEquals("", action.getMappingType());
        action.setMappingType("invalid");
        assertEquals("", action.getMappingType());
        action.setMappingType(ImageDataSourceMappingTypeEnum.IMAGE_SERIES.getValue());
        assertEquals(ImageDataSourceMappingTypeEnum.IMAGE_SERIES.getValue(), action.getMappingType());
    }
}
