/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.study;

import static gov.nih.nci.caintegrator.TestDataFiles.VALID_FILE;
import static org.junit.Assert.assertEquals;
import gov.nih.nci.caintegrator.application.study.AnnotationFile;
import gov.nih.nci.caintegrator.application.study.FileColumn;
import gov.nih.nci.caintegrator.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator.application.study.ValidationException;
import gov.nih.nci.caintegrator.data.CaIntegrator2DaoStub;
import gov.nih.nci.caintegrator.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator.domain.application.EntityTypeEnum;

import java.util.Set;

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
            return AnnotationFile.load(VALID_FILE, new CaIntegrator2DaoStub(), new StudyConfiguration(), EntityTypeEnum.SUBJECT, false);
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
