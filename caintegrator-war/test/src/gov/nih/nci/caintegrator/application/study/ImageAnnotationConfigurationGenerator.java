/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.study;

import gov.nih.nci.caintegrator.application.study.ImageAnnotationConfiguration;
import gov.nih.nci.caintegrator.domain.AbstractCaIntegrator2Object;

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
