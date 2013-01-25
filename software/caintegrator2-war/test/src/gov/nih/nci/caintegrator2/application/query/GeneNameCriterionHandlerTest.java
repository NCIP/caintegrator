/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.data.CaIntegrator2DaoStub;
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.GeneNameCriterion;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.ResultRow;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator2.domain.genomic.Gene;
import gov.nih.nci.caintegrator2.domain.genomic.GeneExpressionReporter;
import gov.nih.nci.caintegrator2.domain.genomic.Platform;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.domain.genomic.Sample;
import gov.nih.nci.caintegrator2.domain.genomic.SampleAcquisition;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.domain.translational.StudySubjectAssignment;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class GeneNameCriterionHandlerTest {
    
    private static final String GENE_NAME = "egfr";
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
        sample.setSampleAcquisition(acquisition);
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
                handler.getReporterMatches(daoStub, null, ReporterTypeEnum.GENE_EXPRESSION_GENE);
        GeneExpressionReporter reporter = (GeneExpressionReporter) reporters.iterator().next();
        assertEquals(1, reporter.getGenes().size());
        Gene gene = reporter.getGenes().iterator().next();
        assertEquals(GENE_NAME, gene.getSymbol());
        assertTrue(daoStub.findGeneExpressionReportersCalled);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testGetReporterMatchesNoReporterType() {
        GeneNameCriterionHandler handler = GeneNameCriterionHandler.create(new GeneNameCriterion());
        handler.getReporterMatches(null, null, null);
    }

    @Test
    public void testIsReporterMatchHandler() {
        assertTrue(GeneNameCriterionHandler.create(null).isReporterMatchHandler());
    }

    @Test
    public void testIsEntityMatchHandler() {
        assertTrue(GeneNameCriterionHandler.create(null).isEntityMatchHandler());
    }

    @Test
    public void testHasEntityCriterion() {
        assertTrue(GeneNameCriterionHandler.create(null).hasEntityCriterion());
    }
    
    private class DaoStub extends CaIntegrator2DaoStub {

        @Override
        public Set<AbstractReporter> findReportersForGenes(Set<String> geneSymbols,
                ReporterTypeEnum reporterType, Study study) {
            Set<AbstractReporter> reporters = new HashSet<AbstractReporter>();
            reporters.add(reporter);
            findGeneExpressionReportersCalled = true;
            return reporters;
        }
        
    }

}
