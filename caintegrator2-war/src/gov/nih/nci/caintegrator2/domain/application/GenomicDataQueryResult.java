package gov.nih.nci.caintegrator2.domain.application;

import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;

import java.util.List;

/**
 * 
 */
public class GenomicDataQueryResult extends AbstractCaIntegrator2Object {

    private static final long serialVersionUID = 1L;
    
    private List<GenomicDataResultRow> rowCollection;
    private Query query;
    private List<GenomicDataResultColumn> columnCollection;
    
    /**
     * @return the rowCollection
     */
    public List<GenomicDataResultRow> getRowCollection() {
        return rowCollection;
    }
    
    /**
     * @param rowCollection the rowCollection to set
     */
    public void setRowCollection(List<GenomicDataResultRow> rowCollection) {
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
    public void setColumnCollection(List<GenomicDataResultColumn> columnCollection) {
        this.columnCollection = columnCollection;
    }

}