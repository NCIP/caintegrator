package gov.nih.nci.caintegrator2.domain.application;


import gov.nih.nci.caintegrator2.application.query.GenomicCriteriaMatchTypeEnum;
import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;

/**
 * 
 */
public class GenomicDataResultValue extends AbstractCaIntegrator2Object {

    private static final long serialVersionUID = 1L;
    private static final String HIGH_VARIANCE_APPEND_STRING = "**";
    private Float value;
    private GenomicDataResultColumn column;
    private GenomicCriteriaMatchTypeEnum criteriaMatchType = GenomicCriteriaMatchTypeEnum.NO_MATCH;
    private boolean highVariance = false;
    
    /**
     * 
     * @return displayable result value.
     */
    public String getDisplayableValue() {
        return value + (highVariance ? HIGH_VARIANCE_APPEND_STRING : "");
    }
    
    /**
     * @return the value
     */
    public Float getValue() {
        return value;
    }
    
    /**
     * @param value the value to set
     */
    public void setValue(Float value) {
        this.value = value;
    }
    
    /**
     * @return the column
     */
    public GenomicDataResultColumn getColumn() {
        return column;
    }
    
    /**
     * @param column the column to set
     */
    public void setColumn(GenomicDataResultColumn column) {
        this.column = column;
    }

    /**
     * @return the meetsCriterion
     */
    public boolean isMeetsCriterion() {
        return !GenomicCriteriaMatchTypeEnum.NO_MATCH.equals(criteriaMatchType);
    }

    
    /**
     * For the JSP to get the color to highlight based on the value.
     * @return highlight color.
     */
    public String getHighlightColor() {
        if (GenomicCriteriaMatchTypeEnum.MATCH_POSITIVE_OR_NEGATIVE.equals(criteriaMatchType)) {
            return value >= 0 ? GenomicCriteriaMatchTypeEnum.OVER.getHighlightColor() 
                                    : GenomicCriteriaMatchTypeEnum.UNDER.getHighlightColor();    
        }
        return criteriaMatchType.getHighlightColor();
    }

    /**
     * @return the highVariance
     */
    public boolean isHighVariance() {
        return highVariance;
    }

    /**
     * @param highVariance the highVariance to set
     */
    public void setHighVariance(boolean highVariance) {
        this.highVariance = highVariance;
    }

    /**
     * @return the criteriaMatchType
     */
    public GenomicCriteriaMatchTypeEnum getCriteriaMatchType() {
        return criteriaMatchType;
    }

    /**
     * @param criteriaMatchType the criteriaMatchType to set
     */
    public void setCriteriaMatchType(GenomicCriteriaMatchTypeEnum criteriaMatchType) {
        this.criteriaMatchType = criteriaMatchType;
    }

}