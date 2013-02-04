/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.study;

import static gov.nih.nci.caintegrator2.TestDataFiles.*;
import static org.junit.Assert.*;
import gov.nih.nci.caintegrator2.data.CaIntegrator2DaoStub;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
        return AnnotationFile.load(file, new CaIntegrator2DaoStub());
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
        List<AnnotationFieldDescriptor> emptyList = Collections.emptyList();
        annotationFile.loadDescriptors(emptyList);
        validateAnnotationFieldDescriptor(testAnnotationFieldDescriptors, annotationFile.getDescriptors());
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
        annotationFile.loadDescriptors(testAnnotationFieldDescriptors);
        
        assertTrue(annotationFile.hasNextDataLine());
        assertEquals("100", annotationFile.getDataValue(annotationFile.getIdentifierColumn()));
        assertEquals("1", annotationFile.getDataValue(testAnnotationFieldDescriptors.get(0)));
        assertEquals("g", annotationFile.getDataValue(testAnnotationFieldDescriptors.get(1)));
        assertEquals("N", annotationFile.getDataValue(testAnnotationFieldDescriptors.get(2)));
        assertEquals("1", annotationFile.getDataValue(annotationFile.getColumns().get(1)));
        assertEquals("g", annotationFile.getDataValue(annotationFile.getColumns().get(2)));
        assertEquals("N", annotationFile.getDataValue(annotationFile.getColumns().get(3)));
        
        assertTrue(annotationFile.hasNextDataLine());
        assertEquals("101", annotationFile.getDataValue(annotationFile.getIdentifierColumn()));
        assertEquals("3", annotationFile.getDataValue(testAnnotationFieldDescriptors.get(0)));
        assertEquals("g", annotationFile.getDataValue(testAnnotationFieldDescriptors.get(1)));
        assertEquals("Y", annotationFile.getDataValue(testAnnotationFieldDescriptors.get(2)));
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
    
    
    private void validateAnnotationFieldDescriptor(
                List<AnnotationFieldDescriptor> testAnnotations, 
                List<AnnotationFieldDescriptor> realAnnotations) {
        for (int x = 0; x < testAnnotations.size(); x++) {
            AnnotationFieldDescriptor testAnnotationDescriptor = testAnnotations.get(x);
            AnnotationFieldDescriptor realAnnotationDescriptor = realAnnotations.get(x);
            assertEquals(testAnnotationDescriptor.getName(), realAnnotationDescriptor.getName());
        }
    }
   
}
