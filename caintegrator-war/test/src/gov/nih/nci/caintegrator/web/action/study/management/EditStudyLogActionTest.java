/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.action.study.management;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator.application.study.LogEntry;
import gov.nih.nci.caintegrator.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator.application.study.StudyManagementServiceStub;
import gov.nih.nci.caintegrator.web.action.AbstractSessionBasedTest;
import gov.nih.nci.caintegrator.web.action.study.management.EditStudyLogAction;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.opensymphony.xwork2.Action;

/**
 *
 */
public class EditStudyLogActionTest extends AbstractSessionBasedTest {

    private EditStudyLogAction action = new EditStudyLogAction();
    private StudyManagementServiceStub studyManagementService;
    private LogEntry logEntry1;
    private LogEntry logEntry2;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();

        action.setStudyConfiguration(new StudyConfiguration());
        action = new EditStudyLogAction();
        studyManagementService = new StudyManagementServiceStub();
        action.setStudyManagementService(studyManagementService);
        action.setWorkspaceService(workspaceService);
        logEntry1 = new LogEntry();
        logEntry1.setTrimSystemLogMessage("message");
        logEntry1.setLogDate(new Date());
        logEntry1.setTrimDescription("desc");
        try {
            Thread.sleep(20l);
        } catch (InterruptedException e) {
        }
        logEntry2 = new LogEntry();
        logEntry2.setSystemLogMessage("message2");
        logEntry2.setLogDate(new Date());
        logEntry2.setDescription("desc");
        action.getStudyConfiguration().getLogEntries().add(logEntry1);
        action.getStudyConfiguration().getLogEntries().add(logEntry2);
    }

    @Test
    public void testPrepare() throws InterruptedException {

        action.prepare();
        // Verify the most recent ones are sorted first.
        assertEquals(logEntry2, action.getDisplayableLogEntries().get(0).getLogEntry());
        assertEquals(logEntry1, action.getDisplayableLogEntries().get(1).getLogEntry());
        assertTrue(studyManagementService.getRefreshedStudyEntityCalled);
    }

    @Test
    public void testSave() {
        action.prepare();
        // logEntry2 is first
        action.getDisplayableLogEntries().get(0).setDescription("new");
        action.getDisplayableLogEntries().get(0).setUpdateDescription(true);

        // logEntry1 will have a new description, but the checkbox will be false.
        action.getDisplayableLogEntries().get(1).setDescription("new");
        action.getDisplayableLogEntries().get(1).setUpdateDescription(false);

        assertEquals(Action.SUCCESS, action.save());
        assertEquals("new", logEntry2.getDescription());
        assertEquals("desc", logEntry1.getDescription());
        assertTrue(studyManagementService.saveCalled);
    }

    @Test
    public void testAcceptableParameterName() {
        assertTrue(action.acceptableParameterName(null));
        assertTrue(action.acceptableParameterName("ABC"));
        assertFalse(action.acceptableParameterName("123"));
        assertFalse(action.acceptableParameterName("d-123-e"));
    }

}
