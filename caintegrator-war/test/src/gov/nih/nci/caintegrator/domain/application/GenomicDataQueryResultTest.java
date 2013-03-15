/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.domain.application;

import static org.junit.Assert.assertEquals;
import gov.nih.nci.caintegrator.domain.genomic.GeneExpressionReporter;
import gov.nih.nci.caintegrator.domain.genomic.Sample;
import gov.nih.nci.caintegrator.domain.genomic.SampleAcquisition;
import gov.nih.nci.caintegrator.domain.genomic.SampleSet;

import java.util.List;

import org.junit.Test;

public class GenomicDataQueryResultTest {

    @Test
    public void testRemoveColumn() {
        GenomicDataQueryResult result = new GenomicDataQueryResult();
        GenomicDataResultColumn column1 = result.addColumn();
        GenomicDataResultColumn column2 = result.addColumn();
        column1.setSampleAcquisition(createSampleAcquisition("sample1"));
        column2.setSampleAcquisition(createSampleAcquisition("sample2"));
        result.getRowCollection().add(createRow(1.1f, 2.2f, result.getColumnCollection()));
        result.getRowCollection().add(createRow(3.3f, 4.4f, result.getColumnCollection()));

        assertEquals(2, result.getColumnCollection().size());
        assertEquals(2, result.getRowCollection().get(0).getValues().size());
        assertEquals(2, result.getRowCollection().get(1).getValues().size());

        result.excludeSampleSet(null);
        assertEquals(2, result.getColumnCollection().size());
        assertEquals(2, result.getRowCollection().get(0).getValues().size());
        assertEquals(2, result.getRowCollection().get(1).getValues().size());

        SampleSet excludedSampleSet = new SampleSet();
        result.excludeSampleSet(excludedSampleSet);
        assertEquals(2, result.getColumnCollection().size());
        assertEquals(2, result.getRowCollection().get(0).getValues().size());
        assertEquals(2, result.getRowCollection().get(1).getValues().size());

        excludedSampleSet.getSamples().add(result.getColumnCollection().get(0).getSampleAcquisition().getSample());
        result.excludeSampleSet(excludedSampleSet);
        assertEquals(1, result.getColumnCollection().size());
        assertEquals(1, result.getRowCollection().get(0).getValues().size());
        assertEquals(1, result.getRowCollection().get(1).getValues().size());

        Sample sample3 = new Sample();
        sample3.setName("sample3");
        excludedSampleSet.getSamples().add(sample3);
        result.excludeSampleSet(excludedSampleSet);
        assertEquals(1, result.getColumnCollection().size());
        assertEquals(1, result.getRowCollection().get(0).getValues().size());
        assertEquals(1, result.getRowCollection().get(1).getValues().size());
    }

    private SampleAcquisition createSampleAcquisition(String sampleName) {
        SampleAcquisition sa = new SampleAcquisition();
        Sample sample = new Sample();
        sample.setName(sampleName);
        sa.setSample(sample);
        return sa;
    }

    private GenomicDataResultRow createRow(float float1, float float2, List<GenomicDataResultColumn> columns) {
        GenomicDataResultRow row = new GenomicDataResultRow();
        GeneExpressionReporter reporter = new GeneExpressionReporter();
        row.setReporter(reporter);
        GenomicDataResultValue value1 = new GenomicDataResultValue();
        value1.setValue(float1);
        value1.setColumn(columns.get(0));
        row.getValues().add(value1);
        GenomicDataResultValue value2 = new GenomicDataResultValue();
        value2.setValue(float2);
        value2.setColumn(columns.get(1));
        row.getValues().add(value2);
        return row;
    }

}
