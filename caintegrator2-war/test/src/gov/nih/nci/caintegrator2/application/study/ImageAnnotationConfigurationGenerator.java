/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.study;

import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;

import java.util.Set;

public class ImageAnnotationConfigurationGenerator extends AbstractTestDataGenerator<ImageAnnotationConfiguration> {

    public static final ImageAnnotationConfigurationGenerator INSTANCE = new ImageAnnotationConfigurationGenerator();

    @Override
    public void compareFields(ImageAnnotationConfiguration original, ImageAnnotationConfiguration retrieved) {
        AnnotationFileGenerator.INSTANCE.compare(original.getAnnotationFile(), retrieved.getAnnotationFile());
    }

    @Override
    public ImageAnnotationConfiguration createPersistentObject() {
        return new ImageAnnotationConfiguration();
    }

    @Override
    public void setValues(ImageAnnotationConfiguration config, Set<AbstractCaIntegrator2Object> nonCascadedObjects) {
        if (config.getAnnotationFile() == null) {
            config.setAnnotationFile(AnnotationFileGenerator.INSTANCE.createPersistentObject());
        }
        AnnotationFileGenerator.INSTANCE.setValues(config.getAnnotationFile(), nonCascadedObjects);        
    }


}
