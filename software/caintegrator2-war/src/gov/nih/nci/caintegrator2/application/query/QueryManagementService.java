/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */

package gov.nih.nci.caintegrator2.application.query;

import gov.nih.nci.caintegrator2.application.CaIntegrator2EntityRefresher;
import gov.nih.nci.caintegrator2.application.analysis.geneexpression.GenesNotFoundInStudyException;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataQueryResult;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.QueryResult;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.application.SubjectList;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.external.ncia.NCIABasket;
import gov.nih.nci.caintegrator2.external.ncia.NCIADicomJob;
import gov.nih.nci.caintegrator2.web.action.query.DisplayableResultRow;

import java.io.File;
import java.util.List;
import java.util.Set;


/**
 * Interface to the service which manages query data for a user's workspace.
 */
public interface QueryManagementService extends CaIntegrator2EntityRefresher {

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
     * Retrieves the geneExpression platforms that exist in a study.
     * @param study to find platforms for.
     * @return gene expression platform names.
     */
    Set<String> retrieveGeneExpressionPlatformsForStudy(Study study);
    
    /**
     * Retrieves the copy number platforms that exist in a study.
     * @param study to find platforms for.
     * @return copy number platform names.
     */
    Set<String> retrieveCopyNumberPlatformsForStudy(Study study);
    
    /**
     * Creates a list of queries given a group of subject lists.
     * @param subscription to study subscription.
     * @return list of queries.
     */
    List<Query> createQueriesFromSubjectLists(StudySubscription subscription);
    
    /**
     * Creates a query from a subject list.
     * @param subscription to study subscription.
     * @param subjectList to create query from.
     * @return query created from SubjectList.
     */
    Query createQueryFromSubjectList(StudySubscription subscription, SubjectList subjectList);
    
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
    
    /**
     * Validates the gene symbols and returns all symbols that exist in the study.
     * @param studySubscription to check gene symbols against.
     * @param geneSymbols to validate existance in the study.
     * @return all valid gene symbols.
     * @throws GenesNotFoundInStudyException if no genes are found.
     */
    List<String> validateGeneSymbols(StudySubscription studySubscription, List<String> geneSymbols)
        throws GenesNotFoundInStudyException;
    
    /**
     * Retrieves all subject identifiers not found in the criteria of the given query.
     * @param query to scan criteria for subject identifiers that aren't found in the study.
     * @return set of all identifiers not found in study but found in criteria.
     * @throws InvalidCriterionException if there is a criterion that specifies subjects and
     * none of the subjects specified exist in the study.
     */
    Set<String> getAllSubjectsNotFoundInCriteria(Query query) throws InvalidCriterionException;

}
