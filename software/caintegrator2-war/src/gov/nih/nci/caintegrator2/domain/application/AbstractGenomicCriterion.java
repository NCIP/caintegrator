/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.domain.application;

/**
 * 
 */
public class AbstractGenomicCriterion extends AbstractCriterion implements Cloneable {

    private static final long serialVersionUID = 1L;

    /**
     * {@inheritDoc}
     */
    protected AbstractGenomicCriterion clone() throws CloneNotSupportedException {
        return (AbstractGenomicCriterion) super.clone();
    }

}
