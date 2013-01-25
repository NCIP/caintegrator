/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.application.study.StudyManagementServiceStub;
import gov.nih.nci.caintegrator2.web.action.study.management.EditStudyActionTest;

import java.io.IOException;

import javax.servlet.ServletException;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;


public class LogoRenderingServletTest {

    @Test
    public void testHandleRequest() {
        ApplicationContext context = new ClassPathXmlApplicationContext("study-management-action-test-config.xml", EditStudyActionTest.class); 
        LogoRenderingServlet servlet = (LogoRenderingServlet) context.getBean("logoRenderingServlet");
        StudyManagementServiceStub serviceStub = (StudyManagementServiceStub) context.getBean("studyManagementService");
        serviceStub.clear();
        
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        
        // Catching these exceptions, because the actual logo files don't exist right now on the filesystem.
        try {
            servlet.handleRequest(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertFalse(serviceStub.retrieveStudyLogoCalled);
        
        request.addParameter("studyId", "1");
        request.addParameter("studyName", "value");
        try {
            servlet.handleRequest(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertTrue(serviceStub.retrieveStudyLogoCalled);
    }

}
