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
import static org.mockito.Matchers.anySetOf;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import gov.nih.nci.caintegrator.application.arraydata.DataRetrievalRequest;
import gov.nih.nci.caintegrator.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator.domain.application.ExpressionLevelCriterion;
import gov.nih.nci.caintegrator.domain.application.Query;
import gov.nih.nci.caintegrator.domain.application.RangeTypeEnum;
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
import gov.nih.nci.caintegrator.domain.translational.Study;
import gov.nih.nci.caintegrator.domain.translational.StudySubjectAssignment;
import gov.nih.nci.caintegrator.mockito.AbstractMockitoTest;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;


/**
 * Tests for the expression level criterion handler.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
public class ExpressionLevelCriterionHandlerTest extends AbstractMockitoTest {
    private Query query;
    private Study study;

    /**
     * Set up unit tests.
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
        reporterList.addReporter(new GeneExpressionReporter());
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
     * Tests expression level criterion results retrieval for >=.
     *
     * @throws InvalidCriterionException on invalid criterion error
     */
    @Test
    public void greaterThanOrEqualMatches() throws InvalidCriterionException {
        ExpressionLevelCriterion criterion = new ExpressionLevelCriterion();
        criterion.setRangeType(RangeTypeEnum.GREATER_OR_EQUAL);
        criterion.setLowerLimit(1.0f);
        criterion.setGeneSymbol("Tester");

        ExpressionLevelCriterionHandler handler = ExpressionLevelCriterionHandler.create(criterion);

        Set<ResultRow> results = handler.getMatches(dao, arrayDataService, query, new HashSet<EntityTypeEnum>());
        verify(arrayDataService, atLeastOnce()).getData(any(DataRetrievalRequest.class));
        assertEquals(1, results.size());
    }

    /**
     * Tests expression level criterion results retrieval for <=.
     *
     * @throws InvalidCriterionException on invalid criterion error
     */
    @Test
    public void lessThanOrEqualMatches() throws InvalidCriterionException {
        ExpressionLevelCriterion criterion = new ExpressionLevelCriterion();
        criterion.setRangeType(RangeTypeEnum.LESS_OR_EQUAL);
        criterion.setUpperLimit(1.0f);
        criterion.setGeneSymbol("Tester");

        ExpressionLevelCriterionHandler handler = ExpressionLevelCriterionHandler.create(criterion);

        Set<ResultRow> results = handler.getMatches(dao, arrayDataService, query, new HashSet<EntityTypeEnum>());
        verify(arrayDataService, atLeastOnce()).getData(any(DataRetrievalRequest.class));
        assertEquals(0, results.size());
    }

    /**
     * Tests determination of genomic criteria match types.
     */
    @Test
    public void genomicCritrionTypes() {
        ExpressionLevelCriterion criterion = new ExpressionLevelCriterion();
        criterion.setRangeType(RangeTypeEnum.INSIDE_RANGE);
        criterion.setLowerLimit(1.0f);
        criterion.setUpperLimit(3.0f);

        Set<Gene> genes = new HashSet<Gene>();
        Gene gene = new Gene();
        gene.setSymbol("Gene");
        genes.add(gene);

        ExpressionLevelCriterionHandler handler = ExpressionLevelCriterionHandler.create(criterion);

        assertEquals(GenomicCriteriaMatchTypeEnum.BETWEEN, handler.getGenomicValueMatchCriterionType(genes, 2f));

        criterion.setGeneSymbol("invalid");
        assertEquals(GenomicCriteriaMatchTypeEnum.NO_MATCH, handler.getGenomicValueMatchCriterionType(genes, 2f));
        criterion.setGeneSymbol("Gene");
        criterion.setRangeType(RangeTypeEnum.OUTSIDE_RANGE);
        assertEquals(GenomicCriteriaMatchTypeEnum.UNDER, handler.getGenomicValueMatchCriterionType(genes, 0f));
        assertEquals(GenomicCriteriaMatchTypeEnum.NO_MATCH, handler.getGenomicValueMatchCriterionType(genes, 1f));

        assertTrue(handler.hasReporterCriterion());
        assertTrue(handler.isReporterMatchHandler());
    }

    /**
     * Tests reporter match retrieval with a specified gene.
     */
    @Test
    public void reporterMatchesWithGeneSymbol() {
        ExpressionLevelCriterion criterion = new ExpressionLevelCriterion();
        criterion.setGeneSymbol("tester");
        ExpressionLevelCriterionHandler handler = ExpressionLevelCriterionHandler.create(criterion);

        Set<AbstractReporter> results =
                handler.getReporterMatches(dao, study, ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET, null);
        assertEquals(1, results.size());
    }

    /**
     * Tests reporter match retrieval without a specified gene.
     */
    @Test
    public void reporterMatchesWithoutGeneSymbol() {
        ExpressionLevelCriterion criterion = new ExpressionLevelCriterion();
        ExpressionLevelCriterionHandler handler = ExpressionLevelCriterionHandler.create(criterion);

        Set<AbstractReporter> results =
                handler.getReporterMatches(dao, study, ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET, null);
        assertEquals(1, results.size());
    }
}
