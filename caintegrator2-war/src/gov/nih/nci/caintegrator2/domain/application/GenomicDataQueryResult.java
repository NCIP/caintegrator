package gov.nih.nci.caintegrator2.domain.application;

import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;

import java.util.Collection;

/**
 * 
 */
public class GenomicDataQueryResult extends AbstractCaIntegrator2Object {

    private static final long serialVersionUID = 1L;
    
    private Collection<GenomicDataResultRow> rowCollection;
    private Query query;
    private Collection<GenomicDataResultColumn> columnCollection;
    
    /**
     * @return the rowCollection
     */
    public Collection<GenomicDataResultRow> getRowCollection() {
        return rowCollection;
    }
    
    /**
     * @param rowCollection the rowCollection to set
     */
    public void setRowCollection(Collection<GenomicDataResultRow> rowCollection) {
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
    public Collection<GenomicDataResultColumn> getColumnCollection() {
        return columnCollection;
    }
    
    /**
     * @param columnCollection the columnCollection to set
     */
    public void setColumnCollection(Collection<GenomicDataResultColumn> columnCollection) {
        this.columnCollection = columnCollection;
    }

}