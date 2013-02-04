/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.study.deployment;

import gov.nih.nci.caintegrator.TestDataFiles;
import gov.nih.nci.caintegrator.application.arraydata.ArrayDataService;
import gov.nih.nci.caintegrator.application.arraydata.ArrayDataValueType;
import gov.nih.nci.caintegrator.application.arraydata.ArrayDataValues;
import gov.nih.nci.caintegrator.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator.application.study.ValidationException;
import gov.nih.nci.caintegrator.application.study.deployment.ExpressionMultiSamplePerFileMappingFileHandler;
import gov.nih.nci.caintegrator.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator.domain.genomic.Array;
import gov.nih.nci.caintegrator.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator.domain.genomic.ArrayDataType;
import gov.nih.nci.caintegrator.domain.genomic.GeneExpressionReporter;
import gov.nih.nci.caintegrator.domain.genomic.Platform;
import gov.nih.nci.caintegrator.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator.domain.genomic.Sample;
import gov.nih.nci.caintegrator.external.ConnectionException;
import gov.nih.nci.caintegrator.external.DataRetrievalException;
import gov.nih.nci.caintegrator.external.caarray.CaArrayFacade;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Loads copy number data from locally-available CNCHP files (as opposed to retrieval from caArray) 
 * for quicker testing.
 */
class LocalExpressionMultiSampleHandler extends ExpressionMultiSamplePerFileMappingFileHandler {

    LocalExpressionMultiSampleHandler(GenomicDataSourceConfiguration genomicSource, CaArrayFacade caArrayFacade,
            ArrayDataService arrayDataService, CaIntegrator2Dao dao) {
        super(genomicSource, caArrayFacade, arrayDataService, dao);
    }

    @Override
    protected void doneWithFile(File cnchpFile) {
        // no-op: don't delete
    }

    @Override
    File getDataFile(String dataFilename) throws ConnectionException, DataRetrievalException,
            ValidationException {
        return TestDataFiles.getAgilentDataFile(dataFilename);
    }
    
    @Override
    protected void loadArrayDataValues(Map<String, List<Float>> agilentDataMap, ArrayData arrayData) {
        List<AbstractReporter> reporters = new ArrayList<AbstractReporter>();
        GeneExpressionReporter reporter = new GeneExpressionReporter();
        reporters.add(reporter);
        setArrayDataValues(new ArrayDataValues(reporters));
        Platform platform = new Platform();
        ReporterList reporterList = platform.addReporterList("reporterList", ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET);
        reporter.setReporterList(reporterList);
        reporterList.getReporters().addAll(reporters);
        platform.addReporterList("reporterList2", ReporterTypeEnum.GENE_EXPRESSION_GENE);
        for (Sample sample : getGenomicSource().getSamples()) {
            addExpressionArrayData(sample, platform, reporterList, getArrayDataValues());
        }
    }

    private void addExpressionArrayData(Sample sample, Platform platform, ReporterList reporterList, ArrayDataValues values) {
        Array array = new Array();
        array.setPlatform(platform);
        array.setName(sample.getName());
        array.getSampleCollection().add(sample);
        ArrayData arrayData = new ArrayData();
        arrayData.setType(ArrayDataType.GENE_EXPRESSION);
        arrayData.setArray(array);
        array.getArrayDataCollection().add(arrayData);
        arrayData.setSample(sample);
        arrayData.getReporterLists().add(reporterList);
        sample.getArrayCollection().add(array);
        sample.getArrayDataCollection().add(arrayData);
        values.setFloatValues(arrayData, reporterList.getReporters(), ArrayDataValueType.EXPRESSION_SIGNAL, new float[reporterList.getReporters().size()]);
    }
    
}
