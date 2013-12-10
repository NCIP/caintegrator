/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator.application.study.CentralTendencyTypeEnum;
import gov.nih.nci.caintegrator.application.study.HighVarianceCalculationTypeEnum;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the central tendency calculator.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
public class CentralTendencyCalculatorTest {

    private CentralTendencyCalculator calculatorNoVariance;
    private CentralTendencyCalculator calculatorWithVariancePercentage;
    private CentralTendencyCalculator calculatorWithVarianceValue;
    private float[] values1 = {2f, 4f, 6f, 8f};
    private float[] values2 = {2f, 4f, 4f, 4f, 5f, 5f, 7f, 9f};

    /**
     * Sets up the tests.
     * @throws Exception on error
     */
    @Before
    public void setUp() throws Exception {
        calculatorNoVariance = new CentralTendencyCalculator(CentralTendencyTypeEnum.MEAN);
        calculatorWithVariancePercentage = new CentralTendencyCalculator(CentralTendencyTypeEnum.MEDIAN, true, 45.0,
                        HighVarianceCalculationTypeEnum.PERCENTAGE);
        calculatorWithVarianceValue = new CentralTendencyCalculator(CentralTendencyTypeEnum.MEDIAN, true, 2.0,
                        HighVarianceCalculationTypeEnum.VALUE);
    }

    /**
     * Calculate central tendency with no variance.
     */
    @Test
    public void calculateCentralTendencyValueNoVariance() {
        calculatorNoVariance.calculateCentralTendencyValue(values1);
        assertEquals(Float.valueOf(5), calculatorNoVariance.getCentralTendencyValue());

        values1 = ArrayUtils.add(values1, 8f);
        calculatorNoVariance.calculateCentralTendencyValue(values1);
        assertEquals(Float.valueOf("5.6"), calculatorNoVariance.getCentralTendencyValue());
        calculatorNoVariance = new CentralTendencyCalculator(CentralTendencyTypeEnum.MEDIAN);
        calculatorNoVariance.calculateCentralTendencyValue(values1);
        assertEquals(Float.valueOf("6"), calculatorNoVariance.getCentralTendencyValue());
    }

    /**
     * Calculate central tendency with variance as a percentage.
     */
    @Test
    public void calculateCentralTendencyValueWithVarianceAsPercentage() {
        calculatorWithVariancePercentage.calculateCentralTendencyValue(values2);
        assertEquals(Float.valueOf("4.5"), calculatorWithVariancePercentage.getCentralTendencyValue());
        assertFalse(calculatorWithVariancePercentage.isHighVariance());
        calculatorWithVariancePercentage = new CentralTendencyCalculator(CentralTendencyTypeEnum.MEDIAN, true, 44.0,
                HighVarianceCalculationTypeEnum.PERCENTAGE);
        calculatorWithVariancePercentage.calculateCentralTendencyValue(values2);
        assertTrue(calculatorWithVariancePercentage.isHighVariance());
        values2 = ArrayUtils.add(values2, 4.5f);
        calculatorWithVariancePercentage.calculateCentralTendencyValue(values2);
        assertFalse(calculatorWithVariancePercentage.isHighVariance());
    }

    /**
     * Calculate central tendency with variance as a value.
     */
    @Test
    public void calculateCentralTendencyValueWithVarianceAsValue() {
        calculatorWithVarianceValue.calculateCentralTendencyValue(values2);
        assertEquals(Float.valueOf("4.5"), calculatorWithVarianceValue.getCentralTendencyValue());
        assertTrue(calculatorWithVarianceValue.isHighVariance());
        values2 = ArrayUtils.add(values2, 4.5f);
        calculatorWithVarianceValue.calculateCentralTendencyValue(values2);
        assertFalse(calculatorWithVarianceValue.isHighVariance());
    }

}
