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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 
 */
public final class HeatmapJnlpFileWriter {
    private static final String XML_VERSION = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    private static final String REPLACEMENT_STRING = "REPLACMENT_STRING_TEXT";
    private static final String JNLP_START_TAG = "<jnlp spec=\"1.5+\" codebase=\"" + REPLACEMENT_STRING + "\">";
    private static final String APPLICATION_DESC_START_TAG = "<application-desc main-class=\"TCGA.Heatmap6\">";
    private static final String APPLICATION_DESC_END_TAG = "</application-desc>";
    private static final String JNPL_END_TAG = "</jnlp>";
    private static final Character NEW_LINE = '\n';
    
    private HeatmapJnlpFileWriter() { }
    
    /**
     * Writes the jnlp file to the directory.
     * @param sessionDirectory to store the session file.
     * @param urlPrefix should be of the form 
     *      "http://caintegrator2.nci.nih.gov/caintegrator2/heatmap/retrieveFile.do?JSESSIONID=12345&file=".
     * @param heatmapDirectoryUrl should be of the form
     *      "http://caintegrator2.nci.nih.gov/caintegrator2/common/"
     * @param heatmapResult the result.
     */
    public static void writeJnlpFile(File sessionDirectory, String urlPrefix, String heatmapDirectoryUrl, 
            HeatmapResult heatmapResult) {
        File jnlpFile = new File(sessionDirectory, HeatmapFileTypeEnum.LAUNCH_FILE.getFilename());
        try {
            FileWriter writer = new FileWriter(jnlpFile);
            writer.write(XML_VERSION + NEW_LINE);
            writer.write(JNLP_START_TAG.replaceAll(REPLACEMENT_STRING, heatmapDirectoryUrl) + NEW_LINE);
            writeInformationBlock(writer);
            writeSecurityBlock(writer);
            writeResourcesBlock(writer);
            writeApplicationBlock(urlPrefix, heatmapResult, writer);
            writer.write(JNPL_END_TAG);
            writer.flush();
            writer.close();
            heatmapResult.setJnlpFile(jnlpFile);
        } catch (IOException e) {
            throw new IllegalArgumentException("Couldn't write file at the path " 
                    + sessionDirectory.getAbsolutePath(), e);
        }
    }

    private static void writeApplicationBlock(String urlPrefix, HeatmapResult heatmapResult, FileWriter writer)
            throws IOException {
        writer.write(APPLICATION_DESC_START_TAG + NEW_LINE);
        if (heatmapResult.getGenomicDataFile() != null) {
            addArgument("-url-gm", writer);
            addArgument(convertUrlAmpersand(urlPrefix) + HeatmapFileTypeEnum.GENOMIC_DATA.getFilename(), writer);
        }
        if (heatmapResult.getLayoutFile() != null) {
            addArgument("-url-set", writer);
            addArgument(convertUrlAmpersand(urlPrefix) + HeatmapFileTypeEnum.LAYOUT.getFilename(), writer);
        }
        if (heatmapResult.getSampleAnnotationFile() != null) {
            addArgument("-url-annot", writer);
            addArgument(convertUrlAmpersand(urlPrefix) + HeatmapFileTypeEnum.ANNOTATIONS.getFilename(), writer);
        }
        addArgument("-binary", writer);
        addArgument("0", writer);
        addArgument("-url-gz", writer);
        addArgument("0", writer);
        writer.write(APPLICATION_DESC_END_TAG + NEW_LINE);
    }
    
    private static void writeInformationBlock(FileWriter writer) throws IOException {
        writer.write("<information>" + NEW_LINE);
        writer.write("<title>Heatmap</title>" + NEW_LINE);
        writer.write("<vendor>National Cancer Institute, NIH/NCI/CCR/LPG</vendor>" + NEW_LINE);
        writer.write("<homepage href=\"https://cgwb.nci.nih.gov/cgi-bin/heatmap\"/>" + NEW_LINE);
        writer.write("<description>Heatmap viewer</description>" + NEW_LINE);
        writer.write("<description kind=\"short\">" + NEW_LINE);
        writer.write("Heatmap" + NEW_LINE);
        writer.write("</description>" + NEW_LINE);
        writer.write("</information>" + NEW_LINE);
    }

    private static void writeSecurityBlock(FileWriter writer) throws IOException {
        writer.write("<security>" + NEW_LINE);
        writer.write("<all-permissions/>" + NEW_LINE);
        writer.write("</security>" + NEW_LINE);
    }
    
    private static void writeResourcesBlock(FileWriter writer) throws IOException {
        writer.write("<resources>" + NEW_LINE);
        writer.write("<j2se version=\"1.5+\" max-heap-size=\"512M\"/>" + NEW_LINE);
        writer.write("<jar href=\"heatmap.jar\"/>" + NEW_LINE);
        writer.write("</resources>" + NEW_LINE);
    }
    
    private static void addArgument(String argument, FileWriter writer) 
    throws IOException {
        writer.write("<argument>" + argument + "</argument>" + NEW_LINE);
    }
    
    private static String convertUrlAmpersand(String url) {
        return url.replaceAll("&", "&amp;");
    }
}
