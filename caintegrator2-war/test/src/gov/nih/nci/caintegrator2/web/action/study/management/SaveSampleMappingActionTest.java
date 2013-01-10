/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.web.action.study.management;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.TestDataFiles;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.application.study.StudyManagementServiceStub;
import gov.nih.nci.caintegrator2.domain.genomic.SampleSet;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.web.action.AbstractSessionBasedTest;

import org.junit.Before;
import org.junit.Test;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

public class SaveSampleMappingActionTest extends AbstractSessionBasedTest {

    private SaveSampleMappingAction action;
    private StudyManagementServiceStub studyManagementServiceStub;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        studyManagementServiceStub = new StudyManagementServiceStub();
        action = new SaveSampleMappingAction();
        action.setStudyManagementService(studyManagementServiceStub);
        action.setWorkspaceService(workspaceService);
    }

    @Test
    public void testExecuteForSampleMapping() {
        action.setSampleMappingFile(TestDataFiles.VALID_FILE);
        Study study = action.getStudyConfiguration().getStudy();
        study.setShortTitleText("Invalid");
        assertEquals(Action.INPUT, action.execute());
        study.setShortTitleText("IOException");
        assertEquals(Action.INPUT, action.execute());
        study.setShortTitleText("Valid");
        assertEquals(Action.SUCCESS, action.execute());
        assertTrue(action.isFileUpload());
        action.setSampleMappingFile(null);
        assertEquals(null, action.getSampleMappingFile());
        action.setSampleMappingFileContentType("Genomic");
        assertEquals("Genomic", action.getSampleMappingFileContentType());
        assertEquals(null, action.getSampleMappingFileFileName());
        action.validate();
        assertTrue(action.hasFieldErrors());
        action.setSampleMappingFileFileName("TestFile");
        assertEquals("TestFile", action.getSampleMappingFileFileName());
        action.validate();

    }

    @Test
    public void testValidateForControlMapping() {
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        action.setStudyConfiguration(studyConfiguration);

        action.validate();
        assertTrue(action.hasErrors());
        action.clearErrorsAndMessages();
        action.setControlSampleSetName("ControlSampleSet1");
        action.validate();
        assertTrue(action.hasErrors());
        action.clearErrorsAndMessages();
        action.setControlSampleFile(TestDataFiles.REMBRANDT_CONTROL_SAMPLES_FILE);
        action.validate();
        assertFalse(action.hasFieldErrors());
        action.clearErrorsAndMessages();
        SampleSet controlSampleSet = new SampleSet();
        controlSampleSet.setName("ControlSampleSet1");
        GenomicDataSourceConfiguration genomicSource = new GenomicDataSourceConfiguration();
        studyConfiguration.getGenomicDataSources().add(genomicSource);
        genomicSource.getControlSampleSetCollection().add(controlSampleSet);
        action.validate();
        assertTrue(action.hasFieldErrors());
    }

    @Test
    public void testExecuteForControlMapping() {
        action.setControlSampleFile(TestDataFiles.REMBRANDT_CONTROL_SAMPLES_FILE);
        action.setControlSampleSetName("ControlSampleSet1");
        assertEquals(ActionSupport.SUCCESS, action.execute());
        assertTrue(studyManagementServiceStub.addControlSampleSetCalled);
    }
}
