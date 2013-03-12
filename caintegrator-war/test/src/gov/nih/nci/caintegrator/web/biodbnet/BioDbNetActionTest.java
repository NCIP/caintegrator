/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.biodbnet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator.external.biodbnet.BioDbNetSearchImpl;
import gov.nih.nci.caintegrator.external.biodbnet.enums.SearchType;
import gov.nih.nci.caintegrator.web.action.AbstractSessionBasedTest;
import gov.nih.nci.caintegrator.web.action.analysis.biodbnet.BioDbNetSearchAction;

import org.junit.Test;

import com.opensymphony.xwork2.Action;

/**
 * Tests for bioDbNet search functionality.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
public class BioDbNetActionTest extends AbstractSessionBasedTest {
    private BioDbNetSearchAction action;

    /**
     * {@inheritDoc}
     */
    @Override
    public void setUp() throws Exception {
        super.setUp();
        BioDbNetSearchImpl bioDbNetSvc = new BioDbNetSearchImpl();
        bioDbNetSvc.setBioDbNetRemoteService(bioDbNetRemoteService);

        action = new BioDbNetSearchAction();
        action.setBioDbNetService(bioDbNetSvc);
        action.setWorkspaceService(workspaceService);
        action.prepare();
    }

    /**
     * Tests input method.
     */
    @Test
    public void input() {
        assertEquals(Action.SUCCESS, action.input());
    }

    /**
     * Tests searching by gene id.
     */
    @Test
    public void searchGeneId() {
        action.prepare();
        action.getSearchParameters().setSearchType(SearchType.GENE_ID);
        action.getSearchParameters().setInputValues("1,2,3,4");
        assertEquals(Action.SUCCESS, action.search());
        assertTrue(action.getGeneResults().size() > 0);
    }

    /**
     * Tests searching by gene alias.
     */
    @Test
    public void searchGeneAlias() {
        action.prepare();
        action.getSearchParameters().setSearchType(SearchType.GENE_ALIAS);
        action.getSearchParameters().setInputValues("brcc1");
        assertEquals(Action.SUCCESS, action.search());
        assertTrue(action.getGeneResults().size() > 0);
    }

    /**
     * Tests searching by gene alias.
     */
    @Test
    public void searchGeneSymbol() {
        action.prepare();
        action.getSearchParameters().setSearchType(SearchType.GENE_SYMBOL);
        action.getSearchParameters().setInputValues("BRCA1,BRCA2");
        assertEquals(Action.SUCCESS, action.search());
        assertTrue(action.getGeneResults().size() > 0);
    }

    /**
     * Tests searching by gene pathways.
     */
    @Test
    public void searchPathways() {
        action.prepare();
        action.getSearchParameters().setSearchType(SearchType.PATHWAY);
        action.getSearchParameters().setInputValues("h_bard1Pathway");
        assertEquals(Action.SUCCESS, action.search());
        assertTrue(action.getGeneResults().size() > 0);
    }

    /**
     * Tests action validation.
     */
    @Test
    public void validate() {
        action.validate();
        assertTrue(action.hasActionErrors());
        assertEquals(1, action.getActionErrors().size());
        assertEquals("struts.messages.error.must.enter.keywords", action.getActionErrors().iterator().next());

        action.clearActionErrors();
        action.prepare();
        action.getSearchParameters().setInputValues("input");
        action.validate();
        assertFalse(action.hasActionErrors());
    }
}
