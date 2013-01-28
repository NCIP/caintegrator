/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.application.study;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import gov.nih.nci.caintegrator.TestDataFiles;
import gov.nih.nci.caintegrator.application.study.AnnotationGroupUploadContent;
import gov.nih.nci.caintegrator.application.study.AnnotationGroupUploadFileHandler;
import gov.nih.nci.caintegrator.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator.application.study.ValidationException;


/**
 * 
 */
public class AnnotationGroupUploadFileHandlerTest {

    @Test
    public void testExtractData() {
        List<AnnotationGroupUploadContent> dataUploads = new ArrayList<AnnotationGroupUploadContent>();
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        // Wrong format file
        AnnotationGroupUploadFileHandler fileHandler = new AnnotationGroupUploadFileHandler(
                studyConfiguration, TestDataFiles.CMS_GCT_FILE);
        boolean gotValidationException = false;
        boolean gotIOException = false;
        try {
            dataUploads = fileHandler.extractUploadData();
        } catch (ValidationException e) {
            gotValidationException = true;
        } catch (IOException e) {
            gotIOException = true;
        }
        assertTrue(gotValidationException);
        assertFalse(gotIOException);
        
        // Good file
        fileHandler = new AnnotationGroupUploadFileHandler(
                studyConfiguration, TestDataFiles.ANNOTATION_GROUP_FILE);
        gotValidationException = false;
        gotIOException = false;
        try {
            dataUploads = fileHandler.extractUploadData();
        } catch (ValidationException e) {
            gotValidationException = true;
        } catch (IOException e) {
            gotIOException = true;
        }
        assertFalse(gotValidationException);
        assertFalse(gotIOException);
        assertEquals(5, dataUploads.size());
    }
}
