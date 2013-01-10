/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
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

import org.apache.commons.collections.CollectionUtils;

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
        if (imagingObservationsExist(annotation)) {
            for (ImagingObservation imagingObservation
                    : annotation.getImagingObservationCollection().getImagingObservations()) {
                if (imagingCharacteristicsExist(imagingObservation)) {
                    for (ImagingObservationCharacteristic characteristic
                            : imagingObservation.getImagingObservationCharacteristicCollection().
                            getImagingObservationCharacteristics()) {
                        annotationWrapper.addDefinitionValueToGroup(IMAGING_OBSERVATION_GROUP_NAME,
                                characteristic.getCodeMeaning(), characteristic.getCodeValue());
                    }
                }
            }
        }
        imageSeriesAnnotationsMap.put(imageSeries, annotationWrapper);
    }

    private boolean imagingCharacteristicsExist(ImagingObservation imagingObservation) {
        return imagingObservation.getImagingObservationCharacteristicCollection() != null
                && CollectionUtils.isNotEmpty(imagingObservation.getImagingObservationCharacteristicCollection()
                        .getImagingObservationCharacteristics());
    }

    private boolean imagingObservationsExist(ImageAnnotation annotation) {
        return annotation.getImagingObservationCollection() != null
                && CollectionUtils.isNotEmpty(annotation.getImagingObservationCollection().getImagingObservations());
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
