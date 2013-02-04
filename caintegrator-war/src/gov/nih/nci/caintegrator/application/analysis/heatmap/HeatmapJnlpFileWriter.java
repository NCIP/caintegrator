/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.analysis.heatmap;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 
 */
public final class HeatmapJnlpFileWriter {
    private static final String XML_VERSION = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    private static final String REPLACEMENT_STRING = "REPLACMENT_STRING_TEXT";
    private static final String JNLP_START_TAG = "<jnlp spec=\"1.5+\" codebase=\"" + REPLACEMENT_STRING + "\">";
    private static final String APPLICATION_DESC_START_TAG = "<application-desc main-class=\"TCGA.Heatmap6\">";
    private static final String APPLICATION_DESC_END_TAG = "</application-desc>";
    private static final String JNPL_END_TAG = "</jnlp>";
    private static final Character NEW_LINE = '\n';
    
    private HeatmapJnlpFileWriter() { }
    
    /**
     * Writes the jnlp file to the directory.
     * @param sessionDirectory to store the session file.
     * @param urlPrefix should be of the form 
     *      "http://caintegrator.nci.nih.gov/caintegrator/heatmap/retrieveFile.do?JSESSIONID=12345&file=".
     * @param heatmapDirectoryUrl should be of the form
     *      "http://caintegrator.nci.nih.gov/caintegrator/common/"
     * @param heatmapResult the result.
     */
    public static void writeJnlpFile(File sessionDirectory, String urlPrefix, String heatmapDirectoryUrl, 
            HeatmapResult heatmapResult) {
        File jnlpFile = new File(sessionDirectory, HeatmapFileTypeEnum.LAUNCH_FILE.getFilename());
        try {
            FileWriter writer = new FileWriter(jnlpFile);
            writer.write(XML_VERSION + NEW_LINE);
            writer.write(JNLP_START_TAG.replaceAll(REPLACEMENT_STRING, heatmapDirectoryUrl) + NEW_LINE);
            writeInformationBlock(writer);
            writeSecurityBlock(writer);
            writeResourcesBlock(writer);
            writeApplicationBlock(urlPrefix, heatmapResult, writer);
            writer.write(JNPL_END_TAG);
            writer.flush();
            writer.close();
            heatmapResult.setJnlpFile(jnlpFile);
        } catch (IOException e) {
            throw new IllegalArgumentException("Couldn't write file at the path " 
                    + sessionDirectory.getAbsolutePath(), e);
        }
    }

    private static void writeApplicationBlock(String urlPrefix, HeatmapResult heatmapResult, FileWriter writer)
            throws IOException {
        writer.write(APPLICATION_DESC_START_TAG + NEW_LINE);
        if (heatmapResult.getGenomicDataFile() != null) {
            addArgument("-url-gm", writer);
            addArgument(convertUrlAmpersand(urlPrefix) + HeatmapFileTypeEnum.GENOMIC_DATA.getFilename(), writer);
        }
        if (heatmapResult.getLayoutFile() != null) {
            addArgument("-url-set", writer);
            addArgument(convertUrlAmpersand(urlPrefix) + HeatmapFileTypeEnum.LAYOUT.getFilename(), writer);
        }
        if (heatmapResult.getSampleAnnotationFile() != null) {
            addArgument("-url-annot", writer);
            addArgument(convertUrlAmpersand(urlPrefix) + HeatmapFileTypeEnum.ANNOTATIONS.getFilename(), writer);
        }
        addArgument("-binary", writer);
        addArgument("0", writer);
        addArgument("-url-gz", writer);
        addArgument("0", writer);
        writer.write(APPLICATION_DESC_END_TAG + NEW_LINE);
    }
    
    private static void writeInformationBlock(FileWriter writer) throws IOException {
        writer.write("<information>" + NEW_LINE);
        writer.write("<title>Heatmap</title>" + NEW_LINE);
        writer.write("<vendor>National Cancer Institute, NIH/NCI/CCR/LPG</vendor>" + NEW_LINE);
        writer.write("<homepage href=\"https://cgwb.nci.nih.gov/cgi-bin/heatmap\"/>" + NEW_LINE);
        writer.write("<description>Heatmap viewer</description>" + NEW_LINE);
        writer.write("<description kind=\"short\">" + NEW_LINE);
        writer.write("Heatmap" + NEW_LINE);
        writer.write("</description>" + NEW_LINE);
        writer.write("</information>" + NEW_LINE);
    }

    private static void writeSecurityBlock(FileWriter writer) throws IOException {
        writer.write("<security>" + NEW_LINE);
        writer.write("<all-permissions/>" + NEW_LINE);
        writer.write("</security>" + NEW_LINE);
    }
    
    private static void writeResourcesBlock(FileWriter writer) throws IOException {
        writer.write("<resources>" + NEW_LINE);
        writer.write("<j2se version=\"1.5+\" max-heap-size=\"512M\"/>" + NEW_LINE);
        writer.write("<jar href=\"heatmap.jar\"/>" + NEW_LINE);
        writer.write("</resources>" + NEW_LINE);
    }
    
    private static void addArgument(String argument, FileWriter writer) 
    throws IOException {
        writer.write("<argument>" + argument + "</argument>" + NEW_LINE);
    }
    
    private static String convertUrlAmpersand(String url) {
        return url.replaceAll("&", "&amp;");
    }
}
