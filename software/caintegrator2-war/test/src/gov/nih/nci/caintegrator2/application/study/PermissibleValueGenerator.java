/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.study;

import static org.junit.Assert.assertEquals;
import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator2.domain.annotation.PermissibleValue;

import java.util.Set;


public final class PermissibleValueGenerator extends AbstractTestDataGenerator<PermissibleValue> {

    public static final PermissibleValueGenerator INSTANCE = new PermissibleValueGenerator();
    
    private PermissibleValueGenerator() { 
        super();
    }
    @Override
    public void compareFields(PermissibleValue original, PermissibleValue retrieved) {
        assertEquals(original.getId(), retrieved.getId());
        assertEquals(original.getValue(), retrieved.getValue());
    }

    @Override
    public PermissibleValue createPersistentObject() {
        return new PermissibleValue();
    }

    @Override
    public void setValues(PermissibleValue permissibleValue, Set<AbstractCaIntegrator2Object> nonCascadedObjects) {
        permissibleValue.setValue(getUniqueString());
    }

}
