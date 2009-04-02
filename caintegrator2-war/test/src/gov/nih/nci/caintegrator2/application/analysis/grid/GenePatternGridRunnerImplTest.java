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
package gov.nih.nci.caintegrator2.application.analysis.grid;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;
import edu.columbia.geworkbench.cagrid.MageBioAssayGeneratorImpl;
import gov.nih.nci.caintegrator2.application.analysis.GenePatternGridClientFactoryStub;
import gov.nih.nci.caintegrator2.application.analysis.grid.preprocess.PreprocessDatasetParameters;
import gov.nih.nci.caintegrator2.application.query.QueryManagementServiceStub;
import gov.nih.nci.caintegrator2.data.StudyHelper;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataQueryResult;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataResultColumn;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataResultRow;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataResultValue;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.application.UserWorkspace;
import gov.nih.nci.caintegrator2.domain.genomic.Gene;
import gov.nih.nci.caintegrator2.domain.genomic.GeneExpressionReporter;
import gov.nih.nci.caintegrator2.domain.genomic.Sample;
import gov.nih.nci.caintegrator2.domain.genomic.SampleAcquisition;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.ServerConnectionProfile;
import gov.nih.nci.caintegrator2.file.FileManagerStub;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import au.com.bytecode.opencsv.CSVReader;

/**
 * 
 */
public class GenePatternGridRunnerImplTest {
    
    private GenePatternGridRunnerImpl genePatternGridRunner;
    private QueryManagementServiceStub queryManagementServiceStub;
    private GenePatternGridClientFactoryStub genePatternGridClientFactoryStub;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        genePatternGridRunner = new GenePatternGridRunnerImpl();
        queryManagementServiceStub = new QueryManagementServiceStub();
        genePatternGridClientFactoryStub = new GenePatternGridClientFactoryStub();
        genePatternGridRunner.setFileManager(new FileManagerStub());
        genePatternGridRunner.setMbaGenerator(new MageBioAssayGeneratorImpl());
        genePatternGridRunner.setQueryManagementService(queryManagementServiceStub);
        genePatternGridRunner.setGenePatternGridClientFactory(genePatternGridClientFactoryStub);
        queryManagementServiceStub.setExpectedGenomicResult(createTestResult());
    }

    @Test
    public void testRunPreprocessDataset() throws ConnectionException, IOException {
        StudyHelper studyHelper = new StudyHelper();
        StudySubscription studySubscription = studyHelper.populateAndRetrieveStudy();
        UserWorkspace userWorkspace = new UserWorkspace();
        userWorkspace.setUsername("testUser");
        studySubscription.setUserWorkspace(userWorkspace);
        ServerConnectionProfile server = new ServerConnectionProfile();
        PreprocessDatasetParameters parameters = new PreprocessDatasetParameters();
        parameters.setProcessedGctFilename("testFile.gct");
        File gctFile = genePatternGridRunner.runPreprocessDataset(studySubscription, server, parameters);
        gctFile.deleteOnExit();
        checkFile(gctFile);
    }
    
    private void checkFile(File gctFile) throws IOException {
        assertTrue(gctFile.exists());
        CSVReader reader = new CSVReader(new FileReader(gctFile), '\t');
        checkLine(reader.readNext(), "#1.2");
        checkLine(reader.readNext(), "2", "3");
        checkLine(reader.readNext(), "NAME", "Description", "SAMPLE1", "SAMPLE2", "SAMPLE3");
        checkLine(reader.readNext(), "REPORTER1", "Gene: GENE1", "1.1", "2.2", "3.3");
        checkLine(reader.readNext(), "REPORTER2", "N/A", "4.4", "5.5", "6.6");
    }

    private void checkLine(String[] line, String... expecteds) {
        assertArrayEquals(expecteds, line);
    }
    
    private GenomicDataQueryResult createTestResult() {
        GenomicDataQueryResult result = new GenomicDataQueryResult();
        addColumn(result, "SAMPLE1");
        addColumn(result, "SAMPLE2");
        addColumn(result, "SAMPLE3");
        addRow(result, "REPORTER1", "GENE1", new float[] {(float) 1.1, (float) 2.2, (float) 3.3});
        addRow(result, "REPORTER2", null, new float[] {(float) 4.4, (float) 5.5, (float) 6.6});
        return result;
    }

    private void addRow(GenomicDataQueryResult result, String reporterName, String geneName, float[] values) {
        GenomicDataResultRow row = new GenomicDataResultRow();
        GeneExpressionReporter reporter = new GeneExpressionReporter();
        reporter.setName(reporterName);
        if (geneName != null) {
            Gene gene = new Gene();
            gene.setSymbol(geneName);
            reporter.getGenes().add(gene);
        }
        row.setReporter(reporter);
        row.setValueCollection(new ArrayList<GenomicDataResultValue>());
        int colNum = 0;
        for (float value : values) {
            GenomicDataResultValue genomicValue = new GenomicDataResultValue();
            genomicValue.setValue(value);
            row.getValueCollection().add(genomicValue);
            genomicValue.setColumn(result.getColumnCollection().get(colNum));
            colNum++;
        }
        result.getRowCollection().add(row);
    }

    private void addColumn(GenomicDataQueryResult result, String sampleName) {
        GenomicDataResultColumn column = new GenomicDataResultColumn();
        column.setSampleAcquisition(new SampleAcquisition());
        column.getSampleAcquisition().setSample(new Sample());
        column.getSampleAcquisition().getSample().setName(sampleName);
        result.getColumnCollection().add(column);
    }

}
