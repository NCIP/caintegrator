/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.external.cabio;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.cabio.domain.Pathway;
import gov.nih.nci.cabio.domain.Taxon;
import gov.nih.nci.caintegrator2.external.ConnectionException;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@SuppressWarnings("unused")
public class CaBioFacadeImplTestIntegration {

    private CaBioFacadeImpl caBioFacade;
    private static final Logger LOGGER = Logger.getLogger(CaBioFacadeImplTestIntegration.class);    
    @Before
    public void setUp() {
        ApplicationContext context = new ClassPathXmlApplicationContext(
                "cabio-test-config.xml", CaBioFacadeImplTestIntegration.class); 
        caBioFacade = (CaBioFacadeImpl) context.getBean("caBioFacade"); 
    }

    @Test
    public void testRetrieveGenes() throws ConnectionException {
        CaBioSearchParameters params = new CaBioSearchParameters();
        params.setKeywords("heart");
        params.setTaxon("human");
        params.setFilterGenesOnStudy(false);
        List<CaBioDisplayableGene> genes = caBioFacade.retrieveGenes(params);
        assertTrue(checkSymbolExists("CDH13", genes));
        assertTrue(checkSymbolExists("FABP3", genes));
        assertTrue(checkSymbolExists("HAND1", genes));
        assertTrue(checkSymbolExists("HAND2", genes));
        assertTrue(checkSymbolExists("LBH", genes));
        assertTrue(checkSymbolExists("LOC128102", genes));
        
        params.setTaxon("mouse");
        genes = caBioFacade.retrieveGenes(params);
        assertFalse(checkSymbolExists("CDH13", genes));
        assertTrue(checkSymbolExists("FABP3", genes));
        
        params.setTaxon(CaBioSearchParameters.ALL_TAXONS);
        genes = caBioFacade.retrieveGenes(params);
        assertTrue(!genes.isEmpty());
    }
    
    @Test 
    public void testRetrievePathways() throws ConnectionException {
        CaBioSearchParameters params = new CaBioSearchParameters();
        params.setKeywords("h_41bbPathway");
        params.setTaxon("human");
        params.setSearchType(CaBioSearchTypeEnum.PATHWAYS);
        List<CaBioDisplayablePathway> pathways = caBioFacade.retrievePathways(params);
        assertEquals(1, pathways.size());
        assertEquals("475", pathways.get(0).getId());
    }
    
    @Test
    public void testRetrieveGenesFromPathways() throws ConnectionException {
        CaBioSearchParameters params = new CaBioSearchParameters();
        Taxon taxon = new Taxon();
        taxon.setCommonName("human");
        params.setTaxon("human");
        params.setFilterGenesOnStudy(false);
        Pathway pathway = new Pathway();
        pathway.setId(665l); //h_LDLpathway
        pathway.setTaxon(taxon);
        Pathway pathway2 = new Pathway();
        pathway2.setId(857l); //h_ace2Pathway
        pathway2.setTaxon(taxon);
        params.getPathways().add(pathway);
        params.getPathways().add(pathway2);
        List<CaBioDisplayableGene> genes = caBioFacade.retrieveGenesFromPathways(params);
        assertEquals(19, genes.size());
    }
    
    @Test
    public void testRetrieveAllTaxons() throws ConnectionException {
        List<String> taxons = caBioFacade.retrieveAllTaxons();
        assertFalse(taxons.isEmpty());
    }
    
    private boolean checkSymbolExists(String symbol, List<CaBioDisplayableGene> genes) {
        for (CaBioDisplayableGene gene : genes) {
            if (symbol.equals(gene.getSymbol())) {
                return true;
            }
        }
        return false;
    }
}
