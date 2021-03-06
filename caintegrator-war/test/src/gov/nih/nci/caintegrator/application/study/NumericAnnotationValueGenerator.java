/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.study;

import static org.junit.Assert.assertEquals;
import gov.nih.nci.caintegrator.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator.domain.annotation.NumericAnnotationValue;

import java.util.Set;


public final class NumericAnnotationValueGenerator extends AbstractTestDataGenerator<NumericAnnotationValue> {

    public static final NumericAnnotationValueGenerator INSTANCE = new NumericAnnotationValueGenerator();
    
    private NumericAnnotationValueGenerator() { 
        super();
    }
    @Override
    public void compareFields(NumericAnnotationValue original, NumericAnnotationValue retrieved) {
        assertEquals(original.getId(), retrieved.getId());
        assertEquals(original.getNumericValue(), retrieved.getNumericValue());
    }

    @Override
    public NumericAnnotationValue createPersistentObject() {
        return new NumericAnnotationValue();
    }

    @Override
    public void setValues(NumericAnnotationValue numericAnnotationValue, Set<AbstractCaIntegrator2Object> nonCascadedObjects) {
        
        numericAnnotationValue.setNumericValue(Double.valueOf(getUniqueInt()));
        numericAnnotationValue.setAnnotationDefinition(new AnnotationDefinition());        
    }

}
