/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.analysis;

import gov.nih.nci.caintegrator2.application.study.Visibility;
import gov.nih.nci.caintegrator2.domain.application.Query;

/**
 * 
 */
public class DisplayableQuery implements Comparable<DisplayableQuery> {
    private static final String QUERY_PREFIX = "[Q]-";
    private static final String SUBJECT_LIST_PREFIX = "[SL]-";
    private static final String GLOBAL_SUBJECT_LIST_PREFIX = "[SL-G]-";
    
    private Query query;
    
    /**
     * Public constructor.
     * @param query to construct object with.
     */
    public DisplayableQuery(Query query) {
        this.query = query;
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
     * Display name for this object.
     * @return display name.
     */
    public String getDisplayName() {
        return getDisplayableQueryName(query); 
    }

    /**
     * {@inheritDoc}
     */
    public int compareTo(DisplayableQuery o) {
        return this.getDisplayName().compareTo(o.getDisplayName());
    }

    /**
     * Retrieves the displayable name based on the query's name.
     * @param inputQuery to get displayable name for.
     * @return displayable name.
     */
    public static String getDisplayableQueryName(Query inputQuery) {
        if (inputQuery.isSubjectListQuery()) {
            return (Visibility.GLOBAL.equals(inputQuery.getSubjectListVisibility()))
                ? GLOBAL_SUBJECT_LIST_PREFIX + inputQuery.getName()
                : SUBJECT_LIST_PREFIX + inputQuery.getName();
        }
        return QUERY_PREFIX + inputQuery.getName();
    }
}
