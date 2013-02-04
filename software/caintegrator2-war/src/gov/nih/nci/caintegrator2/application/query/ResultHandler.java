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
public interface ResultHandler {

    /**
     * Creates QueryResult objects out of a list of ResultRows.
     * @param query the query to create the result for.
     * @param resultRows rows to turn into QueryResult's.
     * @return final result from the rows.
     */
    QueryResult createResults(Query query, Set<ResultRow> resultRows);

}
