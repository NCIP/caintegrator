/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.study;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import gov.nih.nci.caintegrator2.TestDataFiles;
import gov.nih.nci.caintegrator2.application.workspace.WorkspaceServiceStub;
import gov.nih.nci.caintegrator2.data.CaIntegrator2DaoStub;
import gov.nih.nci.caintegrator2.data.StudyHelper;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.annotation.CommonDataElement;
import gov.nih.nci.caintegrator2.domain.annotation.NumericAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.PermissibleValue;
import gov.nih.nci.caintegrator2.domain.annotation.StringAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.SubjectAnnotation;
import gov.nih.nci.caintegrator2.domain.annotation.SurvivalValueDefinition;
import gov.nih.nci.caintegrator2.domain.annotation.ValueDomain;
import gov.nih.nci.caintegrator2.domain.application.AbstractAnnotationCriterion;
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.ResultColumn;
import gov.nih.nci.caintegrator2.domain.application.StringComparisonCriterion;
import gov.nih.nci.caintegrator2.domain.application.UserWorkspace;
import gov.nih.nci.caintegrator2.domain.genomic.Sample;
import gov.nih.nci.caintegrator2.domain.genomic.SampleAcquisition;
import gov.nih.nci.caintegrator2.domain.genomic.SampleSet;
import gov.nih.nci.caintegrator2.domain.imaging.ImageSeries;
import gov.nih.nci.caintegrator2.domain.imaging.ImageSeriesAcquisition;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.domain.translational.StudySubjectAssignment;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.caarray.ExperimentNotFoundException;
import gov.nih.nci.caintegrator2.external.cadsr.CaDSRFacadeStub;
import gov.nih.nci.caintegrator2.file.FileManagerStub;
import gov.nih.nci.caintegrator2.security.SecurityManagerStub;
import gov.nih.nci.security.exceptions.CSException;
import gov.nih.nci.security.exceptions.CSSecurityException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@SuppressWarnings("PMD")
public class StudyManagementServiceTest {
    
    private StudyManagementServiceImpl studyManagementService;
    private CaIntegrator2DaoStub daoStub;
    private CaDSRFacadeStub caDSRFacadeStub;
    private FileManagerStub fileManagerStub;
    private WorkspaceServiceStub workspaceServiceStub;
    private SecurityManagerStub securityManagerStub;
    
    private static final String CONTROL_SAMPLE_SET_NAME = "Control Sample Set 1";

    @Before
    public void setUp() throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("studymanagement-test-config.xml", StudyManagementServiceTest.class); 
        studyManagementService = (StudyManagementServiceImpl) context.getBean("studyManagementService"); 
		daoStub = (CaIntegrator2DaoStub) context.getBean("dao");
        daoStub.clear();                
        caDSRFacadeStub = (CaDSRFacadeStub) context.getBean("caDSRFacadeStub");
        caDSRFacadeStub.clear();
        fileManagerStub = (FileManagerStub) context.getBean("fileManagerStub");
        fileManagerStub.clear();
        workspaceServiceStub = (WorkspaceServiceStub) context.getBean("workspaceServiceStub");
        workspaceServiceStub.clear();
        securityManagerStub = (SecurityManagerStub) context.getBean("securityManagerStub");
        securityManagerStub.clear();
    }

    @Test
    public void testUpdate() {
        StudyConfiguration configTest = new StudyConfiguration();
        studyManagementService.save(configTest);
        assertTrue(daoStub.saveCalled);
    }

    @Test
    public void testDelete() throws ValidationException, CSException {
        StudyHelper studyHelper = new StudyHelper();
        Study study = studyHelper.populateAndRetrieveStudyWithSourceConfigurations();
        
        StudyConfiguration configTest = study.getStudyConfiguration();
        configTest.setStudy(study);
        UserWorkspace userWorkspace = new UserWorkspace();
        configTest.setUserWorkspace(userWorkspace);
        DelimitedTextClinicalSourceConfiguration clinicalSource = new DelimitedTextClinicalSourceConfiguration();
        studyManagementService.delete(configTest, clinicalSource);
        assertTrue(daoStub.deleteCalled);
        daoStub.deleteCalled = false;
        GenomicDataSourceConfiguration genomicSource = configTest.getGenomicDataSources().get(0);
        studyManagementService.delete(configTest, genomicSource);
        assertTrue(daoStub.deleteCalled);
        deleteImageDataSource(study, configTest);
        daoStub.deleteCalled = false;
        
        studyManagementService.delete(configTest);
        assertTrue(daoStub.deleteCalled);
        assertTrue(securityManagerStub.deleteProtectionElementCalled);
        assertTrue(workspaceServiceStub.unSubscribeAllCalled);
        assertTrue(fileManagerStub.deleteStudyDirectoryCalled);
    }
    
    @SuppressWarnings("deprecation")
    private void deleteImageDataSource(Study study, StudyConfiguration configTest) throws ValidationException {
        daoStub.deleteCalled = false;
        ImageDataSourceConfiguration imageSourceToKeep = new ImageDataSourceConfiguration();
        ImageDataSourceConfiguration imageSourceToDelete = configTest.getImageDataSources().get(0);
        ImageSeriesAcquisition imageSeriesAcquisition = new ImageSeriesAcquisition();
        imageSourceToDelete.getImageSeriesAcquisitions().add(imageSeriesAcquisition);
        StudySubjectAssignment ssa = new StudySubjectAssignment();
        study.getAssignmentCollection().add(ssa);
        ssa.getImageStudyCollection().add(imageSeriesAcquisition);
        ImageAnnotationConfiguration imageAnnotationConfig = new ImageAnnotationConfiguration();
        imageAnnotationConfig.setAnnotationFile(new AnnotationFile());
        imageAnnotationConfig.getAnnotationFile().setCurrentlyLoaded("false");
        imageSourceToKeep.setImageAnnotationConfiguration(imageAnnotationConfig);
        configTest.getImageDataSources().add(imageSourceToKeep);
        studyManagementService.delete(configTest, imageSourceToDelete);
        assertFalse(ssa.getImageStudyCollection().contains(imageSeriesAcquisition));
        assertFalse(configTest.getImageDataSources().contains(imageSourceToDelete));
        assertTrue(configTest.getImageDataSources().contains(imageSourceToKeep));
        assertTrue(daoStub.deleteCalled);
    }
    
    @Test
    public void testAddClinicalAnnotationFile() throws ValidationException, IOException {
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        studyManagementService.save(studyConfiguration);
        DelimitedTextClinicalSourceConfiguration sourceConfiguration = 
            studyManagementService.addClinicalAnnotationFile(studyConfiguration, TestDataFiles.VALID_FILE, TestDataFiles.VALID_FILE.getName(),
                    false);
        assertEquals(1, studyConfiguration.getClinicalConfigurationCollection().size());
        assertTrue(studyConfiguration.getClinicalConfigurationCollection().contains(sourceConfiguration));
        assertEquals(5, sourceConfiguration.getAnnotationFile().getColumns().size());
        assertTrue(daoStub.saveCalled);
    }
    
    @Test
    public void testAddStudyLogo() throws IOException {
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        studyManagementService.save(studyConfiguration);
        studyManagementService.addStudyLogo(studyConfiguration, TestDataFiles.VALID_FILE, TestDataFiles.VALID_FILE.getName(), "image/jpeg");
        assertTrue(fileManagerStub.storeStudyFileCalled);
        assertTrue(daoStub.saveCalled);
    }
    
    @Test
    public void testLoadClinicalAnnotation() throws ValidationException, IOException {
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        studyManagementService.save(studyConfiguration);
        DelimitedTextClinicalSourceConfiguration sourceConfiguration = 
            studyManagementService.addClinicalAnnotationFile(studyConfiguration, TestDataFiles.VALID_FILE, TestDataFiles.VALID_FILE.getName(),
                    false);
        sourceConfiguration.getAnnotationFile().setIdentifierColumnIndex(0);
        AnnotationDefinition definition = new AnnotationDefinition();
        definition.setDataType(AnnotationTypeEnum.NUMERIC);
        sourceConfiguration.getAnnotationFile().getColumns().get(1).getFieldDescriptor().setDefinition(definition);
        definition = new AnnotationDefinition();
        definition.setDataType(AnnotationTypeEnum.STRING);
        sourceConfiguration.getAnnotationFile().getColumns().get(2).getFieldDescriptor().setDefinition(definition);
        definition = new AnnotationDefinition();
        definition.setDataType(AnnotationTypeEnum.STRING);
        PermissibleValue pv = new PermissibleValue();
        pv.setValue("testValue");
        definition.getPermissibleValueCollection().add(pv);
        sourceConfiguration.getAnnotationFile().getColumns().get(3).getFieldDescriptor().setDefinition(definition);
        sourceConfiguration.getAnnotationFile().getColumns().get(3).getFieldDescriptor().setHasValidationErrors(true);
        definition = new AnnotationDefinition();
        definition.setDataType(AnnotationTypeEnum.DATE);
        sourceConfiguration.getAnnotationFile().getColumns().get(4).getFieldDescriptor().setDefinition(definition);
        studyManagementService.loadClinicalAnnotation(studyConfiguration, sourceConfiguration); 
        studyManagementService.reLoadClinicalAnnotation(studyConfiguration); 
        assertFalse(sourceConfiguration.getAnnotationFile().getColumns().get(3).getFieldDescriptor().isHasValidationErrors());
    }
    
    @Test
    public void testLoadClinicalAnnotationCreateNewAD() throws ValidationException, IOException {
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        studyManagementService.save(studyConfiguration);
        DelimitedTextClinicalSourceConfiguration sourceConfiguration = 
            studyManagementService.addClinicalAnnotationFile(studyConfiguration, TestDataFiles.VALID_FILE, TestDataFiles.VALID_FILE.getName(),
                    true);
        AnnotationDefinition ad = sourceConfiguration.getAnnotationFile().getColumns().get(0).getFieldDescriptor().getDefinition();
        assertNotNull(ad);
        assertEquals("ID", ad.getKeywords());
        assertEquals(AnnotationTypeEnum.STRING, ad.getDataType());
    }
    
    
    @Test(expected=ValidationException.class)
    public void testLoadInvalidClinicalAnnotation() throws ValidationException, IOException {
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        studyManagementService.save(studyConfiguration);
        DelimitedTextClinicalSourceConfiguration sourceConfiguration = 
            studyManagementService.addClinicalAnnotationFile(studyConfiguration, TestDataFiles.VALID_FILE, TestDataFiles.VALID_FILE.getName(),
                    false);
        sourceConfiguration.getAnnotationFile().setIdentifierColumnIndex(0);
        AnnotationDefinition definition = new AnnotationDefinition();
        definition.setDataType(AnnotationTypeEnum.NUMERIC);
        sourceConfiguration.getAnnotationFile().getColumns().get(1).getFieldDescriptor().setDefinition(definition);
        definition = new AnnotationDefinition();
        definition.setDataType(AnnotationTypeEnum.STRING);
        sourceConfiguration.getAnnotationFile().getColumns().get(2).getFieldDescriptor().setDefinition(definition);
        definition = new AnnotationDefinition();
        definition.setDataType(AnnotationTypeEnum.STRING);
        PermissibleValue pv = new PermissibleValue();
        pv.setValue("testValue");
        definition.getPermissibleValueCollection().add(pv);
        sourceConfiguration.getAnnotationFile().getColumns().get(3).getFieldDescriptor().setDefinition(definition);
        definition = new AnnotationDefinition();
        daoStub.fileColumns.add(sourceConfiguration.getAnnotationFile().getColumns().get(3));
        definition.setDataType(AnnotationTypeEnum.DATE);
        sourceConfiguration.getAnnotationFile().getColumns().get(4).getFieldDescriptor().setDefinition(definition);
        studyManagementService.loadClinicalAnnotation(studyConfiguration, sourceConfiguration); 
        studyManagementService.reLoadClinicalAnnotation(studyConfiguration); 
    }
    
    @Test 
    public void testAddGenomicSource() throws ConnectionException, ExperimentNotFoundException {
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        GenomicDataSourceConfiguration genomicDataSourceConfiguration = new GenomicDataSourceConfiguration();
        studyManagementService.addGenomicSource(studyConfiguration, genomicDataSourceConfiguration);
        genomicDataSourceConfiguration.setId(Long.valueOf(1));        
        assertTrue(studyConfiguration.getGenomicDataSources().contains(genomicDataSourceConfiguration));
        assertTrue(daoStub.saveCalled);
    }
    
    @Test
    public void testGetStudyEntity() {
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        studyConfiguration.setId(1L);
        assertNotNull(studyManagementService.getRefreshedEntity(studyConfiguration));
        assertTrue(daoStub.getCalled);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetStudyEntityNoId() {
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        studyManagementService.getRefreshedEntity(studyConfiguration);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetStudyEntityIllegalClass() {
        Object object = new Object();
        studyManagementService.getRefreshedEntity(object);
    }
    
    @Test
    public void testGetMatchingDefinitions() {
        List<AnnotationDefinition> definitions = studyManagementService.getMatchingDefinitions(
                Arrays.asList(StringUtils.split("test keywords")));
        assertEquals(1, definitions.size());
        assertEquals("definitionName", definitions.get(0).getDisplayName());
    }
    
    @Test
    public void testGetMatchingDataElements() {
        studyManagementService.getMatchingDataElements(Arrays.asList(StringUtils.split("random String")));
        assertTrue(caDSRFacadeStub.retreiveCandidateDataElementsCalled);
    }
    
    @SuppressWarnings("deprecation")
    @Test
    public void testSetDefinition() throws ValidationException {
        Study study = new Study();
        study.setId(Long.valueOf(1L));
        FileColumn fileColumn = new FileColumn();
        fileColumn.setFieldDescriptor(new AnnotationFieldDescriptor());
        fileColumn.setAnnotationFile(new AnnotationFile());
        fileColumn.getAnnotationFile().setCurrentlyLoaded(Boolean.TRUE.toString());
        SampleAcquisition sampleAcquisition = new SampleAcquisition();
        StudySubjectAssignment assignment = new StudySubjectAssignment();
        sampleAcquisition.setAssignment(assignment);
        assignment.setStudy(study);
        ImageSeries imageSeries = new ImageSeries();
        ImageSeriesAcquisition imageStudy = new ImageSeriesAcquisition();
        imageSeries.setImageStudy(imageStudy);
        imageStudy.setAssignment(assignment);
        SubjectAnnotation subjectAnnotation = new SubjectAnnotation();
        subjectAnnotation.setStudySubjectAssignment(assignment);
        
        StringAnnotationValue value1 = new StringAnnotationValue();
        StringAnnotationValue value2 = new StringAnnotationValue();
        StringAnnotationValue value3 = new StringAnnotationValue();
        value1.setSampleAcquisition(sampleAcquisition);
        value2.setImageSeries(imageSeries);
        value3.setSubjectAnnotation(subjectAnnotation);
        AnnotationDefinition firstDefinition = new AnnotationDefinition();
        firstDefinition.getAnnotationValueCollection().add(value1);
        value1.setAnnotationDefinition(firstDefinition);
        value1.setStringValue("1.23");
        firstDefinition.getAnnotationValueCollection().add(value2);
        value2.setAnnotationDefinition(firstDefinition);
        value2.setStringValue("1.23");
        firstDefinition.getAnnotationValueCollection().add(value3);
        value3.setAnnotationDefinition(firstDefinition);
        value3.setStringValue("1.23");
        firstDefinition.setId(1L);
        firstDefinition.setDataType(AnnotationTypeEnum.STRING);
        daoStub.fileColumns.clear();
        daoStub.fileColumns.add(fileColumn);
        studyManagementService.setDefinition(study, fileColumn.getFieldDescriptor(), firstDefinition, EntityTypeEnum.IMAGESERIES);
        assertTrue(daoStub.saveCalled);
        assertEquals(firstDefinition, fileColumn.getFieldDescriptor().getDefinition());
        assertTrue(firstDefinition.getAnnotationValueCollection().contains(value1));
        
        // Now create a new Definition and set it and verify the first definition is removed.
        AnnotationDefinition newDefinition = new AnnotationDefinition();
        newDefinition.setId(2L);
        newDefinition.setDataType(AnnotationTypeEnum.NUMERIC);
        daoStub.fileColumns.clear();
        daoStub.fileColumns.add(fileColumn);
        studyManagementService.setDefinition(study, fileColumn.getFieldDescriptor(), newDefinition, EntityTypeEnum.IMAGESERIES);
        assertEquals(3, newDefinition.getAnnotationValueCollection().size());
        assertTrue(firstDefinition.getAnnotationValueCollection().isEmpty());
        
        assertEquals(Double.valueOf(1.23),
           ((NumericAnnotationValue)newDefinition.getAnnotationValueCollection().iterator().next()).getNumericValue());
    }
    
    @SuppressWarnings("deprecation")
    @Test
    public void testSetDataElement() throws ConnectionException, ValidationException {
        Study study = new Study();
        study.setId(Long.valueOf(1));
        FileColumn fileColumn = new FileColumn();
        fileColumn.setFieldDescriptor(new AnnotationFieldDescriptor());
        // Make an original definition with values already set.
        AnnotationDefinition originalDefinition = new AnnotationDefinition();
        originalDefinition.setDataType(AnnotationTypeEnum.STRING);
        fileColumn.getFieldDescriptor().setDefinition(originalDefinition);
        fileColumn.setAnnotationFile(new AnnotationFile());
        fileColumn.getAnnotationFile().setCurrentlyLoaded(Boolean.TRUE.toString());
        
        StringAnnotationValue validValue = new StringAnnotationValue();
        SubjectAnnotation subjectAnnotation = new SubjectAnnotation();
        StudySubjectAssignment studySubjectAssignment = new StudySubjectAssignment();
        studySubjectAssignment.setStudy(study);
        subjectAnnotation.setStudySubjectAssignment(studySubjectAssignment);
        validValue.setStringValue("Valid");
        validValue.setSubjectAnnotation(subjectAnnotation);
        originalDefinition.getAnnotationValueCollection().add(validValue);
        validValue.setAnnotationDefinition(originalDefinition);
        SurvivalValueDefinition survivalDefinition = new SurvivalValueDefinition();
        survivalDefinition.setSurvivalStartDate(originalDefinition);
        survivalDefinition.setLastFollowupDate(originalDefinition);
        survivalDefinition.setDeathDate(originalDefinition);
        study.getSurvivalValueDefinitionCollection().add(survivalDefinition);
        
        CommonDataElement dataElement = new CommonDataElement();
        dataElement.setLongName("longName");
        dataElement.setDefinition("definition");
        dataElement.setPublicID(1234L);
        ValueDomain valueDomain = new ValueDomain();
        valueDomain.setDataType(AnnotationTypeEnum.STRING);
        dataElement.setValueDomain(valueDomain);
        PermissibleValue permissibleValue = new PermissibleValue();
        permissibleValue.setId(Long.valueOf(1));
        permissibleValue.setValue("Valid");
        valueDomain.getPermissibleValueCollection().add(permissibleValue);
        daoStub.fileColumns.clear();
        daoStub.fileColumns.add(fileColumn);
        studyManagementService.setDataElement(fileColumn.getFieldDescriptor(), dataElement, study, EntityTypeEnum.SUBJECT, "");
        assertTrue(daoStub.saveCalled);
        assertNotNull(fileColumn.getFieldDescriptor().getDefinition());
        assertNotNull(fileColumn.getFieldDescriptor().getDefinition().getCommonDataElement());
        assertEquals("longName", fileColumn.getFieldDescriptor().getDefinition().getDisplayName());
        assertEquals("definition", fileColumn.getFieldDescriptor().getDefinition().getCommonDataElement().getDefinition());
        assertEquals(Long.valueOf(1234), fileColumn.getFieldDescriptor().getDefinition().getCommonDataElement().getPublicID());
        AnnotationDefinition firstDefinition = fileColumn.getFieldDescriptor().getDefinition();
        assertTrue(firstDefinition.getPermissibleValueCollection().iterator().next().equals(permissibleValue));
        assertTrue(firstDefinition.getAnnotationValueCollection().size() == 1);
        assertTrue(survivalDefinition.getSurvivalStartDate().getDisplayName().equals("longName"));
        assertFalse(survivalDefinition.getLastFollowupDate().equals(originalDefinition));
        assertFalse(survivalDefinition.getDeathDate().equals(originalDefinition));
        // Add a value that isn't permissible and catch the exception.
        StringAnnotationValue invalidValue = new StringAnnotationValue();
        SubjectAnnotation subjectAnnotation2 = new SubjectAnnotation();
        studySubjectAssignment.setStudy(study);
        subjectAnnotation2.setStudySubjectAssignment(studySubjectAssignment);
        invalidValue.setStringValue("INVALID");
        invalidValue.setSubjectAnnotation(subjectAnnotation2);
        fileColumn.getFieldDescriptor().getDefinition().getAnnotationValueCollection().add(invalidValue);
        invalidValue.setAnnotationDefinition(fileColumn.getFieldDescriptor().getDefinition());
        boolean exceptionCaught = false;
        try {
            daoStub.fileColumns.clear();
            daoStub.fileColumns.add(fileColumn);
            studyManagementService.setDataElement(fileColumn.getFieldDescriptor(), dataElement, study, EntityTypeEnum.SUBJECT, "");
        } catch (ValidationException e) {
            exceptionCaught = true;
        }
        assertTrue(exceptionCaught);
        
        // Now set a different data element and verify the first one is no longer in the study's collection
        CommonDataElement dataElement2 = new CommonDataElement();
        dataElement2.setLongName("longName2");
        dataElement2.setDefinition("definition2");
        dataElement2.setPublicID(123L);
        dataElement2.getValueDomain().setDataType(AnnotationTypeEnum.STRING);
        daoStub.fileColumns.clear();
        daoStub.fileColumns.add(fileColumn);
        studyManagementService.setDataElement(fileColumn.getFieldDescriptor(), dataElement2, study, EntityTypeEnum.SUBJECT, "");
    }
    
    @Test
    public void testMapSamples() throws ValidationException, IOException {
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        StudySubjectAssignment assignment1 = new StudySubjectAssignment();
        assignment1.setId(1L);
        assignment1.setIdentifier("E05004");
        studyConfiguration.getStudy().getAssignmentCollection().add(assignment1);
        StudySubjectAssignment assignment2 = new StudySubjectAssignment();
        assignment2.setId(2L);
        assignment2.setIdentifier("E05012");
        studyConfiguration.getStudy().getAssignmentCollection().add(assignment2);
        GenomicDataSourceConfiguration genomicDataSourceConfiguration = new GenomicDataSourceConfiguration();
        genomicDataSourceConfiguration.setStudyConfiguration(studyConfiguration);
        genomicDataSourceConfiguration.setPlatformVendor("Affymetrix");
        Sample sample1 = new Sample();
        sample1.setId(1L);
        sample1.setName("5500024030700072107989.B09");
        genomicDataSourceConfiguration.getSamples().add(sample1);
        Sample sample2 = new Sample();
        sample2.setId(2L);
        sample2.setName("5500024030700072107989.G10");
        genomicDataSourceConfiguration.getSamples().add(sample2);
        studyConfiguration.getGenomicDataSources().add(genomicDataSourceConfiguration);
        studyManagementService.mapSamples(studyConfiguration, TestDataFiles.SIMPLE_SAMPLE_MAPPING_FILE, 
                genomicDataSourceConfiguration);
        assertEquals(1, assignment1.getSampleAcquisitionCollection().size());
        assertEquals(sample1, assignment1.getSampleAcquisitionCollection().iterator().next().getSample());
        assertEquals(1, assignment2.getSampleAcquisitionCollection().size());
        assertEquals(sample2, assignment2.getSampleAcquisitionCollection().iterator().next().getSample());
    }
    
    @Test
    public void testMapAgilentSamples() throws ValidationException, IOException {
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        StudySubjectAssignment assignment1 = new StudySubjectAssignment();
        assignment1.setId(1L);
        assignment1.setIdentifier("E05004");
        studyConfiguration.getStudy().getAssignmentCollection().add(assignment1);
        StudySubjectAssignment assignment2 = new StudySubjectAssignment();
        assignment2.setId(2L);
        assignment2.setIdentifier("E05012");
        studyConfiguration.getStudy().getAssignmentCollection().add(assignment2);
        GenomicDataSourceConfiguration genomicDataSourceConfiguration = new GenomicDataSourceConfiguration();
        genomicDataSourceConfiguration.setStudyConfiguration(studyConfiguration);
        genomicDataSourceConfiguration.setPlatformVendor("Agilent");
        genomicDataSourceConfiguration.setUseSupplementalFiles(true);
        Sample sample1 = new Sample();
        sample1.setId(1L);
        sample1.setName("5500024030700072107989.B09");
        genomicDataSourceConfiguration.getSamples().add(sample1);
        Sample sample2 = new Sample();
        sample2.setId(2L);
        sample2.setName("5500024030700072107989.G10");
        genomicDataSourceConfiguration.getSamples().add(sample2);
        studyConfiguration.getGenomicDataSources().add(genomicDataSourceConfiguration);
        boolean hasException = false;
        try {
            studyManagementService.mapSamples(studyConfiguration, TestDataFiles.SIMPLE_SAMPLE_MAPPING_FILE, 
                genomicDataSourceConfiguration);
        } catch (ValidationException e) {
            hasException = true;
        }
        assertTrue(hasException);
        studyManagementService.mapSamples(studyConfiguration, TestDataFiles.SIMPLE_AGILENT_SAMPLE_MAPPING_FILE, 
            genomicDataSourceConfiguration);
        assertEquals(1, assignment1.getSampleAcquisitionCollection().size());
        assertEquals(sample1, assignment1.getSampleAcquisitionCollection().iterator().next().getSample());
        assertEquals(1, assignment2.getSampleAcquisitionCollection().size());
        assertEquals(sample2, assignment2.getSampleAcquisitionCollection().iterator().next().getSample());
    }
    
    @Test
    public void testAddControlSamples() throws ValidationException, IOException {
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        GenomicDataSourceConfiguration genomicDataSourceConfiguration = new GenomicDataSourceConfiguration();
        genomicDataSourceConfiguration.setStudyConfiguration(studyConfiguration);
        Sample sample1 = new Sample();
        sample1.setId(1L);
        sample1.setName("GeneratedSample.Normal_L_20070227_14-01-17-731_HF0088_U133P2");
        genomicDataSourceConfiguration.getSamples().add(sample1);
        Sample sample2 = new Sample();
        sample2.setId(2L);
        sample2.setName("GeneratedSample.Normal_L_20070227_14-01-17-731_HF0120_U133P2");
        genomicDataSourceConfiguration.getSamples().add(sample2);
        studyConfiguration.getGenomicDataSources().add(genomicDataSourceConfiguration);
        studyManagementService.addControlSampleSet(genomicDataSourceConfiguration, CONTROL_SAMPLE_SET_NAME,
                TestDataFiles.SHORT_REMBRANDT_CONTROL_SAMPLES_FILE, TestDataFiles.SHORT_REMBRANDT_CONTROL_SAMPLES_FILE_PATH);
        assertTrue(studyConfiguration.getControlSampleSet(CONTROL_SAMPLE_SET_NAME).getSamples().contains(sample1));
        assertTrue(studyConfiguration.getControlSampleSet(CONTROL_SAMPLE_SET_NAME).getSamples().contains(sample2));
    }
    
    @Test(expected = ValidationException.class)
    public void testAddControlSamplesDuplicate() throws ValidationException, IOException {
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        GenomicDataSourceConfiguration genomicSource = new GenomicDataSourceConfiguration();
        genomicSource.setStudyConfiguration(studyConfiguration);
        studyConfiguration.getGenomicDataSources().add(genomicSource);
        SampleSet controlSampleSet = new SampleSet();
        controlSampleSet.setName(CONTROL_SAMPLE_SET_NAME);
        genomicSource.getControlSampleSetCollection().add(controlSampleSet);
        studyManagementService.addControlSampleSet(genomicSource, CONTROL_SAMPLE_SET_NAME,
                TestDataFiles.REMBRANDT_CONTROL_SAMPLES_FILE, TestDataFiles.REMBRANDT_CONTROL_SAMPLES_FILE_PATH);
    }
    
    @Test(expected = ValidationException.class)
    public void testAddControlSamplesValidation() throws ValidationException, IOException {
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        GenomicDataSourceConfiguration genomicSource = new GenomicDataSourceConfiguration();
        genomicSource.setStudyConfiguration(studyConfiguration);
        studyManagementService.addControlSampleSet(genomicSource, CONTROL_SAMPLE_SET_NAME,
                TestDataFiles.REMBRANDT_CONTROL_SAMPLES_FILE, TestDataFiles.REMBRANDT_CONTROL_SAMPLES_FILE_PATH);
    }

    @Test
    public void testAddImageAnnotationFile() throws Exception {
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        ImageDataSourceConfiguration imageDataSourceConfiguration = new ImageDataSourceConfiguration();
        studyManagementService.addImageSource(studyConfiguration, imageDataSourceConfiguration);
        studyManagementService.save(studyConfiguration);
        ImageAnnotationConfiguration imageAnnotationConfiguration = 
            studyManagementService.addImageAnnotationFile(imageDataSourceConfiguration, TestDataFiles.VALID_FILE, TestDataFiles.VALID_FILE.getName(),
                    false);
        assertEquals(5, imageAnnotationConfiguration.getAnnotationFile().getColumns().size());
        assertTrue(daoStub.saveCalled);
    }

    @Test
    public void testAddImageSource() throws Exception {
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        ImageDataSourceConfiguration imageDataSourceConfiguration = new ImageDataSourceConfiguration();
        studyManagementService.addImageSource(studyConfiguration, imageDataSourceConfiguration);
        imageDataSourceConfiguration.setId(Long.valueOf(1));
        ImageAnnotationConfiguration imageAnnotationConfiguration = 
            studyManagementService.addImageAnnotationFile(imageDataSourceConfiguration, TestDataFiles.VALID_FILE, TestDataFiles.VALID_FILE.getName(),
                    false);
        imageDataSourceConfiguration.setImageAnnotationConfiguration(imageAnnotationConfiguration);
        assertEquals(5, imageDataSourceConfiguration.getImageAnnotationConfiguration().getAnnotationFile().getColumns().size());
        assertTrue(studyConfiguration.getImageDataSources().contains(imageDataSourceConfiguration));
        assertTrue(daoStub.saveCalled);
        assertFalse(imageDataSourceConfiguration.getImageSeriesAcquisitions().isEmpty());
    }

    @Test
    public void testLoadImageAnnotation() throws ValidationException, IOException {
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        studyManagementService.save(studyConfiguration);
        ImageDataSourceConfiguration imageDataSourceConfiguration = new ImageDataSourceConfiguration();
        imageDataSourceConfiguration.setStudyConfiguration(studyConfiguration);
        ImageSeriesAcquisition acquisition = new ImageSeriesAcquisition();
        acquisition.setSeriesCollection(new HashSet<ImageSeries>());
        ImageSeries series1 = new ImageSeries();
        series1.setIdentifier("100");
        ImageSeries series2 = new ImageSeries();
        series2.setIdentifier("101");
        acquisition.getSeriesCollection().add(series1);
        acquisition.getSeriesCollection().add(series2);
        imageDataSourceConfiguration.getImageSeriesAcquisitions().add(acquisition);
        studyConfiguration.getImageDataSources().add(imageDataSourceConfiguration);
        ImageAnnotationConfiguration imageAnnotationConfiguration = 
            studyManagementService.addImageAnnotationFile(imageDataSourceConfiguration, TestDataFiles.VALID_FILE, TestDataFiles.VALID_FILE.getName(),
                    false);
        imageAnnotationConfiguration.getAnnotationFile().setIdentifierColumnIndex(0);
        AnnotationDefinition definition = new AnnotationDefinition();
        definition.setDataType(AnnotationTypeEnum.NUMERIC);
        imageAnnotationConfiguration.getAnnotationFile().getColumns().get(1).getFieldDescriptor().setDefinition(definition);
        definition = new AnnotationDefinition();
        definition.setDataType(AnnotationTypeEnum.STRING);
        imageAnnotationConfiguration.getAnnotationFile().getColumns().get(2).getFieldDescriptor().setDefinition(definition);
        definition = new AnnotationDefinition();
        definition.setDataType(AnnotationTypeEnum.STRING);
        imageAnnotationConfiguration.getAnnotationFile().getColumns().get(3).getFieldDescriptor().setDefinition(definition);
        definition = new AnnotationDefinition();
        definition.setDataType(AnnotationTypeEnum.DATE);
        imageAnnotationConfiguration.getAnnotationFile().getColumns().get(4).getFieldDescriptor().setDefinition(definition);
        imageDataSourceConfiguration.setImageAnnotationConfiguration(imageAnnotationConfiguration);
        
        studyManagementService.loadImageAnnotation(imageDataSourceConfiguration); 
        assertEquals(4, series1.getAnnotationCollection().size());
        assertEquals(4, series2.getAnnotationCollection().size());
    }
    
    @Test
    public void testMapImageSeriesAcquisitions() throws ValidationException, IOException {
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        StudySubjectAssignment assignment1 = new StudySubjectAssignment();
        assignment1.setId(1L);
        assignment1.setIdentifier("E05004");
        studyConfiguration.getStudy().getAssignmentCollection().add(assignment1);
        StudySubjectAssignment assignment2 = new StudySubjectAssignment();
        assignment2.setId(2L);
        assignment2.setIdentifier("E05012");
        studyConfiguration.getStudy().getAssignmentCollection().add(assignment2);
        ImageDataSourceConfiguration imageDataSourceConfiguration = new ImageDataSourceConfiguration();
        imageDataSourceConfiguration.setStudyConfiguration(studyConfiguration);
        ImageSeriesAcquisition acquisition1 = new ImageSeriesAcquisition();
        acquisition1.setIdentifier("100");
        imageDataSourceConfiguration.getImageSeriesAcquisitions().add(acquisition1);
        ImageSeriesAcquisition acquisition2 = new ImageSeriesAcquisition();
        acquisition2.setIdentifier("101");
        imageDataSourceConfiguration.getImageSeriesAcquisitions().add(acquisition2);
        studyConfiguration.getImageDataSources().add(imageDataSourceConfiguration);
        studyManagementService.save(studyConfiguration);
        studyManagementService.mapImageSeriesAcquisitions(studyConfiguration, imageDataSourceConfiguration, 
                TestDataFiles.SIMPLE_IMAGE_MAPPING_FILE, ImageDataSourceMappingTypeEnum.IMAGE_SERIES);
        assertEquals("100", assignment1.getImageStudyCollection().iterator().next().getIdentifier());
        assertEquals("101", assignment2.getImageStudyCollection().iterator().next().getIdentifier());
        
        assertEquals(2, imageDataSourceConfiguration.getMappedImageSeriesAcquisitions().size());
        assertEquals(0, imageDataSourceConfiguration.getUnmappedImageSeriesAcquisitions().size());
    }
    
    @Test
    public void testCreateDefinition() throws ValidationException {
        AnnotationFieldDescriptor descriptor = new AnnotationFieldDescriptor();
        Study study = new Study();
        AnnotationGroup group = new AnnotationGroup();
        group.setName("group");
        study.getAnnotationGroups().add(group);
        group.getAnnotationFieldDescriptors().add(descriptor);
        descriptor.setName("testName");
        AnnotationDefinition definition = studyManagementService.createDefinition(descriptor, study, EntityTypeEnum.SUBJECT, AnnotationTypeEnum.STRING);
        assertEquals(descriptor.getName(), definition.getDisplayName());
        assertEquals(descriptor.getName(), definition.getKeywords());
        assertEquals(1, study.getAllVisibleAnnotationFieldDescriptors(EntityTypeEnum.SUBJECT, null).size());
        definition = studyManagementService.createDefinition(descriptor, study, EntityTypeEnum.IMAGESERIES, AnnotationTypeEnum.STRING);
        assertEquals(1, study.getAllVisibleAnnotationFieldDescriptors(EntityTypeEnum.IMAGESERIES, null).size());
        definition = studyManagementService.createDefinition(descriptor, study, EntityTypeEnum.SAMPLE, AnnotationTypeEnum.STRING);
        assertEquals(1, study.getAllVisibleAnnotationFieldDescriptors(EntityTypeEnum.SAMPLE, null).size());
    }
    
    @Test
    public void testIsDuplicateStudyName() {
        Study study = new Study();
        study.setShortTitleText("studyName");
        assertFalse(studyManagementService.isDuplicateStudyName(study, ""));
        assertTrue(daoStub.isDuplicateStudyNameCalled);
    }
    
    @Test
    public void testRemoveSurvivalValueDefinition() {
        Study study = StudyConfigurationFactory.createNewStudyConfiguration().getStudy();
        SurvivalValueDefinition definition = new SurvivalValueDefinition();
        study.getSurvivalValueDefinitionCollection().add(definition);
        assertTrue(study.getSurvivalValueDefinitionCollection().contains(definition));
        
        studyManagementService.removeSurvivalValueDefinition(study, definition);
        assertTrue(daoStub.removeObjectsCalled);
        assertFalse(study.getSurvivalValueDefinitionCollection().contains(definition));
    }
    
    @Test
    public void testRetrieveImageDataSource() {
        studyManagementService.retrieveImageDataSource(new Study());
        assertTrue(daoStub.retrieveImagingDataSourceForStudyCalled);
    }
    
    @Test
    public void testGetRefreshedSecureStudyConfiguration() {
        SecureDaoStub secureDaoStub = new SecureDaoStub();
        studyManagementService.setDao(secureDaoStub);
        try {
            studyManagementService.getRefreshedSecureStudyConfiguration("invalid", Long.valueOf(1));
            fail();
        } catch (CSSecurityException e) {
            
        }
        try {
            assertEquals(secureDaoStub.studyConfiguration, 
                    studyManagementService.getRefreshedSecureStudyConfiguration("valid", Long.valueOf(1)));
        } catch (CSSecurityException e) {
            fail();
        }
    }
    
    @Test
    public void testSaveAnnotationDefinition() throws ValidationException {
        AnnotationDefinition stringToNumericDefinition = new AnnotationDefinition();
        stringToNumericDefinition.setDataType(AnnotationTypeEnum.NUMERIC);
        StringAnnotationValue value = new StringAnnotationValue();
        value.setAnnotationDefinition(stringToNumericDefinition);
        stringToNumericDefinition.getAnnotationValueCollection().add(value);
        value.setStringValue("invalid");
        try {
            studyManagementService.save(stringToNumericDefinition);
            fail();
        } catch (ValidationException e) {
            value.setAnnotationDefinition(stringToNumericDefinition);
            stringToNumericDefinition.getAnnotationValueCollection().clear();
            stringToNumericDefinition.getAnnotationValueCollection().add(value);
        }
        value.setStringValue("1.23");
        studyManagementService.save(stringToNumericDefinition);
        assertEquals(1, stringToNumericDefinition.getAnnotationValueCollection().size());
        assertEquals(Double.valueOf(1.23),
                ((NumericAnnotationValue)stringToNumericDefinition.getAnnotationValueCollection().iterator().next()).getNumericValue());

    }
    
    @Test
    public void testAddExternalLinksToStudy() throws ValidationException, IOException {
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        ExternalLinkList externalLinkList = new ExternalLinkList();
        externalLinkList.setFile(TestDataFiles.SIMPLE_EXTERNAL_LINKS_FILE);
        studyManagementService.addExternalLinksToStudy(studyConfiguration, externalLinkList);
        assertEquals(6, externalLinkList.getExternalLinks().size());
        assertEquals(3, externalLinkList.getLinksByCategory().keySet().size());
        assertTrue(studyConfiguration.getExternalLinkLists().contains(externalLinkList));
    }
    
    @Test
    public void testSaveAnnotationGroup() throws ValidationException, ConnectionException, IOException {
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        AnnotationGroup annotationGroup = new AnnotationGroup();
        annotationGroup.setName("Test");
        studyManagementService.saveAnnotationGroup(annotationGroup, studyConfiguration,
                TestDataFiles.ANNOTATION_GROUP_FILE);
        assertFalse(studyConfiguration.getStudy().getAnnotationGroups().isEmpty());
        assertEquals(5, annotationGroup.getAnnotationFieldDescriptors().size());
        
        // Test invalid because AFD already exist
        annotationGroup = new AnnotationGroup();
        annotationGroup.setName("Test_2");
        boolean catchException = false;
        try {
            studyManagementService.saveAnnotationGroup(annotationGroup, studyConfiguration,
                    TestDataFiles.ANNOTATION_GROUP_FILE);
        } catch (ValidationException e) {
            catchException = true;
        }
        assertTrue(catchException);
    }
    
    @Test
    public void testDeleteAnnotationGroup() {
        SecureDaoStub secureDaoStub = new SecureDaoStub();
        studyManagementService.setDao(secureDaoStub);
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        studyConfiguration.setStudy(new Study());
        AnnotationGroup group = new AnnotationGroup();
        AnnotationFieldDescriptor afd = new AnnotationFieldDescriptor();
        studyConfiguration.getStudy().getAnnotationGroups().add(group);
        group.getAnnotationFieldDescriptors().add(afd);
        secureDaoStub.stringCriterion.setAnnotationFieldDescriptor(afd);
        Query query = new Query();
        query.getCompoundCriterion().getCriterionCollection().add(secureDaoStub.stringCriterion);
        secureDaoStub.resultColumn.setQuery(query);
        query.getColumnCollection().add(secureDaoStub.resultColumn);
        studyManagementService.delete(studyConfiguration, group);
        assertFalse(studyConfiguration.getStudy().getAnnotationGroups().contains(group));
        assertEquals(null, secureDaoStub.stringCriterion.getAnnotationFieldDescriptor());
        assertTrue(query.getColumnCollection().isEmpty());
        
    }
    
    @Test
    public void testSetStudyLastModifiedByCurrentUser() {
        UserWorkspace lastModifiedBy = new UserWorkspace();
        Query query = new Query();
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        
        studyManagementService.setStudyLastModifiedByCurrentUser(studyConfiguration, lastModifiedBy, null, null);
        assertTrue(studyConfiguration.getLogEntries().isEmpty());
        assertTrue(studyConfiguration.getLastModifiedDate() != null);
        
        studyManagementService.setStudyLastModifiedByCurrentUser(studyConfiguration, lastModifiedBy, query, null);
        assertEquals(studyConfiguration.getLastModifiedDate(), query.getLastModifiedDate());
        assertTrue(studyConfiguration.getLogEntries().isEmpty());
        
        studyManagementService.setStudyLastModifiedByCurrentUser(studyConfiguration, lastModifiedBy, query, "Log Message1");
        assertEquals("Log Message1", studyConfiguration.getLogEntries().get(0).getSystemLogMessage());
        assertTrue(studyConfiguration.getLogEntries().get(0).getUsername() == null);
        
        lastModifiedBy.setUsername("username");
        studyManagementService.setStudyLastModifiedByCurrentUser(studyConfiguration, lastModifiedBy, query, "Log Message2");
        assertEquals("Log Message1", studyConfiguration.getLogEntries().get(0).getSystemLogMessage());
        assertEquals("Log Message2", studyConfiguration.getLogEntries().get(1).getSystemLogMessage());
        assertEquals("username", studyConfiguration.getLogEntries().get(1).getUsername());
        
        
        
        
    }
    
    private static class SecureDaoStub extends CaIntegrator2DaoStub {
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        StringComparisonCriterion stringCriterion = new StringComparisonCriterion();
        ResultColumn resultColumn = new ResultColumn();
        
        @Override
        @SuppressWarnings("unchecked")
        public <T> T get(Long id, java.lang.Class<T> objectClass) {
            Study study = new Study();
            studyConfiguration.setStudy(study);
            study.setStudyConfiguration(studyConfiguration);
            return (T) studyConfiguration;
        }
        
        @Override
        public List<Study> getStudies(String username) {
            List<Study> studies = new ArrayList<Study>();
            if ("valid".equals(username)) {
                studies.add(studyConfiguration.getStudy());
            }
            return studies;
        }
        
        @Override
        public List<AbstractAnnotationCriterion> getCriteriaUsingAnnotation(AnnotationFieldDescriptor fieldDescriptor) {
            List<AbstractAnnotationCriterion> criteria = new ArrayList<AbstractAnnotationCriterion>();
            criteria.add(stringCriterion);
            return criteria;
        }
        
        @Override
        public List<ResultColumn> getResultColumnsUsingAnnotation(AnnotationFieldDescriptor fieldDescriptor) {
            List<ResultColumn> columns = new ArrayList<ResultColumn>();
            columns.add(resultColumn);
            return columns;
        }
    }
}
