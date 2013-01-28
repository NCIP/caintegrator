/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.application.query;

import gov.nih.nci.caintegrator.application.arraydata.ArrayDataService;
import gov.nih.nci.caintegrator.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator.domain.application.Query;
import gov.nih.nci.caintegrator.domain.application.QueryResult;
import gov.nih.nci.caintegrator.domain.application.ResultColumn;
import gov.nih.nci.caintegrator.domain.application.ResultRow;

import java.util.HashSet;
import java.util.Set;


/**
 * Translates a Query to it's various compound criterion, retrieves the results after
 * running the criterion against the database, and fills in the columns.
 */
class QueryTranslator {

    private final ResultHandler resultHandler;
    private final Query query;
    private final CaIntegrator2Dao dao;
    private final ArrayDataService arrayDataService;
    
    /**
     * Only constructor for a QueryTranslator takes a query and a dao.
     * @param query - Query to translate.
     * @param dao - Dao to read from database.
     * @param resultHandler - ResultHandler to handle the results.
     */
    QueryTranslator(Query query, CaIntegrator2Dao dao, ArrayDataService arrayDataService, 
            ResultHandler resultHandler) {
        this.query = query;
        this.dao = dao;
        this.arrayDataService = arrayDataService;
        this.resultHandler = resultHandler;
    }
    
    /**
     * Executes a query and returns the QueryResult.
     * @return result of the query execution.
     * @throws InvalidCriterionException if criterion is invalid.
     */
    QueryResult execute() throws InvalidCriterionException {
        if (query.getCompoundCriterion() != null) {
            CompoundCriterionHandler compoundCriterionHandler = 
                CompoundCriterionHandler.create(query.getCompoundCriterion(), query.getResultType());
            Set<EntityTypeEnum> entityTypesInQuery = new HashSet<EntityTypeEnum>();
            for (ResultColumn col : query.getColumnCollection()) {
                entityTypesInQuery.add(col.getEntityType());
            }
            Set<ResultRow> resultsCollection = 
                compoundCriterionHandler.getMatches(dao, arrayDataService, query, entityTypesInQuery);
            
            return resultHandler.createResults(query, resultsCollection, dao);
        } else {
            // Not sure what to return here if there's no compoundCriterion.
            // Maybe it should be EVERY row since there's no criterion?
            return new QueryResult();
        }
    }
}
