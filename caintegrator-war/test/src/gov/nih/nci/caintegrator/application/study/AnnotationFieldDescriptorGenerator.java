/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.study;

import static org.junit.Assert.assertEquals;
import gov.nih.nci.caintegrator.domain.AbstractCaIntegrator2Object;

import java.util.Set;

/**
 * Annoation Field Description generator.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
public final class AnnotationFieldDescriptorGenerator extends AbstractTestDataGenerator<AnnotationFieldDescriptor> {

    /**
     * The instance.
     */
    public static final AnnotationFieldDescriptorGenerator INSTANCE = new AnnotationFieldDescriptorGenerator();


    private AnnotationFieldDescriptorGenerator() {
        super();
    }

    @Override
    public void compareFields(AnnotationFieldDescriptor original, AnnotationFieldDescriptor retrieved) {
        assertEquals(original.getName(), retrieved.getName());
        assertEquals(original.getType(), retrieved.getType());

    }


    @Override
    public AnnotationFieldDescriptor createPersistentObject() {
        return new AnnotationFieldDescriptor();
    }


    @Override
    public void setValues(AnnotationFieldDescriptor annotationFieldDescriptor,
            Set<AbstractCaIntegrator2Object> nonCascadedObjects) {
        annotationFieldDescriptor.setName(getUniqueString());
        annotationFieldDescriptor.setType(getNewEnumValue(annotationFieldDescriptor
                .getType(), AnnotationFieldType.values()));
    }
}
