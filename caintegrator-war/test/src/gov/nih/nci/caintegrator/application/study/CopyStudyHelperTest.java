/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.study;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator.TestDataFiles;
import gov.nih.nci.caintegrator.application.study.CopyStudyHelper;
import gov.nih.nci.caintegrator.application.study.DelimitedTextClinicalSourceConfiguration;
import gov.nih.nci.caintegrator.application.study.ExternalLink;
import gov.nih.nci.caintegrator.application.study.ExternalLinkList;
import gov.nih.nci.caintegrator.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator.application.study.StudyManagementServiceImpl;
import gov.nih.nci.caintegrator.application.study.ValidationException;
import gov.nih.nci.caintegrator.data.StudyHelper;
import gov.nih.nci.caintegrator.domain.annotation.SurvivalLengthUnitsEnum;
import gov.nih.nci.caintegrator.domain.annotation.SurvivalValueDefinition;
import gov.nih.nci.caintegrator.domain.annotation.SurvivalValueTypeEnum;
import gov.nih.nci.caintegrator.domain.translational.Study;
import gov.nih.nci.caintegrator.external.ConnectionException;
import gov.nih.nci.caintegrator.mockito.AbstractMockitoTest;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

/**
 * @author mshestopalov
 *
 */
public class CopyStudyHelperTest extends AbstractMockitoTest {

    private CopyStudyHelper helper;
    private StudyConfiguration copyFrom;
    private final String NAME = "name";
    private StudyManagementServiceImpl smSvc;

    @Before
    public void setUp() throws Exception {
        StudyHelper studyHelper = new StudyHelper();
        Study study = studyHelper.populateAndRetrieveStudyWithSourceConfigurations();
        copyFrom = study.getStudyConfiguration();
        copyFrom.setId(1L);
        copyFrom.setStudy(study);
        copyFrom.getStudy().setPubliclyAccessible(true);
        ExternalLinkList newExtList = new ExternalLinkList();
        newExtList.setDescription("description");
        newExtList.setName(NAME);
        newExtList.setFileName("filename");
        ExternalLink newExtLink = new ExternalLink();
        newExtLink.setCategory("category");
        newExtLink.setName(NAME);
        newExtLink.setUrl("url");
        newExtList.getExternalLinks().add(newExtLink);
        copyFrom.getExternalLinkLists().add(newExtList);
        SurvivalValueDefinition newSurv = new SurvivalValueDefinition();
        newSurv.setName("NAME");
        newSurv.setDeathDate(studyHelper.getSampleAnnotationDefinition());
        newSurv.setLastFollowupDate(studyHelper.getSampleAnnotationDefinition());
        newSurv.setSurvivalLength(studyHelper.getSampleAnnotationDefinition());
        newSurv.setSurvivalLengthUnits(SurvivalLengthUnitsEnum.DAYS);
        newSurv.setSurvivalStartDate(studyHelper.getSampleAnnotationDefinition());
        newSurv.setSurvivalStatus(studyHelper.getSampleAnnotationDefinition());
        newSurv.setValueForCensored(NAME);
        newSurv.setSurvivalValueType(SurvivalValueTypeEnum.DATE);
        copyFrom.getStudy().getSurvivalValueDefinitionCollection().add(newSurv);
        StudyManagementServiceTest smsTest = new StudyManagementServiceTest();
        smsTest.setUp();
        smSvc = smsTest.getStudyManagementServiceImpl();
        smSvc.setWorkspaceService(workspaceService);
        smSvc.setFileManager(fileManager);
        helper = new CopyStudyHelper(smSvc);
    }


    @Test
    public void testExternalLinks() {
        StudyConfiguration copyTo = createNewStudy(3L);
        assertTrue(copyTo.getExternalLinkLists().isEmpty());
        helper.copyExternalLinks(copyFrom, copyTo);
        assertFalse(copyTo.getExternalLinkLists().isEmpty());
        assertEquals(1, copyTo.getExternalLinkLists().size());
    }

    @Test
    public void testSurvivalValueDefinitions() {
        StudyConfiguration copyTo = createNewStudy(4L);
        assertTrue(copyTo.getStudy().getSurvivalValueDefinitionCollection().isEmpty());
        helper.copySurvivalDefinitions(copyFrom, copyTo);
        assertFalse(copyTo.getStudy().getSurvivalValueDefinitionCollection().isEmpty());
        assertEquals(1, copyTo.getStudy().getSurvivalValueDefinitionCollection().size());
    }

    @Test
    public void testGenomicDataSources() {
        StudyConfiguration copyTo = createNewStudy(5L);
        assertTrue(copyTo.getGenomicDataSources().isEmpty());
        helper.copyStudyGenomicSource(copyFrom, copyTo);
        assertFalse(copyTo.getGenomicDataSources().isEmpty());
        assertEquals(1, copyTo.getGenomicDataSources().size());
    }

    @Test
    public void testImageDataSources() {
        StudyConfiguration copyTo = createNewStudy(6L);
        assertTrue(copyTo.getImageDataSources().isEmpty());
        helper.copyStudyImageSource(copyFrom, copyTo);
        assertFalse(copyTo.getImageDataSources().isEmpty());
        assertEquals(1, copyTo.getImageDataSources().size());
    }

    private StudyConfiguration createNewStudy(Long id) {
        StudyConfiguration copyTo = new StudyConfiguration();
        copyTo.setId(id);
        copyTo.setStudy(new Study());
        return copyTo;
    }

    @Test
    public void testAnnotationGroups() throws ValidationException, ConnectionException, IOException {
        StudyConfiguration copyTo = createNewStudy(7L);
        assertTrue(copyTo.getStudy().getAnnotationGroups().isEmpty());
        helper.copyAnnotationGroups(copyFrom, copyTo);
        assertFalse(copyTo.getStudy().getAnnotationGroups().isEmpty());
        assertEquals(1, copyTo.getStudy().getAnnotationGroups().size());
    }

    @Test
    public void testSubjectAnnotationGroups() throws ValidationException, IOException {
        StudyConfiguration copyTo = createNewStudy(8L);
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        smSvc.save(studyConfiguration);
        DelimitedTextClinicalSourceConfiguration sourceConfiguration =
            smSvc.addClinicalAnnotationFile(studyConfiguration, TestDataFiles.VALID_FILE, TestDataFiles.VALID_FILE.getName(),
                    false);
        assertEquals(1, studyConfiguration.getClinicalConfigurationCollection().size());
        assertTrue(copyTo.getClinicalConfigurationCollection().isEmpty());
        helper.copySubjectAnnotationGroups(studyConfiguration, copyTo);
        assertFalse(copyTo.getClinicalConfigurationCollection().isEmpty());
        assertEquals(1, copyTo.getClinicalConfigurationCollection().size());
    }

    @Test
    public void testAddStudyLogo() throws IOException {
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        smSvc.save(studyConfiguration);
        smSvc.addStudyLogo(studyConfiguration, TestDataFiles.VALID_FILE, TestDataFiles.VALID_FILE.getName(), "image/jpeg");
        assertEquals(TestDataFiles.VALID_FILE.getName(), studyConfiguration.getStudyLogo().getFileName());
    }

}
