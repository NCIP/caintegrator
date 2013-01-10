/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.web.action.abstractlist;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.domain.application.GeneList;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.genomic.Gene;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.web.SessionHelper;
import gov.nih.nci.caintegrator2.web.action.AbstractSessionBasedTest;

import java.util.HashMap;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import com.opensymphony.xwork2.ActionContext;

public class EditGeneListActionTest extends AbstractSessionBasedTest {

    private EditGeneListAction action = new MyEditGeneListAction();
    private StudySubscription subscription = new StudySubscription();

    class MyEditGeneListAction extends EditGeneListAction {

        private static final long serialVersionUID = 1L;

        /**
         * @return is the current user a study manager
         */
        @Override
        public boolean isStudyManager() {
            return true;
        }
    }

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        subscription.setId(1L);
        subscription.setStudy(new Study());
        subscription.getStudy().setStudyConfiguration(new StudyConfiguration());
        SessionHelper.getInstance().getDisplayableUserWorkspace().
            setCurrentStudySubscription(subscription);
        ActionContext.getContext().setSession(new HashMap<String, Object>());
        ActionContext.getContext().getValueStack().setValue("studySubscription", subscription);
        action.setWorkspaceService(workspaceService);

        setStudySubscription(subscription);
        GeneList geneList = new GeneList();
        geneList.setName("List1");
        subscription.getListCollection().add(geneList);
        Gene gene = new Gene();
        gene.setSymbol("SYMBOL1");
        geneList.getGeneCollection().add(gene);
        gene = new Gene();
        gene.setSymbol("SYMBOL2");
        geneList.getGeneCollection().add(gene);
        geneList = new GeneList();
        geneList.setName("List2");
        subscription.getListCollection().add(geneList);

        StudyConfiguration studyConfiguration = subscription.getStudy().getStudyConfiguration();
        studyConfiguration.setListCollection(subscription.getListCollection());
        action.prepare();
    }

    @Test
    public void validate() {
        action.setVisibleToOther(false);
        action.setSelectedAction(EditGeneListAction.EDIT_ACTION);
        action.validate();
        assertTrue(action.hasActionErrors());

        action.setSelectedAction(EditGeneListAction.SAVE_ACTION);
        action.setListName(RandomStringUtils.randomAlphabetic(150));
        action.validate();
        assertFalse(action.hasActionErrors());
        assertTrue(action.hasFieldErrors());
        assertEquals(1, action.getFieldErrors().size());
        action.clearErrorsAndMessages();

        action.setListName("List1");
        action.validate();
        assertFalse(action.hasActionErrors());
        assertTrue(action.hasFieldErrors());
    }

    @Test
    public void testAll() {
        action.setVisibleToOther(false);
        action.setSelectedAction(EditGeneListAction.EDIT_ACTION);
        action.setListName("List1");
        action.validate();

        assertEquals(15, action.getGeneSymbolListing().length());
        assertEquals(EditGeneListAction.SUCCESS, action.execute());

        action.setSelectedAction(EditGeneListAction.SAVE_ACTION);
        action.setListOldName("Oldname");
        action.setListName("");
        action.validate();
        assertTrue(action.hasFieldErrors());
        action.setListName("List1");
        action.validate();
        assertFalse(action.hasActionErrors());
        action.clearErrorsAndMessages();
        assertEquals("success", action.execute());

        action.setListName("List1");
        action.validate();
        assertTrue(action.hasFieldErrors());

        action.setListName("Test");
        action.validate();
        assertFalse(action.hasFieldErrors());
        action.clearErrorsAndMessages();
        assertEquals("success", action.execute());

        action.setListName("List1");
        action.setVisibleToOther(true);
        action.execute();
        assertFalse(action.hasFieldErrors());

        action.setSelectedAction(EditGeneListAction.DELETE_ACTION);
        action.setListName("Test");
        action.execute();
        assertFalse(action.hasFieldErrors());

        action.setListName("List1");
        action.execute();
        assertFalse(action.hasFieldErrors());

        action.setSelectedAction(EditGeneListAction.CANCEL_ACTION);
        assertEquals(EditGeneListAction.HOME_PAGE, action.execute());
    }

    @Test
    public void testAllGlobal() {
        action.setVisibleToOther(true);
        action.setSelectedAction(EditGeneListAction.EDIT_GLOBAL_ACTION);
        action.validate();
        assertTrue(action.hasActionErrors());

        action.setListName("List1");
        action.validate();
        assertFalse(action.hasActionErrors());

        assertEquals(15, action.getGeneSymbolListing().length());

        assertEquals(EditGeneListAction.SUCCESS, action.execute());

        action.setSelectedAction(EditGeneListAction.SAVE_ACTION);
        action.setListOldName("Oldname");
        action.setListName("");
        action.validate();
        assertTrue(action.hasFieldErrors());
        action.setListName("List1");
        action.validate();
        assertFalse(action.hasActionErrors());
        action.clearErrorsAndMessages();
        assertEquals("success", action.execute());

        action.setListName("List1");
        action.validate();
        assertTrue(action.hasFieldErrors());

        action.setListName("Test");
        action.validate();
        assertFalse(action.hasFieldErrors());
        action.clearErrorsAndMessages();
        assertEquals("success", action.execute());

        action.setSelectedAction(EditGeneListAction.DELETE_ACTION);
        action.setListName("Test");
        action.execute();
        assertFalse(action.hasFieldErrors());

        action.setListName("List1");
        action.execute();
        assertFalse(action.hasFieldErrors());

        action.setListName("Test");
        action.setVisibleToOther(false);
        action.execute();
        assertFalse(action.hasFieldErrors());
    }
}
