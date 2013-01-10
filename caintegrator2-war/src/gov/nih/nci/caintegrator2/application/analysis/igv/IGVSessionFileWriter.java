/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.application.analysis.igv;

import gov.nih.nci.caintegrator2.domain.genomic.GenomeBuildVersionEnum;
import gov.nih.nci.caintegrator2.domain.genomic.Platform;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

/**
 *
 */
public final class IGVSessionFileWriter {
    private static final String XML_VERSION = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    private static final String DEFAULT_SESSION_TAG = "<Session genome=\"" + GenomeBuildVersionEnum.HG18.getValue()
                                                            + "\" locus=\"All\" version=\"3\">";
    private static final String RESOURCES_TAG = "<Resources>";
    private static final String RESOURCES_TAG_END = "</Resources>";
    private static final String SESSION_TAG_END = "</Session>";
    private static final Character NEW_LINE = '\n';

    private IGVSessionFileWriter() { }

    /**
     * Writes the session file to the directory.
     * @param sessionDirectory to store the session file.
     * @param urlPrefix should be of the form
     *      "http://caintegrator.nci.nih.gov/caintegrator/igv/retrieveFile.do?JSESSIONID=12345&file=".
     * @param igvResult the result.
     * @param platforms the platforms to retrieve the genome build version from.
     */
    public static void writeSessionFile(File sessionDirectory, String urlPrefix, IGVResult igvResult,
            Collection<Platform> platforms) {
        File sessionFile = new File(sessionDirectory, IGVFileTypeEnum.SESSION.getFilename());
        try {
            FileWriter writer = new FileWriter(sessionFile);
            writer.write(XML_VERSION + NEW_LINE);
            writer.write(retrieveSessionTag(platforms) + NEW_LINE);
            writer.write(RESOURCES_TAG + NEW_LINE);
            if (igvResult.getGeneExpressionFile() != null) {
                writeDataFileLine(convertUrlAmpersand(urlPrefix), writer, IGVFileTypeEnum.GENE_EXPRESSION);
            }
            if (igvResult.getSegmentationFile() != null) {
                writeDataFileLine(convertUrlAmpersand(urlPrefix), writer, IGVFileTypeEnum.SEGMENTATION);
            }
            if (igvResult.getSampleInfoFile() != null) {
                writeDataFileLine(convertUrlAmpersand(urlPrefix), writer, IGVFileTypeEnum.SAMPLE_CLASSIFICATION);
            }
            writer.write(RESOURCES_TAG_END + NEW_LINE);
            writer.write(SESSION_TAG_END);
            writer.flush();
            writer.close();
            igvResult.setSessionFile(sessionFile);
        } catch (IOException e) {
            throw new IllegalArgumentException("Couldn't write file at the path "
                    + sessionDirectory.getAbsolutePath(), e);
        }
    }

    private static String retrieveSessionTag(Collection<Platform> platforms) {
        String sessionTag = DEFAULT_SESSION_TAG;
        GenomeBuildVersionEnum genomeBuildVersion = null;
        for (Platform platform : platforms) {
            if (genomeBuildVersion == null) {
                genomeBuildVersion = platform.getGenomeVersion();
            } else {
                if (genomeBuildVersion != platform.getGenomeVersion()) {
                    return DEFAULT_SESSION_TAG; // If the platforms have differing genomes, use default.
                }
            }
        }
        if (genomeBuildVersion != null) {
            sessionTag = sessionTag.replaceAll(GenomeBuildVersionEnum.HG18.getValue(), genomeBuildVersion.getValue());
        }
        return sessionTag;
    }

    private static void writeDataFileLine(String urlPrefix, FileWriter writer, IGVFileTypeEnum fileType)
    throws IOException {
        writer.write("<Resource path=\""
                + urlPrefix + fileType.getFilename() + "\"/>" + NEW_LINE);
    }

    private static String convertUrlAmpersand(String url) {
        return url.replaceAll("&", "&amp;");
    }
}
