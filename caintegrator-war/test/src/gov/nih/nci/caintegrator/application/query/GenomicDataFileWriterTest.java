/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.query;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator.domain.application.GenomicDataQueryResult;
import gov.nih.nci.caintegrator.domain.application.GenomicDataResultColumn;
import gov.nih.nci.caintegrator.domain.application.GenomicDataResultRow;
import gov.nih.nci.caintegrator.domain.application.GenomicDataResultValue;
import gov.nih.nci.caintegrator.domain.application.Query;
import gov.nih.nci.caintegrator.domain.application.ResultsOrientationEnum;
import gov.nih.nci.caintegrator.domain.genomic.Gene;
import gov.nih.nci.caintegrator.domain.genomic.GeneExpressionReporter;
import gov.nih.nci.caintegrator.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator.domain.genomic.Sample;
import gov.nih.nci.caintegrator.domain.genomic.SampleAcquisition;
import gov.nih.nci.caintegrator.domain.translational.StudySubjectAssignment;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;

import org.junit.Test;

import au.com.bytecode.opencsv.CSVReader;

/**
 * Genomic data file writer tests.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
public class GenomicDataFileWriterTest {

    /**
     * Tests writing a genomic file as csv.
     * @throws IOException on io error.
     */
    @Test
    public void writeAsCsv() throws IOException {
        GenomicDataQueryResult result = createTestResult();
        result.setHasCriterionSpecifiedValues(true);

        Query query = new Query();
        query.setReporterType(ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET);
        query.setOrientation(ResultsOrientationEnum.SUBJECTS_AS_COLUMNS);
        result.setQuery(query);

        File csvFile = new File(System.getProperty("java.io.tmpdir") + File.separator + "genomicResultTest.csv");
        csvFile = GenomicDataFileWriter.writeAsCsv(result, csvFile);
        csvFile.deleteOnExit();
        checkFile(csvFile);

        query.setOrientation(ResultsOrientationEnum.SUBJECTS_AS_ROWS);
        csvFile = GenomicDataFileWriter.writeAsCsv(result, csvFile);
        checkFileWithSubjectsAsRows(csvFile);
    }

    private void checkFile(File csvFile) throws IOException {
        assertTrue(csvFile.exists());
        CSVReader reader = new CSVReader(new FileReader(csvFile), ',');
        checkLine(reader.readNext(), "", "", "Subject ID", "ASSIGNMENT1", "ASSIGNMENT2", "ASSIGNMENT3");
        checkLine(reader.readNext(), "", "", "Sample ID", "SAMPLE1", "SAMPLE2", "SAMPLE3");
        checkLine(reader.readNext(), "Gene Name", "Reporter ID", "", "", "", "");
        checkLine(reader.readNext(), "GENE1", "REPORTER1", "", "1.1", "2.2", "3.3");
        checkLine(reader.readNext(), "", "REPORTER2", "", "4.4", "5.5", "6.6");
    }

    private void checkFileWithSubjectsAsRows(File csvFile) throws IOException {
        assertTrue(csvFile.exists());
        CSVReader reader = new CSVReader(new FileReader(csvFile), ',');
        checkLine(reader.readNext(), "", "", "Gene Name", "GENE1", "");
        checkLine(reader.readNext(), "", "", "Reporter ID", "REPORTER1", "REPORTER2");
        checkLine(reader.readNext(), "Subject ID", "Sample ID", "", "", "");
        checkLine(reader.readNext(), "ASSIGNMENT1", "SAMPLE1",  "", "1.1", "4.4");
        checkLine(reader.readNext(), "ASSIGNMENT2", "SAMPLE2",  "", "2.2", "5.5");
        checkLine(reader.readNext(), "ASSIGNMENT3", "SAMPLE3",  "", "3.3", "6.6");
    }

    private void checkLine(String[] line, String... expecteds) {
        assertArrayEquals(expecteds, line);
    }

    private GenomicDataQueryResult createTestResult() {
        GenomicDataQueryResult result = new GenomicDataQueryResult();
        addColumn(result, "SAMPLE1", "ASSIGNMENT1");
        addColumn(result, "SAMPLE2", "ASSIGNMENT2");
        addColumn(result, "SAMPLE3", "ASSIGNMENT3");
        addRow(result, "REPORTER1", "GENE1", new float[] {(float) 1.1, (float) 2.2, (float) 3.3}, true);
        //will not show up because doesn't have matching values.
        addRow(result, "REPORTER3", "GENE3", new float[] {8, 9, 10}, false);
        addRow(result, "REPORTER2", null, new float[] {(float) 4.4, (float) 5.5, (float) 6.6}, true);
        return result;
    }

    private void addRow(GenomicDataQueryResult result, String reporterName, String geneName, float[] values,
            boolean hasMatchingValues) {
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
        row.setHasMatchingValues(hasMatchingValues);
    }

    private void addColumn(GenomicDataQueryResult result, String sampleName, String assignmentName) {
        GenomicDataResultColumn column = result.addColumn();
        column.setSampleAcquisition(new SampleAcquisition());
        column.getSampleAcquisition().setSample(new Sample());
        column.getSampleAcquisition().getSample().setName(sampleName);
        StudySubjectAssignment assignment = new StudySubjectAssignment();
        assignment.setIdentifier(assignmentName);
        column.getSampleAcquisition().setAssignment(assignment);
    }

}
