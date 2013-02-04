/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.study;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator2.domain.imaging.ImageSeriesAcquisitionGenerator;
import gov.nih.nci.caintegrator2.external.ServerConnectionProfileGenerator;

public class ImageDataSourceConfigurationGenerator extends AbstractTestDataGenerator<ImageDataSourceConfiguration> {

    public static final ImageDataSourceConfigurationGenerator INSTANCE = new ImageDataSourceConfigurationGenerator();

    @Override
    public void compareFields(ImageDataSourceConfiguration original, ImageDataSourceConfiguration retrieved) {
        assertEquals(original.getStudyConfiguration(), retrieved.getStudyConfiguration());
        assertEquals(original.getCollectionName(), retrieved.getCollectionName());
        assertEquals(original.getImageSeriesAcquisitions().size(), retrieved.getImageSeriesAcquisitions().size());
        for (int i = 0; i < original.getImageSeriesAcquisitions().size(); i++) {
            ImageSeriesAcquisitionGenerator.INSTANCE.compare(original.getImageSeriesAcquisitions().get(i), retrieved.getImageSeriesAcquisitions().get(i));
        }
        ServerConnectionProfileGenerator.INSTANCE.compare(original.getServerProfile(), retrieved.getServerProfile());
    }

    @Override
    public ImageDataSourceConfiguration createPersistentObject() {
        return new ImageDataSourceConfiguration();
    }

    @Override
    public void setValues(ImageDataSourceConfiguration config, Set<AbstractCaIntegrator2Object> nonCascadedObjects) {
        config.setCollectionName(getUniqueString());
        ServerConnectionProfileGenerator.INSTANCE.setValues(config.getServerProfile(), nonCascadedObjects);
        config.getImageSeriesAcquisitions().clear();
        for (int i = 0; i < 3; i++) {
            config.getImageSeriesAcquisitions().add(ImageSeriesAcquisitionGenerator.INSTANCE.createPopulatedPersistentObject(nonCascadedObjects));            
        }
    }


}
