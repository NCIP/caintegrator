/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.data;


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.application.study.AnnotationTypeEnum;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.annotation.PermissibleValue;
import gov.nih.nci.caintegrator2.domain.application.SelectedValueCriterion;

import java.util.Collection;
import java.util.HashSet;
import java.util.regex.Pattern;

import org.hibernate.criterion.Criterion;
import org.junit.Test;

public class SelectedValueCriterionHandlerTest {

    @Test
    public void testTranslate() {
        SelectedValueCriterion svCriterion = new SelectedValueCriterion();
        svCriterion.setAnnotationDefinition(new AnnotationDefinition());
        svCriterion.getAnnotationDefinition().setDataType(AnnotationTypeEnum.NUMERIC);
        Collection <PermissibleValue> valueCollection = new HashSet<PermissibleValue>();
        PermissibleValue val1 = new PermissibleValue();
        val1.setValue("123");
        valueCollection.add(val1);
        svCriterion.setValueCollection(valueCollection);
        
        Criterion crit = AbstractAnnotationCriterionHandler.create(svCriterion).translate();
        assertNotNull(crit);
        assertTrue(Pattern.compile(AbstractAnnotationCriterionHandler.NUMERIC_VALUE_COLUMN+" in").
                                    matcher(crit.toString()).find());
        svCriterion.getAnnotationDefinition().setDataType(AnnotationTypeEnum.STRING);
        valueCollection.clear();
        crit = AbstractAnnotationCriterionHandler.create(svCriterion).translate();
        assertTrue(Pattern.compile(AbstractAnnotationCriterionHandler.STRING_VALUE_COLUMN+" in").
                                    matcher(crit.toString()).find());
        svCriterion.getAnnotationDefinition().setDataType(AnnotationTypeEnum.DATE);
        crit = AbstractAnnotationCriterionHandler.create(svCriterion).translate();
        assertTrue(Pattern.compile(AbstractAnnotationCriterionHandler.DATE_VALUE_COLUMN+" in").
                                    matcher(crit.toString()).find());
    }

}
