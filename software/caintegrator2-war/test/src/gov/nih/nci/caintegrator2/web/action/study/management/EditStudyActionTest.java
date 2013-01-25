/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.study.management;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import gov.nih.nci.caintegrator2.web.action.AbstractSessionBasedTest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.opensymphony.xwork2.Action;

public class EditStudyActionTest extends AbstractSessionBasedTest {

    private EditStudyAction editStudyAction;

    @Before
    public void setUp() {
        super.setUp();
        ApplicationContext context = new ClassPathXmlApplicationContext("study-management-action-test-config.xml", EditStudyActionTest.class); 
        editStudyAction = (EditStudyAction) context.getBean("editStudyAction");
    }

    @Test
    public void testExecute() {
        assertEquals(Action.SUCCESS, editStudyAction.execute());
    }

    @Test
    public void testManageStudy() {
        assertEquals(Action.SUCCESS, editStudyAction.manageStudies());
    }

    @Test
    public void testDelete() {
        assertEquals(Action.SUCCESS, editStudyAction.deleteStudy());
    }

    @Test
    public void testGetModel() {
        assertNotNull(editStudyAction.getModel());
    }
}
