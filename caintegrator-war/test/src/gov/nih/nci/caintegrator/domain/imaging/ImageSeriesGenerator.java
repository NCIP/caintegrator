/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.domain.imaging;

import static org.junit.Assert.assertEquals;
import gov.nih.nci.caintegrator.application.study.AbstractTestDataGenerator;
import gov.nih.nci.caintegrator.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator.domain.imaging.ImageSeries;

import java.util.Set;

public final class ImageSeriesGenerator extends AbstractTestDataGenerator<ImageSeries> {

    public static final ImageSeriesGenerator INSTANCE = new ImageSeriesGenerator();
    
    private ImageSeriesGenerator() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void compareFields(ImageSeries original, ImageSeries retrieved) {
        assertEquals(original.getIdentifier(), retrieved.getIdentifier());
        compareCollections(original.getImageCollection(), retrieved.getImageCollection(), ImageGenerator.INSTANCE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ImageSeries createPersistentObject() {
        return new ImageSeries();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValues(ImageSeries imageSeries, Set<AbstractCaIntegrator2Object> nonCascadedObjects) {
        imageSeries.setIdentifier(getUniqueString());
        imageSeries.getImageCollection().clear();

        for (int i = 0; i < 3; i++) {
            imageSeries.getImageCollection().add(ImageGenerator.INSTANCE.createPopulatedPersistentObject(nonCascadedObjects));
        }
    }


}
