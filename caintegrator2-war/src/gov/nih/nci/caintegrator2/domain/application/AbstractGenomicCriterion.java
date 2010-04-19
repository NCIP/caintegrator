package gov.nih.nci.caintegrator2.domain.application;

import java.util.List;

/**
 * 
 */
public abstract class AbstractGenomicCriterion extends AbstractCriterion implements Cloneable {

    private static final long serialVersionUID = 1L;

    /**
     * {@inheritDoc}
     */
    protected AbstractGenomicCriterion clone() throws CloneNotSupportedException {
        return (AbstractGenomicCriterion) super.clone();
    }
    
    /**
     * 
     * @return the platformName.
     */
    public abstract String getPlatformName();
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected abstract List<String> getGeneSymbolsInCriterion();

}