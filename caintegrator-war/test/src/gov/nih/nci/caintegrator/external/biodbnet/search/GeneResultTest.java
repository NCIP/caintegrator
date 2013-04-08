/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.external.biodbnet.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the gene result wrapper object.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
public class GeneResultTest {
    private GeneResults geneResult;

    /**
     * Sets up the unit tests.
     */
    @Before
    public void setUp() {
        geneResult = new GeneResults();
        geneResult.setGeneId(1L);
        geneResult.setSymbol("Test Symbol");
    }

    /**
     * Tests compare to method.
     */
    @Test
    public void compareTo() {
        GeneResults otherGeneResult = new GeneResults();
        otherGeneResult.setGeneId(1L);
        assertEquals(0, geneResult.compareTo(otherGeneResult));
        otherGeneResult.setGeneId(0L);
        assertEquals(1, geneResult.compareTo(otherGeneResult));
        otherGeneResult.setGeneId(2L);
        assertEquals(-1, geneResult.compareTo(otherGeneResult));
    }

    /**
     * Tests gene result equality.
     */
    @Test
    public void equals() {
        Object nullObject = null;
        assertFalse(geneResult.equals(nullObject));
        assertFalse(geneResult.equals(new PathwayResults()));


        GeneResults otherGeneResult = new GeneResults();
        assertFalse(geneResult.equals(otherGeneResult));
        otherGeneResult.setSymbol("Symbol");
        assertFalse(geneResult.equals(otherGeneResult));
        otherGeneResult.setGeneId(2L);
        assertFalse(geneResult.equals(otherGeneResult));
        otherGeneResult.setGeneId(1L);
        assertTrue(geneResult.equals(otherGeneResult));
        assertTrue(geneResult.equals(geneResult));
    }

    /**
     * Tests hash code generation.
     */
    @Test
    public void hashCodeGeneration() {
        GeneResults otherGeneResult = new GeneResults();
        assertTrue(geneResult.hashCode()  != otherGeneResult.hashCode());
        otherGeneResult.setGeneId(2L);
        assertTrue(geneResult.hashCode() != otherGeneResult.hashCode());
        otherGeneResult.setGeneId(1L);
        assertTrue(geneResult.hashCode() == otherGeneResult.hashCode());
    }

}
