/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.file;

import edu.wustl.icr.asrv1.segment.SampleWithChromosomalSegmentSet;
import gov.nih.nci.caintegrator2.application.analysis.ClassificationsToClsConverter;
import gov.nih.nci.caintegrator2.application.analysis.GctDataset;
import gov.nih.nci.caintegrator2.application.analysis.GctDatasetFileWriter;
import gov.nih.nci.caintegrator2.application.analysis.SampleClassificationParameterValue;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.common.ConfigurationHelper;
import gov.nih.nci.caintegrator2.common.ConfigurationParameter;
import gov.nih.nci.caintegrator2.domain.application.AbstractPersistedAnalysisJob;
import gov.nih.nci.caintegrator2.domain.application.ResultsZipFile;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.translational.Study;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.genepattern.cabig.util.ZipUtils;
import org.genepattern.gistic.Marker;
import org.genepattern.gistic.common.GisticUtils;

/**
 * Implementation of file storage and retrieval subsystem.
 */
public class FileManagerImpl implements FileManager {
    
    private ConfigurationHelper configurationHelper;
    private static final Logger LOGGER = Logger.getLogger(FileManagerImpl.class);
    
    /**
     * {@inheritDoc}
     */
    public File storeStudyFile(File sourceFile, String filename, StudyConfiguration studyConfiguration) 
    throws IOException  {
        File destFile = new File(getStudyDirectory(studyConfiguration), filename);
        try {
            FileUtils.copyFile(sourceFile, destFile);
        } catch (IOException e) {
            LOGGER.error("Couldn't copy " + sourceFile.getAbsolutePath() + " to " + destFile.getAbsolutePath(), e);
            throw e;
        }
        return destFile;
    }
    
    /**
     * {@inheritDoc}
     */
    public void deleteStudyDirectory(StudyConfiguration studyConfiguration) {
        FileUtils.deleteQuietly(getStudyDirectory(studyConfiguration));
    }
    
    /**
     * {@inheritDoc}
     */
    public File getNewTemporaryDirectory(String dirName) {
        if (StringUtils.isBlank(dirName)) {
            throw new IllegalArgumentException("Directory name is null or blank.");
        }
        File newTemporaryDirectory = new File(getConfigurationHelper().
                                                getString(ConfigurationParameter.TEMP_DOWNLOAD_STORAGE_DIRECTORY), 
                                                dirName);
        newTemporaryDirectory.mkdirs();
        return newTemporaryDirectory;
    }
    
    private File getStudyDirectory(StudyConfiguration studyConfiguration) {
        return getStudyDirectory(studyConfiguration.getStudy());
    }
    
    /**
     * {@inheritDoc}
     */
    public File getUserDirectory(StudySubscription studySubscription) {
        if (studySubscription.getUserWorkspace() == null 
            || studySubscription.getUserWorkspace().getUsername() == null) {
            throw new IllegalArgumentException("Couldn't determine username from the StudySubscription.");
        }
        File userDirectory = new File(getUserRootDirectory(), studySubscription.getUserWorkspace().getUsername());
        userDirectory.mkdirs();
        return userDirectory;
    }
    
    private File getUserRootDirectory() {
        return new File(getConfigurationHelper().getString(ConfigurationParameter.USER_FILE_STORAGE_DIRECTORY));
    }


    /**
     * {@inheritDoc}
     */
    public File getStudyDirectory(Study study) {
        if (study.getId() == null) {
            throw new IllegalArgumentException("Study has not been saved.");
        }
        return new File(getStorageRootDirectory(), study.getId().toString());
    }

    private File getStorageRootDirectory() {
        return new File(getConfigurationHelper().getString(ConfigurationParameter.STUDY_FILE_STORAGE_DIRECTORY));
    }
    
    /**
     * {@inheritDoc}
     */
    public File createNewStudySubscriptionFile(StudySubscription studySubscription, String filename) {
        return new File(getUserDirectory(studySubscription) + File.separator 
                + filename);
    }
    
    /**
     * {@inheritDoc}
     */
    public File createClassificationFile(StudySubscription studySubscription,
            SampleClassificationParameterValue sampleClassifications, String clsFilename) {
        return ClassificationsToClsConverter.writeAsCls(sampleClassifications, 
                createNewStudySubscriptionFile(studySubscription, clsFilename).getAbsolutePath());
    }
    
    /**
     * {@inheritDoc}
     */
    public File createGctFile(StudySubscription studySubscription, GctDataset gctDataset, String filename) {
        return GctDatasetFileWriter.writeAsGct(gctDataset, 
                createNewStudySubscriptionFile(studySubscription, filename).getAbsolutePath());
    }
    
    /**
     * {@inheritDoc}
     */
    public File createSamplesFile(StudySubscription studySubscription, SampleWithChromosomalSegmentSet[] samples)
            throws IOException {
        File tmpFile = GisticUtils.writeSampleFile(samples); 
        File resultFile = createNewStudySubscriptionFile(studySubscription, tmpFile.getName());
        FileUtils.moveFile(tmpFile, resultFile);
        return resultFile;
    }
    
    /**
     * {@inheritDoc}
     */
    public File createMarkersFile(StudySubscription studySubscription, Marker[] markers) throws IOException {
        File tmpFile = GisticUtils.writeMarkersFile(markers); 
        File resultFile = createNewStudySubscriptionFile(studySubscription, tmpFile.getName());
        FileUtils.moveFile(tmpFile, resultFile);
        return resultFile;
    }
    
    /**
     * {@inheritDoc}
     */
    public File renameCnvFile(File cnvFile) throws IOException {
        File newCnvFile = new File(cnvFile.getParent(), 
                GisticUtils.CNV_SEGMENT_FILE_PREFIX + System.currentTimeMillis() + ".txt");
        FileUtils.copyFile(cnvFile, newCnvFile);
        cnvFile.delete();
        return newCnvFile;
    }
    
    /**
     * {@inheritDoc}
     */
    public ResultsZipFile createInputZipFile(StudySubscription studySubscription, AbstractPersistedAnalysisJob job,
            String filename, File... files) {
        try {
            File inputParametersFile = createNewStudySubscriptionFile(studySubscription, "inputParameters_"
                    + System.currentTimeMillis() + ".txt");
            job.writeJobDescriptionToFile(inputParametersFile);
            Set<File> fileSet = new HashSet<File>(Arrays.asList(files));
            fileSet.add(inputParametersFile);
            File tmpZipFile = new File(ZipUtils.writeZipFile(fileSet));
            ResultsZipFile inputZipFile = new ResultsZipFile();
            inputZipFile.setPath(createNewStudySubscriptionFile(studySubscription, filename).
                    getAbsolutePath());
            FileUtils.moveFile(tmpZipFile, inputZipFile.getFile());
            inputParametersFile.delete();
            return inputZipFile;
        } catch (IOException e) {
            throw new IllegalArgumentException("Couldn't create the input zip file.", e);
        }
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
