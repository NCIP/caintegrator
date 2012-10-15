/**
 * The software subject to this notice and license includes both human readable
 * source code form and machine readable, binary, object code form. The caIntegrator2
 * Software was developed in conjunction with the National Cancer Institute
 * (NCI) by NCI employees, 5AM Solutions, Inc. (5AM), ScenPro, Inc. (ScenPro)
 * and Science Applications International Corporation (SAIC). To the extent
 * government employees are authors, any rights in such works shall be subject
 * to Title 17 of the United States Code, section 105.
 *
 * This caIntegrator2 Software License (the License) is between NCI and You. You (or
 * Your) shall mean a person or an entity, and all other entities that control,
 * are controlled by, or are under common control with the entity. Control for
 * purposes of this definition means (i) the direct or indirect power to cause
 * the direction or management of such entity, whether by contract or otherwise,
 * or (ii) ownership of fifty percent (50%) or more of the outstanding shares,
 * or (iii) beneficial ownership of such entity.
 *
 * This License is granted provided that You agree to the conditions described
 * below. NCI grants You a non-exclusive, worldwide, perpetual, fully-paid-up,
 * no-charge, irrevocable, transferable and royalty-free right and license in
 * its rights in the caIntegrator2 Software to (i) use, install, access, operate,
 * execute, copy, modify, translate, market, publicly display, publicly perform,
 * and prepare derivative works of the caIntegrator2 Software; (ii) distribute and
 * have distributed to and by third parties the caIntegrator2 Software and any
 * modifications and derivative works thereof; and (iii) sublicense the
 * foregoing rights set out in (i) and (ii) to third parties, including the
 * right to license such rights to further third parties. For sake of clarity,
 * and not by way of limitation, NCI shall have no right of accounting or right
 * of payment from You or Your sub-licensees for the rights granted under this
 * License. This License is granted at no charge to You.
 *
 * Your redistributions of the source code for the Software must retain the
 * above copyright notice, this list of conditions and the disclaimer and
 * limitation of liability of Article 6, below. Your redistributions in object
 * code form must reproduce the above copyright notice, this list of conditions
 * and the disclaimer of Article 6 in the documentation and/or other materials
 * provided with the distribution, if any.
 *
 * Your end-user documentation included with the redistribution, if any, must
 * include the following acknowledgment: This product includes software
 * developed by 5AM, ScenPro, SAIC and the National Cancer Institute. If You do
 * not include such end-user documentation, You shall include this acknowledgment
 * in the Software itself, wherever such third-party acknowledgments normally
 * appear.
 *
 * You may not use the names "The National Cancer Institute", "NCI", "ScenPro",
 * "SAIC" or "5AM" to endorse or promote products derived from this Software.
 * This License does not authorize You to use any trademarks, service marks,
 * trade names, logos or product names of either NCI, ScenPro, SAID or 5AM,
 * except as required to comply with the terms of this License.
 *
 * For sake of clarity, and not by way of limitation, You may incorporate this
 * Software into Your proprietary programs and into any third party proprietary
 * programs. However, if You incorporate the Software into third party
 * proprietary programs, You agree that You are solely responsible for obtaining
 * any permission from such third parties required to incorporate the Software
 * into such third party proprietary programs and for informing Your a
 * sub-licensees, including without limitation Your end-users, of their
 * obligation to secure any required permissions from such third parties before
 * incorporating the Software into such third party proprietary software
 * programs. In the event that You fail to obtain such permissions, You agree
 * to indemnify NCI for any claims against NCI by such third parties, except to
 * the extent prohibited by law, resulting from Your failure to obtain such
 * permissions.
 *
 * For sake of clarity, and not by way of limitation, You may add Your own
 * copyright statement to Your modifications and to the derivative works, and
 * You may provide additional or different license terms and conditions in Your
 * sublicenses of modifications of the Software, or any derivative works of the
 * Software as a whole, provided Your use, reproduction, and distribution of the
 * Work otherwise complies with the conditions stated in this License.
 *
 * THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,
 * NON-INFRINGEMENT AND FITNESS FOR A PARTICULAR PURPOSE) ARE DISCLAIMED. IN NO
 * EVENT SHALL THE NATIONAL CANCER INSTITUTE, 5AM SOLUTIONS, INC., SCENPRO, INC.,
 * SCIENCE APPLICATIONS INTERNATIONAL CORPORATION OR THEIR
 * AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
