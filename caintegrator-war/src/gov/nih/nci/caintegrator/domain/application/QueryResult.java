/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.domain.application;

import gov.nih.nci.caintegrator.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator.domain.genomic.Sample;
import gov.nih.nci.caintegrator.domain.genomic.SampleAcquisition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 *
 */
public class QueryResult extends AbstractCaIntegrator2Object {

    private static final long serialVersionUID = 1L;

    private Query query;
    private Collection<ResultRow> rowCollection = new ArrayList<ResultRow>();

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

    /**
     * Returns all samples in this query result.
     *
     * @return all samples.
     */
    public Set<Sample> getAllSamples() {
        Set<Sample> samples = new HashSet<Sample>();
        for (ResultRow row : getRowCollection()) {
            if (row.getSampleAcquisition() != null) {
                samples.add(row.getSampleAcquisition().getSample());
            } else if (row.getSubjectAssignment() != null) {
                for (SampleAcquisition sampleAcquisition
                        : row.getSubjectAssignment().getSampleAcquisitionCollection()) {
                    samples.add(sampleAcquisition.getSample());
                }
            }
        }
        return samples;
    }

}
