/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.study.management;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.application.study.StudyManagementServiceStub;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.opensymphony.xwork2.Action;

public class LoadClinicalSourceActionTest {

    private LoadClinicalSourceAction action;
    private StudyManagementServiceStub studyManagementServiceStub;

    @Before
    public void setUp() {
        ApplicationContext context = new ClassPathXmlApplicationContext("study-management-action-test-config.xml", LoadClinicalSourceActionTest.class); 
        action = (LoadClinicalSourceAction) context.getBean("loadClinicalSourceAction");
        studyManagementServiceStub = (StudyManagementServiceStub) context.getBean("studyManagementService");
        studyManagementServiceStub.clear();
    }

    @Test
    public void testExecute() {
        action.getStudyConfiguration().getStudy().setShortTitleText("Invalid");
        assertEquals(Action.ERROR, action.execute());
        action.getStudyConfiguration().getStudy().setShortTitleText("");
        assertEquals(Action.SUCCESS, action.execute());
        assertTrue(studyManagementServiceStub.loadClinicalAnnotationCalled);
    }

    @Test
    public void testReLoad() {
        action.getStudyConfiguration().getStudy().setShortTitleText("Invalid");
        assertEquals(Action.ERROR, action.reLoad());
        action.getStudyConfiguration().getStudy().setShortTitleText("");
        assertEquals(Action.SUCCESS, action.reLoad());
        assertTrue(studyManagementServiceStub.reLoadClinicalAnnotationCalled);
    }

    @Test
    public void testDelete() {
        action.getStudyConfiguration().getStudy().setShortTitleText("Invalid");
        assertEquals(Action.ERROR, action.delete());
        action.getStudyConfiguration().getStudy().setShortTitleText("");
        assertEquals(Action.SUCCESS, action.delete());
        assertTrue(studyManagementServiceStub.deleteCalled);
    }

}
