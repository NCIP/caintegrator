/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.analysis.copynumber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.application.analysis.AnalysisServiceStub;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.application.workspace.WorkspaceServiceStub;
import gov.nih.nci.caintegrator2.domain.analysis.GisticAnalysis;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.web.SessionHelper;
import gov.nih.nci.caintegrator2.web.action.AbstractSessionBasedTest;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;

/**
 * 
 */
public class EditGisticAnalysisActionTest extends AbstractSessionBasedTest {

    EditGisticAnalysisAction action = new EditGisticAnalysisAction();
    StudySubscription subscription = new StudySubscription();
    WorkspaceServiceStub workspaceService = new WorkspaceServiceStub();
    AnalysisServiceStub analysisService = new AnalysisServiceStub();
    
    @Before
    public void setUp() {
        super.setUp();
        subscription.setId(1L);
        subscription.setStudy(new Study());
        subscription.getStudy().setStudyConfiguration(new StudyConfiguration());
        SessionHelper.getInstance().getDisplayableUserWorkspace().
            setCurrentStudySubscription(subscription);
        ActionContext.getContext().setSession(new HashMap<String, Object>());
        ActionContext.getContext().getValueStack().setValue("studySubscription", subscription);
        workspaceService.setSubscription(subscription);
        action.setWorkspaceService(workspaceService);
        action.setAnalysisService(analysisService);
        analysisService.clear();
        workspaceService.clear();
    }
    
    @Test
    public void testPrepare() {
        action.setGisticAnalysis(new GisticAnalysis());
        action.getGisticAnalysis().setId(1l);
        action.prepare();
        assertEquals(0, action.getAmplifiedGenes().size());
        assertEquals(0, action.getDeletedGenes().size());
        assertTrue(workspaceService.getRefreshedEntityCalled);
        action.getGisticAnalysis().setId(null);
        action.prepare();
    }
    
    @Test
    public void testExecute() {
        assertEquals(Action.SUCCESS, action.execute());
        action.setSelectedAction(EditGisticAnalysisAction.EDIT_ACTION);
        assertEquals(Action.SUCCESS, action.execute());
        action.setSelectedAction(EditGisticAnalysisAction.DELETE_ACTION);
        assertEquals(EditGisticAnalysisAction.HOME_PAGE, action.execute());
        assertTrue(analysisService.deleteGisticAnalysisCalled);
        action.setSelectedAction(EditGisticAnalysisAction.CANCEL_ACTION);
        assertEquals(EditGisticAnalysisAction.HOME_PAGE, action.execute());
        action.setSelectedAction(EditGisticAnalysisAction.SAVE_ACTION);
        assertEquals(EditGisticAnalysisAction.SUCCESS, action.execute());
        assertTrue(workspaceService.saveUserWorspaceCalled);
        assertTrue(action.hasActionMessages());
    }
    
    

}
