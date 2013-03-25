/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.analysis.grid;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import gov.nih.nci.caintegrator.TestDataFiles;
import gov.nih.nci.caintegrator.application.analysis.GenePatternGridClientFactoryImpl;
import gov.nih.nci.caintegrator.application.analysis.StatusUpdateListener;
import gov.nih.nci.caintegrator.application.analysis.grid.comparativemarker.ComparativeMarkerSelectionParameters;
import gov.nih.nci.caintegrator.application.analysis.grid.gistic.GisticParameters;
import gov.nih.nci.caintegrator.application.analysis.grid.gistic.GisticRefgeneFileEnum;
import gov.nih.nci.caintegrator.application.analysis.grid.pca.PCAParameters;
import gov.nih.nci.caintegrator.application.analysis.grid.preprocess.PreprocessDatasetParameters;
import gov.nih.nci.caintegrator.application.query.InvalidCriterionException;
import gov.nih.nci.caintegrator.data.StudyHelper;
import gov.nih.nci.caintegrator.domain.application.ComparativeMarkerSelectionAnalysisJob;
import gov.nih.nci.caintegrator.domain.application.GisticAnalysisJob;
import gov.nih.nci.caintegrator.domain.application.PrincipalComponentAnalysisJob;
import gov.nih.nci.caintegrator.domain.application.StudySubscription;
import gov.nih.nci.caintegrator.domain.application.UserWorkspace;
import gov.nih.nci.caintegrator.domain.genomic.ArrayDataType;
import gov.nih.nci.caintegrator.external.ConnectionException;
import gov.nih.nci.caintegrator.external.ParameterException;
import gov.nih.nci.caintegrator.external.ServerConnectionProfile;
import gov.nih.nci.caintegrator.file.FileManager;
import gov.nih.nci.caintegrator.file.FileManagerImpl;
import gov.nih.nci.caintegrator.mockito.AbstractMockitoTest;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.genepattern.cabig.util.ZipUtils;
import org.genepattern.gistic.common.GisticUtils;
import org.genepattern.io.ParseException;
import org.genepattern.io.odf.OdfObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import au.com.bytecode.opencsv.CSVReader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/**/genepattern-test-config.xml" })
public class GenePatternGridRunnerImplTestIntegration extends AbstractMockitoTest {

    private static final String PREPROCESS_DATASET_URL =
            "http://cagrid.broadinstitute.org:6060/wsrf/services/cagrid/PreprocessDatasetMAGEService";
    private static final String COMPARATIVE_MARKER_URL =
            "http://cagrid.broadinstitute.org:11010/wsrf/services/cagrid/ComparativeMarkerSelMAGESvc";
    private static final String PCA_URL = "http://cagrid.broadinstitute.org:6060/wsrf/services/cagrid/PCA";
    private static final String GISTIC_URL = "http://cagrid.broadinstitute.org:10010/wsrf/services/cagrid/Gistic";

    private GenePatternGridRunnerImpl genePatternGridRunner;
    private FileManager fileManager;
    private StudyHelper studyHelper;
    private StatusUpdateListener updater;

    @Before
    public void setUp() throws Exception {
        updater = mock(StatusUpdateListener.class);
        FileManagerImpl fManager = new FileManagerImpl();
        fManager.setConfigurationHelper(configurationHelper);

        fileManager = fManager;
        genePatternGridRunner = new GenePatternGridRunnerImpl();
        genePatternGridRunner.setFileManager(fileManager);
        genePatternGridRunner.setGenePatternGridClientFactory(new GenePatternGridClientFactoryImpl());
    }

    private StudySubscription setupStudySubscription(ArrayDataType arrayDataType) {
        studyHelper = new StudyHelper();
        studyHelper.setArrayDataType(arrayDataType);
        StudySubscription subscription = studyHelper.populateAndRetrieveStudy();
        UserWorkspace userWorkspace = new UserWorkspace();
        userWorkspace.setUsername("testCaseUser123");
        subscription.setUserWorkspace(userWorkspace);
        return subscription;
    }

    @Test
    public void testRunPreprocessDataset() throws ConnectionException, InvalidCriterionException, IOException {
        StudySubscription subscription = setupStudySubscription(ArrayDataType.GENE_EXPRESSION);
        ServerConnectionProfile server = new ServerConnectionProfile();
        server.setUrl(PREPROCESS_DATASET_URL);
        PreprocessDatasetParameters preprocessParameters = new PreprocessDatasetParameters();
        preprocessParameters.setServer(server);
        PrincipalComponentAnalysisJob job = new PrincipalComponentAnalysisJob();
        job.setSubscription(subscription);

        preprocessParameters.setProcessedGctFilename("preprocessTest.gct");
        File gctFile = new File(fileManager.getUserDirectory(subscription) + File.separator
                + preprocessParameters.getProcessedGctFilename());
        FileUtils.copyFile(TestDataFiles.PCA_TRAIN_FILE, gctFile);
        preprocessParameters.getDatasetParameters().setLogBaseTwo(true);
        genePatternGridRunner.runPreprocessDataset(updater, job, preprocessParameters, gctFile);
        checkGctFile(gctFile, 97, 38, "4.321928094887363");
    }

    @Test
    public void testRunPreprocessComparativeMarker() throws ConnectionException, IOException,
            InvalidCriterionException, ParseException {
        StudySubscription subscription = setupStudySubscription(ArrayDataType.GENE_EXPRESSION);
        ServerConnectionProfile server = new ServerConnectionProfile();
        server.setUrl(PREPROCESS_DATASET_URL);
        PreprocessDatasetParameters preprocessParameters = new PreprocessDatasetParameters();
        preprocessParameters.setServer(server);
        ComparativeMarkerSelectionAnalysisJob job = new ComparativeMarkerSelectionAnalysisJob();
        job.setSubscription(subscription);
        job.getForm().setPreprocessDatasetparameters(preprocessParameters);
        preprocessParameters.setProcessedGctFilename("cmsTest.gct");
        preprocessParameters.getDatasetParameters().setLogBaseTwo(false);
        preprocessParameters.getDatasetParameters().setThreshold(35);
        ComparativeMarkerSelectionParameters comparativeMarkerParameters = new ComparativeMarkerSelectionParameters();
        comparativeMarkerParameters.setClassificationFileName("cmsTest.cls");
        ServerConnectionProfile comparativeMarkerServer = new ServerConnectionProfile();
        comparativeMarkerServer.setUrl(COMPARATIVE_MARKER_URL);
        comparativeMarkerParameters.setServer(comparativeMarkerServer);
        job.getForm().setComparativeMarkerSelectionParameters(comparativeMarkerParameters);
        File gctFile = new File(fileManager.getUserDirectory(subscription) + File.separator
                + preprocessParameters.getProcessedGctFilename());
        File clsFile = new File(fileManager.getUserDirectory(subscription) + File.separator
                + comparativeMarkerParameters.getClassificationFileName());
        FileUtils.copyFile(TestDataFiles.CMS_GCT_FILE, gctFile);
        FileUtils.copyFile(TestDataFiles.CMS_CLASSIFICATIONS_FILE, clsFile);
        File results = genePatternGridRunner.runPreprocessComparativeMarkerSelection(updater, job, gctFile, clsFile);
        assertNotNull(results);
        List<String> filenames = ZipUtils.extractFromZip(results.getAbsolutePath());
        assertEquals(3, filenames.size());
        int validFiles = 0;
        for (String filename : filenames) {
            File file = new File(filename);
            if (filename.contains(".gct")) {
                checkGctFile(file, 41, 35, "-214.0");
                validFiles++;
            } else if (filename.contains(".cls")) {
                validFiles++;
            } else if (filename.contains(".odf")) {
                OdfObject odf = new OdfObject(filename);
                assertEquals(41, odf.getRowCount());
                validFiles++;
            }
            FileUtils.deleteQuietly(file);
        }
        assertEquals(3, validFiles);
        FileUtils.deleteQuietly(fileManager.getUserDirectory(subscription));
    }

    @Test
    public void testRunPCA() throws ConnectionException, InvalidCriterionException, IOException {
        StudySubscription subscription = setupStudySubscription(ArrayDataType.GENE_EXPRESSION);
        ServerConnectionProfile server = new ServerConnectionProfile();
        server.setUrl(PCA_URL);
        PCAParameters parameters = new PCAParameters();
        PrincipalComponentAnalysisJob job = new PrincipalComponentAnalysisJob();
        job.setSubscription(subscription);
        job.getForm().setPcaParameters(parameters);
        parameters.setClusterBy("columns");
        parameters.setGctFileName("testPca.gct");
        parameters.setServer(server);
        File zipFile = null;
        File gctFile = new File(fileManager.getUserDirectory(subscription) + File.separator
                + parameters.getGctFileName());
        FileUtils.copyFile(TestDataFiles.PCA_TRAIN_FILE, gctFile);
        zipFile = genePatternGridRunner.runPCA(updater, job, gctFile);
        assertNotNull(zipFile);
        zipFile.deleteOnExit();
        FileUtils.deleteQuietly(fileManager.getUserDirectory(subscription));
    }

    @Test
    public void testRunGistic() throws ConnectionException, InvalidCriterionException, IOException, ParameterException {
        StudySubscription subscription = setupStudySubscription(ArrayDataType.COPY_NUMBER);
        GisticParameters parameters = new GisticParameters();
        parameters.setRefgeneFile(GisticRefgeneFileEnum.HUMAN_HG16);
        parameters.getServer().setUrl(GISTIC_URL);
        File zipFile = null;
        GisticAnalysisJob job = new GisticAnalysisJob();
        job.setSubscription(subscription);
        job.getGisticAnalysisForm().setGisticParameters(parameters);
        File samplesFile = new File(fileManager.getUserDirectory(subscription) + File.separator
                + GisticUtils.SAMPLE_FILE_PREFIX + "gisticSamplesFile.txt");
        File markersFile = new File(fileManager.getUserDirectory(subscription) + File.separator
                + GisticUtils.MARKER_FILE_PREFIX + "gisticMarkersFile.txt");
        File cnvFile = new File(fileManager.getUserDirectory(subscription) + File.separator
                + GisticUtils.CNV_SEGMENT_FILE_PREFIX + "gisticCnvFile.txt");
        FileUtils.copyFile(TestDataFiles.GISTIC_SAMPLES_FILE, samplesFile);
        FileUtils.copyFile(TestDataFiles.GISTIC_MARKERS_FILE, markersFile);
        FileUtils.copyFile(TestDataFiles.GISTIC_CNV_FILE, cnvFile);

        zipFile = genePatternGridRunner.runGistic(updater, job, samplesFile, markersFile, cnvFile);
        assertNotNull(zipFile);
        List<String> filenames = ZipUtils.extractFromZip(zipFile.getAbsolutePath());
        // if ZIP contains 3 files or less, this typically means the GP module failed to complete.
        boolean isNumFilesGreaterThan3 = filenames.size() > 3;
        String holdStr = "ZIP archive from GISTIC contains too few files: ";
        holdStr = holdStr.concat(Integer.toString(filenames.size()));
        assertTrue(holdStr, isNumFilesGreaterThan3);
        // If ZIP contains 9 files, this is the expected output from the GISTIC module.
        assertEquals(9, filenames.size());
        zipFile.deleteOnExit();
        FileUtils.deleteQuietly(fileManager.getUserDirectory(subscription));
    }

    private void checkGctFile(File gctFile, int numSamples, int numReporters, String value) throws IOException {
        assertTrue("GCT file does not exist.", gctFile.exists());
        CSVReader reader = new CSVReader(new FileReader(gctFile), '\t');
        checkLine(reader.readNext(), "#1.2");
        checkLine(reader.readNext(), String.valueOf(numSamples), String.valueOf(numReporters));
        reader.readNext(); // Header
        checkValue(reader.readNext(), value); // Row values
    }

    private void checkValue(String[] readNext, String f) {
        assertEquals(f, readNext[2]);
    }

    private void checkLine(String[] line, String... expecteds) {
        assertArrayEquals(expecteds, line);
    }

}
