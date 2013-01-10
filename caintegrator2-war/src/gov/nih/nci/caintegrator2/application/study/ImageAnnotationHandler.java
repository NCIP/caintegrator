/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.application.study;

import gov.nih.nci.caintegrator2.domain.annotation.AbstractAnnotationValue;
import gov.nih.nci.caintegrator2.domain.imaging.ImageSeries;
import gov.nih.nci.caintegrator2.domain.translational.Timepoint;

/**
 * Used to load annotation into existing <code>ImageSeries</code>.
 */
public class ImageAnnotationHandler extends AbstractAnnotationHandler {

    private final ImageAnnotationConfiguration imageAnnotationConfiguration;
    private ImageSeries currentImageSeries;

    ImageAnnotationHandler(ImageAnnotationConfiguration imageAnnotationConfiguration) {
        this.imageAnnotationConfiguration = imageAnnotationConfiguration;
    }

    /**
     * {@inheritDoc}
     * @throws ValidationException 
     */
    @Override
    void handleIdentifier(String identifier) throws ValidationException {
        currentImageSeries = imageAnnotationConfiguration.getImageSeries(identifier);
        if (currentImageSeries == null) {
            throw new ValidationException("There is no ImageSeries with the identifier " 
                    + identifier + " in the Study.");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void handleAnnotationValue(AbstractAnnotationValue annotationValue) {
        currentImageSeries.getAnnotationCollection().add(annotationValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void handleAnnotationValue(AbstractAnnotationValue annotationValue, String timepointValue) {
        handleAnnotationValue(annotationValue);
        Timepoint timepoint = imageAnnotationConfiguration.getImageDataSourceConfiguration()
            .getStudyConfiguration().getOrCreateTimepoint(timepointValue);
        currentImageSeries.getImageStudy().setTimepoint(timepoint);
    }

}
