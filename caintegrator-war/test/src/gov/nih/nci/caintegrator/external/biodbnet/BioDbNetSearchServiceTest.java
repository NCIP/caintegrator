/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.external.biodbnet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyCollectionOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import gov.nih.nci.caintegrator.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator.domain.translational.Study;
import gov.nih.nci.caintegrator.external.biodbnet.enums.Taxon;
import gov.nih.nci.caintegrator.external.biodbnet.search.GeneResults;
import gov.nih.nci.caintegrator.external.biodbnet.search.PathwayResults;
import gov.nih.nci.caintegrator.external.biodbnet.search.SearchParameters;
import gov.nih.nci.caintegrator.mockito.AbstractMockitoTest;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;


/**
 * Tests for retrieving and parsing of bioDbNet data.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
public class BioDbNetSearchServiceTest extends AbstractMockitoTest {
    private static final Long BRCA1_GENE_ID = 672L;
    private static final String BRCA1_GENE_NAME = "BRCA1";
    private static final Set<String> GENE_IDS = Sets.newHashSet("101022062", "672", "100347269", "554178", "101046814",
            "100051990", "100049662", "353120", "712634", "449497", "100439533", "373983", "827854", "493761",
            "100952916", "100580360", "101108584", "403437", "100388186", "100224649", "100986197", "101081937",
            "100914310");
    private static final Set<String> PATHWAYS = Sets.newHashSet("h_bard1Pathway", "h_carm-erPathway", "h_atmPathway",
            "h_g2Pathway", "h_atrbrcaPathway");
    private BioDbNetService bioDbNetService;
    private Study studyOne = new Study();
    private Study studyTwo = new Study();

    /**
     * Sets up the necessary mock information.
     */
    @Before
    public void setUp() {
        BioDbNetSearchImpl searchService = new BioDbNetSearchImpl();
        searchService.setBioDbNetRemoteService(bioDbNetRemoteService);

        CaIntegrator2Dao dao = mock(CaIntegrator2Dao.class);
        when(dao.retrieveGeneSymbolsInStudy(anyCollectionOf(String.class), eq(studyOne)))
            .thenReturn(Sets.newHashSet(BRCA1_GENE_NAME));
        when(dao.retrieveGeneSymbolsInStudy(anyCollectionOf(String.class), eq(studyTwo)))
            .thenReturn(new HashSet<String>());
        searchService.setDao(dao);
        bioDbNetService = searchService;
    }

    /**
     * Tests retrieval of gene ids from gene symbols.
     */
    @Test
    public void retrieveGeneIds() {
        SearchParameters params = new SearchParameters();
        params.setInputValues(BRCA1_GENE_NAME);
        params.setTaxon(Taxon.ALL);
        Set<String> geneIds = bioDbNetService.retrieveGeneIds(params);
        assertFalse(geneIds.isEmpty());
        assertEquals(GENE_IDS.size(), geneIds.size());
        for (String id : geneIds) {
            assertTrue(GENE_IDS.contains(id));
        }
    }

    /**
     * Tests gene retrieval and parsing.
     */
    @Test
    public void retrieveGenes() {
        SearchParameters params = new SearchParameters();
        params.setInputValues(StringUtils.join(GENE_IDS, ','));
        params.setTaxon(Taxon.ALL);

        Set<GeneResults> results = bioDbNetService.retrieveGenesById(params);
        assertFalse(results.isEmpty());
        assertEquals(GENE_IDS.size(), results.size());

        GeneResults gene = results.iterator().next();
        assertEquals(gene.getGeneId(), BRCA1_GENE_ID);
        assertEquals(BRCA1_GENE_NAME, gene.getSymbol());
        assertEquals("breast cancer 1, early onset", gene.getDescription());
        assertEquals("human", gene.getTaxon());
        assertEquals("PSCP, RNF53, IRIS, PNCA4, BRCAI, BRCC1, PPP1R53, BROVCA1", gene.getAliases());
    }

    /**
     * Tests retrieval of gene ids from gene aliases.
     */
    @Test
    public void retrieveGeneIdsFromAliases() {
        SearchParameters params = new SearchParameters();
        params.setTaxon(Taxon.HUMAN);
        params.setInputValues("PNCA4,BRCA1,brca1,BRCC1");

        Set<String> geneIds = bioDbNetService.retrieveGeneIdsByAlias(params);
        assertFalse(geneIds.isEmpty());
        assertEquals(2, geneIds.size());
    }

    /**
     * Tests retrieval of gene information from biocarta pathway names.
     */
    @Test
    public void retrieveGenesFromPathways() {
        SearchParameters params = new SearchParameters();
        params.setInputValues(StringUtils.join(PATHWAYS, ','));
        params.setTaxon(Taxon.ALL);

        Set<GeneResults> genes = bioDbNetService.retrieveGenesByPathway(params);
        assertEquals(GENE_IDS.size(), genes.size());

        GeneResults gene = genes.iterator().next();
        assertEquals(gene.getGeneId(), BRCA1_GENE_ID);
        assertEquals(BRCA1_GENE_NAME, gene.getSymbol());
        assertEquals("breast cancer 1, early onset", gene.getDescription());
        assertEquals("human", gene.getTaxon());
        assertEquals("PSCP, RNF53, IRIS, PNCA4, BRCAI, BRCC1, PPP1R53, BROVCA1", gene.getAliases());
    }

    /**
     * Tests retrieval of gene pathways from gene symbols.
     */
    @Test
    public void retrievePathwaysByGeneSymbols() {
        SearchParameters params = new SearchParameters();
        params.setTaxon(Taxon.ALL);
        params.setInputValues(BRCA1_GENE_NAME);

        Set<PathwayResults> pathwayResults = bioDbNetService.retrievePathwaysByGeneSymbols(params);
        assertEquals(5, pathwayResults.size());

        PathwayResults result = pathwayResults.iterator().next();
        assertEquals("h_atmPathway", result.getName());
        assertEquals("ATM Signaling Pathway", result.getTitle());
    }

    /**
     * Tests filtering of gene symbols based on those that are availble to the study when the given gene
     * symbol is allowed.
     */
    @Test
    public void filterGeneSymbolsAllowed() {
        SearchParameters params = new SearchParameters();
        params.setTaxon(Taxon.ALL);
        params.setInputValues(BRCA1_GENE_ID.toString());
        params.setFilterGenesOnStudy(true);
        params.setStudy(studyOne);

        Set<GeneResults> genes = bioDbNetService.retrieveGenesById(params);
        assertFalse(genes.isEmpty());
        assertEquals(23, genes.size());
        for (GeneResults result : genes) {
            assertEquals(BRCA1_GENE_NAME, result.getSymbol());
        }
    }

    /**
     * Tests filtering of gene symbols based on those that are availble to the study when the given gene
     * symbol is not allowed.
     */
    @Test
    public void filterGeneSymbolsNotAllowed() {
        SearchParameters params = new SearchParameters();
        params.setTaxon(Taxon.ALL);
        params.setInputValues("1");
        params.setFilterGenesOnStudy(true);
        params.setStudy(studyTwo);

        Set<GeneResults> genes = bioDbNetService.retrieveGenesById(params);
        assertTrue(genes.isEmpty());
    }
}
