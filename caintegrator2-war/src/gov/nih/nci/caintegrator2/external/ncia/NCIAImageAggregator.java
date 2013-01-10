/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.external.ncia;

import java.util.Set;

/**
 * Interface for anything that aggregates NCIA Images based on ImageSeriesID's, ImageStudyIDs, 
 * or PatientIDs (future).  The basic purpose is to store sets of ID's of objects that can aggregate 
 * down to an Image object (or multiple Image objects).
 */
public interface NCIAImageAggregator {

    /**
     * Gets the set of ImageSeriesIDs.
     * @return ImageSeriesIDs.
     */
    Set<String> getImageSeriesIDs();
    
    /**
     * Gets the set of ImageStudyIDs.
     * @return ImageStudyIDs.
     */
    Set<String> getImageStudyIDs();
    
    /**
     * The images are rolled up to either the image series or image series acquisition level.
     * @return aggregation type.
     */
    NCIAImageAggregationTypeEnum getImageAggregationType();
}
