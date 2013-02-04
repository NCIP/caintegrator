/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */

package gov.nih.nci.caintegrator2.application.query;

import gov.nih.nci.caintegrator2.domain.application.GenomicDataQueryResult;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.QueryResult;
import gov.nih.nci.caintegrator2.external.ncia.NCIABasket;
import gov.nih.nci.caintegrator2.external.ncia.NCIADicomJob;
import gov.nih.nci.caintegrator2.web.action.query.DisplayableResultRow;

import java.io.File;
import java.util.List;


/**
 * Interface to the service which manages query data for a user's workspace.
 */
public interface QueryManagementService {

    /**
     * Executes a clinical query and returns back the result.
     * @param query item to execute.
     * @return - Result of the query being executed
     * @throws InvalidCriterionException if criterion is invalid.
     */
    QueryResult execute(Query query) throws InvalidCriterionException;
    
    /**
     * Executes a query that returns a genomic data set.
     * 
     * @param query the query to execute.
     * @return the resulting data.
     * @throws InvalidCriterionException if criterion is invalid. 
     */
    GenomicDataQueryResult executeGenomicDataQuery(Query query) throws InvalidCriterionException;
    
    /**
     * Creates a Dicom Job object based on the checked rows.
     * @param checkedRows - rows the user selected.
     * @return Dicom Job.
     */
    NCIADicomJob createDicomJob(List<DisplayableResultRow> checkedRows);
    
    /**
     * Creates an NCIA Basket object based on the checked rows.
     * @param checkedRows - rows the user selected.
     * @return NCIA Basket.
     */
    NCIABasket createNciaBasket(List<DisplayableResultRow> checkedRows);

    /**
     * Persists a query.
     * 
     * @param query item to update
     */
    void save(Query query);
    
    /**
     * Deletes a query.
     * 
     * @param query item to update
     */
    void delete(Query query);
    
    /**
     * Creates a CSV file from the genomic results.
     * @param result to create csv file for.
     * @return csv file.
     */
    File createCsvFileFromGenomicResults(GenomicDataQueryResult result);

}
