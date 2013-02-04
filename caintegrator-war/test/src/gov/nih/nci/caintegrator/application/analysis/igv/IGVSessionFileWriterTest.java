/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.analysis.igv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator.TestDataFiles;
import gov.nih.nci.caintegrator.application.analysis.igv.IGVResult;
import gov.nih.nci.caintegrator.application.analysis.igv.IGVSessionFileWriter;
import gov.nih.nci.caintegrator.domain.genomic.GenomeBuildVersionEnum;
import gov.nih.nci.caintegrator.domain.genomic.Platform;
import gov.nih.nci.caintegrator.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator.domain.genomic.ReporterTypeEnum;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

/**
 * 
 */
public class IGVSessionFileWriterTest {

    @Test
    public void testWriteSessionFile() throws IOException {
        String urlPrefix = "http://caintegrator2.nci.nih.gov/caintegrator2/igv/retrieveFile.jnlp?JSESSIONID=12345&file=";
        File tempDirectory = new File(System.getProperty("java.io.tmpdir") + File.separator + "igvTmp");
        tempDirectory.mkdir();
        IGVResult igvResult = new IGVResult();
        igvResult.setGeneExpressionFile(TestDataFiles.VALID_FILE);
        igvResult.setSegmentationFile(TestDataFiles.VALID_FILE);
        igvResult.setSampleInfoFile(TestDataFiles.VALID_FILE);
        
        // Check it with no platforms.
        Set<Platform> platforms = new HashSet<Platform>();
        IGVSessionFileWriter.writeSessionFile(tempDirectory, 
                urlPrefix, igvResult, platforms);
        File sessionFile = igvResult.getSessionFile();
        assertTrue(sessionFile.exists());
        checkFile(sessionFile, GenomeBuildVersionEnum.HG18);
        
        // Check it with HG19 platform
        Platform platform = new Platform();
        ReporterList reporterList = 
            platform.addReporterList("reporterList", ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET);
        reporterList.setGenomeVersion("g19");
        platforms.add(platform);
        IGVSessionFileWriter.writeSessionFile(tempDirectory, 
                urlPrefix, igvResult, platforms);
        sessionFile = igvResult.getSessionFile();
        assertTrue(sessionFile.exists());
        checkFile(sessionFile, GenomeBuildVersionEnum.HG19);
        
        // Check it with an HG 17 and HG 19 platform - should default to HG18 in this case
        Platform platform2 = new Platform();
        ReporterList reporterList2 = 
            platform2.addReporterList("reporterList", ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET);
        reporterList2.setGenomeVersion("g17");
        platforms.add(platform2);
        IGVSessionFileWriter.writeSessionFile(tempDirectory, 
                urlPrefix, igvResult, platforms);
        sessionFile = igvResult.getSessionFile();
        assertTrue(sessionFile.exists());
        checkFile(sessionFile, GenomeBuildVersionEnum.HG18);
        FileUtils.deleteDirectory(tempDirectory);
    }

    private void checkFile(File sessionFile, GenomeBuildVersionEnum genomeVersion) 
        throws FileNotFoundException, IOException {
        BufferedReader reader = new BufferedReader(new FileReader(sessionFile));
        assertEquals(reader.readLine(), "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        assertEquals(reader.readLine(), "<Session genome=\"" + genomeVersion.getValue() + "\" locus=\"All\" version=\"3\">");
        assertEquals(reader.readLine(), "<Resources>");
        assertEquals(
                reader.readLine(),
                "<Resource path=\"http://caintegrator2.nci.nih.gov/"
                + "caintegrator2/igv/retrieveFile.jnlp?JSESSIONID=12345&amp;file=igvGeneExpression.gct\"/>");
        assertEquals(
                reader.readLine(),
                "<Resource path=\"http://caintegrator2.nci.nih.gov/"
                + "caintegrator2/igv/retrieveFile.jnlp?JSESSIONID=12345&amp;file=igvSegmentation.seg\"/>");
        assertEquals(
                reader.readLine(),
                "<Resource path=\"http://caintegrator2.nci.nih.gov/"
                + "caintegrator2/igv/retrieveFile.jnlp?JSESSIONID=12345&amp;file=sampleInfo.txt\"/>");
        assertEquals(reader.readLine(), "</Resources>");
        assertEquals(reader.readLine(), "</Session>");
        reader.close();
    }

}
