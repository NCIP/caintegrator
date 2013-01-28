/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.application.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import gov.nih.nci.caintegrator.application.arraydata.ArrayDataValues;
import gov.nih.nci.caintegrator.application.arraydata.DataRetrievalRequest;
import gov.nih.nci.caintegrator.application.arraydata.PlatformChannelTypeEnum;
import gov.nih.nci.caintegrator.application.query.FoldChangeCriterionHandler;
import gov.nih.nci.caintegrator.application.query.InvalidCriterionException;
import gov.nih.nci.caintegrator.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator.data.CaIntegrator2DaoStub;
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


public class FoldChangeCriterionHandlerTest extends AbstractMockitoTest {
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
        FoldChangeCriterion criterion = new FoldChangeCriterion();
        criterion.setRegulationType(RegulationTypeEnum.getByValue("Up"));
        criterion.setFoldsUp(1.0f);
        criterion.setGeneSymbol("Tester");
        criterion.setControlSampleSetName("controlSampleSet1");
        FoldChangeCriterionHandler handler = FoldChangeCriterionHandler.create(criterion);
        Set<ResultRow> rows = new HashSet<ResultRow>();
        try {
            rows = handler.getMatches(daoStub, arrayDataService, query, new HashSet<EntityTypeEnum>());
            fail();
        } catch(InvalidCriterionException e) { }
        SampleSet sampleSet1 = new SampleSet();
        sampleSet1.setName("controlSampleSet1");
        Sample sample = new Sample();
        sampleSet1.getSamples().add(sample);
        ArrayData arrayData = new ArrayData();
        sample.getArrayDataCollection().add(arrayData);
        ReporterList reporterList = new ReporterList("reporterList", ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET);
        arrayData.getReporterLists().add(reporterList);
        Array array = new Array();
        array.setPlatform(query.getGeneExpressionPlatform());
        arrayData.setArray(array);
        study.getStudyConfiguration().getGenomicDataSources().get(0).getControlSampleSetCollection().add(sampleSet1);
        rows = handler.getMatches(daoStub, arrayDataService, query, new HashSet<EntityTypeEnum>());
        assertEquals(1, rows.size());
        criterion.setRegulationType(RegulationTypeEnum.DOWN);
        criterion.setFoldsDown(1.0f);
        rows = handler.getMatches(daoStub, arrayDataService, query, new HashSet<EntityTypeEnum>());
        assertEquals(0, rows.size());
        verify(arrayDataService, atLeastOnce()).getFoldChangeValues(any(DataRetrievalRequest.class), anyListOf(ArrayDataValues.class),
                any(PlatformChannelTypeEnum.class));
    }

    @Test
    public void testGetters() {
        FoldChangeCriterion criterion = new FoldChangeCriterion();
        criterion.setRegulationType(RegulationTypeEnum.UP);
        criterion.setFoldsUp(1.0f);
        FoldChangeCriterionHandler handler = FoldChangeCriterionHandler.create(criterion);
        assertTrue(handler.hasReporterCriterion());
        assertTrue(handler.isReporterMatchHandler());
    }

    @Test
    public void testGetReporterMatches() {
        FoldChangeCriterion criterion = new FoldChangeCriterion();
        criterion.setGeneSymbol("tester");
        FoldChangeCriterionHandler handler = FoldChangeCriterionHandler.create(criterion);
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
