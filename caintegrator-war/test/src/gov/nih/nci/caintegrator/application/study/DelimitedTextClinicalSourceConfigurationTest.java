/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.study;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator.TestDataFiles;
import gov.nih.nci.caintegrator.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator.application.study.AnnotationFieldType;
import gov.nih.nci.caintegrator.application.study.AnnotationFile;
import gov.nih.nci.caintegrator.application.study.AnnotationTypeEnum;
import gov.nih.nci.caintegrator.application.study.DelimitedTextClinicalSourceConfiguration;
import gov.nih.nci.caintegrator.application.study.FileColumn;
import gov.nih.nci.caintegrator.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator.application.study.ValidationException;
import gov.nih.nci.caintegrator.data.CaIntegrator2DaoStub;
import gov.nih.nci.caintegrator.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator.domain.application.EntityTypeEnum;

import org.junit.Before;
import org.junit.Test;

public class DelimitedTextClinicalSourceConfigurationTest {

    DelimitedTextClinicalSourceConfiguration clinicalSourceConfiguration;
    AnnotationFieldDescriptor identifierDescriptor;
    
    @Before
    public void setUp() throws Exception {
        StudyConfiguration studyConfig = new StudyConfiguration();
        AnnotationFile annotationFile = AnnotationFile.load(TestDataFiles.VALID_FILE, new CaIntegrator2DaoStub(),
                studyConfig, EntityTypeEnum.SUBJECT, false);
        // Create the identifier descriptor for our test file.
        identifierDescriptor = new AnnotationFieldDescriptor();
        identifierDescriptor.setName("ID");
        clinicalSourceConfiguration = new DelimitedTextClinicalSourceConfiguration(annotationFile, studyConfig);
        clinicalSourceConfiguration.getAnnotationFile().setIdentifierColumnIndex(0);
    }

    @Test
    public void testValidate() {
        clinicalSourceConfiguration.validate();
    }

    @Test
    public void testGetType() {
        clinicalSourceConfiguration.getType();
    }

    @Test
    public void testLoadAnnontation() throws ValidationException {
        AnnotationDefinition definition = new AnnotationDefinition();
        definition.getCommonDataElement().getValueDomain().setDataType(AnnotationTypeEnum.NUMERIC);
        clinicalSourceConfiguration.getAnnotationFile().getColumns().get(1).getFieldDescriptor().setDefinition(definition);
        definition = new AnnotationDefinition();
        definition.getCommonDataElement().getValueDomain().setDataType(AnnotationTypeEnum.STRING);
        clinicalSourceConfiguration.getAnnotationFile().getColumns().get(2).getFieldDescriptor().setDefinition(definition);
        definition = new AnnotationDefinition();
        definition.getCommonDataElement().getValueDomain().setDataType(AnnotationTypeEnum.STRING);
        clinicalSourceConfiguration.getAnnotationFile().getColumns().get(3).getFieldDescriptor().setDefinition(definition);
        definition = new AnnotationDefinition();
        definition.getCommonDataElement().getValueDomain().setDataType(AnnotationTypeEnum.DATE);
        clinicalSourceConfiguration.getAnnotationFile().getColumns().get(4).getFieldDescriptor().setDefinition(definition);
        clinicalSourceConfiguration.loadAnnotation();
    }

    @Test
    public void testIsLoadable() {
        assertFalse(clinicalSourceConfiguration.isLoadable());
        for (FileColumn fileColumn : clinicalSourceConfiguration.getAnnotationFile().getColumns()) {
            if (AnnotationFieldType.ANNOTATION.equals(fileColumn.getFieldDescriptor().getType())) {
                fileColumn.getFieldDescriptor().setDefinition(new AnnotationDefinition());
            }
        }
        assertTrue(clinicalSourceConfiguration.isLoadable());
        clinicalSourceConfiguration.getAnnotationFile().setIdentifierColumn(null);
        assertFalse(clinicalSourceConfiguration.isLoadable());
    }
}
