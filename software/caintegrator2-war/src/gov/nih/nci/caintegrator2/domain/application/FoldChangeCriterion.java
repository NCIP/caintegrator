package gov.nih.nci.caintegrator2.domain.application;

import java.util.HashSet;
import java.util.Set;

import gov.nih.nci.caintegrator2.domain.genomic.Sample;

/**
 * 
 */
public class FoldChangeCriterion extends AbstractGenomicCriterion {

    private static final long serialVersionUID = 1L;
    
    private Float folds;
    private RegulationTypeEnum regulationType;
    private String geneSymbol;
    private Set<Sample> compareToSamples = new HashSet<Sample>();
    
    /**
     * @return the folds
     */
    public Float getFolds() {
        return folds;
    }
    
    /**
     * @param folds the folds to set
     */
    public void setFolds(Float folds) {
        this.folds = folds;
    }
    
    /**
     * @return the compareToSamples
     */
    public Set<Sample> getCompareToSamples() {
        return compareToSamples;
    }

    @SuppressWarnings("unused") // necessary for Hibernate
    private void setCompareToSamples(Set<Sample> compareToSamples) {
        this.compareToSamples = compareToSamples;
    }

    /**
     * @return the geneSymbol
     */
    public String getGeneSymbol() {
        return geneSymbol;
    }

    /**
     * @param geneSymbol the geneSymbol to set
     */
    public void setGeneSymbol(String geneSymbol) {
        this.geneSymbol = geneSymbol;
    }

    /**
     * @return the regulationType
     */
    public RegulationTypeEnum getRegulationType() {
        return regulationType;
    }

    /**
     * @param regulationType the regulationType to set
     */
    public void setRegulationType(RegulationTypeEnum regulationType) {
        this.regulationType = regulationType;
    }

}