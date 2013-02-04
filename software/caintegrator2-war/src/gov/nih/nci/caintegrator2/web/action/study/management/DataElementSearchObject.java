/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.study.management;

import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.annotation.CommonDataElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper object used for the searching of AnnotationDefinitions and CommonDataElements.
 */
public class DataElementSearchObject {
    private final List<AnnotationDefinition> searchDefinitions = new ArrayList<AnnotationDefinition>();
    private final List<CommonDataElement> searchCommonDataElements = new ArrayList<CommonDataElement>();
    private String keywordsForSearch;
    
    /**
     * Clears this back to original empty state.
     */
    public void clear() {
        searchDefinitions.clear();
        searchCommonDataElements.clear();
        keywordsForSearch = null;
    }
    /**
     * @return the keywordsForSearch
     */
    public String getKeywordsForSearch() {
        return keywordsForSearch;
    }
    /**
     * @param keywordsForSearch the keywordsForSearch to set
     */
    public void setKeywordsForSearch(String keywordsForSearch) {
        this.keywordsForSearch = keywordsForSearch;
    }
    /**
     * @return the searchDefinitions
     */
    public List<AnnotationDefinition> getSearchDefinitions() {
        return searchDefinitions;
    }
    /**
     * @return the searchCommonDataElements
     */
    public List<CommonDataElement> getSearchCommonDataElements() {
        return searchCommonDataElements;
    }
    
    
}
