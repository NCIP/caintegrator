/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.application.query.domain;

import static org.junit.Assert.assertEquals;
import gov.nih.nci.caintegrator.application.study.AbstractTestDataGenerator;
import gov.nih.nci.caintegrator.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator.domain.application.Query;
import gov.nih.nci.caintegrator.domain.application.QueryResult;
import gov.nih.nci.caintegrator.domain.application.ResultRow;

import java.util.HashSet;
import java.util.Set;


public final class QueryResultGenerator extends AbstractTestDataGenerator<QueryResult> {

    public static final QueryResultGenerator INSTANCE = new QueryResultGenerator();
    
    private QueryResultGenerator() {
        super();
    }

    @Override
    public void compareFields(QueryResult original, QueryResult retrieved) {
        assertEquals(original.getId(), retrieved.getId());
        QueryGenerator.INSTANCE.compareFields(original.getQuery(), retrieved.getQuery());
        assertEquals(original.getRowCollection().size(), retrieved.getRowCollection().size());
        assertEquals(original.getRowCollection().size(), 3);
    }


    @Override
    public QueryResult createPersistentObject() {
        return new QueryResult();
    }


    @Override
    public void setValues(QueryResult queryResult, Set<AbstractCaIntegrator2Object> nonCascadedObjects) {
        Query query = new Query();
        QueryGenerator.INSTANCE.setValues(query, nonCascadedObjects);
        queryResult.setQuery(query);
        queryResult.setRowCollection(new HashSet<ResultRow>());
        for (int i = 0; i < 3; i++) {
            queryResult.getRowCollection().add(ResultRowGenerator.INSTANCE.createPersistentObject());
        }

    }

}

