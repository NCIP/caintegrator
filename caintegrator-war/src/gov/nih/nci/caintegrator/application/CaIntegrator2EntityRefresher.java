/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.application;

import gov.nih.nci.caintegrator.domain.AbstractCaIntegrator2Object;

/**
 * Interface for objects that need to refresh objects.
 */
public interface CaIntegrator2EntityRefresher {

    /**
     * Returns the refreshed entity attached to the current Hibernate session.
     *
     * @param <T> type of object being returned.
     * @param entity a persistent entity with the id set.
     * @return the refreshed entity.
     */
    <T extends AbstractCaIntegrator2Object> T getRefreshedEntity(T entity);

}
