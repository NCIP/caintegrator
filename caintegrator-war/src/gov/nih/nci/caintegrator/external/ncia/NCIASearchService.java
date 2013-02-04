/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.external.ncia;

import gov.nih.nci.caintegrator.external.ConnectionException;
import gov.nih.nci.ncia.domain.Image;
import gov.nih.nci.ncia.domain.Patient;
import gov.nih.nci.ncia.domain.Series;
import gov.nih.nci.ncia.domain.Study;

import java.util.List;

/**
 * Sends off queries to the NCIA system and returns back the appropriate NCIA objects.
 */
public interface NCIASearchService {

    /**
     * Retrieves a list of all different Projects from the CollectionName object.
     * @return List of Projects in String format.
     * @throws ConnectionException if there's a problem connecting to the NCIA server.
     */
    List<String> retrieveAllCollectionNameProjects() throws ConnectionException;

    /**
     * Retrieves a list of all Patients given an Project.
     * @param collectionNameProject project to find the patients of.
     * @return List of Patients
     * @throws ConnectionException if there's a problem connecting to the NCIA server.
     */
    List<Patient> retrievePatientCollectionFromCollectionNameProject(String collectionNameProject)
            throws ConnectionException;
    
    /**
     * Retrieves a list of all Patient Identifiers given an Project.
     * @param collectionNameProject project to find the patients of.
     * @return List of Patient Identifiers.
     * @throws ConnectionException if there's a problem connecting to the NCIA server.
     */
    List<String> retrievePatientCollectionIdsFromCollectionNameProject(String collectionNameProject) 
        throws ConnectionException;

    /**
     * Retrieves a list of all Studies given a Patient ID.
     * @param patientId patient ID to find the studies for.
     * @return List of Studies
     * @throws ConnectionException if there's a problem connecting to the NCIA server.
     */
    List<Study> retrieveStudyCollectionFromPatient(String patientId) throws ConnectionException;
    
    /**
     * Retrieves a list of all Study IDs given a Patient ID.
     * @param patientId patient ID to find the studies for.
     * @return List of Study IDs.
     * @throws ConnectionException if there's a problem connecting to the NCIA server.
     */
    List<String> retrieveStudyCollectionIdsFromPatient(String patientId) throws ConnectionException;

    /**
     * Retrieves a list of all image series from a study.
     * @param studyInstanceUID study UID to retrieve image series from.
     * @return List of ImageSeries.
     * @throws ConnectionException if there's a problem connecting to the NCIA server.
     */
    List<Series> retrieveImageSeriesCollectionFromStudy(String studyInstanceUID) throws ConnectionException;
    
    /**
     * Retrieves a list of all image series IDs from a study.
     * @param studyInstanceUID study UID to retrieve image series from.
     * @return List of ImageSeries IDs.
     * @throws ConnectionException if there's a problem connecting to the NCIA server.
     */
    List<String> retrieveImageSeriesCollectionIdsFromStudy(String studyInstanceUID) throws ConnectionException;

    /**
     * Retrieves a list of all images given a series ID.
     * @param seriesInstanceUID series ID to find images from.
     * @return List of Image metadata
     * @throws ConnectionException if there's a problem connecting to the NCIA server.
     */
    List<Image> retrieveImageCollectionFromSeries(String seriesInstanceUID) throws ConnectionException;
    
    /**
     * Retrieves a list of all images IDs given a series ID.
     * @param seriesInstanceUID series ID to find images from.
     * @return List of Image IDs.
     * @throws ConnectionException if there's a problem connecting to the NCIA server.
     */
    List<String> retrieveImageCollectionIdsFromSeries(String seriesInstanceUID) throws ConnectionException;
    
    /**
     * Retrieves a representative image given a series ID.
     * @param seriesInstanceUID series ID to find image from.
     * @return Image metadata
     * @throws ConnectionException if there's a problem connecting to the NCIA server.
     */
    Image retrieveRepresentativeImageBySeries(String seriesInstanceUID) throws ConnectionException;

    /**
     * Given a seriesInstanceUID we validate whether the series object exists or not.
     * @param seriesInstanceUID sent to see whether the UID is present or not 
     * @return true or false
     * @throws ConnectionException if there's a problem connecting to the NCIA server.
     */
    boolean validate(String seriesInstanceUID) throws ConnectionException;
}
