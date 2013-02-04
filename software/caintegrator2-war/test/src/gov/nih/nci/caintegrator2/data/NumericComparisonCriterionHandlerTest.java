/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.data;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import gov.nih.nci.caintegrator2.domain.application.NumericComparisonCriterion;
import gov.nih.nci.caintegrator2.domain.application.NumericComparisonOperatorEnum;

import org.hibernate.criterion.Criterion;
import org.junit.Test;

public class NumericComparisonCriterionHandlerTest {


    @Test
    public void testTranslate() {
        NumericComparisonCriterion numCriterion = new NumericComparisonCriterion();
        numCriterion.setNumericValue(20.0);
        numCriterion.setNumericComparisonOperator(NumericComparisonOperatorEnum.GREATEROREQUAL);
        Criterion crit = AbstractAnnotationCriterionHandler.create(numCriterion).translate();
        assertNotNull(crit);
       assertEquals(AbstractAnnotationCriterionHandler.NUMERIC_VALUE_COLUMN+">=20.0",crit.toString());
        
        numCriterion.setNumericComparisonOperator(NumericComparisonOperatorEnum.GREATER);
        crit = AbstractAnnotationCriterionHandler.create(numCriterion).translate();
        assertEquals(AbstractAnnotationCriterionHandler.NUMERIC_VALUE_COLUMN+">20.0",crit.toString());
        
        numCriterion.setNumericComparisonOperator(NumericComparisonOperatorEnum.EQUAL);
        crit = AbstractAnnotationCriterionHandler.create(numCriterion).translate();
        assertEquals(AbstractAnnotationCriterionHandler.NUMERIC_VALUE_COLUMN+"=20.0",crit.toString());
        
        numCriterion.setNumericComparisonOperator(NumericComparisonOperatorEnum.LESS);
        crit = AbstractAnnotationCriterionHandler.create(numCriterion).translate();
        assertEquals(AbstractAnnotationCriterionHandler.NUMERIC_VALUE_COLUMN+"<20.0",crit.toString());
        
        numCriterion.setNumericComparisonOperator(NumericComparisonOperatorEnum.LESSOREQUAL);
        crit = AbstractAnnotationCriterionHandler.create(numCriterion).translate();
        assertEquals(AbstractAnnotationCriterionHandler.NUMERIC_VALUE_COLUMN+"<=20.0",crit.toString());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testTranslateNoComparison() {
        NumericComparisonCriterion numCriterion = new NumericComparisonCriterion();
        numCriterion.setNumericValue(20.0);
        AbstractAnnotationCriterionHandler.create(numCriterion).translate();
    }

}
