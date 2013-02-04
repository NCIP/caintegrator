/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.domain.application;

import gov.nih.nci.caintegrator.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator.domain.genomic.SampleSet;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 */
public class GenomicDataQueryResult extends AbstractCaIntegrator2Object {

    private static final long serialVersionUID = 1L;
    
    private List<GenomicDataResultRow> rowCollection = new ArrayList<GenomicDataResultRow>();
    private Query query;
    private List<GenomicDataResultColumn> columnCollection = new ArrayList<GenomicDataResultColumn>();
    private boolean hasCriterionSpecifiedValues = false;
    private boolean hasHighVarianceValues = false;
    
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
     * Used to return the filtered rows if "hasCriterionSpecifiedReporterValues" is true, otherwise
     * returns all rows.
     * @return filtered rows based on having any criterion matches.
     */
    public List<GenomicDataResultRow> getFilteredRowCollection() {
        if (!hasCriterionSpecifiedValues) {
            return rowCollection;
        }
        List<GenomicDataResultRow> filteredRows = new ArrayList<GenomicDataResultRow>();
        for (GenomicDataResultRow row : rowCollection) {
            if (row.isHasMatchingValues()) {
                filteredRows.add(row);
            }
        }
        return filteredRows;
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

    /**
     * Adds a new column to this result.
     * 
     * @return the new column.
     */
    public GenomicDataResultColumn addColumn() {
        GenomicDataResultColumn column = new GenomicDataResultColumn();
        column.setResult(this);
        column.setColumnIndex(getColumnCollection().size());
        getColumnCollection().add(column);
        return column;
    }
    
    /**
     * Remove columns and values in rows based on the excludedSampleSet.
     * @param excludedSampleSet the set of samples to be removed.
     */
    public void excludeSampleSet(SampleSet excludedSampleSet) {
        if (excludedSampleSet != null && !excludedSampleSet.getSamples().isEmpty()) {
            removeRowValue(excludedSampleSet);
            removeColumn(excludedSampleSet);
        }
    }

    /**
     * @param excludedSampleSet
     */
    private void removeColumn(SampleSet excludedSampleSet) {
        List<GenomicDataResultColumn> removedColumns = new ArrayList<GenomicDataResultColumn>();
        for (GenomicDataResultColumn column : columnCollection) {
            if (excludedSampleSet.contains(column.getSampleAcquisition().getSample())) {
                removedColumns.add(column);
            }
        }
        columnCollection.removeAll(removedColumns);
    }

    /**
     * @param excludedSampleSet
     */
    private void removeRowValue(SampleSet excludedSampleSet) {
        for (GenomicDataResultRow row : rowCollection) {
            row.excludeSampleSet(excludedSampleSet);
        }
    }

    /**
     * @return the hasCriterionSpecifiedReporterValues
     */
    public boolean isHasCriterionSpecifiedValues() {
        return hasCriterionSpecifiedValues;
    }

    /**
     * @param hasCriterionSpecifiedValues the hasCriterionSpecifiedValues to set
     */
    public void setHasCriterionSpecifiedValues(boolean hasCriterionSpecifiedValues) {
        this.hasCriterionSpecifiedValues = hasCriterionSpecifiedValues;
    }

    /**
     * @return the hasHighVarianceValues
     */
    public boolean isHasHighVarianceValues() {
        return hasHighVarianceValues;
    }

    /**
     * @param hasHighVarianceValues the hasHighVarianceValues to set
     */
    public void setHasHighVarianceValues(boolean hasHighVarianceValues) {
        this.hasHighVarianceValues = hasHighVarianceValues;
    }
}
