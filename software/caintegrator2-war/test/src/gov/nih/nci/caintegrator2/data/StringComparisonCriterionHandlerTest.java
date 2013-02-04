/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import gov.nih.nci.caintegrator2.domain.application.StringComparisonCriterion;
import gov.nih.nci.caintegrator2.domain.application.WildCardTypeEnum;

import org.hibernate.criterion.Criterion;
import org.junit.Test;



public class StringComparisonCriterionHandlerTest {
    @Test
    public void testTranslate() {
        StringComparisonCriterion strCriterion = new StringComparisonCriterion();
        strCriterion.setStringValue("test");
        Criterion crit = AbstractAnnotationCriterionHandler.create(strCriterion).translate();
        assertNotNull(crit);
        assertEquals(AbstractAnnotationCriterionHandler.STRING_VALUE_COLUMN+" like test",crit.toString());
        
        strCriterion.setWildCardType(WildCardTypeEnum.WILDCARD_AFTER_STRING);
        Criterion crit2 = AbstractAnnotationCriterionHandler.create(strCriterion).translate();
        assertNotNull(crit2);
        assertEquals(AbstractAnnotationCriterionHandler.STRING_VALUE_COLUMN+" like test%",crit2.toString());
        
        strCriterion.setWildCardType(WildCardTypeEnum.WILDCARD_BEFORE_STRING);
        Criterion crit3 = AbstractAnnotationCriterionHandler.create(strCriterion).translate();
        assertNotNull(crit3);
        assertEquals(AbstractAnnotationCriterionHandler.STRING_VALUE_COLUMN+" like %test",crit3.toString());
        
        strCriterion.setWildCardType(WildCardTypeEnum.WILDCARD_BEFORE_AND_AFTER_STRING);
        Criterion crit4 = AbstractAnnotationCriterionHandler.create(strCriterion).translate();
        assertNotNull(crit4);
        assertEquals(AbstractAnnotationCriterionHandler.STRING_VALUE_COLUMN+" like %test%",crit4.toString());
        
        strCriterion.setWildCardType(WildCardTypeEnum.WILDCARD_OFF);
        Criterion crit5 = AbstractAnnotationCriterionHandler.create(strCriterion).translate();
        assertNotNull(crit5);
        assertEquals(AbstractAnnotationCriterionHandler.STRING_VALUE_COLUMN+" like test",crit5.toString());
    }
}
