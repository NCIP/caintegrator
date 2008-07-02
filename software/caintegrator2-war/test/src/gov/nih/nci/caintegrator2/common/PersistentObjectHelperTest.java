package gov.nih.nci.caintegrator2.common;

import static org.junit.Assert.*;

import org.junit.Test;

public class PersistentObjectHelperTest {

    @Test
    @SuppressWarnings("PMD")
    public void testEqualsPersistentObjectObject() {
        PersistentObject id1 = create(1L);
        PersistentObject anotherId1 = create(1L);
        PersistentObject id2 = create(2L);
        PersistentObject nullId = create(null);
        PersistentObject anotherNullId = create(null);
        Object differentClass = new Object();
        assertEquals(id1, id1);
        assertFalse(id1.equals(null));
        assertFalse(id1.equals(id2));
        assertEquals(id1, anotherId1);
        assertEquals(nullId, nullId);
        assertFalse(nullId.equals(null));
        assertFalse(nullId.equals(anotherNullId));
        assertFalse(id1.equals(differentClass));
    }

    @Test
    public void testHashCodePersistentObject() {
        PersistentObject id1 = create(1L);
        PersistentObject nullId = create(null);
        assertEquals(1, id1.hashCode());
        assertEquals(System.identityHashCode(nullId), nullId.hashCode());
    }
    
    private PersistentObject create(final Long id) {
        return new PersistentObject() {

            public Long getId() {
                return id;
            }

            public void setId(Long id) {
                //no-op
            }

            @Override
            public int hashCode() {
                return PersistentObjectHelper.hashCode(this);
            }

            @Override
            public boolean equals(Object obj) {
                return PersistentObjectHelper.equals(this, obj);
            }
            
        };
    }

}
