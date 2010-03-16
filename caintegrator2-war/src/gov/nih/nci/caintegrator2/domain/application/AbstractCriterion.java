package gov.nih.nci.caintegrator2.domain.application;


import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 */
public abstract class AbstractCriterion extends AbstractCaIntegrator2Object implements Cloneable {

    private static final long serialVersionUID = 1L;

    /**
     * {@inheritDoc}
     */
    protected AbstractCriterion clone() throws CloneNotSupportedException {
        return (AbstractCriterion) super.clone();
    }

    /**
     * @return all gene symbols in this criterion.
     */
    protected List<String> getGeneSymbolsInCriterion() {
        return new ArrayList<String>();
    }
}