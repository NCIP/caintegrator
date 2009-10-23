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
package gov.nih.nci.caintegrator2.external.caarray;

import gov.nih.nci.caintegrator2.external.DataRetrievalException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.com.bytecode.opencsv.CSVReader;

/**
 * Reads data in Agilent data file.
 */
public final class AgilentLevelTwoDataFileParser {

    /**
     * The INSTANCE of the AgilentDataLevelTwoFileParser.
     */
    public static final AgilentLevelTwoDataFileParser INSTANCE = new AgilentLevelTwoDataFileParser();
    
    private CSVReader dataFileReader;
    private static final String SAMPLE_HEADER = "Hybridization Ref";
    private static final String PROBE_NAME = "ProbeID";
    private final Map<String, Integer> sampleToIndexMap = new HashMap<String, Integer>();
    
    /**
     * Extract data from the data file.
     * @param dataFile the data file.
     * @param sampleList the list of mapped samples
     * @return the extracted data.
     * @throws DataRetrievalException when error parsing.
     */
    public Map<String, Map<String, Float>> extractData(File dataFile, List<String> sampleList)
    throws DataRetrievalException {
        try {
            return extractData(new InputStreamReader(new FileInputStream(dataFile)), sampleList);
        } catch (IOException e) {
            throw new DataRetrievalException("Couldn't read Agilent data file.", e);
        }
    }
    
    private Map<String, Map<String, Float>> extractData(InputStreamReader inputStreamReader,
            List<String> sampleList) throws DataRetrievalException {
        try {
            dataFileReader = new CSVReader(inputStreamReader, '\t');
            loadHeaders(sampleList);
            Map<String, Map<String, Float>> agilentDataMap = new HashMap<String, Map<String, Float>>();
            loadData(agilentDataMap);
            validateSampleMapping(agilentDataMap, sampleList);
            return agilentDataMap;
        } catch (IOException e) {
            throw new DataRetrievalException("Couldn't read Agilent data file.", e);
        }
    }

    /**
     * @param agilentDataMap
     * @throws IOException
     */
    private void loadData(Map<String, Map<String, Float>> agilentDataMap) throws IOException {
        String[] fields;
        while ((fields = dataFileReader.readNext()) != null) {
            String probeName = fields[0];
            for (String sampleName : sampleToIndexMap.keySet()) {
                Float log2Ratio = getLog2Ratio(fields, sampleToIndexMap.get(sampleName));
                if (log2Ratio != null) {
                    Map<String, Float> reporterMap = getReporterMap(agilentDataMap, sampleName);
                    reporterMap.put(probeName, log2Ratio);
                }
            }
        }
    }

    private void validateSampleMapping(Map<String, Map<String, Float>> agilentDataMap, List<String> sampleList)
    throws DataRetrievalException {
        StringBuffer errorMsg = new StringBuffer();
        for (String sampleName : sampleList) {
            if (!agilentDataMap.containsKey(sampleName)) {
                if (errorMsg.length() > 0) {
                    errorMsg.append(", ");
                }
                errorMsg.append(sampleName);
            }
        }
        if (errorMsg.length() > 0) {
            throw new DataRetrievalException("Sample not found error: " + errorMsg.toString());
        }
    }
    
    private Float getLog2Ratio(String[] fields, int index) {
        try {
            return new Float(fields[index]);
        } catch (Exception e) {
            return null;
        }
    }
    
    private Map<String, Float> getReporterMap(Map<String, Map<String, Float>> agilentDataMap, String sampleName) {
        if (!agilentDataMap.containsKey(sampleName)) {
            Map<String, Float> reporterMap = new HashMap<String, Float>();
            agilentDataMap.put(sampleName, reporterMap);
        }
        return agilentDataMap.get(sampleName);
    }

    private void loadHeaders(List<String> sampleList) throws IOException, DataRetrievalException {
        String[] fields;
        fields = dataFileReader.readNext();
        checkHeadersLine(fields, SAMPLE_HEADER);
        loadSampleHeaders(fields, sampleList);
        fields = dataFileReader.readNext();
        checkHeadersLine(fields, PROBE_NAME);
    }

    private void loadSampleHeaders(String[] headers, List<String> sampleList) {
        for (int i = 3; i < headers.length; i++) {
            if (sampleList.contains(headers[i])) {
                sampleToIndexMap.put(headers[i], i);
            }
        }
    }
    
    private void checkHeadersLine(String[] fields, String keyword) throws DataRetrievalException {
        if (!keyword.equals(fields[0])) {
            throw new DataRetrievalException("Invalid header for Agilent data file.");
        }
    }
}
