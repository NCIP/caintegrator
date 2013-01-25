/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.domain.imaging;

import static org.junit.Assert.assertEquals;
import gov.nih.nci.caintegrator2.application.study.AbstractTestDataGenerator;
import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;

import java.util.HashSet;
import java.util.Set;

public final class ImageSeriesAcquisitionGenerator extends AbstractTestDataGenerator<ImageSeriesAcquisition> {

    public static final AbstractTestDataGenerator<ImageSeriesAcquisition> INSTANCE = new ImageSeriesAcquisitionGenerator();
    
    private ImageSeriesAcquisitionGenerator() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void compareFields(ImageSeriesAcquisition original, ImageSeriesAcquisition retrieved) {
        compareCollections(original.getSeriesCollection(), retrieved.getSeriesCollection(), ImageSeriesGenerator.INSTANCE);
        assertEquals(original.getAssignment(), retrieved.getAssignment());
        assertEquals(original.getTimepoint(), retrieved.getTimepoint());
        assertEquals(original.getIdentifier(), retrieved.getIdentifier());
        assertEquals(original.getNciaTrialIdentifier(), retrieved.getNciaTrialIdentifier());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ImageSeriesAcquisition createPersistentObject() {
        return new ImageSeriesAcquisition();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValues(ImageSeriesAcquisition acquisition, Set<AbstractCaIntegrator2Object> nonCascadedObjects) {
        acquisition.setIdentifier(getUniqueString());
        acquisition.setNciaTrialIdentifier(getUniqueString());
        if (acquisition.getSeriesCollection() == null) {
            acquisition.setSeriesCollection(new HashSet<ImageSeries>());
        } else {            
            acquisition.getSeriesCollection().clear();
        }
        for (int i = 0; i < 3; i++) {
            acquisition.getSeriesCollection().add(ImageSeriesGenerator.INSTANCE.createPopulatedPersistentObject(nonCascadedObjects));
        }
    }


}
