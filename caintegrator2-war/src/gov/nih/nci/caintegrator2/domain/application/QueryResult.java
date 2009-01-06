package gov.nih.nci.caintegrator2.domain.application;

import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;

import java.util.Collection;

/**
 * 
 */
public class QueryResult extends AbstractCaIntegrator2Object {

    private static final long serialVersionUID = 1L;
    
    private Query query;
    private Collection<ResultRow> rowCollection;
    
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
     * @return the rowCollection
     */
    public Collection<ResultRow> getRowCollection() {
        return rowCollection;
    }
    
    /**
     * @param rowCollection the rowCollection to set
     */
    public void setRowCollection(Collection<ResultRow> rowCollection) {
        this.rowCollection = rowCollection;
    }

}