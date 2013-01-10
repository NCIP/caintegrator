/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
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
import gov.nih.nci.caintegrator2.TestDataFiles;
import gov.nih.nci.caintegrator2.data.CaIntegrator2DaoStub;
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.matchers.JUnitMatchers;
import org.junit.rules.ExpectedException;


/**
 * Tests for annotation files.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
public class AnnotationFileTest {
    @Rule
    public ExpectedException expected = ExpectedException.none();

    /**
     * Test method for {@link gov.nih.nci.caintegrator2.application.study.DelimitedTextannotationFile#validateFile()}.
     * @throws ValidationException
     * @throws FileNotFoundException
     */
    @Test
    public void load() throws Exception {
        AnnotationFile annotationFile = createAnnotationFile(VALID_FILE);
        assertNotNull(annotationFile);
        assertEquals(5, annotationFile.getColumns().size());
        assertEquals("ID", annotationFile.getColumns().get(0).getName());
        assertEquals("Col1", annotationFile.getColumns().get(1).getName());
        assertEquals("Col2", annotationFile.getColumns().get(2).getName());
        assertEquals("Col3", annotationFile.getColumns().get(3).getName());
    }

    @Test
    public void loadMissingValue() throws Exception {
        expected.expect(ValidationException.class);
        expected.expectMessage("Number of values in line 3 inconsistent with header line. Expected 4 but found 3 values.");
        createAnnotationFile(INVALID_FILE_MISSING_VALUE);
    }

    @Test
    public void loadEmptyFile() throws Exception {
        expected.expect(ValidationException.class);
        expected.expectMessage("The data file was empty.");
        createAnnotationFile(INVALID_FILE_EMPTY);
    }

    @Test
    public void loadNoData() throws Exception {
        expected.expect(ValidationException.class);
        expected.expectMessage("The data file contained no data (header line only).");
        createAnnotationFile(INVALID_FILE_NO_DATA);
    }

    @Test
    public void loadMissingFile() throws Exception {
        expected.expect(ValidationException.class);
        expected.expectMessage("The file " + INVALID_FILE_DOESNT_EXIST.getAbsolutePath() + " could not be found");
        createAnnotationFile(INVALID_FILE_DOESNT_EXIST);
    }

    @Test
    public void getDescriptors() throws Exception {
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
    public void positionAtData() throws Exception {
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
    public void dataValue() throws Exception {
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
    public void checkForValidIdentifierColumn() throws Exception {
        AnnotationFile annotationFile = createAnnotationFile(VALID_FILE);
        annotationFile.setIdentifierColumn(annotationFile.getColumns().get(0));
        annotationFile.getIdentifierColumn().checkValidIdentifierColumn();
    }

    @Test
    public void checkForInvalidIdentifierColumn() throws Exception {
        expected.expect(ValidationException.class);
        expected.expectMessage("This column cannot be an identifier column because it has a duplicate value");

        AnnotationFile annotationFile = createAnnotationFile(VALID_FILE);
        annotationFile.setIdentifierColumn(annotationFile.getColumns().get(2));
        annotationFile.getIdentifierColumn().checkValidIdentifierColumn();
    }

    @Test
    public void tooLongIdsThrowsValidationException() throws Exception {
        expected.expect(ValidationException.class);
        expected.expectMessage(JUnitMatchers.containsString("Identifiers can only be up to"));

        AnnotationFile annotationFile = createAnnotationFile(TestDataFiles.INVALID_FILE_TOO_LONG_IDS);
        annotationFile.setIdentifierColumn(annotationFile.getColumns().get(0));
        annotationFile.loadAnnontation(new SubjectAnnotationHandler(
                new DelimitedTextClinicalSourceConfiguration(annotationFile, new StudyConfiguration())));
    }

    @Test
    public void duplcateIdsThrowsValidationException() throws IOException, ValidationException {
        expected.expect(ValidationException.class);
        expected.expectMessage(JUnitMatchers.containsString("Multiples identifiers found for"));

        AnnotationFile annotationFile = createAnnotationFile(TestDataFiles.INVALID_FILE_DUPLICATE_IDS);
        annotationFile.setIdentifierColumn(annotationFile.getColumns().get(0));
        annotationFile.loadAnnontation(new SubjectAnnotationHandler(
                new DelimitedTextClinicalSourceConfiguration(annotationFile, new StudyConfiguration())));
    }

    private AnnotationFile createAnnotationFile(File file) throws ValidationException, IOException {
        return AnnotationFile.load(file, new CaIntegrator2DaoStub(), new StudyConfiguration(), EntityTypeEnum.SUBJECT, true);
    }
}
