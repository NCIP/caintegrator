package gov.nih.nci.caintegrator2.domain.application;

import java.util.HashSet;
import java.util.Set;

import gov.nih.nci.caintegrator2.domain.genomic.Sample;

/**
 * 
 */
public class FoldChangeCriterion extends AbstractGenomicCriterion {

    private static final long serialVersionUID = 1L;
    
    private Float foldsUp;
    private Float foldsDown;
    private RegulationTypeEnum regulationType;
    private String geneSymbol;
    private Set<Sample> compareToSamples = new HashSet<Sample>();
    
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

    /**
     * @return the foldsUp
     */
    public Float getFoldsUp() {
        return foldsUp;
    }

    /**
     * @param foldsUp the foldsUp to set
     */
    public void setFoldsUp(Float foldsUp) {
        this.foldsUp = foldsUp;
    }

    /**
     * @return the foldsDown
     */
    public Float getFoldsDown() {
        return foldsDown;
    }

    /**
     * @param foldsDown the foldsDown to set
     */
    public void setFoldsDown(Float foldsDown) {
        this.foldsDown = foldsDown;
    }

}