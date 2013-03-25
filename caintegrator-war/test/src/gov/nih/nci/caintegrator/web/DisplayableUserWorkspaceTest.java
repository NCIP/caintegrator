/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web;

import static org.junit.Assert.assertEquals;
import gov.nih.nci.caintegrator.AcegiAuthenticationStub;
import gov.nih.nci.caintegrator.application.study.Status;
import gov.nih.nci.caintegrator.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator.domain.application.Query;
import gov.nih.nci.caintegrator.domain.application.StudySubscription;
import gov.nih.nci.caintegrator.domain.application.UserWorkspace;
import gov.nih.nci.caintegrator.domain.translational.Study;
import gov.nih.nci.caintegrator.web.action.AbstractSessionBasedTest;

import java.util.HashMap;

import org.acegisecurity.context.SecurityContextHolder;
import org.junit.Before;
import org.junit.Test;

import com.opensymphony.xwork2.ActionContext;


public class DisplayableUserWorkspaceTest extends AbstractSessionBasedTest {
    private UserWorkspace userWorkspace;
    private Integer id = 1;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        AcegiAuthenticationStub authentication = new AcegiAuthenticationStub();
        authentication.setUsername("user");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        ActionContext.getContext().setSession(new HashMap<String, Object>());

        SessionHelper.getInstance().refresh(workspaceService, true);
        userWorkspace = SessionHelper.getInstance().getDisplayableUserWorkspace().getUserWorkspace();
    }

    @Test
    public void testGetOrderedSubscriptionList() {
        userWorkspace.getSubscriptionCollection().clear();
        userWorkspace.getSubscriptionCollection().add(createStudySubscription("Deployed2", Status.DEPLOYED, false));
        userWorkspace.getSubscriptionCollection().add(createStudySubscription("Deployed1", Status.DEPLOYED, false));
        userWorkspace.getSubscriptionCollection().add(createStudySubscription("Processing", Status.PROCESSING, false));
        userWorkspace.getSubscriptionCollection().add(createStudySubscription("Error", Status.ERROR, false));
        userWorkspace.getSubscriptionCollection().add(createStudySubscription("Deployed3Disabled",
                Status.DEPLOYED, true));
        userWorkspace.getSubscriptionCollection().add(createStudySubscription("ProcessingDisabled",
                Status.PROCESSING, true));
        userWorkspace.getSubscriptionCollection().add(createStudySubscription("ErrorDisabled",
                Status.ERROR, true));
        DisplayableUserWorkspace displayableWorkspace = SessionHelper.getInstance().getDisplayableUserWorkspace();
        assertEquals(2, displayableWorkspace.getOrderedSubscriptionList().size());
        assertEquals("Deployed1",
                displayableWorkspace.getOrderedSubscriptionList().get(0).getStudy().getShortTitleText());
        assertEquals("Deployed2",
                displayableWorkspace.getOrderedSubscriptionList().get(1).getStudy().getShortTitleText());
    }

    private StudySubscription createStudySubscription(String studyName, Status status, boolean isDisabled) {
        StudySubscription studySubscription = new StudySubscription();
        Study study = new Study();
        study.setShortTitleText(studyName);
        study.setEnabled(!isDisabled);
        studySubscription.setStudy(study);
        studySubscription.setId(Long.valueOf(id++));
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
