/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.web.action.study.management;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.application.study.StudyManagementServiceStub;
import gov.nih.nci.caintegrator2.web.SessionHelper;
import gov.nih.nci.caintegrator2.web.action.AbstractSessionBasedTest;

import org.junit.Before;
import org.junit.Test;

public class AbstractStudyActionTest extends AbstractSessionBasedTest {

    private AbstractStudyAction abstractStudyAction;
    private StudyManagementServiceStub studyManagementServiceStub;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        studyManagementServiceStub = new StudyManagementServiceStub();
        abstractStudyAction = new SaveStudyAction();
        abstractStudyAction.setStudyManagementService(studyManagementServiceStub);
        abstractStudyAction.setWorkspaceService(workspaceService);
    }

    @Test
    public void testPrepare() {
        abstractStudyAction.prepare();
        assertFalse(studyManagementServiceStub.getRefreshedStudyConfigurationCalled);
        abstractStudyAction.getModel().setId(1L);

        abstractStudyAction.prepare();
        assertTrue(studyManagementServiceStub.getRefreshedStudyConfigurationCalled);
        assertTrue(SessionHelper.getInstance().isAuthorizedPage());
        abstractStudyAction.getModel().setId(1L);
        studyManagementServiceStub.clear();
        studyManagementServiceStub.isThrowCSException = true;
        abstractStudyAction.prepare();
        assertFalse(SessionHelper.getInstance().isAuthorizedPage());

        SessionHelper.getInstance().setStudyManager(false);
        abstractStudyAction.prepare();
        assertFalse(SessionHelper.getInstance().isAuthorizedPage());

    }

}
