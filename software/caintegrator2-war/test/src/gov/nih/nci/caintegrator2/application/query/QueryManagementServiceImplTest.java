/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataServiceStub;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.data.CaIntegrator2DaoStub;
import gov.nih.nci.caintegrator2.domain.application.AbstractCriterion;
import gov.nih.nci.caintegrator2.domain.application.BooleanOperatorEnum;
import gov.nih.nci.caintegrator2.domain.application.CompoundCriterion;
import gov.nih.nci.caintegrator2.domain.application.FoldChangeCriterion;
import gov.nih.nci.caintegrator2.domain.application.GeneNameCriterion;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataQueryResult;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataResultColumn;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.QueryResult;
import gov.nih.nci.caintegrator2.domain.application.RegulationTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.ResultColumn;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator2.domain.genomic.Array;
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
import gov.nih.nci.caintegrator2.external.ncia.NCIABasket;
import gov.nih.nci.caintegrator2.external.ncia.NCIADicomJob;
import gov.nih.nci.caintegrator2.external.ncia.NCIAImageAggregationTypeEnum;
import gov.nih.nci.caintegrator2.web.action.query.DisplayableQueryResultTest;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for QueryManagementServiceImpl
 */
public class QueryManagementServiceImplTest {
 

    private QueryManagementServiceImpl queryManagementService;
    private CaIntegrator2DaoStub dao;
    private Query query;
    private GeneExpressionReporter reporter;
    
    @Before
    public void setup() {
        dao = new CaIntegrator2DaoStub();
        ResultHandler resultHandler = new ResultHandlerImpl();
        dao.clear();
        ArrayDataServiceStub arrayDataService = new ArrayDataServiceStub();
        queryManagementService = new QueryManagementServiceImpl();
        queryManagementService.setDao(dao);
        queryManagementService.setArrayDataService(arrayDataService);
        queryManagementService.setResultHandler(resultHandler);
        query = new Query();
        query.setCompoundCriterion(new CompoundCriterion());
        query.getCompoundCriterion().setCriterionCollection(new HashSet<AbstractCriterion>());
        query.setColumnCollection(new HashSet<ResultColumn>());
        query.setSubscription(new StudySubscription());
        Study study = new Study();
        query.getSubscription().setStudy(study);
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        study.setStudyConfiguration(studyConfiguration);
        studyConfiguration.getGenomicDataSources().add(new GenomicDataSourceConfiguration());
    }

    
    @Test
    public void testExecute() throws InvalidCriterionException {
        QueryResult queryResult = queryManagementService.execute(query);
        assertNotNull(queryResult.getRowCollection());
    }
    
    @Test
    @SuppressWarnings("PMD")
    public void testExecuteGenomicDataQuery() throws InvalidCriterionException {
        GenomicDataTestDaoStub daoStub = new GenomicDataTestDaoStub();
        queryManagementService.setDao(daoStub);
        Study study = query.getSubscription().getStudy();
        StudySubjectAssignment assignment = new StudySubjectAssignment();
        SampleAcquisition acquisition = new SampleAcquisition();
        Sample sample = new Sample();
        sample.setSampleAcquisition(acquisition);
        Array array = new Array();
        ArrayData arrayData = new ArrayData();
        arrayData.setStudy(study);
        arrayData.setArray(array);
        array.getArrayDataCollection().add(arrayData);
        arrayData.setSample(sample);
        sample.getArrayDataCollection().add(arrayData);
        sample.getArrayCollection().add(array);
        ArrayData arrayData2 = new ArrayData();
        arrayData2.setStudy(study);
        arrayData2.setSample(sample);
        array.getArrayDataCollection().add(arrayData2);
        sample.getArrayDataCollection().add(arrayData2);        
        array.getSampleCollection().add(sample);
        acquisition.setSample(sample);
        assignment.getSampleAcquisitionCollection().add(acquisition);
        study.getAssignmentCollection().add(assignment);
        GeneNameCriterion geneNameCriterion = new GeneNameCriterion();
        Gene gene = new Gene();
        gene.setSymbol("GENE");
        reporter = new GeneExpressionReporter();
        Platform platform = new Platform();
        ReporterList reporterList = platform.addReporterList("reporterList", ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET);
        reporter.setReporterList(reporterList);
        reporter.getGenes().add(gene);
        geneNameCriterion.setGeneSymbol("GENE");
        query.getCompoundCriterion().getCriterionCollection().add(geneNameCriterion);
        reporterList.getArrayDatas().add(arrayData);
        reporterList.getArrayDatas().add(arrayData2);
        reporterList.getReporters().add(reporter);
        reporter.setReporterList(reporterList);
        arrayData.getReporterLists().add(reporterList);
        reporterList.getArrayDatas().add(arrayData);
        ReporterList reporterList2 = platform.addReporterList("reporterList2", ReporterTypeEnum.GENE_EXPRESSION_GENE);
        arrayData2.getReporterLists().add(reporterList2);
        reporterList2.getArrayDatas().add(arrayData2);
        query.setReporterType(ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET);
        GenomicDataQueryResult result = queryManagementService.executeGenomicDataQuery(query);
        assertEquals(1, result.getRowCollection().size());
        assertEquals(1, result.getColumnCollection().size());
        assertEquals(1, result.getRowCollection().iterator().next().getValues().size());
        GenomicDataResultColumn column = result.getColumnCollection().iterator().next();
        assertNotNull(column.getSampleAcquisition());
        assertNotNull(column.getSampleAcquisition().getSample());
        FoldChangeCriterion foldChangeCriterion = new FoldChangeCriterion();
        foldChangeCriterion.setFoldsUp(1.0f);
        foldChangeCriterion.setGeneSymbol("GENE");
        foldChangeCriterion.setControlSampleSetName("controlSampleSet1");
        foldChangeCriterion.setRegulationType(RegulationTypeEnum.UP);
        query.getCompoundCriterion().getCriterionCollection().clear();
        query.getCompoundCriterion().getCriterionCollection().add(foldChangeCriterion);
        query.getCompoundCriterion().setBooleanOperator(BooleanOperatorEnum.AND);
        try {
            result = queryManagementService.executeGenomicDataQuery(query);
            fail("Should have caught invalid criterion exception because no control samples in study");
        } catch (InvalidCriterionException e) {
        }
        SampleSet sampleSet1 = new SampleSet();
        sampleSet1.setName("controlSampleSet1");
        sampleSet1.getSamples().add(new Sample());
        study.getStudyConfiguration().getGenomicDataSources().get(0).getControlSampleSetCollection().add(sampleSet1);
        result = queryManagementService.executeGenomicDataQuery(query);
        assertEquals(1, result.getRowCollection().size());
        foldChangeCriterion.setFoldsDown(1.0f);
        foldChangeCriterion.setRegulationType(RegulationTypeEnum.DOWN);
        result = queryManagementService.executeGenomicDataQuery(query);
        assertEquals(0, result.getRowCollection().size());
        foldChangeCriterion.setRegulationType(RegulationTypeEnum.UP_OR_DOWN);
        result = queryManagementService.executeGenomicDataQuery(query);
        assertEquals(1, result.getRowCollection().size());
        foldChangeCriterion.setRegulationType(RegulationTypeEnum.UNCHANGED);
        result = queryManagementService.executeGenomicDataQuery(query);
        assertEquals(0, result.getRowCollection().size());
    }

    
    @Test
    public void testSave() {
       queryManagementService.save(query);
       assertTrue(dao.saveCalled);
       query.setId(Long.valueOf(1));
       queryManagementService.save(query);
       assertTrue(dao.mergeCalled);
    }
    
    @Test
    public void testDelete() {
       queryManagementService.delete(query);
       assertTrue(dao.deleteCalled);
    }
    
    @Test
    public void testCreateDicomJob() {
        queryManagementService.setDao(new ImageStudyTestDaoStub());
        NCIADicomJob dicomJob = queryManagementService.createDicomJob(DisplayableQueryResultTest.
                                                                      getImagingSeriesResult().
                                                                      getRows());
        assertEquals(NCIAImageAggregationTypeEnum.IMAGESERIES, dicomJob.getImageAggregationType());
        assertTrue(!dicomJob.getImageSeriesIDs().isEmpty());
        assertTrue(dicomJob.getImageStudyIDs().isEmpty());
        
        dicomJob = queryManagementService.createDicomJob(DisplayableQueryResultTest.
                                                        getImageStudyResult().
                                                        getRows());
        assertEquals(NCIAImageAggregationTypeEnum.IMAGESTUDY, dicomJob.getImageAggregationType());
        assertTrue(dicomJob.getImageSeriesIDs().isEmpty());
        assertTrue(!dicomJob.getImageStudyIDs().isEmpty());
    }
    
    @Test
    public void testCreateNciaBasket() {
        queryManagementService.setDao(new ImageStudyTestDaoStub());
        NCIABasket nciaBasket = queryManagementService.createNciaBasket(DisplayableQueryResultTest.
                                                                      getImagingSeriesResult().
                                                                      getRows());
        assertEquals(NCIAImageAggregationTypeEnum.IMAGESERIES, nciaBasket.getImageAggregationType());
        assertTrue(!nciaBasket.getImageSeriesIDs().isEmpty());
        assertTrue(nciaBasket.getImageStudyIDs().isEmpty());
        
        nciaBasket = queryManagementService.createNciaBasket(DisplayableQueryResultTest.
                                                        getImageStudyResult().
                                                        getRows());
        assertEquals(NCIAImageAggregationTypeEnum.IMAGESTUDY, nciaBasket.getImageAggregationType());
        assertTrue(nciaBasket.getImageSeriesIDs().isEmpty());
        assertTrue(!nciaBasket.getImageStudyIDs().isEmpty());
    }


    private class GenomicDataTestDaoStub extends CaIntegrator2DaoStub  {

        @Override
        public Set<AbstractReporter> findReportersForGenes(Set<String> geneSymbols,
                ReporterTypeEnum reporterType, Study study) {
            Set<AbstractReporter> reporters = new HashSet<AbstractReporter>();
            reporters.add(reporter );
            return reporters;
        }
    }
    
    @SuppressWarnings("unchecked")
    private static class ImageStudyTestDaoStub extends CaIntegrator2DaoStub {
        public <T> T get(Long id, Class<T> objectClass) {
            return (T) DisplayableQueryResultTest.retrieveStudySubjectAssignment(id);
        }
        
    }

}
