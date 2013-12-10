/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class AbstractCaIntegrator2ObjectTest {

    @Test
    public void testEqualsAbstractCaIntegrator2ObjectObject() {
        AbstractCaIntegrator2Object id1 = create(1L);
        AbstractCaIntegrator2Object anotherId1 = create(1L);
        AbstractCaIntegrator2Object id2 = create(2L);
        AbstractCaIntegrator2Object nullId = create(null);
        AbstractCaIntegrator2Object anotherNullId = create(null);
        Object differentClass = new Object();
        assertEquals(id1, id1);
        assertNotNull(id1);
        assertFalse(id1.equals(id2));
        assertEquals(id1, anotherId1);
        assertEquals(nullId, nullId);
        assertNotNull(nullId);
        assertFalse(nullId.equals(anotherNullId));
        assertFalse(id1.equals(differentClass));
    }

    @Test
    public void testHashCodeAbstractCaIntegrator2Object() {
        AbstractCaIntegrator2Object id1 = create(1L);
        AbstractCaIntegrator2Object nullId = create(null);
        assertEquals(1, id1.hashCode());
        assertEquals(System.identityHashCode(nullId), nullId.hashCode());
    }

    private AbstractCaIntegrator2Object create(final Long id) {
        AbstractCaIntegrator2Object object =  new AbstractCaIntegrator2Object() {
            private static final long serialVersionUID = 1L;
        };
        object.setId(id);
        return object;
    }
}
