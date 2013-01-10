/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.application.analysis.heatmap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.TestDataFiles;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class HeatmapJnlpFileWriterTest {

    @Test
    public void testWriteSessionFile() throws IOException {
        String urlPrefix = "http://caintegrator2.nci.nih.gov/caintegrator2/heatmap/retrieveFile.jnlp?JSESSIONID=12345&file=";
        String heatmapDirectoryUrl = "http://caintegrator2.nci.nih.gov/caintegrator2/common/";
        File tempDirectory = new File(System.getProperty("java.io.tmpdir") + File.separator + "heatmapTmp");
        tempDirectory.mkdir();
        HeatmapResult heatmapResult = new HeatmapResult();
        heatmapResult.setGenomicDataFile(TestDataFiles.VALID_FILE);
        heatmapResult.setLayoutFile(TestDataFiles.VALID_FILE);
        heatmapResult.setSampleAnnotationFile(TestDataFiles.VALID_FILE);
        
        HeatmapJnlpFileWriter.writeJnlpFile(tempDirectory, 
                urlPrefix, heatmapDirectoryUrl, heatmapResult);
        File sessionFile = heatmapResult.getJnlpFile();
        assertTrue(sessionFile.exists());
        checkFile(sessionFile);
        FileUtils.deleteDirectory(tempDirectory);
    }

    private void checkFile(File sessionFile) 
        throws FileNotFoundException, IOException {
        BufferedReader reader = new BufferedReader(new FileReader(sessionFile));
        assertEquals(reader.readLine(), "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        assertEquals(reader.readLine(), "<jnlp spec=\"1.5+\" codebase=\"http://caintegrator2.nci.nih.gov/caintegrator2/common/\">");
        assertEquals(reader.readLine(), "<information>");
        assertEquals(reader.readLine(), "<title>Heatmap</title>");
        assertEquals(reader.readLine(), "<vendor>National Cancer Institute, NIH/NCI/CCR/LPG</vendor>");
        assertEquals(reader.readLine(), "<homepage href=\"https://cgwb.nci.nih.gov/cgi-bin/heatmap\"/>");
        assertEquals(reader.readLine(), "<description>Heatmap viewer</description>");
        assertEquals(reader.readLine(), "<description kind=\"short\">");
        assertEquals(reader.readLine(), "Heatmap");
        assertEquals(reader.readLine(), "</description>");
        assertEquals(reader.readLine(), "</information>");
        assertEquals(reader.readLine(), "<security>");
        assertEquals(reader.readLine(), "<all-permissions/>");
        assertEquals(reader.readLine(), "</security>");
        assertEquals(reader.readLine(), "<resources>");
        assertEquals(reader.readLine(), "<j2se version=\"1.5+\" max-heap-size=\"512M\"/>");
        assertEquals(reader.readLine(), "<jar href=\"heatmap.jar\"/>");
        assertEquals(reader.readLine(), "</resources>");
        assertEquals(reader.readLine(), "<application-desc main-class=\"TCGA.Heatmap6\">");
        assertEquals(reader.readLine(), "<argument>-url-gm</argument>");
        assertEquals(reader.readLine(), "<argument>http://caintegrator2.nci.nih.gov/caintegrator2/heatmap/retrieveFile.jnlp?JSESSIONID=12345&amp;file=" + HeatmapFileTypeEnum.GENOMIC_DATA.getFilename() +  "</argument>");
        assertEquals(reader.readLine(), "<argument>-url-set</argument>");
        assertEquals(reader.readLine(), "<argument>http://caintegrator2.nci.nih.gov/caintegrator2/heatmap/retrieveFile.jnlp?JSESSIONID=12345&amp;file=" + HeatmapFileTypeEnum.LAYOUT.getFilename() +  "</argument>");
        assertEquals(reader.readLine(), "<argument>-url-annot</argument>");
        assertEquals(reader.readLine(), "<argument>http://caintegrator2.nci.nih.gov/caintegrator2/heatmap/retrieveFile.jnlp?JSESSIONID=12345&amp;file=" + HeatmapFileTypeEnum.ANNOTATIONS.getFilename() +  "</argument>");
        assertEquals(reader.readLine(), "<argument>-binary</argument>");
        assertEquals(reader.readLine(), "<argument>0</argument>");
        assertEquals(reader.readLine(), "<argument>-url-gz</argument>");
        assertEquals(reader.readLine(), "<argument>0</argument>");
        assertEquals(reader.readLine(), "</application-desc>");
        assertEquals(reader.readLine(), "</jnlp>");
        reader.close();
    }

}
