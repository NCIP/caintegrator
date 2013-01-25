/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataServiceStub;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.data.CaIntegrator2DaoStub;
import gov.nih.nci.caintegrator2.domain.application.CopyNumberAlterationCriterion;
import gov.nih.nci.caintegrator2.domain.application.CopyNumberCriterionTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.GenomicCriterionTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.ResultRow;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.genomic.Array;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator2.domain.genomic.Platform;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.domain.genomic.Sample;
import gov.nih.nci.caintegrator2.domain.genomic.SampleAcquisition;
import gov.nih.nci.caintegrator2.domain.genomic.SegmentData;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.domain.translational.StudySubjectAssignment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;


public class CopyNumberAlterationCriterionHandlerTest {

    private CaIntegrator2DaoStub daoStub = new DaoStub();
    private ArrayDataServiceStub arrayDataServiceStub = new ArrayDataServiceStub();
    private Query query;
    private Study study;
    private SegmentData segmentData;
    
    @Before
    public void setUp() {
        Platform platform = daoStub.getPlatform("platformName");
        ReporterList reporterList = platform.addReporterList("reporterList", ReporterTypeEnum.DNA_ANALYSIS_REPORTER);
        
        daoStub.clear();       
        study = new Study();
        query = new Query();
        query.setCopyNumberPlatform(platform);
        StudySubscription subscription = new StudySubscription();
        subscription.setStudy(study);
        query.setSubscription(subscription);
        StudySubjectAssignment assignment = new StudySubjectAssignment();
        study.getAssignmentCollection().add(assignment);
        SampleAcquisition acquisition = new SampleAcquisition();
        Sample sample = new Sample();
        Array array = new Array();
        array.setPlatform(platform);
        ArrayData arrayData = new ArrayData();
        arrayData.setStudy(study);
        arrayData.setArray(array);
        arrayData.getReporterLists().add(reporterList);
        reporterList.getArrayDatas().add(arrayData);
        arrayData.setSample(sample);
        sample.setSampleAcquisition(acquisition);
        sample.getArrayDataCollection().add(arrayData);
        segmentData = new SegmentData();
        arrayData.getSegmentDatas().add(segmentData);
        segmentData.setArrayData(arrayData);
        acquisition.setSample(sample);
        assignment.getSampleAcquisitionCollection().add(acquisition);
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        studyConfiguration.getGenomicDataSources().add(new GenomicDataSourceConfiguration());
        study.setStudyConfiguration(studyConfiguration);
    }

    @Test
    public void testGetMatches() throws InvalidCriterionException {        
        CopyNumberAlterationCriterion criterion = new CopyNumberAlterationCriterion();
        CopyNumberAlterationCriterionHandler handler = CopyNumberAlterationCriterionHandler.create(criterion);
        Set<ResultRow> rows = new HashSet<ResultRow>();
        rows = handler.getMatches(daoStub, arrayDataServiceStub, query, new HashSet<EntityTypeEnum>());
        assertEquals(1, rows.size());
    }
    
    @Test
    public void testGetSegmentDataMatches() throws InvalidCriterionException {
        CopyNumberAlterationCriterion criterion = new CopyNumberAlterationCriterion();
        CopyNumberAlterationCriterionHandler handler = CopyNumberAlterationCriterionHandler.create(criterion);
        Set<SegmentData> segments = handler.getSegmentDataMatches(daoStub, study, null);
        assertEquals(1, segments.size());
        assertEquals(segmentData, segments.iterator().next());
    }
    
    @Test
    public void testGetters() {
        String platformName = "Test Platform";
        CopyNumberAlterationCriterion criterion = new CopyNumberAlterationCriterion();
        criterion.setPlatformName(platformName);
        
        CopyNumberAlterationCriterionHandler handler = CopyNumberAlterationCriterionHandler.create(criterion);
        assertFalse(handler.hasReporterCriterion());
        assertFalse(handler.isReporterMatchHandler());
        assertTrue(handler.hasCriterionSpecifiedSegmentValues());
        assertFalse(handler.hasCriterionSpecifiedReporterValues());
        assertEquals(GenomicCriteriaMatchTypeEnum.MATCH_POSITIVE_OR_NEGATIVE, handler.getSegmentValueMatchCriterionType(2f));
        
        criterion.setLowerLimit(1f);
        assertEquals(GenomicCriteriaMatchTypeEnum.MATCH_POSITIVE_OR_NEGATIVE, handler.getSegmentValueMatchCriterionType(2f));
        assertEquals(GenomicCriteriaMatchTypeEnum.NO_MATCH, handler.getSegmentValueMatchCriterionType(.1f));
        
        criterion.setLowerLimit(null);
        criterion.setUpperLimit(4f);
        assertEquals(GenomicCriteriaMatchTypeEnum.MATCH_POSITIVE_OR_NEGATIVE, handler.getSegmentValueMatchCriterionType(4f));
        assertEquals(GenomicCriteriaMatchTypeEnum.NO_MATCH, handler.getSegmentValueMatchCriterionType(5f));
        
        criterion.setLowerLimit(2f);
        assertEquals(GenomicCriteriaMatchTypeEnum.MATCH_POSITIVE_OR_NEGATIVE, handler.getSegmentValueMatchCriterionType(2f));
        assertEquals(GenomicCriteriaMatchTypeEnum.NO_MATCH, handler.getSegmentValueMatchCriterionType(1f));
        assertEquals(GenomicCriteriaMatchTypeEnum.NO_MATCH, handler.getSegmentValueMatchCriterionType(5f));
        
        criterion.setCopyNumberCriterionType(CopyNumberCriterionTypeEnum.CALLS_VALUE);
        Set<Integer> callsValues = new HashSet<Integer>();
        callsValues.add(Integer.decode("1"));
        criterion.setCallsValues(callsValues);
        assertTrue(handler.hasCriterionSpecifiedSegmentValues());
        assertTrue(handler.hasCriterionSpecifiedSegmentCallsValues());
        assertEquals(GenomicCriteriaMatchTypeEnum.MATCH_POSITIVE_OR_NEGATIVE, handler.getSegmentCallsValueMatchCriterionType(1));
        assertFalse(GenomicCriteriaMatchTypeEnum.MATCH_POSITIVE_OR_NEGATIVE.equals(handler.getSegmentCallsValueMatchCriterionType(2)));
        
        // test for various upper and lower limits
        criterion.setLowerLimit(2f);
        assertEquals(String.valueOf(2f),criterion.getDisplayLowerLimit());
        criterion.setUpperLimit(1f);
        assertEquals(String.valueOf(1f),criterion.getDisplayUpperLimit());
        assertFalse(criterion.isInsideBoundaryType());
        assertEquals(GenomicCriteriaMatchTypeEnum.MATCH_POSITIVE_OR_NEGATIVE, handler.getSegmentValueMatchCriterionType(2f));
        criterion.setLowerLimit(1f);
        criterion.setUpperLimit(2f);
        assertTrue(criterion.isInsideBoundaryType());
        assertEquals(GenomicCriteriaMatchTypeEnum.NO_MATCH, handler.getSegmentValueMatchCriterionType(3f));
        criterion.setLowerLimit(3f);
        criterion.setUpperLimit(1f);
        assertEquals(GenomicCriteriaMatchTypeEnum.MATCH_POSITIVE_OR_NEGATIVE, handler.getSegmentValueMatchCriterionType(4f));
        // test rest of criterion value possibilities
        criterion.setLowerLimit(1f);
        criterion.setUpperLimit(3f);
        assertEquals("",criterion.getDisplayChromosomeCoordinateHigh());
        criterion.setChromosomeCoordinateHigh(Integer.valueOf("2000"));
        assertEquals("2000",criterion.getDisplayChromosomeCoordinateHigh());
        assertEquals("",criterion.getDisplayChromosomeCoordinateLow());
        criterion.setChromosomeCoordinateLow(Integer.valueOf("2"));
        assertEquals("2",criterion.getDisplayChromosomeCoordinateLow());
        criterion.setChromosomeNumber("13");
        assertEquals("13",criterion.getChromosomeNumber());
        // test for platform name
        assertEquals(platformName,criterion.getPlatformName(GenomicCriterionTypeEnum.COPY_NUMBER));
        assertEquals(null,criterion.getPlatformName(GenomicCriterionTypeEnum.GENE_EXPRESSION));
    }
    
    private class DaoStub extends CaIntegrator2DaoStub {
        

        @Override
        public List<SegmentData> findMatchingSegmentDatas(CopyNumberAlterationCriterion copyNumberCriterion,
                Study study, Platform platform) {
            List<SegmentData> segmentDatas = new ArrayList<SegmentData>();
            segmentDatas.add(segmentData);
            return segmentDatas;
        }
        
        @Override
        public List<SegmentData> findMatchingSegmentDatasByLocation(List<SegmentData> segmentDatasToMatch, 
                Study study, Platform platform) {
            return findMatchingSegmentDatas(null, study, platform);
        }
        
    }
 
}
