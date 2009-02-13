package gov.nih.nci.caintegrator2.domain.application;

import java.util.Collection;
import java.util.HashSet;

/**
 * 
 */
public class CompoundCriterion extends AbstractCriterion implements Cloneable {

    private static final long serialVersionUID = 1L;
    
    private String booleanOperator;
    private Collection<AbstractCriterion> criterionCollection;
    
    /**
     * @return the booleanOperator
     */
    public String getBooleanOperator() {
        return booleanOperator;
    }
    
    /**
     * @param booleanOperator the booleanOperator to set
     */
    public void setBooleanOperator(String booleanOperator) {
        this.booleanOperator = booleanOperator;
    }
    
    /**
     * @return the criterionCollection
     */
    public Collection<AbstractCriterion> getCriterionCollection() {
        return criterionCollection;
    }
    
    /**
     * @param criterionCollection the criterionCollection to set
     */
    public void setCriterionCollection(Collection<AbstractCriterion> criterionCollection) {
        this.criterionCollection = criterionCollection;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected CompoundCriterion clone() throws CloneNotSupportedException {
        CompoundCriterion clone = (CompoundCriterion) super.clone();
        clone.setCriterionCollection(cloneCriterionCollection());
        return clone;
    }
    
    private Collection<AbstractCriterion> cloneCriterionCollection() throws CloneNotSupportedException {
        Collection<AbstractCriterion> clones = new HashSet<AbstractCriterion>();
        for (AbstractCriterion abstractCriterion : criterionCollection) {
            clones.add((AbstractCriterion) abstractCriterion.clone());
        }
        return clones;
    }
}