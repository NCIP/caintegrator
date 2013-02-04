/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.domain.genomic;

import static org.junit.Assert.*;

import org.junit.Test;

public class AbstractReporterTest {

    @SuppressWarnings("serial")
    @Test
    public void testGetGeneSymbols() {
        AbstractReporter reporter = new AbstractReporter() {};
        assertEquals("", reporter.getGeneSymbols());
        reporter.getGenes().add(createGene("AAAA"));
        assertEquals("AAAA", reporter.getGeneSymbols());
        reporter.getGenes().add(createGene("BBBB"));
        assertEquals("BBBB, AAAA", reporter.getGeneSymbols());
    }

    private Gene createGene(String symbol) {
        Gene gene = new Gene();
        gene.setSymbol(symbol);
        return gene;
    }

}
