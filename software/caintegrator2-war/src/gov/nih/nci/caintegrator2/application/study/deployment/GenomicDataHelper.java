/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.study.deployment;

import static gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValueType.EXPRESSION_SIGNAL;
import edu.mit.broad.genepattern.gp.services.GenePatternClient;
import gov.nih.nci.caintegrator2.application.analysis.GenePatternClientFactory;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataService;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValueType;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValues;
import gov.nih.nci.caintegrator2.application.arraydata.PlatformHelper;
import gov.nih.nci.caintegrator2.application.study.CopyNumberDataConfiguration;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.application.study.ValidationException;
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayDataType;
import gov.nih.nci.caintegrator2.domain.genomic.CopyNumberData;
import gov.nih.nci.caintegrator2.domain.genomic.DnaAnalysisReporter;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.DataRetrievalException;
import gov.nih.nci.caintegrator2.external.bioconductor.BioconductorService;
import gov.nih.nci.caintegrator2.external.caarray.CaArrayFacade;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;

/**
 * Helper class that retrieves data from caArray and loads it into a study.
 */
class GenomicDataHelper {

    private static final double FIFTIETH_PERCENTILE = 50;
    private final CaArrayFacade caArrayFacade;
    private final ArrayDataService arrayDataService;
    private final CaIntegrator2Dao dao;
    private final BioconductorService bioconductorService;
    private final CopyNumberHandlerFactory copyNumberHandlerFactory;
    private GenePatternClientFactory genePatternClientFactory;

    GenomicDataHelper(CaArrayFacade caArrayFacade, ArrayDataService arrayDataService, CaIntegrator2Dao dao,
            BioconductorService bioconductorService, CopyNumberHandlerFactory copyNumberHandlerFactory) {
        this.caArrayFacade = caArrayFacade;
        this.arrayDataService = arrayDataService;
        this.dao = dao;
        this.bioconductorService = bioconductorService;
        this.copyNumberHandlerFactory = copyNumberHandlerFactory;
    }

    void loadData(StudyConfiguration studyConfiguration) 
    throws ConnectionException, DataRetrievalException, ValidationException {
        for (GenomicDataSourceConfiguration genomicSource : studyConfiguration.getGenomicDataSources()) {
            if (genomicSource.isCopyNumberData()) {
                loadCopyNumberData(genomicSource);
            } else if (genomicSource.isExpressionData()) {
                loadExpressionData(genomicSource);
            }
        }
    }
    
    private void loadCopyNumberData(GenomicDataSourceConfiguration genomicSource)
    throws DataRetrievalException, ConnectionException, ValidationException {
        if (genomicSource.getCopyNumberDataConfiguration() != null) {
                handleCopyNumberData(genomicSource);
        }
    }
    
    private void loadExpressionData(GenomicDataSourceConfiguration genomicSource)
    throws ConnectionException, DataRetrievalException {
        if (!genomicSource.getSamples().isEmpty()) {
            ArrayDataValues probeSetValues = caArrayFacade.retrieveData(genomicSource);
            ArrayDataValues geneValues = createGeneArrayDataValues(probeSetValues);
            arrayDataService.save(probeSetValues);
            arrayDataService.save(geneValues);
        }
    }

    private void handleCopyNumberData(GenomicDataSourceConfiguration genomicSource) 
    throws DataRetrievalException, ConnectionException, ValidationException {
        AbstractCopyNumberMappingFileHandler handler = 
            copyNumberHandlerFactory.getHandler(genomicSource, caArrayFacade, arrayDataService, dao);
        List<ArrayDataValues> valuesList = handler.loadCopyNumberData();
        if (genomicSource.getCopyNumberDataConfiguration().isCaDNACopyConfiguration()) {
            for (ArrayDataValues values : valuesList) {
                CopyNumberData copyNumberData = createCopyNumberData(values);
                retrieveSegmentationData(copyNumberData, genomicSource.getCopyNumberDataConfiguration());
            }            
        } else {
            CopyNumberData copyNumberData = createCopyNumberData(valuesList);
            retrieveSegmentationData(copyNumberData, genomicSource.getCopyNumberDataConfiguration());
        }
    }

    private CopyNumberData createCopyNumberData(List<ArrayDataValues> valuesList) {
        List<DnaAnalysisReporter> reporters;
        if (valuesList.isEmpty()) {
            reporters = Collections.emptyList();
        } else {
            reporters = convertToDnaAnalysisReporters(valuesList.get(0).getReporterList());
        }
        CopyNumberData copyNumberData = new CopyNumberData(reporters);
        for (ArrayDataValues values : valuesList) {
            addValues(values, copyNumberData);
        }
        return copyNumberData;
    }

    private CopyNumberData createCopyNumberData(ArrayDataValues values) {
        CopyNumberData copyNumberData = new CopyNumberData(convertToDnaAnalysisReporters(values.getReporterList()));
        addValues(values, copyNumberData);
        return copyNumberData;
    }

    private void addValues(ArrayDataValues values, CopyNumberData copyNumberData) {
        for (ArrayData arrayData : values.getArrayDatas()) {
            copyNumberData.addCopyNumberData(arrayData, 
                    values.getFloatValues(arrayData, ArrayDataValueType.COPY_NUMBER_LOG2_RATIO));
        }
    }

    private void retrieveSegmentationData(CopyNumberData copyNumberData,
            CopyNumberDataConfiguration configuration) throws ConnectionException, DataRetrievalException {
        if (configuration.isCaDNACopyConfiguration()) {
            bioconductorService.addSegmentationData(copyNumberData, configuration);
        } else {
            GenePatternClient client = 
                getGenePatternClientFactory().retrieveClient(configuration.getSegmentationService());
            GladSegmentationHandler gladHandler = new GladSegmentationHandler(client);
            gladHandler.addSegmentationData(copyNumberData);
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
                    computeGeneReporterValue(probeSetReporters, probeSetValues, arrayData));
        }
    }

    private float computeGeneReporterValue(Collection<AbstractReporter> probeSetReporters, 
            ArrayDataValues probeSetValues, ArrayData arrayData) {
        DescriptiveStatistics statistics = new DescriptiveStatistics();
        for (AbstractReporter reporter : probeSetReporters) {
            statistics.addValue(probeSetValues.getFloatValue(arrayData, reporter, EXPRESSION_SIGNAL));
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

}
