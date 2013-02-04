/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.study.management;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.application.study.deployment.DeploymentServiceStub;
import gov.nih.nci.caintegrator2.web.action.AbstractSessionBasedTest;
import gov.nih.nci.caintegrator2.web.ajax.IStudyDeploymentAjaxUpdater;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.opensymphony.xwork2.Action;

public class DeployStudyActionTest extends AbstractSessionBasedTest {

    private DeployStudyAction action;
    private DeploymentServiceStub deploymentServiceStub;
    private DeployStudyAjaxUpdaterStub ajaxUpdaterStub;

    @Before
    public void setUp() {
        super.setUp();
        ApplicationContext context = new ClassPathXmlApplicationContext("study-management-action-test-config.xml", EditStudyActionTest.class); 
        action = (DeployStudyAction) context.getBean("deployStudyAction");
        deploymentServiceStub = (DeploymentServiceStub) context.getBean("deploymentService");
        ajaxUpdaterStub = new DeployStudyAjaxUpdaterStub();
        action.setAjaxUpdater(ajaxUpdaterStub);
    }

    @Test
    public void testExecute() {
        assertEquals(Action.SUCCESS, action.execute());
        assertTrue(ajaxUpdaterStub.runJobCalled);
        assertTrue(deploymentServiceStub.prepareForDeploymentCalled);
    }
    
    private static class DeployStudyAjaxUpdaterStub implements IStudyDeploymentAjaxUpdater {

        public boolean runJobCalled = false;
        
        public void initializeJsp() {
        }

        public void runJob(StudyConfiguration configuration) {
            runJobCalled = true;
        }
        
    }

}
