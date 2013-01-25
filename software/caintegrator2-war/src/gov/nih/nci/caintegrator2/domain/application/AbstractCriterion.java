/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.domain.application;


import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;

/**
 * 
 */
public class AbstractCriterion extends AbstractCaIntegrator2Object implements Cloneable {

    private static final long serialVersionUID = 1L;

    /**
     * {@inheritDoc}
     */
    protected AbstractCriterion clone() throws CloneNotSupportedException {
        return (AbstractCriterion) super.clone();
    }
}
