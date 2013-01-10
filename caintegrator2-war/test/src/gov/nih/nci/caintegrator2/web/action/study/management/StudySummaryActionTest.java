/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.web.action.study.management;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.web.SessionHelper;
import gov.nih.nci.caintegrator2.web.action.AbstractSessionBasedTest;

import org.junit.Before;
import org.junit.Test;

import com.opensymphony.xwork2.Action;

/**
 *
 */
public class StudySummaryActionTest extends AbstractSessionBasedTest {

    private StudySummaryAction action;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        action = new StudySummaryAction();
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
        verify(workspaceService, times(1)).createDisplayableStudySummary(any(Study.class));
        assertNotNull(action.getStudySummary());
    }
}
