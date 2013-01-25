/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web;

import static org.junit.Assert.assertEquals;
import gov.nih.nci.caintegrator2.AcegiAuthenticationStub;
import gov.nih.nci.caintegrator2.application.study.Status;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
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


public class DisplayableUserWorkspaceTest extends AbstractSessionBasedTest {
    private UserWorkspace userWorkspace;
    private static Integer ID = 1;
    
    @Before
    public void setUp() {
        super.setUp();
        AcegiAuthenticationStub authentication = new AcegiAuthenticationStub();
        authentication.setUsername("user");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        ActionContext.getContext().setSession(new HashMap<String, Object>());
        
        SessionHelper.getInstance().refresh(new WorkspaceServiceStub(), true);
        userWorkspace = SessionHelper.getInstance().getDisplayableUserWorkspace().getUserWorkspace();
    }

    @Test
    public void testGetOrderedSubscriptionList() {
        userWorkspace.getSubscriptionCollection().clear();
        userWorkspace.getSubscriptionCollection().add(createStudySubscription("Deployed2", Status.DEPLOYED));
        userWorkspace.getSubscriptionCollection().add(createStudySubscription("Deployed1", Status.DEPLOYED));
        userWorkspace.getSubscriptionCollection().add(createStudySubscription("Processing", Status.PROCESSING));
        userWorkspace.getSubscriptionCollection().add(createStudySubscription("Error", Status.ERROR));
        
        DisplayableUserWorkspace displayableWorkspace = SessionHelper.getInstance().getDisplayableUserWorkspace();
        assertEquals(2, displayableWorkspace.getOrderedSubscriptionList().size());
        assertEquals("Deployed1", 
                displayableWorkspace.getOrderedSubscriptionList().get(0).getStudy().getShortTitleText());
        assertEquals("Deployed2", 
                displayableWorkspace.getOrderedSubscriptionList().get(1).getStudy().getShortTitleText());
    }
    
    private StudySubscription createStudySubscription(String studyName, Status status) {
        StudySubscription studySubscription = new StudySubscription();
        Study study = new Study();
        study.setShortTitleText(studyName);
        studySubscription.setStudy(study);
        studySubscription.setId(Long.valueOf(ID++));
        Query query1 = new Query();
        Query query2 = new Query();
        studySubscription.getQueryCollection().add(query1);
        studySubscription.getQueryCollection().add(query2);
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        studyConfiguration.setStatus(status);
        study.setStudyConfiguration(studyConfiguration);
        return studySubscription;
    }

}
