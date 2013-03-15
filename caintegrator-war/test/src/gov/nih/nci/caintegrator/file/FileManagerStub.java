/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.file;

import edu.wustl.icr.asrv1.segment.SampleWithChromosomalSegmentSet;
import gov.nih.nci.caintegrator.application.analysis.GctDataset;
import gov.nih.nci.caintegrator.application.analysis.SampleClassificationParameterValue;
import gov.nih.nci.caintegrator.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator.common.ConfigurationHelper;
import gov.nih.nci.caintegrator.domain.application.AbstractPersistedAnalysisJob;
import gov.nih.nci.caintegrator.domain.application.ResultsZipFile;
import gov.nih.nci.caintegrator.domain.application.StudySubscription;
import gov.nih.nci.caintegrator.domain.translational.Study;

import java.io.File;
import java.io.IOException;

import org.genepattern.gistic.Marker;

/**
 * In use by one integration test: query-test-integration. DO NOT USE OTHERWISE!!!
 * If you need File Manager look in AbstractMockitoTest.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 *
 */
public class FileManagerStub implements FileManager {

    public boolean storeStudyFileCalled;
    public boolean deleteStudyDirectoryCalled;
    public boolean renameCnvFileCalled;
    public boolean createClassificationFileCalled;
    public boolean createGctFileCalled;
    public boolean createInputZipFileCalled;
    public boolean createMarkersFileCalled;
    public boolean createSamplesFileCalled;

    private ConfigurationHelper configurationHelper;


    public void clear() {
        storeStudyFileCalled = false;
        deleteStudyDirectoryCalled = false;
        renameCnvFileCalled = false;
        createClassificationFileCalled = false;
        createGctFileCalled = false;
        createInputZipFileCalled = false;
        createMarkersFileCalled = false;
        createSamplesFileCalled = false;
    }

    public File storeStudyFile(File sourceFile, String filename, StudyConfiguration studyConfiguration) {
        storeStudyFileCalled = true;
        return sourceFile;
    }

    public File getStudyDirectory(Study study) {
        return new File(System.getProperty("java.io.tmpdir"));
    }

    public File getNewTemporaryDirectory(String dirName) {
        return new File(System.getProperty("java.io.tmpdir"));
    }

    public File getUserDirectory(StudySubscription studySubscription) {
        return new File(System.getProperty("java.io.tmpdir"));
    }

    public void deleteStudyDirectory(StudyConfiguration studyConfiguration) {
        deleteStudyDirectoryCalled = true;
    }

    public File createNewStudySubscriptionFile(StudySubscription studySubscription, String filename) {
        return retrieveTmpFile();
    }

    public File createClassificationFile(StudySubscription studySubscription,
            SampleClassificationParameterValue sampleClassifications, String clsFilename) {
        createClassificationFileCalled = true;
        retrieveTmpFile();
        return retrieveTmpFile();
    }

    public File retrieveTmpFile() {
        File tmpFile = new File(System.getProperty("java.io.tmpdir") + File.separator + "tmpFile");
        tmpFile.deleteOnExit();
        return tmpFile;
    }

    public File renameCnvFile(File cnvFile) throws IOException {
        renameCnvFileCalled = true;
        return cnvFile;
    }

    public File createGctFile(StudySubscription studySubscription, GctDataset gctDataset, String filename) {
        createGctFileCalled = true;
        return retrieveTmpFile();
    }

    public ResultsZipFile createInputZipFile(StudySubscription studySubscription, AbstractPersistedAnalysisJob job,
            String filename, File... files) {
        createInputZipFileCalled = true;
        ResultsZipFile zipFile = new ResultsZipFile();
        zipFile.setPath(retrieveTmpFile().getAbsolutePath());
        return zipFile;
    }

    public File createMarkersFile(StudySubscription studySubscription, Marker[] markers) throws IOException {
        createMarkersFileCalled = true;
        return retrieveTmpFile();
    }

    public File createSamplesFile(StudySubscription studySubscription, SampleWithChromosomalSegmentSet[] samples)
            throws IOException {
        createSamplesFileCalled = true;
        return retrieveTmpFile();
    }

    public String getTempDirectory() {
        return retrieveTmpFile().getAbsolutePath();
    }

    /**
     * @return the configurationHelper
     */
    public ConfigurationHelper getConfigurationHelper() {
        return configurationHelper;
    }

    /**
     * @param configurationHelper the configurationHelper to set
     */
    public void setConfigurationHelper(ConfigurationHelper configurationHelper) {
        this.configurationHelper = configurationHelper;
    }
}
