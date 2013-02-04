/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.action.analysis.copynumber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import gov.nih.nci.caintegrator.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator.domain.analysis.GisticAnalysis;
import gov.nih.nci.caintegrator.domain.application.StudySubscription;
import gov.nih.nci.caintegrator.domain.application.UserWorkspace;
import gov.nih.nci.caintegrator.domain.translational.Study;
import gov.nih.nci.caintegrator.web.SessionHelper;
import gov.nih.nci.caintegrator.web.action.AbstractSessionBasedTest;
import gov.nih.nci.caintegrator.web.action.analysis.copynumber.EditGisticAnalysisAction;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;

/**
 *
 */
public class EditGisticAnalysisActionTest extends AbstractSessionBasedTest {

    private EditGisticAnalysisAction action = new EditGisticAnalysisAction();
    private StudySubscription subscription = new StudySubscription();

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        subscription.setId(1L);
        subscription.setStudy(new Study());
        subscription.getStudy().setStudyConfiguration(new StudyConfiguration());
        SessionHelper.getInstance().getDisplayableUserWorkspace().setCurrentStudySubscription(subscription);
        ActionContext.getContext().setSession(new HashMap<String, Object>());
        ActionContext.getContext().getValueStack().setValue("studySubscription", subscription);
        action.setWorkspaceService(workspaceService);
        action.setAnalysisService(analysisService);
        setStudySubscription(subscription);
    }

    @Test
    public void testPrepare() {
        action.setGisticAnalysis(new GisticAnalysis());
        action.getGisticAnalysis().setId(1l);
        action.prepare();
        assertEquals(0, action.getAmplifiedGenes().size());
        assertEquals(0, action.getDeletedGenes().size());
        verify(workspaceService, times(1)).getRefreshedEntity(any(AbstractCaIntegrator2Object.class));
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
        verify(analysisService, atLeastOnce()).deleteGisticAnalysis(any(GisticAnalysis.class));
        action.setSelectedAction(EditGisticAnalysisAction.CANCEL_ACTION);
        assertEquals(EditGisticAnalysisAction.HOME_PAGE, action.execute());
        action.setSelectedAction(EditGisticAnalysisAction.SAVE_ACTION);
        assertEquals(EditGisticAnalysisAction.SUCCESS, action.execute());
        verify(workspaceService, times(1)).saveUserWorkspace(any(UserWorkspace.class));
        assertTrue(action.hasActionMessages());
    }
}
