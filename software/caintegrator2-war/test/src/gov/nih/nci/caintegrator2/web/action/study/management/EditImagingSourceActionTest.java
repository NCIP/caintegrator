/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.study.management;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.TestDataFiles;
import gov.nih.nci.caintegrator2.application.study.ImageAnnotationConfiguration;
import gov.nih.nci.caintegrator2.application.study.ImageDataSourceMappingTypeEnum;
import gov.nih.nci.caintegrator2.application.study.StudyManagementServiceStub;
import gov.nih.nci.caintegrator2.web.action.AbstractSessionBasedTest;
import gov.nih.nci.caintegrator2.web.ajax.IImagingDataSourceAjaxUpdater;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.opensymphony.xwork2.Action;

/**
 * 
 */
public class EditImagingSourceActionTest extends AbstractSessionBasedTest {

    private EditImagingSourceAction action;
    private StudyManagementServiceStub studyManagementServiceStub;
    private ImagingDataSourceAjaxUpdaterStub updaterStub;

    @Before
    public void setUp() {
        super.setUp();
        ApplicationContext context = new ClassPathXmlApplicationContext("study-management-action-test-config.xml", EditClinicalSourceActionTest.class); 
        action = (EditImagingSourceAction) context.getBean("editImagingSourceAction");
        studyManagementServiceStub = (StudyManagementServiceStub) context.getBean("studyManagementService");
        studyManagementServiceStub.clear();
        action.clearErrorsAndMessages();
        updaterStub = new ImagingDataSourceAjaxUpdaterStub();
        updaterStub.clear();
        action.setUpdater(updaterStub);
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
        action.setImageAnnotationFile(TestDataFiles.INVALID_FILE_DOESNT_EXIST);
        action.clearErrorsAndMessages();
        action.saveImagingSource();
        assertTrue(action.hasFieldErrors());

        action.setImageAnnotationFile(TestDataFiles.VALID_FILE);
        action.clearErrorsAndMessages();
        action.saveImagingSource();
        assertTrue(action.hasFieldErrors());         

        // test with INvalid input files
        action.setImageAnnotationFile(TestDataFiles.VALID_FILE);
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
        action.setImageAnnotationFile(TestDataFiles.VALID_FILE);
        action.setImageClinicalMappingFile(TestDataFiles.VALID_FILE);
        action.clearErrorsAndMessages();
        action.saveImagingSource();
        assertFalse(action.hasFieldErrors());
    }


    @Test
    public void testSaveImagingSource() {
        validateAddSource();
        action.setImageAnnotationFile(null);
        action.setImageClinicalMappingFile(TestDataFiles.VALID_FILE);
        assertEquals(Action.SUCCESS, action.saveImagingSource());
        assertTrue(updaterStub.runJobCalled);
        updaterStub.clear();
        action.getImageSourceConfiguration().setId(1l);
        assertEquals(Action.SUCCESS, action.saveImagingSource());
        assertTrue(studyManagementServiceStub.deleteCalled);
        assertTrue(updaterStub.runJobCalled);        
    }
    
    @Test
    public void testAddImageAnnotations() {
        action.clearErrorsAndMessages();
        action.setImageAnnotationFile(TestDataFiles.VALID_FILE);
        action.setImageAnnotationFileFileName(TestDataFiles.VALID_FILE.getName());
        assertEquals(Action.SUCCESS, action.addImageAnnotations());
        action.setImageClinicalMappingFile(TestDataFiles.VALID_FILE);
        assertTrue(studyManagementServiceStub.addImageAnnotationFileCalled);

        action.setImageAnnotationFile(TestDataFiles.INVALID_FILE_MISSING_VALUE);
        assertEquals(Action.INPUT, action.addImageAnnotations());
        action.clearErrorsAndMessages();
        action.setImageAnnotationFile(TestDataFiles.INVALID_FILE_DOESNT_EXIST);
        assertEquals(Action.ERROR, action.addImageAnnotations());
        action.clearErrorsAndMessages();
        action.setImageAnnotationFile(null);
        assertEquals(Action.INPUT, action.addImageAnnotations());
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
        assertTrue(updaterStub.runJobCalled);
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
    
    private static class ImagingDataSourceAjaxUpdaterStub implements IImagingDataSourceAjaxUpdater {
        
        public boolean runJobCalled = false;
        
        public void clear() {
            runJobCalled = false;
        }
        
        public void initializeJsp() {
            
        }

        public void runJob(Long imagingSourceId, File imageClinicalMappingFile,
                ImageDataSourceMappingTypeEnum mappingType, boolean mapOnly) {
            runJobCalled = true;
        }
        
    }
}
