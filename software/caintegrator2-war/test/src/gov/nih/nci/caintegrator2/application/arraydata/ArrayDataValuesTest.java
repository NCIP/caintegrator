package gov.nih.nci.caintegrator2.application.arraydata;

import static org.junit.Assert.assertEquals;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayDataMatrix;
import gov.nih.nci.caintegrator2.domain.genomic.GeneExpressionReporter;

import org.junit.Test;

@SuppressWarnings("PMD")
public class ArrayDataValuesTest {

    @Test
    public void testAddValues() {
        ArrayDataValues values1 = new ArrayDataValues();
        ArrayDataValues values2 = new ArrayDataValues();
        values1.setArrayDataMatrix(new ArrayDataMatrix());
        GeneExpressionReporter reporter1 = new GeneExpressionReporter();
        reporter1.setId(1L);
        GeneExpressionReporter reporter2 = new GeneExpressionReporter();
        reporter2.setId(2L);
        ArrayData arrayData1 = new ArrayData();
        arrayData1.setId(1L);
        ArrayData arrayData2 = new ArrayData();
        arrayData2.setId(2L);
        ArrayData arrayData3 = new ArrayData();
        arrayData3.setId(3L);
        values1.setValue(arrayData1, reporter1, (float) 1.1);
        values1.setValue(arrayData1, reporter2, (float) 2.2);
        values1.setValue(arrayData2, reporter1, (float) 3.3);
        values1.setValue(arrayData2, reporter2, (float) 4.4);
        values2.setValue(arrayData3, reporter1, (float) 5.5);
        values2.setValue(arrayData3, reporter2, (float) 6.6);
        values1.addValues(values2);
        assertEquals((float) 1.1, values1.getValue(arrayData1, reporter1), 0);
        assertEquals((float) 2.2, values1.getValue(arrayData1, reporter2), 0);
        assertEquals((float) 3.3, values1.getValue(arrayData2, reporter1), 0);
        assertEquals((float) 4.4, values1.getValue(arrayData2, reporter2), 0);
        assertEquals((float) 5.5, values1.getValue(arrayData3, reporter1), 0);
        assertEquals((float) 6.6, values1.getValue(arrayData3, reporter2), 0);
    }

}
