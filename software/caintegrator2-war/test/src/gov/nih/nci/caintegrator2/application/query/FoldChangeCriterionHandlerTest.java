/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataServiceStub;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.data.CaIntegrator2DaoStub;
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.FoldChangeCriterion;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.RegulationTypeEnum;
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
import gov.nih.nci.caintegrator2.domain.genomic.SampleSet;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.domain.translational.StudySubjectAssignment;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;


public class FoldChangeCriterionHandlerTest {

    private CaIntegrator2DaoStub daoStub = new DaoStub();
    private ArrayDataServiceStub arrayDataServiceStub = new ArrayDataServiceStub();
    private Query query;
    private Study study;
    private Gene gene;
    GeneExpressionReporter reporter = new GeneExpressionReporter();
    
    @Before
    public void setUp() {
        Platform platform = new Platform();
        ReporterList reporterList = platform.addReporterList("reporterList", ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET);
        gene = new Gene();
        reporter.getGenes().add(gene);
        
        daoStub.clear();       
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
        sample.setSampleAcquisition(acquisition);
        sample.getArrayDataCollection().add(arrayData);
        acquisition.setSample(sample);
        assignment.getSampleAcquisitionCollection().add(acquisition);
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        studyConfiguration.getGenomicDataSources().add(new GenomicDataSourceConfiguration());
        study.setStudyConfiguration(studyConfiguration);
    }

    @Test
    public void testGetMatches() throws InvalidCriterionException {        
        FoldChangeCriterion criterion = new FoldChangeCriterion();
        criterion.setRegulationType(RegulationTypeEnum.getByValue("Up"));
        criterion.setFoldsUp(1.0f);
        criterion.setGeneSymbol("Tester");
        criterion.setControlSampleSetName("controlSampleSet1");
        FoldChangeCriterionHandler handler = FoldChangeCriterionHandler.create(criterion);
        Set<ResultRow> rows = new HashSet<ResultRow>();
        try {
            rows = handler.getMatches(daoStub, arrayDataServiceStub, query, new HashSet<EntityTypeEnum>());
            fail();
        } catch(InvalidCriterionException e) { }
        SampleSet sampleSet1 = new SampleSet();
        sampleSet1.setName("controlSampleSet1");
        sampleSet1.getSamples().add(new Sample());
        study.getStudyConfiguration().getGenomicDataSources().get(0).getControlSampleSetCollection().add(sampleSet1);
        rows = handler.getMatches(daoStub, arrayDataServiceStub, query, new HashSet<EntityTypeEnum>());
        assertEquals(1, rows.size());
        criterion.setRegulationType(RegulationTypeEnum.DOWN);
        criterion.setFoldsDown(1.0f);
        rows = handler.getMatches(daoStub, arrayDataServiceStub, query, new HashSet<EntityTypeEnum>());
        assertEquals(0, rows.size());
        assertTrue(arrayDataServiceStub.getFoldChangeValuesCalled);
    }
    
    @Test
    public void testGetters() {
        FoldChangeCriterion criterion = new FoldChangeCriterion();
        criterion.setRegulationType(RegulationTypeEnum.UP);
        criterion.setFoldsUp(1.0f);
        FoldChangeCriterionHandler handler = FoldChangeCriterionHandler.create(criterion);
        assertTrue(handler.hasEntityCriterion());
        assertTrue(handler.hasReporterCriterion());
        assertTrue(handler.isEntityMatchHandler());
        assertTrue(handler.isReporterMatchHandler());
    }

    @Test
    public void testGetReporterMatches() {        
        FoldChangeCriterion criterion = new FoldChangeCriterion();
        criterion.setGeneSymbol("tester");
        FoldChangeCriterionHandler handler = FoldChangeCriterionHandler.create(criterion);
        assertEquals(1, handler.getReporterMatches(daoStub, study, ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET).size());
    }
    
    private class DaoStub extends CaIntegrator2DaoStub {

        @Override
        public Set<AbstractReporter> findReportersForGenes(Set<String> geneSymbols,
                ReporterTypeEnum reporterType, Study study) {
            Set<AbstractReporter> reporters = new HashSet<AbstractReporter>();
            reporters.add(reporter);
            return reporters;
        }
        
    }
 
}
