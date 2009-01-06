package gov.nih.nci.caintegrator2.domain.application;

/**
 * 
 */
public class FoldChangeCriterion extends AbstractGenomicCriterion {

    private static final long serialVersionUID = 1L;
    
    private Float folds;
    private String regulationType;
    private SampleList compareToSamples;
    
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
     * @return the regulationType
     */
    public String getRegulationType() {
        return regulationType;
    }
    
    /**
     * @param regulationType the regulationType to set
     */
    public void setRegulationType(String regulationType) {
        this.regulationType = regulationType;
    }
    
    /**
     * @return the compareToSamples
     */
    public SampleList getCompareToSamples() {
        return compareToSamples;
    }
    
    /**
     * @param compareToSamples the compareToSamples to set
     */
    public void setCompareToSamples(SampleList compareToSamples) {
        this.compareToSamples = compareToSamples;
    }

}