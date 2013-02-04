/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.arraydata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator2.domain.genomic.GeneExpressionReporter;
import gov.nih.nci.caintegrator2.domain.genomic.GisticGenomicRegionReporter;
import gov.nih.nci.caintegrator2.domain.genomic.Platform;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.domain.genomic.Sample;
import gov.nih.nci.caintegrator2.domain.genomic.SampleAcquisition;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.domain.translational.StudySubjectAssignment;
import gov.nih.nci.caintegrator2.file.FileManagerStub;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Test;

public class NetCDFManagerTest {
    
    private FileManagerStub fileManagerStub = new FileManagerStub();
    private NetCDFManager manager = new NetCDFManager(fileManagerStub);
    private long nextArrayDataId = 0L;
    
    @After
    public void tearDown() {
        File testFile = new File(fileManagerStub.getStudyDirectory(null), "data1.nc");
        if (testFile.exists()) {
            testFile.delete();
        }
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testStoreValuesEmptyArrayDatas() {
        ArrayDataValues values = new ArrayDataValues(new ArrayList<AbstractReporter>());
        manager.storeValues(values);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testStoreValuesMultipleStudies() {
        List<AbstractReporter> reporters = new ArrayList<AbstractReporter>();
        GeneExpressionReporter reporter = new GeneExpressionReporter();
        reporter.setReporterList(createReporterList());
        reporters.add(reporter);
        ArrayDataValues values = new ArrayDataValues(reporters);
        ArrayData arrayData1 = createArrayData(new Study());
        ArrayData arrayData2 = createArrayData(new Study());
        values.setFloatValue(arrayData1, reporter, ArrayDataValueType.EXPRESSION_SIGNAL, 0.0f);
        values.setFloatValue(arrayData2, reporter, ArrayDataValueType.EXPRESSION_SIGNAL, 0.0f);
        manager.storeValues(values);
    }

    /**
     * @return
     */
    private ReporterList createReporterList() {
        Platform platform = new Platform();
        ReporterList reporterList = platform.addReporterList("reporterList", ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET);
        return reporterList;
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testStoreValuesNullArrayDataIds() {
        List<AbstractReporter> reporters = new ArrayList<AbstractReporter>();
        GeneExpressionReporter reporter = new GeneExpressionReporter();
        ReporterList reporterList = createReporterList();
        reporter.setReporterList(reporterList);
        reporterList.setId(1L);
        reporters.add(reporter);
        ArrayDataValues values = new ArrayDataValues(reporters);
        Study study = new Study();
        study.setId(1L);
        ArrayData arrayData = createArrayData(study);
        arrayData.getReporterLists().add(reporterList);
        arrayData.setId(null);
        values.setFloatValue(arrayData, reporter, ArrayDataValueType.EXPRESSION_SIGNAL, 0.0f);
        manager.storeValues(values);
    }
    
    @Test
    public void testStoreValuesMultipleReporterLists() {
        Platform platform = new Platform();
        platform.setId(1l);
        List<AbstractReporter> reporters = new ArrayList<AbstractReporter>();
        
        // Unrelated, unused ReporterList to test that it is ignored properly
        GeneExpressionReporter reporter0 = new GeneExpressionReporter();
        ReporterList reporterList0 = platform.addReporterList("reporterList0", ReporterTypeEnum.GENE_EXPRESSION_GENE);
        reporterList0.setId(0L);
        reporterList0.getReporters().add(reporter0);
        reporter0.setReporterList(reporterList0);
        reporter0.setIndex(0);

        GeneExpressionReporter reporter1 = new GeneExpressionReporter();
        ReporterList reporterList1 = platform.addReporterList("reporterList1", ReporterTypeEnum.DNA_ANALYSIS_REPORTER);
        reporterList1.setId(1L);
        reporterList1.getReporters().add(reporter1);
        reporter1.setReporterList(reporterList1);
        reporter1.setIndex(0);
        GeneExpressionReporter reporter2 = new GeneExpressionReporter();
        ReporterList reporterList2 = platform.addReporterList("reporterList2", ReporterTypeEnum.DNA_ANALYSIS_REPORTER);
        reporterList2.setId(2L);
        reporterList2.getReporters().add(reporter2);
        reporter2.setReporterList(reporterList2);
        reporter2.setIndex(0);
        reporters.add(reporter1);
        reporters.add(reporter2);
        ArrayDataValues values = new ArrayDataValues(reporters);
        ArrayData arrayData = createArrayData(new Study());
        arrayData.setId(1L);
        arrayData.getReporterLists().add(reporterList1);
        arrayData.getReporterLists().add(reporterList2);
        arrayData.getStudy().setId(1L);
        values.setFloatValue(arrayData, reporter1, ArrayDataValueType.EXPRESSION_SIGNAL, 1.1f);
        values.setFloatValue(arrayData, reporter2, ArrayDataValueType.EXPRESSION_SIGNAL, 2.2f);
        manager.storeValues(values);
        
        DataRetrievalRequest request = new DataRetrievalRequest();
        request.addArrayData(arrayData);
        request.addReporter(reporter1);
        request.addReporter(reporter2);
        request.addType(ArrayDataValueType.EXPRESSION_SIGNAL);
        ArrayDataValues retrieved = manager.retrieveValues(request);
        assertEquals(2, retrieved.getFloatValues(arrayData, ArrayDataValueType.EXPRESSION_SIGNAL).length);
        assertEquals(1.1f, retrieved.getFloatValue(arrayData, reporter1, ArrayDataValueType.EXPRESSION_SIGNAL), 0.0f);
        assertEquals(2.2f, retrieved.getFloatValue(arrayData, reporter2, ArrayDataValueType.EXPRESSION_SIGNAL), 0.0f);
    }
    
    @Test
    public void testStoreValues() throws IOException {
        Platform platform = new Platform();
        platform.setId(1l);
        List<AbstractReporter> reporters = new ArrayList<AbstractReporter>();
        GeneExpressionReporter reporter1 = new GeneExpressionReporter();
        ReporterList reporterList = platform.addReporterList("reporterList", ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET);
        reporterList.setId(1L);
        reporter1.setReporterList(reporterList);
        reporter1.setIndex(0);
        GeneExpressionReporter reporter2 = new GeneExpressionReporter();
        reporter2.setReporterList(reporterList);
        reporter2.setIndex(1);
        GeneExpressionReporter reporter3 = new GeneExpressionReporter();
        reporter3.setReporterList(reporterList);
        reporter3.setIndex(2);
        reporters.add(reporter1);
        reporters.add(reporter2);
        reporters.add(reporter3);
        ArrayDataValues values = new ArrayDataValues(reporters);
        Study study = new Study();
        study.setId(1L);
        ArrayData arrayData1 = createArrayData(study);
        arrayData1.getReporterLists().add(reporterList);
        ArrayData arrayData2 = createArrayData(study);
        arrayData2.getReporterLists().add(reporterList);
        
        // Store first set of values
        values.setFloatValue(arrayData1, reporter1, ArrayDataValueType.DNA_ANALYSIS_LOG2_RATIO, 1.1f);
        values.setFloatValue(arrayData1, reporter2, ArrayDataValueType.DNA_ANALYSIS_LOG2_RATIO, 2.2f);
        values.setFloatValue(arrayData1, reporter3, ArrayDataValueType.DNA_ANALYSIS_LOG2_RATIO, 3.3f);
        values.setFloatValue(arrayData2, reporter1, ArrayDataValueType.DNA_ANALYSIS_LOG2_RATIO, 4.4f);
        values.setFloatValue(arrayData2, reporter2, ArrayDataValueType.DNA_ANALYSIS_LOG2_RATIO, 5.5f);
        values.setFloatValue(arrayData2, reporter3, ArrayDataValueType.DNA_ANALYSIS_LOG2_RATIO, 6.6f);
        values.setFloatValue(arrayData1, reporter1, ArrayDataValueType.EXPRESSION_SIGNAL, 7.7f);
        values.setFloatValue(arrayData1, reporter2, ArrayDataValueType.EXPRESSION_SIGNAL, 8.8f);
        values.setFloatValue(arrayData1, reporter3, ArrayDataValueType.EXPRESSION_SIGNAL, 9.9f);
        values.setFloatValue(arrayData2, reporter1, ArrayDataValueType.EXPRESSION_SIGNAL, 10.10f);
        values.setFloatValue(arrayData2, reporter2, ArrayDataValueType.EXPRESSION_SIGNAL, 11.11f);
        values.setFloatValue(arrayData2, reporter3, ArrayDataValueType.EXPRESSION_SIGNAL, 12.12f);
        File netCdfFile = new File(fileManagerStub.getStudyDirectory(study), 
                "data" + platform.getId() + "_" + ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET.getValue() + ".nc");
        FileUtils.deleteQuietly(netCdfFile);
        manager.storeValues(values);
        assertTrue(netCdfFile.exists());
        // Store additional values
        values = new ArrayDataValues(reporters);
        ArrayData arrayData3 = createArrayData(study);
        arrayData3.getReporterLists().add(reporterList);
        values.setFloatValue(arrayData3, reporter1, ArrayDataValueType.DNA_ANALYSIS_LOG2_RATIO, 13.13f);
        values.setFloatValue(arrayData3, reporter2, ArrayDataValueType.DNA_ANALYSIS_LOG2_RATIO, 14.14f);
        values.setFloatValue(arrayData3, reporter3, ArrayDataValueType.DNA_ANALYSIS_LOG2_RATIO, 15.15f);
        values.setFloatValue(arrayData3, reporter1, ArrayDataValueType.EXPRESSION_SIGNAL, 16.16f);
        values.setFloatValue(arrayData3, reporter2, ArrayDataValueType.EXPRESSION_SIGNAL, 17.17f);
        values.setFloatValue(arrayData3, reporter3, ArrayDataValueType.EXPRESSION_SIGNAL, 18.18f);
        manager.storeValues(values);

        // Retrieve values and compare
        DataRetrievalRequest request = new DataRetrievalRequest();
        request.addArrayData(arrayData1);
        request.addArrayData(arrayData2);
        request.addArrayData(arrayData3);
        request.addType(ArrayDataValueType.DNA_ANALYSIS_LOG2_RATIO);
        request.addType(ArrayDataValueType.EXPRESSION_SIGNAL);
        request.addReporters(reporters);
        values = manager.retrieveValues(request);
        assertEquals(3, values.getArrayDatas().size());
        assertEquals(3, values.getReporters().size());
        assertEquals(2, values.getTypes().size());
        assertEquals(1.1f, values.getFloatValue(arrayData1, reporter1, ArrayDataValueType.DNA_ANALYSIS_LOG2_RATIO), 0.0f);
        assertEquals(2.2f, values.getFloatValue(arrayData1, reporter2, ArrayDataValueType.DNA_ANALYSIS_LOG2_RATIO), 0.0f);
        assertEquals(3.3f, values.getFloatValue(arrayData1, reporter3, ArrayDataValueType.DNA_ANALYSIS_LOG2_RATIO), 0.0f);
        assertEquals(4.4f, values.getFloatValue(arrayData2, reporter1, ArrayDataValueType.DNA_ANALYSIS_LOG2_RATIO), 0.0f);
        assertEquals(5.5f, values.getFloatValue(arrayData2, reporter2, ArrayDataValueType.DNA_ANALYSIS_LOG2_RATIO), 0.0f);
        assertEquals(6.6f, values.getFloatValue(arrayData2, reporter3, ArrayDataValueType.DNA_ANALYSIS_LOG2_RATIO), 0.0f);
        assertEquals(7.7f, values.getFloatValue(arrayData1, reporter1, ArrayDataValueType.EXPRESSION_SIGNAL), 0.0f);
        assertEquals(8.8f, values.getFloatValue(arrayData1, reporter2, ArrayDataValueType.EXPRESSION_SIGNAL), 0.0f);
        assertEquals(9.9f, values.getFloatValue(arrayData1, reporter3, ArrayDataValueType.EXPRESSION_SIGNAL), 0.0f);
        assertEquals(10.10f, values.getFloatValue(arrayData2, reporter1, ArrayDataValueType.EXPRESSION_SIGNAL), 0.0f);
        assertEquals(11.11f, values.getFloatValue(arrayData2, reporter2, ArrayDataValueType.EXPRESSION_SIGNAL), 0.0f);
        assertEquals(12.12f, values.getFloatValue(arrayData2, reporter3, ArrayDataValueType.EXPRESSION_SIGNAL), 0.0f);
        assertEquals(13.13f, values.getFloatValue(arrayData3, reporter1, ArrayDataValueType.DNA_ANALYSIS_LOG2_RATIO), 0.0f);
        assertEquals(14.14f, values.getFloatValue(arrayData3, reporter2, ArrayDataValueType.DNA_ANALYSIS_LOG2_RATIO), 0.0f);
        assertEquals(15.15f, values.getFloatValue(arrayData3, reporter3, ArrayDataValueType.DNA_ANALYSIS_LOG2_RATIO), 0.0f);
        assertEquals(16.16f, values.getFloatValue(arrayData3, reporter1, ArrayDataValueType.EXPRESSION_SIGNAL), 0.0f);
        assertEquals(17.17f, values.getFloatValue(arrayData3, reporter2, ArrayDataValueType.EXPRESSION_SIGNAL), 0.0f);
        assertEquals(18.18f, values.getFloatValue(arrayData3, reporter3, ArrayDataValueType.EXPRESSION_SIGNAL), 0.0f);

        request = new DataRetrievalRequest();
        request.addArrayData(arrayData1);
        request.addArrayData(arrayData3);
        request.addType(ArrayDataValueType.DNA_ANALYSIS_LOG2_RATIO);
        request.addType(ArrayDataValueType.EXPRESSION_SIGNAL);
        request.addReporter(reporter1);
        request.addReporter(reporter3);
        values = manager.retrieveValues(request);
        assertEquals(2, values.getArrayDatas().size());
        assertEquals(2, values.getReporters().size());
        assertEquals(2, values.getTypes().size());
        assertEquals(1.1f, values.getFloatValue(arrayData1, reporter1, ArrayDataValueType.DNA_ANALYSIS_LOG2_RATIO), 0.0f);
        assertEquals(3.3f, values.getFloatValue(arrayData1, reporter3, ArrayDataValueType.DNA_ANALYSIS_LOG2_RATIO), 0.0f);
        assertEquals(7.7f, values.getFloatValue(arrayData1, reporter1, ArrayDataValueType.EXPRESSION_SIGNAL), 0.0f);
        assertEquals(9.9f, values.getFloatValue(arrayData1, reporter3, ArrayDataValueType.EXPRESSION_SIGNAL), 0.0f);
        assertEquals(13.13f, values.getFloatValue(arrayData3, reporter1, ArrayDataValueType.DNA_ANALYSIS_LOG2_RATIO), 0.0f);
        assertEquals(15.15f, values.getFloatValue(arrayData3, reporter3, ArrayDataValueType.DNA_ANALYSIS_LOG2_RATIO), 0.0f);
        assertEquals(16.16f, values.getFloatValue(arrayData3, reporter1, ArrayDataValueType.EXPRESSION_SIGNAL), 0.0f);
        assertEquals(18.18f, values.getFloatValue(arrayData3, reporter3, ArrayDataValueType.EXPRESSION_SIGNAL), 0.0f);
        FileUtils.deleteQuietly(netCdfFile);
    }
    
    @Test
    public void testStoreGisticValues() throws IOException {
        List<AbstractReporter> reporters = new ArrayList<AbstractReporter>();
        GisticGenomicRegionReporter reporter1 = new GisticGenomicRegionReporter();
        ReporterList reporterList = new ReporterList("GisticReporterList", ReporterTypeEnum.GISTIC_GENOMIC_REGION_REPORTER);
        reporterList.setId(1L);
        reporter1.setReporterList(reporterList);
        reporter1.setIndex(0);
        GisticGenomicRegionReporter reporter2 = new GisticGenomicRegionReporter();
        reporter2.setReporterList(reporterList);
        reporter2.setIndex(1);
        GisticGenomicRegionReporter reporter3 = new GisticGenomicRegionReporter();
        reporter3.setReporterList(reporterList);
        reporter3.setIndex(2);
        reporters.add(reporter1);
        reporters.add(reporter2);
        reporters.add(reporter3);
        ArrayDataValues values = new ArrayDataValues(reporters);
        Study study = new Study();
        study.setId(1L);
        ArrayData arrayData1 = createArrayData(study);
        arrayData1.getReporterLists().add(reporterList);
        ArrayData arrayData2 = createArrayData(study);
        arrayData2.getReporterLists().add(reporterList);
        
        // Store first set of values
        values.setFloatValue(arrayData1, reporter1, ArrayDataValueType.DNA_ANALYSIS_LOG2_RATIO, 1.1f);
        values.setFloatValue(arrayData1, reporter2, ArrayDataValueType.DNA_ANALYSIS_LOG2_RATIO, 2.2f);
        values.setFloatValue(arrayData1, reporter3, ArrayDataValueType.DNA_ANALYSIS_LOG2_RATIO, 3.3f);
        values.setFloatValue(arrayData2, reporter1, ArrayDataValueType.DNA_ANALYSIS_LOG2_RATIO, 4.4f);
        values.setFloatValue(arrayData2, reporter2, ArrayDataValueType.DNA_ANALYSIS_LOG2_RATIO, 5.5f);
        values.setFloatValue(arrayData2, reporter3, ArrayDataValueType.DNA_ANALYSIS_LOG2_RATIO, 6.6f);
        File netCdfFile = new File(fileManagerStub.getStudyDirectory(study), 
                "data" + reporterList.getId() + "_" + ReporterTypeEnum.GISTIC_GENOMIC_REGION_REPORTER.getValue() + ".nc");
        FileUtils.deleteQuietly(netCdfFile);
        manager.storeValues(values);
        assertTrue(netCdfFile.exists());
        // Store additional values
        values = new ArrayDataValues(reporters);
        ArrayData arrayData3 = createArrayData(study);
        arrayData3.getReporterLists().add(reporterList);
        values.setFloatValue(arrayData3, reporter1, ArrayDataValueType.DNA_ANALYSIS_LOG2_RATIO, 13.13f);
        values.setFloatValue(arrayData3, reporter2, ArrayDataValueType.DNA_ANALYSIS_LOG2_RATIO, 14.14f);
        values.setFloatValue(arrayData3, reporter3, ArrayDataValueType.DNA_ANALYSIS_LOG2_RATIO, 15.15f);
        manager.storeValues(values);

        // Retrieve values and compare
        DataRetrievalRequest request = new DataRetrievalRequest();
        request.addArrayData(arrayData1);
        request.addArrayData(arrayData2);
        request.addArrayData(arrayData3);
        request.addType(ArrayDataValueType.DNA_ANALYSIS_LOG2_RATIO);
        request.addReporters(reporters);
        values = manager.retrieveValues(request);
        assertEquals(3, values.getArrayDatas().size());
        assertEquals(3, values.getReporters().size());
        assertEquals(1, values.getTypes().size());
        assertEquals(1.1f, values.getFloatValue(arrayData1, reporter1, ArrayDataValueType.DNA_ANALYSIS_LOG2_RATIO), 0.0f);
        assertEquals(2.2f, values.getFloatValue(arrayData1, reporter2, ArrayDataValueType.DNA_ANALYSIS_LOG2_RATIO), 0.0f);
        assertEquals(3.3f, values.getFloatValue(arrayData1, reporter3, ArrayDataValueType.DNA_ANALYSIS_LOG2_RATIO), 0.0f);
        assertEquals(4.4f, values.getFloatValue(arrayData2, reporter1, ArrayDataValueType.DNA_ANALYSIS_LOG2_RATIO), 0.0f);
        assertEquals(5.5f, values.getFloatValue(arrayData2, reporter2, ArrayDataValueType.DNA_ANALYSIS_LOG2_RATIO), 0.0f);
        assertEquals(6.6f, values.getFloatValue(arrayData2, reporter3, ArrayDataValueType.DNA_ANALYSIS_LOG2_RATIO), 0.0f);
        assertEquals(13.13f, values.getFloatValue(arrayData3, reporter1, ArrayDataValueType.DNA_ANALYSIS_LOG2_RATIO), 0.0f);
        assertEquals(14.14f, values.getFloatValue(arrayData3, reporter2, ArrayDataValueType.DNA_ANALYSIS_LOG2_RATIO), 0.0f);
        assertEquals(15.15f, values.getFloatValue(arrayData3, reporter3, ArrayDataValueType.DNA_ANALYSIS_LOG2_RATIO), 0.0f);

        request = new DataRetrievalRequest();
        request.addArrayData(arrayData1);
        request.addArrayData(arrayData3);
        request.addType(ArrayDataValueType.DNA_ANALYSIS_LOG2_RATIO);
        request.addReporter(reporter1);
        request.addReporter(reporter3);
        values = manager.retrieveValues(request);
        assertEquals(2, values.getArrayDatas().size());
        assertEquals(2, values.getReporters().size());
        assertEquals(1, values.getTypes().size());
        assertEquals(1.1f, values.getFloatValue(arrayData1, reporter1, ArrayDataValueType.DNA_ANALYSIS_LOG2_RATIO), 0.0f);
        assertEquals(3.3f, values.getFloatValue(arrayData1, reporter3, ArrayDataValueType.DNA_ANALYSIS_LOG2_RATIO), 0.0f);
        assertEquals(13.13f, values.getFloatValue(arrayData3, reporter1, ArrayDataValueType.DNA_ANALYSIS_LOG2_RATIO), 0.0f);
        assertEquals(15.15f, values.getFloatValue(arrayData3, reporter3, ArrayDataValueType.DNA_ANALYSIS_LOG2_RATIO), 0.0f);
        FileUtils.deleteQuietly(netCdfFile);
    }

    private ArrayData createArrayData(Study study) {
        ArrayData arrayData = new ArrayData();
        arrayData.setStudy(study);
        arrayData.setId(nextArrayDataId++);
        arrayData.setSample(new Sample());
        arrayData.getSample().setSampleAcquisition(new SampleAcquisition());
        arrayData.getSample().getSampleAcquisition().setAssignment(new StudySubjectAssignment());
        arrayData.getSample().getSampleAcquisition().getAssignment().setStudy(study);
        return arrayData;
    }

}
