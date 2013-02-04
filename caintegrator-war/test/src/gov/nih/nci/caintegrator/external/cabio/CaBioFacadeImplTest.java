/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.external.cabio;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.cabio.domain.Pathway;
import gov.nih.nci.caintegrator.data.CaIntegrator2DaoStub;
import gov.nih.nci.caintegrator.external.ConnectionException;
import gov.nih.nci.caintegrator.external.cabio.CaBioDisplayableGene;
import gov.nih.nci.caintegrator.external.cabio.CaBioDisplayablePathway;
import gov.nih.nci.caintegrator.external.cabio.CaBioFacadeImpl;
import gov.nih.nci.caintegrator.external.cabio.CaBioSearchParameters;
import gov.nih.nci.caintegrator.external.cabio.CaBioSearchTypeEnum;
import gov.nih.nci.caintegrator.external.cabio.KeywordSearchPreferenceEnum;
import gov.nih.nci.caintegrator.external.cabio.CaBioApplicationServiceFactoryStub.ApplicationServiceStub;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class CaBioFacadeImplTest {
    private CaBioFacadeImpl caBioFacade;
    private ApplicationServiceStub applicationServiceStub;

    @Before
    public void setUp() throws Exception {
        caBioFacade = new CaBioFacadeImpl();
        caBioFacade.setDao(new CaIntegrator2DaoStub());
        caBioFacade.setCaBioApplicationServiceFactory(new CaBioApplicationServiceFactoryStub());
        applicationServiceStub = (ApplicationServiceStub)
            caBioFacade.getCaBioApplicationServiceFactory().retrieveCaBioApplicationService("");
    }

    @Test
    public void testRetrieveGenes() throws ConnectionException {
        CaBioSearchParameters params = new CaBioSearchParameters();
        params.setKeywords("test here");
        params.setFilterGenesOnStudy(false);
        params.setSearchType(CaBioSearchTypeEnum.GENE_KEYWORDS);
        params.setSearchPreferenceForDisplay(KeywordSearchPreferenceEnum.ANY.getValue());
        List<CaBioDisplayableGene> genes = caBioFacade.retrieveGenes(params);
        assertEquals("BRCA1", genes.get(0).getSymbol());
        assertEquals("EGFR", genes.get(1).getSymbol());
        assertEquals("EGFR", genes.get(2).getSymbol());
        assertTrue(applicationServiceStub.hqlString.contains("(  lower(o.fullName) LIKE ?  )  OR  (  lower(o.fullName) LIKE ?  )"));
        params.setKeywords("test here");
        params.setFilterGenesOnStudy(true);
        params.setSearchPreferenceForDisplay(KeywordSearchPreferenceEnum.ALL.getValue());
        genes = caBioFacade.retrieveGenes(params);
        assertTrue(applicationServiceStub.hqlString.contains("(  lower(o.fullName) LIKE ?  )  AND  (  lower(o.fullName) LIKE ?  )"));
        assertTrue(applicationServiceStub.hqlString.contains("o.taxon.commonName LIKE ?"));
        assertEquals(1, genes.size());
        assertEquals("BRCA1", genes.get(0).getSymbol());

        params.setSearchType(CaBioSearchTypeEnum.GENE_SYMBOL);
        params.setKeywords("egfr brca");
        params.setSearchPreference(KeywordSearchPreferenceEnum.ANY);
        genes = caBioFacade.retrieveGenes(params);
        // Should contain 4 keyword matches, because 2 keywords, and 2 fields (symbol and hugoSymbol)
        assertTrue(applicationServiceStub.hqlString.contains("(  lower(o.symbol) LIKE ?  OR  lower(o.hugoSymbol) LIKE ?  )  OR  (  lower(o.symbol) LIKE ?  OR  lower(o.hugoSymbol) LIKE ?  )"));
        params.setSearchPreference(KeywordSearchPreferenceEnum.ALL);
        genes = caBioFacade.retrieveGenes(params);
        // Should contain 4 keyword matches, because 2 keywords, and 2 fields (symbol and hugoSymbol), but it must match both keywords, hence the AND instead of OR.
        assertTrue(applicationServiceStub.hqlString.contains("(  lower(o.symbol) LIKE ?  OR  lower(o.hugoSymbol) LIKE ?  )  AND  (  lower(o.symbol) LIKE ?  OR  lower(o.hugoSymbol) LIKE ?  )"));

        params.setSearchType(CaBioSearchTypeEnum.DATABASE_CROSS_REF);
        params.setKeywords("OMIM");
        genes = caBioFacade.retrieveGenes(params);
        // Should contain 2 keyword matches
        assertTrue(applicationServiceStub.hqlString.contains("(  lower(o.crossReferenceId) LIKE ?  )  ) and o.gene.taxon.commonName LIKE ?"));
    }

    @Test
    public void testRetrieveGenesFromGeneAlias() throws ConnectionException {
        CaBioSearchParameters params = new CaBioSearchParameters();
        params.setKeywords("test here");
        params.setFilterGenesOnStudy(false);
        params.setSearchType(CaBioSearchTypeEnum.GENE_ALIAS);
        params.setSearchPreferenceForDisplay(KeywordSearchPreferenceEnum.ANY.getValue());
        params.setTaxon(CaBioSearchParameters.ALL_TAXONS);
        List<CaBioDisplayableGene> genes = caBioFacade.retrieveGenes(params);
        assertEquals("BRCA1", genes.get(0).getSymbol());
        assertEquals("EGFR", genes.get(1).getSymbol());
        assertEquals("EGFR", genes.get(2).getSymbol());
        assertTrue(applicationServiceStub.hqlString.contains("(  lower(o.name) LIKE ?  )  OR  (  lower(o.name) LIKE ?  )"));
        params.setKeywords("test here");
        params.setFilterGenesOnStudy(true);
        params.setSearchPreferenceForDisplay(KeywordSearchPreferenceEnum.ALL.getValue());
        params.setTaxon(CaBioSearchParameters.HUMAN_TAXON);
        genes = caBioFacade.retrieveGenes(params);
        assertTrue(applicationServiceStub.hqlString.contains("(  lower(o.name) LIKE ?  )  AND  (  lower(o.name) LIKE ?  )"));
        assertTrue(applicationServiceStub.hqlString.contains("gene.taxon.commonName LIKE ?"));
        assertEquals(1, genes.size());
        assertEquals("BRCA1", genes.get(0).getSymbol());
    }

    @Test
    public void testRetrievePathways() throws ConnectionException {
        CaBioSearchParameters params = new CaBioSearchParameters();
        params.setKeywords("test here");
        params.setFilterGenesOnStudy(false);
        params.setSearchType(CaBioSearchTypeEnum.PATHWAYS);
        params.setSearchPreferenceForDisplay(KeywordSearchPreferenceEnum.ANY.getValue());
        params.setTaxon(CaBioSearchParameters.ALL_TAXONS);
        List<CaBioDisplayablePathway> pathways = caBioFacade.retrievePathways(params);
        assertEquals("pathway1", pathways.get(0).getName());
        assertEquals("pathway3", pathways.get(1).getName());
        assertTrue(applicationServiceStub.hqlString.contains("(  lower(o.name) LIKE ?  OR  lower(o.displayValue) LIKE ?  OR  lower(o.description) LIKE ?  )  OR  (  lower(o.name) LIKE ?  OR  lower(o.displayValue) LIKE ?  OR  lower(o.description) LIKE ?  )"));
        assertFalse(applicationServiceStub.hqlString.contains("o.taxon.commonName LIKE ?"));
        params.setKeywords("test here");
        params.setFilterGenesOnStudy(true);
        params.setSearchPreferenceForDisplay(KeywordSearchPreferenceEnum.ALL.getValue());
        params.setTaxon(CaBioSearchParameters.HUMAN_TAXON);
        pathways = caBioFacade.retrievePathways(params);
        assertTrue(applicationServiceStub.hqlString.contains("(  lower(o.name) LIKE ?  OR  lower(o.displayValue) LIKE ?  OR  lower(o.description) LIKE ?  )  AND  (  lower(o.name) LIKE ?  OR  lower(o.displayValue) LIKE ?  OR  lower(o.description) LIKE ?  )"));
        assertTrue(applicationServiceStub.hqlString.contains("o.taxon.commonName LIKE ?"));
    }

    @Test
    public void testRetrieveGenesFromPathways() throws ConnectionException {
        CaBioSearchParameters params = new CaBioSearchParameters();
        params.getPathways().add(new Pathway());
        params.setTaxon(CaBioSearchParameters.HUMAN_TAXON);
        params.setFilterGenesOnStudy(false);
        List<CaBioDisplayableGene> genes = caBioFacade.retrieveGenesFromPathways(params);
        assertEquals(1, genes.size());
    }

}
