/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.query;

import static org.junit.Assert.*;
import gov.nih.nci.caintegrator2.domain.annotation.NumericAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.StringAnnotationValue;
import gov.nih.nci.caintegrator2.domain.application.ResultColumn;
import gov.nih.nci.caintegrator2.domain.application.ResultRow;
import gov.nih.nci.caintegrator2.domain.application.ResultValue;
import gov.nih.nci.caintegrator2.domain.application.SortTypeEnum;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.junit.Test;

@SuppressWarnings("PMD")
public class ResultRowComparatorTest {

    @Test
    public void testSort() {
        ResultRow row1 = new ResultRow();
        row1.setId(Long.valueOf(1));
        ResultRow row2 = new ResultRow();
        row2.setId(Long.valueOf(2));
        ResultRow row3 = new ResultRow();
        row3.setId(Long.valueOf(3));
        ResultRow row4 = new ResultRow();
        row4.setId(Long.valueOf(4));
        ResultRow row5 = new ResultRow();
        row5.setId(Long.valueOf(5));
        
        ResultColumn col1 = new ResultColumn();
        ResultColumn col2 = new ResultColumn();
        
        Collection<ResultRow> rowCollection = new HashSet<ResultRow>();
        rowCollection.add(row1);
        ResultValue row1col1Value = new ResultValue();
        ResultValue row1col2Value = new ResultValue();
        Collection <ResultValue> row1ValueCollection = new HashSet<ResultValue>();
        row1.setValueCollection(row1ValueCollection);
        
        rowCollection.add(row2);
        ResultValue row2col1Value = new ResultValue();
        ResultValue row2col2Value = new ResultValue();
        Collection <ResultValue> row2ValueCollection = new HashSet<ResultValue>();
        row2.setValueCollection(row2ValueCollection);
       
        rowCollection.add(row3);
        ResultValue row3col1Value = new ResultValue();
        ResultValue row3col2Value = new ResultValue();
        Collection <ResultValue> row3ValueCollection = new HashSet<ResultValue>();
        row3.setValueCollection(row3ValueCollection);
        
        rowCollection.add(row4);
        ResultValue row4col1Value = new ResultValue();
        ResultValue row4col2Value = new ResultValue();
        Collection <ResultValue> row4ValueCollection = new HashSet<ResultValue>();
        row4.setValueCollection(row4ValueCollection);
        
        rowCollection.add(row5);
        ResultValue row5col1Value = new ResultValue();
        ResultValue row5col2Value = new ResultValue();
        Collection <ResultValue> row5ValueCollection = new HashSet<ResultValue>();
        row5.setValueCollection(row5ValueCollection);
        
        NumericAnnotationValue numVal1 = new NumericAnnotationValue();
        numVal1.setNumericValue(1.0);
        NumericAnnotationValue numVal2 = new NumericAnnotationValue();
        numVal2.setNumericValue(5.0);
        NumericAnnotationValue numVal3 = new NumericAnnotationValue();
        numVal3.setNumericValue(2.0);
        NumericAnnotationValue numVal4 = new NumericAnnotationValue();
        numVal4.setNumericValue(5.0);
        NumericAnnotationValue numVal5 = new NumericAnnotationValue();
        numVal5.setNumericValue(1.0);
        
        StringAnnotationValue stringVal1 = new StringAnnotationValue();
        // Commenting this out to make sure that null values are sorted properly. (Always lower)
//        stringVal1.setStringValue("value 1");
        StringAnnotationValue stringVal2 = new StringAnnotationValue();
        stringVal2.setStringValue("value 2");
        StringAnnotationValue stringVal3 = new StringAnnotationValue();
        stringVal3.setStringValue("value 3");
        StringAnnotationValue stringVal4 = new StringAnnotationValue();
        stringVal4.setStringValue("value 4");
        StringAnnotationValue stringVal5 = new StringAnnotationValue();
        stringVal5.setStringValue("value 5");
        
        row1col1Value.setColumn(col1);
        row1col1Value.setValue(numVal1);
        row1col2Value.setColumn(col2);
        row1col2Value.setValue(stringVal1);
        row1ValueCollection.add(row1col1Value);
        row1ValueCollection.add(row1col2Value);
        
        row2col1Value.setColumn(col1);
        row2col1Value.setValue(numVal2);
        row2col2Value.setColumn(col2);
        row2col2Value.setValue(stringVal2);
        row2ValueCollection.add(row2col1Value);
        row2ValueCollection.add(row2col2Value);
        
        row3col1Value.setColumn(col1);
        row3col1Value.setValue(numVal3);
        row3col2Value.setColumn(col2);
        row3col2Value.setValue(stringVal3);
        row3ValueCollection.add(row3col1Value);
        row3ValueCollection.add(row3col2Value);
        
        row4col1Value.setColumn(col1);
        row4col1Value.setValue(numVal4);
        row4col2Value.setColumn(col2);
        row4col2Value.setValue(stringVal4);
        row4ValueCollection.add(row4col1Value);
        row4ValueCollection.add(row4col2Value);
        
        row5col1Value.setColumn(col1);
        row5col1Value.setValue(numVal5);
        row5col2Value.setColumn(col2);
        row5col2Value.setValue(stringVal5);
        row5ValueCollection.add(row5col1Value);
        row5ValueCollection.add(row5col2Value);
        
        col1.setSortOrder(1);
        col1.setSortType(SortTypeEnum.ASCENDING);
        
        col2.setSortOrder(2);
        col2.setSortType(SortTypeEnum.DESCENDING);
        
        List <ResultColumn> sortColumns = new ArrayList<ResultColumn>();
        sortColumns.add(col1);
        sortColumns.add(col2);
        
        // Teset1 is col1.sortType ascending, col2.sortType descending.
        List <ResultRow> sortedRows = ResultRowComparator.sort(rowCollection, sortColumns);
        assertEquals(Long.valueOf(5), sortedRows.get(0).getId());
        assertEquals(Long.valueOf(1), sortedRows.get(1).getId());
        assertEquals(Long.valueOf(3), sortedRows.get(2).getId());
        assertEquals(Long.valueOf(4), sortedRows.get(3).getId());
        assertEquals(Long.valueOf(2), sortedRows.get(4).getId());

        // Test2 is col1.sortType null (should default to ascending), col2.sortType ascending.
        col1.setSortType(null);
        col2.setSortType(SortTypeEnum.ASCENDING);
        assertNull(col1.getSortType());
        List <ResultRow> sortedRows2 = ResultRowComparator.sort(rowCollection, sortColumns);
        assertEquals(Long.valueOf(1), sortedRows2.get(0).getId());
        assertEquals(Long.valueOf(5), sortedRows2.get(1).getId());
        assertEquals(Long.valueOf(3), sortedRows2.get(2).getId());
        assertEquals(Long.valueOf(2), sortedRows2.get(3).getId());
        assertEquals(Long.valueOf(4), sortedRows2.get(4).getId());
        // Test to make sure that column's default to Ascending sort types.
        assertEquals(SortTypeEnum.ASCENDING, col1.getSortType());
        
    }



}
