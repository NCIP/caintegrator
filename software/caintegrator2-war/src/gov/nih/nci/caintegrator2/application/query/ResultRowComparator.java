/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.query;

import gov.nih.nci.caintegrator2.common.DateUtil;
import gov.nih.nci.caintegrator2.domain.annotation.AbstractAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.DateAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.NumericAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.StringAnnotationValue;
import gov.nih.nci.caintegrator2.domain.application.ResultColumn;
import gov.nih.nci.caintegrator2.domain.application.ResultRow;
import gov.nih.nci.caintegrator2.domain.application.ResultValue;
import gov.nih.nci.caintegrator2.domain.application.SortTypeEnum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;


/**
 * A comparator class for Result Rows, which can only be instantiated by calling the static function
 * sort().  The Sort takes in a collection of rows to sort and a list of columns that need to be sorted
 * upon and based on that it sorts the rows and returns it in a List form.
 */
@SuppressWarnings({ "PMD.CyclomaticComplexity" }) // See handleNumericValues()
final class ResultRowComparator implements Comparator <ResultRow> {

    private static final int EQUAL        = 0;
    private static final int LESS_THAN    = -1;
    private static final int GREATER_THAN = 1;

    private final List <ResultColumn> sortColumns;
    
    /**
     * Private constructor which takes in a list of columns to sort on.
     * @param sortColumns Columns to sort the rows by.
     */
    private ResultRowComparator(List<ResultColumn> sortColumns) {
        this.sortColumns = sortColumns;
    }
    
    /**
     * Static sort method that is a wrapper for Collections.sort().
     * @param rowsToSort Rows to sort.
     * @param sortColumns Ordered list of columns to sort the rows by. 
     * @return List of rows that are sorted.
     */
    static List<ResultRow> sort(Collection<ResultRow> rowsToSort, List<ResultColumn> sortColumns) {
        for (ResultColumn col : sortColumns) {
            if (col.getSortType() == null) {  // Check sort types and if not there assume it's ascending.
                col.setSortType(SortTypeEnum.ASCENDING);
            }
        }
        List<ResultRow> rowsList = Arrays.asList(rowsToSort.toArray(new ResultRow[rowsToSort.size()]));
        Collections.sort(rowsList, new ResultRowComparator(sortColumns));
        int rowNumber = 0;
        for (ResultRow row : rowsList) {
            rowNumber++;
            row.setRowIndex(rowNumber);
        }
        return rowsList;
    }
    
    /**
     * {@inheritDoc}
     */
    public int compare(ResultRow row1, ResultRow row2) {
        Iterator <ResultColumn> sortColumnsIterator = sortColumns.iterator();
        ResultColumn currentColumn = sortColumnsIterator.next();
        ResultValue resultValue1 = getResultValueFromRowColumn(row1, currentColumn);
        ResultValue resultValue2 = getResultValueFromRowColumn(row2, currentColumn);
        int order = compareResultValues(resultValue1, 
                                        resultValue2, 
                                        currentColumn.getSortType());
        if (order == 0 && sortColumnsIterator.hasNext()) { // Tie-breaker check, goes to next sort column
            List <ResultColumn> newColumns = new ArrayList<ResultColumn>();
            newColumns.addAll(sortColumns);
            List <ResultRow> newRows = new ArrayList<ResultRow>();
            newRows.add(row1);
            newRows.add(row2);
            newColumns.remove(0);
            List<ResultRow> sortedRows = ResultRowComparator.sort(newRows, newColumns);
            if (sortedRows.iterator().next() == row1) { // row1 is first
                return -1;
            } else {
                return 1;
            }
        } else {
            return order;
        }
    }

    private ResultValue getResultValueFromRowColumn(ResultRow row, ResultColumn currentColumn) {
        for (ResultValue value : row.getValueCollection()) {
            if (value.getColumn() == currentColumn) {
                return value;
            }
        }
        return null;
    }
    
    private int compareResultValues(ResultValue rv1, ResultValue rv2, SortTypeEnum sortType) {
        AbstractAnnotationValue value1 = rv1.getValue();
        AbstractAnnotationValue value2 = rv2.getValue();
        
        if (value1 instanceof NumericAnnotationValue || value2 instanceof NumericAnnotationValue) {
            return handleNumericValues((NumericAnnotationValue) value1, (NumericAnnotationValue) value2, sortType);
        } else if (value1 instanceof StringAnnotationValue || value2 instanceof StringAnnotationValue) {
            return handleStringValues((StringAnnotationValue) value1, (StringAnnotationValue) value2, sortType);
        } else if (value1 instanceof DateAnnotationValue || value2 instanceof DateAnnotationValue) {
            return handleDateValues((DateAnnotationValue) value1, (DateAnnotationValue) value2, sortType);
        } else {
            // Equal when both values are null (imaging data with no annotation)
            // Or could be an error if the values are not one of the above instance.
            return 0;
        }
    }

    @SuppressWarnings({ "PMD.CyclomaticComplexity" }) // Checking null values
    private int handleNumericValues(NumericAnnotationValue value1, 
                                    NumericAnnotationValue value2,
                                    SortTypeEnum sortType) {
        boolean val1null = value1 == null || value1.getNumericValue() == null;
        boolean val2null = value2 == null || value2.getNumericValue() == null;
        
        if (!val1null && !val2null) {
            if (value1.getNumericValue() < value2.getNumericValue()) {
                return LESS_THAN * getSortOrder(sortType);
            }
            if (value1.getNumericValue() > value2.getNumericValue()) {
                return GREATER_THAN * getSortOrder(sortType);
            }
            // Must be equal.
            return EQUAL * getSortOrder(sortType);
        } else {
            return handleNullValues(val1null, val2null, sortType);
        }
    }

    @SuppressWarnings({ "PMD.CyclomaticComplexity" }) // Checking null values
    private int handleDateValues(DateAnnotationValue value1, 
                                 DateAnnotationValue value2,
                                 SortTypeEnum sortType) {
        boolean val1null = value1 == null || value1.getDateValue() == null;
        boolean val2null = value2 == null || value2.getDateValue() == null;

        if (!val1null && !val2null) {
            return DateUtil.toStringForComparison(value1.getDateValue()).compareTo(
                DateUtil.toStringForComparison(value2.getDateValue()));
        } else {
            return handleNullValues(val1null, val2null, sortType);
        }
    }
    
    private int handleStringValues(StringAnnotationValue value1, 
                                    StringAnnotationValue value2, 
                                    SortTypeEnum sortType) {
        boolean val1null = value1 == null || value1.getStringValue() == null;
        boolean val2null = value2 == null || value2.getStringValue() == null;

        if (!val1null && !val2null) {
            return value1.getStringValue().compareTo(value2.getStringValue()) * getSortOrder(sortType);
        } else {
            return handleNullValues(val1null, val2null, sortType);
        }
    }

    private int handleNullValues(boolean val1null, boolean val2null, SortTypeEnum sortType) {
        if (val1null && !val2null) {
            return LESS_THAN * getSortOrder(sortType);
        }
        if (!val1null && val2null) {
            return GREATER_THAN * getSortOrder(sortType);
        }
        // Both null means equal.
        return EQUAL * getSortOrder(sortType);
    }
    
    private int getSortOrder(SortTypeEnum sortType) {
        if (SortTypeEnum.ASCENDING.equals(sortType)) {
            return 1;
        } else if (SortTypeEnum.DESCENDING.equals(sortType)) {
            return -1;
        }
        return 0;
    }
}
