/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
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
import org.bioconductor.cagrid.cadnacopy.DerivedDNAcopySegment;
import org.bioconductor.cagrid.cadnacopy.ExpressionData;
import org.bioconductor.packages.caDNAcopy.common.CaDNAcopyI;

/**
 * Implementation that uses Bioconductor grid services.
 */
public class BioconductorServiceImpl implements BioconductorService {
    
    private static final Logger LOGGER = Logger.getLogger(BioconductorServiceImpl.class);
    private BioconductorClientFactory clientFactory = new BioconductorClientFactoryImpl();

    /**
     * {@inheritDoc}
     * @throws DataRetrievalException 
     */
    public void addSegmentationData(DnaAnalysisData dnaAnalysisData,
            DnaAnalysisDataConfiguration configuration) 
    throws ConnectionException, DataRetrievalException {
        String url = configuration.getSegmentationService().getUrl();
        try {
            CaDNAcopyI client = getClient(url);
            DNAcopyAssays assays = buildAssays(dnaAnalysisData);
            DNAcopyParameter parameter = new DNAcopyParameter();
            parameter.setChangePointSignificanceLevel(configuration.getChangePointSignificanceLevel());
            parameter.setEarlyStoppingCriterion(configuration.getEarlyStoppingCriterion());
            parameter.setPermutationReplicates(configuration.getPermutationReplicates());
            parameter.setRandomNumberSeed(configuration.getRandomNumberSeed());
            DerivedDNAcopySegment segment = client.getDerivedDNAcopySegment(assays, parameter);
            addSegmentationData(segment, dnaAnalysisData);
        } catch (RemoteException e) {
            LOGGER.error("Couldn't complete CaDNACopy job", e);
            throw new DataRetrievalException("Couldn't complete CaDNACopy job: " + e.getMessage(), e);
        }
    }

    private CaDNAcopyI getClient(String url) throws ConnectionException {
        try {
            return getClientFactory().getCaDNAcopyI(url);
        } catch (MalformedURIException e) {
            throw new ConnectionException("Invalid URL: " + url, e);
        } catch (RemoteException e) {
            LOGGER.error("Couldn't connect to CaDNACopy service", e);
            throw new ConnectionException("Couldn't connect to " + url + ": " + e.getMessage(), e);
        }
    }

    private void addSegmentationData(DerivedDNAcopySegment segment, DnaAnalysisData dnaAnalysisData) {
        Map<String, ArrayData> arrayDataMap = getArrayDataMap(dnaAnalysisData);
        for (int segmentIndex = 0;  segmentIndex < segment.getSampleId().length; segmentIndex++) {
            ArrayData arrayData = arrayDataMap.get(arrayDataKey(segment, segmentIndex));
            SegmentData segmentData = createSegmentData(segment, segmentIndex);
            segmentData.setArrayData(arrayData);
            arrayData.getSegmentDatas().add(segmentData);
        }
    }

    private String arrayDataKey(DerivedDNAcopySegment segment, int segmentIndex) {
        String sampleId = segment.getSampleId(segmentIndex);
        if (sampleId.charAt(0) == 'X') {
            return sampleId.substring(1);
        } else {
            return sampleId;
        }
    }

    private SegmentData createSegmentData(DerivedDNAcopySegment segment, int segmentIndex) {
        SegmentData segmentData = new SegmentData();
        segmentData.setLocation(new ChromosomalLocation());
        segmentData.setNumberOfMarkers(segment.getMarkersPerSegment(segmentIndex));
        segmentData.setSegmentValue((float) segment.getAverageSegmentValue(segmentIndex));
        segmentData.getLocation().setChromosome(segment.getChromosomeIndex(segmentIndex));
        segmentData.getLocation().setStartPosition((int) segment.getStartMapPosition(segmentIndex));
        segmentData.getLocation().setEndPosition((int) segment.getEndMapPosition(segmentIndex));
        return segmentData;
    }

    private Map<String, ArrayData> getArrayDataMap(DnaAnalysisData dnaAnalysisData) {
        Map<String, ArrayData> arrayDataMap = new HashMap<String, ArrayData>();
        for (ArrayData arrayData : dnaAnalysisData.getArrayDatas()) {
            arrayDataMap.put(String.valueOf(arrayData.getId()), arrayData);
        }
        return arrayDataMap;
    }

    private DNAcopyAssays buildAssays(DnaAnalysisData dnaAnalysisData) {
        DNAcopyAssays assays = new DNAcopyAssays();
        int reporterCount = getReporterCount(dnaAnalysisData.getReporters());
        configureMapInformation(dnaAnalysisData, assays, reporterCount);
        assays.setExpressionDataCollection(new ExpressionData[dnaAnalysisData.getArrayDatas().size()]);
        int index = 0;
        for (ArrayData arrayData : dnaAnalysisData.getArrayDatas()) {
            assays.setExpressionDataCollection(index, buildExpressionData(dnaAnalysisData, arrayData, reporterCount));
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

    private void configureMapInformation(DnaAnalysisData dnaAnalysisData, DNAcopyAssays assays, int reporterCount) {
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
