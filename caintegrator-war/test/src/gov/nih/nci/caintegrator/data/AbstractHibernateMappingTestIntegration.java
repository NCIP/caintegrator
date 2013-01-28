/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.data;

import gov.nih.nci.caintegrator.application.study.AbstractTestDataGenerator;
import gov.nih.nci.caintegrator.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator.domain.AbstractCaIntegrator2Object;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.SessionFactory;
import org.junit.Test;
import org.springframework.test.AbstractTransactionalSpringContextTests;

/**
 * Provides testing of the ORM persistence of classes, including the ORM mapping and the underlying table. To test a persistence
 * mapping, create a subclass parameterized to the type being tested and implement getDataGenerator to return a data generator
 * instance that handles that type.
 * 
 * <p>Persistence checking is done in the testMapping method which tests insert, retrieval, update and retrieval of the updated object.
 */
public abstract class AbstractHibernateMappingTestIntegration<T> extends AbstractTransactionalSpringContextTests {
    
    private CaIntegrator2Dao caIntegrator2Dao;
    private SessionFactory sessionFactory;
    private AbstractTestDataGenerator<T> dataGenerator;
    
    protected String[] getConfigLocations() {
        return new String[] {"classpath*:/**/dao-test-config.xml"};
    }
    
    @SuppressWarnings("unchecked")
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
        T retrieved = (T) getCaIntegrator2Dao().get(id, original.getClass());
        compare(original, retrieved);
        setValues(retrieved, nonCascadedObjects);
        saveObject(retrieved, nonCascadedObjects);
        T retrieved2 = (T) getCaIntegrator2Dao().get(id, original.getClass());
        compare(retrieved, retrieved2);
    }

    abstract protected AbstractTestDataGenerator<T> getDataGenerator();

    private void saveObject(T object, Set<AbstractCaIntegrator2Object> nonCascadedObjects) {
        for (AbstractCaIntegrator2Object nonCascadedObject : nonCascadedObjects) {
            if (nonCascadedObject.getId() == null) {
                getSessionFactory().getCurrentSession().saveOrUpdate(nonCascadedObject);
            }
        }
        nonCascadedObjects.clear();
        getCaIntegrator2Dao().save(object);
        getSessionFactory().getCurrentSession().flush();
        getSessionFactory().getCurrentSession().clear();
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

    public CaIntegrator2Dao getCaIntegrator2Dao() {
        return caIntegrator2Dao;
    }

    public void setCaIntegrator2Dao(CaIntegrator2Dao caIntegrator2Dao) {
        this.caIntegrator2Dao = caIntegrator2Dao;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

}
