/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.analysis;

import gov.nih.nci.caintegrator2.domain.genomic.SegmentData;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

/**
 * Writes a SegmentDataset to an IGV .SEG format file.
 */
public final class SegmentDatasetFileWriter {

    private static final char TAB = '\t';
    private static final char NEW_LINE = '\n';
    
    private static final String IGV_SEG_HEADER =
        "Track Name\tChromosome\tStart Position\tEnd Position\tSegment Value\n";
    
    private SegmentDatasetFileWriter() {
        super();
    }

    /**
     * Writes a segmentDataset to the given file path.
     * @param dataset object representing the list of segment data.
     * @param segFilePath path to write file.
     * @param isUseCGHCall whether or not to use CGH call for the segmentation file.
     * @return written SEG file.
     */
    public static File writeAsSegFile(Collection<SegmentData> dataset, String segFilePath, boolean isUseCGHCall) {
        File segFile = new File(segFilePath);
        try {
            FileWriter writer = new FileWriter(segFile);
            writer.write(IGV_SEG_HEADER);
            writeData(writer, dataset, isUseCGHCall);
            writer.flush();
            writer.close();
            return segFile;
        } catch (IOException e) {
            throw new IllegalArgumentException("Couldn't write file at the path " + segFilePath, e);
        }
    }

    private static void writeData(FileWriter writer, Collection<SegmentData> dataset, 
            boolean isUseCGHCall) throws IOException {
        for (SegmentData segmentData : dataset) {
            writer.write(segmentData.getArrayData().getSample().getName());
            writer.write(TAB);
            writer.write(segmentData.getLocation().getChromosome());
            writer.write(TAB);
            writer.write(segmentData.getLocation().getStartPosition().toString());
            writer.write(TAB);
            writer.write(segmentData.getLocation().getEndPosition().toString());
            writer.write(TAB);
            if (isUseCGHCall) {
                writer.write(segmentData.getCallsValue() == null ? "" : segmentData.getCallsValue().toString());
            } else {
                writer.write(segmentData.getSegmentValue().toString());
            }
            writer.write(NEW_LINE);
        }
    }

}
