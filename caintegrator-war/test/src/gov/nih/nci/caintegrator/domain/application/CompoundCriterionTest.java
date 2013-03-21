/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.domain.application;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator.application.query.InvalidCriterionException;

import java.util.Set;

import org.junit.Test;

/**
 * Tests validation of various compound criteria.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
public class CompoundCriterionTest {

    /**
     * Tests valid fold change.
     *
     * @throws InvalidCriterionException on unexpected error
     */
    @Test
    public void validFoldChange() throws InvalidCriterionException {
        FoldChangeCriterion allGenesCriterion = new FoldChangeCriterion();

        CompoundCriterion compoundCriterion = new CompoundCriterion();
        compoundCriterion.getCriterionCollection().add(allGenesCriterion);
        compoundCriterion.validateGeneExpressionCriterion();
    }

    /**
     *  Tests that duplicate all genes fold change criterion result in a validation exception.
     *
     * @throws InvalidCriterionException on expected invalid criterion exception
     */
    @Test(expected = InvalidCriterionException.class)
    public void invalidDuplicateAllGenesFoldChangeCriterion() throws InvalidCriterionException {
        FoldChangeCriterion allGenesCriterion = new FoldChangeCriterion();
        FoldChangeCriterion otherAllGeneCriterion = new FoldChangeCriterion();

        CompoundCriterion compoundCriterion = new CompoundCriterion();
        compoundCriterion.getCriterionCollection().add(allGenesCriterion);
        compoundCriterion.getCriterionCollection().add(otherAllGeneCriterion);
        compoundCriterion.validateGeneExpressionCriterion();
    }

    /**
     *  Tests that a fold change criterion with a single gene and one with all genes result in a validation exception.
     *
     * @throws InvalidCriterionException on expected invalid criterion exception
     */
    @Test(expected = InvalidCriterionException.class)
    public void invalidAllGenesAndOneGeneFoldChangeCriterion() throws InvalidCriterionException {
        FoldChangeCriterion allGenesCriterion = new FoldChangeCriterion();
        FoldChangeCriterion singleGeneCriterion = new FoldChangeCriterion();
        singleGeneCriterion.setGeneSymbol("egfr");

        CompoundCriterion compoundCriterion = new CompoundCriterion();
        compoundCriterion.getCriterionCollection().add(allGenesCriterion);
        compoundCriterion.getCriterionCollection().add(singleGeneCriterion);
        compoundCriterion.validateGeneExpressionCriterion();
    }

    /**
     * Tests that two gene fold change criterion with different separate genes results is a valid criteria.
     *
     * @throws InvalidCriterionException on unexpected error
     */
    @Test
    public void validTwoGeneFoldChangeCriterion() throws InvalidCriterionException {
        FoldChangeCriterion brca1GenesCriterion = new FoldChangeCriterion();
        brca1GenesCriterion.setGeneSymbol("brac1");
        FoldChangeCriterion egfrGeneCriterion = new FoldChangeCriterion();
        egfrGeneCriterion.setGeneSymbol("egfr");

        CompoundCriterion compoundCriterion = new CompoundCriterion();
        compoundCriterion.getCriterionCollection().add(brca1GenesCriterion);
        compoundCriterion.getCriterionCollection().add(egfrGeneCriterion);
        compoundCriterion.validateGeneExpressionCriterion();
    }

    /**
     * Tests that a gene game and fold change criterion cannot be mixed.
     *
     * @throws InvalidCriterionException on the expected invalid criterion exception
     */
    @Test(expected = InvalidCriterionException.class)
    public void invalidFoldChangeAndGeneName() throws InvalidCriterionException {
        FoldChangeCriterion egfrGeneCriterion = new FoldChangeCriterion();
        egfrGeneCriterion.setGeneSymbol("egfr");
        GeneNameCriterion nameCriterion = new GeneNameCriterion();

        CompoundCriterion compoundCriterion = new CompoundCriterion();
        compoundCriterion.getCriterionCollection().add(egfrGeneCriterion);
        compoundCriterion.getCriterionCollection().add(nameCriterion);
        compoundCriterion.validateGeneExpressionCriterion();
    }

    /**
     * Tests that an expression level criterion's (upper level > lower level) when using the inside range type.
     *
     * @throws InvalidCriterionException  on the expected invalid criterion exception
     */
    @Test(expected = InvalidCriterionException.class)
    public void invalidInsideRangeExpressionLevelCriterion() throws InvalidCriterionException {
        ExpressionLevelCriterion expressionLevelCriterion = new ExpressionLevelCriterion();
        expressionLevelCriterion.setLowerLimit(2.0f);
        expressionLevelCriterion.setUpperLimit(1.0f);
        expressionLevelCriterion.setRangeType(RangeTypeEnum.INSIDE_RANGE);

        CompoundCriterion compoundCriterion = new CompoundCriterion();
        compoundCriterion.getCriterionCollection().add(expressionLevelCriterion);
        compoundCriterion.validateGeneExpressionCriterion();
    }

    /**
     * Tests that an expression level criterion's (upper level > lower level) when using the outside range type.
     *
     * @throws InvalidCriterionException  on the expected invalid criterion exception
     */
    @Test(expected = InvalidCriterionException.class)
    public void invalidOutsideRangeExpressionLevelCriterion() throws InvalidCriterionException {
        ExpressionLevelCriterion expressionLevelCriterion = new ExpressionLevelCriterion();
        expressionLevelCriterion.setLowerLimit(2.0f);
        expressionLevelCriterion.setUpperLimit(1.0f);
        expressionLevelCriterion.setRangeType(RangeTypeEnum.OUTSIDE_RANGE);

        CompoundCriterion compoundCriterion = new CompoundCriterion();
        compoundCriterion.getCriterionCollection().add(expressionLevelCriterion);
        compoundCriterion.validateGeneExpressionCriterion();
    }

    /**
     * Tests that an expression level criterion's (upper level > lower level) doesn't matter
     * when using the >= range type.
     *
     * @throws InvalidCriterionException on an unexpected validation error
     */
    @Test
    public void validGreaterThanEqualToExpressionLevelCriterion() throws InvalidCriterionException {
        ExpressionLevelCriterion expressionLevelCriterion = new ExpressionLevelCriterion();
        expressionLevelCriterion.setLowerLimit(2.0f);
        expressionLevelCriterion.setUpperLimit(1.0f);
        expressionLevelCriterion.setRangeType(RangeTypeEnum.GREATER_OR_EQUAL);

        CompoundCriterion compoundCriterion = new CompoundCriterion();
        compoundCriterion.getCriterionCollection().add(expressionLevelCriterion);
        compoundCriterion.validateGeneExpressionCriterion();
    }

    /**
     * Tests that an expression level criterion's (upper level > lower level) is valid for outside range types.
     *
     * @throws InvalidCriterionException on an unexpected validation error
     */
    @Test
    public void validOutsideRangeExpressionLevelCriterion() throws InvalidCriterionException {
        ExpressionLevelCriterion expressionLevelCriterion = new ExpressionLevelCriterion();
        expressionLevelCriterion.setLowerLimit(1.0f);
        expressionLevelCriterion.setUpperLimit(2.0f);
        expressionLevelCriterion.setRangeType(RangeTypeEnum.OUTSIDE_RANGE);

        CompoundCriterion compoundCriterion = new CompoundCriterion();
        compoundCriterion.getCriterionCollection().add(expressionLevelCriterion);
        compoundCriterion.validateGeneExpressionCriterion();
    }

    /**
     * Tests retrieval of compound criterion platform names.
     */
    @Test
    public void allPlatformNames() {
        FoldChangeCriterion brca1GenesCriterion = new FoldChangeCriterion();
        brca1GenesCriterion.setGeneSymbol("brac1");
        brca1GenesCriterion.setPlatformName("Test Platform");

        CompoundCriterion compoundCriterion = new CompoundCriterion();
        compoundCriterion.getCriterionCollection().add(brca1GenesCriterion);

        Set<String> platformNames = compoundCriterion.getAllPlatformNames(GenomicCriterionTypeEnum.GENE_EXPRESSION);
        assertFalse(platformNames.isEmpty());
        assertEquals(1, platformNames.size());
        assertTrue(platformNames.contains("Test Platform"));
    }
}
