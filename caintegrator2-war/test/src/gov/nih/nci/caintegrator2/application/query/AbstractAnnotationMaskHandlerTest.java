/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.query;

import static org.junit.Assert.assertEquals;
import gov.nih.nci.caintegrator2.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator2.domain.annotation.mask.AbstractAnnotationMask;
import gov.nih.nci.caintegrator2.domain.annotation.mask.MaxNumberMask;
import gov.nih.nci.caintegrator2.domain.annotation.mask.NumericRangeMask;
import gov.nih.nci.caintegrator2.domain.application.AbstractCriterion;
import gov.nih.nci.caintegrator2.domain.application.BooleanOperatorEnum;
import gov.nih.nci.caintegrator2.domain.application.CompoundCriterion;
import gov.nih.nci.caintegrator2.domain.application.NumericComparisonCriterion;
import gov.nih.nci.caintegrator2.domain.application.NumericComparisonOperatorEnum;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;


public class AbstractAnnotationMaskHandlerTest {

    @Test
    public void testCreateMaskedCriterion() {
        Set<AbstractAnnotationMask> masks = new HashSet<AbstractAnnotationMask>();
        NumericComparisonCriterion criterion = new NumericComparisonCriterion();
        criterion.setAnnotationFieldDescriptor(new AnnotationFieldDescriptor());
        criterion.setNumericComparisonOperator(NumericComparisonOperatorEnum.GREATER);
        criterion.setNumericValue(52d);
        // First try without any masks.
        NumericComparisonCriterion newCriteria = (NumericComparisonCriterion) AbstractAnnotationMaskHandler.
                                                                        createMaskedCriterion(masks, criterion);
        assertEquals(criterion, newCriteria);
        
        // add range of 5, max of 90.
        NumericRangeMask range1 = new NumericRangeMask();
        range1.setNumericRange(5);
        MaxNumberMask max1 = new MaxNumberMask();
        max1.setMaxNumber(90.0);
        masks.add(range1);
        masks.add(max1);
        newCriteria = (NumericComparisonCriterion) AbstractAnnotationMaskHandler.
        createMaskedCriterion(masks, criterion);
        assertEquals(Double.valueOf(50), newCriteria.getNumericValue());
        assertEquals(NumericComparisonOperatorEnum.GREATEROREQUAL, newCriteria.getNumericComparisonOperator());
        
        // greater or equal operator
        criterion.setNumericComparisonOperator(NumericComparisonOperatorEnum.GREATEROREQUAL);
        newCriteria = (NumericComparisonCriterion) AbstractAnnotationMaskHandler.
        createMaskedCriterion(masks, criterion);
        assertEquals(Double.valueOf(50), newCriteria.getNumericValue());
        assertEquals(NumericComparisonOperatorEnum.GREATEROREQUAL, newCriteria.getNumericComparisonOperator());
        
        // less than operator
        criterion.setNumericComparisonOperator(NumericComparisonOperatorEnum.LESS);
        newCriteria = (NumericComparisonCriterion) AbstractAnnotationMaskHandler.
        createMaskedCriterion(masks, criterion);
        assertEquals(Double.valueOf(54), newCriteria.getNumericValue());
        assertEquals(NumericComparisonOperatorEnum.LESSOREQUAL, newCriteria.getNumericComparisonOperator());
        
        // less or equal operator
        criterion.setNumericComparisonOperator(NumericComparisonOperatorEnum.LESSOREQUAL);
        newCriteria = (NumericComparisonCriterion) AbstractAnnotationMaskHandler.
        createMaskedCriterion(masks, criterion);
        assertEquals(Double.valueOf(54), newCriteria.getNumericValue());
        assertEquals(NumericComparisonOperatorEnum.LESSOREQUAL, newCriteria.getNumericComparisonOperator());
        
        // equal operator
        criterion.setNumericComparisonOperator(NumericComparisonOperatorEnum.EQUAL);
        CompoundCriterion newCompoundCriteria = (CompoundCriterion) AbstractAnnotationMaskHandler.
        createMaskedCriterion(masks, criterion);
        assertEquals(2, newCompoundCriteria.getCriterionCollection().size());
        for (AbstractCriterion crit : newCompoundCriteria.getCriterionCollection()) {
            NumericComparisonCriterion numCrit = (NumericComparisonCriterion) crit;
            if (NumericComparisonOperatorEnum.LESSOREQUAL.equals(numCrit.getNumericComparisonOperator())) {
                assertEquals(Double.valueOf(54), numCrit.getNumericValue());
            }
            if (NumericComparisonOperatorEnum.GREATEROREQUAL.equals(numCrit.getNumericComparisonOperator())) {
                assertEquals(Double.valueOf(50), numCrit.getNumericValue());
            }
        }
        assertEquals(BooleanOperatorEnum.AND, newCompoundCriteria.getBooleanOperator());
        
        // not equal operator
        criterion.setNumericComparisonOperator(NumericComparisonOperatorEnum.NOTEQUAL);
        newCompoundCriteria = (CompoundCriterion) AbstractAnnotationMaskHandler.
        createMaskedCriterion(masks, criterion);
        assertEquals(2, newCompoundCriteria.getCriterionCollection().size());
        for (AbstractCriterion crit : newCompoundCriteria.getCriterionCollection()) {
            NumericComparisonCriterion numCrit = (NumericComparisonCriterion) crit;
            if (NumericComparisonOperatorEnum.LESS.equals(numCrit.getNumericComparisonOperator())) {
                assertEquals(Double.valueOf(50), numCrit.getNumericValue());
            }
            if (NumericComparisonOperatorEnum.GREATER.equals(numCrit.getNumericComparisonOperator())) {
                assertEquals(Double.valueOf(54), numCrit.getNumericValue());
            }
        }
        assertEquals(BooleanOperatorEnum.OR, newCompoundCriteria.getBooleanOperator());
        
        // Higher than the max number.
        criterion.setNumericValue(95d);
        criterion.setNumericComparisonOperator(NumericComparisonOperatorEnum.GREATER);
        newCriteria = (NumericComparisonCriterion) AbstractAnnotationMaskHandler.
        createMaskedCriterion(masks, criterion);
        assertEquals(Double.valueOf(90), newCriteria.getNumericValue());
        assertEquals(NumericComparisonOperatorEnum.GREATEROREQUAL, newCriteria.getNumericComparisonOperator());
        
        // Higher than max number with Less than.
        criterion.setNumericValue(95d);
        criterion.setNumericComparisonOperator(NumericComparisonOperatorEnum.LESS);
        newCriteria = (NumericComparisonCriterion) AbstractAnnotationMaskHandler.
        createMaskedCriterion(masks, criterion);
        assertEquals(Double.valueOf(90), newCriteria.getNumericValue());
        assertEquals(NumericComparisonOperatorEnum.LESS, newCriteria.getNumericComparisonOperator());
    }

}
