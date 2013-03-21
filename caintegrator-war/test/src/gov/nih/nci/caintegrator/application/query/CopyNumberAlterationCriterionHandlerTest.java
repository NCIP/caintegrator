/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.when;
import gov.nih.nci.caintegrator.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator.domain.application.CopyNumberAlterationCriterion;
import gov.nih.nci.caintegrator.domain.application.CopyNumberCriterionTypeEnum;
import gov.nih.nci.caintegrator.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator.domain.application.Query;
import gov.nih.nci.caintegrator.domain.application.ResultRow;
import gov.nih.nci.caintegrator.domain.application.StudySubscription;
import gov.nih.nci.caintegrator.domain.genomic.Array;
import gov.nih.nci.caintegrator.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator.domain.genomic.Platform;
import gov.nih.nci.caintegrator.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator.domain.genomic.Sample;
import gov.nih.nci.caintegrator.domain.genomic.SampleAcquisition;
import gov.nih.nci.caintegrator.domain.genomic.SegmentData;
import gov.nih.nci.caintegrator.domain.translational.Study;
import gov.nih.nci.caintegrator.domain.translational.StudySubjectAssignment;
import gov.nih.nci.caintegrator.mockito.AbstractMockitoTest;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the copy number alteration criterion handler.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
public class CopyNumberAlterationCriterionHandlerTest extends AbstractMockitoTest {
    private Query query;
    private Study study;
    private SegmentData segmentData;

    /**
     * Unit test setup.
     * @throws Exception on error
     */
    @Before
    public void setUp() throws Exception {
        Platform platform = dao.getPlatform("platformName");
        ReporterList reporterList = platform.addReporterList("reporterList", ReporterTypeEnum.DNA_ANALYSIS_REPORTER);

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
        sample.getSampleAcquisitions().add(acquisition);
        sample.getArrayDataCollection().add(arrayData);
        segmentData = new SegmentData();
        arrayData.getSegmentDatas().add(segmentData);
        segmentData.setArrayData(arrayData);
        acquisition.setSample(sample);
        assignment.getSampleAcquisitionCollection().add(acquisition);
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        studyConfiguration.getGenomicDataSources().add(new GenomicDataSourceConfiguration());
        study.setStudyConfiguration(studyConfiguration);

        when(dao.findMatchingSegmentDatas(any(CopyNumberAlterationCriterion.class), any(Study.class),
                any(Platform.class))).thenReturn(Arrays.asList(segmentData));
        when(dao.findMatchingSegmentDatasByLocation(anyListOf(SegmentData.class), any(Study.class),
                any(Platform.class))).thenReturn(Arrays.asList(segmentData));
    }

    /**
     * Tests match retrieval.
     *
     * @throws InvalidCriterionException on unexpected invalid criterion error
     */
    @Test
    public void getMatches() throws InvalidCriterionException {
        CopyNumberAlterationCriterion criterion = new CopyNumberAlterationCriterion();
        CopyNumberAlterationCriterionHandler handler = CopyNumberAlterationCriterionHandler.create(criterion);
        Set<ResultRow> rows = handler.getMatches(dao, arrayDataService, query, new HashSet<EntityTypeEnum>());
        assertEquals(1, rows.size());
    }

    /**
     * Tests segment data retrieval.
     *
     * @throws InvalidCriterionException on unexpected invalid criterion error
     */
    @Test
    public void getSegmentDataMatches() throws InvalidCriterionException {
        CopyNumberAlterationCriterion criterion = new CopyNumberAlterationCriterion();
        CopyNumberAlterationCriterionHandler handler = CopyNumberAlterationCriterionHandler.create(criterion);
        Set<SegmentData> segments = handler.getSegmentDataMatches(dao, study, null);
        assertEquals(1, segments.size());
        assertEquals(segmentData, segments.iterator().next());
    }

    /**
     * Tests that the handler doesn't attempt to deal with data is should not.
     */
    @Test
    public void capabilities() {
        CopyNumberAlterationCriterion criterion = new CopyNumberAlterationCriterion();

        CopyNumberAlterationCriterionHandler handler = CopyNumberAlterationCriterionHandler.create(criterion);
        assertFalse(handler.hasReporterCriterion());
        assertFalse(handler.isReporterMatchHandler());
        assertTrue(handler.hasCriterionSpecifiedSegmentValues());
        assertFalse(handler.hasCriterionSpecifiedReporterValues());
    }

    /**
     * Tests for various segment value match criterion.
     */
    @Test
    public void matchCriterion() {
        CopyNumberAlterationCriterion criterion = new CopyNumberAlterationCriterion();

        CopyNumberAlterationCriterionHandler handler = CopyNumberAlterationCriterionHandler.create(criterion);
        assertEquals(GenomicCriteriaMatchTypeEnum.MATCH_POSITIVE_OR_NEGATIVE,
                handler.getSegmentValueMatchCriterionType(2f));

        criterion.setLowerLimit(1f);
        assertEquals(GenomicCriteriaMatchTypeEnum.MATCH_POSITIVE_OR_NEGATIVE,
                handler.getSegmentValueMatchCriterionType(2f));
        assertEquals(GenomicCriteriaMatchTypeEnum.NO_MATCH, handler.getSegmentValueMatchCriterionType(0f));

        criterion.setLowerLimit(null);
        criterion.setUpperLimit(4f);
        assertEquals(GenomicCriteriaMatchTypeEnum.MATCH_POSITIVE_OR_NEGATIVE,
                handler.getSegmentValueMatchCriterionType(4f));
        assertEquals(GenomicCriteriaMatchTypeEnum.NO_MATCH,
                handler.getSegmentValueMatchCriterionType(5f));

        criterion.setLowerLimit(2f);
        assertEquals(GenomicCriteriaMatchTypeEnum.MATCH_POSITIVE_OR_NEGATIVE,
                handler.getSegmentValueMatchCriterionType(2f));
        assertEquals(GenomicCriteriaMatchTypeEnum.NO_MATCH, handler.getSegmentValueMatchCriterionType(1f));
        assertEquals(GenomicCriteriaMatchTypeEnum.NO_MATCH, handler.getSegmentValueMatchCriterionType(5f));
    }

    /**
     * Tests for handling calls value criterion type.
     */
    @Test
    public void callsValues() {
        Set<Integer> callsValues = new HashSet<Integer>();
        callsValues.add(1);

        CopyNumberAlterationCriterion criterion = new CopyNumberAlterationCriterion();
        criterion.setCopyNumberCriterionType(CopyNumberCriterionTypeEnum.CALLS_VALUE);
        criterion.setCallsValues(callsValues);

        CopyNumberAlterationCriterionHandler handler = CopyNumberAlterationCriterionHandler.create(criterion);

        assertTrue(handler.hasCriterionSpecifiedSegmentValues());
        assertTrue(handler.hasCriterionSpecifiedSegmentCallsValues());
        assertEquals(GenomicCriteriaMatchTypeEnum.MATCH_POSITIVE_OR_NEGATIVE,
                handler.getSegmentCallsValueMatchCriterionType(1));
        assertFalse(GenomicCriteriaMatchTypeEnum.MATCH_POSITIVE_OR_NEGATIVE
                .equals(handler.getSegmentCallsValueMatchCriterionType(2)));
    }

    /**
     * Tests display of limits.
     */
    @Test
    public void displayLimits() {
        CopyNumberAlterationCriterion criterion = new CopyNumberAlterationCriterion();
        CopyNumberAlterationCriterionHandler handler = CopyNumberAlterationCriterionHandler.create(criterion);

        criterion.setLowerLimit(2f);
        criterion.setUpperLimit(1f);
        assertEquals("2.0", criterion.getDisplayLowerLimit());
        assertEquals("1.0", criterion.getDisplayUpperLimit());
        assertFalse(criterion.isInsideBoundaryType());
        assertEquals(GenomicCriteriaMatchTypeEnum.MATCH_POSITIVE_OR_NEGATIVE,
                handler.getSegmentValueMatchCriterionType(2f));

    }

    /**
     * Ensures that boundaries can be determined correctly based on lower and upper limits.
     */
    @Test
    public void insideBoundary() {
        CopyNumberAlterationCriterion criterion = new CopyNumberAlterationCriterion();
        CopyNumberAlterationCriterionHandler handler = CopyNumberAlterationCriterionHandler.create(criterion);

        criterion.setLowerLimit(2f);
        criterion.setUpperLimit(1f);

        assertFalse(criterion.isInsideBoundaryType());
        assertEquals(GenomicCriteriaMatchTypeEnum.MATCH_POSITIVE_OR_NEGATIVE,
                handler.getSegmentValueMatchCriterionType(2f));

        criterion.setLowerLimit(1f);
        criterion.setUpperLimit(2f);
        assertTrue(criterion.isInsideBoundaryType());
        assertEquals(GenomicCriteriaMatchTypeEnum.NO_MATCH, handler.getSegmentValueMatchCriterionType(3f));

        criterion.setLowerLimit(3f);
        criterion.setUpperLimit(1f);
        assertFalse(criterion.isInsideBoundaryType());
        assertEquals(GenomicCriteriaMatchTypeEnum.MATCH_POSITIVE_OR_NEGATIVE,
                handler.getSegmentValueMatchCriterionType(4f));
    }
}
