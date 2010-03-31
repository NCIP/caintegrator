package gov.nih.nci.caintegrator2.external.bioconductor;

import static org.junit.Assert.assertEquals;
import gov.nih.nci.caintegrator2.application.study.DnaAnalysisDataConfiguration;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator2.domain.genomic.DnaAnalysisData;
import gov.nih.nci.caintegrator2.domain.genomic.DnaAnalysisReporter;
import gov.nih.nci.caintegrator2.domain.genomic.SegmentData;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.DataRetrievalException;
import gov.nih.nci.caintegrator2.external.ServerConnectionProfile;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.axis.types.URI.MalformedURIException;
import org.bioconductor.cagrid.cadnacopy.DNAcopyAssays;
import org.bioconductor.cagrid.cadnacopy.DNAcopyParameter;
import org.bioconductor.cagrid.cadnacopy.DerivedDNAcopySegment;
import org.bioconductor.cagrid.cadnacopy.ExpressionData;
import org.bioconductor.packages.caDNAcopy.common.CaDNAcopyI;
import org.junit.Before;
import org.junit.Test;
import org.oasis.wsrf.properties.GetMultipleResourcePropertiesResponse;
import org.oasis.wsrf.properties.GetMultipleResourceProperties_Element;
import org.oasis.wsrf.properties.GetResourcePropertyResponse;
import org.oasis.wsrf.properties.QueryResourcePropertiesResponse;
import org.oasis.wsrf.properties.QueryResourceProperties_Element;

public class BioconductorServiceTest {

    private BioconductorServiceImpl service = new BioconductorServiceImpl();
    
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
        arrayData1.setId(0L);
        dnaAnalysisData.addDnaAnalysisData(arrayData1, new float[] {(float) 1.1, (float) 2.2, (float) 3.3});
        ArrayData arrayData2 = new ArrayData();
        arrayData2.setId(1L);
        dnaAnalysisData.addDnaAnalysisData(arrayData2, new float[] {(float) 4.4, (float) 5.5, (float) 6.6});
        DnaAnalysisDataConfiguration configuration = new DnaAnalysisDataConfiguration();
        configuration.setSegmentationService(new ServerConnectionProfile());
        configuration.setChangePointSignificanceLevel(0.0);
        configuration.setEarlyStoppingCriterion(0.0);
        configuration.setPermutationReplicates(0);
        configuration.setRandomNumberSeed(0);
        service.addSegmentationData(dnaAnalysisData, configuration);
        checkArrayDatas(dnaAnalysisData);
    }

    private DnaAnalysisReporter createReporter(String chromosome, int start) {
        DnaAnalysisReporter reporter = new DnaAnalysisReporter();
        reporter.setChromosome(chromosome);
        reporter.setPosition(start);
        return reporter;
    }

    private void checkArrayDatas(DnaAnalysisData dnaAnalysisData) {
        ArrayData arrayData1 = getArrayData(dnaAnalysisData, 0);
        assertEquals(2, arrayData1.getSegmentDatas().size());
        Iterator<SegmentData> iterator = arrayData1.getSegmentDatas().iterator();
        checkSegmentData(arrayData1, iterator.next(), 5, 1.1F, "1", 0, 9);
        checkSegmentData(arrayData1, iterator.next(), 10, 2.2F, "1", 10, 19);
        ArrayData arrayData2 = getArrayData(dnaAnalysisData, 1);
        assertEquals(1, arrayData2.getSegmentDatas().size());
        iterator = arrayData2.getSegmentDatas().iterator();
        checkSegmentData(arrayData2, iterator.next(), 15, 3.3F, "2", 20, 29);
    }

    private void checkSegmentData(ArrayData arrayData1, SegmentData segmentData1, int markers, float value, String chromosome, int start, int end) {
        assertEquals(arrayData1, segmentData1.getArrayData());
        assertEquals(markers, (int) segmentData1.getNumberOfMarkers());
        assertEquals(value, (float) segmentData1.getSegmentValue(), 0.0000001);
        assertEquals(chromosome, segmentData1.getLocation().getChromosome());
        assertEquals(start, (int) segmentData1.getLocation().getStartPosition());
        assertEquals(end, (int) segmentData1.getLocation().getEndPosition());
    }

    private ArrayData getArrayData(DnaAnalysisData dnaAnalysisData, long id) {
        for (ArrayData arrayData : dnaAnalysisData.getArrayDatas()) {
            if (arrayData.getId() == id) {
                return arrayData;
            }
        }
        return null;
    }

    private static void checkAssays(DNAcopyAssays assays) {
        compareIntArrays(new int[] {1, 1, 2}, assays.getChromsomeId());
        compareLongArrays(new long[] {0, 5, 0}, assays.getMapLocation());
        compareDoubleArrays(new double[] {1.1, 2.2, 3.3}, getExpressionData(assays, 0).getLogRatioValues());
        compareDoubleArrays(new double[] {4.4, 5.5, 6.6}, getExpressionData(assays, 1).getLogRatioValues());
    }

    private static ExpressionData getExpressionData(DNAcopyAssays assays, int id) {
        for (ExpressionData data : assays.getExpressionDataCollection()) {
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

    private static DerivedDNAcopySegment createTestSegment() {
        DerivedDNAcopySegment segment = new DerivedDNAcopySegment();
        segment.setAverageSegmentValue(new double[] {1.1, 2.2, 3.3});
        segment.setChromosomeIndex(new String[] {"1", "1", "2"});
        segment.setEndMapPosition(new long[] {9, 19, 29});
        segment.setMarkersPerSegment(new int[] {5, 10, 15});
        segment.setSampleId(new String[] {"0", "0", "1"});
        segment.setStartMapPosition(new long[] {0, 10, 20});
        return segment;
    }

    private static class TestClientFactory implements BioconductorClientFactory {

        public CaDNAcopyI getCaDNAcopyI(String url) throws MalformedURIException, RemoteException {
            return new CaDNAcopyI() {

                public DerivedDNAcopySegment getDerivedDNAcopySegment(DNAcopyAssays assays,
                        DNAcopyParameter acopyParameter) throws RemoteException {
                    checkAssays(assays);
                    return createTestSegment();
                }

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
                
            };
        }

    }

}
