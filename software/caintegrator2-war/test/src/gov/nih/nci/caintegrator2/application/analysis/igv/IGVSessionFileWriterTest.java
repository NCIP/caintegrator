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
package gov.nih.nci.caintegrator2.application.analysis.igv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.TestDataFiles;
import gov.nih.nci.caintegrator2.domain.genomic.GenomeBuildVersionEnum;
import gov.nih.nci.caintegrator2.domain.genomic.Platform;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

/**
 * 
 */
public class IGVSessionFileWriterTest {

    @Test
    public void testWriteSessionFile() throws IOException {
        String urlPrefix = "http://caintegrator2.nci.nih.gov/caintegrator2/igv/retrieveFile.jnlp?JSESSIONID=12345&file=";
        File tempDirectory = new File(System.getProperty("java.io.tmpdir") + File.separator + "igvTmp");
        tempDirectory.mkdir();
        IGVResult igvResult = new IGVResult();
        igvResult.setGeneExpressionFile(TestDataFiles.VALID_FILE);
        igvResult.setSegmentationFile(TestDataFiles.VALID_FILE);
        igvResult.setSampleInfoFile(TestDataFiles.VALID_FILE);
        
        // Check it with no platforms.
        Set<Platform> platforms = new HashSet<Platform>();
        IGVSessionFileWriter.writeSessionFile(tempDirectory, 
                urlPrefix, igvResult, platforms);
        File sessionFile = igvResult.getSessionFile();
        assertTrue(sessionFile.exists());
        checkFile(sessionFile, GenomeBuildVersionEnum.HG18);
        
        // Check it with HG19 platform
        Platform platform = new Platform();
        ReporterList reporterList = 
            platform.addReporterList("reporterList", ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET);
        reporterList.setGenomeVersion("g19");
        platforms.add(platform);
        IGVSessionFileWriter.writeSessionFile(tempDirectory, 
                urlPrefix, igvResult, platforms);
        sessionFile = igvResult.getSessionFile();
        assertTrue(sessionFile.exists());
        checkFile(sessionFile, GenomeBuildVersionEnum.HG19);
        
        // Check it with an HG 17 and HG 19 platform - should default to HG18 in this case
        Platform platform2 = new Platform();
        ReporterList reporterList2 = 
            platform2.addReporterList("reporterList", ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET);
        reporterList2.setGenomeVersion("g17");
        platforms.add(platform2);
        IGVSessionFileWriter.writeSessionFile(tempDirectory, 
                urlPrefix, igvResult, platforms);
        sessionFile = igvResult.getSessionFile();
        assertTrue(sessionFile.exists());
        checkFile(sessionFile, GenomeBuildVersionEnum.HG18);
        FileUtils.deleteDirectory(tempDirectory);
    }

    private void checkFile(File sessionFile, GenomeBuildVersionEnum genomeVersion) 
        throws FileNotFoundException, IOException {
        BufferedReader reader = new BufferedReader(new FileReader(sessionFile));
        assertEquals(reader.readLine(), "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        assertEquals(reader.readLine(), "<Session genome=\"" + genomeVersion.getValue() + "\" locus=\"All\" version=\"3\">");
        assertEquals(reader.readLine(), "<Resources>");
        assertEquals(
                reader.readLine(),
                "<Resource path=\"http://caintegrator2.nci.nih.gov/"
                + "caintegrator2/igv/retrieveFile.jnlp?JSESSIONID=12345&amp;file=igvGeneExpression.gct\"/>");
        assertEquals(
                reader.readLine(),
                "<Resource path=\"http://caintegrator2.nci.nih.gov/"
                + "caintegrator2/igv/retrieveFile.jnlp?JSESSIONID=12345&amp;file=igvSegmentation.seg\"/>");
        assertEquals(
                reader.readLine(),
                "<Resource path=\"http://caintegrator2.nci.nih.gov/"
                + "caintegrator2/igv/retrieveFile.jnlp?JSESSIONID=12345&amp;file=sampleInfo.txt\"/>");
        assertEquals(reader.readLine(), "</Resources>");
        assertEquals(reader.readLine(), "</Session>");
        reader.close();
    }

}
