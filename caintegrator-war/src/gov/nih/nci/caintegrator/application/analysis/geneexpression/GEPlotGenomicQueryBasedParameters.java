/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.application.analysis.geneexpression;

import gov.nih.nci.caintegrator.domain.application.Query;
import gov.nih.nci.caintegrator.domain.genomic.ReporterTypeEnum;

/**
 * Parameters used for creating an Genomic Query based Gene Expression plot. 
 */
public class GEPlotGenomicQueryBasedParameters extends AbstractGEPlotParameters {

    private Query query;
    private ReporterTypeEnum reporterType;
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validate() {
        getErrorMessages().clear();
        boolean isValid = true;
        if (query == null) {
            getErrorMessages().add("Must select a query");
            isValid = false;
        }
        return isValid;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        query = null;
        reporterType = null;
    }

    /**
     * @return the reporterType
     */
    public ReporterTypeEnum getReporterType() {
        return reporterType;
    }

    /**
     * @param reporterType the reporterType to set
     */
    public void setReporterType(ReporterTypeEnum reporterType) {
        this.reporterType = reporterType;
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

}
