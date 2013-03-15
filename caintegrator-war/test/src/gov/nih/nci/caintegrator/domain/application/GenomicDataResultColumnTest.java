/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.domain.application;

import static org.junit.Assert.assertEquals;
import gov.nih.nci.caintegrator.domain.application.GenomicDataQueryResult;
import gov.nih.nci.caintegrator.domain.application.GenomicDataResultColumn;
import gov.nih.nci.caintegrator.domain.application.GenomicDataResultRow;
import gov.nih.nci.caintegrator.domain.application.GenomicDataResultValue;
import gov.nih.nci.caintegrator.domain.genomic.GeneExpressionReporter;

import java.util.List;

import org.junit.Test;

public class GenomicDataResultColumnTest {

    @Test
    public void testGetValues() {
        GenomicDataQueryResult result = new GenomicDataQueryResult();
        GenomicDataResultColumn column1 = result.addColumn();
        GenomicDataResultColumn column2 = result.addColumn();
        result.getRowCollection().add(createRow(1.1f, 2.2f, result.getColumnCollection()));
        result.getRowCollection().add(createRow(3.3f, 4.4f, result.getColumnCollection()));
        assertEquals(1.1f, (float) column1.getValues().get(0).getValue(), 0.0f);
        assertEquals(3.3f, (float) column1.getValues().get(1).getValue(), 0.0f);
        assertEquals(2.2f, (float) column2.getValues().get(0).getValue(), 0.0f);
        assertEquals(4.4f, (float) column2.getValues().get(1).getValue(), 0.0f);
    }

    private GenomicDataResultRow createRow(float float1, float float2, List<GenomicDataResultColumn> columns) {
        GenomicDataResultRow row = new GenomicDataResultRow();
        GeneExpressionReporter reporter = new GeneExpressionReporter();
        row.setReporter(reporter);
        GenomicDataResultValue value1 = new GenomicDataResultValue();
        value1.setValue(float1);
        value1.setColumn(columns.get(0));
        row.getValues().add(value1);
        GenomicDataResultValue value2 = new GenomicDataResultValue();
        value2.setValue(float2);
        value2.setColumn(columns.get(1));
        row.getValues().add(value2);
        return row;
    }

}
