/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.external.cadsr;

import gov.nih.nci.cadsr.freestylesearch.util.Search;
import gov.nih.nci.cadsr.freestylesearch.util.SearchAC;
import gov.nih.nci.cadsr.freestylesearch.util.SearchResults;

import java.util.Vector;

/**
 *
 */
public class SearchStub extends Search {

    @Override
    public Vector<SearchResults> findReturningSearchResults(final String keywords) {
        String testLiteral = "test";
        SearchResults searchResults = new SearchResults(SearchAC.valueOf(1),
        testLiteral,
        testLiteral,
        2,
        testLiteral,
        testLiteral,
        testLiteral,
        testLiteral,
        testLiteral,
        1,
        testLiteral,
        3,
        testLiteral) {
            @Override
            public String getPreferredDefinition() {
                return keywords;
            }
        };

        Vector<SearchResults> searchResultsVector = new Vector<SearchResults>();
        searchResultsVector.add(searchResults);
        return searchResultsVector;
    }

}
