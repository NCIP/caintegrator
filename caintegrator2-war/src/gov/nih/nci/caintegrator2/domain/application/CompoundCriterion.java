package gov.nih.nci.caintegrator2.domain.application;

import java.util.Collection;

/**
 * 
 */
public class CompoundCriterion extends AbstractCriterion {

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

}