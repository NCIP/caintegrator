/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.external.aim;

import gov.nih.nci.caintegrator2.domain.imaging.ImageSeries;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.ServerConnectionProfile;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 */
public class AIMFacadeStub implements AIMFacade {

    public Map<ImageSeries, ImageSeriesAnnotationsWrapper> retrieveImageSeriesAnnotations(
            ServerConnectionProfile connection, Collection<ImageSeries> imageSeriesCollection)
            throws ConnectionException {
        Map<ImageSeries, ImageSeriesAnnotationsWrapper> map = new HashMap<ImageSeries, ImageSeriesAnnotationsWrapper>();
        for (ImageSeries imageSeries : imageSeriesCollection) {
            ImageSeriesAnnotationsWrapper annotations = new ImageSeriesAnnotationsWrapper();
            annotations.addDefinitionValueToGroup("Group", "Definition", "Value");
            map.put(imageSeries, annotations);
        }
        return map;
    }

    public void validateAimConnection(ServerConnectionProfile profile) throws ConnectionException {
        
    }

}
