/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.study.management;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.TestDataFiles;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.web.action.AbstractSessionBasedTest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AddStudyLogoActionTest extends AbstractSessionBasedTest {

    private AddStudyLogoAction action;

    @Before
    public void setUp() {
        super.setUp();
        ApplicationContext context = new ClassPathXmlApplicationContext("study-management-action-test-config.xml", AddStudyLogoAction.class); 
        action = (AddStudyLogoAction) context.getBean("addStudyLogoAction");
    }

    @Test
    public void testExecute() {
        assertNull(action.getStudyLogoFile());
        action.setStudyLogoFile(TestDataFiles.NCRI_LOGO);
        action.setStudyLogoFileFileName(TestDataFiles.NCRI_LOGO_FILE_PATH);
        action.setStudyLogoFileContentType("jpg");
        action.setStudyConfiguration(new StudyConfiguration());
        assertEquals(AddStudyLogoAction.SUCCESS, action.execute());
    }

    @Test
    public void testValidate() {
        action.validate();
        assertTrue(action.hasFieldErrors());
        action.setStudyLogoFile(TestDataFiles.NCRI_LOGO);
        action.clearFieldErrors();
        action.validate();
        assertFalse(action.hasFieldErrors());
    }
}
