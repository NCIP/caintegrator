/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.file;

import edu.wustl.icr.asrv1.segment.SampleWithChromosomalSegmentSet;
import gov.nih.nci.caintegrator2.application.analysis.GctDataset;
import gov.nih.nci.caintegrator2.application.analysis.SampleClassificationParameterValue;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.domain.application.AbstractPersistedAnalysisJob;
import gov.nih.nci.caintegrator2.domain.application.ResultsZipFile;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.translational.Study;

import java.io.File;
import java.io.IOException;

import org.genepattern.gistic.Marker;

/**
 * Provides file storage and retrieval functionality.
 */
public interface FileManager {
    
    /**
     * Persists a file in caIntegrator 2. This allows temp files to be copied into permanent storage associated
     * with a study.
     * 
     * @param sourceFile the contents to be stored. The source file may be deleted after the completion of this method.
     * @param filename the filename to use for the file (may be different from sourceFile.getName()).
     * @param studyConfiguration file is associated with this studyConfiguration.
     * @return the permanent file.
     * @throws IOException if the file couldn't be copied to its destination.
     */
    File storeStudyFile(File sourceFile, String filename, StudyConfiguration studyConfiguration) throws IOException;
    
    /**
     * Deletes the storage directory for the given study.
     * @param studyConfiguration for the directory to delete.
     */
    void deleteStudyDirectory(StudyConfiguration studyConfiguration);
        
    /**
     * Returns the storage directory for the given study.
     * 
     * @param study get directory for this study.
     * @return the directory.
     */
    File getStudyDirectory(Study study);


    /**
     * Creates a temporary directory to use based on the given directory name.
     * @param dirName temporary directory name to use (just name, not path).
     * @return the directory.
     */
    File getNewTemporaryDirectory(String dirName);
    
    /**
     * Retrieves the directory for the study subscription's user.
     * @param studySubscription to get username from.
     * @return the directory.
     */
    File getUserDirectory(StudySubscription studySubscription);
    
    /**
     * Creates a file (with given name) in the study directory.
     * @param studySubscription to get directory for user.
     * @param filename of file to return.
     * @return newly created file.
     */
    File createNewStudySubscriptionFile(StudySubscription studySubscription, String filename);
    
    /**
     * Creates a classification file (.cls) for the given sampleClassifications.
     * @param studySubscription to store file to user directory.
     * @param sampleClassifications for classifying samples.
     * @param clsFilename name of file.
     * @return .cls file.
     */
    File createClassificationFile(StudySubscription studySubscription,
            SampleClassificationParameterValue sampleClassifications, String clsFilename);
    
    /**
     * Creates a GCT file based on the gctDataset.
     * @param studySubscription to store file to study folder.
     * @param gctDataset for genomic data to be written to file.
     * @param filename name of file.
     * @return .gct file.
     */
    File createGctFile(StudySubscription studySubscription, GctDataset gctDataset, String filename);

    /**
     * Creates samples file for GISTIC input.
     * @param studySubscription to store file to study folder.
     * @param samples to write to file.
     * @return gistic input sample segment file.
     * @throws IOException if unable to write file.
     */
    File createSamplesFile(StudySubscription studySubscription, SampleWithChromosomalSegmentSet[] samples)
    throws IOException;
    
    /**
     * Creates markers file for GISTIC input.
     * @param studySubscription to store file to study folder.
     * @param markers to write to file
     * @return gistic input markers file.
     * @throws IOException if unable to write file.
     */
    File createMarkersFile(StudySubscription studySubscription, Marker[] markers) throws IOException;
    
    /**
     * Renames the cnvFile so it can be used by GISTIC grid service.
     * @param cnvFile to rename.
     * @return new cnvFile
     * @throws IOException if unable to rename.
     */
    File renameCnvFile(File cnvFile) throws IOException;
    
    /**
     * Creates the input zip file for a given job.
     * @param studySubscription to store file to study folder.
     * @param job analysis job.
     * @param filename of the zip file to be created.
     * @param files set of input files.
     * @return input zip file.
     */
    ResultsZipFile createInputZipFile(StudySubscription studySubscription, AbstractPersistedAnalysisJob job,
            String filename, File... files);
}
