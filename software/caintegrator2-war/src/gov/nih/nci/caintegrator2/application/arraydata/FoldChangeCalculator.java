/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.arraydata;

import static gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValueType.EXPRESSION_SIGNAL;

import gov.nih.nci.caintegrator2.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;

/**
 * Computes fold change values for a set of arrays based on data from a set of control arrays.
 */
class FoldChangeCalculator {

    private final ArrayDataValues values;
    private final ArrayDataValues controlValues;
    private final ArrayDataValues foldChangeValues;
    private static final double NATURAL_LOG_OF_2 = Math.log(2);

    FoldChangeCalculator(ArrayDataValues values, ArrayDataValues controlValues) {
        this.values = values;
        this.controlValues = controlValues;
        foldChangeValues = new ArrayDataValues(values.getReporters());
    }

    ArrayDataValues calculate() {
        for (AbstractReporter reporter : values.getReporters()) {
            calculate(reporter);
        }
        return foldChangeValues;
    }

    private void calculate(AbstractReporter reporter) {
        double log2MeanOfControls = getLog2MeanOfControls(reporter);
        for (ArrayData arrayData : values.getArrayDatas()) {
            double log2OfSample = log2(values.getFloatValue(arrayData, reporter, EXPRESSION_SIGNAL));
            float foldChange = (float) getFoldChange(log2OfSample, log2MeanOfControls);
            foldChangeValues.setFloatValue(arrayData, reporter, EXPRESSION_SIGNAL, foldChange);
        }
    }

    private double getFoldChange(double log2OfSample, double log2MeanOfControls) {
        double log2Ratio = log2OfSample - log2MeanOfControls;
        if (log2Ratio >= 0) {
            return Math.pow(2, log2Ratio);
        } else {
            return -Math.pow(2, -log2Ratio);
        }
    }

    private double getLog2MeanOfControls(AbstractReporter reporter) {
        double valueProduct = 1.0f;
        for (ArrayData arrayData : controlValues.getArrayDatas()) {
            valueProduct *= controlValues.getFloatValue(arrayData, reporter, EXPRESSION_SIGNAL);
        }
        double logSum = log2(valueProduct);
        return logSum / controlValues.getArrayDatas().size();
    }
    
    private double log2(double value) {
        return Math.log(value) / NATURAL_LOG_OF_2;
    }

}
