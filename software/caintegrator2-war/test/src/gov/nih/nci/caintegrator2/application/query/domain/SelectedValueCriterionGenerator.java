/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.query.domain;

import static org.junit.Assert.assertEquals;
import gov.nih.nci.caintegrator2.application.study.AbstractTestDataGenerator;
import gov.nih.nci.caintegrator2.application.study.PermissibleValueGenerator;
import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator2.domain.annotation.PermissibleValue;
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.SelectedValueCriterion;

import java.util.HashSet;
import java.util.Set;

/**
 * Gets called by the QueryResultTestIntegration through the CompoundCriterionGenerator.
 */
public final class SelectedValueCriterionGenerator extends AbstractTestDataGenerator<SelectedValueCriterion> {

    public static final SelectedValueCriterionGenerator INSTANCE = new SelectedValueCriterionGenerator();
    
    private SelectedValueCriterionGenerator() { 
        super();
    }
    @Override
    public void compareFields(SelectedValueCriterion original, SelectedValueCriterion retrieved) {
        assertEquals(original.getId(), retrieved.getId());
        assertEquals(original.getAnnotationFieldDescriptor(), retrieved.getAnnotationFieldDescriptor());
        assertEquals(original.getValueCollection().size(), retrieved.getValueCollection().size());
    }

    @Override
    public SelectedValueCriterion createPersistentObject() {
        return new SelectedValueCriterion();
    }

    @Override
    public void setValues(SelectedValueCriterion selectedValueCriterion, Set<AbstractCaIntegrator2Object> nonCascadedObjects) {
        selectedValueCriterion.setEntityType(EntityTypeEnum.SUBJECT);
        selectedValueCriterion.setValueCollection(new HashSet<PermissibleValue>());

        PermissibleValue stringVal = PermissibleValueGenerator.INSTANCE.createPersistentObject();
        PermissibleValueGenerator.INSTANCE.setValues(stringVal, nonCascadedObjects);
                
        selectedValueCriterion.getValueCollection().add(stringVal);
    }

}
