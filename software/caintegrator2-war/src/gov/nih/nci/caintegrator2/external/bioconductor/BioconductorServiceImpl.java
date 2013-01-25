/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.external.bioconductor;

import gov.nih.nci.caintegrator2.application.study.DnaAnalysisDataConfiguration;
import gov.nih.nci.caintegrator2.common.DateUtil;
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
        LOGGER.info("End Retrieving segment from BioConductor caCGHcall: "
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
      LOGGER.info("End Retrieving segment from caDNAcopy: "
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
            arrayDataMap.put(makeIdForSegmentation(arrayData), arrayData);
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
        String sampleName = makeIdForSegmentation(arrayData);
        ExpressionData data = new ExpressionData();
        data.setSampleId(sampleName);
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
        String sampleName = makeIdForSegmentation(arrayData);
        CGHcallExpressionData data = new CGHcallExpressionData();
        data.setSampleId(sampleName);
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
    
    /**
     * @param arrayData the arrayData element for which an identifier will be created.
     */
    private String makeIdForSegmentation(ArrayData arrayData) {

        String studyName = "";
        String sampleName = "";
        String date = "";
        
        studyName = arrayData.getStudy().getShortTitleText();
        sampleName = arrayData.getSample().getName();
        date = DateUtil.getFilenameTimeStamp(arrayData.getStudy().getStudyConfiguration().getDeploymentStartDate());
        
        return String.valueOf(makeId(studyName, sampleName, date));
    }
    
    /**
     * @param studyName the study name string.
     * @param sampleName the sample name string.
     * @param date the date string.
     * @return single formatted string.
     */
    public String makeId(String studyName, String sampleName, String date) {
        String hold = String.valueOf(studyName + "_" + sampleName + "_" + date);
        hold = hold.replaceAll("[^a-zA-Z0-9]", ".");
        return String.valueOf(hold);
    }    

}
