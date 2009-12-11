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