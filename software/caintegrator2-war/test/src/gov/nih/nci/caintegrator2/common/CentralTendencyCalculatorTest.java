/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.application.study.CentralTendencyTypeEnum;
import gov.nih.nci.caintegrator2.application.study.HighVarianceCalculationTypeEnum;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * 
 */
public class CentralTendencyCalculatorTest {

    private CentralTendencyCalculator calculatorNoVariance;
    private CentralTendencyCalculator calculatorWithVariancePercentage;
    private CentralTendencyCalculator calculatorWithVarianceValue;
    private List<Float> values1;
    private List<Float> values2;
    
    
    @Before
    public void setUp() throws Exception {
        calculatorNoVariance = new CentralTendencyCalculator(CentralTendencyTypeEnum.MEAN);
        calculatorWithVariancePercentage = new CentralTendencyCalculator(CentralTendencyTypeEnum.MEDIAN, true, 45.0, HighVarianceCalculationTypeEnum.PERCENTAGE);
        calculatorWithVarianceValue = new CentralTendencyCalculator(CentralTendencyTypeEnum.MEDIAN, true, 2.0, HighVarianceCalculationTypeEnum.VALUE);
        values1 = new ArrayList<Float>();
        values1.add(2f);
        values1.add(4f);
        values1.add(6f);
        values1.add(8f);
        
        values2 = new ArrayList<Float>();
        values2.add(2f);
        values2.add(4f);
        values2.add(4f);
        values2.add(4f);
        values2.add(5f);
        values2.add(5f);
        values2.add(7f);
        values2.add(9f);
        
    }

    @Test
    public void testCalculateCentralTendencyValueNoVariance() {
        calculatorNoVariance.calculateCentralTendencyValue(values1);
        assertEquals(Float.valueOf(5), calculatorNoVariance.getCentralTendencyValue());
        values1.add(8f);
        calculatorNoVariance.calculateCentralTendencyValue(values1);
        assertEquals(Float.valueOf("5.6"), calculatorNoVariance.getCentralTendencyValue());
        calculatorNoVariance = new CentralTendencyCalculator(CentralTendencyTypeEnum.MEDIAN);
        calculatorNoVariance.calculateCentralTendencyValue(values1);
        assertEquals(Float.valueOf("6"), calculatorNoVariance.getCentralTendencyValue());
    }
    
    @Test
    public void testCalculateCentralTendencyValueWithVarianceAsPercentage() {
        calculatorWithVariancePercentage.calculateCentralTendencyValue(values2);
        assertEquals(Float.valueOf("4.5"), calculatorWithVariancePercentage.getCentralTendencyValue());
        assertFalse(calculatorWithVariancePercentage.isHighVariance());
        calculatorWithVariancePercentage = new CentralTendencyCalculator(CentralTendencyTypeEnum.MEDIAN, true, 44.0, HighVarianceCalculationTypeEnum.PERCENTAGE);
        calculatorWithVariancePercentage.calculateCentralTendencyValue(values2);
        assertTrue(calculatorWithVariancePercentage.isHighVariance());
        values2.add(4.5f);
        calculatorWithVariancePercentage.calculateCentralTendencyValue(values2);
        assertFalse(calculatorWithVariancePercentage.isHighVariance());
    }
    
    @Test
    public void testCalculateCentralTendencyValueWithVarianceAsValue() {
        calculatorWithVarianceValue.calculateCentralTendencyValue(values2);
        assertEquals(Float.valueOf("4.5"), calculatorWithVarianceValue.getCentralTendencyValue());
        assertTrue(calculatorWithVarianceValue.isHighVariance());
        values2.add(4.5f);
        calculatorWithVarianceValue.calculateCentralTendencyValue(values2);
        assertFalse(calculatorWithVarianceValue.isHighVariance());
    }

}
