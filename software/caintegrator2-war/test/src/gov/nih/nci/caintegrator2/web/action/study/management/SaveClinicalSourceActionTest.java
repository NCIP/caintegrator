/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
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

public class SaveClinicalSourceActionTest {

    private SaveClinicalSourceAction action;
    private StudyManagementServiceStub studyManagementServiceStub;

    @Before
    public void setUp() {
        ApplicationContext context = new ClassPathXmlApplicationContext("study-management-action-test-config.xml", SaveClinicalSourceActionTest.class); 
        action = (SaveClinicalSourceAction) context.getBean("saveClinicalSourceAction");
        studyManagementServiceStub = (StudyManagementServiceStub) context.getBean("studyManagementService");
        studyManagementServiceStub.clear();
    }

    @Test
    public void testExecute() {
        assertEquals(Action.SUCCESS, action.execute());
        assertTrue(studyManagementServiceStub.saveCalled);
    }

}
