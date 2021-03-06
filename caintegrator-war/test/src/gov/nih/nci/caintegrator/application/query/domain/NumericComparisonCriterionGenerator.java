/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.query.domain;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import gov.nih.nci.caintegrator.application.study.AbstractTestDataGenerator;
import gov.nih.nci.caintegrator.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator.domain.application.NumericComparisonCriterion;


public final class NumericComparisonCriterionGenerator extends AbstractTestDataGenerator<NumericComparisonCriterion> {

    public static final NumericComparisonCriterionGenerator INSTANCE = new NumericComparisonCriterionGenerator();
    
    private NumericComparisonCriterionGenerator() { 
        super();
    }
    @Override
    public void compareFields(NumericComparisonCriterion original, NumericComparisonCriterion retrieved) {
        assertEquals(original.getId(), retrieved.getId());
        assertEquals(original.getNumericValue(), retrieved.getNumericValue());
        assertEquals(original.getAnnotationFieldDescriptor(), retrieved.getAnnotationFieldDescriptor());

    }

    @Override
    public NumericComparisonCriterion createPersistentObject() {
        return new NumericComparisonCriterion();
    }

    @Override
    public void setValues(NumericComparisonCriterion numericComparisonCriterion, Set<AbstractCaIntegrator2Object> nonCascadedObjects) {
        numericComparisonCriterion.setEntityType(EntityTypeEnum.SUBJECT);
        numericComparisonCriterion.setNumericValue(Double.valueOf(getUniqueInt()));

    }

}
