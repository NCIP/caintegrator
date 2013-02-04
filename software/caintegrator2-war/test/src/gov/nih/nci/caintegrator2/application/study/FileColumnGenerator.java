/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.study;

import static org.junit.Assert.*;
import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;

import java.util.Set;


public final class FileColumnGenerator extends AbstractTestDataGenerator <FileColumn> {

    public static final FileColumnGenerator INSTANCE = new FileColumnGenerator();

    private FileColumnGenerator() {
        super();
    }

    @Override
    public void compareFields(FileColumn original, FileColumn retrieved) {
        assertEquals(original.getId(), retrieved.getId());
        assertEquals(original.getName(), retrieved.getName());
        assertEquals(original.getPosition(), retrieved.getPosition());
        assertEquals(original.getFieldDescriptor(), retrieved.getFieldDescriptor());
        assertEquals(original.getAnnotationFile(), retrieved.getAnnotationFile());
        AnnotationFieldDescriptorGenerator.INSTANCE.compare(original.getFieldDescriptor(), retrieved.getFieldDescriptor());
    }


    @Override
    public FileColumn createPersistentObject() {
        return new FileColumn();
    }


    @Override
    public void setValues(FileColumn column, Set<AbstractCaIntegrator2Object> nonCascadedObjects) {
        column.setName(getUniqueString());
        column.setPosition(getUniqueInt());
        if (column.getFieldDescriptor() == null) {
            column.setFieldDescriptor(AnnotationFieldDescriptorGenerator.INSTANCE.createPersistentObject());
            }
        AnnotationFieldDescriptorGenerator.INSTANCE.setValues(column.getFieldDescriptor(), nonCascadedObjects);

    }

}
