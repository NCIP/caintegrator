/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.query;

import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.QueryResult;
import gov.nih.nci.caintegrator2.domain.application.ResultRow;

import java.util.Set;

/**
 * 
 */
public class ResultHandlerStub implements ResultHandler {

    public boolean createResultsCalled = false;
    
    public void clear() {
        createResultsCalled = false;
    }
    /**
     * {@inheritDoc}
     */
    public QueryResult createResults(Query query, Set<ResultRow> resultRows) {
        createResultsCalled = true;
        return new QueryResult();
    }

}
