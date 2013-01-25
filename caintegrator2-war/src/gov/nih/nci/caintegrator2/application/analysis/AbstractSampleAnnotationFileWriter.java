/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.analysis;

import gov.nih.nci.caintegrator2.domain.application.CopyNumberCriterionTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.ResultColumn;
import gov.nih.nci.caintegrator2.domain.genomic.Sample;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Writes a sample annotation file to the given path.
 */
public abstract class AbstractSampleAnnotationFileWriter {
    /**
     * Tab char.
     */
    protected static final char TAB = '\t';
    /**
     * New line char.
     */
    protected static final char NEW_LINE = '\n';
    private static List<String> headers = new ArrayList<String>();

    /**
     * Writes a ssample annotation file to the given file path.
     * @param sampleValuesMap hashmap of sample -> annotation string -> value.
     * @param columns the columns used in query.
     * @param filePath path to write file.
     * @param copyNumberSubType subtype for copynumber data (null if no copy number exists).
     * @return written classification file.
     */
    public File writeSampleInfoFile(Map<Sample, Map<String, String>> sampleValuesMap, 
            Collection<ResultColumn> columns, String filePath,
            CopyNumberCriterionTypeEnum copyNumberSubType) {
        File sampleInfoFile = new File(filePath);
        try {
            FileWriter writer = new FileWriter(sampleInfoFile);
            setupHeaders(columns);
            writeHeaderLine(writer, copyNumberSubType);
            writeData(writer, sampleValuesMap, copyNumberSubType);
            writer.flush();
            writer.close();
            return sampleInfoFile;
        } catch (IOException e) {
            throw new IllegalArgumentException("Couldn't write file at the path " + filePath, e);
        }
    }

    private void setupHeaders(Collection<ResultColumn> columns) {
        headers.clear();
        for (ResultColumn column : columns) {
            String displayName = column.getDisplayName();
            headers.add(displayName);
        }
        
    }
    
    private void writeHeaderLine(FileWriter writer, CopyNumberCriterionTypeEnum copyNumberSubType) throws IOException {
        writeFirstHeaders(writer, copyNumberSubType);
        for (String header : headers) {
            writer.write(TAB);
            writer.write(header);
        }
        writer.write(NEW_LINE);
    }

    private void writeData(FileWriter writer, Map<Sample, Map<String, String>> sampleValuesMap, 
            CopyNumberCriterionTypeEnum copyNumberSubType) throws IOException {
        for (Sample sample : sampleValuesMap.keySet()) {
            writerFirstData(writer, sample, copyNumberSubType);
            for (String header : headers) {
                writer.write(TAB);
                writer.write(sampleValuesMap.get(sample).get(header));
            }
            writer.write(NEW_LINE);
        }
    }
    
    /**
     * Writes the headers dynamic to the subclass.
     * @param writer to write to file.
     * @param copyNumberSubType subtype for copynumber data (null if no copy number exists).
     * @throws IOException if unable to write.
     */
    protected abstract void writeFirstHeaders(FileWriter writer, CopyNumberCriterionTypeEnum copyNumberSubType)
        throws IOException;
    
    /**
     * Writes the first data belonging to the first headers dynamic to the subclass.
     * @param writer to write to file.
     * @param sample belonging to the current row of data to write.
     * @param copyNumberSubType subtype for copynumber data (null if no copy number exists).
     * @throws IOException if unable to write.
     */
    protected abstract void writerFirstData(FileWriter writer, Sample sample, 
            CopyNumberCriterionTypeEnum copyNumberSubType) throws IOException;

}
