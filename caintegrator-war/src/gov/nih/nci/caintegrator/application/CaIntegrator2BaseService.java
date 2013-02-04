/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application;

import gov.nih.nci.caintegrator.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator.domain.AbstractCaIntegrator2Object;

import org.springframework.transaction.annotation.Transactional;

import com.fiveamsolutions.nci.commons.util.CGLIBUtils;

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
    public <T extends AbstractCaIntegrator2Object> T getRefreshedEntity(T entity) {
        Long id = entity.getId();
        if (id == null) {
            throw new IllegalArgumentException("Id was null");
        }
        return (T) dao.get(id, CGLIBUtils.unEnhanceCBLIBClass(entity.getClass()));
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
