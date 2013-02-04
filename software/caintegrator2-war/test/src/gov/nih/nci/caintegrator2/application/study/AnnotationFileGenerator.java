/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.study;

import static gov.nih.nci.caintegrator2.TestDataFiles.VALID_FILE;
import static org.junit.Assert.assertEquals;

import java.util.Set;

import gov.nih.nci.caintegrator2.data.CaIntegrator2DaoStub;
import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;

/**
 * 
 */
public final class AnnotationFileGenerator extends AbstractTestDataGenerator<AnnotationFile> {

    public static final AnnotationFileGenerator INSTANCE = new AnnotationFileGenerator();

    private AnnotationFileGenerator() {
        super();
    }

    @Override
    public void compareFields(AnnotationFile original, AnnotationFile retrieved) {
        assertEquals(original.getId(), retrieved.getId());
        assertEquals(original.getFile(), retrieved.getFile());
        assertEquals(original.getIdentifierColumn(), retrieved.getIdentifierColumn());
        assertEquals(original.getTimepointColumn(), retrieved.getTimepointColumn());
        assertEquals(original.getColumns().size(), retrieved.getColumns().size());
        for (int x = 0; x < original.getColumns().size(); x++) {
            FileColumn originalColumn = (FileColumn) original.getColumns().get(x);
            FileColumn retrievedColumn = (FileColumn) retrieved.getColumns().get(x);
            FileColumnGenerator.INSTANCE.compare(originalColumn, retrievedColumn);
        }
    }


    @Override
    public AnnotationFile createPersistentObject() {
        try {
            return AnnotationFile.load(VALID_FILE, new CaIntegrator2DaoStub());
        } catch (ValidationException e) {
            return null;
        }
    }


    @Override
    public void setValues(AnnotationFile annotationFile, Set<AbstractCaIntegrator2Object> nonCascadedObjects) {
        annotationFile.setIdentifierColumnIndex(0);
        annotationFile.getColumns().clear();
        annotationFile.getColumns().add(new FileColumn(annotationFile));
        annotationFile.getColumns().add(new FileColumn(annotationFile));
        for(FileColumn column : annotationFile.getColumns()) {
            FileColumnGenerator.INSTANCE.setValues(column, nonCascadedObjects);
        }
    }

}
