/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.web.action.query;

import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.QueryResult;
import gov.nih.nci.caintegrator2.domain.application.ResultColumn;
import gov.nih.nci.caintegrator2.domain.application.ResultRow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Wraps access to a <code>QueryResult</code> object for easy use in display JSPs.
 */
public final class DisplayableQueryResult {
    private static final String ASTERISK_FOR_ANNOTATION_MASKS = "*";
    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final Comparator<ResultColumn> COLUMN_COMPARATOR = new ColumnComparator();
    private final QueryResult result;
    private final List<String> headers = new ArrayList<String>();
    private final List<DisplayableResultRow> rows = new ArrayList<DisplayableResultRow>();
    private final Map<String, Integer> newColumnLocations = new HashMap<String, Integer>();
    private boolean hasSubjects;
    private boolean hasSamples;
    private boolean hasImageSeries;
    private int pageSize = DEFAULT_PAGE_SIZE;
    private Boolean selectAll = true;
    private Boolean selectAllSubject = true;
    
    DisplayableQueryResult(QueryResult result) {
        this.result = result;
        load();
    }
    
    private void load() {
        loadHeaders();
        loadRows();
    }

    private void loadRows() {
        for (ResultRow row : result.getRowCollection()) {
            DisplayableResultRow displayableResultRow = new DisplayableResultRow(row, newColumnLocations);
            rows.add(displayableResultRow);
            hasSubjects |= displayableResultRow.getSubjectAssignment() != null;
            hasSamples |= displayableResultRow.getSampleAcquisition() != null;
            hasImageSeries |= displayableResultRow.getImageSeries() != null;
        }
    }

    private void loadHeaders() {
        List<ResultColumn> sortedColumns = new ArrayList<ResultColumn>();
        sortedColumns.addAll(getQuery().retrieveVisibleColumns());
        Collections.sort(sortedColumns, COLUMN_COMPARATOR);
        int columnLocation = 0;
        for (ResultColumn column : sortedColumns) {
            String columnName = column.getAnnotationDefinition().getDisplayName();
            String headerName = !column.getAnnotationFieldDescriptor().getAnnotationMasks().isEmpty()
                    ? columnName + ASTERISK_FOR_ANNOTATION_MASKS : columnName;
            headers.add(columnLocation, headerName);
            newColumnLocations.put(columnName, columnLocation++);
        }
    }

    /**
     * Gets the query.
     * 
     * @return the query.
     */
    public Query getQuery() {
        return result.getQuery();
    }
    
    /**
     * Returns the list of headers to display.
     * 
     * @return the headers.
     */
    public List<String> getHeaders() {
        return headers;
    }

    /**
     * @return the hasSubjects
     */
    public boolean getHasSubjects() {
        return hasSubjects;
    }

    /**
     * @return the hasSamples
     */
    public boolean getHasSamples() {
        return hasSamples;
    }

    /**
     * @return the hasImageSeries
     */
    public boolean getHasImageSeries() {
        return hasImageSeries;
    }

    /**
     * @return the rows
     */
    public List<DisplayableResultRow> getRows() {
        return rows;
    }
    
    /**
     * @return the number of rows.
     */
    public int getNumberOfRows() {
        return getRows().size();
    }

    /**
     * Used to put columns in order by index.
     */
    private static class ColumnComparator implements Comparator<ResultColumn> {

        /**
         * {@inheritDoc}
         */
        public int compare(ResultColumn column1, ResultColumn column2) {
            return column1.getColumnIndex() - column2.getColumnIndex();
        }

    }


    /**
     * @return the pageSize
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * @param pageSize the pageSize to set
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * @return the selectAll
     */
    public Boolean getSelectAll() {
        return selectAll;
    }

    /**
     * @param selectAll the selectAll to set
     */
    public void setSelectAll(Boolean selectAll) {
        this.selectAll = selectAll;
    }

    /**
     * @return the selectAllSubject
     */
    public Boolean getSelectAllSubject() {
        return selectAllSubject;
    }

    /**
     * @param selectAllSubject the selectAllSubject to set
     */
    public void setSelectAllSubject(Boolean selectAllSubject) {
        this.selectAllSubject = selectAllSubject;
    }
}
