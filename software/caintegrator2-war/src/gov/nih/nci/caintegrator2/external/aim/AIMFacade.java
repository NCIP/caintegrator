/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.external.aim;

import gov.nih.nci.caintegrator2.domain.imaging.ImageSeries;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.ServerConnectionProfile;

import java.util.Collection;
import java.util.Map;

/**
 * Facade to talk to the AIM data service.
 */
public interface AIMFacade {

    /**
     * Annotates the image series using the given AIM server server.
     * @param connection to connect to AIM.
     * @param imageSeriesCollection to annotate.
     * @return a Map of ImageSeries -> ImageSeriesAnnotationsWrapper.
     * @throws ConnectionException if unable to connect to AIM service.
     */
    Map<ImageSeries, ImageSeriesAnnotationsWrapper> retrieveImageSeriesAnnotations(ServerConnectionProfile connection, 
            Collection<ImageSeries> imageSeriesCollection) throws ConnectionException;
    
    /**
     * Validates that a connection can be made.
     * @param profile contains connection information for the AIM server.
     * @throws ConnectionException if unable to connect to server.
     */
    void validateAimConnection(ServerConnectionProfile profile) 
        throws ConnectionException;
}
