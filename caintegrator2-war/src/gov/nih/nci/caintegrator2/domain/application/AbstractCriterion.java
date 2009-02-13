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