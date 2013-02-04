/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.study.deployment;

import static gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValueType.EXPRESSION_SIGNAL;
import edu.mit.broad.genepattern.gp.services.CaIntegrator2GPClient;
import gov.nih.nci.caintegrator2.application.analysis.GenePatternClientFactory;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataLoadingTypeEnum;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataService;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValueType;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValues;
import gov.nih.nci.caintegrator2.application.arraydata.PlatformHelper;
import gov.nih.nci.caintegrator2.application.study.DnaAnalysisDataConfiguration;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.Status;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.application.study.ValidationException;
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayDataType;
import gov.nih.nci.caintegrator2.domain.genomic.DnaAnalysisData;
import gov.nih.nci.caintegrator2.domain.genomic.DnaAnalysisReporter;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.domain.genomic.Sample;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.DataRetrievalException;
import gov.nih.nci.caintegrator2.external.bioconductor.BioconductorService;
import gov.nih.nci.caintegrator2.external.caarray.CaArrayFacade;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;
import org.genepattern.webservice.WebServiceException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Helper class that retrieves data from caArray and loads it into a study.
 */
@Transactional (propagation = Propagation.REQUIRED)
class GenomicDataHelper {

    private static final double FIFTIETH_PERCENTILE = 50;
    private final CaArrayFacade caArrayFacade;
    private final ArrayDataService arrayDataService;
    private final CaIntegrator2Dao dao;
    private final BioconductorService bioconductorService;
    private final DnaAnalysisHandlerFactory dnaAnalysisHandlerFactory;
    private ExpressionHandlerFactory expressionHandlerFactory;
    private GenePatternClientFactory genePatternClientFactory;

    GenomicDataHelper(CaArrayFacade caArrayFacade, ArrayDataService arrayDataService, CaIntegrator2Dao dao,
            BioconductorService bioconductorService, DnaAnalysisHandlerFactory dnaAnalysisHandlerFactory) {
        this.caArrayFacade = caArrayFacade;
        this.arrayDataService = arrayDataService;
        this.dao = dao;
        this.bioconductorService = bioconductorService;
        this.dnaAnalysisHandlerFactory = dnaAnalysisHandlerFactory;
    }

    void loadData(StudyConfiguration studyConfiguration) 
    throws ConnectionException, DataRetrievalException, ValidationException, IOException {
        for (GenomicDataSourceConfiguration genomicSource : studyConfiguration.getGenomicDataSources()) {
            if (!Status.LOADED.equals(genomicSource.getStatus())) {
                if (genomicSource.isExpressionData()) {
                    loadExpressionData(genomicSource);
                } else {
                    loadDnaAnalysisData(genomicSource);
                }
                genomicSource.setStatus(Status.LOADED);
                dao.save(genomicSource);
            }
        }
    }
    
    private void loadDnaAnalysisData(GenomicDataSourceConfiguration genomicSource)
    throws DataRetrievalException, ConnectionException, ValidationException, IOException {
        if (genomicSource.getDnaAnalysisDataConfiguration() != null) {
            if (ArrayDataLoadingTypeEnum.PARSED_DATA.equals(genomicSource.getLoadingType())) {
                handleParsedDnaAnalysisData(genomicSource);
            } else {
                handleDnaAnalysisData(genomicSource);
            }
        }
    }
    
    private void loadExpressionData(GenomicDataSourceConfiguration genomicSource)
    throws ConnectionException, DataRetrievalException, ValidationException, IOException {
        if (!genomicSource.getSamples().isEmpty()) {
            ArrayDataValues probeSetValues;
            if (ArrayDataLoadingTypeEnum.PARSED_DATA.equals(genomicSource.getLoadingType())) {
                probeSetValues = caArrayFacade.retrieveData(genomicSource);
            } else {
                AbstractExpressionMappingFileHandler handler = expressionHandlerFactory.getHandler(
                        genomicSource, caArrayFacade, arrayDataService, dao);
                probeSetValues = handler.loadArrayData();
            } 
            ArrayDataValues geneValues = createGeneArrayDataValues(probeSetValues);
            arrayDataService.save(probeSetValues);
            arrayDataService.save(geneValues);
        }
    }

    private void handleParsedDnaAnalysisData(GenomicDataSourceConfiguration genomicSource) 
    throws DataRetrievalException, ConnectionException, ValidationException, IOException {
        List<ArrayDataValues> arrayValuesList = caArrayFacade.retrieveDnaAnalysisData(genomicSource, arrayDataService);
        computeSegmentationData(genomicSource, arrayValuesList);
    }

    private void handleDnaAnalysisData(GenomicDataSourceConfiguration genomicSource) 
    throws DataRetrievalException, ConnectionException, ValidationException, IOException {
        AbstractUnparsedSupplementalMappingFileHandler handler = 
            dnaAnalysisHandlerFactory.getHandler(genomicSource, caArrayFacade, arrayDataService, dao);
        List<ArrayDataValues> valuesList = handler.loadArrayData();
        computeSegmentationData(genomicSource, valuesList);
    }

    private void computeSegmentationData(GenomicDataSourceConfiguration genomicSource, List<ArrayDataValues> valuesList)
            throws ConnectionException, DataRetrievalException {
        if (genomicSource.getDnaAnalysisDataConfiguration().isCaDNACopyConfiguration()
                || genomicSource.getDnaAnalysisDataConfiguration().isCaCGHCallConfiguration()) {
            for (ArrayDataValues values : valuesList) {
                DnaAnalysisData dnaAnalysisData = createDnaAnalysisData(values);
                retrieveSegmentationData(dnaAnalysisData, genomicSource.getDnaAnalysisDataConfiguration());
            }            
        } else {
            DnaAnalysisData dnaAnalysisData = createDnaAnalysisData(valuesList);
            retrieveSegmentationData(dnaAnalysisData, genomicSource.getDnaAnalysisDataConfiguration());
        }
    }

    private DnaAnalysisData createDnaAnalysisData(List<ArrayDataValues> valuesList) {
        List<DnaAnalysisReporter> reporters;
        if (valuesList.isEmpty()) {
            reporters = Collections.emptyList();
        } else {
            reporters = convertToDnaAnalysisReporters(valuesList.get(0).getReporterList());
        }
        DnaAnalysisData dnaAnalysisData = new DnaAnalysisData(reporters);
        for (ArrayDataValues values : valuesList) {
            addValues(values, dnaAnalysisData);
        }
        return dnaAnalysisData;
    }

    private DnaAnalysisData createDnaAnalysisData(ArrayDataValues values) {
        DnaAnalysisData dnaAnalysisData = new DnaAnalysisData(convertToDnaAnalysisReporters(values.getReporterList()));
        addValues(values, dnaAnalysisData);
        return dnaAnalysisData;
    }

    private void addValues(ArrayDataValues values, DnaAnalysisData dnaAnalysisData) {
        for (ArrayData arrayData : values.getArrayDatas()) {
            dnaAnalysisData.addDnaAnalysisData(arrayData, 
                    values.getFloatValues(arrayData, ArrayDataValueType.DNA_ANALYSIS_LOG2_RATIO));
        }
    }

    private void retrieveSegmentationData(DnaAnalysisData dnaAnalysisData,
            DnaAnalysisDataConfiguration configuration) throws ConnectionException, DataRetrievalException {
        dao.runSessionKeepAlive();
        if (configuration.isCaDNACopyConfiguration() || configuration.isCaCGHCallConfiguration()) {
            bioconductorService.addSegmentationData(dnaAnalysisData, configuration);
        } else {
            CaIntegrator2GPClient client;
            try {
                client = getGenePatternClientFactory().retrieveClient(configuration.getSegmentationService());
                GladSegmentationHandler gladHandler = new GladSegmentationHandler(client);
                gladHandler.addSegmentationData(dnaAnalysisData);
            } catch (WebServiceException e) {
                throw new ConnectionException(e.getMessage(), e);
            }
        }
    }

    @SuppressWarnings({ "unchecked", "PMD.UnnecessaryLocalBeforeReturn" })  // for efficient conversion of List.
    private List<DnaAnalysisReporter> convertToDnaAnalysisReporters(ReporterList reporterList) {
        List reporters = reporterList.getReporters();
        return reporters;
    }

    private ArrayDataValues createGeneArrayDataValues(ArrayDataValues probeSetValues) {
        PlatformHelper platformHelper = 
            new PlatformHelper(probeSetValues.getReporterList().getPlatform());
        Set<ReporterList> reporterLists = platformHelper.getReporterLists(ReporterTypeEnum.GENE_EXPRESSION_GENE);
        ArrayDataValues geneValues = new ArrayDataValues(platformHelper.getAllReportersByType(
                                                         ReporterTypeEnum.GENE_EXPRESSION_GENE));
        for (ArrayData arrayData : probeSetValues.getArrayDatas()) {
            loadGeneArrayDataValues(geneValues, probeSetValues, arrayData, platformHelper, reporterLists);
        }
        return geneValues;
    }

    private void loadGeneArrayDataValues(ArrayDataValues geneValues, ArrayDataValues probeSetValues, 
            ArrayData arrayData,
            PlatformHelper platformHelper, Set<ReporterList> reporterLists) {
        ArrayData geneArrayData = createGeneArrayData(arrayData, reporterLists);
        loadGeneValues(geneValues, probeSetValues, arrayData, geneArrayData, platformHelper);
    }

    private void loadGeneValues(ArrayDataValues geneValues, ArrayDataValues probeSetValues, ArrayData arrayData,
            ArrayData geneArrayData, PlatformHelper platformHelper) {
        for (AbstractReporter geneReporter 
                : platformHelper.getAllReportersByType(ReporterTypeEnum.GENE_EXPRESSION_GENE)) {
            Collection<AbstractReporter> probeSetReporters = 
                platformHelper.getReportersForGene(geneReporter.getGenes().iterator().next(), 
                        ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET);
            geneValues.setFloatValue(geneArrayData, geneReporter, ArrayDataValueType.EXPRESSION_SIGNAL,
                    computeGeneReporterValue(probeSetReporters, probeSetValues, arrayData, geneReporter));
        }
    }

    private float computeGeneReporterValue(Collection<AbstractReporter> probeSetReporters, 
            ArrayDataValues probeSetValues, ArrayData arrayData, AbstractReporter geneReporter) {
        Sample sample = arrayData.getSample();
        DescriptiveStatistics statistics = new DescriptiveStatistics();
        for (AbstractReporter reporter : probeSetReporters) {
            statistics.addValue(probeSetValues.getFloatValue(arrayData, reporter, EXPRESSION_SIGNAL));
            if (reporter.getSamplesHighVariance().contains(sample)) {
                geneReporter.getSamplesHighVariance().add(sample);
                sample.getReportersHighVariance().add(geneReporter);
            }
        }
        return (float) statistics.getPercentile(FIFTIETH_PERCENTILE);
    }

    private ArrayData createGeneArrayData(ArrayData probeSetArrayData, Set<ReporterList> reporterLists) {
        ArrayData arrayData = new ArrayData();
        arrayData.setType(ArrayDataType.GENE_EXPRESSION);
        arrayData.setStudy(probeSetArrayData.getStudy());
        arrayData.setArray(probeSetArrayData.getArray());
        arrayData.setSample(probeSetArrayData.getSample());
        arrayData.getSample().getArrayDataCollection().add(arrayData);
        if (!reporterLists.isEmpty()) {
            arrayData.getReporterLists().addAll(reporterLists);
            for (ReporterList reporterList : reporterLists) {
                reporterList.getArrayDatas().add(arrayData);
            }
        }
        dao.save(arrayData);
        return arrayData;
    }

    GenePatternClientFactory getGenePatternClientFactory() {
        return genePatternClientFactory;
    }

    void setGenePatternClientFactory(GenePatternClientFactory genePatternClientFactory) {
        this.genePatternClientFactory = genePatternClientFactory;
    }

    /**
     * @return the expressionHandlerFactory
     */
    public ExpressionHandlerFactory getExpressionHandlerFactory() {
        return expressionHandlerFactory;
    }

    /**
     * @param expressionHandlerFactory the expressionHandlerFactory to set
     */
    public void setExpressionHandlerFactory(ExpressionHandlerFactory expressionHandlerFactory) {
        this.expressionHandlerFactory = expressionHandlerFactory;
    }

}
