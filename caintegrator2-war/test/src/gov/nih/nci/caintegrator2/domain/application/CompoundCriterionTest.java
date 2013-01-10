/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.domain.application;

import static org.junit.Assert.*;
import gov.nih.nci.caintegrator2.application.query.InvalidCriterionException;

import org.junit.Test;


/**
 * 
 */
public class CompoundCriterionTest {

    @Test
    public void testValidate() {
        
        boolean getException = false;
        FoldChangeCriterion foldChange1 = new FoldChangeCriterion();
        FoldChangeCriterion foldChange2 = new FoldChangeCriterion();
        
        // Test valid FoldChange
        CompoundCriterion compoundCriterion = new CompoundCriterion();
        compoundCriterion.getCriterionCollection().add(foldChange1);
        try {
            compoundCriterion.validateGeneExpressionCriterion();
        } catch (InvalidCriterionException e) {
            getException = true;
        }
        assertFalse(getException);

        // Test invalid 2 FoldChange with duplicate all genes
        compoundCriterion.getCriterionCollection().add(foldChange2);
        getException = false;
        try {
            compoundCriterion.validateGeneExpressionCriterion();
        } catch (InvalidCriterionException e) {
            getException = true;
        }
        assertTrue(getException);

        // Test invalid 2 FoldChange with a gene and all genes
        foldChange1.setGeneSymbol("egfr");
        getException = false;
        try {
            compoundCriterion.validateGeneExpressionCriterion();
        } catch (InvalidCriterionException e) {
            getException = true;
        }
        assertTrue(getException);

        // Test valid 2 FoldChange with different genes
        foldChange2.setGeneSymbol("brac1");
        getException = false;
        try {
            compoundCriterion.validateGeneExpressionCriterion();
        } catch (InvalidCriterionException e) {
            getException = true;
        }
        assertFalse(getException);

        // Test invalid FoldChange and Gene Name
        compoundCriterion = new CompoundCriterion();
        compoundCriterion.getCriterionCollection().add(foldChange1);
        compoundCriterion.getCriterionCollection().add(new GeneNameCriterion());
        getException = false;
        try {
            compoundCriterion.validateGeneExpressionCriterion();
        } catch (InvalidCriterionException e) {
            getException = true;
        }
        assertTrue(getException);
        
        // Test invalid expression level criterion
        compoundCriterion = new CompoundCriterion();
        ExpressionLevelCriterion expressionLevelCriterion = new ExpressionLevelCriterion();
        compoundCriterion.getCriterionCollection().add(expressionLevelCriterion);
        expressionLevelCriterion.setLowerLimit(2.0f);
        expressionLevelCriterion.setUpperLimit(1.0f);
        expressionLevelCriterion.setRangeType(RangeTypeEnum.INSIDE_RANGE);
        getException = false;
        try {
            compoundCriterion.validateGeneExpressionCriterion();
        } catch (InvalidCriterionException e) {
            getException = true;
        }
        assertTrue(getException);
        // Now try invalid outside range type.
        expressionLevelCriterion.setRangeType(RangeTypeEnum.OUTSIDE_RANGE);
        getException = false;
        try {
            compoundCriterion.validateGeneExpressionCriterion();
        } catch (InvalidCriterionException e) {
            getException = true;
        }
        assertTrue(getException);
        // Valid greater or equal type
        expressionLevelCriterion.setRangeType(RangeTypeEnum.GREATER_OR_EQUAL);
        getException = false;
        try {
            compoundCriterion.validateGeneExpressionCriterion();
        } catch (InvalidCriterionException e) {
            getException = true;
        }
        assertFalse(getException);
        // Valid outside range type.
        getException = false;
        expressionLevelCriterion.setLowerLimit(1.0f);
        expressionLevelCriterion.setUpperLimit(2.0f);
        expressionLevelCriterion.setRangeType(RangeTypeEnum.OUTSIDE_RANGE);
        try {
            compoundCriterion.validateGeneExpressionCriterion();
        } catch (InvalidCriterionException e) {
            getException = true;
        }
        assertFalse(getException);
    }
}
