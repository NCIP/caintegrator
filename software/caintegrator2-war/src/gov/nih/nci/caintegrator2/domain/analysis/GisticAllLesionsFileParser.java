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
package gov.nih.nci.caintegrator2.domain.analysis;

import gov.nih.nci.caintegrator2.domain.genomic.AmplificationTypeEnum;
import gov.nih.nci.caintegrator2.domain.genomic.Gene;
import gov.nih.nci.caintegrator2.domain.genomic.GisticGenomicRegionReporter;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator2.external.DataRetrievalException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import au.com.bytecode.opencsv.CSVReader;

/**
 * Parse the allLesion file from GISTIC result.
 */
public class GisticAllLesionsFileParser {

    private static final float DECIMAL_1000 = 1000.0f;
    private final Map<String, Integer> headerMap = new HashMap<String, Integer>();
    private final Map<Integer, String> sampleMap = new HashMap<Integer, String>();
    private final Map<String, Map<GisticGenomicRegionReporter, Float>> gisticData =
        new HashMap<String, Map<GisticGenomicRegionReporter, Float>>();
    private final Map<String, List<Gene>> ampGeneMap;
    private final Map<String, List<Gene>> delGeneMap;
    private final ReporterList reporterList;
    private int regionReporterIndex;

    private static final String UNIQUE_NAME_HEADER = "Unique Name";
    private static final String DESCRIPTOR_HEADER = "Descriptor";
    private static final String WIDE_PEAK_LIMITS_HEADER = "Wide Peak Limits";
    private static final String PEAK_LIMITS_HEADER = "Peak Limits";
    private static final String REGION_LIMITS_HEADER = "Region Limits";
    private static final String Q_VALUES_HEADER = "q values";
    private static final String RESIDUAL_Q_VALUES_HEADER =
        "Residual q values after removing segments shared with higher peaks";
    private static final String BROAD_FOCAL_HEADER = "Broad or Focal";
    private static final String AMPLITUDE_THRESHOLD_HEADER = "Amplitude Threshold";
    private static final String[] REQUIRED_HEADERS = {UNIQUE_NAME_HEADER, DESCRIPTOR_HEADER,
        WIDE_PEAK_LIMITS_HEADER, PEAK_LIMITS_HEADER, REGION_LIMITS_HEADER, Q_VALUES_HEADER,
        RESIDUAL_Q_VALUES_HEADER, BROAD_FOCAL_HEADER, AMPLITUDE_THRESHOLD_HEADER};

    /**
     * @param ampGeneMap cytoband and amp genes map
     * @param delGeneMap cytoband and delete genes map
     * @param reporterList the reporter list
     */
    public GisticAllLesionsFileParser(Map<String, List<Gene>> ampGeneMap,
            Map<String, List<Gene>> delGeneMap, ReporterList reporterList) {
        super();
        this.ampGeneMap = ampGeneMap;
        this.delGeneMap = delGeneMap;
        this.reporterList = reporterList;
    }

    /**
     * 
     * @param inputFile input file
     * @throws IOException IO exception
     * @throws DataRetrievalException data retrieval exception
     * @return GISTIC data mapping
     */
    public Map<String, Map<GisticGenomicRegionReporter, Float>> parse(File inputFile)
    throws IOException, DataRetrievalException {
        FileReader fileReader = new FileReader(inputFile);
        CSVReader csvReader = new CSVReader(fileReader, '\t');
        loadHeaders(csvReader);
        String[] fields;
        regionReporterIndex = 0;
        while ((fields = csvReader.readNext()) != null) {
            processDataLine(fields);
        }
        csvReader.close();
        fileReader.close();
        FileUtils.deleteQuietly(inputFile);
        return gisticData;
    }
    
    private void processDataLine(String[] fields) {
        if (getField(fields, AMPLITUDE_THRESHOLD_HEADER).equalsIgnoreCase("Actual Log2 Ratio Given")) {
            if (getField(fields, UNIQUE_NAME_HEADER).contains("Amplification")) {
                createRegionReporter(fields, ampGeneMap, AmplificationTypeEnum.AMPLIFIED);
            } else if (getField(fields, UNIQUE_NAME_HEADER).contains("Deletion")) {
                createRegionReporter(fields, delGeneMap, AmplificationTypeEnum.DELETED);
            }
        }
    }

    private void createRegionReporter(String[] fields, Map<String, List<Gene>> geneMap,
            AmplificationTypeEnum type) {
        String boundaries = getBoundaries(fields);
        if (geneMap.containsKey(boundaries)) {
            GisticGenomicRegionReporter regionReporter = new GisticGenomicRegionReporter();
            reporterList.getReporters().add(regionReporter);
            regionReporter.setIndex(regionReporterIndex++);
            regionReporter.setReporterList(reporterList);
            regionReporter.setGeneAmplificationType(type);
            regionReporter.setGenomicDescriptor(getField(fields, DESCRIPTOR_HEADER));
            regionReporter.setBroadOrFocal(getField(fields, BROAD_FOCAL_HEADER));
            regionReporter.setQvalue(roundToThreeDecimalPlaces(getField(fields, Q_VALUES_HEADER)));
            regionReporter.setResidualQValue(roundToThreeDecimalPlaces(
                    getField(fields, RESIDUAL_Q_VALUES_HEADER)));
            regionReporter.setRegionBoundaries(getField(fields, REGION_LIMITS_HEADER));
            regionReporter.setWidePeakBoundaries(getField(fields, WIDE_PEAK_LIMITS_HEADER));
            regionReporter.getGenes().addAll(geneMap.get(boundaries));
            loadGisticData(regionReporter, fields);
        }
    }

    private String getBoundaries(String[] fields) {
        String boundaries = getField(fields, WIDE_PEAK_LIMITS_HEADER);
        boundaries =  boundaries.substring(0, boundaries.indexOf("(probes"));
        return boundaries;
    }

    private String getField(String[] fields, String descriptorHeader) {
        return fields[headerMap.get(descriptorHeader)].trim();
    }

    private void loadGisticData(GisticGenomicRegionReporter regionReporter, String[] fields) {
        for (int i = REQUIRED_HEADERS.length; i < fields.length; i++) {
            if (!StringUtils.isBlank(fields[i])) {
                gisticData.get(sampleMap.get(i)).put(regionReporter, Float.valueOf(fields[i]));
            }
        }
    }

    private void loadHeaders(CSVReader csvReader) throws IOException, DataRetrievalException {
        boolean headerFound = false;
        String[] fields;
        while ((fields = csvReader.readNext()) != null) {
            for (int i = 0; i < REQUIRED_HEADERS.length; i++) {
                if (!fields[i].equalsIgnoreCase(REQUIRED_HEADERS[i])) {
                    break;
                }
                headerMap.put(REQUIRED_HEADERS[i], i);
            }
            headerFound = true;
            break;
        }
        if (!headerFound) {
            throw new DataRetrievalException("Header line not found");
        }
        loadSampleHeaders(fields);
    }

    private void loadSampleHeaders(String[] fields) {
        for (int i = REQUIRED_HEADERS.length; i < fields.length; i++) {
            if (!StringUtils.isBlank(fields[i])) {
                sampleMap.put(i, fields[i]);
                gisticData.put(fields[i], new HashMap<GisticGenomicRegionReporter, Float>());
            }
        }
    }

    private Double roundToThreeDecimalPlaces(String value) {
        float floatValue = Float.valueOf(value);
        return Double.valueOf(Math.round(floatValue * DECIMAL_1000) / DECIMAL_1000);        
    }

}
