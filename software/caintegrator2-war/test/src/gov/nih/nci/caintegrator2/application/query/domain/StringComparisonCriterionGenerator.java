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
import gov.nih.nci.caintegrator2.domain.application.StringComparisonCriterion;


public final class StringComparisonCriterionGenerator extends AbstractTestDataGenerator<StringComparisonCriterion> {

    public static final StringComparisonCriterionGenerator INSTANCE = new StringComparisonCriterionGenerator();
    
    private StringComparisonCriterionGenerator() { 
        super();
    }
    @Override
    public void compareFields(StringComparisonCriterion original, StringComparisonCriterion retrieved) {
        assertEquals(original.getId(), retrieved.getId());
        assertEquals(original.getStringValue(), retrieved.getStringValue());
        assertEquals(original.getAnnotationFieldDescriptor(), retrieved.getAnnotationFieldDescriptor());

    }

    @Override
    public StringComparisonCriterion createPersistentObject() {
        return new StringComparisonCriterion();
    }

    @Override
    public void setValues(StringComparisonCriterion stringComparisonCriterion, Set<AbstractCaIntegrator2Object> nonCascadedObjects) {
        stringComparisonCriterion.setEntityType(EntityTypeEnum.SUBJECT);
        stringComparisonCriterion.setStringValue(getUniqueString());

    }

}
