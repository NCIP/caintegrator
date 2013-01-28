/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.application.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator.application.query.GeneNameCriterionHandler;
import gov.nih.nci.caintegrator.data.CaIntegrator2DaoStub;
import gov.nih.nci.caintegrator.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator.domain.application.GeneNameCriterion;
import gov.nih.nci.caintegrator.domain.application.Query;
import gov.nih.nci.caintegrator.domain.application.ResultRow;
import gov.nih.nci.caintegrator.domain.application.StudySubscription;
import gov.nih.nci.caintegrator.domain.genomic.AbstractReporter;
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

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class GeneNameCriterionHandlerTest {

    private static final String GENE_NAME = "EGFR";
    private static final Long ASSIGNMENT_ID = Long.valueOf(1);
    private CaIntegrator2DaoStub daoStub = new DaoStub();
    private Query query;
    private Study study;
    private Gene gene;
    GeneExpressionReporter reporter = new GeneExpressionReporter();

    @Before
    public void setUp() {
        daoStub.clear();
        Platform platform = new Platform();
        ReporterList reporterList = platform.addReporterList("reporterList", ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET);
        gene = new Gene();
        gene.setSymbol(GENE_NAME);
        reporter.getGenes().add(gene);
        reporter.setReporterList(reporterList);

        study = new Study();
        query = new Query();
        StudySubscription subscription = new StudySubscription();
        subscription.setStudy(study);
        query.setSubscription(subscription);
        StudySubjectAssignment assignment = new StudySubjectAssignment();
        study.getAssignmentCollection().add(assignment);
        SampleAcquisition acquisition = new SampleAcquisition();
        Sample sample = new Sample();
        ArrayData arrayData = new ArrayData();
        arrayData.setStudy(study);
        arrayData.getReporterLists().add(reporterList);
        reporterList.getArrayDatas().add(arrayData);
        arrayData.setSample(sample);
        ArrayData arrayData2 = new ArrayData();
        arrayData2.setStudy(study);
        arrayData2.setSample(new Sample());
        reporterList.getArrayDatas().add(arrayData);
        reporterList.getArrayDatas().add(arrayData2);
        sample.getSampleAcquisitions().add(acquisition);
        sample.getArrayDataCollection().add(arrayData);
        acquisition.setSample(sample);
        assignment.getSampleAcquisitionCollection().add(acquisition);
        assignment.setId(ASSIGNMENT_ID);
        acquisition.setAssignment(assignment);
    }

    @Test
    public void testGetMatches() {
        GeneNameCriterion criterion = new GeneNameCriterion();
        criterion.setGeneSymbol(GENE_NAME);
        GeneNameCriterionHandler handler = GeneNameCriterionHandler.create(criterion);
        Set<ResultRow> rows = handler.getMatches(daoStub, null, query, new HashSet<EntityTypeEnum>());
        ResultRow row = rows.iterator().next();
        assertEquals(ASSIGNMENT_ID, row.getSubjectAssignment().getId());
        assertNull(row.getSampleAcquisition());
    }

    @Test
    public void testGetReporterMatches() {
        GeneNameCriterion criterion = new GeneNameCriterion();
        criterion.setGeneSymbol(GENE_NAME);
        GeneNameCriterionHandler handler = GeneNameCriterionHandler.create(criterion);
        Set<AbstractReporter> reporters =
                handler.getReporterMatches(daoStub, null, ReporterTypeEnum.GENE_EXPRESSION_GENE, null);
        GeneExpressionReporter reporter = (GeneExpressionReporter) reporters.iterator().next();
        assertEquals(1, reporter.getGenes().size());
        Gene gene = reporter.getGenes().iterator().next();
        assertEquals(GENE_NAME, gene.getSymbol());
        assertTrue(daoStub.findGeneExpressionReportersCalled);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testGetReporterMatchesNoReporterType() {
        GeneNameCriterionHandler handler = GeneNameCriterionHandler.create(new GeneNameCriterion());
        handler.getReporterMatches(null, null, null, null);
    }

    @Test
    public void testIsReporterMatchHandler() {
        assertTrue(GeneNameCriterionHandler.create(null).isReporterMatchHandler());
    }

    private class DaoStub extends CaIntegrator2DaoStub {

        @Override
        public Set<AbstractReporter> findReportersForGenes(Set<String> geneSymbols,
                ReporterTypeEnum reporterType, Study study, Platform platform) {
            Set<AbstractReporter> reporters = new HashSet<AbstractReporter>();
            reporters.add(reporter);
            findGeneExpressionReportersCalled = true;
            return reporters;
        }

    }

}
