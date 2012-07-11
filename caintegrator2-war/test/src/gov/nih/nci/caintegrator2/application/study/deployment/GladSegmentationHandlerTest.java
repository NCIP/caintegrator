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
package gov.nih.nci.caintegrator2.application.study.deployment;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import edu.mit.broad.genepattern.gp.services.GenePatternClient;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator2.domain.genomic.DnaAnalysisData;
import gov.nih.nci.caintegrator2.domain.genomic.DnaAnalysisReporter;
import gov.nih.nci.caintegrator2.domain.genomic.SegmentData;
import gov.nih.nci.caintegrator2.external.DataRetrievalException;
import gov.nih.nci.caintegrator2.mockito.AbstractMockitoTest;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.activation.DataHandler;

import org.apache.commons.io.IOUtils;
import org.genepattern.webservice.JobInfo;
import org.genepattern.webservice.ParameterInfo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class GladSegmentationHandlerTest extends AbstractMockitoTest {
    private static final String TEST_OUTPUT = "Sample\tChromosome\tStart.bp\tEnd.bp\tNum.SNPs\tSeg.CN\n"
            + "1\t17\t41419603\t36581538\t6427\t2.06\n"
            + "2\t18\t56507051\t10075159\t6732\t1.881\n";
    private GladSegmentationHandler handler;
    private GenePatternClient genePatternClient;

    @Before
    public void setUp() throws Exception {
        genePatternClient = mock(GenePatternClient.class);
        when(genePatternClient.runAnalysis(anyString(), anyListOf(ParameterInfo.class))).thenReturn(new JobInfo());
        when(genePatternClient.getStatus(any(JobInfo.class))).thenAnswer(new Answer<JobInfo>() {
            @Override
            public JobInfo answer(InvocationOnMock invocation) throws Throwable {
                JobInfo jobInfo = (JobInfo) invocation.getArguments()[0];
                jobInfo.setStatus("Completed");
                return jobInfo;
            }
        });
        when(genePatternClient.getResultFile(any(JobInfo.class), anyString())).thenAnswer(new Answer<File>() {
            @Override
            public File answer(InvocationOnMock invocation) throws Throwable {
                File outputFile = File.createTempFile("output", ".glad");
                DataHandler dataHandler = mock(DataHandler.class);
                when(dataHandler.getInputStream()).thenReturn(new ByteArrayInputStream(TEST_OUTPUT.getBytes()));
                FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
                IOUtils.copy(dataHandler.getInputStream(), fileOutputStream);
                fileOutputStream.close();
                return outputFile;
            }
        });
        handler = new GladSegmentationHandler(genePatternClient);
    }

    @Test
    public void testAddSegmentationData() throws DataRetrievalException {
        List<DnaAnalysisReporter> reporters = new ArrayList<DnaAnalysisReporter>();
        DnaAnalysisData dnaAnalysisData = new DnaAnalysisData(reporters);
        ArrayData arrayData1 = new ArrayData();
        arrayData1.setId(1L);
        dnaAnalysisData.addDnaAnalysisData(arrayData1, new float[0]);
        ArrayData arrayData2 = new ArrayData();
        arrayData2.setId(2L);
        dnaAnalysisData.addDnaAnalysisData(arrayData2, new float[0]);
        handler.addSegmentationData(dnaAnalysisData);
        assertEquals(1, arrayData1.getSegmentDatas().size());
        assertEquals(1, arrayData2.getSegmentDatas().size());
        SegmentData segmentData = arrayData1.getSegmentDatas().first();
        assertEquals("17", segmentData.getLocation().getChromosome());
        assertEquals(41419603, (int) segmentData.getLocation().getStartPosition());
        assertEquals(36581538, (int) segmentData.getLocation().getEndPosition());
        assertEquals(6427, (int) segmentData.getNumberOfMarkers());
        assertEquals(2.06, segmentData.getSegmentValue(), 0.001);
    }
}
