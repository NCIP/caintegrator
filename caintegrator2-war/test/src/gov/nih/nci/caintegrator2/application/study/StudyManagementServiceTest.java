/**
 * The software subject to this notice and license includes both human readable
 * source code form and machine readable, binary, object code form. The caIntegrator2
 * Software was developed in conjunction with the National Cancer Institute 
 * (NCI) by NCI employees, 5AM Solutions, Inc. (5AM), ScenPro, Inc. (ScenPro)
 * and Science Applications International Corporation (SAIC). To the extent 
 * government employees are authors, any rights in such works shall be subject 
 * to Title 17 of the United States Code, section 105. 
 *
 * This caIntegrator2 Software License (the License) is between NCI and You. You (or 
 * Your) shall mean a person or an entity, and all other entities that control, 
 * are controlled by, or are under common control with the entity. Control for 
 * purposes of this definition means (i) the direct or indirect power to cause 
 * the direction or management of such entity, whether by contract or otherwise,
 * or (ii) ownership of fifty percent (50%) or more of the outstanding shares, 
 * or (iii) beneficial ownership of such entity. 
 *
 * This License is granted provided that You agree to the conditions described 
 * below. NCI grants You a non-exclusive, worldwide, perpetual, fully-paid-up, 
 * no-charge, irrevocable, transferable and royalty-free right and license in 
 * its rights in the caIntegrator2 Software to (i) use, install, access, operate, 
 * execute, copy, modify, translate, market, publicly display, publicly perform,
 * and prepare derivative works of the caIntegrator2 Software; (ii) distribute and 
 * have distributed to and by third parties the caIntegrator2 Software and any 
 * modifications and derivative works thereof; and (iii) sublicense the 
 * foregoing rights set out in (i) and (ii) to third parties, including the 
 * right to license such rights to further third parties. For sake of clarity, 
 * and not by way of limitation, NCI shall have no right of accounting or right 
 * of payment from You or Your sub-licensees for the rights granted under this 
 * License. This License is granted at no charge to You.
 *
 * Your redistributions of the source code for the Software must retain the 
 * above copyright notice, this list of conditions and the disclaimer and 
 * limitation of liability of Article 6, below. Your redistributions in object 
 * code form must reproduce the above copyright notice, this list of conditions 
 * and the disclaimer of Article 6 in the documentation and/or other materials 
 * provided with the distribution, if any. 
 *
 * Your end-user documentation included with the redistribution, if any, must 
 * include the following acknowledgment: This product includes software 
 * developed by 5AM, ScenPro, SAIC and the National Cancer Institute. If You do 
 * not include such end-user documentation, You shall include this acknowledgment 
 * in the Software itself, wherever such third-party acknowledgments normally 
 * appear.
 *
 * You may not use the names "The National Cancer Institute", "NCI", "ScenPro",
 * "SAIC" or "5AM" to endorse or promote products derived from this Software. 
 * This License does not authorize You to use any trademarks, service marks, 
 * trade names, logos or product names of either NCI, ScenPro, SAID or 5AM, 
 * except as required to comply with the terms of this License. 
 *
 * For sake of clarity, and not by way of limitation, You may incorporate this 
 * Software into Your proprietary programs and into any third party proprietary 
 * programs. However, if You incorporate the Software into third party 
 * proprietary programs, You agree that You are solely responsible for obtaining
 * any permission from such third parties required to incorporate the Software 
 * into such third party proprietary programs and for informing Your a
 * sub-licensees, including without limitation Your end-users, of their 
 * obligation to secure any required permissions from such third parties before 
 * incorporating the Software into such third party proprietary software 
 * programs. In the event that You fail to obtain such permissions, You agree 
 * to indemnify NCI for any claims against NCI by such third parties, except to 
 * the extent prohibited by law, resulting from Your failure to obtain such 
 * permissions. 
 *
 * For sake of clarity, and not by way of limitation, You may add Your own 
 * copyright statement to Your modifications and to the derivative works, and 
 * You may provide additional or different license terms and conditions in Your 
 * sublicenses of modifications of the Software, or any derivative works of the 
 * Software as a whole, provided Your use, reproduction, and distribution of the
 * Work otherwise complies with the conditions stated in this License.
 *
 * THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED WARRANTIES, 
 * (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY, 
 * NON-INFRINGEMENT AND FITNESS FOR A PARTICULAR PURPOSE) ARE DISCLAIMED. IN NO 
 * EVENT SHALL THE NATIONAL CANCER INSTITUTE, 5AM SOLUTIONS, INC., SCENPRO, INC.,
 * SCIENCE APPLICATIONS INTERNATIONAL CORPORATION OR THEIR 
 * AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR 
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF 
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
import gov.nih.nci.caintegrator2.domain.annotation.AbstractAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.AbstractPermissibleValue;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.annotation.CommonDataElement;
import gov.nih.nci.caintegrator2.domain.annotation.StringAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.StringPermissibleValue;
import gov.nih.nci.caintegrator2.domain.annotation.SubjectAnnotation;
import gov.nih.nci.caintegrator2.domain.annotation.SurvivalValueDefinition;
import gov.nih.nci.caintegrator2.domain.annotation.ValueDomain;
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;
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
            studyManagementService.addClinicalAnnotationFile(studyConfiguration, TestDataFiles.VALID_FILE, TestDataFiles.VALID_FILE.getName());
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
            studyManagementService.addClinicalAnnotationFile(studyConfiguration, TestDataFiles.VALID_FILE, TestDataFiles.VALID_FILE.getName());
        sourceConfiguration.getAnnotationFile().setIdentifierColumnIndex(0);
        AnnotationDefinition definition = new AnnotationDefinition();
        definition.setType(AnnotationTypeEnum.NUMERIC.getValue());
        sourceConfiguration.getAnnotationFile().getColumns().get(1).getFieldDescriptor().setDefinition(definition);
        definition = new AnnotationDefinition();
        definition.setType(AnnotationTypeEnum.STRING.getValue());
        sourceConfiguration.getAnnotationFile().getColumns().get(2).getFieldDescriptor().setDefinition(definition);
        definition = new AnnotationDefinition();
        definition.setType(AnnotationTypeEnum.STRING.getValue());
        sourceConfiguration.getAnnotationFile().getColumns().get(3).getFieldDescriptor().setDefinition(definition);
        definition = new AnnotationDefinition();
        definition.setType(AnnotationTypeEnum.DATE.getValue());
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
        assertNotNull(studyManagementService.getRefreshedStudyEntity(studyConfiguration));
        assertTrue(daoStub.getCalled);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetStudyEntityNoId() {
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        studyManagementService.getRefreshedStudyEntity(studyConfiguration);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetStudyEntityIllegalClass() {
        Object object = new Object();
        studyManagementService.getRefreshedStudyEntity(object);
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
        
        AbstractAnnotationValue value1 = new StringAnnotationValue();
        AbstractAnnotationValue value2 = new StringAnnotationValue();
        AbstractAnnotationValue value3 = new StringAnnotationValue();
        value1.setSampleAcquisition(sampleAcquisition);
        value2.setImageSeries(imageSeries);
        value3.setSubjectAnnotation(subjectAnnotation);
        AnnotationDefinition firstDefinition = new AnnotationDefinition();
        firstDefinition.getAnnotationValueCollection().add(value1);
        firstDefinition.getAnnotationValueCollection().add(value2);
        firstDefinition.getAnnotationValueCollection().add(value3);
        firstDefinition.setId(1L);
        firstDefinition.setType(AnnotationTypeEnum.STRING.getValue());
        studyManagementService.setDefinition(study, fileColumn, firstDefinition, EntityTypeEnum.IMAGESERIES);
        assertTrue(daoStub.saveCalled);
        assertEquals(firstDefinition, fileColumn.getFieldDescriptor().getDefinition());
        assertTrue(study.getImageSeriesAnnotationCollection().contains(firstDefinition));
        assertTrue(firstDefinition.getAnnotationValueCollection().contains(value1));
        
        // Now create a new Definition and set it and verify the first definition is removed.
        AnnotationDefinition newDefinition = new AnnotationDefinition();
        newDefinition.setId(2L);
        newDefinition.setType(AnnotationTypeEnum.STRING.getValue());
        studyManagementService.setDefinition(study, fileColumn, newDefinition, EntityTypeEnum.IMAGESERIES);
        assertFalse(study.getImageSeriesAnnotationCollection().contains(firstDefinition));
        assertTrue(study.getImageSeriesAnnotationCollection().contains(newDefinition));
        assertTrue(newDefinition.getAnnotationValueCollection().contains(value1));
        assertTrue(newDefinition.getAnnotationValueCollection().contains(value2));
        assertTrue(newDefinition.getAnnotationValueCollection().contains(value3));
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
        originalDefinition.setType(AnnotationTypeEnum.STRING.getValue());
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
        
        CommonDataElement dataElement = new CommonDataElement();
        dataElement.setLongName("longName");
        dataElement.setDefinition("definition");
        dataElement.setPublicID(1234L);
        ValueDomain valueDomain = new ValueDomain();
        valueDomain.setDataType(AnnotationTypeEnum.STRING.getValue());
        dataElement.setValueDomain(valueDomain);
        valueDomain.setPermissibleValueCollection(new HashSet<AbstractPermissibleValue>());
        StringPermissibleValue permissibleValue = new StringPermissibleValue();
        permissibleValue.setId(Long.valueOf(1));
        permissibleValue.setStringValue("Valid");
        valueDomain.getPermissibleValueCollection().add(permissibleValue);
        
        studyManagementService.setDataElement(fileColumn, dataElement, study, EntityTypeEnum.SUBJECT, "");
        assertTrue(daoStub.saveCalled);
        assertNotNull(fileColumn.getFieldDescriptor().getDefinition());
        assertNotNull(fileColumn.getFieldDescriptor().getDefinition().getCde());
        assertEquals("longName", fileColumn.getFieldDescriptor().getDefinition().getDisplayName());
        assertEquals("definition", fileColumn.getFieldDescriptor().getDefinition().getPreferredDefinition());
        assertEquals(Long.valueOf(1234), fileColumn.getFieldDescriptor().getDefinition().getCde().getPublicID());
        AnnotationDefinition firstDefinition = fileColumn.getFieldDescriptor().getDefinition();
        assertTrue(firstDefinition.getPermissibleValueCollection().iterator().next().equals(permissibleValue));
        assertTrue(firstDefinition.getAnnotationValueCollection().size() == 1);
        assertTrue(study.getSubjectAnnotationCollection().contains(firstDefinition));
        
        // Add a value that isn't permissible and catch the exception.
        StringAnnotationValue invalidValue = new StringAnnotationValue();
        SubjectAnnotation subjectAnnotation2 = new SubjectAnnotation();
        studySubjectAssignment.setStudy(study);
        subjectAnnotation2.setStudySubjectAssignment(studySubjectAssignment);
        invalidValue.setStringValue("INVALID");
        invalidValue.setSubjectAnnotation(subjectAnnotation2);
        fileColumn.getFieldDescriptor().getDefinition().getAnnotationValueCollection().add(invalidValue);
        boolean exceptionCaught = false;
        try {
            studyManagementService.setDataElement(fileColumn, dataElement, study, EntityTypeEnum.SUBJECT, "");
        } catch (ValidationException e) {
            exceptionCaught = true;
        }
        assertTrue(exceptionCaught);
        
        // Now set a different data element and verify the first one is no longer in the study's collection
        CommonDataElement dataElement2 = new CommonDataElement();
        dataElement2.setLongName("longName2");
        dataElement2.setDefinition("definition2");
        dataElement2.setPublicID(123L);
        dataElement2.setType(AnnotationTypeEnum.STRING.getValue());
        studyManagementService.setDataElement(fileColumn, dataElement2, study, EntityTypeEnum.SUBJECT, "");
        AnnotationDefinition newDefinition = fileColumn.getFieldDescriptor().getDefinition();
        assertFalse(study.getSubjectAnnotationCollection().contains(firstDefinition));
        assertTrue(study.getSubjectAnnotationCollection().contains(newDefinition));
        
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
    public void testAddImageAnnotationFile() throws ValidationException, IOException, ConnectionException {
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        ImageDataSourceConfiguration imageDataSourceConfiguration = new ImageDataSourceConfiguration();
        studyManagementService.addImageSource(studyConfiguration, imageDataSourceConfiguration);
        studyManagementService.save(studyConfiguration);
        ImageAnnotationConfiguration imageAnnotationConfiguration = 
            studyManagementService.addImageAnnotationFile(imageDataSourceConfiguration, TestDataFiles.VALID_FILE, TestDataFiles.VALID_FILE.getName());
        assertEquals(5, imageAnnotationConfiguration.getAnnotationFile().getColumns().size());
        assertTrue(daoStub.saveCalled);
    }

    @Test
    public void testAddImageSource() throws ConnectionException, ValidationException, IOException {
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        ImageDataSourceConfiguration imageDataSourceConfiguration = new ImageDataSourceConfiguration();
        studyManagementService.addImageSource(studyConfiguration, imageDataSourceConfiguration);
        imageDataSourceConfiguration.setId(Long.valueOf(1));
        ImageAnnotationConfiguration imageAnnotationConfiguration = 
            studyManagementService.addImageAnnotationFile(imageDataSourceConfiguration, TestDataFiles.VALID_FILE, TestDataFiles.VALID_FILE.getName());
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
            studyManagementService.addImageAnnotationFile(imageDataSourceConfiguration, TestDataFiles.VALID_FILE, TestDataFiles.VALID_FILE.getName());
        imageAnnotationConfiguration.getAnnotationFile().setIdentifierColumnIndex(0);
        AnnotationDefinition definition = new AnnotationDefinition();
        definition.setType(AnnotationTypeEnum.NUMERIC.getValue());
        imageAnnotationConfiguration.getAnnotationFile().getColumns().get(1).getFieldDescriptor().setDefinition(definition);
        definition = new AnnotationDefinition();
        definition.setType(AnnotationTypeEnum.STRING.getValue());
        imageAnnotationConfiguration.getAnnotationFile().getColumns().get(2).getFieldDescriptor().setDefinition(definition);
        definition = new AnnotationDefinition();
        definition.setType(AnnotationTypeEnum.STRING.getValue());
        imageAnnotationConfiguration.getAnnotationFile().getColumns().get(3).getFieldDescriptor().setDefinition(definition);
        definition = new AnnotationDefinition();
        definition.setType(AnnotationTypeEnum.DATE.getValue());
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
    }
    
    @Test
    public void testCreateDefinition() {
        AnnotationFieldDescriptor descriptor = new AnnotationFieldDescriptor();
        Study study = new Study();
        descriptor.setName("testName");
        AnnotationDefinition definition = studyManagementService.createDefinition(descriptor, study, EntityTypeEnum.SUBJECT);
        assertEquals(descriptor.getName(), definition.getDisplayName());
        assertEquals(descriptor.getName(), definition.getKeywords());
        assertEquals(1, study.getSubjectAnnotationCollection().size());
        definition = studyManagementService.createDefinition(descriptor, study, EntityTypeEnum.IMAGESERIES);
        assertEquals(1, study.getImageSeriesAnnotationCollection().size());
        definition = studyManagementService.createDefinition(descriptor, study, EntityTypeEnum.SAMPLE);
        assertEquals(1, study.getSampleAnnotationCollection().size());
    }
    
    @Test
    public void testIsDuplicateStudyName() {
        Study study = new Study();
        study.setShortTitleText("studyName");
        assertFalse(studyManagementService.isDuplicateStudyName(study, ""));
        assertTrue(daoStub.isDuplicateStudyNameCalled);
    }
    
    @Test
    public void testCreateNewSurvivalValueDefinition() {
        Study study = StudyConfigurationFactory.createNewStudyConfiguration().getStudy();
        SurvivalValueDefinition definition = studyManagementService.createNewSurvivalValueDefinition(study);
        assertTrue(study.getSurvivalValueDefinitionCollection().contains(definition));
        assertTrue(daoStub.saveCalled);
    }
    
    @Test
    public void testRemoveSurvivalValueDefinition() {
        Study study = StudyConfigurationFactory.createNewStudyConfiguration().getStudy();
        SurvivalValueDefinition definition = studyManagementService.createNewSurvivalValueDefinition(study);
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
    
    private static class SecureDaoStub extends CaIntegrator2DaoStub {
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        
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
    }
}
