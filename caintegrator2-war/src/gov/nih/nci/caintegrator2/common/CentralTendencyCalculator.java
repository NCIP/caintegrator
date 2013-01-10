/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.common;

import gov.nih.nci.caintegrator2.application.study.CentralTendencyTypeEnum;
import gov.nih.nci.caintegrator2.application.study.HighVarianceCalculationTypeEnum;

import java.util.List;

/**
 * This calculator takes in central tendency type parameters, and a collection of float values, and after it runs will
 * figure out the central tendency value and (optionally) whether the values have a high variance or not.
 */
public class CentralTendencyCalculator {
    private static final Integer ONE_HUNDRED_PERCENT = 100;
    private final Double highVarianceThreshold;
    private final CentralTendencyTypeEnum type;
    private final HighVarianceCalculationTypeEnum varianceCalculationType;
    private final boolean useHighVarianceCalculation;
    
    private Float centralTendencyValue;
    private boolean highVariance = false;
    
    /**
     * Constructor to use if not calculating the high variance as well.
     * @param type central tendency type.
     */
    public CentralTendencyCalculator(CentralTendencyTypeEnum type) {
        this.type = type;
        this.useHighVarianceCalculation = false;
        this.highVarianceThreshold = null;
        this.varianceCalculationType = null;
    }
    
    /**
     * Public constructor.
     * @param type central tendency type.
     * @param useHighVarianceCalculation whether or not to calculate a standard deviation limit.
     * @param highVarianceThreshold percentage off of the central tendency that the standard deviation can be within
     *                                  before being flagged as "highVariance".
     * @param varianceCalculationType type of calculation to use for high variance.
     */
    public CentralTendencyCalculator(CentralTendencyTypeEnum type, boolean useHighVarianceCalculation, 
            Double highVarianceThreshold, HighVarianceCalculationTypeEnum varianceCalculationType) {
        this.type = type;
        this.useHighVarianceCalculation = useHighVarianceCalculation;
        this.highVarianceThreshold = highVarianceThreshold;
        this.varianceCalculationType = varianceCalculationType;
    }
    
    /**
     * 
     * @param values to turn into a single central tendency value.
     */
    public void calculateCentralTendencyValue(List<Float> values) {
        Double stdDev = 0.0;
        highVariance = false;
        if (CentralTendencyTypeEnum.MEDIAN.equals(type)) {
            stdDev = handleMedianType(values);
        } else if (CentralTendencyTypeEnum.MEAN.equals(type)) {
            stdDev = handleMeanType(values);
        } else {
            throw new IllegalArgumentException("Unknokwn CentralTendencyType.");
        }
        checkHighVariance(stdDev, values.size());
    }

    private void checkHighVariance(Double stdDev, int numValues) {
        if (isHighVarianceCalculationNecessary(numValues)) {
            if (HighVarianceCalculationTypeEnum.PERCENTAGE.equals(varianceCalculationType)) {
                calculatePercentageHighVariance(stdDev);
            } else if (HighVarianceCalculationTypeEnum.VALUE.equals(varianceCalculationType)) {
                calculateValueHighVariance(stdDev);
            }
        }
    }

    /**
     * This calculation determines if the standard deviation is >= the percentage of high variance 
     * (example: 50%) of the central tendency value.  An example is if the central tendency is 10, the 
     * highVarianceThreshold is 50 and the stdDeviation is 5.  In that case this would be considered 
     * high variance = true.  In that scenario, if the stdDeviation is 4.9 then it would be considered false.
     * @param stdDev
     */
    private void calculatePercentageHighVariance(Double stdDev) {
        Double percentageVariance = ((stdDev / centralTendencyValue) * ONE_HUNDRED_PERCENT); 
        if (percentageVariance >= highVarianceThreshold) {
            highVariance = true;
        }
    }
    
    private void calculateValueHighVariance(Double stdDev) {
        if (stdDev >= highVarianceThreshold) {
            highVariance = true;
        }
    }

    private Double handleMeanType(List<Float> values) {
        Double stdDev = 0.0;
        centralTendencyValue = MathUtil.mean(values);
        if (isHighVarianceCalculationNecessary(values.size())) {
            stdDev = MathUtil.standardDeviation(values, centralTendencyValue);
        }
        return stdDev;
    }

    private Double handleMedianType(List<Float> values) {
        Double stdDev = 0.0;
        centralTendencyValue = MathUtil.median(values);
        if (isHighVarianceCalculationNecessary(values.size())) {
            stdDev = MathUtil.standardDeviation(values, MathUtil.mean(values));
        }
        return stdDev;
    }

    /**
     * @return the centralTendencyValue
     */
    public Float getCentralTendencyValue() {
        return centralTendencyValue;
    }

    /**
     * @param centralTendencyValue the centralTendencyValue to set
     */
    public void setCentralTendencyValue(Float centralTendencyValue) {
        this.centralTendencyValue = centralTendencyValue;
    }
    
    private boolean isHighVarianceCalculationNecessary(int numValues) {
        return useHighVarianceCalculation && numValues > 1;
    }

    /**
     * @return the useHighVarianceCalculation
     */
    public boolean isUseHighVarianceCalculation() {
        return useHighVarianceCalculation;
    }

    /**
     * @return the highVariance
     */
    public boolean isHighVariance() {
        return highVariance;
    }


}
