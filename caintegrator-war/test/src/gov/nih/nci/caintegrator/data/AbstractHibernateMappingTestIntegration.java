/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.data;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import gov.nih.nci.caintegrator.application.study.AbstractTestDataGenerator;
import gov.nih.nci.caintegrator.domain.AbstractCaIntegrator2Object;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Provides testing of the ORM persistence of classes, including the ORM mapping and the underlying table.
 * To test a persistence mapping, create a subclass parameterized to the type being tested and implement
 * getDataGenerator to return a data generator instance that handles that type.
 *
 * <p>Persistence checking is done in the testMapping method which tests insert, retrieval, update and retrieval of
 * the updated object.
 *
 * @param <T> the entity class
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:integration-test-config.xml")
@Transactional
public abstract class AbstractHibernateMappingTestIntegration<T> {
    @Autowired
    private CaIntegrator2Dao caIntegrator2Dao;
    @Autowired
    private SessionFactory sessionFactory;
    private AbstractTestDataGenerator<T> dataGenerator;


    @Test
    public void testMapping() {
        Set<AbstractCaIntegrator2Object> nonCascadedObjects = new HashSet<AbstractCaIntegrator2Object>();
        dataGenerator = getDataGenerator();
        T original = createPersistentObject();
        setValues(original, nonCascadedObjects);
        assertNull(getId(original));
        saveObject(original, nonCascadedObjects);
        Long id = getId(original);
        assertNotNull(id);
        T retrieved = (T) caIntegrator2Dao.get(id, original.getClass());
        compare(original, retrieved);
        setValues(retrieved, nonCascadedObjects);
        saveObject(retrieved, nonCascadedObjects);
        T retrieved2 = (T) caIntegrator2Dao.get(id, original.getClass());
        compare(retrieved, retrieved2);
    }

    protected abstract AbstractTestDataGenerator<T> getDataGenerator();

    private void saveObject(T object, Set<AbstractCaIntegrator2Object> nonCascadedObjects) {
        for (AbstractCaIntegrator2Object nonCascadedObject : nonCascadedObjects) {
            if (nonCascadedObject.getId() == null) {
                sessionFactory.getCurrentSession().saveOrUpdate(nonCascadedObject);
            }
        }
        nonCascadedObjects.clear();
        caIntegrator2Dao.save(object);
        sessionFactory.getCurrentSession().flush();
        sessionFactory.getCurrentSession().clear();
    }

    private void compare(T original, T retrieved) {
        dataGenerator.compareFields(original, retrieved);
    }

    private void setValues(T object, Set<AbstractCaIntegrator2Object> nonCascadedObjects) {
        dataGenerator.setValues(object, nonCascadedObjects);
    }

    private T createPersistentObject() {
        return dataGenerator.createPersistentObject();
    }

    private Long getId(T object) {
        return dataGenerator.getId(object);
    }
}
