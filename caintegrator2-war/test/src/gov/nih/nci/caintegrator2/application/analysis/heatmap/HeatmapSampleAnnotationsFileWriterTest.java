/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.application.analysis.heatmap;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator2.common.QueryUtil;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.annotation.StringAnnotationValue;
import gov.nih.nci.caintegrator2.domain.application.QueryResult;
import gov.nih.nci.caintegrator2.domain.application.ResultColumn;
import gov.nih.nci.caintegrator2.domain.application.ResultRow;
import gov.nih.nci.caintegrator2.domain.application.ResultValue;
import gov.nih.nci.caintegrator2.domain.genomic.Sample;
import gov.nih.nci.caintegrator2.domain.genomic.SampleAcquisition;
import gov.nih.nci.caintegrator2.domain.translational.StudySubjectAssignment;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import au.com.bytecode.opencsv.CSVReader;

/**
 *
 */
public class HeatmapSampleAnnotationsFileWriterTest {

private Collection<ResultColumn> columns = new HashSet<ResultColumn>();


    @Test
    public void testWriteSampleInfoFile() throws IOException {
        File tempDirectory = new File(System.getProperty("java.io.tmpdir") + File.separator + "heatmapTmp");
        tempDirectory.mkdir();
        HeatmapSampleAnnotationsFileWriter fileWriter = new HeatmapSampleAnnotationsFileWriter();

        File sampleInfoFile = fileWriter.writeSampleInfoFile(QueryUtil.retrieveSampleValuesMap(setupQueryResult()),
                columns, new File(tempDirectory.getAbsolutePath() + File.separator
                        + HeatmapFileTypeEnum.ANNOTATIONS.getFilename()).getAbsolutePath(), null);
        checkFile(sampleInfoFile);
        FileUtils.deleteDirectory(tempDirectory);
    }

    private QueryResult setupQueryResult() {
        ResultValue resultValue1 = new ResultValue();
        StringAnnotationValue stringValue1 = new StringAnnotationValue();
        stringValue1.setStringValue("val1");
        StringAnnotationValue stringValue2 = new StringAnnotationValue();
        stringValue2.setStringValue("val2");
        ResultValue resultValue2 = new ResultValue();
        ResultColumn resultColumn1 = new ResultColumn();
        columns.add(resultColumn1);
        resultValue1.setColumn(resultColumn1);
        resultValue1.setValue(stringValue1);
        resultValue2.setColumn(resultColumn1);
        resultValue2.setValue(stringValue2);
        AnnotationFieldDescriptor afd = new AnnotationFieldDescriptor();
        AnnotationDefinition ad = new AnnotationDefinition();
        ad.setDisplayName("annotation");
        resultColumn1.setAnnotationFieldDescriptor(afd);
        afd.setDefinition(ad);

        QueryResult queryResult = new QueryResult();
        queryResult.setRowCollection(new HashSet<ResultRow>());
        ResultRow row1 = new ResultRow();
        StudySubjectAssignment assignment1 = new StudySubjectAssignment();
        assignment1.setIdentifier("assignment1");
        row1.setSubjectAssignment(assignment1);
        SampleAcquisition sampleAcquisition1 = new SampleAcquisition();
        Sample sample1 = new Sample();
        sample1.setName("sample1");
        sampleAcquisition1.setSample(sample1);
        sampleAcquisition1.setAssignment(assignment1);
        sample1.getSampleAcquisitions().add(sampleAcquisition1);
        assignment1.getSampleAcquisitionCollection().add(sampleAcquisition1);
        row1.setSampleAcquisition(sampleAcquisition1);
        row1.setValueCollection(new ArrayList<ResultValue>());
        row1.getValueCollection().add(resultValue1);

        ResultRow row2 = new ResultRow();
        StudySubjectAssignment assignment2 = new StudySubjectAssignment();
        assignment2.setIdentifier("assignment2");
        row2.setSubjectAssignment(assignment2);
        SampleAcquisition sampleAcquisition2 = new SampleAcquisition();
        Sample sample2 = new Sample();
        sample2.setName("sample2");
        sampleAcquisition2.setSample(sample2);
        sampleAcquisition2.setAssignment(assignment2);
        sample2.getSampleAcquisitions().add(sampleAcquisition2);
        assignment2.getSampleAcquisitionCollection().add(sampleAcquisition2);
        row2.setSampleAcquisition(sampleAcquisition2);
        row2.setValueCollection(new ArrayList<ResultValue>());
        row2.getValueCollection().add(resultValue2);

        queryResult.getRowCollection().add(row1);
        queryResult.getRowCollection().add(row2);
        return queryResult;
    }

    private void checkFile(File sampleInfoFile) throws IOException {
        assertTrue(sampleInfoFile.exists());
        CSVReader reader = new CSVReader(new FileReader(sampleInfoFile), '\t');
        checkLine(reader.readNext(), "SAMPLE_ID", "annotation");
        checkSampleInfoLine(reader.readNext());
        checkSampleInfoLine(reader.readNext());
        reader.close();
    }

    private void checkSampleInfoLine(String[] line) {
        if ("sample1".equals(line[0])) {
            checkLine(line, "sample1", "val1");
        } else {
            checkLine(line, "sample2", "val2");
        }
    }

    private void checkLine(String[] line, String... expecteds) {
        assertArrayEquals(expecteds, line);
    }
}
