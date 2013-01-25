/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.analysis.heatmap;

import gov.nih.nci.caintegrator2.application.analysis.AbstractSampleAnnotationFileWriter;
import gov.nih.nci.caintegrator2.domain.application.CopyNumberCriterionTypeEnum;
import gov.nih.nci.caintegrator2.domain.genomic.Sample;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Writes a sample info file for HeatMap to a heatmap sample annotation format file.
 */
public class HeatmapSampleAnnotationsFileWriter extends AbstractSampleAnnotationFileWriter {

    private static final String SAMPLE_ID_HEADER = "SAMPLE_ID";

    @Override
    protected void writeFirstHeaders(FileWriter writer,
            CopyNumberCriterionTypeEnum copyNumberSubType) throws IOException {
        writer.write(SAMPLE_ID_HEADER);
        
    }

    @Override
    protected void writerFirstData(FileWriter writer, Sample sample, 
            CopyNumberCriterionTypeEnum copyNumberSubType) throws IOException {
        writer.write(sample.getName());
    }

}
