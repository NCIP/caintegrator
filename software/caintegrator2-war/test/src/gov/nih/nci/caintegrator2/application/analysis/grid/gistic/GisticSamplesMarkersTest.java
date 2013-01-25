/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.analysis.grid.gistic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import edu.wustl.icr.asrv1.segment.ChromosomalSegment;
import edu.wustl.icr.asrv1.segment.SampleWithChromosomalSegmentSet;
import gov.nih.nci.caintegrator2.data.StudyHelper;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayDataType;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.domain.translational.Study;

import java.math.BigInteger;

import org.genepattern.gistic.Marker;
import org.junit.Before;
import org.junit.Test;


public class GisticSamplesMarkersTest {

    private Study study;
    private GisticSamplesMarkers gisticSamplesMarkers;
    
    @Before
    public void setUp() throws Exception {
        StudyHelper studyHelper = new StudyHelper();
        studyHelper.setArrayDataType(ArrayDataType.COPY_NUMBER);
        study = studyHelper.populateAndRetrieveStudy().getStudy();
        gisticSamplesMarkers = new GisticSamplesMarkers();
    }

    @Test
    public void testAddReporterListToGisticMarkers() {
        ReporterList reporterList = getArrayData().getReporterLists().iterator().next();
        
        gisticSamplesMarkers.addReporterListToGisticMarkers(reporterList);
        Marker marker = gisticSamplesMarkers.getMarkers()[0];
        assertEquals("1", marker.getChromosome());
        assertEquals("SNP_A_1677174", marker.getName());
        assertEquals(836727, marker.getPosition());
    }

    private ArrayData getArrayData() {
        return study.getArrayDatas(ReporterTypeEnum.DNA_ANALYSIS_REPORTER, null).iterator().next();
    }

    @Test
    public void testAddSegmentDataToGisticSamples() {
        gisticSamplesMarkers
                .addSegmentDataToGisticSamples(getArrayData().getSegmentDatas(), getArrayData().getSample());
        SampleWithChromosomalSegmentSet sample = gisticSamplesMarkers.getSamples()[0];
        ChromosomalSegment segment = sample.getSegments().getChromosomalSegment(0);
        assertTrue(sample.getName().contains("SAMPLE_"));
        int chromosomeNumber = Integer.valueOf(segment.getChromosomeNumber());
        assertEquals(BigInteger.valueOf(chromosomeNumber * 10000), segment.getSegmentStart());
        assertEquals(BigInteger.valueOf(chromosomeNumber * 20000), segment.getSegmentEnd());
    }

}
