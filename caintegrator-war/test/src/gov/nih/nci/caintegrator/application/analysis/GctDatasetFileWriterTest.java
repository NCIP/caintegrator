/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.analysis;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator.application.analysis.GctDataset;
import gov.nih.nci.caintegrator.application.analysis.GctDatasetFileWriter;
import gov.nih.nci.caintegrator.domain.application.GenomicDataQueryResult;
import gov.nih.nci.caintegrator.domain.application.GenomicDataResultColumn;
import gov.nih.nci.caintegrator.domain.application.GenomicDataResultRow;
import gov.nih.nci.caintegrator.domain.application.GenomicDataResultValue;
import gov.nih.nci.caintegrator.domain.genomic.Gene;
import gov.nih.nci.caintegrator.domain.genomic.GeneExpressionReporter;
import gov.nih.nci.caintegrator.domain.genomic.Sample;
import gov.nih.nci.caintegrator.domain.genomic.SampleAcquisition;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import au.com.bytecode.opencsv.CSVReader;

public class GctDatasetFileWriterTest {

    @Test
    public void testWriteAsGct() throws IOException {
        GenomicDataQueryResult result = createTestResult();
        String testFilePath = System.getProperty("java.io.tmpdir") + File.separator + "gctTest.gct";
        File gctFile = GctDatasetFileWriter.writeAsGct(new GctDataset(result), testFilePath);
        gctFile.deleteOnExit();
        checkFile(gctFile, result, true);
        gctFile = GctDatasetFileWriter.writeAsGct(new GctDataset(result, false), testFilePath);
        checkFile(gctFile, result, false);
    }

    private void checkFile(File gctFile, GenomicDataQueryResult result, boolean hasGenes) throws IOException {
        assertTrue(gctFile.exists());
        CSVReader reader = new CSVReader(new FileReader(gctFile), '\t');
        checkLine(reader.readNext(), "#1.2");
        checkLine(reader.readNext(), "2", "3");
        checkLine(reader.readNext(), "NAME", "Description", "SAMPLE1", "SAMPLE2", "SAMPLE3");
        if (hasGenes) {
            checkLine(reader.readNext(), "REPORTER1", "Gene: GENE1", "1.1", "2.2", "3.3");
        } else {
            checkLine(reader.readNext(), "REPORTER1", "N/A", "1.1", "2.2", "3.3");
        }
        
        checkLine(reader.readNext(), "REPORTER2", "N/A", "4.4", "5.5", "6.6");
        IOUtils.closeQuietly(reader);
    }

    private void checkLine(String[] line, String... expecteds) {
        assertArrayEquals(expecteds, line);
    }

    private GenomicDataQueryResult createTestResult() {
        GenomicDataQueryResult result = new GenomicDataQueryResult();
        addColumn(result, "SAMPLE1");
        addColumn(result, "SAMPLE2");
        addColumn(result, "SAMPLE3");
        addRow(result, "REPORTER1", "GENE1", new float[] {(float) 1.1, (float) 2.2, (float) 3.3});
        addRow(result, "REPORTER2", null, new float[] {(float) 4.4, (float) 5.5, (float) 6.6});
        return result;
    }

    private void addRow(GenomicDataQueryResult result, String reporterName, String geneName, float[] values) {
        GenomicDataResultRow row = new GenomicDataResultRow();
        GeneExpressionReporter reporter = new GeneExpressionReporter();
        reporter.setName(reporterName);
        if (geneName != null) {
            Gene gene = new Gene();
            gene.setSymbol(geneName.toUpperCase(Locale.getDefault()));
            reporter.getGenes().add(gene);
        }
        row.setReporter(reporter);
        int colNum = 0;
        for (float value : values) {
            GenomicDataResultValue genomicValue = new GenomicDataResultValue();
            genomicValue.setValue(value);
            row.getValues().add(genomicValue);
            genomicValue.setColumn(result.getColumnCollection().get(colNum));
            colNum++;
        }
        result.getRowCollection().add(row);
    }

    private void addColumn(GenomicDataQueryResult result, String sampleName) {
        GenomicDataResultColumn column = new GenomicDataResultColumn();
        column.setSampleAcquisition(new SampleAcquisition());
        column.getSampleAcquisition().setSample(new Sample());
        column.getSampleAcquisition().getSample().setName(sampleName);
        result.getColumnCollection().add(column);
    }

}
