package gov.nih.nci.caintegrator2.file;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import gov.nih.nci.caintegrator2.TestDataFiles;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.application.study.StudyManagementServiceTest;
import gov.nih.nci.caintegrator2.common.ConfigurationHelperStub;
import gov.nih.nci.caintegrator2.common.ConfigurationParameter;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class FileManagerImplTest {

    private static final String TEST_FILENAME = "testfile.csv";
    
    private FileManager fileManager;
    private ConfigurationHelperStub configurationHelperStub;

    @Before
    public void setUp() throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("studymanagement-test-config.xml", StudyManagementServiceTest.class); 
        fileManager = (FileManager) context.getBean("fileManager"); 
        configurationHelperStub = (ConfigurationHelperStub) context.getBean("configurationHelperStub");
        configurationHelperStub.clear();                
    }

    @Test
    public void testStoreStudyFile() throws IOException {
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        studyConfiguration.getStudy().setId(1L);
        File storedFile = fileManager.storeStudyFile(TestDataFiles.VALID_FILE, TEST_FILENAME, studyConfiguration);
        storedFile.deleteOnExit();
        File expectedFile = new File(System.getProperty("java.io.tmpdir") + File.separator + "1" + File.separator + TEST_FILENAME);
        assertEquals(expectedFile.getCanonicalPath(), storedFile.getCanonicalPath());
        assertTrue(configurationHelperStub.getStringCalled);
        assertEquals(ConfigurationParameter.STUDY_FILE_STORAGE_DIRECTORY, configurationHelperStub.parameterPassed);
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
        assertEquals(ConfigurationParameter.TEMP_DOWNLOAD_STORAGE_DIRECTORY, configurationHelperStub.parameterPassed);
        
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

}