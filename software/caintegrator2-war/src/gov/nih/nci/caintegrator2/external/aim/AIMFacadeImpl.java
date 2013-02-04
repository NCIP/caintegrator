/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.external.aim;

import edu.northwestern.radiology.aim.jaxb.ImageAnnotation;
import edu.northwestern.radiology.aim.jaxb.ImagingObservation;
import edu.northwestern.radiology.aim.jaxb.ImagingObservationCharacteristic;
import gov.nih.nci.caintegrator2.domain.imaging.ImageSeries;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.ServerConnectionProfile;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * AIM data service facade.
 */
public class AIMFacadeImpl implements AIMFacade {
    
    private static final String IMAGING_OBSERVATION_GROUP_NAME = "Imaging Observations";
    private AIMServiceFactory aimServiceFactory;
    
    /**
     * {@inheritDoc}
     */
    public Map<ImageSeries, ImageSeriesAnnotationsWrapper> retrieveImageSeriesAnnotations(
            ServerConnectionProfile connection, Collection<ImageSeries> imageSeriesCollection) 
            throws ConnectionException {
        Map<ImageSeries, ImageSeriesAnnotationsWrapper> imageSeriesAnnotationsMap = 
                    new HashMap<ImageSeries, ImageSeriesAnnotationsWrapper>();
        AIMSearchService searchService = aimServiceFactory.createAIMSearchService(connection);
        for (ImageSeries imageSeries : imageSeriesCollection) {
            ImageAnnotation annotation = searchService.getImageSeriesAnnotation(imageSeries.getIdentifier());
            if (annotation != null) {
                createImageAnnotationsWrapper(imageSeriesAnnotationsMap, imageSeries, annotation);
            }
        }
        return imageSeriesAnnotationsMap;
    }

    /**
     * {@inheritDoc}
     */
    public void validateAimConnection(ServerConnectionProfile connection) 
        throws ConnectionException {
        aimServiceFactory.createAIMSearchService(connection);
    }

    private void createImageAnnotationsWrapper(
            Map<ImageSeries, ImageSeriesAnnotationsWrapper> imageSeriesAnnotationsMap, ImageSeries imageSeries,
            ImageAnnotation annotation) {
        ImageSeriesAnnotationsWrapper annotationWrapper = new ImageSeriesAnnotationsWrapper();
        if (annotation.getImagingObservationCollection() != null) {
            for (ImagingObservation imagingObservation 
                    : annotation.getImagingObservationCollection().getImagingObservations()) {
                for (ImagingObservationCharacteristic characteristic 
                    : imagingObservation.getImagingObservationCharacteristicCollection().
                                getImagingObservationCharacteristics()) {
                    annotationWrapper.addDefinitionValueToGroup(IMAGING_OBSERVATION_GROUP_NAME, 
                            characteristic.getCodeMeaning(), characteristic.getCodeValue());
                }
            }
        }
        imageSeriesAnnotationsMap.put(imageSeries, annotationWrapper);
    }

    /**
     * @return the aimServiceFactory
     */
    public AIMServiceFactory getAimServiceFactory() {
        return aimServiceFactory;
    }

    /**
     * @param aimServiceFactory the aimServiceFactory to set
     */
    public void setAimServiceFactory(AIMServiceFactory aimServiceFactory) {
        this.aimServiceFactory = aimServiceFactory;
    }
}
