/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.data;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import gov.nih.nci.caintegrator.common.DateUtil;
import gov.nih.nci.caintegrator.data.AbstractAnnotationCriterionHandler;
import gov.nih.nci.caintegrator.domain.application.DateComparisonCriterion;
import gov.nih.nci.caintegrator.domain.application.DateComparisonOperatorEnum;

import java.text.ParseException;
import java.util.Date;

import org.hibernate.criterion.Criterion;
import org.junit.Test;

public class DateComparisonCriterionHandlerTest {

    @Test
    public void testTranslate() throws ParseException {
        Date testDate = DateUtil.createDate("01-01-2000");
        
        DateComparisonCriterion dateCriterion = new DateComparisonCriterion();
        dateCriterion.setDateValue(testDate);
        dateCriterion.setDateComparisonOperator(DateComparisonOperatorEnum.GREATEROREQUAL);
        Criterion crit = AbstractAnnotationCriterionHandler.create(dateCriterion).translate();
        assertNotNull(crit);
       assertEquals(AbstractAnnotationCriterionHandler.DATE_VALUE_COLUMN+">="+testDate.toString(),crit.toString());
        
       dateCriterion.setDateComparisonOperator(DateComparisonOperatorEnum.GREATER);
        crit = AbstractAnnotationCriterionHandler.create(dateCriterion).translate();
        assertEquals(AbstractAnnotationCriterionHandler.DATE_VALUE_COLUMN+">"+testDate.toString(),crit.toString());
        
        dateCriterion.setDateComparisonOperator(DateComparisonOperatorEnum.EQUAL);
        crit = AbstractAnnotationCriterionHandler.create(dateCriterion).translate();
        assertEquals(AbstractAnnotationCriterionHandler.DATE_VALUE_COLUMN+"="+testDate.toString(),crit.toString());
        
        dateCriterion.setDateComparisonOperator(DateComparisonOperatorEnum.LESS);
        crit = AbstractAnnotationCriterionHandler.create(dateCriterion).translate();
        assertEquals(AbstractAnnotationCriterionHandler.DATE_VALUE_COLUMN+"<"+testDate.toString(),crit.toString());
        
        dateCriterion.setDateComparisonOperator(DateComparisonOperatorEnum.LESSOREQUAL);
        crit = AbstractAnnotationCriterionHandler.create(dateCriterion).translate();
        assertEquals(AbstractAnnotationCriterionHandler.DATE_VALUE_COLUMN+"<="+testDate.toString(),crit.toString());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testTranslateNoComparison() throws ParseException {
        Date testDate = DateUtil.createDate("01-01-2000");
        DateComparisonCriterion dateCriterion = new DateComparisonCriterion();
        dateCriterion.setDateValue(testDate);
        AbstractAnnotationCriterionHandler.create(dateCriterion).translate();
    }

}
