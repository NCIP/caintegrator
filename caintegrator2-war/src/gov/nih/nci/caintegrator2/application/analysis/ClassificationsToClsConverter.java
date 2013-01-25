/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.analysis;

import gov.nih.nci.caintegrator2.application.analysis.SampleClassificationParameterValue.SampleClassification;
import gov.nih.nci.caintegrator2.domain.genomic.Sample;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Utility class that writes out a GenePattern .cls format file based on a 
 * <code>SampleClassificationParameterValue</code>.
 */
public final class ClassificationsToClsConverter {
    
    private ClassificationsToClsConverter() {
        super();
    }

    /**
     * Writes the sample classifications to the specified file path.
     * @param parameterValue sample classification values to write.
     * @param clsFilePath file to write to.
     * @return cls file.
     */
    public static File writeAsCls(SampleClassificationParameterValue parameterValue, String clsFilePath) {
        File clsFile = new File(clsFilePath);
        try {
            FileWriter writer = new FileWriter(clsFile);
            writeCountHeader(writer, parameterValue); 
            writeClassificationNameHeader(writer, parameterValue); 
            writeClassifications(writer, parameterValue);
            writer.flush();
            writer.close();
            return clsFile;
        } catch (IOException e) {
            throw new IllegalArgumentException("Couldn't write file at the path " + clsFile, e);
        }
    }

    private static void writeCountHeader(FileWriter writer, SampleClassificationParameterValue parameterValue) 
    throws IOException {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(parameterValue.getClassifiedSamples().size());
        stringBuffer.append(' ');
        stringBuffer.append(parameterValue.getClassifications().size());
        stringBuffer.append(" 1\n");
        writer.write(stringBuffer.toString());
    }

    private static void writeClassificationNameHeader(FileWriter writer,
            SampleClassificationParameterValue parameterValue) throws IOException {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append('#');
        for (SampleClassification classification : parameterValue.getClassifications()) {
            stringBuffer.append(' ');
            stringBuffer.append(classification.getName().replaceAll(" ", "_"));
        }
        stringBuffer.append('\n');
        writer.write(stringBuffer.toString());
    }

    private static void writeClassifications(FileWriter writer, SampleClassificationParameterValue parameterValue) 
    throws IOException {
        StringBuffer stringBuffer = new StringBuffer();
        boolean first = true;
        for (Sample sample : parameterValue.getClassifiedSamples()) {
            if (!first) {
                stringBuffer.append(' ');
            } else {
                first = false;
            }
            stringBuffer.append(parameterValue.getClassification(sample).getIndex());
        }
        stringBuffer.append('\n');
        writer.write(stringBuffer.toString());
    }

}
