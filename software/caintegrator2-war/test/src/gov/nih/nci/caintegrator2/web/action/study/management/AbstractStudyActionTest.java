/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.study.management;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.application.study.StudyManagementServiceStub;
import gov.nih.nci.caintegrator2.web.SessionHelper;
import gov.nih.nci.caintegrator2.web.action.AbstractSessionBasedTest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AbstractStudyActionTest extends AbstractSessionBasedTest {

    private AbstractStudyAction abstractStudyAction;
    private StudyManagementServiceStub studyManagementServiceStub;

    @Before
    public void setUp() {
        super.setUp();
        ApplicationContext context = new ClassPathXmlApplicationContext("study-management-action-test-config.xml", EditStudyActionTest.class); 
        abstractStudyAction = (AbstractStudyAction) context.getBean("saveStudyAction");
        studyManagementServiceStub = (StudyManagementServiceStub) context.getBean("studyManagementService");
        studyManagementServiceStub.clear();
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
        
    }

}
