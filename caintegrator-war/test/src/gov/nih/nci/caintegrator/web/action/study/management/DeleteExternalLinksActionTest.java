/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.web.action.study.management;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator.application.study.StudyManagementServiceStub;
import gov.nih.nci.caintegrator.web.action.AbstractSessionBasedTest;
import gov.nih.nci.caintegrator.web.action.study.management.DeleteExternalLinksAction;

import org.junit.Before;
import org.junit.Test;

import com.opensymphony.xwork2.Action;

public class DeleteExternalLinksActionTest extends AbstractSessionBasedTest {

    private DeleteExternalLinksAction action;
    private StudyManagementServiceStub studyManagementServiceStub;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        studyManagementServiceStub = new StudyManagementServiceStub();
        action = new DeleteExternalLinksAction();
        action.setStudyManagementService(studyManagementServiceStub);
        action.setWorkspaceService(workspaceService);
    }

    @Test
    public void testPrepare() {
        action.prepare();
        assertFalse(studyManagementServiceStub.getRefreshedStudyEntityCalled);
        action.getExternalLinkList().setId(1l);
        action.prepare();
        assertTrue(studyManagementServiceStub.getRefreshedStudyEntityCalled);

    }

    @Test
    public void testExecute() {
        assertEquals(Action.ERROR, action.execute());
        action.getExternalLinkList().setName("validName");
        assertEquals(Action.SUCCESS, action.execute());

    }

}
