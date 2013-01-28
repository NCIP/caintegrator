/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.web.action.study.management;

import static org.junit.Assert.assertEquals;
import gov.nih.nci.caintegrator.application.study.StudyManagementServiceStub;
import gov.nih.nci.caintegrator.mockito.AbstractMockitoTest;
import gov.nih.nci.caintegrator.web.action.study.management.EditSampleMappingAction;

import org.junit.Before;
import org.junit.Test;

import com.opensymphony.xwork2.Action;

public class EditSampleMappingActionTest extends AbstractMockitoTest {

    private EditSampleMappingAction action;
    private StudyManagementServiceStub studyManagementServiceStub;

    @Before
    public void setUp() {
        studyManagementServiceStub = new StudyManagementServiceStub();
        action = new EditSampleMappingAction();
        action.setStudyManagementService(studyManagementServiceStub);
        action.setWorkspaceService(workspaceService);
    }

    @Test
    public void testExecute() {
        assertEquals(Action.SUCCESS, action.execute());
    }
}
