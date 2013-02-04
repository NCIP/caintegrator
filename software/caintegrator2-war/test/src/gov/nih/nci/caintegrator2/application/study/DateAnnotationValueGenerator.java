/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.study;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.common.DateUtil;
import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.annotation.DateAnnotationValue;

import java.util.Date;
import java.util.Set;


public final class DateAnnotationValueGenerator extends AbstractTestDataGenerator<DateAnnotationValue> {

    public static final DateAnnotationValueGenerator INSTANCE = new DateAnnotationValueGenerator();
    
    private DateAnnotationValueGenerator() { 
        super();
    }
    @Override
    public void compareFields(DateAnnotationValue original, DateAnnotationValue retrieved) {
        assertEquals(original.getId(), retrieved.getId());
        assertTrue(DateUtil.equal(original.getDateValue(), retrieved.getDateValue()));
        
    }

    @Override
    public DateAnnotationValue createPersistentObject() {
        return new DateAnnotationValue();
    }

    @Override
    public void setValues(DateAnnotationValue dateAnnotationValue, Set<AbstractCaIntegrator2Object> nonCascadedObjects) {
        
        dateAnnotationValue.setDateValue(new Date());
        dateAnnotationValue.setAnnotationDefinition(new AnnotationDefinition());        
    }

}
