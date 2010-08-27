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
package gov.nih.nci.caintegrator2.application.query;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataQueryResult;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataResultColumn;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataResultRow;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataResultValue;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.ResultsOrientationEnum;
import gov.nih.nci.caintegrator2.domain.genomic.Gene;
import gov.nih.nci.caintegrator2.domain.genomic.GeneExpressionReporter;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.domain.genomic.Sample;
import gov.nih.nci.caintegrator2.domain.genomic.SampleAcquisition;
import gov.nih.nci.caintegrator2.domain.translational.StudySubjectAssignment;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;

import org.junit.Test;

import au.com.bytecode.opencsv.CSVReader;

public class GenomicDataFileWriterTest {

    @Test
    public void testWriteAsCsv() throws IOException {
        GenomicDataQueryResult result = createTestResult();
        result.setHasCriterionSpecifiedValues(true);
        Query query = new Query();
        query.setReporterType(ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET);
        query.setOrientation(ResultsOrientationEnum.SUBJECTS_AS_COLUMNS);
        result.setQuery(query);
        File csvFile = new File(System.getProperty("java.io.tmpdir") + File.separator + "genomicResultTest.csv");
        csvFile = GenomicDataFileWriter.writeAsCsv(result, csvFile);
        csvFile.deleteOnExit();
        checkFile(csvFile, result);
        query.setOrientation(ResultsOrientationEnum.SUBJECTS_AS_ROWS);
        csvFile = GenomicDataFileWriter.writeAsCsv(result, csvFile);
        checkFileWithSubjectsAsRows(csvFile, result);
    }

    private void checkFile(File csvFile, GenomicDataQueryResult result) throws IOException {
        assertTrue(csvFile.exists());
        CSVReader reader = new CSVReader(new FileReader(csvFile), ',');
        checkLine(reader.readNext(), "", "", "Subject ID", "ASSIGNMENT1", "ASSIGNMENT2", "ASSIGNMENT3");
        checkLine(reader.readNext(), "", "", "Sample ID", "SAMPLE1", "SAMPLE2", "SAMPLE3");
        checkLine(reader.readNext(), "Gene Name", "Reporter ID", "", "", "", "");
        checkLine(reader.readNext(), "GENE1", "REPORTER1", "", "1.1", "2.2", "3.3");
        checkLine(reader.readNext(), "", "REPORTER2", "", "4.4", "5.5", "6.6");
    }

    private void checkFileWithSubjectsAsRows(File csvFile, GenomicDataQueryResult result) throws IOException {
        assertTrue(csvFile.exists());
        CSVReader reader = new CSVReader(new FileReader(csvFile), ',');
        checkLine(reader.readNext(), "", "", "Gene Name", "GENE1", "");
        checkLine(reader.readNext(), "", "", "Reporter ID", "REPORTER1", "REPORTER2");
        checkLine(reader.readNext(), "Subject ID", "Sample ID", "", "", "");
        checkLine(reader.readNext(), "ASSIGNMENT1", "SAMPLE1",  "", "1.1", "4.4");
        checkLine(reader.readNext(), "ASSIGNMENT2", "SAMPLE2",  "", "2.2", "5.5");
        checkLine(reader.readNext(), "ASSIGNMENT3", "SAMPLE3",  "", "3.3", "6.6");
    }

    private void checkLine(String[] line, String... expecteds) {
        assertArrayEquals(expecteds, line);
    }

    private GenomicDataQueryResult createTestResult() {
        GenomicDataQueryResult result = new GenomicDataQueryResult();
        addColumn(result, "SAMPLE1", "ASSIGNMENT1");
        addColumn(result, "SAMPLE2", "ASSIGNMENT2");
        addColumn(result, "SAMPLE3", "ASSIGNMENT3");
        addRow(result, "REPORTER1", "GENE1", new float[] {(float) 1.1, (float) 2.2, (float) 3.3}, true);
        addRow(result, "REPORTER3", "GENE3", new float[] {(float) 8, (float) 9, (float) 10}, false); // will not show up because doesn't have matching values.
        addRow(result, "REPORTER2", null, new float[] {(float) 4.4, (float) 5.5, (float) 6.6}, true);
        return result;
    }

    private void addRow(GenomicDataQueryResult result, String reporterName, String geneName, float[] values, boolean hasMatchingValues) {
        GenomicDataResultRow row = new GenomicDataResultRow();
        GeneExpressionReporter reporter = new GeneExpressionReporter();
        reporter.setName(reporterName);
        if (geneName != null) {
            Gene gene = new Gene();
            gene.setSymbol(geneName.toUpperCase(Locale.getDefault()));
            reporter.getGenes().add(gene);
        }
        row.setReporter(reporter);
        int colNum = 0;
        for (float value : values) {
            GenomicDataResultValue genomicValue = new GenomicDataResultValue();
            genomicValue.setValue(value);
            row.getValues().add(genomicValue);
            genomicValue.setColumn(result.getColumnCollection().get(colNum));
            colNum++;
        }
        result.getRowCollection().add(row);
        row.setHasMatchingValues(hasMatchingValues);
    }

    private void addColumn(GenomicDataQueryResult result, String sampleName, String assignmentName) {
        GenomicDataResultColumn column = result.addColumn();
        column.setSampleAcquisition(new SampleAcquisition());
        column.getSampleAcquisition().setSample(new Sample());
        column.getSampleAcquisition().getSample().setName(sampleName);
        StudySubjectAssignment assignment = new StudySubjectAssignment();
        assignment.setIdentifier(assignmentName);
        column.getSampleAcquisition().setAssignment(assignment);
    }

}
