/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.study;

import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator2.domain.application.Query;

/**
 * An AuthorizedQuery is a <code>Query</code> that has been
 * authorized for inclusion in an <code>AuthorizedStudyElementsGroup</code>.
 */
public class AuthorizedQuery extends AbstractCaIntegrator2Object {

    private static final long serialVersionUID = 1L;
    private Query query;
    private AuthorizedStudyElementsGroup authorizedStudyElementsGroup;

    /**
     * @param query the query to set
     */
    public void setQuery(Query query) {
        this.query = query;
    }
    /**
     * @return the Query
     */
    public Query getQuery() {
        return query;
    }    
    /**
     * @param authorizedStudyElementsGroup the authorizedStudyElementsGroup to set
     */
    public void setAuthorizedStudyElementsGroup(AuthorizedStudyElementsGroup authorizedStudyElementsGroup) {
        this.authorizedStudyElementsGroup = authorizedStudyElementsGroup;
    }
    /**
     * @return the authorizedStudyElementsGroup
     */
    public AuthorizedStudyElementsGroup getAuthorizedStudyElementsGroup() {
        return authorizedStudyElementsGroup;
    }

}
