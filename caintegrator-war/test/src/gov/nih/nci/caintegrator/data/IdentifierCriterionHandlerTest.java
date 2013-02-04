/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import gov.nih.nci.caintegrator.data.AbstractAnnotationCriterionHandler;
import gov.nih.nci.caintegrator.domain.application.IdentifierCriterion;
import gov.nih.nci.caintegrator.domain.application.WildCardTypeEnum;

import org.hibernate.criterion.Criterion;
import org.junit.Test;



public class IdentifierCriterionHandlerTest {
    @Test
    public void testTranslate() {
        IdentifierCriterion identifierCriterion = new IdentifierCriterion();
        identifierCriterion.setStringValue("test");
        identifierCriterion.setWildCardType(WildCardTypeEnum.WILDCARD_AFTER_STRING);
        Criterion crit = AbstractAnnotationCriterionHandler.create(identifierCriterion).translate();
        assertNotNull(crit);
        assertEquals("identifier like test%",crit.toString());
    }
}
