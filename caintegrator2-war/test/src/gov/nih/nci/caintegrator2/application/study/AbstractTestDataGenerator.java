/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.application.study;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * Base class for test data generators. These provide support for creating test objects and comparing two test objects. These were
 * designed principally to test persistence, but may be applicable whenever test data is necessary. 
 * 
 * <p>Subclasses must provide implementations of:
 * 
 * <ul>
 *  <li><code>createPersistentObject</code>: create an instance of the class handled by the generator.</li>
 *  <li><code>setValues</code>: set a unique or changed set of values on all fields within the instance.</li>
 *  <li><code>compareFields</code>: compare all the fields between two instances asserting that they are equal..</li>
 * </ul>
 */
public abstract class AbstractTestDataGenerator<T> {
    
    private static int uniqueInt;
    
    public final void compare(T original, T retrieved) {
        if (original == null) {
            assertNull(retrieved);
        } else {
            assertEquals(original, retrieved);
        }
        if (getId(original) != null) {
            assertEquals(getId(original), getId(retrieved));
        }
        compareFields(original, retrieved);
    }

    public Long getId(T object) {
        try {
            return (Long) object.getClass().getMethod("getId").invoke(object);
        } catch (Exception e) {
            throw new IllegalArgumentException("Object doesn't have a getId method", e);
        }
    }

    public abstract void compareFields(T original, T retrieved);

    public abstract void setValues(T object, Set<AbstractCaIntegrator2Object> nonCascadedObjects);

    public abstract T createPersistentObject();

    public T createPopulatedPersistentObject(Set <AbstractCaIntegrator2Object> nonCascadedObjects) {
        T object = createPersistentObject();
        setValues(object, nonCascadedObjects);
        return object;
    }

    protected String getUniqueString() {
        return String.valueOf(getUniqueInt());
    }

    protected String getUniqueString(int maxLength) {
        String value = getUniqueString();
        if (value.length() > maxLength) {
            return value.substring(value.length() - maxLength);
        } else {
            return value;
        }
    }

    protected int getUniqueInt() {
        return uniqueInt++;
    }
    
    protected float getUniqueFloat() {
        return (float) getUniqueInt();
    }

    protected Character getUniqueChar() {
        return (char) ((getUniqueInt() % 50) + 50);
    }

    @SuppressWarnings("hiding")
    protected <T extends Enum<?>> T getNewEnumValue(T enumValue, T[] values) {
        if (enumValue == null || enumValue.ordinal() == (values.length - 1)) {
            return values[0];
        } else {
            return values[enumValue.ordinal() + 1];
        }
    }

    @SuppressWarnings("hiding")
    protected <T> void compareCollections(Collection<T> originalCollection, Collection<T> retrievedCollection, AbstractTestDataGenerator<T> generator) {
        assertEquals(originalCollection.size(), retrievedCollection.size());
        for (Iterator<T> retrievedIterator = retrievedCollection.iterator(); retrievedIterator.hasNext();) {
            T retrieved = retrievedIterator.next();
            T original = getOriginal(originalCollection, retrieved);
            generator.compare(original, retrieved);
        }
    }

    @SuppressWarnings("hiding")
    private <T> T getOriginal(Collection<T> originalCollection, T retrieved) {
        for (T original : originalCollection) {
            if (original.equals(retrieved)) {
                return original;
            }
        }
        return null;
    }

    protected Boolean getChangedBoolean(Boolean currentValue) {
        return currentValue == null ? true : !currentValue;
    }

    protected double getUniqueDouble() {
        return getUniqueFloat();
    }

}
