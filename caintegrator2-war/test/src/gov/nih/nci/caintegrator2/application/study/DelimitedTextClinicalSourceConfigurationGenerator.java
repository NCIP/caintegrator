/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.application.study;

import static org.junit.Assert.*;
import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;

import java.util.Set;

public final class DelimitedTextClinicalSourceConfigurationGenerator extends AbstractTestDataGenerator<DelimitedTextClinicalSourceConfiguration> {
    
    public static final DelimitedTextClinicalSourceConfigurationGenerator INSTANCE = new DelimitedTextClinicalSourceConfigurationGenerator();

    private DelimitedTextClinicalSourceConfigurationGenerator() {
        super();
    }
    
    @Override
    public void compareFields(DelimitedTextClinicalSourceConfiguration original, DelimitedTextClinicalSourceConfiguration retrieved) {
        assertEquals(original.getAnnotationFile(), retrieved.getAnnotationFile());
        AnnotationFileGenerator.INSTANCE.compare(original.getAnnotationFile(), retrieved.getAnnotationFile());
    }

    @Override
    public DelimitedTextClinicalSourceConfiguration createPersistentObject() {
        return new DelimitedTextClinicalSourceConfiguration();
    }

    @Override
    public void setValues(DelimitedTextClinicalSourceConfiguration configuration, Set<AbstractCaIntegrator2Object> nonCascadedObjects) {        
        if (configuration.getAnnotationFile() == null) {
            configuration.setAnnotationFile(AnnotationFileGenerator.INSTANCE.createPersistentObject());
        }
        AnnotationFileGenerator.INSTANCE.setValues(configuration.getAnnotationFile(), nonCascadedObjects);        
    }

}
