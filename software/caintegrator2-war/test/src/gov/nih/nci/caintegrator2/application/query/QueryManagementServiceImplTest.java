/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataServiceStub;
import gov.nih.nci.caintegrator2.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.data.CaIntegrator2DaoStub;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.application.AbstractCriterion;
import gov.nih.nci.caintegrator2.domain.application.BooleanOperatorEnum;
import gov.nih.nci.caintegrator2.domain.application.CompoundCriterion;
import gov.nih.nci.caintegrator2.domain.application.CopyNumberAlterationCriterion;
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.FoldChangeCriterion;
import gov.nih.nci.caintegrator2.domain.application.GeneNameCriterion;
import gov.nih.nci.caintegrator2.domain.application.GenomicCriterionTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataQueryResult;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataResultColumn;
import gov.nih.nci.caintegrator2.domain.application.NumericComparisonCriterion;
import gov.nih.nci.caintegrator2.domain.application.NumericComparisonOperatorEnum;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.QueryResult;
import gov.nih.nci.caintegrator2.domain.application.RegulationTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.ResultColumn;
import gov.nih.nci.caintegrator2.domain.application.ResultTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.application.SubjectIdentifier;
import gov.nih.nci.caintegrator2.domain.application.SubjectList;
import gov.nih.nci.caintegrator2.domain.application.SubjectListCriterion;
import gov.nih.nci.caintegrator2.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator2.domain.genomic.Array;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator2.domain.genomic.ChromosomalLocation;
import gov.nih.nci.caintegrator2.domain.genomic.Gene;
import gov.nih.nci.caintegrator2.domain.genomic.GeneExpressionReporter;
import gov.nih.nci.caintegrator2.domain.genomic.Platform;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.domain.genomic.Sample;
import gov.nih.nci.caintegrator2.domain.genomic.SampleAcquisition;
import gov.nih.nci.caintegrator2.domain.genomic.SampleSet;
import gov.nih.nci.caintegrator2.domain.genomic.SegmentData;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.domain.translational.StudySubjectAssignment;
import gov.nih.nci.caintegrator2.external.ncia.NCIABasket;
import gov.nih.nci.caintegrator2.external.ncia.NCIADicomJob;
import gov.nih.nci.caintegrator2.external.ncia.NCIAImageAggregationTypeEnum;
import gov.nih.nci.caintegrator2.web.action.query.DisplayableQueryResultTest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
    private ArrayDataServiceStub arrayDataService;
    private SegmentData segmentData;
    private SegmentData segmentData2;
    
    @Before
    public void setup() {
        dao = new GenomicDataTestDaoStub();
        ResultHandler resultHandler = new ResultHandlerImpl();
        dao.clear();
        arrayDataService = new ArrayDataServiceStub();
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
        segmentData = new SegmentData();
        ChromosomalLocation location = new ChromosomalLocation();
        location.setStartPosition(1);
        location.setEndPosition(2);
        segmentData.setLocation(location);
        segmentData2 = new SegmentData();
        segmentData2.setLocation(location);
    }

    
    @Test
    public void testExecute() throws InvalidCriterionException {
        QueryResult queryResult = queryManagementService.execute(query);
        assertNotNull(queryResult.getRowCollection());
        assertFalse(query.isHasMaskedValues());
        NumericComparisonCriterion numericCrit = new NumericComparisonCriterion();
        numericCrit.setEntityType(EntityTypeEnum.SUBJECT);
        numericCrit.setNumericValue(12d);
        numericCrit.setNumericComparisonOperator(NumericComparisonOperatorEnum.LESSOREQUAL);
        
        AnnotationFieldDescriptor afd = new AnnotationFieldDescriptor();
        afd.setAnnotationEntityType(EntityTypeEnum.SUBJECT);
        afd.setMaxNumber(10d);
        afd.setNumericRange(4);
        afd.setDefinition(new AnnotationDefinition());
        numericCrit.setAnnotationFieldDescriptor(afd);
        ResultColumn column = new ResultColumn();
        column.setAnnotationFieldDescriptor(afd);
        query.getColumnCollection().add(column);
        query.getCompoundCriterion().getCriterionCollection().add(numericCrit);
        queryManagementService.execute(query);
        assertTrue(query.isHasMaskedValues());
        
        // Test SubjectList with all matches
        SubjectList sl1 = new SubjectList();
        SubjectIdentifier subjectIdentifier = new SubjectIdentifier();
        subjectIdentifier.setIdentifier("real");
        sl1.getSubjectIdentifiers().add(subjectIdentifier);
        
        StudySubjectAssignment subject1 = new StudySubjectAssignment();
        subject1.setIdentifier("real");
        query.getSubscription().getStudy().getAssignmentCollection().add(subject1);
        query.getCompoundCriterion().getCriterionCollection().clear();
        SubjectListCriterion subjectListCriterion = new SubjectListCriterion();
        subjectListCriterion.getSubjectListCollection().add(sl1);
        query.getCompoundCriterion().getCriterionCollection().add(subjectListCriterion);
        queryResult = queryManagementService.execute(query);
        assertTrue(queryResult.getQuery().getSubjectIdsNotFound().isEmpty());
        
        // Test 2 subject lists, one matches, and one doesn't.
        SubjectList sl2 = new SubjectList();
        SubjectIdentifier subjectIdentifier2 = new SubjectIdentifier();
        subjectIdentifier2.setIdentifier("fake");
        sl2.getSubjectIdentifiers().add(subjectIdentifier2);
        subjectListCriterion.getSubjectListCollection().add(sl2);
        queryResult = queryManagementService.execute(query);
        assertTrue(queryResult.getQuery().getSubjectIdsNotFound().contains("fake"));
        
        
        // Test 1 subject list, where neither matches.
        subjectListCriterion.getSubjectListCollection().clear();
        subjectListCriterion.getSubjectListCollection().add(sl2);
        try {
            queryResult = queryManagementService.execute(query);
            fail("Should catch invalid criterion exception because there are no valid subject IDs in query.");
        } catch (InvalidCriterionException e) {
            // noop - expect it to come here.
        }
        
    }
    
    @Test
    @SuppressWarnings("PMD")
    public void testExecuteGenomicDataQuery() throws InvalidCriterionException {
        Platform platform = dao.getPlatform("platformName");
        Study study = query.getSubscription().getStudy();
        StudySubjectAssignment assignment = new StudySubjectAssignment();
        SampleAcquisition acquisition = new SampleAcquisition();
        Sample sample = new Sample();
        sample.setSampleAcquisition(acquisition);
        Array array = new Array();
        array.setPlatform(platform);
        ArrayData arrayData = new ArrayData();
        arrayData.setStudy(study);
        arrayData.setArray(array);
        array.getArrayDataCollection().add(arrayData);
        arrayData.setSample(sample);
        sample.getArrayDataCollection().add(arrayData);
        segmentData.setSegmentValue(.1f);
        arrayData.getSegmentDatas().add(segmentData);
        segmentData.setArrayData(arrayData);
        sample.getArrayCollection().add(array);
        ArrayData arrayData2 = new ArrayData();
        arrayData2.setStudy(study);
        arrayData2.setSample(sample);
        arrayData2.setArray(array);
        segmentData2.setSegmentValue(.4f);
        arrayData2.getSegmentDatas().add(segmentData2);
        segmentData2.setArrayData(arrayData2);
        array.getArrayDataCollection().add(arrayData2);
        sample.getArrayDataCollection().add(arrayData2);        
        array.getSampleCollection().add(sample);
        acquisition.setSample(sample);
        assignment.getSampleAcquisitionCollection().add(acquisition);
        study.getAssignmentCollection().add(assignment);
        GeneNameCriterion geneNameCriterion = new GeneNameCriterion();
        geneNameCriterion.setGenomicCriterionType(GenomicCriterionTypeEnum.GENE_EXPRESSION);
        Gene gene = new Gene();
        gene.setSymbol("GENE");
        reporter = new GeneExpressionReporter();
        ReporterList reporterList = platform.addReporterList("reporterList", ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET);
        reporter.setReporterList(reporterList);
        reporter.getGenes().add(gene);
        geneNameCriterion.setGeneSymbol("GENE");
        query.getCompoundCriterion().getCriterionCollection().add(geneNameCriterion);
        reporterList.getArrayDatas().add(arrayData);
        reporterList.getArrayDatas().add(arrayData2);
        reporterList.getReporters().add(reporter);
        arrayData.getReporterLists().add(reporterList);
        reporterList.getArrayDatas().add(arrayData);
        ReporterList reporterList2 = platform.addReporterList("reporterList2", ReporterTypeEnum.GENE_EXPRESSION_GENE);
        arrayData2.getReporterLists().add(reporterList2);
        reporterList2.getArrayDatas().add(arrayData2);
        
        query.setReporterType(ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET);
        try {
            arrayDataService.numberPlatformsInStudy = 2;
            queryManagementService.executeGenomicDataQuery(query);
            fail("Should have caught invalid criterion exception because no platforms selected.");
        } catch (InvalidCriterionException e) {
        }
        arrayDataService.numberPlatformsInStudy = 1;
        geneNameCriterion.setPlatformName("platformName");
        query.setResultType(ResultTypeEnum.GENE_EXPRESSION);
        GenomicDataQueryResult result = queryManagementService.executeGenomicDataQuery(query);
        
        assertEquals(1, result.getFilteredRowCollection().size());
        assertEquals(1, result.getColumnCollection().size());
        assertEquals(1, result.getFilteredRowCollection().iterator().next().getValues().size());
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
        assertEquals(1, result.getFilteredRowCollection().size());
        foldChangeCriterion.setFoldsDown(1.0f);
        foldChangeCriterion.setRegulationType(RegulationTypeEnum.DOWN);
        result = queryManagementService.executeGenomicDataQuery(query);
        assertEquals(0, result.getFilteredRowCollection().size());
        foldChangeCriterion.setRegulationType(RegulationTypeEnum.UP_OR_DOWN);
        result = queryManagementService.executeGenomicDataQuery(query);
        assertEquals(1, result.getFilteredRowCollection().size());
        foldChangeCriterion.setRegulationType(RegulationTypeEnum.UNCHANGED);
        result = queryManagementService.executeGenomicDataQuery(query);
        assertEquals(0, result.getFilteredRowCollection().size());
        try {
            foldChangeCriterion.setGeneSymbol("EGFR");
            result = queryManagementService.executeGenomicDataQuery(query);
            fail("Should have caught invalid criterion exception because genes are not found.");
        } catch (InvalidCriterionException e) {
        }
        
        ReporterList reporterList3 = platform.addReporterList("reporterList3", ReporterTypeEnum.DNA_ANALYSIS_REPORTER);
        arrayData.getReporterLists().add(reporterList3);
        reporterList3.getArrayDatas().add(arrayData);
        arrayData2.getReporterLists().add(reporterList3);
        reporterList3.getArrayDatas().add(arrayData2);
        CopyNumberAlterationCriterion copyNumberCriterion = new CopyNumberAlterationCriterion();
        query.setResultType(ResultTypeEnum.COPY_NUMBER);
        query.getCompoundCriterion().getCriterionCollection().clear();
        query.getCompoundCriterion().getCriterionCollection().add(copyNumberCriterion);
        result = queryManagementService.executeGenomicDataQuery(query);
        assertEquals(1, result.getFilteredRowCollection().size());
        ChromosomalLocation location = new ChromosomalLocation();
        location.setStartPosition(1);
        location.setEndPosition(4);
        segmentData2.setLocation(location);
        result = queryManagementService.executeGenomicDataQuery(query);
        assertEquals(2, result.getFilteredRowCollection().size());
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
                ReporterTypeEnum reporterType, Study study, Platform platform) {
            Set<AbstractReporter> reporters = new HashSet<AbstractReporter>();
            reporters.add(reporter );
            return reporters;
        }
        
        @Override
        public List<SegmentData> findMatchingSegmentDatas(CopyNumberAlterationCriterion copyNumberCriterion,
                Study study, Platform platform) {
            List<SegmentData> segmentDatas = new ArrayList<SegmentData>();
            segmentDatas.add(segmentData);
            segmentDatas.add(segmentData2);
            return segmentDatas;
        }
        
        @Override
        public List<SegmentData> findMatchingSegmentDatasByLocation(List<SegmentData> segmentDatasToMatch, 
                Study study, Platform platform) {
            return findMatchingSegmentDatas(null, study, platform);
        }
    }
    
    @SuppressWarnings("unchecked")
    private static class ImageStudyTestDaoStub extends CaIntegrator2DaoStub {
        public <T> T get(Long id, Class<T> objectClass) {
            return (T) DisplayableQueryResultTest.retrieveStudySubjectAssignment(id);
        }
        
    }

}
