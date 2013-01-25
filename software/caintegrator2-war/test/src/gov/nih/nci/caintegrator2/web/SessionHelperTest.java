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
import gov.nih.nci.caintegrator2.web.SessionHelper;

import java.util.HashMap;
import java.util.HashSet;

import org.acegisecurity.context.SecurityContextHolder;
import org.junit.Before;
import org.junit.Test;

import com.opensymphony.xwork2.ActionContext;

public class SessionHelperTest {
    
    private SessionHelper sessionHelper;
    private UserWorkspace userWorkspace;
    private StudySubscription studySubscription;
    
    @Before
    public void setUp() {
        AcegiAuthenticationStub authentication = new AcegiAuthenticationStub();
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
        Query query2 = new Query();
        studySubscription.setQueryCollection(new HashSet<Query>());
        studySubscription.getQueryCollection().add(query1);
        studySubscription.getQueryCollection().add(query2);
        userWorkspace.setSubscriptionCollection(new HashSet<StudySubscription>());
        userWorkspace.getSubscriptionCollection().add(studySubscription);
    }

    /**
     * Test method for {@link gov.nih.nci.caintegrator2.web.DisplayableUserWorkspace#refreshUserWorkspace(gov.nih.nci.caintegrator2.domain.application.UserWorkspace)}.
     */
    @Test
    public void testRefresh() {
        WorkspaceServiceStub workspaceServiceStub = new WorkspaceServiceStub();
        sessionHelper.refresh(workspaceServiceStub);
        assertTrue(sessionHelper.getDisplayableUserWorkspace().getUserWorkspace().getSubscriptionCollection().size() == 1);
        assertEquals(sessionHelper.getUsername(), "user");
        assertTrue(sessionHelper.isAuthenticated());
    }
    
}
