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

import gov.nih.nci.caintegrator2.domain.application.GenomicDataQueryResult;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataResultColumn;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataResultRow;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataResultValue;
import gov.nih.nci.caintegrator2.domain.application.ResultsOrientationEnum;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * Writes GenomicDataQueryResults in csv format.
 */
public final class GenomicDataFileWriter {

    private GenomicDataFileWriter() { }
    
    /**
     * Writes a GenomicDataQueryResult to the given file.  
     * @param result genomic query result to write in csv format.
     * @param csvFile to write file to.
     * @return csv file.
     */
    public static File writeAsCsv(GenomicDataQueryResult result, File csvFile) {
        try {
            FileWriter writer = new FileWriter(csvFile);
            if (ResultsOrientationEnum.SUBJECTS_AS_COLUMNS.equals(result.getQuery().getOrientation())) {
                writeStandardOrientation(result, writer);
            } else {
                writeSubjectsAsRowsOrientation(result, writer);
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new IllegalArgumentException("Couldn't write file at the path " + csvFile.getAbsolutePath(), e);
        }
        return csvFile;
    }

    private static void writeSubjectsAsRowsOrientation(GenomicDataQueryResult result, FileWriter writer) 
    throws IOException {
        writeSubjectsAsRowsHeaders(result, writer);
        writeSubjectsAsRowsRows(result, writer);
    }

    private static void writeSubjectsAsRowsHeaders(GenomicDataQueryResult result, FileWriter writer) 
    throws IOException {
        List<String> line = getGeneHeaderLine(result);
        writeAsCsvRow(writer, line);
        int columnCount = line.size();
        if (isProbeSetResultType(result)) {
            writeAsCsvRow(writer, getReporterHeaderLine(result));
        }
        String[] sampleHeaderLine = new String[columnCount];
        sampleHeaderLine[0] = "Subject ID";
        sampleHeaderLine[1] = "Sample ID"; 
        writeAsCsvRow(writer, sampleHeaderLine);
    }

    private static List<String> getReporterHeaderLine(GenomicDataQueryResult result) {
        List<String> line = new ArrayList<String>();
        line.add("");
        line.add("");
        line.add("Reporter ID");
        for (GenomicDataResultRow row : result.getFilteredRowCollection()) {
            line.add(row.getReporter().getName());
        }
        return line;
    }

    private static List<String> getGeneHeaderLine(GenomicDataQueryResult result) {
        List<String> line = new ArrayList<String>();
        line.add("");
        line.add("");
        line.add("Gene Name");
        for (GenomicDataResultRow row : result.getFilteredRowCollection()) {
            line.add(row.getReporter().getGeneSymbols());
        }
        return line;
    }

    private static void writeSubjectsAsRowsRows(GenomicDataQueryResult result, FileWriter writer) 
    throws IOException {
        for (GenomicDataResultColumn column : result.getColumnCollection()) {
            writeAsCsvRow(writer, getResultLine(column));
        }
    }

    private static List<String> getResultLine(GenomicDataResultColumn column) {
        List<String> line = new ArrayList<String>();
        line.add(column.getSampleAcquisition().getAssignment().getIdentifier());
        line.add(column.getSampleAcquisition().getSample().getName());
        line.add("");
        for (GenomicDataResultValue value : column.getValues()) {
            line.add(String.valueOf(value.getValue()));
        }
        return line;
    }

    private static void writeStandardOrientation(GenomicDataQueryResult result, FileWriter writer) throws IOException {
        writeHeaders(result, writer);
        writeRows(result, writer);
    }

    private static void writeHeaders(GenomicDataQueryResult result, FileWriter writer) throws IOException {
        List<String> assignmentIdentifiersRow = new ArrayList<String>();
        assignmentIdentifiersRow.add("");
        assignmentIdentifiersRow.add("Subject ID");
        List<String> sampleIdentifiersRow = new ArrayList<String>();
        sampleIdentifiersRow.add("");
        sampleIdentifiersRow.add("Sample ID");
        if (isProbeSetResultType(result)) {
            assignmentIdentifiersRow.add(1, "");
            sampleIdentifiersRow.add(1, "");
        }
        for (GenomicDataResultColumn column : result.getColumnCollection()) {
            sampleIdentifiersRow.add(column.getSampleAcquisition().getSample().getName());
            assignmentIdentifiersRow.add(column.getSampleAcquisition().getAssignment().getIdentifier());
        }
        writeAsCsvRow(writer, assignmentIdentifiersRow);
        writeAsCsvRow(writer, sampleIdentifiersRow);
        String[] reporterHeadersRow = new String[assignmentIdentifiersRow.size()];
        reporterHeadersRow[0] = "Gene Name";
        if (isProbeSetResultType(result)) {
            reporterHeadersRow[1] = "Reporter ID";
        }
        writeAsCsvRow(writer, reporterHeadersRow);
    }
    
    private static void writeRows(GenomicDataQueryResult result, FileWriter writer) throws IOException {
        for (GenomicDataResultRow row : result.getFilteredRowCollection()) {
            List<String> resultValuesRow = new ArrayList<String>();
            resultValuesRow.add(row.getReporter().getGeneSymbols().replaceAll(",", "-"));
            if (isProbeSetResultType(result)) {
                resultValuesRow.add(row.getReporter().getName());
            }
            resultValuesRow.add("");
            for (GenomicDataResultValue value : row.getValues()) {
                resultValuesRow.add(String.valueOf(value.getValue()));
            }
            writeAsCsvRow(writer, resultValuesRow);
        }
    }
    

    private static void writeAsCsvRow(FileWriter writer, String[] csvRowStringArray) throws IOException {
        writeAsCsvRow(writer, Arrays.asList(csvRowStringArray));
    }
    
    private static void writeAsCsvRow(FileWriter writer, List<String> csvRowStrings) throws IOException {
        writer.append(StringUtils.join(csvRowStrings, ","));
        writer.append("\n");
    }
    
    private static boolean isProbeSetResultType(GenomicDataQueryResult result) {
        return ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET.equals(result.getQuery().getReporterType());
    }

}
