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
 * Tests for the pathway result wrapper object.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
public class PathwayResultTest {
    private static final String NAME = "Name";
    private PathwayResults pathwayResult;

    /**
     * Sets up the unit tests.
     */
    @Before
    public void setUp() {
        pathwayResult = new PathwayResults();
        pathwayResult.setName(NAME);
        pathwayResult.setTitle("Title");
    }

    /**
     * Test compare to method.
     */
    @Test
    public void compareTo() {
        PathwayResults otherPathway = new PathwayResults();
        otherPathway.setName(NAME);
        assertEquals(0, pathwayResult.compareTo(otherPathway));
        otherPathway.setName("A Name");
        assertTrue(pathwayResult.compareTo(otherPathway) >= 1);
        otherPathway.setName("Other Name");
        assertTrue(pathwayResult.compareTo(otherPathway) < 0);
    }

    /**
     * Tests pathway result equality.
     */
    @Test
    public void equals() {
        Object nullObject = null;
        assertFalse(pathwayResult.equals(nullObject));
        assertFalse(pathwayResult.equals(new GeneResults()));

        PathwayResults otherPathway = new PathwayResults();
        assertFalse(pathwayResult.equals(otherPathway));
        otherPathway.setName("Other Pathway");
        assertFalse(pathwayResult.equals(otherPathway));
        otherPathway.setName(NAME);
        assertTrue(pathwayResult.equals(otherPathway));
        assertTrue(pathwayResult.equals(pathwayResult));
    }

    /**
     * Tests hash code generation.
     */
    @Test
    public void hashCodeGeneration() {
        PathwayResults otherPathway = new PathwayResults();
        assertTrue(pathwayResult.hashCode() != otherPathway.hashCode());
        otherPathway.setName("Other Name");
        assertTrue(pathwayResult.hashCode() != otherPathway.hashCode());
        otherPathway.setName(NAME);
        assertTrue(pathwayResult.hashCode() == otherPathway.hashCode());
    }
}
