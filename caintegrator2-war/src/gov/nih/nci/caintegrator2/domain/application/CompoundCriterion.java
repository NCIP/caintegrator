package gov.nih.nci.caintegrator2.domain.application;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.xwork.StringUtils;

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
    
    /**
     * @return Returns all gene symbols for all criterion under this compound criterion.
     */
    public List<String> getAllGeneSymbols() {
        List<String> geneSymbolsInQuery = new ArrayList<String>();
        for (AbstractCriterion criterion : getCriterionCollection()) {
            geneSymbolsInQuery.addAll(criterion.getGeneSymbolsInCriterion());
        }
        
        return geneSymbolsInQuery;
    }
    
    /**
     * 
     * @return returns all platform names for all criterion under this compound criterion.
     */
    public Set<String> getAllPlatformNames() {
        Set<String> platformNames = new HashSet<String>();
        for (AbstractCriterion criterion : getCriterionCollection()) {
            if (criterion instanceof CompoundCriterion) {
                platformNames.addAll(((CompoundCriterion) criterion).getAllPlatformNames());
            } else {
                if (StringUtils.isNotBlank(criterion.getPlatformName())) {
                    platformNames.add(criterion.getPlatformName());
                }
            }
        }
        return platformNames;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<String> getGeneSymbolsInCriterion() {
        return getAllGeneSymbols();
    }
}