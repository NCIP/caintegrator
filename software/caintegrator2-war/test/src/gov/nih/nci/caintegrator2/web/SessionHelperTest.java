/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.AcegiAuthenticationStub;
import gov.nih.nci.caintegrator2.application.workspace.WorkspaceServiceStub;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.application.UserWorkspace;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.web.action.AbstractSessionBasedTest;

import java.util.HashMap;

import org.acegisecurity.context.SecurityContextHolder;
import org.junit.Before;
import org.junit.Test;

import com.opensymphony.xwork2.ActionContext;

public class SessionHelperTest extends AbstractSessionBasedTest {

    private SessionHelper sessionHelper;
    private UserWorkspace userWorkspace;
    private StudySubscription studySubscription;
    private AcegiAuthenticationStub authentication = new AcegiAuthenticationStub();

    @Override
    @Before
    public void setUp() {
        super.setUp();

        authentication.setUsername("user");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        ActionContext.getContext().setSession(new HashMap<String, Object>());
        sessionHelper = SessionHelper.getInstance();
        userWorkspace = new UserWorkspace();
        studySubscription = new StudySubscription();
        Study study = new Study();
        study.setShortTitleText("fakeStudy");
        studySubscription.setStudy(study);
        studySubscription.setId(Long.valueOf(1));
        Query query1 = new Query();
        query1.setName("query1");
        Query query2 = new Query();
        query2.setName("query2");
        studySubscription.getQueryCollection().add(query1);
        studySubscription.getQueryCollection().add(query2);
        userWorkspace.getSubscriptionCollection().add(studySubscription);
    }

    /**
     * Test method for {@link gov.nih.nci.caintegrator2.web.DisplayableUserWorkspace#refreshUserWorkspace(gov.nih.nci.caintegrator2.domain.application.UserWorkspace)}.
     */
    @Test
    public void testRefresh() {
        WorkspaceServiceStub workspaceServiceStub = new WorkspaceServiceStub();
        workspaceServiceStub.setSubscription(studySubscription);
        sessionHelper.refresh(workspaceServiceStub, true);
        assertTrue(sessionHelper.getDisplayableUserWorkspace().getUserWorkspace().getSubscriptionCollection().size() == 1);
        assertEquals(SessionHelper.getUsername(), "user");
        assertTrue(sessionHelper.isAuthenticated());
        assertEquals(2, sessionHelper.getDisplayableUserWorkspace().getUserQueries().size());
    }

    @Test
    public void testRefreshAnonymousUser() {
        assertTrue(SessionHelper.getAnonymousUserWorkspace() == null);
        authentication.setUsername(UserWorkspace.ANONYMOUS_USER_NAME);
        WorkspaceServiceStub workspaceServiceStub = new WorkspaceServiceStub();
        sessionHelper.refresh(workspaceServiceStub, true);
        assertTrue(sessionHelper.getDisplayableUserWorkspace().getUserWorkspace().getSubscriptionCollection().size() == 1);
        assertEquals(SessionHelper.getUsername(), UserWorkspace.ANONYMOUS_USER_NAME);
        assertTrue(sessionHelper.isAuthenticated());
        assertTrue(workspaceServiceStub.getWorkspaceReadOnlyCalled);
        assertTrue(SessionHelper.getAnonymousUserWorkspace() != null);
    }

}
