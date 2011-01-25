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
package gov.nih.nci.caintegrator2.external.bioconductor;

import gov.nih.nci.caintegrator2.application.study.DnaAnalysisDataConfiguration;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator2.domain.genomic.ChromosomalLocation;
import gov.nih.nci.caintegrator2.domain.genomic.DnaAnalysisData;
import gov.nih.nci.caintegrator2.domain.genomic.DnaAnalysisReporter;
import gov.nih.nci.caintegrator2.domain.genomic.SegmentData;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.DataRetrievalException;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.axis.types.URI.MalformedURIException;
import org.apache.log4j.Logger;

import org.bioconductor.cagrid.cadnacopy.DNAcopyAssays;
import org.bioconductor.cagrid.cadnacopy.DNAcopyParameter;
import org.bioconductor.cagrid.cadnacopy.ExpressionData;
import org.bioconductor.cagrid.cadnacopy.DerivedDNAcopySegment;
import org.bioconductor.packages.caDNAcopy.common.CaDNAcopyI;

import org.bioconductor.cagrid.cacghcall.CGHcallAssays;
import org.bioconductor.cagrid.cacghcall.CGHcallParameter;
import org.bioconductor.cagrid.cacghcall.CGHcallExpressionData;
import org.bioconductor.cagrid.cacghcall.DerivedCGHcallSegment;
import org.bioconductor.packages.caCGHcall.common.CaCGHcallI;


/**
 * Implementation that uses Bioconductor grid services.
 */
public class BioconductorServiceImpl implements BioconductorService {
    
    private static final Logger LOGGER = Logger.getLogger(BioconductorServiceImpl.class);
    private BioconductorClientFactory clientFactory = new BioconductorClientFactoryImpl();
    
    /**
     * {@inheritDoc}
     * @throws ConnectionException 
     * @throws DataRetrievalException 
     */
    public void addSegmentationData(DnaAnalysisData dnaAnalysisData,
            DnaAnalysisDataConfiguration configuration) 
    throws ConnectionException, DataRetrievalException {
        String segmentationServiceUrl = configuration.getSegmentationService().getUrl();
        try {
            LOGGER.info("Begin Retrieving segment from BioConductor: "
                      + configuration.getSegmentationService().getUrl());
            if (configuration.isUseCghCall()) {
                addCGHcallSegmentationData(dnaAnalysisData, configuration, segmentationServiceUrl);
            } else {
                addDNAcopySegmentationData(dnaAnalysisData, configuration, segmentationServiceUrl);
            }
        } catch (RemoteException e) {
            LOGGER.error("Couldn't complete segmentation job", e);
            throw new DataRetrievalException("Couldn't complete segmentation job: " + e.getMessage(), e);
        }
    }    

    private void addCGHcallSegmentationData(DnaAnalysisData dnaAnalysisData,
            DnaAnalysisDataConfiguration configuration, String segmentationServiceUrl) 
    throws ConnectionException, DataRetrievalException, RemoteException {
        CGHcallAssays assays = buildCGHcallAssays(dnaAnalysisData);
        CGHcallParameter parameter = new CGHcallParameter();
        parameter.setChangePointSignificanceLevel(configuration.getChangePointSignificanceLevel());
        parameter.setEarlyStoppingCriterion(configuration.getEarlyStoppingCriterion());
        parameter.setPermutationReplicates(configuration.getPermutationReplicates());
        parameter.setRandomNumberSeed(configuration.getRandomNumberSeed());
        parameter.setNumberLevels(configuration.getNumberLevelCall());
        CaCGHcallI client = getCGHcallClient(segmentationServiceUrl);
        DerivedCGHcallSegment segment = client.getDerivedCGHcallSegment(assays, parameter);
        LOGGER.info("End Retrieving segment from BioConductor: "
                + configuration.getSegmentationService().getUrl());
        addCGHcallSegmentationData(segment, dnaAnalysisData);
    }    
    
    private void addDNAcopySegmentationData(DnaAnalysisData dnaAnalysisData,
            DnaAnalysisDataConfiguration configuration, String segmentationServiceUrl) 
    throws ConnectionException, DataRetrievalException, RemoteException {
      DNAcopyAssays assays = buildDNAcopyAssays(dnaAnalysisData);
      DNAcopyParameter parameter = new DNAcopyParameter();
      parameter.setChangePointSignificanceLevel(configuration.getChangePointSignificanceLevel());
      parameter.setEarlyStoppingCriterion(configuration.getEarlyStoppingCriterion());
      parameter.setPermutationReplicates(configuration.getPermutationReplicates());
      parameter.setRandomNumberSeed(configuration.getRandomNumberSeed());
      CaDNAcopyI client = getDNAcopyClient(segmentationServiceUrl);
      DerivedDNAcopySegment segment = client.getDerivedDNAcopySegment(assays, parameter);
      LOGGER.info("End Retrieving segment from BioConductor: "
              + configuration.getSegmentationService().getUrl());
      addDNAcopySegmentationData(segment, dnaAnalysisData);
    }
    
    private CaDNAcopyI getDNAcopyClient(String url) throws ConnectionException {
        try {
            return getClientFactory().getCaDNAcopyI(url);
        } catch (MalformedURIException e) {
            throw new ConnectionException("Invalid URL: " + url, e);
        } catch (RemoteException e) {
            LOGGER.error("Couldn't connect to CaDNACopy service", e);
            throw new ConnectionException("Couldn't connect to " + url + ": " + e.getMessage(), e);
        }
    }
    
    private CaCGHcallI getCGHcallClient(String url) throws ConnectionException {
        try {
            return getClientFactory().getCaCGHcallI(url);
        } catch (MalformedURIException e) {
            throw new ConnectionException("Invalid URL: " + url, e);
        } catch (RemoteException e) {
            LOGGER.error("Couldn't connect to caCGHcallI service", e);
            throw new ConnectionException("Couldn't connect to " + url + ": " + e.getMessage(), e);
        }
    }    

    private void addDNAcopySegmentationData(DerivedDNAcopySegment segment, DnaAnalysisData dnaAnalysisData) {
        Map<String, ArrayData> arrayDataMap = getArrayDataMap(dnaAnalysisData);
        for (int segmentIndex = 0;  segmentIndex < segment.getSampleId().length; segmentIndex++) {
            ArrayData arrayData = arrayDataMap.get(getDNAcopyArrayDataKey(segment, segmentIndex));
            SegmentData segmentData = createDNAcopySegmentData(segment, segmentIndex);
            segmentData.setArrayData(arrayData);
            arrayData.getSegmentDatas().add(segmentData);
        }
    }   
    
    private void addCGHcallSegmentationData(DerivedCGHcallSegment segment, DnaAnalysisData dnaAnalysisData) {
        Map<String, ArrayData> arrayDataMap = getArrayDataMap(dnaAnalysisData);
        for (int segmentIndex = 0;  segmentIndex < segment.getSampleId().length; segmentIndex++) {
            ArrayData arrayData = arrayDataMap.get(getCGHcallArrayDataKey(segment, segmentIndex));
            SegmentData segmentData = createCGHcallSegmentData(segment, segmentIndex);
            segmentData.setArrayData(arrayData);
            arrayData.getSegmentDatas().add(segmentData);
        }
    }    

    private String getDNAcopyArrayDataKey(DerivedDNAcopySegment segment, int segmentIndex) {
        String sampleId = segment.getSampleId(segmentIndex);
        if (sampleId.charAt(0) == 'X') {
            return sampleId.substring(1);
        } else {
            return sampleId;
        }
    }
    
    private String getCGHcallArrayDataKey(DerivedCGHcallSegment segment, int segmentIndex) {
        String sampleId = segment.getSampleId(segmentIndex);
        if (sampleId.charAt(0) == 'X') {
            return sampleId.substring(1);
        } else {
            return sampleId;
        }
    }    

    private SegmentData createDNAcopySegmentData(DerivedDNAcopySegment segment, int segmentIndex) {
        SegmentData segmentData = new SegmentData();
        segmentData.setLocation(new ChromosomalLocation());
        segmentData.setNumberOfMarkers(segment.getMarkersPerSegment(segmentIndex));
        segmentData.setSegmentValue((float) segment.getAverageSegmentValue(segmentIndex));
        segmentData.getLocation().setChromosome(segment.getChromosomeIndex(segmentIndex));
        segmentData.getLocation().setStartPosition((int) segment.getStartMapPosition(segmentIndex));
        segmentData.getLocation().setEndPosition((int) segment.getEndMapPosition(segmentIndex));
        return segmentData;
    }
    
    private SegmentData createCGHcallSegmentData(DerivedCGHcallSegment segment, int segmentIndex) {
        SegmentData segmentData = new SegmentData();
        segmentData.setLocation(new ChromosomalLocation());
        segmentData.setNumberOfMarkers(segment.getMarkersPerSegment(segmentIndex));
        segmentData.setSegmentValue((float) segment.getAverageSegmentValue(segmentIndex));
        segmentData.getLocation().setChromosome(segment.getChromosomeIndex(segmentIndex));
        segmentData.getLocation().setStartPosition((int) segment.getStartMapPosition(segmentIndex));
        segmentData.getLocation().setEndPosition((int) segment.getEndMapPosition(segmentIndex));
        segmentData.setCallsValue((int) segment.getCalls(segmentIndex));
        segmentData.setProbabilityLoss((float) segment.getProbLoss(segmentIndex));
        segmentData.setProbabilityNormal((float) segment.getProbNorm(segmentIndex));
        segmentData.setProbabilityGain((float) segment.getProbGain(segmentIndex));
        segmentData.setProbabilityAmplification((float) segment.getProbAmp(segmentIndex));
        return segmentData;
    }    

    private Map<String, ArrayData> getArrayDataMap(DnaAnalysisData dnaAnalysisData) {
        Map<String, ArrayData> arrayDataMap = new HashMap<String, ArrayData>();
        for (ArrayData arrayData : dnaAnalysisData.getArrayDatas()) {
            arrayDataMap.put(String.valueOf(arrayData.getId()), arrayData);
        }
        return arrayDataMap;
    }

    private DNAcopyAssays buildDNAcopyAssays(DnaAnalysisData dnaAnalysisData) {
        DNAcopyAssays assays = new DNAcopyAssays();
        int reporterCount = getReporterCount(dnaAnalysisData.getReporters());
        configureMapInformationDNAcopy(dnaAnalysisData, assays, reporterCount);
        assays.setExpressionDataCollection(new ExpressionData[dnaAnalysisData.getArrayDatas().size()]);
        int index = 0;
        for (ArrayData arrayData : dnaAnalysisData.getArrayDatas()) {
            assays.setExpressionDataCollection(index, buildExpressionData(dnaAnalysisData, arrayData, reporterCount));
            index++;
        }
        return assays;
    }
    
    private CGHcallAssays buildCGHcallAssays(DnaAnalysisData dnaAnalysisData) {
        CGHcallAssays assays = new CGHcallAssays();
        int reporterCount = getReporterCount(dnaAnalysisData.getReporters());
        configureMapInformationCGHcall(dnaAnalysisData, assays, reporterCount);
        assays.setExpressionDataCollection(new CGHcallExpressionData[dnaAnalysisData.getArrayDatas().size()]);
        int index = 0;
        for (ArrayData arrayData : dnaAnalysisData.getArrayDatas()) {
            assays.setExpressionDataCollection(index,
                buildExpressionDataCGHcall(dnaAnalysisData, arrayData, reporterCount));
            index++;
        }
        return assays;
    }    

    private ExpressionData buildExpressionData(DnaAnalysisData dnaAnalysisData,
            ArrayData arrayData, int reporterCount) {
        ExpressionData data = new ExpressionData();
        data.setSampleId(String.valueOf(arrayData.getId()));
        double[] values = new double[reporterCount];
        int index = 0;
        for (int i = 0; i < dnaAnalysisData.getReporters().size(); i++) {
            DnaAnalysisReporter reporter = dnaAnalysisData.getReporters().get(i);
            if (reporter.hasValidLocation()) {
                values[index++] = dnaAnalysisData.getValues(arrayData)[i];
            }
        }
        data.setLogRatioValues(values);
        return data;
    }
    
    private CGHcallExpressionData buildExpressionDataCGHcall(DnaAnalysisData dnaAnalysisData,
            ArrayData arrayData, int reporterCount) {
        CGHcallExpressionData data = new CGHcallExpressionData();
        data.setSampleId(String.valueOf(arrayData.getId()));
        double[] values = new double[reporterCount];
        int index = 0;
        for (int i = 0; i < dnaAnalysisData.getReporters().size(); i++) {
            DnaAnalysisReporter reporter = dnaAnalysisData.getReporters().get(i);
            if (reporter.hasValidLocation()) {
                values[index++] = dnaAnalysisData.getValues(arrayData)[i];
            }
        }
        data.setLogRatioValues(values);
        return data;
    }    

    private void configureMapInformationDNAcopy(DnaAnalysisData dnaAnalysisData,
            DNAcopyAssays assays, int reporterCount) {
        assays.setChromsomeId(new int[reporterCount]);
        assays.setMapLocation(new long[reporterCount]);
        int index = 0;
        for (DnaAnalysisReporter reporter : dnaAnalysisData.getReporters()) {
            if (reporter.hasValidLocation()) {
                assays.setChromsomeId(index, reporter.getChromosomeAsInt());
                assays.setMapLocation(index++, reporter.getPosition());
            }
        }
    }
    
    private void configureMapInformationCGHcall(DnaAnalysisData dnaAnalysisData,
            CGHcallAssays assays, int reporterCount) {
        assays.setChromsomeId(new int[reporterCount]);
        assays.setMapLocation(new long[reporterCount]);
        int index = 0;
        for (DnaAnalysisReporter reporter : dnaAnalysisData.getReporters()) {
            if (reporter.hasValidLocation()) {
                assays.setChromsomeId(index, reporter.getChromosomeAsInt());
                assays.setMapLocation(index++, reporter.getPosition());
            }
        }
    }    

    private int getReporterCount(List<DnaAnalysisReporter> reporters) {
       int reporterCount = 0;
       for (DnaAnalysisReporter reporter : reporters) {
           if (reporter.hasValidLocation()) {
               reporterCount++;
           }
       } 
       return reporterCount;
    }
    
    /**
     * @return the clientFactory
     */
    public BioconductorClientFactory getClientFactory() {
        return clientFactory;
    }

    /**
     * @param clientFactory the clientFactory to set
     */
    public void setClientFactory(BioconductorClientFactory clientFactory) {
        this.clientFactory = clientFactory;
    }

}
