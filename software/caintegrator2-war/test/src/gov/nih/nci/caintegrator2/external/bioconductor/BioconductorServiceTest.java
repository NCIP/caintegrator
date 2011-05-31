package gov.nih.nci.caintegrator2.external.bioconductor;

import static org.junit.Assert.assertEquals;
import gov.nih.nci.caintegrator2.application.study.DnaAnalysisDataConfiguration;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.common.DateUtil;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator2.domain.genomic.DnaAnalysisData;
import gov.nih.nci.caintegrator2.domain.genomic.DnaAnalysisReporter;
import gov.nih.nci.caintegrator2.domain.genomic.Sample;
import gov.nih.nci.caintegrator2.domain.genomic.SegmentData;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.DataRetrievalException;
import gov.nih.nci.caintegrator2.external.ServerConnectionProfile;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.axis.types.URI.MalformedURIException;

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

import org.junit.Before;
import org.junit.Test;
import org.oasis.wsrf.properties.GetMultipleResourcePropertiesResponse;
import org.oasis.wsrf.properties.GetMultipleResourceProperties_Element;
import org.oasis.wsrf.properties.GetResourcePropertyResponse;
import org.oasis.wsrf.properties.QueryResourcePropertiesResponse;
import org.oasis.wsrf.properties.QueryResourceProperties_Element;

public class BioconductorServiceTest {

    private static BioconductorServiceImpl service = new BioconductorServiceImpl();
    private static String STUDY_NAME = "UnitTestStudyName";
    private static Date STUDY_DEPLOYMENT_START_DATE = new Date();
    private static String SAMPLE_1_NAME = "UnitTestSample10000";
    private static String SAMPLE_2_NAME = "UnitTestSample20000";
    
    @Before
    public void setUp() {
        service.setClientFactory(new TestClientFactory());
    }

    @Test
    public void testAddSegmentationData() throws ConnectionException, DataRetrievalException {
        List<DnaAnalysisReporter> reporters = new ArrayList<DnaAnalysisReporter>();
        reporters.add(createReporter("1", 0));
        reporters.add(createReporter("1", 5));
        reporters.add(createReporter("2", 0));
        DnaAnalysisData dnaAnalysisData = new DnaAnalysisData(reporters);
        ArrayData arrayData1 = new ArrayData();
        // create study for access to study name and deployment date
        Study study = new Study();
        study.setShortTitleText(STUDY_NAME);
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        studyConfiguration.setDeploymentStartDate(STUDY_DEPLOYMENT_START_DATE);
        study.setStudyConfiguration(studyConfiguration);
        // create sample for access to sample name        
        Sample sample1 = new Sample();
        sample1.setName(SAMPLE_1_NAME);
        arrayData1.setId(0L);
        arrayData1.setSample(sample1);
        arrayData1.setStudy(study);
        dnaAnalysisData.addDnaAnalysisData(arrayData1, new float[] {(float) 1.1, (float) 2.2, (float) 3.3});
        //
        ArrayData arrayData2 = new ArrayData();
        Sample sample2 = new Sample();
        sample2.setName(SAMPLE_2_NAME);
        arrayData2.setId(1L);
        arrayData2.setSample(sample2);
        arrayData2.setStudy(study);
        dnaAnalysisData.addDnaAnalysisData(arrayData2, new float[] {(float) 4.4, (float) 5.5, (float) 6.6});
        DnaAnalysisDataConfiguration configuration = new DnaAnalysisDataConfiguration();
        configuration.setSegmentationService(new ServerConnectionProfile());
        configuration.setChangePointSignificanceLevel(0.0);
        configuration.setEarlyStoppingCriterion(0.0);
        configuration.setPermutationReplicates(0);
        configuration.setRandomNumberSeed(25);
        configuration.setUseCghCall(true);
        service.addSegmentationData(dnaAnalysisData, configuration);
        checkArrayDatasCGHcall(dnaAnalysisData);
        configuration.setUseCghCall(false);
        service.addSegmentationData(dnaAnalysisData, configuration);
        checkArrayDatasDNAcopy(dnaAnalysisData);
    }

    private DnaAnalysisReporter createReporter(String chromosome, int start) {
        DnaAnalysisReporter reporter = new DnaAnalysisReporter();
        reporter.setChromosome(chromosome);
        reporter.setPosition(start);
        return reporter;
    }

    private void checkArrayDatasDNAcopy(DnaAnalysisData dnaAnalysisData) {
        ArrayData arrayData1 = getArrayData(dnaAnalysisData, 0);
        assertEquals(2, arrayData1.getSegmentDatas().size());
        Iterator<SegmentData> iterator = arrayData1.getSegmentDatas().iterator();
        checkSegmentDataDNAcopy(arrayData1, iterator.next(), 5, 1.1F, "1", 0, 9);
        checkSegmentDataDNAcopy(arrayData1, iterator.next(), 10, 2.2F, "1", 10, 19);
        ArrayData arrayData2 = getArrayData(dnaAnalysisData, 1);
        assertEquals(1, arrayData2.getSegmentDatas().size());
        iterator = arrayData2.getSegmentDatas().iterator();
        checkSegmentDataDNAcopy(arrayData2, iterator.next(), 15, 3.3F, "2", 20, 29);
    }
    
    private void checkArrayDatasCGHcall(DnaAnalysisData dnaAnalysisData) {
        ArrayData arrayData1 = getArrayData(dnaAnalysisData, 0);
        assertEquals(2, arrayData1.getSegmentDatas().size());
        Iterator<SegmentData> iterator = arrayData1.getSegmentDatas().iterator();
        checkSegmentDataCGHcall(arrayData1, iterator.next(), 5, 1.1F, "1", 0, 9, -1, 0.1F, 0.1F, 0.1F, 0.1F);
        checkSegmentDataCGHcall(arrayData1, iterator.next(), 10, 2.2F, "1", 10, 19, 0, 0.2F, 0.2F, 0.2F, 0.2F);
        ArrayData arrayData2 = getArrayData(dnaAnalysisData, 1);
        assertEquals(1, arrayData2.getSegmentDatas().size());
        iterator = arrayData2.getSegmentDatas().iterator();
        checkSegmentDataCGHcall(arrayData2, iterator.next(), 15, 3.3F, "2", 20, 29, 1, 0.3F, 0.3F, 0.3F, 0.3F);
    }    

    private void checkSegmentDataDNAcopy(ArrayData arrayData1,
                                            SegmentData segmentData1,
                                            int markers,
                                            float value,
                                            String chromosome,
                                            int start,
                                            int end) {
        assertEquals(arrayData1, segmentData1.getArrayData());
        assertEquals(markers, (int) segmentData1.getNumberOfMarkers());
        assertEquals(value, (float) segmentData1.getSegmentValue(), 0.0000001);
        assertEquals(chromosome, segmentData1.getLocation().getChromosome());
        assertEquals(start, (int) segmentData1.getLocation().getStartPosition());
        assertEquals(end, (int) segmentData1.getLocation().getEndPosition());
    }
    
    private void checkSegmentDataCGHcall(ArrayData arrayData1,
                                            SegmentData segmentData1,
                                            int markers,
                                            float value,
                                            String chromosome,
                                            int start,
                                            int end,
                                            int calls,
                                            float probLoss,
                                            float probNorm,
                                            float probGain,
                                            float probAmp) {
        assertEquals(arrayData1, segmentData1.getArrayData());
        assertEquals(markers, (int) segmentData1.getNumberOfMarkers());
        assertEquals(value, (float) segmentData1.getSegmentValue(), 0.0000001);
        assertEquals(chromosome, segmentData1.getLocation().getChromosome());
        assertEquals(start, (int) segmentData1.getLocation().getStartPosition());
        assertEquals(end, (int) segmentData1.getLocation().getEndPosition());
        assertEquals(calls, (int) segmentData1.getCallsValue());
        assertEquals(probLoss, (float) segmentData1.getProbabilityLoss(), 0.0000001);
        assertEquals(probNorm, (float) segmentData1.getProbabilityNormal(), 0.0000001);
        assertEquals(probGain, (float) segmentData1.getProbabilityGain(), 0.0000001);
        assertEquals(probAmp, (float) segmentData1.getProbabilityAmplification(), 0.0000001);
    }    

    private ArrayData getArrayData(DnaAnalysisData dnaAnalysisData, long id) {
        for (ArrayData arrayData : dnaAnalysisData.getArrayDatas()) {
            if (arrayData.getId() == id) {
                return arrayData;
            }
        }
        return null;
    }

    private static void checkAssaysCGHcall(CGHcallAssays assays) {
        compareIntArrays(new int[] {1, 1, 2}, assays.getChromsomeId());
        compareLongArrays(new long[] {0, 5, 0}, assays.getMapLocation());
        String sampleName1 = service.makeId(STUDY_NAME,SAMPLE_1_NAME,DateUtil.getFilenameTimeStamp(STUDY_DEPLOYMENT_START_DATE));
        String sampleName2 = service.makeId(STUDY_NAME,SAMPLE_2_NAME,DateUtil.getFilenameTimeStamp(STUDY_DEPLOYMENT_START_DATE));
        compareDoubleArrays(new double[] {1.1, 2.2, 3.3}, getExpressionDataCGHcall(assays, sampleName1).getLogRatioValues());
        compareDoubleArrays(new double[] {4.4, 5.5, 6.6}, getExpressionDataCGHcall(assays, sampleName2).getLogRatioValues());
    }
    
    private static void checkAssaysDNAcopy(DNAcopyAssays assays) {
        compareIntArrays(new int[] {1, 1, 2}, assays.getChromsomeId());
        compareLongArrays(new long[] {0, 5, 0}, assays.getMapLocation());
        String sampleName1 = service.makeId(STUDY_NAME,SAMPLE_1_NAME,DateUtil.getFilenameTimeStamp(STUDY_DEPLOYMENT_START_DATE));
        String sampleName2 = service.makeId(STUDY_NAME,SAMPLE_2_NAME,DateUtil.getFilenameTimeStamp(STUDY_DEPLOYMENT_START_DATE));
        compareDoubleArrays(new double[] {1.1, 2.2, 3.3}, getExpressionDataDNAcopy(assays, sampleName1).getLogRatioValues());
        compareDoubleArrays(new double[] {4.4, 5.5, 6.6}, getExpressionDataDNAcopy(assays, sampleName2).getLogRatioValues());
    }      

    private static ExpressionData getExpressionDataDNAcopy(DNAcopyAssays assays, String id) {
        for (ExpressionData data : assays.getExpressionDataCollection()) {
            if (data.getSampleId().equals(String.valueOf(id))) {
                return data;
            }
        }
        return null;
    }
    
    private static CGHcallExpressionData getExpressionDataCGHcall(CGHcallAssays assays, String id) {
        for (CGHcallExpressionData data : assays.getExpressionDataCollection()) {
            if (data.getSampleId().equals(String.valueOf(id))) {
                return data;
            }
        }
        return null;
    }    

    private static void compareIntArrays(int[] expecteds, int[] actuals) {
        assertEquals(expecteds.length, actuals.length);
        for (int i = 0; i < expecteds.length; i++) {
            assertEquals("Incorrect value at " + i, expecteds[i], actuals[i]);
        }
    }

    private static void compareLongArrays(long[] expecteds, long[] actuals) {
        assertEquals(expecteds.length, actuals.length);
        for (int i = 0; i < expecteds.length; i++) {
            assertEquals("Incorrect value at " + i, expecteds[i], actuals[i]);
        }
    }
    private static void compareDoubleArrays(double[] expecteds, double[] actuals) {
        assertEquals(expecteds.length, actuals.length);
        for (int i = 0; i < expecteds.length; i++) {
            assertEquals("Incorrect value at " + i, expecteds[i], actuals[i], 0.000001);
        }
    }

    private static DerivedDNAcopySegment createDNAcopyTestSegment() {
        DerivedDNAcopySegment segment = new DerivedDNAcopySegment();
        segment.setAverageSegmentValue(new double[] {1.1, 2.2, 3.3});
        segment.setChromosomeIndex(new String[] {"1", "1", "2"});
        segment.setEndMapPosition(new long[] {9, 19, 29});
        segment.setMarkersPerSegment(new int[] {5, 10, 15});
        String sampleName1 = service.makeId(STUDY_NAME,SAMPLE_1_NAME,DateUtil.getFilenameTimeStamp(STUDY_DEPLOYMENT_START_DATE));
        String sampleName2 = service.makeId(STUDY_NAME,SAMPLE_2_NAME,DateUtil.getFilenameTimeStamp(STUDY_DEPLOYMENT_START_DATE));
        segment.setSampleId(new String[] {sampleName1, sampleName1, sampleName2});
        segment.setStartMapPosition(new long[] {0, 10, 20});
        return segment;
    }
    
    private static DerivedCGHcallSegment createCGHcallTestSegment() {
        DerivedCGHcallSegment segment = new DerivedCGHcallSegment();
        segment.setAverageSegmentValue(new double[] {1.1, 2.2, 3.3});
        segment.setChromosomeIndex(new String[] {"1", "1", "2"});
        segment.setEndMapPosition(new long[] {9, 19, 29});
        segment.setMarkersPerSegment(new int[] {5, 10, 15});
        String sampleName1 = service.makeId(STUDY_NAME,SAMPLE_1_NAME,DateUtil.getFilenameTimeStamp(STUDY_DEPLOYMENT_START_DATE));
        String sampleName2 = service.makeId(STUDY_NAME,SAMPLE_2_NAME,DateUtil.getFilenameTimeStamp(STUDY_DEPLOYMENT_START_DATE));
        segment.setSampleId(new String[] {sampleName1, sampleName1, sampleName2});
        segment.setStartMapPosition(new long[] {0, 10, 20});
        segment.setCalls(new int[] {-1, 0, 1});
        segment.setProbLoss(new double[] {0.1, 0.2, 0.3});
        segment.setProbNorm(new double[] {0.1, 0.2, 0.3});
        segment.setProbGain(new double[] {0.1, 0.2, 0.3});
        segment.setProbAmp(new double[] {0.1, 0.2, 0.3});
        return segment;
    }    

    private static class TestClientFactory implements BioconductorClientFactory {

        public CaDNAcopyI getCaDNAcopyI(String url) throws MalformedURIException, RemoteException {
            return new CaDNAcopyI() {

                public GetMultipleResourcePropertiesResponse getMultipleResourceProperties(
                        GetMultipleResourceProperties_Element params) throws RemoteException {
                    return null;
                }

                public GetResourcePropertyResponse getResourceProperty(QName arg0) throws RemoteException {
                    return null;
                }

                public QueryResourcePropertiesResponse queryResourceProperties(QueryResourceProperties_Element arg0)
                        throws RemoteException {
                    return null;
                }

				public DerivedDNAcopySegment getDerivedDNAcopySegment(
				        DNAcopyAssays dNAcopyAssays,
				        DNAcopyParameter dNAcopyParameter)
						throws RemoteException {
				    checkAssaysDNAcopy(dNAcopyAssays);
                    return createDNAcopyTestSegment();
				}
                
            };
        }

		/* (non-Javadoc)
		 * @see gov.nih.nci.caintegrator2.external.bioconductor.BioconductorClientFactory#getCaCGHcallI(java.lang.String)
		 */
		public CaCGHcallI getCaCGHcallI(String url)
				throws MalformedURIException, RemoteException {
            return new CaCGHcallI() {

                public GetMultipleResourcePropertiesResponse getMultipleResourceProperties(
                        GetMultipleResourceProperties_Element params) throws RemoteException {
                    return null;
                }

                public GetResourcePropertyResponse getResourceProperty(QName arg0) throws RemoteException {
                    return null;
                }

                public QueryResourcePropertiesResponse queryResourceProperties(QueryResourceProperties_Element arg0)
                        throws RemoteException {
                    return null;
                }

				public DerivedCGHcallSegment getDerivedCGHcallSegment(
						CGHcallAssays dNAcopyAssays,
						CGHcallParameter dNAcopyParameter)
						throws RemoteException {
                    checkAssaysCGHcall(dNAcopyAssays);
                    return createCGHcallTestSegment();
				}
                
            };
        }

    }

}
