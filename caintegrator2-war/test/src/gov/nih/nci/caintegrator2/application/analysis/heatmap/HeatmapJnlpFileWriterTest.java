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
package gov.nih.nci.caintegrator2.application.analysis.heatmap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.TestDataFiles;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class HeatmapJnlpFileWriterTest {

    @Test
    public void testWriteSessionFile() throws IOException {
        String urlPrefix = "http://caintegrator2.nci.nih.gov/caintegrator2/heatmap/retrieveFile.do?JSESSIONID=12345&file=";
        String heatmapDirectoryUrl = "http://caintegrator2.nci.nih.gov/caintegrator2/common/";
        File tempDirectory = new File(System.getProperty("java.io.tmpdir") + File.separator + "heatmapTmp");
        tempDirectory.mkdir();
        HeatmapResult heatmapResult = new HeatmapResult();
        heatmapResult.setGenomicDataFile(TestDataFiles.VALID_FILE);
        heatmapResult.setLayoutFile(TestDataFiles.VALID_FILE);
        heatmapResult.setSampleAnnotationFile(TestDataFiles.VALID_FILE);
        
        HeatmapJnlpFileWriter.writeJnlpFile(tempDirectory, 
                urlPrefix, heatmapDirectoryUrl, heatmapResult);
        File sessionFile = heatmapResult.getJnlpFile();
        assertTrue(sessionFile.exists());
        checkFile(sessionFile);
        FileUtils.deleteDirectory(tempDirectory);
    }

    private void checkFile(File sessionFile) 
        throws FileNotFoundException, IOException {
        BufferedReader reader = new BufferedReader(new FileReader(sessionFile));
        assertEquals(reader.readLine(), "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        assertEquals(reader.readLine(), "<jnlp spec=\"1.5+\" codebase=\"http://caintegrator2.nci.nih.gov/caintegrator2/common/\">");
        assertEquals(reader.readLine(), "<information>");
        assertEquals(reader.readLine(), "<title>Heatmap</title>");
        assertEquals(reader.readLine(), "<vendor>National Cancer Institute, NIH/NCI/CCR/LPG</vendor>");
        assertEquals(reader.readLine(), "<homepage href=\"https://cgwb.nci.nih.gov/cgi-bin/heatmap\"/>");
        assertEquals(reader.readLine(), "<description>Heatmap viewer</description>");
        assertEquals(reader.readLine(), "<description kind=\"short\">");
        assertEquals(reader.readLine(), "Heatmap");
        assertEquals(reader.readLine(), "</description>");
        assertEquals(reader.readLine(), "</information>");
        assertEquals(reader.readLine(), "<security>");
        assertEquals(reader.readLine(), "<all-permissions/>");
        assertEquals(reader.readLine(), "</security>");
        assertEquals(reader.readLine(), "<resources>");
        assertEquals(reader.readLine(), "<j2se version=\"1.5+\" max-heap-size=\"512M\"/>");
        assertEquals(reader.readLine(), "<jar href=\"heatmap.jar\"/>");
        assertEquals(reader.readLine(), "</resources>");
        assertEquals(reader.readLine(), "<application-desc main-class=\"TCGA.Heatmap6\">");
        assertEquals(reader.readLine(), "<argument>-url-gm</argument>");
        assertEquals(reader.readLine(), "<argument>http://caintegrator2.nci.nih.gov/caintegrator2/heatmap/retrieveFile.do?JSESSIONID=12345&amp;file=heatmapGenomicData.txt</argument>");
        assertEquals(reader.readLine(), "<argument>-url-set</argument>");
        assertEquals(reader.readLine(), "<argument>http://caintegrator2.nci.nih.gov/caintegrator2/heatmap/retrieveFile.do?JSESSIONID=12345&amp;file=heatmapLayout.dat</argument>");
        assertEquals(reader.readLine(), "<argument>-url-annot</argument>");
        assertEquals(reader.readLine(), "<argument>http://caintegrator2.nci.nih.gov/caintegrator2/heatmap/retrieveFile.do?JSESSIONID=12345&amp;file=heatmapAnnotations.txt</argument>");
        assertEquals(reader.readLine(), "<argument>-binary</argument>");
        assertEquals(reader.readLine(), "<argument>0</argument>");
        assertEquals(reader.readLine(), "<argument>-url-gz</argument>");
        assertEquals(reader.readLine(), "<argument>0</argument>");
        assertEquals(reader.readLine(), "<application-desc>");
        assertEquals(reader.readLine(), "</jnlp>");
        reader.close();
    }

}
