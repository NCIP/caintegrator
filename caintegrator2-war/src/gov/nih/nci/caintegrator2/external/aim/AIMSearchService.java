/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.external.aim;


import edu.northwestern.radiology.aim.jaxb.ImageAnnotation;
import gov.nih.nci.caintegrator2.external.ConnectionException;

/**
 * 
 */
public interface AIMSearchService {
    
    /**
     * Retrieves the image annotation for the given image series instance UID.
     * @param seriesInstanceUID to find annotation for.
     * @return Image Annotation for series.
     * @throws ConnectionException if unable to connect.
     */
    ImageAnnotation getImageSeriesAnnotation(String seriesInstanceUID) throws ConnectionException;

}
