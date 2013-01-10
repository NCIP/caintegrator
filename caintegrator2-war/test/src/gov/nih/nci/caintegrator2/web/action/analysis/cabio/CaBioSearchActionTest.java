/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.web.action.analysis.cabio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.cabio.CaBioDisplayableGene;
import gov.nih.nci.caintegrator2.external.cabio.CaBioDisplayablePathway;
import gov.nih.nci.caintegrator2.external.cabio.CaBioFacade;
import gov.nih.nci.caintegrator2.external.cabio.CaBioSearchParameters;
import gov.nih.nci.caintegrator2.external.cabio.CaBioSearchTypeEnum;
import gov.nih.nci.caintegrator2.web.action.AbstractSessionBasedTest;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;


public class CaBioSearchActionTest extends AbstractSessionBasedTest {
    private CaBioSearchAction action;
    private CaBioFacade caBioFacade;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        caBioFacade = mock(CaBioFacade.class);
        when(caBioFacade.retrieveAllTaxons()).thenReturn(new ArrayList<String>());
        when(caBioFacade.retrievePathways(any(CaBioSearchParameters.class))).thenReturn(new ArrayList<CaBioDisplayablePathway>());
        when(caBioFacade.retrieveGenesFromPathways(any(CaBioSearchParameters.class))).thenReturn(Arrays.asList(new CaBioDisplayableGene()));
        action = new CaBioSearchAction();
        action.setWorkspaceService(workspaceService);
        action.setCaBioFacade(caBioFacade);
    }

    @Test
    public void testInput() {
        assertEquals(CaBioSearchAction.SUCCESS, action.input());
    }

    @Test
    public void geneSearchRunNotSelected() throws Exception {
        // If it's not "runSearchSelected" set to true it returns success.
        assertEquals(CaBioSearchAction.SUCCESS, action.searchCaBio());
        verify(caBioFacade, never()).retrieveGenes(any(CaBioSearchParameters.class));
    }

    @Test
    public void geneSearchRunSelected() throws Exception {
        action.setRunSearchSelected(true);
        assertEquals(CaBioSearchAction.INPUT, action.searchCaBio());
    }

    @Test
    public void geneSearchUnderThreeCharacters() throws Exception {
        // If keywords is under 3 characters
        action.setRunSearchSelected(true);
        action.getSearchParams().setKeywords("aa");
        assertEquals(CaBioSearchAction.INPUT, action.searchCaBio());
        assertFalse(action.isRunSearchSelected());
        verify(caBioFacade, never()).retrieveGenes(any(CaBioSearchParameters.class));
    }

    @Test
    public void geneSearchNoResults() throws Exception {
        action.setRunSearchSelected(true);
        action.getSearchParams().setKeywords("keywords");
        assertEquals(CaBioSearchAction.INPUT, action.searchCaBio());
        assertFalse(action.isRunSearchSelected());
        verify(caBioFacade, times(1)).retrieveGenes(any(CaBioSearchParameters.class));
    }

    @Test
    public void geneSearchResultsFound() throws Exception {
        // If keywords exist and something is returned.
        when(caBioFacade.retrieveGenes(any(CaBioSearchParameters.class))).thenReturn(Arrays.asList(new CaBioDisplayableGene()));
        action.setRunSearchSelected(true);
        action.getSearchParams().setKeywords("keywords");
        assertEquals(CaBioSearchAction.SUCCESS, action.searchCaBio());
        assertFalse(action.isRunSearchSelected());
        verify(caBioFacade, times(1)).retrieveGenes(any(CaBioSearchParameters.class));
    }

    @Test
    public void searchError() throws Exception {
        when(caBioFacade.retrieveGenes(any(CaBioSearchParameters.class))).thenThrow(new ConnectionException("Connection Error"));
        // Now do a connection exception.
        action.setRunSearchSelected(true);
        action.getSearchParams().setKeywords("keywords");
        assertEquals(CaBioSearchAction.ERROR, action.searchCaBio());
    }

    @Test
    public void searchPathwaysNoResults() {
        // Search on pathways now.
        action.setRunSearchSelected(true);
        action.getSearchParams().setKeywords("keywords");
        action.getSearchParams().setSearchType(CaBioSearchTypeEnum.PATHWAYS);
        assertEquals(CaBioSearchAction.INPUT, action.searchCaBio());
    }

    @Test
    public void searchPathways() throws Exception {
        when(caBioFacade.retrievePathways(any(CaBioSearchParameters.class))).thenReturn(Arrays.asList(new CaBioDisplayablePathway()));
        action.setRunSearchSelected(true);
        action.getSearchParams().setKeywords("keywords");
        action.getSearchParams().setSearchType(CaBioSearchTypeEnum.PATHWAYS);
        assertEquals(CaBioSearchAction.SUCCESS, action.searchCaBio());
        verify(caBioFacade, times(1)).retrievePathways(any(CaBioSearchParameters.class));
    }

    @Test
    public void searchGenesFromPathways() throws Exception {
        //Now search for genes based on given pathways
        action.setRunSearchSelected(false);
        action.setRunCaBioGeneSearchFromPathways(true);
        action.setCheckedPathwayBoxes("0 ");
        CaBioDisplayablePathway pathway = new CaBioDisplayablePathway();
        pathway.setId("1");

        // First without any pathways selected.
        assertEquals(CaBioSearchAction.INPUT, action.searchCaBio());
        action.getCaBioPathways().add(pathway);

        // Now with pathways.
        action.setRunSearchSelected(false);
        action.setRunCaBioGeneSearchFromPathways(true);
        assertEquals(CaBioSearchAction.SUCCESS, action.searchCaBio());
        verify(caBioFacade, times(1)).retrieveGenesFromPathways(any(CaBioSearchParameters.class));
    }

}
