/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.query.domain;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import gov.nih.nci.caintegrator2.application.study.AbstractTestDataGenerator;
import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.NumericComparisonCriterion;


public final class NumericComparisonCriterionGenerator extends AbstractTestDataGenerator<NumericComparisonCriterion> {

    public static final NumericComparisonCriterionGenerator INSTANCE = new NumericComparisonCriterionGenerator();
    
    private NumericComparisonCriterionGenerator() { 
        super();
    }
    @Override
    public void compareFields(NumericComparisonCriterion original, NumericComparisonCriterion retrieved) {
        assertEquals(original.getId(), retrieved.getId());
        assertEquals(original.getNumericValue(), retrieved.getNumericValue());
        assertEquals(original.getAnnotationDefinition(), retrieved.getAnnotationDefinition());

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
