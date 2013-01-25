/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.arraydata;

import static gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValueType.EXPRESSION_SIGNAL;
import gov.nih.nci.caintegrator2.common.Cai2Util;
import gov.nih.nci.caintegrator2.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;

import java.util.List;

/**
 * Computes fold change values for a set of arrays based on data from a set of control arrays.
 */
class FoldChangeCalculator {

    private final ArrayDataValues values;
    private final List<ArrayDataValues> controlValuesList;
    private final ArrayDataValues foldChangeValues;
    private final PlatformChannelTypeEnum channelType;

    FoldChangeCalculator(ArrayDataValues values, List<ArrayDataValues> controlValuesList,
           PlatformChannelTypeEnum channelType) {
        this.values = values;
        this.controlValuesList = controlValuesList;
        this.channelType = channelType;
        foldChangeValues = new ArrayDataValues(values.getReporters());
    }

    ArrayDataValues calculate() {
        for (AbstractReporter reporter : values.getReporters()) {
            calculate(reporter);
        }
        return foldChangeValues;
    }

    private void calculate(AbstractReporter reporter) {
        double log2MeanOfControls = getLog2MeanOfControls(reporter,
                getControlValues(reporter));
        for (ArrayData arrayData : values.getArrayDatas()) {
            double log2OfSample = values.getLog2Value(arrayData, reporter, EXPRESSION_SIGNAL, channelType);
            float foldChange = (float) getFoldChange(log2OfSample, log2MeanOfControls);
            foldChangeValues.setFloatValue(arrayData, reporter, EXPRESSION_SIGNAL, foldChange);
        }
    }

    private ArrayDataValues getControlValues(AbstractReporter reporter) {
        if (controlValuesList.size() > 1) {
            for (ArrayDataValues controlValues : controlValuesList) {
                if (controlValues.getReporters().contains(reporter)) {
                    return controlValues;
                }
            }
        }
        return controlValuesList.get(0);
    }

    private double getFoldChange(double log2OfSample, double log2MeanOfControls) {
        double log2Ratio = log2OfSample - log2MeanOfControls;
        if (log2Ratio >= 0) {
            return Math.pow(2, log2Ratio);
        } else {
            return -Math.pow(2, -log2Ratio);
        }
    }

    private double getLog2MeanOfControls(AbstractReporter reporter, ArrayDataValues controlValues) {
        double totalSum = 0.0f;
        for (ArrayData arrayData : controlValues.getArrayDatas()) {
            if (PlatformChannelTypeEnum.ONE_COLOR.equals(channelType)) {
                totalSum += controlValues.getFloatValue(arrayData, reporter, EXPRESSION_SIGNAL);
            } else {
                totalSum += Cai2Util.antiLog2(
                        controlValues.getLog2Value(arrayData, reporter, EXPRESSION_SIGNAL, channelType));
            }
        }
        return Cai2Util.log2(totalSum / controlValues.getArrayDatas().size());
    }

}
