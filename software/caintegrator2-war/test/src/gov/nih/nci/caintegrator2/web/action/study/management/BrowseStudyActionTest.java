/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.study.management;

import static org.junit.Assert.assertEquals;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.web.SessionHelper;
import gov.nih.nci.caintegrator2.web.action.AbstractSessionBasedTest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.opensymphony.xwork2.Action;

public class BrowseStudyActionTest extends AbstractSessionBasedTest {
    
    private BrowseStudyAction action;
    
    @Before
    public void setUp() {
        super.setUp();
        ApplicationContext context = new ClassPathXmlApplicationContext("study-management-action-test-config.xml", BrowseStudyActionTest.class); 
        action = (BrowseStudyAction) context.getBean("browseStudyAction");
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
