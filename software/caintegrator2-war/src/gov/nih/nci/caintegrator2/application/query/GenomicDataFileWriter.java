/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.query;

import gov.nih.nci.caintegrator2.domain.application.GenomicDataQueryResult;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataResultColumn;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataResultRow;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataResultValue;
import gov.nih.nci.caintegrator2.domain.application.ResultsOrientationEnum;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * Writes GenomicDataQueryResults in csv format.
 */
public final class GenomicDataFileWriter {

    private GenomicDataFileWriter() { }
    
    /**
     * Writes a GenomicDataQueryResult to the given file.  
     * @param result genomic query result to write in csv format.
     * @param csvFile to write file to.
     * @return csv file.
     */
    public static File writeAsCsv(GenomicDataQueryResult result, File csvFile) {
        try {
            FileWriter writer = new FileWriter(csvFile);
            if (ResultsOrientationEnum.SUBJECTS_AS_COLUMNS.equals(result.getQuery().getOrientation())) {
                writeStandardOrientation(result, writer);
            } else {
                writeSubjectsAsRowsOrientation(result, writer);
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new IllegalArgumentException("Couldn't write file at the path " + csvFile.getAbsolutePath(), e);
        }
        return csvFile;
    }

    private static void writeSubjectsAsRowsOrientation(GenomicDataQueryResult result, FileWriter writer) 
    throws IOException {
        writeSubjectsAsRowsHeaders(result, writer);
        writeSubjectsAsRowsRows(result, writer);
    }

    private static void writeSubjectsAsRowsHeaders(GenomicDataQueryResult result, FileWriter writer) 
    throws IOException {
        List<String> line = getGeneHeaderLine(result);
        writeAsCsvRow(writer, line);
        int columnCount = line.size();
        if (isProbeSetResultType(result)) {
            writeAsCsvRow(writer, getReporterHeaderLine(result));
        }
        String[] sampleHeaderLine = new String[columnCount];
        sampleHeaderLine[0] = "Subject ID";
        sampleHeaderLine[1] = "Sample ID"; 
        writeAsCsvRow(writer, sampleHeaderLine);
    }

    private static List<String> getReporterHeaderLine(GenomicDataQueryResult result) {
        List<String> line = new ArrayList<String>();
        line.add("");
        line.add("");
        line.add("Reporter ID");
        for (GenomicDataResultRow row : result.getFilteredRowCollection()) {
            line.add(row.getReporter().getName());
        }
        return line;
    }

    private static List<String> getGeneHeaderLine(GenomicDataQueryResult result) {
        List<String> line = new ArrayList<String>();
        line.add("");
        line.add("");
        line.add("Gene Name");
        for (GenomicDataResultRow row : result.getFilteredRowCollection()) {
            line.add(row.getReporter().getGeneSymbols());
        }
        return line;
    }

    private static void writeSubjectsAsRowsRows(GenomicDataQueryResult result, FileWriter writer) 
    throws IOException {
        for (GenomicDataResultColumn column : result.getColumnCollection()) {
            writeAsCsvRow(writer, getResultLine(column));
        }
    }

    private static List<String> getResultLine(GenomicDataResultColumn column) {
        List<String> line = new ArrayList<String>();
        line.add(column.getSampleAcquisition().getAssignment().getIdentifier());
        line.add(column.getSampleAcquisition().getSample().getName());
        line.add("");
        for (GenomicDataResultValue value : column.getValues()) {
            line.add(String.valueOf(value.getValue()));
        }
        return line;
    }

    private static void writeStandardOrientation(GenomicDataQueryResult result, FileWriter writer) throws IOException {
        writeHeaders(result, writer);
        writeRows(result, writer);
    }

    private static void writeHeaders(GenomicDataQueryResult result, FileWriter writer) throws IOException {
        List<String> assignmentIdentifiersRow = new ArrayList<String>();
        assignmentIdentifiersRow.add("");
        assignmentIdentifiersRow.add("Subject ID");
        List<String> sampleIdentifiersRow = new ArrayList<String>();
        sampleIdentifiersRow.add("");
        sampleIdentifiersRow.add("Sample ID");
        if (isProbeSetResultType(result)) {
            assignmentIdentifiersRow.add(1, "");
            sampleIdentifiersRow.add(1, "");
        }
        for (GenomicDataResultColumn column : result.getColumnCollection()) {
            sampleIdentifiersRow.add(column.getSampleAcquisition().getSample().getName());
            assignmentIdentifiersRow.add(column.getSampleAcquisition().getAssignment().getIdentifier());
        }
        writeAsCsvRow(writer, assignmentIdentifiersRow);
        writeAsCsvRow(writer, sampleIdentifiersRow);
        String[] reporterHeadersRow = new String[assignmentIdentifiersRow.size()];
        reporterHeadersRow[0] = "Gene Name";
        if (isProbeSetResultType(result)) {
            reporterHeadersRow[1] = "Reporter ID";
        }
        writeAsCsvRow(writer, reporterHeadersRow);
    }
    
    private static void writeRows(GenomicDataQueryResult result, FileWriter writer) throws IOException {
        for (GenomicDataResultRow row : result.getFilteredRowCollection()) {
            List<String> resultValuesRow = new ArrayList<String>();
            resultValuesRow.add(row.getReporter().getGeneSymbols().replaceAll(",", "-"));
            if (isProbeSetResultType(result)) {
                resultValuesRow.add(row.getReporter().getName());
            }
            resultValuesRow.add("");
            for (GenomicDataResultValue value : row.getValues()) {
                resultValuesRow.add(String.valueOf(value.getValue()));
            }
            writeAsCsvRow(writer, resultValuesRow);
        }
    }
    

    private static void writeAsCsvRow(FileWriter writer, String[] csvRowStringArray) throws IOException {
        writeAsCsvRow(writer, Arrays.asList(csvRowStringArray));
    }
    
    private static void writeAsCsvRow(FileWriter writer, List<String> csvRowStrings) throws IOException {
        writer.append(StringUtils.join(csvRowStrings, ","));
        writer.append("\n");
    }
    
    private static boolean isProbeSetResultType(GenomicDataQueryResult result) {
        return ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET.equals(result.getQuery().getReporterType());
    }

}
