/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.study;

import gov.nih.nci.caintegrator.common.Cai2Util;
import gov.nih.nci.caintegrator.domain.annotation.AnnotationDefinition;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * Comparator to get the higher match score between two different AnnotationFieldDescriptors.
 * It sorts the list from highest to lowest match scores.
 */
public class MatchScoreComparator implements Comparator<AnnotationDefinition> {

    private final Collection<String> keywords;
    private static final int PERCENT_TO_NUMBER = 100;

    /**
     * Constructor based on Keywords.
     * @param keywords - words to compare with.
     */
    public MatchScoreComparator(Collection<String> keywords) {
        this.keywords = keywords;
    }

    /**
     * {@inheritDoc}
     */
    public int compare(AnnotationDefinition ad1, AnnotationDefinition ad2) {
        return getMatchScore(ad2.getKeywords()) - getMatchScore(ad1.getKeywords());
    }

    private int getMatchScore(String annotationDefinitionKeywords) {
        int numMatched = 0;
        for (String word : keywords) {
            if (Cai2Util.containsIgnoreCase(convertStringToList(annotationDefinitionKeywords), word)) {
                numMatched++;
            }
        }
        return Math.round(((float) numMatched / (float) keywords.size()) * PERCENT_TO_NUMBER);
    }

    /**
     * Returns the keywords as a <code>List</code>.
     * @param keywordsString a string with keywords seperated by spaces.
     * @return the keywords.
     */
    public List<String> convertStringToList(String keywordsString) {
        if (keywordsString != null) {
            return Arrays.asList(StringUtils.split(keywordsString));
        } else {
            return Collections.emptyList();
        }
    }



}
