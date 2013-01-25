/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.study.management;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.application.study.LogEntry;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.application.workspace.WorkspaceServiceStub;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.web.SessionHelper;
import gov.nih.nci.caintegrator2.web.action.AbstractSessionBasedTest;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.opensymphony.xwork2.Action;

/**
 * 
 */
public class ViewStudyLogActionTest extends AbstractSessionBasedTest {
    
    private ViewStudyLogAction action = new ViewStudyLogAction();
    private StudySubscription subscription = new StudySubscription();
    private WorkspaceServiceStub workspaceService;

    @Before
    public void setUp() {
        super.setUp();

        subscription.setId(1L);
        subscription.setStudy(new Study());
        subscription.getStudy().setStudyConfiguration(new StudyConfiguration());
        SessionHelper.getInstance().getDisplayableUserWorkspace().
            setCurrentStudySubscription(subscription);
        workspaceService = new WorkspaceServiceStub();
        action = new ViewStudyLogAction();
        
        action.setWorkspaceService(workspaceService);
    }


    @Test
    public void testPrepare() {
        action.prepare();
        LogEntry logEntry = new LogEntry();
        logEntry.setSystemLogMessage("message");
        logEntry.setLogDate(new Date());
        action.getCurrentStudy().getStudyConfiguration().getLogEntries().add(logEntry);
        action.prepare();
        assertFalse(workspaceService.getRefreshedEntityCalled);
        assertTrue(action.getDisplayableLogEntries().isEmpty());
        
        
        LogEntry logEntryNoDescription = new LogEntry();
        logEntryNoDescription.setLogDate(new Date());
        action.getCurrentStudy().getStudyConfiguration().getLogEntries().add(logEntryNoDescription);
        logEntry.setDescription("desc");
        action.prepare();
        assertTrue(workspaceService.getRefreshedEntityCalled);
        assertTrue(action.getDisplayableLogEntries().size() == 1);
        assertEquals(action.getDisplayableLogEntries().get(0).getDescription(), (logEntry.getDescription()));
    }


    @Test
    public void testExecute() {
        setUserAnonymous();
        action.prepare();
        assertEquals(Action.ERROR, action.execute());
        setUsername("user");
        action.prepare();
        assertEquals(Action.SUCCESS, action.execute());
    }
    
    @Test
    public void testAcceptableParameterName() {
        assertTrue(action.acceptableParameterName(null));
        assertTrue(action.acceptableParameterName("ABC"));
        assertFalse(action.acceptableParameterName("123"));
        assertFalse(action.acceptableParameterName("d-123-e"));
    }
}
