/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.domain.application;


import java.util.Collection;
import java.util.HashSet;

/**
 * 
 */
public class CompoundCriterion extends AbstractCriterion implements Cloneable {

    private static final long serialVersionUID = 1L;
    
    private BooleanOperatorEnum booleanOperator;
    private Collection<AbstractCriterion> criterionCollection = new HashSet<AbstractCriterion>();
    
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
     * @return the booleanOperator
     */
    public BooleanOperatorEnum getBooleanOperator() {
        return booleanOperator;
    }

    /**
     * @param booleanOperator the booleanOperator to set
     */
    public void setBooleanOperator(BooleanOperatorEnum booleanOperator) {
        this.booleanOperator = booleanOperator;
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
