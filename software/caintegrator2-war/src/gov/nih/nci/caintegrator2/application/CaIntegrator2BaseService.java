/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application;

import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;

import org.springframework.transaction.annotation.Transactional;

/**
 * Base service class for CaIntegrator2 Services.
 */
public class CaIntegrator2BaseService {
    
    private CaIntegrator2Dao dao;
    
    /**
     * Returns the refreshed entity attached to the current Hibernate session.
     * 
     * @param <T> type of object being returned.
     * @param entity a persistent entity with the id set.
     * @return the refreshed entity.
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public <T> T getRefreshedEntity(T entity) {
        Long id;
        try {
            id = (Long) entity.getClass().getMethod("getId").invoke(entity);
        } catch (Exception e) {
            throw new IllegalArgumentException("Entity doesn't have a getId() method", e);
        }
        if (id == null) {
            throw new IllegalArgumentException("Id was null");
        }
        return (T) dao.get(id, entity.getClass());
    }

    /**
     * @return the dao
     */
    public CaIntegrator2Dao getDao() {
        return dao;
    }

    /**
     * @param dao the dao to set
     */
    public void setDao(CaIntegrator2Dao dao) {
        this.dao = dao;
    }
}
