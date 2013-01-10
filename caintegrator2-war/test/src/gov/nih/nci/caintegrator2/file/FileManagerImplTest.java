/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.file;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import gov.nih.nci.caintegrator2.TestDataFiles;
import gov.nih.nci.caintegrator2.application.analysis.CBSToHeatmapFactory;
import gov.nih.nci.caintegrator2.application.analysis.heatmap.HeatmapFileTypeEnum;
import gov.nih.nci.caintegrator2.application.analysis.heatmap.HeatmapParameters;
import gov.nih.nci.caintegrator2.application.analysis.heatmap.HeatmapResult;
import gov.nih.nci.caintegrator2.application.analysis.igv.IGVFileTypeEnum;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.common.ConfigurationParameter;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.application.UserWorkspace;
import gov.nih.nci.caintegrator2.domain.genomic.GeneLocationConfiguration;
import gov.nih.nci.caintegrator2.domain.genomic.Platform;
import gov.nih.nci.caintegrator2.domain.genomic.SegmentData;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.mockito.AbstractMockitoTest;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class FileManagerImplTest extends AbstractMockitoTest {

    private static final String TEST_FILENAME = "testfile.csv";

    private FileManagerImpl fileManager;
    private AnalysisFileManagerImpl analysisFileManager;

    @Before
    public void setUp() throws Exception {
        fileManager = new FileManagerImpl();
        fileManager.setConfigurationHelper(configurationHelper);
        analysisFileManager = new AnalysisFileManagerImpl();
        analysisFileManager.setFileManager(fileManager);
    }

    @Test
    public void testStoreStudyFile() throws IOException {
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        studyConfiguration.getStudy().setId(1L);
        File storedFile = fileManager.storeStudyFile(TestDataFiles.VALID_FILE, TEST_FILENAME, studyConfiguration);
        storedFile.deleteOnExit();
        File expectedFile = new File(System.getProperty("java.io.tmpdir") + File.separator + "1" + File.separator + TEST_FILENAME);
        assertEquals(expectedFile.getCanonicalPath(), storedFile.getCanonicalPath());
        verify(configurationHelper, atLeastOnce()).getString(any(ConfigurationParameter.class));
        assertEquals(TestDataFiles.VALID_FILE.length(), storedFile.length());
    }

    @Test
    public void testDeleteStudyDirectory() throws IOException {
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        studyConfiguration.getStudy().setId(100L);
        File storedFile = fileManager.storeStudyFile(TestDataFiles.VALID_FILE, TEST_FILENAME, studyConfiguration);
        assertTrue(storedFile.exists());
        assertTrue(new File(System.getProperty("java.io.tmpdir") + File.separator + "100").exists());
        fileManager.deleteStudyDirectory(studyConfiguration);
        assertFalse(storedFile.exists());
        assertFalse(new File(System.getProperty("java.io.tmpdir") + File.separator + "100").exists());

    }

    @Test
    public void testGetNewTemporaryDirectory() {
        File newTemporaryDirectory = fileManager.getNewTemporaryDirectory("test");
        newTemporaryDirectory.deleteOnExit();
        File expectedTemporaryDirectory = new File(System.getProperty("java.io.tmpdir") +
                                                    File.separator +
                                                    "tmpDownload" +
                                                    File.separator + "test" );
        assertEquals(expectedTemporaryDirectory, newTemporaryDirectory);

        try {
            fileManager.getNewTemporaryDirectory(null);
            fail("Expcted illegal argument for null dir name.");
        } catch(IllegalArgumentException e) {
        }

        try {
            fileManager.getNewTemporaryDirectory("");
            fail("Expcted illegal argument for blank dir name.");
        } catch(IllegalArgumentException e) {
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStoreStudyFileIllegalArgument() throws IOException  {
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        fileManager.storeStudyFile(TestDataFiles.VALID_FILE, TEST_FILENAME, studyConfiguration);
    }

    @Test(expected = IOException.class)
    public void testStoreStudyFileIOException() throws IOException  {
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        studyConfiguration.getStudy().setId(1L);
        fileManager.storeStudyFile(TestDataFiles.INVALID_FILE_DOESNT_EXIST, TEST_FILENAME, studyConfiguration);
    }

    @Test
    public void testIGV() {
        Study study = new Study();
        study.setId(1L);
        File file = analysisFileManager.retrieveIGVFile(study, IGVFileTypeEnum.GENE_EXPRESSION, "Platform1");
        assertEquals("Platform1_igvGeneExpression.gct", file.getName());

        // Test getIGVDirectory
        boolean gotException = false;
        try {
            file = analysisFileManager.getIGVDirectory("");
        } catch(Exception e) {
            gotException = true;
        }
        assertTrue(gotException);
        gotException = false;
        try {
            file = analysisFileManager.getIGVDirectory("12345");
        } catch(Exception e) {
            gotException = true;
        }
        assertFalse(gotException);
        assertTrue(file.getName().equals("12345"));

        // Test deleteIGVDirectory
        gotException = false;
        try {
            analysisFileManager.deleteSessionDirectories("12345");
        } catch(Exception e) {
            gotException = true;
        }
        assertFalse(gotException);

    }

    @Test
    public void testHeatmap() throws IOException {
        Study study = new Study();
        study.setId(1L);
        StudySubscription studySubscription = new StudySubscription();
        studySubscription.setStudy(study);
        Platform platform = new Platform();
        platform.setName("platformName");

        File file = analysisFileManager.retrieveHeatmapFile(study, HeatmapFileTypeEnum.CALLS_DATA, "Platform1");
        assertEquals("Platform1_heatmapCallsData.txt", file.getName());

        HeatmapParameters parameters = new HeatmapParameters();
        parameters.setPlatform(platform);
        parameters.setSessionId("12345");
        parameters.setStudySubscription(studySubscription);
        HeatmapResult result = new HeatmapResult();
        Set<SegmentData> segmentDatas = new HashSet<SegmentData>();
        GeneLocationConfiguration geneLocationConfiguration = new GeneLocationConfiguration();

        CBSToHeatmapFactory cbsToHeatmapFactory = mock(CBSToHeatmapFactory.class);
        analysisFileManager.createHeatmapGenomicFile(parameters, result, segmentDatas, geneLocationConfiguration, cbsToHeatmapFactory);

        assertEquals("heatmapGenomicData.txt", result.getGenomicDataFile().getName());
        assertEquals("chr2genecount.dat", result.getLayoutFile().getName());

        parameters.setUseCGHCall(true);
        analysisFileManager.createHeatmapGenomicFile(parameters, result, segmentDatas, geneLocationConfiguration, cbsToHeatmapFactory);
        assertEquals("heatmapCallsData.txt", result.getGenomicDataFile().getName());
        assertEquals("chr2genecount.dat", result.getLayoutFile().getName());


        parameters.setViewAllData(true);
        parameters.setUseCGHCall(false);
        analysisFileManager.createHeatmapGenomicFile(parameters, result, segmentDatas, geneLocationConfiguration, cbsToHeatmapFactory);
        assertEquals("platformName_heatmapGenomicData.txt", result.getGenomicDataFile().getName());
        assertEquals("platformName_chr2genecount.dat", result.getLayoutFile().getName());

        parameters.setViewAllData(true);
        parameters.setUseCGHCall(true);
        analysisFileManager.createHeatmapGenomicFile(parameters, result, segmentDatas, geneLocationConfiguration, cbsToHeatmapFactory);
        assertEquals("platformName_heatmapCallsData.txt", result.getGenomicDataFile().getName());
        assertEquals("platformName_chr2genecount.dat", result.getLayoutFile().getName());
    }

    @Test
    public void testGetUserDirectory() {
        StudySubscription studySubscription = new StudySubscription();
        File userDirectory = null;
        boolean gotException = false;
        try {
            userDirectory = fileManager.getUserDirectory(studySubscription);
        } catch(Exception e) {
            gotException = true;
        }
        assertTrue(gotException);

        UserWorkspace userWorkspace = new UserWorkspace();
        userWorkspace.setUsername("TestUser");
        studySubscription.setUserWorkspace(userWorkspace);
        gotException = false;
        try {
            userDirectory = fileManager.getUserDirectory(studySubscription);
        } catch(Exception e) {
            gotException = true;
        }
        assertFalse(gotException);
        assertTrue(userDirectory.exists());
    }

}
