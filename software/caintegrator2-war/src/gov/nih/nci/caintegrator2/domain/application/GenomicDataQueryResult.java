package gov.nih.nci.caintegrator2.domain.application;

import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 */
public class GenomicDataQueryResult extends AbstractCaIntegrator2Object {

    private static final long serialVersionUID = 1L;
    
    private List<GenomicDataResultRow> rowCollection = new ArrayList<GenomicDataResultRow>();
    private Query query;
    private List<GenomicDataResultColumn> columnCollection = new ArrayList<GenomicDataResultColumn>();;
    
    /**
     * @return the rowCollection
     */
    public List<GenomicDataResultRow> getRowCollection() {
        return rowCollection;
    }
    
    /**
     * @param rowCollection the rowCollection to set
     */
    @SuppressWarnings("unused") // Required by Hibernate
    private void setRowCollection(List<GenomicDataResultRow> rowCollection) {
        this.rowCollection = rowCollection;
    }
    
    /**
     * @return the query
     */
    public Query getQuery() {
        return query;
    }
    
    /**
     * @param query the query to set
     */
    public void setQuery(Query query) {
        this.query = query;
    }
    
    /**
     * @return the columnCollection
     */
    public List<GenomicDataResultColumn> getColumnCollection() {
        return columnCollection;
    }
    
    /**
     * @param columnCollection the columnCollection to set
     */
    @SuppressWarnings("unused") // Required by Hibernate
    private void setColumnCollection(List<GenomicDataResultColumn> columnCollection) {
        this.columnCollection = columnCollection;
    }

}