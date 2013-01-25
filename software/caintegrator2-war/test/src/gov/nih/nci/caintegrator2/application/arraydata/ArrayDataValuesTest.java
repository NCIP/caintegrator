/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.arraydata;

import static org.junit.Assert.assertEquals;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValueType;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValues;
import gov.nih.nci.caintegrator2.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator2.domain.genomic.GeneExpressionReporter;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ArrayDataValuesTest {

    @Test(expected = IllegalArgumentException.class)
    public void testSetFloatValueNullType() {
        ArrayDataValues values = createValues();
        values.setFloatValue(new ArrayData(), values.getReporters().get(0), null, 0.0f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetFloatValueNullData() {
        ArrayDataValues values = createValues();
        values.setFloatValue(null, values.getReporters().get(0), ArrayDataValueType.EXPRESSION_SIGNAL, 0.0f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetFloatValueNullReporter() {
        ArrayDataValues values = createValues();
        values.setFloatValue(new ArrayData(), null, ArrayDataValueType.EXPRESSION_SIGNAL, 0.0f);
    }

    @Test
    public void testSetFloatValue() {
        ArrayDataValues values = createValues();
        AbstractReporter reporter1 = values.getReporters().get(0);
        AbstractReporter reporter2 = values.getReporters().get(1);

        ArrayData arrayData1 = new ArrayData();
        ArrayData arrayData2 = new ArrayData();
        values.setFloatValue(arrayData1, reporter1, ArrayDataValueType.COPY_NUMBER_LOG2_RATIO, 1.1f);
        values.setFloatValue(arrayData1, reporter2, ArrayDataValueType.COPY_NUMBER_LOG2_RATIO, 2.2f);
        values.setFloatValue(arrayData2, reporter1, ArrayDataValueType.COPY_NUMBER_LOG2_RATIO, 3.3f);
        values.setFloatValue(arrayData2, reporter2, ArrayDataValueType.COPY_NUMBER_LOG2_RATIO, 4.4f);
        values.setFloatValue(arrayData1, reporter1, ArrayDataValueType.EXPRESSION_SIGNAL, 5.5f);
        values.setFloatValue(arrayData1, reporter2, ArrayDataValueType.EXPRESSION_SIGNAL, 6.6f);
        values.setFloatValue(arrayData2, reporter1, ArrayDataValueType.EXPRESSION_SIGNAL, 7.7f);
        values.setFloatValue(arrayData2, reporter2, ArrayDataValueType.EXPRESSION_SIGNAL, 8.8f);
        
        assertEquals(2, values.getArrayDatas().size());
        assertEquals(1.1f, values.getFloatValue(arrayData1, reporter1, ArrayDataValueType.COPY_NUMBER_LOG2_RATIO), 0.0f);
        assertEquals(2.2f, values.getFloatValue(arrayData1, reporter2, ArrayDataValueType.COPY_NUMBER_LOG2_RATIO), 0.0f);
        assertEquals(3.3f, values.getFloatValue(arrayData2, reporter1, ArrayDataValueType.COPY_NUMBER_LOG2_RATIO), 0.0f);
        assertEquals(4.4f, values.getFloatValue(arrayData2, reporter2, ArrayDataValueType.COPY_NUMBER_LOG2_RATIO), 0.0f);
        assertEquals(5.5f, values.getFloatValue(arrayData1, reporter1, ArrayDataValueType.EXPRESSION_SIGNAL), 0.0f);
        assertEquals(6.6f, values.getFloatValue(arrayData1, reporter2, ArrayDataValueType.EXPRESSION_SIGNAL), 0.0f);
        assertEquals(7.7f, values.getFloatValue(arrayData2, reporter1, ArrayDataValueType.EXPRESSION_SIGNAL), 0.0f);
        assertEquals(8.8f, values.getFloatValue(arrayData2, reporter2, ArrayDataValueType.EXPRESSION_SIGNAL), 0.0f);
    }
    
    private ArrayDataValues createValues() {
        List<AbstractReporter> reporters = new ArrayList<AbstractReporter>();
        GeneExpressionReporter reporter1 = new GeneExpressionReporter();
        GeneExpressionReporter reporter2 = new GeneExpressionReporter();
        reporters.add(reporter1);
        reporters.add(reporter2);
        ArrayDataValues values = new ArrayDataValues(reporters);
        return values;
    }

}
