package gov.nih.nci.caintegrator2.domain.application;

import gov.nih.nci.caintegrator2.domain.genomic.SampleSet;

/**
 * 
 */
public class FoldChangeCriterion extends AbstractGenomicCriterion implements Cloneable {

    private static final long serialVersionUID = 1L;
    
    private Float foldsUp;
    private Float foldsDown;
    private RegulationTypeEnum regulationType;
    private String geneSymbol;
    private SampleSet compareToSampleSet = new SampleSet();
    

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

    /**
     * {@inheritDoc}
     */
    protected FoldChangeCriterion clone() throws CloneNotSupportedException {
        FoldChangeCriterion clone = (FoldChangeCriterion) super.clone();
        clone.setCompareToSampleSet(this.compareToSampleSet);
        return clone;
    }

    /**
     * @return the compareToSampleSet
     */
    public SampleSet getCompareToSampleSet() {
        return compareToSampleSet;
    }

    /**
     * @param compareToSampleSet the compareToSampleSet to set
     */
    public void setCompareToSampleSet(SampleSet compareToSampleSet) {
        this.compareToSampleSet = compareToSampleSet;
    }

}