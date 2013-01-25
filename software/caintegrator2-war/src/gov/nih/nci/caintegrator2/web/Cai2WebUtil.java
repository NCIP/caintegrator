/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web;

import gov.nih.nci.caintegrator2.application.query.QueryManagementService;
import gov.nih.nci.caintegrator2.common.QueryUtil;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.web.action.analysis.DisplayableQuery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Utility for web objects.
 */
public final class Cai2WebUtil {
    
    private Cai2WebUtil() { }

    /**
     * Creates a list of DisplayableQuery objects composed of all queries and subject
     * lists for the given studySubscription.
     * @param studySubscription for the study.
     * @param queryManagementService for query management.
     * @param includeGeneExpression determines whether or not need to include gene expression type queries as well.
     * @return list of displayable queries.
     */
    public static List<DisplayableQuery> retrieveDisplayableQueries(StudySubscription studySubscription,
            QueryManagementService queryManagementService, boolean includeGeneExpression) {
        List<DisplayableQuery> displayableQueries = new ArrayList<DisplayableQuery>();
        displayableQueries.addAll(retrieveQueries(studySubscription, includeGeneExpression));
        displayableQueries.addAll(retrieveSubjectLists(studySubscription, queryManagementService));
        Collections.sort(displayableQueries);
        return displayableQueries;
    }

    private static List<DisplayableQuery> retrieveQueries(StudySubscription studySubscription, 
        boolean includeGeneExpression) {
        List<DisplayableQuery> displayableQueries = new ArrayList<DisplayableQuery>();
        for (Query query 
                : studySubscription.getQueryCollection()) {
            if (includeGeneExpression 
                || !QueryUtil.isQueryGeneExpression(query)) {
                displayableQueries.add(new DisplayableQuery(query));
            }
        }
        return displayableQueries;
    }
    
    private static List<DisplayableQuery> retrieveSubjectLists(StudySubscription studySubscription,
            QueryManagementService queryManagementService) {
        List<DisplayableQuery> displayableQueries = new ArrayList<DisplayableQuery>();
        List<Query> subjectListQueries = queryManagementService.createQueriesFromSubjectLists(
                studySubscription);
        for (Query subjectListQuery : subjectListQueries) {
            displayableQueries.add(new DisplayableQuery(subjectListQuery));
        }
        return displayableQueries;
    }
    
}
