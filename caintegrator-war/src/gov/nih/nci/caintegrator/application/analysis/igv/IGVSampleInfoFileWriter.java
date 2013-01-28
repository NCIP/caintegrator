/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.application.analysis.igv;

import gov.nih.nci.caintegrator.application.analysis.AbstractSampleAnnotationFileWriter;
import gov.nih.nci.caintegrator.domain.application.CopyNumberCriterionTypeEnum;
import gov.nih.nci.caintegrator.domain.genomic.Sample;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Writes a sample info file for IGV to an IGV .SEG format file.
 */
public class IGVSampleInfoFileWriter extends AbstractSampleAnnotationFileWriter {

    private static final String TRACK_ID_HEADER = "TRACK_ID";
    private static final String SUBJECT_ID_HEADER = "SUBJECT_ID";
    private static final String COPY_NUMBER_SUBTYPE_HEADER = "COPYNUMBER_SUBTYPE";

    @Override
    protected void writeFirstHeaders(FileWriter writer,
            CopyNumberCriterionTypeEnum copyNumberSubType) throws IOException {
        writer.write(TRACK_ID_HEADER);
        writer.write(TAB);
        writer.write(SUBJECT_ID_HEADER);
        if (copyNumberSubType != null) {
            writer.write(TAB);
            writer.write(COPY_NUMBER_SUBTYPE_HEADER);
        }

    }

    @Override
    protected void writerFirstData(FileWriter writer, Sample sample,
            CopyNumberCriterionTypeEnum copyNumberSubType) throws IOException {
        writer.write(sample.getName());
        writer.write(TAB);
        writer.write(sample.getSampleAcquisitions().iterator().next().getAssignment().getIdentifier());
        if (copyNumberSubType != null) {
            writer.write(TAB);
            writer.write(sample.getGenomicDataSource().isCopyNumberData() ? copyNumberSubType.getValue() : "NA");
        }
    }

}
