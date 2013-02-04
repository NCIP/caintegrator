/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.analysis;

import edu.mit.broad.genepattern.gp.services.CaIntegrator2GPClient;
import gov.nih.nci.caintegrator2.application.CaIntegrator2BaseService;
import gov.nih.nci.caintegrator2.application.analysis.geneexpression.AbstractGEPlotHandler;
import gov.nih.nci.caintegrator2.application.analysis.geneexpression.AbstractGEPlotParameters;
import gov.nih.nci.caintegrator2.application.analysis.geneexpression.ControlSamplesNotMappedException;
import gov.nih.nci.caintegrator2.application.analysis.geneexpression.GenesNotFoundInStudyException;
import gov.nih.nci.caintegrator2.application.analysis.grid.GenePatternGridRunner;
import gov.nih.nci.caintegrator2.application.analysis.grid.gistic.GisticParameters;
import gov.nih.nci.caintegrator2.application.analysis.grid.gistic.GisticSamplesMarkers;
import gov.nih.nci.caintegrator2.application.analysis.grid.pca.PCAParameters;
import gov.nih.nci.caintegrator2.application.analysis.grid.preprocess.PreprocessDatasetParameters;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataService;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValueType;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValues;
import gov.nih.nci.caintegrator2.application.geneexpression.GeneExpressionPlotGroup;
import gov.nih.nci.caintegrator2.application.geneexpression.GeneExpressionPlotService;
import gov.nih.nci.caintegrator2.application.kmplot.KMPlot;
import gov.nih.nci.caintegrator2.application.kmplot.KMPlotService;
import gov.nih.nci.caintegrator2.application.query.InvalidCriterionException;
import gov.nih.nci.caintegrator2.application.query.QueryManagementService;
import gov.nih.nci.caintegrator2.common.Cai2Util;
import gov.nih.nci.caintegrator2.common.GenePatternUtil;
import gov.nih.nci.caintegrator2.common.HibernateUtil;
import gov.nih.nci.caintegrator2.domain.analysis.GisticAnalysis;
import gov.nih.nci.caintegrator2.domain.analysis.GisticResultZipFileParser;
import gov.nih.nci.caintegrator2.domain.application.AbstractPersistedAnalysisJob;
import gov.nih.nci.caintegrator2.domain.application.AnalysisJobStatusEnum;
import gov.nih.nci.caintegrator2.domain.application.ComparativeMarkerSelectionAnalysisJob;
import gov.nih.nci.caintegrator2.domain.application.GisticAnalysisJob;
import gov.nih.nci.caintegrator2.domain.application.PrincipalComponentAnalysisJob;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator2.domain.genomic.Array;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayDataType;
import gov.nih.nci.caintegrator2.domain.genomic.GisticGenomicRegionReporter;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.domain.genomic.Sample;
import gov.nih.nci.caintegrator2.domain.genomic.SampleSet;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.DataRetrievalException;
import gov.nih.nci.caintegrator2.external.ParameterException;
import gov.nih.nci.caintegrator2.external.ServerConnectionProfile;
import gov.nih.nci.caintegrator2.file.FileManager;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.genepattern.webservice.WebServiceException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of the AnalysisService subsystem.
 */
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class AnalysisServiceImpl extends CaIntegrator2BaseService implements AnalysisService {
    
    private KMPlotService kmPlotService;
    private GeneExpressionPlotService gePlotService;
    private QueryManagementService queryManagementService;
    private GenePatternClientFactory genePatternClientFactory;
    private GenePatternGridRunner genePatternGridRunner;
    private FileManager fileManager;
    private ArrayDataService arrayDataService;
    
    /**
     * {@inheritDoc}
     */
    public List<AnalysisMethod> getGenePatternMethods(ServerConnectionProfile server)
            throws WebServiceException {
        return new GenePatternHelper(retrieveClient(server)).getMethods();
    }

    private CaIntegrator2GPClient retrieveClient(ServerConnectionProfile server) throws WebServiceException {
        return genePatternClientFactory.retrieveOldGenePatternClient(server);
    }

    /**
     * {@inheritDoc}
     */
    public JobInfoWrapper executeGenePatternJob(ServerConnectionProfile server, AnalysisMethodInvocation invocation) 
    throws WebServiceException {
        JobInfoWrapper jobInfo = new GenePatternHelper(retrieveClient(server)).execute(invocation);
        URL serviceUrl;
        URL resultUrl;
        try {
            serviceUrl = new URL(server.getUrl());
            resultUrl = new URL(serviceUrl.getProtocol(), serviceUrl.getHost(), serviceUrl.getPort(), 
                    "/gp/pages/jobResults.jsf");
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid URL provided for server " + server.getUrl(), e);
        }
        jobInfo.setUrl(resultUrl);
        return jobInfo;
    }
    
    /**
     * {@inheritDoc}
     */
    public File executeGridPreprocessComparativeMarker(StatusUpdateListener updater,
            ComparativeMarkerSelectionAnalysisJob job) 
        throws ConnectionException, InvalidCriterionException {
        StudySubscription studySubscription = getDao().get(job.getSubscription().getId(), StudySubscription.class);
        PreprocessDatasetParameters preprocessParams = job.getForm().getPreprocessDatasetparameters();
        job.setSubscription(studySubscription);
        GctDataset gctDataset = createGctDataset(studySubscription, preprocessParams.getClinicalQueries(), 
                preprocessParams.getPlatformName(), preprocessParams.getExcludedControlSampleSet());
        File gctFile = createGctFile(gctDataset, studySubscription,
                preprocessParams.getProcessedGctFilename());
        File clsFile = createClassificationFile(studySubscription, 
                job.getForm().getComparativeMarkerSelectionParameters().getClinicalQueries(), 
                job.getForm().getComparativeMarkerSelectionParameters().getClassificationFileName(), 
                gctDataset.getColumnSampleNames());
        job.setInputZipFile(fileManager.createInputZipFile(studySubscription, job,
                "CMS_INPUT_" +  System.currentTimeMillis() + ".zip", 
                gctFile, clsFile));
        checkForMissingSubjects(job, gctDataset.getSubjectsNotFoundFromQueries());
        return genePatternGridRunner.runPreprocessComparativeMarkerSelection(updater, job, gctFile, clsFile);
    }
    
    /**
     * {@inheritDoc}
     * @throws DataRetrievalException 
     */
    @Transactional(readOnly = false)
    public File executeGridGistic(StatusUpdateListener updater, GisticAnalysisJob job) 
        throws ConnectionException, InvalidCriterionException, ParameterException, IOException,
            DataRetrievalException {
        File resultsZipFile = null;
        StudySubscription studySubscription = getDao().get(job.getSubscription().getId(), StudySubscription.class); 
        job.setSubscription(studySubscription);
        GisticSamplesMarkers gisticSamplesMarkers = 
            GenePatternUtil.createGisticSamplesMarkers(queryManagementService, 
                    job.getGisticAnalysisForm().getGisticParameters(), job.getSubscription());
        checkForMissingSubjects(job, queryManagementService.getAllSubjectsNotFoundInCriteria(job
                .getGisticAnalysisForm().getGisticParameters().getClinicalQuery()));
        resultsZipFile = runGistic(updater, job, studySubscription, gisticSamplesMarkers);
        GisticAnalysis gisticAnalysis = createGisticAnalysis(job, gisticSamplesMarkers.getUsedSamples());
        Map<String, Map<GisticGenomicRegionReporter, Float>> gisticData = parseGisticResults(
                gisticAnalysis.getReporterList(), resultsZipFile);
        getDao().save(studySubscription);
        createArrayData(studySubscription.getStudy(), gisticAnalysis, gisticData);
        job.setStatus(AnalysisJobStatusEnum.COMPLETED);
        updater.updateStatus(job);
        return resultsZipFile;
    }

    private File runGistic(StatusUpdateListener updater, GisticAnalysisJob job, StudySubscription studySubscription,
            GisticSamplesMarkers gisticSamplesMarkers) throws IOException, ConnectionException,
            InvalidCriterionException, ParameterException {
        File resultsZipFile;
        List<File> inputFiles = new ArrayList<File>();
        File segmentFile = fileManager.createSamplesFile(studySubscription, gisticSamplesMarkers.getSamples());
        File markersFile = fileManager.createMarkersFile(studySubscription, gisticSamplesMarkers.getMarkers());
        File cnvFile = job.getGisticAnalysisForm().getGisticParameters().getCnvSegmentsToIgnoreFile();
        if (cnvFile != null) {
            File newCnvFile = fileManager.renameCnvFile(cnvFile);
            inputFiles.add(newCnvFile);
        }
        inputFiles.add(segmentFile);
        inputFiles.add(markersFile);
        job.setInputZipFile(fileManager.createInputZipFile(studySubscription, job,
                "GISTIC_INPUT_" +  System.currentTimeMillis() + ".zip", 
                inputFiles.toArray(new File[inputFiles.size()])));
        if (job.isGridServiceCall()) {
            resultsZipFile = genePatternGridRunner.runGistic(updater, job, segmentFile, markersFile, cnvFile);
        } else {
            resultsZipFile = runGisticWebService(job, updater, segmentFile, markersFile);
        }
        return resultsZipFile;
    }
    
    private Map<String, Map<GisticGenomicRegionReporter, Float>> parseGisticResults(
            ReporterList reporterList, File resultsZipFile) throws DataRetrievalException {
        if (resultsZipFile != null) {
            return new GisticResultZipFileParser(reporterList, getDao()).parse(resultsZipFile);
        }
        return null;
    }
    
    private void createArrayData(Study study, GisticAnalysis gisticAnalysis,
            Map<String, Map<GisticGenomicRegionReporter, Float>> gisticData) {
        if (gisticData != null) {
            for (String sampleName : gisticData.keySet()) {
                ArrayData arrayData = createArrayData(study, gisticAnalysis.getSample(sampleName), gisticAnalysis
                        .getReporterList());
                getDao().save(arrayData);
                List<AbstractReporter> regionReporters = new ArrayList<AbstractReporter>();
                for (GisticGenomicRegionReporter reporter : gisticData.get(sampleName).keySet()) {
                    regionReporters.add((AbstractReporter) reporter);
                }
                ArrayDataValues values = new ArrayDataValues(regionReporters);
                for (GisticGenomicRegionReporter regionReporter : gisticData.get(sampleName).keySet()) {
                    values.setFloatValue(arrayData, regionReporter, ArrayDataValueType.DNA_ANALYSIS_LOG2_RATIO,
                            gisticData.get(sampleName).get(regionReporter));
                }
                getArrayDataService().save(values);
            }
        }
    }

    private ArrayData createArrayData(Study study, Sample sample, ReporterList reporterList) {
        ArrayData arrayData = new ArrayData();
        arrayData.setType(ArrayDataType.GISTIC_ANALYSIS);
        arrayData.setSample(sample);
        sample.getArrayDataCollection().add(arrayData);
        arrayData.setStudy(study);
        Array array = new Array();
        array.getArrayDataCollection().add(arrayData);
        arrayData.setArray(array);
        array.getSampleCollection().add(sample);
        sample.getArrayCollection().add(array);
        arrayData.getReporterLists().add(reporterList);
        reporterList.getArrayDatas().add(arrayData);    
        return arrayData;
    }


    private GisticAnalysis createGisticAnalysis(GisticAnalysisJob job, Set<Sample> samplesUsed) {
        GisticAnalysis gisticAnalysis = new GisticAnalysis();
        StudySubscription subscription = job.getSubscription();
        GisticParameters parameters = job.getGisticAnalysisForm().getGisticParameters();
        gisticAnalysis.setAmplificationsThreshold(parameters.getAmplificationsThreshold());
        gisticAnalysis.setDeletionsThreshold(parameters.getDeletionsThreshold());
        gisticAnalysis.setGenomeBuildInformation(parameters.getRefgeneFile().getParameterValue());
        gisticAnalysis.setJoinSegmentSize(parameters.getJoinSegmentSize());
        gisticAnalysis.setName(job.getName());
        if (parameters.getClinicalQuery() != null) {
            gisticAnalysis.setQueryOrListName(parameters.getClinicalQuery().getName());
        }
        gisticAnalysis.setQvThreshold(parameters.getQvThresh());
        gisticAnalysis.setUrl(parameters.getServer().getUrl());
        gisticAnalysis.getSamplesUsedForCalculation().addAll(samplesUsed);
        subscription.getCopyNumberAnalysisCollection().add(gisticAnalysis);

        ReporterList reporterList = new ReporterList("GISTIC result", ReporterTypeEnum.GISTIC_GENOMIC_REGION_REPORTER);
        gisticAnalysis.setReporterList(reporterList);

        return gisticAnalysis;
    }
    
    private File runGisticWebService(GisticAnalysisJob job, StatusUpdateListener updater, 
            File segmentFile, File markersFile) 
    throws ConnectionException, InvalidCriterionException, IOException {
        ServerConnectionProfile server = job.getGisticAnalysisForm().getGisticParameters().getServer();
        GisticWebServiceRunner gisticWebServiceRunner;
        try {
            gisticWebServiceRunner = new GisticWebServiceRunner(retrieveClient(server), getFileManager());
            return gisticWebServiceRunner.runGistic(updater, job, segmentFile, markersFile);
        } catch (WebServiceException e) {
            throw new ConnectionException("Unable to connect to GenePattern.", e);
        }
        
    }
    
    /**
     * {@inheritDoc}
     */
    public File executeGridPCA(StatusUpdateListener updater,
            PrincipalComponentAnalysisJob job) throws ConnectionException, InvalidCriterionException {
        StudySubscription studySubscription = getDao().get(job.getSubscription().getId(), StudySubscription.class); 
        job.setSubscription(studySubscription);
        PCAParameters parameters = job.getForm().getPcaParameters();
        GctDataset gctDataset = createGctDataset(studySubscription, parameters.getClinicalQueries(), 
                parameters.getPlatformName(), parameters.getExcludedControlSampleSet());
        File gctFile = createGctFile(gctDataset, studySubscription, parameters.getGctFileName());
        checkForMissingSubjects(job, gctDataset.getSubjectsNotFoundFromQueries());
        job.setInputZipFile(fileManager.createInputZipFile(studySubscription, job,
                "PCA_INPUT_" +  System.currentTimeMillis() + ".zip", 
                gctFile));
        return genePatternGridRunner.runPCA(updater, job, gctFile);
    }

    private void checkForMissingSubjects(AbstractPersistedAnalysisJob job, Set<String> subjectsNotFound) {
        if (!subjectsNotFound.isEmpty()) {
            job.setStatusDescription("Subjects not found in queries/subject lists: " 
                    + StringUtils.join(subjectsNotFound, ","));
        }
    }

    /**
     * {@inheritDoc}
     */
    public KMPlot createKMPlot(StudySubscription subscription, AbstractKMParameters kmParameters) 
        throws InvalidCriterionException, GenesNotFoundInStudyException, InvalidSurvivalValueDefinitionException {
        AbstractKMPlotHandler kmPlotHandler = AbstractKMPlotHandler.createKMPlotHandler(
            subscription, getDao(), kmParameters.getSurvivalValueDefinition(), queryManagementService, kmParameters);
        kmPlotHandler.setupAndValidateParameters(this);
        return kmPlotHandler.createPlot(kmPlotService);

    }
    
    /**
     * {@inheritDoc}
     */
    public GeneExpressionPlotGroup createGeneExpressionPlot(StudySubscription studySubscription,
            AbstractGEPlotParameters plotParameters) 
    throws ControlSamplesNotMappedException, InvalidCriterionException, GenesNotFoundInStudyException {
        if (StringUtils.isNotBlank(plotParameters.getGeneSymbol())) {
            plotParameters.getGenesNotFound().clear();
            plotParameters.getGenesNotFound().addAll(validateGeneSymbols(studySubscription, 
                    Cai2Util.createListFromCommaDelimitedString(plotParameters.getGeneSymbol())));
        }
        AbstractGEPlotHandler gePlotHandler = AbstractGEPlotHandler.createGeneExpressionPlotHandler(
                getDao(), queryManagementService, plotParameters, gePlotService);
        
        return gePlotHandler.createPlots(studySubscription);
    }

    /**
     * {@inheritDoc}
     */
    public List<String> validateGeneSymbols(StudySubscription studySubscription, List<String> geneSymbols)
            throws GenesNotFoundInStudyException {
        return queryManagementService.validateGeneSymbols(studySubscription, geneSymbols);
    }
    
    private File createClassificationFile(StudySubscription studySubscription, List<Query> clinicalQueries,
            String clsFilename, List<String> sampleColumnOrdering) throws InvalidCriterionException {
        return fileManager.createClassificationFile(studySubscription, 
                GenePatternUtil.createSampleClassification(
                        queryManagementService, clinicalQueries, sampleColumnOrdering), 
                clsFilename);
    }
    
    private File createGctFile(GctDataset gctDataset, StudySubscription studySubscription, String filename)
    throws InvalidCriterionException {
        return fileManager.createGctFile(studySubscription, gctDataset, filename);
    }

    private GctDataset createGctDataset(StudySubscription studySubscription, Collection<Query> querySet,
            String platformName, SampleSet excludedSet) throws InvalidCriterionException {
        SampleSet refreshedExcludedSet = excludedSet == null ? null : getRefreshedEntity(excludedSet);
        return GenePatternUtil.createGctDataset(studySubscription, querySet,
                refreshedExcludedSet, queryManagementService, platformName);
    }
    
    /**
     * {@inheritDoc}
     */
    public StudySubscription getRefreshedStudySubscription(StudySubscription studySubscription) {
        StudySubscription refreshedStudySubscription = getRefreshedEntity(studySubscription);
        HibernateUtil.loadCollection(refreshedStudySubscription);
        return refreshedStudySubscription;
    }

    /**
     * @return the kmPlotService
     */
    public KMPlotService getKmPlotService() {
        return kmPlotService;
    }

    /**
     * @param kmPlotService the kmPlotService to set
     */
    public void setKmPlotService(KMPlotService kmPlotService) {
        this.kmPlotService = kmPlotService;
    }

    /**
     * @return the queryManagementService
     */
    public QueryManagementService getQueryManagementService() {
        return queryManagementService;
    }

    /**
     * @param queryManagementService the queryManagementService to set
     */
    public void setQueryManagementService(QueryManagementService queryManagementService) {
        this.queryManagementService = queryManagementService;
    }

    /**
     * @return the gePlotService
     */
    public GeneExpressionPlotService getGePlotService() {
        return gePlotService;
    }

    /**
     * @param gePlotService the gePlotService to set
     */
    public void setGePlotService(GeneExpressionPlotService gePlotService) {
        this.gePlotService = gePlotService;
    }

    /**
     * @return the genePatternClientFactory
     */
    public GenePatternClientFactory getGenePatternClientFactory() {
        return genePatternClientFactory;
    }

    /**
     * @param genePatternClientFactory the genePatternClientFactory to set
     */
    public void setGenePatternClientFactory(GenePatternClientFactory genePatternClientFactory) {
        this.genePatternClientFactory = genePatternClientFactory;
    }

    /**
     * @return the genePatternGridRunner
     */
    public GenePatternGridRunner getGenePatternGridRunner() {
        return genePatternGridRunner;
    }

    /**
     * @param genePatternGridRunner the genePatternGridRunner to set
     */
    public void setGenePatternGridRunner(GenePatternGridRunner genePatternGridRunner) {
        this.genePatternGridRunner = genePatternGridRunner;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = false)
    public void deleteAnalysisJob(Long jobId) {
        AbstractPersistedAnalysisJob job = getAnalysisJob(jobId);
        job.getSubscription().getAnalysisJobCollection().remove(job);
        deleteFiles(job);
        getDao().delete(job);
    }
    
    private void deleteFiles(AbstractPersistedAnalysisJob job) {
        if (job.getResultsZipFile() != null) {
            FileUtils.deleteQuietly(job.getResultsZipFile().getFile());
        }
        if (job.getInputZipFile() != null) {
            FileUtils.deleteQuietly(job.getInputZipFile().getFile());
        }
    }

    private AbstractPersistedAnalysisJob getAnalysisJob(Long id) {
        return getDao().get(id, AbstractPersistedAnalysisJob.class);
    }
    
    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = false)
    public void deleteGisticAnalysis(GisticAnalysis gisticAnalysis) {
        StudySubscription studySubscription = gisticAnalysis.getStudySubscription();
        if (gisticAnalysis.getReporterList() != null) {
            getArrayDataService().deleteGisticAnalysisNetCDFFile(studySubscription.getStudy(), 
                    gisticAnalysis.getReporterList().getId());
            for (ArrayData arrayData : gisticAnalysis.getReporterList().getArrayDatas()) {
                for (Sample sample : arrayData.getArray().getSampleCollection()) {
                    sample.getArrayCollection().remove(arrayData.getArray());
                    sample.getArrayDataCollection().remove(arrayData);
                }
                getDao().delete(arrayData.getArray());
            }
        }
        studySubscription.getCopyNumberAnalysisCollection().remove(gisticAnalysis);
        getDao().delete(gisticAnalysis);
        getDao().save(studySubscription);
    }

    /**
     * @return the fileManager
     */
    public FileManager getFileManager() {
        return fileManager;
    }

    /**
     * @param fileManager the fileManager to set
     */
    public void setFileManager(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    /**
     * @return the arrayDataService
     */
    public ArrayDataService getArrayDataService() {
        return arrayDataService;
    }

    /**
     * @param arrayDataService the arrayDataService to set
     */
    public void setArrayDataService(ArrayDataService arrayDataService) {
        this.arrayDataService = arrayDataService;
    }
}
