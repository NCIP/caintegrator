/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.domain.application;


import gov.nih.nci.caintegrator.application.query.GenomicCriteriaMatchTypeEnum;

/**
 * Holder for genomic data result values.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
public class GenomicDataResultValue {
    private static final String HIGH_VARIANCE_APPEND_STRING = "**";
    private float value;
    private int callsValue;
    private float probabilityAmplification;
    private float probabilityGain;
    private float probabilityLoss;
    private float probabilityNormal;

    private GenomicDataResultColumn column;
    private GenomicCriteriaMatchTypeEnum criteriaMatchType = GenomicCriteriaMatchTypeEnum.NO_MATCH;
    private boolean highVariance = false;

    /**
     * @return the callsValue
     */
    public int getCallsValue() {
        return callsValue;
    }

    /**
     * @param callsValue the callsValue to set
     */
    public void setCallsValue(int callsValue) {
        this.callsValue = callsValue;
    }

    /**
     * @return the probabilityAmplification
     */
    public float getProbabilityAmplification() {
        return probabilityAmplification;
    }

    /**
     * @param probabilityAmplification the probabilityAmplification to set
     */
    public void setProbabilityAmplification(float probabilityAmplification) {
        this.probabilityAmplification = probabilityAmplification;
    }

    /**
     * @return the probabilityGain
     */
    public float getProbabilityGain() {
        return probabilityGain;
    }

    /**
     * @param probabilityGain the probabilityGain to set
     */
    public void setProbabilityGain(float probabilityGain) {
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
    public float getProbabilityNormal() {
        return probabilityNormal;
    }

    /**
     * @param probabilityNormal the probabilityNormal to set
     */
    public void setProbabilityNormal(float probabilityNormal) {
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
    public float getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(float value) {
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
