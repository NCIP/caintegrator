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
    private Integer callsValue;
    private Float probabilityAmplification;
    private Float probabilityGain;
    private Float probabilityLoss;
    private Float probabilityNormal;

    private GenomicDataResultColumn column;
    private GenomicCriteriaMatchTypeEnum criteriaMatchType = GenomicCriteriaMatchTypeEnum.NO_MATCH;
    private boolean highVariance = false;
    
    /**
     * @return the callsValue
     */
    public Integer getCallsValue() {
        return callsValue;
    }

    /**
     * @param callsValue the callsValue to set
     */
    public void setCallsValue(Integer callsValue) {
        this.callsValue = callsValue;
    }

    /**
     * @return the probabilityAmplification
     */
    public Float getProbabilityAmplification() {
        return probabilityAmplification;
    }

    /**
     * @param probabilityAmplification the probabilityAmplification to set
     */
    public void setProbabilityAmplification(Float probabilityAmplification) {
        this.probabilityAmplification = probabilityAmplification;
    }

    /**
     * @return the probabilityGain
     */
    public Float getProbabilityGain() {
        return probabilityGain;
    }

    /**
     * @param probabilityGain the probabilityGain to set
     */
    public void setProbabilityGain(Float probabilityGain) {
        this.probabilityGain = probabilityGain;
    }

    /**
     * @return the probabilityLoss
     */
    public Float getProbabilityLoss() {
        return probabilityLoss;
    }

    /**
     * @param probabilityLoss the probabilityLoss to set
     */
    public void setProbabilityLoss(Float probabilityLoss) {
        this.probabilityLoss = probabilityLoss;
    }

    /**
     * @return the probabilityNormal
     */
    public Float getProbabilityNormal() {
        return probabilityNormal;
    }

    /**
     * @param probabilityNormal the probabilityNormal to set
     */
    public void setProbabilityNormal(Float probabilityNormal) {
        this.probabilityNormal = probabilityNormal;
    }
    
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
     * @return color the calls value
     */
    public boolean isColorCalls() {
        return callsValue != 0;
    }
    
    /**
     * For the JSP to get the color to highlight based on the calls value.
     * @return highlight color for the Calls value.
     */
    public String getHighlightColorCalls() {
        return callsValue > 0 ? GenomicCriteriaMatchTypeEnum.OVER.getHighlightColor() 
                                    : GenomicCriteriaMatchTypeEnum.UNDER.getHighlightColor();   
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