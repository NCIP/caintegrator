/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.analysis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import edu.mit.broad.genepattern.gp.services.CaIntegrator2GPClient;
import gov.nih.nci.caintegrator2.application.analysis.geneexpression.AbstractGEPlotParameters;
import gov.nih.nci.caintegrator2.application.analysis.geneexpression.ControlSamplesNotMappedException;
import gov.nih.nci.caintegrator2.application.analysis.geneexpression.GEPlotAnnotationBasedParameters;
import gov.nih.nci.caintegrator2.application.analysis.geneexpression.GEPlotClinicalQueryBasedParameters;
import gov.nih.nci.caintegrator2.application.analysis.geneexpression.GEPlotGenomicQueryBasedParameters;
import gov.nih.nci.caintegrator2.application.analysis.geneexpression.GenesNotFoundInStudyException;
import gov.nih.nci.caintegrator2.application.analysis.grid.GenePatternGridRunnerStub;
import gov.nih.nci.caintegrator2.application.analysis.grid.comparativemarker.ComparativeMarkerSelectionParameters;
import gov.nih.nci.caintegrator2.application.analysis.grid.gistic.GisticParameters;
import gov.nih.nci.caintegrator2.application.analysis.grid.gistic.GisticRefgeneFileEnum;
import gov.nih.nci.caintegrator2.application.analysis.grid.pca.PCAParameters;
import gov.nih.nci.caintegrator2.application.analysis.grid.preprocess.PreprocessDatasetParameters;
import gov.nih.nci.caintegrator2.application.geneexpression.GeneExpressionPlotGroup;
import gov.nih.nci.caintegrator2.application.geneexpression.GeneExpressionPlotServiceImpl;
import gov.nih.nci.caintegrator2.application.geneexpression.PlotCalculationTypeEnum;
import gov.nih.nci.caintegrator2.application.kmplot.CaIntegratorKMPlotServiceStub;
import gov.nih.nci.caintegrator2.application.kmplot.KMPlot;
import gov.nih.nci.caintegrator2.application.kmplot.KMPlotServiceCaIntegratorImpl;
import gov.nih.nci.caintegrator2.application.kmplot.PlotTypeEnum;
import gov.nih.nci.caintegrator2.application.query.InvalidCriterionException;
import gov.nih.nci.caintegrator2.application.query.QueryManagementServiceForKMPlotStub;
import gov.nih.nci.caintegrator2.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator2.data.CaIntegrator2DaoStub;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.application.AbstractCriterion;
import gov.nih.nci.caintegrator2.domain.application.AbstractPersistedAnalysisJob;
import gov.nih.nci.caintegrator2.domain.application.AnalysisJobStatusEnum;
import gov.nih.nci.caintegrator2.domain.application.BooleanOperatorEnum;
import gov.nih.nci.caintegrator2.domain.application.ComparativeMarkerSelectionAnalysisJob;
import gov.nih.nci.caintegrator2.domain.application.CompoundCriterion;
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.GisticAnalysisJob;
import gov.nih.nci.caintegrator2.domain.application.PrincipalComponentAnalysisJob;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.ResultColumn;
import gov.nih.nci.caintegrator2.domain.application.ResultTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.domain.genomic.SampleSet;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.ParameterException;
import gov.nih.nci.caintegrator2.external.ServerConnectionProfile;
import gov.nih.nci.caintegrator2.file.FileManagerStub;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.genepattern.webservice.JobInfo;
import org.genepattern.webservice.ParameterInfo;
import org.genepattern.webservice.TaskExecutor;
import org.genepattern.webservice.TaskInfo;
import org.genepattern.webservice.WebServiceException;
import org.junit.Before;
import org.junit.Test;

public class AnalysisServiceTest {

    private AnalysisService service;
    private TestGenePatternClientStub genePatternClientStub = new TestGenePatternClientStub();
    private GenePatternClientFactoryStub genePatternClientFactoryStub = new GenePatternClientFactoryStub();
    // KMPlot Items
    private CaIntegratorKMPlotServiceStub caIntegratorPlotServiceStub = new CaIntegratorKMPlotServiceStub();
    private DaoForAnalysisServiceStub daoStub = new DaoForAnalysisServiceStub();
    private QueryManagementServiceForKMPlotStub queryManagementServiceForKmPlotStub = 
                        new QueryManagementServiceForKMPlotStub();
    private GenePatternGridRunnerStub genePatternGridRunnerStub = new GenePatternGridRunnerStub();
    private FileManagerStub fileManagerStub = new FileManagerStub();
    
    @Before
    public void setUp() {
        AnalysisServiceImpl serviceImpl = new AnalysisServiceImpl();
        KMPlotServiceCaIntegratorImpl kmPlotService = new KMPlotServiceCaIntegratorImpl();
        kmPlotService.setCaIntegratorPlotService(caIntegratorPlotServiceStub);
        GeneExpressionPlotServiceImpl gePlotService = new GeneExpressionPlotServiceImpl();
        caIntegratorPlotServiceStub.clear();
        fileManagerStub.clear();
        serviceImpl.setGenePatternClientFactory(genePatternClientFactoryStub); 
        serviceImpl.setDao(daoStub);
        serviceImpl.setKmPlotService(kmPlotService);
        serviceImpl.setGePlotService(gePlotService);
        queryManagementServiceForKmPlotStub.setDao(daoStub);
        serviceImpl.setQueryManagementService(queryManagementServiceForKmPlotStub);
        serviceImpl.setFileManager(fileManagerStub);
        service = serviceImpl;
        genePatternGridRunnerStub.clear();
        serviceImpl.setGenePatternGridRunner(genePatternGridRunnerStub);
    }

    @Test
    public void testGetGenePatternMethods() throws WebServiceException {
        ServerConnectionProfile server = new ServerConnectionProfile();
        List<AnalysisMethod> methods = service.getGenePatternMethods(server);
        assertEquals(2, methods.size());
        assertEquals(AnalysisServiceType.GENEPATTERN, methods.get(0).getServiceType());
        assertEquals("task1", methods.get(0).getName());
        assertEquals("description1", methods.get(0).getDescription());
        assertEquals(3, methods.get(0).getParameters().size());
        assertEquals("parameter1", methods.get(0).getParameters().get(0).getName());
        assertEquals(true, methods.get(0).getParameters().get(0).isRequired());
        assertEquals(false, methods.get(0).getParameters().get(1).isRequired());
        assertEquals(AnalysisParameterType.STRING, methods.get(0).getParameters().get(0).getType());
        AnalysisParameter parameterWithChoices = methods.get(0).getParameters().get(2);
        IntegerParameterValue value = (IntegerParameterValue) parameterWithChoices.getDefaultValue();
        assertEquals(methods.get(0).getParameters().get(2), value.getParameter());
        assertEquals(Integer.valueOf(2), value.getValue());
        assertEquals("parameter3", parameterWithChoices.getName());
        assertEquals(2, parameterWithChoices.getChoiceKeys().size());
        assertEquals(Integer.valueOf(1), ((IntegerParameterValue) parameterWithChoices.getChoice("choice1")).getValue());
    }
    
    @Test
    public void testGisticWebService() throws ConnectionException, InvalidCriterionException, ParameterException, IOException {
        StatusUpdateTestListener listener = new StatusUpdateTestListener();
        GisticAnalysisJob job = new GisticAnalysisJob();
        StudySubscription subscription = new StudySubscription();
        subscription.setStudy(new Study());
        job.setSubscription(subscription);
        GisticParameters parameters = new GisticParameters();
        job.getGisticAnalysisForm().setGisticParameters(parameters);
        parameters.getServer().setUrl("http://genepattern.broadinstitute.org/gp/services/Analysis");
        parameters.setRefgeneFile(GisticRefgeneFileEnum.HUMAN_HG16);
        service.executeGridGistic(listener, job);
        assertTrue(listener.statuses.contains(AnalysisJobStatusEnum.PROCESSING_LOCALLY));
        assertTrue(listener.statuses.contains(AnalysisJobStatusEnum.PROCESSING_REMOTELY));
        assertTrue(listener.statuses.contains(AnalysisJobStatusEnum.COMPLETED));
        assertTrue(fileManagerStub.createMarkersFileCalled);
        assertTrue(fileManagerStub.createSamplesFileCalled);
        
        assertFalse(genePatternGridRunnerStub.runGisticCalled);
        parameters.setCnvSegmentsToIgnoreFile(fileManagerStub.retrieveTmpFile());
        parameters.getServer().setUrl("something/Gistic");
        service.executeGridGistic(listener, job);
        assertTrue(genePatternGridRunnerStub.runGisticCalled);
        assertTrue(fileManagerStub.renameCnvFileCalled);
        Query clinicalQuery = new Query();
        clinicalQuery.setName("clinicalQueryName");
        SampleSet sampleSet = new SampleSet();
        sampleSet.setName("sampleSetName");
        parameters.setExcludeControlSampleSet(sampleSet);
        parameters.setClinicalQuery(clinicalQuery);
        parameters.getServer().setHostname("hostname");
        assertTrue(job.toString().contains(clinicalQuery.getName()));
        assertTrue(job.toString().contains(sampleSet.getName()));
        assertTrue(job.toString().contains(parameters.getServer().getHostname()));
    }
    
    private static class StatusUpdateTestListener implements StatusUpdateListener {
        private Set<AnalysisJobStatusEnum> statuses = new HashSet<AnalysisJobStatusEnum>();

        public void updateStatus(AbstractPersistedAnalysisJob job) {
            statuses.add(job.getStatus());
        }
    }
    
    @Test
    public void testExecuteGridPCA() throws ConnectionException, InvalidCriterionException {
        StatusUpdateTestListener updater = new StatusUpdateTestListener();
        PrincipalComponentAnalysisJob job = new PrincipalComponentAnalysisJob();
        PCAParameters pcaParameters = new PCAParameters();
        Query query = new Query();
        query.setCompoundCriterion(new CompoundCriterion());
        query.setName("queryNameString");
        pcaParameters.getClinicalQueries().add(query);
        SampleSet sampleSet = new SampleSet();
        sampleSet.setId(1l);
        sampleSet.setName("sampleSetNameString");
        pcaParameters.setExcludedControlSampleSet(sampleSet);
        job.getForm().setPcaParameters(pcaParameters);
        StudySubscription subscription = new StudySubscription();
        subscription.setStudy(new Study());
        job.setSubscription(subscription);
        service.executeGridPCA(updater, job);
        assertTrue(genePatternGridRunnerStub.runPCACalled);
        assertTrue(fileManagerStub.createGctFileCalled);
        assertTrue(fileManagerStub.createInputZipFileCalled);
        assertTrue(job.toString().contains(query.getName()));
        assertTrue(job.toString().contains(sampleSet.getName()));
    }
    
    @Test
    public void testExecuteGridPreprocessComparativeMarker() throws ConnectionException, InvalidCriterionException {
        StatusUpdateTestListener updater = new StatusUpdateTestListener();
        ComparativeMarkerSelectionAnalysisJob job = new ComparativeMarkerSelectionAnalysisJob();
        PreprocessDatasetParameters preprocessParams = new PreprocessDatasetParameters();
        ComparativeMarkerSelectionParameters cmsParams = new ComparativeMarkerSelectionParameters();
        Query query = new Query();
        ResultColumn column = new ResultColumn();
        AnnotationFieldDescriptor descriptor = new AnnotationFieldDescriptor();
        descriptor.setDefinition(new AnnotationDefinition());
        descriptor.setAnnotationEntityType(EntityTypeEnum.SUBJECT);
        column.setAnnotationFieldDescriptor(descriptor);
        column.setColumnIndex(0);
        query.setColumnCollection(new HashSet<ResultColumn>());
        query.getColumnCollection().add(column);
        query.setCompoundCriterion(new CompoundCriterion());
        query.getCompoundCriterion().setBooleanOperator(BooleanOperatorEnum.AND);
        query.getCompoundCriterion().setCriterionCollection(new HashSet<AbstractCriterion>());
        query.setName("queryNameString");
        cmsParams.getClinicalQueries().add(query);
        SampleSet sampleSet = new SampleSet();
        sampleSet.setId(1l);
        sampleSet.setName("sampleSetNameString");
        preprocessParams.setExcludedControlSampleSet(sampleSet);
        job.getForm().setComparativeMarkerSelectionParameters(cmsParams);
        job.getForm().setPreprocessDatasetparameters(preprocessParams);
        KMPlotStudyCreator studyCreator = new KMPlotStudyCreator();
        Study study = studyCreator.createKMPlotStudy();
        StudySubscription subscription = new StudySubscription();
        query.setSubscription(subscription);
        subscription.setStudy(study);
        job.setSubscription(subscription);
        queryManagementServiceForKmPlotStub.kmPlotType = PlotTypeEnum.ANNOTATION_BASED;
        service.executeGridPreprocessComparativeMarker(updater, job);
        assertTrue(fileManagerStub.createClassificationFileCalled);
        assertTrue(fileManagerStub.createGctFileCalled);
        assertTrue(fileManagerStub.createInputZipFileCalled);
        assertTrue(genePatternGridRunnerStub.runPreprocessComparativeMarkerSelectionCalled);
        assertTrue(job.toString().contains(query.getName()));
        assertTrue(job.toString().contains(sampleSet.getName()));
        
    }
    
    
    
    @Test
    public void testExecuteGenePatternJob() throws WebServiceException {
        AnalysisMethodInvocation invocation = new AnalysisMethodInvocation();
        ServerConnectionProfile server = new ServerConnectionProfile();
        AnalysisMethod method = new AnalysisMethod();
        server.setUrl("http://localhost");
        method.setName("method");
        invocation.setMethod(method);
        StringParameterValue parameterValue = new StringParameterValue();
        parameterValue.setValue("value");
        AnalysisParameter parameter = new AnalysisParameter();
        parameter.setType(AnalysisParameterType.STRING);
        parameter.setName("parameter");
        method.getParameters().add(parameter);
        parameterValue.setParameter(parameter);
        invocation.setParameterValue(parameter, parameterValue);
        service.executeGenePatternJob(server, invocation);
        assertEquals("method", genePatternClientStub.taskName);
        assertEquals(1, genePatternClientStub.parameters.size());
        ParameterInfo genePatternParameter = genePatternClientStub.parameters.get(0);
        assertEquals("parameter", genePatternParameter.getName());
        assertEquals("value", genePatternParameter.getValue());
    }
    
    private void runKMPlotTest(KMPlotStudyCreator studyCreator, StudySubscription subscription, AbstractKMParameters annotationParameters) 
    throws InvalidCriterionException, GenesNotFoundInStudyException, InvalidSurvivalValueDefinitionException {
        KMPlot kmPlot = service.createKMPlot(subscription, annotationParameters);
        
        assertNotNull(kmPlot);
        assertTrue(caIntegratorPlotServiceStub.computeLogRankPValueBetweenCalled);
        assertTrue(caIntegratorPlotServiceStub.getChartCalled);
        boolean exceptionCaught = false;
        try { // Try giving no survival value definition.
            annotationParameters.setSurvivalValueDefinition(null);
            kmPlot = service.createKMPlot(subscription, annotationParameters);
        } catch (InvalidSurvivalValueDefinitionException e) {
            exceptionCaught = true;
        }
        assertTrue(exceptionCaught);
        
        exceptionCaught = false;
        studyCreator.getSurvivalValueDefinition().setLastFollowupDate(null);
        try { // Try giving survivalValueDefinition without a followup date
            annotationParameters.setSurvivalValueDefinition(studyCreator.getSurvivalValueDefinition());
            kmPlot = service.createKMPlot(subscription, annotationParameters);
        } catch (InvalidSurvivalValueDefinitionException e) {
            exceptionCaught = true;
        }
        assertTrue(exceptionCaught);
    }
    
    @Test
    public void testCreateAnnotationBasedKMPlot() throws InvalidCriterionException, GenesNotFoundInStudyException, InvalidSurvivalValueDefinitionException {
        KMPlotStudyCreator studyCreator = new KMPlotStudyCreator();
        Study study = studyCreator.createKMPlotStudy();
        StudySubscription subscription = new StudySubscription();
        subscription.setStudy(study);
        queryManagementServiceForKmPlotStub.kmPlotType = PlotTypeEnum.ANNOTATION_BASED;
        KMAnnotationBasedParameters annotationParameters = new KMAnnotationBasedParameters();
        assertFalse(annotationParameters.validate());
        annotationParameters.setSelectedAnnotation(studyCreator.getGroupAnnotationFieldDescriptor());
        annotationParameters.getSelectedValues().addAll(studyCreator.getPlotGroupValues());
        annotationParameters.setSurvivalValueDefinition(studyCreator.getSurvivalValueDefinition());
        assertTrue(annotationParameters.validate());
        runKMPlotTest(studyCreator, subscription, annotationParameters);
        
    }
    
    @Test
    public void testCreateGeneExpressionBasedKMPlot() throws InvalidCriterionException, GenesNotFoundInStudyException, InvalidSurvivalValueDefinitionException {
        KMPlotStudyCreator studyCreator = new KMPlotStudyCreator();
        Study study = studyCreator.createKMPlotStudy();
        StudySubscription subscription = new StudySubscription();
        subscription.setStudy(study);
        queryManagementServiceForKmPlotStub.kmPlotType = PlotTypeEnum.GENE_EXPRESSION;
        KMGeneExpressionBasedParameters geneExpressionParameters = new KMGeneExpressionBasedParameters();
        assertFalse(geneExpressionParameters.validate());
        geneExpressionParameters.setGeneSymbol("EGFR");
        geneExpressionParameters.setOverexpressedFoldChangeNumber(2.0F);
        geneExpressionParameters.setUnderexpressedFoldChangeNumber(2.0F);
        geneExpressionParameters.setSurvivalValueDefinition(studyCreator.getSurvivalValueDefinition());
        geneExpressionParameters.setControlSampleSetName("name");
        assertTrue(geneExpressionParameters.validate());
        runKMPlotTest(studyCreator, subscription, geneExpressionParameters);
        
    }
    
    @Test
    public void testCreateQueryBasedKMPlot() throws InvalidCriterionException, GenesNotFoundInStudyException, InvalidSurvivalValueDefinitionException {
        KMPlotStudyCreator studyCreator = new KMPlotStudyCreator();
        Study study = studyCreator.createKMPlotStudy();
        StudySubscription subscription = new StudySubscription();
        subscription.setStudy(study);
        queryManagementServiceForKmPlotStub.kmPlotType = PlotTypeEnum.QUERY_BASED;
        KMQueryBasedParameters queryBasedParameters = new KMQueryBasedParameters();
        assertFalse(queryBasedParameters.validate());
        queryBasedParameters.setExclusiveGroups(true);
        queryBasedParameters.setAddPatientsNotInQueriesGroup(true);
        Query query1 = new Query();
        query1.setId(Long.valueOf(1));
        query1.setSubscription(subscription);
        query1.setResultType(ResultTypeEnum.GENOMIC);
        queryBasedParameters.getQueries().add(query1);
        Query query2 = new Query();
        query2.setId(Long.valueOf(2));
        query2.setSubscription(subscription);
        queryBasedParameters.getQueries().add(query2);
        queryBasedParameters.setSurvivalValueDefinition(studyCreator.getSurvivalValueDefinition());
        assertTrue(queryBasedParameters.validate());
        runKMPlotTest(studyCreator, subscription, queryBasedParameters);
        assertEquals(ResultTypeEnum.CLINICAL, query1.getResultType());
    }
    
    private void runGEPlotTest(StudySubscription subscription, AbstractGEPlotParameters parameters) 
    throws ControlSamplesNotMappedException, InvalidCriterionException, GenesNotFoundInStudyException {
        try {
            queryManagementServiceForKmPlotStub.throwGenesNotFoundException = true;
            service.createGeneExpressionPlot(subscription, parameters);
            if (!(parameters instanceof GEPlotGenomicQueryBasedParameters)) { // Genomic Query doesn't have gene input.
                fail();
            }
        } catch (GenesNotFoundInStudyException e) {
            // expected result.
            queryManagementServiceForKmPlotStub.clear();
        }
        
        GeneExpressionPlotGroup gePlot = service.createGeneExpressionPlot(subscription, parameters);
        
        assertNotNull(gePlot.getPlot(PlotCalculationTypeEnum.MEAN));

    }
    
    @Test
    public void testCreateAnnotationBasedGEPlot() throws ControlSamplesNotMappedException, InvalidCriterionException, GenesNotFoundInStudyException {
        KMPlotStudyCreator studyCreator = new KMPlotStudyCreator();
        Study study = studyCreator.createKMPlotStudy();
        StudySubscription subscription = new StudySubscription();
        subscription.setStudy(study);
        queryManagementServiceForKmPlotStub.kmPlotType = PlotTypeEnum.ANNOTATION_BASED;
        GEPlotAnnotationBasedParameters annotationParameters = new GEPlotAnnotationBasedParameters();
        annotationParameters.setSelectedAnnotation(studyCreator.getGroupAnnotationFieldDescriptor());
        annotationParameters.getSelectedValues().addAll(studyCreator.getPlotGroupValues());
        annotationParameters.setGeneSymbol("egfr");
        annotationParameters.setAddControlSamplesGroup(true);
        annotationParameters.setAddPatientsNotInQueriesGroup(true);
        annotationParameters.setControlSampleSetName("samples");

        assertTrue(annotationParameters.validate());
        runGEPlotTest(subscription, annotationParameters);
        
    }
    
    @Test
    public void testCreateGenomicQueryBasedGEPlot() throws ControlSamplesNotMappedException, InvalidCriterionException, GenesNotFoundInStudyException {
        KMPlotStudyCreator studyCreator = new KMPlotStudyCreator();
        Study study = studyCreator.createKMPlotStudy();
        StudySubscription subscription = new StudySubscription();
        subscription.setStudy(study);
        queryManagementServiceForKmPlotStub.kmPlotType = PlotTypeEnum.QUERY_BASED;
        GEPlotGenomicQueryBasedParameters genomicQueryParameters = new GEPlotGenomicQueryBasedParameters();
        Query query1 = new Query();
        query1.setName("Query1");
        query1.setId(Long.valueOf(1));
        query1.setSubscription(subscription);
        query1.setResultType(ResultTypeEnum.GENOMIC);
        query1.setReporterType(ReporterTypeEnum.GENE_EXPRESSION_GENE);
        query1.setCompoundCriterion(new CompoundCriterion());
        assertFalse(genomicQueryParameters.validate());
        genomicQueryParameters.setQuery(query1);
        genomicQueryParameters.setReporterType(ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET);
        runGEPlotTest(subscription, genomicQueryParameters);
        assertEquals(ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET, query1.getReporterType());
    }
    
    @Test
    public void testCreateClinicalQueryBasedGEPlot() throws ControlSamplesNotMappedException, InvalidCriterionException, GenesNotFoundInStudyException {
        KMPlotStudyCreator studyCreator = new KMPlotStudyCreator();
        Study study = studyCreator.createKMPlotStudy();
        StudySubscription subscription = new StudySubscription();
        subscription.setStudy(study);
        queryManagementServiceForKmPlotStub.kmPlotType = PlotTypeEnum.QUERY_BASED;
        GEPlotClinicalQueryBasedParameters genomicQueryParameters = new GEPlotClinicalQueryBasedParameters();
        genomicQueryParameters.setAddPatientsNotInQueriesGroup(true);
        genomicQueryParameters.setExclusiveGroups(true);
        Query query1 = new Query();
        query1.setName("Query1");
        query1.setId(Long.valueOf(1));
        query1.setSubscription(subscription);
        query1.setResultType(ResultTypeEnum.CLINICAL);
        genomicQueryParameters.getQueries().add(query1);
        Query query2 = new Query();
        query2.setName("Query2");
        query2.setId(Long.valueOf(2));
        query2.setSubscription(subscription);
        genomicQueryParameters.getQueries().add(query2);
        assertFalse(genomicQueryParameters.validate());
        genomicQueryParameters.setGeneSymbol("EGFR");
        assertTrue(genomicQueryParameters.validate());
        runGEPlotTest(subscription, genomicQueryParameters);
    }
    
    private final class GenePatternClientFactoryStub implements GenePatternClientFactory {

        public CaIntegrator2GPClient retrieveClient(ServerConnectionProfile server) {
            return genePatternClientStub;
        }

        public CaIntegrator2GPClient retrieveOldGenePatternClient(ServerConnectionProfile server) {
            return genePatternClientStub;
        }
        
    }

    private final class TestGenePatternClientStub extends GenePatternClientStub {

        private String taskName;
        private List<ParameterInfo> parameters;

        @Override
        public JobInfo getStatus(JobInfo jobInfo) {
            jobInfo.setStatus("Completed");
            return jobInfo;
        }

        @SuppressWarnings("unchecked")
        public TaskInfo[] getTasks() throws WebServiceException {
            TaskInfo[] tasks = new TaskInfo[4];
            tasks[0] = new TaskInfo();
            tasks[0].setName("task1");
            tasks[0].setDescription("description1");
            tasks[0].setTaskInfoAttributes(new HashMap<String, String>());
            tasks[0].setParameterInfoArray(new ParameterInfo[3]);
            tasks[0].getParameterInfoArray()[0] = new ParameterInfo();
            tasks[0].getParameterInfoArray()[0].setName("parameter1");
            tasks[0].getParameterInfoArray()[0].setAttributes(new HashMap<String, String>());
            tasks[0].getParameterInfoArray()[0].getAttributes().put("type", "java.lang.String");
            tasks[0].getParameterInfoArray()[0].getAttributes().put("optional", "");
            tasks[0].getParameterInfoArray()[0].getAttributes().put(TaskExecutor.PARAM_INFO_DEFAULT_VALUE[0], "default");
            

            tasks[0].getParameterInfoArray()[1] = new ParameterInfo();
            tasks[0].getParameterInfoArray()[1].setName("parameter2");
            tasks[0].getParameterInfoArray()[1].setAttributes(new HashMap<String, String>());
            tasks[0].getParameterInfoArray()[1].getAttributes().put("type", "java.io.File");
            tasks[0].getParameterInfoArray()[1].getAttributes().put("fileFormat", "gct;res");
            tasks[0].getParameterInfoArray()[1].getAttributes().put("optional", "on");
            
            tasks[0].getParameterInfoArray()[2] = new ParameterInfo();
            tasks[0].getParameterInfoArray()[2].setName("parameter3");
            tasks[0].getParameterInfoArray()[2].setAttributes(new HashMap<String, String>());
            tasks[0].getParameterInfoArray()[2].getAttributes().put(TaskExecutor.PARAM_INFO_DEFAULT_VALUE[0], "2");
            tasks[0].getParameterInfoArray()[2].getAttributes().put("type", "java.lang.Integer");
            tasks[0].getParameterInfoArray()[2].setValue("1=choice1;2=choice2");
            
            tasks[1] = new TaskInfo();
            tasks[1].setName("task2");
            tasks[1].setDescription("description2");
            tasks[1].setParameterInfoArray(new ParameterInfo[2]);
            tasks[1].setTaskInfoAttributes(new HashMap<String, String>());
            tasks[1].getParameterInfoArray()[0] = new ParameterInfo();
            tasks[1].getParameterInfoArray()[0].setName("parameter1");
            tasks[1].getParameterInfoArray()[0].setAttributes(new HashMap<String, String>());
            tasks[1].getParameterInfoArray()[0].getAttributes().put("type", "java.lang.String");
            tasks[1].getParameterInfoArray()[1] = new ParameterInfo();
            tasks[1].getParameterInfoArray()[1].setName("parameter2");
            tasks[1].getParameterInfoArray()[1].setAttributes(new HashMap<String, String>());
            tasks[1].getParameterInfoArray()[1].getAttributes().put("type", "java.io.File");
            tasks[1].getParameterInfoArray()[1].getAttributes().put("fileFormat", "cls");
            
            tasks[2] = new TaskInfo();
            tasks[2].setName("task3");
            tasks[2].setDescription("description3");
            tasks[2].setTaskInfoAttributes(new HashMap<String, String>());
            tasks[2].setParameterInfoArray(new ParameterInfo[3]);
            tasks[2].getParameterInfoArray()[0] = new ParameterInfo();
            tasks[2].getParameterInfoArray()[0].setName("parameter1");
            tasks[2].getParameterInfoArray()[0].setAttributes(new HashMap<String, String>());
            tasks[2].getParameterInfoArray()[0].getAttributes().put("type", "java.lang.Float");

            tasks[2].getParameterInfoArray()[1] = new ParameterInfo();
            tasks[2].getParameterInfoArray()[1].setName("parameter2");
            tasks[2].getParameterInfoArray()[1].setAttributes(new HashMap<String, String>());
            tasks[2].getParameterInfoArray()[1].getAttributes().put("type", "java.io.File");
            tasks[2].getParameterInfoArray()[1].getAttributes().put("fileFormat", "gct;res");

            tasks[2].getParameterInfoArray()[2] = new ParameterInfo();
            tasks[2].getParameterInfoArray()[2].setName("parameter3");
            tasks[2].getParameterInfoArray()[2].setAttributes(new HashMap<String, String>());
            tasks[2].getParameterInfoArray()[2].getAttributes().put("type", "java.io.File");
            tasks[2].getParameterInfoArray()[2].getAttributes().put("fileFormat", "cls");
            
            tasks[3] = new TaskInfo();
            tasks[3].setName("task3");
            tasks[3].setDescription("description3");
            Map<String, String> task4Attributes = new HashMap<String, String>();
            task4Attributes.put("taskType", "Visualizer");
            tasks[3].setTaskInfoAttributes(task4Attributes);
            tasks[3].setParameterInfoArray(new ParameterInfo[3]);
            return tasks;
        }
        
        /**
         * {@inheritDoc}
         */
        @Override
        public JobInfo runAnalysis(String taskName, List<ParameterInfo> parameters) throws WebServiceException {
            this.taskName = taskName;
            this.parameters = parameters;
            return new JobInfo();
        }


    }

    private static class DaoForAnalysisServiceStub extends CaIntegrator2DaoStub {
        
        public boolean returnNoGeneSymbols = false;
        
        /**
         * {@inheritDoc}
         */
        @SuppressWarnings("unchecked")
        @Override
        public <T> T get(Long id, Class<T> objectClass) {
            if (objectClass.equals(StudySubscription.class)) {
                 StudySubscription studySubscription = new StudySubscription();
                 studySubscription.setStudy(new Study());
                 return (T) studySubscription;
            } else {
                return super.get(id, objectClass);
            }
        }
        
        @Override
        public void clear() {
            super.clear();
            returnNoGeneSymbols = false;
        }
        
        @Override
        public Set<String> retrieveGeneSymbolsInStudy(Collection<String> symbols, Study study) {
            Set<String> newSymbols = new HashSet<String>();
            for (String symbol : symbols) {
               newSymbols.add(symbol); 
            }
            return returnNoGeneSymbols ? new HashSet<String>() : newSymbols;
        }
    }
}
