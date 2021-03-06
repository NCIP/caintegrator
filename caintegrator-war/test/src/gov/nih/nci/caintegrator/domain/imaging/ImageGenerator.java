/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.domain.imaging;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import gov.nih.nci.caintegrator.application.study.AbstractTestDataGenerator;
import gov.nih.nci.caintegrator.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator.domain.imaging.Image;

public final class ImageGenerator extends AbstractTestDataGenerator<Image> {

    public static final ImageGenerator INSTANCE = new ImageGenerator();
    
    private ImageGenerator() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void compareFields(Image original, Image retrieved) {
        assertEquals(original.getIdentifier(), retrieved.getIdentifier());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Image createPersistentObject() {
        return new Image();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValues(Image image, Set<AbstractCaIntegrator2Object> nonCascadedObjects) {
        image.setIdentifier(getUniqueString());
    }


}
