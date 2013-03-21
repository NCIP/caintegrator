/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anySetOf;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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
import gov.nih.nci.caintegrator.mockito.AbstractMockitoTest;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;

/**
 * Gene name criterion handler tests.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
public class GeneNameCriterionHandlerTest extends AbstractMockitoTest {

    private static final String GENE_NAME = "EGFR";
    private static final Long ASSIGNMENT_ID = 1L;
    private Query query;

    /**
     * Unit test setup.
     */
    @Before
    public void setUp() {
        Platform platform = new Platform();
        ReporterList reporterList =
                platform.addReporterList("reporterList", ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET);
        Gene gene = new Gene();
        gene.setSymbol(GENE_NAME);

        GeneExpressionReporter reporter = new GeneExpressionReporter();
        reporter.getGenes().add(gene);
        reporter.setReporterList(reporterList);

        Study study = new Study();
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

        when(dao.findReportersForGenes(anySetOf(String.class), any(ReporterTypeEnum.class), any(Study.class),
                any(Platform.class))).thenReturn(Sets.<AbstractReporter>newHashSet(reporter));
    }

    /**
     * Tests gene name results retrieval.
     */
    @Test
    public void geneNameMatches() {
        GeneNameCriterion criterion = new GeneNameCriterion();
        criterion.setGeneSymbol(GENE_NAME);
        GeneNameCriterionHandler handler = GeneNameCriterionHandler.create(criterion);

        Set<ResultRow> results = handler.getMatches(dao, null, query, new HashSet<EntityTypeEnum>());
        assertEquals(1, results.size());

        ResultRow row = results.iterator().next();
        assertEquals(ASSIGNMENT_ID, row.getSubjectAssignment().getId());
        assertNull(row.getSampleAcquisition());
    }

    /**
     * Report matches by gene name retrieval.
     */
    @Test
    public void testGetReporterMatches() {
        GeneNameCriterion criterion = new GeneNameCriterion();
        criterion.setGeneSymbol(GENE_NAME);
        GeneNameCriterionHandler handler = GeneNameCriterionHandler.create(criterion);
        Set<AbstractReporter> results =
                handler.getReporterMatches(dao, null, ReporterTypeEnum.GENE_EXPRESSION_GENE, null);
        assertEquals(1, results.size());

        GeneExpressionReporter reporter = (GeneExpressionReporter) results.iterator().next();
        assertEquals(1, reporter.getGenes().size());

        Gene gene = reporter.getGenes().iterator().next();
        assertEquals(GENE_NAME, gene.getSymbol());
        verify(dao, atLeastOnce()).findReportersForGenes(anySetOf(String.class), any(ReporterTypeEnum.class),
                any(Study.class), any(Platform.class));
    }

    /**
     * Ensures that attempting to retrieve matches without specifying a report results in an error.
     */
    @Test(expected = IllegalArgumentException.class)
    public void getReporterMatchesNoReporterType() {
        GeneNameCriterionHandler handler = GeneNameCriterionHandler.create(new GeneNameCriterion());
        handler.getReporterMatches(null, null, null, null);
    }
}
