/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.external.ncia;

import gov.nih.nci.caintegrator.domain.imaging.ImageSeriesAcquisition;
import gov.nih.nci.caintegrator.external.ConnectionException;
import gov.nih.nci.caintegrator.external.InvalidImagingCollectionException;
import gov.nih.nci.caintegrator.external.ServerConnectionProfile;

import java.io.File;
import java.util.List;

/**
 * Interface to the NCIA subsystem used to interface with an external NCIA server.
 */
public interface NCIAFacade {

    /**
     * Retrieves all CollectionNames Projects from NCIA for a given NCIA Grid ServerConnectionProfile.
     * @param profile contains connection information for the NCIA server
     * @return - All CollectionName Projects that exist in this instance of NCIA.
     * @throws ConnectionException if there's a problem connecting to the NCIA server.
     */
    List<String> getAllCollectionNameProjects(ServerConnectionProfile profile) 
        throws ConnectionException;
    
    /**
     * Validates that a connection can be made, and that the project name exists on the server.
     * @param profile contains connection information for the NBIA server.
     * @param collectionNameProject proejct for the collection name.
     * @throws ConnectionException if unable to connect to server.
     * @throws InvalidImagingCollectionException if there is no collection with the given name.
     */
    void validateImagingSourceConnection(ServerConnectionProfile profile, String collectionNameProject) 
        throws ConnectionException, InvalidImagingCollectionException;
    
    /**
     * Retrieves the list of ImageStudys from NCIAGridService and converts them to our 
     * ImageSeriesAcquisition object.
     * @param collectionNameProject - Project for the CollectionName
     * @param profile contains connection information for the NCIA server
     * @return ImageStudy will be ImageSeriesAcquisition after it gets mapped.
     * @throws ConnectionException if there's a problem connecting to the NCIA server.
     * @throws InvalidImagingCollectionException if there are no images associated with collection.
     */
    List<ImageSeriesAcquisition> getImageSeriesAcquisitions(String collectionNameProject, 
            ServerConnectionProfile profile) throws ConnectionException, InvalidImagingCollectionException;
    
    /**
     * Retrieves the Dicom files for the given job (currently only supports 1 imageSeries UID).
     * @param job is the job to retrieve dicom images for.
     * @return the zip file which contains the files.
     * @throws ConnectionException if there's a problem connecting to the NCIA server.
     */
    File retrieveDicomFiles(NCIADicomJob job) throws ConnectionException;
}

