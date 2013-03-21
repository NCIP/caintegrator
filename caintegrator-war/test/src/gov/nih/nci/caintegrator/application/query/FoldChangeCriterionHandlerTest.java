/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anySetOf;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import gov.nih.nci.caintegrator.application.arraydata.ArrayDataValues;
import gov.nih.nci.caintegrator.application.arraydata.DataRetrievalRequest;
import gov.nih.nci.caintegrator.application.arraydata.PlatformChannelTypeEnum;
import gov.nih.nci.caintegrator.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator.domain.application.FoldChangeCriterion;
import gov.nih.nci.caintegrator.domain.application.Query;
import gov.nih.nci.caintegrator.domain.application.RegulationTypeEnum;
import gov.nih.nci.caintegrator.domain.application.ResultRow;
import gov.nih.nci.caintegrator.domain.application.StudySubscription;
import gov.nih.nci.caintegrator.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator.domain.genomic.Array;
import gov.nih.nci.caintegrator.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator.domain.genomic.Gene;
import gov.nih.nci.caintegrator.domain.genomic.GeneExpressionReporter;
import gov.nih.nci.caintegrator.domain.genomic.Platform;
import gov.nih.nci.caintegrator.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator.domain.genomic.Sample;
import gov.nih.nci.caintegrator.domain.genomic.SampleAcquisition;
import gov.nih.nci.caintegrator.domain.genomic.SampleSet;
import gov.nih.nci.caintegrator.domain.translational.Study;
import gov.nih.nci.caintegrator.domain.translational.StudySubjectAssignment;
import gov.nih.nci.caintegrator.mockito.AbstractMockitoTest;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;

/**
 * Tests for the fold change criterion handler.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
public class FoldChangeCriterionHandlerTest extends AbstractMockitoTest {
    private static final String SAMPLE_SET_NAME = "controlSampleSet";
    private Query query;
    private Study study;

    /**
     * Unit test setup.
     */
    @Before
    public void setUp() {
        Platform platform = dao.getPlatform("platformName");
        ReporterList reporterList =
                platform.addReporterList("reporterList", ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET);

        GeneExpressionReporter reporter = new GeneExpressionReporter();
        reporter.getGenes().add(new Gene());

        study = new Study();
        query = new Query();
        query.setGeneExpressionPlatform(platform);
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
        acquisition.setSample(sample);
        assignment.getSampleAcquisitionCollection().add(acquisition);
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        studyConfiguration.getGenomicDataSources().add(new GenomicDataSourceConfiguration());
        study.setStudyConfiguration(studyConfiguration);

        when(dao.findReportersForGenes(anySetOf(String.class), any(ReporterTypeEnum.class), any(Study.class),
                any(Platform.class))).thenReturn(Sets.<AbstractReporter>newHashSet(reporter));
    }

    /**
     * Ensures that attempting to search by sample set with no existing sample set results in an error.
     *
     * @throws InvalidCriterionException on expected invalid criterion error
     */
    @Test(expected = InvalidCriterionException.class)
    public void getMatchesNoSampleSet()  throws InvalidCriterionException {
        FoldChangeCriterion criterion = new FoldChangeCriterion();
        criterion.setRegulationType(RegulationTypeEnum.UP);
        criterion.setFoldsUp(1.0f);
        criterion.setGeneSymbol("Tester");
        criterion.setControlSampleSetName(SAMPLE_SET_NAME);

        FoldChangeCriterionHandler handler = FoldChangeCriterionHandler.create(criterion);
        handler.getMatches(dao, arrayDataService, query, new HashSet<EntityTypeEnum>());
    }

    /**
     * Tests retrieval of fold change results with type up.
     *
     * @throws InvalidCriterionException on unexpected
     */
    @Test
    public void getMatchesRegulationTypeUp() throws InvalidCriterionException {
        FoldChangeCriterion criterion = new FoldChangeCriterion();
        criterion.setRegulationType(RegulationTypeEnum.UP);
        criterion.setFoldsUp(1.0f);
        criterion.setGeneSymbol("Tester");
        criterion.setControlSampleSetName(SAMPLE_SET_NAME);
        FoldChangeCriterionHandler handler = FoldChangeCriterionHandler.create(criterion);

        SampleSet sampleSet = generateSampleSet(SAMPLE_SET_NAME);
        study.getStudyConfiguration().getGenomicDataSources().get(0).getControlSampleSetCollection().add(sampleSet);

        Set<ResultRow> results = handler.getMatches(dao, arrayDataService, query, new HashSet<EntityTypeEnum>());
        assertEquals(1, results.size());
        verify(arrayDataService, atLeastOnce()).getFoldChangeValues(any(DataRetrievalRequest.class),
                anyListOf(ArrayDataValues.class), any(PlatformChannelTypeEnum.class));
    }

    /**
     * Tests retrieval of fold change results with type down.
     *
     * @throws InvalidCriterionException on unexpected
     */
    @Test
    public void getMatchesRegulationTypeDown() throws InvalidCriterionException {
        FoldChangeCriterion criterion = new FoldChangeCriterion();
        criterion.setRegulationType(RegulationTypeEnum.DOWN);
        criterion.setFoldsDown(1.0f);
        criterion.setGeneSymbol("Tester");
        criterion.setControlSampleSetName(SAMPLE_SET_NAME);
        FoldChangeCriterionHandler handler = FoldChangeCriterionHandler.create(criterion);

        SampleSet sampleSet = generateSampleSet(SAMPLE_SET_NAME);
        study.getStudyConfiguration().getGenomicDataSources().get(0).getControlSampleSetCollection().add(sampleSet);

        Set<ResultRow> results = handler.getMatches(dao, arrayDataService, query, new HashSet<EntityTypeEnum>());
        assertTrue(results.isEmpty());
        verify(arrayDataService, atLeastOnce()).getFoldChangeValues(any(DataRetrievalRequest.class),
                anyListOf(ArrayDataValues.class), any(PlatformChannelTypeEnum.class));
    }

    /**
     * Test retrieval of reporter matches.
     */
    @Test
    public void getReporterMatches() {
        FoldChangeCriterion criterion = new FoldChangeCriterion();
        criterion.setGeneSymbol("tester");
        FoldChangeCriterionHandler handler = FoldChangeCriterionHandler.create(criterion);
        Set<AbstractReporter> results =
                handler.getReporterMatches(dao, study, ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET, null);
        assertEquals(1, results.size());
    }

    private SampleSet generateSampleSet(String sampleSetName) {
        SampleSet sampleSet = new SampleSet();
        sampleSet.setName(sampleSetName);
        Sample sample = new Sample();
        sampleSet.getSamples().add(sample);
        ArrayData arrayData = new ArrayData();
        sample.getArrayDataCollection().add(arrayData);
        ReporterList reporterList = new ReporterList("reporterList", ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET);
        arrayData.getReporterLists().add(reporterList);
        Array array = new Array();
        array.setPlatform(query.getGeneExpressionPlatform());
        arrayData.setArray(array);
        return sampleSet;
    }
}
