/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.analysis.cabio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.external.cabio.CaBioDisplayablePathway;
import gov.nih.nci.caintegrator2.external.cabio.CaBioFacadeStub;
import gov.nih.nci.caintegrator2.external.cabio.CaBioSearchTypeEnum;
import gov.nih.nci.caintegrator2.web.action.AbstractSessionBasedTest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class CaBioSearchActionTest extends AbstractSessionBasedTest {
    
    private CaBioSearchAction action;
    private CaBioFacadeStub caBioFacade;

    @Before
    public void setUp() {
        super.setUp();
        ApplicationContext context = new ClassPathXmlApplicationContext("cabio-test-config.xml", CaBioFacadeStub.class);
        action = (CaBioSearchAction) context.getBean("caBioSearchAction");
        caBioFacade = (CaBioFacadeStub) context.getBean("caBioFacadeStub");
    }


    @Test
    public void testInput() {
        assertEquals(CaBioSearchAction.SUCCESS, action.input());
    }


    @Test
    public void testSearchCaBio() {
        // If it's not "runSearchSelected" set to true it returns success.
        assertEquals(CaBioSearchAction.SUCCESS, action.searchCaBio());
        
        // If no keywords.
        assertFalse(caBioFacade.retrieveGenesCalled);
        action.setRunSearchSelected(true);
        assertEquals(CaBioSearchAction.INPUT, action.searchCaBio());
        
        // If keywords is under 3 characters
        action.setRunSearchSelected(true);
        action.getSearchParams().setKeywords("aa");
        assertEquals(CaBioSearchAction.INPUT, action.searchCaBio());
        assertFalse(caBioFacade.retrieveGenesCalled);
        assertFalse(action.isRunSearchSelected());
        
        // If keywords exist but nothing is returned.
        action.setRunSearchSelected(true);
        action.getSearchParams().setKeywords("keywords");
        assertEquals(CaBioSearchAction.INPUT, action.searchCaBio());
        assertTrue(caBioFacade.retrieveGenesCalled);
        assertFalse(action.isRunSearchSelected());
        
        // If keywords exist and something is returned.
        action.setRunSearchSelected(true);
        action.getSearchParams().setKeywords("keywords");
        caBioFacade.isReturnResults = true;
        assertEquals(CaBioSearchAction.SUCCESS, action.searchCaBio());
        assertTrue(caBioFacade.retrieveGenesCalled);
        assertFalse(action.isRunSearchSelected());
        
        // Now do a connection exception.
        action.setRunSearchSelected(true);
        action.getSearchParams().setKeywords("keywords");
        caBioFacade.isConnectionException = true;
        assertEquals(CaBioSearchAction.ERROR, action.searchCaBio());
        
        // Search on pathways now.
        action.setRunSearchSelected(true);
        action.getSearchParams().setKeywords("keywords");
        action.getSearchParams().setSearchType(CaBioSearchTypeEnum.PATHWAYS);
        caBioFacade.isReturnResults = false;
        assertEquals(CaBioSearchAction.INPUT, action.searchCaBio());
        caBioFacade.isReturnResults = true;
        assertEquals(CaBioSearchAction.SUCCESS, action.searchCaBio());
        assertTrue(caBioFacade.retrievePathwaysCalled);
        
        // Now search for genes based on given pathways
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
        assertTrue(caBioFacade.retrieveGenesFromPathwaysCalled);
    }

}
