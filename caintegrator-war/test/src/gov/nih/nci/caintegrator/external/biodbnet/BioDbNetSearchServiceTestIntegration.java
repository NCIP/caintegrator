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
import gov.nih.nci.caintegrator.external.biodbnet.enums.Taxon;
import gov.nih.nci.caintegrator.external.biodbnet.search.GeneResults;
import gov.nih.nci.caintegrator.external.biodbnet.search.SearchParameters;

import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.google.common.collect.Sets;

/**
 * Integration tests for retrieving and parsing of bioDbNet data.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
public class BioDbNetSearchServiceTestIntegration {
    private static final Long BRCA1_GENE_ID = 672L;
    private static final String BRCA1_GENE_NAME = "BRCA1";
    private static final Set<String> GENE_IDS = Sets.newHashSet("101022062", "672", "100347269", "554178", "101046814",
            "100051990", "100049662", "353120", "712634", "449497", "100439533", "373983", "827854", "493761",
            "100952916", "100580360", "101108584", "403437", "100388186", "100224649", "100986197", "101081937",
            "100914310");
    private static final Set<String> PATHWAYS = Sets.newHashSet("h_bard1Pathway", "h_carm-erPathway", "h_atmPathway",
            "h_g2Pathway", "h_atrbrcaPathway");
    private BioDbNetService bioDbNetService;

    /**
     * Sets up the necessary mock information.
     */
    @Before
    public void setUp() {
        ApplicationContext context = new ClassPathXmlApplicationContext("biodbnet-test-config.xml",
                BioDbNetSearchServiceTestIntegration.class);
        bioDbNetService = (BioDbNetSearchImpl) context.getBean("bioDbNetService");
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
     * Tests gene retrieval and parsing of genes with case sensitivity enabled.
     */
    @Test
    public void retrieveGenes() {
        SearchParameters params = new SearchParameters();
        params.setInputValues(StringUtils.join(GENE_IDS, ','));
        params.setTaxon(Taxon.ALL);
        params.setCaseSensitiveSearch(true);

        Set<GeneResults> results = bioDbNetService.retrieveGenesById(params);
        assertFalse(results.isEmpty());
        assertEquals(GENE_IDS.size(), results.size());

        GeneResults gene = results.iterator().next();
        assertEquals(gene.getGeneId(), BRCA1_GENE_ID);
        assertEquals(BRCA1_GENE_NAME, gene.getSymbol());
        assertEquals("breast cancer 1, early onset", gene.getDescription());
        assertEquals("human", gene.getTaxon());
        assertEquals("PSCP,RNF53,IRIS,PNCA4,BRCAI,BRCC1,PPP1R53,BROVCA1", gene.getAliases());
    }

    /**
     * Tests gene retrieval and parsing with case sensitivity disabled.
     */
    @Test
    public void retrieveGenesCaseInsensitive() {
        SearchParameters params = new SearchParameters();
        params.setInputValues(StringUtils.join(GENE_IDS, ','));
        params.setTaxon(Taxon.ALL);
        params.setCaseSensitiveSearch(false);

        Set<GeneResults> results = bioDbNetService.retrieveGenesById(params);
        assertFalse(results.isEmpty());
        assertEquals(GENE_IDS.size(), results.size());

        GeneResults gene = results.iterator().next();
        assertEquals(gene.getGeneId(), BRCA1_GENE_ID);
        assertEquals(BRCA1_GENE_NAME, gene.getSymbol());
        assertEquals("breast cancer 1, early onset", gene.getDescription());
        assertEquals("human", gene.getTaxon());
        assertEquals("PSCP,RNF53,IRIS,PNCA4,BRCAI,BRCC1,PPP1R53,BROVCA1", gene.getAliases());
    }

    /**
     * Tests retrieval of gene ids from gene aliases with case sensitivity enabled.
     */
    @Test
    public void retrieveGeneIdsFromAliases() {
        SearchParameters params = new SearchParameters();
        params.setTaxon(Taxon.HUMAN);
        params.setInputValues("PNCA4,BRCA1,brca1,BRCC1");
        params.setCaseSensitiveSearch(true);

        Set<String> geneIds = bioDbNetService.retrieveGeneIdsByAlias(params);
        assertFalse(geneIds.isEmpty());
        assertEquals(2, geneIds.size());
    }

    /**
     * Tests retrieval of gene ids from gene aliases with case sensitivity disabled.
     */
    @Test
    public void retrieveGeneIdsFromAliasesCaseInsensitive() {
        SearchParameters params = new SearchParameters();
        params.setTaxon(Taxon.ALL);
        params.setInputValues("PNCA4,BRCA1,BRCC1");
        params.setCaseSensitiveSearch(false);

        Set<String> geneIds = bioDbNetService.retrieveGeneIdsByAlias(params);
        assertFalse(geneIds.isEmpty());
        assertEquals(24, geneIds.size());
    }

    /**
     * Tests retrieval of gene information from biocarta pathway names.
     */
    @Test
    public void retrieveGenesFromPathways() {
        SearchParameters params = new SearchParameters();
        params.setInputValues(StringUtils.join(PATHWAYS, ','));
        params.setTaxon(Taxon.ALL);
        params.setCaseSensitiveSearch(true);

        Set<GeneResults> genes = bioDbNetService.retrieveGenesByPathway(params);
        assertEquals(122,  genes.size());

        GeneResults gene = genes.iterator().next();
        assertTrue(gene.getGeneId().equals(190L));
        assertEquals("NR0B1", gene.getSymbol());
        assertEquals("nuclear receptor subfamily 0, group B, member 1", gene.getDescription());
        assertEquals("human", gene.getTaxon());
        assertEquals("AHX,DAX-1,SRXY2,AHC,HHG,NROB1,DAX1,AHCH,GTD,DSS", gene.getAliases());
    }

}
