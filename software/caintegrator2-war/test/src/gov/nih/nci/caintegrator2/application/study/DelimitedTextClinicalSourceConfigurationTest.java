/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.study;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.TestDataFiles;
import gov.nih.nci.caintegrator2.data.CaIntegrator2DaoStub;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;

import org.junit.Before;
import org.junit.Test;

public class DelimitedTextClinicalSourceConfigurationTest {

    DelimitedTextClinicalSourceConfiguration clinicalSourceConfiguration;
    AnnotationFieldDescriptor identifierDescriptor;
    
    @Before
    public void setUp() throws Exception {
        StudyConfiguration studyConfig = new StudyConfiguration();
        AnnotationFile annotationFile = AnnotationFile.load(TestDataFiles.VALID_FILE, new CaIntegrator2DaoStub());
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
    public void testLoadDescriptors() {
        clinicalSourceConfiguration.loadDescriptors();
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
        clinicalSourceConfiguration.loadDescriptors();
        clinicalSourceConfiguration.loadAnnontation();
    }

    @Test
    public void testIsLoadable() {
        assertFalse(clinicalSourceConfiguration.isLoadable());
        for (FileColumn fileColumn : clinicalSourceConfiguration.getAnnotationFile().getColumns()) {
            fileColumn.setFieldDescriptor(new AnnotationFieldDescriptor());
            fileColumn.getFieldDescriptor().setDefinition(new AnnotationDefinition());
        }
        assertTrue(clinicalSourceConfiguration.isLoadable());
        clinicalSourceConfiguration.getAnnotationFile().setIdentifierColumn(null);
        assertFalse(clinicalSourceConfiguration.isLoadable());
    }
}
