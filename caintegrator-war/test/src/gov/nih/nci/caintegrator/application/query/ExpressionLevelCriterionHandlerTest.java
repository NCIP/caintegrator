/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.application.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import gov.nih.nci.caintegrator.application.arraydata.DataRetrievalRequest;
import gov.nih.nci.caintegrator.application.query.ExpressionLevelCriterionHandler;
import gov.nih.nci.caintegrator.application.query.GenomicCriteriaMatchTypeEnum;
import gov.nih.nci.caintegrator.application.query.InvalidCriterionException;
import gov.nih.nci.caintegrator.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator.data.CaIntegrator2DaoStub;
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


public class ExpressionLevelCriterionHandlerTest extends AbstractMockitoTest {
    private CaIntegrator2DaoStub daoStub = new DaoStub();
    private Query query;
    private Study study;
    private Gene gene;
    GeneExpressionReporter reporter = new GeneExpressionReporter();

    @Before
    public void setUp() {
        Platform platform = daoStub.getPlatform("platformName");
        ReporterList reporterList = platform.addReporterList("reporterList", ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET);
        gene = new Gene();
        reporter.getGenes().add(gene);

        daoStub.clear();
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
    }

    @Test
    public void testGetMatches() throws InvalidCriterionException {
        ExpressionLevelCriterion criterion = new ExpressionLevelCriterion();
        criterion.setRangeType(RangeTypeEnum.GREATER_OR_EQUAL);
        criterion.setLowerLimit(1.0f);
        criterion.setGeneSymbol("Tester");
        ExpressionLevelCriterionHandler handler = ExpressionLevelCriterionHandler.create(criterion);
        Set<ResultRow> rows = new HashSet<ResultRow>();
        rows = handler.getMatches(daoStub, arrayDataService, query, new HashSet<EntityTypeEnum>());
        assertEquals(1, rows.size());
        criterion.setRangeType(RangeTypeEnum.LESS_OR_EQUAL);
        criterion.setUpperLimit(1.0f);
        rows = handler.getMatches(daoStub, arrayDataService, query, new HashSet<EntityTypeEnum>());
        assertEquals(0, rows.size());
        verify(arrayDataService, atLeastOnce()).getData(any(DataRetrievalRequest.class));
    }

    @Test
    public void testIsGenomicValueMatchCriterion() {
        ExpressionLevelCriterion criterion = new ExpressionLevelCriterion();
        criterion.setRangeType(RangeTypeEnum.INSIDE_RANGE);
        criterion.setLowerLimit(1.0f);
        criterion.setUpperLimit(2.0f);
        ExpressionLevelCriterionHandler handler = ExpressionLevelCriterionHandler.create(criterion);
        Set<Gene> genes = new HashSet<Gene>();
        Gene gene = new Gene();
        gene.setSymbol("Gene");
        genes.add(gene);
        assertEquals(GenomicCriteriaMatchTypeEnum.BETWEEN, handler.getGenomicValueMatchCriterionType(genes, 1.4f));

        criterion.setGeneSymbol("invalid");
        assertEquals(GenomicCriteriaMatchTypeEnum.NO_MATCH, handler.getGenomicValueMatchCriterionType(genes, 1.4f));
        criterion.setGeneSymbol("Gene");
        criterion.setRangeType(RangeTypeEnum.OUTSIDE_RANGE);
        assertEquals(GenomicCriteriaMatchTypeEnum.UNDER, handler.getGenomicValueMatchCriterionType(genes, .9f));
        assertEquals(GenomicCriteriaMatchTypeEnum.NO_MATCH, handler.getGenomicValueMatchCriterionType(genes, 1f));

        assertTrue(handler.hasReporterCriterion());
        assertTrue(handler.isReporterMatchHandler());
    }

    @Test
    public void testGetReporterMatches() {
        ExpressionLevelCriterion criterion = new ExpressionLevelCriterion();
        criterion.setGeneSymbol("tester");
        ExpressionLevelCriterionHandler handler = ExpressionLevelCriterionHandler.create(criterion);
        assertEquals(1, handler.getReporterMatches(daoStub, study, ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET, null).size());

        criterion.setGeneSymbol("");
        assertEquals(1, handler.getReporterMatches(daoStub, study, ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET, null).size());

    }

    private class DaoStub extends CaIntegrator2DaoStub {

        @Override
        public Set<AbstractReporter> findReportersForGenes(Set<String> geneSymbols,
                ReporterTypeEnum reporterType, Study study, Platform platform) {
            Set<AbstractReporter> reporters = new HashSet<AbstractReporter>();
            reporters.add(reporter);
            return reporters;
        }

    }

}
