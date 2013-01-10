/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator.plots.kaplanmeier;

import gov.nih.nci.caintegrator.plots.kaplanmeier.dto.KMSampleDTO;
import gov.nih.nci.caintegrator.plots.kaplanmeier.model.XYCoordinate;

import java.util.Collection;

/**
 * @author caIntegrator Team
*/

public interface KMAlgorithm {
    public Collection<XYCoordinate> getPlottingCoordinates(Collection<KMSampleDTO> sampleColleciton);
    public Double getLogRankPValue(Collection<KMSampleDTO> group1, Collection<KMSampleDTO> group2);
    public static final Double UNKNOWN_PVALUE = -100.0;
}
