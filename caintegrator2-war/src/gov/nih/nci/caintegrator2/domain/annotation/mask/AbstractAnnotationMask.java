/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.domain.annotation.mask;

import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;

/**
 * Masks are used by a Study manager to hide the actual annotation value from the user and convert it to
 * an appropriate viewable value.
 */
public abstract class AbstractAnnotationMask extends AbstractCaIntegrator2Object implements
        Comparable<AbstractAnnotationMask> {
    
    /**
     * Default serialize.
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * The order with which to apply the mask.
     * @return sort order.
     */
    public abstract Integer getMaskOrder();
    
    /**
     * 
     * @return displayable restriction for this mask.
     */
    public abstract String getDisplayableRestriction();
    
    /**
     * {@inheritDoc}
     */
    public int compareTo(AbstractAnnotationMask o) {
        return getMaskOrder().compareTo(o.getMaskOrder());
    }

}
