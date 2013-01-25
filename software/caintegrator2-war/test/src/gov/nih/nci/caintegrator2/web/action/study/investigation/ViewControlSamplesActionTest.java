/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.study.investigation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.application.workspace.WorkspaceServiceStub;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.web.SessionHelper;
import gov.nih.nci.caintegrator2.web.action.AbstractSessionBasedTest;

import java.util.HashMap;

import org.junit.Test;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;

/**
 * 
 */
public class ViewControlSamplesActionTest extends AbstractSessionBasedTest {

    private ViewControlSamplesAction action = new ViewControlSamplesAction();

    @Test
    public void testPrepare() {
        super.setUp();
        WorkspaceServiceStub workspaceServiceStub = new WorkspaceServiceStub();
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        action.setWorkspaceService(workspaceServiceStub);
        action.prepare();
        assertTrue(SessionHelper.getInstance().getInvalidDataBeingAccessed());
        
        
        SessionHelper.getInstance().setInvalidDataBeingAccessed(false);
        GenomicDataSourceConfiguration genomicSource = new GenomicDataSourceConfiguration();
        genomicSource.setId(1l);
        genomicSource.setStudyConfiguration(studyConfiguration);
        action.setGenomicSource(genomicSource);
        action.prepare();
        assertTrue(SessionHelper.getInstance().getInvalidDataBeingAccessed());
        
        SessionHelper.getInstance().setInvalidDataBeingAccessed(false);
        StudySubscription subscription = new StudySubscription();
        subscription.setId(1L);
        subscription.setStudy(studyConfiguration.getStudy());
        subscription.getStudy().setStudyConfiguration(studyConfiguration);
        SessionHelper.getInstance().getDisplayableUserWorkspace().
            setCurrentStudySubscription(subscription);
        ActionContext.getContext().setSession(new HashMap<String, Object>());
        ActionContext.getContext().getValueStack().setValue("studySubscription", subscription);
        workspaceServiceStub.setSubscription(subscription);
        action.prepare();
        assertFalse(SessionHelper.getInstance().getInvalidDataBeingAccessed());
        
    }


    @Test
    public void testExecute() {
        assertEquals(Action.SUCCESS, action.execute());
    }

}
