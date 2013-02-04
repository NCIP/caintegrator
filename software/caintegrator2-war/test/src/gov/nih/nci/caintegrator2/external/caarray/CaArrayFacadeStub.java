/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.external.caarray;

import gov.nih.nci.caarray.external.v1_0.data.File;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataService;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValueType;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValues;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator2.domain.genomic.Array;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayDataType;
import gov.nih.nci.caintegrator2.domain.genomic.GeneExpressionReporter;
import gov.nih.nci.caintegrator2.domain.genomic.Platform;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.domain.genomic.Sample;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.ServerConnectionProfile;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CaArrayFacadeStub implements CaArrayFacade {
    
    public boolean retrieveDataCalled = false;
    
    public void reset() {
        retrieveDataCalled = false;
    }

    /**
     * {@inheritDoc}
     */
    public List<Sample> getSamples(String experimentIdentifier, ServerConnectionProfile profile)
            throws ConnectionException {
        List<Sample> samples = new ArrayList<Sample>();
        samples.add(new Sample());
        return samples;
    }

    /**
     * {@inheritDoc}
     */
    public ArrayDataValues retrieveData(GenomicDataSourceConfiguration genomicSource) throws ConnectionException {
        retrieveDataCalled = true;
        List<AbstractReporter> reporters = new ArrayList<AbstractReporter>();
        GeneExpressionReporter reporter = new GeneExpressionReporter();
        reporters.add(reporter);
        ArrayDataValues values = new ArrayDataValues(reporters);
        Platform platform = new Platform();
        ReporterList reporterList = platform.addReporterList("reporterList", ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET);
        reporter.setReporterList(reporterList);
        reporterList.getReporters().addAll(reporters);
        platform.addReporterList("reporterList2", ReporterTypeEnum.GENE_EXPRESSION_GENE);
        for (Sample sample : genomicSource.getSamples()) {
            addExpressionArrayData(sample, platform, reporterList, values);
        }
        return values;
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

    /**
     * {@inheritDoc}
     */
    public List<ArrayDataValues> retrieveDnaAnalysisData(GenomicDataSourceConfiguration genomicSource, ArrayDataService arrayDataService) 
    throws ConnectionException {
        retrieveDataCalled = true;
        List<ArrayDataValues> valuesList = new ArrayList<ArrayDataValues>();
        List<AbstractReporter> reporters = new ArrayList<AbstractReporter>();
        GeneExpressionReporter reporter = new GeneExpressionReporter();
        reporters.add(reporter);
        ArrayDataValues values = new ArrayDataValues(reporters);
        Platform platform = new Platform();
        ReporterList reporterList = platform.addReporterList("reporterList", ReporterTypeEnum.DNA_ANALYSIS_REPORTER);
        reporter.setReporterList(reporterList);
        reporterList.getReporters().addAll(reporters);
        for (Sample sample : genomicSource.getSamples()) {
            addDnaAnalysisArrayData(sample, platform, reporterList, values);
        }
        valuesList.add(values);
        return valuesList;
    }

    private void addDnaAnalysisArrayData(Sample sample, Platform platform, ReporterList reporterList, ArrayDataValues values) {
        Array array = new Array();
        array.setPlatform(platform);
        array.setName(sample.getName());
        array.getSampleCollection().add(sample);
        ArrayData arrayData = new ArrayData();
        arrayData.setType(ArrayDataType.COPY_NUMBER);
        arrayData.setArray(array);
        array.getArrayDataCollection().add(arrayData);
        arrayData.setSample(sample);
        arrayData.getReporterLists().add(reporterList);
        sample.getArrayCollection().add(array);
        sample.getArrayDataCollection().add(arrayData);
        values.setFloatValues(arrayData, reporterList.getReporters(), ArrayDataValueType.DNA_ANALYSIS_LOG2_RATIO, new float[reporterList.getReporters().size()]);
    }

    /**
     * {@inheritDoc}
     */
    public byte[] retrieveFile(GenomicDataSourceConfiguration source, String filename) {
        return new byte[0];
    }

    public void validateGenomicSourceConnection(GenomicDataSourceConfiguration genomicSource)
            throws ConnectionException, ExperimentNotFoundException {
    }


    public List<File> retrieveFilesForGenomicSource(GenomicDataSourceConfiguration genomicSource)
            throws ConnectionException, FileNotFoundException {
        return Collections.emptyList();
    }

}
