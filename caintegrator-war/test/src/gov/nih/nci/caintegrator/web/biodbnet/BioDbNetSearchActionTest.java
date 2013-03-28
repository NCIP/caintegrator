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
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.refEq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import gov.nih.nci.caintegrator.external.biodbnet.BioDbNetSearchImpl;
import gov.nih.nci.caintegrator.external.biodbnet.domain.Db2DbParams;
import gov.nih.nci.caintegrator.external.biodbnet.domain.DbWalkParams;
import gov.nih.nci.caintegrator.external.biodbnet.enums.SearchType;
import gov.nih.nci.caintegrator.web.action.AbstractSessionBasedTest;
import gov.nih.nci.caintegrator.web.action.analysis.biodbnet.BioDbNetSearchAction;

import org.junit.Before;
import org.junit.Test;

import com.opensymphony.xwork2.Action;

/**
 * Tests for bioDbNet search functionality.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
public class BioDbNetSearchActionTest extends AbstractSessionBasedTest {
    private BioDbNetSearchAction action;
    private final String[] ignoredParams = {"taxonId", "outputs", "input" };
    private static final String GENE_ALIAS = "brcc1";
    private static final String PATHWAY_NAME = "h_bard1Pathway";

    /**
     * {@inheritDoc}
     */
    @Before
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
     * Tests expected behavior when an exception occurs during the search process.
     */
    @Test
    public void searchWithException() {
        doThrow(Exception.class).when(bioDbNetRemoteService).db2db(any(Db2DbParams.class));
        action.getSearchParameters().setSearchType(SearchType.PATHWAY);
        action.getSearchParameters().setInputValues(PATHWAY_NAME);

        assertEquals(Action.INPUT, action.search());
        assertTrue(action.hasActionErrors());
        assertEquals(1, action.getActionErrors().size());
        assertEquals("bioDbNet.noResultsFound", action.getActionErrors().iterator().next());
    }

    /**
     * Tests expected behavior when not results are found.
     */
    @Test
    public void searchNoResults() {
        action.getSearchParameters().setSearchType(null);

        assertEquals(Action.INPUT, action.search());
        assertTrue(action.getGeneResults().isEmpty());
        assertTrue(action.hasActionErrors());
        assertEquals(1, action.getActionErrors().size());
        assertEquals("bioDbNet.noResultsFound", action.getActionErrors().iterator().next());
    }

    /**
     * Tests searching by gene id with case sensitivity enabled.
     */
    @Test
    public void searchGeneId() {
        action.getSearchParameters().setSearchType(SearchType.GENE_ID);
        action.getSearchParameters().setInputValues("1,2,3,4");
        action.getSearchParameters().setCaseSensitiveSearch(true);

        assertEquals(Action.SUCCESS, action.search());
        assertTrue(action.getGeneResults().size() > 0);

        Db2DbParams expectedInput = new Db2DbParams();
        expectedInput.setInputValues("1,2,3,4");
        verify(bioDbNetRemoteService).db2db(refEq(expectedInput, ignoredParams));
    }

    /**
     * Tests searching for gene id with case sensitivity disabled.
     */
    @Test
    public void searchGeneIdCaseInsensitive() {
        action.getSearchParameters().setSearchType(SearchType.GENE_ID);
        action.getSearchParameters().setInputValues("1,2,3,4");
        action.getSearchParameters().setCaseSensitiveSearch(false);

        assertEquals(Action.SUCCESS, action.search());
        assertTrue(action.getGeneResults().size() > 0);

        Db2DbParams expectedInput = new Db2DbParams();
        expectedInput.setInputValues("1,2,3,4");
        verify(bioDbNetRemoteService).db2db(refEq(expectedInput, ignoredParams));
    }

    /**
     * Tests searching by gene alias with case sensitivity enabled.
     */
    @Test
    public void searchGeneAlias() {
        action.getSearchParameters().setSearchType(SearchType.GENE_ALIAS);
        action.getSearchParameters().setInputValues(GENE_ALIAS);
        action.getSearchParameters().setCaseSensitiveSearch(true);

        assertEquals(Action.SUCCESS, action.search());
        assertTrue(action.getGeneResults().size() > 0);

        DbWalkParams expectedInput = new DbWalkParams();
        expectedInput.setInputValues(GENE_ALIAS);
        verify(bioDbNetRemoteService).dbWalk(refEq(expectedInput, "dbPath", "taxonId"));
    }

    /**
     * Tests searching by gene alias with case sensitivity disabled.
     */
    @Test
    public void searchGeneAliasCaseInsensitive() {
        action.getSearchParameters().setSearchType(SearchType.GENE_ALIAS);
        action.getSearchParameters().setInputValues(GENE_ALIAS);
        action.getSearchParameters().setCaseSensitiveSearch(false);

        assertEquals(Action.SUCCESS, action.search());
        assertTrue(action.getGeneResults().size() > 0);

        DbWalkParams expectedInput = new DbWalkParams();
        expectedInput.setInputValues("BRCC1,Brcc1,brcc1");
        verify(bioDbNetRemoteService).dbWalk(refEq(expectedInput, "dbPath", "taxonId"));
    }

    /**
     * Tests searching by gene alias with case sensitivity enabled.
     */
    @Test
    public void searchGeneSymbol() {
        action.getSearchParameters().setSearchType(SearchType.GENE_SYMBOL);
        action.getSearchParameters().setInputValues("BRCA1,BRCA2");
        action.getSearchParameters().setCaseSensitiveSearch(true);

        assertEquals(Action.SUCCESS, action.search());
        assertTrue(action.getGeneResults().size() > 0);

        Db2DbParams expectedInput = new Db2DbParams();
        expectedInput.setInputValues("BRCA1,BRCA2");
        verify(bioDbNetRemoteService).db2db(refEq(expectedInput, ignoredParams));
    }

    /**
     * Tests searching by gene alias with case sensitivity enabled.
     */
    @Test
    public void searchGeneSymbolCaseInsensitive() {
        action.getSearchParameters().setSearchType(SearchType.GENE_SYMBOL);
        action.getSearchParameters().setInputValues("BRCA1,BRCA2");
        action.getSearchParameters().setCaseSensitiveSearch(false);

        assertEquals(Action.SUCCESS, action.search());
        assertTrue(action.getGeneResults().size() > 0);

        Db2DbParams expectedInput = new Db2DbParams();
        expectedInput.setInputValues("BRCA1,BRCA2,brca1,brca2");
        verify(bioDbNetRemoteService).db2db(refEq(expectedInput, ignoredParams));
    }

    /**
     * Tests searching by gene pathways with case sensitivity enabled.
     */
    @Test
    public void searchPathways() {
        action.getSearchParameters().setSearchType(SearchType.PATHWAY);
        action.getSearchParameters().setInputValues(PATHWAY_NAME);
        action.getSearchParameters().setCaseSensitiveSearch(true);

        assertEquals(Action.SUCCESS, action.search());
        assertTrue(action.getGeneResults().size() > 0);

        Db2DbParams expectedInput = new Db2DbParams();
        expectedInput.setInputValues(PATHWAY_NAME);
        verify(bioDbNetRemoteService).db2db(refEq(expectedInput, ignoredParams));
    }

    /**
     * Tests searching by gene pathways with case sensitivity disabled.
     */
    @Test
    public void searchPathwaysCaseInsensitive() {
        action.getSearchParameters().setSearchType(SearchType.PATHWAY);
        action.getSearchParameters().setInputValues(PATHWAY_NAME);
        action.getSearchParameters().setCaseSensitiveSearch(false);

        assertEquals(Action.SUCCESS, action.search());
        assertTrue(action.getGeneResults().size() > 0);

        Db2DbParams expectedInput = new Db2DbParams();
        expectedInput.setInputValues("H_BARD1PATHWAY,H_bard1Pathway,h_bard1Pathway,h_bard1pathway");
        verify(bioDbNetRemoteService).db2db(refEq(expectedInput, ignoredParams));
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
