package gov.nih.nci.caintegrator2.domain.application;


import gov.nih.nci.caintegrator2.application.query.InvalidCriterionException;

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

    private static final String ALL_GENES = "-AllGenes-";
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
     * Determines if there exists any masked criteria in this compound criterion.
     * @return boolean if there are any masked criteria.
     */
    public boolean isHasMaskedCriteria() {
        for (AbstractCriterion criterion : getCriterionCollection()) {
            if (criterion.isMaskedCriterion()) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isMaskedCriterion() {
        return isHasMaskedCriteria();
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
     * @return Returns all gene symbols for all criterion under this compound criterion.
     */
    public Set<String> getAllSubjectIds() {
        Set<String> subjectIdsInQuery = new HashSet<String>();
        for (AbstractCriterion criterion : getCriterionCollection()) {
            subjectIdsInQuery.addAll(criterion.getSubjectIdentifiers());
        }
        
        return subjectIdsInQuery;
    }
    
    /**
     * @param genomicCriterionType the criterion type to get the platforms for.
     * @return returns all platform names for all criterion under this compound criterion.
     */
    public Set<String> getAllPlatformNames(GenomicCriterionTypeEnum genomicCriterionType) {
        Set<String> platformNames = new HashSet<String>();
        for (AbstractCriterion criterion : getCriterionCollection()) {
            if (criterion instanceof CompoundCriterion) {
                platformNames.addAll(((CompoundCriterion) criterion).getAllPlatformNames(genomicCriterionType));
            } else {
                if (StringUtils.isNotBlank(criterion.getPlatformName(genomicCriterionType))) {
                    platformNames.add(criterion.getPlatformName(genomicCriterionType));
                }
            }
        }
        return platformNames;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getPlatformName(GenomicCriterionTypeEnum genomicCriterionType) {
        return null;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected Set<String> getSubjectIdentifiers() {
        return getAllSubjectIds();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getGeneSymbolsInCriterion() {
        return getAllGeneSymbols();
    }

    /**
     * Validate GeneExpression criterion.
     * @throws InvalidCriterionException when not valid
     */
    public void validateGeneExpressionCriterion() throws InvalidCriterionException {
        boolean isFoldChange = false;
        boolean isGeneName = false;
        List<String> geneSymbols = new ArrayList<String>();
        for (AbstractCriterion criterion : getCriterionCollection()) {
            if (criterion instanceof FoldChangeCriterion) {
                isFoldChange = true;
                validateDuplicateGeneSymbol(geneSymbols, criterion);
                validateMixedCriterion(isFoldChange, isGeneName);
            } else if (criterion instanceof GeneNameCriterion) {
                isGeneName = true;
                validateMixedCriterion(isFoldChange, isGeneName);
            }
        }
    }

    private void validateMixedCriterion(boolean isFoldChange, boolean isGeneName) throws InvalidCriterionException {
        if (isFoldChange && isGeneName) {
            throw new InvalidCriterionException("Gene name and Fold change criterion can't be mixed.");
        }
    }

    private void validateDuplicateGeneSymbol(List<String> geneSymbols,
            AbstractCriterion criterion) throws InvalidCriterionException {
        List<String> criterionGeneSymbols = getGeneSymbols(criterion.getGeneSymbolsInCriterion());
        for (String geneSymbol : criterionGeneSymbols) {
            checkDuplicateGene(geneSymbols, geneSymbol);
        }
        geneSymbols.addAll(criterionGeneSymbols);
    }

    private void checkDuplicateGene(List<String> geneSymbols, String geneSymbol)
    throws InvalidCriterionException {
        if (geneSymbols.contains(geneSymbol)) {
            throw new InvalidCriterionException("Gene symbol '" + geneSymbol
                    + "' is in more than 1 Fold Change criterion.");
        } else if (geneSymbols.contains(ALL_GENES)
                || (ALL_GENES.equals(geneSymbol) && !geneSymbols.isEmpty())) {
            throw new InvalidCriterionException("All Genes can't be mixed in different Fold Change criterion.");
        }
    }

    private List<String> getGeneSymbols(List<String> geneSymbolsInCriterion) {
        List<String> geneSymbols = new ArrayList<String>();
        if (geneSymbolsInCriterion.isEmpty()) {
            geneSymbols.add(ALL_GENES);
        } else {
            geneSymbols.addAll(geneSymbolsInCriterion);
        }
        return geneSymbols;
    }
}