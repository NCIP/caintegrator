/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.application.study;

import static org.junit.Assert.assertEquals;
import gov.nih.nci.caintegrator.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator.domain.annotation.PermissibleValue;

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
