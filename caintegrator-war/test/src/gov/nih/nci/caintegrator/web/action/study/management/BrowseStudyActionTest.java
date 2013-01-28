/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.web.action.study.management;

import static org.junit.Assert.assertEquals;
import gov.nih.nci.caintegrator.domain.application.StudySubscription;
import gov.nih.nci.caintegrator.web.SessionHelper;
import gov.nih.nci.caintegrator.web.action.AbstractSessionBasedTest;
import gov.nih.nci.caintegrator.web.action.study.management.BrowseStudyAction;

import org.junit.Before;
import org.junit.Test;

import com.opensymphony.xwork2.Action;

public class BrowseStudyActionTest extends AbstractSessionBasedTest {

    private BrowseStudyAction action;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        action = new BrowseStudyAction();
        action.setWorkspaceService(workspaceService);
        action.prepare();
    }

    @Test
    public void testExecute() {
        SessionHelper.getInstance().getDisplayableUserWorkspace().setCurrentStudySubscription(null);
        assertEquals(Action.ERROR, action.execute());
        StudySubscription subscription = new StudySubscription();
        subscription.setId(1L);
        SessionHelper.getInstance().getDisplayableUserWorkspace().setCurrentStudySubscription(subscription);
        assertEquals(Action.SUCCESS, action.execute());
    }
}
