/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.abstractlist;

import static org.junit.Assert.assertEquals;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.application.workspace.WorkspaceServiceStub;
import gov.nih.nci.caintegrator2.domain.application.GeneList;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.genomic.Gene;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.web.SessionHelper;
import gov.nih.nci.caintegrator2.web.action.AbstractSessionBasedTest;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import com.opensymphony.xwork2.ActionContext;

public class SearchGeneListActionTest extends AbstractSessionBasedTest {

    SearchGeneListAction action = new SearchGeneListAction();
    StudySubscription subscription = new StudySubscription();
    WorkspaceServiceStub workspaceService = new WorkspaceServiceStub();

    @Before
    public void setUp() {
        super.setUp();
        SessionHelper.getInstance().getDisplayableUserWorkspace().
            setCurrentStudySubscription(subscription);
        ActionContext.getContext().setSession(new HashMap<String, Object>());
        ActionContext.getContext().getValueStack().setValue("studySubscription", subscription);
        workspaceService.setSubscription(subscription);
        subscription.setStudy(new Study());
        subscription.getStudy().setStudyConfiguration(new StudyConfiguration());
        action.setWorkspaceService(workspaceService);
    }
    
    @Test
    public void testAll() {
        // Test Execute
        assertEquals(ManageGeneListAction.SUCCESS, action.execute());
        assertEquals(null, action.getGeneListName());
        
        GeneList geneList = new GeneList();
        geneList.setName("List1");
        subscription.getListCollection().add(geneList);
        assertEquals(ManageGeneListAction.SUCCESS, action.execute());
        assertEquals("List1", action.getGeneListName());
        assertEquals(0, action.getGenes().size());
        
        geneList.getGeneCollection().add(new Gene());
        assertEquals(ManageGeneListAction.SUCCESS, action.execute());
        assertEquals("List1", action.getGeneListName());
        assertEquals(1, action.getGenes().size());
    }
}
