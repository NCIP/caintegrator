package gov.nih.nci.caintegrator2.domain.application;


/**
 * 
 */
public class ExpressionLevelCriterion extends AbstractGenomicCriterion implements Cloneable {

    private static final long serialVersionUID = 1L;
    
    private Float lowerLimit; // values should be >= this number
    private Float upperLimit; // values should be <= this number
    private RangeTypeEnum rangeType;
    
    
    /**
     * @return the lowerLimit
     */
    public Float getLowerLimit() {
        return lowerLimit;
    }

    /**
     * @param lowerLimit the lowerLimit to set
     */
    public void setLowerLimit(Float lowerLimit) {
        this.lowerLimit = lowerLimit;
    }

    /**
     * @return the upperLimit
     */
    public Float getUpperLimit() {
        return upperLimit;
    }

    /**
     * @param upperLimit the upperLimit to set
     */
    public void setUpperLimit(Float upperLimit) {
        this.upperLimit = upperLimit;
    }

    /**
     * @return the rangeType
     */
    public RangeTypeEnum getRangeType() {
        return rangeType;
    }

    /**
     * @param rangeType the rangeType to set
     */
    public void setRangeType(RangeTypeEnum rangeType) {
        this.rangeType = rangeType;
    }

    /**
     * {@inheritDoc}
     */
    protected ExpressionLevelCriterion clone() throws CloneNotSupportedException {
        return (ExpressionLevelCriterion) super.clone();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getPlatformName(GenomicCriterionTypeEnum genomicCriterionType) {
        if (GenomicCriterionTypeEnum.GENE_EXPRESSION.equals(genomicCriterionType)) {
            return getPlatformName();
        }
        return null;
    }
}