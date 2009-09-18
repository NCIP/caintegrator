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

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import au.com.bytecode.opencsv.CSVReader;
import edu.mit.broad.genepattern.gp.services.FileWrapper;
import edu.mit.broad.genepattern.gp.services.GenePatternClient;
import edu.mit.broad.genepattern.gp.services.GenePatternServiceException;
import edu.mit.broad.genepattern.gp.services.JobInfo;
import edu.mit.broad.genepattern.gp.services.ParameterInfo;
import gov.nih.nci.caintegrator2.common.GenePatternUtil;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator2.domain.genomic.ChromosomalLocation;
import gov.nih.nci.caintegrator2.domain.genomic.CopyNumberData;
import gov.nih.nci.caintegrator2.domain.genomic.DnaAnalysisReporter;
import gov.nih.nci.caintegrator2.domain.genomic.SegmentData;
import gov.nih.nci.caintegrator2.external.DataRetrievalException;

/**
 * Adds segmentation data by invoking the GLAD GenePattern service.
 */
class GladSegmentationHandler {

    private static final String OUTPUT_FILENAME = "output.glad";

    private static final float DECIMAL_1000 = 1000.0f;

    private final GenePatternClient client;

    GladSegmentationHandler(GenePatternClient client) {
        this.client = client;
    }

    void addSegmentationData(CopyNumberData copyNumberData) throws DataRetrievalException {
        try {
            List<ParameterInfo> parameters = new ArrayList<ParameterInfo>();
            ParameterInfo inputFileParameter = new ParameterInfo();
            inputFileParameter.setName("copy.number.input.file");
            File inputFile = createInputFile(copyNumberData);
            inputFileParameter.setValue(inputFile.getAbsolutePath());
            parameters.add(inputFileParameter);
            ParameterInfo outputFilenameParameter = new ParameterInfo();
            outputFilenameParameter.setName("output.filename");
            outputFilenameParameter.setValue(OUTPUT_FILENAME);
            parameters.add(outputFilenameParameter);
            JobInfo jobInfo = client.runAnalysis("GLAD", parameters);
            jobInfo = GenePatternUtil.waitToComplete(jobInfo, client);
            inputFile.delete();
            handleResults(jobInfo, copyNumberData);
        } catch (IOException e) {
            throw new DataRetrievalException("Couldn't run GLAD job: " + e.getMessage(), e);
        } catch (GenePatternServiceException e) {
            throw new DataRetrievalException("Couldn't run GLAD job: " + e.getMessage(), e);
        }
    }

    private void handleResults(JobInfo jobInfo, CopyNumberData copyNumberData) throws IOException,
            GenePatternServiceException, DataRetrievalException {
        File outputFile = getOutputFile(jobInfo);
        addSegmentationData(outputFile, copyNumberData);
        outputFile.delete();
    }

    private void addSegmentationData(File outputFile, CopyNumberData copyNumberData) throws IOException {
        FileReader fileReader = new FileReader(outputFile);
        CSVReader csvReader = new CSVReader(fileReader, '\t');
        csvReader.readNext(); // skip header line;
        Map<String, ArrayData> arrayDataMap = getArrayDataMap(copyNumberData);
        String[] fields;
        while ((fields = csvReader.readNext()) != null) {
            ArrayData arrayData = arrayDataMap.get(fields[0]);
            handleSegmentDataLine(fields, arrayData);
        }
        csvReader.close();
        fileReader.close();
    }

    private void handleSegmentDataLine(String[] fields, ArrayData arrayData) {
        String chromosome = fields[1];
        int start = Integer.parseInt(fields[2]);
        int end = Integer.parseInt(fields[3]);
        int numberOfMarkers = Integer.parseInt(fields[4]);
        float segmentValue = Float.parseFloat(fields[5]);
        SegmentData segmentData = new SegmentData();
        segmentData.setLocation(new ChromosomalLocation());
        segmentData.getLocation().setChromosome(chromosome);
        segmentData.getLocation().setStartPosition(start);
        segmentData.getLocation().setEndPosition(end);
        segmentData.setNumberOfMarkers(numberOfMarkers);
        segmentData.setSegmentValue(segmentValue);
        arrayData.getSegmentDatas().add(segmentData);
    }

    private Map<String, ArrayData> getArrayDataMap(CopyNumberData copyNumberData) {
        Map<String, ArrayData> arrayDataMap = new HashMap<String, ArrayData>();
        for (ArrayData arrayData : copyNumberData.getArrayDatas()) {
            arrayDataMap.put(String.valueOf(arrayData.getId()), arrayData);
        }
        return arrayDataMap;
    }

    private File getOutputFile(JobInfo jobInfo) 
    throws IOException, GenePatternServiceException, DataRetrievalException {
        FileWrapper outputFileWrapper = client.getResultFile(jobInfo, OUTPUT_FILENAME);
        if (outputFileWrapper == null) {
            throw new DataRetrievalException("GLAD job did not complete successfully, output was not returned");
        }
        File outputFile = File.createTempFile("output", ".glad");
        FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
        IOUtils.copy(outputFileWrapper.getDataHandler().getInputStream(), fileOutputStream);
        fileOutputStream.close();
        return outputFile;
    }

    private File createInputFile(CopyNumberData copyNumberData) throws IOException {
        File inputFile = File.createTempFile("glad_input", ".cn");
        FileWriter writer = new FileWriter(inputFile);
        List<ArrayData> arrayDatas = new ArrayList<ArrayData>();
        arrayDatas.addAll(copyNumberData.getArrayDatas());
        writeHeader(writer, arrayDatas);
        writeData(writer, arrayDatas, copyNumberData);
        writer.flush();
        writer.close();
        return inputFile;
    }

    private void writeData(FileWriter writer, List<ArrayData> arrayDatas, CopyNumberData copyNumberData)
            throws IOException {
        for (int i = 0; i < copyNumberData.getReporters().size(); i++) {
            DnaAnalysisReporter reporter = copyNumberData.getReporters().get(i);
            if (reporter.hasValidLocation()) {
                writeDataLine(writer, arrayDatas, copyNumberData, i, reporter);
            }
        }
    }

    private void writeDataLine(FileWriter writer, List<ArrayData> arrayDatas, CopyNumberData copyNumberData, int i,
            DnaAnalysisReporter reporter) throws IOException {
        writer.write(reporter.getName() + "\t" + reporter.getChromosomeAsInt() + "\t" + reporter.getPosition());
        for (ArrayData arrayData : arrayDatas) {
            float nonLogTransformedValue = (float) Math.pow(2, copyNumberData.getValues(arrayData)[i]);
            writer.write("\t" + roundToThreeDecimalPlaces(nonLogTransformedValue));
        }
        writer.write("\n");
    }
    
    private float roundToThreeDecimalPlaces(float value) {
        return Math.round(value * DECIMAL_1000) / DECIMAL_1000;        
    }

    private void writeHeader(FileWriter writer, List<ArrayData> arrayDatas) throws IOException {
        writer.write("SNP\tChromosome\tPhysicalPosition");
        for (ArrayData arrayData : arrayDatas) {
            writer.write("\t" + arrayData.getId());
        }
        writer.write("\n");
    }

}
