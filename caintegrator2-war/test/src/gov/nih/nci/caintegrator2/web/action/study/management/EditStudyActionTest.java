/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.study.management;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.application.study.StudyLogo;
import gov.nih.nci.caintegrator2.application.study.StudyManagementServiceStub;
import gov.nih.nci.caintegrator2.web.action.AbstractSessionBasedTest;

import org.junit.Before;
import org.junit.Test;

import com.opensymphony.xwork2.Action;

public class EditStudyActionTest extends AbstractSessionBasedTest {

    private EditStudyAction editStudyAction;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        editStudyAction = new EditStudyAction();
        editStudyAction.setStudyManagementService(new StudyManagementServiceStub());
        editStudyAction.setWorkspaceService(workspaceService);
    }

    @Test
    public void testExecute() {
        assertEquals(Action.SUCCESS, editStudyAction.execute());
    }

    @Test
    public void testManageStudy() {
        assertEquals(Action.SUCCESS, editStudyAction.manageStudies());
    }

    @Test
    public void testDelete() {
        assertEquals(Action.SUCCESS, editStudyAction.deleteStudy());
    }

    @Test
    public void testDisable() {
        assertEquals(Action.SUCCESS, editStudyAction.disableStudy());
    }

    @Test
    public void testEnable() {
        assertEquals(Action.SUCCESS, editStudyAction.enableStudy());
    }

    @Test
    public void testGetModel() {
        assertNotNull(editStudyAction.getModel());
    }

    @Test
    public void testStudyLogo() {
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        studyConfiguration.setId(1L);
        studyConfiguration.setStudyLogo(new StudyLogo());
        editStudyAction.setStudyConfiguration(studyConfiguration);
        editStudyAction.prepare();
        assertEquals(Action.SUCCESS, editStudyAction.retrieveStudyLogoPreview());
    }
}
