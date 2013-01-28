/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.application.analysis;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Writes a GctDataset to a GenePattern .GCT format file.
 */
public final class GctDatasetFileWriter {
    
    private static final String GCT_VERSION_HEADER = "#1.2\n";
    
    private GctDatasetFileWriter() {
        super();
    }

    /**
     * Writes a gctDataset to the given file path.
     * @param dataset object representing the gct data.
     * @param gctFilePath path to write file.
     * @return written gct file.
     */
    public static File writeAsGct(GctDataset dataset, String gctFilePath) {
        File gctFile = new File(gctFilePath);
        try {
            FileWriter writer = new FileWriter(gctFile);
            writeHeader(writer, dataset); 
            writeData(writer, dataset);
            writer.flush();
            writer.close();
            return gctFile;
        } catch (IOException e) {
            throw new IllegalArgumentException("Couldn't write file at the path " + gctFilePath, e);
        }
    }

    private static void writeHeader(FileWriter writer, GctDataset dataset) throws IOException {
        writer.write(GCT_VERSION_HEADER);
        writer.write(String.valueOf(dataset.getRowReporterNames().size()));
        writer.write('\t');
        writer.write(String.valueOf(dataset.getColumnSampleNames().size()));
        writer.write('\n');
        writer.write("NAME\tDescription");
        for (String column : dataset.getColumnSampleNames()) {
            writer.write('\t');
            writer.write(column);
        }
        writer.write('\n');
    }

    private static void writeData(FileWriter writer, GctDataset dataset) throws IOException {
        int rowNum = 0;
        for (String row : dataset.getRowReporterNames()) {
            writer.write(row);
            writer.write('\t');
            writer.write(dataset.getRowDescription().get(rowNum));
            writeValues(writer, rowNum, dataset);
            writer.write('\n');
            rowNum++;
        }
    }

    private static void writeValues(FileWriter writer, int rowNumber, GctDataset dataset) 
    throws IOException {
        float[] rowValues = dataset.getValues()[rowNumber];
        for (float value : rowValues) {
            writer.write('\t');
            writer.write(String.valueOf(value));
        }
    }

}
