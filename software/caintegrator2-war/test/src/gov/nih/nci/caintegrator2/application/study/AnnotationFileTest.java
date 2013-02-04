/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.study;

import static gov.nih.nci.caintegrator2.TestDataFiles.INVALID_FILE_DOESNT_EXIST;
import static gov.nih.nci.caintegrator2.TestDataFiles.INVALID_FILE_EMPTY;
import static gov.nih.nci.caintegrator2.TestDataFiles.INVALID_FILE_MISSING_VALUE;
import static gov.nih.nci.caintegrator2.TestDataFiles.INVALID_FILE_NO_DATA;
import static gov.nih.nci.caintegrator2.TestDataFiles.VALID_FILE;
import static gov.nih.nci.caintegrator2.TestDataFiles.VALID_FILE_TIMEPOINT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import gov.nih.nci.caintegrator2.data.CaIntegrator2DaoStub;
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;


public class AnnotationFileTest {
    
    private List<AnnotationFieldDescriptor> testAnnotationFieldDescriptors;
    
    @Before
    public void setUp() throws Exception {
        setupTestAnnotations();
    }
    
    private AnnotationFile createAnnotationFile(File file)
        throws ValidationException, IOException {
        return AnnotationFile.load(file, new CaIntegrator2DaoStub(), new StudyConfiguration(), EntityTypeEnum.SUBJECT, false);
    }

    /**
     * Test method for {@link gov.nih.nci.caintegrator2.application.study.DelimitedTextannotationFile#validateFile()}.
     * @throws ValidationException 
     * @throws FileNotFoundException 
     */
    @Test
    public void testLoad() throws ValidationException, IOException {
        AnnotationFile annotationFile = createAnnotationFile(VALID_FILE);
        assertNotNull(annotationFile);
        assertEquals(5, annotationFile.getColumns().size());
        assertEquals("ID", annotationFile.getColumns().get(0).getName());
        assertEquals("Col1", annotationFile.getColumns().get(1).getName());
        assertEquals("Col2", annotationFile.getColumns().get(2).getName());
        assertEquals("Col3", annotationFile.getColumns().get(3).getName());
        checkInvalid(INVALID_FILE_MISSING_VALUE, "Number of values in line 3 inconsistent with header line. Expected 4 but found 3 values.");
        checkInvalid(INVALID_FILE_EMPTY, "The data file was empty.");
        checkInvalid(INVALID_FILE_NO_DATA, "The data file contained no data (header line only).");
        checkInvalid(INVALID_FILE_DOESNT_EXIST, "The file " + INVALID_FILE_DOESNT_EXIST.getAbsolutePath() + " could not be found");
    }

    private void checkInvalid(File file, String expectedMessage) {
        try {
            createAnnotationFile(file);
            fail("ValidationException expected");
        } catch (ValidationException e) {
            assertEquals(expectedMessage, e.getResult().getInvalidMessage());
        } catch (IOException e) {
            assertEquals(expectedMessage, e.getMessage());
        }
    }

    @Test
    public void testGetDescriptors() throws ValidationException, IOException {
        AnnotationFile annotationFile = createAnnotationFile(VALID_FILE_TIMEPOINT);
        annotationFile.setIdentifierColumn(annotationFile.getColumns().get(0));
        annotationFile.setTimepointColumn(annotationFile.getColumns().get(1));
        List<AnnotationFieldDescriptor> descriptors = annotationFile.getDescriptors();
        assertEquals(AnnotationFieldType.IDENTIFIER, descriptors.get(0).getType());
        assertEquals(AnnotationFieldType.TIMEPOINT, descriptors.get(1).getType());
        assertEquals(AnnotationFieldType.ANNOTATION, descriptors.get(2).getType());
        assertEquals("Col2", descriptors.get(3).getName());
        assertEquals("Col3", descriptors.get(4).getName());
    }

    @Test
    public void testPositionAtData() throws ValidationException, IOException {
        AnnotationFile annotationFile = createAnnotationFile(VALID_FILE);
        annotationFile.setId(Long.valueOf(1));
        assertTrue(annotationFile.hasNextDataLine());
        assertEquals("100", annotationFile.getDataValue(annotationFile.getColumns().get(0)));
        assertTrue(annotationFile.hasNextDataLine());
        assertEquals("101", annotationFile.getDataValue(annotationFile.getColumns().get(0)));
        assertFalse(annotationFile.hasNextDataLine());
        annotationFile.positionAtData();
        assertTrue(annotationFile.hasNextDataLine());
        assertEquals("100", annotationFile.getDataValue(annotationFile.getColumns().get(0)));
    }

    @Test
    public void testDataValue() throws ValidationException, IOException {
        AnnotationFile annotationFile = createAnnotationFile(VALID_FILE);
        annotationFile.setIdentifierColumn(annotationFile.getColumns().get(0));
        annotationFile.positionAtData();
        
        assertTrue(annotationFile.hasNextDataLine());
        assertEquals("100", annotationFile.getDataValue(annotationFile.getIdentifierColumn()));
        assertEquals("1", annotationFile.getDataValue(annotationFile.getColumns().get(1).getFieldDescriptor()));
        assertEquals("g", annotationFile.getDataValue(annotationFile.getColumns().get(2).getFieldDescriptor()));
        assertEquals("N", annotationFile.getDataValue(annotationFile.getColumns().get(3).getFieldDescriptor()));
        assertEquals("1", annotationFile.getDataValue(annotationFile.getColumns().get(1)));
        assertEquals("g", annotationFile.getDataValue(annotationFile.getColumns().get(2)));
        assertEquals("N", annotationFile.getDataValue(annotationFile.getColumns().get(3)));
        
        assertTrue(annotationFile.hasNextDataLine());
        assertEquals("101", annotationFile.getDataValue(annotationFile.getIdentifierColumn()));
        assertEquals("3", annotationFile.getDataValue(annotationFile.getColumns().get(1).getFieldDescriptor()));
        assertEquals("g", annotationFile.getDataValue(annotationFile.getColumns().get(2).getFieldDescriptor()));
        assertEquals("Y", annotationFile.getDataValue(annotationFile.getColumns().get(3).getFieldDescriptor()));
        assertEquals("3", annotationFile.getDataValue(annotationFile.getColumns().get(1)));
        assertEquals("g", annotationFile.getDataValue(annotationFile.getColumns().get(2)));
        assertEquals("Y", annotationFile.getDataValue(annotationFile.getColumns().get(3)));
        
        assertFalse(annotationFile.hasNextDataLine());
    }
    
    @Test
    public void testCheckValidIdentifierColumn() throws IOException, ValidationException {
        AnnotationFile annotationFile = createAnnotationFile(VALID_FILE);
        try {
            annotationFile.setIdentifierColumn(annotationFile.getColumns().get(0));
            annotationFile.getIdentifierColumn().checkValidIdentifierColumn();
        } catch (ValidationException e) {
            fail(); // Should be valid.
        }
        try {
            annotationFile.setIdentifierColumn(annotationFile.getColumns().get(2));
            annotationFile.getIdentifierColumn().checkValidIdentifierColumn();
            fail();
        } catch(ValidationException e) {
            // noop - it should catch exception because it contains 2 of the same identifiers.
        }
    }

    private void setupTestAnnotations() {
        testAnnotationFieldDescriptors = new ArrayList<AnnotationFieldDescriptor>();
        
        AnnotationFieldDescriptor testAnnotationFieldDescriptor = new AnnotationFieldDescriptor();
        testAnnotationFieldDescriptor.setId(Long.valueOf(1));
        testAnnotationFieldDescriptor.setName("Col1");
        testAnnotationFieldDescriptors.add(testAnnotationFieldDescriptor);
        
        testAnnotationFieldDescriptor = new AnnotationFieldDescriptor();
        testAnnotationFieldDescriptor.setId(Long.valueOf(2));
        testAnnotationFieldDescriptor.setName("Col2");
        testAnnotationFieldDescriptors.add(testAnnotationFieldDescriptor);
        
        testAnnotationFieldDescriptor = new AnnotationFieldDescriptor();
        testAnnotationFieldDescriptor.setId(Long.valueOf(3));
        testAnnotationFieldDescriptor.setName("Col3");
        testAnnotationFieldDescriptors.add(testAnnotationFieldDescriptor);
    }
}
