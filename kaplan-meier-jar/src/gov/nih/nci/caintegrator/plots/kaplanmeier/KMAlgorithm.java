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
}
